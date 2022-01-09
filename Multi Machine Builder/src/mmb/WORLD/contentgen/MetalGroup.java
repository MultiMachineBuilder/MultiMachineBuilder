/**
 * 
 */
package mmb.WORLD.contentgen;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.annotation.Nonnull;

import mmb.DATA.contents.texture.Textures;
import mmb.GRAPHICS.texgen.TexGen;
import mmb.WORLD.block.Block;
import mmb.WORLD.block.BlockEntityType;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.blocks.machine.manual.Crafting;
import mmb.WORLD.crafting.Craftings;
import mmb.WORLD.electric.VoltageTier;
import mmb.WORLD.item.Item;

/**
 * @author oskar
 *
 */
public class MetalGroup{
	@Nonnull public final Block block;
	@Nonnull public final Item base;
	@Nonnull public final Item nugget;
	
	@Nonnull public final Item wire;
	@Nonnull public final Item dust;
	@Nonnull public final Item minidust;
	
	@Nonnull public final Item panel;
	@Nonnull public final Item foil;
	@Nonnull public final Item gear;
	
	@Nonnull public final Color c;
	
	/**
	 * @param c display color
	 * @param id material ID
	 * @param title display title
	 * @param volt minimum voltage tier for recipes
	 * @param baseCost base cost of smelting in joules
	 */
	public MetalGroup(Color c, String id, String title, VoltageTier volt, double baseCost) {
		this.c = c;
		block = createBlock(c, id, title);
		base = createBase(c, id, title);
		nugget = createNugget(c, id, title);
				
		wire = createWire(c, id, title);
		dust = createDust(c, id, title);
		minidust = createMinidust(c, id, title);
		
		panel = createPanel(c, id, title);
		foil = createFoil(c, id, title);
		gear = createGear(c, id, title);
		
		//Recipes
		Craftings.crusher.add(base, dust, volt, baseCost/2);
		Craftings.crusher.add(panel, dust, volt, baseCost/2);
		Craftings.crusher.add(gear, dust, volt, baseCost/2);
		Craftings.crusher.add(nugget, minidust, volt, baseCost/32);
		Craftings.crusher.add(foil, minidust, volt, baseCost/32);
		
		Craftings.smelting.add(dust, base, volt, baseCost/2);
		Craftings.smelting.add(minidust, nugget, volt, baseCost/32);
		
		Craftings.clusterMill.add(base, panel, volt, baseCost/2);
		Craftings.clusterMill.add(nugget, foil, volt, baseCost/32);
		Craftings.clusterMill.add(panel, foil, 16, volt, baseCost/2);
		
		Crafting.ingotNugget(base, nugget);
		Crafting.ingotNugget(block, base);
		Crafting.ingotNugget(dust, minidust);
	}
	/**
	 * @param c color
	 * @param id material ID
	 * @param title material title
	 * @return the created nugget
	 */
	@Nonnull public Item createMinidust(Color c, String id, String title) {
		return new Item()
		.title(title+" tiny dust")
		.texture(minidust(c))
		.volumed(0.000125)
		.finish("minidust."+id);
	}
	/**
	 * @param c color
	 * @param id material ID
	 * @param title material title
	 * @return the created gear
	 */
	@Nonnull public Item createGear(Color c, String id, String title) {
		return new Item()
		.title(title+" gear")
		.texture(gear(c))
		.volumed(0.00125)
		.finish("gear."+id);
	}
	/**
	 * @param c color
	 * @param id material ID
	 * @param title material title
	 * @return the created foil
	 */
	@Nonnull public Item createFoil(Color c, String id, String title) {
		return new Item()
		.title(title+" foil")
		.texture(foil(c))
		.volumed(0.000125)
		.finish("foil."+id);
	}
	/**
	 * @param c color
	 * @param id material ID
	 * @param title material title
	 * @return the created panel
	 */
	@Nonnull public Item createPanel(Color c, String id, String title) {
		return new Item()
		.title(title+" plate")
		.texture(panel(c))
		.volumed(0.00125)
		.finish("panel."+id);
	}
	/**
	 * @param c color
	 * @param id material ID
	 * @param title material title
	 * @return the created dust
	 */
	@Nonnull public Item createDust(Color c, String id, String title) {
		return new Item()
		.title(title+" dust")
		.texture(dust(c))
		.volumed(0.00125)
		.finish("dust."+id);
	}
	/**
	 * @param c color
	 * @param id material ID
	 * @param title material title
	 * @return the created wire spool
	 */
	@Nonnull public Item createWire(Color c, String id, String title) {
		return new Item()
		.title(title+" wire spool")
		.texture(TexGen.wire(c))
		.volumed(0.00125)
		.finish("wirespool."+id);
	}
	/**
	 * @param c color
	 * @param id material ID
	 * @param title material title
	 * @return the created nugget
	 */
	@Nonnull public Item createNugget(Color c, String id, String title) {
		return new Item()
		.title(title+" nugget")
		.texture(TexGen.nugget(c))
		.volumed(0.000125)
		.finish("nugget."+id);
	}
	/**
	 * @param c color
	 * @param id material ID
	 * @param title material title
	 * @return the created base item
	 */
	@Nonnull public Item createBase(Color c, String id, String title) {
		return new Item()
		.title(title+" ingot")
		.texture(TexGen.ingot(c))
		.volumed(0.00125)
		.finish("ingot."+id);
	}
	/**
	 * @param c color
	 * @param id material ID
	 * @param title material title
	 * @return the created block
	 */
	@Nonnull public Block createBlock(Color c, String id, String title) {
		return new Block()
		.texture(block(c))
		.title(title+" block")
		.volumed(0.0125)
		.finish("block."+id);
	}

	@Nonnull private static final BufferedImage BLOCK = Textures.get("block/block.png");
	@Nonnull private static final BufferedImage DUST = Textures.get("item/dust.png");
	@Nonnull private static final BufferedImage MINIDUST = Textures.get("item/minidust.png");
	@Nonnull private static final BufferedImage PANEL = Textures.get("item/panel.png");
	@Nonnull private static final BufferedImage FOIL = Textures.get("item/foil.png");
	@Nonnull private static final BufferedImage GEAR = Textures.get("item/gear.png");
	@Nonnull protected static BufferedImage block(Color c) {
		return TexGen.genTexture(c, BLOCK, null);
	}
	@Nonnull protected static BufferedImage dust(Color c) {
		return TexGen.genTexture(c, DUST, null);
	}
	@Nonnull protected static BufferedImage minidust(Color c) {
		return TexGen.genTexture(c, MINIDUST, null);
	}
	@Nonnull protected static BufferedImage panel(Color c) {
		return TexGen.genTexture(c, PANEL, null);
	}
	@Nonnull protected static BufferedImage foil(Color c) {
		return TexGen.genTexture(c, FOIL, null);
	}
	@Nonnull protected static BufferedImage gear(Color c) {
		return TexGen.genTexture(c, GEAR, null);
	}
}
