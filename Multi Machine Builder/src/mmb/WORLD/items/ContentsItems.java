/**
 * 
 */
package mmb.WORLD.items;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.LookupOp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import mmb.GlobalSettings;
import mmb.DATA.contents.Textures;
import mmb.GRAPHICS.awt.ColorMapper;
import mmb.WORLD.blocks.machine.pack.Pack;
import mmb.WORLD.item.Item;
import mmb.WORLD.item.ItemEntityType;
import mmb.WORLD.item.Items;
import mmb.WORLD.items.data.ItemBOM;
import mmb.WORLD.items.data.Stencil;
import mmb.WORLD.items.filter.EntryFilter;
import mmb.WORLD.items.pickaxe.Pickaxe;
import mmb.WORLD.items.pickaxe.Pickaxe.PickaxeType;

/**
 * @author oskar
 *
 */
public class ContentsItems {
	
	private ContentsItems() {}
	/** Initializes items */
	public static void init() {/* just for initialization */}
		
	//Pickaxe heads
	@Nonnull public static final Item pickHeadWood = new Item()
			.title("#pickh-wood")
			.texture("item/wood pick head.png")
			.volumed(0.00125)
			.finish("pickHead.wood");
	@Nonnull public static final Item pickHeadRudimentary = new Item()
			.title("#pickh-rud")
			.texture("item/rudimentary pick head.png")
			.volumed(0.00125)
			.finish("pickHead.rudimentary");
	
	//Misc
	@Nonnull public static final Item leaf = new Item()
		.title("#leaf")
		.texture("item/lisc.png")
		.volumed(0.000125)
		.finish("plant.leaf");
	@Nonnull public static final Item alcopod = new AlcoPod()
		.title("AlcoPod")
		.texture("item/alcopod.png")
		.volumed(0.000125)
		.finish("drugs.alcopod");
	
	//Tools
	@Nonnull public static final PickaxeType pickVW = Pickaxe.create(120, 15, "item/wood pick.png", "#pick-b", "pick.weak");
	@Nonnull public static final PickaxeType pickWood = Pickaxe.create(100, 100, "item/wood pick.png", "#pick-wood", "pick.wood");
	@Nonnull public static final PickaxeType pickRudimentary = Pickaxe.create(50, 400, "item/rudimentary pick.png", "#pick-rud", "pick.rudimentary");
	@Nonnull public static final Item bucket = new Bucket()
			.title("#bucket")
			.texture("dropItems.png")
			.finish("mmb.bucket");
	@Nonnull public static final Item configExtractors= new ConfigExtractors()
			.title("#cdie")
			.texture("hoover.png")
			.finish("mmb.cdie");
	
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
	@Nonnull public static final Item motor1 = new Item()
		.title("#ind-mot1")
		.texture("item/motor 1.png")
		.volumed(0.00125)
		.finish("industry.motor1");
	@Nonnull public static final Item motor2 = new Item()
		.title("#ind-mot2")
		.texture("item/motor 2.png")
		.volumed(0.00125)
		.finish("industry.motor2");
	
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
	@Nonnull public static final Item circuit0 = new Item()
			.title("#ind-circ0")
			.texture("item/circuit 0.png")
			.volumed(0.00625)
			.finish("industry.processor0");
	@Nonnull public static final Item substrate0 = new Item()
			.title("#ind-sub0")
			.texture("item/substrate 0.png")
			.volumed(0.00125)
			.finish("industry.substrate0");
	@Nonnull public static final Item circuit1 = new Item()
			.title("#ind-circ1")
			.texture("item/circuit 1.png")
			.volumed(0.00625)
			.finish("industry.processor1");
	@Nonnull public static final Item substrate1 = new Item()
			.title("#ind-sub1")
			.texture("item/substrate 1.png")
			.volumed(0.00125)
			.finish("industry.substrate1");
	
	//Resource beds
	@Nonnull public static final Item resrc1 = new Item()
			.title("#ind-qua1")
			.texture("item/resrcbed.png")
			.volumed(0.00125)
			.finish("industry.resrc1");

	//Packaged items
	@Nonnull public static final ItemEntityType pack = new ItemEntityType()
		.title("#ipack1")
		.texture("item/package.png")
		.volumed(0.001)
		.factory(Pack::createEmpty)
		.finish("boxed.packItem");
	
	//Item filters
	@Nonnull public static final ItemEntityType ifilterEntries =new ItemEntityType()
		.title("#filt-ient")
		.texture("item/filter entry.png")
		.volumed(0.001)
		.factory(EntryFilter::new)
		.finish("filter.ientries");
	static {
		Items.tagItems("shape-pickhead", pickHeadWood, pickHeadRudimentary);
		Items.tagItems("tool", pickVW, pickWood, pickRudimentary, bucket, configExtractors);
		Items.tagItems("craftcode", craftcodes);
		Items.tagItems("parts-electronic", resistor, capacitor, inductor, diode, transistor, IC, circuit0, substrate0, circuit1, substrate1);
	}
}
