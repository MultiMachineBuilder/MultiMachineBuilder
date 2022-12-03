/**
 * 
 */
package mmb.world.blocks;

import java.awt.Graphics;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.world.rotate.RotatedImageGroup;
import mmb.world.rotate.Rotation;

/**
 * @author oskar
 *
 */
public abstract class SkeletalBlockEntityRotary extends BlockEntityData {
	@Override
	public void render(int x, int y, Graphics g, int ss) {
		getImage().get(side).draw(this, x, y, g, ss);
	}
	public abstract RotatedImageGroup getImage();
	@SuppressWarnings({ "null", "unused" })
	@Override
	public final void load(JsonNode data) {
		if(data == null) return;
		side = Rotation.valueOf(data.get("side").asText());
		if(side == null) side = Rotation.N;
		load1((ObjectNode) data);
	}
	@Nonnull private Rotation side = Rotation.N;
	@Override
	public void setRotation(Rotation rotation) {
		side = rotation;
	}
	@Override
	public Rotation getRotation() {
		return side;
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
	
	@Override
	public boolean isRotary() {
		return true;
	}
}
