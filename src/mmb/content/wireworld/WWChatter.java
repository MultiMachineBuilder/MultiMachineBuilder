/**
 * 
 */
package mmb.content.wireworld;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.content.ContentsBlocks;
import mmb.engine.block.BlockEntityData;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.debug.Debugger;
import mmb.engine.worlds.MapProxy;
import mmb.engine.worlds.world.World;
import mmb.engine.worlds.world.WorldUtils;
import mmbbase.cgui.BlockActivateListener;
import mmbbase.menu.world.TextEditor;
import mmbbase.menu.world.window.WorldWindow;

/**
 * @author oskar
 *
 */
public class WWChatter extends BlockEntityData implements TextMessageProvider, BlockActivateListener {
	private String contents;
	private static final Debugger debug = new Debugger("CHAT");

	/**
	 * @param contents2
	 */
	public WWChatter(@Nullable String contents2) {
		contents = contents2;
	}
	/**
	 * Creates an empty chatter
	 */
	public WWChatter() {}

	@Override
	public void load(@Nullable JsonNode data) {
		if(data == null) return;
		contents = data.get("value").asText();
	}

	@Override
	public BlockType type() {
		return ContentsBlocks.ww_chatter;
	}

	@Override
	protected void save0(ObjectNode node) {
		node.put("value", contents);
	}

	@Override
	public String getMessage() {
		return contents;
	}

	@Override
	public void setMessage(String msg) {
		contents = msg;
	}

	@Override
	public void onTick(MapProxy map) {
		if(WorldUtils.hasIncomingSignal(this)) {
			debug.printl(contents);
		}
	}

	@Override
	public void click(int blockX, int blockY, World map, @Nullable WorldWindow window, double partX, double partY) {
		if(window == null) return;
		TextEditor editor = new TextEditor(this, this, window);
		window.openDialogWindow(editor, editor.title);
	}

	@Override
	public BlockEntry blockCopy() {
		return new WWChatter(contents);
	}

}
