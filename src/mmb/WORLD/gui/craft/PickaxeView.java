/**
 * 
 */
package mmb.WORLD.gui.craft;

import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;

import mmb.WORLD.recipes.PickaxeGroup.PickaxeInfo;

/**
 * Represents a quasi-recipe view for pickaxes
 * @author oskar
 */
public class PickaxeView extends RecipeView<PickaxeInfo>{
	private static final long serialVersionUID = -2864705123116802475L;
	private JLabel lblVolt;
	private JLabel lblEnergy;
	private JLabel lblIncoming;
	private JLabel lblOutgoing;
	private JLabel lblIn;
	private JLabel lblMachine;
	private JLabel lblOut;
	
	/** Creates a quasi-recipe view for pickaxes*/
	public PickaxeView() {
		setLayout(new MigLayout("", "[grow][grow]", "[][][][]"));
		
		lblMachine = new JLabel(CRConstants.MACHINE);
		add(lblMachine, "cell 0 0 2 1,growx");
		
		lblVolt = new JLabel(CRConstants.VOLT);
		add(lblVolt, "cell 0 1");
		
		lblEnergy = new JLabel(CRConstants.ENERGY);
		add(lblEnergy, "cell 1 1,growx");
		
		lblIncoming = new JLabel(CRConstants.IN);
		add(lblIncoming, "cell 0 2,growx");
		
		lblOutgoing = new JLabel(CRConstants.OUT);
		add(lblOutgoing, "cell 1 2,growx");
		
		lblIn = new JLabel();
		add(lblIn, "cell 0 3,grow");
		
		lblOut = new JLabel();
		add(lblOut, "cell 1 3");
	}
	@Override public void set(PickaxeInfo recipe) {
		lblMachine.setText(CRConstants.MACHINE+recipe.group.title());
		lblIn.setIcon(recipe.input.icon());
		lblIn.setText(recipe.input.title());
		lblOut.setIcon(recipe.output.getIcon());
		lblOut.setText(recipe.output.title());
	}
}
