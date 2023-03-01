/**
 * 
 */
package mmb.content.electronics;

import static mmb.content.ContentsItems.paper;
import static mmb.content.CraftingGroups.assembler;
import static mmb.content.CraftingGroups.crafting;
import static mmb.content.CraftingGroups.inscriber;
import static mmb.content.rawmats.Materials.PE;
import static mmb.content.rawmats.Materials.PTFE;
import static mmb.content.rawmats.Materials.PVC;
import static mmb.content.rawmats.Materials.alnico;
import static mmb.content.rawmats.Materials.coal;
import static mmb.content.rawmats.Materials.copper;
import static mmb.content.rawmats.Materials.electrosteel;
import static mmb.content.rawmats.Materials.gold;
import static mmb.content.rawmats.Materials.iridium;
import static mmb.content.rawmats.Materials.iron;
import static mmb.content.rawmats.Materials.nichrome;
import static mmb.content.rawmats.Materials.nickel;
import static mmb.content.rawmats.Materials.platinum;
import static mmb.content.rawmats.Materials.quartz;
import static mmb.content.rawmats.Materials.rubber;
import static mmb.content.rawmats.Materials.rudimentary;
import static mmb.content.rawmats.Materials.rudimentium;
import static mmb.content.rawmats.Materials.silicarbide;
import static mmb.content.rawmats.Materials.silicon;
import static mmb.content.rawmats.Materials.silicopper;
import static mmb.content.rawmats.Materials.silver;
import static mmb.content.rawmats.Materials.steel;
import static mmb.content.rawmats.Materials.wireRudimentary;

import java.awt.Color;

import mmb.NN;
import mmb.content.agro.Agro;
import mmb.content.electric.VoltageTier;
import mmb.content.rawmats.Materials;
import mmb.engine.craft.SimpleItemList;
import mmb.engine.item.Item;
import mmb.engine.item.ItemEntry;
import mmb.engine.item.Items;
import monniasza.collects.Collects;
import monniasza.collects.selfset.HashSelfSet;
import monniasza.collects.selfset.SelfSet;

/**
 * Contains all electronic components.
 * <br>The component variable is the component ID concatenated with the suffix
 * <br>Tiers(tier - suffix): basic - _, {@link #enhanced} - 0,
 * {@link #advanced} - 1, {@link #extreme} - 2,
 * {@link #insane} - 3, {@link #ludicrous} - 4, {@link #ultimate} - 5
 * 
 * 
 * @author oskar
 * 
 */
public class Electronics {
	private Electronics() {}
	static {
		
	}
	
	//Component index
	@NN        static final SelfSet<@NN String, @NN ComponentTier> tiers0 = HashSelfSet.createNonnull(ComponentTier.class);
	/** List of all component tiers*/
	@NN public static final SelfSet<@NN String, @NN ComponentTier> tiers = Collects.unmodifiableSelfSet(tiers0);
	@NN        static final SelfSet<@NN String, @NN ComponentGenerator> comptypes0 = HashSelfSet.createNonnull(ComponentGenerator.class);
	/** List of all component generators */
	@NN public static final SelfSet<@NN String, @NN ComponentGenerator> comptypes = Collects.unmodifiableSelfSet(comptypes0);
	
	//Component tiers
	@NN public static final ComponentTier enhanced = new ComponentTier(Materials.colorCopper, "enh");
	@NN public static final ComponentTier advanced = new ComponentTier(Materials.colorSilver, "adv");
	@NN public static final ComponentTier extreme = new ComponentTier(Materials.colorGold, "ext");
	@NN public static final ComponentTier insane = new ComponentTier(Materials.colorDiamond, "ins");
	@NN public static final ComponentTier ludicrous = new ComponentTier(new Color(200, 100, 200), "lud");
	@NN public static final ComponentTier ultimate = new ComponentTier(VoltageTier.V5.c, "ultim");
	
