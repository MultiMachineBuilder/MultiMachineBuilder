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
	public void onTick(World w, Player p, double ctrlX, double ctrlY) {		
		//Transfer to next or previous
		boolean check = true;
		while(check) {
			//Perform checks
			boolean below = direction == Direction.BWD && progress < 0;
			boolean above = direction == Direction.FWD && progress > pipe.path.length;
			check = below || above;
			
			//Some old values
			Direction olddir = direction;
			double len = pipe.path.length;
			
			//Define replacements
			Direction newdir = direction;
			double newprog = progress;
			double oldprog = progress;
			PipeTunnel newpipe = pipe;
			
			if(below) {
				PipeTunnelEntry pte = pipe.findPrev();
				if(pte == null) {
					progress += len;
					eject(p);
					return;
				}
				newprog += len;
				newpipe = pte.pipe;
				newdir = pte.dir;
				if(olddir != newdir) newprog = -oldprog;
			}
			if(above) {
				PipeTunnelEntry pte = pipe.findNext();
				if(pte == null) {
					progress -= len;
					eject(p);
					return;
				}
				newprog -= len;
				newpipe = pte.pipe;
				newdir = pte.dir;
				/*
				 * 0123456789ABCDEFGHIJKLMN
				 *       EDCBA9876543210
				 * 0 - - - a>|<b - - - 0
				 * 
				 * 
				 * pt1
				 * 0 2 4 6 8 A
				 * 0 - - - a>|
				 *       |<->|
				 * pt2
				 * A 8 6 4 2 0
				 * |<b - - - 0
				 * |<->|
				 * 0 - - - a>|<b - - - 0
				 *       |<->|<->|
				 *         pt1   pt2
				 *       |<-------->|
				 *        len travel
				 */
				if(olddir != newdir) newprog = (len+newpipe.path.length)/2 - newprog;
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
