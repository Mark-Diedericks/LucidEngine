package com.lucid.lucidengine.gui;

import java.util.ArrayList;

import com.lucid.lucidengine.gameUI.GameBase;
import com.lucid.lucidengine.gui.components.*;
import com.lucid.lucidengine.main.EngineCore;
import com.lucid.lucidengine.main.EngineWindow;
import com.lucid.lucidengine.ui.Player;

/**
 * @author Mark Diedericks
 *
 */

public class Test extends GuiScreen {
	
	private GuiScrollList scrollList;
	private GuiDropDown dropDown;
	private GuiButton button;
	private GuiTextBox textBox;
	private GuiTexture background;
	
	public Test(EngineCore engineCore) {
		super(engineCore, 1);
		background = new GuiTexture(engineCore.getGuiEngine(), "/res/textures/grass9.JPG", 0, 0, EngineWindow.width, EngineWindow.height);
		
		GuiButton button = new GuiButton(engineCore.getGuiEngine(), "/res/textures/button.png", 0, 0, 100, 50);
		button.setText("<");
		
		GuiButton button2 = new GuiButton(engineCore.getGuiEngine(), "/res/textures/button.png", 0, 0, 100, 50);
		button2.setText(">");
		
		ArrayList<String> options = new ArrayList<String>();
		options.add("low");
		options.add("medium");
		options.add("high");
		options.add("extreme");
		options.add("ultra");
		
		this.scrollList = new GuiScrollList(engineCore.getGuiEngine(), options, "/res/textures/dirt1.jpg", 200, 200, 200, 50, button, button2);
		
		this.button = new GuiButton(engineCore.getGuiEngine(), "/res/textures/button.png", 800, 200, 200, 50);
		this.button.setText("EXIT");
		
		GuiButton button3 = new GuiButton(engineCore.getGuiEngine(), "/res/textures/button.png", 0, 0, 100, 50);
		button3.setText("Hello");
		
		GuiButton button4 = new GuiButton(engineCore.getGuiEngine(), "/res/textures/button.png", 0, 0, 100, 50);
		button4.setText("Goodbye");
		
		ArrayList<GuiButton> buttons = new ArrayList<GuiButton>();
		buttons.add(button3);
		buttons.add(button4);
		
		this.dropDown = new GuiDropDown(engineCore.getGuiEngine(), buttons, "/res/textures/dirt1.jpg", 200, 600, 200, 50, 10);
		
		this.textBox = new GuiTextBox(engineCore.getGuiEngine(), "/res/textures/grass.png", 200, 400, 200, 50);
	}
	
	@Override
	public void update(int width, int height) {
		
	}
	
	@Override
	public void render(EngineCore engineCore, GameBase gamebase, Player player, boolean inGame, int width, int height, float aspectRatio) {
		super.render(engineCore, gamebase, player, inGame, width, height, aspectRatio);
		background.render(engineCore, gamebase, player, inGame, width, height, aspectRatio);
		button.render(engineCore, gamebase, player, inGame, width, height, aspectRatio);
		this.scrollList.render(engineCore, gamebase, player, inGame, width, height, aspectRatio);
		this.dropDown.render(engineCore, gamebase, player, inGame, width, height, aspectRatio);
		this.textBox.render(engineCore, gamebase, player, inGame, width, height, aspectRatio);
	}
	
	@Override
	public void keyPressed(int keyCode, char keyChar) {
		textBox.keyPressed(keyCode, keyChar);
	}
	
	@Override
	public void mouseClicked(int button, int x, int y) {
		if(this.button.isClicked(button, x, y) == Button.LEFT) System.exit(0);
		this.scrollList.isClicked(button, x, y);
		this.dropDown.isClicked(button, x, y);
		this.textBox.isClicked(button, x, y);
	}
}
