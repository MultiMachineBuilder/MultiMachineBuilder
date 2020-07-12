package mmb.addon.module;

import mmb.addon.data.ModMetadata;
import mmb.addon.patch.Overrides;

/**
 * Central class of each addon
 * @author oskar
 * Interface should be implemented by classes only
 * Class should have constructor with no inputs
 * Class should set its public static variable which contains its instance
 */
public interface AddonCentral {
	/**
	 * 
	 */
	//Constructor - should be as short as possible and have no inputs.
	public ModMetadata info();
	public void firstOpen(); //first open
	public void makeContent(); //Make content for addon
	public void integrationModules(); //integration modules
	/**
	 * Create overrides and implementations
	 * @return
	 */
	default public Overrides overrides() {
		return new Overrides();
	};
	
}
