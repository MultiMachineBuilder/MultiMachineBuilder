/**
 * 
 */
package mmb.content.wireworld.actuator;

import java.awt.Point;

import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.Nil;
import mmb.cgui.BlockActivateListener;
import mmb.content.ContentsBlocks;
import mmb.content.wireworld.AbstractActuatorBase;
import mmb.data.variables.ListenableValue;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.debug.Debugger;
import mmb.engine.item.Items;
import mmb.engine.rotate.RotatedImageGroup;
import mmb.engine.texture.Textures;
import mmb.engine.worlds.MapProxy;
import mmb.engine.worlds.world.World;
import mmb.menu.world.window.WorldWindow;

/**
 * Places unlimited blocks when powered.
 * It may be configured by clicking on it by using a dialog box
 * @author oskar
 * @see SelectBlock
 * @see BlockSetting
 */
public class ActuatorPlaceBlock extends AbstractActuatorBase implements BlockActivateListener , BlockSetting{
	private static final Debugger debug = new Debugger("ACTUATOR-PLACER");
	private static final RotatedImageGroup texture = RotatedImageGroup.create(Textures.get("machine/placer.png"));
	@Override
	protected void save1(ObjectNode node) {
		BlockType block = blockSetting();
		if(block == null) node.put("place", "mmb.grass");
		else node.put("place", block.id());
	}
	@Override
	protected void load1(ObjectNode node) {
		setBlockSetting(Items.getExpectType(node.get("place").asText(null), BlockType.class));
	}

	@Override
	public BlockType type() {
		return ContentsBlocks.PLACER;
	}

	@Override
	public RotatedImageGroup getImage() {
		return texture;
	}

	@Override
	protected void run(Point p, BlockEntry ent, MapProxy proxy) {
		BlockType block0 = blockSetting();
		if(block0 == null) return;
		try {
			proxy.place(block0, p);
		}catch(Exception e) {
			debug.stacktraceError(e, "Failed to place a block");
		}
	}
	@Override
	public void click(int blockX, int blockY, World map, @Nil WorldWindow window, double partX, double partY) {
		if(window == null) return;
		window.openAndShowWindow(new SelectBlock(this, window), "["+posX()+","+posY()+"]");
	}
	@Override
	public BlockEntry blockCopy() {
		ActuatorPlaceBlock result = new ActuatorPlaceBlock();
		result.setChirotation(getChirotation());
		result.setBlockSetting(blockSetting());
		return result;
	}
	
	//Block settings
	private static final ListenableValue<@Nil BlockType> bsetting = new ListenableValue<>(null);
	@Override
	public ListenableValue<@Nil BlockType> getBlockVariable() {
		return bsetting;
	}
	

}
