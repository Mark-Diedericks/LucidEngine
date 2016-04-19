package com.lucid.lucidengine.ui;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.lucid.lucidengine.main.EngineCore;
import com.lucid.lucidengine.mathematics.Transform;
import com.lucid.lucidengine.mathematics.Vector3f;
import com.lucid.lucidengine.object.MapComponent;
import com.lucid.lucidengine.object.MapObject;
import com.lucid.lucidengine.object.Terrain;
import com.lucid.lucidengine.physics.MeshCollider;

/**
 * @author Mark Diedericks
 *
 */

public class Player extends MapObject {
	
	private Camera camera;
	private boolean terrainCollision = false;
	private float gravity = -1.0f;
	private Terrain terrain;
	private boolean isInAir = false;
	private final float EYE_OFFSET = 1.7f;
	private final String name;
	private MeshCollider collider;
	
	private Vector3f ellipsiodRadius;
	
	private Vector3f velocity;
	
	public Player(Camera camera, String name, Vector3f ellipsiodRadius) {
		this.camera = camera;
		this.name = name;
		this.velocity = new Vector3f(0, 0, 0);
		addComponent(camera);
		this.ellipsiodRadius = ellipsiodRadius;
	}
	
	public void setCamera(Camera cam) {
		this.components = new ArrayList<MapComponent>();
		this.camera = cam;
		addComponent(this.camera);
		for(MapComponent comp : this.components) comp.addToEngineCore(getEngineCore());
	}
	
	public void setCollider(MeshCollider collider) {
		this.collider = collider;
		addComponent(this.collider);
	}
	
	public Vector3f getEllipsiodRadius() {
		return ellipsiodRadius;
	}
	
	public MeshCollider getPlayerCollider() {
		return this.collider;
	}
	
	public void setGravity(float gravity) {
		this.gravity = gravity;
	}
	
	public void setTerrainCollision(boolean bool, Terrain terrain) {
		this.terrainCollision = bool;
		this.terrain = terrain;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean canDown = true;
	public boolean canUp = true;
	
	@Override
	public void update(float delta) {
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			pause(delta);
		}
		super.update(delta);
		
		if(terrainCollision && terrain != null) {
			float height = terrain.getHeightAt(getTransform().getPos().getX(), getTransform().getPos().getZ());
			if(getTransform().getPos().getY() < height) {
				this.setInAir(false);
				this.getTransform().getPos().setY(height);
			}
		}
		
		if(EngineCore.ingame) {
			Transform transformTemp = new Transform();
			
			transformTemp.getPos().set(getTransform().getPos().add(new Vector3f(0f, EYE_OFFSET, 0f)));
			transformTemp.getRotation().set(getTransform().getRotation());
			transformTemp.getScale().set(getTransform().getScale());
			
			camera.setTransform(transformTemp);
			
			getEngineCore().getRenderingEngine().setCamera(camera);
			Mouse.setGrabbed(true);
			getEngineCore().getRenderingEngine().setVector3f("C_eyePos",  transformTemp.getTransformedPos());
		} else {
			Mouse.setGrabbed(false);
		}
	}
	
	public void pause(float delta) {
		if(delta > 100) {
			EngineCore.ingame = !EngineCore.ingame;
		}
	}
	
	public void bind(Camera camera) {
		this.camera = camera;
	}
	
	public Camera getCamera() {
		return this.camera;
	}

	public boolean isInAir() {
		return isInAir;
	}

	public void setInAir(boolean isInAir) {
		this.isInAir = isInAir;
	}
	
	public float getGravity() {
		return gravity;
	}
	
	public void setVelocity(Vector3f velocity) {
		this.velocity = velocity;
	}
	
	public Vector3f getVelocity() {
		return velocity;
	}

	public float getTerrainHeightAt(float x, float z) {
		return this.terrain.getHeightAt(x, z);
	}
}