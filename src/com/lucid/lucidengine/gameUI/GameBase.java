package com.lucid.lucidengine.gameUI;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

import com.lucid.lucidengine.gui.Test;
import com.lucid.lucidengine.main.*;
import com.lucid.lucidengine.mathematics.Quaternion;
import com.lucid.lucidengine.mathematics.Vector3f;
import com.lucid.lucidengine.object.Map;
import com.lucid.lucidengine.object.MapObject;
import com.lucid.lucidengine.object.MeshObject;
import com.lucid.lucidengine.object.Terrain;
import com.lucid.lucidengine.object.lights.DirectionalLight;
import com.lucid.lucidengine.object.lights.PointLight;
import com.lucid.lucidengine.physics.AxisAlignedBoundingBox;
import com.lucid.lucidengine.physics.BoundingEllipsiod;
import com.lucid.lucidengine.physics.BoundingSphere;
import com.lucid.lucidengine.physics.CollisionAttributes;
import com.lucid.lucidengine.physics.MeshCollider;
import com.lucid.lucidengine.physics.PhysicsObject;
import com.lucid.lucidengine.physics.Raycast;
import com.lucid.lucidengine.physics.BoundingMesh;
import com.lucid.lucidengine.rendering.Material;
import com.lucid.lucidengine.rendering.Texture;
import com.lucid.lucidengine.rendering.mesh.MeshRenderer;
import com.lucid.lucidengine.rendering.mesh.StaticMesh;
import com.lucid.lucidengine.ui.*;

/**
 * @author Mark Diedericks
 *
 */

public class GameBase {
	
	protected EngineCore engineCore;
	
	public static GameBase instance;
	private Map world;
	
	private Raycast raycast;
	private MeshObject cube1;
	private MeshRenderer playerMesh;
	
	public GameBase() {
		this.engineCore = new EngineCore(this);
		this.initGameBasics();
		this.run();
	}
	
	public static void main(String[] args) {
		EngineWindow.createWindow(EngineWindow.res_720p, "Lucid Engine");
		instance = new GameBase();
	}
	
	protected void initGameBasics() {
		world = initializeMap();
		System.out.println("Polycount is: " + EngineCore.polycount);
	}
	
	public Terrain terrain;
	public Map initializeMap() {
		Camera camera = new Camera((float)Math.toRadians(EngineSettings.fov), (1920.0f / 1080.0f), 0.1f, 1000f);
		Player player = new Player(camera, "marko5049", new Vector3f(0.125f, 0.9f, 0.125f));
		GamePerspective perspective = new GamePerspective(player);
		player.getTransform().getPos().setVector(24f, 72f, 24f);
		player.addComponent(perspective);
		StaticMesh ellipsiod = new StaticMesh("/res/models/ellipsiod.obj", true);
		PhysicsObject physObj = new PhysicsObject(new AxisAlignedBoundingBox(new Vector3f(-1.5f, -2.0f, -1.5f), new Vector3f(1.5f, 2.0f, 1.5f)), new BoundingEllipsiod(new Vector3f(0.25f, 0.9f, 0.25f), new Vector3f(0f, 0f, 0f)), new Vector3f(24, 72, 24), new Vector3f(0, 0, 0), engineCore.getPhysicsEngine().getUniqueDynamicPhysicsID());
		physObj.setScale(new Vector3f(1.0f, 1f, 1.0f));
		physObj.setCustomCollision(perspective);
		player.setCollider(new MeshCollider(false, physObj, new CollisionAttributes(), player.getTransform().getPos()));
		
		playerMesh = new MeshRenderer(ellipsiod, new Material(new Texture("/res/textures/null.png"), new Vector3f(1, 1, 1), 2, 8));
		
		initCoreVariables(player);
		MapObject mapobj = new MapObject();
		
		terrain = new Terrain(new Material(new Texture("/res/textures/grass9.JPG"), new Vector3f(1, 1, 1), 1f, 8f)).getFormHeightMap("/res/terrain/tech.png", 127.0f, engineCore.getRenderingEngine());
		terrain.getTransform().getPos().setVector(-256f, 0f, -256f);
		
		DirectionalLight dirLight = new DirectionalLight(new Vector3f(1f, 1f, 1f), 5.0f, new Vector3f(1.0f, 1.0f, 1.0f));
		dirLight.getTransform().getPos().setVector(0f, terrain.getHeightAt(256, 256) + 8f, 8f);
		dirLight.getTransform().setRotation(new Quaternion(new Vector3f(1f, 1f, 1f), (float)Math.toRadians(45f)));
		
		//StaticMesh mesh2 = new StaticMesh(new Vertex[] {new Vertex(new Vector3f(-1, 1, 0)), new Vertex(new Vector3f(-1, -1, 0)), new Vertex(new Vector3f(1, 1, 0))}, new int[] {0, 1, 2}, true);
		StaticMesh mesh2 = new StaticMesh("/res/models/cube.obj", true);
		MeshRenderer cubeRender = new MeshRenderer(mesh2, new Material(new Texture("/res/textures/pattern.png"), new Vector3f(1, 1, 1), 1f, 1f));
		CollisionAttributes cubeAttrib = new CollisionAttributes();
		MeshCollider cubeCollide = new MeshCollider(true, new PhysicsObject(new AxisAlignedBoundingBox(new Vector3f(-1.75f, -1.75f, -1.75f), new Vector3f(1.75f, 1.75f, 1.75f)), new BoundingMesh(mesh2), new Vector3f(42f, 72f, 42f), new Vector3f(0, 0, 0), engineCore.getPhysicsEngine().getUniqueStaticPhysicsID()), cubeAttrib, new Vector3f(42f, 65f, 42f));
		cube1 = new MeshObject(cubeRender, cubeCollide);
		cube1.getTransform().getPos().setVector(42f, 65f, 42f);
//		cube1.getTransform().getScale().setVector(0.01f, 0.01f, 0.01f);
		cube1.getTransform().setRotation(new Quaternion(new Vector3f(1f, 0f, 0f), (float)Math.toRadians(-90f)));
		
		this.raycast = new Raycast(camera);
		
		player.setTerrainCollision(true, terrain);
		player.setGravity(-9.8f);
		
		player.getTransform().getPos().setVector(24f, 72f, 24f);
		
		mapobj.addComponent(dirLight);
		mapobj.addComponent(terrain);
		mapobj.addComponent(playerMesh);
		
		engineCore.getGuiEngine().setFocusedGui(new Test(engineCore));
		
		Map baseMap = new Map("BASEMAP");
		baseMap.setEngineCore(engineCore);
		baseMap.addChild(mapobj);
		baseMap.addChild(player);
		baseMap.addChild(cube1);
		baseMap.setEngineCore(engineCore);
		
		return baseMap;
	}
	
