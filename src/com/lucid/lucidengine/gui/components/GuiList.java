package com.lucid.lucidengine.gui.components;

import java.util.ArrayList;
import com.lucid.lucidengine.gameUI.GameBase;
import com.lucid.lucidengine.gui.Button;
import com.lucid.lucidengine.gui.GuiEngine;
import com.lucid.lucidengine.main.EngineCore;
import com.lucid.lucidengine.main.EngineWindow;
import com.lucid.lucidengine.ui.Player;

/**
 * @author Mark Diedericks
 *
 */

public class GuiList {
	
	private int x;
	private int y;
	private int width;
	
	private float scale = 1.0f;
	private int valuesShown;
	private int offset = 0;
	private int selectedValue = 0;
	
	private ArrayList<GuiButton> options;
	
	private boolean isVisible = true;
	
	public GuiList(GuiEngine guiEngine, ArrayList<GuiButton> options, int x, int y, int width, int valuesShown) {
		this.x = x;
		this.y = y;
		this.options = options;
		this.valuesShown = valuesShown;
		this.width = width;
	}
	
	public void update(int width, int height) {
		if(!isVisible) return;
	}
	
	public void render(EngineCore engineCore, GameBase gamebase, Player player, boolean inGame, int width, int height, float aspectRatio) {
		if(!isVisible) return;
		
		if(offset >= options.size() - valuesShown) offset = options.size() - valuesShown - 1;
		if(offset < 0) offset = 0;
		if(valuesShown > options.size()) valuesShown = options.size();
		for(int i = offset; i < valuesShown; i++) {
			GuiButton button = options.get(i);
			if(button != null) {
				button.setScale(scale);
				button.setPosition(this.x, this.y + ((i - offset) * 32));
				button.setSize(engineCore.getGuiEngine(), this.width, 32);
				button.render(engineCore, gamebase, player, inGame, width, height, aspectRatio);
			}
		}
	}
	
	public Button isClicked(int button, int x, int y) {
		if(!isVisible) return Button.NONE;
		if(x > this.x*scale && y < (EngineWindow.height - this.y*scale) && x < (this.x + this.width)*scale && y > (EngineWindow.height - (this.y + 32*this.valuesShown)*scale)) {
			if(offset >= options.size() - valuesShown) offset = options.size() - valuesShown - 1;
			if(offset < 0) offset = 0;
			for(int i = offset; i < valuesShown; i++) {
				GuiButton buddon = options.get(i);
				if(buddon != null && buddon.isClicked(button, x, y) == Button.LEFT) {
					this.selectedValue = i;
				}
			}
			return Button.valueOf(button);
		}
		
		return Button.NONE;
	}
	
	public int getValuesShown() {
		return valuesShown;
	}
	
	public void setValuesShown(int valuesShown) {
		this.valuesShown = valuesShown;
	}
	
	public int getSelectedValue() {
		return selectedValue;
	}
	
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}
}
