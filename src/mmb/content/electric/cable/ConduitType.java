package mmb.content.electric.cable;

import java.util.Objects;

import mmb.PropertyExtension;
import mmb.content.electric.VoltageTier;
import mmb.engine.Verify;
import mmb.engine.block.BlockType;

public class ConduitType extends BlockType {
	public ConduitType(String id, PropertyExtension... properties) {
		super(id, properties);
		// TODO Auto-generated constructor stub
	}
	
	private VoltageTier voltRating = VoltageTier.V1;
	private double powerRating = 0;
	public VoltageTier getVoltRating() {
		return voltRating;
	}
	public void setVoltRating(VoltageTier voltRating) {
		Objects.requireNonNull(voltRating, "voltRating is null");
		this.voltRating = voltRating;
	}
	public double getPowerRating() {
		return powerRating;
	}
	public void setPowerRating(double powerRating) {
		Verify.requireNonNegative(powerRating);
		this.powerRating = powerRating;
	}
	
	
	public static PropertyExtension withVoltage(VoltageTier voltage) {
		return (obj) -> ((ConduitType)obj).setVoltRating(voltage);
	}
	public static PropertyExtension withPowerRating(double watts) {
		return (obj) -> ((ConduitType)obj).setPowerRating(watts);
	}
	
}	
