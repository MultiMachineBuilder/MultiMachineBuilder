/**
 * 
 */
package mmb.WORLD.gui.craft;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.ListCellRenderer;

import mmb.WORLD.crafting.RecipeOutput;
import mmb.WORLD.crafting.recipes.ElectroSimpleProcessingRecipeGroup.ElectroSimpleProcessingRecipe;
import mmb.WORLD.crafting.recipes.StackedProcessingRecipeGroup.StackedProcessingRecipe;
import mmb.WORLD.inventory.ItemStack;
import mmb.WORLD.items.ItemEntry;

import javax.swing.JList;
import javax.swing.JPanel;

/**
 * @author oskar
 *
 */
public class StackedRecipeView extends JPanel {
	private static final long serialVersionUID = -2864705123116802475L;
	private JLabel lblVolt;
	private JLabel lblEnergy;
	private JLabel lblIncoming;
	private JLabel lblOutgoing;
	private JLabel lblIn;
	private JList<ItemStack> outList;
	
	public StackedRecipeView() {
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
		
		outList = new JList<>();
		outList.setCellRenderer(new CellRenderer());
		add(outList, "cell 1 3,growx,aligny center");
	}
	public void set(StackedProcessingRecipe recipe, ItemStack[] vector) {
		lblVolt.setText(CRConstants.VOLT+recipe.voltage.name);
		lblEnergy.setText(CRConstants.ENERGY+recipe.energy);
		lblMachine.setText(CRConstants.MACHINE+recipe.group.title);
		ItemEntry item = recipe.input;
		lblIn.setIcon(item.icon());
		lblIn.setText(item.title() +" x "+recipe.amount);
		outList.setListData(vector);
	}
	
	static final CellRenderer renderer = new CellRenderer();
	private JLabel lblMachine;
	static class CellRenderer extends JLabel implements ListCellRenderer<ItemStack>{
		private static final long serialVersionUID = -3535344904857285958L;
		private final Dimension PRESENT = new Dimension(275, 32);
		private final Dimension ABSENT = new Dimension();
		@Override
		public Component getListCellRendererComponent(
			@SuppressWarnings("null") JList<? extends ItemStack> list,
			@SuppressWarnings("null") ItemStack itemType,
			int index,
			boolean isSelected,
			boolean cellHasFocus
		){
			if(itemType.amount == 0) {
				setPreferredSize(ABSENT);
				setMinimumSize(ABSENT);
			}else {
				setPreferredSize(PRESENT);
				setMinimumSize(PRESENT);
			}
			setOpaque(true);
			setIcon(itemType.item.icon());
			setText(itemType.id().title() + " � " + itemType.amount);
			
			if (isSelected) {
			    setBackground(list.getSelectionBackground());
			    setForeground(list.getSelectionForeground());
			} else {
			    setBackground(list.getBackground());
			    setForeground(list.getForeground());
			}
			return this;
		}
	}
	@Nonnull public static Vector<ItemStack> list2vector(RecipeOutput output){
		return output
				.getContents()
				.object2IntEntrySet()
				.stream()
				.map(ent -> new ItemStack(ent.getKey(), ent.getIntValue()))
				.collect(Collectors.toCollection(() -> new Vector<ItemStack>()));
	}
	@Nonnull public static ItemStack[] list2arr(RecipeOutput output){
		return output
				.getContents()
				.object2IntEntrySet()
				.stream()
				.map(entry -> new ItemStack(entry.getKey(), entry.getIntValue()))
				.toArray(n -> new ItemStack[n]);
	}
}
