package com.lucid.lucidengine.rendering.lighting;

import com.lucid.lucidengine.mathematics.Matrix4f;
import com.lucid.lucidengine.mathematics.Quaternion;
import com.lucid.lucidengine.mathematics.Vector3f;
import com.lucid.lucidengine.ui.Camera;

/**
 * @author Mark Diedericks
 *
 */

public class Shadow {

	private Matrix4f projection;
	private boolean flipFaces;
	private boolean transform;
	
	private float vMin = 0.00002f;
	private float lightBleedReduction = 0.2f;
	private float shadowSoftness = 1.0f;
	private int shadowMapSizePower = 1;
	private Vector3f position;
	private Quaternion rotation;
	
	public Shadow(Matrix4f projection, boolean flipFaces) {
		this.projection = projection;
		this.flipFaces = flipFaces;
	}
	
	public float getvMin() {
		return vMin;
	}

	public Shadow setvMin(float vMin) {
		this.vMin = vMin;
		return this;
	}

	public float getLightBleedReduction() {
		return lightBleedReduction;
	}

	public Shadow setLightBleedReduction(float lightBleedReduction) {
		this.lightBleedReduction = lightBleedReduction;
		return this;
	}

	public float getShadowSoftness() {
		return shadowSoftness;
	}

	public Shadow setShadowSoftness(float shadowSoftness) {
		this.shadowSoftness = shadowSoftness;
		return this;
	}
	
	public Matrix4f getProjection() {
		return this.projection;
	}

	public boolean isFlipFaces() {
		return flipFaces;
	}

	public Vector3f getPosition(Camera camera) {
		if(this.transform) return position.add(camera.getTransform().getPos());
		return position;
	}

	public Quaternion getRotation() {
		return rotation;
	}
	
	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public void setRotation(Quaternion rotation) {
		this.rotation = rotation;
	}

	public int getShadowMapSizePower() {
		return shadowMapSizePower;
	}
	
	public void setTransform(boolean transform) {
		this.transform = transform;
	}
	
	public Shadow setShadowMapSizePower(int shadowMapSizePower) {
		this.shadowMapSizePower = shadowMapSizePower;
		return this;
	}
}
