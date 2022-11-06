/**
 * 
 */
package mmb.world.modulars.chest;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.data.contents.Textures;
import mmb.menu.world.inv.AbstractInventoryController;
import mmb.world.chance.Chance;
import mmb.world.crafting.RecipeOutput;
import mmb.world.crafting.SimpleItemList;
import mmb.world.crafting.SingleItem;
import mmb.world.inventory.SaveInventory;
import mmb.world.item.ItemEntityMutable;
import mmb.world.items.ItemEntry;
import mmb.world.modulars.BlockCore;

/**
 * A chest core
 * @author oskar
 * @param <Tinv> type of the inventory
 */
public abstract class ChestCore<Tinv extends SaveInventory> extends ItemEntityMutable implements BlockCore<ChestCore<Tinv>>{
	/** Base constructor for chest cores */
	protected ChestCore(Tinv inventory) {
		this.inventory = inventory;
	}
	
	@Override
	@Nonnull public Chance dropItems() {
		return new SimpleItemList(inventory);
	}
	
	/** The inventory for the core*/
	@Nonnull public final Tinv inventory;

	/**
	 * Creates a new inventory controller
	 * @return a new inventory controller
	 */
	public abstract @Nonnull AbstractInventoryController invctrl();

	/**
	 * Creates an empty chest core of the same type. Used by the {@link #returnToPlayer()}
	 * @return an empty chest core of the same type
	 */
	public abstract @Nonnull ChestCore<Tinv> makeEmpty();
	
	@Override
	public RecipeOutput returnToPlayer() {
		return makeEmpty();
	}

	@Override
	public void load(@Nullable JsonNode data) {
		double capacity = inventory.capacity();
		inventory.load(data);
		inventory.setCapacity(capacity);
	}

	@Override
	public JsonNode save() {
		return inventory.save();
	}
	
	/** @return item to display on modular chests */
	@Nullable public abstract SingleItem getRenderItem();

	@Override
	public void render(Graphics g, int x, int y, int w, int h) {
		super.render(g, x, y, w, h);
		
		//Get the stack
		SingleItem stack = getRenderItem();
		if(stack == null) return;
		
		//Render the item
		int iw = w/4;
		int ih = h/4;
		int ix = x + (3*w)/8;
		int iy = y + (3*h)/8;
		ItemEntry item = stack.item();
		item.render(g, ix, iy, iw, ih);
	
		//Render the count
		if(stack == item) return; //Do not render counts for plain items
		if(w < 128 || h < 128) return; //To small to see digits
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
		int dvoffset = y + (81*h)/128;
		int dwidth = (5*w)/128;
		int dheight = (10*h)/128;
		for(int i = 0; i < 6; i++) {
			int digX = dhoffset+(i*dwidth);
			int digit = digits[i];
			if(digit == -1) continue;
			g.drawImage(font[digit], digX, dvoffset, dwidth, dheight, Color.CYAN, null);
		}
	}
	
	@Nonnull private static final BufferedImage[] font;
	
	static {
		BufferedImage srcfont = Textures.get("nums.png");
		int chwidth = srcfont.getWidth() / 12;
		int h = srcfont.getHeight();
		BufferedImage[] font0 = new BufferedImage[12]; 
		for(int i = 0; i < 12; i++) {
			int x = i*chwidth;
			font0[i] = srcfont.getSubimage(x, 0, chwidth, h);
		}
		font = font0;
	}
}
