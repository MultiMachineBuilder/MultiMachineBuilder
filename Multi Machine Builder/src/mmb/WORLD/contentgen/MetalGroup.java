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

import mmb.DATA.contents.Textures;
import mmb.GRAPHICS.texgen.TexGen;
import mmb.WORLD.block.Block;
import mmb.WORLD.blocks.machine.manual.Crafting;
import mmb.WORLD.crafting.Craftings;
import mmb.WORLD.electric.VoltageTier;
import mmb.WORLD.item.Item;
import mmb.WORLD.item.Items;
import mmb.WORLD.items.ItemEntry;
import static mmb.GlobalSettings.*;

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
			 public final boolean isGem;
	
	@Nonnull public final String t_nominative;
	@Nonnull public final String t_nominative_short;
	@Nonnull public final String t_basic;
	@Nonnull public final String t_adjective;
	@Nonnull public final VoltageTier volt;
	         public final double power;
			@Nonnull static final BufferedImage GEM = Textures.get("item/gem.png");
	
	/**
	 * @param c display color
	 * @param id material ID
	 * @param volt minimum voltage tier for recipes
	 * @param baseCost base cost of smelting in joules
	 * @param isGem is a given material a gem?
	 */
	public MetalGroup(Color c, String id, VoltageTier volt, double baseCost, boolean isGem) {
		this.power = baseCost;
		this.volt = volt;
		this.c = c;
		this.id = id;
		this.t_nominative = $res("matname-"+id);
		this.t_basic = $res("matname1-"+id);
		this.t_adjective = $res("matname2-"+id);
		this.isGem = isGem;
		int index = t_nominative.indexOf(' ')+1;
		if(index == 3 || index == 2) {
			t_nominative_short=t_nominative.substring(index);
		}else {
			t_nominative_short=t_nominative;
		}
		
		
		block = new Block()
		.texture(block(c))
		.title(materialConcatenate("mattype-block"))
		.volumed(0.0125)
		.finish("block."+id);
		cluster = new Item()
		.title(materialConcatenate("mattype-cluster"))
		.texture(cluster(c))
		.volumed(0.005)
		.finish("fcluster."+id);
		base = createBase(c, id);
		frag = new Item()
		.title(materialConcatenate("mattype-frag"))
		.texture(fragment(c))
		.volumed(0.0005)
		.finish("frag."+id);
		nugget = new Item()
		.title(materialConcatenate("mattype-nugget"))
		.texture(TexGen.nugget(c))
		.volumed(0.000125)
		.finish("nugget."+id);
				
		wire = new Item()
		.title(materialConcatenate("mattype-wire"))
		.texture(TexGen.wire(c))
		.volumed(0.00125)
		.finish("wirespool."+id);
		megadust = new Item()
		.title(materialConcatenate("mattype-megadust"))
		.texture(ldust(c))
		.volumed(0.0005)
		.finish("ldust."+id);
		dust = new Item()
		.title(materialConcatenate("mattype-dust"))
		.texture(dust(c))
		.volumed(0.00125)
		.finish("dust."+id);
		smalldust = new Item()
		.title(materialConcatenate("mattype-smalldust"))
		.texture(mdust(c))
		.volumed(0.005)
		.finish("mdust."+id);
		tinydust = new Item()
		.title(materialConcatenate("mattype-tinydust"))
		.texture(minidust(c))
		.volumed(0.000125)
		.finish("minidust."+id);
		
		panel = new Item()
		.title(materialConcatenate("mattype-panel"))
		.texture(panel(c))
		.volumed(0.00125)
		.finish("panel."+id);
		foil = new Item()
		.title(materialConcatenate("mattype-foil"))
		.texture(foil(c))
		.volumed(0.000125)
		.finish("foil."+id);
		gear = new Item()
		.title(materialConcatenate("mattype-gear"))
		.texture(gear(c))
		.volumed(0.00125)
		.finish("gear."+id);
		
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
		
		
		
		//Index
		byID0.put(id, this);
		byID0sort.put(id, this);
		byVoltage0.put(volt, this);
	}
	/**
	 * @param c color
	 * @param id material ID
	 * @param t_nominative material title
	 * @return the created base item
	 */
	@Nonnull public Item createBase(Color c, String id) {
		if(isGem) {
			return new Item()
				.title(t_basic)
				.texture(gem(c))
				.volumed(0.00125)
				.finish("gem."+id);
		}
		return new Item()
		.title(materialConcatenate("mattype-ingot"))
		.texture(TexGen.ingot(c))
		.volumed(0.00125)
		.finish("ingot."+id);
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
	
	public static final boolean order = Boolean.parseBoolean($res("material-grammar"));
	@Nonnull public String materialConcatenate(String matname) {
		String mattype = $res(matname);
		if(order) return mattype+" "+t_nominative;
		return t_nominative+" "+mattype;
	}
	public String materialConcatenateShort(String matname) {
		String mattype = $res(matname);
		if(order) return mattype+" "+t_nominative_short;
		return t_nominative_short+" "+mattype;
	}
	@Nonnull protected static BufferedImage gem(Color c) {
		return TexGen.genTexture(c, GEM, null);
	}
}
