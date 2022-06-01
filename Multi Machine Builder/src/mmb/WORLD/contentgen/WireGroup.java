/**
 * 
 */
package mmb.WORLD.contentgen;

import javax.annotation.Nonnull;
import javax.swing.Icon;

import mmb.WORLD.block.BlockEntityType;
import mmb.WORLD.crafting.Craftings;
import mmb.WORLD.electric.BlockConduit;
import mmb.WORLD.electric.ElecRenderer;
import mmb.WORLD.electric.VoltageTier;
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
	@Nonnull public final String id;
	@Nonnull public final String title;
	@Nonnull public final VoltageTier volt;
	/**
	 * @param mul power of smallest wire in watts
	 * @param group the imetal group
	 * @param volt voltage tier
	 */
	public WireGroup(double mul, MetalGroup group, VoltageTier volt) {
		this.volt = volt;
		ElecRenderer tiny0  =  ElecRenderer.repaint(volt.c, ElecRenderer.tiny);
		ElecRenderer small0 =  ElecRenderer.repaint(volt.c, ElecRenderer.small);
		ElecRenderer medium0 = ElecRenderer.repaint(volt.c, ElecRenderer.medium);
		ElecRenderer large0 =  ElecRenderer.repaint(volt.c, ElecRenderer.large);
		this.title = group.t_adjective;
		this.id = group.id;
		tiny   = conduit(title+" "+matnames[0], mul   ,   tiny0,   "elecwire.tiny."+id, volt);
		small  = conduit(title+" "+matnames[1], mul* 3,  small0,  "elecwire.small."+id, volt);
		medium = conduit(title+" "+matnames[2], mul*10, medium0, "elecwire.medium."+id, volt);
		large  = conduit(title+" "+matnames[3], mul*30,  large0,  "elecwire.large."+id, volt);
		_index.add(this);
		
		//Crafting recipes
		Craftings.wiremill.add(group.base, medium, group.volt, group.power/2);
		Craftings.wiremill.add(medium, group.wire, 16, group.volt, group.power/2);
		
		Craftings.crafting.addRecipeGrid(medium, 1, 3, large);
		Craftings.crafting.addRecipe(large, medium, 3);
		Craftings.crafting.addRecipe(medium, tiny, 10);
		Craftings.crafting.addRecipeGrid(tiny, 1, 3, small);
		Craftings.crafting.addRecipeGrid(new ItemEntry[] {small, small, small, tiny}, 2, 2, medium);
		
		Craftings.crafting.addRecipe(small, tiny, 3);
		
		Items.tagItems("voltage-"+volt.name, tiny, small, medium, large);
		Items.tagItems("machine-wire", tiny, small, medium, large);
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
				.finish(id);
	}
	
	//Localization
	private static final String[] matnames = {$res("mattype-wire1"), $res("mattype-wire2"), $res("mattype-wire3"), $res("mattype-wire4")};
	
	//Indexing
	@Override
	public String id() {
		return id;
	}
	@Nonnull private static final SelfSet<@Nonnull String, @Nonnull WireGroup> _index = new HashSelfSet<>();
	@Nonnull public static final SelfSet<@Nonnull String, @Nonnull WireGroup> index = Collects.unmodifiableSelfSet(_index);
}
