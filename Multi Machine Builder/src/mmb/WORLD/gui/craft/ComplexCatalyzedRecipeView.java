/**
 * 
 */
package mmb.WORLD.gui.craft;

import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;

import mmb.WORLD.inventory.ItemStack;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.recipes.ComplexCatalyzedRecipeGroup.ComplexCatalyzedRecipe;

import javax.swing.JList;

import org.ainslec.picocog.PicoWriter;

import io.github.parubok.text.multiline.MultilineLabel;

/**
 * @author oskar
 * Represents a recipe view for multi-item recipes with catalyst
 */
public class ComplexCatalyzedRecipeView extends RecipeView<ComplexCatalyzedRecipe> {
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
	private MultilineLabel lblMaybe;
	
	/** Creates recipe view for multi-item recipes with catalyst*/
	public ComplexCatalyzedRecipeView() {
		setLayout(new MigLayout("", "[grow][grow][grow]", "[][][][fill]"));
		
		lblMachine = new JLabel(CRConstants.MACHINE);
		add(lblMachine, "cell 0 0,growx");
		
		lblMaybe = new MultilineLabel(CRConstants.CHANCE);
		lblMaybe.setPreferredViewportLineCount(9999);
		add(lblMaybe, "cell 1 0");
		
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
		outList.setCellRenderer(ItemStackCellRenderer.instance);
		add(outList, "cell 2 3,grow");
		
		inList = new JList<>();
		inList.setCellRenderer(ItemStackCellRenderer.instance);
		add(inList, "cell 0 3,grow");
		inList.setMaximumSize(null); 
	}
	@Override public void set(ComplexCatalyzedRecipe recipe) {
		lblVolt.setText(CRConstants.VOLT+recipe.voltage.name);
		lblEnergy.setText(CRConstants.ENERGY+recipe.energy);
		lblMachine.setText(CRConstants.MACHINE+recipe.group().title());
		inList.setListData(VectorUtils.list2arr(recipe.input));
		outList.setListData(VectorUtils.list2arr(recipe.output));
		ItemEntry catalyst0 = recipe.catalyst;
		if(catalyst0 == null) {
			catalyst.setText("none");
			catalyst.setIcon(null);
		}else {
			catalyst.setText(catalyst0.title());
			catalyst.setIcon(catalyst0.icon());
		}
		PicoWriter writer = new PicoWriter();
		writer.writeln(CRConstants.CHANCE);
		recipe.luck().represent(writer);
		lblMaybe.setText(writer.toString());
	}
}
