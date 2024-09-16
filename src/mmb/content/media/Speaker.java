/**
 * 
 */
package mmb.content.media;

import java.awt.Graphics;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rainerhahnekamp.sneakythrow.Sneaky;

import mmb.NN;
import mmb.Nil;
import mmb.beans.BlockActivateListener;
import mmb.content.ContentsBlocks;
import mmb.engine.block.BlockEntityData;
import mmb.engine.block.BlockEntry;
import mmb.engine.block.BlockType;
import mmb.engine.debug.Debugger;
import mmb.engine.sound.Sound;
import mmb.engine.sound.Sounds;
import mmb.engine.texture.BlockDrawer;
import mmb.engine.texture.Textures;
import mmb.engine.worlds.MapProxy;
import mmb.engine.worlds.world.World;
import mmb.engine.worlds.world.WorldUtils;
import mmb.menu.world.window.WorldWindow;

/**
 * @author oskar
 * The speaker is a block which allows sound to be played
 */
public class Speaker extends BlockEntityData implements BlockActivateListener {
	//un-saveable, reconstructed from the save data
	private Sound sound;
	/**
	 * The clip should be preserved for speaker's life, but activated or reactivated when needed
	 */
	@NN private final Clip clip = Sneaky.sneak(AudioSystem::getClip);
	
	//saveable elements
	private String selection;
	private int phase;
	private boolean playbackRequested;
	private boolean hasAlreadyGotSignal;
	@NN private SpeakerMode mode = SpeakerMode.ONCE;
	private final Object soundLock = new Object();
	
	/**
	 * Creates a speaker block
	 */
	public Speaker() {
		eventRemoval.addListener(e -> clip.close());
	}
	
	@Override
	public BlockType type() {
		return ContentsBlocks.SPEAKER;
	}

	@Override
	public BlockEntry blockCopy() {
		return new Speaker();
	}

	@Override
	public void load(@Nil JsonNode data) {
		if(data == null) return;
		JsonNode nsound = data.get("sound");
		if(nsound != null) setSelection(nsound.asText());
		JsonNode nmode = data.get("mode");
		if(nmode != null) setMode(SpeakerMode.valueOf(nmode.asText()));
		JsonNode nstate = data.get("state");
		if(nstate != null) playbackRequested = nstate.asBoolean();
		JsonNode nsignal = data.get("signal");
		if(nsignal != null) hasAlreadyGotSignal = nsignal.asBoolean();
		JsonNode nphase = data.get("phase");
		if(nphase != null) phase = nphase.asInt();
	}

	@Override
	protected void save0(ObjectNode node) {
		node.put("sound", selection);
		node.put("mode", mode.name());
		node.put("state", playbackRequested);
		node.put("signal", hasAlreadyGotSignal);
		node.put("phase", phase);
	}
	
	/**
	 * @param sound
	 */
	public void setSound(@Nil Sound sound) { //something breaks the block, when loading, clip is created and sound is set correctly
		synchronized(soundLock){
			this.sound = sound;
			selection = null;
			clip.close();
			if (sound != null) {
				Sneaky.sneaked(() -> sound.open(clip)).run();
			}
		}
	}

	/**
	 * @return the animation's phase
	 */
	public int getPhase() {
		return phase;
	}

	/**
	 * @param phase new animation phase to go to
	 */
	public void setPhase(int phase) {
		this.phase = phase;
	}

	private static final BlockDrawer spk1 = BlockDrawer.ofImage(Textures.get("machine/speaker 1.png"));
	private static final BlockDrawer spk2 = BlockDrawer.ofImage(Textures.get("machine/speaker 2.png"));
	private static final BlockDrawer spk3 = BlockDrawer.ofImage(Textures.get("machine/speaker 3.png"));
	@Override
	public void render(int x, int y, Graphics g, int side) {
		switch(phase) {
		case 0:
		case 2:
			spk2.draw(this, x, y, g, side);
			break;
		case 1:
			spk1.draw(this, x, y, g, side);
			break;
		case 3:
			spk3.draw(this, x, y, g, side);
			break;
		default:
			break;
		}
	}

