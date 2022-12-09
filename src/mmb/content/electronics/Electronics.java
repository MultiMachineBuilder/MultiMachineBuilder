/**
 * 
 */
package mmb.content.electronics;

import java.awt.Color;

import javax.annotation.Nonnull;

import mmb.content.agro.AgroRecipeGroup.AgroProcessingRecipe;
import mmb.content.electric.VoltageTier;
import mmb.content.rawmats.Materials;
import monniasza.collects.Collects;
import monniasza.collects.selfset.HashSelfSet;
import monniasza.collects.selfset.SelfSet;

/**
 * @author oskar
 * Contains all electronics components in Enhanced or higher tiers
 */
public class Electronics {
	private Electronics() {}
	/**
	 * Initializes electronic components
	 */
	public static void init() {}
	
	//Component index
	@Nonnull        static final SelfSet<String, ComponentTier> tiers0 = HashSelfSet.createNonnull(ComponentTier.class);
	@Nonnull public static final SelfSet<String, ComponentTier> tiers = Collects.unmodifiableSelfSet(tiers0);
	@Nonnull        static final SelfSet<String, ComponentGenerator> comptypes0 = HashSelfSet.createNonnull(ComponentGenerator.class);
	@Nonnull public static final SelfSet<String, ComponentGenerator> comptypes = Collects.unmodifiableSelfSet(comptypes0);
	//Advanced+ components
	
	//Component tiers
	@Nonnull public static final ComponentTier enhanced = new ComponentTier(Materials.colorCopper, "enh");
	@Nonnull public static final ComponentTier advanced = new ComponentTier(Materials.colorSilver, "adv");
	@Nonnull public static final ComponentTier extreme = new ComponentTier(Materials.colorGold, "ext");
	@Nonnull public static final ComponentTier insane = new ComponentTier(Materials.colorDiamond, "ins");
	@Nonnull public static final ComponentTier ludicrous = new ComponentTier(new Color(200, 100, 200), "lud");
	@Nonnull public static final ComponentTier ultimate = new ComponentTier(VoltageTier.V5.c, "ultim");
	
	//Component types
	@Nonnull public static final ComponentGenerator resistor = new ComponentGenerator("resistor");
	@Nonnull public static final ComponentGenerator capacitor = new ComponentGenerator("capacitor");
	@Nonnull public static final ComponentGenerator inductor = new ComponentGenerator("inductor");
	@Nonnull public static final ComponentGenerator diode = new ComponentGenerator("diode");
	@Nonnull public static final ComponentGenerator transistor = new ComponentGenerator("transistor");
	@Nonnull public static final ComponentGenerator IC = new ComponentGenerator("ic");
	@Nonnull public static final ComponentGenerator ceritor = new ComponentGenerator("ceritor");
	@Nonnull public static final ComponentGenerator die = new ComponentGenerator("die");
	@Nonnull public static final ComponentGenerator wafer = new ComponentGenerator("wafer");
	@Nonnull public static final ComponentGenerator ram = new ComponentGenerator("ram");
	@Nonnull public static final ComponentGenerator cpu = new ComponentGenerator("cpu");
	@Nonnull public static final ComponentGenerator gpu = new ComponentGenerator("gpu");
	@Nonnull public static final ComponentGenerator ai = new ComponentGenerator("ai");
	@Nonnull public static final ComponentGenerator rtx = new ComponentGenerator("rtx");
	@Nonnull public static final ComponentGenerator neuron = new ComponentGenerator("neuron");
	@Nonnull public static final ComponentGenerator bhole = new ComponentGenerator("bhole");
	@Nonnull public static final ComponentGenerator brain = new ComponentGenerator("brain");
	@Nonnull public static final ComponentGenerator bbreak = new ComponentGenerator("brainbreaker");
	@Nonnull public static final ComponentGenerator neuristor = new ComponentGenerator("neuristor");
	@Nonnull public static final ComponentGenerator subcomponentA = new ComponentGenerator("subcA");
	@Nonnull public static final ComponentGenerator subcomponentB = new ComponentGenerator("subcB");
	@Nonnull public static final ComponentGenerator subcomponentC = new ComponentGenerator("subcC");
	@Nonnull public static final ComponentGenerator subcomponentD = new ComponentGenerator("subcD");
	@Nonnull public static final ComponentGenerator subsystemA = new ComponentGenerator("subsA");
	@Nonnull public static final ComponentGenerator subsystemB = new ComponentGenerator("subsB");
	@Nonnull public static final ComponentGenerator subsystemC = new ComponentGenerator("subsC");
	@Nonnull public static final ComponentGenerator subsystemD = new ComponentGenerator("subsD");
	//Components themselves
	//Enhanced
	/** Enhanced IC */
	@Nonnull public static final ElectronicsComponent ic0 = enhanced.generate(IC);
	
