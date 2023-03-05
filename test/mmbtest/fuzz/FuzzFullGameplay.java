/**
 * 
 */
package mmbtest.fuzz;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeAll;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.code_intelligence.jazzer.junit.FuzzTest;
import com.rainerhahnekamp.sneakythrow.Sneaky;

import mmb.Main;
import mmb.engine.MMBUtils;
import mmb.engine.debug.Debugger;
import mmb.engine.files.LocalFile;
import mmb.engine.files.Save;
import mmb.menu.main.PlayAWorld;
import mmb.menu.world.window.WorldWindow;

/**
 *
 * @author oskar
 *
 */
class FuzzFullGameplay {
	
	private static final Debugger debug = new Debugger("FUZZ WORLD LOADER");
	private static File testfile;
	private static WorldWindow unclosedWorldWindows;

	static void setup() throws Exception {
		//hook so the test can fail without crashing rest of the game
		Main.errorhook(e -> crash = e);
		//start the game as normal
		Main.main();
		//prepare a slot for data
		testfile = File.createTempFile("fuzzing-", ".mworld");
	}

	private static Throwable crash;
	
	@FuzzTest
	void test(FuzzedDataProvider fdp) {
		try {
			if(!Main.isRunning()) {
				debug.printl("Setup...");
				setup();
			}
			
			debug.printl("START");
			//Stuff the file with data
			try(FileWriter w = new FileWriter(testfile)){
				debug.printl("Stuffing...");
				String s = fdp.consumeRemainingAsString();
				w.write(s);
				debug.printl("Stuffed the world");
			}
			Save save = new Save(new LocalFile(testfile));
			
			//Create a semaphore and an atomic reference to fetch error(s);
			Object semaphore = new Object();
			AtomicReference<Throwable> errhere = new AtomicReference<>();
			
			//Prepare the handlers
			PlayAWorld paw = new PlayAWorld();
			paw.onLoad.addListener(e ->{
				errhere.set(e);
				synchronized(semaphore){semaphore.notifyAll();}
			});
			
			//Load The World
			EventQueue.invokeLater(() -> {
				debug.printl("Playing soon...");
				unclosedWorldWindows = paw.play(save);
				debug.printl("GO GO GO!");
			});
			
			//Await and throw
			synchronized(semaphore){semaphore.wait();}
			debug.printl("Got it!");
			Throwable e1 = errhere.get();
			if(e1 != null) throw e1;
		}catch(Throwable e) {
			if(e instanceof InterruptedException) {
				debug.stacktraceError(e, "INTERRUPTED!");
				Thread.currentThread().interrupt();
			}
			if(crash != null) {
				debug.stacktraceError(e, "CRASH!");
				MMBUtils.shoot(e);
			}else {
				//debug.pstm(e, "Test exception, noncrashing");
			}
		}finally {
			Object semaphore = new Object();
			unclosedWorldWindows.addWindowListener(new WindowAdapter() {
				@Override public void windowClosed(WindowEvent e) {
					synchronized(semaphore){semaphore.notifyAll();}
				}
			});
			unclosedWorldWindows.dispose();
			Sneaky.sneaked(() -> {synchronized(semaphore){semaphore.wait();}}).run();
		}
	}

}
