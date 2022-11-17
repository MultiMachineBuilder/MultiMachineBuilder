/**
 * 
 */
package mmb.world.blocks.actuators;

import java.awt.Point;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.beans.BlockSetting;
import mmb.cgui.BlockActivateListener;
import mmb.data.contents.Textures;
import mmb.debug.Debugger;
import mmb.menu.world.SelectBlock;
import mmb.menu.world.window.WorldWindow;
import mmb.world.block.BlockEntry;
import mmb.world.block.BlockType;
import mmb.world.block.Blocks;
import mmb.world.blocks.ContentsBlocks;
import mmb.world.item.Items;
import mmb.world.rotate.RotatedImageGroup;
import mmb.world.worlds.MapProxy;
import mmb.world.worlds.world.World;

/**
 * @author oskar
 *
 */
public class ActuatorPlaceBlock extends AbstractActuatorBase implements BlockActivateListener , BlockSetting{
	private BlockType block;
	private static final Debugger debug = new Debugger("ACTUATOR-PLACER");
	private static final RotatedImageGroup texture = RotatedImageGroup.create(Textures.get("machine/placer.png"));
	@Override
	protected void save1(ObjectNode node) {
		if(block == null) node.put("place", "mmb.grass");
		else node.put("place", block.id());
	}
	@Override
	protected void load1(ObjectNode node) {
		block = Items.getExpectType(node.get("place").asText(null), BlockType.class);
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
		BlockType block0 = block;
		if(block0 == null) return;
		try {
			proxy.place(block0, p);
		}catch(Exception e) {
			debug.pstm(e, "Failed to place a block");
		}
	}
	@Override
	public void click(int blockX, int blockY, World map, @Nullable WorldWindow window, double partX, double partY) {
		if(window == null) return;
		window.openDialogWindow(new SelectBlock(this, window), "["+posX()+","+posY()+"]");
	}
	@Override
	public BlockType getBlockSetting() {
		return block;
	}
	@Override
	public void setBlockSetting(@Nullable BlockType setting) {
		block = setting;
	}
	@Override
	public BlockEntry blockCopy() {
		ActuatorPlaceBlock result = new ActuatorPlaceBlock();
		result.setChirotation(getChirotation());
		result.block = block;
		return result;
	}

}
