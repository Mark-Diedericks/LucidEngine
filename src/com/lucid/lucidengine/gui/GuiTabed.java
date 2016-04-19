package com.lucid.lucidengine.gui;

import org.lwjgl.input.Keyboard;

import com.lucid.lucidengine.gameUI.GameBase;
import com.lucid.lucidengine.gui.components.GuiButton;
import com.lucid.lucidengine.main.EngineCore;
import com.lucid.lucidengine.main.EngineWindow;
import com.lucid.lucidengine.ui.Player;

/**
 * @author Mark Diedericks
 *
 */

public class GuiTabed extends GuiScreen {
	
	private int currentTab = 0;
	
	private GuiButton[] buttons;
	private GuiScreen[] guis;
	
	public GuiTabed(EngineCore engineCore, int type, GuiButton[] buttons, GuiScreen[] guis) {
		super(engineCore, type);
		this.guis = guis;
		
		int buttonWidth = (int)((float)EngineWindow.width-20 / (float)buttons.length);
		for(int i = 1; i <= buttons.length; i++) {
			if(buttons[i] != null) {
				buttons[i].setPosition(i*buttonWidth+10, 10);
				buttons[i].setSize(engineCore.getGuiEngine(), buttonWidth, 50);
			}
		}
	}
	
	@Override
	public void update(int width, int height) {
		if(this.guis[this.currentTab] != null) this.guis[this.currentTab].update(width, height);
	}
	
	@Override
	public void render(EngineCore engineCore, GameBase gamebase, Player player, boolean inGame, int width, int height, float aspectRatio) {
		super.render(engineCore, gamebase, player, inGame, width, height, aspectRatio);
		for(int i = 1; i <= buttons.length; i++) {
			if(buttons[i] != null) {
				buttons[i].render(engineCore, gamebase, player, inGame, width, height, aspectRatio);
			}
		}
		if(this.guis[this.currentTab] != null) this.guis[this.currentTab].render(engineCore, gamebase, player, inGame, width, height, aspectRatio);
	}
	
	@Override
	public void keyPressed(int keyCode, char keyChar) {
		if(keyCode == Keyboard.KEY_TAB && currentTab < buttons.length) {
			currentTab++;
		}
		if(this.guis[this.currentTab] != null) this.guis[this.currentTab].keyPressed(keyCode, keyChar);
	}
	
	@Override
	public void mouseClicked(int button, int x, int y) {
		for(int i = 1; i <= buttons.length; i++) {
			if(buttons[i] != null) {
				if(buttons[i].isClicked(button, x, y) == Button.LEFT) {
					this.currentTab = i;
				}
			}
		}
		if(this.guis[this.currentTab] != null) this.guis[this.currentTab].mouseClicked(button, x, y);
	}
}
