/**
 * 
 */
package mmb.WORLD.inventory;

/**
 * @author oskar
 *
 */
public class SimpleItem implements Item {
	public double volume = 0.02;
	public String id;
	public String name;
	/**
	 * @return the id
	 */
	@Override
	public String getID() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setID(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param volume the volume to set
	 */
	public void setVolume(double volume) {
		this.volume = volume;
	}

	@Override
	public double getVolume() {
		return volume;
	}

	@Override
	public SimpleItem getType() {
		return this;
	}

}
