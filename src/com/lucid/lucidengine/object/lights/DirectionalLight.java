package com.lucid.lucidengine.object.lights;

import com.lucid.lucidengine.main.EngineCore;
import com.lucid.lucidengine.mathematics.Matrix4f;
import com.lucid.lucidengine.mathematics.Vector3f;
import com.lucid.lucidengine.rendering.RenderingEngine;
import com.lucid.lucidengine.rendering.lighting.Shader;
import com.lucid.lucidengine.rendering.lighting.Shadow;
import com.lucid.lucidengine.ui.Camera;

/**
 * @author Mark Diedericks
 *
 */

public class DirectionalLight extends Light {
	
	private Vector3f direction;
	private float halfShadowCoverSize;
	
	public DirectionalLight(Vector3f color, float intensity, Vector3f direction, int shadowMapSizePower, float shadowCoverSize, float shadowSoftness, float varianceMin, float lightBleedReduction) {
		super(color, intensity);
		this.direction = direction.normalize();
		if(shadowMapSizePower >= 14) shadowMapSizePower = 10;
		super.setShader(new Shader("forward-directional.vs", "forward-directional.fs"));
		halfShadowCoverSize = shadowCoverSize / 2.0f;
		if(shadowMapSizePower > 0) super.setShadow(new Shadow(new Matrix4f().initOthographic(-halfShadowCoverSize, halfShadowCoverSize, -halfShadowCoverSize, halfShadowCoverSize, -halfShadowCoverSize, halfShadowCoverSize), true).setShadowSoftness(shadowSoftness).setvMin(varianceMin).setLightBleedReduction(lightBleedReduction).setShadowMapSizePower(shadowMapSizePower));
		this.getShadow().setTransform(true);
	}
	
	public DirectionalLight(Vector3f color, float intensity, Vector3f direction) {
		this(color, intensity, direction, 14, 128, 1.0f, 0.00002f, 0.02f);
	}

	public Vector3f getDirection() {
		return direction;
	}

	public void setDirection(Vector3f direction) {
		this.direction = direction;
	}
	
	@Override
	public void render(Shader shader, RenderingEngine renderingEngine,Camera camera) {
		super.render(shader, renderingEngine, camera);
	}
	
	@Override
	public void addToEngineCore(EngineCore engineCore) {
		engineCore.getRenderingEngine().addLight(this);
	}
}
