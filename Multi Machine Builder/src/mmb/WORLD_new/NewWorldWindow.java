/**
 * 
 */
package mmb.WORLD_new;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JFrame;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import mmb.DATA.json.JsonTool;
import mmb.WORLD.tileworld.DrawerPlainColor;
import mmb.WORLD_new.block.BlockType;
import mmb.WORLD_new.block.Blocks;
import mmb.WORLD_new.gui.WorldFrame;
import mmb.WORLD_new.worlds.MapProxy;
import mmb.WORLD_new.worlds.world.World;
import mmb.debug.Debugger;
import mmb.files.saves.Save;

/**
 * @author oskar
 *
 */
public class NewWorldWindow extends JFrame{
	private Save file;
	@Override
	public void dispose() {
		worldFrame.enterWorld(null);
		worldFrame.setActive(false);
		//future save code
		super.dispose();
	}
	public NewWorldWindow() {
		worldFrame = new WorldFrame();
		getContentPane().add(worldFrame, BorderLayout.CENTER);
		setTitle("Test");
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		worldFrame.setActive(true);
		//OLD TEST CODE
		/**/
		
	}
	private static Debugger debug = new Debugger("WORLD TEST");
	private WorldFrame worldFrame;
	public static void main(String[] args) {
		NewWorldWindow nww = new NewWorldWindow();
		nww.setVisible(true);
		World w = World.createBlankWorld(new Dimension(5, 5));
		w.setName("Test");
		
		BlockType test = new BlockType();
		test.id = "Test Block";
		test.drawer = new DrawerPlainColor(Color.GREEN);
		Blocks.register(test);
		
		try(MapProxy mp = w.main.createVolatileProxy()){
			mp.setImmediately(test, 3, 3);
			mp.setImmediately(test, 3, 4);
		} catch (Exception e) {
			debug.pstm(e, "Failed to populate the map");
		}
		//Serialize
		JsonElement ser = w.serialize();
		String out = JsonTool.gson.toJson(ser);
		StringBuilder print = new StringBuilder();
		print.append("Serialized world data:\n");
		print.append(out);
		debug.printl(print.toString());
		
		//De-serialize
		JsonParser parser = new JsonParser();
		JsonObject tree = parser.parse(out).getAsJsonObject();
		World w2 = World.deserialize(tree, "test");
		
		nww.worldFrame.enterWorld(w2);
		nww.worldFrame.setActive(true);
		if(nww.worldFrame.getMap() == null) debug.printl("Map not properly loaded");
	}
	/**
	 * @param s
	 * @param deserialize
	 */
	public void setWorld(Save s, World deserialized) {
		file = s;
		worldFrame.enterWorld(deserialized);
	}
}
