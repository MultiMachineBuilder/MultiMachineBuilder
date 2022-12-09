/**
 * 
 */
package mmbgame.wireworld;

import java.awt.Point;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.beans.BlockSetting;
import mmb.cgui.BlockActivateListener;
import mmb.data.variables.ListenableValue;
import mmb.menu.world.window.WorldWindow;
import mmbeng.block.BlockEntry;
import mmbeng.block.BlockType;
import mmbeng.debug.Debugger;
import mmbeng.item.Items;
import mmbeng.rotate.RotatedImageGroup;
import mmbeng.texture.Textures;
import mmbeng.worlds.MapProxy;
import mmbeng.worlds.world.World;
import mmbgame.ContentsBlocks;

/**
 * @author oskar
 *
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
			debug.pstm(e, "Failed to place a block");
		}
	}
	@Override
	public void click(int blockX, int blockY, World map, @Nullable WorldWindow window, double partX, double partY) {
		if(window == null) return;
		window.openDialogWindow(new SelectBlock(this, window), "["+posX()+","+posY()+"]");
	}
	@Override
	public BlockEntry blockCopy() {
		ActuatorPlaceBlock result = new ActuatorPlaceBlock();
		result.setChirotation(getChirotation());
		result.setBlockSetting(blockSetting());
		return result;
	}
	
	//Block settings
	@Nonnull private static final ListenableValue<@Nullable BlockType> bsetting = new ListenableValue<>(null);
	@Override
	public ListenableValue<BlockType> getBlockVariable() {
		return bsetting;
	}
	

}
