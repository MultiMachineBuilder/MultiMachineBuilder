/**
 * 
 */
package mmbmods.stn;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.data.variables.DataValueDouble;
import mmbeng.inv.io.InventoryReader;
import mmbeng.inv.io.InventoryWriter;
import mmbeng.item.Item;
import mmbeng.item.ItemEntityMutable;
import mmbeng.item.ItemType;
import mmbeng.json.JsonTool;

/**
 * @author oskar
 *
 */
public final class CablingTool extends ItemEntityMutable {
	/** Amount of cable in spools */
	public final DataValueDouble amt = new DataValueDouble(0);
	/** The item required to load the cabling tool */
	@Nonnull public static final Item WIRE = STN.STN_a.wire;
	/** Maximum wire in spools */
	public final double MAX_WIRE = 1000;
	
	/** Creates an STN cabling tool */
	public CablingTool() {
		//empty
	}
	/** Copy constructor */
	protected CablingTool(CablingTool original) {
		amt.set(original.amt.getDouble());
	}

	@Override
	public CablingTool itemClone() {
		return new CablingTool(this);
	}

	@Override
	public void load(@Nullable JsonNode data) {
		if(data == null || !data.isObject()) return;
		JsonNode amtNode = data.get("amt");
		if(amtNode != null) amt.set(amtNode.asDouble());
	}
	@Override
	public JsonNode save() {
		ObjectNode node = JsonTool.newObjectNode();
		node.put("amt", amt.getDouble());
		return node;
	}
	
	public boolean unloadWire(InventoryWriter inv) {
		if(amt.getDouble() < 0) return false;
		int write = inv.insert(WIRE);
		boolean result = write==1;
		if(result) amt.set(amt.getDouble()-1);
		return result;
	}
	public boolean loadWire(InventoryReader inv) {
		double remain = MAX_WIRE - amt.getDouble();
		if(remain < 1) return false;
		int write = 0;
		switch(inv.level()) {
		case RANDOM:
			write = inv.extract(WIRE, 1);
			break;
		case SKIPPING:
			while(inv.currentItem() != WIRE){
				if(!inv.hasNext()) return false;
				inv.next();
				//look for the wire
			}
			break;
		case SEQUENTIAL:
			if(inv.currentItem() != WIRE) return false;
			write = inv.extract(1);
			break;
		default:
			Objects.requireNonNull(inv.level(), "Extraction level is null");
			throw new IllegalArgumentException("Invalid extraction level: "+inv.level());
		}
		boolean result = write==1;
		if(result) amt.set(amt.getDouble()-1);
		return result;
	}
	@Override
	public ItemType type() {
		return STN.STN_cabler;
	}
}
