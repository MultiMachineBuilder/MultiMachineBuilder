/**
 * 
 */
package monniasza.collects.grid;

/**
 * @author oskar
 *
 */
public interface Grid<T> {
	public void set(int x, int y, T data);
	public T get(int x, int y);
	public int width();
	public int height();
}
