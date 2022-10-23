/**
 * 
 */
package mmb.world.items;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.LookupOp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import mmb.GlobalSettings;
import mmb.data.contents.Textures;
import mmb.graphics.awt.ColorMapper;
import mmb.graphics.texgen.TexGen;
import mmb.menu.wtool.ConfigureDroppedItemExtractors;
import mmb.menu.wtool.DumpItems;
import mmb.menu.wtool.ToolAim;
import mmb.world.blocks.machine.pack.Pack;
import mmb.world.contentgen.Materials;
import mmb.world.crafting.RecipeOutput;
import mmb.world.electric.VoltageTier;
import mmb.world.item.Item;
import mmb.world.item.ItemEntityType;
import mmb.world.item.ItemRaw;
import mmb.world.item.Items;
import mmb.world.items.data.ItemBOM;
import mmb.world.items.data.ItemPIngredients;
import mmb.world.items.data.Stencil;
import mmb.world.items.electric.ItemBattery;
import mmb.world.items.filter.EntryFilter;
import mmb.world.items.pickaxe.Pickaxe;
import mmb.world.items.pickaxe.Pickaxe.PickaxeType;

/**
 * @author oskar
 *
 */
public class ContentsItems {
	
	private ContentsItems() {}
	/** Initializes items */
	public static void init() {/* just for initialization */}
	
	//Misc
	/*
	 * +-----------------------------------------------------+
	 * |REAL-LIFE ALCOHOL CONSUMPTION IS HARMFUL             |
	 * |½ LITER OF BEER CONTAINS 25g OF ETHYL ALCOHOL        |
	 * |SALE OF LIQUOR TO PEOPLE BELOW 18y IS A CRIME        |
	 * |EVEN THAT AMOUNT HARMS HEALTH OF PREGNANT WOMEN      |
	 * |AND IS DANGEROUS TO DRIVERS                          |
	 * +-----------------------------------------------------+
	 */
	@Nonnull public static final Item leaf = new Item()
		.title("#leaf")
		.texture("item/lisc.png")
		.volumed(0.000125)
		.finish("plant.leaf");
	@Nonnull public static final Item rrubber = new Item()
		.title("#rrubber")
		.texture("item/rubber.png")
		.volumed(0.00125)
		.finish("plant.rubber");
	@Nonnull public static final AlcoPod alcopod = (AlcoPod) new AlcoPod(1, RecipeOutput.NONE)
		.title("AlcoPod")
		.texture("item/alcopod.png")
		.volumed(0.000125)
		.finish("drugs.alcopod");
	@Nonnull public static final Item beerEmpty = new Item()
		.title("#beerb")
		.texture("item/beer empty.png")
		.volumed(0.004)
		.finish("drugs.beer0");
	@Nonnull public static final AlcoPod beer = (AlcoPod) new AlcoPod(1.5, beerEmpty)
		.title("#beer")
		.texture("item/beer.png")
		.volumed(0.004)
		.finish("drugs.beer");
	@Nonnull public static final Item glass = new Item()
		.title("#glass")
		.texture("item/glass.png")
		.volumed(0.004)
		.finish("item.glass");
	@Nonnull public static final Item sdraconium = shard("#shard-draconic", Materials.colorDraconium, "item.draconic");
	@Nonnull public static final Item sadraconium = shard("#shard-adraconic", Materials.colorADraconium, "item.adraconic");
	@Nonnull public static final Item schaotium = shard("#shard-chaotic", Materials.colorChaotium, "item.chaotic");
	@Nonnull public static final Item scrystal = shard("#shard-crystal", Materials.colorCrystal, "item.crystal");
	@Nonnull public static final Item sstellar = shard("#shard-stellar", Materials.colorADraconium, "item.stellar");
	@Nonnull public static final Item sunobtainium = shard("#shard-unobtainium", Materials.colorChaotium, "item.unobtainium");
	@Nonnull public static final Item glassp = new Item()
		.title("#glassp")
		.texture("item/glass panel.png")
		.volumed(0.004)
		.finish("item.glassp");
	@Nonnull public static final Item yeast = new Item()
			.title("#yeast")
			.texture("item/yeast.png")
			.volumed(0.001)
			.finish("item.yeast");
	@Nonnull public static final Item hops = new Item()
			.title("#hops")
			.texture("item/hops.png")
			.volumed(0.001)
			.finish("item.hops");
	@Nonnull public static final Item seeds = new Item()
		.title("#seeds")
		.texture("item/seeds.png")
		.volumed(0.002)
		.finish("item.seeds");
	
