package com.lucid.lucidengine.rendering.mesh;

import static org.lwjgl.opengl.GL15.*;

/**
 * @author Mark Diedericks
 *
 */

public class StaticMeshHandler {
	private int vbo;
	private int ibo;
	private int size;
	private int refCount;
	
	public StaticMeshHandler() {
		vbo = glGenBuffers();
		ibo = glGenBuffers();
		size = 0;
		refCount = 1;
	}
	
	@Override
	protected void finalize() {
		glDeleteBuffers(vbo);
		glDeleteBuffers(ibo);
	}
	
	public void addReference() {
		refCount++;
	}
	
	public boolean subReference() {
		refCount--;
		return refCount == 0;
	}
	
	public int getVbo() {
		return vbo;
	}
	
	public int getIbo() {
		return ibo;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}