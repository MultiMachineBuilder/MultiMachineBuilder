package mmb.string;

import java.util.*;
import java.util.stream.Collectors;

import mmb.data.variables.DataValueInt;

/**
 * Formats quantities in its base unit into dynamically chosen unit steps. Example:
 * <ul>
 * 	<li>1 -> 1 t</li>
 *  <li>50 -> 1 s</li>
 *  <li>3000 -> 1 min</li>
 *  <li>180000 -> 1 h</li>
 * </ul>
 */
public final class ScaledUnitFormatter {

	/** The base unit. Does not include the smaller unit if binary. */
    public final UnitDescriptor unit;
    /** All unit steps, like (L, cu, Kicu, Micu....) or (...mW, W, kW, MW...)*/
    public final List<UnitStep> steps; // sorted by multiplier
    /** The default precision setting. It is a variable so changes in its value will change precision of formatted strings. */
    public final DataValueInt defaultPrecision;

    /**
     * Creates a new DecimalUnitFormatter
     * @param unit The base unit
     * @param steps Set of scaled units
     * @param defaultPrecision Variable for the default precision setting
     */
    public ScaledUnitFormatter(
            UnitDescriptor unit,
            Collection<UnitStep> steps,
            DataValueInt defaultPrecision
    ) {
        this.unit = unit;
        this.steps = steps.stream().sorted(Comparator.comparingDouble(x -> x.sizeInBase())).collect(Collectors.toUnmodifiableList());
        this.defaultPrecision = defaultPrecision;
    }

    /** Formats a value in base units with the default precision */
    public String format(double value) {
        return format(value, defaultPrecision.getInt());
    }
    
    /** Formats a value in base units with given precision */
    public String format(double value, int precision) {
        if (Double.isNaN(value)) return "NaN";
        if (Double.isInfinite(value)) return value > 0 ? "∞" : "-∞";

        UnitStep best = steps.get(0);
        for (UnitStep p : steps) {
            if (Math.abs(value) >= p.sizeInBase()) {
                best = p;
            } else break;
        }

        double shown = value / best.sizeInBase();
        return formatNumber(shown, precision)
                + " "
                + best.symbol();
    }

    /** Formats a floating-point number with given number of decimal places */
    public static String formatNumber(double v, int precision) {
        return String.format("%." + precision + "f", v);
    }
}

