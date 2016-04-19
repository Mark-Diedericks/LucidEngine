package com.lucid.lucidengine.physics;

import com.lucid.lucidengine.mathematics.Vector3f;

public class NarrowPhaseUtils {
	
	public static boolean insidePolygon(Vector3f vIntersection, Vector3f[] polygons, int vertexCount) {
		final double MATCH_FACTOR = 0.99;
		double Angle = 0.0;
		Vector3f p1, p2;
		
		for(int i = 0; i < vertexCount; i++) {
			p1 = polygons[i].sub(vIntersection);
			p2 = polygons[(i + 1) % vertexCount].sub(vIntersection);
			
			Angle += angleBetweenVectors(p1, p2);
		}
		
		if(Angle >= (MATCH_FACTOR * (2.0 * Math.PI)))
			return true;
		
		return false;
	}
	
	public static boolean edgeEllipsiodCollision(Vector3f center, Vector3f[] vertices, int vertexCount, Vector3f radius) {
		Vector3f vPoint;
		
		for(int i = 0; i < vertexCount; i++) {
			
			vPoint = closestPointOnLine(vertices[i], vertices[(i + 1) % vertexCount], center);
			float distance = vPoint.sub(center).getLength();
			
			if(distance < radius.getLength())
				return true;
		}
		
		return false;
	}
	
	private static Vector3f closestPointOnLine(Vector3f p1, Vector3f p2, Vector3f vPoint) {
		Vector3f vector1 = vPoint.sub(p1);
		Vector3f vector2 = p2.sub(p1).normalize();
		
		float d = p1.sub(p2).getLength();
		
		float t = vector2.dot(vector1);
		
		if(t <= 0.0f)
			return p1;
		
		if(t >= d)
			return p2;
		
		Vector3f vector3 = vector2.mul(t);
		Vector3f closestPoint = p1.add(vector3);
		
		return closestPoint;
	}
	
	private static double angleBetweenVectors(Vector3f p1, Vector3f p2) {
		float dot = p1.dot(p2);
		
		float vectorMag = p1.getLength() * p2.getLength();
		
		double angle = Math.acos(dot / vectorMag);
		
		 if(Double.isNaN(angle))
			 return 0;
		 
		 return angle;
	}
}
