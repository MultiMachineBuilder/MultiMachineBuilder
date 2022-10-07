/**
 * 
 */
package mmb.world.electromachine;

import mmb.beans.Electric;
import mmb.world.crafting.RecipeGroup;
import mmb.world.electric.Battery;
import mmb.world.electric.Electricity;
import mmb.world.electric.VoltageTier;
import mmb.world.inventory.Inventory;

/**
 * @author oskar
 * Provides an interface to a single recipe at once machine
 */
public interface ElectroMachine extends Electric{
	/**
	 * @return the voltage tier
	 */
	public VoltageTier voltage();
	/**
	 * @return the input inventory for inventory controller
	 */
	public Inventory input();
	/**
	 * @return the output inventory for inventory controller
	 */
	public Inventory output();
	/**
	 * @return the internal electrical buffer
	 */
	public Battery energy();
	/**
	 * @return the recipe name
	 */
	public String machineName();
	/**
	 * @return the recipe group
	 */
	public RecipeGroup<?> recipes();
	@Override
	public default Electricity getElectricity() {
		return Electricity.optional(energy());
	}
	
	/**
	 * @return does machine auto-extract items?
	 */
	public boolean isAutoExtract();
	/**
	 * @param extract should machine auto-extract items?
	 */
	public void setAutoExtract(boolean extract);
	/**
	 * @return does machine pass on unsupported items?
	 */
	public boolean isPass();
	/**
	 * @param pass should machine pass on unsupported items?
	 */
	public void setPass(boolean pass);
}
