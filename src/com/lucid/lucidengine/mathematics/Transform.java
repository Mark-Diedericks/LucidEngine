package com.lucid.lucidengine.mathematics;

/**
 * @author Mark Diedericks
 *
 */

public class Transform {
	
	private Vector3f pos;
	private Quaternion rotation;
	private Vector3f scale;
	
	private Vector3f oldPos;
	private Quaternion oldRotation;
	private Vector3f oldScale;
	
	private Matrix4f currentMatrix;
	private Transform parent;
	private boolean identity;
	
	public Transform() {
		pos = new Vector3f(0, 0, 0);
		rotation = new Quaternion(0, 0, 0, 1);
		scale = new Vector3f(1, 1, 1);
		oldPos = new Vector3f(0,0,0).set(pos).add(1f);
		oldRotation = new Quaternion(0,0,0,0).set(rotation).mul(0.5f);
		oldScale = new Vector3f(0,0,0).set(scale).add(1f);
		this.currentMatrix = new Matrix4f().initIdentity();
		
	}
	
	public boolean hasChanged()
	{
		if(parent != null && parent.hasChanged())
			return true;
		if(!pos.equals(oldPos)) 
			return true;
		if(!rotation.equals(oldRotation)) 
			return true;
		if(!scale.equals(oldScale))
			return true;
		return false;
	}

	public Matrix4f getTransformation()
	{
		if(hasChanged()) {
			Matrix4f translationMatrix = new Matrix4f().initTranslation(pos.getX(), pos.getY(), pos.getZ());
			Matrix4f rotationMatrix = rotation.toRotationMatrix();
			Matrix4f scaleMatrix = new Matrix4f().initScale(scale.getX(), scale.getY(), scale.getZ());
			this.currentMatrix = getParentMatrix().mul(translationMatrix.mul(rotationMatrix.mul(scaleMatrix)));
			oldPos.set(pos);
			oldRotation.set(rotation);
			oldScale.set(scale);
		}
		if(identity) return new Matrix4f().initIdentity();
		return currentMatrix;
	}
	
	public void setIdentity(boolean identity) {
		this.identity = identity;
	}
	
	public Matrix4f getParentMatrix() {
		if(this.parent != null) return parent.getTransformation();
		else return new Matrix4f().initIdentity();
	}
	
	public Vector3f getTransformedPos() {
		return this.getParentMatrix().transform(this.pos);
	}
	
	public Quaternion getTransformedRot()
	{
		Quaternion parentRotation = new Quaternion(0,0,0,1);

		if(parent != null)
			parentRotation = parent.getTransformedRot();

		return parentRotation.mul(this.rotation);
	}
	
	public void move(Vector3f dir, float amt) {
		pos.set(pos.add(dir.mul(amt)));
	}
	
	public void aimAt(Vector3f p, Vector3f up) {
		rotation = getAimDirection(p, up);
	}
	
	public Quaternion getAimDirection(Vector3f p, Vector3f up) {
		return new Quaternion(new Matrix4f().initRotation(p.sub(pos).normalize(), up));
	}
	
	public void setParent(Transform parent) {
		this.parent = parent;
	}
	
	public Vector3f getTransform() {
		return pos;
	}
	
	public Quaternion getRotation() {
		return rotation;
	}

	public void setRotation(Quaternion rotation) {
		this.rotation = rotation;
	}

	public Vector3f getScale() {
		return scale;
	}

	public Vector3f getPos() {
		return this.pos;
	}
}
