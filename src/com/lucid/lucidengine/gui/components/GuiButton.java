package com.lucid.lucidengine.gui.components;

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

public class GuiButton {
	
	private Texture texture;
	private Transform transform;
	private StaticMesh staticMesh;
	
	private int x;
	private int y;
	private int width;
	private int height;
	
	private float scale = 1.0f;
	private String text = "";
	
	private boolean isVisible = true;
	
	public GuiButton(GuiEngine guiEngine, Texture texture, int x, int y, int width, int height) {
		this.texture = texture;
		this.transform = new Transform();
		this.staticMesh = new StaticMesh(guiEngine.createRectangleVertices(width, height), guiEngine.createRectangleIndices(), false);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public GuiButton(GuiEngine guiEngine, String texture, int x, int y, int width, int height) {
		this(guiEngine, new Texture(texture), x, y, width, height);
	}
	
	public void render(EngineCore engineCore, GameBase gamebase, Player player, boolean inGame, int width, int height, float aspectRatio) {
		if(!isVisible) return;
		transform.getPos().setVector(x, y, 1.0f);
		transform.getScale().setVector(scale, scale, scale);
		engineCore.getGuiEngine().drawComponent(texture, staticMesh, transform);
		
		engineCore.getGuiEngine().getTextRenderer().renderString(text, x + engineCore.getGuiEngine().getTextRenderer().getCenteredStart(text, this.width, this.height-18), y + 9, this.height-18);
	}
	
	public Button isClicked(int button, int x, int y) {
		if(!isVisible) return Button.NONE;
		if(x > this.x*scale && y < (EngineWindow.height - this.y*scale) && x < (this.x + this.width)*scale && y > (EngineWindow.height - (this.y + this.height)*scale)) {
			return Button.valueOf(button);
		}
		
		return Button.NONE;
	}
	
	public String getText() {
		return this.text;
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setSize(GuiEngine guiEngine, int width, int height) {
		this.width = width;
		this.height = height;
		this.staticMesh = new StaticMesh(guiEngine.createRectangleVertices(width, height), guiEngine.createRectangleIndices(), false);
	}
	
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public void setText(String string) {
		this.text = string;
	}
}
