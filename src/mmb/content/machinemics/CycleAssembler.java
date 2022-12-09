/**
 * 
 */
package mmbgame.machinemics;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.cgui.BlockActivateListener;
import mmb.menu.world.window.WorldWindow;
import mmbeng.block.BlockEntry;
import mmbeng.block.BlockType;
import mmbeng.block.BlockEntityRotary;
import mmbeng.rotate.RotatedImageGroup;
import mmbeng.worlds.world.World;
import mmbgame.ContentsBlocks;

/**
 * @author oskar
 *
 */
public class CycleAssembler extends BlockEntityRotary implements BlockActivateListener {

	@Override
	public BlockType type() {
		return ContentsBlocks.CYCLEASSEMBLY;
	}
	
	private static final RotatedImageGroup TEXTURE = RotatedImageGroup.create("machine/cyclic assembler.png");

	@Override
	public RotatedImageGroup getImage() {
		return TEXTURE;
	}

	@Override
	public void click(int blockX, int blockY, World map, @Nullable WorldWindow window, double partX, double partY) {
		if(window == null) return;
	}

	@Override
	protected void save1(ObjectNode node) {
		// TODO Auto-generated method stub
		super.save1(node);
	}

	@Override
	protected void load1(ObjectNode node) {
		// TODO Auto-generated method stub
		super.load1(node);
	}

	@Override
	public BlockEntry blockCopy() {
		CycleAssembler assembler = new CycleAssembler();
		return assembler;
	}

}
