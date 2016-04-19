package com.lucid.lucidengine.object;

import static org.lwjgl.opengl.GL11.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import com.lucid.lucidengine.main.EngineCore;
import com.lucid.lucidengine.main.EngineResource;
import com.lucid.lucidengine.mathematics.Vector2f;
import com.lucid.lucidengine.mathematics.Vector3f;
import com.lucid.lucidengine.mathematics.Vertex;
import com.lucid.lucidengine.rendering.Material;
import com.lucid.lucidengine.rendering.RenderingEngine;
import com.lucid.lucidengine.rendering.lighting.Shader;
import com.lucid.lucidengine.rendering.mesh.StaticMesh;
import com.lucid.lucidengine.ui.Camera;

/**
 * @author Mark Diedericks
 *
 */

public class Terrain extends MapComponent {
	
	private StaticMesh[] terrain;
	private float exaggeration;
	private Material material;
	private BufferedImage heightMap;
	private float scale;
	int width;
	int height;
	
	public Terrain(Material mat) {
		material = mat;
	}
	
	public void setMaterial(Material material) {
		this.material = material;
	}
	
	public void render(Shader shader, RenderingEngine renderingEngine, Camera camera) {
		shader.bind();
		shader.updateUniforms(this.getTransform(), renderingEngine, material, camera);
		for(int x = 0; x < terrain.length; x++) terrain[x].drawStrips();
	}
	
	public void setScale(float scale) {
		this.scale = scale;
		getTransform().getScale().setVector(scale, scale, scale);
	}
	
	public Terrain getFormHeightMap(String fileName, float exaggeration, RenderingEngine renderingEngine) {
		try {
			this.heightMap = ImageIO.read(EngineResource.getResource(fileName));
			this.scale = 1.0f;
			this.width = heightMap.getWidth();
			this.height = heightMap.getHeight();
			this.exaggeration = exaggeration;
			this.terrain = new StaticMesh[width];
			Vertex[] vertices = new Vertex[height*2-height];
			int[] indices = new int[height*2-height];
			getTransform().getScale().setVector(scale, scale, scale);
			
			EngineCore.polycount += (height*2-height) / 3.0f;
			
			for(int x = 0; x < width; x++) {
				for(int z = 0; z < height; z+=2) {
						
						vertices[z + 1] = new Vertex(new Vector3f((float)x, ((float)new Color(heightMap.getRGB(x, z)).getRed())/255f*exaggeration, (float)z), new Vector2f(x, z), new Vector3f((float)x, ((float)new Color(heightMap.getRGB(x, z)).getRed())/255f*exaggeration, (float)z));
						indices[z] = z;
						
						if(x < width-1) {
							vertices[z] = new Vertex(new Vector3f((float)x+1, ((float)new Color(heightMap.getRGB(x+1, z)).getRed())/255f*exaggeration, (float)z), new Vector2f(x+1, z), new Vector3f((float)x+1, ((float)new Color(heightMap.getRGB(x+1, z)).getRed())/255f*exaggeration, (float)z));
							indices[z + 1] = z + 1;
						} else {
							vertices[z] = new Vertex(new Vector3f((float)x, ((float)new Color(heightMap.getRGB(x, z)).getRed())/255f*exaggeration, (float)z), new Vector2f(x, z), new Vector3f((float)x, ((float)new Color(heightMap.getRGB(x, z)).getRed())/255f*exaggeration, (float)z));
							indices[z + 1] = z + 1;
						}
				}
				terrain[x] = new StaticMesh(vertices, indices, false);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return this;
	}
	
	public float getHeightAt(float x, float z)
	{
		float xPos = x - this.getTransform().getPos().getX();
		float zPos = z - this.getTransform().getPos().getZ();
		float gridSquareSize = 1.0f * scale;
		int gridX = (int)Math.floor(xPos / gridSquareSize);
		int gridZ = (int)Math.floor(zPos / gridSquareSize);
		if(gridX >= this.width-1 || gridZ >= this.height-1 || gridX < 0 || gridZ < 0) return 0f;
		float xCoord = (xPos % gridSquareSize) / gridSquareSize;
		float zCoord = (zPos % gridSquareSize) / gridSquareSize;
		float result;
		if(xCoord <= (1 - zCoord)) {
			result = barryCentric(new Vector3f(0, (float)(new Color(heightMap.getRGB(gridX, gridZ)).getRed())/255f*exaggeration, 0), new Vector3f(1, (float)(new Color(heightMap.getRGB(gridX + 1, gridZ)).getRed())/255f*exaggeration, 0), new Vector3f(0, (float)(new Color(heightMap.getRGB(gridX, gridZ + 1)).getRed())/255f*exaggeration, 1), new Vector2f(xCoord, zCoord));
		} else {
			result = barryCentric(new Vector3f(1, (float)(new Color(heightMap.getRGB(gridX + 1, gridZ)).getRed())/255f*exaggeration, 0), new Vector3f(1, (float)(new Color(heightMap.getRGB(gridX + 1, gridZ + 1)).getRed())/255f*exaggeration, 1), new Vector3f(0, (float)(new Color(heightMap.getRGB(gridX, gridZ + 1)).getRed())/255f*exaggeration, 1), new Vector2f(xCoord, zCoord));
		}
		return result + this.getTransform().getPos().getY();
	}
	
	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.getZ() - p3.getZ()) * (p1.getX() - p3.getX()) + (p3.getX() - p2.getX()) * (p1.getZ() - p3.getZ());
		float l1 = ((p2.getZ() - p3.getZ()) * (pos.getX() - p3.getX()) + (p3.getX() - p2.getX()) * (pos.getY() - p3.getZ())) / det;
		float l2 = ((p3.getZ() - p1.getZ()) * (pos.getX() - p3.getX()) + (p1.getX() - p3.getX()) * (pos.getY() - p3.getZ())) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.getY() + l2 * p2.getY() + l3 * p3.getY();
	}

	public float getSize() {
		return (float)heightMap.getWidth() * scale;
	}
}