	//Component types
	@NN public static final ComponentGenerator resistor = new ComponentGenerator("resistor");
	@NN public static final ComponentGenerator capacitor = new ComponentGenerator("capacitor");
	@NN public static final ComponentGenerator inductor = new ComponentGenerator("inductor");
	@NN public static final ComponentGenerator diode = new ComponentGenerator("diode");
	@NN public static final ComponentGenerator transistor = new ComponentGenerator("transistor");
	@NN public static final ComponentGenerator IC = new ComponentGenerator("ic");
	@NN public static final ComponentGenerator ceritor = new ComponentGenerator("ceritor");
	@NN public static final ComponentGenerator die = new ComponentGenerator("die");
	@NN public static final ComponentGenerator wafer = new ComponentGenerator("wafer");
	@NN public static final ComponentGenerator ram = new ComponentGenerator("ram");
	@NN public static final ComponentGenerator cpu = new ComponentGenerator("cpu");
	@NN public static final ComponentGenerator gpu = new ComponentGenerator("gpu");
	@NN public static final ComponentGenerator ai = new ComponentGenerator("ai");
	@NN public static final ComponentGenerator rtx = new ComponentGenerator("rtx");
	@NN public static final ComponentGenerator neuron = new ComponentGenerator("neuron");
	@NN public static final ComponentGenerator bhole = new ComponentGenerator("bhole");
	@NN public static final ComponentGenerator brain = new ComponentGenerator("brain");
	@NN public static final ComponentGenerator bbreak = new ComponentGenerator("brainbreaker");
	@NN public static final ComponentGenerator neuristor = new ComponentGenerator("neuristor");
	@NN public static final ComponentGenerator subcomponentA = new ComponentGenerator("subcA");
	@NN public static final ComponentGenerator subcomponentB = new ComponentGenerator("subcB");
	@NN public static final ComponentGenerator subcomponentC = new ComponentGenerator("subcC");
	@NN public static final ComponentGenerator subcomponentD = new ComponentGenerator("subcD");
	@NN public static final ComponentGenerator subsystemA = new ComponentGenerator("subsA");
	@NN public static final ComponentGenerator subsystemB = new ComponentGenerator("subsB");
	@NN public static final ComponentGenerator subsystemC = new ComponentGenerator("subsC");
	@NN public static final ComponentGenerator subsystemD = new ComponentGenerator("subsD");
	
	//Basic
	@NN public static final Item IC_ = new Item()
	.title("#ind-ic1")
	.texture("item/IC.png")
	.volumed(0.00125)
	.finish("industry.IC1");
	@NN public static final Item resistor_ = new Item()
	.title("#ind-res1")
	.texture("item/resistor.png")
	.volumed(0.00125)
	.finish("industry.resistor1");
	@NN public static final Item resistors_ = new Item()
	.title("#ind-resa1")
	.texture("item/resistor array.png")
	.volumed(0.00625)
	.finish("industry.resistors1");
	@NN public static final Item capacitor_ = new Item()
	.title("#ind-cap1")
	.texture("item/capacitor.png")
	.volumed(0.00125)
	.finish("industry.capacitor1");
	@NN public static final Item inductor_ = new Item()
	.title("#ind-ind1")
	.texture("item/inductor.png")
	.volumed(0.00125)
	.finish("industry.inductor1");
	@NN public static final Item diode_ = new Item()
	.title("#ind-dio1")
	.texture("item/diode.png")
	.volumed(0.00125)
	.finish("industry.diode1");
	@NN public static final Item transistor_ = new Item()
	.title("#ind-tra1")
	.texture("item/transistor.png")
	.volumed(0.00125)
	.finish("industry.transistor1");
	
	//Enhanced
	/** Enhanced IC */
	@NN public static final ElectronicsComponent ic0 = enhanced.generate(IC);
	
