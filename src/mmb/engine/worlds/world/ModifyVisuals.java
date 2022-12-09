/**
 * 
 */
package mmbeng.worlds.world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import mmbeng.visuals.Visual;

/**
 * @author oskar
 * A class to help apply visuals
 */
public class ModifyVisuals {	
	@Nonnull private List<@Nonnull Visual> add = new ArrayList<>();
	@Nonnull private List<@Nonnull Visual> remove = new ArrayList<>();
	
	/**
	 * Adds a single visual object
	 * @param vis the visual object
	 */
	public void add(Visual vis) {
		add.add(vis);
	}
	/**
	 * Adds multiple visual objects
	 * @param vis array of visual objects
	 */
	public void adds(Visual... vis) {
		add.addAll(Arrays.asList(vis));
	}
	/**
	 * Adds multiple visual objects
	 * @param vis collection of visual objects
	 */
	public void adds(Collection<@Nonnull Visual> vis) {
		add.addAll(vis);
	}
	/**
	 * Removes a single visual object
	 * @param vis the visual object
	 */
	public void remove(Visual vis) {
		remove.add(vis);
	}
	/**
	 * Removes multiple visual objects
	 * @param vis array of visual objects
	 */
	public void removes(Visual... vis) {
		remove.addAll(Arrays.asList(vis));
	}
	/**
	 * Removes multiple visual objects
	 * @param vis collection of visual objects
	 */
	public void removes(Collection<@Nonnull Visual> vis) {
		remove.addAll(vis);
	}
	/**
	 * Applies this visuals change to the world
	 * @param that the world
	 */
	public void apply(World that) {
		that.addVisuals(add);
		that.removeVisuals(remove);
	}
}
