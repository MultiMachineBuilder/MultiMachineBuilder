package mmb.content.imachine.tipd;

import com.fasterxml.jackson.databind.node.ObjectNode;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import mmb.NN;
import mmb.Nil;
import mmb.cgui.BlockActivateListener;
import mmb.content.ContentsBlocks;
import mmb.content.ditems.ItemBOM;
import mmb.data.variables.ListenableInt;
import mmb.engine.block.BlockEntityChirotable;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.inv.Inventories;
import mmb.engine.inv.Inventory;
import mmb.engine.inv.ItemRecord;
import mmb.engine.inv.storage.SimpleInventory;
import mmb.engine.inv.storage.SingleItemInventory;
import mmb.engine.item.ItemEntry;
import mmb.engine.json.JsonTool;
import mmb.engine.recipe.SimpleItemList;
import mmb.engine.rotate.ChirotatedImageGroup;
import mmb.engine.rotate.Side;
import mmb.engine.worlds.MapProxy;
import mmb.engine.worlds.world.World;
import mmb.menu.world.window.WorldWindow;

/**
 * The Timed Ingredient Packet Dispatcher.
 */
public class TIPD extends BlockEntityChirotable implements BlockActivateListener{
	/** Texture of the Timed Ingredient Packet Dispatcher */
	public static final ChirotatedImageGroup crig = ChirotatedImageGroup.create("machine/rationer.png", "machine/rationer backwards.png");
	/** This Timed Ingredient Packet Dispatcher's inventory */
	public final SimpleInventory inv = new SimpleInventory();
	/** Time between extractions */
	public final ListenableInt interval = new ListenableInt(50);
	/** Maximum number of times that items here may be stored */
	public final ListenableInt margin = new ListenableInt(10);
	/** Maximum number of packets per interval */
	public final ListenableInt maxtransfer = new ListenableInt(1);
	/** Inventory with the Bill of Materials */
	public final SingleItemInventory bom = new SingleItemInventory();
	/** Time elapsed since last packet */
	public int timer = 0;
	
	@Override
	public BlockType type() {
		return ContentsBlocks.TIPD;
	}

	@Override
	public BlockEntry blockCopy() {
		TIPD copy = new TIPD();
		copy.setChirotation(getChirotation());
		copy.bom.set(bom);
		copy.interval.set(interval.getInt());
		copy.margin.set(margin.getInt());
		copy.maxtransfer.set(maxtransfer.getInt());
		copy.inv.set(inv);
		copy.timer = timer;
		return copy;
	}

	@Override
	public ChirotatedImageGroup getImage() {
		return crig;
	}

	@Override
	protected void save1(ObjectNode node) {
		node.set("inv", inv.save());
		node.set("bom", bom.save());
		node.put("interval", interval.getInt());
		node.put("margin", margin.getInt());
		node.put("maxtransfer", maxtransfer.getInt());
		node.put("timer", timer);
	}

	@Override
	protected void load1(ObjectNode node) {
		inv.load(node.get("inv"));
		inv.setCapacity(2);
		bom.load(node.get("bom"));
		bom.setCapacity(2);
		interval.set(JsonTool.requestInt(node, "interval", 50));
		margin.set(JsonTool.requestInt(node, "margin", 50));
		maxtransfer.set(JsonTool.requestInt(node, "maxtransfer", 50));
		timer = JsonTool.requestInt(node, "timer", 0);
	}

	@Override
	public Inventory getInventory(Side s) {
		return inv;
	}

	@Override
	public void onTick(MapProxy map) {
		SimpleItemList itemlist = null;
		ItemEntry stencilRaw = bom.getContents();
		if(stencilRaw instanceof ItemBOM) {
			ItemBOM stencil = ((ItemBOM)stencilRaw);
			itemlist = stencil.contents();
		}
		Object2IntMap<@NN ItemEntry> toextract = new Object2IntOpenHashMap<>();
		for(ItemRecord irecord: inv) {
			int maxcount = Integer.MAX_VALUE;
			ItemEntry item = irecord.item();
			if(itemlist != null) maxcount = itemlist.get(item) * margin.getInt();
			int actualCount = irecord.amount();
			if(actualCount > maxcount) toextract.put(item, actualCount - maxcount);
		}
		for(Entry<@NN ItemEntry> irecord: toextract.object2IntEntrySet()) {
			Side u = Side.U.transform(getChirotation());
			Side d = u.negate();
			Inventories.transferStack(inv, getAtSide(u).getInput(d), irecord.getKey(), irecord.getIntValue());
		}
		timer++;
		if(timer < interval.getInt()) return;
		timer = 0;
		Side r = Side.R.transform(getChirotation());
		Side l = r.negate();
		if(itemlist != null)
			Inventories.transferBulk(inv.createReader(), getAtSide(r).getInput(l), itemlist, maxtransfer.getInt());
	}
	
	@Nil TipdGUI gui;
	@Override
	public void click(int blockX, int blockY, World map, @Nil WorldWindow window, double partX, double partY) {
		if(window == null || gui != null) return;
		gui = new TipdGUI(this, window);
		window.openAndShowWindow(gui, "TIPD");
	}
	
}
