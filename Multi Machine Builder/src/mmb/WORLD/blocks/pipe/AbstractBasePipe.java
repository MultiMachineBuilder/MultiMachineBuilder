/**
 * 
 */
package mmb.WORLD.blocks.pipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.GameObject;
import mmb.DATA.json.JsonTool;
import mmb.WORLD.RotatedImageGroup;
import mmb.WORLD.Side;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.block.SkeletalBlockEntityRotary;
import mmb.WORLD.gui.Variable;
import mmb.WORLD.inventory.io.InventoryReader;
import mmb.WORLD.inventory.io.InventoryWriter;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.worlds.world.World;

/**
 * @author oskar
 * A base class for pipes.
 */
public abstract class AbstractBasePipe extends SkeletalBlockEntityRotary {
	
	@Override
	public InventoryReader getOutput(Side s) {
		if(s == side.U() && outU != null) return outU;
		if(s == side.D() && outD != null) return outD;
		if(s == side.L() && outL != null) return outL;
		if(s == side.R() && outR != null) return outR;
		return InventoryReader.NONE;
	}

	@Override
	public InventoryWriter getInput(Side s) {
		if(s == side.U() && inU != null) return inU;
		if(s == side.D() && inD != null) return inD;
		if(s == side.L() && inL != null) return inL;
		if(s == side.R() && inR != null) return inR;
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

	@Override
	public void onBreak(World map, GameObject obj) {
		for(ItemEntry item: items) {
			map.dropItem(item, posX(), posY());
		}
	}

	@Nonnull private final BlockType type;

	@Override
	protected void save1(ObjectNode node) {
		node.set("items", JsonTool.saveArray(ItemEntry::saveItem, items));
	}

	@Override
	protected void load1(ObjectNode node) {
		JsonNode data = node.get("items");
		if(data == null || data.isNull() || data.isMissingNode()) return;
		JsonTool.loadToArray(ItemEntry::loadFromJson, (ArrayNode) data, items);
	}

	protected InventoryWriter inU, inD, inL, inR;
	protected InventoryReader outU, outD, outL, outR;
	
	@Nonnull protected final ItemEntry[] items;

	protected Variable<ItemEntry> getSlot(int index){
		return Variable.ofArraySlot(index, items);
	}
	
	private final RotatedImageGroup texture;
	protected AbstractBasePipe(BlockType type, int numItems, RotatedImageGroup texture) {
		this.type = type;
		this.items = new ItemEntry[numItems];
		this.texture = texture;
	}

	@Override
	public RotatedImageGroup getImage() {
		return texture;
	}

	@Override
	public BlockType type() {
		return type;
	}
	
	/**
	 * 
	 * @author oskar
	 *
	 */
	protected class Pusher implements InventoryWriter{
		private final Variable<@Nullable ItemEntry> from;
		@Nonnull private final Side other;
		/**
		 * 
		 * @param from item entry source variable
		 * @param other the side, to which items are pushed
		 */
		public Pusher(Variable<@Nullable ItemEntry> from, Side other) {
			this.from = from;
			this.other = other;
		}
		@Override
		public int write(ItemEntry ent, int amount) {
			if(!ent.exists()) return 0;
			//Free space
			if(from.get() == null) {
				from.set(ent);
				return 1;
			}
			//No free space & Space freed
			if(push()) {
				from.set(ent);
				return 1;
			}
			//Not freed
			return 0;
		}
		//Returns: did pushing make assigned slot free
		public boolean push() {
			Side cother = getRotation().apply(other);
			Side nother = cother.negate();
			InventoryWriter writer = owner().getAtSide(cother, posX(), posY()).getInput(nother);
			if(from.get() == null) return true;
			int amt = writer.write(from.get());
			if(amt == 1) {
				from.set(null);
				return true;
			}
			return false;
		}
	}

	protected class ExtractForItemVar implements InventoryReader{
		@Override
		public int toBeExtracted(int amount) {
			if(vitem.get() == null) return 0;
			if(amount < 1) return 0;
			return 1;
		}
		private final Variable<@Nullable ItemEntry> vitem;
		public ExtractForItemVar(Variable<@Nullable ItemEntry> vitem) {
			this.vitem = vitem;
		}
		@Override
		public int currentAmount() {
			if(vitem.get() == null) return 0;
			return 1;
		}

		@Override
		public ItemEntry currentItem() {
			return vitem.get();
		}

		@Override
		public int extract(int amount) {
			if(vitem.get() == null) return 0;
			if(amount < 1) return 0;
			vitem.set(null);
			return 1;
		}

		@Override
		public void skip() {
			throw new UnsupportedOperationException("Skip not supported");
		}

		@Override
		public int extract(ItemEntry entry, int amount) {
			throw new UnsupportedOperationException("Random access not supported");
		}

		@Override
		public void next() {
			throw new UnsupportedOperationException("Only one ItemEntry is here");
		}

		@Override
		public boolean hasNext() {
			return false;
		}

		@Override
		public ExtractionLevel level() {
			return ExtractionLevel.SEQUENTIAL;
		}
		@Override
		public boolean hasCurrent() {
			return vitem.get() != null;
		}
		
	}
}


