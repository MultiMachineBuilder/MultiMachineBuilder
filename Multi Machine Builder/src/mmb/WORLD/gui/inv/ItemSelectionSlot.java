/**
 * 
 */
package mmb.WORLD.gui.inv;

import java.awt.Graphics;

import javax.annotation.Nullable;
import javax.swing.JComponent;

import com.pploder.events.CatchingEvent;
import com.pploder.events.Event;

import mmb.WORLD.gui.Variable;
import mmb.WORLD.items.ItemEntry;
import mmb.debug.Debugger;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author oskar
 *
 */
public class ItemSelectionSlot extends JComponent {
	private static final long serialVersionUID = -5582224599293758548L;
	
	private boolean canSet = true;
	
	private Variable<@Nullable ItemEntry> selectionSrc;
	private ItemEntry selection;
	/**
	 * @return the selection source
	 */
	public Variable<@Nullable ItemEntry> getSelectionSrc() {
		return selectionSrc;
	}
	/**
	 * @param selectionSrc new selection source
	 */
	public void setSelectionSrc(Variable<@Nullable ItemEntry> selectionSrc) {
		this.selectionSrc = selectionSrc;
	}
	/**
	 * @return currently selected item
	 */
	public @Nullable ItemEntry getSelection() {
		return selection;
	}
	/**
	 * @param selection item to be selected
	 */
	public void setSelection(@Nullable ItemEntry selection) {
		if(!canSet) return;
		this.selection = selection;
		stateChanged.trigger(selection);
	}
	
	@SuppressWarnings("null")
	@Override
	protected void paintComponent(Graphics g) {
		if(selection != null) {
			selection.render(g, 4, 4, 32, 32);
		}
	}
	/**
	 * Creates an item selection slot
	 */
	public ItemSelectionSlot() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(@SuppressWarnings("null") MouseEvent arg0) {
				if(selectionSrc != null) setSelection(selectionSrc.get());
				repaint();
			}
		});
	}
	
	/**
	 * @return can the value be set by the user
	 */
	public boolean canSet() {
		return canSet;
	}
	/**
	 * @param canSet should value be settable by the user?
	 */
	public void setCanSet(boolean canSet) {
		this.canSet = canSet;
	}

	private static final Debugger debug = new Debugger("ITEM SELECTION SLOT");
	/**
	 * Invoked when contents of this slot change
	 */
	public final Event<ItemEntry> stateChanged = new CatchingEvent<ItemEntry>(debug, "Could not fire item changed event");
}
