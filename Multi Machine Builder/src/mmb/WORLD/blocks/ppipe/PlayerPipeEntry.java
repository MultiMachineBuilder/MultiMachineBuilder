/**
 * 
 */
package mmb.WORLD.blocks.ppipe;

import javax.annotation.Nonnull;

import mmb.WORLD.block.BlockEntry;
import mmb.WORLD.blocks.ContentsBlocks;
import mmb.WORLD.rotate.ChirotatedImageGroup;
import mmb.WORLD.rotate.Side;
import mmb.WORLD.worlds.player.PlayerPhysicsPipe;
import mmb.WORLD.worlds.world.Player;
import mmb.WORLD.worlds.world.World;

/**
 * @author oskar
 *
 */
public class PlayerPipeEntry extends PlayerPipe {
	@Nonnull private static final ChirotatedImageGroup img = ChirotatedImageGroup.create("machine/pipe exit.png");
	public PlayerPipeEntry() {
		super(ContentsBlocks.PPIPE_cap, img, Side.U, Side.D, 1);
	}

	@Override
	public void onPlayerCollide(int blockX, int blockY, World world, Player player) {
		int collide = BlockEntry.displaceFrom(blockY+0.25, blockY+0.25, blockX+0.75, blockY+0.75, player, false);
		if(collide != 0) {
			PlayerPhysicsPipe phys = new PlayerPhysicsPipe();
			phys.pipe = tunnel;
			phys.progress = 0;
			phys.direction = Direction.FWD;
			player.physics = phys;
		}
	}

}
