package mmb.world.vessels;

import mmb.world.parts.Linker;
import mmb.world.parts.Part;
import mmb.world.vessels.linkages.Jettisoner;

public class MechanicalLinkage implements Linker {
	public Part A;
	public Part B;
	public boolean jettisoned = false;
	public MechanicalLinkage() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param a
	 * @param b
	 * @param jettisoned
	 */
	public MechanicalLinkage(Part a, Part b) {
		super();
		A = a;
		B = b;
		a.mechanicalLinkages.add(this);
		b.mechanicalLinkages.add(this);
	}
	public boolean contains(Part p) {
		return A == p || B == p;
	}
	/* (non-Javadoc)
	 * @see mmb.world.vessels.linkages.Jettisonable#jettison()
	 */
	@Override
	public void jettison() {
		// TODO Auto-generated method stub
		Jettisoner.jettison(this);
	}
}
