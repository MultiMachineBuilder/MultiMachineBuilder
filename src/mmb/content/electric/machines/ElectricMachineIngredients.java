package mmb.content.electric.machines;

import java.util.List;

import mmb.content.ContentsItems;
import mmb.content.electronics.Electronics;
import mmb.content.rawmats.Materials;
import mmb.content.rawmats.MetalGroup;
import mmb.engine.item.Item;

/**
 * Collections of ingredients for electric machines
 */
public class ElectricMachineIngredients {
	public static final List<MetalGroup> structuralMetalGroups;
	public static final List<MetalGroup> electricalMetalGroups;
	public static final List<MetalGroup> resistiveMetalGroups;
	public static final List<MetalGroup> insulatingMetalGroups;
	public static final List<Item> motors;
	public static final List<Item> conveyors;
	public static final List<Item> robots;
	public static final List<Item> pumps;
	public static final List<Item> circuits;
	
	static {
		structuralMetalGroups = List.of(
				Materials.rudimentary, Materials.iron, Materials.steel,
				Materials.stainless, Materials.titanium, Materials.signalum,
				Materials.enderium, Materials.draconium, Materials.adraconium,
				Materials.chaotium);
		electricalMetalGroups = List.of(
				Materials.rudimentary, Materials.copper, Materials.silver,
				Materials.gold, Materials.platinum, Materials.iridium, 
				Materials.crystal, Materials.stellar, Materials.adraconium,
				Materials.chaotium); //TBC to superconductor
		resistiveMetalGroups = List.of(
				Materials.coal, Materials.nickel, Materials.nichrome,
				Materials.stainless, Materials.titanium, Materials.tungsten,
				Materials.tungstenC, Materials.draconium, Materials.adraconium,
				Materials.chaotium); //last three need a replacement
		insulatingMetalGroups = List.of(
				Materials.rubber, Materials.rubber, Materials.PE,
				Materials.PVC, Materials.PTFE); //the list of insulating materials needs to expand
		motors = ContentsItems.motor.items;
		conveyors = ContentsItems.conveyor.items;
		robots = ContentsItems.robot.items;
		pumps = ContentsItems.pump.items;
		circuits = List.of(
				Electronics.circuit0, Electronics.circuit1, Electronics.circuit2,
				Electronics.circuit3, Electronics.circuit4, Electronics.circuit5, 
				Electronics.circuit6, Electronics.circuit7, Electronics.circuit8, 
				Electronics.circuit9);
	}
}
