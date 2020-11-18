/**
 * 
 */
package mmb.world2.rail;

import e3d.d3.DDDLocation;

/**
 * @author oskar
 *
 */
public interface RailSegment {
	public DDDLocation locA();
	public DDDLocation locB();
	public RailSegment endA();
	public RailSegment endB();
	public DDDLocation pos(double distance);
	public double length();
	
	//Connection order
	default public boolean AconnectedA() {
		return locA() == endA().locA();
	}
	default public boolean AconnectedB() {
		return locA() == endA().locB();
	}
	default public boolean BconnectedA() {
		return locB() == endB().locA();
	}
	default public boolean BconnectedB() {
		return locB() == endB().locB();
	}
}
