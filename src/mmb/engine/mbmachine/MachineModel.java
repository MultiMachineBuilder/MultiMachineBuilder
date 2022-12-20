/**
 * 
 */
package mmb.engine.mbmachine;

import java.awt.Graphics;
import java.awt.Point;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.checkerframework.checker.nullness.qual.NonNull;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.NN;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.Placer;
import mmb.engine.debug.Debugger;
import mmb.engine.item.Item;
import mmb.engine.worlds.world.World;
import mmbbase.menu.world.window.WorldWindow;

/**
 * @author oskar
 *
 */
public class MachineModel extends Item implements Placer {
	private static final Debugger debug = new Debugger("MACHINES");
	public final Class<@NonNull ? extends Machine> machineClass;
	private final String errorMessage;
	
	public Previewer preview = (a, b, c, d) -> {/*unused*/};
	
	@SuppressWarnings("unchecked")
	private MachineModel(Class<@NonNull ? extends Machine> machineClass, String name) {
		models.put((Class<? extends MachineModel>) machineClass, this);
		models2.put(name, this);
		this.machineClass = machineClass;
		title(name);
		finish("mmb.multimachine."+name);
		this.errorMessage = "Failed to create a machine "+name;
	}
	public Machine place() {
		try {
			@NN Machine result = machineClass.getConstructor().newInstance(); //create
			result.onPlace();
			return result;
		} catch (Exception e) {
			debug.pstm(e, errorMessage);
			return null;
		}
	}
	
	public Machine initialize() {
		try {
			@NN Machine result = machineClass.getConstructor().newInstance(); //create
			result.onStartup();
			return result;
		} catch (Exception e) {
			debug.pstm(e, errorMessage);
			return null;
		}
	}
	public Machine initialize(JsonNode je) {
		try {
			@NN Machine result = machineClass.getConstructor().newInstance(); //create
			result.onStartup();
			result.load(je);
			result.onDataLoaded();
			return result;
		} catch (Exception e) {
			debug.pstm(e, errorMessage);
			return null;
		}
	}
	public Machine initialize(int x, int y, JsonNode je) {
		try {
			@NN Machine result = machineClass.getConstructor().newInstance(); //create
			result.onStartup();
			result.load(je);
			result.setPos(x, y);
			result.onDataLoaded();
			return result;
		} catch (Exception e) {
			debug.pstm(e, errorMessage);
			return null;
		}
	}
	public Machine initialize(Point p, JsonNode je) {
		return initialize(p.x, p.y, je);
	}
	
	private static final Map<Class<? extends MachineModel>, MachineModel> models = new HashMap<>();
	private static final Map<String, MachineModel> models2 = new HashMap<>();
	/**
	 * Create a MachineModel for given class.
	 * @param cls machine class
	 * @param name machine's ID
	 * @return a MachineModel with given class
	 */
	public static MachineModel forClass(Class<? extends Machine> cls, String name) {
		MachineModel result = models.get(cls);
		if(result == null) return new MachineModel(cls, name);
		return result;
	}
	public static MachineModel forID(String id) {
		return models2.get(id);
	}
	/**
	 * @return an immutable {@code Map<String, MachineModel>} of all machines
	 */
	public static Map<String, MachineModel> getMachineModels(){
		return Collections.unmodifiableMap(models2);
	}

	@Override
	public BlockEntry place(int x, int y, World map) {
		map.placeMachine(this, x, y);
		return null;
	}
	@Override
	public void openGUI(WorldWindow window) {
		//unused
	}
	@Override
	public void closeGUI(WorldWindow window) {
		//unused
	}
	@Override
	public void preview(Graphics g, Point renderStartPos, World map, Point targetLocation, int side) {
		preview.draw(g, renderStartPos, map, targetLocation);
	}
}
