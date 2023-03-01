/**
 * 
 */
package mmb.content.old;

import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.Nil;
import mmb.cgui.BlockActivateListener;
import mmb.content.ContentsBlocks;
import mmb.engine.block.BlockEntityRotary;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.rotate.RotatedImageGroup;
import mmb.engine.worlds.world.World;
import mmb.menu.world.window.WorldWindow;

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
	public void click(int blockX, int blockY, World map, @Nil WorldWindow window, double partX, double partY) {
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
