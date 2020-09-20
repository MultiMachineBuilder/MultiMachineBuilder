/**
 * 
 */
package mmb.ships.modules;
import mmb.ships.*;
/**
 * @author oskar
 * @param <T> this type
 * @param <U> key type
 */
public interface ShipModule<T extends Module<T, Unit, U>, U> extends Module<T, Unit, U> {
	
	
}
