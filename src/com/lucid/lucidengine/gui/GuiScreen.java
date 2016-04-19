package com.lucid.lucidengine.gui;

import com.lucid.lucidengine.gameUI.GameBase;
import com.lucid.lucidengine.main.EngineCore;
import com.lucid.lucidengine.ui.Player;

/**
 * @author Mark Diedericks
 *
 */

public class GuiScreen {
	
	protected EngineCore engineCore;
	public final int TYPE;
	
	public GuiScreen(EngineCore engineCore, int type) {
		this.engineCore = engineCore;
		this.TYPE = type;
	}
	
	public void update(int width, int height) {}
	
	public void render(EngineCore engineCore, GameBase gamebase, Player player, boolean inGame, int width, int height, float aspectRatio) {}
	
	public void keyPressed(int keyCode, char keyChar) {}
	
	public void mouseClicked(int button, int x, int y) {}
}
