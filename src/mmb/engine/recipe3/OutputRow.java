package mmb.engine.recipe3;

import mmb.annotations.Nil;
import mmb.content.electric.VoltageTier;
import mmb.engine.item.ItemEntry;
import mmb.engine.recipe.SingleItem;

/**
 * A single recipe output. Defines item, chance, amount, minimum tier and buffs to the amount and chance.
 * @param item Item to be produced
 * @param amount Amount of the item produced
 * @param chance Probability between 0 and 1 to get the record
 * @param minVoltage Minimum tier of the machine to get this output
 * @param amountBuff Applied to the amount for every voltage tier above the recipe tier
 * @param chanceBuff Applied to the chance for every voltage tier above the recipe tier
 */
public record OutputRow(
    ItemEntry item,
    double amount,
    double chance,
    VoltageTier minVoltage,
    LinearFunction amountBuff,
    LinearFunction chanceBuff
){
	public OutputRow(SingleItem item) {
		this(item.item(), item.amount(), 1, VoltageTier.V1, LinearFunction.IDENTITY, LinearFunction.IDENTITY);
	}
	public OutputRow(ItemEntry item, double amount) {
		this(item, amount, 1, VoltageTier.V1, LinearFunction.IDENTITY, LinearFunction.IDENTITY);
	}
	
	/**
	 * Applies voltage-related buffs to this output row
	 * @param voltage voltage tier the recipe runs at
	 * @return the output row with buffs applied
	 */
	public @Nil OutputRow applyVoltageBuffs(VoltageTier voltage) {
		int voltageDifference = minVoltage.ordinal() - voltage.ordinal();
		if(voltageDifference < 0) return null; //Rejected
		double amount = this.amount;
		double chance = this.chance;
		for(int i = 0; i < voltageDifference; i++) {
			amount = amountBuff.applyAsDouble(amount);
			chance = chanceBuff.applyAsDouble(chance);
		}
		return new OutputRow(item, Math.max(amount, 0), Math.clamp(chance, 0, 1), voltage, amountBuff, chanceBuff);
	}
	
	/** @return tooltip to be shown in recipes */
	public String tooltip() {
		StringBuilder sb = new StringBuilder(item.title());
		sb.append(" * ").append(amount).append('\n')
		.append(amountBuff.toString(false, true)).append(" output")
		.append(chanceBuff.toString(false, true)).append(" chance. ")
		.append("Min voltage tier: ").append(minVoltage);
		return sb.toString();
	}
	/** 
	 * Multiplies the amount of the output in this output row by a specified amount
	 * @return product of this and mul
	 */
	public OutputRow mul(double mul) {
		return new OutputRow(item, amount*mul, chance, minVoltage, amountBuff, chanceBuff);
	}
	
	/** @return is this output row certain to produce the output? */
	public boolean isCertain() {
		return chance >= 1;
	}
	/** @return is this output row rejectable? Must be applied after buffs.*/
	public boolean isRejectable() {
		return amount <= 0 || chance <= 0;
	}
	/**
	 * Checks if the row is to be rejected after applying buffs.
	 * @param row the output row to be checked
	 * @return is the output row to be rejected?
	 */
	public static boolean isRejectable(@Nil OutputRow row) {
		if(row == null) return true;
		return row.isRejectable();
	}
}
