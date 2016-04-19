package com.lucid.lucidengine.rendering.mesh;

import java.util.ArrayList;
import com.lucid.lucidengine.mathematics.Vector2f;
import com.lucid.lucidengine.mathematics.Vector3f;

/**
 * @author Mark Diedericks
 *
 */

public class IndexModel {
	
	private ArrayList<Vector3f> pos;
	private ArrayList<Vector2f> textureUV;
	private ArrayList<Vector3f> normal;
	private ArrayList<Vector3f> tangent;
	private ArrayList<Integer> indices;
	
	public IndexModel() {
		pos = new ArrayList<Vector3f>();
		textureUV = new ArrayList<Vector2f>();
		normal = new ArrayList<Vector3f>();
		tangent = new ArrayList<Vector3f>();
		indices = new ArrayList<Integer>();
	}
	
	public void calcNormals() {
		for(int i = 0; i < indices.size(); i += 3)
		{
			int i0 = indices.get(i);
			int i1 = indices.get(i + 1);
			int i2 = indices.get(i + 2);

			Vector3f v1 = pos.get(i1).sub(pos.get(i0));
			Vector3f v2 = pos.get(i2).sub(pos.get(i0));

			Vector3f normalized = v1.cross(v2).normalize();

			normal.get(i0).set(normal.get(i0).add(normalized));
			normal.get(i1).set(normal.get(i1).add(normalized));
			normal.get(i2).set(normal.get(i2).add(normalized));
		}

		for(int i = 0; i < normal.size(); i++)
			normal.get(i).set(normal.get(i).normalize());
	}
	
	public void calcTangents() {
		for(int i = 0; i < indices.size(); i += 3)
		{
			int i0 = indices.get(i);
			int i1 = indices.get(i + 1);
			int i2 = indices.get(i + 2);

			Vector3f edge1 = pos.get(i1).sub(pos.get(i0));
			Vector3f edge2 = pos.get(i2).sub(pos.get(i0));

			float deltaU1 = textureUV.get(i1).getX() - textureUV.get(i0).getX();
			float deltaV1 = textureUV.get(i1).getY() - textureUV.get(i0).getY();
			float deltaU2 = textureUV.get(i2).getX() - textureUV.get(i0).getX();
			float deltaV2 = textureUV.get(i2).getY() - textureUV.get(i0).getY();

			float f = 1.0f/(deltaU1*deltaV2 - deltaU2*deltaV1);

			Vector3f tan = new Vector3f(0,0,0);
			tan.setX(f * (deltaV2 * edge1.getX() - deltaV1 * edge2.getX()));
			tan.setY(f * (deltaV2 * edge1.getY() - deltaV1 * edge2.getY()));
			tan.setZ(f * (deltaV2 * edge1.getZ() - deltaV1 * edge2.getZ()));

			tangent.get(i0).set(tangent.get(i0).add(tan));
			tangent.get(i1).set(tangent.get(i1).add(tan));
			tangent.get(i2).set(tangent.get(i2).add(tan));
		}

		for(int i = 0; i < tangent.size(); i++)
			tangent.get(i).set(tangent.get(i).normalize());
	}
	
	public void clear() {
		this.indices.clear();
		this.pos.clear();
		this.normal.clear();
		this.textureUV.clear();
	}
	
	public ArrayList<Vector3f> getPos() {
		return pos;
	}

	public ArrayList<Vector2f> getTextureUV() {
		return textureUV;
	}

	public ArrayList<Vector3f> getNormal() {
		return normal;
	}
	
	public ArrayList<Vector3f> getTangent() {
		return tangent;
	}

	public ArrayList<Integer> getIndices() {
		return indices;
	}

	public void setPos(ArrayList<Vector3f> pos) {
		this.pos = pos;
	}

	public void setTextureUV(ArrayList<Vector2f> textureUV) {
		this.textureUV = textureUV;
	}

	public void setNormal(ArrayList<Vector3f> normal) {
		this.normal = normal;
	}

	public void setTangent(ArrayList<Vector3f> tangent) {
		this.tangent = tangent;
	}

	public void setIndices(ArrayList<Integer> indices) {
		this.indices = indices;
	}
}
