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

public class MapObject {
	
	protected ArrayList<MapObject> children;
	protected ArrayList<MapComponent> components;
	private Transform transform;
	private MapObject parent;
	private Map jesus;
	private EngineCore engineCore;
	private int id;
	
	public MapObject() {
		children = new ArrayList<MapObject>();
		components = new ArrayList<MapComponent>();
		transform = new Transform();
		engineCore = null;
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
	
	public void addChild(MapObject child) {
		child.getTransform().setParent(transform);
		child.setParent(this);
		child.setEngineCore(engineCore);
		children.add(child);
	}
	
	public MapObject addComponent(MapComponent comp) {
		comp.getTransform().setParent(transform);
		comp.setParent(this);
		components.add(comp);
		return this;
	}
	
	public Transform getTransform() {
		return transform;
	}
	
	public void input() {
		for(MapComponent comp:components) {
			comp.input();
		}
		
		for(MapObject child : children) {
			child.input();
		}
	}
	
	public void update(float delta) {
		for(MapComponent comp:components) {
			comp.update(delta);
		}
		
		for(MapObject child: children) {
			child.update(delta);
		}
	}
	
	public void postUpdate(float delta) {
		for(MapComponent comp:components) {
			comp.postUpdate(delta);
		}
		
		for(MapObject child: children) {
			child.postUpdate(delta);
		}
	}
	
	public void render(Shader shader, RenderingEngine renderingEngine, Camera camera) {
		for(MapComponent comp: components) {
			comp.render(shader, renderingEngine, camera);
		}
		
		for(MapObject child: children) {
			child.render(shader, renderingEngine, camera);
		}
	}
	
	public void renderPost(Shader shader, RenderingEngine renderingEngine, Camera camera) {
		for(MapComponent comp: components) {
			comp.renderPost(shader, renderingEngine, camera);
		}
		
		for(MapObject child: children) {
			child.renderPost(shader, renderingEngine, camera);
		}
	}

	public Map getJesus() {
		return jesus;
	}
	
	public ArrayList<MapComponent> getComponents() {
		return this.components;
	}
	
	public void setJesus(Map jesus) {
		this.jesus = jesus;
	}
	
	public EngineCore getEngineCore() {
		return this.engineCore;
	}
	
	public void setEngineCore(EngineCore engineCore) {
		if(this.engineCore != engineCore) {
			this.engineCore = engineCore;
			
			for(MapComponent comp:components) {
				comp.addToEngineCore(engineCore);
			}
			
			for(MapObject child: children) {
				child.setEngineCore(engineCore);
			}
		}
	}
}