	//Advanced
	@NN public static final ElectronicsComponent resistor1 = advanced.generate(resistor);
	@NN public static final ElectronicsComponent capacitor1 = advanced.generate(capacitor);
	@NN public static final ElectronicsComponent inductor1 = advanced.generate(inductor);
	@NN public static final ElectronicsComponent diode1 = advanced.generate(diode);
	@NN public static final ElectronicsComponent transistor1 = advanced.generate(transistor);
	@NN public static final ElectronicsComponent ic1 = advanced.generate(IC);
	@NN public static final ElectronicsComponent die1 = advanced.generate(die);
	
	//Extreme
	@NN public static final ElectronicsComponent resistor2 = extreme.generate(resistor);
	@NN public static final ElectronicsComponent capacitor2 = extreme.generate(capacitor);
	@NN public static final ElectronicsComponent inductor2 = extreme.generate(inductor);
	@NN public static final ElectronicsComponent diode2 = extreme.generate(diode);
	@NN public static final ElectronicsComponent transistor2 = extreme.generate(transistor);
	@NN public static final ElectronicsComponent ic2 = extreme.generate(IC);
	@NN public static final ElectronicsComponent ceritor2 = extreme.generate(ceritor);
	@NN public static final ElectronicsComponent die2 = extreme.generate(die);
	@NN public static final ElectronicsComponent wafer2 = extreme.generate(wafer);
	@NN public static final ElectronicsComponent ram2 = extreme.generate(ram);
	@NN public static final ElectronicsComponent cpu2 = extreme.generate(cpu);
	@NN public static final ElectronicsComponent gpu2 = extreme.generate(gpu);
	
	//Insane
	@NN public static final ElectronicsComponent resistor3 = insane.generate(resistor);
	@NN public static final ElectronicsComponent capacitor3 = insane.generate(capacitor);
	@NN public static final ElectronicsComponent inductor3 = insane.generate(inductor);
	@NN public static final ElectronicsComponent diode3 = insane.generate(diode);
	@NN public static final ElectronicsComponent transistor3 = insane.generate(transistor);
	@NN public static final ElectronicsComponent ic3 = insane.generate(IC);
	@NN public static final ElectronicsComponent ceritor3 = insane.generate(ceritor);
	@NN public static final ElectronicsComponent die3 = insane.generate(die);
	@NN public static final ElectronicsComponent wafer3 = insane.generate(wafer);
	@NN public static final ElectronicsComponent ram3 = insane.generate(ram);
	@NN public static final ElectronicsComponent cpu3 = insane.generate(cpu);
	@NN public static final ElectronicsComponent gpu3 = insane.generate(gpu);
	@NN public static final ElectronicsComponent ai3 = insane.generate(ai);
	@NN public static final ElectronicsComponent rtx3 = insane.generate(rtx);
	@NN public static final ElectronicsComponent neuron3 = insane.generate(neuron);
	
	//Ludicrous
	@NN public static final ElectronicsComponent resistor4 = ludicrous.generate(resistor);
	@NN public static final ElectronicsComponent capacitor4 = ludicrous.generate(capacitor);
	@NN public static final ElectronicsComponent inductor4 = ludicrous.generate(inductor);
	@NN public static final ElectronicsComponent diode4 = ludicrous.generate(diode);
	@NN public static final ElectronicsComponent transistor4 = ludicrous.generate(transistor);
	@NN public static final ElectronicsComponent ic4 = ludicrous.generate(IC);
	@NN public static final ElectronicsComponent ceritor4 = ludicrous.generate(ceritor);
	@NN public static final ElectronicsComponent die4 = ludicrous.generate(die);
	@NN public static final ElectronicsComponent wafer4 = ludicrous.generate(wafer);
	@NN public static final ElectronicsComponent ram4 = ludicrous.generate(ram);
	@NN public static final ElectronicsComponent cpu4 = ludicrous.generate(cpu);
	@NN public static final ElectronicsComponent gpu4 = ludicrous.generate(gpu);
	@NN public static final ElectronicsComponent ai4 = ludicrous.generate(ai);
	@NN public static final ElectronicsComponent rtx4 = ludicrous.generate(rtx);
	@NN public static final ElectronicsComponent neuron4 = ludicrous.generate(neuron);
	@NN public static final ElectronicsComponent bhole4 = ludicrous.generate(bhole);
	@NN public static final ElectronicsComponent brain4 = ludicrous.generate(brain);
	@NN public static final ElectronicsComponent bbreak4 = ludicrous.generate(bbreak);
	
