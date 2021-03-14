package mmb.debug;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.annotation.Nullable;

/**
 * 
 * @author oskar
 */
public class Debugger {
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
		} catch (FileNotFoundException e) {
			// deepcode ignore DontUsePrintStackTrace: no workarounds avaliable			e.printStackTrace();
		}
	}
	
	public void pstm(Throwable t, @Nullable String s) {
		printl(s);
		pst(t);
	}
	public void pst(Throwable t) {
		printl(t.getClass().getCanonicalName());
		printl(t.getMessage());
		printStackTrace(t.getStackTrace());
		if(t.getCause() != null) pstm(t.getCause(), "Caused by: ");
	}
	public void print(@Nullable String s) {
		printFinal("("+id+") "+ s);
	}
	public void printl(@Nullable String s) {
		print(s + "\n");
	}
	private static void printFinal(String s) {
		System.out.print(s);
	}
	//Instance code
	public String id = "";
	public Debugger(String id) {
		this.id = id;
	}
	private void printStackTrace(StackTraceElement[] ste) {
		for(int i = 0; i < ste.length; i++) {
			printl(ste[i].toString());
		}
	}
	/**
	 * @param keyChar
	 */
	public void printl(char ch) {
		printFinal("("+id+") "+ch+ "\n");
	}
}
