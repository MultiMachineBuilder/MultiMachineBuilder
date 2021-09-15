package mmb.debug;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

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
		try {
			PrintWriter writer = new PrintWriter("log.txt", "UTF-8");
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			System.setOut(new TeePrintStream("log.txt", System.out));
		} catch (FileNotFoundException e1) {
			Main.crash(e1);
		}
		initialized = true;
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
	 * @param keyChar
	 */
	public void printl(char ch) {
		System.out.print('(');
		System.out.print(id);
		System.out.print(')');
		System.out.print(ch);
	}

	public void printerr(String s) {
		System.err.print('(');
		System.err.print(id);
		System.err.print(") ");
		System.err.print(s);
	}
	public void printerrl(String s) {
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
