/**
 * 
 */
package mmb.world.quantities;

/**
 * @author oskar
 *
 */
public class UnitDisplay {
	public static voltage = new UnitDisplay(4, "㎴ ㎵ ㎶ ㎷ V㎸ ㎹"),
			      current = new UnitDisplay(4, "㎀ ㎁ ㎂ ㎃A ㎄"),
			      power   = new UnitDisplay(4, "㎺ ㎻ ㎼ ㎽ V㎾ ㎿");
			      
	
	
	
	
	/**
	 * 
	 */
	public UnitDisplay(byte offsetFromNorm, String series) {
		
	}
	
	
	
	static {
		
	}
}
