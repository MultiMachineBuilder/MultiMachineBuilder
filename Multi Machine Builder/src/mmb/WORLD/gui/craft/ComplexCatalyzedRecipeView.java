/**
 * 
 */
package mmb.WORLD.gui.craft;

import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;

import mmb.WORLD.crafting.recipes.ComplexCatalyzedProcessingRecipeGroup.ComplexCatalyzedProcessingRecipe;
import mmb.WORLD.inventory.ItemStack;
import mmb.WORLD.items.ItemEntry;
import mmb.debug.Debugger;

import javax.swing.JList;
import javax.swing.JPanel;

/**
 * @author oskar
 *
 */
public class ComplexCatalyzedRecipeView extends JPanel {
	private static final long serialVersionUID = -2864705123116802475L;
	private JLabel lblVolt;
	private JLabel lblEnergy;
	private JLabel lblIncoming;
	private JLabel lblOutgoing;
	private JList<ItemStack> inList;
	private JList<ItemStack> outList;
	private JLabel lblMachine;
	private JLabel lblCatalyst;
	private JLabel catalyst;
	private static final Debugger debug = new Debugger("TEST LAYOUT");
	
	public ComplexCatalyzedRecipeView() {
		setLayout(new MigLayout("", "[grow][grow][grow]", "[][][][fill]"));
		
		lblMachine = new JLabel(CRConstants.MACHINE);
		add(lblMachine, "cell 0 0,growx");
		
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
		
		catalyst = new JLabel();
		add(catalyst, "cell 1 3, grow");
		
		outList = new JList<>();
		outList.setCellRenderer(SimpleRecipeView.renderer);
		add(outList, "cell 2 3,grow");
		
		inList = new JList<>();
		inList.setCellRenderer(SimpleRecipeView.renderer);
		add(inList, "cell 0 3,grow");
		inList.setMaximumSize(null); 
	}
	public void set(ComplexCatalyzedProcessingRecipe recipe, ItemStack[] vectorO, ItemStack[] vectorI) {
		lblVolt.setText(CRConstants.VOLT+recipe.voltage.name);
		lblEnergy.setText(CRConstants.ENERGY+recipe.energy);
		lblMachine.setText(CRConstants.MACHINE+recipe.group.title);
		inList.setListData(vectorI);
		outList.setListData(vectorO);
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
