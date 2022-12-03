/**
 * 
 */
package mmb.world;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.LookupOp;

import javax.annotation.Nonnull;

import mmb.graphics.awt.ColorMapper;
import mmb.texture.Textures;
import mmb.world.blocks.ContentsBlocks;
import mmb.world.item.Item;
import mmb.world.item.Items;

/**
 * @author oskar
 *
 */
public class Chemistry {
	private Chemistry() {}
	/** Initializes chemicals */
	public static void init() {/* just for initialization */}
	@Nonnull private static final BufferedImage gas = Textures.get("chem/gas.png");
	
	//Common chemicals
	/** Carbon dioxide */
	@Nonnull public static final Item CO2 = gas("#co2", new Color(150, 150, 150), "chem.co2");
	/** Nitrogen */
	@Nonnull public static final Item N2 = gas("#n2", new Color(0, 150, 150), "chem.n2");
	/** Hydrogen */
	@Nonnull public static final Item H2 = gas("#h2", new Color(0, 180, 220), "chem.h2");
	/** Oxygen */
	@Nonnull public static final Item O2 = gas("#o2", new Color(220, 0, 0), "chem.o2");
	
	//Hydrocarbons
	@Nonnull public static final Item CH4 = gas("#ch4", new Color(180, 0, 220), "chem.ch4");
	@Nonnull public static final Item C2H6 = gas("#c2h6", new Color(200, 0, 220), "chem.c2h6");
	
	static {
		Items.tagItems("chem", ContentsBlocks.air, ContentsBlocks.water);
	}
			
	
	@Nonnull private static Item gas(String title, Color c, String id) {
		ColorMapper mapper = ColorMapper.ofType(
				gas.getType(), Color.RED, c);
		LookupOp op = new LookupOp(mapper, null);
		BufferedImage img = op.createCompatibleDestImage(gas, null);
		op.filter(gas, img);
		Item item = new Item()
				.title(title)
				.texture(img)
				.volumed(0.004)
				.finish(id);
		Items.tagItem("chem", item);
		return item;
	}
}
