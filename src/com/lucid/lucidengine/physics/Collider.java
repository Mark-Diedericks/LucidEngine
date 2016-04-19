package com.lucid.lucidengine.physics;

import com.lucid.lucidengine.mathematics.Quaternion;
import com.lucid.lucidengine.mathematics.Vector3f;

/**
 * @author Mark Diedericks
 *
 */

public class Collider {
	
	private Type type;
	protected PhysicsObject parent;
	
	protected Vector3f position = new Vector3f(0, 0, 0);
	protected Quaternion rotation = new Quaternion(0, 0, 0, 1);
	protected Vector3f scale = new Vector3f(1, 1, 1);
	protected Vector3f velocity = new Vector3f(0, 0, 0);
	
	public enum Type {
        SPHERE,
        PLANE,
        AABB,
        MESH,
        ELLIPSIOD;
	};   
	
	public Collider(Type type) {
		this.type = type;
	}
	
	public void setParent(PhysicsObject parent) {
		this.parent = parent;
	}
	
	public Type getType() {
		return this.type;
	}
	
	public void Transform(Vector3f translation) {
		this.position = translation;
	}
	
	public Vector3f getCenter() {
		return this.position;
	}
	
	public Quaternion getRotation() {
		return rotation;
	}
	
	public Vector3f getScale() {
		return scale;
	}
	
	public Vector3f getVelocity() {
		return velocity;
	}
	
	public ICollisionData Intersect(Collider other) {
		
		//SPHERE SPHERE
		if(this.type == Type.SPHERE && other.getType() == Type.SPHERE) {
			BoundingSphere me = (BoundingSphere)this;
			return me.IntersectBoundingSphere((BoundingSphere)other);
		}
		
		//AABB AABB
		if(this.type == Type.AABB && other.getType() == Type.AABB) {
			AxisAlignedBoundingBox me = (AxisAlignedBoundingBox)this;
			return me.IntersectAABB((AxisAlignedBoundingBox)other);
		}
		
		//MESH MESH
		if(this.type == Type.MESH && other.type == Type.MESH) {
			BoundingMesh me = (BoundingMesh)this;
			return me.intersectRigidBody((BoundingMesh)other);
		}
		
		//--------------------------------------------------------------\\
		
		//PLANE SPHERE --- SPHERE PLANE
		if(this.type == Type.PLANE && other.getType() == Type.SPHERE) {
			BoundingPlane me = (BoundingPlane)this;
			return me.IntersectSphere((BoundingSphere)other);
		}
		
		if(this.type == Type.SPHERE && other.getType() == Type.PLANE) {
			BoundingPlane me = (BoundingPlane)other;
			return me.IntersectSphere((BoundingSphere)this);
		}
		
		//MESH SPHERE --- SPHERE MESH
		if(this.type == Type.MESH && other.type == Type.SPHERE) {
			BoundingMesh me = (BoundingMesh)this;
			return me.intersectSphere((BoundingSphere)other);
		}
		
		if(this.type == Type.SPHERE && other.type == Type.MESH) {
			BoundingMesh me = (BoundingMesh)other;
			return me.intersectSphere((BoundingSphere)this);
		}
		
		//MESH ELLIPSIOD --- SPHERE ELLIPSIOD
		if(this.type == Type.MESH && other.type == Type.ELLIPSIOD) {
			BoundingMesh me = (BoundingMesh)this;
			return me.intersectEllipsiod((BoundingEllipsiod)other);
		}
		
		if(this.type == Type.ELLIPSIOD && other.type == Type.MESH) {
			BoundingMesh me = (BoundingMesh)other;
			return me.intersectEllipsiod((BoundingEllipsiod)this);
		}
		
		System.out.println("Invalid Collision between: " + this.type + " and " + other.getType());
		return null;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	public void setRotation(Quaternion rotation) {
		this.rotation = rotation;
	}
	
	public void setScale(Vector3f scale) {
		this.scale = scale;
	}
	
	public void setVelocity(Vector3f velocity) {
		this.velocity = velocity;
	}
}
