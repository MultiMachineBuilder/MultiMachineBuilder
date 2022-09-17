/**
 * 
 */
package mmb.WORLD.gui.craft;

import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;

import mmb.WORLD.inventory.ItemStack;
import mmb.WORLD.items.ItemEntry;
import mmb.WORLD.recipes.AlcoholInfoGroup.AlcoholInfo;

import javax.swing.JList;

/**
 * Represents a recipe view for alcoholic beverages
 * @author oskar
 */
public class AlcoholInfoView extends RecipeView<AlcoholInfo> {
	private static final long serialVersionUID = -2864705123116802475L;
	private JLabel lblIncoming;
	private JLabel lblOutgoing;
	private JLabel lblIn;
	private JList<ItemStack> outList;
	private JLabel lblMachine;
	private JLabel lblIntoxication;
	
	/** Creates recipe view for alcoholic beverages */
	public AlcoholInfoView() {
		setLayout(new MigLayout("", "[grow][grow]", "[][][]"));
		
		lblMachine = new JLabel(CRConstants.MACHINE);
		add(lblMachine, "cell 0 0,growx");
		
		lblIntoxication = new JLabel("New label");
		add(lblIntoxication, "cell 1 0,alignx left");
		
		lblIncoming = new JLabel(CRConstants.IN);
		add(lblIncoming, "cell 0 1,growx");
		
		lblOutgoing = new JLabel(CRConstants.OUT);
		add(lblOutgoing, "cell 1 1,growx");
		
		lblIn = new JLabel();
		add(lblIn, "cell 0 2,grow");
		
		outList = new JList<>();
		outList.setCellRenderer(ItemStackCellRenderer.instance);
		add(outList, "cell 1 2,growx,aligny center");
	}
	@Override public void set(AlcoholInfo recipe) {
		lblMachine.setText(CRConstants.MACHINE+recipe.group().title());
		ItemEntry item = recipe.input;
		lblIntoxication.setText(CRConstants.DOSE+recipe.dose);
		lblIn.setIcon(item.icon());
		lblIn.setText(item.title());
		outList.setListData(VectorUtils.list2arr(recipe.output));
	}
	
	static final ItemStackCellRenderer renderer = new ItemStackCellRenderer();
	
}