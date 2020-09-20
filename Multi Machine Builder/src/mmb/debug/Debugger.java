package mmb.debug;

import java.io.FileNotFoundException;
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
		Throwable cause0 = t.getCause();
		String cause = cause0 == null ? "Cause unknown" : cause0.getMessage();
		StringBuilder message = new StringBuilder(s);
		message.append(" ");
		message.append(t.getClass().getName());
		message.append("\nCaused by: ");
		message.append(cause);
		message.append(" ");
		message.append(t.getMessage());
		printl(message.toString());
		printStackTrace(t.getStackTrace());
	}
	public void pst(Throwable t) {
		printl(t.getMessage());
		printStackTrace(t.getStackTrace());
	}
	public void print(String s) {
		printFinal("("+id+") "+ s);
	}
	public void printl(String s) {
		print(s + "\n");
	}
	private void printFinal(String s) {
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
}
