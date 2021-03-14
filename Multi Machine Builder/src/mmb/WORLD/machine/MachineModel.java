/**
 * 
 */
package mmb.WORLD.machine;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;

import mmb.GameObject;
import mmb.WORLD.BlockDrawer;
import mmb.WORLD.gui.Placer;
import mmb.WORLD.worlds.world.World;
import mmb.WORLD.worlds.world.World.BlockMap;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class MachineModel implements Placer {
	private static final Debugger debug = new Debugger("MACHINES");
	public final Class<? extends Machine> machineClass;
	public final String name;
	private final String errorMessage;
	public BlockDrawer icon;
	public String title;
	
	public Previewer preview = (a, b, c, d) -> {};
	
	@SuppressWarnings("unchecked")
	private MachineModel(Class<? extends Machine> machineClass, String name) {
		models.put((Class<? extends MachineModel>) machineClass, this);
		models2.put(name, this);
		this.machineClass = machineClass;
		this.name = name;
		this.errorMessage = "Failed to create a machine "+name;
		title = name;
	}
	public Machine place() {
		return place(null);
	}
	public Machine place(@Nullable GameObject owner) {
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
	public Machine initialize(JsonNode je) {
		try {
			Machine result = machineClass.newInstance(); //create
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
			Machine result = machineClass.newInstance(); //create
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
		try {
			Machine result = machineClass.newInstance(); //create
			result.onStartup();
			result.load(je);
			result.setPos(p);
			result.onDataLoaded();
			return result;
		} catch (Exception e) {
			debug.pstm(e, errorMessage);
			return null;
		}
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
	public BufferedImage getIcon() {
		return icon.img;
	}
	@Override
	public String title() {
		return title;
	}
	@Override
	public void place(int x, int y, World map) {
		map.placeMachine(this, x, y);
	}
	@Override
	public void openGUI() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void closeGUI() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void preview(Graphics g, Point renderStartPos, BlockMap map, Point targetLocation) {
		preview.draw(g, renderStartPos, map, targetLocation);
	}
}
