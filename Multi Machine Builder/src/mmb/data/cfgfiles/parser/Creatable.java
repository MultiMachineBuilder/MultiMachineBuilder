/**
 * 
 */
package mmb.data.cfgfiles.parser;

import mmb.data.cfgfiles.CFG.*;

/**
 * @author oskar
 *
 */
public interface Creatable<T> {
	public T createFromCFG(Node data);
	public String getCreatorNode();
}
