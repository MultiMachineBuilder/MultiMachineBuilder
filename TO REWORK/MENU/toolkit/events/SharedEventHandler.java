/**
 * 
 */
package mmb.MENU.toolkit.events;

/**
 * @author oskar
 *	Contains events shared by all components
 */
@Deprecated
public interface SharedEventHandler {
	default public void keyPress(int key){};
	default public void keyRelease(int key){};
	default public void scroll(int scroll){};
}
