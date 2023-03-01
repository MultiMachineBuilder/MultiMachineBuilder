/**
 * 
 */
package mmb.menu.components;

import java.awt.Component;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.junit.jupiter.api.Test;

import mmb.Nil;
import mmb.data.variables.BooleanVariable;
import mmb.engine.debug.Debugger;

/**
 * @author oskar
 *
 */
public class GUIStacker extends JPanel {
	private static final long serialVersionUID = 9085450552728360984L;
	private static final Debugger debug = new Debugger("GUI STACKER");
	/**
	 * Creates an empty UI stacker, which allows empty stack
	 */
	public GUIStacker() {}
	/**
	 * Creates an UI stacker with given component,
	 * which allows empty stack. When null is provided, the component will not be added
	 * @param comp initial component
	 */
	public GUIStacker(@Nil Component comp) {
		this(comp, false);
	}
	/**
	 * Creates an UI stacker with given component, which may allow empty stack
	 * @param comp initial component
	 * @param componentsRequired are components required?
	 */
	public GUIStacker(@Nil Component comp, boolean componentsRequired) {
		this.componentsRequired = componentsRequired;
	}
	
	private boolean componentsRequired;
	//Component stack
	private final Deque<Component> stack = new ArrayDeque<>();
	/**
	 * Pops a component from the stack.
	 * If component implements {@link AutoCloseable}, its close() method is called before being removed
	 * @return the component closed, or else null
	 */
	
	@Nil public Component npop() {
		Component last = stack.element();
		if(componentsRequired && stack.size() < 2)
			return null;
		if(last instanceof AutoCloseable) {
			try {
				((AutoCloseable) last).close();
			} catch (Exception e) {
				debug.stacktraceError(e, "Failed to properly close the component");
			}
		}
		last = stack.pollFirst();
		update();
		return last;
	}
	/**
	 * Add a component to this stacker
	 * @param comp the component to be added
	 * @throws NullPointerException when component is null. To allow optional addition, use {@link #pushn(Component)}
	 */
	public void push(Component comp) {
		Objects.requireNonNull(comp, "comp is null");
		stack.add(comp);
		update();
	}
	/**
	 * Optionally add a component to the stacker, if it is not null.
	 * To check components, use {@link #push(Component)}, which throws {@link NullPointerException}
	 * @param comp
	 */
	public void pushn(@Nil Component comp) {
		if(comp == null) return;
		stack.add(comp);
		update();
	}
	
	private Component current;
	private void update() {
		if(current != null) remove(current);
		current = stack.element();
		add(current);
	}
	/**
	 * @return are components required?
	 */
	public boolean areComponentsRequired() {
		return componentsRequired;
	}
	/**
	 * @param componentsRequired should components be required?
	 * @throws IllegalStateException when there are no components, but their presence is forced on
	 */
	public void setComponentsRequired(boolean componentsRequired) {
		if(componentsRequired && stack.isEmpty()) 
			throw new IllegalStateException("Forcing components where they don't exist.");
		this.componentsRequired = componentsRequired;
	}
	
	@Test static void test() {
		//Create a new stacker
		BooleanVariable hasCalledClose = new BooleanVariable();
		GUIStacker stacker = new GUIStacker();
		class TestComponent extends JComponent implements AutoCloseable{
			private static final long serialVersionUID = 7804797541646505045L;

			@Override
			public void close() throws Exception {
				hasCalledClose.setValue(true);
				throw new Exception("Exception not properly caught at pop()");
			}
		}
		@SuppressWarnings("resource")
		Component comp = new TestComponent();
		stacker.push(comp);
		stacker.npop();
	}

}
