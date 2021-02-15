/**
 * 
 */
package mmb;

import javax.annotation.Nullable;

import mmb.RUNTIME.RuntimeManager;

/**
 * @author oskar
 *
 */
public interface RunnableObject extends GameObject {
	/**
	 * Get a runtime manager which runs given {@code GameObject}.
	 * Return null to indicate that game object does not have runtime
	 * @return a runtime manager, or null
	 */
	@Nullable
	public RuntimeManager getRuntimeManager();
}
