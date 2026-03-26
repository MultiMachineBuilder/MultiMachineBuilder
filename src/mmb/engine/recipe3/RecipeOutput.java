package mmb.engine.recipe3;

import java.util.*;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import mmb.annotations.NN;
import mmb.annotations.Nil;
import mmb.engine.item.ItemEntry;
import mmb.engine.recipe.ItemList;
import mmb.inventory.io.InventoryWriter;
import monniasza.collects.Collects;

/** A collection of recipe outputs with chances, min voltages and buffs. */
public class RecipeOutput {
	/** Collection of recipe outputs */
	public final List<OutputRow> rows;
	/** Set of unique items in this recipe output */
	public final Set<ItemEntry> items;
	/** Rows with no guarantees of creation */
	public final List<OutputRow> chancedRows;
	/** All guaranteed outputs */
	public final ItemList certainRows;
	/** The maximum space that the recipe output can take*/
	public final ItemList maximumOutputs;
	
	/** Empty recipe output with no rows */
	public static RecipeOutput EMPTY = new RecipeOutput();
	
	/** Creates a new recipe output from a collection of rows */
	public RecipeOutput(Collection<@NN OutputRow> rows) {
		Objects.requireNonNull(rows, "rows is null");
		this.rows = List.copyOf(rows);
		this.items = createItemsSetFromList();
		var certain = new ArrayList<OutputRow>();
		var chance = new ArrayList<OutputRow>();
		for(var row: rows){
			if(row.chance() <= 0) continue;
			if(row.chance() >= 1) 
				certain.add(row);
			else chance.add(row);
		}
		
		this.chancedRows = List.copyOf(chance);
		this.certainRows = OutputRow.compress(certain);
		this.maximumOutputs = OutputRow.compress(rows);
	}
	/** Creates a new recipe output from a collection of rows */
	public RecipeOutput(@NN OutputRow @NN ... rows) {
		this(List.of(Objects.requireNonNull(rows, "rows is null")));
	}
	
	private Set<ItemEntry> createItemsSetFromList(){
		return rows.stream().map(x -> x.item()).collect(Collectors.toUnmodifiableSet());
	}
	
	//OPERATIONS
	/**
	 * Multiplies all output rows by an amount.
	 * @param mul multiplier to apply
	 * @return product of the multiplier and this
	 */
	public RecipeOutput mul(double mul) {
		@SuppressWarnings("null")
		@NN OutputRow[] transformedRows = new OutputRow[rows.size()];
		for(int i = 0 ; i < rows.size(); i++) {
			transformedRows[i] = rows.get(i).mul(mul);
		}
		return new RecipeOutput(transformedRows);
	}
	/**
	 * Applied voltage-related buffs to this recipe output.
	 * @param tier voltage tier to apply buffs to
	 * @return recipe output with buffs added and incompatible ingredients skipped
	 */
	public RecipeOutput applyVoltageBuffs(VoltageTier tier) {
		Objects.requireNonNull(tier, "tier is null");
		@NN List<@NN OutputRow> transformedRows = new ArrayList<>(rows.size());
		for(int i = 0 ; i < rows.size(); i++) {
			OutputRow transformedRow = rows.get(i).applyVoltageBuffs(tier);
			if(transformedRow == null) continue;
			transformedRows.add(transformedRow);
		}
		return new RecipeOutput(transformedRows);
	}
	/**
	 * Adds this and another recipe output's contents into a new recipe output
	 * @param other the other list to add
	 * @return sum of this and other
	 */
	public RecipeOutput add(RecipeOutput other) {
		Objects.requireNonNull(other, "other is null");
		OutputRow[] newrows = new OutputRow[rows.size() + other.rows.size()];
		for(int i = 0; i < rows.size(); i++) newrows[i] = rows.get(i);
		for(int i = 0; i < other.rows.size(); i++) newrows[rows.size() + i] = other.rows.get(i);
		return new RecipeOutput(newrows);
	}
	
	public RecipeOutput transform(UnaryOperator<@NN OutputRow> filter) {
		Objects.requireNonNull(filter, "filter is null");
		OutputRow[] newrows = new OutputRow[rows.size()];
		for(int i = 0; i < rows.size(); i++) {
			@SuppressWarnings("null")
			OutputRow result = filter.apply(rows.get(i));
			newrows[i] = Objects.requireNonNull(result, "result #"+i+" is empty");
		}
		return new RecipeOutput(newrows);
	}
	public RecipeOutput filterTransform(Function<@NN OutputRow, @Nil OutputRow> filter) {
		Objects.requireNonNull(filter, "filter is null");
		List<@NN OutputRow> transformedRows = new ArrayList<>(rows.size());
		for(int i = 0; i < rows.size(); i++) {
			OutputRow result = filter.apply(rows.get(i));
			if(result != null) transformedRows.add(result);
		}
		return new RecipeOutput(transformedRows);
	}
	
	public void produceOutputs(InventoryWriter out, int amount) {
		//First transfer the certain items
		out.bulkInsert(certainRows, amount);
		
		//Then transfer the chances
		for(var row: chancedRows) {
			double roll = Math.random();
			boolean test = roll < row.chance();
			if(test) out.insert(row.item(), (int)row.amount());
		}
	}
}
