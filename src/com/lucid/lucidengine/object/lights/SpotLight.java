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

public class SpotLight extends PointLight {
	
	private float cutoff;
	
	public SpotLight(Vector3f color, float intensity, Vector3f atten, float viewAngle, int shadowMapSizePower, float shadowSoftness, float varianceMin, float lightBleedReduction) {
		super(color, intensity, atten);
		this.cutoff = (float)Math.cos(viewAngle / 2.0f);
		if(shadowMapSizePower >= 12) shadowMapSizePower = 8;
		super.setShader(new Shader("forward-spot.vs", "forward-spot.fs"));
		if(shadowMapSizePower > 0) super.setShadow(new Shadow(new Matrix4f().initPerspective(viewAngle, 1.0f, 0.1f, this.getRange()), false).setShadowSoftness(shadowSoftness).setvMin(varianceMin).setLightBleedReduction(lightBleedReduction).setShadowMapSizePower(shadowMapSizePower));
	}
	
	public SpotLight(Vector3f color, float intensity, Vector3f atten, float viewAngle) {
		this(color, intensity, atten, viewAngle, 8, 1.0f, 0.00002f, 0.999f);
	}
	
	public Vector3f getDirection() {
		return getTransform().getRotation().getRotateForward();
	}
	
	public float getCutoff() {
		return cutoff;
	}
	
	public void setCutoff(float cutoff) {
		this.cutoff = cutoff;
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
