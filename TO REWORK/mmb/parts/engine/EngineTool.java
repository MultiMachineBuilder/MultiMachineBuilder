/**
 * 
 */
package mmb.parts.engine;

import mmb.parts.modules.PartModule;
import mmb.world2.Circumstances;

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
