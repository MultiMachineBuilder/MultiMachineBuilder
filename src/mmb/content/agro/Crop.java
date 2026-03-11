/**
 * 
 */
package mmb.content.agro;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.annotations.Nil;
import mmb.engine.Vector2iconst;
import mmb.engine.block.BlockEntityData;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.chance.Chance;
import mmb.engine.worlds.MapProxy;

/**
 * A crop is a growable block, which every specified time it drops specified items
 * @author oskar
 */
public class Crop extends BlockEntityData {
	//Constructors
	/**
	 * Creates a crop block
	 * @param type block type
	 */
	public Crop(CropType type) {
		super();
		this.type = type;
	}
	
	//Contents
	private final CropType type;
	
	private int progress;
	/** @return time elapsed since last drop */
	public int getProgress() {
		return progress;
	}
	/** @param progress new time to count as time elapsed since last drop */
	public void setProgress(int progress) {
		this.progress = progress;
	}
	
	//Block methods
	@Override
	public BlockType itemType() {
		return type;
	}
	@Override
	public void onTick(MapProxy map) {
		if(owner().drops.get(new Vector2iconst(posX(), posY())).isEmpty())
			progress++;
		if(progress >= type.time) {
			var dropper = map.getMap().createDropper(posX(), posY());
			type.drops.produceOutputs(dropper, 1);
			progress -= type.time;
		}
	}
	@Override
	public BlockEntry blockCopy() {
		Crop copy = new Crop(type);
		copy.progress = progress;
		return copy;
	}
	
	//Serialization
	public static Crop load(CropType blockType, int time, Chance cropDrop, @Nil JsonNode data) {
		Crop crop = new Crop(blockType);
		if(data == null) return crop;
		JsonNode progNode = data.get("progress");
		if(progNode != null) crop.progress = progNode.asInt();
		return crop;
	}
	@Override
	protected void save0(ObjectNode node) {
		node.put("progress", progress);
	}
}
