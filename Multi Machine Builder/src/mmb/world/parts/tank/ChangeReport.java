/**
 * 
 */
package mmb.world.parts.tank;

/**
 * @author oskar
 *
 */
public class ChangeReport {
	double previous,current,increase,decrease;
	
	/**
	 * 
	 */
	public ChangeReport() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * 
	 * @param p previous quantity
	 * @param c current quantity
	 */
	public ChangeReport(double p, double c) {
		reset(p, c);
	}
	/**
	 * 
	 * @param p previous quantity
	 * @param c current quantity
	 */
	public void reset(double p, double c) {
		previous = p;
		current = c;
		increase = c - p;
		decrease = p - c;
	}
	/**
	 * @return the previous
	 */
	public double getPrevious() {
		return previous;
	}
	/**
	 * @param previous the previous to set
	 */
	public void setPrevious(double previous) {
		this.previous = previous;
		increase = current - previous;
		decrease = previous - current;
	}
	/**
	 * @return the current
	 */
	public double getCurrent() {
		return current;
	}
	/**
	 * @param current the current to set
	 */
	public void setCurrent(double current) {
		this.current = current;
		increase = current - previous;
		decrease = previous - current;
	}
	/**
	 * @return the increase
	 */
	public double getIncrease() {
		return increase;
	}
	/**
	 * @return the decrease
	 */
	public double getDecrease() {
		return decrease;
	}
	
}
