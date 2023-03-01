/**
 * 
 */
package mmb.content.electric.machines;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import mmb.content.electric.Battery;
import mmb.engine.inv.Inventory;
import mmb.engine.inv.ItemRecord;
import mmb.engine.item.ItemEntry;

/**
 * Burns fuels to generate energy
 * @author oskar
 */
public class FuelBurner {
	public final double mul;
	public final Inventory inv;
	public final Battery bat;
	public final Object2DoubleMap<ItemEntry> fuels;
	/**
	 * @param mul efficiency multiplier (1 is equal to normal furnace efficiency)
	 * @param inv the inventory to extract fuels from
	 * @param bat the target power output
	 */
	public FuelBurner(double mul, Inventory inv, Battery bat, Object2DoubleMap<ItemEntry> fuels) {
		super();
		this.mul = mul;
		this.inv = inv;
		this.bat = bat;
		this.fuels = fuels;
	}
	/**
	 * Polls the inventory and burns any fuels within
	 */
	public void cycle(){
		for(ItemRecord ir: inv) {
			if(ir.amount() == 0) continue; //The item record is empty
			double fuelEnergy = fuels.getOrDefault(ir.item(), Double.NaN)*mul; //Fuel energy in joules
			if(Double.isNaN(fuelEnergy)) continue; //The item is not a fuel
			double remainCharge = bat.remain();
			double fuelCharge = fuelEnergy/bat.voltage.volts; //Fuel energy in coulombs
			if(remainCharge < fuelCharge) return; //There is not enough room for the fuel
			int extract = ir.extract(1);
			if(extract == 0) continue; //The extraction failed
			bat.stored += fuelCharge;
		}
	}
	
}
