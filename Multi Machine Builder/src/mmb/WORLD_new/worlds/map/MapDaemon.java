/**
 * 
 */
package mmb.WORLD_new.worlds.map;

import java.util.ArrayList;

import mmb.WORLD_new.block.BlockEntry;
import mmb.WORLD_new.block.BlockType;
import mmb.debug.Debugger;

/**
 * @author oskar
 * A map daemon:
 * <ul>
 * 	<li>runs update cycle</li>
 *  <li>manages block activity</li>
 * </ul>
 */
public class MapDaemon{
	private final Debugger debug;
	public final BlockMap map;
	public boolean running = false;
	//This thread is a shutdown hook to close a map after JVM is stopped
	private final Thread closer = new Thread(() -> {
		//Run shutdown listeners
	});
	
	private final Thread run = new Thread(() -> {
		
	});
	protected MapDaemon(BlockMap m) {
		map = m;
		Debugger debugger;
		if(m.getName() == null) {
			debugger = new Debugger("MAP DAEMON anonymous");
		}else {
			debugger = new Debugger("MAP DAEMON "+m.getName());
		}
		debug = debugger;
		Runtime.getRuntime().addShutdownHook(closer);
	}
	@Override
	public void finalize() {
		Runtime.getRuntime().removeShutdownHook(closer);
	}
	
	//Shutdown listeners
	private ArrayList<DaemonShutdownListener> listeners = new ArrayList<DaemonShutdownListener>();
	public void addShutdownListener(DaemonShutdownListener l) {
		listeners.add(l);
	}
	public void removeShutdownListener(DaemonShutdownListener l) {
		listeners.remove(l);
	}
	
	//Run an update tick
	private void runTick() {
		for(int x = map.startX; x < map.endX; x++) {
			for(int y = map.startY; y < map.endY; y++) {
				try {
					BlockEntry ent = map.get(x, y);
					BlockType typ = ent.getType();
					if(typ.onUpdate != null) {
						typ.onUpdate.accept(ent);
					}
				} catch (Exception e) {
					debug.pstm(e, "Failed to update the block at ("+x+","+y+")");
				}
			}
		}
	}
}
