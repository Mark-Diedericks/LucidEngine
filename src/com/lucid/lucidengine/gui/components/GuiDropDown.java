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

public class GuiDropDown {

	private Texture texture;
	private Transform transform;
	private StaticMesh staticMesh;
	
	private int x;
	private int y;
	private int width;
	private int height;
	
	private float scale = 1.0f;
	private String text;
	
	private ArrayList<GuiButton> options;
	
	boolean isExpanded = false;
	private GuiList list;
	
	private boolean isVisible = true;
	
	public GuiDropDown(GuiEngine guiEngine, ArrayList<GuiButton> options, Texture texture, int x, int y, int width, int height, int valuesShown) {
		this.texture = texture;
		this.transform = new Transform();
		this.staticMesh = new StaticMesh(guiEngine.createRectangleVertices(width, height), guiEngine.createRectangleIndices(), false);
		this.options = options;
		this.list = new GuiList(guiEngine, options, x, y+height, width, valuesShown);
		this.list.setVisible(false);
		this.text = options.get(list.getSelectedValue()).getText();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public GuiDropDown(GuiEngine guiEngine, ArrayList<GuiButton> options, String texture, int x, int y, int width, int height, int valuesShown) {
		this(guiEngine, options, new Texture(texture), x, y, width, height, valuesShown);
	}
	
	public void update(int width, int height) {
		this.list.update(width, height);
	}
	
	public void render(EngineCore engineCore, GameBase gamebase, Player player, boolean inGame, int width, int height, float aspectRatio) {
		if(!isVisible) return;
		transform.getPos().setVector(x, y, 1.0f);
		transform.getScale().setVector(scale, scale, scale);
		engineCore.getGuiEngine().drawComponent(texture, staticMesh, transform);
		this.text = this.options.get(list.getSelectedValue()).getText();
		engineCore.getGuiEngine().getTextRenderer().renderString(text, x + engineCore.getGuiEngine().getTextRenderer().getCenteredStart(text, this.width, 32f), y + 9, 32f);
		
		this.list.render(engineCore, gamebase, player, inGame, width, height, aspectRatio);
		
	}
	
	private void clicked(int button, int x, int y) {
		if(!isExpanded && (x > this.x*scale && y < (EngineWindow.height - this.y*scale) && x < (this.x + this.width)*scale && y > (EngineWindow.height - (this.y + this.height)*scale))) {
			this.isExpanded = true;
			this.list.setVisible(true);
		} else if (isExpanded) {
			if(list.isClicked(button, x, y) == Button.LEFT) {
				this.isExpanded = false;
				this.list.setVisible(false);
				System.out.println("CLICKED");
			}
		}
	}
	
	public Button isClicked(int button, int x, int y) {
		if(!isVisible) return Button.NONE;
		this.clicked(button, x, y);
		if(x > this.x*scale && y < (EngineWindow.height - this.y)*scale && x < (this.x + this.width)*scale && y > (EngineWindow.height - (this.y + this.height))*scale) {
			return Button.valueOf(button);
		}
		
		return Button.NONE;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
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
	
	public void setText(String string) {
		this.text = string;
	}
}
