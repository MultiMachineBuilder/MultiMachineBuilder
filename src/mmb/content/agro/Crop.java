/**
 * 
 */
package mmb.content.agro;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.NN;
import mmb.Nil;
import mmb.engine.block.BlockEntityData;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.chance.Chance;
import mmb.engine.worlds.MapProxy;

/**
 * @author oskar
 * A crop is growable block, which after some time it drops specified items
 */
public class Crop extends BlockEntityData {

	@Override
	public BlockType type() {
		return type;
	}

	private BlockType type;
	private Chance drop;
	private int progress;
	private int time;
	public Crop(BlockType type, int time, Chance drop) {
		super();
		this.type = type;
		this.time = time;
		this.drop = drop;
	}

	@Override
	public void load(@Nil JsonNode data) {
		if(data == null) return;
		JsonNode progNode = data.get("progress");
		if(progNode != null) progress = progNode.asInt();
	}

	@Override
	protected void save0(@NN ObjectNode node) {
		node.put("progress", progress);
	}

	/**
	 * @return time elapsed since last drop
	 */
	public int getProgress() {
		return progress;
	}

	/**
	 * @param progress new time to count as time elapsed since last drop
	 */
	public void setProgress(int progress) {
		this.progress = progress;
	}

	/**
	 * @return time between successive drops
	 */
	public int getTime() {
		return time;
	}

	/**
	 * @param time new time between successive drops
	 */
	public void setTime(int time) {
		this.time = time;
	}

	@Override
	public void onTick(MapProxy map) {
		progress++;
		if(progress >= time) {
			drop.drop(null, owner(), posX(), posY());
			progress -= time;
		}
	}

	
	@Override
	public BlockEntry blockCopy() {
		Crop copy = new Crop(type, time, drop);
		copy.progress = progress;
		return copy;
	}

}
