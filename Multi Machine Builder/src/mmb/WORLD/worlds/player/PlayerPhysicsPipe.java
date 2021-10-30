/**
 * 
 */
package mmb.WORLD.worlds.player;

import javax.annotation.Nonnull;

import org.joml.Vector2d;

import mmb.WORLD.blocks.ppipe.Direction;
import mmb.WORLD.blocks.ppipe.PipeTunnel;
import mmb.WORLD.blocks.ppipe.PipeTunnelEntry;
import mmb.WORLD.worlds.world.Player;
import mmb.WORLD.worlds.world.World;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class PlayerPhysicsPipe implements PlayerPhysics {
	private static final Debugger debug = new Debugger("PLAYER IN PIPE");

	//When player reaches end of the pipe, then it returns to beginning
	@Override
	public void onTick(World w, Player p, int ctrlX, int ctrlY) {		
		//Transfer to next or previous
		boolean check = true;
		while(check) {
			//Perform checks
			boolean below = progress < 0;
			boolean above = progress > pipe.path.length;
			check = below || above;
			
			//Some old values
			Direction olddir = direction;
			double len = pipe.path.length;
			
			//Define replacements
			Direction newdir = direction;
			double newprog = progress;
			PipeTunnel newpipe = pipe;
			
			//Player jumps over diagonals
			if(below) {
				double oldprog = progress;
				PipeTunnelEntry pte = pipe.findPrev();
				if(pte == null) {
					progress += len;
					eject(p);
					return;
				}
				newprog += len;
				newpipe = pte.pipe;
				newdir = pte.dir;
				if(olddir != newdir) {
					newprog = -oldprog;
				}
			}
			if(above) { //When encountering smaller diagonal pipe in other direction, it is skipped
				PipeTunnelEntry pte = pipe.findNext();
				if(pte == null) {
					progress -= len;
					eject(p);
					return;
				}
				newprog -= len;
				newpipe = pte.pipe;
				newdir = pte.dir;
				if(olddir != newdir) {
					newprog = len - progress;
				}
			}
			
			//Update the values
			direction = newdir;
			progress = newprog;
			pipe = newpipe;
		}
		
		
		//Move along the pipe
		double speed = p.speed.x;
		speed += ACCELRATION * 0.02;
		if(speed > MAX_SPEED) speed = MAX_SPEED;
		progress += speed * direction.mul * 0.02;
		pipe.path.interpolate(progress, p.pos);
		p.speed.x = speed;
		p.speed.y = 0;
	}
	private static final double MAX_SPEED = 10.0;
	private static final double ACCELRATION = 1.0;
	//Ejects the player out of the pipe
	private void eject(Player p) {
		Vector2d dump = new Vector2d();
		progress += 1.5 * direction.mul;
		pipe.path.interpolate(progress, dump);
		p.pos.set(dump);
		p.speed.x = 0;
		p.speed.y = 0;
		p.physics = new PlayerPhysicsNormal();
		debug.printl("Player ejected");
	}
	public double progress;
	public PipeTunnel pipe;
	@Nonnull public Direction direction = Direction.FWD;
	@Override
	public String description() {
		return "Direction: "+direction;
	}
}
