package com.lucid.lucidengine.gui;

import com.lucid.lucidengine.main.EngineCore;

/**
 * @author Mark Diedericks
 *
 */

public class GuiInputThread extends Thread {
	
	private EngineCore engineCore;
	public GuiInputThread(EngineCore engineCore) {
		this.engineCore = engineCore;
		start();
	}
	
	@Override
	public void run() {
		while(this.engineCore.isRunning) {
			this.engineCore.getGuiEngine().updateInput();
		}
		System.out.println("Disposing of gui input thread!");
		System.exit(0);
	}
}
