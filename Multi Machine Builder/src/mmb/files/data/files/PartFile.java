/**
 * 
 */
package mmb.files.data.files;

import mmb.parts.PartData;

/**
 * @author oskar
 *
 */
public interface PartFile {
	default void addPartByFile() {
		
	}
	PartData loadSpecs();
}
