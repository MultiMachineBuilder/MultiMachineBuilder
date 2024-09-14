/**
 * 
 */
package mmb.content.craft;

import org.ainslec.picocog.PicoWriter;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import mmb.NN;
import mmb.Nil;
import mmb.content.CraftingGroups;
import mmb.content.craft.CraftingRecipeGroup.CraftingRecipe;
import mmb.content.ditems.Stencil;
import mmb.engine.debug.Debugger;
import mmb.engine.inv.Inventory;
import mmb.engine.inv.ItemRecord;
import mmb.engine.item.ItemEntry;
import mmb.engine.item.ItemRaw;
import mmb.engine.recipe.RecipeUtil;
import mmb.engine.recipe.ItemStack;
import mmb.engine.recipe.RecipeOutput;
import mmb.engine.recipe.SimpleItemList;
import mmb.engine.settings.GlobalSettings;
import mmb.menu.Icons;
import mmb.menu.world.ItemStackCellRenderer;
import mmb.menu.world.inv.InventoryController;
import mmb.menu.world.window.GUITab;
import mmb.menu.world.window.WorldWindow;
import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;

import monniasza.collects.grid.FixedGrid;
import monniasza.collects.grid.Grid;

import javax.swing.JLabel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;

import static mmb.engine.settings.GlobalSettings.$res;

import java.awt.Color;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JList;

/**
 * @author oskar
 *
 */
public class CraftGUI extends GUITab {
	private static final long serialVersionUID = 4989669443794364281L;
	private static final Debugger debug = new Debugger("CRAFTING GUI");
	
	/** The crafting grid component */
	public final CraftingGrid craftingGrid;
	/** Inventory controller for the crafting */
	public final InventoryController inventoryController;
	private JLabel lblOutputs;
	/** Place toolbars here */
	public final Box box;
	/** The button bar */
	public final Box buttonbar1;
	/**
	 * Creates a crafting GUI with given size and inventory
	 * @param size size of each grid side in squares
	 * @param inv inventory, which will be used as a selector
	 * @param crafter the crafter, which owns this GUI (optional)
	 * @param window the window, which stores this GUI (optional)
	 * @wbp.parser.constructor
	 */
	public CraftGUI(int size, @Nil Inventory inv, @Nil ManCrafter crafter, @Nil WorldWindow window) {
		Object2IntMap<ItemEntry> ins = new Object2IntOpenHashMap<>();
		AtomicReference<RecipeOutput> outs = new AtomicReference<>();
		DefaultListModel<ItemStack> listmodel = new DefaultListModel<>();
		
		setLayout(new MigLayout("", "[grow][][]", "[][][grow]"));
		Grid<@Nil ItemEntry> contents = new FixedGrid<>(size, size);
		
		box = new Box(BoxLayout.Y_AXIS);
		add(box, "cell 1 2 2 2,grow");
		
		inventoryController = new InventoryController(inv);	
		add(inventoryController, "cell 0 0 1 3,grow");
		
		craftingGrid = new CraftingGrid(size);
		add(craftingGrid, "cell 2 0,grow");
		craftingGrid.setAlignmentX(RIGHT_ALIGNMENT);
		craftingGrid.gridStateChanged.addListener(e -> {
			debug.printl("Recipe state changed: "+e);
			contents.set(e.x, e.y, e.newEntry);
			CraftingRecipe recipe = CraftingGroups.crafting.findRecipe(contents);
			outs.set(recipe == null ? null: recipe.out);  //Find new item to be crafted
			RecipeOutput rout = outs.get();
			ins.clear();
			listmodel.clear();
			if(rout == null) {
				lblOutputs.setText(GlobalSettings.$res("wguir-invalid"));
			}else {
				//fails to add
				for(ItemEntry item: contents) {
						if(item != null) {
							ins.mergeInt(item, 1, Integer::sum);
						}
				}
				for(ItemStack stk: rout) {
					listmodel.addElement(stk);
				}
				lblOutputs.setText(GlobalSettings.$res("wguir-out"));
			}
		});
		craftingGrid.setSource(inventoryController::getSelectedItem);
		
		JScrollPane scrollOutputs = new JScrollPane();
		add(scrollOutputs, "cell 1 0,grow");
		
		JList<ItemStack> listOutputs = new JList<>();
		listOutputs.setModel(listmodel);
		listOutputs.setCellRenderer(ItemStackCellRenderer.instance);
		scrollOutputs.setViewportView(listOutputs);
		
		lblOutputs = new JLabel(GlobalSettings.$res("wguir-invalid"));
		scrollOutputs.setColumnHeaderView(lblOutputs);
		lblOutputs.setAlignmentX(RIGHT_ALIGNMENT);
		
		buttonbar1 = Box.createHorizontalBox();
		add(buttonbar1, "cell 1 1 2 1,alignx center,aligny center");
		
		//<==
		JButton btnSave = new JButton(Icons.encode);
		buttonbar1.add(btnSave);
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
				RecipeUtil.transact(item, inv0, new ItemStack(newStencil, 1), inv0, 1);
			}//else it is not a stencil
		});
		
		//==>
		JButton btnLoad = new JButton(Icons.decode);
		buttonbar1.add(btnLoad);
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
		
		//X
		JButton btnClear = new JButton(Icons.erase);
		buttonbar1.add(btnClear);
		btnClear.setToolTipText($res("wguic-clear"));
		btnClear.addActionListener(e -> 
			craftingGrid.items.fill(null)
		);
		btnClear.setBackground(new Color(255, 69, 0));
		
		//<<<
		JButton btnCraft = new JButton(Icons.craft);
		buttonbar1.add(btnCraft);
		btnCraft.setBackground(new Color(255, 0, 255));
		btnCraft.setToolTipText($res("wguic-craft"));
		btnCraft.addActionListener(e -> {
			debug.printl("Running the crafing");
			RecipeOutput rout = outs.get();
			if(rout == null) return;
			Inventory inv0 = inventoryController.getInv();
			if(inv0 == null) return;
			RecipeUtil.transact(new SimpleItemList(ins),inv0, rout, inv0, 1);
			inventoryController.refresh();
		});
		
		//!!!
		JButton btnActivateItems = new JButton(Icons.activate);
		buttonbar1.add(btnActivateItems);
		btnActivateItems.setBackground(new Color(255, 140, 0));
		btnActivateItems.setToolTipText($res("wguim-activate"));
		btnActivateItems.addActionListener(e -> {
			ItemRecord irecord = inventoryController.getSelectedValue();
			if(irecord == null) return;
			ItemEntry item = irecord.item();
			Inventory inv1 = inventoryController.getInv();
			if(inv1 == null) return;
			if(item instanceof ItemRaw) {
				ItemRaw raw = (ItemRaw) item;
				CraftingGroups.activateItem(raw, inv1, inv1);
				inventoryController.refresh();
			}
		});
		
		//Exit
		if(window != null) {
			JButton btnExit = new JButton($res("wguic-exit"));
			btnExit.setForeground(new Color(0, 0, 0));
			btnExit.setToolTipText($res("wguic-exit-d"));
			btnExit.addActionListener(e -> {
				if(crafter != null) crafter.closeWindow(window);
			});
			btnExit.setBackground(Color.RED);
			buttonbar1.add(btnExit);
		}
		
	}
	@Override
	public void close(WorldWindow window) {
		craftingGrid.close();
	}
}
