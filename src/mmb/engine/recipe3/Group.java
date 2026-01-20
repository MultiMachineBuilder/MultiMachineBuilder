package mmb.engine.recipe3;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import mmb.annotations.NN;

/** 
 * An item group. Used to define a set of items that can be substituted for each other in a recipe.
 */
public class Group {
	private static int NEXT_ORDINAL = 0;
	/** Group ordinal. Used for optimizations */
	public final int ordinal;
	/** The group ID used in {@link #of(String)} to create this group */
	public final String id;
	private Group(String id) {
		this.ordinal = NEXT_ORDINAL++;
		this.id = id;
		internalGroups.add(this);
	}
	private static final List<@NN Group> internalGroups = new ArrayList<>();
	/** Finds existing groups by their ordinals */
	public static final List<@NN Group> groups = Collections.unmodifiableList(internalGroups);
	private static final Map<@NN String, @NN Group> internalLookup = new ConcurrentHashMap<>();
	/** Finds existing groups by their IDs */
	public static final Map<@NN String, @NN Group> lookupExisting = Collections.unmodifiableMap(internalLookup);
	
	/**
	 * Gets or creates an item group
	 * @param id item group id
	 * @return an item group with given id
	 */
	public static Group of(String id) {
		Objects.requireNonNull(id, "id is null");
		return internalLookup.computeIfAbsent(id, x -> new Group(x));
	}
	
	/**
	 * Group reserved for slots that expect no items in it
	 * @implNote NOTE:
	 * Group.NONE and Group.ANY rely on deterministic static initialization order.
	 * Their ordinals are semantically meaningful and must not be reordered.
	 */
	public static final Group NONE = of("none");
	/**
	 * Matches any item
	 * @implNote NOTE:
	 * Group.NONE and Group.ANY rely on deterministic static initialization order.
	 * Their ordinals are semantically meaningful and must not be reordered.
	 */
	public static final Group ANY = of("any");
}
