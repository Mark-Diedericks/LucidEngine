package com.lucid.lucidengine.physics;

import com.lucid.lucidengine.mathematics.Quaternion;
import com.lucid.lucidengine.mathematics.Vector3f;

/**
 * @author Mark Diedericks
 *
 */

public class PhysicsObject {
	
	private Vector3f position;
	private Vector3f oldPosition;
	private Vector3f velocity;
	private Collider collider;
	private Collider broadphaseCollider;
	private Vector3f collidingAxis;
	private CollisionAttributes attrib;
	
	private PhysicsEngine physicsEngine;
	
	private Quaternion rotation;
	private Vector3f scale;
	
	private ICollisionHandler customCollision;
	
	public final int id;
	
	private boolean collided;
	
	public PhysicsObject(Collider broadphaseCollider, Collider collider, Vector3f start, Vector3f velocity, int id) {
		this.position = start;
		this.oldPosition = start;
		this.velocity = velocity;
		this.broadphaseCollider = broadphaseCollider;
		this.collider = collider;
		this.id = id;
		this.scale = new Vector3f(1, 1, 1);
		this.rotation = new Quaternion(0, 0, 0, 1);
		
		this.broadphaseCollider.setParent(this);
		this.collider.setParent(this);
	}
	
	public void Intergrate(float delta) {
		this.oldPosition = position;
		if(velocity.isNaN()) velocity.setVector(0f, 0f, 0f);
		position.addEq(velocity.mul(delta));
		this.collider.setVelocity(velocity);
	}
	
	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getVelocity() {
		return velocity;
	}
	
	public Vector3f getScale() {
		return scale;
	}
	
	public Quaternion getRotation() {
		return rotation;
	}
	
	public Collider getCollider() {
		this.collider.setPosition(this.position);
		this.collider.setRotation(this.rotation);
		this.collider.setScale(this.scale);
		return collider;
	}
	
	public void handleCollision(ICollisionData data, float delta, PhysicsObject other) {
		if(customCollision != null){
			customCollision.handleCollision(data, delta, this, other);
		} else {
			if(data instanceof IntersectionData) this.getVelocity().reflect(((IntersectionData)data).getDirection());
		}
	}
	
	public PhysicsObject setAtrributes(CollisionAttributes attrib) {
		this.attrib = attrib;
		return this;
	}
	
	public CollisionAttributes getCollisionAttributes() {
		return this.attrib;
	}

	public void setPosition(Vector3f pos) {
		this.oldPosition = position;
		this.position = pos;
		this.velocity = oldPosition.sub(position);
	}

	public void setCollided(boolean intersect) {
		this.collided = intersect;
	}
	
	public void setScale(Vector3f scale) {
		this.scale = scale;
	}
	
	public void setRotation(Quaternion rotation) {
		this.rotation = rotation;
	}
	
	public boolean hasCollided() {
		return collided;
	}
	
	public Vector3f getCollidingAxis() {
		return this.collidingAxis;
	}
	
	public void setCollidingAxis(Vector3f collidingAxis) {
		this.collidingAxis = collidingAxis;
	}
	
	public Collider getBroadphaseCollider() {
		this.broadphaseCollider.setPosition(this.position);
		return broadphaseCollider;
	}
	
	public void setCustomCollision(ICollisionHandler customCollision) {
		this.customCollision = customCollision;
	}
	
	public ICollisionHandler getCustomCollision() {
		return customCollision;
	}

	public void setVelocity(Vector3f velocity) {
		this.velocity = velocity;
	}
	
	public PhysicsEngine getPhysicsEngine() {
		return physicsEngine;
	}
	
	public void setPhysicsEngine(PhysicsEngine physicsEngine) {
		this.physicsEngine = physicsEngine;
	}
}
