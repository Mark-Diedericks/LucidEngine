package com.lucid.lucidengine.physics;

import com.lucid.lucidengine.mathematics.Vector3f;
import com.lucid.lucidengine.mathematics.Vertex;
import com.lucid.lucidengine.rendering.mesh.StaticMesh;

public class BoundingMesh extends Collider {
	
	private StaticMesh colliding;
	
	public BoundingMesh(StaticMesh collidingMesh) {
		super(Type.MESH);
		this.colliding = collidingMesh;
	}
	
	public StaticMesh getCollidingMesh() {
		return this.colliding;
	}
	
	public void setColliding(StaticMesh colliding) {
		this.colliding = colliding;
	}
	
	public ICollisionData intersectRigidBody(BoundingMesh other) {
		IntersectionData intersection = new IntersectionData(false, new Vector3f(999999999999999999f, 99999999999999999999f, 99999999999999999999f), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
		
		Vertex[] oneV = this.getCollidingMesh().getVertices();
		int[] oneI = this.getCollidingMesh().getIndices();
		
		Vertex[] twoV = other.getCollidingMesh().getVertices();
		int[] twoI = other.getCollidingMesh().getIndices();
		
		for(int i = 0; i <  this.getCollidingMesh().getIndices().length; i+=3) {
			BoundingTriangle t1 = new BoundingTriangle(transformed(oneV[oneI[i+0]].getPos(), this), transformed(oneV[oneI[i+1]].getPos(), this), transformed(oneV[oneI[i+2]].getPos(), this), this.getCenter());	
			for(int j = 0; j < other.getCollidingMesh().getIndices().length; j+=3) {
				BoundingTriangle t2 = new BoundingTriangle(transformed(twoV[twoI[j+0]].getPos(), other), transformed(twoV[twoI[j+1]].getPos(), other), transformed(twoV[twoI[j+2]].getPos(), other), other.getCenter());
				
				ICollisionData tD = t1.intersectTriangle(t2);
				if(tD instanceof IntersectionData) {
					if((intersection.isIntersect() == false || ((IntersectionData)tD).getDistance() < intersection.getDistance()) && ((IntersectionData)tD).isIntersect()) {
						intersection = ((IntersectionData)tD);
					}
				}
			}
		}
		
		return intersection;
	}
	
	public ICollisionData intersectSphere(BoundingSphere other) {
		AdvancedIntersectionData intersection = new AdvancedIntersectionData();
		
		Vertex[] oneV = this.getCollidingMesh().getVertices();
		int[] oneI = this.getCollidingMesh().getIndices();
		
		for(int i = 0; i <  this.getCollidingMesh().getIndices().length; i+=3) {
			BoundingTriangle t1 = new BoundingTriangle(transformed(oneV[oneI[i+0]].getPos(), this), transformed(oneV[oneI[i+1]].getPos(), this), transformed(oneV[oneI[i+2]].getPos(), this), this.getCenter());	

			ICollisionData tD = t1.intersectSphere(other);
			if(tD instanceof IntersectionData) {
				if(((IntersectionData)tD).isIntersect()) {
					intersection.addIntersect(((IntersectionData)tD));
				}
			}
		}
		
		return intersection;
	}
	
	public ICollisionData intersectEllipsiod(BoundingEllipsiod other) {
		AdvancedIntersectionData intersection = new AdvancedIntersectionData();
		
		Vertex[] oneV = this.getCollidingMesh().getVertices();
		int[] oneI = this.getCollidingMesh().getIndices();
		
		for(int i = 0; i <  this.getCollidingMesh().getIndices().length; i+=3) {
			BoundingTriangle t1 = new BoundingTriangle(transformed(oneV[oneI[i+0]].getPos(), this), transformed(oneV[oneI[i+1]].getPos(), this), transformed(oneV[oneI[i+2]].getPos(), this), this.getCenter());	

			ICollisionData tD = t1.intersectEllipsiod(other);
			if(tD instanceof IntersectionData) {
				if(((IntersectionData)tD).isIntersect()) {
					intersection.addIntersect(((IntersectionData)tD));
				}
			}
		}
		
		return intersection;
	}
	
	private Vector3f transformed(Vector3f vec, BoundingMesh body) {
		return vec.mul(body.getScale()).rotate(body.getRotation()).add(body.getCenter());
	}
	
}
