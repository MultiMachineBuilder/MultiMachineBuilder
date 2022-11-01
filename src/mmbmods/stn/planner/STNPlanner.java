/**
 * 
 */
package mmbmods.stn.planner;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Iterators;

import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import mmb.world.crafting.RecipeOutput;
import mmb.world.items.ItemEntry;
import mmb.world.items.data.Stencil;
import mmbmods.stn.network.DataLayerSTN;
import mmbmods.stn.network.STNNetworkProcessing.STNRGroupTag.STNPRecipe;

/**
 * A class used to plan STN crafting jobs.
 * 
 * Plans the processing sequence of the items before commencing the process.
 * 
 * The item flow must form an Acyclic Directed Graph
 * 
 * The planner uses a cyclic planner
 * 
 * 
 * 
 * <br> Planning phases:
 * <ol>
 * 	<li>Calculate required withdrawals, procurements, crafts and processes.
 * 		The method will fail with {@link ArithmeticException} if there are growing cycles in the recipe graph which involve planned items</li>
 *  <li>Arrange the flow of process items. This step will fail if there are circular references in the item flow.</li>
 *  <li>Assign the steps to the machines</li>
 * </ol>
 * Run phases:
 * <ol>
 * 	<li>Take required items from the network 
 *  (this will fail if any required items were extracted between phase 1 and now,
 *  or there is not enough space in the crafting helper)</li>
 *  <li>Start the machines</li>
 *  <li>Run until all processes are finished:
 *  	<ol>
 *  		<li>Deliver items from a recipe to a crafter or machine if it is a crafting or processing operation</li>
 *  		<li>Receive items from a recipe, factory, or crafter and count arrivals</li>
 *  	</ol>
 *  </li>
 *  <li>Deliver crafted items to the network, or the user</li>
 * </ol>
 * 
 * Planning overview:
 * <ol>
 * 	<li>Populate the process plan with head nodes for planned outputs</li>
 *  <li>Look for valid sources in order of: exhaustion queue, inventory, procurement, crafting, processing.
 *  If no valid option is found, report inventory access</li>
 * </ol>
 * 
 * @author oskar
 */
public class STNPlanner {
	@Nonnull public final DataLayerSTN stn;
	public STNPlanner(DataLayerSTN stn) {
		this.stn = stn;
	}
	
	//Phase 1
	/**
	 * Phase 1 planning report
	 * @author oskar
	 */
	public static class Phase1{
		/** Items which are going to be withdrawn */
		@Nonnull public final Object2IntOpenHashMap<@Nonnull ItemEntry> itemsWithdrawn;
		/** Used processing recipes */
		@Nonnull public final Object2IntOpenHashMap<@Nonnull STNPRecipe> processes;
		/** Used crafts */
		@Nonnull public final Object2IntOpenHashMap<@Nonnull Stencil> crafts;
		/** Used procurements */
		@Nonnull public final Object2IntOpenHashMap<@Nonnull ItemEntry> procurements;
		/** Missing items */
		@Nonnull public final Object2IntOpenHashMap<@Nonnull ItemEntry> missing;
		/**
		 * Creates a finished Phase 1 plan
		 * @param itemsWithdrawn
		 * @param processes used processing recipes
		 * @param crafts used crafts
		 * @param procurements used procurements
		 * @param missing missing items
		 */
		public Phase1(Object2IntOpenHashMap<@Nonnull ItemEntry> itemsWithdrawn,
				Object2IntOpenHashMap<@Nonnull STNPRecipe> processes, Object2IntOpenHashMap<@Nonnull Stencil> crafts,
				Object2IntOpenHashMap<@Nonnull ItemEntry> procurements, Object2IntOpenHashMap<@Nonnull ItemEntry> missing) {
			this.itemsWithdrawn = itemsWithdrawn;
			this.processes = processes;
			this.crafts = crafts;
			this.procurements = procurements;
			this.missing = missing;
		}
	}
	
