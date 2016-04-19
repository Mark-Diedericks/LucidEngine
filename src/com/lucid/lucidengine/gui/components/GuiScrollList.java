package com.lucid.lucidengine.gui.components;

import java.util.ArrayList;

import com.lucid.lucidengine.gameUI.GameBase;
import com.lucid.lucidengine.gui.Button;
import com.lucid.lucidengine.gui.GuiEngine;
import com.lucid.lucidengine.main.EngineCore;
import com.lucid.lucidengine.main.EngineWindow;
import com.lucid.lucidengine.mathematics.Transform;
import com.lucid.lucidengine.rendering.Texture;
import com.lucid.lucidengine.rendering.mesh.StaticMesh;
import com.lucid.lucidengine.ui.Player;

/**
 * @author Mark Diedericks
 *
 */

public class GuiScrollList {
	
	private Texture texture;
	private Transform transform;
	private StaticMesh staticMesh;
	
	private int x;
	private int y;
	private int width;
	private int height;
	
	private float scale = 1.0f;
	private String text = "";
	
	private int selectedValue = 0;
	private ArrayList<String> options;
	
	private GuiButton left;
	private GuiButton right;
	
	private boolean isVisible = true;
	
	public GuiScrollList(GuiEngine guiEngine, ArrayList<String> options, Texture texture, int x, int y, int width, int height, GuiButton left, GuiButton right) {
		this.texture = texture;
		this.transform = new Transform();
		this.staticMesh = new StaticMesh(guiEngine.createRectangleVertices(width, height), guiEngine.createRectangleIndices(), false);
		this.options = options;
		this.text = options.get(selectedValue);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.left = left;
		this.right = right;
	}
	
	public GuiScrollList(GuiEngine guiEngine, ArrayList<String> options, String texture, int x, int y, int width, int height, GuiButton left, GuiButton right) {
		this(guiEngine, options, new Texture(texture), x, y, width, height, left, right);
	}
	
	public void update(int width, int height) {
		if(!isVisible) return;
	}
	
	public void render(EngineCore engineCore, GameBase gamebase, Player player, boolean inGame, int width, int height, float aspectRatio) {
		if(!isVisible) return;
		transform.getPos().setVector(x, y, 1.0f);
		transform.getScale().setVector(scale, scale, scale);
		engineCore.getGuiEngine().drawComponent(texture, staticMesh, transform);
		
		this.text = options.get(selectedValue);
		engineCore.getGuiEngine().getTextRenderer().renderString(text, x + engineCore.getGuiEngine().getTextRenderer().getCenteredStart(text, this.width, this.height-18), y + 9, this.height-18);
		
		left.setPosition(x-70, y);
		left.setSize(engineCore.getGuiEngine(), 50, this.height);
		left.render(engineCore, gamebase, player, inGame, width, height, aspectRatio);
		
		right.setPosition(x+this.width+20, y);
		right.setSize(engineCore.getGuiEngine(), 50, this.height);
		right.render(engineCore, gamebase, player, inGame, width, height, aspectRatio);
	}
	
	public Button isClicked(int button, int x, int y) {
		if(!isVisible) return Button.NONE;
		
		if(left.isClicked(button, x, y) == Button.LEFT) {
			if(selectedValue > 0) selectedValue--;
		}
		
		if(right.isClicked(button, x, y) == Button.LEFT) {
			if(selectedValue < options.size()-1) selectedValue++;
		}
		
		if(x > this.x*scale && y < (EngineWindow.height - this.y*scale) && x < (this.x + this.width)*scale && y > (EngineWindow.height - (this.y + this.height)*scale)) {
			return Button.valueOf(button);
		}
		
		return Button.NONE;
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
