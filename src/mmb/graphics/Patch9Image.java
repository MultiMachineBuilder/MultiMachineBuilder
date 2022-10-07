/**
 * 
 */
package mmb.graphics;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

/**
 * @author oskar
 *
 */
public class Patch9Image {
	public final int pw, ph, w, h;
	public final BufferedImage ul, uc, ur, cl, cc, cr, dl, dc, dr;
	
	public Patch9Image(BufferedImage src) {
		w = src.getWidth(null);
		h = src.getHeight(null);
		pw = w/3;
		ph = h/3;
		ul = src.getSubimage(0, 0, pw, ph);
		uc = src.getSubimage(pw, 0, pw, ph);
		ur = src.getSubimage(pw*2, 0, pw, ph);
		cl = src.getSubimage(0, ph, pw, ph);
		cc = src.getSubimage(pw, ph, pw, ph);
		cr = src.getSubimage(pw*2, ph, pw, ph);
		dl = src.getSubimage(0, ph*2, pw, ph);
		dc = src.getSubimage(pw, ph*2, pw, ph);
		dr = src.getSubimage(pw*2, ph*2, pw, ph);
	}
	
	public void draw(Graphics g, ImageObserver io, int x, int y, int w, int h) {
		int X = (x+w)-1;
		int Y = (y+h)-1;
		int l = x+pw;//OK
		int r = X-(pw-1);
		int u = y+ph;//OK
		int d = Y-(ph-1);
		
		//Center
		g.drawImage(cc, l-1, u-1, r, d, 0, 0, pw-1, ph-1, io);
		
		//Edges
		g.drawImage(cl, x, u, l-1, d, 0, 0, pw-1, ph-1, io); //L
		g.drawImage(cr, r, u, X, d-1, 0, 0, pw, ph-1, io); //R
		g.drawImage(uc, l, y, r, u-1, 0, 0, pw-1, ph-1, io); //U
		g.drawImage(dc, l, d, r-1, Y, 0, 0, pw-1, ph, io); //D
		
		//Corners
		g.drawImage(ul, x, y, io);
		g.drawImage(ur, r-1, y, io);
		g.drawImage(dl, x, d-1, io);
		g.drawImage(dr, r-1, d-1, io);
		
		//TEST display edges
		//  g.drawImage(cr, 0, 0, X, Y, 0, 0, pw, ph, io);
	}
	public void draw(Graphics g, int x, int y, int w, int h) {
		draw(g, null, x, y, w, h);
	}
	public void draw(Graphics g, ImageObserver io, Point p, Dimension d) {
		draw(g, io, p.x, p.y, d.width, d.height);
	}
	public void draw(Graphics g, Point p, Dimension d) {
		draw(g, null, p.x, p.y, d.width, d.height);
	}
	public void draw(Graphics g, ImageObserver io, Rectangle r) {
		draw(g, io, r.x, r.y, r.width, r.height);
	}
	public void draw(Graphics g, Rectangle r) {
		draw(g, null, r.x, r.y, r.width, r.height);
	}
}
