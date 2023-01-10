/**
 * 
 */
package mmb.content.electric.machines;

import mmb.content.electric.Battery;
import mmb.content.electric.Electric;
import mmb.content.electric.Electricity;
import mmb.content.electric.VoltageTier;
import mmb.engine.craft.RecipeGroup;
import mmb.engine.inv.Inventory;

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
	/** @return the recipe group */
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
