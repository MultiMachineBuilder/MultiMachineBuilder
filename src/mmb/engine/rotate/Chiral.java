/**
 * 
 */
package mmb.engine.rotate;

/**
 * Adds chirality support to any object
 * @author oskar
 */
public interface Chiral {
	/**
	 * @return the chirality of this block
	 */
	public Chirality getChirality();
	/**
	 * @param chirality new chirality
	 */
	public void setChirality(Chirality chirality);
}
