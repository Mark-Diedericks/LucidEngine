package com.lucid.lucidengine.main;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.Display;

import com.lucid.lucidengine.mathematics.Util;
import com.lucid.lucidengine.mathematics.Vector2f;

/**
 * @author Mark Diedericks
 *
 */

public class EngineWindow {
	
	private static boolean forceFps = false;
	private static int FrameRate = 60;
	public static int width;
	public static int height;
	private static String icon16 = "icon16.png";
	private static String icon32 = "icon32.png";
	
	public static final DisplayType res_360p = new DisplayType(480, 360);
	public static final DisplayType res_480p = new DisplayType(720, 480);
	public static final DisplayType res_720p = new DisplayType(1280, 720);
	public static final DisplayType res_1080p = new DisplayType(1920, 1080);
	
	public static void createWindow(DisplayType dispType, String title) {
		try {
			Display.setDisplayMode(dispType.getMode());
			width = dispType.getWidth();
			height = dispType.getHeight();
			Display.setIcon(Util.getIcon(icon16, icon32));
			Display.setTitle(title);
			Display.setInitialBackground(0, 0, 0);
			Display.setVSyncEnabled(false);
			Display.setResizable(true);
			Display.create();
			Keyboard.create();
			Mouse.create();
			Mouse.setCursorPosition((int)getCenter().getX(), (int)getCenter().getY());
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}

	public static void render() {
		if(forceFps) {
			Display.sync(FrameRate);
		}
		Display.update();
		width = Display.getWidth();
		height = Display.getHeight();
	}
	
	public static void destroy() {
		Display.destroy();
		Keyboard.destroy();
		Mouse.destroy();
	}
	
	public static void bindAsRenderTarget() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL11.glViewport(0, 0, width, height);
	}
	
	public static Vector2f getCenter() {
		return new Vector2f(Display.getWidth() / 2, Display.getHeight() / 2);
	}
	
	public static boolean isCloseRequested() {
		return Display.isCloseRequested();
	}
	
	public static boolean isForceFps() {
		return forceFps;
	}
	
	public static void setForceFps(boolean forceFps) {
		EngineWindow.forceFps = forceFps;
	}

	public static int getFrameRate() {
		return FrameRate;
	}

	public static void setFrameRate(int frameRate) {
		FrameRate = frameRate;
	}
}
