/**
 * 
 */
package mmb.files.data.files;

import java.io.InputStream;

/**
 * @author oskar
 *
 */
public interface GameFile<T> {
	String getPath();
	InputStream getInputStream();
	Throwable getErrorMessage();
	T getRawData();
	void addLoadedHandler(Runnable data);
	
	/**
	 * Starts loading process, without waiting for its ending. Calls a load handler given by addLoadedHandler()
	 */
    void start();
	
	/**
	 * Runs loading process without running a loaded handler;
	 */
    void run();
}
