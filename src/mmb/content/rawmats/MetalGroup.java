/**
 * 
 */
package mmbgame.rawmats;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mmbeng.block.Block;
import mmbeng.item.Item;
import mmbeng.item.ItemEntry;
import mmbeng.item.Items;
import mmbeng.java2d.TexGen;
import mmbeng.mods.GameLoader;
import mmbeng.texture.Textures;
import mmbgame.ContentsItems;
import mmbgame.CraftingGroups;
import mmbgame.electric.VoltageTier;
import mmbgame.machinemics.manual.Crafting;

import static mmbeng.GlobalSettings.*;
import static mmbgame.ContentsItems.frame1;

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
	@Nonnull public final Item rod;
	@Nonnull public final Item ring;
	@Nonnull public final Item sheet;
	@Nonnull public final Item frame;
	
	@Nonnull public final Color c;
	@Nonnull public final String id;
			 public final boolean isGem;
	
	@Nonnull public final String t_nominative;
	@Nonnull public final String t_nominative_short;
	@Nonnull public final String t_basic;
	@Nonnull public final String t_adjective;
	@Nonnull public final VoltageTier volt;
	         public final double baseCost;
	@Nonnull static final BufferedImage GEM = Textures.get("item/gem.png");
	
	/**
	 * @param c display color
	 * @param id material ID
	 * @param volt minimum voltage tier for recipes
	 * @param baseCost base cost of smelting in joules
	 * @param isGem is a given material a gem?
	 */
	public MetalGroup(Color c, String id, VoltageTier volt, double baseCost, boolean isGem) {
		this.baseCost = baseCost;
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
		.volumed(0.005)
		.finish("ldust."+id);
		dust = new Item()
		.title(materialConcatenate("mattype-dust"))
		.texture(dust(c))
		.volumed(0.00125)
		.finish("dust."+id);
		smalldust = new Item()
		.title(materialConcatenate("mattype-smalldust"))
		.texture(mdust(c))
		.volumed(0.0005)
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
		rod = new Item()
		.title(materialConcatenate("mattype-rod"))
		.texture(rod(c))
		.volumed(0.00125)
		.finish("rod."+id);
		ring = new Item()
		.title(materialConcatenate("mattype-ring"))
		.texture(ring(c))
		.volumed(0.00125)
		.finish("ring."+id);
		sheet = new Item()
		.title(materialConcatenate("mattype-sheet"))
		.texture(sheet(c))
		.volumed(0.0005)
		.finish("sheet."+id);
		frame = new Item()
		.title(materialConcatenate("mattype-frame"))
		.texture(frame(c))
		.volumed(0.02)
		.finish("frame."+id);
		
		GameLoader.onIntegRun(this::recipes);
		
		//Tagging
		Items.tagItems("material-"+id, block, cluster, base, frag, nugget, wire, megadust, dust, smalldust, tinydust, panel, foil, gear, rod, ring, sheet, frame);
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
		Items.tagItem("shape-rod", rod);
		Items.tagItem("shape-ring", ring);
		Items.tagItem("shape-sheet", sheet);
		Items.tagItem("shape-frame", frame);
		
		//Index
		byID0.put(id, this);
		byID0sort.put(id, this);
		byVoltage0.put(volt, this);
	}
	private void recipes() {
		//Recipes
			CraftingGroups.crusher.add(base, dust, volt, baseCost/2);
			CraftingGroups.crusher.add(panel, dust, volt, baseCost/2);
			CraftingGroups.crusher.add(gear, dust, volt, baseCost/2);
			CraftingGroups.crusher.add(nugget, tinydust, volt, baseCost/32);
			CraftingGroups.crusher.add(foil, tinydust, volt, baseCost/32);
			CraftingGroups.crusher.add(frag, smalldust, volt, baseCost/8);
			CraftingGroups.crusher.add(cluster, megadust, volt, baseCost/2);
			CraftingGroups.crusher.add(sheet, smalldust, volt, baseCost/8);
			CraftingGroups.crusher.add(rod, smalldust, volt, baseCost/8);
			CraftingGroups.crusher.add(ring, smalldust, volt, baseCost/8);
			CraftingGroups.crusher.add(frame, megadust, 2, volt, baseCost);
			
			CraftingGroups.smelting.add(dust, base, volt, baseCost/2);
			CraftingGroups.smelting.add(tinydust, nugget, volt, baseCost/32);
			CraftingGroups.smelting.add(megadust, cluster, volt, baseCost*2);
			CraftingGroups.smelting.add(smalldust, frag, volt, baseCost/8);
			
			CraftingGroups.clusterMill.add(base, panel, volt, baseCost/2);
			CraftingGroups.clusterMill.add(nugget, foil, volt, baseCost/32);
			CraftingGroups.clusterMill.add(panel, foil, 16, volt, baseCost/2);
			
			CraftingGroups.crafting.addRecipeGrid(new ItemEntry[] {
					null, frag, null,
					frag, null, frag,
					null, frag, null
			}, 3, 3, gear);
			
			//Nugget <-> frag
			CraftingGroups.crafting.addRecipeGrid(nugget, 2, 2, frag);
			CraftingGroups.crafting.addRecipe(frag, nugget, 4);
			CraftingGroups.splitter.add(frag,            nugget, 4, volt, baseCost/64);
			CraftingGroups.combiner.add(nugget.stack(4), frag,      volt, baseCost/64);
			
			//Frag <-> base
			CraftingGroups.crafting.addRecipeGrid(frag, 2, 2, base);
			//No crafting base -> fragment
			CraftingGroups.splitter.add(base,          frag, 4, volt, baseCost/16);
			CraftingGroups.combiner.add(frag.stack(4), base,    volt, baseCost/16);
			
			//Base <-> cluster
			CraftingGroups.crafting.addRecipeGrid(base, 2, 2, cluster);
			CraftingGroups.crafting.addRecipe(cluster, base, 4);
			CraftingGroups.splitter.add(cluster,       base, 4, volt, baseCost/4);
			CraftingGroups.combiner.add(base.stack(4), cluster, volt, baseCost/4);
			
			//Block <-> cluster
			CraftingGroups.crafting.addRecipeGrid(cluster, 2, 2, block);
			//No crafting block -> cluster
			CraftingGroups.splitter.add(block,            cluster, 4, volt, baseCost);
			CraftingGroups.combiner.add(cluster.stack(4), block,      volt, baseCost);
			
			//SmallDust <-> TinyDust
			CraftingGroups.crafting.addRecipeGrid(tinydust, 2, 2, smalldust);
			CraftingGroups.crafting.addRecipe(smalldust, tinydust, 4);
			CraftingGroups.splitter.add(smalldust,         tinydust, 4, volt, baseCost/256);
			CraftingGroups.combiner.add(tinydust.stack(4), smalldust,   volt, baseCost/256);
			
			//Dust <-> SmallDust
			CraftingGroups.crafting.addRecipeGrid(smalldust, 2, 2, dust);
			//No crafting recipe Dust -> SmallDust
			CraftingGroups.splitter.add(dust,               smalldust, 4, volt, baseCost/64);
			CraftingGroups.combiner.add(smalldust.stack(4), dust,         volt, baseCost/64);
			
			//MegaDust <-> Dust
			CraftingGroups.crafting.addRecipeGrid(dust, 2, 2, megadust);
			CraftingGroups.crafting.addRecipe(megadust, dust, 4);
			CraftingGroups.splitter.add(megadust,      dust, 4,  volt, baseCost/16);
			CraftingGroups.combiner.add(dust.stack(4), megadust, volt, baseCost/16);
			
			//WireMill
			CraftingGroups.wiremill.add(nugget, wire, volt, baseCost/32);
					
			Crafting.ingotNugget(base, nugget);
			Crafting.ingotNugget(block, base);
			Crafting.ingotNugget(dust, tinydust);
			
			//Rod
			CraftingGroups.extruder.add(frag, rod, ContentsItems.bearing1, volt, baseCost/8);
			//Ring
			CraftingGroups.extruder.add(frag, ring, ContentsItems.rod1, volt, baseCost/8);
			//Sheet
			CraftingGroups.clusterMill.add(frag, sheet, volt, baseCost/8);
			//Frame
			CraftingGroups.crafting.addRecipeGrid(new ItemEntry[]{
			base, panel, base,
			panel,  null, panel,
			base, panel, base
			}, 3, 3, frame);
	}
	
	/**
	 * @param c color
	 * @param id material ID
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
	@Nonnull private static final BufferedImage ROD = Textures.get("item/rod.png");
	@Nonnull private static final BufferedImage RING = Textures.get("item/ring 1.png");
	@Nonnull private static final BufferedImage SHEET = Textures.get("item/sheet.png");
	@Nonnull private static final BufferedImage FRAME = Textures.get("item/frame 1.png");
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
	@Nonnull protected static BufferedImage rod(Color c) {
		return TexGen.genTexture(c, ROD, null);
	}
	@Nonnull protected static BufferedImage ring(Color c) {
		return TexGen.genTexture(c, RING, null);
	}
	@Nonnull protected static BufferedImage sheet(Color c) {
		return TexGen.genTexture(c, SHEET, null);
	}
	@Nonnull protected static BufferedImage frame(Color c) {
		return TexGen.genTexture(c, FRAME, null);
	}
	@Nonnull protected static BufferedImage gem(Color c) {
		return TexGen.genTexture(c, GEM, null);
	}

	/**
	 * Represent a stack of a material,
	 * Used for alloying recipes
	 * @author oskar
	 */
	public static class MaterialStack{
		/** The stacked material */
		@Nonnull public final MetalGroup material;
		/** Amount of material stacked */
		public final int amount;
		/**
		 * Creates a material stack
		 * @param material
		 * @param amount
		 */
		public MaterialStack(MetalGroup material, int amount) {
			this.material = material;
			this.amount = amount;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + amount;
			result = prime * result + material.hashCode();
			return result;
		}
		@Override
		public boolean equals(@Nullable Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			MaterialStack other = (MaterialStack) obj;
			if (amount != other.amount)
				return false;
			else if (!material.equals(other.material))
				return false;
			return true;
		}
		@Override
		public String toString() {
			return "MaterialStack " + material + "*" + amount;
		}
		
	}
	
	/**
	 * Creates a material stack
	 * @param amount
	 * @return a new material stack
	 */
	public final MaterialStack stack(int amount) {
		return new MaterialStack(this, amount);
	}
	
	/**
	 * The orderting of the elements
	 */
	public static final boolean order = Boolean.parseBoolean($res("material-grammar"));
	@Nonnull public String materialConcatenate(String matname) {
		String mattype = $res(matname);
		if(order) return mattype+" "+t_nominative;
		return t_nominative+" "+mattype;
	}
	@Nonnull public String materialConcatenateShort(String matname) {
		String mattype = $res(matname);
		if(order) return mattype+" "+t_nominative_short;
		return t_nominative_short+" "+mattype;
	}
	
	@Override
	public String toString() {
		return "MetalGroup [" + id + "] " + t_basic;
	}
	
	//Indexing
	private static final Map<String, MetalGroup> byID0 = new HashMap<>();
	public static final Map<String, MetalGroup> byID = Collections.unmodifiableMap(byID0);
	private static final NavigableMap<String, MetalGroup> byID0sort = new TreeMap<>();
	public static final NavigableMap<String, MetalGroup> byIDsort = Collections.unmodifiableNavigableMap(byID0sort);
	private static final Map<VoltageTier, MetalGroup> byVoltage0 = new EnumMap<>(VoltageTier.class);
	public static final Map<VoltageTier, MetalGroup> byVoltage = Collections.unmodifiableMap(byVoltage0);
	
}
