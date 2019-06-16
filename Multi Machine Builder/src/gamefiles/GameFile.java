package gamefiles;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;

import debug.Avaliability;
import debug.Debugger;

public class GameFile {
	public Avaliability avaliability = new Avaliability();
	public InputStream file;
	public GameFile() {
		// TODO Auto-generated constructor stub
	}
	public GameFile(File f) {
		try {
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
}
