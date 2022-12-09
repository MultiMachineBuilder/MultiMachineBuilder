/**
 * 
 */
package mmbgame.modular.gui;

/**
 * Like {@link AutoCloseable}, but no exceptions allowed
 * @author oskar
 */
public interface SafeCloseable extends AutoCloseable{
	@Override
	void close();
}
