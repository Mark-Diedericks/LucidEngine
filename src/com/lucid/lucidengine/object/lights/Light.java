package com.lucid.lucidengine.object.lights;

import com.lucid.lucidengine.main.EngineCore;
import com.lucid.lucidengine.mathematics.Quaternion;
import com.lucid.lucidengine.mathematics.Vector3f;
import com.lucid.lucidengine.object.MapComponent;
import com.lucid.lucidengine.rendering.Material;
import com.lucid.lucidengine.rendering.RenderingEngine;
import com.lucid.lucidengine.rendering.Texture;
import com.lucid.lucidengine.rendering.lighting.Shader;
import com.lucid.lucidengine.rendering.lighting.Shadow;
import com.lucid.lucidengine.rendering.mesh.MeshRenderer;
import com.lucid.lucidengine.rendering.mesh.StaticMesh;
import com.lucid.lucidengine.ui.Camera;

/**
 * @author Mark Diedericks
 *
 */

public class Light extends MapComponent {
	
	private Vector3f color;
	private float intensity;
	private Shader shader;
	private Shadow shadow;
	private MeshRenderer globe;
	
	public Light(Vector3f color, float intensity, MeshRenderer mesh) {
		this.color = color;
		this.intensity = intensity;
		this.globe = new MeshRenderer(new StaticMesh("/res/models/sphere.obj", true), new Material(new Texture("/res/textures/white.png"), new Vector3f(1f, 1f, 1f), 1f, 1f));
	}
	
	public Light(Vector3f color, float intensity) {
		this(color, intensity, null);
	}
	
	public Shadow calcShadowCameraTransform(Vector3f mainCameraPosition, Quaternion mainCameraRotation) {
		if(shadow != null) shadow.setPosition(getTransform().getTransformedPos());
		if(shadow != null) shadow.setRotation(getTransform().getTransformedRot());
		return shadow;
	}
	
	@Override
	public void render(Shader shader, RenderingEngine renderingEngine,	Camera camera) {
		if(shader.fileName.contains("occl")) {
			globe.setTransform(getTransform());
			globe.render(shader, renderingEngine, camera);
		}
	}
	
	@Override
	public void addToEngineCore(EngineCore engineCore) {
		engineCore.getRenderingEngine().addLight(this);
	}
	
	public Shadow getShadow() {
		return this.shadow;
	}
	
	protected void setShadow(Shadow shadow) {
		this.shadow = shadow;
	}
	
	public void setShader(Shader shader) {
		this.shader = shader;
	}
	
	public Shader getShader() {
		return shader;
	}
	
	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public float getIntensity() {
		return intensity;
	}

	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}
	
}
