/**
 * 
 */
package mmb;
import java.lang.reflect.Field;

import mmb.ERRORS.UndeclarableThrower;
import sun.misc.Unsafe;
/**
 * @author oskar
 *
 */
public class Unsafes {
	
	private static Unsafe init(){
		try {
			Class<?> cls = Unsafe.class;
			Field field = cls.getDeclaredField("theUnsafe");
			field.setAccessible(true);
			return (Unsafe) field.get(null);
		}catch(Exception e) {
			UndeclarableThrower.shoot(e);
			return null;
		}
	}
	public static final Unsafe unsafe = init();

}
