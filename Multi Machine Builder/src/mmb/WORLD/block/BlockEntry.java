/**
 * 
 */
package mmb.WORLD.block;

import java.awt.Point;
import javax.annotation.Nonnull;

import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import mmb.BEANS.Saver;
import mmb.COLLECTIONS.HashSelfSet;
import mmb.COLLECTIONS.SelfSet;
import mmb.DATA.json.JsonTool;
import mmb.WORLD.BlockDrawer;
import mmb.WORLD.block.properties.BlockProperty;
import mmb.WORLD.worlds.map.BlockMap;
import mmb.debug.Debugger;

/**
 * @author oskar
 * To be removed in 0.5.
 * @discouraged For machines, use {@link mmb.WORLD.machine.Machine}. For blocks, use {@link BlockType}
 */
public class BlockEntry implements Saver<JsonNode> {
	private static Debugger debug = new Debugger("BLOCKS");
	
	public SelfSet<String, BlockProperty> properties = new HashSelfSet<>();
	
	//Positioning
	public final int x, y;
	public Point getPosition() {
		return new Point(x, y);
	}
	
	//Rendering
	public BlockDrawer getTexture() {
		return type.drawer;
	}
	
	public BlockEntry(int x, int y, BlockMap owner2) {
		super();
		this.x = x;
		this.y = y;
		this.owner = owner2;
	}

	public BlockMap owner;
	
	//Block entity

	//Type
	public BlockType type;

	private static void loaderProperties(int x, int y, BlockMap requester, BlockType typ) {
		
	}
	/**
	 * 
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param requester target map
	 * @param typ block type
	 * @return newly created block entry, ready to be set into map.
	 */
	public static BlockEntry defaultProperties(int x, int y, BlockMap requester, BlockType typ) {
		if(typ == null) return null;
		BlockEntry ben = new BlockEntry(x, y, requester, typ);
		Set<Class<? extends BlockProperty>> props = typ.properties;
		for(Class<? extends BlockProperty> item: props) {
			if(item == null) continue;
			BlockProperty prop;
			try {
				prop = item.newInstance();
				ben.properties.add(prop);
			} catch (Exception e) {
				debug.pstm(e, "Couldn't create block property");
			}
		}
		return ben;
	}

	/**
	 * @return
	 */
	public BlockEntry[] getNeighbors4() {
		return new BlockEntry[] {
				owner.get(x, y-1),
				owner.get(x, y+1),
				owner.get(x-1, y),
				owner.get(x+1, y)
		};
	}
	public BlockEntry[] getNeighbors8() {
		return new BlockEntry[] {
				owner.get(x, y-1),
				owner.get(x, y+1),
				owner.get(x-1, y),
				owner.get(x+1, y),
				owner.get(x-1, y-1),
				owner.get(x-1, y+1),
				owner.get(x+1, y-1),
				owner.get(x+1, y+1)
		};
	}
	
	//[start] Rotation
	public Rotation rotation;
	public void wrenchCW() {
		rotation = type.rotations.cw(rotation);
	}
	public void wrenchCCW() {
		rotation = type.rotations.ccw(rotation);
	}
	public Rotation orientation() {
		return rotation;
	}
	//[end]	
	/**
	 * @param x @param y coordinates
	 * @param owner2 block owner
	 */
	public BlockEntry(int x, int y, @Nonnull BlockMap owner2, @Nonnull BlockType typ) {
		this(x, y, owner2);
		type = typ;
	}

	@Override
	public JsonNode save() {
		if(properties.isEmpty()) return new TextNode(type.id); //if block has no data
		ObjectNode data = JsonTool.newObjectNode();
		data.set("blocktype", new TextNode(type.id));
		for(BlockProperty property: properties) {
			data.set(property.name(), property.save());
		}
		return data;
	}

	public boolean typeof(BlockType type) {
		return type == this.type;
	}

	public BlockProperty getProperty(@Nonnull String string) {
		return properties.get(string);
	}
	
	
}
