/**
 * 
 */
package mmb.content;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.LookupOp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import mmb.DeprecatedExtra;
import mmb.annotations.NN;
import mmb.content.agro.Agro;
import mmb.content.aim.ToolAim;
import mmb.content.ditems.ItemBOM;
import mmb.content.ditems.ItemPIngredients;
import mmb.content.ditems.Stencil;
import mmb.content.electric.ItemBattery;
import mmb.content.electric.VoltageTier;
import mmb.content.electric.VoltagedItemGroup;
import mmb.content.imachine.SpeedUpgrade;
import mmb.content.imachine.extractor.ConfigureDroppedItemExtractors;
import mmb.content.machinemics.pack.Pack;
import mmb.content.pickaxe.Pickaxe;
import mmb.content.pickaxe.Pickaxe.PickaxeType;
import mmb.engine.item.Item;
import mmb.engine.item.ItemType;
import mmb.engine.item.ItemRaw;
import mmb.engine.item.Items;
import mmb.engine.java2d.ColorMapper;
import mmb.engine.java2d.TexGen;
import mmb.engine.settings.GlobalSettings;
import mmb.engine.texture.Textures;
import mmb.materials.Materials;

/**
 * @author oskar
 *
 */
public class ContentsItems {
	
	private ContentsItems() {}
	/** Initializes items */
	public static void init() {/* just for initialization */}
	
	//Misc
	@NN public static final Item leaf = new Item()
		.title("#leaf")
		.texture("item/lisc.png")
		.volumed(0.000125)
		.finish("plant.leaf");
	@NN public static final Item rrubber = new Item()
		.title("#rrubber")
		.texture("item/rubber.png")
		.volumed(0.00125)
		.finish("plant.rubber");
	
	/** A brittle and transparent material, used to make bottles and breweries*/
	@NN public static final Item glass = new Item()
		.title("#glass")
		.texture("item/glass.png")
		.volumed(0.004)
		.finish("item.glass");
	@NN public static final Item sdraconium = shard("#shard-draconic", Materials.colorDraconium, "item.draconic");
	@NN public static final Item sadraconium = shard("#shard-adraconic", Materials.colorADraconium, "item.adraconic");
	@NN public static final Item schaotium = shard("#shard-chaotic", Materials.colorChaotium, "item.chaotic");
	@NN public static final Item scrystal = shard("#shard-crystal", Materials.colorCrystal, "item.crystal");
	@NN public static final Item sstellar = shard("#shard-stellar", Materials.colorADraconium, "item.stellar");
	@NN public static final Item sunobtainium = shard("#shard-unobtainium", Materials.colorChaotium, "item.unobtainium");
	@NN public static final Item glassp = new Item()
		.title("#glassp")
		.texture("item/glass panel.png")
		.volumed(0.004)
		.finish("item.glassp");
	
	//Tools
	/** A short-life pickaxe given at beginning of the game */
	@NN public static final PickaxeType pickVW = Pickaxe.create(120, 15, "item/wood pick.png", "#pick-b", "pick.weak");
	/** The most basic pickaxe available in the game */
	@NN public static final PickaxeType pickWood = Pickaxe.create(100, 100, "item/wood pick.png", "#pick-wood", "pick.wood");
	/** An improved pickaxe available later */
	@NN public static final PickaxeType pickRudimentary = Pickaxe.create(50, 200, "item/rudimentary pick.png", "#pick-rud", "pick.rudimentary");
	/** An improved pickaxe made of stone */
	@NN public static final PickaxeType pickStone = Pickaxe.create(50, 150, "item/stone pick.png", "#pick-stone", "pick.stone");
	/** A mid-tier pickaxe */
	@NN public static final PickaxeType pickIron = Pickaxe.create(32, 250, "item/iron pick.png", "#pick-iron", "pick.iron");
	/** An advanced pickaxe */
	@NN public static final PickaxeType pickSteel = Pickaxe.create(20, 350, "item/steel pick.png", "#pick-steel", "pick.steel");
	/** The definitive pickaxe */
	@NN public static final PickaxeType pickStainless = Pickaxe.create(14, 600, "item/stainless pick.png", "#pick-stainless", "pick.stainless");
	
	/** Configure resizable machines (dropped item extractors and diggers)*/
	@NN public static final Item configExtractors= new TooledItem(new ConfigureDroppedItemExtractors())
		.title("#cdie")
		.texture("hoover.png")
		.finish("mmb.cdie");
	/** Configure aimable machines (now only power towers*/
	@NN public static final Item aim = new TooledItem(new ToolAim())
		.title("#aim")
		.texture("aim.png")
		.finish("mmb.aim");
	
