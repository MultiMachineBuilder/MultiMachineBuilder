/**
 * 
 */
package mmb.WORLD.gui.inv;

import java.awt.Graphics;

import javax.swing.JComponent;

import com.pploder.events.CatchingEvent;
import com.pploder.events.Event;

import mmb.WORLD.gui.Variable;
import mmb.WORLD.inventory.ItemEntry;
import mmb.debug.Debugger;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author oskar
 *
 */
public class ItemSelectionSlot extends JComponent {
	private static final long serialVersionUID = -5582224599293758548L;
	
	private Variable<ItemEntry> selectionSrc;
	private ItemEntry selection;
	/**
	 * @return the selection source
	 */
	public Variable<ItemEntry> getSelectionSrc() {
		return selectionSrc;
	}
	/**
	 * @param selectionSrc new selection source
	 */
	public void setSelectionSrc(Variable<ItemEntry> selectionSrc) {
		this.selectionSrc = selectionSrc;
	}
	/**
	 * @return currently selected item
	 */
	public ItemEntry getSelection() {
		return selection;
	}
	/**
	 * @param selection item to be selected
	 */
	public void setSelection(ItemEntry selection) {
		this.selection = selection;
	}
	
	@SuppressWarnings("null")
	@Override
	protected void paintComponent(Graphics g) {
		if(selection != null) {
			selection.render(g, 0, 0);
		}
	}
	/**
	 * Creates an item selection slot
	 */
	public ItemSelectionSlot() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(selectionSrc != null) selection = selectionSrc.get();
			}
		});
	}
	
	private static final Debugger debug = new Debugger("ITEM SELECTION SLOT");
	public final Event<ItemEntry> stateChanged = new CatchingEvent<ItemEntry>(debug, "Could not fire item changed event");
}
