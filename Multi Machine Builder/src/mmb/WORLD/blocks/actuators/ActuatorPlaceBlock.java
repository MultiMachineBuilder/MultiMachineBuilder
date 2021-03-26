/**
 * 
 */
package mmb.WORLD.blocks.actuators;

import java.awt.Point;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.BEANS.BlockActivateListener;
import mmb.BEANS.BlockSetting;
import mmb.DATA.contents.texture.Textures;
import mmb.WORLD.RotatedImageGroup;
import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.block.Blocks;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.gui.SelectBlock;
import mmb.WORLD.gui.WorldWindow;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.world.World;
import mmb.WORLD.worlds.world.World.BlockMap;
import mmb.debug.Debugger;

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
		block = Blocks.get(node.get("place").asText(null));
	}

	public ActuatorPlaceBlock(int x, int y, BlockMap owner2) {
		super(x, y, owner2);
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
		if(block == null) return;
		try {
			proxy.place(block, p);
		}catch(Exception e) {
			debug.pstm(e, "Failed to place a block");
		}
	}
	@Override
	public void click(int blockX, int blockY, World map, @Nullable WorldWindow window) {
		if(window == null) return;
		window.openDialogWindow(new SelectBlock(this, window), "["+x+","+y+"]");
	}
	@Override
	public BlockType getBlockSetting() {
		return block;
	}
	@Override
	public void setBlockSetting(BlockType setting) {
		block = setting;
	}

}
