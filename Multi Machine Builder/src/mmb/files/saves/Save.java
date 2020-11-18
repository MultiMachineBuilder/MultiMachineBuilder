/**
 * 
 */
package mmb.files.saves;

import mmb.DATA.file.AdvancedFile;

/**
 * @author oskar
 *
 */
public class Save {
	public AdvancedFile file;
	public String name;
	/**
	 * 
	 */
	public Save(AdvancedFile file) {
		this.file = file;
		name = AdvancedFile.dirName(file.name())[1];
	}

}
