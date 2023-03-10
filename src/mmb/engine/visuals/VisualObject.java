/**
 * 
 */
package mmb.engine.visuals;

import mmb.Nil;
import mmb.engine.worlds.world.World;

/**
 * @author oskar
 * This class allows easy management of visual objects (effects, handles etc...)
 * @param <T> type of the visual object
 */
public class VisualObject<@Nil T extends Visual> {
	private T obj;
	/** The world with visual objects */
	public final World w;
	
	/**
	 * Creates a visual object with a visual
	 * @param obj visual object (may be null)
	 * @param w the underlying world
	 */
	public VisualObject(@Nil T obj, World w) {
		super();
		setObj(obj);
		this.w = w;
	}
	/**
	 * Creates a visual object without a visual
	 * @param w the underlying world
	 */
	public VisualObject(World w) {
		super();
		this.w = w;
	}
	
	/** @return the current visual object */
	public T getObj() {
		return obj;
	}
	/** @param obj1 new visual object */
	public void setObj(T obj1) {
		if(obj != null) w.removeVisual(obj);
		obj = obj1;
		if(obj1 != null) w.addVisual(obj1);
	}
	/** Removes the visual object */
	public void kill() {
		setObj(null);
	}
}
