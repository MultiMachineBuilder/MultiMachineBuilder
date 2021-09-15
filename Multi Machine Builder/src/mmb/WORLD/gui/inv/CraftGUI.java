/**
 * 
 */
package mmb.WORLD.gui.inv;

import javax.swing.JPanel;

import org.ainslec.picocog.PicoWriter;
import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.bag.HashBag;

import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;

import mmb.LAMBDAS.Consumers;
import mmb.WORLD.blocks.machine.Crafting;
import mmb.WORLD.crafting.Craftings;
import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.gui.Variable;
import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.ItemRecord;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.items.Stencil;
import mmb.debug.Debugger;
import monniasza.collects.grid.FixedGrid;
import monniasza.collects.grid.Grid;

import javax.swing.JLabel;
import javax.annotation.Nullable;
import javax.swing.Box;
import java.awt.Color;
import java.awt.Component;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author oskar
 *
 */
public class CraftGUI extends JPanel {
	private static final long serialVersionUID = 4989669443794364281L;
	private static final Debugger debug = new Debugger("CRAFTING GUI");
	
	/**
	 * The crafting grid object
	 */
	public final CraftingGrid craftingGrid;
	/**
	 * Invenotry controller for the crafting
	 */
	public final InventoryController inventoryController;
	/**
	 * Creates a crafting GUI with given size and inventory
	 * @param size size of each grid side in squares
	 * @param inv inventory, which will be used as a selector
	 * @param crafter the crafter, which owns this GUI (optional)
	 * @param window the window, which stores this GUI (optional)
	 */
	public CraftGUI(int size, Inventory inv, @Nullable Crafting crafter, @Nullable WorldWindow window) {
		Bag<ItemEntry> ins = new HashBag<>();
		AtomicReference<RecipeOutput> outs = new AtomicReference<>();
		
		setLayout(new MigLayout("", "[263px][]", "[155px,grow]"));
		Grid<@Nullable ItemEntry> contents = new FixedGrid<>(size, size);
		
		verticalBox = Box.createVerticalBox();
		add(verticalBox, "flowx,cell 1 0,growy");
		
		inventoryController = new InventoryController(inv);
		add(inventoryController, "cell 0 0,alignx left,growy");
		
		lblRecipeOutputs = new JLabel("Recipe output: none");
		verticalBox.add(lblRecipeOutputs);
		
		craftingGrid = new CraftingGrid(size);
		craftingGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
		craftingGrid.gridStateChanged.addListener(e -> {
			debug.printl("Recipe state changed: "+e);
			contents.set(e.x, e.y, e.newEntry);
			outs.set(Crafting.findRecipe(contents));  //Find new item to be crafted
			debug.printl("Recipe output: "+outs.get());
			ins.clear();
			if(outs.get() == null)
				lblRecipeOutputs.setText("Recipe output: none");
			else {
				for(ItemEntry item: contents) {
						if(item != null) {
							ins.add(item);
						}
				}
				PicoWriter out = new PicoWriter();
				out.write("Recipe output:");
				outs.get().represent(out);
				lblRecipeOutputs.setText(out.toString());
			}
		});
		verticalBox.add(craftingGrid);
		for(ItemSelectionSlot[] slots: craftingGrid.slots) {
			for(ItemSelectionSlot slot: slots) {
				slot.setSelectionSrc(Variable.delegate(() -> {
					ItemRecord sel = inventoryController.getSelectedValue();
					if(sel == null) return null;
					return sel.item();
				}, Consumers.doNothing()));
			}
		}
		
		btnCraft = new JButton("<< Craft to inventory");
		btnCraft.setForeground(new Color(0, 0, 0));
		btnCraft.setToolTipText(
				"<html>\r\n"
				+ "Places the output items, "
				+ "if there are both enough needed input items and there is sufficient space in the output. "
				+ "<br>The operation removes ingredients from the inventory\r\n"
				+ "</html>"
		);
		btnCraft.addActionListener(e -> {
			debug.printl("Running the crafing");
			if(outs.get() == null) return;
			Craftings.transact(ins, outs.get(), inventoryController.getInv(), inventoryController.getInv());
			inventoryController.refresh();
		});
		verticalBox.add(btnCraft);
		
		if(crafter != null && window != null) {
			btnExit = new JButton("Close this GUI");
			btnExit.setForeground(new Color(0, 0, 0));
			btnExit.setToolTipText("Closes this crafting GUI, discarding any selections.");
			btnExit.addActionListener(e -> crafter.closeWindow(window));
			btnExit.setBackground(Color.RED);
			verticalBox.add(btnExit);
		}
		btnLoad = new JButton("Stencil \u2192 Grid");
		btnLoad.setToolTipText("Sets contents of this grid using a stencil."
				+ "\r\nIf given stencil is larger than the grid, the upper left part is used."
				+ "\r\nIf given item is not a stencil, nothing happens");
		btnLoad.setBackground(new Color(184, 134, 11));
		btnLoad.setForeground(new Color(0, 0, 0));
		btnLoad.addActionListener(e -> {
			craftingGrid.items.fill(0, 0, size, size, null);
			//Get the stencil
			ItemRecord record = inventoryController.getSelectedValue();
			if(record == null) return;
			ItemEntry item = record.item();
			if(item instanceof Stencil) {
				//It is a stencil
				Stencil stencil = (Stencil) item;
				int sizeX = Math.min(size, stencil.width());
				int sizeY = Math.min(size, stencil.height());
				Grid.copy(0, 0, stencil, 0, 0, craftingGrid.items, sizeX, sizeY);
			}//else it is not a stencil
		});
		
		btnNewButton = new JButton("Clear");
		btnNewButton.addActionListener(e -> {
			craftingGrid.items.fill(0, 0, size, size, null);
		});
		btnNewButton.setBackground(new Color(255, 69, 0));
		verticalBox.add(btnNewButton);
		verticalBox.add(btnLoad);
		
		btnSave = new JButton("Stencil \u2190 Grid");
		btnSave.setToolTipText("Replaces given stencil with one with contents of the grid"
				+ "\r\nIf given item is not a stencil, nothing happens");
		btnSave.setForeground(new Color(192, 192, 192));
		btnSave.setBackground(new Color(25, 25, 112));
		btnSave.addActionListener(e -> {
			//Get the stencil
			ItemRecord record = inventoryController.getSelectedValue();
			if(record == null) return;
			ItemEntry item = record.item();
			if(item instanceof Stencil) {
				Stencil newStencil = new Stencil(craftingGrid.items);
				Craftings.transact(item, newStencil, inventoryController.getInv(), inventoryController.getInv());
			}//else it is not a stencil
		});
		verticalBox.add(btnSave);
	}

	/**
	 * The Box column with all crafting controls
	 */
	public final Box verticalBox;
	private JButton btnCraft;
	private JLabel lblRecipeOutputs;
	private JButton btnExit;
	private JButton btnLoad;
	private JButton btnSave;
	private JButton btnNewButton;
}
