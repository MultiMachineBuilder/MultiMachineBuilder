/**
 * 
 */
package mmb.world.blocks.ppipe;

import javax.annotation.Nonnull;

import mmb.debug.Debugger;
import mmb.world.block.BlockEntry;
import mmb.world.blocks.ContentsBlocks;
import mmb.world.rotate.ChirotatedImageGroup;
import mmb.world.rotate.Side;
import mmb.world.worlds.world.Player;
import mmb.world.worlds.world.PlayerPhysicsPipe;
import mmb.world.worlds.world.World;

/**
 * @author oskar
 *
 */
public class PlayerPipeEntry extends PlayerPipe {
	@Nonnull private static final ChirotatedImageGroup img = ChirotatedImageGroup.create("machine/pipe exit.png");
	public PlayerPipeEntry() {
		super(ContentsBlocks.PPIPE_cap, img, Side.U, Side.D, 1);
	}

	private static final Debugger debug = new Debugger("TEST PIPES");
	@Override //on some pipes this does not work
	public void onPlayerCollide(int blockX, int blockY, World world, Player player) {
		
		int collide = BlockEntry.displaceFrom(blockX+0.25, blockY+0.25, blockX+0.75, blockY+0.75, player, false);
		debug.printl("X: "+posX()+"/"+blockX+", Y: "+posY()+"/"+blockY+", "+collide);
		if(collide != 0) {
			PlayerPhysicsPipe phys = new PlayerPhysicsPipe();
			phys.pipe = tunnel;
			phys.progress = 0;
			phys.direction = Direction.FWD;
			player.physics = phys;
		}
	}

}
