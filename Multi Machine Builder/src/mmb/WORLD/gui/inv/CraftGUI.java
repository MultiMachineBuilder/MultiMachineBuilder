/**
 * 
 */
package mmb.WORLD.gui.inv;

import org.ainslec.picocog.PicoWriter;
import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.bag.HashBag;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;

import mmb.DATA.variables.Variable;
import mmb.LAMBDAS.Consumers;
import mmb.WORLD.blocks.machine.manual.Crafting;
import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.crafting.SimpleItemList;
import mmb.WORLD.gui.window.GUITab;
import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.inventory.Inventory;
import mmb.WORLD.inventory.ItemRecord;
import mmb.WORLD.inventory.ItemStack;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.items.data.Stencil;
import mmb.WORLD.recipes.CraftingGroups;
import mmb.WORLD.recipes.CraftingRecipeGroup.CraftingRecipe;
import mmb.debug.Debugger;
import monniasza.collects.grid.FixedGrid;
import monniasza.collects.grid.Grid;

import javax.swing.JLabel;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.Box;
import javax.swing.BoxLayout;

import static mmb.GlobalSettings.$res;

import java.awt.Color;
import java.awt.Component;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author oskar
 *
 */
public class CraftGUI extends GUITab {
	private static final long serialVersionUID = 4989669443794364281L;
	private static final Debugger debug = new Debugger("CRAFTING GUI");
	
	/**
	 * The crafting grid object
	 */
	@Nonnull public final CraftingGrid craftingGrid;
	/**
	 * Invenotry controller for the crafting
	 */
	@Nonnull public final InventoryController inventoryController;
	/**
	 * Creates a crafting GUI with given size and inventory
	 * @param size size of each grid side in squares
	 * @param inv inventory, which will be used as a selector
	 * @param crafter the crafter, which owns this GUI (optional)
	 * @param window the window, which stores this GUI (optional)
	 * @wbp.parser.constructor
	 */
	public CraftGUI(int size, @Nullable Inventory inv, @Nullable Crafting crafter, @Nullable WorldWindow window) {
		this(size, crafter, window, new InventoryController(inv));
	}
	/**
	 * Creates a crafting GUI with given size and inventory controller
	 * @param size size of each grid side in squares
	 * @param crafter the crafter, which owns this GUI (optional)
	 * @param window the window, which stores this GUI (optional)
	 * @param ctrl inventory controller, which will be used as a selector
	 */
	public CraftGUI(int size, @Nullable Crafting crafter, @Nullable WorldWindow window, InventoryController ctrl) {
		Object2IntMap<ItemEntry> ins = new Object2IntOpenHashMap<>();
		AtomicReference<RecipeOutput> outs = new AtomicReference<>();
		
		setLayout(new MigLayout("", "[263px][]", "[155px,grow]"));
		Grid<@Nullable ItemEntry> contents = new FixedGrid<>(size, size);
		
		verticalBox = new Box(BoxLayout.Y_AXIS);
		add(verticalBox, "flowx,cell 1 0,growy");
		
		inventoryController = ctrl;	
		add(inventoryController, "cell 0 0,alignx left,growy");
		
		lblRecipeOutputs = new JLabel("Recipe output: none");
		verticalBox.add(lblRecipeOutputs);
		
		craftingGrid = new CraftingGrid(size);
		craftingGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
		craftingGrid.gridStateChanged.addListener(e -> {
			debug.printl("Recipe state changed: "+e);
			contents.set(e.x, e.y, e.newEntry);
			CraftingRecipe recipe = CraftingGroups.crafting.findRecipe(contents);
			outs.set(recipe == null ? null: recipe.out);  //Find new item to be crafted
			RecipeOutput rout = outs.get();
			debug.printl("Recipe output: "+rout);
			ins.clear();
			if(rout == null)
				lblRecipeOutputs.setText("Recipe output: none");
			else {
				//fails to add
				for(ItemEntry item: contents) {
						if(item != null) {
							ins.mergeInt(item, 1, Integer::sum);
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
		
		btnCraft = new JButton($res("wguic-craft"));
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
			RecipeOutput rout = outs.get();
			if(rout == null) return;
			Inventory inv0 = inventoryController.getInv();
			if(inv0 == null) return;
			CraftingGroups.transact(new SimpleItemList(ins), rout, inv0, inv0);
			inventoryController.refresh();
		});
		verticalBox.add(btnCraft);
		
		if(window != null) {
			btnExit = new JButton($res("wguic-exit"));
			btnExit.setForeground(new Color(0, 0, 0));
			btnExit.setToolTipText("Closes this crafting GUI, discarding any selections.");
			btnExit.addActionListener(e -> {
				if(crafter != null) crafter.closeWindow(window);
			});
			btnExit.setBackground(Color.RED);
			verticalBox.add(btnExit);
		}
		btnLoad = new JButton($res("wguic-s2g"));
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
		
		btnClear = new JButton($res("wguic-clear"));
		btnClear.addActionListener(e -> 
			craftingGrid.items.fill(0, 0, size, size, null)
		);
		btnClear.setBackground(new Color(255, 69, 0));
		verticalBox.add(btnClear);
		verticalBox.add(btnLoad);
		
		btnSave = new JButton($res("wguic-g2s"));
		btnSave.setToolTipText("Replaces given stencil with one with contents of the grid"
				+ "\r\nIf given item is not a stencil, nothing happens");
		btnSave.setForeground(new Color(192, 192, 192));
		btnSave.setBackground(new Color(25, 25, 112));
		btnSave.addActionListener(e -> {
			//Get the stencil
			ItemRecord irecord = inventoryController.getSelectedValue();
			if(irecord == null) return;
			ItemEntry item = irecord.item();
			if(item instanceof Stencil) {
				Stencil newStencil = new Stencil(craftingGrid.items);
				Inventory inv0 = inventoryController.getInv();
				if(inv0 == null) return;
				CraftingGroups.transact(item, new ItemStack(newStencil, 1), inv0, inv0);
			}//else it is not a stencil
		});
		verticalBox.add(btnSave);
	}

	/**
	 * The Box column with all crafting controls
	 */
	@Nonnull public final Box verticalBox;
	@Nonnull private JButton btnCraft;
	@Nonnull private JLabel lblRecipeOutputs;
	private JButton btnExit;	
	@Nonnull private JButton btnLoad;
	@Nonnull private JButton btnSave;
	@Nonnull private JButton btnClear;
	@Override
	public void createTab(WorldWindow window) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void destroyTab(WorldWindow window) {
		// TODO Auto-generated method stub
		
	}
}
