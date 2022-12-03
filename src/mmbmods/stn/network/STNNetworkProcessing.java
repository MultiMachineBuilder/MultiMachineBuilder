/**
 * 
 */
package mmbmods.stn.network;

import java.util.Collections;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pploder.events.Event;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import mmb.CatchingEvent;
import mmb.beans.Saver;
import mmb.data.json.JsonTool;
import mmb.debug.Debugger;
import mmb.world.blocks.ContentsBlocks;
import mmb.world.crafting.ItemLists;
import mmb.world.crafting.RecipeOutput;
import mmb.world.inventory.basic.SetInventory;
import mmb.world.item.ItemEntry;
import mmb.world.items.data.Stencil;
import mmb.world.recipes.AgroRecipeGroup.AgroProcessingRecipe;
import mmb.world.recipes.CraftingRecipeGroup.CraftingRecipe;
import mmbmods.stn.block.STNBaseMachine;
import monniasza.collects.CollectionOps;
import monniasza.collects.Collects;
import monniasza.collects.Identifiable;
import monniasza.collects.indexar.Database;
import monniasza.collects.indexar.ManyToManyIndex;
import monniasza.collects.indexar.OneToOneIndex;
import monniasza.collects.selfset.HashSelfSet;
import monniasza.collects.selfset.SelfSet;

/**
 * The implementation of the Auto-Crafting component of the STN
 * TODO crafting
 * TODO procurement
 * @author oskar
 */
public class STNNetworkProcessing implements Saver{
	@Nonnull private final Debugger debug = new Debugger("STN crafting and procurement");
	
	//Processing recipe
	//recipe counter (to avoid confusion)
	int rgrouprcounter = 0;
	/**
	 * A recipe tag used by STN machines
	 * @author oskar
	 */
	public class STNRGroupTag implements Identifiable<String>, Saver{
		/** The icon shown in lists */
		@Nonnull public ItemEntry icon = ContentsBlocks.blockVoid;
		/** The name of the crafting group */
		@Nonnull public final String name;
		/**
		 * Creates a recipe tag
		 * @param icon the recipe tag's icon
		 * @param s name of the recipe tag
		 */
		STNRGroupTag(ItemEntry icon, String s) {
			this.icon = icon;
			this.name = s;
		}
		
		//Equality checks for data structures
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + name.hashCode();
			return result;
		}
		@Override
		public boolean equals(@Nullable Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			STNRGroupTag other = (STNRGroupTag) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			return name.equals(other.name);
		}
		private STNNetworkProcessing getEnclosingInstance() {
			return STNNetworkProcessing.this;
		}
		@Override
		public String id() {
			return name;
		}
				
		//Recipe indexing
		/**
		 * A processing recipe.
		 * @author oskar
		 */
		public class STNPRecipe{
			/** The ingredients required to craft the recipe*/
			@Nonnull public final RecipeOutput in;
			/** The results of this recipe */
			@Nonnull public final RecipeOutput out;
			/** The recipe counter (unique ID) */
			public final int count;
			
			STNPRecipe(RecipeOutput in, RecipeOutput out, int count){
				this.in = in;
				this.out = out;
				this.count = count;
			}

			@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				result = prime * result + getEnclosingInstance().hashCode();
				result = prime * result + (in.hashCode());
				result = prime * result + (out.hashCode());
				return result;
			}

			@Override
			public boolean equals(@Nullable Object obj) {
				if (this == obj)
					return true;
				if (obj == null)
					return false;
				if (getClass() != obj.getClass())
					return false;
				STNPRecipe other = (STNPRecipe) obj;
				if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
					return false;
				if (!in.equals(other.in))
					return false;
				if (!out.equals(other.out))
					return false;
				return true;
			}

