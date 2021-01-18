/**
 * 
 */
package mmb.RUNTIME.actions;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Supplier;

/**
 * @author oskar
 *
 */
public class MMBRuntime {
	private static List<Supplier<WorldBehavior>> wbs = new ArrayList<Supplier<WorldBehavior>>();
	
	public static void addWorldBehavior(Supplier<WorldBehavior> wb){
		wbs.add(wb);
	}
	public static void removeWorldBehavior(Supplier<WorldBehavior> wb) {
		wbs.remove(wb);
	}
	@SuppressWarnings("unchecked")
	public static Supplier<WorldBehavior>[] getWorldBehaviors() {
		return wbs.toArray(new Supplier[wbs.size()]);
	}
}
