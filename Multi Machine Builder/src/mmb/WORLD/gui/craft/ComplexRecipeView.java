/**
 * 
 */
package mmb.WORLD.gui.craft;

import java.util.Vector;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import mmb.WORLD.crafting.recipes.ComplexProcessingRecipeGroup.ComplexProcessingRecipe;
import mmb.WORLD.inventory.ItemStack;
import javax.swing.JList;
import javax.swing.JPanel;

/**
 * @author oskar
 *
 */
public class ComplexRecipeView extends JPanel {
	private static final long serialVersionUID = -2864705123116802475L;
	private JLabel lblVolt;
	private JLabel lblEnergy;
	private JLabel lblIncoming;
	private JLabel lblOutgoing;
	private JList<ItemStack> inList;
	private JList<ItemStack> outList;
	private JLabel lblMachine;
	
	public ComplexRecipeView() {
		setLayout(new MigLayout("", "[grow][grow]", "[][][][fill]"));
		
		lblMachine = new JLabel(CRConstants.MACHINE);
		add(lblMachine, "cell 0 0");
		
		lblVolt = new JLabel(CRConstants.VOLT);
		add(lblVolt, "cell 0 1");
		
		lblEnergy = new JLabel(CRConstants.ENERGY);
		add(lblEnergy, "cell 1 1,growx");
		
		lblIncoming = new JLabel(CRConstants.IN);
		add(lblIncoming, "cell 0 2,growx");
		
		lblOutgoing = new JLabel(CRConstants.OUT);
		add(lblOutgoing, "cell 1 2,growx");
		
		outList = new JList<>();
		outList.setCellRenderer(SimpleRecipeView.renderer);
		add(outList, "cell 1 3,growx,aligny center");
		
		inList = new JList<>();
		inList.setCellRenderer(SimpleRecipeView.renderer);
		add(inList, "cell 0 3,growx,aligny center");
	}
	public void set(ComplexProcessingRecipe recipe, ItemStack[] vectorO, ItemStack[] vectorI) {
		lblVolt.setText(CRConstants.VOLT+recipe.voltage.name);
		lblEnergy.setText(CRConstants.ENERGY+recipe.energy);
		lblMachine.setText(CRConstants.MACHINE+recipe.group.title);
		inList.setListData(vectorI);
		outList.setListData(vectorO);
		
	}
}
