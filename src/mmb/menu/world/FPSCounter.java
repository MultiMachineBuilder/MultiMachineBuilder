/**
 * 
 */
package mmb.menu.world;

/**
 * @author oskar
 *
 */
public class FPSCounter {
	private int frameCounter;
	private int FPS;
	public void count() {
		frameCounter++;
	}
	public int reset() {
		FPS = frameCounter;
		frameCounter = 0;
		return FPS;
	}
	public int get() {
		return FPS;
	}
}
