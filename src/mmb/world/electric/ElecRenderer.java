/**
 * 
 */
package mmb.world.electric;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.LookupOp;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.joml.Vector4f;
import org.joml.Vector4fc;

import mmb.data.contents.Textures;
import mmb.data.contents.Textures.Texture;
import mmb.gl.GLHelper;
import mmb.gl.RenderCtx;
import mmb.graphics.awt.ColorMapper;
import mmb.graphics.texture.BlockDrawer;
import mmb.world.block.BlockEntry;
import mmb.world.rotate.RotatedImageGroup;
import mmb.world.rotate.Side;

/**
 * @author oskar
 *
 */
public class ElecRenderer implements BlockDrawer {
	//Static stuff
	@Nonnull public static final ElecRenderer tiny =   gen("tiny");
	@Nonnull public static final ElecRenderer small =  gen("small");
	@Nonnull public static final ElecRenderer medium = gen("medium");
	@Nonnull public static final ElecRenderer large =  gen("large");
	@Nonnull public static final ElecRenderer huge  =  gen("huge");
	
	@Nonnull private static ElecRenderer gen(String title) {
		String rigPath = "machine/power/"+title+" connector.png";
		String iconPath = "machine/power/"+title+" wire.png";
		String basePath = "machine/power/"+title+" center.png";
		RotatedImageGroup rig = RotatedImageGroup.create(rigPath);
		ImageIcon icon = new ImageIcon(Textures.get(iconPath));
		Texture base = Textures.get1(basePath);
		return new ElecRenderer(rig, icon, base.img, base, Color.WHITE);
	}
	
	//Instance stuff
	@Nonnull public final RotatedImageGroup rig;
	@Nonnull public final ImageIcon icon;
	@Nonnull public final BufferedImage base;
	@Nonnull public final Texture baseTex;
	@Nonnull public final Vector4fc color;
	public ElecRenderer(RotatedImageGroup rig, ImageIcon icon, BufferedImage base, Texture baseTex, Vector4fc color) {
		this.rig = rig;
		this.icon = icon;
		this.base = base;
		this.baseTex = baseTex;
		this.color = new Vector4f(color);
	}
	public ElecRenderer(RotatedImageGroup rig, ImageIcon icon, BufferedImage base, Texture baseTex, Color color) {
		this.rig = rig;
		this.icon = icon;
		this.base = base;
		this.baseTex = baseTex;
		this.color = GLHelper.color2vec(color, new Vector4f());
	}
	@Override
	public void draw(@Nullable BlockEntry ent, int x, int y, Graphics g, int w, int h) {
		g.drawImage(base, x, y, w, h, null);
		if(ent instanceof BlockConduit) {
			int xx = ((BlockConduit) ent).posX();
			int yy = ((BlockConduit) ent).posY();
			Electricity u = ((BlockConduit) ent).owner().getAtSide(Side.U, xx, yy).getElectricalConnection(Side.D);
			if(u != null) rig.U.draw(ent, x, y, g, w, h);
			Electricity d = ((BlockConduit) ent).owner().getAtSide(Side.D, xx, yy).getElectricalConnection(Side.U);
			if(d != null) rig.D.draw(ent, x, y, g, w, h);
			Electricity l = ((BlockConduit) ent).owner().getAtSide(Side.L, xx, yy).getElectricalConnection(Side.R);
			if(l != null) rig.L.draw(ent, x, y, g, w, h);
			Electricity r = ((BlockConduit) ent).owner().getAtSide(Side.R, xx, yy).getElectricalConnection(Side.L);
			if(r != null) rig.R.draw(ent, x, y, g, w, h);
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
		BufferedImage base0 = renderer.baseTex.img;
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
		return new ElecRenderer(RotatedImageGroup.create(conn1), new ImageIcon(icon1), base1, renderer.baseTex, c);
	}
	
	private int lod = -1;
	@Override
	public int LOD() {
		if(lod < 0) {
			int rgb1 = mmb.graphics.texture.LODs.calcLOD(base);
			int r = (rgb1 & 0x00ff0000) >> 16;
			int g = (rgb1 & 0x0000ff00) >> 8;
			int b = (rgb1 & 0x000000ff);
			int rgb2 = rig.U.LOD();
			r += (rgb2 & 0x00ff0000) >> 16;
			g += (rgb2 & 0x0000ff00) >> 8;
			b += (rgb2 & 0x000000ff);
			r/=2; g/=2; b/=2;
			lod = r*65536 + g*256 + b;
		}
		return lod;
	}
}
