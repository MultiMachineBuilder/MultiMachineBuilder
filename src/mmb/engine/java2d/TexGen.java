/**
 * 
 */
package mmb.engine.java2d;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import mmb.NN;
import mmb.Nil;
import mmb.content.electric.VoltageTier;
import mmb.engine.texture.Textures;

/**
 * Texture generation utilities
 * @author oskar
 */
public class TexGen {
	private TexGen() {}
	
	/**
	 * Colormaps an input image to an output image
	 * @param from source color
	 * @param to destination color
	 * @param src source image
	 * @param dest destination image
	 * @return dest, or a new image
	 */
	@NN public static BufferedImage colormap(Color from, Color to, BufferedImage src, @Nil BufferedImage dest) {
		ColorMapper mapper = ColorMapper.ofType(src.getType(), from, to);
		LookupOp lookup = new LookupOp(mapper, null);
		BufferedImage result = dest;
		if(result == null) result = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
		lookup.filter(src, result);
		return result;
	}
	
	/**
	 * Recolors a source image
	 * @param color main color
	 * @param src the source texture
	 * @param dest destination image. If it is null, a new compatible image is constructed
	 * @return the generated texture
	 */
	@NN public static BufferedImage genTexture(Color color, BufferedImage src, @Nil BufferedImage dest) {
		ColorMul mapper = ColorMul.ofType(src.getType());
		mapper.setColor(color);
		LookupOp lookup = new LookupOp(mapper, null);
		BufferedImage result = dest;
		if(result == null) result = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
		lookup.filter(src, result);
		
		return result;
	}
	/**
	 * Recolors a source image
	 * @param r red component of main color
	 * @param g green component of main color
	 * @param b blue component of main color
	 * @param src the source texture
	 * @param dest destination image. If it is null, a new compatible image is constructed
	 * @return the generated texture
	 */
	@NN public static BufferedImage genTexture(int r, int g, int b, BufferedImage src, @Nil BufferedImage dest) {
		return genTexture(new Color(r, g, b), src, dest);
	}
	/**
	 * Recolors the source image and places it over a background
	 * @param c color
	 * @param src the source texture
	 * @param bg the image, over which a recolored component is placed. This image is unchanged
	 * @param dest destination image. If it is null, a new compatible image is constructed
	 * @return the generated texture
	 */
	@NN public static BufferedImage genTexture(Color c, BufferedImage src, BufferedImage bg, @Nil BufferedImage dest) {
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
	 * Recolors the source image and places it over a background
	 * @param r red component of main color
	 * @param g green component of main color
	 * @param b blue component of main color
	 * @param src the source texture
	 * @param bg the image, over which a recolored component is placed. This image is unchanged
	 * @param dest destination image. If it is null, a new compatible image is constructed
	 * @return the generated texture
	 */
	@NN public static BufferedImage genTexture(int r, int g, int b, BufferedImage src, BufferedImage bg, @Nil BufferedImage dest) {
		return genTexture(new Color(r, g, b), src, bg, dest);
	}

	@NN public static final BufferedImage NUGGET = Textures.get("item/nugget.png");
	@NN public static BufferedImage nugget(Color c) {
		return genTexture(c, NUGGET, null);
	}

	@NN public static final BufferedImage WIREBASE = Textures.get("item/wire spool.png");
	@NN public static final BufferedImage WIREOV = Textures.get("item/wire overlay.png");
	@NN public static BufferedImage wire(Color c) {
		return genTexture(c, TexGen.WIREOV, TexGen.WIREBASE, null);
	}

	@NN public static final BufferedImage INGOT = Textures.get("item/ingot.png");
	@NN public static BufferedImage ingot(Color c) {
		return genTexture(c, TexGen.INGOT, null);
	}

	@NN public static final BufferedImage OREOV = Textures.get("block/ore overlay.png");
	
	@NN public static final BufferedImage ORE = Textures.get("block/coal ore.png");
	@NN public static BufferedImage genOre(Color c) {
		return genTexture(c, TexGen.OREOV, TexGen.ORE, null);
	}
	
	@NN public static final BufferedImage CROP = Textures.get("block/coal crop.png");
	@NN public static BufferedImage genCrop(Color c) {
		return genTexture(c, TexGen.OREOV, TexGen.CROP, null);
	}
	
	/**
	 * Generates a list of images for all voltage tiers of machines
	 * @param src the source image
	 * @return the list with generated machine textures, in order of increasing voltages
	 */
	@NN public static List<@NN BufferedImage> generateMachineTextures(BufferedImage src){
		ColorMapper mapper = ColorMapper.ofType(src.getType(), Color.RED, Color.BLACK);
		LookupOp lookup = new LookupOp(mapper, null);
		return Stream.of(VoltageTier.values())
				.map(vtier -> {
					mapper.setTo(vtier.c);
					return lookup.filter(src, null);
				}).collect(Collectors.toList());
	}
}
