/**
 * 
 */
package mmb.menu.components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.function.Consumer;

import javax.swing.JComponent;

import mmb.NN;
import mmb.Nil;
import mmb.content.modular.gui.SafeCloseable;
import mmb.data.reactive.ListenableProperty;

/**
 * A placeholder displays a component provided by a property.
 * The displayed component is fully functional
 * @author oskar
 */
public class Placeholder extends JComponent implements SafeCloseable {
	private static final long serialVersionUID = -2374883511304183860L;
	
	/**
	 * Creates a placeholder with a property
	 * @param property source of components to use
	 */
	public Placeholder(ListenableProperty<? extends Component> property) {
		setProperty(property);
		setLayout(new BorderLayout());
	}
	/**
	 * Creates a placeholder without a property
	 */
	public Placeholder() {
		setLayout(new BorderLayout());
	}
	
	//Component properties
	@Override
	public void close(){
		setProperty(null);
	}
	
	//The underlying component
	@Nil private transient ListenableProperty<? extends Component> property;
	@Nil Component oldComponent;
	/** 
	 * Sets up the component after closing 
	 * @param newp  new property
	 */
	public void setProperty(@Nil ListenableProperty<? extends Component> newp) {
		ListenableProperty<? extends Component> old = property;
		if(old != null) old.unlistenadd(listener);
		
		if(newp != null) {
			newp.listenadd(listener);
			listener(newp.get());
		}else {
			listener(null);
		}
		property = newp;
		
	}
	/** @return current underlying property */
	public @Nil ListenableProperty<? extends Component> getProperty(){
		return property;
	}
	
	//Listener
	@NN private final transient Consumer<Component> listener = this::listener;
	private void listener(@Nil Component newComponent) {
		if(oldComponent != null) remove(oldComponent);
		setLayout(new BorderLayout());
		if(newComponent != null) add(newComponent, BorderLayout.CENTER);
	}

}
