/**
 * 
 */
package mmb.DATA.file;

/**
 * @author oskar
 *
 */
public class NamedByteArray {
	public String name;
	public byte[] data;
	/**
	 * 
	 */
	public NamedByteArray(String n) {
		name = n;
	}
	public NamedByteArray(String n, byte[] d) {
		name = n;
		data = d;
	}
}