	@Override
	public void onTick(MapProxy map) {
		//animate
		if(clip.isActive()) {
			phase++;
			if(phase > 3) phase = 0;
		}else phase=0;
		
		synchronized(soundLock){
			//sound playback
			boolean isSignalled = WorldUtils.hasIncomingSignal(this); //signal reads correctly
			mode.run(this, isSignalled); //mode is read correctly
			hasAlreadyGotSignal = isSignalled;
		}
	}

	@Override
	public void onStartup(World map, int x, int y) {
		clip.close();
		if(sound != null){
			Sneaky.sneaked(() -> sound.open(clip)).run();
		}
	}

	@Override
	public void onPlace(World map, int x, int y) {
		onStartup(map, x, y);
	}
	
	/**
	 * @return the currently selected sound
	 */
	public String getSelection() {
		return selection;
	}

	/**
	 * @param selection new sound to select
	 */
	public void setSelection(@Nil String selection) {
		if(selection == null) {
			setSound(null);
		}else {
			Sound s = Sounds.getSound(selection);
			setSound(s);
		}
		this.selection = selection;
	}

	/**
	 * @return the sound
	 */
	public Sound getSound() {
		return sound;
	}

	/**
	 * @return the clip
	 */
	public Clip clip() {
		return clip;
	}

	/**
	 * @return is the speaker requested to play a sound?
	 */
	public boolean isPlaybackRequested() {
		return playbackRequested;
	}

	/**
	 * @param playbackRequested should the speaker requested to play a sound?
	 */
	public void setPlaybackRequested(boolean playbackRequested) {
		this.playbackRequested = playbackRequested;
	}
	
	/** Run the speaker as a loop. Used by {@link SpeakerMode}*/
	void handleLoop() {
		if(playbackRequested) {
			if(!clip.isActive()) {
				clip.setMicrosecondPosition(0);
				clip.start();
			}
		}
	}
	
	/** Run the speaker as a loop. Used by {@link SpeakerMode}*/
	void handleLoopInterruptible() {
		if(playbackRequested) {
			if(!clip.isActive()) {
				clip.setMicrosecondPosition(0);
				clip.start();
			}
		}else {
			clip.stop();
		}
	}
	
	/** Run the speaker once. Used by {@link SpeakerMode}*/
	void handleOnce() {
		if(playbackRequested) {
			if(!clip.isActive()) {
				clip.start();
			}
			playbackRequested=false;
			if(!hasAlreadyGotSignal) clip.setMicrosecondPosition(0);
		}
	}
	
	/** Run the speaker once. Used by {@link SpeakerMode}*/
	void handleOnceInterruptible() {
		if(playbackRequested) {
			if(!clip.isActive()) {
				clip.start();
			}
		}else {
			clip.stop();
			clip.setMicrosecondPosition(0);
		}
	}
	
	//Playback controls
	public void rewind() {
		clip.setMicrosecondPosition(0);
	}
	public void stop() {
		playbackRequested = false;
		clip.stop();
		clip.setMicrosecondPosition(0);
	}
	public void pause() {
		clip.stop();
	}
	public void play() {
		playbackRequested = true;
		clip.setMicrosecondPosition(0);
		clip.start();
	}
	public void resume() {
		playbackRequested = true;
		clip.start();
	}

	//Click listener
	@Override
	public void click(int blockX, int blockY, World map, @Nil WorldWindow window, double partX, double partY) {
		if(window == null) {
			//Clicked by a Block Clicking Claw
		}else {
			//Clicked by a player
			window.openAndShowWindow(new SpeakerGUI(this, window), type().title());
		}
	}

	/**
	 * @return the mode
	 */
	public SpeakerMode getMode() {
		return mode;
	}

	/**
	 * @param mode the mode to set
	 */
	public void setMode(SpeakerMode mode) {
		this.mode = mode;
	}
}
