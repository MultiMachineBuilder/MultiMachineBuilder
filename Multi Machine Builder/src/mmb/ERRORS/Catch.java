/**
 * 
 */
package mmb.ERRORS;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author oskar
 *
 */
public class Catch {
	@SuppressWarnings("unchecked")
	public static <T extends Throwable> void catchByType(Class<T> extype, Runnable action, Consumer<T> catcher){
		try {
			action.run();
		// deepcode ignore DontCatch: defined by class literal		}catch(Throwable e) {
			if(extype.isInstance(e)) catcher.accept((T) e);
			else throw e;
		}
	}
	public static void catchIf(Runnable action, Predicate<Throwable> test, Consumer<Throwable> catcher) {
		try {
			action.run();
		// deepcode ignore DontCatch: defined by class literal
		}catch(Throwable e) {
			if(test.test(e)) catcher.accept(e);
			else throw e;
		}
	}
}
