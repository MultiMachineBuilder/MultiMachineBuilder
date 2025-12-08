/**
 * 
 */
package mmb.content.electric.recipes;

import java.util.Vector;
import java.util.stream.Collectors;

import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JList;

import org.ainslec.picocog.PicoWriter;

import io.github.parubok.text.multiline.MultilineLabel;
import mmb.annotations.NN;
import mmb.content.electric.recipes.StackedRecipeGroup.StackedRecipe;
import mmb.engine.UnitFormatter;
import mmb.engine.item.ItemEntry;
import mmb.engine.recipe.CRConstants;
import mmb.engine.recipe.ItemStack;
import mmb.engine.recipe.RecipeOutput;
import mmb.engine.recipe.RecipeView;
import mmb.engine.recipe.VectorUtils;
import mmb.menu.world.ItemStackCellRenderer;

/**
 * Represents a recipe view for stacked-item recipes 
 * @author oskar
 */
public class StackedRecipeView extends RecipeView<StackedRecipe> {
	private static final long serialVersionUID = -2864705123116802475L;
	private JLabel lblVolt;
	private JLabel lblEnergy;
	private JLabel lblIncoming;
	private JLabel lblOutgoing;
	private JLabel lblIn;
	private JList<ItemStack> outList;
	private JLabel lblMachine;
	private MultilineLabel lblMaybe;
	
	/** Creates a recipe view for stacked-item recipes */
	public StackedRecipeView() {
		setLayout(new MigLayout("", "[grow][grow]", "[][][][]"));
		
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
		
		lblOutgoing = new JLabel(CRConstants.OUT);
		add(lblOutgoing, "cell 1 2,growx");
		
		lblIn = new JLabel();
		add(lblIn, "cell 0 3,grow");
		
		outList = new JList<>();
		outList.setCellRenderer(ItemStackCellRenderer.instance);
		add(outList, "cell 1 3,growx,aligny center");
	}
	@Override public void set(StackedRecipe recipe) {
		lblVolt.setText(CRConstants.VOLT+recipe.voltage.name);
		lblEnergy.setText(CRConstants.ENERGY+UnitFormatter.formatEnergy(recipe.energy));
		lblMachine.setText(CRConstants.MACHINE+recipe.group().title());
		ItemEntry item = recipe.item();
		lblIn.setIcon(item.icon());
		lblIn.setText(item.title() +" x "+recipe.amount());
		outList.setListData(VectorUtils.list2arr(recipe.output));
		PicoWriter writer = new PicoWriter();
		writer.writeln(CRConstants.CHANCE);
		recipe.luck().represent(writer);
		lblMaybe.setText(writer.toString());
	}
	
	
	@NN public static Vector<ItemStack> list2vector(RecipeOutput output){
		return output
				.getContents()
				.object2IntEntrySet()
				.stream()
				.map(ent -> new ItemStack(ent.getKey(), ent.getIntValue()))
				.collect(Collectors.toCollection(Vector::new));
	}
	@NN public static ItemStack[] list2arr(RecipeOutput output){
		return output
				.getContents()
				.object2IntEntrySet()
				.stream()
				.map(entry -> new ItemStack(entry.getKey(), entry.getIntValue()))
				.toArray(n -> new ItemStack[n]);
	}
}
