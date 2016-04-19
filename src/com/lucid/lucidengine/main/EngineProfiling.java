package com.lucid.lucidengine.main;

/**
 * @author Mark Diedericks
 *
 */

public class EngineProfiling {
	
	private int numInvocation;
	private double totalTime;
	private double startTime;
	private final String profilingClass;
	
	public EngineProfiling(String profilingClass) {
		this.profilingClass = profilingClass;
		numInvocation = 0;
		totalTime = 0;
		startTime = 0;
	}
	
	public double displayAndReset(String message, double inDividend) {
		
		double dividend = inDividend;
		double time;
		if(dividend != 0) dividend = numInvocation;
		if(dividend == 0)	time = 0; else time = ((totalTime / 1000000.0) / (double)dividend);
		
		System.out.println(message + ((totalTime / 1000000.0) / (double)numInvocation) + " milliseconds - Currently profiling class: " + profilingClass);
		reset();
		return time;
	}
	
	public void reset() {
		totalTime = 0.0;
		numInvocation = 0;
	}
	
	public void startInvocation() {
		startTime = System.nanoTime();
	}
	
	public void stopInvocation() {
		if(startTime != 0) {
			numInvocation++;
			totalTime = (System.nanoTime() - startTime);
			startTime = 0;
		} else {
			System.err.println("EngineProfiling: StopInvocation() called without calling StartInvocation() - Currently profiling class: " + profilingClass);
		}
	}
}
