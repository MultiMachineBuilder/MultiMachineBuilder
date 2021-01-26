/**
 * 
 */
package mmb.WORLD.block.properties;

import com.google.gson.JsonElement;

import mmb.Identifiable;

/**
 * @author oskar
 * The {@code BlockPropertyInfo} represents a block property type. It is used by loaders.
 * The required bytecode will be generated in 0.5 using CGLIB and deployed as Supplier<BlockProperty> and Function<JsonElement, BlockProperty> using ByteBuddy
 */
public interface BlockPropertyInfo extends Identifiable<String>{
	public BlockProperty createNew();
	public BlockProperty load(JsonElement e);
	/**
	 * A displayed title
	 * @return on-screen title
	 */
	public String title();
}
