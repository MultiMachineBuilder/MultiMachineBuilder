/**
 * 
 */
package mmb.WORLD_new.block;

/**
 * @author oskar
 *
 */
public class BlockMap {
	public final int startX, startY;
	public final BlockEntry[][] entries;
	public BlockMap(int startX, int startY, BlockEntry[][] entries) {
		super();
		this.startX = startX;
		this.startY = startY;
		this.entries = entries;
	}
	public BlockMap(int startX, int startY, int sizeX, int sizeY) {
		super();
		this.startX = startX;
		this.startY = startY;
		this.entries = new BlockEntry[sizeX][sizeY];
	}
}
