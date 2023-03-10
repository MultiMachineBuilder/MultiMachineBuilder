/**
 * 
 */
package mmb.content.drugs;

import static mmb.content.ContentsBlocks.*;
import static mmb.content.ContentsItems.*;
import static mmb.content.CraftingGroups.*;

import java.util.ResourceBundle;

import mmb.NN;
import mmb.content.agro.Agro;
import mmb.content.electric.VoltageTier;
import mmb.engine.item.Item;
import mmb.engine.item.ItemEntry;
import mmb.engine.item.Items;
import mmb.engine.recipe.SimpleItemList;
import mmb.engine.settings.GlobalSettings;
import mmb.menu.wtool.Tools;
import mmb.menu.wtool.WindowToolModel;

/**
 * The main class for alcoholic beverages.
 * Theuy have intoxicating effects
 * +-----------------------------------------------------+
 * |REAL-LIFE ALCOHOL CONSUMPTION IS HARMFUL             |
 * |½ LITER OF BEER CONTAINS 25g OF ETHYL ALCOHOL        |
 * |SALE OF LIQUOR TO PEOPLE BELOW 18yrs OLD IS A CRIME  |
 * |EVEN THAT AMOUNT HARMS HEALTH OF PREGNANT WOMEN      |
 * |AND IS DANGEROUS TO DRIVERS                          |
 * +-----------------------------------------------------+
 * @author oskar
 */
public class Alcohol {
	private Alcohol() {}
	static {
		GlobalSettings.injectResources(ResourceBundle.getBundle("mmb.content.drugs.bundle"));
	}
	
	/** A recipe group for alcoholic beverages */
	@NN public static final AlcoholInfoGroup alcohol = new AlcoholInfoGroup("alcohol");
	/** An empty container for beer */
	@NN public static final Item beerEmpty = new Item()
		.title("#beerb")
		.texture("alcohol/beer empty.png")
		.volumed(0.004)
		.finish("drugs.beer0");
	/** Beer, an alcoholic drink (now the only alcoholic drink)*/
	@NN public static final AlcoPod beer = (AlcoPod) new AlcoPod(1.5, beerEmpty)
		.title("#beer")
		.texture("alcohol/beer.png")
		.volumed(0.004)
		.finish("drugs.beer");
	/** The window tool used by alcoholic beverages */
	public static final WindowToolModel TOOL_ALCOHOL =
		new WindowToolModel(ToolAlcohol.ICON, ToolAlcohol::new, "alcohol");
	
	private static boolean inited = false;
	/** Initializes items */
	public static void init() {
		if(inited) return;
		inited = true;
		
		Items.tagItem("alcohol", beer);
		Items.tagItem("material-glass", beerEmpty);
		
		//Alcohol
		crafting.addRecipeGrid(new ItemEntry[]{
		glassp, glassp, glassp,
		glassp,  paper, glassp,
		glassp, glassp, glassp
		}, 3, 3, Alcohol.beerEmpty, 16); //Beer bottle
		
		//Brewery recipe for beer
		brewery.add(new SimpleItemList(
		Alcohol.beerEmpty.stack(16),
		Agro.seeds.stack(8),
		water.stack(1),
		Agro.hops.stack(1),
		Agro.yeast.stack(3)
		), new SimpleItemList(Alcohol.beer.stack(16), Agro.yeast.stack(4)), VoltageTier.V1, 1000000);
		
		//Alcohol tool
		Tools.toollist.add(TOOL_ALCOHOL);
	}
}
