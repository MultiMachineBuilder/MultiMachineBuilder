/**
 * 
 */
package mmb.WORLD.blocks.machine.pack;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import mmb.DATA.json.JsonTool;
import mmb.WORLD.crafting.ItemLists;
import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.item.ItemEntity;
import mmb.WORLD.item.ItemEntityType;
import mmb.WORLD.items.ContentsItems;
import mmb.WORLD.items.ItemEntry;

/**
 * @author oskar
 *
 */
public class Pack extends ItemEntity {

	private double volumeInner = 0;
	private double volumeOuter = 0.001;
	@Nonnull private RecipeOutput contents = RecipeOutput.NONE;
	
	Pack(ItemEntityType type) {
		super(type);
	}
	
	Pack(ItemEntityType type, RecipeOutput ilist) {
		this(type);
		contents = ilist;
		recalc();
	}
	/**
	 * Creates an empty non-coded item pack
	 */
	Pack() {
		super(ContentsItems.pack);
	}
	Pack(RecipeOutput ilist) {
		this();
		contents = ilist;
		recalc();
	}
	
	public static Pack createEmpty() {
		return new Pack();
	}
	public static Pack create(RecipeOutput ilist) {
		return new Pack(ilist);
	}

	@Override
	public ItemEntry itemClone() {
		return this;
	}

	@Override
	public void load(@Nullable JsonNode data) {
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
			volumeInner += ent.getKey().volume(ent.getIntValue());
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contents == null) ? 0 : contents.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pack other = (Pack) obj;
		return Objects.equals(contents, other.contents);
	}

}