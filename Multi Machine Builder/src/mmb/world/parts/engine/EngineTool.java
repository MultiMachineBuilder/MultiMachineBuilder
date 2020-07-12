/**
 * 
 */
package mmb.world.parts.engine;

import mmb.world.Circumstances;
import mmb.world.parts.modules.PartModule;

/**
 * @author oskar
 *
 */
public interface EngineTool extends PartModule {
	default public void beforeKick() {}
	default public void afterKick() {}
	@Override
	default public void flight(Circumstances cs) {
		beforeKick();
		

	}
	public double calcThrust();
}
