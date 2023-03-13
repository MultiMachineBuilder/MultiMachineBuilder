/**
 * 
 */
package mmb.content.rawmats;

import static mmb.engine.settings.GlobalSettings.*;

import javax.swing.Icon;

import mmb.NN;
import mmb.content.CraftingGroups;
import mmb.content.electric.BlockConduit;
import mmb.content.electric.VoltageTier;
import mmb.content.electric.machines.ElecRenderer;
import mmb.engine.UnitFormatter;
import mmb.engine.block.BlockEntityType;
import mmb.engine.item.Item;
import mmb.engine.item.Items;
import monniasza.collects.Collects;
import monniasza.collects.Identifiable;
import monniasza.collects.selfset.HashSelfSet;
import monniasza.collects.selfset.SelfSet;

/**
 * Defines a set of 5 power cables with different power ratings, but with the same voltage
 * @author oskar
 */
public class WireGroup implements Identifiable<String> {
	@NN public final BlockEntityType tiny;
	@NN public final BlockEntityType small;
	@NN public final BlockEntityType medium;
	@NN public final BlockEntityType large;
	@NN public final BlockEntityType huge;
	@NN public final String id;
	@NN public final String title;
	@NN public final VoltageTier volt;
	@NN public final MetalGroup group;
	/**
	 * @param mul power of smallest wire in watts
	 * @param group the imetal group
	 * @param volt voltage tier
	 */
	public WireGroup(double mul, MetalGroup group, VoltageTier volt) {
		this.volt = volt;
		this.group = group;
		ElecRenderer tiny0  =  ElecRenderer.repaint(group.c, ElecRenderer.tiny);
		ElecRenderer small0 =  ElecRenderer.repaint(group.c, ElecRenderer.small);
		ElecRenderer medium0 = ElecRenderer.repaint(group.c, ElecRenderer.medium);
		ElecRenderer large0 =  ElecRenderer.repaint(group.c, ElecRenderer.large);
		ElecRenderer huge0 =   ElecRenderer.repaint(group.c, ElecRenderer.huge);
		this.title = group.t_adjective;
		this.id = group.id;
		tiny   = conduit(title+" "+matnames[0], mul/4, tiny0,   "elecwire.tiny."+id, volt);
		small  = conduit(title+" "+matnames[1], mul/2, small0,  "elecwire.small."+id, volt);
		medium = conduit(title+" "+matnames[2], mul,   medium0, "elecwire.medium."+id, volt);
		large  = conduit(title+" "+matnames[3], mul*2, large0,  "elecwire.large."+id, volt);
		huge   = conduit(title+" "+matnames[4], mul*4, huge0,   "elecwire.huge."+id, volt);
		_index.add(this);
		
		//Crafting recipes
		CraftingGroups.wiremill.add(group.base, medium, group.volt, group.baseCost/2);
		CraftingGroups.wiremill.add(tiny, group.wire, 4, group.volt, group.baseCost/8);
		CraftingGroups.wiremill.add(small, group.wire, 8, group.volt, group.baseCost/4);
		CraftingGroups.wiremill.add(medium, group.wire, 16, group.volt, group.baseCost/2);
		
		gridCombo(tiny, small,   0.0625);
		gridCombo(small, medium, 0.125);
		gridCombo(medium, large, 0.25);
		gridCombo(large, huge,   0.5);
		
		Items.tagsItems(new String[]{"voltage-"+volt.name, "machine-wire"}, tiny, small, medium, large, huge);
	}
	//Various helper methods
	@NN public static BlockEntityType conduit(String title, double pwr, ElecRenderer texture, String id, VoltageTier volt) {
		BlockEntityType b = new BlockEntityType() {
			@Override public Icon icon() {
				return texture.icon;
			}
		};
		return b.title(title)
				.factory(() -> new BlockConduit(b, pwr, volt))
				.texture(texture)
				.describe($res("machine-power")+" "+UnitFormatter.formatPower(pwr))
				.finish(id);
	}
	private void gridCombo(Item smaller, Item larger, double scale) {
		CraftingGroups.crafting.addRecipeGrid(smaller, 1, 2, larger);
		CraftingGroups.crafting.addRecipe(larger, smaller, 2);
		CraftingGroups.splitter.add(larger,          smaller, 2, group.volt, scale*group.baseCost);
		CraftingGroups.combiner.add(smaller.stack(2), larger,    group.volt, scale*group.baseCost);
	}
	
	//Localization
	private static final String[] matnames = {$res("mattype-wire1"), $res("mattype-wire2"), $res("mattype-wire3"), $res("mattype-wire4"), $res("mattype-wire5")};
	
	//Indexing
	@Override
	public String id() {
		return id;
	}
	@NN private static final SelfSet<@NN String, @NN WireGroup> _index = HashSelfSet.createNonnull(WireGroup.class);
	/** Set of all wire groups */
	@NN public static final SelfSet<@NN String, @NN WireGroup> index = Collects.unmodifiableSelfSet(_index);
}
