/**
 * 
 */
package mmb.engine.generator;

import java.util.List;

import javax.swing.DefaultListModel;

import mmb.annotations.NN;
import monniasza.collects.Collects;

/**
 * A set of world generation utilities
 * @author oskar
 */
public class Generators {
	private static boolean inited = false;
	/** Initializes world generators */
	public static void init() {
		if(inited) return;
		generators.addElement(new GeneratorMultiBiome());
		generators.addElement(new GeneratorPlain());
		inited = true;
	}
	/** A list of the world generators */
	@NN public static final DefaultListModel<Generator> generators = new DefaultListModel<>();
	/** A list of the world generators */
	@NN public static final List<Generator> generatorsList = Collects.toWritableList(generators);
}
