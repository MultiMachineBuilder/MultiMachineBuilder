/**
 * 
 */
package mmb.content.pickaxe;

import java.awt.Graphics;
import java.awt.Color;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.IntNode;

import mmb.NN;
import mmb.Nil;
import mmb.engine.item.ItemEntityMutable;
import mmb.engine.item.ItemEntityType;
import mmb.engine.item.ItemEntry;
import mmb.menu.wtool.WindowTool;

/**
 * A tool to mine blocks
 * @author oskar
 */
public class Pickaxe extends ItemEntityMutable {
	//Constructors
	/**
	 * Creates a pickaxe
	 * @param type pickaxe type
	 */
	protected Pickaxe(PickaxeType type) {
		this.type = type;
		pick = new ToolPickaxe(this);
	}
	
	//Contents
	private int uses;
	/** @return use count */
	public int getUses() {
		return uses;
	}
	/**  @param uses new use count */
	public void setUses(int uses) {
		this.uses = uses;
	}
	
	//Item methods
	@Override
	public ItemEntry itemClone() {
		Pickaxe copy = new Pickaxe(type);
		copy.uses = uses;
		return copy;
	}
	
	@NN private final PickaxeType type;
	@Override
	public PickaxeType type() {
		return type;
	}
	
	//Serialization
	@Override
	public void load(@Nil JsonNode data) {
		if(data == null) return;
		uses = data.asInt();
	}
	
	@Override
	public JsonNode save() {
		return new IntNode(uses);
	}

	//Tool methods
	private final ToolPickaxe pick;
	@Override
	public WindowTool getTool() {
		return pick;
	}
	
	//Preview
	@Override
	public void render(Graphics g, int x, int y, int w, int h) {
		super.render(g, x, y, w, h);
		renderDurability(1.0-((double)uses/type.durability), g, x, y, w, h);
	}
	@Override
	public String title() {
		return type().title() + " ("+(type.durability-uses)+"/"+type.durability+")";
	}
	/**
	 * Renders durability for a block. Used most by items, but still useful for blocks
	 * @param percent amount from 0 to 1 representing remaining energy in the block or item
	 * @param g graphics context
	 * @param x left X coordinate
	 * @param y upper Y coordinate
	 * @param w width
	 * @param h height
	 */
	public static void renderDurability(double percent, Graphics g, int x, int y, int w, int h) {
		//render durability
		if(percent > 0) {
			int min = w/10;
			int max = (w*9)/10;
			int yoff = (h*9)/10;
			if(percent < 0) {
				g.setColor(Color.RED);
				g.drawLine(x+min, y+yoff, x+max, y+yoff);
			}else if(percent > 1) {
				g.setColor(Color.CYAN);
				g.drawLine(x+min, y+yoff, x+max, y+yoff);
			}else{
				g.setColor(Color.BLACK);
				g.drawLine(x+min, y+yoff, x+max, y+yoff);
				int red = 0; 
				int green = 0;
				if(percent > 0.5) {
					green = 255;
					red = (int)(511*(1-percent));
				}else {
					red = 255;
					green = (int)(511*percent);
				}
				Color c = new Color(red, green, 0);
				int scale = (w*8)/10;
				double offset = min+(scale*percent);
				g.setColor(c);
				g.drawLine(x+min, y+yoff, (int)(x+min+offset), y+yoff);
			}
		}
	}
	
	//Utils
	/**
	 * An extension for {@link ItemEntityType} for pickaxes
	 * @author oskar
	 */
	public static class PickaxeType extends ItemEntityType{
		/** Creates a new pickaxe type*/
		public PickaxeType() {
			super();
			setUnstackable(true);
		}
		private int durability;
		private int time;
		/**
		 * @return the durability
		 */
		public int getDurability() {
			return durability;
		}
		/**
		 * @param durability the durability to set
		 * @return this
		 */
		@NN public PickaxeType setDurability(int durability) {
			this.durability = durability;
			return this;
		}
		@Override
		public ItemEntry create() {
			return new Pickaxe(this);
		}
		/**
		 * @return the time
		 */
		public int getTime() {
			return time;
		}
		/**
		 * Sets the mining time
		 * @param time the time to set
		 * @return this
		 */
		@NN public PickaxeType setTime(int time) {
			this.time = time;
			return this;
		}
	}
	/**
	 * Creates and registers an pickaxe
	 * @param time time to mine a block in ticks
	 * @param durability maximum uses of the pickaxe
	 * @param texture texture of the pickaxe
	 * @param title title of the pickaxe
	 * @param id ID of the pickaxe
	 * @return a new registered pickaxe type
	 */
	@NN public static PickaxeType create(int time, int durability, String texture, String title, String id) {
		PickaxeType result = new PickaxeType();
		return (PickaxeType) result.setDurability(durability).setTime(time).texture(texture).title(title).finish(id);
	}
}
