package mmb.MODS.info;

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
    ModMetadata info();
	void firstOpen(); //first open
	void makeContent(); //Make content for addon
	void integrationModules(); //integration modules
	/**
	 * Create overrides and implementations
	 * @return
	 */
	/*default Overrides overrides() {
		//return new Overrides();
	}*/

}