	//Tools
	@Nonnull public static final PickaxeType pickVW = Pickaxe.create(120, 15, "item/wood pick.png", "#pick-b", "pick.weak");
	@Nonnull public static final PickaxeType pickWood = Pickaxe.create(100, 100, "item/wood pick.png", "#pick-wood", "pick.wood");
	@Nonnull public static final PickaxeType pickRudimentary = Pickaxe.create(50, 400, "item/rudimentary pick.png", "#pick-rud", "pick.rudimentary");
	@Nonnull public static final Item bucket = new TooledItem(new DumpItems())
			.title("#bucket")
			.texture("dropItems.png")
			.finish("mmb.bucket");
	@Nonnull public static final Item configExtractors= new TooledItem(new ConfigureDroppedItemExtractors())
			.title("#cdie")
			.texture("hoover.png")
			.finish("mmb.cdie");
	@Nonnull public static final Item aim = new TooledItem(new ToolAim())
			.title("#aim")
			.texture("aim.png")
			.finish("mmb.aim");
	
	//Crafting aids
	@Nonnull public static final ItemEntityType stencil = new ItemEntityType()
			.title("#stencil")
			.texture("item/stencil.png")
			.volumed(0.001)
			.factory(Stencil::new)
			.finish("crafting.cstencil");
	@Nonnull public static final ItemEntityType BOM = new ItemEntityType()
			.title("#BOM")
			.texture("item/list.png")
			.volumed(0.001)
			.factory(ItemBOM::new)
			.finish("crafting.BOMItems");
	@Nonnull public static final ItemEntityType pingredients = new ItemEntityType()
			.title("#pingredients")
			.texture("item/slist.png")
			.volumed(0.001)
			.factory(ItemPIngredients::new)
			.finish("crafting.ingredients");
	@Nonnull public static final Item paper = new Item()
			.title("#paper")
			.texture("item/paper.png")
			.volumed(0.001)
			.finish("mmb.paper");
	@Nonnull public static final List<@Nonnull Item> craftcodes = createCraftCodes(); //NOSONAR the returned list is immutable
	@Nonnull private static List<@Nonnull Item> createCraftCodes(){
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
	@Nonnull public static final VoltagedItemGroup motor = new VoltagedItemGroup("parts/motor.png", "motor");
	@Nonnull public static final VoltagedItemGroup pump = new VoltagedItemGroup("parts/pump.png", "pump");
	@Nonnull public static final VoltagedItemGroup conveyor = new VoltagedItemGroup("parts/conveyor.png", "conveyor");
	@Nonnull public static final VoltagedItemGroup robot = new VoltagedItemGroup("parts/robot.png", "robot");
	@Nonnull public static final Item frame1 = new Item()
		.title("#ind-frame1")
		.texture("item/frame 1.png")
		.finish("industry.frame1");
	@Nonnull public static final Item rod1 = new Item()
		.title("#ind-rod1")
		.texture("item/steel rod.png")
		.volumed(0.00125)
		.finish("industry.rod1");
	@Nonnull public static final Item bearing1 = new Item()
		.title("#ind-ring1")
		.texture("item/ring 1.png")
		.volumed(0.00125)
		.finish("industry.bearing1");
	
	//Electronic parts
	@Nonnull public static final Item resistor = new Item()
			.title("#ind-res1")
			.texture("item/resistor.png")
			.volumed(0.00125)
			.finish("industry.resistor1");
	@Nonnull public static final Item resistors = new Item()
			.title("#ind-resa1")
			.texture("item/resistor array.png")
			.volumed(0.00625)
			.finish("industry.resistors1");
	@Nonnull public static final Item capacitor = new Item()
			.title("#ind-cap1")
			.texture("item/capacitor.png")
			.volumed(0.00125)
			.finish("industry.capacitor1");
	@Nonnull public static final Item inductor = new Item()
			.title("#ind-ind1")
			.texture("item/inductor.png")
			.volumed(0.00125)
			.finish("industry.inductor1");
	@Nonnull public static final Item diode = new Item()
			.title("#ind-dio1")
			.texture("item/diode.png")
			.volumed(0.00125)
			.finish("industry.diode1");
	@Nonnull public static final Item transistor = new Item()
			.title("#ind-tra1")
			.texture("item/transistor.png")
			.volumed(0.00125)
			.finish("industry.transistor1");
	@Nonnull public static final Item IC = new Item()
			.title("#ind-ic1")
			.texture("item/IC.png")
			.volumed(0.00125)
			.finish("industry.IC1");
	
	@Nonnull public static final Item circuit0 = circuit(0);
	@Nonnull public static final Item substrate0 = substrate(0);
	@Nonnull public static final Item circuit1 = circuit(1);
	@Nonnull public static final Item substrate1 = substrate(1);
	@Nonnull public static final Item circuit2 = circuit(2);
	@Nonnull public static final Item substrate2 = substrate(2);
	@Nonnull public static final Item circuit3 = circuit(3);
	@Nonnull public static final Item substrate3 = substrate(3);
	@Nonnull public static final Item circuit4 = circuit(4);
	@Nonnull public static final Item substrate4 = substrate(4);
	@Nonnull public static final Item circuit5 = circuit(5);
	@Nonnull public static final Item substrate5 = substrate(5);
	@Nonnull public static final Item circuit6 = circuit(6);
	@Nonnull public static final Item substrate6 = substrate(6);
	@Nonnull public static final Item circuit7 = circuit(7);
	@Nonnull public static final Item substrate7 = substrate(7);
	@Nonnull public static final Item circuit8 = circuit(8);
	@Nonnull public static final Item substrate8 = substrate(8);
	@Nonnull public static final Item circuit9 = circuit(9);
	@Nonnull public static final Item substrate9 = substrate(9);
	
	//Batteries
	private static final String BATTERY = GlobalSettings.$res("battery");
	@Nonnull public static final ItemEntityType bat1 = battery(VoltageTier.V1);
	@Nonnull public static final ItemEntityType bat2 = battery(VoltageTier.V2);
	@Nonnull public static final ItemEntityType bat3 = battery(VoltageTier.V3);
	@Nonnull public static final ItemEntityType bat4 = battery(VoltageTier.V4);
	@Nonnull public static final ItemEntityType bat5 = battery(VoltageTier.V5);
	@Nonnull public static final ItemEntityType bat6 = battery(VoltageTier.V6);
	@Nonnull public static final ItemEntityType bat7 = battery(VoltageTier.V7);
	
	//Resource beds
	@Nonnull public static final Item resrc1 = resrcbed(1, Color.RED);
	@Nonnull public static final Item resrc2 = resrcbed(2, Color.ORANGE);
	@Nonnull public static final Item resrc3 = resrcbed(3, Color.YELLOW);
	@Nonnull public static final Item resrc4 = resrcbed(4, Color.GREEN);
	@Nonnull public static final Item resrc5 = resrcbed(5, Color.CYAN);
	@Nonnull public static final Item resrc6 = resrcbed(6, Color.BLUE);
	@Nonnull public static final Item resrc7 = resrcbed(7, Color.MAGENTA);
	
	//Speed upgrades
	@Nonnull public static final Item speed1 =  speed(1,  new Color(128,   0,   0), 1.4);
	@Nonnull public static final Item speed2 =  speed(2,  new Color(192,   0,   0), 2.0);
	@Nonnull public static final Item speed3 =  speed(3,  new Color(255,   0,   0), 2.8);
	@Nonnull public static final Item speed4 =  speed(4,  new Color(255,  64,   0), 4.0);
	@Nonnull public static final Item speed5 =  speed(5,  new Color(255, 128,   0), 5.6);
	@Nonnull public static final Item speed6 =  speed(6,  new Color(255, 192,   0), 8.0);
	@Nonnull public static final Item speed7 =  speed(7,  new Color(255, 255,   0), 11.2);
	@Nonnull public static final Item speed8 =  speed(8,  new Color(192, 255,   0), 16.0);
	@Nonnull public static final Item speed9 =  speed(9,  new Color(128, 255,   0), 22.4);
	@Nonnull public static final Item speed10 = speed(10, new Color( 64, 255,   0), 32.0);
	@Nonnull public static final Item speed11 = speed(11, new Color(  0, 255,   0), 44.8);
	@Nonnull public static final Item speed12 = speed(12, new Color(  0, 255,  64), 64.0);
	@Nonnull public static final Item speed13 = speed(13, new Color(  0, 255, 128), 89.6);
	@Nonnull public static final Item speed14 = speed(14, new Color(  0, 255, 192), 128.0);
	@Nonnull public static final Item speed15 = speed(15, new Color(  0, 255, 255), 179.2);
	@Nonnull public static final Item speed16 = speed(16, new Color(  0, 192, 255), 256.0);
	@Nonnull public static final Item speed17 = speed(17, new Color(  0, 128, 255), 358.4);
	@Nonnull public static final Item speed18 = speed(16, new Color(  0,  64, 255), 512.0);
	@Nonnull public static final Item speed19 = speed(17, new Color(  0,   0, 255), 716.8);
	
	//Packaged items
	@Nonnull public static final ItemEntityType pack = new ItemEntityType()
		.title("#ipack1")
		.texture("item/package.png")
		.volumed(0.001)
		.factory(Pack::new)
		.finish("boxed.packItem");
	
	//Item filters
	@Nonnull public static final ItemEntityType ifilterEntries =new ItemEntityType()
		.title("#filt-ient")
		.texture("item/filter entry.png")
		.volumed(0.001)
		.factory(EntryFilter::new)
		.finish("filter.ientries");
	static {
		Items.tagItems("tool", pickVW, pickWood, pickRudimentary, bucket, configExtractors, aim);
		Items.tagItems("craftcode", craftcodes);
		Items.tagItems("parts-electronic", resistor, capacitor, inductor, diode, transistor, IC, resistors);
		Items.tagItems("agro", yeast, hops, seeds);
		Items.tagItems("material-glass", glass, glassp, beerEmpty);
		Items.tagItems("alcohol", alcopod, beer);
		Items.tagItems("machine-battery", bat1, bat2, bat3, bat4, bat5, bat6, bat7);
		Items.tagItem("voltage-ULV", bat1);
		Items.tagItem("voltage-VLV", bat2);
		Items.tagItem("voltage-LV", bat3);
		Items.tagItem("voltage-MV", bat4);
		Items.tagItem("voltage-HV", bat5);
		Items.tagItem("voltage-EV", bat6);
		Items.tagItem("voltage-IV", bat7);
		Items.tagItems("resrcbed", resrc1, resrc2, resrc3, resrc4, resrc5, resrc6, resrc7);
		Items.deprecate("industry.motor1", motor.items.get(0));
		Items.deprecate("industry.motor2", motor.items.get(1));
		Items.deprecate("pickHead.wood", ItemRaw.make(pickWood));
		Items.deprecate("pickHead.rudimentary", ItemRaw.make(pickRudimentary));
	}
	
	//Helper methods
	@Nonnull private static ItemEntityType battery(VoltageTier voltage) {
		ItemEntityType type = new ItemEntityType();
		return type
				.title(BATTERY+" "+voltage.name)
				.texture("item/battery "+(voltage.ordinal()+1)+".png")
				.volumed(0.1)
				.factory(() -> new ItemBattery(type, voltage))
				.finish("industry.bat"+(voltage.ordinal()+1));
	}
	@Nonnull private static Item circuit(int n) {
		Item item = new Item()
		.title("#ind-circ"+n)
		.texture("item/circuit "+n+".png")
		.volumed(0.00125)
		.finish("industry.processor"+n);
		Items.tagItem("parts-electronic", item);
		return item;
	}
	@Nonnull private static final String speedd1 = GlobalSettings.$res("speedupd1");
	@Nonnull private static final String speedd2 = GlobalSettings.$res("speedupd2");
	@Nonnull private static final String speedlag = GlobalSettings.$res("speedlag");
	@Nonnull private static Item speed(int n, Color c, double mul) {
		StringBuilder descr = new StringBuilder().append(speedd1).append(' ').append(mul).append(' ').append(speedd2);
		if(mul > 100) descr.append(' ').append(speedlag);
		Item item = new SpeedUpgrade(mul)
		.title(GlobalSettings.$res("speedup")+" "+n)
		.texture(TexGen.colormap(Color.RED, c, Textures.get("item/speed.png"), null))
		.volumed(0.00125)
		.describe(descr.toString())
		.finish("industry.speed"+n);
		Items.tagItem("speed", item);
		return item;
	}
	@Nonnull private static Item substrate(int n) {
		Item item = new Item()
		.title("#ind-sub"+n)
		.texture("item/substrate "+n+".png")
		.volumed(0.00125)
		.finish("industry.substrate"+n);
		Items.tagItem("parts-electronic", item);
		return item;
	}
	@Nonnull private static Item resrcbed(int n, Color c) {
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
	@Nonnull private static Item shard(String title, Color c, String id) {
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
