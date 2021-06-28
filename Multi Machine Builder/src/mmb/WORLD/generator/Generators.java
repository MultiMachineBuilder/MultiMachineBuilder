/**
 * 
 */
package mmb.WORLD.generator;

import java.util.List;

import javax.swing.DefaultListModel;

import monniasza.collects.Collects;

/**
 * @author oskar
 *
 */
public class Generators {
	private static boolean inited = false;
	public static void init() {
		if(inited) return;
		add(new GeneratorMultiBiome());
		add(new GeneratorPlain());
		inited = true;
	}
	public static final DefaultListModel<Generator> generators = new DefaultListModel<>();
	public static final List<Generator> generatorsList = Collects.toWritableList(generators);
	public static void add(Generator g) {
		generators.addElement(g);
	}
	public static void remove(Generator g) {
		generators.removeElement(g);
	}
}
