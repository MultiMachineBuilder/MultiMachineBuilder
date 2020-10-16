/**
 * 
 */
package mmb.files.data.cfgfiles.parser;

import mmb.files.data.cfgfiles.CFG.*;

/**
 * @author oskar
 *
 */
public interface Creatable<T> {
	public T createFromCFG(Node data);
	public String getCreatorNode();
}
