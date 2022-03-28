/**
 * 
 */
package mmb.WORLD.contentgen;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import javax.annotation.Nonnull;

import mmb.DATA.contents.texture.Textures;
import mmb.GRAPHICS.texgen.TexGen;
import mmb.WORLD.block.Block;
import mmb.WORLD.blocks.machine.manual.Crafting;
import mmb.WORLD.crafting.Craftings;
import mmb.WORLD.electric.VoltageTier;
import mmb.WORLD.item.Item;
import mmb.WORLD.item.Items;
import mmb.WORLD.items.ItemEntry;

/**
 * @author oskar
 *
 */
public class MetalGroup{
	@Nonnull public final Block block;
	@Nonnull public final Item cluster;
	@Nonnull public final Item base;
	@Nonnull public final Item frag;
	@Nonnull public final Item nugget;
	
	@Nonnull public final Item wire;
	@Nonnull public final Item megadust;
	@Nonnull public final Item dust;
	@Nonnull public final Item smalldust;
	@Nonnull public final Item tinydust;
	
	@Nonnull public final Item panel;
	@Nonnull public final Item foil;
	@Nonnull public final Item gear;
	
	@Nonnull public final Color c;
	
	@Nonnull public final String id;
	@Nonnull public final String title;
	@Nonnull public final VoltageTier volt;
	         public final double power;
	
