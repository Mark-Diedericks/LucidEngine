package com.lucid.lucidengine.rendering.lighting;

import org.lwjgl.opengl.Display;

import com.lucid.lucidengine.mathematics.Vector3f;
import com.lucid.lucidengine.object.lights.DirectionalLight;

/**
 * @author Mark Diedericks
 *
 */

public class LightCycle {
	
	private double currentTime = 0.00;
	private long inGameMinute = 1000L;
	private DirectionalLight[] lights;
	private DirectionalLight currentLight;
	
	public LightCycle(double startTime, long inGameMinute) {
		this.currentTime = startTime + 0.01;
		this.inGameMinute = inGameMinute;
		
//		lights = new DirectionalLight[] {
//				new DirectionalLight(new Vector3f(1.00f, 0.75f, 0.80f), 0.250f, new Vector3f(1, 1, 1)), //0am
//					new DirectionalLight(new Vector3f(1.00f, 0.75f, 0.80f), 0.250f, new Vector3f(1, 1, 1)), //1am
//						new DirectionalLight(new Vector3f(1.00f, 0.75f, 0.80f), 0.250f, new Vector3f(1, 1, 1)), //2am
//							new DirectionalLight(new Vector3f(1.00f, 0.75f, 0.80f), 0.250f, new Vector3f(1, 1, 1)), //3am
//								new DirectionalLight(new Vector3f(1.00f, 0.75f, 0.775f), 0.30f, new Vector3f(1, 1, 1)), //4am
//									new DirectionalLight(new Vector3f(1.00f, 0.75f, 0.775f), 0.30f, new Vector3f(1, 1, 1)), //5am
//										new DirectionalLight(new Vector3f(1.00f, 0.75f, 0.775f), 0.40f, new Vector3f(1, 1, 1)), //6am
//											new DirectionalLight(new Vector3f(1.00f, 0.75f, 0.775f), 0.65f, new Vector3f(1, 1, 1)), //7am
//												new DirectionalLight(new Vector3f(1.00f, 0.75f, 0.75f), 0.750f, new Vector3f(1, 1, 1)), //8am
//													new DirectionalLight(new Vector3f(1.00f, 0.75f, 0.75f), 0.750f, new Vector3f(1, 1, 1)), //9am
//														new DirectionalLight(new Vector3f(1.00f, 0.75f, 0.75f), 0.800f, new Vector3f(1, 1, 1)), //10am
//															new DirectionalLight(new Vector3f(1.00f, 0.75f, 0.75f), 0.900f, new Vector3f(1, 1, 1)), //11am
//				new DirectionalLight(new Vector3f(1.00f, 0.75f, 0.725f), 1.00f, new Vector3f(1, 1, 1)), //12pm
//					new DirectionalLight(new Vector3f(1.00f, 0.75f, 0.725f), 1.00f, new Vector3f(1, 1, 1)), //13pm
//						new DirectionalLight(new Vector3f(1.00f, 0.75f, 0.725f), 1.00f, new Vector3f(1, 1, 1)), //14pm
//							new DirectionalLight(new Vector3f(1.00f, 0.75f, 0.725f), 0.90f, new Vector3f(1, 1, 1)), //15pm
//								new DirectionalLight(new Vector3f(1.00f, 0.75f, 0.75f), 0.80f, new Vector3f(1, 1, 1)), //16pm
//									new DirectionalLight(new Vector3f(1.00f, 0.75f, 0.75f), 0.750f, new Vector3f(1, 1, 1)), //17pm
//										new DirectionalLight(new Vector3f(1.00f, 0.75f, 0.75f), 0.650f, new Vector3f(1, 1, 1)), //18pm
//											new DirectionalLight(new Vector3f(1.00f, 0.75f, 0.75f), 0.50f, new Vector3f(1, 1, 1)), //19pm
//												new DirectionalLight(new Vector3f(1.00f, 0.75f, 0.775f), 0.25f, new Vector3f(1, 1, 1)), //20pm
//													new DirectionalLight(new Vector3f(1.00f, 0.75f, 0.775f), 0.25f, new Vector3f(1, 1, 1)), //21pm
//														new DirectionalLight(new Vector3f(1.00f, 0.75f, 0.775f), 0.25f, new Vector3f(1, 1, 1)), //22pm
//															new DirectionalLight(new Vector3f(1.00f, 0.75f, 0.775f), 0.25f, new Vector3f(1, 1, 1)), //23pm
//		};
		
		this.currentLight = new DirectionalLight(new Vector3f(1.00f, 0.75f, 0.80f), 0.250f, new Vector3f(1, 1, 1));
	}
	
	public DirectionalLight getLight() {
		return currentLight;
	}
	
	public void setInGameMinute(long inGameMinute) {
		this.inGameMinute = inGameMinute;
	}
	
	private int fps = 0;
	private long start = 0;
	private int Samt = 0;
	private int StotFps = 0;
	private int SavFps = 0;
	private int amt = 0;
	private int totFps = 0;
	public int averageFps = 0;
	public int peakFps = 0;
	public int minFps = 10000;
	public void tick(float delta) {
		if(start == 0) start = System.currentTimeMillis();
		
		int temp = (int)(1.0f / delta);
		
		if(temp < 250000) this.fps = temp;
		
		Samt++;
		StotFps += fps;
		
		if(System.currentTimeMillis() - start >= 1000l) {
			SavFps = (int)((float)StotFps / (float)Samt);
			Samt = 0;
			StotFps = 0;
			start = System.currentTimeMillis();
			Display.setTitle("FPS: " + SavFps + "  -  Survival Island");
			
			if(SavFps > peakFps) {
				peakFps = SavFps;
			} else if(SavFps < minFps && SavFps > 1) {
				minFps = SavFps;
			}
			
			amt++;
			totFps += SavFps;;
			averageFps = (int)((float)totFps / (float)amt);
		}
	}
}
