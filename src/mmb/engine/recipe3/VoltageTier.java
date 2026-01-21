package mmb.engine.recipe3;

import java.awt.Color;
import java.util.*;

import mmb.AlreadyExistsException;
import mmb.annotations.Nil;
import mmb.engine.settings.GlobalSettings;

public final class VoltageTier implements Comparable<VoltageTier>{
	public final int ordinal;
	public final double volts;
	public final String id;
	public final String name;
	public final Color color;
	
	/**
	 * Creates a new representation of a voltage tier.
	 * @param ordinal the voltage index
	 * @param id short lowercase voltage identifier
	 * @param name descriptive name for the user
	 * @throws NullPointerException when {@code id} or {@code name} is null
	 */
	public VoltageTier(int ordinal, String id, String name) {
		Objects.requireNonNull(id, "id is null");
		Objects.requireNonNull(name, "name is null");
		this.ordinal = ordinal;
		this.name = GlobalSettings.$str1(name);
		this.id=id;
		this.volts=25 * Math.pow(4, ordinal);
		this.color = new Color(192, 192, 192, 255);
	}
	@Override
	public int compareTo(VoltageTier o) {
		return Integer.compare(this.ordinal, o.ordinal);
	}
	@Override
	public int hashCode() {
		return ordinal;
	}
	@Override
	public boolean equals(@Nil Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VoltageTier other = (VoltageTier) obj;
		return ordinal == other.ordinal;
	}
	@Override
	public String toString() {
		return "VoltageTier [ordinal=" + ordinal + ", volts=" + volts + ", id=" + id + ", name=" + name + "]";
	}
	
	private static final Map<String, VoltageTier> internalStringLookup = new HashMap<>();
	private static final NavigableMap<Integer, VoltageTier> internalOrdinalLookup = new TreeMap<>();
	/** String ID to voltage tier lookup*/
	public static final Map<String, VoltageTier> stringLookup = Collections.unmodifiableMap(internalStringLookup);
	/** Ordinal to voltage tier lookup*/
	public static final NavigableMap<Integer, VoltageTier> ordinalLookup = Collections.unmodifiableNavigableMap(internalOrdinalLookup);
	/**
	 * Adds a voltage tier
	 * @param voltage new voltage tier
	 * @return voltage tier object
	 * @throws NullPointerException when {@code voltage} is null
	 * @throws AlreadyExistsException when a voltage with the same ordinal or short ID exists
	 */
	public static VoltageTier addVoltageTier(VoltageTier voltage) {
		Objects.requireNonNull(voltage, "voltage is null");
		if(stringLookup.containsKey(voltage.id)) throw new AlreadyExistsException("A voltage of id " + voltage.id + " already exists");
		if(ordinalLookup.containsKey(voltage.ordinal)) throw new AlreadyExistsException("A voltage of ordinal " + voltage.ordinal + " already exists");
		internalStringLookup.put(voltage.id, voltage);
		internalOrdinalLookup.put(voltage.ordinal, voltage);
		return voltage;
	}
	
	//Special values
	public static final VoltageTier MIN = addVoltageTier(new VoltageTier(Integer.MIN_VALUE, "MIN", "Minimum Voltage"));
	public static final VoltageTier MAX = addVoltageTier(new VoltageTier(Integer.MAX_VALUE, "MAX", "Maximum Voltage"));
}
