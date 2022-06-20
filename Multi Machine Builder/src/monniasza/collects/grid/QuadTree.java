/**
 * 
 */
package monniasza.collects.grid;

/**
 * @author oskar
 *
 */
public interface QuadTree<T> {
	public QuadTreeNode<T> ul();
	public QuadTreeNode<T> ur();
	public QuadTreeNode<T> dl();
	public QuadTreeNode<T> dr();
	public void setUL(QuadTreeNode<T> node);
	public void setUR(QuadTreeNode<T> node);
	public void setDL(QuadTreeNode<T> node);
	public void setDR(QuadTreeNode<T> node);
}
