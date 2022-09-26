/**
 * 
 */
package mmb.BEANS;

/**
 * @author oskar
 *
 */
public interface Resizable extends Positioned{
	public void setRangeX(int range);
	public int getRangeX();
	public void setRangeY(int range);
	public int getRangeY();
	public int maxSize();
}
