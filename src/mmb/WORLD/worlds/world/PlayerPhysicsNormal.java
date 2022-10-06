/**
 * 
 */
package mmb.WORLD.worlds.world;

import mmb.WORLD.block.BlockEntry;

/**
 * @author oskar
 *
 */
public class PlayerPhysicsNormal implements PlayerPhysics {
	@Override
	public void onTick(World w, Player p, double ctrlX, double ctrlY) {
		//Collision check - ensure in boundaries
		double minX = w.startX + 0.4;
		if (p.pos.x < minX) {
			p.speed.x = 0;
			p.pos.x = minX;
		}
		double maxX = w.endX - 0.4;
		if (p.pos.x > maxX) {
			p.speed.x = 0;
			p.pos.x = maxX;
		}
		double minY = w.startY + 0.4;
		if (p.pos.y < minY) {
			p.speed.y = 0;
			p.pos.y = minY;
		}
		double maxY = w.endY - 0.4;
		if (p.pos.y > maxY) {
			p.speed.y = 0;
			p.pos.y = maxY;
		}
		//Collision check t - restrict to blocks	
		double l = p.pos.x - 0.35;
		double r = p.pos.x + 0.35;
		double u = p.pos.y - 0.35;
		double d = p.pos.y + 0.35;
		int L = (int) Math.floor(l);
		int R = (int) Math.floor(r);
		int U = (int) Math.floor(u);
		int D = (int) Math.floor(d);
		if(L != R) {
			if (U == D) {
				//The player spans 2 blocks horizontally
				BlockEntry ll = w.get(L, U);
				BlockEntry rr = w.get(R, U);
				ll.onPlayerCollide(L, U, w, p);
				rr.onPlayerCollide(R, U, w, p);
			}else{
				//The player spans 4 blocks
				BlockEntry ul = w.get(L, U);
				BlockEntry ur = w.get(R, U);
				BlockEntry dl = w.get(L, D);
				BlockEntry dr = w.get(R, D);
				ul.onPlayerCollide(L, U, w, p);
				ur.onPlayerCollide(R, U, w, p);
				dl.onPlayerCollide(L, D, w, p);
				dr.onPlayerCollide(R, D, w, p);
			}
		}else if(U != D){
			//The player spans 2 blocks horizontally
			BlockEntry uu = w.get(L, U);
			BlockEntry dd = w.get(L, D);
			uu.onPlayerCollide(L, U, w, p);
			dd.onPlayerCollide(L, D, w, p);
		}else{
			BlockEntry b = w.get(L, U);
			b.onPlayerCollide(L, U, w, p);
		}
		
		//Motion
		p.speed.x += ctrlX * 0.02;
		p.speed.y += ctrlY * 0.02;
		p.pos.x += p.speed.x * 0.02;
		p.pos.y += p.speed.y * 0.02;
	}

	@Override
	public String description() {
		return "Normal";
	}
}
