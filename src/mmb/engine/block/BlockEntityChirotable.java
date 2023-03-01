/**
 * 
 */
package mmb.engine.block;

import java.awt.Graphics;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pploder.events.Event;

import mmb.NN;
import mmb.Nil;
import mmb.engine.CatchingEvent;
import mmb.engine.debug.Debugger;
import mmb.engine.rotate.ChiralRotation;
import mmb.engine.rotate.Chirality;
import mmb.engine.rotate.ChirotatedImageGroup;
import mmb.engine.rotate.Rotation;
import mmb.engine.rotate.Chirality.ChiralityListener;
import mmb.engine.rotate.Rotation.RotationListener;

/**
 * A block entity with chirotation support
 * @author oskar
 * @see ChiralRotation
 */
public abstract class BlockEntityChirotable extends BlockEntityData {
	@NN private static final Debugger debug = new Debugger("CHIROTABLE BLOCK");
	@Override
	public void load(@Nil JsonNode data) {
		if(data == null) return;
		JsonNode dirnode = data.get("dir");
		boolean dir = (dirnode == null) ? false : dirnode.asBoolean();
		Chirality chirality1 = dir ? Chirality.L : Chirality.R;
		Rotation rot = Rotation.N;
		JsonNode rotnode = data.get("side");
		if(rotnode != null) {
			String str = rotnode.asText("N");
			if(str != null) {
				rot = Rotation.valueOf(str);
			}
		}
		setChirotation(chirality1, rot);
		load1((ObjectNode) data);
	}

	//Chirality
	@NN private Chirality chirality = Chirality.R;
	@Override public Chirality getChirality() {
		return chirality;
	}
	@Override public void setChirality(Chirality chirality) {
		if(this.chirality == chirality) return;
		this.chirality = chirality;
		chirotation = ChiralRotation.of(rotation, chirality);
		onChirality.trigger(chirality);
	}
	private Event<Chirality> onChirality = new CatchingEvent<>(debug, "Failed to run chirality event");
	public void addChiralityListener(ChiralityListener listener) {
		onChirality.addListener(listener);
	}
	public void removeChiralityListener(ChiralityListener listener) {
		onChirality.removeListener(listener);
	}
	@Override public boolean isChiral() {
		return true;
	}
	
	//Rotation
	@NN private Rotation rotation = Rotation.N;
	private Event<Rotation> onRotation = new CatchingEvent<>(debug, "Failed to run rotation event");
	@Override public void setRotation(Rotation rotation) {
		if(this.rotation == rotation) return;
		this.rotation = rotation;
		chirotation = ChiralRotation.of(rotation, chirality);
		onRotation.trigger(rotation);
	}
	@Override public Rotation getRotation() {
		return rotation;
	}
	public void addRotationListener(RotationListener listener) {
		onRotation.addListener(listener);
	}
	public void removeRotationListener(RotationListener listener) {
		onRotation.removeListener(listener);
	}
	@Override public boolean isRotary() {
		return true;
	}

	//Chirotation
	@NN private ChiralRotation chirotation = ChiralRotation.Nr;
	/** @return current chiral rotation */
	@Override public ChiralRotation getChirotation() {
		return chirotation;
	}
	/** @param chirotation new chiral rotation */
	@Override public void setChirotation(ChiralRotation chirotation) {
		this.chirotation = chirotation;
		this.chirality = chirotation.chirality;
		this.rotation = chirotation.rotation;
		onRotation.trigger(rotation);
		onChirality.trigger(chirality);
	}

	/**
	 * Sets chirotation from the chirality and rotation
	 * @param chirality new chirality
	 * @param rotation new rotation
	 */
	public void setChirotation(Chirality chirality, Rotation rotation) {
		this.chirotation = ChiralRotation.of(rotation, chirality);
		this.chirality = chirality;
		this.rotation = rotation;
		onRotation.trigger(rotation);
		onChirality.trigger(chirality);
	}
	
	@Override protected final void save0(ObjectNode node) {
		node.put("side", rotation.toString());
		node.put("dir", chirality == Chirality.L);
		save1(node);
	}
	
	/**
	 * Additional function used to save additional data
	 * @param node node, to which data can be saved
	 */
	protected void save1(ObjectNode node) {
		//optional
	}
	/**
	 * Additional function used to save additional data
	 * @param node node, to which data can be loaded
	 */
	protected void load1(ObjectNode node) {
		//optional
	}
	
	@Override
	public void render(int x, int y, Graphics g, int ss) {
		getImage().get(rotation, chirality).draw(this, x, y, g, ss);
	}
	/**
	 * @return the texture of this block
	 */
	public abstract ChirotatedImageGroup getImage();
}
