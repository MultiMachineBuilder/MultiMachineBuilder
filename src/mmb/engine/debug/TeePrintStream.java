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
 * Combines two print streams into one
 * @author oskar
 */
public class TeePrintStream extends PrintStream {
	private final PrintStream second;
	
	/**
	 * Creates a print stream to an output stream
	 * @param arg0 output stream
	 * @param in second print stream
	 */
	public TeePrintStream(OutputStream arg0, PrintStream in) {
		super(arg0);
		second = in;
	}
	/**
	 * Creates a print stream to a file
	 * @param arg0 file name
	 * @param in second print stream
	 * @throws FileNotFoundException when failed to create a file
	 */
	public TeePrintStream(String arg0, PrintStream in) throws FileNotFoundException {
		super(arg0);
		second = in;
	}
	/**
	 * Creates a print stream to a file
	 * @param arg0 file
	 * @param in second print stream
	 * @throws FileNotFoundException when failed to create a file
	 */
	public TeePrintStream(File arg0, PrintStream in) throws FileNotFoundException {
		super(arg0);
		second = in;
	}
	/**
	 * Creates a print stream with auto-flushin
	 * @param arg0 output stream
	 * @param arg1 should the stream auto flush?
	 * @param in second print stream
	 */
	public TeePrintStream(OutputStream arg0, boolean arg1, PrintStream in) {
		super(arg0, arg1);
		second = in;
	}
	/**
	 * Creates a print stream with a specified encoding to a file
	 * @param fileName file name
	 * @param csn charset
	 * @param in second print stream
	 * @throws FileNotFoundException when failed to create a file
	 * @throws UnsupportedEncodingException when given encoding is not supported
	 */
	public TeePrintStream(String fileName, String csn, PrintStream in) throws FileNotFoundException, UnsupportedEncodingException {
		super(fileName, csn);
		second = in;
	}
	/**
	 * Creates a print stream with a specified encoding to a file
	 * @param file file
	 * @param csn charset
	 * @param in second print stream
	 * @throws FileNotFoundException when failed to create a file
	 * @throws UnsupportedEncodingException when given encoding is not supported
	 */
	public TeePrintStream(File file, String csn, PrintStream in) throws FileNotFoundException, UnsupportedEncodingException {
		super(file, csn);
		second = in;
	}
	/**
	 * Creates a print stream with auto-flushing and a specified encoding
	 * @param arg0 output stream
	 * @param arg1 should the stream auto flush?
	 * @param arg2 charset
	 * @param in second print stream
	 * @throws UnsupportedEncodingException when given encoding is not supported
	 */
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
