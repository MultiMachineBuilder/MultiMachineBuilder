/**
 * 
 */
package mmb.content.drugs;

import mmb.engine.chance.Chance;

/**
 * An abstraction over various intoxicating items (AlcoPod, beer, vodka, wine, ethanol etc...)
 * @author oskar
 */
public interface Intoxicating {
	/**
	 * @return strength of the intoxicating effect of the item.
	 * A value of 1 means that the item carries 1 standard drink.
	 */
	public double alcoholicity();
	/** @return the items to inject into inventory after drinking */
	public Chance postdrink();
	/** Called after alcohol consumption */
	public default void effects() {
		//unused
	}
}
