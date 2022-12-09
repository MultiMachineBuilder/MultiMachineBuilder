/**
 * 
 */
package mmb.content.drugs;

import static mmb.content.ContentsBlocks.*;
import static mmb.content.ContentsItems.*;
import static mmb.content.CraftingGroups.*;

import java.util.ResourceBundle;

import javax.annotation.Nonnull;

import mmb.content.electric.VoltageTier;
import mmb.engine.GlobalSettings;
import mmb.engine.craft.SimpleItemList;
import mmb.engine.item.Item;
import mmb.engine.item.ItemEntry;
import mmb.engine.item.Items;

/**
 * The main class for alcoholic beverages
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
	@Nonnull public static final AlcoholInfoGroup alcohol = new AlcoholInfoGroup("alcohol");
	/** An empty container for beer */
	@Nonnull public static final Item beerEmpty = new Item()
		.title("#beerb")
		.texture("alcohol/beer empty.png")
		.volumed(0.004)
		.finish("drugs.beer0");
	/** Beer, an alcoholic drink (now the only alcoholic drink)*/
	@Nonnull public static final AlcoPod beer = (AlcoPod) new AlcoPod(1.5, beerEmpty)
		.title("#beer")
		.texture("alcohol/beer.png")
		.volumed(0.004)
		.finish("drugs.beer");
	
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
		seeds.stack(8),
		water.stack(1),
		hops.stack(1),
		yeast.stack(3)
		), new SimpleItemList(Alcohol.beer.stack(16), yeast.stack(4)), VoltageTier.V1, 1000000);
	}
}
