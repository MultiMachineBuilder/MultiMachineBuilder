/**
 * 
 */
package mmb.geom;

import java.awt.*;
import java.util.*;
import java.util.List;
import e3d.*;
import e3d.d3.Transform3;
import e3d.d3.Vector3;
/**
 * @author oskar
 *
 */

public class SimpleGeometry implements Geometry{
	
	public SimpleGeometry(ArrayList<Vector3> verts, ArrayList<Tri> tris) {
		super();
		this.verts = verts;
		this.tris = tris;
	}
	public SimpleGeometry() {}
	static class Paair {
		public double depht;
		public Tri poly;
		public Paair(double d, Tri p) {
			depht = d;
			poly = p;
		}
	}
	public ArrayList<Vector3> verts = new ArrayList<Vector3>();
	public ArrayList<Tri> tris = new ArrayList<Tri>();
	
	public void rasterize(Viewport vp, Transform3 tf) {
		Vector3[] vs = new Vector3[tris.size()];
		int w = vp.img.getWidth(), h = vp.img.getHeight();
		for(int i = 0; i < verts.size(); i++) {
			vs[i] = tf.transform(verts.get(i));
		}
		tris.forEach((Tri t) -> {
			
			double[] zBuffer = new double[vp.img.getWidth() * vp.img.getHeight()];
            // initialize array with extremely far away depths
            for (int q = 0; q < zBuffer.length; q++) {
                zBuffer[q] = Double.NEGATIVE_INFINITY;
            }
			Vector3 v1 = tf.transform(vs[t.a]);
            v1.x += w / 2;
            v1.y += h / 2;
            Vector3 v2 = tf.transform(vs[t.b]);
            v2.x += w / 2;
            v2.y += h / 2;
            Vector3 v3 = tf.transform(vs[t.c]);
            v3.x += w / 2;
            v3.y += h / 2;

            Vector3 ab = new Vector3(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
            Vector3 ac = new Vector3(v3.x - v1.x, v3.y - v1.y, v3.z - v1.z);
            Vector3 norm = new Vector3(
                 ab.y * ac.z - ab.z * ac.y,
                 ab.z * ac.x - ab.x * ac.z,
                 ab.x * ac.y - ab.y * ac.x
            );
            double normalLength = Math.sqrt(norm.x * norm.x + norm.y * norm.y + norm.z * norm.z);
            norm.x /= normalLength;
            norm.y /= normalLength;
            norm.z /= normalLength;

            int minX = (int) Math.max(0, Math.ceil(Math.min(v1.x, Math.min(v2.x, v3.x))));
            int maxX = (int) Math.min(w - 1, Math.floor(Math.max(v1.x, Math.max(v2.x, v3.x))));
            int minY = (int) Math.max(0, Math.ceil(Math.min(v1.y, Math.min(v2.y, v3.y))));
            int maxY = (int) Math.min(h - 1, Math.floor(Math.max(v1.y, Math.max(v2.y, v3.y))));

            double triangleArea = (v1.y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - v1.x);

            for (int y = minY; y <= maxY; y++) {
                for (int x = minX; x <= maxX; x++) {
                    double b1 = ((y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - x)) / triangleArea;
                    double b2 = ((y - v1.y) * (v3.x - v1.x) + (v3.y - v1.y) * (v1.x - x)) / triangleArea;
                    double b3 = ((y - v2.y) * (v1.x - v2.x) + (v1.y - v2.y) * (v2.x - x)) / triangleArea;
                    if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) {
                        double depth = b1 * v1.z + b2 * v2.z + b3 * v3.z;
                        int zIndex = y * vp.img.getWidth() + x;
                        if (zBuffer[zIndex] < depth) {
                            vp.img.setRGB(x, y, t.color.getRGB());
                            zBuffer[zIndex] = depth;
                        }
                    }
                }
            }}
		);
	}
	void rasterize(Graphics2D g, Tri p, int x, int y) {
		
	}
	@Override
	public ArrayList<Tri> getThisPolys() {
		return tris;
	}
	@Override
	public ArrayList<Vector3> getThisVertices() {
		return verts;
	}
	@Override
	public ArrayList<Tri> getAllPolys() {
		return tris;
	}
	@Override
	public ArrayList<Vector3> getAllVertices() {
		return verts;
	}

	@Override
	public ArrayList<Geometry> getSubContexts() {
		return new ArrayList<Geometry>();
	}
	@Override
	public boolean isNamed() {
		return false;
	}
	@Override
	public boolean isNumbered() {
		return false;
	}
	@Override
	public boolean hasSubContexts() {
		return false;
	}
	@Override
	public Geometry getSubContext(String name) {
		throw new Error("No  support for subcontexts");
	}
	@Override
	public Geometry getSubContext(int id) {
		throw new Error("No  support for subcontexts");
	}
	@Override
	public void createSubContext(String name, Geometry sub) {
		throw new Error("No  support for subcontexts");
	}
	@Override
	public void createSubContext(Geometry sub) {
		throw new Error("No  support for subcontexts");
	}
	@Override
	public void removeSubContext(Geometry sub) {
		throw new Error("No  support for subcontexts");
	}
	@Override
	public void removeSubContext(String name) {
		throw new Error("No  support for subcontexts");
	}
	@Override
	public void removeSubContext(int id) {
		throw new Error("No  support for subcontexts");
	}
	@Override
	public void rasterize(Viewport vp) {
		rasterize(vp, new Transform3(1,0,0, 0,1,0, 0,0,1));
	}
}
