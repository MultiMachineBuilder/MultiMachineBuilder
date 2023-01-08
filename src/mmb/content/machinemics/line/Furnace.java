/**
 * 
 */
package mmb.content.machinemics.line;

import java.awt.image.BufferedImage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.NN;
import mmb.Nil;
import mmb.cgui.BlockActivateListener;
import mmb.content.ContentsBlocks;
import mmb.content.CraftingGroups;
import mmb.content.electric.Battery;
import mmb.content.electric.VoltageTier;
import mmb.content.electric.helper.SimpleProcessHelper;
import mmb.content.electric.machines.FuelBurner;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.debug.Debugger;
import mmb.engine.rotate.RotatedImageGroup;
import mmb.engine.texture.Textures;
import mmb.engine.worlds.world.World;
import mmb.menu.world.window.WorldWindow;

/**
 * @author oskar
 *
 */
public class Furnace extends SkeletalBlockLinear implements BlockActivateListener {
	
	@NN public static final BufferedImage TEXTURE_INERT = Textures.get("machine/smelter inert.png");
	@NN public static final BufferedImage TEXTURE_ACTIVE = Textures.get("machine/smelter active.png");
	@NN public static final RotatedImageGroup IMAGE_INERT = RotatedImageGroup.create(TEXTURE_INERT);
	@NN public static final RotatedImageGroup IMAGE_ACTIVE = RotatedImageGroup.create(TEXTURE_ACTIVE);
	private static final Debugger debug = new Debugger("FURNACE");

	private FurnaceGUI openWindow;
	@NN private Battery elec = new Battery(20_000, 120_000, this, VoltageTier.V1);
	@NN private SimpleProcessHelper processor = new SimpleProcessHelper(CraftingGroups.smelting, incoming, output, 500, elec, null, VoltageTier.V1); //borked here
	@NN private final FuelBurner burner = new FuelBurner(1, incoming, elec, CraftingGroups.furnaceFuels);

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
	public void click(int blockX, int blockY, World map, @Nil WorldWindow window, double partX, double partY) {
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

