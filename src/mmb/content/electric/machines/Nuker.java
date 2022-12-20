/**
 * 
 */
package mmb.content.electric.machines;

import java.awt.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.NN;
import mmb.content.ContentsBlocks;
import mmb.content.CraftingGroups;
import mmb.content.electric.VoltageTier;
import mmb.content.rawmats.Materials;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.debug.Debugger;
import mmb.engine.inv.Inventories;
import mmb.engine.inv.storage.SingleItemInventory;
import mmb.engine.item.ItemEntry;
import mmb.engine.worlds.MapProxy;

/**
 * @author oskar
 * Represents a nuclear reactor.
 * 
 * Operation:
 * The reactor takes one item until it accumulates 100 gigajoules of energy
 * The reactor produces 1 MW of electricity until it has less than 20 kilojoules of stored fuel
 * It has 10 MJ energy buffer
 * 
 */
public class Nuker extends SkeletalBlockMachine {
	private static final Debugger debug = new Debugger("NUCLEAR REACTOR");
	@Override
	protected void onTick0(MapProxy map) {
		//Extract fuel if possible
		if(nuked.isEmpty()) 
			Inventories.transferFirst(inItems, nuked);
		
		ItemEntry cont = nuked.getContents();
		//Check the cache
		if(cont != null) {
			if(CraftingGroups.nukeFuels.containsKey(cont) && fuelRemain < 1200_000_000L) {
				//Valid fuel
				fuelRemain += CraftingGroups.nukeFuels.getDouble(cont.type());
				nuked.setContents(null);
			}else {
				//Invalid fuel
				Inventories.transfer(nuked, outItems);
			}
		}
		
		//Burn the fuel
		if((!Double.isFinite(fuelRemain)) || fuelRemain < 0) {
			debug.printl("Invalid remaining energy: "+fuelRemain);
			fuelRemain = 0;
		}else if(fuelRemain < 20_000) {
			fuelRemain -= outElec.insert(fuelRemain, VoltageTier.V1);
			if(fuelRemain < 1) fuelRemain = 0;
		}else {
			fuelRemain -= outElec.insert(20_000, VoltageTier.V1);
		}
	}

	/**
	 * @return the fuelRemain
	 */
	public double getFuelRemain() {
		return fuelRemain;
	}

	/**
	 * @param fuelRemain the fuelRemain to set
	 */
	public void setFuelRemain(double fuelRemain) {
		this.fuelRemain = fuelRemain;
	}

	@Override
	protected Component createGUI() {
		return new NukeGUI(this);
	}

	private double fuelRemain;
	@NN private final SingleItemInventory nuked = new SingleItemInventory();

	public Nuker() {
		super(15);
		outElec.maxPower = 50000;
	}

	@Override
	public BlockType type() {
		return ContentsBlocks.NUKEGEN;
	}

	@Override
	public void load1(ObjectNode data) {
		nuked.setContents(ItemEntry.loadFromJson(data.get("fuel")));
		JsonNode node = data.get("energy");
		if(node != null) fuelRemain = node.asDouble();
	}

	@Override
	protected void save1(ObjectNode node) {
		node.set("fuel", ItemEntry.saveItem(nuked.getContents()));
		node.put("energy", fuelRemain);
	}

	private static boolean inited;
	public static void init() {
		if(inited) throw new IllegalStateException("Already initialized");
		CraftingGroups.nukeFuels.put(Materials.uranium.base, 1000_000_000.0); //uranium - 1 GJ
		inited = true;
	}
	
	@Override
	public BlockEntry blockCopy() {
		Nuker copy = new Nuker();
		copy.cfgInElec.set(cfgInElec);
		copy.cfgOutElec.set(cfgOutElec);
		copy.cfgInItems.set(cfgInItems);
		copy.cfgOutItems.set(cfgOutItems);
		copy.fuelRemain = fuelRemain;
		return copy;
	}
}
