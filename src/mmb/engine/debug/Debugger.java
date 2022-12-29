package mmb.engine.debug;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import mmb.Main;
import mmb.NN;
import mmb.Nil;
import mmb.engine.MMBUtils;

/**
 * 
 * @author oskar
 */
@SuppressWarnings({ "resource", "null" })
public class Debugger {
	private static boolean initialized = false;
	
	//Log file creation
	private static OutputStream logstream;
	@NN private static OutputStream logstream() {
		OutputStream os = logstream;
		try {
			if(os == null) {
				File file = new File("log.txt");
				boolean success = file.createNewFile();
				PrintStream out = System.out;
				if(out != null) out.println("Creating log file: "+success);			
				os = new FileOutputStream(file, false);
				logstream = os;
			}
			return os;
		} catch (Exception e1) {
			MMBUtils.shoot(e1);
			return null;
		}
		
	}
	
	private static PrintStream err() {
		PrintStream out = System.err;
		if(out == null) 
			out = new PrintStream(logstream());
		return out;
	}	
	private static PrintStream out() {
		PrintStream out = System.out;
		if(out == null) 
			out = new PrintStream(logstream());
		return out;
	}
	
	public static void init() {
		if(initialized) return;
		if(Main.isRunning()){ //suppress init if the game is not run
			try {
				OutputStream stream = logstream();
				System.setOut(new TeePrintStream(stream, System.out));
				System.setErr(new TeePrintStream(stream, System.err));
			} catch (Exception e1) {
				MMBUtils.shoot(e1);
			}
			initialized = true;
		}
	}
	
	public void pstm(Throwable t, @Nil String s) {
		printerrl(s);
		pst(t);
	}
	public void pst(Throwable t) {
		printName();
		t.printStackTrace(System.err);
	}
	public void print(@Nil String s) {
		printName();
		out().print(s);
	}
	private void printName() {
		out().print('(');
		out().print(id);
		out().print(") ");
	}
	public void printl(@Nil String s) {
		print(s);
		out().print('\n');
	}
	//Instance code
	/** The debugger ID */
	@NN public String id = "";
	/**
	 * Creates a debugger
	 * @param id ddebugger ID
	 */
	public Debugger(String id) {
		init();
		this.id = id;
	}
	/**
	 * Prints a character
	 * @param ch character to be printed
	 */
	public void printl(char ch) {
		out().print('(');
		out().print(id);
		out().print(')');
		out().print(ch);
	}

	public void printerr(@Nil String s) {
		err().print('(');
		err().print(id);
		err().print(") ");
		err().print(s);
	}
	public void printerrl(@Nil String s) {
		printerr(s);
		err().print('\n');
	}
	/**
	 * @return is initialized();
	 */
	public static boolean isInitialized() {
		return initialized;
	}
}
