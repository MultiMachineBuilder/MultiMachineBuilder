package mmb.parts.engine;

import java.util.*;

import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;

import mmb.parts.Part;
import mmb.parts.modules.PartModule;
import mmb.parts.utils.NumericalCurve;
import mmb.world2.Circumstances;

public class ProceduralEngine implements PartModule {
	static List<EnginePresetAtmo> optimizeAtmo = new ArrayList<EnginePresetAtmo>();
	public static void addAtmoProfile(NumericalCurve curve, String name) {
		optimizeAtmo.add(new EnginePresetAtmo(name, curve));
	}
	
	
	public ArrayList<EngineNozzle> engines = new ArrayList<EngineNozzle>();
	@Override
	public void build() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void flight(Circumstances cs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partCreation(Part p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partCrash(Circumstances cs) {
		// TODO Auto-generated method stub
		
	}
	
	
	

	@Override
	public void createGeometry(Geometry g, Spatial s) {
		// TODO Auto-generated method stub
		
	}

	

}
