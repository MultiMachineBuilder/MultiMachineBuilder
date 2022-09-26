/**
 * 
 */
package com.c05mic.timer;

import java.util.HashSet;

/**
 * @author oskar
 * Aggregates mutiple timers into one convenient set
 */
public class TimerAggregator extends HashSet<Timer> {
	private static final long serialVersionUID = 4623598949262420169L;

	@Override
	public boolean add(Timer timer) {
		if(timer.aggregator != null) throw new IllegalStateException("The timer already belongs to an aggregator");
		timer.aggregator = this;
		return super.add(timer);
	}

	@Override
	public boolean remove(Object obj) {
		if(obj instanceof Timer) {
			Timer timer = (Timer)obj;
			if(timer.aggregator == this) timer.aggregator = null;
		}
		return super.remove(obj);
	}
	public void start() {
		for(Timer timer: this) {
			timer.start();
		}
	}
	public void pause() {
		for(Timer timer: this) {
			timer.pause();
		}
	}
	public void cancel() {
		for(Timer timer: this) {
			timer.cancel();
		}
	}
	public void destroy() {
		for(Timer timer: this) {
			timer.destroy();
		}
		clear();
	}
}
