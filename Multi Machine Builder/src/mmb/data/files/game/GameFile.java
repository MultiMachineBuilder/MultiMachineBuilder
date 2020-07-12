package mmb.data.files.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;

import mmb.debug.Debugger;

public class GameFile {
	/**
	 * Not public due to security concerns:
	 * Malicious mods can make game think that file is avaliable.
	 */
	boolean avaliable;
	
	private Debugger debug;
	public InputStream file;
	public GameFile() {
		// TODO Auto-generated constructor stub
	}
	public GameFile(File f) {
		try {
			String[] x = originalPath.split("//");
			String y = x[x.length - 1];
			String[] z = y.split("\\\\");
			String debugName = z[z.length - 1];
			debug = new Debugger("FILE "debugName);
			file = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public GameFile(Path f) {
		File ff = null;
		try {
			ff = f.toFile();
			file = new FileInputStream(ff);
		}catch(Exception e) {
			Debugger.printl("File" + f.toString() + "is unavaliable");
		}
	}
	public boolean isAvaliable() {
		return avaliable;
	}
}
