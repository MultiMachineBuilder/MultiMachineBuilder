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
	T createFromCFG(Node data);
	String getCreatorNode();
}
