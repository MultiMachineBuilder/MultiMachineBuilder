/**
 * 
 */
package mmb.WORLD.blocks;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mmb.BEANS.BlockActivateListener;
import mmb.BEANS.TextMessageProvider;
import mmb.WORLD.block.BlockType;
import mmb.WORLD.block.BlockEntityData;
import mmb.WORLD.gui.NewTextEditor;
import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.SignalUtils;
import mmb.WORLD.worlds.world.World;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class WWChatter extends BlockEntityData implements TextMessageProvider, BlockActivateListener {
	private String contents;
	private static final Debugger debug = new Debugger("CHAT");

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
		if(SignalUtils.hasIncomingSignal(this)) {
			debug.printl(contents);
		}
	}

	@Override
	public void click(int blockX, int blockY, World map, @Nullable WorldWindow window, double partX, double partY) {
		if(window == null) return;
		NewTextEditor editor = new NewTextEditor(this, this, window);
		window.openDialogWindow(editor, editor.title);
	}

}
