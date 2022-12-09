/**
 * 
 */
package mmb.content.ppipe;

import javax.annotation.Nonnull;

import mmb.content.ContentsBlocks;
import mmb.engine.block.BlockEntry;
import mmb.engine.debug.Debugger;
import mmb.engine.rotate.ChirotatedImageGroup;
import mmb.engine.rotate.Side;
import mmb.engine.worlds.world.Player;
import mmb.engine.worlds.world.PlayerPhysicsPipe;
import mmb.engine.worlds.world.World;

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
