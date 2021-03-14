/**
 * 
 */
package mmb.WORLD.block;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.BEANS.Rotable;
import mmb.WORLD.RotatedImageGroup;
import mmb.WORLD.worlds.world.World.BlockMap;

/**
 * @author oskar
 *
 */
public abstract class SkeletalBlockEntityRotary extends SkeletalBlockEntityData implements Rotable {
	protected SkeletalBlockEntityRotary(int x, int y, BlockMap owner2) {
		super(x, y, owner2);
	}
	public abstract RotatedImageGroup getImage();
	@Override
	public final void load(JsonNode data) {
		side = Rotation.valueOf(data.get("side").asText());
		//if(side == null) side = Rotation.N;
		load1(data);
	}
	protected Rotation side = Rotation.N;
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
	protected void save1(ObjectNode node) {}
	protected void load1(JsonNode node) {}

}
