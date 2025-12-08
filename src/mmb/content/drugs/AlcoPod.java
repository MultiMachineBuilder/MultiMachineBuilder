/**
 * 
 */
package mmb.content.drugs;

import mmb.annotations.NN;
import mmb.engine.item.Item;
import mmb.engine.recipe.RecipeOutput;
import mmb.menu.wtool.WindowTool;

/**
 * This item represents alcoholic beverages ingame
 * @author oskar
 *  +-----------------------------------------------------+
 *  |REAL-LIFE ALCOHOL CONSUMPTION IS HARMFUL             |
 *  |½ LITER OF BEER CONTAINS 25g OF ETHYL ALCOHOL        |
 *  |SALE OF LIQUOR TO PEOPLE BELOW 18y IS A CRIME        |
 *  |EVEN THAT AMOUNT HARMS HEALTH OF PREGNANT WOMEN      |
 *  |AND IS DANGEROUS TO DRIVERS                          |
 *  +-----------------------------------------------------+
 */
public class AlcoPod extends Item implements Intoxicating {
	//Constructors
	/**
	 * Creates an alcoholic beverage
	 * @param dose amount of alcohol per drink
	 * @param drop items left after use
	 */
	public AlcoPod(double dose, RecipeOutput drop) {
		super();
		this.dose = dose;
		this.drop = drop;
	}
	
	//Contents
	private final double dose;
	@NN private final RecipeOutput drop;
	@Override
	public double alcoholicity() {
		return dose;
	}
	@Override
	public RecipeOutput postdrink() {
		return drop;
	}
	@Override
	public void onregister() {
		Alcohol.alcohol.add(this, drop, dose);
	}
	
	//Tool methods
	private static final WindowTool TOOL = new ToolAlcohol();
	@Override
	public WindowTool getTool() {
		return TOOL;
	}
	
}
