/**
 * 
 */
package monniasza.collects.grid;

/**
 * @author oskar
 *
 */
public class FixedGrid<T> implements Grid<T> {
	private final Object[][] arr;
	private final int h;
	
	public FixedGrid(int w, int h) {
		arr = new Object[w][h];
		this.h = h;
	}
	
	@Override
	public void set(int x, int y, T data) {
		arr[x][y] = data;
	}

	@SuppressWarnings({"unchecked", "unchested"})
	@Override
	public T get(int x, int y) {
		return (T) arr[x][y];
	}

	@Override
	public int width() {
		return arr.length;
	}

	@Override
	public int height() {
		return h;
	}

}
