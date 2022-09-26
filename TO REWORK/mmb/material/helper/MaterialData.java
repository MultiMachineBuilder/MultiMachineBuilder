package mmb.material.helper;

import mmb.material.Material;

public class MaterialData implements Material{
	public final String ID;
	public double density;
	public MaterialData(String name, double dens) {
		ID = name;
		density = dens;
	}
	@Override
	public String name() {
		return ID;
	}
}
