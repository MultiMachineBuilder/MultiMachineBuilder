/**
 * 
 */
package mmb.WORLD.items.pickaxe;

import java.awt.Graphics;
import java.awt.Color;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.IntNode;

import mmb.WORLD.item.ItemEntity;
import mmb.WORLD.item.ItemEntityType;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.tool.ToolPickaxe;
import mmb.WORLD.tool.WindowTool;

/**
 * @author oskar
 *
 */
public class Pickaxe extends ItemEntity {
	public final int durability;
	private int uses;
	/**
	 * @param type
	 */
	protected Pickaxe(PickaxeType type, int durability) {
		super(type);
		this.durability = durability;
		pick = new ToolPickaxe(this);
	}

	@Override
	public ItemEntry itemClone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void load(@Nullable JsonNode data) {
		if(data == null) return;
		uses = data.asInt();
	}

	@Override
	public JsonNode save() {
		return new IntNode(uses);
	}

	/**
	 * @return the uses
	 */
	public int getUses() {
		return uses;
	}

	/**
	 * @param uses the uses to set
	 */
	public void setUses(int uses) {
		this.uses = uses;
	}

	@Override
	public void render(Graphics g, int x, int y, int w, int h) {
		super.render(g, x, y, w, h);
		//render durability
		if(uses > 0) {
			double percent = 1.0-((double)uses/durability);
			int min = w/10;
			int max = (w*9)/10;
			if(percent < 0) {
				g.setColor(Color.RED);
				g.drawLine(x+min, y+max, x+max, y+max);
			}else if(percent > 1) {
				g.setColor(Color.CYAN);
				g.drawLine(x+min, y+max, x+max, y+max);
			}else{
				g.setColor(Color.BLACK);
				g.drawLine(x+min, y+max, x+max, y+max);
				int red = 0, green = 0;
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
				g.drawLine(x+min, y+max, (int)(x+min+offset), y+max);
			}
			
		}
	}
	
	/**
	 * An extension for {@link ItemEntityType} for pickaxes
	 * @author oskar
	 *
	 */
	public static class PickaxeType extends ItemEntityType{
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
		@Nonnull public PickaxeType setDurability(int durability) {
			this.durability = durability;
			return this;
		}

		@Override
		public ItemEntry create() {
			return new Pickaxe(this, durability);
		}

		/**
		 * @return the time
		 */
		public int getTime() {
			return time;
		}

		/**
		 * @param time the time to set
		 */
		@Nonnull public PickaxeType setTime(int time) {
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
	@Nonnull public static PickaxeType create(int time, int durability, String texture, String title, String id) {
		PickaxeType result = new PickaxeType();
		return (PickaxeType) result.setDurability(durability).setTime(time).texture(texture).title(title).finish(id);
	}

	private final ToolPickaxe pick;
	@Override
	public WindowTool getTool() {
		return pick;
	}

	@Override
	public String title() {
		return type().title() + " ("+(durability-uses)+"/"+durability+")";
	}

	@Override
	public PickaxeType type() {
		return (PickaxeType) super.type();
	}

}
