package com.lucid.lucidengine.gui;

/**
 * @author Mark Diedericks
 *
 */

public enum Button {
	
	NONE(-1),
	LEFT(0),
	RIGHT(1),
	MIDDLE(2);
	
	private int value;
	
	private Button(int value) {
		this.value = value;
	}
	
	public int getButton() {
		return this.value;
	}
	
	public static Button valueOf(int button) {
		if(button == 0) return LEFT;
		else if(button == 1) return RIGHT;
		else if (button == 2) return MIDDLE;
		else return NONE;
	}
	
}
