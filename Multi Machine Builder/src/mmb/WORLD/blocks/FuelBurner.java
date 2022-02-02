/**
 * 
 */
package mmb.WORLD.blocks;

import mmb.WORLD.crafting.Craftings;
import mmb.WORLD.electric.Battery;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.ItemRecord;

/**
 * @author oskar
 * A helper class to help burn fuels
 */
public class FuelBurner {
	public final double mul;
	public final Inventory inv;
	public final Battery bat;
	/**
	 * @param mul efficiency multiplier (1 is equal to normal furnace efficiency)
	 * @param inv the inventory to extract fuels from
	 * @param bat the target power output
	 */
	public FuelBurner(double mul, Inventory inv, Battery bat) {
		super();
		this.mul = mul;
		this.inv = inv;
		this.bat = bat;
	}
	/**
	 * Polls the inventory and burns any fuels within
	 */
	public void cycle(){
		for(ItemRecord ir: inv) {
			if(ir.amount() == 0) continue; //The item record is empty
			double fv = Craftings.furnaceFuels.getOrDefault(ir.item(), Double.NaN)*mul; //Fuel energy in joules
			if(Double.isNaN(fv)) continue; //The item is not a fuel
			double remain = bat.remain();
			double fv2 = fv/bat.voltage.volts; //Fuel energy in coulombs
			if(remain < fv2) return; //There is not enough room for the fuel
			int extract = ir.extract(1);
			if(extract == 0) continue; //The extraction failed
			bat.amt += fv2;
		}
	}
	
}
