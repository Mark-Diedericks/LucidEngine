package com.lucid.lucidengine.main;

import static org.lwjgl.opengl.GL11.*;

import com.lucid.lucidengine.gameUI.*;
import com.lucid.lucidengine.gui.GuiEngine;
import com.lucid.lucidengine.object.LevelManager;
import com.lucid.lucidengine.physics.PhysicsEngine;
import com.lucid.lucidengine.rendering.*;
import com.lucid.lucidengine.rendering.lighting.LightCycle;
import com.lucid.lucidengine.ui.*;

/**
 * @author Mark Diedericks
 *
 */

public class EngineCore {
	
	private GameBase game;
	private Player player;
	private EngineProfiling profilerUpdate;
	public EngineProfiling profilerTotal;
	private RenderingEngine renderingEngine;
	private PhysicsEngine physicsEngine;
	private GuiEngine guiEngine;
	private LightCycle lightCycle;
	private LevelManager levelManager;
	public static boolean ingame = true;
	public static int polycount = 0;
	private boolean profiling = true;
	public boolean isRunning = true;
	
	public static int getWidth() {
		return EngineWindow.width;
	}
	
	public static int getHeight() {
		return EngineWindow.height;
	}
	
	public EngineCore(GameBase game) {
		this.renderingEngine = new RenderingEngine(this);
		this.physicsEngine = new PhysicsEngine(this);
		this.guiEngine = new GuiEngine(this);
		this.levelManager = new LevelManager();
		this.lightCycle = new LightCycle(0.00, 100L);
		this.profilerUpdate = new EngineProfiling("EngineCore");
		this.profilerTotal = new EngineProfiling("EngineCore");
		this.setGame(game);
	}

	public void run() {
		if(!EngineWindow.isCloseRequested() && this.game != null) {
			renderingEngine.render(this.game.getMap());
			EngineWindow.render();
		}
	}
	
	private static long startU = 0;
	private static long startI = 0;
	private static long end = 0;
	public void tick() {
		if(startU == 0) startU = System.currentTimeMillis();
		profilerUpdate.startInvocation();
		
		float delta = (System.currentTimeMillis() - startU) / 1000.0f;
		startU = System.currentTimeMillis();
		
		lightCycle.tick(delta);
		
		this.game.update(delta);
		this.physicsEngine.Simulate(delta);
		this.game.postUpdate(delta);
		
		this.guiEngine.update(EngineWindow.width, EngineWindow.height);
		
		profilerUpdate.stopInvocation();
		
		if(end - startI >= 1000.0) {
			startI = System.currentTimeMillis();
			if(profiling) {
				renderingEngine.DisplayProfilingInfo(1);
				profilerUpdate.displayAndReset("Update Time: ", 1);
				profilerTotal.displayAndReset("Total Time: ", 1);
			}
		}
		
		renderingEngine.ResetProfilingInfo();
		profilerUpdate.reset();
		profilerTotal.reset();
	}
	
	public static long getDelta() {
		return end - startU;
	}

	public LightCycle getLightCycle() {
		return lightCycle;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void setIngame(boolean ingame) {
		EngineCore.ingame = ingame;
	}

	public GameBase getGame() {
		return game;
	}

	public void setGame(GameBase game) {
		this.game = game;
	}
	
	public LevelManager getLevelManager() {
		return levelManager;
	}

	public void setLevelManager(LevelManager levelManager) {
		this.levelManager = levelManager;
	}

	public RenderingEngine getRenderingEngine() {
		return renderingEngine;
	}
	
	public void setRenderingEngine(RenderingEngine renderingEngine) {
		this.renderingEngine = renderingEngine;
	}
	
	public PhysicsEngine getPhysicsEngine() {
		return physicsEngine;
	}
	
	public void setPhysicsEngine(PhysicsEngine physicsEngine) {
		this.physicsEngine = physicsEngine;
	}

	public GuiEngine getGuiEngine() {
		return guiEngine;
	}

	public void setGuiEngine(GuiEngine guiEngine) {
		this.guiEngine = guiEngine;
	}
	
	public static void setWireFrame(boolean wireFrame) {
		if(wireFrame) glPolygonMode(GL_FRONT_AND_BACK, GL_LINE); else glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
	}

	public boolean isProfiling() {
		return profiling;
	}

	public void setProfiling(boolean profiling) {
		this.profiling = profiling;
	}
}
