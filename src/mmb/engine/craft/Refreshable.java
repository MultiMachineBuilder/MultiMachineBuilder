/**
 * 
 */
package mmb.engine.craft;

import javax.annotation.Nullable;

/**
 * @author oskar
 * An object which is refreshed during processing
 */
public interface Refreshable{
	/** Refreshes the input list */
	public void refreshInputs();
	/** Refreshes the output list */
	public void refreshOutputs();
	/** Refreshes the progress bar 
	 * @param progress processing progress , where 0 is beginning and 1 is end
	 * @param recipe item which is currently smelted
	 * @param max energy required to complete the recipe
	 */
	public void refreshProgress(double progress, @Nullable Recipe<?> recipe);
}