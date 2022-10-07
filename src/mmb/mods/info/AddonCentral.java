package mmb.mods.info;

import javax.annotation.*;

/**
 * Central class of each addon
 * @author oskar
 * Interface should be implemented by classes only
 * Class must have implicit or explicit constructor with no inputs
 * Class should set its public static variable which contains its instance
 */
public interface AddonCentral {
	
	/**
	 * @return a ModMetadata object with mod information
	 * 
	 */
    @Nonnull ModMetadata info();
	void firstOpen(); //first open
	void makeContent(); //Make content for addon
	void integrationModules(); //integration modules
}
