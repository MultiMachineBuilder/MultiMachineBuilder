/**
 * 
 */
package mmb.content.electric;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import mmb.annotations.NN;
import mmb.engine.NotFoundException;
import mmb.engine.item.Item;
import mmb.engine.item.Items;
import mmb.engine.java2d.TexGen;
import mmb.engine.settings.GlobalSettings;
import mmb.engine.texture.Textures;

/**
 * Generates 9 items, one for each voltage
 * @author oskar
 */
public class VoltagedItemGroup {
	/** All items in this group */
	@NN public final List<@NN Item> items;
	/** All item textures */
	@NN public final List<@NN BufferedImage> images;
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
	 * Creates a voltaged item group
	 * @param texture texture name
	 * @param id group ID
	 * @throws NotFoundException when texture does not exist
	 */
	public VoltagedItemGroup(String texture, String id) {
		this(Textures.get(texture), id);
	}
}
