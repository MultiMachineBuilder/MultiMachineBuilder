/**
 * 
 */
package mmb.WORLD.contentgen;

import javax.annotation.Nonnull;
import javax.swing.Icon;

import mmb.UnitFormatter;
import mmb.WORLD.block.BlockEntityType;
import mmb.WORLD.crafting.Craftings;
import mmb.WORLD.electric.BlockConduit;
import mmb.WORLD.electric.ElecRenderer;
import mmb.WORLD.electric.VoltageTier;
import mmb.WORLD.item.Item;
import mmb.WORLD.item.Items;
import mmb.WORLD.items.ItemEntry;
import monniasza.collects.Collects;
import monniasza.collects.Identifiable;
import monniasza.collects.selfset.HashSelfSet;
import monniasza.collects.selfset.SelfSet;
import static mmb.GlobalSettings.*;

/**
 * @author oskar
 *
 */
public class WireGroup implements Identifiable<String> {
	@Nonnull public final BlockEntityType tiny;
	@Nonnull public final BlockEntityType small;
	@Nonnull public final BlockEntityType medium;
	@Nonnull public final BlockEntityType large;
	@Nonnull public final BlockEntityType huge;
	@Nonnull public final String id;
	@Nonnull public final String title;
	@Nonnull public final VoltageTier volt;
	@Nonnull public final MetalGroup group;
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
		Craftings.wiremill.add(group.base, medium, group.volt, group.power/2);
		Craftings.wiremill.add(tiny, group.wire, 4, group.volt, group.power/8);
		Craftings.wiremill.add(small, group.wire, 8, group.volt, group.power/4);
		Craftings.wiremill.add(medium, group.wire, 16, group.volt, group.power/2);
		
		gridCombo(tiny, small,   0.0625);
		gridCombo(small, medium, 0.125);
		gridCombo(medium, large, 0.25);
		gridCombo(large, huge,   0.5);
		
		Items.tagsItems(new String[]{"voltage-"+volt.name, "machine-wire"}, tiny, small, medium, large, huge);
	}
	//Various helper methods
	@Nonnull public static BlockEntityType conduit(String title, double pwr, ElecRenderer texture, String id, VoltageTier volt) {
		BlockEntityType b = new BlockEntityType() {
			@Override public Icon icon() {
				return texture.icon;
			}
		};
		return b.title(title)
				.factory(() -> new BlockConduit(b, pwr, volt))
				.texture(texture)
				.describe("Power: "+UnitFormatter.formatPower(pwr))
				.finish(id);
	}
	private void gridCombo(Item smaller, Item larger, double scale) {
		Craftings.crafting.addRecipeGrid(smaller, 1, 2, larger);
		Craftings.crafting.addRecipe(larger, smaller, 2);
		Craftings.splitter.add(larger,    smaller, 2, group.volt, scale*group.power);
		Craftings.combiner.add(smaller, 2, larger,    group.volt, scale*group.power);
	}
	
	//Localization
	private static final String[] matnames = {$res("mattype-wire1"), $res("mattype-wire2"), $res("mattype-wire3"), $res("mattype-wire4"), $res("mattype-wire5")};
	
	//Indexing
	@Override
	public String id() {
		return id;
	}
	@Nonnull private static final SelfSet<@Nonnull String, @Nonnull WireGroup> _index = new HashSelfSet<>();
	@Nonnull public static final SelfSet<@Nonnull String, @Nonnull WireGroup> index = Collects.unmodifiableSelfSet(_index);
}
