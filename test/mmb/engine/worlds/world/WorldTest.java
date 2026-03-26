package mmb.engine.worlds.world;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.engine.Vector2iconst;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.block.Blocks;
import mmb.engine.item.ItemEntry;
import mmb.engine.rotate.Side;
import mmb.engine.visuals.Visual;
import monniasza.collects.grid.Grid;

class WorldTest {

	private World world;

	@BeforeEach
	void setUp() {
		world = new World(3, 4, 10, 20);
	}

	// -------------------------------------------------
	// Construction / identity / metadata
	// -------------------------------------------------

	@Test
	void constructorSetsBoundsAndSize() {
		assertEquals(10, world.startX);
		assertEquals(20, world.startY);
		assertEquals(3, world.sizeX);
		assertEquals(4, world.sizeY);
		assertEquals(13, world.endX);
		assertEquals(24, world.endY);

		assertEquals(10, world.startX());
		assertEquals(20, world.startY());
		assertEquals(3, world.sizeX());
		assertEquals(4, world.sizeY());
		assertEquals(13, world.endX());
		assertEquals(24, world.endY());
	}

	@Test
	void constructorCreatesLodBufferWithCorrectSize() {
		BufferedImage lods = world.LODs;
		assertNotNull(lods);
		assertEquals(3, lods.getWidth());
		assertEquals(4, lods.getHeight());
	}

	@Test
	void newWorldIsValid() {
		assertTrue(world.isValid());
	}

	@Test
	void newWorldHasPlayer() {
		assertNotNull(world.player);
	}

	@Test
	void newWorldHasNoBlockEntities() {
		assertTrue(world.blockents.isEmpty());
	}

	@Test
	void newWorldHasNoDrops() {
		assertTrue(world.drops.isEmpty());
	}

	@Test
	void setNameUpdatesNameAndId() {
		world.setName("test-world");

		assertEquals("test-world", world.getName());
		assertEquals("test-world", world.id());
	}

	@Test
	void setNameNullClearsNameAndId() {
		world.setName("abc");
		world.setName(null);

		assertNull(world.getName());
		assertNull(world.id());
	}

	@Test
	void worldStartsWithoutUniverse() {
		assertNull(world.getUniverse());
	}

	// -------------------------------------------------
	// Bounds / geometry helpers
	// -------------------------------------------------

	@Test
	void inBoundsWorksOnCornersAndOutside() {
		assertTrue(world.inBounds(10, 20));
		assertTrue(world.inBounds(12, 23));

		assertFalse(world.inBounds(9, 20));
		assertFalse(world.inBounds(10, 19));
		assertFalse(world.inBounds(13, 20));
		assertFalse(world.inBounds(10, 24));
	}

	@Test
	void inBoundsPointWorks() {
		assertTrue(world.inBounds(new Point(10, 20)));
		assertTrue(world.inBounds(new Point(12, 23)));
		assertFalse(world.inBounds(new Point(13, 24)));
	}

	@Test
	void startReturnsCorrectPoint() {
		assertEquals(new Point(10, 20), world.start());
	}

	@Test
	void rectsizeReturnsCorrectDimension() {
		assertEquals(new Dimension(3, 4), world.rectsize());
	}

	@Test
	void boundsReturnsCorrectRectangle() {
		assertEquals(new Rectangle(10, 20, 3, 4), world.bounds());
	}

	// -------------------------------------------------
	// Block access / placement
	// -------------------------------------------------

	@Test
	void newWorldIsFilledWithGrass() {
		for (int y = world.startY; y < world.endY; y++) {
			for (int x = world.startX; x < world.endX; x++) {
				assertSame(Blocks.grass, world.get(x, y),
						"New world should be initialized with grass everywhere");
			}
		}
	}

	@Test
	void getOutOfBoundsReturnsVoid() {
		assertSame(Blocks.blockVoid, world.get(9, 20));
		assertSame(Blocks.blockVoid, world.get(10, 19));
		assertSame(Blocks.blockVoid, world.get(13, 20));
		assertSame(Blocks.blockVoid, world.get(10, 24));
	}

