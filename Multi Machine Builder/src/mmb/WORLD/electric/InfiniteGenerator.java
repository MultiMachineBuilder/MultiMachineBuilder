/**
 * 
 */
package mmb.WORLD.electric;

import javax.annotation.Nonnull;

import mmb.WORLD.Side;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.block.SkeletalBlockEntityDataless;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.world.BlockMap;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class InfiniteGenerator extends SkeletalBlockEntityDataless {
	public InfiniteGenerator(int x, int y, @Nonnull BlockMap owner2) {
		super(x, y, owner2);
	}

	@Override
	public void onTick(MapProxy map) {
		shoveElectricity(Side.U, 1_000_000);
		shoveElectricity(Side.D, 1_000_000);
		shoveElectricity(Side.L, 1_000_000);
		shoveElectricity(Side.R, 1_000_000);
	}
	private static final Debugger debug = new Debugger("INFINITY GENERATOR");
	public void shoveElectricity(@Nonnull Side s, double amt) {
		Electricity elec = owner.getAtSide(s, x, y).getElectricalConnection(s.negate());
		if(elec == null) return;
		double insert = elec.insert(amt);
		//debug.printl("Electricity inserted: "+insert+" at "+s);
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
