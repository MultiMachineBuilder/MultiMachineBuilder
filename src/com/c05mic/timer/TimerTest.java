package com.c05mic.timer;

public class TimerTest {
	public static void main(String[] args){
		//This creates a timer which will tick every second indefinitely.
		Timer oneSecondInfiniteTimer = new ExampleTimer();
		
		//This creates a timer which ticks every 2 seconds, and runs for 20 seconds.
		Timer twoSecondTimer = new ExampleTimer(2000l, 20000l);
		
		//Start the timer.
		twoSecondTimer.start();
		
		//Pause the timer.
		twoSecondTimer.pause();
	}
}
