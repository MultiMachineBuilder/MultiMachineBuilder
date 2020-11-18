package mmb.material.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import mmb.debug.Debugger;
import mmb.material.Material;
import mmb.material.types.Fluid;

/**
 * 
 * @author oskar
 *Manipulation of materials
 */
public class MaterialUtilities {
	private static ArrayList<Material> materials;
	
	public static Fluid hydrazine = new MaterialDataFluid("Hydrazine", 1.01);
	public static Fluid hyperdrive = new MaterialDataFluid("Hyperdrive Fuel", 1);
	public static Fluid superluminarFuel = new MaterialDataFluid("Superluminar Fuel", 1);
	public static Fluid water = new MaterialDataFluid("Water", 1);
	
	//Ion drive
	public static Fluid xenon = new MaterialDataFluid("Xenon", 0.1);
	public static Fluid argon = new MaterialDataFluid("Argon", 0.1);
	
	//Chemical
	public static Fluid kerosene = new MaterialDataFluid("Kerosene", 0.832);
	public static Fluid ethanol = new MaterialDataFluid("Ethanol", 0.79);
	public static Fluid gasoline = new MaterialDataFluid("Gasoline", 0.6);
	public static Fluid diesel = new MaterialDataFluid("Diesel Fuel", 0.78);
	public static Fluid acetylene = new MaterialDataFluid("Acetylene", 0.1);
	public static Fluid hydrogen = new MaterialDataFluid("Hydrogen", 0.072);
	public static Fluid deuterium = new MaterialDataFluid("Deuterium", 0.1);
	public static Fluid oxygen = new MaterialDataFluid("Oxygen", 1.152);
	public static Fluid CNG = new MaterialDataFluid("CNG", 0.4);
	public static Fluid LPG = new MaterialDataFluid("LPG", 0.5);
	public static Fluid metHydrogen = new MaterialDataFluid("Metal Hydrogen", 0.5);
	
	//Antimatter
	public static Fluid antihydrogen = new MaterialDataFluid("Anti-Hydrogen", 0.072);
	public static Fluid antihelium = new MaterialDataFluid("Anti-Helium", 0.13);
	public static Fluid positron = new MaterialDataFluid("Positrons", 0.1);
	public static Fluid antineutron = new MaterialDataFluid("Anti-Neutrons", 0.3);
	public static Fluid antiproton = new MaterialDataFluid("Anti-Protons", 0.3);
	
	
	static {
		add(hyperdrive);
	}
	
	static public void add(Material mat) {
		
	}
}
