package mmb.string;

/** The unit prefix */
public record UnitPrefix(
		/** The symbol placed before the unit eg. a billionth of a unit is prefixed by 'n'*/
        String symbol,
        /** How much the unit is changed. Powers of 1000 for decimal, positive powers of 1024 for binary multiples*/
        double multiplier
) {}

