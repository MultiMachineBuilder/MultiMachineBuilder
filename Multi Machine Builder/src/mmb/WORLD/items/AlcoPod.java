/**
 * 
 */
package mmb.WORLD.items;

import mmb.BEANS.Intoxicating;
import mmb.WORLD.item.Item;
import mmb.WORLD.tool.ToolAlcohol;
import mmb.WORLD.tool.WindowTool;

/**
 * @author oskar
 * This item represents an AlcoPod, a fictional alcoholic beverage available in pods
 *  ╔═════════════════════════════════════════════════════╗
 *  ║REAL-LIFE ALCOHOL CONSUMPTION IS HARMFUL             ║
 *  ║½ LITER OF BEER CONTAINS 25g OF ETHYL ALCOHOL        ║
 *  ║SALE OF LIQUOR TO PEOPLE BELOW 18y IS A CRIME        ║
 *  ║EVEN THAT AMOUNT HARMS HEALTH OF PREGNANT WOMEN      ║
 *  ║AND IS DANGEROUS TO DRIVERS                          ║
 *  ╠═════════════════════════════════════════════════════╣
 *  ║SPOŻYCIE ALKOHOLU W RZECZYWISTOŚCI JEST SZKODLIWE    ║
 *  ║½ LITRA PIWA ZAWIERA 25g ALKOHOLU ETYLOWEGO          ║
 *  ║SPRZEDAŻ ALKOHOLU OSOBOM DO LAT 18 JEST PRZESTĘPSTWEM║
 *  ║NAWET TAKA ILOŚĆ SZKODZI ZDROWIU KOBIET W CIĄŻY      ║
 *  ║I JEST NIEBEZPIECZNA DLA KIEROWCÓW                   ║
 *  ╚═════════════════════════════════════════════════════╝
 */
public class AlcoPod extends Item implements Intoxicating {
	private static final WindowTool TOOL = new ToolAlcohol();
	@Override
	public WindowTool getTool() {
		return TOOL;
	}
	@Override
	public double alcoholicity() {
		return 1;
	}
	
}
