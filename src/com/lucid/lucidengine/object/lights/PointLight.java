package com.lucid.lucidengine.object.lights;

import com.lucid.lucidengine.main.EngineCore;
import com.lucid.lucidengine.mathematics.Vector3f;
import com.lucid.lucidengine.rendering.RenderingEngine;
import com.lucid.lucidengine.rendering.lighting.Shader;
import com.lucid.lucidengine.ui.Camera;

/**
 * @author Mark Diedericks
 *
 */

public class PointLight extends Light {
	
	private static final int COLOR_DEPTH = 256;
	
	private float constant;
	private float linear;
	private float exp;
	private float range;
	
	public PointLight(Vector3f color, float intensity, Vector3f atten, float range) {
		super(color, intensity);
		this.constant = atten.getX();
		this.linear = atten.getY();
		this.exp = atten.getZ();
		this.range = range;
		super.setShader(new Shader("forward-point.vs", "forward-point.fs"));
	}
	
	public PointLight(Vector3f color, float intensity, Vector3f atten) {
		this(color, intensity, atten, (float)((-(atten.getY()) + Math.sqrt((atten.getY()) * (atten.getY()) - 4 * (atten.getZ()) * (atten.getX() - COLOR_DEPTH * intensity * color.max())))/(2 * (atten.getZ()))));
	}
	
	public float getConstant() {
		return constant;
	}

	public void setConstant(float constant) {
		this.constant = constant;
	}

	public float getLinear() {
		return linear;
	}

	public void setLinear(float linear) {
		this.linear = linear;
	}

	public float getExp() {
		return exp;
	}

	public void setExp(float exp) {
		this.exp = exp;
	}

	public float getRange() {
		return range;
	}

	public void setRange(float range) {
		this.range = range;
	}
	
	@Override
	public void render(Shader shader, RenderingEngine renderingEngine, Camera camera) {
		super.render(shader, renderingEngine, camera);
	}
	
	@Override
	public void addToEngineCore(EngineCore engineCore) {
		engineCore.getRenderingEngine().addLight(this);
	}
}
