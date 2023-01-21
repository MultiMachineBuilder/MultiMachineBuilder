/**
 * 
 */
package mmb.content.modular.chest;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.NN;
import mmb.Nil;
import mmb.content.modular.BlockCore;
import mmb.content.modular.gui.ModuleConfigHandler;
import mmb.content.modular.part.PartEntity;
import mmb.engine.chance.Chance;
import mmb.engine.craft.SimpleItemList;
import mmb.engine.craft.SingleItem;
import mmb.engine.inv.SaveInventory;
import mmb.engine.item.ItemEntry;
import mmb.engine.texture.Textures;
import mmb.menu.world.inv.AbstractInventoryController;
import mmb.menu.world.inv.InventoryController;

/**
 * A chest core
 * @author oskar
 * @param <Tinv> type of the inventory
 */
public abstract class ChestCore<Tinv extends SaveInventory> extends PartEntity implements BlockCore<ChestCore<Tinv>>{
	/** Base constructor for chest cores */
	protected ChestCore(Tinv inventory) {
		this.inventory = inventory;
	}
	
	@Override
	@NN public Chance dropItems() {
		return new SimpleItemList(inventory);
	}
	
	/** The inventory for the core*/
	@NN public final Tinv inventory;

	/**
	 * Creates a new inventory controller
	 * @return a new inventory controller
	 */
	public abstract @NN AbstractInventoryController invctrl();

	/**
	 * Creates an empty chest core of the same type. Used by the {@link #returnToPlayer()}
	 * @return an empty chest core of the same type
	 */
	public abstract @NN ChestCore<Tinv> makeEmpty();
	
	@Override
	public void load(@Nil JsonNode data) {
		double capacity = inventory.capacity();
		inventory.load(data);
		inventory.setCapacity(capacity);
	}

	@Override
	public JsonNode save() {
		return inventory.save();
	}
	
	/** @return item to display on modular chests */
	@Nil public abstract SingleItem getRenderItem();

	@Override
	public void render(int x, int y, Graphics g, int w) {
		super.render(x, y, g, w);
		//Get the stack
		SingleItem stack = getRenderItem();
		if(stack == null) return;
		
		//Render the item
		int is = w/4;
		int ix = x + (3*w)/8;
		int iy = y + (3*w)/8;
		ItemEntry item = stack.item();
		item.render(g, ix, iy, is, is);
	
		//Render the count
		if(stack == item) return; //Do not render counts for plain items
		if(w < 128) return; //To small to see digits
		int[] digits = new int[6];
		digits[4] = -1;
		int count = stack.amount();
		int divided = count;
		if(count > 9_999_999) {
			divided = count/1000_000;
			digits[5] = 11;
		}else if(count > 9_999) {
			divided = count/1000;
			digits[5] = 10;
		}
		digits[0] = divided / 1000;
		digits[1] = (divided / 100) % 10;
		digits[2] = (divided / 10) % 10;
		digits[3] = divided % 10;
		
		//Width: 5/128m
		//Height: 10/128m
		
		//voffset = (5/8) + (1/128) = (80/128)+(1/128) = 81/128
		//==========================
		//hoffset+30/128=64/128
		//hoffset=(64-15)/128 = 49/128
		
		int dhoffset = x + (49*w)/128;
		int dvoffset = y + (81*w)/128;
		int dwidth = (5*w)/128;
		int dheight = (10*w)/128;
		for(int i = 0; i < 6; i++) {
			int digX = dhoffset+(i*dwidth);
			int digit = digits[i];
			if(digit == -1) continue;
			g.drawImage(font[digit], digX, dvoffset, dwidth, dheight, Color.CYAN, null);
		}
	}
	
	@NN private static final BufferedImage[] font;
	static {
		BufferedImage srcfont = Textures.get("nums.png");
		int chwidth = 5;
		int chheight = 9;
		BufferedImage[] font0 = new BufferedImage[12]; 
		for(int i = 0; i < 6; i++) {
			int x = i*chwidth;
			font0[i] = srcfont.getSubimage(x, 0, chwidth, chheight);
		}
		for(int i = 0; i < 6; i++) {
			int x = i*chwidth;
			font0[i+6] = srcfont.getSubimage(x, 16, chwidth, chheight);
		}
		
		font = font0;
	}

	@Override
	protected int hash0() {
		return inventory.hashCode();
	}

	@Override
	protected boolean equal0(PartEntity other) {
		if(other instanceof ChestCore) {
			return inventory.equals(((ChestCore<?>)other).inventory);
		} return false;
	}

	
	@Override
	public @Nil ModuleConfigHandler<ChestCore<Tinv>, ?> mch() {
		return new ModuleConfigHandler<ChestCore<Tinv>, ChestCoreAccess>() {
			@Override
			public @NN ChestCoreAccess newComponent(InventoryController invctrl, ChestCore<Tinv> core) {
				return new ChestCoreAccess(invctrl, core);
			}			
		};
	}
}