package debug;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Debugger {
	
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
	
	public static void pst(Exception e) {
		e.printStackTrace(log);
	}
	public static void print(String s) {
		log.print(s);
		System.out.print(s);
	}
	public static void printl(String s) {
		log.println(s);
		System.out.println(s);
	}

}
