package mmb.addon.patch;

import java.awt.*;
import java.awt.event.*;
import java.util.function.Consumer;

public class Overrides {
	public Runnable initWorld = null,
					tickWorld = null,
					exitWorld = null,
					
					initEditor = null,
					tickEditor = null,
					exitEditor = null;
	
	public Consumer<Graphics2D> paintWorld = null,
								paintEditor = null;
	
	public MouseAdapter mouseEditor = null,
						mouseWorld = null;
	
	public KeyAdapter	keyEditor = null,
						keyWorld = null;
	
}
