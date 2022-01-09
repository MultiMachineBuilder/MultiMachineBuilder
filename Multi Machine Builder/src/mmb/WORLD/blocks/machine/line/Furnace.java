/**
 * 
 */
package mmb.WORLD.blocks.machine.line;

import java.awt.image.BufferedImage;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.BEANS.BlockActivateListener;
import mmb.DATA.contents.texture.Textures;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.blocks.FuelBurner;
import mmb.WORLD.blocks.machine.SkeletalBlockLinear;
import mmb.WORLD.contentgen.Materials;
import mmb.WORLD.crafting.Craftings;
import mmb.WORLD.crafting.ElectroItemProcessHelper;
import mmb.WORLD.crafting.recipes.SimpleProcessingRecipeGroup.SimpleProcessingRecipe;
import mmb.WORLD.electric.Battery;
import mmb.WORLD.electric.VoltageTier;
import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.inventory.ItemRecord;
import mmb.WORLD.items.ContentsItems;
import mmb.WORLD.rotate.RotatedImageGroup;
import mmb.WORLD.worlds.world.World;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class Furnace extends SkeletalBlockLinear implements BlockActivateListener {
	
	@Nonnull public static final BufferedImage TEXTURE_INERT = Textures.get("machine/smelter inert.png");
	@Nonnull public static final BufferedImage TEXTURE_ACTIVE = Textures.get("machine/smelter active.png");
	@Nonnull public static final RotatedImageGroup IMAGE_INERT = RotatedImageGroup.create(TEXTURE_INERT);
	@Nonnull public static final RotatedImageGroup IMAGE_ACTIVE = RotatedImageGroup.create(TEXTURE_ACTIVE);
	private static final Debugger debug = new Debugger("FURNACE");

	private FurnaceGUI openWindow;
	@Nonnull private Battery elec = new Battery(20_000, 120_000, this, VoltageTier.V1);
	@Nonnull private ElectroItemProcessHelper processor = new ElectroItemProcessHelper(Craftings.smelting, incoming, output, 500, elec, VoltageTier.V1);
	@Nonnull private final FuelBurner burner = new FuelBurner(1, incoming, elec);
	private static boolean inited = false;
	public static void init() {
		if(inited) return;
		Craftings.smelting.add(ContentsBlocks.logs, Materials.coal.base, 1, VoltageTier.V1, 50_000);
		Craftings.smelting.add(ContentsBlocks.coal_ore, Materials.coal.base, 3, VoltageTier.V1, 50_000);
		inited = true;
	}

	@Override
	protected void save2(ObjectNode node) {
		processor.save(node);
		JsonNode bat = elec.save();
		node.set("energy", bat);
	}
	@Override
	protected void load2(ObjectNode node) {
		processor.load(node);
		JsonNode bat = node.get("energy");
		elec.load(bat);
		//Validation
		elec.capacity = 120_000;
		elec.maxPower = 20_000;
		elec.voltage = VoltageTier.V1;
	}

	@Override
	public BlockType type() {
		return ContentsBlocks.FURNACE;
	}

	@Override
	public RotatedImageGroup getImage() {
		if(processor.underway == null) return IMAGE_INERT;
		return IMAGE_ACTIVE;
	}

	@Override
	public void cycle() {
		//Look for fuels
		burner.cycle();
		processor.cycle();
	}

	@Override
	public void click(int blockX, int blockY, World map, @Nullable WorldWindow window, double partX, double partY) {
		if(window == null) return;
		if(openWindow != null) return;
		openWindow = new FurnaceGUI(this, window);
		window.openAndShowWindow(openWindow, "Furnace");
		processor.refreshable = openWindow;
		openWindow.refreshProgress(0, null);
	}
	
	void closeWindow() {
		openWindow = null;
		processor.refreshable = null;
	}

	/**
	 * @return amount of active fuel
	 */
	public double getFuelLevel() {
		return elec.amt * 100;
	}
}

