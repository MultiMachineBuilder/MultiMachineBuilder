/**
 * 
 */
package mmb.WORLD.gui;

import java.awt.Component;

import javax.annotation.Nonnull;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import mmb.WORLD.tool.ToolSelectionModel;
import mmb.WORLD.tool.Tools;
import mmb.WORLD.tool.WindowTool;
import mmb.debug.Debugger;
import monniasza.collects.Collects;

import javax.swing.ListSelectionModel;

/**
 * @author oskar
 *
 */
public class WorldToolList extends JList<WindowTool> {
	private static final long serialVersionUID = -8108364897856379665L;
	@Nonnull public final DefaultListModel<WindowTool> model = new DefaultListModel<>();
	private static final Debugger debug = new Debugger("TOOL LIST");
	public WorldToolList(ToolSelectionModel tsmodel) {
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		addListSelectionListener(e -> {
			WindowTool tool = getSelectedValue();
			//model.elementAt(e.getFirstIndex());
			debug.printl("Tool selected: "+ tool.id);
			tsmodel.toolSelectedToolList(tool);
		});
		Tools.createWindowTools(Collects.toWritableList(model));
		setCellRenderer(new CellRenderer());
		setModel(model);
	}
	
	private static class CellRenderer extends JLabel implements ListCellRenderer<WindowTool>{
		private static final long serialVersionUID = 1968960106003843654L;
		
		public CellRenderer() {
			setOpaque(true);
		}
		@Override
		public Component getListCellRendererComponent(
			@SuppressWarnings("null") JList<? extends WindowTool> list,
			@SuppressWarnings("null") WindowTool tool,
			int index,
			boolean isSelected,
			boolean hasFocus
		){
			setOpaque(true);
			setIcon(tool.getIcon());
			setText(tool.title());
			
			if (isSelected) {
			    setBackground(list.getSelectionBackground());
			    setForeground(list.getSelectionForeground());
			} else {
			    setBackground(list.getBackground());
			    setForeground(list.getForeground());
			}
			return this;
		}
		
	}
}
