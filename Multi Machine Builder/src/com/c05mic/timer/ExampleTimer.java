package com.c05mic.timer;

public class ExampleTimer extends Timer{
	
	public ExampleTimer() {
		super();
	}
	
	public ExampleTimer(long interval, long duration){
		super(interval, duration);
	}

	@Override
	protected void onTick() {
		System.out.println("onTick called!");
	}

	@Override
	protected void onFinish() {
		System.out.println("onFinish called!");
	}
	
}
