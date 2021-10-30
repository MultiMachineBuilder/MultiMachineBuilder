/**
 * 
 */
package mmb.WORLD.electric;

import mmb.WORLD.block.BlockType;
import mmb.WORLD.block.BlockEntityDataless;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.rotate.Side;
import mmb.WORLD.worlds.MapProxy;

/**
 * @author oskar
 *
 */
public class InfiniteGenerator extends BlockEntityDataless {
	@Override
	public void onTick(MapProxy map) {
		shoveElectricity(Side.U, 1_000_000);
		shoveElectricity(Side.D, 1_000_000);
		shoveElectricity(Side.L, 1_000_000);
		shoveElectricity(Side.R, 1_000_000);
	}
	public void shoveElectricity(Side s, double amt) {
		Electricity elec = getAtSide(s).getElectricalConnection(s.negate());
		if(elec == null) return;
		elec.insert(amt);
	}

	@Override
	public Electricity getElectricalConnection(Side s) {
		return Electricity.NONE;
	}

	@Override
	public BlockType type() {
		return ContentsBlocks.INFINIGEN;
	}

}
