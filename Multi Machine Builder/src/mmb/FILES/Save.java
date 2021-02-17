/**
 * 
 */
package mmb.FILES;

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
