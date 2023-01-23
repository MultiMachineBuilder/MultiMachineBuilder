/**
 * 
 */
package mmb.content.electric.helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.Nil;
import mmb.content.electric.machines.CycleResult;
import mmb.content.electric.machines.GUIMachine;
import mmb.engine.craft.Recipe;

/**
 * Handles processing logic for autocrafting machines (for all types)
 * @author oskar
 * @param <Trecipe> type of recipes
 */
public interface Helper<Trecipe extends Recipe<?>> {
	/** @return the currently used recipe */
	public Trecipe currentRecipe();
	/**
	 * Saves data to the machine JSON node
	 * @param on node to save to
	 */
	public void save(ObjectNode on);
	/**
	 * Reads data from JSON
	 * @param data node to load from
	 */
	public void load(@Nil JsonNode data);
	/** 
	 * Runs the crafting process
	 * @return the result of crafting processes
	 */
	public CycleResult cycle();
	/**
	 * Resets the GUI for this helper
	 * @param tab the GUI to use
	 */
	public void setRefreshable(@Nil GUIMachine tab);
}
