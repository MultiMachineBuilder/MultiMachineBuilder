/**
 * 
 */
package mmb.WORLD.block;

import java.awt.Graphics;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pploder.events.CatchingEvent;
import com.pploder.events.Event;

import mmb.WORLD.rotate.Chirality;
import mmb.WORLD.rotate.ChirotatedImageGroup;
import mmb.WORLD.rotate.Chirality.ChiralityListener;
import mmb.WORLD.rotate.Rotation;
import mmb.debug.Debugger;

/**
 * @author oskar
 *
 */
public abstract class BlockEntityChirotable extends BlockEntityData {
	private static final Debugger debug = new Debugger("CHIROTABLE BLOCK");
	@Override
	public void load(@Nullable JsonNode data) {
		if(data == null) return;
		JsonNode dirnode = data.get("dir");
		boolean dir = (dirnode == null) ? false : dirnode.asBoolean();
		chirality = dir ? Chirality.L : Chirality.R;
		JsonNode rotnode = data.get("side");
		if(rotnode != null) {
			String str = rotnode.asText("N");
			if(str != null) {
				Rotation rot = Rotation.valueOf(str);
				rotation = rot;
			}
		}
		load1((ObjectNode) data);
	}

	//Chirality
	@Nonnull private Chirality chirality = Chirality.R;
	@Override
	public Chirality getChirality() {
		return chirality;
	}
	@Override
	public void setChirality(Chirality chirality) {
		if(this.chirality == chirality) return;
		this.chirality = chirality;
		onChirality.trigger(chirality);
	}
	private Event<Chirality> onChirality = new CatchingEvent<>(debug, "Failed to run chirality event");
	public void addChiralityListener(ChiralityListener listener) {
		onChirality.addListener(listener);
	}
	public void removeChiralityListener(ChiralityListener listener) {
		onChirality.removeListener(listener);
	}
	@Override
	public boolean isChiral() {
		return true;
	}
	
	//Rotation
	@Nonnull private Rotation rotation = Rotation.N;
	private Event<Rotation> onRotation = new CatchingEvent<>(debug, "Failed to run rotation event");
	@Override
	public void setRotation(Rotation rotation) {
		if(this.rotation == rotation) return;
		this.rotation = rotation;
		onRotation.trigger(rotation);
	}
	@Override
	public Rotation getRotation() {
		return rotation;
	}
	@Override
	public boolean isRotary() {
		return true;
	}

	@Override
	protected final void save0(ObjectNode node) {
		node.put("side", rotation.toString());
		node.put("dir", chirality == Chirality.L);
		save1(node);
	}
	
	protected void save1(ObjectNode node) {}
	protected void load1(ObjectNode node) {}
	
	public void render(int x, int y, Graphics g, int ss) {
		getImage().get(rotation, chirality).draw(this, x, y, g, ss);
	}
	/**
	 * @return the texture of this block
	 */
	public abstract ChirotatedImageGroup getImage();
	

}
