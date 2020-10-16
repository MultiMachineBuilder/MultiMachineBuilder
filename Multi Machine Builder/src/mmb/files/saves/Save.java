/**
 * 
 */
package mmb.files.saves;

import org.apache.commons.vfs2.FileObject;

/**
 * @author oskar
 *
 */
public class Save {
	public FileObject file;
	public String name;
	/**
	 * 
	 */
	public Save(FileObject file) {
		this.file = file;
		name = file.getName().getBaseName();
	}

}
