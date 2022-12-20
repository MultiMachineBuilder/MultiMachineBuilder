/**
 * 
 */
package mmb.content.machinemics.pack;

import java.util.Objects;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import mmb.NN;
import mmb.Nil;
import mmb.content.ContentsItems;
import mmb.engine.craft.ItemLists;
import mmb.engine.craft.RecipeOutput;
import mmb.engine.item.ItemEntity;
import mmb.engine.item.ItemEntry;
import mmb.engine.item.ItemType;
import mmb.engine.json.JsonTool;

/**
 * @author oskar
 *
 */
public class Pack extends ItemEntity {

	private double volumeInner = 0;
	private double volumeOuter = 0.001;
	@NN private RecipeOutput contents = RecipeOutput.NONE;

	/**
	 * Creates an empty item pack
	 */
	public Pack() {
		super();
	}
	/**
	 * Creates an item pack with item(s)
	 * @param ilist contents
	 */
	public Pack(RecipeOutput ilist) {
		super();
		contents = ilist;
		recalc();
	}

	@Override
	public ItemEntry itemClone() {
		return this;
	}

	@Override
	public void load(@Nil JsonNode data) {
		if(data == null) return;
		JsonNode ilist = data.get("contents");
		RecipeOutput contents0 = ItemLists.read(ilist);
		if(contents0 == null) {
			contents = RecipeOutput.NONE;
		}else {
			contents = contents0;
		}
		load0(data);
		recalc();
	}
	private void recalc() {
		volumeInner = 0;
		for(Entry<ItemEntry> ent : contents.getContents().object2IntEntrySet()) {
			volumeInner += ent.getKey().volume() * ent.getIntValue();
		}
		volumeOuter = 0.001 + volumeInner;
	}
	
	@Override
	public double volume() {
		return volumeOuter;
	}

	@Override
	public JsonNode save() {
		JsonNode data = ItemLists.save(contents);
		ObjectNode master = JsonTool.newObjectNode();
		master.set("contents", data);
		save0(master);
		return master;
	}
	
	void save0(ObjectNode obj) {
		//used in CodedPack
	}
	void load0(JsonNode node) {
		//used in CodedPack
	}

	@Override
	public int hash0() {
		final int prime = 31;
		int result = 1;
		result = prime * result + contents.hashCode();
		return result;
	}

	@Override
	public boolean equal0(ItemEntity obj) {
		if (getClass() != obj.getClass())
			return false;
		Pack other = (Pack) obj;
		return Objects.equals(contents, other.contents);
	}
	@Override
	public ItemType type() {
		return ContentsItems.pack;
	}
}
