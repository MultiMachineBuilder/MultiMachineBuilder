/**
 * 
 */
package mmb.WORLD.items.pickaxe;

import java.awt.Graphics;

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
	protected Pickaxe(ItemEntityType type, int durability) {
		super(type);
		this.durability = durability;
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
	public void render(Graphics g, int x, int y, int side) {
		super.render(g, x, y, side);
		//render durability
		if(uses > 0) {
			double percent = 1.0-((double)uses/durability);
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
		public PickaxeType setDurability(int durability) {
			this.durability = durability;
			return this;
		}

		@Override
		public ItemEntry create() {
			return new Pickaxe(this, durability);
		}
	}
	/**
	 * Creates and registers an pickaxe
	 * @param durability maximum uses of the pickaxe
	 * @param texture texture of the pickaxe
	 * @param title title of the pickaxe
	 * @param id ID of the pickaxe
	 * @return a new registered pickaxe type
	 */
	public static PickaxeType create(int durability, String texture, String title, String id) {
		PickaxeType result = new PickaxeType();
		return (PickaxeType) result.setDurability(durability).texture(texture).title(title).finish(id);
	}

	private ToolPickaxe pick;
	@Override
	public WindowTool getTool() {
		if(pick == null) pick = new ToolPickaxe(this);
		return pick;
	}

	@Override
	public String title() {
		return type().title() + " ("+(durability-uses)+"/"+durability+")";
	}

}
