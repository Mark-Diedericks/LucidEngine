package com.lucid.lucidengine.gui.components;

import com.lucid.lucidengine.gameUI.GameBase;
import com.lucid.lucidengine.gui.GuiEngine;
import com.lucid.lucidengine.main.EngineCore;
import com.lucid.lucidengine.mathematics.Transform;
import com.lucid.lucidengine.rendering.Texture;
import com.lucid.lucidengine.rendering.mesh.StaticMesh;
import com.lucid.lucidengine.ui.Player;

/**
 * @author Mark Diedericks
 *
 */

public class GuiTexture {
	
	private Texture texture;
	private Transform transform;
	private StaticMesh staticMesh;
	
	private int x;
	private int y;
	
	private float scale = 1.0f;
	
	private boolean isVisible = true;
	
	public GuiTexture(GuiEngine guiEngine, Texture texture, int x, int y, int width, int height) {
		this.texture = texture;
		this.transform = new Transform();
		this.staticMesh = new StaticMesh(guiEngine.createRectangleVertices(width, height), guiEngine.createRectangleIndices(), false);
		this.x = x;
		this.y = y;
	}
	
	public GuiTexture(GuiEngine guiEngine, String texture, int x, int y, int width, int height) {
		this(guiEngine, new Texture(texture), x, y, width, height);
	}
	
	public void render(EngineCore engineCore, GameBase gamebase, Player player, boolean inGame, int width, int height, float aspectRatio) {
		if(!isVisible) return;
		transform.getPos().setVector(x, y, 1.0f);
		transform.getScale().setVector(scale, scale, scale);
		engineCore.getGuiEngine().drawComponent(texture, staticMesh, transform);
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}

	public void setSize(GuiEngine guiEngine, int width, int height) {
		this.staticMesh = new StaticMesh(guiEngine.createRectangleVertices(width, height), guiEngine.createRectangleIndices(), false);
	}
	
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
}
