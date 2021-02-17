/**
 * 
 */
package mmb.FILES;

import java.util.Map;

import org.apache.commons.csv.CSVRecord;

import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class Downloadable {

	public String name;
	public String download;
	private final Debugger debug;
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
	public void download(AdvancedFile directory) {
		String fileName = download.substring(download.lastIndexOf(47 /*slash*/)+1, download.length()-1);
		String tgt = "mods/"+fileName;
		try {
			//fileName = fileName.substring(fileName.lastIndexOf(92 /*backslash*/)+1, fileName.length()-1);
			debug.printl("Connecting with "+download);
			AdvancedFile source = FileUtil.getFile(fileName);
			
			debug.printl("Opening "+tgt);
			AdvancedFile target = FileUtil.getFile(tgt);
			
			debug.printl("Downloading "+download);
			source.copyTo(target);
		} catch (Exception e) {
			debug.print("Couldn't download ");
			debug.print(download);
			debug.print(" to ");
			debug.printl(tgt);
		}
		
	}

}