			private STNRGroupTag getEnclosingInstance() {
				return STNRGroupTag.this;
			}
		}
		/** Index of recipes by input, for this recipe tag*/
		@Nonnull public final ManyToManyIndex<STNPRecipe, ItemEntry> inputIndex =
				new ManyToManyIndex<>(recipe -> recipe.in.items());
		/** Index of recipes by output, for this recipe tag*/
		@Nonnull public final ManyToManyIndex<STNPRecipe, ItemEntry> outputIndex =
				new ManyToManyIndex<>(recipe -> recipe.out.items());
		/** Index of recipes by input, for this recipe tag*/
		@Nonnull public final OneToOneIndex<STNPRecipe, Integer> countIndex =
				new OneToOneIndex<>(recipe -> Integer.valueOf(recipe.count));
		@Nonnull final Database<STNPRecipe> recipes0 =
				new Database<>(STNPRecipe.class)
				.addIndex(inputIndex)
				.addIndex(outputIndex);
		/** The index of recipes for this tag*/
		@Nonnull public final Set<STNPRecipe> recipes = Collections.unmodifiableSet(recipes0);
		
		//Recipe manipulation
		/**
		 * Produces a processing recipe
		 * @param in incoming items
		 * @param out outgoing items
		 * @return a new recipe
		 */
		public STNPRecipe produceRecipe(RecipeOutput in, RecipeOutput out) {
			return produceRecipe(in, out, rgrouprcounter++);
		}
		//like its smaller cousin, but takes an ID for (de)serialization purposes
		STNPRecipe produceRecipe(RecipeOutput in, RecipeOutput out, int count) {
			STNPRecipe recipe = new STNPRecipe(in, out, count);
			recipeProduced.trigger(recipe);
			precipeProduced.trigger(recipe);
			recipes0.add(recipe);
			return recipe;
		}
		
		void kill() {
			//Kill all recipes
			for(STNPRecipe recipe: recipes) {
				precipeKilled.trigger(recipe);
				recipeKilled.trigger(recipe);
			}
		}
		
		//Events
		/** Invoked when a processing recipe is produced */
		@Nonnull public final Event<STNRGroupTag.STNPRecipe> recipeProduced = new CatchingEvent<>(debug, "Failed to run a processing recipe produced event");
		/** Invoked when a processing recipe is killed */
		@Nonnull public final Event<STNRGroupTag.STNPRecipe> recipeKilled = new CatchingEvent<>(debug, "Failed to run a processing recipe killed event");
		
		@Override
		public @Nullable JsonNode save() {
			ObjectNode node = JsonTool.newObjectNode();
			
			//Save the icon
			JsonNode nodeIcon = ItemEntry.saveItem(icon);
			node.set("icon", nodeIcon);
			
			//Save recipes
			ObjectNode nodeRecipes = JsonTool.newObjectNode();
			for(STNPRecipe recipe: recipes) {
				int id = recipe.count;
				JsonNode nodeIn = ItemLists.save(recipe.in);
				JsonNode nodeOut = ItemLists.save(recipe.out);
				ArrayNode nodeArray = JsonTool.newArrayNode().add(nodeIn).add(nodeOut);
				nodeRecipes.set(Integer.toHexString(id), nodeArray);
			}
			node.set("recipes", nodeRecipes);
			
			return node;
		}

		@Override
		public void load(@Nullable JsonNode data) {
			if(data == null) return;
			
			//Load the icon
			JsonNode nodeIcon = data.get("icon");
			icon = ItemEntry.loadFromJson(nodeIcon);
			
			//Load recipes
			JsonNode nodeRecipes = data.get("recipes");
			for(Entry<String, JsonNode> recipe: Collects.iter(nodeRecipes.fields())) {
				String numberString = recipe.getKey();
				JsonNode nodeArray = recipe.getValue();
				
				JsonNode nodeIn = nodeArray.get(0);
				JsonNode nodeOut = nodeArray.get(1);
				
				RecipeOutput in = ItemLists.read(nodeIn);
				RecipeOutput out = ItemLists.read(nodeOut);
				int id = Integer.parseInt(numberString, 16);
				
				produceRecipe(in, out, id);
			}
		}
	}
	
	/**
	 * @param dataLayerSTN
	 */
	public STNNetworkProcessing(DataLayerSTN dataLayerSTN) {
		debug.id = "STN crafting and procurement @"+dataLayerSTN.id().getName();
	}

	//Crafting system
	void theProcessingMachineIsGone(STNBaseMachine m) {
		/*if(m instanceof STNProcessor) {
			
		}else if(m instanceof STNCrafter){
		
		}*/
	}
	
	//TODO procurer index
	
	
	//TODO crafter index
	
	//craft recipe index
	/** Index of stencil inputs */
	@Nonnull public final ManyToManyIndex<Stencil, ItemEntry> stencil2InIndex = new ManyToManyIndex<>(s -> s.in().items());
	/** Index of stencil outputs */
	@Nonnull public final ManyToManyIndex<Stencil, ItemEntry> stencil2OutIndex = new ManyToManyIndex<>(s -> s.out().items());
	@Nonnull private final Database<Stencil> stencils0 = new Database<>(Stencil.class).addIndex(stencil2OutIndex).addIndex(stencil2InIndex);
	/** 
	 * The inventory for stencils 
	 * @apiNote DO NOT BULK INSERT
	 */
	@Nonnull public final SetInventory<Stencil> stencilinv = new SetInventory<>(stencils0, Stencil.class).setCapacity(Double.POSITIVE_INFINITY);
	/** The set of stencils */
	@Nonnull public final Set<Stencil> stencils = Collections.unmodifiableSet(stencils0);
	
	//TODO processor index
	
	//process recipe index
	/** Index of recipes by input, for this world*/
	@Nonnull public final ManyToManyIndex<STNRGroupTag.STNPRecipe, ItemEntry> processRecipe2InIndex =
			new ManyToManyIndex<>(recipe -> recipe.in.items());
	/** Index of recipes by output, for this world*/
	@Nonnull public final ManyToManyIndex<STNRGroupTag.STNPRecipe, ItemEntry> processRecipe2OutIndex =
			new ManyToManyIndex<>(recipe -> recipe.out.items());
	/** Index of recipes by input, for this recipe tag*/
	@Nonnull public final OneToOneIndex<STNRGroupTag.STNPRecipe, Integer> processCountIndex =
			new OneToOneIndex<>(recipe -> Integer.valueOf(recipe.count));
	/** The full index of processing recipes */
	@Nonnull private final Database<STNRGroupTag.STNPRecipe> precipes0 =
			new Database<>(STNRGroupTag.STNPRecipe.class)
			.addIndex(processRecipe2InIndex)
			.addIndex(processRecipe2OutIndex)
			.addIndex(processCountIndex);
	/** The full index of recipes*/
	@Nonnull public final Set<STNRGroupTag.STNPRecipe> precipes = Collections.unmodifiableSet(precipes0);
	
	//Process tag index
	@Nonnull private final SelfSet<String, STNRGroupTag> processingTagsIndex0 = HashSelfSet.createNonnull(STNRGroupTag.class);
	/** The self-set of tags */
	@Nonnull public final SelfSet<String, STNRGroupTag> processingTagsIndex = Collects.unmodifiableSelfSet(processingTagsIndex0);
	/**
	 * Produces a new tag
	 * @param icon item to be used as an icon
	 * @param name name of the tag
	 * @return a new tag
	 */
	public STNRGroupTag newTag(ItemEntry icon, String name) {
		STNRGroupTag tag = new STNRGroupTag(icon, name);
		processingTagsIndex0.add(tag); //index this tag
		tagProduced.trigger(tag);
		return tag;
	}
	/**
	 * Kills the tag (removes everything related to it)
	 * @param tag
	 */
	public void killTag(STNRGroupTag tag) {
		//Eliminate recipes from DB
		Set<STNRGroupTag.STNPRecipe> recipes2go = tag.recipes;
		precipes0.removeAll(recipes2go);
		tag.kill();
		tagKilled.trigger(tag);
	}
	
	//Events
	/** Invoked when tag is produced */
	@Nonnull public final Event<STNRGroupTag> tagProduced = new CatchingEvent<>(debug, "Failed to run a tag produced event");
	/** Invoked when tag is killed */
	@Nonnull public final Event<STNRGroupTag> tagKilled = new CatchingEvent<>(debug, "Failed to run a tag killed event");
	/** Invoked when a processing recipe is produced */
	@Nonnull public final Event<STNRGroupTag.STNPRecipe> precipeProduced = new CatchingEvent<>(debug, "Failed to run a processing recipe produced event");
	/** Invoked when a processing recipe is killed */
	@Nonnull public final Event<STNRGroupTag.STNPRecipe> precipeKilled = new CatchingEvent<>(debug, "Failed to run a processing recipe killed event");
	
	//Serialization
	@Override
	public JsonNode save() {
		ObjectNode node = JsonTool.newObjectNode();
		
		//Save processing recipes
		ObjectNode procrNode = JsonTool.newObjectNode();
		for(STNRGroupTag tag: processingTagsIndex) {
			//Save each tag individually
			JsonNode savedTag = tag.save();
			String tagId = tag.id();
			procrNode.set(tagId, savedTag);
		}
		node.set("recipesProcessing", procrNode);
		
		//Save crafting recipes
		JsonNode craftrNode = stencilinv.save();
		node.set("recipesCrafting", craftrNode);
		
		//TODO Save procurers
		
		return node;
	}

	@Override
	public void load(@Nullable JsonNode data) {
		if(data == null) return;
		
		//Load processing recipes
		JsonNode procrNode = data.get("recipesProcessing");
		for(Entry<String, JsonNode> entry: Collects.iter(procrNode.fields())) {
			String id = entry.getKey();
			JsonNode nodeTag = entry.getValue();
			STNRGroupTag ttag = newTag(ContentsBlocks.CHEST, id);
			ttag.load(nodeTag);
		}
		
		//Load crafting recipes
		JsonNode craftrNode = data.get("recipesCrafting");
		stencilinv.load(craftrNode);
		stencilinv.setCapacity(Double.POSITIVE_INFINITY);
		
		//Load procurers
		
	}
	
	/**
	 * Checks if the item is potentially producible
	 * (procurable, craftable or processable to)
	 * @param entry item to test
	 * @return does given item entry have any valid crafting/process/procurement?
	 */
	public boolean isEverProducible(ItemEntry entry) {
		//TODO procurement
		if(stencil2OutIndex.containsTo(entry)) return true;
		if(processRecipe2OutIndex.containsTo(entry)) return true;
		return false;
	}
	/**
	 * Checks if the item is potentially obtainable
	 * (the item is planned with a negative amount, stored in the inventory, or producible ({@link #isEverProducible(ItemEntry)})
	 * @param entry item to test
	 * @param inv remaining items in inventory map
	 * @param queue queue from which items may be withdrawn
	 * @return is the item either in the inventory or producible
	 */
	public boolean isEverObtainable(ItemEntry entry, Object2IntMap<ItemEntry> inv, Object2IntMap<ItemEntry> queue) {
		return queue.getOrDefault(entry, 0) < 0 || inv.getOrDefault(entry, 0) > 0 || isEverProducible(entry);
	}
	
	public boolean isAllObtainable(Set<mmb.world.item.ItemEntry> entries, Object2IntMap<ItemEntry> inv, Object2IntMap<ItemEntry> queue) {
		return CollectionOps.isAll(entries, item -> isEverObtainable(item, inv, queue));
	}
}
