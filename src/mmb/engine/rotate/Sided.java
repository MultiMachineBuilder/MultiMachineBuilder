/**
 * 
 */
package mmb.engine.rotate;

/**
 * @author oskar
 * @param <T> type of data
 *
 */
public class Sided<T> {
	public T u, d, l, r, ul, ur, dl, dr;
	/**
	 * Retrieves data at given side
	 * @param s side to retrieve
	 * @return value at given side
	 * @throws IllegalArgumentException if given side is invalid
	 */
	public T get(Side s) {
		switch(s) {
		case D:
			return d;
		case DL:
			return dl;
		case DR:
			return dr;
		case L:
			return l;
		case R:
			return r;
		case U:
			return u;
		case UL:
			return ul;
		case UR:
			return ur;
		default:
			throw new IllegalArgumentException("Unknown side: "+s);
		}
	}
	/**
	 * Sets data at given side to given value
	 * @param s side to associate
	 * @param data new value
	 * @throws IllegalArgumentException if given side is invalid
	 */
	public void set(Side s, T data) {
		switch(s) {
		case D:
			d = data;
			break;
		case DL:
			dl = data;
			break;
		case DR:
			dr = data;
			break;
		case L:
			l = data;
			break;
		case R:
			r = data;
			break;
		case U:
			u = data;
			break;
		case UL:
			ul = data;
			break;
		case UR:
			ur = data;
			break;
		default:
			throw new IllegalArgumentException("Unknown side: "+s);
		}
	}
	/**
	 * Removes all data from this container
	 */
	public void reset() {
		u = null;
		d = null;
		l = null;
		r = null;
		ul = null;
		ur = null;
		dl = null;
		dr = null;
	}
}
