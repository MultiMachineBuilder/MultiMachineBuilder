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
import mmb.WORLD.block.SkeletalBlockEntityData;
import mmb.WORLD.gui.NewTextEditor;
import mmb.WORLD.gui.window.WorldWindow;
import mmb.WORLD.worlds.MapProxy;
import mmb.WORLD.worlds.SignalUtils;
import mmb.WORLD.worlds.world.World;
import mmb.WORLD.worlds.world.World.BlockMap;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public class WWChatter extends SkeletalBlockEntityData implements TextMessageProvider, BlockActivateListener {
	private String contents;
	private static final Debugger debug = new Debugger("CHAT");

	public WWChatter(int x, int y, BlockMap owner2) {
		super(x, y, owner2);
	}

	@Override
	public void load(JsonNode data) {
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
		if(SignalUtils.hasIncomingSignal(x, y, owner)) {
			debug.printl(contents);
		}
	}

	@Override
	public void click(int blockX, int blockY, World map, @Nullable WorldWindow window) {
		if(window == null) return;
		NewTextEditor editor = new NewTextEditor(this, this, window);
		window.openDialogWindow(editor, editor.title);
	}

}
