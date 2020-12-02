/**
 * 
 */
package mmb.DATA.lists;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.joml.Vector3i;

/**
 * @author oskar
 *
 */
public class ReadList3D<T> {
	protected final T[][][] data;
	/**
	 * 
	 * @param x @param y @param z coordinates
	 * @param value new value
	 */
	public void set(int x, int y, int z, T value) {
		throw new ReadOnlyException("This list is read only");
	}
	public T get(int x, int y, int z) {
		return data[x][y][z];
	}
	public Vector3i dimensions() {
		return new Vector3i(data.length, data[0].length, data[0][0].length);
	}
	public ReadList3D(T[][][] data) {
		super();
		this.data = data;
	}
	
	/**
	 * 
	 * @param x @param y @param z dimensions
	 */
	@SuppressWarnings("unchecked")
	public ReadList3D(int x, int y, int z) {
		super();
		data = (T[][][]) new Object[x][][];
		for(int i = 0; i < x; i++) {
			data[i] = (T[][]) new Object[x][];
			for(int j = 0; j < y; j++) {
				data[i][j] = (T[]) new Object[x];
			}
		}
	}
	
	public ReadList3D(Vector3i size) {
		this(size.x, size.y, size.z);
	}
	public void set(Vector3i pos, T value) {
		set(pos.x, pos.y, pos.z, value);
	}
	public T get(Vector3i pos) {
		return get(pos.x, pos.y, pos.z);
	}
	public ReadList3D<T> copyR(){
		return new ReadList3D<T>(data);
	}
	public ReadWriteList3D<T> copyRW(){
		return new ReadWriteList3D<T>(data);
	}
	public void forEach(Consumer<T> cons) {
		for(T[][] t: data) {
			for(T[] u: t) {
				for(T v: u) {
					cons.accept(v);
				}
			}
		}
	}
	public void forEachIter(BiConsumer<Vector3i, T> cons) {
		int i = 0;
		for(T[][] t: data) {
			int j = 0;
			for(T[] u: t) {
				int k = 0;
				for(T v: u) {
					cons.accept(new Vector3i(i, j, k), v);
					k++;
				}
				j++;
			}
			i++;
		}
	}
}
