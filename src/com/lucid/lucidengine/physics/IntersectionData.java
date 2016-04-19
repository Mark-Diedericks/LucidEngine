package com.lucid.lucidengine.physics;

import com.lucid.lucidengine.mathematics.Vector3f;

/**
 * @author Mark Diedericks
 *
 */

public class IntersectionData implements ICollisionData {
	
	private boolean isIntersect;
	private Vector3f direction;
	private Vector3f collidingAxis;
	private Vector3f normal;
	private Vector3f intersectionPoint;
	
	public IntersectionData(boolean isIntersect, Vector3f direction, Vector3f normal, Vector3f intersectionPoint) {
		this.isIntersect = isIntersect;
		this.normal = normal;
		this.intersectionPoint = intersectionPoint;
		this.direction = direction;
		this.collidingAxis = direction;
	}
	
	public IntersectionData(boolean isIntersect, Vector3f direction, Vector3f collidingAxis) {
		this.isIntersect = isIntersect;
		this.direction = direction;
		this.collidingAxis = collidingAxis;
	}
	
	public IntersectionData(boolean isIntersect, Vector3f direction) {
		this.isIntersect = isIntersect;
		this.direction = direction;
		this.collidingAxis = direction;
	}
	
	public boolean isIntersect() {
		return isIntersect;
	}
	
	public float getDistance() {
		return direction.getLength();
	}

	public Vector3f getDirection() {
		return direction;
	}

	public Vector3f getCollidingAxis() {
		return collidingAxis;
	}
	
	public Vector3f getNormal() {
		return normal;
	}
	
	public Vector3f getIntersectionPoint() {
		return intersectionPoint;
	}
	
	public void setIntersect(boolean isIntersect) {
		this.isIntersect = isIntersect;
	}
}
