/**
 * 
 */
package mmbgame.wireworld;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.cgui.BlockActivateListener;
import mmb.menu.world.TextEditor;
import mmb.menu.world.window.WorldWindow;
import mmbeng.block.BlockEntityData;
import mmbeng.block.BlockEntry;
import mmbeng.block.BlockType;
import mmbeng.debug.Debugger;
import mmbeng.worlds.MapProxy;
import mmbeng.worlds.world.World;
import mmbeng.worlds.world.WorldUtils;
import mmbgame.ContentsBlocks;

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
