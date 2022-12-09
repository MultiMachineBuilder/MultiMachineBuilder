/**
 * 
 */
package mmb.engine.debug;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
 * @author oskar
 *
 */
public class TeePrintStream extends PrintStream {
	private final PrintStream second;
	/**
	 * @param arg0
	 * @param in second print stream
	 */
	public TeePrintStream(OutputStream arg0, PrintStream in) {
		super(arg0);
		second = in;
	}

	public TeePrintStream(String arg0, PrintStream in) throws FileNotFoundException {
		super(arg0);
		second = in;
	}

	public TeePrintStream(File arg0, PrintStream in) throws FileNotFoundException {
		super(arg0);
		second = in;
	}

	public TeePrintStream(OutputStream arg0, boolean arg1, PrintStream in) {
		super(arg0, arg1);
		second = in;
	}

	public TeePrintStream(String fileName, String csn, PrintStream in) throws FileNotFoundException, UnsupportedEncodingException {
		super(fileName, csn);
		second = in;
	}

	public TeePrintStream(File file, String csn, PrintStream in) throws FileNotFoundException, UnsupportedEncodingException {
		super(file, csn);
		second = in;
	}

	public TeePrintStream(OutputStream arg0, boolean arg1, String arg2, PrintStream in) throws UnsupportedEncodingException {
		super(arg0, arg1, arg2);
		second = in;
	}

	@Override
	public void close() {
		second.close();
		super.close();
	}

	@Override
	public void flush() {
		second.flush();
		super.flush();
	}
	@Override
	public void write(@SuppressWarnings("null") byte[] buf, int off, int len) {
		second.write(buf, off, len);
		super.write(buf, off, len);
	}

	@Override
	public void write(int b) {
		second.write(b);
		super.write(b);
	}

}
