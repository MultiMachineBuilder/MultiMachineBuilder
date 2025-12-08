/**
 * 
 */
package mmb.engine.files;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import mmb.annotations.NN;
import mmb.engine.debug.Debugger;

/**
 * An online advanced file
 * @author oskar
 */
public class OnlineFile implements AdvancedFile {
	private static final Debugger debug = new Debugger("FILES");
	/** File URL */
	@NN public final URL url;
	
	/**
	 * Creates an online file
	 * @param l file URL
	 */
	public OnlineFile(URL l) {
		url = l;
	}
	/**
	 * Creates an online file
	 * @param l file URI
	 * @throws MalformedURLException If a protocol handler for the URL could not be found,
	 * or if some other error occurred while constructing the URL
	 */
	@SuppressWarnings("null")
	public OnlineFile(URI l) throws MalformedURLException {
		url = l.toURL();
	}
	/**
	 * Creates an online file
	 * @param l file URL
	 * @throws MalformedURLException if no protocol is specified, or an unknown protocol is found,
	 * or spec is null, or the parsed URL fails to comply with the specific syntax of the associated protocol.
	 */
	public OnlineFile(String l) throws MalformedURLException {
		url = new URL(l);
	}

	/* (non-Javadoc)
	 * @see mmb.DATA.file.AdvancedFile#getInputStream()
	 */
	@SuppressWarnings("null")
	@Override
	public InputStream getInputStream() throws IOException {
		return url.openStream();
	}

	/* (non-Javadoc)
	 * @see mmb.DATA.file.AdvancedFile#getOutputStream()
	 */
	@Override
	public OutputStream getOutputStream() throws IOException {
		throw new IOException("Online files are read only");
	}

	/* (non-Javadoc)
	 * @see mmb.DATA.file.AdvancedFile#asFile()
	 */
	@Override
	public File asFile() throws IOException {
		throw new IOException("Not local");
	}

	/* (non-Javadoc)
	 * @see mmb.DATA.file.AdvancedFile#name()
	 */
	@Override
	public String name() {
		return url.toString();
	}

	/* (non-Javadoc)
	 * @see mmb.DATA.file.AdvancedFile#url()
	 */
	@Override
	public URL url(){
		return url;
	}
	/* (non-Javadoc)
	 * @see mmb.DATA.file.AdvancedFile#parent()
	 */
	@SuppressWarnings("null")
	@Override
	public AdvancedFile parent() {
		URI uri;
		try {
			uri = url.toURI();
			return new OnlineFile(uri.getPath().endsWith("/") ? uri.resolve("..") : uri.resolve("."));
		} catch (URISyntaxException|MalformedURLException e) {
			debug.stacktraceError(e, "Couldn't retrieve parent");
			return null;
		}
	}
	/* (non-Javadoc)
	 * @see mmb.DATA.file.AdvancedFile#children()
	 */
	@Override
	public AdvancedFile[] children() throws Exception {
		throw new UnsupportedOperationException("Unable to get children of online files");
	}
	/* (non-Javadoc)
	 * @see mmb.DATA.file.AdvancedFile#exists()
	 */
	@Override
	public boolean exists() {
		return true;
	}
	@Override
	public void create() {
		throw new RuntimeException(new IOException("Unable to create files remotely using OnlineFile"));
	}
	@Override
	public boolean isDirectory() {
		return false;
	}

}
