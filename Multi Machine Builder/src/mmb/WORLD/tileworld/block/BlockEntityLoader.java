/**
 * 
 */
package mmb.WORLD.tileworld.block;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import com.google.gson.JsonObject;

/**
 * @author oskar
 *
 */
public class BlockEntityLoader {	
	public Supplier<BlockEntityData> blockEntityGen = null;
	public Function<JsonObject, BlockEntityData> loader = null;
	public Function<BlockEntityData, JsonObject> saver = BlockEntityData.universalSaver;
	public UnaryOperator<BlockEntityData> survivalClone = (bed) -> bed.clone();
}
