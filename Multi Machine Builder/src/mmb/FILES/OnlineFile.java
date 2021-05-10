/**
 * 
 */
package mmb.FILES;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class OnlineFile implements AdvancedFile {
	private File download = null;
	private static final Debugger debug = new Debugger("FILES");
	public final URL url;
	/**
	 * 
	 */
	public OnlineFile(URL l) {
		url = l;
	}
	public OnlineFile(URI l) throws MalformedURLException {
		url = l.toURL();
	}
	public OnlineFile(String l) throws MalformedURLException {
		url = new URL(l);
	}

	/* (non-Javadoc)
	 * @see mmb.DATA.file.AdvancedFile#getInputStream()
	 */
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
	@SuppressWarnings("null")
	@Override
	public File asFile() throws IOException {
		if(download == null) {
			try(InputStream is = getInputStream()){
				download = StreamUtil.stream2file(is);
			}
		}
		return download;
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
	 * @see mmb.DATA.file.AdvancedFile#uri()
	 */
	@Override
	public URI uri() throws URISyntaxException{
		return url.toURI();
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
			debug.pstm(e, "Couldn't retrieve parent");
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
