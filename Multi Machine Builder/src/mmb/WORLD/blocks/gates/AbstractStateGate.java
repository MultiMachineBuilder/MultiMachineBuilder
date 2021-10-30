/**
 * 
 */
package mmb.WORLD.blocks.gates;

import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.WORLD.rotate.RotatedImageGroup;

/**
 * @author oskar
 *
 */
public abstract class AbstractStateGate extends AbstractUnaryGateBase {

	@Override
	public RotatedImageGroup getImage() {
		if(state) 
			return getOnImage();
		return getOffImage();
	}

	protected boolean state;
	@Override
	protected final void save1(ObjectNode node) {
		node.put("state", state);
		save2(node);
	}

	@Override
	protected final void load1(ObjectNode node) {
		state = node.get("state").asBoolean();
		load2(node);
	}
	
	/**
	 * Additional function used to save additional data
	 * @param node node, to which data can be saved
	 */
	protected void save2(ObjectNode node) {
		//optional
	}
	/**
	 * Additional function used to save additional data
	 * @param node node, to which data can be loaded
	 */
	protected void load2(ObjectNode node) {
		//optional
	}
	
	protected abstract RotatedImageGroup getOnImage();
	protected abstract RotatedImageGroup getOffImage();
}
