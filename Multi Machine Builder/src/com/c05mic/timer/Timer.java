package com.c05mic.timer;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 
 * A abstract timer utility class, that supports pause and resume.
 * 
 * Can be used either as a normal timer or a countdown timer.
 * 
 * @author Sridhar Sundar Raman
 * 
 */
public abstract class Timer {
	public static final int DURATION_INFINITY = -1;
	private volatile boolean isRunning = false;
	private long interval;
	private long elapsedTime;
	private long duration;
	private ScheduledExecutorService execService = Executors
			.newSingleThreadScheduledExecutor();
	private Future<?> future = null;
	TimerAggregator aggregator;

	/**
	 * Default constructor which sets the interval to 1000 ms (1s) and the
	 * duration to {@link Timer#DURATION_INFINITY}
	 */
	public Timer() {
		this(1000, -1);
	}

	/**
	 * @param interval
	 *            The time gap between each tick in millis.
	 * @param duration
	 *            The period in millis for which the timer should run. Set it to {@code Timer#DURATION_INFINITY} if the timer has to run indefinitely.
	 */
	public Timer(long interval, long duration) {
		this.interval = interval;
		this.duration = duration;
		this.elapsedTime = 0;
	}

	/**
	 * Starts or resumes the timer. If the timer was already running, this call is ignored.
	 */
	public void start() {
		if (isRunning)
			return;

		isRunning = true;
		//scheduleWithFixedDelay
		future = execService.scheduleAtFixedRate(() -> {
				onTick();
				elapsedTime += Timer.this.interval;
				if (duration > 0 && elapsedTime >=duration){
					onFinish();
					future.cancel(false);
				}
			}, 0, this.interval, TimeUnit.MILLISECONDS);
	}

	/**
	 * Paused the timer. If the timer is not running, this call is ignored.
	 */
	public void pause() {
		if(!isRunning) return;
		future.cancel(false);
		isRunning = false;
	}
	
	/**
	 *	This method is called periodically with the interval set as the delay between subsequent calls. 
	 */
	protected abstract void onTick();
	
	/**
	 * This method is called once the timer has run for the specified duration. If the duration was set as infinity, then this method is never called.
	 */
	protected abstract void onFinish();

	/**
	 * Stops the timer. If the timer is not running, then this call does nothing.
	 */
	public void cancel() {
		pause();
		this.elapsedTime = 0;
	}

	/**
	 * @return the elapsed time (in millis) since the start of the timer.
	 */
	public long getElapsedTime() {
		return this.elapsedTime;
	}
	
	/**
	 * @return the time remaining (in millis) for the timer to stop. If the duration was set to {@code Timer#DURATION_INFINITY}, then -1 is returned.
	 */
	public long getRemainingTime(){
		if(this.duration <0)
			return Timer.DURATION_INFINITY;
		return duration-elapsedTime;
	}
	
	/**
	 * @return true if the timer is currently running, and false otherwise.
	 */
	public boolean isRunning() {
		return isRunning;
	}

	/**
	 * Destroys the timer
	 */
	public void destroy() {
		cancel();
		execService.shutdown();
	}

	/**
	 * Creates a timer which runs once per second indefinitely
	 * @param tick action
	 * @return a new timer
	 */
	public static Timer of(Runnable tick) {
		return of(tick, () -> {}, 1000, DURATION_INFINITY);
	}
	
	/**
	 * Creates a timer which runs with specified period and duration
	 * @param tick action
	 * @param interval interval
	 * @param duration duration
	 * @return a new timer
	 */
	public static Timer of(Runnable tick, long interval, long duration) {
		return of(tick, () -> {}, interval, duration);
	}
	
	/**
	 * Creates a timer which runs with specified period and duration and calls finish after finishing
	 * @param tick run loop
	 * @param finish finish action
	 * @param interval interval
	 * @param duration duration
	 * @return a new timer
	 */
	public static Timer of(Runnable tick, Runnable finish, long interval, long duration) {
		return new Timer(interval, duration) {
			@Override protected void onTick() {
				tick.run();
			}

			@Override protected void onFinish() {
				finish.run();
			}
		};
	}
}