	//Ultimate
	@NN public static final ElectronicsComponent resistor5 = ultimate.generate(resistor);
	@NN public static final ElectronicsComponent capacitor5 = ultimate.generate(capacitor);
	@NN public static final ElectronicsComponent inductor5 = ultimate.generate(inductor);
	@NN public static final ElectronicsComponent diode5 = ultimate.generate(diode);
	@NN public static final ElectronicsComponent transistor5 = ultimate.generate(transistor);
	@NN public static final ElectronicsComponent ic5 = ultimate.generate(IC);
	@NN public static final ElectronicsComponent ceritor5 = ultimate.generate(ceritor);
	@NN public static final ElectronicsComponent die5 = ultimate.generate(die);
	@NN public static final ElectronicsComponent wafer5 = ultimate.generate(wafer);
	@NN public static final ElectronicsComponent ram5 = ultimate.generate(ram);
	@NN public static final ElectronicsComponent cpu5 = ultimate.generate(cpu);
	@NN public static final ElectronicsComponent gpu5 = ultimate.generate(gpu);
	@NN public static final ElectronicsComponent ai5 = ultimate.generate(ai);
	@NN public static final ElectronicsComponent rtx5 = ultimate.generate(rtx);
	@NN public static final ElectronicsComponent neuron5 = ultimate.generate(neuron);
	@NN public static final ElectronicsComponent bhole5 = ultimate.generate(bhole);
	@NN public static final ElectronicsComponent brain5 = ultimate.generate(brain);
	@NN public static final ElectronicsComponent bbreak5 = ultimate.generate(bbreak);
	@NN public static final ElectronicsComponent neuristor5 = ultimate.generate(neuristor);
	@NN public static final ElectronicsComponent subcomponentA5 = ultimate.generate(subcomponentA);
	@NN public static final ElectronicsComponent subcomponentB5 = ultimate.generate(subcomponentB);
	@NN public static final ElectronicsComponent subcomponentC5 = ultimate.generate(subcomponentC);
	@NN public static final ElectronicsComponent subcomponentD5 = ultimate.generate(subcomponentD);
	@NN public static final ElectronicsComponent subsystemA5 = ultimate.generate(subsystemA);
	@NN public static final ElectronicsComponent subsystemB5 = ultimate.generate(subsystemB);
	@NN public static final ElectronicsComponent subsystemC5 = ultimate.generate(subsystemC);
	@NN public static final ElectronicsComponent subsystemD5 = ultimate.generate(subsystemD);
	
	//Circuits and substrates
	@NN public static final Item circuit0 = Electronics.circuit(0);
	@NN public static final Item substrate0 = Electronics.substrate(0);
	@NN public static final Item circuit1 = Electronics.circuit(1);
	@NN public static final Item substrate1 = Electronics.substrate(1);
	@NN public static final Item circuit2 = Electronics.circuit(2);
	@NN public static final Item substrate2 = Electronics.substrate(2);
	@NN public static final Item circuit3 = Electronics.circuit(3);
	@NN public static final Item substrate3 = Electronics.substrate(3);
	@NN public static final Item circuit4 = Electronics.circuit(4);
	@NN public static final Item substrate4 = Electronics.substrate(4);
	@NN public static final Item circuit5 = Electronics.circuit(5);
	@NN public static final Item substrate5 = Electronics.substrate(5);
	@NN public static final Item circuit6 = Electronics.circuit(6);
	@NN public static final Item substrate6 = Electronics.substrate(6);
	@NN public static final Item circuit7 = Electronics.circuit(7);
	@NN public static final Item substrate7 = Electronics.substrate(7);
	@NN public static final Item circuit8 = Electronics.circuit(8);
	@NN public static final Item substrate8 = Electronics.substrate(8);
	@NN public static final Item circuit9 = Electronics.circuit(9);
	@NN public static final Item substrate9 = Electronics.substrate(9);
	
