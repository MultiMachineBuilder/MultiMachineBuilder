/**
 * 
 */
package mmb.world.parts.engine.jet;

import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;

import mmb.world.Circumstances;
import mmb.world.parts.Part;
import mmb.world.parts.engine.EngineTool;
import mmb.world.parts.modules.PartModule;

/**
 * @author oskar
 *
 */
public class JetEngine implements EngineTool {
	public JetSpecs specs;
	public double thrust;

	
	/**
	 * 
	 */
	public JetEngine() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see mmb.world.parts.modules.PartModule#build()
	 */
	@Override
	public void build() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see mmb.world.parts.modules.PartModule#partCreation(mmb.world.parts.Part)
	 */
	@Override
	public void partCreation(Part p) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see mmb.world.parts.modules.PartModule#partCrash(mmb.world.Circumstances)
	 */
	@Override
	public void partCrash(Circumstances cs) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see mmb.world.parts.modules.PartModule#createGeometry(com.jme3.scene.Geometry, com.jme3.scene.Spatial)
	 */
	@Override
	public void createGeometry(Geometry g, Spatial s) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see mmb.world.parts.engine.EngineTool#beforeKick()
	 */
	@Override
	public void beforeKick() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see mmb.world.parts.engine.EngineTool#afterKick()
	 */
	@Override
	public void afterKick() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see mmb.world.parts.engine.EngineTool#calcThrust()
	 */
	@Override
	public double calcThrust() {
		// TODO Auto-generated method stub
		return 0;
	}

	

}
