package mmb.beans;
/**
 * Interface for storage volume for items
 */
public interface IStorageVolume {
	/** @return this item's storage volume */
	public double getStorageVolume();
	/**
	 * Sets this item's storage volume
	 * @param volume new storage volume
	 */
	public void setStorageVolume(double volume);
}
