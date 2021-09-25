/**
 * 
 */
package mmb.WORLD.electric;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.annotation.Nonnull;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import mmb.DATA.contents.texture.Textures;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.rotate.RotatedImageGroup;
import mmb.WORLD.rotate.Side;
import mmb.WORLD.texture.BlockDrawer;

/**
 * @author oskar
 *
 */
public class ElecRenderer implements BlockDrawer {
	private final RotatedImageGroup rig;
	private final ImageIcon icon;
	private final BufferedImage base;
	public ElecRenderer(RotatedImageGroup rig, ImageIcon icon, BufferedImage base) {
		this.rig = rig;
		this.icon = icon;
		this.base = base;
	}
	
	private static final RotatedImageGroup rig0 = RotatedImageGroup.create("machine/power/thick connector.png");
	private static final ImageIcon icon0 = new ImageIcon(Textures.get("machine/power/thick wire.png"));
	private static final BufferedImage base0 = Textures.get("machine/power/thick center.png");
	@Nonnull public static final ElecRenderer render = new ElecRenderer(rig0, icon0, base0);
	
	private static final RotatedImageGroup rig1 = RotatedImageGroup.create("machine/power/vthick connector.png");
	private static final ImageIcon icon1 = new ImageIcon(Textures.get("machine/power/vthick wire.png"));
	private static final BufferedImage base1 = Textures.get("machine/power/vthick center.png");
	@Nonnull public static final ElecRenderer renderthick = new ElecRenderer(rig1, icon1, base1);
	
	@Override
	public void draw(BlockEntry ent, int x, int y, Graphics g, int side) {
		g.drawImage(base, x, y, side, side, null);
		if(ent instanceof Conduit) {
			int xx = ((Conduit) ent).posX();
			int yy = ((Conduit) ent).posY();
			Electricity u = ((Conduit) ent).owner().getAtSide(Side.U, xx, yy).getElectricalConnection(Side.D);
			if(u != null) rig.U.draw(ent, x, y, g, side);
			Electricity d = ((Conduit) ent).owner().getAtSide(Side.D, xx, yy).getElectricalConnection(Side.U);
			if(d != null) rig.D.draw(ent, x, y, g, side);
			Electricity l = ((Conduit) ent).owner().getAtSide(Side.L, xx, yy).getElectricalConnection(Side.R);
			if(l != null) rig.L.draw(ent, x, y, g, side);
			Electricity r = ((Conduit) ent).owner().getAtSide(Side.R, xx, yy).getElectricalConnection(Side.L);
			if(r != null) rig.R.draw(ent, x, y, g, side);
		}
	}

	@Override
	public Icon toIcon() {
		return icon;
	}

}
