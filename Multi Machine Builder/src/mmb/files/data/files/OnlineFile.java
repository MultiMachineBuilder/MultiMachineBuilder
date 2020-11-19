/**
 * 
 */
package mmb.files.data.files;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import mmb.DATA.file.AdvancedFile;

/**
 * @author oskar
 *
 */
public class OnlineFile implements AdvancedFile /*GameFile<URL>*/ {
	
	private final URL url;
	private final Runnable run = null;
	
	
	

	public OnlineFile(URL url) {
		super();
		this.url = url;
	}

	

	/* (non-Javadoc)
	 * @see mmb.files.data.files.GameFile#getInputStream()
	 */
	@Override
	public InputStream getInputStream() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see mmb.files.data.files.GameFile#getErrorMessage()
	 */
	public Throwable getErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}



	/* (non-Javadoc)
	 * @see mmb.files.data.files.GameFile#getPath()
	 */
	public String getPath() {
		return url.toString();
	}



	/* (non-Javadoc)
	 * @see mmb.files.data.files.GameFile#getRawData()
	 */
	public URL getRawData() {
		return url;
	}



	/* (non-Javadoc)
	 * @see mmb.files.data.files.GameFile#addLoadedHandler(java.lang.Runnable)
	 */
	public void addLoadedHandler(Runnable data) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public OutputStream getOutputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public File asFile() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String name() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public AdvancedFile parent() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public AdvancedFile[] children() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public void create() throws IOException {
		// TODO Auto-generated method stub
		
	}

}
