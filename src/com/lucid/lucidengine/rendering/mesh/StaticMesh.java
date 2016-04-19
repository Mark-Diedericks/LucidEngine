package com.lucid.lucidengine.rendering.mesh;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import java.util.ArrayList;
import java.util.HashMap;

import com.lucid.lucidengine.main.EngineCore;
import com.lucid.lucidengine.mathematics.Util;
import com.lucid.lucidengine.mathematics.Vector3f;
import com.lucid.lucidengine.mathematics.Vertex;

/**
 * @author Mark Diedericks
 *
 */

public class StaticMesh {
	
	private StaticMeshHandler handler;
	private String fileName;
	private Vertex[] vertices;
	private int[] indices;
	private HashMap<String, StaticMeshHandler> meshes = new HashMap<String, StaticMeshHandler>();
	
	public StaticMesh() {
		handler = new StaticMeshHandler();
	}
	
	public StaticMesh(Vertex[] vertices, int[] indices) {
		this(vertices, indices, false);
	}
	
	
	public StaticMesh(Vertex[] vertices, int[] indices, boolean calcNorm) {
		handler = new StaticMeshHandler();
		addVertices(vertices, indices, calcNorm);
	}
	
	public StaticMesh(String fileName, boolean calcNorm) {
		this(fileName, "pattern.png", calcNorm);
	}
	
	public StaticMesh(String file, String texture, boolean calcNorm) {
		String fileName = file;
		StaticMeshHandler mesh = meshes.get(fileName);
		this.fileName = fileName;
		if(mesh != null) {
			this.handler = mesh;
			this.handler.addReference();
		}
		else {
			handler = new StaticMeshHandler();
			loadMesh(fileName, texture);
			meshes.put(fileName, handler);
		}
	}
	
	@Override
	protected void finalize() {
		if(handler.subReference() && !fileName.isEmpty()) {
			meshes.remove(this.fileName);
		}
	}
	
	private Vertex[] calcNormals(Vertex[] vertices, int[] indices) {
		for(int i = 0; i < indices.length; i += 3)
		{
			int i0 = indices[i];
			int i1 = indices[i + 1];
			int i2 = indices[i + 2];

			Vector3f v1 = vertices[i1].getPos().sub(vertices[i0].getPos());
			Vector3f v2 = vertices[i2].getPos().sub(vertices[i0].getPos());

			Vector3f normal = v1.cross(v2).normalize();

			vertices[i0].setNormal(vertices[i0].getNormal().add(normal));
			vertices[i1].setNormal(vertices[i1].getNormal().add(normal));
			vertices[i2].setNormal(vertices[i2].getNormal().add(normal));
		}
		for(int i = 0; i < vertices.length; i++) vertices[i].getNormal().normalize();
		return vertices;
	}
	
	public void addVertices(Vertex[] vertices, int[] indices, boolean calcNorm) {
		if(calcNorm) {
			vertices = calcNormals(vertices, indices);
		}
		
		this.indices = indices;
		
		handler.setSize(indices.length);
		EngineCore.polycount += indices.length/3;
		
		this.vertices = vertices;
		
		glBindBuffer(GL_ARRAY_BUFFER, handler.getVbo());
		glBufferData(GL_ARRAY_BUFFER, Util.createFlippedBuffer(vertices), GL_STATIC_DRAW);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, handler.getIbo());
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, Util.createFlippedBuffer(indices), GL_STATIC_DRAW);
	}
	
	public void draw() {
		finalDraw(GL_TRIANGLES);
	}
	
	public void drawStrips() {
		finalDraw(GL_TRIANGLE_STRIP);
	}
	
	private void finalDraw(int typeIndex) {
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glEnableVertexAttribArray(3);
		
		glBindBuffer(GL_ARRAY_BUFFER, handler.getVbo());
		glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, Vertex.SIZE * 4, 12);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, Vertex.SIZE * 4, 20);
		glVertexAttribPointer(3, 3, GL_FLOAT, false, Vertex.SIZE * 4, 32);
		glVertexAttribPointer(3, 3, GL_FLOAT, false, Vertex.SIZE * 4, 44);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, handler.getIbo());
		glDrawElements(typeIndex, handler.getSize(), GL_UNSIGNED_INT, 0);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
	}
	
	private void loadMesh(String fileName, String textureName) {
		
		if(!fileName.endsWith(".obj")) {
			System.err.println("Wrong file format.... Mesh: " + fileName);
			new Exception().printStackTrace();
			System.exit(1);
		}
		
		OBJModel model = new OBJModel(fileName);
		IndexModel indexModel = model.toIndexModel();
		
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		
		for(int i = 0; i < indexModel.getPos().size(); i++) {
			vertices.add(new Vertex(indexModel.getPos().get(i), indexModel.getTextureUV().get(i), indexModel.getNormal().get(i), indexModel.getTangent().get(i)));
		}
		
		Vertex[] verticesData = new Vertex[vertices.size()];
		vertices.toArray(verticesData);
		Integer[] indicesData = new Integer[indexModel.getIndices().size()];
		indexModel.getIndices().toArray(indicesData);
		System.out.println(fileName + "===" + indicesData.length/3);
		addVertices(verticesData, Util.toIntArray(indicesData), false);
		vertices.clear();
		model.clear();
		indexModel.clear();
	}
	
	public int[] getIndices() {
		return indices;
	}
	
	public Vertex[] getVertices() {
		return this.vertices;
	}
}
