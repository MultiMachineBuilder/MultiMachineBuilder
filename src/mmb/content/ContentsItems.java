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
import mmb.NN;
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
import mmb.content.rawmats.Materials;
import mmb.engine.item.Item;
import mmb.engine.item.ItemEntityType;
import mmb.engine.item.ItemRaw;
import mmb.engine.item.Items;
import mmb.engine.item.TooledItem;
import mmb.engine.java2d.ColorMapper;
import mmb.engine.java2d.TexGen;
import mmb.engine.settings.GlobalSettings;
import mmb.engine.texture.Textures;

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
	@NN public static final ItemEntityType stencil = new ItemEntityType()
			.title("#stencil")
			.texture("item/stencil.png")
			.volumed(0.001)
			.factory(Stencil::new)
			.finish("crafting.cstencil");
	/** Lists items in an item form*/
	@NN public static final ItemEntityType BOM = new ItemEntityType()
			.title("#BOM")
			.texture("item/list.png")
			.volumed(0.001)
			.factory(ItemBOM::new)
			.finish("crafting.BOMItems");
	/** Lists both inputs and outputs of a recipe*/
	@NN public static final ItemEntityType pingredients = new ItemEntityType()
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
	/** 64 generic purpose color codes*/
	@NN public static final List<@NN Item> craftcodes = createCraftCodes(); //NOSONAR the returned list is immutable
	@NN private static List<@NN Item> createCraftCodes(){
		Item[] items = new Item[64];
		String title = GlobalSettings.$res("ccode")+" ";
		BufferedImage texture = Textures.get("item/component code.png");
		ColorMapper mapper = ColorMapper.ofType(texture.getType(), Color.RED, Color.BLACK);
		LookupOp op = new LookupOp(mapper, null);
		for(int r = 0, i = 0; r < 4; r++) {
			for(int g = 0; g < 4; g++) {
				for(int b = 0; b < 4; b++, i++) {
					Color c = new Color(r*85, g*85, b*85);
					mapper.setTo(c);
					BufferedImage texture0 = op.filter(texture, null);
					Item item = new Item()
							.title(new StringBuilder(title).append(r).append(g).append(b).toString())
							.texture(texture0)
							.volumed(0.001)
							.finish("mmb.ccode"+i);
					items[i] = item;
				}
			}
		}
		return Collections.unmodifiableList(Arrays.asList(items));
	}
	
	//Machine parts
	/** A machine part */
	@NN public static final VoltagedItemGroup motor = new VoltagedItemGroup("parts/motor.png", "motor");
	/** A machine part */
	@NN public static final VoltagedItemGroup pump = new VoltagedItemGroup("parts/pump.png", "pump");
	/** A machine part */
	@NN public static final VoltagedItemGroup conveyor = new VoltagedItemGroup("parts/conveyor.png", "conveyor");
	/** A machine part */
	@NN public static final VoltagedItemGroup robot = new VoltagedItemGroup("parts/robot.png", "robot");
	@NN public static final Item frame1 = new Item()
		.title("#ind-frame1")
		.texture("item/frame 1.png")
		.finish("industry.frame1");
	@NN public static final Item rod1 = new Item()
		.title("#ind-rod1")
		.texture("item/steel rod.png")
		.volumed(0.00125)
		.finish("industry.rod1");
	@NN public static final Item bearing1 = new Item()
		.title("#ind-ring1")
		.texture("item/ring 1.png")
		.volumed(0.00125)
		.finish("industry.bearing1");
	
	//Batteries
	private static final String BATTERY = GlobalSettings.$res("battery");
	@NN public static final ItemEntityType bat1 = battery(VoltageTier.V1);
	@NN public static final ItemEntityType bat2 = battery(VoltageTier.V2);
	@NN public static final ItemEntityType bat3 = battery(VoltageTier.V3);
	@NN public static final ItemEntityType bat4 = battery(VoltageTier.V4);
	@NN public static final ItemEntityType bat5 = battery(VoltageTier.V5);
	@NN public static final ItemEntityType bat6 = battery(VoltageTier.V6);
	@NN public static final ItemEntityType bat7 = battery(VoltageTier.V7);
	
	//Resource beds
	@NN public static final Item resrc1 = resrcbed(1, Color.RED);
	@NN public static final Item resrc2 = resrcbed(2, Color.ORANGE);
	@NN public static final Item resrc3 = resrcbed(3, Color.YELLOW);
	@NN public static final Item resrc4 = resrcbed(4, Color.GREEN);
	@NN public static final Item resrc5 = resrcbed(5, Color.CYAN);
	@NN public static final Item resrc6 = resrcbed(6, Color.BLUE);
	@NN public static final Item resrc7 = resrcbed(7, Color.MAGENTA);
	
	//Speed upgrades
	@NN public static final Item speed1 =  speed(1,  new Color(128,   0,   0), 1.4);
	@NN public static final Item speed2 =  speed(2,  new Color(192,   0,   0), 2.0);
	@NN public static final Item speed3 =  speed(3,  new Color(255,   0,   0), 2.8);
	@NN public static final Item speed4 =  speed(4,  new Color(255,  64,   0), 4.0);
	@NN public static final Item speed5 =  speed(5,  new Color(255, 128,   0), 5.6);
	@NN public static final Item speed6 =  speed(6,  new Color(255, 192,   0), 8.0);
	@NN public static final Item speed7 =  speed(7,  new Color(255, 255,   0), 11.2);
	@NN public static final Item speed8 =  speed(8,  new Color(192, 255,   0), 16.0);
	@NN public static final Item speed9 =  speed(9,  new Color(128, 255,   0), 22.4);
	@NN public static final Item speed10 = speed(10, new Color( 64, 255,   0), 32.0);
	@NN public static final Item speed11 = speed(11, new Color(  0, 255,   0), 44.8);
	@NN public static final Item speed12 = speed(12, new Color(  0, 255,  64), 64.0);
	@NN public static final Item speed13 = speed(13, new Color(  0, 255, 128), 89.6);
	@NN public static final Item speed14 = speed(14, new Color(  0, 255, 192), 128.0);
	@NN public static final Item speed15 = speed(15, new Color(  0, 255, 255), 179.2);
	@NN public static final Item speed16 = speed(16, new Color(  0, 192, 255), 256.0);
	@NN public static final Item speed17 = speed(17, new Color(  0, 128, 255), 358.4);
	@NN public static final Item speed18 = speed(16, new Color(  0,  64, 255), 512.0);
	@NN public static final Item speed19 = speed(17, new Color(  0,   0, 255), 716.8);
	
	//Packaged items
	@NN public static final ItemEntityType pack = new ItemEntityType()
		.title("#ipack1")
		.texture("item/package.png")
		.volumed(0.001)
		.factory(Pack::new)
		.finish("boxed.packItem");
	
	//Deprecated items
	/** Pick up or drop items */
	@DeprecatedExtra(replacementVer="0.5.1", removal="0.6")
	@Deprecated(since="0.5.1", forRemoval=true)
	/**@deprecated Replaced by extra keybinds in the standard tool */
	@NN public static final Item bucket = new Item()
		.title("#depr-bucket")
		.texture("dropItems.png")
		.finish("mmb.bucket");
	
	static {
		Items.tagItems("tool", pickVW, pickWood, pickRudimentary, pickStone, pickIron, pickSteel, pickStainless, bucket, configExtractors, aim);
		Items.tagItems("craftcode", craftcodes);
		Items.tagItems("agro", Agro.yeast, Agro.hops, Agro.seeds);
		Items.tagItems("material-glass", glass, glassp);
		Items.tagItems("machine-battery", bat1, bat2, bat3, bat4, bat5, bat6, bat7);
		Items.tagItems("craftaid", stencil, BOM, pingredients);
		Items.tagItems("shape-shard", sdraconium, sadraconium, schaotium, scrystal, sstellar, sunobtainium);
		Items.tagItem("voltage-ULV", bat1);
		Items.tagItem("voltage-VLV", bat2);
		Items.tagItem("voltage-LV", bat3);
		Items.tagItem("voltage-MV", bat4);
		Items.tagItem("voltage-HV", bat5);
		Items.tagItem("voltage-EV", bat6);
		Items.tagItem("voltage-IV", bat7);
		Items.tagItems("resrcbed", resrc1, resrc2, resrc3, resrc4, resrc5, resrc6, resrc7);
		Items.tagItem("deprecated", bucket);
		
		Items.deprecate("industry.motor1", motor.items.get(0));
		Items.deprecate("industry.motor2", motor.items.get(1));
		Items.deprecate("pickHead.wood", ItemRaw.make(pickWood));
		Items.deprecate("pickHead.rudimentary", ItemRaw.make(pickRudimentary));
	}
	
	//Helper methods
	@NN private static ItemEntityType battery(VoltageTier voltage) {
		ItemEntityType type = new ItemEntityType();
		return type
				.title(BATTERY+" "+voltage.name)
				.texture("item/battery "+(voltage.ordinal()+1)+".png")
				.volumed(0.1)
				.factory(() -> new ItemBattery(type, voltage))
				.finish("industry.bat"+(voltage.ordinal()+1));
	}
	@NN private static Item speed(int n, Color c, double mul) {
		StringBuilder descr = new StringBuilder().append(GlobalSettings.$res("speedupd1")).append(' ').append(mul).append(' ').append(GlobalSettings.$res("speedupd2"));
		if(mul > 100) descr.append(' ').append(GlobalSettings.$res("speedlag"));
		Item item = new SpeedUpgrade(mul)
		.title(GlobalSettings.$res("speedup")+" "+n)
		.texture(TexGen.colormap(Color.RED, c, Textures.get("item/speed.png"), null))
		.volumed(0.00125)
		.describe(descr.toString())
		.finish("industry.speed"+n);
		Items.tagItem("speed", item);
		return item;
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
