/**
 * 
 */
package mmb.WORLD.item;

import java.util.concurrent.ExecutionException;

import javax.annotation.Nonnull;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import mmb.GlobalSettings;
import mmb.DATA.contents.NotFoundException;

/**
 * @author oskar
 *
 */
public class ItemRaw extends Item {
	@Nonnull private static final String RAW = " "+GlobalSettings.$res("rawitem");
	@Nonnull private static final LoadingCache<@Nonnull ItemEntityType, @Nonnull ItemRaw> memoizer
	= CacheBuilder.newBuilder().build(CacheLoader.from(ItemRaw::new));
	
	/**
	 * Obtains a raw item for the item entity type. If it does not exist, the item will be registered and set up
	 * @param iet item entity type
	 * @return a raw item
	 * @throws InternalError when constructor evaluation fails (this WILL be a bug)
	 */
	@Nonnull public static ItemRaw make(ItemEntityType iet) {
		try {
			return memoizer.get(iet);
		} catch (ExecutionException e) {
			throw new InternalError("Evaluation failed for "+iet.id(), e);
		}
	}
	
	@Nonnull public final ItemEntityType iet;
	private ItemRaw(ItemEntityType iet) {
		this.iet = iet;
		title(iet.title()+RAW);
		texture(iet.getTexture());
		describe(iet.description());
		volumed(iet.volume());
		finish("rawitem."+iet.id());
		
		Items.tagsItem(this, Items.btags.get(iet));
		Items.tagItem("raw", this);
	}
	@Override
	public String toString() {
		return "ItemRaw " + iet;
	}
}