	/**
	 * 1st phase of planning: calculate items extracted, procured, crafted, processed and missing 
	 * 
	 * @param items
	 * @return a crafting plan
	 * @throws ArithmeticException if there are growing cycles which involve planned items
	 * @throws IllegalStateException if there are additional required items (if planning logic fails)
	 */
	public Phase1 plan1(RecipeOutput items) {
		//Results
		Object2IntOpenHashMap<@Nonnull STNPRecipe> processRecipes = new Object2IntOpenHashMap<>();
		Object2IntOpenHashMap<@Nonnull Stencil> craftRecipes = new Object2IntOpenHashMap<>();
		Object2IntOpenHashMap<@Nonnull ItemEntry> procurements = new Object2IntOpenHashMap<>();
		
		//The planning queue (negative values mean that there are excess items
		Queue<ItemEntry> queue = new ArrayDeque<>();
		Object2IntOpenHashMap<@Nonnull ItemEntry> planMap = new Object2IntOpenHashMap<>();
		Object2IntOpenHashMap<@Nonnull ItemEntry> invRemain = new Object2IntOpenHashMap<>();
		Object2IntOpenHashMap<@Nonnull ItemEntry> missing = new Object2IntOpenHashMap<>();
		
		//Populate the planning queue
		planMap.putAll(items.getContents());
		queue.addAll(items.items());
		stn.inv.contents(invRemain);
		
		//Run the planning
		ItemEntry entry;
		outer: while((entry = queue.poll()) != null) {
			int plannedAmount = planMap.getInt(entry);
			if(plannedAmount <= 0) continue; //All items are planned for now
			
			//Option A: Inventory
			int itemsInInv = invRemain.getInt(entry);
			if(itemsInInv >= plannedAmount) {
				//Option A1: all planned items are stored
				invRemain.addTo(entry, -plannedAmount);
			}else if(itemsInInv > 0) {
				//Option A2: some planned items are stored
				plannedAmount -= itemsInInv;
				invRemain.put(entry, 0);
			}else if(itemsInInv < 0) {
				//Error in planning logic
				throw new IllegalStateException("Counted negative items in inventory: "+itemsInInv+" for: "+entry);
			}
			
			//TODO Option B: Procurement
			
			
			//Option C: Crafting
			Set<Stencil> stencils = stn.processor.stencil2OutIndex.multimap().get(entry);
			Stencil stencil = findPlausibleRecipe(planMap, invRemain, stencils, s -> s.in().items());
			//If no recipes are plausible, no recipe will be usable
			if(stencil != null) {
				//If a recipe is plausible, plan it
				Set<ItemEntry> toPlan = planItems(stencil.in(), stencil.out(), entry, plannedAmount, planMap, stencil, craftRecipes, queue);
				queue.addAll(toPlan);
				continue;
			}
			
			//Option D: Processing
			Set<STNPRecipe> processables = stn.processor.processRecipe2OutIndex.multimap().get(entry);
			STNPRecipe processRecipe = findPlausibleRecipe(planMap, invRemain, processables, pr -> pr.in.items());
			//If no recipes are plausible, no recipe will be usable
			if(processRecipe != null) {
				//If a recipe is plausible, plan it
				Set<ItemEntry> toPlan = planItems(processRecipe.in, processRecipe.out, entry, plannedAmount, planMap, processRecipe, processRecipes, queue);
				queue.addAll(toPlan);
				continue;
			}
			
			//Else: Missing items
			missing.addTo(entry, plannedAmount);
		}
		
		Object2IntOpenHashMap<@Nonnull ItemEntry> putback = new Object2IntOpenHashMap<>();
		//Check for stray items
		for(Entry<@Nonnull ItemEntry> stack: planMap.object2IntEntrySet()) {
			int amount = -stack.getIntValue();
			if(amount < 0) throw new IllegalStateException("Unnacounted-for items in the plans: "+(-amount)+" x "+stack.getKey());
			putback.put(stack.getKey(), amount);
		}
		
		//Calculate extractions
		Object2IntOpenHashMap<@Nonnull ItemEntry> itemsInInv = new Object2IntOpenHashMap<>();
		Object2IntOpenHashMap<@Nonnull ItemEntry> itemsToWithdraw = new Object2IntOpenHashMap<>();
		stn.inv.contents(itemsInInv);
		for(Entry<@Nonnull ItemEntry> itemCompare: itemsInInv.object2IntEntrySet()) {
			ItemEntry item = itemCompare.getKey();
			int remaining = invRemain.getInt(item);
			int stored = itemCompare.getIntValue();
			itemsToWithdraw.put(item, stored-remaining);
		}
		
		//FINISH
		return new Phase1(itemsToWithdraw, processRecipes, craftRecipes, procurements, missing);
	}
	/**
	 * Internal helper method for Phase 1
	 * @param <T> type of recipes
	 * @param planMap planning map
	 * @param invRemain
	 * @param possible all potential recipes
	 * @param transformer obtains input items for the recipe
	 * @return a iterator of plausible recipes, or null if not found
	 */
	private @Nullable <T> @Nullable T findPlausibleRecipe(Object2IntOpenHashMap<@Nonnull ItemEntry> planMap,
			Object2IntOpenHashMap<@Nonnull ItemEntry> invRemain, @Nullable Set<T> possible, Function<? super T, @Nonnull Set<ItemEntry>> transformer) {
		if(possible == null) return null;
		for(T recipe: possible) {
			Set<ItemEntry> inputs = transformer.apply(recipe);
			if(stn.processor.isAllObtainable(inputs, invRemain, planMap))
				return recipe;
		}
		return null;
	}
	private static <T> Set<@Nonnull ItemEntry> planItems(RecipeOutput inputs, RecipeOutput outputs, ItemEntry plannedItem, int plannedAmount,
			Object2IntOpenHashMap<@Nonnull ItemEntry> planMap, T recipe, Object2IntOpenHashMap<@Nonnull T> recipesCounter, Queue<ItemEntry> queue) {
		int unitOutputQuantity = outputs.get(plannedItem);
		if(unitOutputQuantity <= 0) throw new InternalError("No such item: "+plannedItem);
		double recipeQuantity0 = (double)plannedAmount / unitOutputQuantity;
		int recipeQuantity = (int) Math.ceil(recipeQuantity0);
		recipesCounter.addTo(recipe, recipeQuantity);
		
		//Add the inputs to the plans and queue
		Object2IntOpenHashMap<@Nonnull ItemEntry> totalInputs = inputs.mul2map(recipeQuantity, Object2IntOpenHashMap::new);
		for(Entry<@Nonnull ItemEntry> input: totalInputs.object2IntEntrySet()) {
			planMap.addTo(input.getKey(), Math.multiplyExact(input.getIntValue(), recipeQuantity));
			queue.add(input.getKey());
		}
		
		//Add the outputs to the plans
		Object2IntOpenHashMap<@Nonnull ItemEntry> totalOutputs = outputs.mul2map(recipeQuantity, Object2IntOpenHashMap::new);
		for(Entry<@Nonnull ItemEntry> output: totalOutputs.object2IntEntrySet()) 
			planMap.addTo(output.getKey(), Math.multiplyExact(-output.getIntValue(), recipeQuantity));
		
		return totalInputs.keySet();
	}
}
