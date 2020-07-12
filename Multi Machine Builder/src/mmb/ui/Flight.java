/**
 * 
 */
package mmb.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import e3d.color.ColorUtils;
import e3d.d3.DDDLocation;
import e3d.display.Display3D;
import mmb.debug.Debugger;
import javax.swing.JScrollBar;

/**
 * @author oskar
 *
 */
public class Flight extends JFrame {

	private static Debugger debug = new Debugger("FLIGHT");

	//Math
	private int h, w;
	private int hw, hh;
	private Dimension oldSize = new Dimension(0,0);
	public DDDLocation pos = new DDDLocation();
	
	//Graphics objects
	private JPanel contentPane;
	private JPanel display3D;
	private BufferedImage bi;
	private Timer framerate = new Timer();
	private JScrollBar horizontal;
	private Graphics graphics;
	private JFrame that = this;

	//Render control
	protected boolean running = true;
	private int renderCount = 0;
	private int counter = 0;
	private int FPS = 0;
	private ThreadLock A, B, C, D;
	private static final class Lock {
		public static final class Lock2{}
		public Lock2 lck;
		public Lock() {
			lck = new Lock2();
		}
		public void wwait() throws InterruptedException {
			synchronized(lck) {
				lck.wait();
			}
		}
		public void nnotify() {
			synchronized(lck) {
				lck.notify();
			}
		}
	}
	private static final class ThreadLock{
		public Thread thr;
		public Lock lck;
		public ThreadLock(Thread thr, Lock lck) {
			super();
			this.thr = thr;
			this.lck = lck;
		}
		public void interrupt() {
			thr.interrupt();
		}
		public void start() {
			try {
				thr.start();
			} catch (Exception e) {
				debug.pst(e);
			}
		}
		
		public void wwait() throws InterruptedException {
				lck.wwait();
		}
		public void nnotify() {
				lck.nnotify();
		}
		
	}
	
	/**
	 * @return the counter
	 */
	public int getCounter() {
		return counter;
	}

	/**
	 * @return the fPS
	 */
	public int getFPS() {
		return FPS;
	}

	
	private ThreadLock renderThread(int x1, int y1, int x2, int y2) {
		Lock lck = new Lock();
		Thread thr = new Thread(() -> {
			while(running) {
				renderRect(x1, y1, x2, y2);
				renderCount++;
				try {
					lck.wwait();
				} catch (InterruptedException e) {}
				if(renderCount == 4) {
					renderCount = 0;
					graphics.drawImage(bi, 0, 0, null);
					counter++;
					resumeAll();
				}
			}
		});
		return new ThreadLock(thr, lck);
	}
	private void resumeAll() {
		A.nnotify();
		B.nnotify();
		C.nnotify();
		D.nnotify();
	}
	/**
	 * Create the frame.
	 */
	public Flight() {
		
		setDefaultCloseOperation(2);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		display3D = new Display3D();
		contentPane.add(display3D, BorderLayout.CENTER);
		
		horizontal = new JScrollBar();
		horizontal.setOrientation(JScrollBar.HORIZONTAL);
		contentPane.add(horizontal, BorderLayout.SOUTH);
		
		framerate.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				FPS();
				if(!display3D.getSize().equals(oldSize)) {
					try {
						resetScreen();
					} catch (Exception e) {
						debug.pst(e);
					}
				}
			}
			
		}, new Date(), 1000);
		
		addWindowListener(new WindowAdapter() {	 
			@Override
			public void windowClosing(WindowEvent e) {
				running = false;
				killThreads();
				framerate.cancel();
				that.dispose();
			} 
		});
	}
	
	private void resetScreen() {
		bi = new BufferedImage(display3D.getWidth(), display3D.getHeight(), BufferedImage.TYPE_INT_RGB);
		oldSize = display3D.getSize();
		graphics = display3D.getGraphics();
		h = display3D.getHeight();
		w = display3D.getWidth();
		hh = h/2;
		hw = w/2;
		try {
			killThreads();
		} catch (Exception e) {
			debug.pstm(e,"Exception when resetting threads");
		}
		
		A = renderThread(0,    0,    hw,  hh);
		B = renderThread(0,    hh+1, hw,  h-1);
		C = renderThread(hw+1, 0,    w-1, hh);
		D = renderThread(hw+1, hh+1, w-1, h-1);
		
		if(A.thr != null) {
			debug.print("not null");
		}
		A.start();
		B.start();
		C.start();
		D.start();
	}
	
	private void killThreads() {
		try {
			A.interrupt();
			B.interrupt();
			C.interrupt();
			D.interrupt();
		} catch (Exception e) {
			debug.pst(e);
		}
	}
	private void renderRect(int x1, int y1, int x2, int y2) {
		try {
			for(int i = y1; i <= y2; i++) renderLine(x1, x2, i);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void renderLine(int x1, int x2, int y){
		for(int i = x1; i <= x2; i++) renderPixel(i, y);
	}
	private void renderPixel(int x, int y) {
		double X = x - (w / 2);
		double Y = h / 2 - y;
		double zz = pos.pos.y/Y;
		double xx = zz * X / 16;
		double zzz = pos.pos.z - zz * 16;
		if(Y < 0) {
			bi.setRGB(x, y, ground(xx, zzz));
		}else {
			bi.setRGB(x, y, sky(Y));
		}
		
	}
	private int ground(double x, double y) {
		if(x < 0) return ground(-x, y);
		if(y < 0) return ground(x, -y);
		double X = x % 1;
		double Y = y % 1;
		if(X > 0.5) X = 1-X;
		if(Y > 0.5) Y = 1-Y;
		X = (X+Y)*128 + 128;
		if(X > 255) X = 255;
		return ColorUtils.colorRGBA(0, 0, (int) X, 255);
	}
	private int sky(double y) {
		return atmoshade(Math.exp(pos.pos.y * y / -20000));
	}
	private int atmoshade(double progress) {
		if(!(progress > 0)) {
			return ColorUtils.black;
		}else if(progress < 0.5) {
			return ColorUtils.colorRGBA(0, 0, (int)(progress * 511), 255);
		}else if(progress == 0.5) {
			return ColorUtils.blue;
		}else if(progress < 1) {
			return ColorUtils.colorRGBA(0, (int)(progress*511.5 - 255), 255, 255);
		}else {
			return ColorUtils.cyan;
		}
	}
	
	public void start() {
		bi = new BufferedImage(display3D.getWidth(), display3D.getHeight(), BufferedImage.TYPE_INT_RGB);
		oldSize = display3D.getSize();
		graphics = display3D.getGraphics();
		h = display3D.getHeight();
		w = display3D.getWidth();
		hh = h/2;
		hw = w/2;
		A = renderThread(0,    0,    hw,  hh);
		B = renderThread(0,    hh+1, hw,  h-1);
		C = renderThread(hw+1, 0,    w-1, hh);
		D = renderThread(hw+1, hh+1, w-1, h-1);
		A.start();
		B.start();
		C.start();
		D.start();
	}
	private void FPS() {
		FPS = counter;
		counter = 0;
		setTitle("FPS:" + FPS);
	}

}
