package com.lucid.lucidengine.rendering;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;

import com.lucid.lucidengine.main.EngineResource;
import com.lucid.lucidengine.mathematics.Util;

/**
 * @author Mark Diedericks
 *
 */

public class Texture {
	
	private TextureHandler handler;
	public final String fileName;
	private int width = 0;
	private int height = 0;
	private HashMap<String, TextureHandler> textures = new HashMap<String, TextureHandler>();
	
	private int num_textures = 1;
	private int textureNum = 0;
	private int frameBufferID = 0;
	private int renderBufferID = 0;
	
	public Texture(String file) {
		String fileName = file;
		TextureHandler texture = textures.get(fileName);
		this.fileName = fileName;
		if(texture != null) {
			this.handler = texture;
			this.handler.addReference();
		}
		else {
			try {
				BufferedImage textureimg = ImageIO.read(EngineResource.getResource(fileName));
				ByteBuffer buffer = Util.loadBufferedImage(textureimg);
				this.handler = new TextureHandler(loadTexture(buffer, new int[] {GL_LINEAR_MIPMAP_LINEAR}, textureimg.getWidth(), textureimg.getHeight(), GL_LINEAR, false, GL_RGBA8, GL_RGBA));
				textures.put(fileName, handler);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Texture(String identifier, int[] filters, int width, int height, ByteBuffer data, int textureParam, int internalFormat, int format, boolean clamp, int[] attachment) {
		this.fileName = new String(identifier + width + height + textureParam + internalFormat + format + clamp);
		this.handler = new TextureHandler(loadTexture(data, filters, width, height, textureParam, clamp, internalFormat, format));
		loadRenderTargets(attachment, 1);
		textures.put(this.fileName, handler);
	}
	
	private void loadRenderTargets(int[] attachment, int numOfTextures) {
		IntBuffer drawingBuffer = Util.createIntBuffer(numOfTextures);
		boolean hasDepth = false;
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		for(int i = 0; i < numOfTextures; i++) {
			if(attachment[i] == GL_DEPTH_ATTACHMENT) {
				drawingBuffer.put(i, GL_NONE);
				hasDepth = true;
			} else {
				drawingBuffer.put(i, attachment[i]);
			}
			
			if(attachment[i] == GL_NONE) continue;
			
			if(frameBufferID == 0) {
				System.out.println("Making framebuffer");
				frameBufferID = glGenFramebuffers();
				glBindFramebuffer(GL_FRAMEBUFFER, frameBufferID);
			}
			
			glFramebufferTexture2D(GL_FRAMEBUFFER, attachment[i], GL_TEXTURE_2D, this.handler.getID()[i], 0);
		}
		
		if(frameBufferID == 0) return;
		
		if(!hasDepth) {
			renderBufferID = glGenRenderbuffers();
			glBindRenderbuffer(GL_RENDERBUFFER, renderBufferID);
			glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height);
			glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, renderBufferID);
		}
		
		glDrawBuffers(drawingBuffer);
		
		if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
			System.err.println("Framebuffer failed to create!");
		}
		
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}
	
	public void bindAsRenderTarget() {
		glBindTexture(GL_TEXTURE_2D, 0);
		glBindFramebuffer(GL_FRAMEBUFFER, frameBufferID);
		glViewport(0, 0, this.width, this.height);
	}
	
	public void bind() {
		this.bind(GL_TEXTURE_2D, textureNum);
	}
	
	public void bind(int texture, int textureNum) {
		glBindTexture(texture, handler.getID()[textureNum]);
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	@Override
	protected void finalize() {
		if(handler.subReference() && !fileName.isEmpty()) {
			textures.remove(this.fileName);
			if(frameBufferID != 0) glDeleteFramebuffers(frameBufferID);
			if(renderBufferID != 0) glDeleteRenderbuffers(renderBufferID);
		}
	}
	
	public int[] getId() {
		return this.handler.getID();
	}
	
	private int[] loadTexture(ByteBuffer buffer, int[] filters, int width, int height, int textureParam, boolean clamp, int internalFormat, int format) {
		try {
			int[] id = new int[num_textures];
			for(int i = 0; i < num_textures; i++) {
				if(width > 0 && height > 0) {
					GL13.glActiveTexture(GL13.GL_TEXTURE0);
					this.width = width;
					this.height = height;

					id[i] = glGenTextures();
					glBindTexture(GL_TEXTURE_2D, id[i]);
					
					glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
					glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
					
					glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, textureParam);
					glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, textureParam);
					
					if(clamp) {
						glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
						glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
					}
					
					glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, GL_UNSIGNED_BYTE, (ByteBuffer)buffer);
					
					if(filters[i] == GL_NEAREST_MIPMAP_NEAREST || filters[i] == GL_NEAREST_MIPMAP_LINEAR || filters[i] == GL_LINEAR_MIPMAP_LINEAR || filters[i] == GL_LINEAR_MIPMAP_NEAREST) {
						glGenerateMipmap(GL_TEXTURE_2D);
						float maxAniso = glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT);
						glTexParameterf(GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, maxAniso);
					} else {
						glTexParameterf(GL_TEXTURE_2D, GL12.GL_TEXTURE_BASE_LEVEL, 0);
						glTexParameterf(GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, 0);
					}
					
					return id;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return new int[] {0};
	}
	
}
