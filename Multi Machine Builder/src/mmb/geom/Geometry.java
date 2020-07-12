package mmb.geom;

import java.awt.Color;
import java.util.ArrayList;

import e3d.d3.Vector3;

public interface Geometry {
	public ArrayList<Tri> getThisPolys();
	public ArrayList<Tri> getAllPolys();
	public ArrayList<Vector3> getThisVertices();
	public ArrayList<Vector3> getAllVertices();
	public void rasterize(Viewport vp);
	
	public boolean isNamed();
	public boolean isNumbered();
	public boolean hasSubContexts();
	
	public ArrayList<Geometry> getSubContexts();
	public Geometry getSubContext(String name);
	public Geometry getSubContext(int id);
	
	public void createSubContext(String name, Geometry sub);
	public void createSubContext(Geometry sub);
	
	public void removeSubContext(Geometry sub);
	public void removeSubContext(String name);
	public void removeSubContext(int id);
	
	/*default public SimpleGeometry mergeAll() {
		@SuppressWarnings("unchecked")
		SimpleGeometry sg = new SimpleGeometry((ArrayList<Vector3>)getThisVertices().clone(), (ArrayList<Tri>)getThisPolys().clone());
		int offset  = 0;
		int sv = sg.verts.size();
		for(int i = 0; i < getSubContexts().size(); i++) {
			SimpleGeometry merged = getSubContexts().get(i).mergeAll();
			
			//Offset polys so the stay unbroken
			ArrayList<Tri> ips = new ArrayList<Tri>();

		}
	}*/
	
	default public void replace(Geometry geom) {
		replace(geom.getThisVertices(), geom.getThisPolys());
	}
	default public void replace(ArrayList<Vector3> verts, ArrayList<Tri> polys) {
		getThisPolys().clear();
		polys.forEach((Tri ip) -> {
			getThisPolys().add(ip);
		});
		
		getThisVertices().clear();
		verts.forEach((Vector3 v) -> {
			getThisVertices().add(v);
		});
	}
	
	default public void add(int a, int b, int c, Color x) {
		getAllPolys().add(new Tri(a,b,c,x));
	}
	
	default public void add(int a, int b, int c, int d, Color x) {
		getAllPolys().add(new Tri(a,b,c,x));
		getAllPolys().add(new Tri(c,d,a,x));
	}

}
