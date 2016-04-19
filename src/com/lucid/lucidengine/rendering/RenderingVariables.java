package com.lucid.lucidengine.rendering;

import java.util.HashMap;

import com.lucid.lucidengine.mathematics.Matrix4f;
import com.lucid.lucidengine.mathematics.Vector3f;

/**
 * @author Mark Diedericks
 *
 */

public class RenderingVariables {
	
	private HashMap<String, Integer> integers;
	private HashMap<String, Float> floats;
	private HashMap<String, Vector3f> vector3f;
	private HashMap<String, Matrix4f> matrix4f;
	private HashMap<String, Texture> textures;
	
	public void addInteger(String name, int value) {
		if(integers == null) integers = new HashMap<String, Integer>();
		integers.put(name, value);
	}
	
	public void addFloat(String name, float value) {
		if(floats == null) floats = new HashMap<String, Float>();
		floats.put(name, value);
	}
	
	public void addVector3f(String name, Vector3f value) {
		if(vector3f == null) vector3f = new HashMap<String, Vector3f>();
		vector3f.put(name, value);
	}
	
	public void addMatrix4f(String name, Matrix4f value) {
		if(matrix4f == null) matrix4f = new HashMap<String, Matrix4f>();
		matrix4f.put(name, value);
	}
	
	public void addTexture(String name, Texture value) {
		if(textures == null) textures = new HashMap<String, Texture>();
		textures.put(name, value);
	}
	
	public void setInteger(String name, int value) {
		if(integers == null) integers = new HashMap<String, Integer>();
		integers.remove(name);
		integers.put(name, value);
	}
	
	public void setFloat(String name, float value) {
		if(floats == null) floats = new HashMap<String, Float>();
		floats.remove(name);
		floats.put(name, value);
	}
	
	public void setVector3f(String name, Vector3f value) {
		if(vector3f == null) vector3f = new HashMap<String, Vector3f>();
		vector3f.remove(name);
		vector3f.put(name, value);
	}
	
	public void setMatrix4f(String name, Matrix4f value) {
		if(matrix4f == null) matrix4f = new HashMap<String, Matrix4f>();
		matrix4f.remove(name);
		matrix4f.put(name, value);
	}
	
	public void setTexture(String name, Texture value) {
		if(textures == null) textures = new HashMap<String, Texture>();
		textures.remove(name);
		textures.put(name, value);
	}
	
	public int getInteger(String name) {
		return integers.get(name);
	}
	
	public float getFloat(String name) {
		return floats.get(name);
	}
	
	public Vector3f getVector3f(String name) {
		return vector3f.get(name);
	}
	
	public Matrix4f getMatrix4f(String name) {
		return matrix4f.get(name);
	}
	
	public Texture getTexture(String name) {
		return textures.get(name);
	}
}
