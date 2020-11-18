package mmb.parts.modules;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;

import mmb.parts.Part;
import mmb.parts.PartSpec;
import mmb.world2.Circumstances;
public interface PartModule {
	default public void removeFromSpec(PartSpec s) {
		s.specs.modules.remove(this);
	}
	default public void addToPart(PartSpec s) {
		s.specs.modules.add(this);
	}
	
	public void build();
	public void flight(Circumstances cs);
	public void partCreation(Part p);
	public void partCrash(Circumstances cs);
	public void createGeometry(Geometry g, Spatial s);
}