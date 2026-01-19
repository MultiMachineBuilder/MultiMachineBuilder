/**
 * 
 */
package mmb.engine.recipe2.processing;

import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JList;

import mmb.annotations.NN;
import mmb.engine.UnitFormatter;
import mmb.engine.recipe.CRConstants;
import mmb.engine.recipe.ItemStack;
import mmb.engine.recipe.RecipeView;
import mmb.engine.recipe.VectorUtils;
import mmb.engine.recipe2.OutputRow;
import mmb.menu.world.ItemStackCellRenderer;
import mmb.menu.world.OutputRowCellRenderer;
import mmb.menu.world.RecipeSetupCellRenderer;

/**
 * @author oskar
 */
public class ProcessingRecipeView extends RecipeView<@NN ProcessingRecipeData> {
	private static final long serialVersionUID = -2864705123116802475L;
	private JLabel lblVolt;
	private JLabel lblEnergy;
	private JLabel lblIncoming;
	private JLabel lblOutgoing;
	private JList<ItemStack> inList;
	private JList<OutputRow> outList;
	private JLabel lblCatalyst;
	private RecipeSetupCellRenderer catalyst;
	
	/** Creates recipe view for multi-item recipes with catalyst*/
	public ProcessingRecipeView() {
		setLayout(new MigLayout("", "[grow][grow][grow]", "[][][][fill]"));
		
		lblVolt = new JLabel(CRConstants.VOLT);
		add(lblVolt, "cell 0 1");
		
		lblEnergy = new JLabel(CRConstants.ENERGY);
		add(lblEnergy, "cell 1 1,growx");
		
		lblIncoming = new JLabel(CRConstants.IN);
		add(lblIncoming, "cell 0 2,growx");
		
		lblCatalyst = new JLabel(CRConstants.CAT);
		add(lblCatalyst, "cell 1 2,growx");
		
		lblOutgoing = new JLabel(CRConstants.OUT);
		add(lblOutgoing, "cell 2 2,growx");
		
		catalyst = new RecipeSetupCellRenderer();
		add(catalyst, "cell 1 3, grow");
		
		outList = new JList<>();
		outList.setCellRenderer(OutputRowCellRenderer.instance);
		add(outList, "cell 2 3,grow");
		
		inList = new JList<>();
		inList.setCellRenderer(ItemStackCellRenderer.instance);
		add(inList, "cell 0 3,grow");
		inList.setMaximumSize(null); 
	}
	@Override public void set(ProcessingRecipeData recipe) {
		lblVolt.setText(CRConstants.VOLT+recipe.voltage().name);
		lblEnergy.setText(CRConstants.ENERGY+UnitFormatter.formatEnergy(recipe.energyCost()));
		inList.setListData(VectorUtils.list2arr(recipe.inputs()));
		outList.setListData(VectorUtils.list2arr(recipe.out()));
		catalyst.setUp(recipe.getConfiguration());
	}
}
