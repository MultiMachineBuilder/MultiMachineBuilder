package mmb.geom;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import javax.swing.JPanel;

import e3d.d3.Vector3;

public class Viewport extends JPanel {

	public BufferedImage img;
	public Vector3 pos = new Vector3(0, 0, 0);
	public Viewport(int x, int y) {
		img = new BufferedImage(x, y, BufferedImage.TYPE_3BYTE_BGR);
	}
	
	public void paintComponent(Graphics2D g2d) {
		g2d.drawImage(img, 0, 0, new ImageObserver() {
			@Override
			public boolean imageUpdate(Image arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
				// TODO Auto-generated method stub
				return true;
			}
			
		});
	}
}
