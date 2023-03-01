/**
 * 
 */
package mmb.content.electronics;

import static mmb.engine.settings.GlobalSettings.$res;

import java.awt.Color;

import mmb.NN;
import mmb.beans.Titled;
import mmb.engine.item.Item;
import mmb.engine.item.Items;
import monniasza.collects.Identifiable;

/**
 * Describes a component tier, used for electronics progression
 * @author oskar
 */
public class ComponentTier implements Titled, Identifiable<String>{
	/** The color of the component's contacts */
	@NN public final Color c;
	/** The prefix for component titles */
	@NN public final String tiername;
	/** The ID used for item ID generation*/
	@NN public final String id;
	/**
	 * Creates a component tier
	 * @param c tier color. It is shown on its contacts
	 * @param id tier ID
	 */
	public ComponentTier(Color c, String id) {
		this.c= c;
		this.id= id;
		this.tiername = $res("tier-"+id);
		Electronics.tiers0.add(this);
	}
	
	@NN public ElectronicsComponent generate(ComponentGenerator gen) {
		return advcomponent(gen);
	}
	@NN private ElectronicsComponent advcomponent(ComponentGenerator gen) {
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
