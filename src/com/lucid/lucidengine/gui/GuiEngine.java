package com.lucid.lucidengine.gui;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL32;

import com.lucid.lucidengine.gameUI.GameBase;
import com.lucid.lucidengine.main.EngineCore;
import com.lucid.lucidengine.main.EngineWindow;
import com.lucid.lucidengine.mathematics.Matrix4f;
import com.lucid.lucidengine.mathematics.Transform;
import com.lucid.lucidengine.mathematics.Vector2f;
import com.lucid.lucidengine.mathematics.Vector3f;
import com.lucid.lucidengine.mathematics.Vertex;
import com.lucid.lucidengine.rendering.Material;
import com.lucid.lucidengine.rendering.Texture;
import com.lucid.lucidengine.rendering.lighting.Shader;
import com.lucid.lucidengine.rendering.mesh.StaticMesh;
import com.lucid.lucidengine.ui.Camera;
import com.lucid.lucidengine.ui.Player;

/**
 * @author Mark Diedericks
 *
 */

public class GuiEngine {
	
	private EngineCore engineCore;
	private TextRenderer textRenderer;
	
	private ArrayList<GuiScreen> guis;
	
	private GuiScreen focusedGui;
	
	private Camera camera;
	private Shader shader;
	private Material material;
	
	private GuiInputThread thread;
	
	public GuiEngine(EngineCore engineCore) {
		this.engineCore = engineCore;
		this.guis = new ArrayList<GuiScreen>();
		this.textRenderer = new TextRenderer(this);
		
		this.camera = new Camera(50, 1920.0f/1080.0f, -1f, 10f);
		this.camera.setProjection(new Matrix4f().initOthographic(0, EngineWindow.width, EngineWindow.height, 0, -1f, 10f));
		
		this.material = new Material(engineCore.getRenderingEngine().getTexture("diffuse"), new Vector3f(1f, 1f, 1f), 1f, 1f);
		
		this.thread = new GuiInputThread(engineCore);
	}
	
	public void update(int width, int height) {
		for(GuiScreen gui : guis) {
			gui.update(width, height);
		}
		
		if(focusedGui != null) focusedGui.update(width, height);
	}
	
	public void render(EngineCore engineCore, Shader shader, GameBase gamebase, Player player, boolean inGame, int width, int height, float aspectRatio) {
		this.shader = shader;
		
		for(GuiScreen gui : guis) {
			if(gui.TYPE == 0 && inGame) { //HUD
				gui.render(engineCore, gamebase, player, inGame, width, height, aspectRatio);
			}
		}
		if(focusedGui == null) return;
		if(focusedGui.TYPE == 1 && !inGame) { //MENU
			focusedGui.update(width, height);
			focusedGui.render(engineCore, gamebase, player, inGame, width, height, aspectRatio);
		}
	}
	
	public void drawComponent(Texture texture, StaticMesh staticMesh, Transform transform) {
		EngineWindow.bindAsRenderTarget();
		glDisable(GL_CULL_FACE);
		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_TEXTURE_2D);
		glDisable(GL_DEPTH_TEST);
		glEnable(GL32.GL_DEPTH_CLAMP);
		glDepthMask(false);
		
		shader.bind();
		material.setTexture("diffuse", texture);
		shader.updateUniforms(transform, this.engineCore.getRenderingEngine(), material, camera);
		staticMesh.draw();
		
		glEnable(GL_CULL_FACE);
		glDisable(GL_BLEND);
		glEnable(GL_DEPTH_TEST);
		glDepthMask(true);
	}
	
	public Vertex[] createRectangleVertices(float width, float height) {
		return new Vertex[] {new Vertex(new Vector3f(width, height, 0.1f), new Vector2f(1f, 1f)),
				new Vertex(new Vector3f(width, 0, 0.1f), new Vector2f(1f, 0)),
				new Vertex(new Vector3f(0, height, 0.1f), new Vector2f(0, 1f)),
				new Vertex(new Vector3f(0, 0, 0.1f), new Vector2f(0, 0))};
	}
	
	public int[] createRectangleIndices() {
		return new int[] {0, 1, 2, 2, 1, 3};
	}
	
	public void addGui(GuiScreen gui) {
		this.guis.add(gui);
	}
	
	public void setFocusedGui(GuiScreen gui) {
		this.focusedGui = gui;
	}
	
	public TextRenderer getTextRenderer() {
		return this.textRenderer;
	}
	
	public EngineCore getEngineCore() {
		return this.engineCore;
	}
	
	public GuiInputThread getInputThread() {
		return thread;
	}
	
	public void updateInput() {
		while(Mouse.next()) {
			mouseClicked();
		}
		while(Keyboard.next()) {
			keyTyped();
		}
	}
	
	private void keyTyped() {
		if(focusedGui == null) return;
		if(Keyboard.getEventKeyState()) {
			this.focusedGui.keyPressed(Keyboard.getEventKey(), Keyboard.getEventCharacter());
		}
	}
	
	private void mouseClicked() {
		if(focusedGui == null) return;
		if(Mouse.getEventButtonState()) {
			this.focusedGui.mouseClicked(Mouse.getEventButton(), Mouse.getEventX(), Mouse.getEventY());
		}
	}
}
