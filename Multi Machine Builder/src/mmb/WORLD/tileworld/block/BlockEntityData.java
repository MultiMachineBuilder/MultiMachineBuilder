/**
 * 
 */
package mmb.WORLD.tileworld.block;

import java.util.function.Function;

import com.google.gson.JsonObject;

/**
 * @author oskar
 *
 */
public interface BlockEntityData extends Cloneable {
	Function<BlockEntityData,JsonObject> universalSaver = (BlockEntityData o) -> {return o.save();};
	JsonObject save();
	String name();
	/**
	 * @return
	 */
    BlockEntityData clone();
}
