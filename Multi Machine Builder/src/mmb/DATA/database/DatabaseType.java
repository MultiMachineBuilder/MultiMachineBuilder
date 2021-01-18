/**
 * 
 */
package mmb.DATA.database;

/**
 * @author oskar
 * @param <T>
 * 
 */
public interface DatabaseType<T> {
	public void load(String s);
	public void run();
	public T get();
}