	/**
	 * @param c display color
	 * @param id material ID
	 * @param title display title
	 * @param volt minimum voltage tier for recipes
	 * @param baseCost base cost of smelting in joules
	 */
	public MetalGroup(Color c, String id, String title, VoltageTier volt, double baseCost) {
		this.power = baseCost;
		this.volt = volt;
		this.c = c;
		block = createBlock(c, id, title);
		cluster = new Item()
				.title(title+" cluster")
				.texture(cluster(c))
				.volumed(0.005)
				.finish("fcluster."+id);
		base = createBase(c, id, title);
		frag = new Item()
				.title(title+" fragment")
				.texture(fragment(c))
				.volumed(0.0005)
				.finish("frag."+id);
		nugget = createNugget(c, id, title);
				
		wire = createWire(c, id, title);
		megadust = new Item()
				.title(title+" large dust")
				.texture(ldust(c))
				.volumed(0.0005)
				.finish("ldust."+id);
		dust = createDust(c, id, title);
		smalldust = new Item()
				.title(title+" small dust")
				.texture(mdust(c))
				.volumed(0.005)
				.finish("mdust."+id);
		tinydust = createMinidust(c, id, title);
		
		panel = createPanel(c, id, title);
		foil = createFoil(c, id, title);
		gear = createGear(c, id, title);
		
		//Recipes
		Craftings.crusher.add(base, dust, volt, baseCost/2);
		Craftings.crusher.add(panel, dust, volt, baseCost/2);
		Craftings.crusher.add(gear, dust, volt, baseCost/2);
		Craftings.crusher.add(nugget, tinydust, volt, baseCost/32);
		Craftings.crusher.add(foil, tinydust, volt, baseCost/32);
		Craftings.crusher.add(frag, smalldust, volt, baseCost/8);
		Craftings.crusher.add(cluster, megadust, volt, baseCost/2);
		
		Craftings.smelting.add(dust, base, volt, baseCost/2);
		Craftings.smelting.add(tinydust, nugget, volt, baseCost/32);
		Craftings.smelting.add(megadust, cluster, volt, baseCost*2);
		Craftings.smelting.add(smalldust, frag, volt, baseCost/8);
		
		Craftings.clusterMill.add(base, panel, volt, baseCost/2);
		Craftings.clusterMill.add(nugget, foil, volt, baseCost/32);
		Craftings.clusterMill.add(panel, foil, 16, volt, baseCost/2);
		
		Craftings.crafting.addRecipeGrid(new ItemEntry[] {
				null, frag, null,
				frag, null, frag,
				null, frag, null
		}, 3, 3, gear);
		
		//Nugget <-> frag
		Craftings.crafting.addRecipeGrid(nugget, 2, 2, frag);
		Craftings.crafting.addRecipe(frag, nugget, 4);
		Craftings.splitter.add(frag,      nugget, 4, volt, baseCost/64);
		Craftings.combiner.add(nugget, 4, frag,      volt, baseCost/64);
		
		//Frag <-> base
		Craftings.crafting.addRecipeGrid(frag, 2, 2, base);
		//No crafting base -> fragment
		Craftings.splitter.add(base,    frag, 4, volt, baseCost/16);
		Craftings.combiner.add(frag, 4, base,    volt, baseCost/16);
		
		//Base <-> cluster
		Craftings.crafting.addRecipeGrid(base, 2, 2, cluster);
		Craftings.crafting.addRecipe(cluster, base, 4);
		Craftings.splitter.add(cluster, base, 4, volt, baseCost/4);
		Craftings.combiner.add(base, 4, cluster, volt, baseCost/4);
		
		//Block <-> cluster
		Craftings.crafting.addRecipeGrid(cluster, 2, 2, block);
		//No crafting block -> cluster
		Craftings.splitter.add(block,      cluster, 4, volt, baseCost);
		Craftings.combiner.add(cluster, 4, block,      volt, baseCost);
		
		//SmallDust <-> TinyDust
		Craftings.crafting.addRecipeGrid(tinydust, 2, 2, smalldust);
		Craftings.crafting.addRecipe(smalldust, tinydust, 4);
		Craftings.splitter.add(smalldust,   tinydust, 4, volt, baseCost/256);
		Craftings.combiner.add(tinydust, 4, smalldust,   volt, baseCost/256);
		
		//Dust <-> SmallDust
		Craftings.crafting.addRecipeGrid(smalldust, 2, 2, dust);
		//No crafting recipe Dust -> SmallDust
		Craftings.splitter.add(dust,         smalldust, 4, volt, baseCost/64);
		Craftings.combiner.add(smalldust, 4, dust,         volt, baseCost/64);
		
		//MegaDust <-> Dust
		Craftings.crafting.addRecipeGrid(dust, 2, 2, megadust);
		Craftings.crafting.addRecipe(megadust, dust, 4);
		Craftings.splitter.add(megadust, dust, 4,  volt, baseCost/16);
		Craftings.combiner.add(dust, 4,  megadust, volt, baseCost/16);
		
		//WireMill
		Craftings.wiremill.add(nugget, wire, volt, baseCost/32);
				
		Crafting.ingotNugget(base, nugget);
		Crafting.ingotNugget(block, base);
		Crafting.ingotNugget(dust, tinydust);
		
		//Tagging
		Items.tagItems("material-"+id, block, cluster, base, frag, nugget, wire, megadust, dust, smalldust, tinydust, panel, foil, gear);
		Items.tagItem("shape-block", block);
		Items.tagItem("shape-cluster", cluster);
		Items.tagItem("shape-base", base);
		Items.tagItem("shape-frag", frag);
		Items.tagItem("shape-nugget", nugget);
		Items.tagItem("shape-wire", wire);
		Items.tagItem("shape-megadust", megadust);
		Items.tagItem("shape-dust", dust);
		Items.tagItem("shape-smalldust", smalldust);
		Items.tagItem("shape-tinydust", tinydust);
		Items.tagItem("shape-panel", panel);
		Items.tagItem("shape-foil", foil);
		Items.tagItem("shape-gear", gear);
		
		this.id = id;
		this.title = title;
		
		//Index
		byID0.put(id, this);
		byID0sort.put(id, this);
		byVoltage0.put(volt, this);
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
	@Nonnull private static final BufferedImage FRAG = Textures.get("item/fragment.png");
	@Nonnull private static final BufferedImage CLUSTER = Textures.get("item/cluster.png");
	@Nonnull private static final BufferedImage MDUST = Textures.get("item/smalldust.png");
	@Nonnull private static final BufferedImage LDUST = Textures.get("item/ldust.png");
	@Nonnull protected static BufferedImage mdust(Color c) {
		return TexGen.genTexture(c, MDUST, null);
	}
	@Nonnull protected static BufferedImage ldust(Color c) {
		return TexGen.genTexture(c, LDUST, null);
	}
	@Nonnull protected static BufferedImage block(Color c) {
		return TexGen.genTexture(c, BLOCK, null);
	}
	@Nonnull protected static BufferedImage dust(Color c) {
		return TexGen.genTexture(c, DUST, null);
	}
	@Nonnull protected static BufferedImage fragment(Color c) {
		return TexGen.genTexture(c, FRAG, null);
	}
	@Nonnull protected static BufferedImage cluster(Color c) {
		return TexGen.genTexture(c, CLUSTER, null);
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

	public static class MaterialStack{
		@Nonnull public final MetalGroup material;
		public final int amount;
		public MaterialStack(MetalGroup material, int amount) {
			this.material = material;
			this.amount = amount;
		}
	}
	
	public final MaterialStack stack(int amount) {
		return new MaterialStack(this, amount);
	}
	
	private static final Map<String, MetalGroup> byID0 = new HashMap<>();
	public static final Map<String, MetalGroup> byID = Collections.unmodifiableMap(byID0);
	private static final NavigableMap<String, MetalGroup> byID0sort = new TreeMap<>();
	public static final NavigableMap<String, MetalGroup> byIDsort = Collections.unmodifiableNavigableMap(byID0sort);
	private static final Map<VoltageTier, MetalGroup> byVoltage0 = new EnumMap<>(VoltageTier.class);
	public static final Map<VoltageTier, MetalGroup> byVoltage = Collections.unmodifiableMap(byVoltage0);
}
