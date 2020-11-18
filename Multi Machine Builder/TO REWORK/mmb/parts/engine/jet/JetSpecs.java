package mmb.parts.engine.jet;

import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;

import mmb.material.FuelRate;
import mmb.parts.Part;
import mmb.parts.modules.PartModule;
import mmb.parts.utils.NumericalCurve;
import mmb.world2.Circumstances;
import mmb.world2.vessels.VesselFreeform;

public class JetSpecs {
	public NumericalCurve thrustByPressureMult;
	public NumericalCurve thrustBySpeedMult;
	public double multStandstillSuction = 0;
	public double baseSpeed = 0;
	public boolean requireOxygen = false;
	public FuelRate[] mr;
	public JetSpecs() {
		// TODO Auto-generated constructor stub
	}

	public JetEngineActionReport apply(Circumstances cst) {
		double asp = cst.airspeed;
		double speed = asp + baseSpeed;
		double pressure = cst.density * speed * speed + multStandstillSuction * cst.pressure;
		double pwr = cst.cp.getPower();
		VesselFreeform v = cst.v;
		
	}
	
	private double calcThrust() {
		
	}

}