	protected void initCoreVariables(Player player) {
		engineCore.setPlayer(player);
		engineCore.getRenderingEngine().setCamera(player.getCamera());
		EngineCore.ingame = true;
	}
	
	protected void initGameWindow() {
		try {
			Display.setDisplayMode(EngineWindow.res_720p.getMode());
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	//TODO: TEMPORARY FOR MY GAME
	private void updatePlayerCoords() {
		float size = terrain.getSize() / 2.0f;
		if(engineCore.getPlayer().getTransform().getPos().getX() < -size + 1)
			engineCore.getPlayer().getTransform().getPos().setX(-size + 1);
		if(engineCore.getPlayer().getTransform().getPos().getX() > size - 1)
			engineCore.getPlayer().getTransform().getPos().setX(size - 1);
		if(engineCore.getPlayer().getTransform().getPos().getZ() < -size + 1)
			engineCore.getPlayer().getTransform().getPos().setZ(-size + 1);
		if(engineCore.getPlayer().getTransform().getPos().getZ() > size - 1)
			engineCore.getPlayer().getTransform().getPos().setZ(size - 1);
		
		playerMesh.getTransform().getPos().set(this.engineCore.getPlayer().getTransform().getPos());
	}
	
	private void print() {
//		
//		this.raycast.setPosition(engineCore.getPlayer().getCamera().getTransform().getPos());
//		this.raycast.setCamera(engineCore.getRenderingEngine().getCamera());
//		this.raycast.update();
//		Vector3f point = this.raycast.getIntersectionPoint(this.terrain, 100, 1000.0f);
//		if(!point.equals(this.raycast.getPosition()))
//			this.cube1.getTransform().getPos().set(point);
//		
	}
	
	
	public void run() {
		while(!EngineWindow.isCloseRequested()) {
			engineCore.profilerTotal.startInvocation();
			engineCore.tick();
			updatePlayerCoords();
			engineCore.run();
			engineCore.profilerTotal.stopInvocation();
			print();
		}
		engineCore.getGuiEngine().getInputThread().stop();
		System.out.println("Shutting down Lucid Engine, FPS: Min: " + engineCore.getLightCycle().minFps + "    Max: " + engineCore.getLightCycle().peakFps + "    Average: " + engineCore.getLightCycle().averageFps);
		engineCore.isRunning = false;
		EngineWindow.destroy();
		System.exit(0);
	}
	
	public void update(float delta) {
		engineCore.getLevelManager().addMap(world);
		world.update(delta);
	}
	
	public void postUpdate(float delta) {
		world.postUpdate(delta);
	}
	
	public synchronized Map getMap() {
		return world;
	}
}
