package mmb.debug;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Debugger {
	//Static code
	static {
		try {
			PrintWriter writer = new PrintWriter("log.txt", "UTF-8");
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			log = new PrintStream("log.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	static protected PrintStream log;
	
	public void pstm(Throwable t, String s) {
		printl(s);
		printStackTrace(t.getStackTrace());
	}
	public void pst(Throwable t) {
		printl(t.getClass().getCanonicalName());
		printl(t.getMessage());
		printStackTrace(t.getStackTrace());
		if(t.getCause() != null) pstm(t.getCause(), "Caused by: ");
	}
	public void print(String s) {
		printFinal("("+id+") "+ s);
	}
	public void printl(String s) {
		print(s + "\n");
	}
	private static void printFinal(String s) {
		log.print(s);
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
