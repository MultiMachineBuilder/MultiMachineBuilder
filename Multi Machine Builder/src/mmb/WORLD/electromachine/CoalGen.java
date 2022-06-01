/**
 * 
 */
package mmb.WORLD.electromachine;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.LookupOp;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.BEANS.BlockActivateListener;
import mmb.DATA.contents.texture.Textures;
import mmb.GRAPHICS.awt.ColorMapper;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.block.SkeletalBlockEntityRotary;
import mmb.WORLD.crafting.Craftings;
import mmb.WORLD.electric.Battery;
import mmb.WORLD.electric.Electricity;
import mmb.WORLD.electric.VoltageTier;
import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.storage.SimpleInventory;
import mmb.WORLD.rotate.RotatedImageGroup;
import mmb.WORLD.rotate.Side;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.world.World;

/**
 * @author oskar
 *
 */
public class CoalGen extends SkeletalBlockEntityRotary implements BlockActivateListener{
	
	@Override
	public BlockType type() {
		return type;
	}

	@Nonnull public static final BufferedImage img;
	@Nonnull private static final RotatedImageGroup tex0;
	@Nonnull public static final BufferedImage img1;
	@Nonnull private static final RotatedImageGroup tex1;
	@Nonnull public static final BufferedImage img2;
	@Nonnull private static final RotatedImageGroup tex2;
	static {
		img = Textures.get("machine/coalgen.png");
		tex0 = RotatedImageGroup.create(img);
		ColorMapper mapper = ColorMapper.ofType(img.getType(), Color.RED, VoltageTier.V2.c);
		LookupOp op = new LookupOp(mapper, null);
		img1 = op.filter(img, null);
		tex1 = RotatedImageGroup.create(img1);
		mapper.setTo(VoltageTier.V3.c);
		img2 = op.filter(img, null);
		tex2 = RotatedImageGroup.create(img2);
	}
	
	@Override
	public RotatedImageGroup getImage() {
		switch(volt) {
		case V1:
			return tex0;
		case V2:
			return tex1;
		case V3:
			return tex2;
		default:
			throw new IllegalStateException("Voltage "+volt.name+" is not supported by Furnace Generator");
		}
	}
	
	@Nonnull public final VoltageTier volt;
	@Nonnull private final BlockType type;
	@Nonnull final Battery fuel = new Battery(0, 0, this, VoltageTier.V1);
	@Nonnull final Battery buffer = new Battery(0, 0, this, VoltageTier.V1);
	@Nonnull public final SimpleInventory inv = new SimpleInventory();
	@Nonnull private final FuelBurner burner;
	public CoalGen(VoltageTier volt, BlockType type) {
		this.volt = volt;
		this.type = type;
		this.burner = new FuelBurner(1.5+volt.ordinal(), inv, fuel, Craftings.furnaceFuels);
		resetBuffer();
	}

	/**
	 * @param volt
	 */
	private void resetBuffer() {
		buffer.voltage = volt;
		buffer.maxPower = 20;
		buffer.capacity = 10_000;
		buffer.pressureWt = volt.volts;
		fuel.voltage = volt;
		fuel.maxPower = 20;
		fuel.capacity = 500_000;
	}

	@Override
	public Inventory getInventory(Side s) {
		return inv.lockExtractions();
	}

	@Override
	public Electricity getElectricalConnection(Side s) {
		return Electricity.extractOnly(buffer);
	}
	
	@Override
	public void onTick(MapProxy map) {
		burner.cycle();
		fuel.extractTo(buffer);
		Electricity.equatePPs(this, map, buffer, 0.999);
		Electricity to = getAtSide(getRotation().U()).getElectricalConnection(getRotation().D());
		if(to != null) buffer.extractTo(to);
		if(tab != null) tab.refresh();
	}

	CoalGenTab tab;
	@Override
	public void click(int blockX, int blockY, World map, WorldWindow window, double partX, double partY) {
		if(window == null) return;
		if(tab != null) return;
		tab = new CoalGenTab(window, this);
		window.openAndShowWindow(tab, type.title());
	}

	
	@Override
	protected void save1(ObjectNode node) {
		node.set("in", inv.save());
		node.set("energy", fuel.save());
		node.set("out", buffer.save());
	}

	@Override
	protected void load1(ObjectNode node) {
		inv.load(node.get("in"));
		fuel.load(node.get("energy"));
		buffer.load(node.get("out"));
		resetBuffer();
	}

	@Override
	public BlockEntry blockCopy() {
		CoalGen copy = new CoalGen(volt, type);
		copy.buffer.set(buffer);
		copy.fuel.set(fuel);
		copy.inv.set(inv);
		return copy;
	}

}
