package com.lucid.lucidengine.main;

import org.lwjgl.input.Keyboard;

import com.lucid.lucidengine.rendering.RenderingEngine;

/**
 * @author Mark Diedericks
 *
 */

public class EngineSettings {
	
	/*
	 * Sound
	 */
	public static float MusicVolume = 1.0F;
	public static float SFxVolume = 1.0F;
	public static float CinematicVolume = 1.0F;
	
	
	
	/*
	 * Controls
	 */
	public static float sensitivity = 0.5F;
	public static float fov = 50f;
	
	//Mouse & Keyboard
	public static int forward = Keyboard.KEY_W;
	public static int backward = Keyboard.KEY_S;
	public static int left = Keyboard.KEY_A;
	public static int right = Keyboard.KEY_D;
	public static int sprint = Keyboard.KEY_LSHIFT;
	public static int sneak = Keyboard.KEY_LCONTROL;
	public static int jump = Keyboard.KEY_SPACE;
	
	//XBox Controller
	public static String X = "Button 2";
	public static String Y = "Button 3";
	public static String B = "Button 1";
	public static String A = "Button 0";
	public static String BACK = "Button 6";
	public static String START = "Button 7";
	public static String UP = "U";
	public static String DOWN = "D";
	public static String LEFT = "L";
	public static String RIGHT = "R";
	public static String LEFT_SHOULDER = "Button 4";
	public static String RIGHT_SHOULDER = "Button 5";
	public static String LEFT_TRIGGER = "Z Axis";
	public static String RIGHT_TRIGGER = "Z Axis";
	public static String LEFT_STICK = "Button 8";
	public static String RIGHT_STICK = "Button 9";
	public static String LEFT_X = "X Axis";
	public static String LEFT_Y = "Y Axis";
	public static String RIGHT_X = "X Rotation";
	public static String RIGHT_Y = "Y Rotation";
	public static float POV_CENTER = 0.0f;
	public static float POV_UP = 0.25f;
	public static float POV_RIGHT = 0.5f;
	public static float POV_DOWN = 0.75f;
	public static float POV_LEFT = 1.0f;
	public static float POV_UP_RIGHT = 0.375f;
	public static float POV_UP_LEFT = 0.125f;
	public static float POV_DOWN_RIGHT = 0.625f;
	public static float POV_DOWN_LEFT = 0.875f;
	
	
	/*
	 * Rendering
	 */
	public static int PostRenderingFilter = RenderingEngine.FILTER_FXAA;
	public static boolean vSync = false;
	public static boolean fullscreen = false;
	public static boolean godrays = true;
}
