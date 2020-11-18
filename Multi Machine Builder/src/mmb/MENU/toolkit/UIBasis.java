/**
 * 
 */
package mmb.MENU.toolkit;

import java.awt.Graphics;

import javax.swing.JPanel;

import mmb.MENU.toolkit.events.UIMouseEvent;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;

/**
 * @author oskar
 *
 */
public class UIBasis extends JPanel{
	@Override
	protected void finalize() throws Throwable {
		contents.destroy();
		super.finalize();
	}
	private UIBasis that = this;
	private boolean dynamic = false;
	private Timer repaint = new Timer();
	public Runnable refresh = () -> {};
	private TimerTask repaintTask = new TimerTask() {
		@Override public void run() {
			that.paintImmediately();
			refresh.run();
		}
	};
	
	public UIBasis() {
		super();
		
		init();
	}
	
	public UIBasis(UIComponent comp) {
		super();
		contents = comp;
		init();
	}
	
	private void init() {
		setFocusable(true);
		addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				if(contents.getCommonEventHandler() == null) return;
				contents.getCommonEventHandler().scroll(arg0.getScrollAmount());
			}
		});
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(contents.getCommonEventHandler() == null) return;
				contents.getCommonEventHandler().keyPress(arg0.getKeyCode());
			}
			@Override
			public void keyReleased(KeyEvent e) {
				if(contents.getCommonEventHandler() == null) return;
				contents.getCommonEventHandler().keyRelease(e.getKeyCode());
			}
			@Override
			public void keyTyped(KeyEvent arg0) {
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				ComponentEventHandler ceh = contents.getHandler();
				if(ceh != null) {
					ceh.drag(new UIMouseEvent(e));
				}
			}
			@Override
			public void mouseMoved(MouseEvent e) {
				ComponentEventHandler ceh = contents.getHandler();
				if(ceh != null) {
					ceh.mouseMoved(new UIMouseEvent(e));
				}
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				ComponentEventHandler ceh = contents.getHandler();
				if(ceh != null) {
					ceh.press(new UIMouseEvent(e));
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				ComponentEventHandler ceh = contents.getHandler();
				if(ceh != null) {
					ceh.release(new UIMouseEvent(e));
				}
			}
		});
	}
	/**
	 * The main component to draw
	 */
	public UIComponent contents;

	@Override
	public void paint(Graphics arg0) {
		if(contents == null) return;
		contents.setHeight(getHeight());
		contents.setWidth(getWidth());
		contents.prepare(arg0);
		contents.render(arg0);
	}
	/**
	 * Paints all contents immediately
	 */
	public void paintImmediately() {
		paintImmediately(0, 0, getWidth(), getHeight());
	}
	/**
	 * @return the dynamic
	 */
	public boolean isDynamic() {
		return dynamic;
	}
	/**
	 * @param dynamic the dynamic to set
	 */
	public void setDynamic(boolean dynamic) {
		if(dynamic) {
			if(!this.dynamic) repaint.scheduleAtFixedRate(repaintTask, new Date(), 20);
		}else if(this.dynamic) repaint.cancel();
		this.dynamic = dynamic;
		
	}
	
	

}
