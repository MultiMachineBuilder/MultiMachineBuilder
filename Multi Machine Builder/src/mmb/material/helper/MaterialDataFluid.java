/**
 * 
 */
package mmb.material.helper;

import mmb.material.types.Fluid;

/**
 * @author oskar
 *
 */
public class MaterialDataFluid extends MaterialData implements Fluid {
	/**
	 * @param name
	 */
	public MaterialDataFluid(String name, double dens) {
		super(name, dens);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see mmb.material.Material#name()
	 */
	@Override
	public String name() {
		// TODO Auto-generated method stub
		return ID;
	}

}
