/**
 * 
 */
package mmb.WORLD.gui.craft;

import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;

import mmb.UnitFormatter;
import mmb.WORLD.inventory.ItemStack;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.recipes.CatalyzedSingleRecipeGroup.CatalyzedSimpleRecipe;

import javax.swing.JList;

/**
 * Represents a recipe view for single-item recipes
 * @author oskar
 */
public class SimpleCatalyzedRecipeView extends RecipeView<CatalyzedSimpleRecipe>{
	private static final long serialVersionUID = -2864705123116802475L;
	private JLabel lblVolt;
	private JLabel lblEnergy;
	private JLabel lblIncoming;
	private JLabel lblOutgoing;
	private JLabel lblIn;
	private JList<ItemStack> outList;
	private JLabel lblMachine;
	private JLabel lblCatalyst;
	private JLabel catalyst;
	
	/**
	 * Creates a recipe view for single-item recipes
	 */
	public SimpleCatalyzedRecipeView() {
		setLayout(new MigLayout("", "[grow][grow][grow]", "[][][][]"));
		
		lblMachine = new JLabel(CRConstants.MACHINE);
		add(lblMachine, "cell 0 0 2 1,growx");
		
		lblVolt = new JLabel(CRConstants.VOLT);
		add(lblVolt, "cell 0 1");
		
		lblEnergy = new JLabel(CRConstants.ENERGY);
		add(lblEnergy, "cell 1 1,growx");
		
		lblIncoming = new JLabel(CRConstants.IN);
		add(lblIncoming, "cell 0 2,growx");
		
		lblCatalyst = new JLabel(CRConstants.CAT);
		add(lblCatalyst, "cell 1 2,grow");
		
		lblOutgoing = new JLabel(CRConstants.OUT);
		add(lblOutgoing, "cell 2 2,growx");
		
		lblIn = new JLabel();
		add(lblIn, "cell 0 3,grow");
		
		catalyst = new JLabel();
		add(catalyst, "cell 1 3,grow");
		
		outList = new JList<>();
		outList.setCellRenderer(ItemStackCellRenderer.instance);
		add(outList, "cell 2 3,growx,aligny center");
	}
	@Override public void set(CatalyzedSimpleRecipe recipe) {
		lblVolt.setText(CRConstants.VOLT+recipe.voltage.name);
		lblEnergy.setText(CRConstants.ENERGY+UnitFormatter.formatEnergy(recipe.energy));
		lblMachine.setText(CRConstants.MACHINE+recipe.group().title());
		ItemEntry item = recipe.input;
		lblIn.setIcon(item.icon());
		lblIn.setText(item.title());
		outList.setListData(VectorUtils.list2arr(recipe.output));
		ItemEntry catalyst0 = recipe.catalyst;
		if(catalyst0 == null) {
			catalyst.setText("none");
			catalyst.setIcon(null);
		}else {
			catalyst.setText(catalyst0.title());
			catalyst.setIcon(catalyst0.icon());
		}
	}
}
