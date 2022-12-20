/**
 * 
 */
package mmb.content.electronics;

import java.awt.Color;

import mmb.NN;
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
	@NN        static final SelfSet<String, ComponentTier> tiers0 = HashSelfSet.createNonnull(ComponentTier.class);
	@NN public static final SelfSet<String, ComponentTier> tiers = Collects.unmodifiableSelfSet(tiers0);
	@NN        static final SelfSet<String, ComponentGenerator> comptypes0 = HashSelfSet.createNonnull(ComponentGenerator.class);
	@NN public static final SelfSet<String, ComponentGenerator> comptypes = Collects.unmodifiableSelfSet(comptypes0);
	//Advanced+ components
	
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
	//Components themselves
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
}
