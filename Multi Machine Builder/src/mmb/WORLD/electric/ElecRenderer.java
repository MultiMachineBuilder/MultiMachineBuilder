/**
 * 
 */
package mmb.WORLD.electric;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.LookupOp;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import mmb.DATA.contents.texture.Textures;
import mmb.GRAPHICS.awt.ColorMapper;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.rotate.RotatedImageGroup;
import mmb.WORLD.rotate.Side;
import mmb.WORLD.texture.BlockDrawer;

/**
 * @author oskar
 *
 */
public class ElecRenderer implements BlockDrawer {
	//Static stuff
	@Nonnull public static final ElecRenderer tiny= gen("tiny");
	@Nonnull public static final ElecRenderer small = gen("small");
	@Nonnull public static final ElecRenderer medium = gen("medium");
	@Nonnull public static final ElecRenderer large = gen("large");
	
	@Nonnull private static ElecRenderer gen(String title) {
		String rigPath = "machine/power/"+title+" connector.png";
		String iconPath = "machine/power/"+title+" wire.png";
		String basePath = "machine/power/"+title+" center.png";
		RotatedImageGroup rig = RotatedImageGroup.create(rigPath);
		ImageIcon icon = new ImageIcon(Textures.get(iconPath));
		BufferedImage base = Textures.get(basePath);
		return new ElecRenderer(rig, icon, base);
	}
	
	//Instance stuff
	@Nonnull public final RotatedImageGroup rig;
	@Nonnull public final ImageIcon icon;
	@Nonnull public final BufferedImage base;
	public ElecRenderer(RotatedImageGroup rig, ImageIcon icon, BufferedImage base) {
		this.rig = rig;
		this.icon = icon;
		this.base = base;
	}
	@Override
	public void draw(@Nullable BlockEntry ent, int x, int y, Graphics g, int side) {
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
	@Nonnull public static ElecRenderer repaint(Color c, ElecRenderer renderer) {
		BufferedImage conn0 = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
		Graphics g = conn0.createGraphics();
		renderer.rig.U.draw(null, 0, 0, g, 32);
		g.dispose();
		BufferedImage icon0 = (BufferedImage) renderer.icon.getImage();
		BufferedImage base0 = renderer.base;
		ColorMapper mapperC = ColorMapper.ofType(conn0.getType(), Color.RED, c);
		ColorMapper mapperI = ColorMapper.ofType(icon0.getType(), Color.RED, c);
		ColorMapper mapperB = ColorMapper.ofType(base0.getType(), Color.RED, c);
		LookupOp opC = new LookupOp(mapperC, null);
		LookupOp opI = new LookupOp(mapperI, null);
		LookupOp opB = new LookupOp(mapperB, null);
		BufferedImage conn1 = opC.createCompatibleDestImage(conn0, null);
		opC.filter(conn0, conn1);
		BufferedImage icon1 = opI.createCompatibleDestImage(icon0, null);
		opC.filter(icon0, icon1);
		BufferedImage base1 = opB.createCompatibleDestImage(base0, null);
		opC.filter(base0, base1);
		return new ElecRenderer(RotatedImageGroup.create(conn1), new ImageIcon(icon1), base1);
	}
}