	@Test
	void getPointReturnsSameAsCoordinateGet() {
		assertSame(world.get(10, 20), world.get(new Point(10, 20)));
		assertSame(world.get(12, 23), world.get(new Point(12, 23)));
	}

	@Test
	void setPreservesBlockIdentity() {
		BlockEntry block = Blocks.blockVoid;

		BlockEntry returned = world.set(block, 11, 21);
		BlockEntry stored = world.get(11, 21);

		assertSame(block, returned, "set() should return the exact block instance that was placed");
		assertSame(block, stored, "get() should return the exact same block instance that was passed to set()");
	}

	@Test
	void replacingBlockPreservesNewBlockIdentity() {
		BlockEntry first = Blocks.grass;
		BlockEntry second = Blocks.blockVoid;

		world.set(first, 11, 21);
		BlockEntry returned = world.set(second, 11, 21);
		BlockEntry stored = world.get(11, 21);

		assertSame(second, returned, "set() should return the newly placed block instance");
		assertSame(second, stored, "World should store the exact newly placed block instance");
		assertNotSame(first, stored, "Old block instance should no longer be stored at that position");
	}

	@Test
	void settingOnePositionDoesNotAffectOtherPositions() {
		BlockEntry blockA = Blocks.grass;
		BlockEntry blockB = Blocks.blockVoid;

		world.set(blockA, 10, 20);
		world.set(blockB, 12, 23);

		assertSame(blockA, world.get(10, 20));
		assertSame(blockB, world.get(12, 23));
		assertNotSame(world.get(10, 20), world.get(12, 23));
	}

	@Test
	void getAtSideReturnsNeighborBlock() {
		BlockEntry neighbor = Blocks.blockVoid;
		world.set(neighbor, 11, 20);

		assertSame(neighbor, world.getAtSide(Side.R, 10, 20));
	}

	@Test
	void placeReturnsPlacedBlockAndStoresIt() {
		BlockType type = Blocks.grass.itemType();

		BlockEntry placed = world.place(type, 11, 22);

		assertNotNull(placed);
		assertSame(placed, world.get(11, 22));
	}

	@Test
	void placeCreatesDistinctInstancesPerTile() {
		BlockEntry a = world.place(Blocks.grass.itemType(), 10, 20);
		BlockEntry b = world.place(Blocks.grass.itemType(), 11, 21);

		assertNotNull(a);
		assertNotNull(b);
		assertNotSame(a, b, "Each placed block should be a distinct instance");
		assertSame(a, world.get(10, 20));
		assertSame(b, world.get(11, 21));
	}

	// -------------------------------------------------
	// Grid adapter
	// -------------------------------------------------

	@Test
	void toGridHasCorrectDimensions() {
		Grid<BlockEntry> grid = world.toGrid();

		assertEquals(3, grid.width());
		assertEquals(4, grid.height());
	}

	@Test
	void toGridGetReadsWorldData() {
		Grid<BlockEntry> grid = world.toGrid();
		BlockEntry block = Blocks.blockVoid;

		world.set(block, 11, 22);

		assertSame(block, grid.get(1, 2));
	}

	@Test
	void toGridSetWritesWorldData() {
		Grid<BlockEntry> grid = world.toGrid();
		BlockEntry block = Blocks.blockVoid;

		grid.set(2, 3, block);

		assertSame(block, world.get(12, 23));
	}

	// -------------------------------------------------
	// Drops
	// -------------------------------------------------

	@Test
	void getDropsReturnsLiveCollectionForCoordinates() {
		Collection<ItemEntry> drops = world.getDrops(11, 22);

		assertNotNull(drops);
		assertTrue(drops.isEmpty());
	}

