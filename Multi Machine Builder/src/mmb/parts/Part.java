package mmb.parts;

import java.util.ArrayList;
import java.util.List;

import e3d.d3.DDDLocation;
import e3d.d3.Rotation3;
import e3d.d3.Vector3;
import mmb.data.contents.TreeNode;
import mmb.debug.Debugger;
import mmb.world.vessels.Assembly;
import mmb.world.vessels.MechanicalLinkage;
import mmb.world.vessels.VesselFreeform;
import mmb.world.vessels.linkages.Jettisoner;

/**
 * An universal part, can contain predefined values
 * @author oskar
 *
 */
public class Part {

	public Vector3 assLocation;

	public TreeNode<Part> myNode;
	public ArrayList<MechanicalLinkage> mechanicalLinkages;
	public ArrayList<Rope> ropes;
	public Assembly containingAssembly;
	protected String partType;
	public String name;
	public PartData partData;
	protected Rotation3 orientation;
	public PartModel model;

	public Part connectedTo() {
		return myNode.getParent().value;
	}
	
	public List<Part> subparts(){
		List<Part> result = new ArrayList<Part>();
		TreeNode<Part>[] input = myNode.getChildren();
		for(int i = 0; i < input.length; i++) {
			result.add(input[i].value);
		}
		return result;
	}
	
	public Assembly getAssembly() {
		return Parts.correspondingAssemblies.get(this);
	}
	
	public VesselFreeform getVessel() {
		return Parts.correspondingVessels.get(this);
	}
	
	
	
	public Part() {
		// TODO Auto-generated constructor stub
	}
	
	public Part traceToRoot() {
		return myNode.root().value;
	}
	
	public String name() {
		if(name == null) {
			return partType + " " + hashCode();
		}else {
			return name;
		}
	}
	/**
	 * Used when part collides
	 * @param part
	 */
	public void collidePart(Part part) {
		if(part.containingAssembly.speed.diff(this.containingAssembly.speed) > 1) {
			crash(part);
		}
	}
	/**
	 * Used when part crashes
	 */
	public void crash(Part part) {
		
	}
	public void jettisonAll() {
		//Jettison all connections
		mechanicalLinkages.forEach((MechanicalLinkage l) -> {Jettisoner.jettison(l);});
		
		//Jettison all connected parts
		TreeNode<Part>[] raw = this.myNode.getChildren();
		for(int i = 0; i < raw.length; i++) {
			Jettisoner.jettison(raw[i].value);
		}
		Jettisoner.jettison(this); //Jettison this part
	}
	public void clearJettisonedLinkages() {
		//remove unnecessary, jettisoned linkages
		mechanicalLinkages.removeIf((MechanicalLinkage arg0) -> arg0.jettisoned);
	}

	/**
	 * @return the containingAssembly
	 */
	public Assembly getContainingAssembly() {
		return containingAssembly;
	}

	/**
	 * @param containingAssembly the containingAssembly to set
	 */
	public void setContainingAssembly(Assembly containingAssembly) {
		this.containingAssembly = containingAssembly;
	}

	/**
	 * @return the partType
	 */
	public String getPartType() {
		return partType;
	}

	/**
	 * @param partType the partType to set
	 */
	public void setPartType(String partType) {
		this.partType = partType;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the partData
	 */
	public PartData getPartData() {
		return partData;
	}

	/**
	 * @param partData the partData to set
	 */
	public void setPartData(PartData pd) {
		this.partData = pd;
		Debugger.printl("Changed part specs to" + pd.toString());
	}

	/**
	 * @return the orientation of this part only
	 */
	public Rotation3 getOrientation() {
		return orientation;
	}

	/**
	 * @param orientation turn THIS PART ONLY to given orientation
	 */
	public void setOrientation(Rotation3 orientation) {
		this.orientation = orientation;
	}
	
	/**
	 * 
	 * @param amt how much momentum to apply
	 * @param engine from where
	 */
	public void kick(double amt, DDDLocation engine) {
		
	}
	
}
