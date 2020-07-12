/**
 * 
 */
package mmb.data.files;

import mmb.world.parts.PartData;

/**
 * @author oskar
 *
 */
public interface PartFile {
	default public void addPartByFile() {
		
	}
	public PartData loadSpecs();
}
