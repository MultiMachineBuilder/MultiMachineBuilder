package mmb.testing;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mmb.DATA.file.AdvancedFile;
import mmb.SOUND.MP3Loader;
import mmb.WORLD.inventory.ItemInventory;
import mmb.WORLD.inventory.SimpleItem;
import mmb.debug.Debugger;

class Testing {
	private static Debugger debug = new Debugger("MP3");

	
	public static void main(String[] args) {
		
		File file = new File("sound/error.mp3");
		InputStream is;
		try {
			is = new FileInputStream(file);
			MP3Loader mp3l = new MP3Loader(is);
			mp3l.start();
			EventQueue.invokeLater(() -> {
			    try {
					mp3l.untilLoad();
				} catch (InterruptedException e) {}
			    mp3l.getClip().start();
			    try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {}
			});
		} catch (FileNotFoundException e1) {
			debug.pstm(e1, "Failed to load MP3 file");
		}
		
	}

}
