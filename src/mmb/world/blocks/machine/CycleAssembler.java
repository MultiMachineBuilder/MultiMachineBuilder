/**
 * 
 */
package mmb.world.blocks.machine;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.cgui.BlockActivateListener;
import mmb.menu.world.window.WorldWindow;
import mmb.world.block.BlockEntry;
import mmb.world.block.BlockType;
import mmb.world.blocks.ContentsBlocks;
import mmb.world.blocks.SkeletalBlockEntityRotary;
import mmb.world.rotate.RotatedImageGroup;
import mmb.world.worlds.world.World;

/**
 * @author oskar
 *
 */
public class CycleAssembler extends SkeletalBlockEntityRotary implements BlockActivateListener {

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
