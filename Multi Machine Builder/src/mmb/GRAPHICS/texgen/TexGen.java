/**
 * 
 */
package mmb.GRAPHICS.texgen;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mmb.DATA.contents.texture.Textures;
import mmb.GRAPHICS.awt.ColorMapper;
import mmb.GRAPHICS.awt.ColorMul;
import mmb.WORLD.electric.VoltageTier;

/**
 * @author oskar
 *
 */
public class TexGen {
	/**
	 * Generates a texture from a template.
	 * @param color main color
	 * @param src the source texture
	 * @param dest destination image. If it is null, a new compatible image is constructed
	 * @return the generated texture
	 */
	@Nonnull public static BufferedImage genTexture(Color color, BufferedImage src, @Nullable BufferedImage dest) {
		ColorMul mapper = ColorMul.ofType(src.getType());
		mapper.setColor(color);
		LookupOp lookup = new LookupOp(mapper, null);
		BufferedImage result = dest;
		if(result == null) result = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
		lookup.filter(src, result);
		
		return result;
	}
	
	/**
	 * Generates a texture from a template.
	 * @param r red component of main color
	 * @param g green component of main color
	 * @param b blue component of main color
	 * @param src the source texture
	 * @param dest destination image. If it is null, a new compatible image is constructed
	 * @return the generated texture
	 */
	@Nonnull public static BufferedImage genTexture(int r, int g, int b, BufferedImage src, @Nullable BufferedImage dest) {
		return genTexture(new Color(r, g, b), src, dest);
	}
	
	/**
	 * Generates a texture from a template.
	 * @param c color
	 * @param src the source texture
	 * @param bg the image, over which a recolored component is placed. This image is unchanged
	 * @param dest destination image. If it is null, a new compatible image is constructed
	 * @return the generated texture
	 */
	@Nonnull public static BufferedImage genTexture(Color c, BufferedImage src, BufferedImage bg, @Nullable BufferedImage dest) {
		BufferedImage dest0 = dest;
		if(dest0 == null) dest0 = new BufferedImage(
				bg.getWidth(), bg.getHeight(), bg.getType());
		BufferedImage overlay = genTexture(c, src, null);
		Graphics gr = dest0.createGraphics();
		gr.drawImage(bg, 0, 0, null);
		gr.drawImage(overlay, 0, 0, null);
		gr.dispose();
		return dest0;
	}
	
	/**
	 * Generates a texture from a template.
	 * @param r red component of main color
	 * @param g green component of main color
	 * @param b blue component of main color
	 * @param src the source texture
	 * bg the image, over which a recolored component is placed. This image is unchanged
	 * @param dest destination image. If it is null, a new compatible image is constructed
	 * @return the generated texture
	 */
	@Nonnull public static BufferedImage genTexture(int r, int g, int b, BufferedImage src, BufferedImage bg, @Nullable BufferedImage dest) {
		return genTexture(new Color(r, g, b), src, bg, dest);
	}

	
	
	@Nonnull public static final BufferedImage NUGGET = Textures.get("item/nugget.png");
	@Nonnull public static BufferedImage nugget(Color c) {
		return genTexture(c, NUGGET, null);
	}

	@Nonnull public static final BufferedImage WIREBASE = Textures.get("item/wire spool.png");
	@Nonnull public static final BufferedImage WIREOV = Textures.get("item/wire overlay.png");
	@Nonnull public static BufferedImage wire(Color c) {
		return genTexture(c, TexGen.WIREOV, TexGen.WIREBASE, null);
	}

	@Nonnull public static final BufferedImage INGOT = Textures.get("item/ingot.png");
	@Nonnull public static BufferedImage ingot(Color c) {
		return genTexture(c, TexGen.INGOT, null);
	}

	
	@Nonnull public static final BufferedImage OREOV = Textures.get("block/ore overlay.png");
	
	@Nonnull public static final BufferedImage ORE = Textures.get("block/coal ore.png");
	@Nonnull public static BufferedImage genOre(Color c) {
		return genTexture(c, TexGen.OREOV, TexGen.ORE, null);
	}
	
	@Nonnull public static final BufferedImage CROP = Textures.get("block/coal crop.png");
	@Nonnull public static BufferedImage genCrop(Color c) {
		return genTexture(c, TexGen.OREOV, TexGen.CROP, null);
	}
	
	/**
	 * @param src the source image
	 * @return the list with generated machine textures, in order of increasing voltages
	 */
	@Nonnull public static List<@Nonnull BufferedImage> generateMachineTextures(BufferedImage src){
		ColorMapper mapper = ColorMapper.ofType(src.getType(), Color.RED, Color.BLACK);
		LookupOp lookup = new LookupOp(mapper, null);
		return Stream.of(VoltageTier.values())
				.map(vtier -> {
					mapper.setTo(vtier.c);
					return lookup.filter(src, null);
				}).collect(Collectors.toList());
	}
}
