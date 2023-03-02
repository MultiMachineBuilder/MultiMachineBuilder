package mmb.engine.mods;

import mmb.NN;

/**
 * Central class of each addon. Runs when the mod is loaded
 * @author oskar
 * Interface should be implemented by classes only
 * Class must have implicit or explicit constructor with no inputs
 * Class should set its public static variable which contains its instance
 */
public interface AddonCentral {
	/** @return a ModMetadata object with mod information */
    @NN ModMetadata info();
    /** Place your init and translation code here */
	void firstOpen(); //first open
	/** Place an init call to your blocks/items class here and create content */
	void makeContent(); //Make content for addon
	/** Place an init call to your recipes class here and integrate with other mods */
	void integrationModules(); //integration modules
}
