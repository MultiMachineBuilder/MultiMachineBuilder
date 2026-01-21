package mmb.string;

import java.util.List;

import mmb.data.variables.DataValueInt;
import mmb.engine.settings.GlobalSettings;

public final class UnitFormatters {
	private UnitFormatters() {}
	
	//Binary multiples
	public static final double Ki = 1024;
	public static final double Mi = Ki * Ki;
	public static final double Gi = Mi * Ki;
	public static final double Ti = Gi * Ki;
	public static final double Pi = Ti * Ki;
	public static final double Ei = Pi * Ki;
	public static final double Zi = Ei * Ki;
	public static final double Yi = Zi * Ki;
	public static final double Ri = Yi * Ki;
	public static final double Qi = Ri * Ki;
	
	/** Decimal prefixes */
	public static final List<UnitPrefix> SI = List.of(
		new UnitPrefix("q", 1e-30),
		new UnitPrefix("r", 1e-27),
	    new UnitPrefix("y", 1e-24),
	    new UnitPrefix("z", 1e-21),
	    new UnitPrefix("a", 1e-18),
	    new UnitPrefix("f", 1e-15),
	    new UnitPrefix("p", 1e-12),
	    new UnitPrefix("n", 1e-9),
	    new UnitPrefix("µ", 1e-6),
	    new UnitPrefix("m", 1e-3),
	    new UnitPrefix("",  1),
	    new UnitPrefix("k", 1e3),
	    new UnitPrefix("M", 1e6),
	    new UnitPrefix("G", 1e9),
	    new UnitPrefix("T", 1e12),
	    new UnitPrefix("P", 1e15),
	    new UnitPrefix("E", 1e18),
	    new UnitPrefix("Z", 1e21),
	    new UnitPrefix("Y", 1e24),
	    new UnitPrefix("R", 1e27),
	    new UnitPrefix("Q", 1e30)
	);
	public static final List<UnitPrefix> BINARY = List.of(
	    new UnitPrefix("",  1),
	    new UnitPrefix("Ki", Ki),
	    new UnitPrefix("Mi", Mi),
	    new UnitPrefix("Gi", Gi),
	    new UnitPrefix("Ti", Ti),
	    new UnitPrefix("Pi", Pi),
	    new UnitPrefix("Ei", Ei),
	    new UnitPrefix("Zi", Zi),
	    new UnitPrefix("Yi", Yi),
	    new UnitPrefix("Ri", Ri),
	    new UnitPrefix("Qi", Qi)
	);
	
	public static final ScaledUnitFormatter storageVolumeFormatter = createBinaryFormatter(
		new UnitDescriptor("slice", "sl", 1/Ki),
		new UnitDescriptor("cylinder", "Cy", 1),
		GlobalSettings.storageRoundingAccuracy
	);
	public static final ScaledUnitFormatter fluidVolumeFormatter = createBinaryFormatter(
		new UnitDescriptor("liter", "l", 1/Ki),
		new UnitDescriptor("cubit", "cu", 1),
		GlobalSettings.fluidRoundingAccuracy
	);
	public static final ScaledUnitFormatter dataVolumeFormatter = createBinaryFormatter(
		new UnitDescriptor("bit", "b", 0.125),
		new UnitDescriptor("byte", "B", 1),
		GlobalSettings.dataRoundingAccuracy
	);
	public static final ScaledUnitFormatter timeFormatter = new ScaledUnitFormatter(
		new UnitDescriptor("second", "s", 50),
		List.of(
			new UnitStep("tick", "t", 1),
			new UnitStep("second", "s", 50),
			new UnitStep("minute", "min", 3000),
			new UnitStep("hour", "h", 180000)
		), GlobalSettings.timeRoundingAccuracy
	);
	public static final ScaledUnitFormatter quantityDecimalFormatter = createDecimalFormatter(
		new UnitDescriptor("unit", "", 1),
		GlobalSettings.quantityRoundingAccuracy
	);
	
	public static final ScaledUnitFormatter energyFormatter = createDecimalFormatter(
		new UnitDescriptor("joule", "j", 1),
		GlobalSettings.energyRoundingAccuracy
	);
	
	/**
	 * Creates a binary unit formatter
	 * @param smallerUnit the smaller unit, below 1
	 * @param largerUnit the larger unit, multiples 1 and above
	 * @param precision default rounding precision
	 * @return a new binary unit formatter
	 */
	public static ScaledUnitFormatter createBinaryFormatter(UnitDescriptor smallerUnit, UnitDescriptor largerUnit, DataValueInt precision) {
		UnitStep[] unitSteps = new UnitStep[BINARY.size() + 1];
		unitSteps[0] = new UnitStep(smallerUnit.baseName(), smallerUnit.baseSymbol(), smallerUnit.baseScale());
		unitSteps[1] = new UnitStep(largerUnit.baseName(), largerUnit.baseSymbol(), largerUnit.baseScale());
		for(int i = 1; i < BINARY.size(); i++) {
			UnitPrefix prefix = BINARY.get(i);
			String concatenatedUnitName = prefix.symbol() + largerUnit.baseSymbol();
			unitSteps[i+1] = new UnitStep(concatenatedUnitName, concatenatedUnitName, prefix.multiplier() / largerUnit.baseScale());
		}
		return new ScaledUnitFormatter(largerUnit, List.of(unitSteps), precision);
	}
	
	public static ScaledUnitFormatter createDecimalFormatter(UnitDescriptor unit, DataValueInt precision) {
		UnitStep[] unitSteps = new UnitStep[SI.size()];
		for(int i = 0; i < SI.size(); i++) {
			UnitPrefix prefix = SI.get(i);
			String concatenatedUnitName = prefix.symbol() + unit.baseSymbol();
			unitSteps[i] = new UnitStep(concatenatedUnitName, concatenatedUnitName, prefix.multiplier() / unit.baseScale());
		}
		return new ScaledUnitFormatter(unit, List.of(unitSteps), precision);
	}
}