	private static boolean inited = false;
	/** Initializes electronic components */
	public static void init() {
		if(inited) return;
		inited = true;
		
		//Tags
		Items.tagItems("parts-electronic", Electronics.resistor_, Electronics.capacitor_, Electronics.inductor_, Electronics.diode_, Electronics.transistor_, Electronics.IC_, Electronics.resistors_);
		
		//Basic and enhanced
		assembler.add(new SimpleItemList(
			copper.wire,
			coal.nugget),
			resistor_,   null, 8, VoltageTier.V1,  10000);
		assembler.add(new SimpleItemList(
			copper.wire,
			paper),
			capacitor_,  null, 8, VoltageTier.V1,  10000);
		assembler.add(new SimpleItemList(
			copper.wire.stack(2),
			iron.nugget),
			inductor_,   null, 8, VoltageTier.V1,  10000);
		assembler.add(new SimpleItemList(
			copper.wire,
			rudimentary.nugget,
			silicon.nugget),
			diode_,      null, 8, VoltageTier.V1,  20000);
		assembler.add(new SimpleItemList(
			copper.wire, copper.nugget, silicon.nugget),
			transistor_, null, 8, VoltageTier.V1,  40000);
		assembler.add(new SimpleItemList(
			copper.wire.stack(4),
			rudimentium.nugget.stack(2),
			silicon.foil,
			silicon.nugget),
			IC_, null, 16, VoltageTier.V1,  80000);
		assembler.add(new SimpleItemList(
			resistor_.stack(16),
			nickel.wire.stack(8)
		), resistors_, null, VoltageTier.V1, 100000);
		
		//Enhanced
		assembler.add(new SimpleItemList(
			silver.wire,
			silver.nugget,
			silicon.sheet,
			silicon.nugget),
		ic0, null, 2, VoltageTier.V2,  80000);
		
		//Advanced
		assembler.add(new SimpleItemList(
			silver.wire,
			nickel.wire),
		resistor1, null, 16, VoltageTier.V2,  20000);
		assembler.add(new SimpleItemList(
			silver.wire,
			rubber.foil),
		capacitor1, null, 16, VoltageTier.V2,  40000);
		assembler.add(new SimpleItemList(
			silver.wire.stack(2),
			steel.nugget),
		inductor1, null, 16, VoltageTier.V2,  80000);
		assembler.add(new SimpleItemList(
			silver.wire,
			rudimentary.nugget,
			silicon.nugget),
		diode1, null, 16, VoltageTier.V2, 160000);
		assembler.add(new SimpleItemList(
			silver.wire,
			copper.nugget,
			silicon.nugget),
		transistor1, null, 16, VoltageTier.V2, 320000);
		assembler.add(new SimpleItemList(
			gold.wire,
			gold.nugget,
			silicon.frag,
			silicon.sheet.stack(2)),
		ic1, null, 2, VoltageTier.V3, 640000);
		assembler.add(new SimpleItemList(
			gold.wire,
			gold.nugget,
			PE.sheet.stack(4)),
		die1, null, 24, VoltageTier.V3, 160000);
		
		//Extreme
		assembler.add(new SimpleItemList(
			gold.wire,
			nichrome.wire),
		resistor2, null, 24, VoltageTier.V3,  80000);
		assembler.add(new SimpleItemList(
			gold.wire,
			PE.foil),
		capacitor2, null, 24, VoltageTier.V3, 160000);
		assembler.add(new SimpleItemList(
			gold.wire.stack(2),
			electrosteel.nugget),
		inductor2, null, 24, VoltageTier.V3, 320000);
		assembler.add(new SimpleItemList(
			gold.wire,
			copper.nugget,
			silicon.nugget.stack(2)),
		diode2, null, 24, VoltageTier.V3, 640000);
		assembler.add(new SimpleItemList(
			gold.wire,
			silver.nugget,
			silicon.nugget.stack(2)),
		transistor2, null, 16, VoltageTier.V3, 1280000);
		assembler.add(new SimpleItemList(
			platinum.wire.stack(4),
			platinum.nugget.stack(4),
			silicopper.nugget.stack(16),
			silicon.sheet.stack(2)),
		ic2, null, 48, VoltageTier.V3, 2560000);
		assembler.add(new SimpleItemList(
			platinum.wire.stack(2),
			platinum.nugget.stack(2),
			PVC.sheet.stack(4)),
		die2, null, 24, VoltageTier.V4, 640000);
		inscriber.add(silicon.panel, wafer2, ic1, 1, VoltageTier.V4, 640000);
		assembler.add(new SimpleItemList(
			gold.wire,
			quartz.nugget.stack(2),
			platinum.wire),
		ceritor2, null, 24, VoltageTier.V4, 320000);
		assembler.add(new SimpleItemList(
			platinum.wire,
			ic2.stack(6),
			copper.nugget,
			silicopper.nugget.stack(4)),
		ram2, null, 16, VoltageTier.V4, 2560000);
		assembler.add(new SimpleItemList(
			platinum.wire,
			ic2.stack(6),
			gold.nugget,
			silicopper.nugget.stack(4)),
		gpu2, null, 2, VoltageTier.V4, 2560000);
		assembler.add(new SimpleItemList(
			platinum.wire,
			ic2.stack(6),
			silver.nugget,
			silicopper.nugget.stack(4)),
		cpu2, null, 8, VoltageTier.V4, 2560000);
		
		//Insane
		assembler.add(new SimpleItemList(
			platinum.wire,
			nichrome.wire),
		resistor3, null, 32, VoltageTier.V4,  320000);
		assembler.add(new SimpleItemList(
			platinum.wire,
			PVC.foil),
		capacitor3, null, 32, VoltageTier.V4, 640000);
		assembler.add(new SimpleItemList(
			platinum.wire.stack(2),
			alnico.nugget),
		inductor3, null, 32, VoltageTier.V4, 1280000);
		assembler.add(new SimpleItemList(
			platinum.wire,
			silver.nugget,
			silicon.nugget.stack(8)),
		diode3, null, 32, VoltageTier.V4, 2560000);
		assembler.add(new SimpleItemList(
			platinum.wire,
			gold.nugget,
			silicon.nugget.stack(2)),
		transistor3, null, 24, VoltageTier.V4, 5120000);
		assembler.add(new SimpleItemList(
			iridium.wire.stack(4),
			iridium.nugget.stack(4),
			silicopper.nugget.stack(16),
			silicon.sheet.stack(2)),
		ic3, null, 64, VoltageTier.V4, 10240000);
		assembler.add(new SimpleItemList(
			iridium.wire.stack(2),
			iridium.nugget.stack(2),
			PTFE.sheet.stack(4)),
		die3, null, 48, VoltageTier.V5, 2560000);
		inscriber.add(silicarbide.panel, wafer3, ic2, 1, VoltageTier.V5, 2560000);
		assembler.add(new SimpleItemList(
			platinum.wire,
			quartz.nugget.stack(2),
			iridium.wire),
		ceritor3, null, 48, VoltageTier.V5, 1280000);
		assembler.add(new SimpleItemList(
			iridium.wire,
			ic3.stack(6),
			copper.nugget,
			silicarbide.nugget.stack(4)),
		ram3, null, 32, VoltageTier.V5, 10240000);
		assembler.add(new SimpleItemList(
			iridium.wire,
			ic3.stack(6),
			gold.nugget,
			silicarbide.nugget.stack(4)),
		gpu3, null, 4, VoltageTier.V5, 10240000);
		assembler.add(new SimpleItemList(
			iridium.wire,
			ic3.stack(6),
			silver.nugget,
			silicarbide.nugget.stack(4)),
		cpu3, null, 16, VoltageTier.V5, 10240000);
		assembler.add(new SimpleItemList(
				iridium.wire,
				ic3.stack(6),
				platinum.nugget,
				silicopper.nugget.stack(4)),
			ai3, null, 8, VoltageTier.V5, 10240000);
			assembler.add(new SimpleItemList(
				iridium.wire,
				ic3.stack(6),
				iridium.nugget,
				silicopper.nugget.stack(4)),
			rtx3, null, 8, VoltageTier.V5, 10240000);
			assembler.add(new SimpleItemList(
				platinum.wire,
				Agro.yeast,
				Agro.seeds),
			neuron3, null, 32, VoltageTier.V5, 10240000);
		
		//Substrates
		assembler.add(new SimpleItemList(
				paper.stack(1),                         rudimentary.foil.stack(2)),
				substrate0, null, 8, VoltageTier.V1,  10000);
		assembler.add(new SimpleItemList(
				paper.stack(1),                              copper.foil.stack(2)),
				substrate1, null,    VoltageTier.V1,  10000);
		
		//Primitive Circuit
		crafting.addRecipeGrid(new ItemEntry[]{
			null,                  coal.base,    null,
			wireRudimentary.tiny,  paper,        wireRudimentary.tiny,
			null,                  silicon.frag, null
		}, 3, 3, circuit0);
		assembler.add(new SimpleItemList(
			substrate0.stack(1),
			resistor_.stack(2),
			capacitor_.stack(2),
			inductor_.stack(2)
		), circuit0.stack(8), null, VoltageTier.V1,  10000);
		//Basic Circuit
		assembler.add(new SimpleItemList(
			substrate1.stack(1),
			resistor_.stack(4),
			capacitor_.stack(4),
			inductor_.stack(4),
			diode_.stack(2),
			circuit0.stack(2)
		), circuit1, null, VoltageTier.V1, 100000);
		assembler.add(new SimpleItemList(
			substrate1.stack(1),
			IC_.stack(1)
		), circuit1.stack(8), null, VoltageTier.V2, 100000);
		//Enhanced Circuit
		assembler.add(new SimpleItemList(
			resistor_.stack(16),
			capacitor_.stack(16),
			inductor_.stack(16),
			diode_.stack(8),
			transistor_.stack(4),
			substrate2.stack(1),
			circuit1.stack(2)
		), circuit2, null, VoltageTier.V2, 400000);
		assembler.add(new SimpleItemList(
			capacitor1.stack(2),
			inductor1.stack(2),
			resistor1.stack(2),
			ic0.stack(1),
			substrate2.stack(1)
		), circuit2.stack(8), null, VoltageTier.V3, 400000);
		//Refined Circuit
		assembler.add(new SimpleItemList(
			capacitor1.stack(8),
			inductor1.stack(8),
			resistor1.stack(8),
			diode1.stack(4),
			transistor1.stack(2),
			ic0.stack(1),
			circuit2.stack(2),
			substrate3.stack(2)
		), circuit3, null, VoltageTier.V3, 1600000);
		assembler.add(new SimpleItemList(
			transistor2.stack(16),
			diode1.stack(16),
			ic1.stack(1),
			substrate3.stack(2)
		), circuit3.stack(8), null, VoltageTier.V4, 1600000);
		//Advanced Circuit
	}

	@NN
	public static Item circuit(int n) {
		Item item = new Item()
		.title("#ind-circ"+n)
		.texture("item/circuit "+n+".png")
		.volumed(0.00125)
		.finish("industry.processor"+n);
		Items.tagItem("parts-electronic", item);
		return item;
	}

	@NN private static Item substrate(int n) {
		Item item = new Item()
		.title("#ind-sub"+n)
		.texture("item/substrate "+n+".png")
		.volumed(0.00125)
		.finish("industry.substrate"+n);
		Items.tagItem("parts-electronic", item);
		return item;
	}
}
