/**
 * 
 */
package mmb.data.contents;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author oskar
 *
 */
public class TreeNode<T> {
	protected TreeNode<T> parent;
	protected List<TreeNode<T>> children;
	public T value;
	
	public void addTo(TreeNode<T> nd) {
		parent = nd;
		nd.children.add(this);
	}
	
	public void removeFrom() {
		if(parent != null) {
			parent.children.remove(this);
			parent = null;
		}
	}
	public TreeNode<T>[] getChildren(){
		return (TreeNode<T>[]) children.toArray();
	}
	public TreeNode<T> getParent(){
		return parent;
	}
	public TreeNode<T>findHolder(T toSearch) {
		if(value == toSearch) {
			return this;
		}
		TreeNode<T> ths = null;
		for(int i = 0; i < children.size(); i++) {
			ths = children.get(i);
			if(ths.value == toSearch) {
				return ths;
			}
		}
		if(parent == null) {
			return null;
		}
		return parent.findHolder(toSearch);
	}
	public TreeNode<T> root(){
		if(parent == null) {
			return this;
		}
		return parent.root();
	}
	
	public void iterateOverSubNodes(Consumer<TreeNode<T>> action) {
		action.accept(this);
		children.forEach((TreeNode<T> t) -> {t.iterateOverSubNodes(action);});
	}
	
	public void iterateOverAllNodes(Consumer<TreeNode<T>> action) {
		root().iterateOverSubNodes(action);
	}
	
}
