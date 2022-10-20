/**
 * 
 */
package mmb.world.blocks.chest;

import java.awt.Color;
import java.awt.Graphics;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.cgui.BlockActivateListener;
import mmb.data.Save;
import mmb.data.json.JsonTool;
import mmb.menu.world.window.WorldWindow;
import mmb.world.block.BlockEntry;
import mmb.world.block.BlockType;
import mmb.world.block.SkeletalBlockEntityRotary;
import mmb.world.blocks.ContentsBlocks;
import mmb.world.inventory.Inventories;
import mmb.world.inventory.Inventory;
import mmb.world.inventory.io.InventoryReader;
import mmb.world.inventory.io.InventoryWriter;
import mmb.world.inventory.storage.SimpleInventory;
import mmb.world.rotate.RotatedImageGroup;
import mmb.world.rotate.Side;
import mmb.world.worlds.MapProxy;
import mmb.world.worlds.world.World;

/**
 * @author oskar
 * A hopper chest
 */
public class Hopper extends SkeletalBlockEntityRotary implements ArbitraryChest, BlockActivateListener {
	
	@Override
	public BlockType type() {
		return ContentsBlocks.HOPPER;
	}
	
	public final byte state;
	/**
	 * Creates a new hopper chest
	 */
	public Hopper(byte state) {
		this.state = state;
		inv.setCapacity(5);
		switch(state) {
		case 1:
			tex0 = TEXTUREH;
			type = ContentsBlocks.HOPPER;
			break;
		case 2:
			tex0 = TEXTURES;
			type = ContentsBlocks.HOPPER_suck;
			break;
		case 3:
			tex0 = TEXTUREB;
			type = ContentsBlocks.HOPPER_both;
			break;
		default:
			throw new IllegalArgumentException("Unsupported hopper-chest: "+state);
		}
	}

	@Override
	public BlockEntry blockCopy() {
		Hopper result = new Hopper(state);
		result.setRotation(getRotation());
		result.inv.set(inv);
		result.c = c;
		return result;
	}
	private static final RotatedImageGroup TEXTUREH = RotatedImageGroup.create("machine/hopper.png");
	private static final RotatedImageGroup TEXTURES = RotatedImageGroup.create("machine/sucker.png");
	private static final RotatedImageGroup TEXTUREB = RotatedImageGroup.create("machine/transferrer.png");
	
	private final RotatedImageGroup tex0;
	private final BlockType type;
	@Override
	public RotatedImageGroup getImage() {
		return tex0;
	}
	@Override
	public void click(int blockX, int blockY, World map, @Nullable WorldWindow window, double partX, double partY) {
		if(window == null) return;
		window.openAndShowWindow(new ChestGui(this, window), "chest");
	}
	
	//Inventory
	@Nonnull private final SimpleInventory inv = new SimpleInventory();
	@Override
	public Inventory inv() {
		return inv;
	}
	@Override
	public Inventory getInventory(Side s) {
		return inv;
	}
	
	//The hopping action
	@Override
	public void onTick(MapProxy proxy) {
		if((state & 1) != 0) {
			BlockEntry block = proxy.getAtSide(posX(), posY(), getRotation().U());
			InventoryWriter w = block.getInput(getRotation().D());
			Inventories.transferFirst(inv, w);
		}
		if((state & 2) != 0) {
			BlockEntry block = proxy.getAtSide(posX(), posY(), getRotation().D());
			InventoryReader r = block.getOutput(getRotation().U());
			Inventories.transferFirst(r, inv);
		}
	}

	//Color
	@Nonnull private Color c = Color.WHITE;
	@Override
	public Color getColor() {
		return c;
	}

	@Override
	public void setColor(Color c) {
		this.c = c;
	}

	
	@Override
	public void render(int x, int y, Graphics g, int ss) {
		g.setColor(c);
		g.fillRect(x, y, ss, ss);
		super.render(x, y, g, ss);
	}

	@Override
	protected final void load1(@Nullable ObjectNode data) {
		if(data == null) return;
		inv.load(JsonTool.requestArray("inventory", data));
		inv.setCapacity(5);
		ObjectNode on = data;
		JsonNode cnode = on.get("color");
		if(!(cnode == null || cnode.isMissingNode() || cnode.isNull()))
			setColor(Save.loadColor(cnode));
		else
			setColor(Color.WHITE);
	}
	@Override
	protected final void save1(ObjectNode node) {
		node.set("inventory", inv.save());
		node.set("color", Save.saveColor(c));
	}
}
