/**
 * 
 */
package mmbbase.menu.components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.JComponent;

import mmb.content.modular.gui.SafeCloseable;
import mmbbase.data.reactive.ListenableProperty;

/**
 * A placeholder displays a component provided by a property
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
	@Nullable private transient ListenableProperty<? extends Component> property;
	@Nullable Component oldComponent;
	/** 
	 * Sets up the component after closing 
	 * @param newp  new property
	 */
	public void setProperty(@Nullable ListenableProperty<? extends Component> newp) {
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
	public @Nullable ListenableProperty<? extends Component> getProperty(){
		return property;
	}
	
	//Listener
	@Nonnull private final transient Consumer<Component> listener = this::listener;
	private void listener(@Nullable Component newComponent) {
		if(oldComponent != null) remove(oldComponent);
		setLayout(new BorderLayout());
		if(newComponent != null) add(newComponent, BorderLayout.CENTER);
	}

}
