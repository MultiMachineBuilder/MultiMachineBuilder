/**
 * 
 */
package mmb.engine.block;

import java.awt.Graphics;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.annotations.NN;
import mmb.engine.rotate.RotatedImageGroup;
import mmb.engine.rotate.Rotation;

/**
 * @author oskar
 *
 */
public abstract class BlockEntityRotary extends BlockEntityData {
	//Rendering
	@Override
	public void render(int x, int y, Graphics g, int ss) {
		getImage().get(side).draw(this, x, y, g, ss);
	}
	/** @return this block's texture */
	public abstract RotatedImageGroup getImage();
	
	//Rotation
	@NN private Rotation side = Rotation.N;
	@Override
	public void setRotation(Rotation rotation) {
		side = rotation;
	}
	@Override
	public Rotation getRotation() {
		return side;
	}
	@Override
	public boolean isRotary() {
		return true;
	}
	
	//Serialization
	@SuppressWarnings({ "null", "unused" })
	@Override
	public final void load(JsonNode data) {
		if(data == null) return;
		side = Rotation.valueOf(data.get("side").asText());
		if(side == null) side = Rotation.N;
		load1((ObjectNode) data);
	}
	@Override
	protected final void save0(ObjectNode node) {
		node.put("side", side.toString());
		save1(node);
	}
	
	/**
	 * Additional function used to save additional data
	 * @param node node, to which data can be saved
	 */
	protected void save1(ObjectNode node) {
		//optional
	}
	/**
	 * Additional function used to save additional data
	 * @param node node, to which data can be loaded
	 */
	protected void load1(ObjectNode node) {
		//optional
	}
}
