package com.lucid.lucidengine.rendering.mesh;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import com.lucid.lucidengine.main.EngineResource;
import com.lucid.lucidengine.mathematics.Vector2f;
import com.lucid.lucidengine.mathematics.Vector3f;

/**
 * @author Mark Diedericks
 *
 */

public class OBJModel {
	
	private ArrayList<Vector3f> pos;
	private ArrayList<Vector2f> textureUV;
	private ArrayList<Vector3f> normal;
	private ArrayList<OBJIndex> indices;
	private boolean hasTextureUV;
	private boolean hasNormal;
	
	public OBJModel(String file) {
		pos = new ArrayList<Vector3f>();
		textureUV = new ArrayList<Vector2f>();
		normal = new ArrayList<Vector3f>();
		indices = new ArrayList<OBJIndex>();
		hasTextureUV = false;
		hasNormal = false;
		loadObj(file);
	}
	
	private void loadObj(String file) {
		BufferedReader decrypter;
		try {
			decrypter = new BufferedReader(new InputStreamReader(EngineResource.getResource(file)));
			String line;
			
			while((line = decrypter.readLine()) != null) {
				String[] meshData = line.split(" ");
				
				if(meshData.length == 0 || meshData[0].equals("#")) {
					continue;
				} else if(meshData[0].equalsIgnoreCase("v")) {
					pos.add(new Vector3f(Float.valueOf(meshData[1]), Float.valueOf(meshData[2]), Float.valueOf(meshData[3])));
				} else if(meshData[0].equalsIgnoreCase("vt")) {
					textureUV.add(new Vector2f(Float.valueOf(meshData[1]), Float.valueOf(meshData[2])));
				} else if(meshData[0].equalsIgnoreCase("vn")) {
					normal.add(new Vector3f(Float.valueOf(meshData[1]), Float.valueOf(meshData[2]), Float.valueOf(meshData[3])));
				} else if(meshData[0].equalsIgnoreCase("f")) {
					for(int i = 0; i < (meshData.length - 3); i++) {
						indices.add(parseOBJIndex(meshData[1]));
						indices.add(parseOBJIndex(meshData[2 + i]));
						indices.add(parseOBJIndex(meshData[3 + i]));
					}
				}
			}
			decrypter.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private OBJIndex parseOBJIndex(String meshData) {
		String[] values = meshData.replaceAll("//", "/").split("/");
		OBJIndex res = new OBJIndex();
		res.vertexIndex = Integer.parseInt(values[0]) - 1;
		
		if(values.length > 1) {
			hasTextureUV = true;
			res.textureIndex = Integer.parseInt(values[1]) - 1;
			if(values.length > 2) {
				hasNormal = true;
				res.normalIndex = Integer.parseInt(values[2]) - 1;
			}
		}
		
		return res;
	}
	
	public IndexModel toIndexModel() {
		IndexModel res = new IndexModel();
		IndexModel nres = new IndexModel();
		HashMap<OBJIndex, Integer> indexResMap = new HashMap<OBJIndex, Integer>();
		HashMap<Integer, Integer> indexNormalMap = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> indexMap = new HashMap<Integer, Integer>();
		
		int counter = 0;
		for(int i = 0; i < indices.size(); i++) {
			OBJIndex index = indices.get(i);
			
			Vector3f indexPos = pos.get(index.vertexIndex);
			Vector2f indexTextureUV;
			Vector3f indexNormal;
			
			if(hasTextureUV) indexTextureUV = textureUV.get(index.textureIndex);
			else indexTextureUV = new Vector2f(0, 0);
			
			if(hasNormal) indexNormal = normal.get(index.normalIndex);
			else indexNormal = new Vector3f(0, 0, 0);
			
			Integer prevIndex = indexResMap.get(index);
			if(prevIndex == null) {
				prevIndex = res.getPos().size();
				indexResMap.put(index, prevIndex);
				
				res.getPos().add(indexPos);
				res.getTextureUV().add(indexTextureUV);
				if(hasNormal) res.getNormal().add(indexNormal);
				res.getTangent().add(new Vector3f(0, 0, 0));
			}
			
			Integer normalModelIndex = indexNormalMap.get(index);
			if(normalModelIndex == null) {
				normalModelIndex = nres.getPos().size();
				indexNormalMap.put(index.vertexIndex, normalModelIndex);
				
				nres.getPos().add(indexPos);
				nres.getTextureUV().add(indexTextureUV);
				nres.getNormal().add(indexNormal);
				nres.getTangent().add(new Vector3f(0, 0, 0));
			}
			
			res.getIndices().add(prevIndex);
			nres.getIndices().add(normalModelIndex);
			indexMap.put(prevIndex, normalModelIndex);
			
			if(i > counter)  {
				checkHeap(i, indices.size());
				counter += 250000;
			}
		}
		if(!hasNormal) {
			nres.calcNormals();
			for(int i = 0; i < res.getPos().size(); i++) res.getNormal().add(nres.getNormal().get(indexMap.get(i)));
		}
		
		nres.calcTangents();
		for(int i = 0; i < res.getPos().size(); i++) res.getTangent().set(i, nres.getTangent().get(indexMap.get(i)));
		
		nres.clear();
		indexResMap.clear();
		indexNormalMap.clear();
		indexMap.clear();
		
		checkHeap(indices.size(), indices.size());
		return res;
	}
	
	private final int mega = 1024*1024;
	private void checkHeap(int index, int size) {
		Runtime runtime = Runtime.getRuntime();
		runtime.gc();
		long free = runtime.freeMemory() / mega;
		long total = runtime.totalMemory() / mega;
		long used = total - free / mega;
		long max = runtime.maxMemory() / mega;
		System.out.println(index + " out of " + size + " --- Heap Space >>> Max: " + max + " Total: " + total + " Used: " + used + " Free: " + free);
	}
	
	public void clear() {
		this.indices.clear();
		this.pos.clear();
		this.normal.clear();
		this.textureUV.clear();
	}
}
