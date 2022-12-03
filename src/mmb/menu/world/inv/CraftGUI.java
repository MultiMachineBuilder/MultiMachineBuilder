/**
 * 
 */
package mmb.menu.world.inv;

import org.ainslec.picocog.PicoWriter;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;

import mmb.debug.Debugger;
import mmb.menu.Icons;
import mmb.menu.world.window.GUITab;
import mmb.menu.world.window.WorldWindow;
import mmb.world.blocks.machine.manual.Crafting;
import mmb.world.crafting.RecipeOutput;
import mmb.world.crafting.SimpleItemList;
import mmb.world.inventory.Inventory;
import mmb.world.inventory.ItemRecord;
import mmb.world.inventory.ItemStack;
import mmb.world.item.ItemEntry;
import mmb.world.item.ItemRaw;
import mmb.world.items.data.Stencil;
import mmb.world.recipes.CraftingGroups;
import mmb.world.recipes.CraftingRecipeGroup.CraftingRecipe;
import monniasza.collects.grid.FixedGrid;
import monniasza.collects.grid.Grid;

import javax.swing.JLabel;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.Box;
import javax.swing.BoxLayout;

import static mmb.GlobalSettings.$res;

import java.awt.Color;
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
		
		box = new Box(BoxLayout.Y_AXIS);
		add(box, "flowy,cell 1 0,growy");
		
		inventoryController = ctrl;	
		add(inventoryController, "cell 0 0,alignx left,growy");
		
		lblRecipeOutputs = new JLabel("Recipe output: none");
		box.add(lblRecipeOutputs);
		lblRecipeOutputs.setAlignmentX(RIGHT_ALIGNMENT);
		
		craftingGrid = new CraftingGrid(size);
		craftingGrid.setAlignmentX(RIGHT_ALIGNMENT);
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
		box.add(craftingGrid);
		craftingGrid.setSource(inventoryController::getSelectedItem);
		
		//Buttons
		buttonbar = new Box(BoxLayout.X_AXIS);
		buttonbar.setAlignmentX(RIGHT_ALIGNMENT);
		box.add(buttonbar);
		
		//<==
		JButton btnSave = new JButton(Icons.encode);
		btnSave.setToolTipText($res("wguic-g2s"));
		btnSave.setBackground(new Color(0, 191, 255));
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
		buttonbar.add(btnSave);
		
		//==>
		JButton btnLoad = new JButton(Icons.decode);
		btnLoad.setToolTipText($res("wguic-s2g"));
		btnLoad.setBackground(new Color(0, 191, 255));
		btnLoad.addActionListener(e -> {
			craftingGrid.items.fill(0, 0, size, size, null);
			//Get the stencil
			ItemRecord irecord = inventoryController.getSelectedValue();
			if(irecord == null) return;
			ItemEntry item = irecord.item();
			if(item instanceof Stencil) {
				//It is a stencil
				Stencil stencil = (Stencil) item;
				Grid<ItemEntry> grid = stencil.grid();
				int sizeX = Math.min(size, grid.width());
				int sizeY = Math.min(size, grid.height());
				Grid.copy(0, 0, grid, 0, 0, craftingGrid.items, sizeX, sizeY);
			}//else it is not a stencil
		});
		buttonbar.add(btnLoad);
		
		//X
		JButton btnClear = new JButton(Icons.erase);
		btnClear.setToolTipText($res("wguic-clear"));
		btnClear.addActionListener(e -> 
			craftingGrid.items.fill(null)
		);
		btnClear.setBackground(new Color(255, 69, 0));
		buttonbar.add(btnClear);
		
		//<<<
		JButton btnCraft = new JButton(Icons.craft);
		btnCraft.setBackground(new Color(255, 0, 255));
		btnCraft.setToolTipText($res("wguic-craft"));
		btnCraft.addActionListener(e -> {
			debug.printl("Running the crafing");
			RecipeOutput rout = outs.get();
			if(rout == null) return;
			Inventory inv0 = inventoryController.getInv();
			if(inv0 == null) return;
			CraftingGroups.transact(new SimpleItemList(ins), rout, inv0, inv0);
			inventoryController.refresh();
		});
		buttonbar.add(btnCraft);
		
		//!!!
		JButton btnActivateItems = new JButton(Icons.activate);
		btnActivateItems.setBackground(new Color(255, 140, 0));
		btnActivateItems.setToolTipText($res("wguim-activate"));
		btnActivateItems.addActionListener(e -> {
			ItemRecord irecord = inventoryController.getSelectedValue();
			if(irecord == null) return;
			ItemEntry item = irecord.item();
			Inventory inv = inventoryController.getInv();
			if(inv == null) return;
			if(item instanceof ItemRaw) {
				ItemRaw raw = (ItemRaw) item;
				CraftingGroups.activateItem(raw, inv, inv);
				inventoryController.refresh();
			}
		});
		buttonbar.add(btnActivateItems);
		
		//Exit
		if(window != null) {
			JButton btnExit = new JButton($res("wguic-exit"));
			btnExit.setForeground(new Color(0, 0, 0));
			btnExit.setToolTipText("Closes this crafting GUI, discarding any selections.");
			btnExit.addActionListener(e -> {
				if(crafter != null) crafter.closeWindow(window);
			});
			btnExit.setBackground(Color.RED);
			buttonbar.add(btnExit);
		}
	}
	@Nonnull private JLabel lblRecipeOutputs;
	/** Place toolbars here */
	@Nonnull public final Box box;
	/** Place buttons here */
	@Nonnull public final Box buttonbar;
	
	@Override
	public void close(WorldWindow window) {
		craftingGrid.close();
	}
}
