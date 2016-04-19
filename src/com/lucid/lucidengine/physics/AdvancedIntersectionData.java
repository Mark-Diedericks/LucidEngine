package com.lucid.lucidengine.physics;

import java.util.ArrayList;

public class AdvancedIntersectionData implements ICollisionData {
	
	private ArrayList<IntersectionData> intersects;
	private boolean intersect;
	
	public AdvancedIntersectionData() {
		intersects = new ArrayList<IntersectionData>();
	}
	
	public ArrayList<IntersectionData> getIntersects() {
		return this.intersects;
	}
	
	public void addIntersect(IntersectionData data) {
		intersects.add(data);
		if(data.isIntersect()) intersect = true;
	}
	
	public boolean isIntersect() {
		return intersect;
	}
	
}
