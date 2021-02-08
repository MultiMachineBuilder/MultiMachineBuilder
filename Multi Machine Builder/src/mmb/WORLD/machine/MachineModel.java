/**
 * 
 */
package mmb.WORLD.machine;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;

import mmb.GameObject;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class MachineModel {
	private static final Debugger debug = new Debugger("MACHINES");
	public final Class<? extends Machine> machineClass;
	public final String name;
	private final String errorMessage;
	
	@SuppressWarnings("unchecked")
	private MachineModel(Class<? extends Machine> machineClass, String name) {
		models.put((Class<? extends MachineModel>) machineClass, this);
		this.machineClass = machineClass;
		this.name = name;
		this.errorMessage = "Failed to create a machine "+name;
	}
	public Machine place() {
		return place(null);
	}
	public Machine place(GameObject owner) {
		try {
			Machine result = machineClass.newInstance(); //create
			result.onPlace(owner);
			return result;
		} catch (Exception e) {
			debug.pstm(e, errorMessage);
			return null;
		}
	}
	
	public Machine initialize() {
		try {
			Machine result = machineClass.newInstance(); //create
			result.onStartup();
			return result;
		} catch (Exception e) {
			debug.pstm(e, errorMessage);
			return null;
		}
	}
	public Machine initialize(JsonElement je) {
		try {
			Machine result = machineClass.newInstance(); //create
			result.onStartup();
			result.onDataLoaded(je);
			return result;
		} catch (Exception e) {
			debug.pstm(e, errorMessage);
			return null;
		}
	}
	public Machine initialize(int x, int y, JsonElement je) {
		try {
			Machine result = machineClass.newInstance(); //create
			result.onStartup();
			result.onDataLoaded(je);
			result.setPos(x, y);
			return result;
		} catch (Exception e) {
			debug.pstm(e, errorMessage);
			return null;
		}
	}
	public Machine initialize(Point p, JsonElement je) {
		try {
			Machine result = machineClass.newInstance(); //create
			result.onStartup();
			result.onDataLoaded(je);
			result.setPos(p);
			return result;
		} catch (Exception e) {
			debug.pstm(e, errorMessage);
			return null;
		}
	}
	
	private static final Map<Class<? extends MachineModel>, MachineModel> models = new HashMap<>();
	private static final Map<String, MachineModel> models2 = new HashMap();
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
}
