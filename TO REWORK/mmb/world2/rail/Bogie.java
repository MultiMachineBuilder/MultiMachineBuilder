/**
 * 
 */
package mmb.world2.rail;

/**
 * @author oskar
 *
 */
public class Bogie {
	public double distance;
	public double speed;
	RailSegment current;
	/**
	 * 
	 */
	public Bogie() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Advance the bogie
	 */
	public void step(double time) {
		if(current == null) {
			
		}else{
			double step = time * speed;
			distance += step;
			if(distance < 0) {
				current = current.endA();
				distance += current.length();
				if(current.AconnectedA()) {
					//Reverse
					speed = -speed;
					distance = current.length() - distance;
				}else if(current.AconnectedB()) {
					
				}else {
					//No further railway
					current = null;
				}
			}else if(distance > current.length()) {
				distance -= current.length();;
				current = current.endB();
				if(current.BconnectedA()) {
					
				}else if(current.BconnectedB()) {
					//Reverse
					speed = -speed;
					distance = current.length() - distance;
				}else {
					//No further railway
					current = null;
				}
			}
		}
		
	}

}
