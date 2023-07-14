/**
 * 
 */
package mmb.content.imachine.filter;

import mmb.NN;
import mmb.Nil;
import mmb.cgui.DestroyTab;
import mmb.engine.inv.storage.SingleItemInventory;

/**
 * Provides filtering settings for pipes and other machines
 * @author oskar
 */
public interface ControllableFilter extends DestroyTab{
	/**
	 * 
	 * @return a list of filters
	 */
	public SingleItemInventory[] getFilters();
	/**
	 * Gets the list of titles.
	 * Returns null if filters are not named
	 * If result is non-null, the number of titles must match number of filters,
	 * otherwise the {@link FilterGUI} will fail with {@link IllegalStateException};
	 * @return a list of filter titles
	 */
	public @Nil String @NN [] getTitles();
	
}
