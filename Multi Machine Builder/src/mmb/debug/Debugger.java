package mmb.debug;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.annotation.Nullable;

import mmb.Main;

/**
 * 
 * @author oskar
 */
@SuppressWarnings({ "resource", "null" })
public class Debugger {
	private static boolean initialized = false;
	//Static code
	static {
		if(Main.isRunning()){ //suppress init if the game is not run
			System.out.println("Creating log file");
			try {
				File file = new File("log.txt");
				boolean create = file.createNewFile();
				if(!create) {
					PrintWriter writer = new PrintWriter(file, "UTF-8");
					writer.close();
				}
				OutputStream stream = new FileOutputStream(file, false);
				System.setOut(new TeePrintStream(stream, System.out));
				System.setErr(new TeePrintStream(stream, System.err));
			} catch (IOException e1) {
				Main.crash(e1);
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
		t.printStackTrace();
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
