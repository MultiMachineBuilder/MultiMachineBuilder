/**
 * 
 */
package mmb.WORLD.electromachine;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.LookupOp;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.BEANS.BlockActivateListener;
import mmb.DATA.contents.Textures;
import mmb.GRAPHICS.awt.ColorMapper;
import mmb.MENU.world.machine.GUIGeneratorSolid;
import mmb.MENU.world.window.WorldWindow;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.block.SkeletalBlockEntityRotary;
import mmb.WORLD.electric.Battery;
import mmb.WORLD.electric.Electricity;
import mmb.WORLD.electric.VoltageTier;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.storage.SimpleInventory;
import mmb.WORLD.recipes.CraftingGroups;
import mmb.WORLD.rotate.RotatedImageGroup;
import mmb.WORLD.rotate.Side;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.world.World;

/**
 * @author oskar
 *
 */
public class BlockGeneratorSolid extends SkeletalBlockEntityRotary implements BlockActivateListener{
	
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
	
	@Nonnull public static final BufferedImage turboimg;
	@Nonnull private static final RotatedImageGroup turbotex0;
	@Nonnull public static final BufferedImage turboimg1;
	@Nonnull private static final RotatedImageGroup turbotex1;
	@Nonnull public static final BufferedImage turboimg2;
	@Nonnull private static final RotatedImageGroup turbotex2;
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
	
	@Nonnull public final VoltageTier volt;
	@Nonnull private final BlockType type;
	@Nonnull public final Battery fuel;
	@Nonnull public final Battery buffer;
	@Nonnull public final SimpleInventory inv = new SimpleInventory();
	@Nonnull private final FuelBurner burner;
	public final int mul;
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

	/**
	 * @param volt
	 */
	private void resetBuffer() {
		buffer.voltage = volt;
		buffer.maxPower = 50.0*mul;
		buffer.capacity = 10_000;
		buffer.pressureWt = volt.volts;
		fuel.voltage = volt;
		fuel.maxPower = 20.0*mul;
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

	GUIGeneratorSolid tab;
	@Override
	public void click(int blockX, int blockY, World map, @Nullable WorldWindow window, double partX, double partY) {
		if(window == null) return;
		if(tab != null) return;
		tab = new GUIGeneratorSolid(window, this);
		window.openAndShowWindow(tab, type.title());
	}
	
	@SuppressWarnings("javadoc") //internal use only
	public void close(GUIGeneratorSolid tbb) {
		if(tab == tbb) tab = null;
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
		BlockGeneratorSolid copy = new BlockGeneratorSolid(mul, volt, type);
		copy.buffer.set(buffer);
		copy.fuel.set(fuel);
		copy.inv.set(inv);
		return copy;
	}

}
