/**
 * 
 */
package mmb.downloader;

import java.util.Map;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.vfs2.AllFileSelector;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;

import mmb.debug.Debugger;
import mmb.files.FileGetter;

/**
 * @author oskar
 *
 */
public class Downloadable {

	public String name;
	public String download;
	private Debugger debug;
	/**
	 * 
	 */
	public Downloadable(CSVRecord record) {
		debug = new Debugger("CSV PARSER");
		debug.printl("Loading entry #"+record.getRecordNumber());
		Map<String, String> data = record.toMap();
		name = data.get("name");
		download = data.get("download");
		debug.id = "DOWNLOADABLE-"+name;
	}
	
	/**
	 * Downloads this file to a given directory
	 * @param directory
	 */
	public void download(FileObject directory) {
		String fileName = download.substring(download.lastIndexOf(47 /*slash*/)+1, download.length()-1);
		String tgt = "mods/"+fileName;
		try {
			//fileName = fileName.substring(fileName.lastIndexOf(92 /*backslash*/)+1, fileName.length()-1);
			debug.printl("Connecting with "+download);
			FileObject source = FileGetter.getFileAbsolute(download);
			
			debug.printl("Opening "+tgt);
			FileObject target = FileGetter.getFileRelative(tgt);
			
			debug.printl("Downloading "+download);
			target.copyFrom(target, new AllFileSelector());
		} catch (FileSystemException e) {
			debug.print("Couldn't download ");
			debug.print(download);
			debug.print(" to ");
			debug.printl(tgt);
		}
		
	}

}
