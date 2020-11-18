package mmb.geom;

import java.util.*;

import e3d.d3.Vector3;

public class AdvGeometry implements Geometry {
	public ArrayList<Geometry> subs = new ArrayList<Geometry>();


	@Override
	public void rasterize(Viewport vp) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public ArrayList<Geometry> getSubContexts() {
		return subs;
	}

	@Override
	public boolean isNamed() {
		return false;
	}

	@Override
	public boolean isNumbered() {
		return true;
	}

	@Override
	public boolean hasSubContexts() {
		return true;
	}

	@Override
	public Geometry getSubContext(String name) {
		return null;
	}

	@Override
	public Geometry getSubContext(int id) {
		return subs.get(id);
	}

	@Override
	public void createSubContext(String name, Geometry sub) {
		createSubContext(sub);
	}

	@Override
	public void createSubContext(Geometry sub) {
		subs.add(sub);
	}

	@Override
	public void removeSubContext(Geometry sub) {
		subs.remove(sub);
	}

	@Override
	public void removeSubContext(String name) {
		throw new Error("No support for text");
	}

	@Override
	public void removeSubContext(int id) {
		subs.remove(id);
	}


	@Override
	public ArrayList<Tri> getThisPolys() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ArrayList<Tri> getAllPolys() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ArrayList<Vector3> getThisVertices() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ArrayList<Vector3> getAllVertices() {
		// TODO Auto-generated method stub
		return null;
	}

}
