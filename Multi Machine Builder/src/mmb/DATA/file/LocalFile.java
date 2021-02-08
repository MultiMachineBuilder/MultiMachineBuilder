/**
 * 
 */
package mmb.DATA.file;

import java.io.*;
import java.nio.file.Path;

/**
 * @author oskar
 *
 */
public class LocalFile implements AdvancedFile {
	public File file;
	/**
	 * 
	 */
	public LocalFile(File file) {
		this.file = file;
	}
	public LocalFile(Path path) {
		file = path.toFile();
	}
	public LocalFile(String path) {
		file = new File(path);
	}

	/* (non-Javadoc)
	 * @see mmb.DATA.file.AdvancedFile#getInputStream()
	 */
	@Override
	public InputStream getInputStream() throws FileNotFoundException {
		return new FileInputStream(file);
	}

	/* (non-Javadoc)
	 * @see mmb.DATA.file.AdvancedFile#getOutputStream()
	 */
	@Override
	public OutputStream getOutputStream() throws FileNotFoundException {
		return new FileOutputStream(file);
	}

	/* (non-Javadoc)
	 * @see mmb.DATA.file.AdvancedFile#asFile()
	 */
	@Override
	public File asFile() {
		return file;
	}

	/* (non-Javadoc)
	 * @see mmb.DATA.file.AdvancedFile#name()
	 */
	@Override
	public String name() {
		return file.getAbsolutePath();
	}
	/* (non-Javadoc)
	 * @see mmb.DATA.file.AdvancedFile#parent()
	 */
	@Override
	public AdvancedFile parent() {
		return new LocalFile(file.getParentFile());
	}
	/* (non-Javadoc)
	 * @see mmb.DATA.file.AdvancedFile#children()
	 */
	@Override
	public AdvancedFile[] children(){
		File[] descendants = file.listFiles();
		if(descendants == null) return new AdvancedFile[0];
		AdvancedFile[] result = new AdvancedFile[descendants.length];
		for(int i = 0; i < descendants.length; i++) {
			result[i] = new LocalFile(descendants[i]);
		}
		return result;
	}
	/* (non-Javadoc)
	 * @see mmb.DATA.file.AdvancedFile#exists()
	 */
	@Override
	public boolean exists() {
		return file.exists();
	}
	@Override
	public void create() throws IOException {
		file.createNewFile();
	}
	@Override
	public boolean isDirectory() {
		return file.isDirectory();
	}

}
