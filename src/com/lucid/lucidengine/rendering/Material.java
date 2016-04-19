package com.lucid.lucidengine.rendering;

import com.lucid.lucidengine.mathematics.Vector3f;

/**
 * @author Mark Diedericks
 *
 */

public class Material extends RenderingVariables {
	
	private Vector3f color;
	private float specularIntensity;
	private float specularExponent;
	
	public Material(Texture text, Texture normalMap, Vector3f color, float specInt, float specExp) {
		this.addTexture("diffuse", text);
		this.addTexture("normalMap", normalMap);
		this.color = color;
		this.specularIntensity = specInt;
		this.specularExponent = specExp;
	}
	
	public Material(Texture text, Vector3f color, float specInt, float specExp) {
		this(text, new Texture("/res/textures/default_normal.jpg"), color, specInt, specExp);
	}
	
	public Material() {}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public float getSpecularIntensity() {
		return specularIntensity;
	}

	public void setSpecularIntensity(float specularIntensity) {
		this.specularIntensity = specularIntensity;
	}

	public float getSpecularExponent() {
		return specularExponent;
	}

	public void setSpecularExponent(float specularExponent) {
		this.specularExponent = specularExponent;
	}
	
}