	//Crafting aids
	/** Specifies a crafting recipe in an item form*/
	@NN public static final ItemType stencil = new ItemType()
			.title("#stencil")
			.texture("item/stencil.png")
			.volumed(0.001)
			.factory(Stencil::new)
			.finish("crafting.cstencil");
	/** Lists items in an item form*/
	@NN public static final ItemType BOM = new ItemType()
			.title("#BOM")
			.texture("item/list.png")
			.volumed(0.001)
			.factory(ItemBOM::new)
			.finish("crafting.BOMItems");
	/** Lists both inputs and outputs of a recipe*/
	@NN public static final ItemType pingredients = new ItemType()
			.title("#pingredients")
			.texture("item/slist.png")
			.volumed(0.001)
			.factory(ItemPIngredients::new)
			.finish("crafting.ingredients");
	/** A basic writing material ,used to create stencils and BOMs*/
	@NN public static final Item paper = new Item()
			.title("#paper")
			.texture("item/paper.png")
			.volumed(0.001)
			.finish("mmb.paper");
	
	//Machine parts
	/** A machine part */
	@NN public static final VoltagedItemGroup motor = new VoltagedItemGroup("parts/motor.png", "motor");
	/** A machine part */
	@NN public static final VoltagedItemGroup pump = new VoltagedItemGroup("parts/pump.png", "pump");
	/** A machine part */
	@NN public static final VoltagedItemGroup conveyor = new VoltagedItemGroup("parts/conveyor.png", "conveyor");
	/** A machine part */
	@NN public static final VoltagedItemGroup robot = new VoltagedItemGroup("parts/robot.png", "robot");
	
	//Resource beds
	@NN public static final Item resrc1 = resrcbed(1, Color.RED);
	@NN public static final Item resrc2 = resrcbed(2, Color.ORANGE);
	@NN public static final Item resrc3 = resrcbed(3, Color.YELLOW);
	@NN public static final Item resrc4 = resrcbed(4, Color.GREEN);
	@NN public static final Item resrc5 = resrcbed(5, Color.CYAN);
	@NN public static final Item resrc6 = resrcbed(6, Color.BLUE);
	@NN public static final Item resrc7 = resrcbed(7, Color.MAGENTA);
	
	//Packaged items
	@NN public static final ItemType pack = new ItemType()
		.title("#ipack1")
		.texture("item/package.png")
		.volumed(0.001)
		.factory(Pack::new)
		.finish("boxed.packItem");
	
	//Deprecated items
	//empty
	
	static {
		Items.tagItems("tool", pickVW, pickWood, pickRudimentary, pickStone, pickIron, pickSteel, pickStainless, configExtractors, aim);
		Items.tagItems("agro", Agro.yeast, Agro.hops, Agro.seeds);
		Items.tagItems("material-glass", glass, glassp);
		Items.tagItems("craftaid", stencil, BOM, pingredients);
		Items.tagItems("shape-shard", sdraconium, sadraconium, schaotium, scrystal, sstellar, sunobtainium);
		Items.tagItems("resrcbed", resrc1, resrc2, resrc3, resrc4, resrc5, resrc6, resrc7);
		
		Items.deprecate("industry.motor1", motor.items.get(0));
		Items.deprecate("industry.motor2", motor.items.get(1));
		Items.deprecate("pickHead.wood", ItemRaw.make(pickWood));
		Items.deprecate("pickHead.rudimentary", ItemRaw.make(pickRudimentary));
	}

	@NN private static Item resrcbed(int n, Color c) {
		BufferedImage textureResrcbed = Textures.get("item/resrcbed.png");
		ColorMapper mapper = ColorMapper.ofType(
				textureResrcbed.getType(), Color.RED, c);
		LookupOp op = new LookupOp(mapper, null);
		BufferedImage img = op.createCompatibleDestImage(textureResrcbed, null);
		op.filter(textureResrcbed, img);
		return new Item()
				.title("#ind-qua"+n)
				.texture(img)
				.volumed(0.00125)
				.finish("industry.resrc"+n);
	}
	@NN private static Item shard(String title, Color c, String id) {
		BufferedImage textureResrcbed = Textures.get("item/shard.png");
		ColorMapper mapper = ColorMapper.ofType(
				textureResrcbed.getType(), Color.RED, c);
		LookupOp op = new LookupOp(mapper, null);
		BufferedImage img = op.createCompatibleDestImage(textureResrcbed, null);
		op.filter(textureResrcbed, img);
		return new Item()
		.title(title)
		.texture(img)
		.volumed(0.004)
		.finish(id);
	}
}
