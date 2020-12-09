/**
 * 
 */
package mmb.RUNTIME;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author oskar
 *
 */
public class GameLoop implements Runnable{
	public Runnable before = () -> {};
	public Runnable loop = () -> {};
	public Runnable after = () -> {};
	public Consumer<InterruptedException> onInterrupt = (e) -> {};
	public Supplier<Boolean> condition = null;
	public boolean isContinue = true;
	private LoopState state = LoopState.NEW;
	public final Thread thread = new Thread(this::fullRun);
	enum LoopState {
		NEW, START, RUN, END, FINISHED
	}
	
	@Override
	public GameLoop clone() throws CloneNotSupportedException {
		GameLoop loops = (GameLoop) super.clone();
		loops.before = before;
		loops.after = after;
		loops.loop = loop;
		loops.condition = condition;
		loops.onInterrupt = onInterrupt;
		return loops;
	}

	protected void fullRun() {
		state = LoopState.START;
		before.run();
		state = LoopState.RUN;
		while(condition==null? isContinue : condition.get()) {
			loop.run();
		}
		state = LoopState.END;
		state = LoopState.FINISHED;
	}

	@Override
	public void run() {
		if(!thread.isAlive()) thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			onInterrupt.accept(e);
		}
	}

}
