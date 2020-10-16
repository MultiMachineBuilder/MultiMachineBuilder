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
	default public void addPartByFile() {
		
	}
	public PartData loadSpecs();
}
