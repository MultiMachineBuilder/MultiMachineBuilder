/**
 * 
 */
package mmbgame.machinemics.line;

import java.awt.image.BufferedImage;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.cgui.BlockActivateListener;
import mmb.menu.world.window.WorldWindow;
import mmbeng.block.BlockEntry;
import mmbeng.block.BlockType;
import mmbeng.debug.Debugger;
import mmbeng.rotate.RotatedImageGroup;
import mmbeng.texture.Textures;
import mmbeng.worlds.world.World;
import mmbgame.ContentsBlocks;
import mmbgame.CraftingGroups;
import mmbgame.electric.Battery;
import mmbgame.electric.VoltageTier;
import mmbgame.electric.helper.SimpleProcessHelper;
import mmbgame.electric.machines.FuelBurner;

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
	@Nonnull private SimpleProcessHelper processor = new SimpleProcessHelper(CraftingGroups.smelting, incoming, output, 500, elec, null, VoltageTier.V1); //borked here
	@Nonnull private final FuelBurner burner = new FuelBurner(1, incoming, elec, CraftingGroups.furnaceFuels);

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
		return elec.stored * 100;
	}

	@Override
	public BlockEntry blockCopy() {
		Furnace copy = new Furnace();
		copy.elec.set(elec);
		copy.incoming.set(incoming);
		copy.outgoing.set(outgoing);
		copy.processor.set(processor);
		return copy;
	}
}

