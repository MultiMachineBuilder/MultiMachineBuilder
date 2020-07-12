/**
 * 
 */
package mmb.scaling;

/**
 * @author oskar
 *
 */
public interface Scaleable1 {
	/*1 dimension
	 * Uses:
	 * Engines
	 */
	public void setA(double size);
	public double getA();
	default public String nameA() {
		return "Scale A";
	}
}
