package mmb.engine.recipe3;

import java.util.List;
import java.util.function.Function;

public record Recipe(
	List<RecipeInput> inputs,
	RecipeOutput outputsRaw,
	Function<RecipeRunContext, RecipeOutput> outputFunction,
	boolean shapeless,
	VoltageTier minVoltage,
	VoltageTier maxVoltage,
	double energyIn,
	double energyOut
) {}