	@Test
	void getDropsByVectorReturnsSameUnderlyingCollection() {
		Collection<ItemEntry> byCoords = world.getDrops(11, 22);
		Collection<ItemEntry> byVector = world.getDrops(new Vector2iconst(11, 22));

		assertSame(byCoords, byVector);
	}

	@Test
	void createDropperReturnsWriter() {
		assertNotNull(world.createDropper(11, 22));
	}

	// NOTE:
	// dropItem/dropItems/dropChance need a concrete ItemEntry/Chance fixture from your codebase.
	// Add those once you have a stable test item reference.

	// -------------------------------------------------
	// Visuals
	// -------------------------------------------------

	@Test
	void visualsInitiallyEmpty() {
		assertNotNull(world.visuals());
		assertEquals(0, world.visuals().size());
	}

	// NOTE:
	// addVisual / removeVisual / addVisuals / removeVisuals need a simple Visual test fixture.
	// Add once you have an easy stub or concrete test visual.

	// -------------------------------------------------
	// Proxy / lifecycle-safe basics
	// -------------------------------------------------

	@Test
	void createProxyReturnsNonNullProxy() {
		assertNotNull(world.createProxy());
	}

	@Test
	void worldIsNotRunningInitially() {
		assertFalse(world.isRunning());
	}

	@Test
	void worldIsNotShutdownInitially() {
		assertFalse(world.hasShutDown());
	}

	@Test
	void shutdownBeforeStartDoesNotThrow() {
		assertDoesNotThrow(() -> world.shutdown());
	}

	@Test
	void destroyBeforeStartDoesNotThrow() {
		assertDoesNotThrow(() -> world.destroy());
	}

	// -------------------------------------------------
	// Serialization
	// -------------------------------------------------

	@Test
	void saveProducesBasicWorldMetadata() {
		JsonNode saved = World.save(world);

		assertNotNull(saved);
		assertEquals(3, saved.get("sizeX").asInt());
		assertEquals(4, saved.get("sizeY").asInt());
		assertEquals(10, saved.get("startX").asInt());
		assertEquals(20, saved.get("startY").asInt());
		assertNotNull(saved.get("world"));
		assertNotNull(saved.get("drops"));
		assertNotNull(saved.get("player"));
	}

	@Test
	void loadRejectsNull() {
		assertThrows(NullPointerException.class, () -> World.load(null));
	}

	@Test
	void loadRejectsMissingSizeX() {
		JsonNode json = com.fasterxml.jackson.databind.node.JsonNodeFactory.instance.objectNode()
				.put("sizeY", 1)
				.put("startX", 0)
				.put("startY", 0);

		assertThrows(World.WorldLoadException.class, () -> World.load(json));
	}

	@Test
	void loadRejectsMissingSizeY() {
		JsonNode json = com.fasterxml.jackson.databind.node.JsonNodeFactory.instance.objectNode()
				.put("sizeX", 1)
				.put("startX", 0)
				.put("startY", 0);

		assertThrows(World.WorldLoadException.class, () -> World.load(json));
	}

	@Test
	void loadRejectsMissingStartX() {
		JsonNode json = com.fasterxml.jackson.databind.node.JsonNodeFactory.instance.objectNode()
				.put("sizeX", 1)
				.put("sizeY", 1)
				.put("startY", 0);

		assertThrows(World.WorldLoadException.class, () -> World.load(json));
	}

	@Test
	void loadRejectsMissingStartY() {
		JsonNode json = com.fasterxml.jackson.databind.node.JsonNodeFactory.instance.objectNode()
				.put("sizeX", 1)
				.put("sizeY", 1)
				.put("startX", 0);

		assertThrows(World.WorldLoadException.class, () -> World.load(json));
	}

	@Test
	void loadRejectsMissingWorldArray() {
		JsonNode json = com.fasterxml.jackson.databind.node.JsonNodeFactory.instance.objectNode()
				.put("sizeX", 1)
				.put("sizeY", 1)
				.put("startX", 0)
				.put("startY", 0);

		assertThrows(World.WorldLoadException.class, () -> World.load(json));
	}
}