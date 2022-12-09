/**
 * 
 */
package mmbbase.menu.world.window;

import java.awt.Component;

import javax.annotation.Nonnull;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import mmb.engine.debug.Debugger;
import mmbbase.menu.wtool.ToolSelectionModel;
import mmbbase.menu.wtool.Tools;
import mmbbase.menu.wtool.WindowTool;
import monniasza.collects.Collects;

/**
 * @author oskar
 *
 */
public class WorldToolList extends JList<WindowTool> {
	private static final long serialVersionUID = -8108364897856379665L;
	@Nonnull public final DefaultListModel<WindowTool> model = new DefaultListModel<>();
	private static final Debugger debug = new Debugger("TOOL LIST");
	private JPanel DUMMY = new JPanel();
	public WorldToolList(ToolSelectionModel tsmodel, WorldWindow window) {
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		addListSelectionListener(e -> {
			WindowTool tool = getSelectedValue();
			if(window.toolEditorSplitPane != null) {
				window.toolEditorSplitPane.setRightComponent(tool.GUI() == null?DUMMY:tool.GUI());
			}
			debug.printl("Tool selected: "+ tool.id);
			tsmodel.toolSelectedToolList(tool);
		});
		Tools.createWindowTools(Collects.toWritableList(model), window);
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
