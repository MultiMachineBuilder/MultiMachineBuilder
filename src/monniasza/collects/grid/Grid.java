/**
 * 
 */
package monniasza.collects.grid;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

import javax.annotation.Nonnull;

/**
 * A grid contains data arranged in a regular grid.
 * @author oskar
 * @param <T> type of data
 */
public interface Grid<T> extends Iterable<T>{
	//Basic operations
	/**
	 * Sets a value at a specified location in a grid
	 * @param x X coordinate of a value
	 * @param y Y coordinate of a value
	 * @param data new value
	 * @throws UnsupportedOperationException if the grid does not support modification
	 */
	public void set(int x, int y, T data);
	/**
	 * Gets a value at a specified location in a grid
	 * @param x X coordinate of a value
	 * @param y Y coordinate of a value
	 * @return value at a given location
	 */
	public T get(int x, int y);
	public int width();
	public int height();
	
	//Functional style operations
	/** @return number of items in a grid */
	public default int size() {
		return width() * height();
	}
	/**
	 * Copies a portion of this grid
	 * @param x initial X coordinate
	 * @param y initial Y coordinate
	 * @param w width
	 * @param h height
	 * @return a new grid with values copied
	 */
	@Nonnull public default FixedGrid<T> copy(int x, int y, int w, int h){
		FixedGrid<T> grid = new FixedGrid<>(w, h);
		for(int i = 0, X = x; i < w; i++, X++) {
			for(int j = 0, Y = y; j < h; j++, Y++) {
				grid.set(i, j, get(X, Y));
			}
		}
		return grid;
	}
	/**
	 * Trims a grid so on no side are there null values
	 * @return a trimmed grid
	 */
	@Nonnull public default FixedGrid<T> trim(){
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
	public default Iterator<T> iterator(){
		return new Iterator<>() {
			int x = 0;
			int y = 0;
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
	 * Copies this grid into a new grid
	 * @return a copy of this grid
	 */
	public default FixedGrid<T> copy(){
		return copy(0, 0, width(), height());
	}
	/**
	 * Transforms each value in this grid with a specified function.
	 * @param <U> type of output values
	 * @param fn function to apply to values
	 * @return a new grid with a result
	 */
	public default <U> FixedGrid<U> map(Function<? super T, ? extends U> fn){
		FixedGrid<U> result = new FixedGrid<>(width(), height());
		for(int i= 0; i < width(); i++) {
			for(int j = 0; j < height(); j++) {
				result.set(i, j, fn.apply(get(i, j)));
			}
		}
		return result;
	}
	
	//Mutative operations
	/**
	 * Replaces a portion of values in this grid
	 * @param x initial X coordinate
	 * @param y initial Y coordinate
	 * @param w width
	 * @param h height
	 * @param data value to fill with
	 */
	public default void fill(int x, int y, int w, int h, T data) {
		for(int X = x, i = 0; i < w; X++, i++) {
			for(int Y = y, j = 0; j < h; Y++, j++) {
				set(X, Y, data);
			}
		}
	}
	/**
	 * Replaces all values in this grid
	 * @param data value to fill with
	 */
	public default void fill(T data) {
		fill(0, 0, width(), height(), data);
	}
	
	//Static operations
	/**
	 * Copies a portion of a grid to another grid
	 * @param <U> type of data
	 * @param x1 source start column
	 * @param y1 source start row
	 * @param src source
	 * @param x2 target start column
	 * @param y2 target start row
	 * @param tgt target
	 * @param w width width of data to copy
	 * @param h height height of data to copy
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
}
