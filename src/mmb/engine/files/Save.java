/**
 * 
 */
package mmb.engine.files;

/**
 * A reference to a saved game
 * @author oskar
 */
public class Save {
	/** The file with a saved game */
	public AdvancedFile file;
	/** Name of the saved game */
	public String name;
	/**
	 * Creates a save record for this save file
	 * @param file the file with save
	 */
	public Save(AdvancedFile file) {
		this.file = file;
		name = AdvancedFile.dirName(file.name())[1];
	}

}
