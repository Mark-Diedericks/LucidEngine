package com.lucid.lucidengine.physics;

import java.util.ArrayList;
import com.lucid.lucidengine.main.EngineCore;
import com.lucid.lucidengine.rendering.RenderingEngine;

/**
 * @author Mark Diedericks
 *
 */

public class PhysicsEngine {
	
	private int staticId = 0;
	private int dynamicId = 0;
	private ArrayList<PhysicsObject> staticObjects;
	private ArrayList<PhysicsObject> dynamicObjects;
	private ArrayList<CollisionPair> collidingPairs;
	
	private EngineCore engineCore;
	
	public PhysicsEngine(EngineCore engineCore) {
		this.engineCore = engineCore;
		staticObjects = new ArrayList<PhysicsObject>();
		dynamicObjects = new ArrayList<PhysicsObject>();
		collidingPairs = new ArrayList<CollisionPair>();
	}
	
	public void Simulate(float delta) {
		for(PhysicsObject obj : dynamicObjects) {
			obj.Intergrate(delta);
		}
		
		physics(delta);
	}
	
	private void physics(float delta) {
		broadphaseCollision();
		narrowphaseCollision(delta);
	}
	
	public PhysicsObject getStaticPhysicsObject(int id) {
		for(PhysicsObject obj : staticObjects) {
			if(obj.id == id)
				return obj;
		}
		return null;
	}
	
	public PhysicsObject getDynamicPhysicsObject(int id) {
		for(PhysicsObject obj : dynamicObjects) {
			if(obj.id == id)
				return obj;
		}
		return null;
	}
	
	public void broadphaseCollision() {
		for(int i = 0; i < dynamicObjects.size(); i++) {
			for(int j = 0; j < staticObjects.size(); j++) {
				IntersectionData intersect = (IntersectionData)dynamicObjects.get(i).getBroadphaseCollider().Intersect(staticObjects.get(j).getBroadphaseCollider());
				
				if(intersect.isIntersect()) {
					collidingPairs.add(new CollisionPair(dynamicObjects.get(i), staticObjects.get(j)));
				}
			}
			
			for(int k = i + 1; k < dynamicObjects.size(); k++) {
				IntersectionData intersect = (IntersectionData)dynamicObjects.get(i).getBroadphaseCollider().Intersect(dynamicObjects.get(k).getBroadphaseCollider());
				
				if(intersect.isIntersect()) {
					collidingPairs.add(new CollisionPair(dynamicObjects.get(i), staticObjects.get(k)));
				}
			}
		}
	}
	
	public void narrowphaseCollision(float delta) {
		for(CollisionPair cp : collidingPairs) {
			ICollisionData intersects = cp.getIntersectionDataOne();
			
			if(intersects instanceof IntersectionData) {
				IntersectionData intersect = (IntersectionData)intersects;
				if(intersect.isIntersect()) {
					cp.object2.handleCollision(intersect, delta, cp.object1);
					cp.object1.handleCollision(cp.getIntersectionDataTwo(), delta, cp.object2);
				}
			} else if(intersects instanceof AdvancedIntersectionData) {
				AdvancedIntersectionData intersect = (AdvancedIntersectionData)intersects;
				if(intersect.isIntersect()) {
					cp.object2.handleCollision(intersect, delta, cp.object1);
					cp.object1.handleCollision(cp.getIntersectionDataTwo(), delta, cp.object2);
				}
			}
		}
		
		collidingPairs.clear();
	}
	
	public void recur(CollisionPair cp) {
		ICollisionData intersects = cp.getIntersectionDataOne();
		float delta = 0.01f;
		
		if(intersects instanceof IntersectionData) {
			IntersectionData intersect = (IntersectionData)intersects;
			if(intersect.isIntersect()) {
				cp.object2.handleCollision(intersect, delta, cp.object1);
				cp.object1.handleCollision(cp.getIntersectionDataTwo(), delta, cp.object2);
			}
		} else if(intersects instanceof AdvancedIntersectionData) {
			AdvancedIntersectionData intersect = (AdvancedIntersectionData)intersects;
			if(intersect.isIntersect()) {
				cp.object2.handleCollision(intersect, delta, cp.object1);
				cp.object1.handleCollision(cp.getIntersectionDataTwo(), delta, cp.object2);
			}
		}
	}
	
	public void addStaticObject(PhysicsObject obj) {
		obj.setPhysicsEngine(this);
		this.staticObjects.add(obj);
	}
	
	public void addDynamicObject(PhysicsObject obj) {
		obj.setPhysicsEngine(this);
		this.dynamicObjects.add(obj);
	}
	
	public EngineCore getEngineCore() {
		return engineCore;
	}
	
	public RenderingEngine getRenderingEngine() {
		return engineCore.getRenderingEngine();
	}

	public int getNumberOfObjects() {
		return staticObjects.size() + dynamicObjects.size();
	}
	
	public int getNumberOfStaticObjects() {
		return staticObjects.size();
	}
	
	public int getNumberOfDynamicObjects() {
		return dynamicObjects.size();
	}
	
	public int getUniqueStaticPhysicsID() {
		return staticId++;
	}
	
	public int getUniqueDynamicPhysicsID() {
		return dynamicId++;
	}
}
