/**
 * 
 */
package mmb.engine.files;

import java.util.Map;

import org.apache.commons.csv.CSVRecord;

import mmb.NN;
import mmb.engine.debug.Debugger;

/**
 * @author oskar
 *
 */
public class Downloadable {

	/** The mod name of downloadable mod*/
	public final String name;
	/** The download URL */
	public final String download;
	private final Debugger debug;
	/**
	 * @param record record to be parsed
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
	 * @param directory directory to place the downloaded file in
	 */
	public void download(AdvancedFile directory) {
		@NN String fileName = download.substring(download.lastIndexOf('/')+1, download.length()-1);
		String tgt = "mods/"+fileName;
		try {
			debug.printl("Connecting with "+download);
			AdvancedFile source = FileUtil.getFile(fileName);
			
			debug.printl("Opening "+tgt);
			@NN AdvancedFile target = FileUtil.getFile(tgt);
			
			debug.printl("Downloading "+download);
			source.copyTo(target);
		} catch (Exception e) {
			debug.print("Couldn't download ");
			debug.print(download);
			debug.print(" to ");
			debug.stacktraceError(e, tgt);
		}
		
	}

}
