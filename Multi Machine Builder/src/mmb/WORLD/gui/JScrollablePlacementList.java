/**
 * 
 */
package mmb.WORLD.gui;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * @author oskar
 *
 */
public class JScrollablePlacementList extends JList<Placer> {
	public JScrollablePlacementList() {
	}
	private static final long serialVersionUID = -7291181619952855702L;
	private ArrayList<PlacerLabel> placers = new ArrayList<>();
	ListCellRenderer<Placer> lcr = new ListCellRenderer<Placer>() {

		@Override
		public Component getListCellRendererComponent(JList<? extends Placer> list, Placer value, int index,
				boolean isSelected, boolean cellHasFocus) {
			return placers.get(index);
		}
		
	};
	@Override
	public ListCellRenderer<? super Placer> getCellRenderer() {
		// TODO Auto-generated method stub
		return super.getCellRenderer();
	}
	protected static class PlacerLabel extends JComponent{
		private static final long serialVersionUID = -349407795991633986L;
		
	}
}
