/**
 * 
 */
package mmb.debug;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * @author oskar
 *
 */
public class StringBuilderWriter extends Writer {
	private StringBuilder sb;
	public StringBuilderWriter(StringBuilder sb) {
		super();
		this.sb = sb;
	}

	@Override
	public void close() throws IOException {
		sb = null;
	}

	@Override
	public void flush(){}

	@Override
	public void write(char[] arg0, int arg1, int arg2){
		sb.append(arg0, arg1, arg2);
	}
}
