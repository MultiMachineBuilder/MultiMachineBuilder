package mmb.control;

import e3d.d3.Rotation3;
import e3d.d3.Vector3;

public interface ControlPoint {
	public double getPower();
	
	public double yaw();
	public double roll();
	public double pitch();
	
	public double x();
	public double y();
	public double z();
	
	default public Rotation3 getRotationInput() {
		return new Rotation3(pitch(), yaw(), roll());
	}
	default public Vector3 getOffsetInput() {
		return new Vector3(x(), y(), z());
	}
}
