/**
 * 
 */
package mmb.files.data.files;

import java.io.InputStream;
import java.net.URL;

/**
 * @author oskar
 *
 */
public class OnlineFile implements GameFile<URL> {
	
	private URL url;
	private Runnable run = null;
	
	
	

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
	@Override
	public Throwable getErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}



	/* (non-Javadoc)
	 * @see mmb.files.data.files.GameFile#getPath()
	 */
	@Override
	public String getPath() {
		return url.toString();
	}



	/* (non-Javadoc)
	 * @see mmb.files.data.files.GameFile#getRawData()
	 */
	@Override
	public URL getRawData() {
		return url;
	}



	/* (non-Javadoc)
	 * @see mmb.files.data.files.GameFile#addLoadedHandler(java.lang.Runnable)
	 */
	@Override
	public void addLoadedHandler(Runnable data) {
		// TODO Auto-generated method stub
		
	}

}
