/**
 * 
 */
package mmb.menu;

import javax.annotation.Nonnull;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import mmb.texture.Textures;

/**
 * @author oskar
 * A collection of text and image icons for GUIs
 */
public class Icons {
	private Icons() {}
	
	@Nonnull public static final String refresh = "🗘";
	@Nonnull public static final String drop = "🠋";
	@Nonnull public static final String pick = "🠉";
	@Nonnull public static final Icon insertitems = new ImageIcon(Textures.get("UIs/dropoff.png"));
	@Nonnull public static final String unsel = "―";
	
	@Nonnull public static final String grid = "⋮⋮⋮";
	@Nonnull public static final String list = "☰";
	
	@Nonnull public static final String encode = "⟵";
	@Nonnull public static final String decode = "⟶";
	@Nonnull public static final String erase = "🞪";
	@Nonnull public static final String craft = "<<<";
	@Nonnull public static final String activate = "!!!";
}
