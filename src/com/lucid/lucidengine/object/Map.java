package com.lucid.lucidengine.object;

import java.util.ArrayList;

import com.lucid.lucidengine.main.EngineCore;
import com.lucid.lucidengine.mathematics.Transform;
import com.lucid.lucidengine.rendering.RenderingEngine;
import com.lucid.lucidengine.rendering.lighting.Shader;
import com.lucid.lucidengine.ui.Camera;

/**
 * @author Mark Diedericks
 *
 */

public class Map {
	
	private ArrayList<MapObject> children;
	private Transform transform;
	public final String name;
	private EngineCore engineCore;
	
	public Map(String name) {
		this.name = name;
		children = new ArrayList<MapObject>();
		transform = new Transform();
	}
	
	private int index = 0;
	public synchronized void addChild(MapObject child) {
		child.setId(index);
		index++;
		child.getTransform().setParent(transform);
		child.setJesus(this);
		if(engineCore != null) child.setEngineCore(engineCore);
		children.add(child);
	}
	
	public synchronized void removeChild(MapObject child) {
		int i = getIndex(child);
		if(i < 0) return;
		this.children.remove(i);
	}
	
	public synchronized MapObject getChild(int id) {
		int i = getIndex(id);
		if(i < 0) return null;
		return this.children.get(i);
	}
	
	private synchronized int getIndex(MapObject child) {
		int index = 0;
		for(MapObject m : children) {
			if(m.getId() == child.getId()) {
				return index;
			}
			index++;
		}
		return -1;
	}
	
	private synchronized int getIndex(int child) {
		int index = 0;
		for(MapObject m : children) {
			if(m.getId() == child) {
				return index;
			}
			index++;
		}
		return -1;
	}
	
	public Transform getTransform() {
		return transform;
	}
	
	public synchronized void input() {
		for(MapObject child: children) {
			child.input();
		}
	}
	
	public synchronized void update(float delta) {
		for(MapObject child: children) {
			child.update(delta);
		}
	}
	
	public synchronized void postUpdate(float delta) {
		for(MapObject child: children) {
			child.postUpdate(delta);
		}
	}
	
	public synchronized void render(Shader shader, RenderingEngine renderingEngine, Camera camera) {
		for(MapObject child: children) {
			child.render(shader, renderingEngine, camera);
		}
	}
	
	public synchronized void renderPost(Shader shader, RenderingEngine renderingEngine, Camera camera) {
		for(MapObject child: children) {
			child.renderPost(shader, renderingEngine, camera);
		}
	}
	
	public synchronized void setEngineCore(EngineCore engineCore) {
		this.engineCore = engineCore;
		for(MapObject child: children) {
			child.setEngineCore(engineCore);
		}
	}
}
