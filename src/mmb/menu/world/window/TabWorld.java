package mmb.menu.world.window;

import java.awt.Color;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;

import io.github.parubok.text.multiline.MultilineLabel;
import mmb.NN;
import mmb.engine.inv.ItemRecord;
import mmb.menu.wtool.ToolStandard;
import mmb.menu.wtool.WindowTool;
import net.miginfocom.swing.MigLayout;
import mmb.menu.components.Drag;

public class TabWorld extends JSplitPane {
	private static final long serialVersionUID = 3769240753970370607L;
	
	/** The standard tool */
	public final ToolStandard std;
	/** The index of the standard tool */
	public final int stdIndex;
	/** The world frame */
	public final WorldFrame worldFrame;
	/** The scrollable placement list */
	@NN public final WorldWindow.ScrollablePlacementList scrollablePlacementList;
	public final JScrollPane scrollistPane;
	public final ListSelectionModel selModel;
	public final DefaultListModel<ItemRecord> invModel;
	public final JProgressBar progressHP;
	public final JLabel lblStatus;
	public final WorldToolList toolList;
	public final MultilineLabel lblTool;
	/** The tool selection. Changes to the model are reflected in the window and vice versa */
	@NN public final transient ToolSelectionModel toolModel;
	private Drag drag;

	TabWorld(WorldWindow ww){
		ToolStandard std1 = null;
		this.setDividerLocation(320);
		//[start] The world frame panel
			JPanel worldFramePanel = new JPanel();
			worldFramePanel.setLayout(new MigLayout("", "[27.00][101px,grow,center]", "[80px,grow][][][]"));
			this.setRightComponent(worldFramePanel);
			
			worldFrame = new WorldFrame(ww);
			worldFrame.setBackground(Color.GRAY);
			worldFrame.titleChange.addListener(ww::updateTitle);
			worldFramePanel.add(worldFrame, "cell 0 0 2 1,grow");
			
			progressHP = new JProgressBar();
			worldFramePanel.add(progressHP, "cell 1 1,growx");
			
			drag = new Drag();
			drag.dragged.addListener(offset -> {
				double posmul = worldFrame.getBlockScale();
				worldFrame.perspective.add(offset.x() * -8 / posmul, offset.y() * -8 / posmul);
			});
			worldFramePanel.add(drag, "cell 0 1 1 2,grow");
			
			lblStatus = new JLabel("STATUSBAR");
			lblStatus.setOpaque(true);
			lblStatus.setBackground(new Color(65, 105, 225));
			worldFramePanel.add(lblStatus, "cell 1 2,growx");
		//[end]
		//[start] Scrollable Placement List Pane
			JSplitPane scrollistBipane = new JSplitPane();
			scrollistBipane.setResizeWeight(0.5);
			scrollistBipane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			scrollistBipane.setDividerLocation(0.8);
			//Scrollable Placement List
				toolModel = new ToolSelectionModel(ww);
				scrollablePlacementList = ww.new ScrollablePlacementList(toolModel);
				scrollistPane = new JScrollPane();
				scrollistPane.setViewportView(scrollablePlacementList);
				scrollistBipane.setLeftComponent(scrollistPane);
				selModel = scrollablePlacementList.getSelectionModel();
				invModel = scrollablePlacementList.getModel();
			//Tool Pane
				JScrollPane toolPane = new JScrollPane();
				toolList = new WorldToolList(toolModel, ww);
				toolList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				toolPane.setViewportView(toolList);
				WindowTool std0 = null;
				int stdIndex0 = -1;
				for(int i = 0; i < toolList.model.getSize(); i++) {
					WindowTool tool = toolList.model.elementAt(i);
					if(tool instanceof ToolStandard) {
						stdIndex0 = i;
						std0 = tool;
						break;
					}
				}
				if(std0 == null) throw new IllegalStateException("ToolStandard is missing");
				std = (ToolStandard) std0;
				stdIndex = stdIndex0;
				scrollistBipane.setRightComponent(toolPane);
			this.setLeftComponent(scrollistBipane);
		//[end]
		worldFrame.setActive(true);
		worldFrame.setPlacer(scrollablePlacementList);
		
		lblTool = new MultilineLabel("Tool description goes here");
		worldFramePanel.add(lblTool, "cell 0 3 2 1,grow");
		lblTool.setForeground(Color.WHITE);
		lblTool.setBackground(Color.BLUE);
		lblTool.setOpaque(true);
	}
}