	//Advanced
	@Nonnull public static final ElectronicsComponent resistor1 = advanced.generate(resistor);
	@Nonnull public static final ElectronicsComponent capacitor1 = advanced.generate(capacitor);
	@Nonnull public static final ElectronicsComponent inductor1 = advanced.generate(inductor);
	@Nonnull public static final ElectronicsComponent diode1 = advanced.generate(diode);
	@Nonnull public static final ElectronicsComponent transistor1 = advanced.generate(transistor);
	@Nonnull public static final ElectronicsComponent ic1 = advanced.generate(IC);
	@Nonnull public static final ElectronicsComponent die1 = advanced.generate(die);
	
	//Extreme
	@Nonnull public static final ElectronicsComponent resistor2 = extreme.generate(resistor);
	@Nonnull public static final ElectronicsComponent capacitor2 = extreme.generate(capacitor);
	@Nonnull public static final ElectronicsComponent inductor2 = extreme.generate(inductor);
	@Nonnull public static final ElectronicsComponent diode2 = extreme.generate(diode);
	@Nonnull public static final ElectronicsComponent transistor2 = extreme.generate(transistor);
	@Nonnull public static final ElectronicsComponent ic2 = extreme.generate(IC);
	@Nonnull public static final ElectronicsComponent ceritor2 = extreme.generate(ceritor);
	@Nonnull public static final ElectronicsComponent die2 = extreme.generate(die);
	@Nonnull public static final ElectronicsComponent wafer2 = extreme.generate(wafer);
	@Nonnull public static final ElectronicsComponent ram2 = extreme.generate(ram);
	@Nonnull public static final ElectronicsComponent cpu2 = extreme.generate(cpu);
	@Nonnull public static final ElectronicsComponent gpu2 = extreme.generate(gpu);
	
	//Insane
	@Nonnull public static final ElectronicsComponent resistor3 = insane.generate(resistor);
	@Nonnull public static final ElectronicsComponent capacitor3 = insane.generate(capacitor);
	@Nonnull public static final ElectronicsComponent inductor3 = insane.generate(inductor);
	@Nonnull public static final ElectronicsComponent diode3 = insane.generate(diode);
	@Nonnull public static final ElectronicsComponent transistor3 = insane.generate(transistor);
	@Nonnull public static final ElectronicsComponent ic3 = insane.generate(IC);
	@Nonnull public static final ElectronicsComponent ceritor3 = insane.generate(ceritor);
	@Nonnull public static final ElectronicsComponent die3 = insane.generate(die);
	@Nonnull public static final ElectronicsComponent wafer3 = insane.generate(wafer);
	@Nonnull public static final ElectronicsComponent ram3 = insane.generate(ram);
	@Nonnull public static final ElectronicsComponent cpu3 = insane.generate(cpu);
	@Nonnull public static final ElectronicsComponent gpu3 = insane.generate(gpu);
	@Nonnull public static final ElectronicsComponent ai3 = insane.generate(ai);
	@Nonnull public static final ElectronicsComponent rtx3 = insane.generate(rtx);
	@Nonnull public static final ElectronicsComponent neuron3 = insane.generate(neuron);
	
