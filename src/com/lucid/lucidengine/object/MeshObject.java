package com.lucid.lucidengine.object;

import com.lucid.lucidengine.main.EngineCore;
import com.lucid.lucidengine.physics.MeshCollider;
import com.lucid.lucidengine.rendering.RenderingEngine;
import com.lucid.lucidengine.rendering.lighting.Shader;
import com.lucid.lucidengine.rendering.mesh.MeshRenderer;
import com.lucid.lucidengine.ui.Camera;

public class MeshObject extends MapObject {
	
	private MeshCollider collider;
	private MeshRenderer renderer;
	
	public MeshObject(MeshRenderer meshRenderer, MeshCollider meshCollider) {
		this.collider = meshCollider;
		this.renderer = meshRenderer;
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		this.renderer.getTransform().getPos().set(this.collider.getPhysicsObject().getPosition());
		this.renderer.getTransform().setRotation(this.getTransform().getRotation());
		this.renderer.getTransform().getScale().set(this.getTransform().getScale());
	}
	
	@Override
	public void render(Shader shader, RenderingEngine renderingEngine, Camera camera) {
		super.render(shader, renderingEngine, camera);
		this.renderer.render(shader, renderingEngine, camera);
	}
	
	@Override
	public void renderPost(Shader shader, RenderingEngine renderingEngine, Camera camera) {
		super.renderPost(shader, renderingEngine, camera);
		this.renderer.renderPost(shader, renderingEngine, camera);
	}
	
	@Override
	public void setEngineCore(EngineCore engineCore) {
		super.setEngineCore(engineCore);
		collider.addToEngineCore(engineCore);
		renderer.addToEngineCore(engineCore);
	}
	
}
