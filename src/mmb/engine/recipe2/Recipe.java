package mmb.engine.recipe2;

import mmb.engine.recipe2.processing.ProcessingRecipeData;

/**
 * 
 * @param <Tconfig> type of configurations
 */
public interface Recipe<Tconfig>{
	/**
	 * Get the process data required by the macgine
	 * @return
	 */
	public ProcessingRecipeData getProcessData();
	/**
	 * Gets the machine setup for the recipe. The machine's setup must match the recipe's setup exactly to work.
	 * @return required machine setup
	 */
	public Tconfig getConfiguration();
}
