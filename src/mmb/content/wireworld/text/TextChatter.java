/**
 * 
 */
package mmb.content.wireworld.text;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.Nil;
import mmb.cgui.BlockActivateListener;
import mmb.content.ContentsBlocks;
import mmb.engine.block.BlockEntityData;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.debug.Debugger;
import mmb.engine.worlds.MapProxy;
import mmb.engine.worlds.world.World;
import mmb.engine.worlds.world.WorldUtils;
import mmb.menu.world.window.WorldWindow;

/**
 * Prints a specified message when powered.
 * @author oskar
 * @see TextMessageProvider
 * @see TextEditor
 */
public class TextChatter extends BlockEntityData implements TextMessageProvider, BlockActivateListener {
	private static final Debugger debug = new Debugger("CHAT");
	
	//Constructors
	/**
	 * Creates a chetter with text
	 * @param contents2 text to use
	 */
	public TextChatter(@Nil String contents2) {
		contents = contents2;
	}
	/** Creates an empty chatter */
	public TextChatter() {}
	
	//Contents
	@Nil private String contents;
	@Override
	@Nil public String getMessage() {
		return contents;
	}
	@Override
	public void setMessage(@Nil String msg) {
		contents = msg;
	}
	
	//Block methods
	@Override
	public BlockType type() {
		return ContentsBlocks.ww_chatter;
	}
	@Override
	public BlockEntry blockCopy() {
		return new TextChatter(contents);
	}
	@Override
	public void onTick(MapProxy map) {
		if(WorldUtils.hasIncomingSignal(this)) {
			debug.printl(contents);
		}
	}
	@Override
	public void click(int blockX, int blockY, World map, @Nil WorldWindow window, double partX, double partY) {
		if(window == null) return;
		TextEditor editor = new TextEditor(this, this, window);
		window.openAndShowWindow(editor, editor.title);
	}
	
	//Serialization
	@Override
	public void load(@Nil JsonNode data) {
		if(data == null) return;
		contents = data.get("value").asText();
	}
	@Override
	protected void save0(ObjectNode node) {
		node.put("value", contents);
	}
}
