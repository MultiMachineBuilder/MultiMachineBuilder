/**
 * 
 */
package mmb.engine.recipe2.crafting;

import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;

import monniasza.collects.grid.Grid;
import javax.swing.JList;

import mmb.engine.item.ItemEntry;
import mmb.engine.recipe.CRConstants;
import mmb.engine.recipe.ItemStack;
import mmb.engine.recipe.RecipeView;
import mmb.engine.recipe.VectorUtils;
import mmb.engine.recipe2.CraftingRecipeData;
import mmb.engine.recipe2.OutputRow;
import mmb.menu.world.ItemStackCellRenderer;
import mmb.menu.world.OutputRowCellRenderer;

import java.awt.Color;

/**
 * Displays a crafting recipe
 * @author oskar
 */
public class CraftingRecipeView extends RecipeView<CraftingRecipeData>{
	private static final long serialVersionUID = 5070877744489415798L;
	private ItemGrid grid;
	private JList<OutputRow> out;
	private JList<ItemStack> in;
	/** Creates recipe view for crafting recipes */
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
		in.setCellRenderer(ItemStackCellRenderer.instance);
		add(in, "cell 1 1,grow");
		
		out = new JList<>();
		out.setCellRenderer(OutputRowCellRenderer.instance);
		add(out, "cell 2 1,grow");
	}
	@Override
	public void set(CraftingRecipeData recipe) {
		grid.setGrid(recipe.items);
		out.setListData(VectorUtils.list2arr(recipe.out));
		in.setListData(VectorUtils.list2arr(recipe.itemCounts));
	}
	/**
	 * @author oskar
	 *
	 */
	public static class ItemGrid extends JComponent{
		private static final long serialVersionUID = 952344490217533557L;
		private transient Grid<ItemEntry> grid;
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
		public void paint(@SuppressWarnings("null") Graphics g) {
			if(grid == null) return;
			for(int x = 0; x < grid.width(); x++) {
				for(int y = 0; y < grid.height(); y++) {
					ItemEntry item = grid.get(x, y);
					if(item != null) item.icon().paintIcon(this, g, x*32, y*32);
				}
			}
		}
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(grid.width() * 32, grid.height() * 32);
		}
	}
}
