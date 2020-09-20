/**
 * 
 */
package mmb.data.files;

import java.io.InputStream;

/**
 * @author oskar
 *
 */
public interface GameFile<T> {
	public String getPath();
	public InputStream getInputStream();
	public Throwable getErrorMessage();
	public T getRawData();
	public void addLoadedHandler(Runnable data);
	
	/**
	 * Starts loading process, without waiting for its ending. Calls a load handler given by addLoadedHandler()
	 */
	public void start();
	
	/**
	 * Runs loading process without running a loaded handler;
	 */
	public void run();
}
