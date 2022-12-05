/**
 * 
 */
package mmbgame.electronics;

import javax.annotation.Nonnull;

import mmbeng.item.Item;

/**
 * @author oskar
 *
 */
public class ElectronicsComponent extends Item {
	@Nonnull public final ComponentGenerator gen;
	@Nonnull public final ComponentTier tier;
	ElectronicsComponent(ComponentGenerator gen, ComponentTier tier){
		this.gen = gen;
		this.tier = tier;
	}
}
