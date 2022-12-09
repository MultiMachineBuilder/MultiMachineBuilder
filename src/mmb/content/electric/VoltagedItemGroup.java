/**
 * 
 */
package mmb.content.electric;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import mmb.engine.GlobalSettings;
import mmb.engine.item.Item;
import mmb.engine.item.Items;
import mmb.engine.java2d.TexGen;
import mmb.engine.texture.Textures;

/**
 * @author oskar
 * A  group of items by voltage
 */
public class VoltagedItemGroup {
	@Nonnull public final List<@Nonnull Item> items;
	@Nonnull public final List<@Nonnull BufferedImage> images;
	private static final VoltageTier[] volts = VoltageTier.values();
	/**
	 * Creates a voltaged item group
	 * @param image base image to use for items
	 * @param id group ID
	 */
	public VoltagedItemGroup(BufferedImage image, String id) {
		images = TexGen.generateMachineTextures(image);
		IntStream stream = IntStream.range(0, 9);
		items = stream.mapToObj(num -> {
			VoltageTier volt = volts[num];
			Item type = new Item();
			type.title(GlobalSettings.$res1("component-"+id)+' '+volt.name);
			type.texture(images.get(num));
			type.finish("machineparts."+id+num);
			return type;
		}).collect(Collectors.toList());
		Items.tagItems("component-"+id, items);
		for(int i = 0; i < 9; i++) {
			Item block = items.get(i);
			Items.tagItem("voltage-"+volts[i].name, block);
		}
	}
	/**
	 * @param string
	 * @param id
	 */
	public VoltagedItemGroup(String texture, String id) {
		this(Textures.get(texture), id);
	}
}
