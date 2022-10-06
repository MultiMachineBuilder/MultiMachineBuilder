package mmb.debug;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import javax.annotation.Nullable;

import mmb.Main;
import mmb.UndeclarableThrower;

/**
 * 
 * @author oskar
 */
@SuppressWarnings({ "resource", "null" })
public class Debugger {
	private static boolean initialized = false;	
	public static void init() {
		if(initialized) return;
		if(Main.isRunning()){ //suppress init if the game is not run
			try {
				File file = new File("log.txt");
				boolean success = file.createNewFile();
				System.out.println("Creating log file: "+success);			
				OutputStream stream = new FileOutputStream(file, false);
				System.setOut(new TeePrintStream(stream, System.out));
				System.setErr(new TeePrintStream(stream, System.err));
			} catch (Exception e1) {
				UndeclarableThrower.shoot(e1);
			}
			initialized = true;
		}
	}
	
	public void pstm(Throwable t, @Nullable String s) {
		printerrl(s);
		pst(t);
	}
	public void pst(Throwable t) {
		printName();
		t.printStackTrace(System.err);
	}
	public void print(@Nullable String s) {
		printName();
		System.out.print(s);
	}
	private void printName() {
		System.out.print('(');
		System.out.print(id);
		System.out.print(") ");
	}
	public void printl(@Nullable String s) {
		print(s);
		System.out.print('\n');
	}
	//Instance code
	public String id = "";
	public Debugger(String id) {
		this.id = id;
	}
	/**
	 * @param ch characted to be printed
	 */
	public void printl(char ch) {
		System.out.print('(');
		System.out.print(id);
		System.out.print(')');
		System.out.print(ch);
	}

	public void printerr(@Nullable String s) {
		System.err.print('(');
		System.err.print(id);
		System.err.print(") ");
		System.err.print(s);
	}
	public void printerrl(@Nullable String s) {
		printerr(s);
		System.err.print('\n');
	}
	/**
	 * @return is initialized();
	 */
	public static boolean isInitialized() {
		return initialized;
	}
}
