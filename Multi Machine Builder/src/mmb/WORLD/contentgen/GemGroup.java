/**
 * 
 */
package mmb.WORLD.contentgen;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.annotation.Nonnull;

import mmb.DATA.contents.texture.Textures;
import mmb.GRAPHICS.texgen.TexGen;
import mmb.WORLD.electric.VoltageTier;
import mmb.WORLD.item.Item;

/**
 * @author oskar
 * Represents a gem
 */
public class GemGroup extends MetalGroup {

	/**
	 * @param c display color
	 * @param id material ID
	 * @param title display title
	 * @param volt minimum voltage tier for recipes
	 * @param baseCost base cost of smelting in joules
	 */
	public GemGroup(Color c, String id, String title, VoltageTier volt, double baseCost) {
		super(c, id, title, volt, baseCost);
	}

	@Override
	public Item createBase(Color c, String id, String title) {
		return new Item()
				.title(title)
				.texture(gem(c))
				.volumed(0.00125)
				.finish("gem."+id);
	}
	
	@Nonnull private static final BufferedImage GEM = Textures.get("item/gem.png");
	@Nonnull protected static BufferedImage gem(Color c) {
		return TexGen.genTexture(c, GEM, null);
	}
	
}
