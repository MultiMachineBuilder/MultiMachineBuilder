/**
 * 
 */
package mmb.WORLD.gui.craft;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Vector;

import javax.swing.JComponent;
import mmb.WORLD.crafting.recipes.CraftingRecipeGroup.CraftingRecipe;
import mmb.WORLD.inventory.ItemStack;
import mmb.WORLD.items.ItemEntry;
import monniasza.collects.grid.Grid;
import javax.swing.JList;
import java.awt.Color;

/**
 * @author oskar
 *
 */
public class CraftingRecipeView extends JPanel{
	private static final long serialVersionUID = 5070877744489415798L;
	private ItemGrid grid;
	private JList<ItemStack> out;
	private JList<ItemStack> in;
	
	public CraftingRecipeView() {
		setLayout(new MigLayout("", "[grow][grow][grow]", "[][grow]"));
		
		JLabel lblI = new JLabel(CRConstants.IN);
		lblI.setBackground(Color.ORANGE);
		lblI.setOpaque(true);
		add(lblI, "cell 0 0 2 1,growx");
		
		JLabel lblO = new JLabel(CRConstants.OUT);
		lblO.setBackground(Color.BLUE);
		lblO.setOpaque(true);
		add(lblO, "cell 2 0,growx");
		
		grid = new ItemGrid();
		add(grid, "cell 0 1,growx");
		
		in = new JList<>();
		in.setCellRenderer(SimpleRecipeView.renderer);
		add(in, "cell 1 1,grow");
		
		out = new JList<>();
		out.setCellRenderer(SimpleRecipeView.renderer);
		add(out, "cell 2 1,grow");
	}
	public void set(CraftingRecipe recipe, ItemStack[] stackO, ItemStack[] stackI) {
		grid.setGrid(recipe.grid);
		out.setListData(stackO);
		in.setListData(stackI);
	}
	/**
	 * @author oskar
	 *
	 */
	public static class ItemGrid extends JComponent{
		private static final long serialVersionUID = 952344490217533557L;
		private Grid<ItemEntry> grid;
		/**
		 * @return the grid
		 */
		public Grid<ItemEntry> getGrid() {
			return grid;
		}
		/**
		 * @param grid the grid to set
		 */
		public void setGrid(Grid<ItemEntry> grid) {
			this.grid = grid;
		}
		
		@Override
		public void paint(Graphics g) {
			if(grid == null) return;
			for(int x = 0; x < grid.width(); x++) {
				for(int y = 0; y < grid.height(); y++) {
					ItemEntry item = grid.get(x, y);
					if(item != null) item.icon().paintIcon(this, g, x*32, y*32);
				}
			}
		}
		public Dimension getPreferredSize() {
			return new Dimension(grid.width() * 32, grid.height() * 32);
		}
	}
}
