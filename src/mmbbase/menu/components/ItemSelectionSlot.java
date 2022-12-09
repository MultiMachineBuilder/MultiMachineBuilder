/**
 * 
 */
package mmbbase.menu.components;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.JComponent;

import com.pploder.events.Event;

import mmb.engine.CatchingEvent;
import mmb.engine.debug.Debugger;
import mmb.engine.item.ItemEntry;
import mmbbase.data.variables.ListenableValue;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author oskar
 *
 */
public class ItemSelectionSlot extends JComponent implements AutoCloseable{
	private static final long serialVersionUID = -5582224599293758548L;
	
	//Item events
	@Nonnull private static final Debugger debug = new Debugger("ITEM SELECTION SLOT");
	/** Invoked when contents of this slot change  */
	public final transient Event<ItemEntry> stateChanged = new CatchingEvent<>(debug, "Could not fire item changed event");
	
	//Selection source
	@Nullable private transient Supplier<ItemEntry> selector;
	/** @return current selector */
	public Supplier<ItemEntry> getSelector() {
		return selector;
	}
	/** @param selectionSrc new selector */
	public void setSelector(Supplier<ItemEntry> selectionSrc) {
		this.selector = selectionSrc;
	}
	
	//Target (a variable where values are stored)
	@Nullable private transient ListenableValue<@Nullable ItemEntry> target;
	private void handleChanges(@Nullable ItemEntry i) { repaint(); stateChanged.trigger(i); }
	@Nonnull private final transient Consumer<@Nullable ItemEntry> changeHandler = this::handleChanges;
	/** @return the target item variable*/
	@Nullable public ListenableValue<@Nullable ItemEntry> getTarget() {
		return target;
	}
	/** @param newTarget new target item variable */
	public void setTarget(@Nullable ListenableValue<@Nullable ItemEntry> newTarget) {
		ItemEntry oldSelection = getSelection();
		ListenableValue<@Nullable ItemEntry> oldTarget = target;
		if(oldTarget != null) oldTarget.unlistenadd(changeHandler);
		if(newTarget != null) newTarget.listenadd(changeHandler);
		target = newTarget;
		ItemEntry newSelection = getSelection();
		if(oldSelection != newSelection) handleChanges(newSelection);
	}
	
	//Selection
	/**
	 * @return currently selected item
	 */
	public @Nullable ItemEntry getSelection() {
		final ListenableValue<ItemEntry> target2 = target;
		if (target2 == null) return null;
		return target2.get();
		
	}
	/**
	 * @param selection item to be selected
	 */
	public void setSelection(@Nullable ItemEntry selection) {
		if(!isEnabled()) return;
		if(target != null) target.set(selection);
		handleChanges(selection);
	}
	
	//Component
	@SuppressWarnings("null")
	@Override
	protected void paintComponent(Graphics g) {
		int w = getWidth();
		int h = getHeight();
		int size = (int) (Math.min(w, h)*0.8);
		//center the item
		int x = (w-size)/2;
		int y = (h-size)/2;
		ItemEntry selection = getSelection();
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
				if(selector != null) setSelection(selector.get());
				repaint();
			}
		});
	}
	@Override
	public void close() {
		setTarget(null);
	}
}
