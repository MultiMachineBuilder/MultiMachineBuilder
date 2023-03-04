/**
 * 
 */
package mmb.content.imachine.pipe;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.NN;
import mmb.engine.block.BlockEntityChirotable;
import mmb.engine.block.BlockType;
import mmb.engine.inv.io.InventoryReader;
import mmb.engine.inv.io.InventoryWriter;
import mmb.engine.inv.storage.SingleItemInventory;
import mmb.engine.item.ItemEntry;
import mmb.engine.json.JsonTool;
import mmb.engine.recipe.RecipeOutput;
import mmb.engine.rotate.ChirotatedImageGroup;
import mmb.engine.rotate.Side;

/**
 * An abstract base class for all item pipes
 * @author oskar
 */
public abstract class AbstractBasePipe extends BlockEntityChirotable {
	//Constructors
	protected AbstractBasePipe(BlockType type, int numItems, ChirotatedImageGroup texture) {
		this.type = type;
		this.items = new SingleItemInventory[numItems];
		for(int i = 0; i < numItems; i++)
			items[i] = new SingleItemInventory();
		this.texture = texture;
		eventDemolition.addListener(event -> {
			for(SingleItemInventory item: items) {
				ItemEntry ent = item.getContents();
				if(ent != null)
					event.world.dropItem(ent, posX(), posY());
			}
		});
	}
	
	//Contents
	protected InventoryWriter inU, inD, inL, inR;
	protected InventoryReader outU, outD, outL, outR;
	@NN protected final SingleItemInventory[] items;
	
	//Block methods
	@Override
	public InventoryReader getOutput(Side s) {
		if(s == getRotation().U() && outU != null) return outU;
		if(s == getRotation().D() && outD != null) return outD;
		if(s == getRotation().L() && outL != null) return outL;
		if(s == getRotation().R() && outR != null) return outR;
		return InventoryReader.NONE;
	}
	@Override
	public InventoryWriter getInput(Side s) {
		if(s == getRotation().U() && inU != null) return inU;
		if(s == getRotation().D() && inD != null) return inD;
		if(s == getRotation().L() && inL != null) return inL;
		if(s == getRotation().R() && inR != null) return inR;
		return InventoryWriter.NONE;
	}
	protected void setIn(InventoryWriter in, Side s) {
		switch(s) {
		case U:
			inU = in;
			break;
		case D:
			inD = in;
			break;
		case L:
			inL = in;
			break;
		case R:
			inR = in;
			break;
		default:
			break;
		}
	}
	protected void setOut(InventoryReader out, Side s) {
		switch(s) {
		case U:
			outU = out;
			break;
		case D:
			outD = out;
			break;
		case L:
			outL = out;
			break;
		case R:
			outR = out;
			break;
		default:
			break;
		}
	}
	@NN private final BlockType type;
	private final ChirotatedImageGroup texture;
	@Override
	public ChirotatedImageGroup getImage() {
		return texture;
	}

	@Override
	public BlockType type() {
		return type;
	}
	
	//Serialization
	@Override
	protected void save1(ObjectNode node) {
		node.set("items", JsonTool.saveArray(i -> ItemEntry.saveItem(i==null?null:i.getContents()), items));
	}
	@Override
	protected void load1(ObjectNode node) {
		JsonNode data = node.get("items");
		if(data == null || data.isNull() || data.isMissingNode()) return;
		JsonTool.loadToArray(ItemEntry::loadFromJson, (ArrayNode) data, items);
	}
	
	//Utilities
	/**
	 * A runnable item input used to handle item pipes
	 * @author oskar
	 */
	protected class Pusher implements InventoryWriter{
		private final SingleItemInventory from;
		@NN private final Side other;
		/**
		 * 
		 * @param from item entry source variable
		 * @param other the side, to which items are pushed
		 */
		public Pusher(SingleItemInventory from, Side other) {
			this.from = from;
			this.other = other;
		}	
		//Returns: did pushing make assigned slot free
		public boolean push() {
			Side cother = getRotation().apply(other);
			Side nother = cother.negate();
			InventoryWriter writer = owner().getAtSide(cother, posX(), posY()).getInput(nother);
			ItemEntry ent = from.getContents();
			if(ent == null) return true;
			int amt = writer.insert(ent);
			if(amt == 1) {
				from.setContents(null);
				return true;
			}
			return false;
		}
		@Override
		public int insert(ItemEntry ent, int amount) {
			return from.insert(ent, amount);
		}
		@Override
		public int bulkInsert(RecipeOutput block, int amount) {
			return from.bulkInsert(block, amount);
		}
		@Override
		public int toInsertBulk(RecipeOutput block, int amount) {
			return from.insertibleRemainBulk(amount, block);
		}
		@Override
		public int toInsert(ItemEntry item, int amount) {
			return from.insertibleRemain(amount, item);
		}
	}		
}