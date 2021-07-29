/**
 * 
 */
package mmb.testing;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingConstants;

import mmb.DATA.contents.texture.Textures;
import mmb.WORLD.RotatedImageGroup;

/**
 * @author oskar
 *
 */
public class TestRotatedImageGroup extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 265599626278698011L;

	public TestRotatedImageGroup() {
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(2);
		class RotatedImageDrawer extends JComponent{
			/**
			 * 
			 */
			private static final long serialVersionUID = 5380480106431817395L;
			private RotatedImageGroup rig;
			public RotatedImageDrawer(RotatedImageGroup rig) {
				this.rig = rig;
			}
			@Override
			public void paint(Graphics g) {
				rig.U.draw(null, 0, 0, g, 32);
				rig.D.draw(null, 0, 32, g, 32);
				rig.L.draw(null, 32, 0, g, 32);
				rig.R.draw(null, 32, 32, g, 32);
			}
		}
		RotatedImageGroup rig = RotatedImageGroup.create(Textures.get("logic/AND.png"));
		RotatedImageDrawer rid = new RotatedImageDrawer(rig);
		add(rid, SwingConstants.CENTER);
	}

}
