/**
 * 
 */
package mmb.menu.world.inv;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.annotation.Nullable;
import javax.swing.JComponent;

import com.pploder.events.Event;

import mmb.CatchingEvent;
import mmb.data.variables.Variable;
import mmb.debug.Debugger;
import mmb.world.items.ItemEntry;

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
	private Variable<@Nullable ItemEntry> target;
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
		if(target != null) target.set(selection);
	}
	
	@SuppressWarnings("null")
	@Override
	protected void paintComponent(Graphics g) {
		int w = getWidth();
		int h = getHeight();
		int size = (int) (Math.min(w, h)*0.8);
		//center the item
		int x = (w-size)/2;
		int y = (h-size)/2;
		if(selection != null) {
			selection.render(g, x, y, size, size);
		}
	}
	/**
	 * Creates an item selection slot
	 */
	public ItemSelectionSlot() {
		setPreferredSize(new Dimension(40, 40));
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

	/** @return the target item variable*/
	public Variable<@Nullable ItemEntry> getTarget() {
		return target;
	}
	/** @param target new target item variable */
	public void setTarget(Variable<@Nullable ItemEntry> target) {
		setSelection(target.get());
		this.target = target;
	}

	private static final Debugger debug = new Debugger("ITEM SELECTION SLOT");
	/**
	 * Invoked when contents of this slot change
	 */
	public final Event<ItemEntry> stateChanged = new CatchingEvent<ItemEntry>(debug, "Could not fire item changed event");
}
