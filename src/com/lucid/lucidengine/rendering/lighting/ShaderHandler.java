package com.lucid.lucidengine.rendering.lighting;

import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.glCreateProgram;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Mark Diedericks
 *
 */

public class ShaderHandler {
	private int program;
	private int refCount;
	private ArrayList<Uniform> uniforms;
	private HashMap<String, Integer> locations;
	
	public ShaderHandler() {
		program = glCreateProgram();
		uniforms = new ArrayList<Uniform>();
		locations = new HashMap<String, Integer>();
		
		if(program == 0) {
			System.err.println("Shader creation failed, could not find valid memory location in constructor!");
			System.exit(1);
		}
		
		refCount = 1;
	}

	@Override
	protected void finalize() {
		glDeleteBuffers(program);
	}
	
	public ArrayList<Uniform> getUniforms() {
		return uniforms;
	}
	
	public HashMap<String, Integer> getLocations() {
		return locations;
	}
	
	public void addUniform(Uniform uniform) {
		uniforms.add(uniform);
		locations.put(uniform.name, uniform.location);
	}
	
	public void addReference() {
		refCount++;
	}
	
	public boolean subReference() {
		refCount--;
		return refCount == 0;
	}

	public int getProgram() {
		return this.program;
	}
}
