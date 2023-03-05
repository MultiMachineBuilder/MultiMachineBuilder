/**
 * 
 */
package mmb.engine.recipe;

import mmb.Nil;

/**
 * An object which is refreshed during processing
 * @author oskar
 */
public interface Refreshable{
	/** Refreshes the input list */
	public void refreshInputs();
	/** Refreshes the output list */
	public void refreshOutputs();
	/** Refreshes the progress bar 
	 * @param progress processing progress , where 0 is beginning and 1 is end
	 * @param recipe item which is currently smelted
	 */
	public void refreshProgress(double progress, @Nil Recipe<?> recipe);
}