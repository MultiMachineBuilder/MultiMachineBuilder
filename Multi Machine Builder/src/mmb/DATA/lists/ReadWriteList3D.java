/**
 * 
 */
package mmb.DATA.lists;

/**
 * @author oskar
 *
 */
public class ReadWriteList3D<T> extends ReadList3D<T> {

	/**
	 * @param x @param y @param z dimensions
	 */
	public ReadWriteList3D(int x, int y, int z) {
		super(x, y, z);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void set(int x, int y, int z, T value) {
		data[x][y][z] = value;
	}

	public ReadWriteList3D(T[][][] data) {
		super(data);
	}

}
