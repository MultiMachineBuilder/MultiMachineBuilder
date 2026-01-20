package mmb.engine.recipe3;

import java.util.*;
import java.util.function.Function;

import monniasza.collects.Collects;

/**
 * Defines a recipe
 * @param inputs Required input slots. Must not be null or empty and must not contain null.
 * @param prohibited prohibited items. Must not contain Group.ANY, Group.NONE or null and must be non-null
 * @param outputsRaw List of recipe outputs. Must not be null or empty
 * @param outputFunction calculated the final outputs. Must be repeatable and not null.
 * @param shapeless is the recipe shapeless?
 * @param minVoltage minimum voltage tier to run recipe at. Must be <=maxVoltage and not null
 * @param maxVoltage maximum voltage tier to run recipe at. Must be >=minVoltage and not null
 * @param energyIn input energy expenditure over the course of the run in joules. Must be nonnegative.
 * @param energyOut energy awarded at the end of a run in joules. Must be nonnegative.
 */
public record RecipeSpec(
	List<RecipeInput> inputs,
	Set<Group> prohibited,
	RecipeOutput outputsRaw,
	Function<RecipeRunContext, RecipeOutput> outputFunction,
	boolean shapeless,
	VoltageTier minVoltage,
	VoltageTier maxVoltage,
	double energyIn,
	double energyOut
) {
	/**
	 * Creates a recipe specification
	 * @param inputs Required input slots. Must not be null or empty and must not contain null.
	 * @param prohibited prohibited items. Must not contain Group.ANY, Group.NONE or null and must be non-null
	 * @param outputsRaw List of recipe outputs. Must not be null or empty
	 * @param outputFunction calculated the final outputs. Must be repeatable and not null.
	 * @param shapeless is the recipe shapeless?
	 * @param minVoltage minimum voltage tier to run recipe at. Must be <=maxVoltage and not null
	 * @param maxVoltage maximum voltage tier to run recipe at. Must be >=minVoltage and not null
	 * @param energyIn input energy expenditure over the course of the run in joules. Must be nonnegative.
	 * @param energyOut energy awarded at the end of a run in joules. Must be nonnegative.
	 * @throws NullPointerException if inputs is {@code null}
	 * @throws NoSuchElementException if inputs is empty
	 * @throws BadElementException if inputs contains {@code null}
	 * @throws NullPointerException if prohibited is {@code null}
	 * @throws BadElementException if prohibited contains {@link Group#ANY}
	 * @throws BadElementException if prohibited contains {@link Group#NONE}
	 * @throws BadElementException if prohibited contains {@code null}
	 * @throws NullPointerException if outputsRaw is {@code null}
	 * @throws NullPointerException if outputFunction is {@code null}
	 * @throws NullPointerException if minVoltage is {@code null}
	 * @throws NullPointerException if maxVoltage is {@code null}
	 * @throws IllegalArgumentException if minVoltage is greater than maxVoltage
	 * @throws IllegalArgumentException if energyIn is negative
	 * @throws IllegalArgumentException if energyOut is negative
	 */
	public RecipeSpec {
		Collects.requireContents(inputs, "inputs");
		Objects.requireNonNull(prohibited, "prohibited is null");
		if(prohibited.contains(Group.ANY)) throw new BadElementException("prohibited contains ANY");
		if(prohibited.contains(Group.NONE)) throw new BadElementException("prohibited contains NONE");
		Collects.rejectNullElements(prohibited, "prohibited");
		Objects.requireNonNull(outputsRaw, "outputsRaw is null");
		Objects.requireNonNull(outputFunction, "outputFunction is null");
		Objects.requireNonNull(minVoltage, "minVoltage is null");
		Objects.requireNonNull(maxVoltage, "maxVoltage is null");
		if(minVoltage.ordinal > maxVoltage.ordinal) throw new IllegalArgumentException("minVoltage > maxVoltage");
		if(energyIn < 0) throw new IllegalArgumentException("energyIn < 0");
		if(energyOut < 0) throw new IllegalArgumentException("energyOut < 0");
	}
}
