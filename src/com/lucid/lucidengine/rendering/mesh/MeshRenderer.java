package com.lucid.lucidengine.rendering.mesh;

import com.lucid.lucidengine.object.MapComponent;
import com.lucid.lucidengine.rendering.Material;
import com.lucid.lucidengine.rendering.RenderingEngine;
import com.lucid.lucidengine.rendering.lighting.Shader;
import com.lucid.lucidengine.ui.Camera;

/**
 * @author Mark Diedericks
 *
 */

public class MeshRenderer extends MapComponent {
	
	private StaticMesh mesh;
	private Material mat;
	private float radius;
	
	public MeshRenderer(StaticMesh mesh, Material mat) {
		super();
		this.mesh = mesh;
		this.mat = mat;
	}
	
	@Override
	public void render(Shader shader, RenderingEngine renderingEngine, Camera camera) {
		shader.bind();
		shader.updateUniforms(this.getTransform(), renderingEngine, mat, camera);
		mesh.draw();
	}
	
	public Material getMaterial() {
		return this.mat;
	}
	
	public StaticMesh getStaticMesh() {
		return this.mesh;
	}
	
	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}
}
