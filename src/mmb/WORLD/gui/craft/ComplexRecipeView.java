/**
 * 
 */
package mmb.WORLD.gui.craft;

import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;

import mmb.UnitFormatter;
import mmb.WORLD.inventory.ItemStack;
import mmb.WORLD.recipes.ComplexRecipeGroup.ComplexRecipe;

import javax.swing.JList;

import org.ainslec.picocog.PicoWriter;

import io.github.parubok.text.multiline.MultilineLabel;

/**
 * Represents a recipe view for multi-item recipes
 * @author oskar
 */
public class ComplexRecipeView extends RecipeView<ComplexRecipe> {
	private static final long serialVersionUID = -2864705123116802475L;
	private JLabel lblVolt;
	private JLabel lblEnergy;
	private JLabel lblIncoming;
	private JLabel lblOutgoing;
	private JList<ItemStack> inList;
	private JList<ItemStack> outList;
	private JLabel lblMachine;
	private MultilineLabel lblMaybe;
	
	/** Creates recipe view for multi-item recipes */
	public ComplexRecipeView() {
		setLayout(new MigLayout("", "[grow][grow]", "[][][][fill]"));
		
		lblMachine = new JLabel(CRConstants.MACHINE);
		add(lblMachine, "cell 0 0");
		
		lblMaybe = new MultilineLabel(CRConstants.CHANCE);
		lblMaybe.setPreferredViewportLineCount(9999);
		add(lblMaybe, "cell 1 0");
		
		lblVolt = new JLabel(CRConstants.VOLT);
		add(lblVolt, "cell 0 1");
		
		lblEnergy = new JLabel(CRConstants.ENERGY);
		add(lblEnergy, "cell 1 1,growx");
		
		lblIncoming = new JLabel(CRConstants.IN);
		add(lblIncoming, "cell 0 2,growx");
		
		lblOutgoing = new JLabel(CRConstants.OUT);
		add(lblOutgoing, "cell 1 2,growx");
		
		outList = new JList<>();
		outList.setCellRenderer(ItemStackCellRenderer.instance);
		add(outList, "cell 1 3,growx,aligny center");
		
		inList = new JList<>();
		inList.setCellRenderer(ItemStackCellRenderer.instance);
		add(inList, "cell 0 3,growx,aligny center");
	}
	@Override
	public void set(ComplexRecipe recipe) {
		lblVolt.setText(CRConstants.VOLT+recipe.voltage.name);
		lblEnergy.setText(CRConstants.ENERGY+UnitFormatter.formatEnergy(recipe.energy));
		lblMachine.setText(CRConstants.MACHINE+recipe.group().title());
		inList.setListData(VectorUtils.list2arr(recipe.input));
		outList.setListData(VectorUtils.list2arr(recipe.output));
		PicoWriter writer = new PicoWriter();
		writer.writeln(CRConstants.CHANCE);
		recipe.luck().represent(writer);
		lblMaybe.setText(writer.toString());
	}
}
