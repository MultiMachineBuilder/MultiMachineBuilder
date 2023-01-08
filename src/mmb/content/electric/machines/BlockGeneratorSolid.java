/**
 * 
 */
package mmb.content.electric.machines;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.LookupOp;

import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.NN;
import mmb.Nil;
import mmb.cgui.BlockActivateListener;
import mmb.content.CraftingGroups;
import mmb.content.electric.Battery;
import mmb.content.electric.Electricity;
import mmb.content.electric.GUIGeneratorSolid;
import mmb.content.electric.VoltageTier;
import mmb.engine.block.BlockEntityRotary;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.inv.Inventory;
import mmb.engine.inv.storage.SimpleInventory;
import mmb.engine.java2d.ColorMapper;
import mmb.engine.rotate.RotatedImageGroup;
import mmb.engine.rotate.Side;
import mmb.engine.texture.Textures;
import mmb.engine.worlds.MapProxy;
import mmb.engine.worlds.world.World;
import mmb.menu.world.window.WorldWindow;

/**
 * @author oskar
 *
 */
public class BlockGeneratorSolid extends BlockEntityRotary implements BlockActivateListener{
	//Standard generator definitions
	/** The texture for ULV furnace generator */
	@NN public static final BufferedImage img;
	@NN private static final RotatedImageGroup tex0;
	/** The texture for VLV furnace generator */
	@NN public static final BufferedImage img1;
	@NN private static final RotatedImageGroup tex1;
	/** The texture for LV furnace generator */
	@NN public static final BufferedImage img2;
	@NN private static final RotatedImageGroup tex2;
	
	//Turbogenerator definitions
	/** The texture foor VLV turbogenerator */
	@NN public static final BufferedImage turboimg;
	@NN private static final RotatedImageGroup turbotex0;
	/** The texture for LV turbogenerator */
	@NN public static final BufferedImage turboimg1;
	@NN private static final RotatedImageGroup turbotex1;
	/** The texture for MV turbogenerator*/
	@NN public static final BufferedImage turboimg2;
	@NN private static final RotatedImageGroup turbotex2;
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
		
		BufferedImage tmp = Textures.get("machine/turbogen.png");
		mapper.setTo(VoltageTier.V2.c);
		turboimg = op.filter(tmp, null);
		turbotex0 = RotatedImageGroup.create(img);
		mapper.setTo(VoltageTier.V3.c);
		turboimg1 = op.filter(tmp, null);
		turbotex1 = RotatedImageGroup.create(img1);
		mapper.setTo(VoltageTier.V4.c);
		turboimg2 = op.filter(tmp, null);
		turbotex2 = RotatedImageGroup.create(img2);
	}
		
	//Block definition
	/** Voltage tier */
	@NN public final VoltageTier volt;
	@NN private final BlockType type;
	/** Fuel storage */
	@NN public final Battery fuel;
	/** Output buffer */
	@NN public final Battery buffer;
	/** Burn queue */
	@NN public final SimpleInventory inv = new SimpleInventory();
	@NN private final FuelBurner burner;
	/** 1-furnace generator, 2-turbogenerator */
	public final int mul;
	/**
	 * Creates a new furnace generator/turbogenerator
	 * @param mul 1-furnace generator, 2-turbogenerator
	 * @param volt voltage tier
	 * @param type block type
	 */
	public BlockGeneratorSolid(int mul, VoltageTier volt, BlockType type) {
		this.volt = volt;
		this.type = type;
		this.mul = mul;
		fuel = new Battery(0, 0, this, volt);
		buffer = new Battery(0, 0, this, volt);
		fuel.maxPower = Double.POSITIVE_INFINITY;
		buffer.maxPower = volt.speedMul*10_000/volt.volts;
		this.burner = new FuelBurner(1.5+volt.ordinal(), inv, fuel, CraftingGroups.furnaceFuels);
		resetBuffer();
	}

	//Inventory
	@Override
	public Inventory getInventory(Side s) {
		return inv.lockExtractions();
	}

	//Tick handler
	@Override
	public void onTick(MapProxy map) {
		burner.cycle();
		fuel.extractTo(buffer);
		Electricity.equatePPs(this, map, buffer, 0.999);
		Electricity to = getAtSide(getRotation().U()).getElectricalConnection(getRotation().D());
		if(to != null) buffer.extractTo(to);
		if(tab != null) tab.refresh();
	}
	
	//GUI
	GUIGeneratorSolid tab;
	@Override
	public void click(int blockX, int blockY, World map, @Nil WorldWindow window, double partX, double partY) {
		if(window == null) return;
		if(tab != null) return;
		tab = new GUIGeneratorSolid(window, this);
		window.openAndShowWindow(tab, type.title());
	}
	@SuppressWarnings("javadoc") //internal use only
	public void close(GUIGeneratorSolid tbb) {
		if(tab == tbb) tab = null;
	}

	//Block attributes
	@Override
	public BlockEntry blockCopy() {
		BlockGeneratorSolid copy = new BlockGeneratorSolid(mul, volt, type);
		copy.buffer.set(buffer);
		copy.fuel.set(fuel);
		copy.inv.set(inv);
		return copy;
	}
	@Override
	public RotatedImageGroup getImage() {
		if(mul == 2) {
			switch(volt) {
			case V2:
				return turbotex0;
			case V3:
				return turbotex1;
			case V4:
				return turbotex2;
			default:
				throw new IllegalStateException("Voltage "+volt.name+" is not supported by Turbo Generator");
			}
		}
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
	@Override
	public BlockType type() {
		return type;
	}

	//Electricity
	@Override
	public Electricity getElectricalConnection(Side s) {
		return Electricity.extractOnly(buffer);
	}
	
	//Serialization
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
	private void resetBuffer() {
		buffer.voltage = volt;
		buffer.maxPower = 50.0*mul;
		buffer.capacity = 10_000;
		buffer.pressureWt = volt.volts;
		fuel.voltage = volt;
		fuel.maxPower = 20.0*mul;
		fuel.capacity = 500_000;
	}
}
