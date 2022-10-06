/**
 * 
 */
package mmb.MENU.wtool;

import java.util.Objects;
import java.util.function.Supplier;

import javax.swing.Icon;

import monniasza.collects.Identifiable;

/**
 * @author oskar
 *
 */
public class WindowToolModel implements Identifiable<String> {
	private Icon icon;
	public final String id;
	/**
	 * @param icon icon of the tool
	 * @param instantiator the {@link Supplier} factory of the window tool
	 * @param id the unique ID of the tool
	 * @throws NullPointerException if {@code instantiator} or {@code id} is null
	 */
	public WindowToolModel(Icon icon, Supplier<WindowTool> instantiator, String id) {
		this.icon = icon;
		this.instantiator = Objects.requireNonNull(instantiator, "instantiator is null");
		this.id = Objects.requireNonNull(id, "id is null");
	}
	/**
	 * The {@link Supplier} factory of the window tool
	 */
	public final Supplier<WindowTool> instantiator;
	/**
	 * @return the icon
	 */
	public Icon getIcon() {
		return icon;
	}
	/**
	 * @param icon the icon to set
	 */
	public void setIcon(Icon icon) {
		this.icon = icon;
	}
	/**
	 * @return a new instance of given window tool
	 */
	public WindowTool create() {
		return instantiator.get();
	}
	@Override
	public String toString() {
		return "WindowToolModel [icon=" + icon + ", id=" + id + ", instantiator=" + instantiator + "]";
	}
	@Override
	public String id() {
		return id;
	}
}
