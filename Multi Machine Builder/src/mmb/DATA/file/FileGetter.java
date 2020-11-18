/**
 * 
 */
package mmb.DATA.file;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;

import mmb.DATA.contents.texture.Textures;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class FileGetter {
	private static String home;
	private static FileSystemManager fileGetter;
	private static final Debugger debug = new Debugger("FILES");
	
	static public void init(){
		try {
			home = System. getProperty("user.dir")+"/";
			fileGetter = VFS.getManager();
			debug.printl(home);
		} catch (FileSystemException e) {
			debug.pstm(e, "Unable to connect to file system");
		}
	}
	
	@Deprecated
	public static FileSystemManager getFileGetter() {
		return fileGetter;
	}
	@Deprecated
	public static String getHome() {
		return home;
	}
	@Deprecated
	public static FileObject getFileAbsolute(String path) throws FileSystemException {
		return fileGetter.resolveFile(path);
	}
	@Deprecated
	public static FileObject getFileRelative(String path) throws FileSystemException {
		return getFileAbsolute(home + path);
	}
	@Deprecated
	public static FileName getWebsite(String path) throws FileSystemException {
		return fileGetter.resolveURI(path);
	}
	
	/**
	 * @param relative path of desired ZIP file
	 * @return the root of ZIP file. To get other contents, use getAll()
	 * @throws FileSystemException 
	 */
	@Deprecated
	public static FileObject openZIPRelative(String file) throws FileSystemException {
		return openZIP(getFileRelative(file));
	}
	
	/**
	 * @param absolute path of desired ZIP file
	 * @return the root of ZIP file. To get other contents, use getAll()
	 * @throws FileSystemException 
	 */
	@Deprecated
	public static FileObject openZIPAbsolute(String file) throws FileSystemException {
		return openZIP(getFileAbsolute(file));
	}
	
	@Deprecated
	public static FileObject openZIP(FileObject file) throws FileSystemException {
		return fileGetter.createFileSystem(file);
	}
	
	/**
	 * Gets all successors of a given FileObject
	 * @param file root
	 * @return list of successors
	 * @throws FileSystemException if given file is erroneous
	 */
	@Deprecated
	public static List<FileObject> getAll(FileObject file) throws FileSystemException {
		if(!file.isFolder()) return new ArrayList<FileObject>();
		List<FileObject> result = new ArrayList<FileObject>();
		FileObject[] data = file.getChildren();
		result.addAll(Arrays.asList(data));
		for(int i = 0; i<data.length; i++) {
			FileObject item = data[i];
			List<FileObject> all = getAll(item);
			result.addAll(all);
		}
		return result;
	}
	
	public static AdvancedFile getFile(String path) throws MalformedURLException {
		if(path.startsWith("http://") || path.startsWith("https://")) {
			return new OnlineFile(path);
		}
		return new LocalFile(path);
	}
	
	@Deprecated
	public static BufferedImage getTexture(String texture) throws IOException {
		//AdvancedFile x = new LocalFile("textures/"+texture);
		//return ImageIO.read(x.getInputStream());
		return Textures.get(texture);
	}
	
}
