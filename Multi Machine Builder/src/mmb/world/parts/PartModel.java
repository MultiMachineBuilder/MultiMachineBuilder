/**
 * 
 */
package mmb.world.parts;

/**
 * @author oskar
 *
 */
public class PartModel {
	public String id;
	/**
	 * 
	 */
	public PartModel() {
		// TODO Auto-generated constructor stub
	}
	public PartSpec specs() {
		return Parts.specs.get(id);
	}
}
