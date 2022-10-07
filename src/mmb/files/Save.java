/**
 * 
 */
package mmb.files;

/**
 * @author oskar
 *
 */
public class Save {
	public AdvancedFile file;
	public String name;
	/**
	 * Creates a save record for this save file
	 * @param file the file with save
	 */
	@SuppressWarnings("null")
	public Save(AdvancedFile file) {
		this.file = file;
		name = AdvancedFile.dirName(file.name())[1];
	}

}
