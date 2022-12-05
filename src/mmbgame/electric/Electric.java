/**
 * 
 */
package mmbgame.electric;

import javax.annotation.Nonnull;

/**
 * @author oskar
 * Allows blocks and items to provide the main electrical connection
 */
public interface Electric {
	/** @return the main electrical connection */
	@Nonnull public Electricity getElectricity();
}
