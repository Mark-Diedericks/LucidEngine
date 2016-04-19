package com.lucid.lucidengine.rendering;

import static org.lwjgl.opengl.GL15.glDeleteBuffers;

/**
 * @author Mark Diedericks
 *
 */

public class TextureHandler {
	private int[] id;
	private int refCount;
	
	public TextureHandler(int[] id) {
		this.id = id;
		refCount = 1;
	}
	
	@Override
	protected void finalize() {
		for(int i = 0; i < id.length; i++)
		glDeleteBuffers(id[i]);
	}
	
	public void addReference() {
		refCount++;
	}
	
	public boolean subReference() {
		refCount--;
		return refCount == 0;
	}

	public int[] getID() {
		return this.id;
	}
}
