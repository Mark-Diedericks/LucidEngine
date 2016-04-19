package com.lucid.lucidengine.gui.components;

import org.lwjgl.input.Keyboard;

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

public class GuiTextBox {

	private Texture texture;
	private Transform transform;
	private StaticMesh staticMesh;
	
	private int x;
	private int y;
	private int width;
	private int height;
	
	private float scale = 1.0f;
	private StringBuilder text;
	private String textString = "";
	private int charsInWidth;
	
	private boolean focused = false;
	private boolean isVisible = true;
	
	public GuiTextBox(GuiEngine guiEngine, Texture texture, int x, int y, int width, int height) {
		this.texture = texture;
		this.transform = new Transform();
		this.staticMesh = new StaticMesh(guiEngine.createRectangleVertices(width, height), guiEngine.createRectangleIndices(), false);
		this.text = new StringBuilder();
		this.charsInWidth = guiEngine.getTextRenderer().charsInWidth(this.height-18, this.width-10)-1;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public GuiTextBox(GuiEngine guiEngine, String texture, int x, int y, int width, int height) {
		this(guiEngine, new Texture(texture), x, y, width, height);
	}
	
	public void render(EngineCore engineCore, GameBase gamebase, Player player, boolean inGame, int width, int height, float aspectRatio) {
		if(!isVisible) return;
		transform.getPos().setVector(x, y, 1.0f);
		transform.getScale().setVector(scale, scale, scale);
		engineCore.getGuiEngine().drawComponent(texture, staticMesh, transform);
		textString = text.toString();
		charsInWidth = engineCore.getGuiEngine().getTextRenderer().charsInWidth(this.height-18, this.width-10)-1;
		if(textString.length() > charsInWidth){
			textString = textString.substring(0, charsInWidth);
		}
		engineCore.getGuiEngine().getTextRenderer().renderString(textString, x + 5, y + 9, this.height-18);
	}
	
	public void keyPressed(int keyCode, char keyChar) {
		if(text.length() < charsInWidth && this.focused) {
			if(keyCode == Keyboard.KEY_DELETE) setText(this.text.substring(0, this.text.length()-1));
			if(keyCode != Keyboard.KEY_RETURN) this.text.append(keyChar);
		}
	}
	
	public Button isClicked(int button, int x, int y) {
		if(!isVisible) return Button.NONE;
		if(x > this.x*scale && y < (EngineWindow.height - this.y*scale) && x < (this.x + this.width)*scale && y > (EngineWindow.height - (this.y + this.height)*scale)) {
			if(Button.valueOf(button) == Button.LEFT) this.focused = true;
			return Button.valueOf(button);
		}
		this.focused = false;
		return Button.NONE;
	}
	
	public String getText() {
		return this.textString;
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
		this.text = new StringBuilder();
		this.text.append(string);
	}
}
