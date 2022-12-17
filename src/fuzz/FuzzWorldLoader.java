/**
 * 
 */
package fuzz;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.gitlab.javafuzz.core.AbstractFuzzTarget;

import mmb.Main;
import mmb.engine.files.LocalFile;
import mmb.engine.files.Save;import mmb.engine.window.FullScreen;
import mmbbase.menu.main.PanelSaves;
import mmbbase.menu.world.window.WorldWindow;

/**
 *
 * @author oskar
 *
 */
public class FuzzWorldLoader extends AbstractFuzzTarget {
	private WorldWindow ww;
	
	public FuzzWorldLoader() {
		//Load the game as a normal
		Main.main();
	}
	
	@Override
	public void fuzz(byte[] save) {
		
		
		try {
			ww = new WorldWindow();
			FullScreen.setWindow(ww);
			
			File file = File.createTempFile("fuzzworld-", ".mworld");
			try(FileOutputStream fos = new FileOutputStream(file)){
				fos.write(save);
				fos.flush();
			}
			
			//Run the game
			Save s = new Save(new LocalFile(file));
			PanelSaves.INSTANCE.fullPlay(s, ww);
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(ww != null) ww.dispose();
		}
		
		
	}
	
}
