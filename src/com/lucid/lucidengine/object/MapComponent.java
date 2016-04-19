package com.lucid.lucidengine.object;

import com.lucid.lucidengine.main.EngineCore;
import com.lucid.lucidengine.mathematics.Transform;
import com.lucid.lucidengine.rendering.RenderingEngine;
import com.lucid.lucidengine.rendering.lighting.Shader;
import com.lucid.lucidengine.ui.Camera;

/**
 * @author Mark Diedericks
 *
 */

public abstract class MapComponent {
	
	private MapObject parent;
	private Transform transform;
	private int id;
	
	public MapComponent() {
		super();
		transform = new Transform();
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public MapObject getParent() {
		return parent;
	}
	
	public void setParent(MapObject parent) {
		this.parent = parent;
	}
	
	public Transform getTransform() {
		return transform;
	}
	
	public void setTransform(Transform transform) {
		this.transform = transform;
	}
	
	public void input() {}
	public void update(float delta) {}
	public void postUpdate(float delta) {}
	public void render(Shader shader, RenderingEngine renderingEngine, Camera camera) {}
	public void renderPost(Shader shader, RenderingEngine renderingEngine, Camera camera) {}
	
	public void addToEngineCore(EngineCore engineCore) {}
}