	//Ludicrous
	@Nonnull public static final ElectronicsComponent resistor4 = ludicrous.generate(resistor);
	@Nonnull public static final ElectronicsComponent capacitor4 = ludicrous.generate(capacitor);
	@Nonnull public static final ElectronicsComponent inductor4 = ludicrous.generate(inductor);
	@Nonnull public static final ElectronicsComponent diode4 = ludicrous.generate(diode);
	@Nonnull public static final ElectronicsComponent transistor4 = ludicrous.generate(transistor);
	@Nonnull public static final ElectronicsComponent ic4 = ludicrous.generate(IC);
	@Nonnull public static final ElectronicsComponent ceritor4 = ludicrous.generate(ceritor);
	@Nonnull public static final ElectronicsComponent die4 = ludicrous.generate(die);
	@Nonnull public static final ElectronicsComponent wafer4 = ludicrous.generate(wafer);
	@Nonnull public static final ElectronicsComponent ram4 = ludicrous.generate(ram);
	@Nonnull public static final ElectronicsComponent cpu4 = ludicrous.generate(cpu);
	@Nonnull public static final ElectronicsComponent gpu4 = ludicrous.generate(gpu);
	@Nonnull public static final ElectronicsComponent ai4 = ludicrous.generate(ai);
	@Nonnull public static final ElectronicsComponent rtx4 = ludicrous.generate(rtx);
	@Nonnull public static final ElectronicsComponent neuron4 = ludicrous.generate(neuron);
	@Nonnull public static final ElectronicsComponent bhole4 = ludicrous.generate(bhole);
	@Nonnull public static final ElectronicsComponent brain4 = ludicrous.generate(brain);
	@Nonnull public static final ElectronicsComponent bbreak4 = ludicrous.generate(bbreak);
	
	//Ultimate
	@Nonnull public static final ElectronicsComponent resistor5 = ultimate.generate(resistor);
	@Nonnull public static final ElectronicsComponent capacitor5 = ultimate.generate(capacitor);
	@Nonnull public static final ElectronicsComponent inductor5 = ultimate.generate(inductor);
	@Nonnull public static final ElectronicsComponent diode5 = ultimate.generate(diode);
	@Nonnull public static final ElectronicsComponent transistor5 = ultimate.generate(transistor);
	@Nonnull public static final ElectronicsComponent ic5 = ultimate.generate(IC);
	@Nonnull public static final ElectronicsComponent ceritor5 = ultimate.generate(ceritor);
	@Nonnull public static final ElectronicsComponent die5 = ultimate.generate(die);
	@Nonnull public static final ElectronicsComponent wafer5 = ultimate.generate(wafer);
	@Nonnull public static final ElectronicsComponent ram5 = ultimate.generate(ram);
	@Nonnull public static final ElectronicsComponent cpu5 = ultimate.generate(cpu);
	@Nonnull public static final ElectronicsComponent gpu5 = ultimate.generate(gpu);
	@Nonnull public static final ElectronicsComponent ai5 = ultimate.generate(ai);
	@Nonnull public static final ElectronicsComponent rtx5 = ultimate.generate(rtx);
	@Nonnull public static final ElectronicsComponent neuron5 = ultimate.generate(neuron);
	@Nonnull public static final ElectronicsComponent bhole5 = ultimate.generate(bhole);
	@Nonnull public static final ElectronicsComponent brain5 = ultimate.generate(brain);
	@Nonnull public static final ElectronicsComponent bbreak5 = ultimate.generate(bbreak);
	@Nonnull public static final ElectronicsComponent neuristor5 = ultimate.generate(neuristor);
	@Nonnull public static final ElectronicsComponent subcomponentA5 = ultimate.generate(subcomponentA);
	@Nonnull public static final ElectronicsComponent subcomponentB5 = ultimate.generate(subcomponentB);
	@Nonnull public static final ElectronicsComponent subcomponentC5 = ultimate.generate(subcomponentC);
	@Nonnull public static final ElectronicsComponent subcomponentD5 = ultimate.generate(subcomponentD);
	@Nonnull public static final ElectronicsComponent subsystemA5 = ultimate.generate(subsystemA);
	@Nonnull public static final ElectronicsComponent subsystemB5 = ultimate.generate(subsystemB);
	@Nonnull public static final ElectronicsComponent subsystemC5 = ultimate.generate(subsystemC);
	@Nonnull public static final ElectronicsComponent subsystemD5 = ultimate.generate(subsystemD);
}
