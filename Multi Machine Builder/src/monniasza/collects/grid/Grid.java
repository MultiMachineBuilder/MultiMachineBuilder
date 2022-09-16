/**
 * 
 */
package monniasza.collects.grid;

import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.annotation.Nonnull;

/**
 * @author oskar
 *
 */
public interface Grid<T> extends Iterable<T>{
	public void set(int x, int y, T data);
	public T get(int x, int y);
	public int width();
	public int height();
	default public int size() {
		return width() * height();
	}
	@Nonnull default public FixedGrid<T> copy(int x, int y, int w, int h){
		FixedGrid<T> grid = new FixedGrid<>(w, h);
		for(int i = 0, X = x; i < w; i++, X++) {
			for(int j = 0, Y = y; j < h; j++, Y++) {
				grid.set(i, j, get(X, Y));
			}
		}
		return grid;
	}
	@Nonnull default public FixedGrid<T> trim(){
		int left = width();
		int right = -1;
		int down = -1;
		int up = height();
		for(int x = 0; x < width(); x++) {
			for(int y = 0; y < height(); y++) {
				T item = get(x, y);
				if(item != null) {
					//Set bound
					if(x < left) left = x;
					if(x > right) right = x;
					if(y < up) up = y;
					if(y > down) down = y;
				}
			}
		}
		if(left > right || up > down) return new FixedGrid<>(0);
		return copy(left, up, 1+right-left, 1+down-up);
	}
	
	@Override
	default public Iterator<T> iterator(){
		return new Iterator<T>() {
			int x = 0, y = 0;
			@Override
			public boolean hasNext() {
				if(width() == 0) return false;
				if(height() == 0) return false;
				if(y >= height()) return false;
				if(y == (height()-1) && x >= width()) return false;
				return true;
			}

			@Override
			public T next() {
				if(!hasNext()) throw new NoSuchElementException("No more elements");
				T value = get(x, y); //Retrieve
				//Iterate
				x++;
				if(x >= width()) {
					y++;
					x = 0;
				}
				return value;
			}
		};
	}
	
	/**
	 * @param <U>
	 * @param x1 source start column
	 * @param y1 source start row
	 * @param src source
	 * @param x2 target start column
	 * @param y2 target start row
	 * @param tgt target
	 * @param w width
	 * @param h height
	 */
	public static <U> void copy(int x1, int y1, Grid<? extends U> src, int x2, int y2, Grid<U> tgt, int w, int h) {
		int X1 = x1;
		int X2 = x2;
		for(int i = 0; i < w; i++) {
			int Y2 = y2;
			int Y1 = y1;
			for(int j = 0; j < h; j++) {
				tgt.set(X2, Y2, src.get(X1, Y1));
				Y2++;
				Y1++;
			}
			X1++;
			X2++;
		}
	}

	public default Grid<T> copy(){
		return copy(0, 0, width(), height());
	}
	
	public default void fill(int x, int y, int w, int h, T data) {
		for(int X = x, i = 0; i < w; X++, i++) {
			for(int Y = y, j = 0; j < h; Y++, j++) {
				set(X, Y, data);
			}
		}
	}
	public default void fill(T data) {
		fill(0, 0, width(), height(), data);
	}
}
