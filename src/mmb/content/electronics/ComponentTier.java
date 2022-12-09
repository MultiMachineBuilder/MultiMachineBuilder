/**
 * 
 */
package mmb.content.electronics;

import static mmb.engine.GlobalSettings.$res;

import java.awt.Color;

import javax.annotation.Nonnull;

import mmb.engine.item.Item;
import mmb.engine.item.Items;
import mmbbase.beans.Titled;
import monniasza.collects.Identifiable;

/**
 * @author oskar
 *
 */
public class ComponentTier implements Titled, Identifiable<String>{
	/** The color of the component's contacts */
	@Nonnull public final Color c;
	/** The prefix for component titles */
	@Nonnull public final String tiername;
	/** The ID used for item ID generation*/
	@Nonnull public final String id;
	public ComponentTier(Color c, String id) {
		this.c= c;
		this.id= id;
		this.tiername = $res("tier-"+id);
		Electronics.tiers0.add(this);
	}
	
	@Nonnull public ElectronicsComponent generate(ComponentGenerator gen) {
		return advcomponent(gen);
	}
	@Nonnull private ElectronicsComponent advcomponent(ComponentGenerator gen) {
		Item item = new ElectronicsComponent(gen, this)
		.title(tiername+gen.pre)
		.texture(gen.newTexture(this))
		.volumed(0.00125)
		.finish("industry."+gen.id+"-"+id);
		Items.tagsItem(item, "parts-electronic", "parts-electronic"+id);
		return (ElectronicsComponent) item;
	}
	
	@Override
	public String id() {
		return id;
	}
	@Override
	public String title() {
		return tiername;
	}
}
