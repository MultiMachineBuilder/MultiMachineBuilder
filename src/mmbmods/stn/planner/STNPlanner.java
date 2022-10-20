/**
 * 
 */
package mmbmods.stn.planner;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import mmb.world.crafting.RecipeOutput;
import mmb.world.crafting.SimpleItemList;
import mmb.world.inventory.ItemStack;
import mmb.world.items.ItemEntry;
import mmbmods.stn.network.DataLayerSTN;
import mmbmods.stn.planner.RecipeNode.NodeSpec;

/**
 * A class used to plan STN crafting jobs.
 * @author oskar
 */
public class STNPlanner {
	@Nonnull public final DataLayerSTN stn;
	public STNPlanner(DataLayerSTN stn) {
		this.stn = stn;
	}
	
	//Auxiliary variables
	//@Nonnull private final Multimap<ItemEntry, >;
	//@Nonnull private final Set<@Nonnull Sink> sinks = new HashSet<>();
	
	/**
	 * Plans the processing sequence of the items before commencing the process.
	 * 
	 * The item flow must form an Acyclic Directed Graph
	 * 
	 * Planning overview:
	 * <ol>
	 * 	<li>Populate the process plan with head nodes for planned outputs</li>
	 *  <li>Look for valid sources in order of: exhaustion queue, inventory, procurement, crafting, processing. If no valid option is found, report inventory access</li>
	 * </ol>
	 * @param items
	 * @return a crafting plan
	 */
	public void plan(RecipeOutput items) {
		//Results	
		Object2IntOpenHashMap<@Nonnull ItemEntry> invProcurements = new Object2IntOpenHashMap<>();
		Set<@Nonnull Procurement> procurements = new HashSet<>();
		
		//The planning queue
		Object2IntOpenHashMap<@Nonnull ItemEntry> planQueue = new Object2IntOpenHashMap<>();
		Object2IntOpenHashMap<@Nonnull ItemEntry> exhQueue = new Object2IntOpenHashMap<>();
		Object2IntOpenHashMap<@Nonnull ItemEntry> itemsRemainingInInv = new Object2IntOpenHashMap<>(new SimpleItemList(stn.inv).getContents());
		
		//Populate the planning queue
		planQueue.putAll(items.getContents());
		
		//Run the planning
		while(!planQueue.isEmpty()) {
			//new set of planned items
			Object2IntOpenHashMap<@Nonnull ItemEntry> newQueue = new Object2IntOpenHashMap<>();
			inner: for(Entry<@Nonnull ItemEntry> plannedStack: planQueue.object2IntEntrySet()) {
				ItemEntry planned = plannedStack.getKey();
				int remaining = plannedStack.getIntValue();
				//Option A: Exhaustion queue
				int exhaustable = exhQueue.getInt(planned);
				if(exhaustable > remaining) {
					//Option A1: the exhaustion queue provides more than enough items required
					exhQueue.addTo(planned, -remaining);
					continue inner;
				}else if(exhaustable == remaining){
					//Option A2: the exhaustion queue provides exactly all items required
					exhQueue.removeInt(planned);
					continue inner;
				}else if(exhaustable > 0) {
					//Option A3: the exhaustion queue provides some items required
					remaining -= exhaustable;
					exhQueue.removeInt(planned);
				}
				
				//Option B: Inventory
				int amountStored = itemsRemainingInInv.getInt(planned);
				if(amountStored > remaining) {
					//Option B1: the inventory provides more than enough or exactly all items required
					itemsRemainingInInv.addTo(planned, -remaining);
					invProcurements.addTo(planned, remaining);
					continue inner;
				}else if(amountStored == remaining) {
					//Option B2: the inventory provides all items required
					invProcurements.addTo(planned, amountStored);
					itemsRemainingInInv.removeInt(planned);
					continue inner;
				}else if(amountStored > 0){
					//Option B3: the inventory provides some items required
					invProcurements.addTo(planned, amountStored);
					itemsRemainingInInv.removeInt(planned);
				}
				
				//Option C: Procurement
				//Option D: Crafting
				//Option E: Processing
				//Else: Missing items
			}
			planQueue = newQueue;
		}
		
		
		//Connect exhausttion queue
		
	}
	
}
