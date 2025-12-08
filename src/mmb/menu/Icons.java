/**
 * 
 */
package mmb.menu;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import mmb.annotations.NN;
import mmb.engine.texture.Textures;

/**
 * A collection of text and image icons for GUIs
 * @author oskar
 */
public class Icons {
	private Icons() {}
	
	@NN public static final String refresh = "🗘";
	@NN public static final String drop = "🠋";
	@NN public static final String pick = "🠉";
	@NN public static final Icon insertitems = new ImageIcon(Textures.get("UIs/dropoff.png"));
	@NN public static final String unsel = "―";
	
	@NN public static final String grid = "⋮⋮⋮";
	@NN public static final String list = "☰";
	
	@NN public static final String encode = "⟵";
	@NN public static final String decode = "⟶";
	@NN public static final String erase = "🞪";
	@NN public static final String craft = "<<<";
	@NN public static final String activate = "!!!";
}
