package mmb.world.parts.engine;

import mmb.world.parts.utils.NumericalCurve;

public class EnginePresetAtmo {
	public String name;
	public NumericalCurve curve;
	public EnginePresetAtmo(String name, NumericalCurve curve) {
		super();
		this.name = name;
		this.curve = curve;
	}
}
