/**
 * 
 */
package mmb.WORLD.contentgen;

import java.awt.Color;

import javax.annotation.Nonnull;
import javax.swing.Icon;

import mmb.WORLD.block.BlockEntityType;
import mmb.WORLD.crafting.Craftings;
import mmb.WORLD.electric.Conduit;
import mmb.WORLD.electric.ElecRenderer;
import mmb.WORLD.items.ItemEntry;
import monniasza.collects.Collects;
import monniasza.collects.Identifiable;
import monniasza.collects.selfset.HashSelfSet;
import monniasza.collects.selfset.SelfSet;

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
	/**
	 * @param c color of the cable
	 * @param mul power of smallest wire in watts
	 * @param group the imetal group
	 * @param id the identifier of the cables
	 * @param title display title
	 */
	public WireGroup(Color c, double mul, MetalGroup group, String id, String title) {
		ElecRenderer tiny0  =  ElecRenderer.repaint(c, ElecRenderer.tiny);
		ElecRenderer small0 =  ElecRenderer.repaint(c, ElecRenderer.small);
		ElecRenderer medium0 = ElecRenderer.repaint(c, ElecRenderer.medium);
		ElecRenderer large0 =  ElecRenderer.repaint(c, ElecRenderer.large);
		tiny   = conduit(title+" tiny power cable", mul,    tiny0,   "elecwire.tiny."+id);
		small  = conduit(title+" small power cable", mul*3,  small0,  "elecwire.small."+id);
		medium = conduit(title+" medium power cable", mul*10, medium0, "elecwire.medium."+id);
		large  = conduit(title+" large power cable", mul*30, large0,  "elecwire.large."+id);
		this.title = title;
		this.id = id;
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
	}
	//Various helper methods
	@Nonnull public static BlockEntityType conduit(String title, double pwr, ElecRenderer texture, String id) {
		BlockEntityType b = new BlockEntityType() {
			@Override public Icon icon() {
				return texture.icon;
			}
		};
		return b.title(title)
				.factory(() -> new Conduit(b, pwr))
				.texture(texture)
				.finish(id);
	}
	
	//Indexing
	@Override
	public String id() {
		return id;
	}
	@Nonnull private static final SelfSet<@Nonnull String, @Nonnull WireGroup> _index = new HashSelfSet<>();
	@Nonnull public static final SelfSet<@Nonnull String, @Nonnull WireGroup> index = Collects.unmodifiableSelfSet(_index);
}
