package com.lucid.lucidengine.physics;

import com.lucid.lucidengine.main.EngineCore;
import com.lucid.lucidengine.mathematics.Vector3f;
import com.lucid.lucidengine.object.MapComponent;
import com.lucid.lucidengine.rendering.RenderingEngine;
import com.lucid.lucidengine.rendering.lighting.Shader;
import com.lucid.lucidengine.ui.Camera;
import com.lucid.lucidengine.ui.Player;

public class MeshCollider extends MapComponent {
	
	private PhysicsObject physicsObj;
	private CollisionAttributes attrib;
	private final boolean isStatic;
	
	private EngineCore engineCore;
	int id = 0;
	
	public MeshCollider(boolean isStatic, PhysicsObject physicsObject, CollisionAttributes collisionAttributes, Vector3f startPos) {
		this.physicsObj = physicsObject;
		this.isStatic = isStatic;
		this.physicsObj.setPosition(startPos);
		this.id = physicsObj.id;
		this.attrib = collisionAttributes;
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		if(getParent() != null) {
			if(getParent() instanceof Player) {
				engineCore.getPhysicsEngine().getDynamicPhysicsObject(id).setVelocity(((Player)getParent()).getVelocity());
				engineCore.getPhysicsEngine().getDynamicPhysicsObject(id).setPosition(getParent().getTransform().getPos());
			} else {
				getParent().getTransform().getPos().set(engineCore.getPhysicsEngine().getDynamicPhysicsObject(id).getPosition());
			}
		}
	}
	
	@Override
	public void render(Shader shader, RenderingEngine renderingEngine, Camera camera) {
		super.render(shader, renderingEngine, camera);
	}
	
	@Override
	public void addToEngineCore(EngineCore engineCore) {
		super.addToEngineCore(engineCore);
		this.engineCore = engineCore;
		if(isStatic) engineCore.getPhysicsEngine().addStaticObject(physicsObj.setAtrributes(attrib));
		else engineCore.getPhysicsEngine().addDynamicObject(physicsObj.setAtrributes(attrib));
	}
	
	public PhysicsObject getPhysicsObject() {
		if(isStatic) return engineCore.getPhysicsEngine().getStaticPhysicsObject(id);
		else return engineCore.getPhysicsEngine().getDynamicPhysicsObject(id);
	}
}
