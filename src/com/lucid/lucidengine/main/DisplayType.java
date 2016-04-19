package com.lucid.lucidengine.main;

import org.lwjgl.opengl.DisplayMode;

/**
 * @author Mark Diedericks
 *
 */

public class DisplayType {
	
	private DisplayMode displayMode;
	private int width;
	private int height;
	
	public DisplayType(int width, int height) {
		this.width = width;
		this.height = height;
		this.displayMode = new DisplayMode(width, height);
	}
	
	public DisplayMode getMode() {
		return this.displayMode;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
