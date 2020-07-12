package mmb.world.parts.modules;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;

import mmb.world.Circumstances;
import mmb.world.parts.Part;
import mmb.world.parts.PartSpec;
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