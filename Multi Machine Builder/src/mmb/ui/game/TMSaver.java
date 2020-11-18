/**
 * 
 */
package mmb.ui.game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mmb.WORLD.tileworld.block.Block;
import mmb.WORLD.tileworld.block.Blocks;
import mmb.WORLD.tileworld.map.TileMap;
import mmb.debug.Debugger;
import mmb.files.databuffer.Savers;

/**
 * @author oskar
 *
 */
public class TMSaver {
	private static final Debugger debug = new Debugger("SAVE & LOAD TILEWORLD");

	public static void save2(DataOutputStream dos, TileMap map) throws IOException {
		save1(dos, map);
		Savers.txtsSaver.save(dos, (String[]) map.blockdata.toArray());
	}

	public static void save1(DataOutputStream dos, TileMap data) throws IOException {
		dos.writeInt(data.startX);
		dos.writeInt(data.startY);
		dos.writeInt(data.sizeX);
		dos.writeInt(data.sizeY);
		for(int i = 0; i < data.size; i++) {
			dos.writeInt(data.data[i]);
		}
	}
	
	//Readers
	
	public static TileMap read2(DataInputStream dis) throws IOException {
		TileMap tm = read1(dis);
		String[] read = Savers.txtsSaver.read(dis);
		List<Block> blocks = new ArrayList<Block>();
		for(int i = 0; i < read.length; i++) {
			blocks.add(Blocks.blocks.get(read[i]));
		}
		return tm;
	}
	public static TileMap read1(DataInputStream dis) throws IOException {
		return TileMap.loadv1(dis);
	}

}
