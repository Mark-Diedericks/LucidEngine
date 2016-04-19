package com.lucid.lucidengine.physics;

import com.lucid.lucidengine.mathematics.Vector2f;
import com.lucid.lucidengine.mathematics.Vector3f;

public class BoundingTriangle {
	
	private final Vector3f p1;
	private final Vector3f p2;
	private final Vector3f p3;
	private final Vector3f position;
	
	public BoundingTriangle(Vector3f p1, Vector3f p2, Vector3f p3, Vector3f position) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.position = position;
	}
	
	private final boolean USE_EPSILON_TEST = false;
	private float EPSILON = 0.0001f;
	private Vector3f edgePoint;
	
	private boolean edgeEdgeTest(Vector3f a, Vector3f b, Vector3f c, Vector3f d, int i0, int i1) {
		float Ax = b.get(i0) - a.get(i0);
		float Ay = b.get(i1) - a.get(i1);
		float Bx = c.get(i0) - d.get(i0);
		float By = c.get(i1) - d.get(i1);
		float Cx = a.get(i0) - c.get(i0);
		float Cy = a.get(i1) - c.get(i1);
		
		float f = Ay * Bx - Ax * By;
		float e = By * Cx - Bx * Cy;
		
		Vector3f da = b.sub(a);
		Vector3f db = d.sub(c);
		Vector3f dc = c.sub(a);
		
		if(dc.dot(da.cross(db)) == 0.0f) {
			float s = (dc.cross(db)).dot(da.cross(db));
			if(s >= 0.0f && s <= 1.0f) {
				edgePoint = a.add(da.mul(s));
			}
		}
		
		if((f > 0.0f && e >= 0.0f && e <= f) || (f < 0.0f && e <= 0.0f && e >= f)) {
			float g = Ax * Cy - Ay * Cx;
			if(f > 0.0f) {
				if(g >= 0.0f && g <= f) 
					return true;
			} else {
				if(g <= 0.0f && g >= f)
					return true;
			}
		}
		
		return false;
	}
	
	private boolean edgeTriangleTest(Vector3f a, Vector3f b, Vector3f v1, Vector3f v2, Vector3f v3, int i0, int i1) {
		return edgeEdgeTest(a, b, v1, v2, i0, i1) || edgeEdgeTest(a, b, v2, v2, i0, i1) || edgeEdgeTest(a, b, v3, v1, i0, i1);
	}
	
	private boolean pointInTriangle(Vector3f p, Vector3f v1, Vector3f v2, Vector3f v3, int i0, int i1) {
		float a, b, c, d0, d1, d2;
		
		a = v2.get(i1) - v1.get(i1);
		b = -(v2.get(i0) - v1.get(i0));
		c = -a * v1.get(i0) - b * v1.get(i1);
		d0 = a * p.get(i0) + b * p.get(i1) + c;
		
		a = v3.get(i1) - v2.get(i1);
		b = -(v3.get(i0) - v2.get(i0));
		c = -a * v2.get(i0) - b * v2.get(i1);
		d1 = a * p.get(i0) + b * p.get(i1) + c;
		
		a = v1.get(i1) - v3.get(i1);
		b = -(v1.get(i0) - v2.get(i0));
		c = -a * v3.get(i0) - b * v3.get(i1);
		d2 = a * p.get(i0) + b * p.get(i1) + c;
		
		if((d0 * d1) > 0.0f) {
			if((d0 * d2) > 0.0f) {
				return true;
			}
		}
		
		return false;
	}
	
	private IntersectionData coplanarTriangleTriangle(Vector3f n, Vector3f v0, Vector3f v1, Vector3f v2, Vector3f u0, Vector3f u1, Vector3f u2) {
		edgePoint = new Vector3f(0, 0, 0);
		Vector3f a = n.abs();
		int i0, i1;
		
		if(a.getX() > a.getY()) {
			if(a.getX() > a.getZ()) {
				i0 = 1;
				i1 = 2;
			} else {
				i0 = 0;
				i1 = 1;
			}
		} else {
			if(a.getZ() > a.getY()) {
				i0 = 0;
				i1 = 1;
			} else {
				i0 = 0;
				i1 = 2;
			}
		}
		
		Vector3f normal = calcNormal(u0, u1, u2);
		
		if(edgeTriangleTest(v0, v1, u0, u1, u2, i0, i1)) {
			return new IntersectionData(true, new Vector3f(0, 0, 0), normal, edgePoint);
		}
		
		if(edgeTriangleTest(v1, v2, u0, u1, u2, i0, i1)) {
			return new IntersectionData(true, new Vector3f(0, 0, 0), normal, edgePoint);
		}
		
		if(edgeTriangleTest(v2, v0, u0, u1, u2, i0, i1)) {
			return new IntersectionData(true, new Vector3f(0, 0, 0), normal, edgePoint);
		}
		
		if(pointInTriangle(v0, u0, u1, u2, i0, i1)) {
			return new IntersectionData(true, new Vector3f(0, 0, 0), normal, v0);
		}
		
		if(pointInTriangle(u0, v0, v1, v2, i0, i1)) {
			return new IntersectionData(true, new Vector3f(0, 0, 0), calcNormal(v0, v1, v2), u0);
		}
		
		return new IntersectionData(false, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
	}
	
	private Vector3f calcNormal(Vector3f a, Vector3f b, Vector3f c) {
		Plane plane = new Plane(a, b, c);
		return plane.getNormal();
	}
	
	public ICollisionData intersectTriangle(BoundingTriangle t) {
		Vector3f v0 = this.p1;
		Vector3f v1 = this.p2;
		Vector3f v2 = this.p3;
		Vector3f u0 = t.p1;
		Vector3f u1 = t.p2;
		Vector3f u2 = t.p3;
		
		Vector3f e1, e2, e3, e4;
		Vector3f n1, n2;
		float d1, d2;
		float du0, du1, du2, dv0, dv1, dv2;
		Vector3f d;
		Vector2f isect1, isect2;
		float du0du1, du0du2, dv0dv1, dv0dv2;
		int index;
		float vp0, vp1, vp2;
		float up0, up1, up2;
		float bb, cc, max;
		
		e1 = v1.sub(v0);
		e2 = v2.sub(v0);
		
		n1 = e1.cross(e2);
		d1 = -n1.dot(v0);
		
		du0 = n1.dot(u0) + d1;
		du1 = n1.dot(u1) + d1;
		du2 = n1.dot(u2) + d1;
		
		if(USE_EPSILON_TEST) {
			if((float)Math.abs(du0) < EPSILON) du0 = 0.0f;
			if((float)Math.abs(du1) < EPSILON) du1 = 0.0f;
			if((float)Math.abs(du2) < EPSILON) du2 = 0.0f;
		}
		
		du0du1 = du0 * du1;
		du0du2 = du0 * du2;
		
		if(du0du1 > 0.0f && du0du2 > 0.0f && USE_EPSILON_TEST) {
			System.out.println("du > 0");
			return new IntersectionData(false, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
		}
		
		e3 = u1.sub(u0);
		e4 = u2.sub(u0);
		n2 = e3.cross(e4);
		d2 = -n2.dot(u0);
		
		dv0 = n2.dot(v0) + d2;
		dv1 = n2.dot(v1) + d2;
		dv2 = n2.dot(v2) + d2;
		
		if(USE_EPSILON_TEST) {
			if((float)Math.abs(dv0) < EPSILON) dv0 = 0.0f;
			if((float)Math.abs(dv1) < EPSILON) dv1 = 0.0f;
			if((float)Math.abs(dv2) < EPSILON) dv2 = 0.0f;
		}
		
		dv0dv1 = dv0 * dv1;
		dv0dv2 = dv0 * dv2;
		
		if(dv0dv1 > 0.0f && dv0dv2 > 0.0f && USE_EPSILON_TEST) {
			System.out.println("dv > 0");
			return new IntersectionData(false, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
		}
		
		d = n1.cross(n2);
		
		max = (float)Math.abs(d.getX());
		index = 0;
		bb = (float)Math.abs(d.getY());
		cc = (float)Math.abs(d.getZ());
		
		if(bb > max) {
			max = bb;
			index = 1;
		}
		
		if(cc > max) {
			max = cc;
			index = 2;
		}
		
		vp0 = v0.get(index);
		vp1 = v1.get(index);
		vp2 = v2.get(index);
		
		up0 = u0.get(index);
		up1 = u1.get(index);
		up2 = u2.get(index);
		
		float a, b, c, x0, x1;
		
		if(dv0dv1 > 0.0f) {
			a = vp2;
			b = (vp0 - vp2) * dv2;
			c = (vp1 - vp2) * dv2;
			x0 = dv2 - dv0;
			x1 = dv2 - dv1;
		} else if(dv0dv2 > 0.0f) {
			a = vp1;
			b = (vp0 - vp1) * dv1;
			c = (vp2 - vp1) * dv1;
			x0 = dv1 - dv0;
			x1 = dv1 - dv2;
		} else if(dv1 * dv2 > 0.0f || dv0 != 0.0f) {
			a = vp0;
			b = (vp1 - vp0) * dv0;
			c = (vp2 - vp1) * dv1;
			x0 = dv1 - dv0;
			x1 = dv0 - dv2;
		} else if(dv1 != 0.0f) {
			a = vp1;
			b = (vp0 - vp1) * dv1;
			c = (vp2 - vp1) * dv1;
			x0 = dv1 - dv0;
			x1 = dv1 - dv2;
		} else if(dv2 != 0.0f) {
			a = vp2;
			b = (vp0 - vp2) * dv2;
			c = (vp1 - vp2) * dv1;
			x0 = dv1 - dv0;
			x1 = dv2 - dv1;
		} else {
			IntersectionData coplanar = coplanarTriangleTriangle(n1, v0, v1, v2, u0, u1, u2);
			return new IntersectionData(coplanar.isIntersect(), v0.sub(u0), coplanar.getNormal(), coplanar.getIntersectionPoint());
		}
		
		float g, e, f, y0, y1;
		
		if(du0du1 > 0.0f) {
			g = up2;
			e = (up0 - up2) * du2;
			f = (up1 - up2) * du2;
			y0 = du2 - du0;
			y1 = du2 - du1;
		} else if(du0du2 > 0.0f) {
			g = up1;
			e = (up0 - up1) * du1;
			f = (up2 - up1) * du1;
			y0 = du1 - du0;
			y1 = du1 - du2;
		} else if(du1 * du2 > 0.0f || du0 != 0.0f) {
			g = up0;
			e = (up1 - up0) * du0;
			f = (up2 - up1) * du1;
			y0 = du1 - du0;
			y1 = du0 - du2;
		} else if(du1 != 0.0f) {
			g = up1;
			e = (up0 - up1) * du1;
			f = (up2 - up1) * du1;
			y0 = du1 - du0;
			y1 = du1 - du2;
		} else if(du2 != 0.0f) {
			g = up2;
			e = (up0 - up2) * du2;
			f = (up1 - up2) * du1;
			y0 = du1 - du0;
			y1 = du2 - du1;
		} else {
			IntersectionData coplanar = coplanarTriangleTriangle(n1, v0, v1, v2, u0, u1, u2);
			return new IntersectionData(coplanar.isIntersect(), v0.sub(u0), coplanar.getNormal(), coplanar.getIntersectionPoint());
		}
		
		float xx, yy, xxyy, temp;
		xx = x0 * x1;
		yy = y0 * y1;
		xxyy = xx * yy;
		
		temp = a * xxyy;
		isect1 = new Vector2f(temp + b * x1 * yy, temp + c * x0 * yy);
		
		temp = g * xxyy;
		isect2 = new Vector2f(temp + e * xx * y1, temp + f * xx * y0);
		
		isect1.sort();
		isect2.sort();
		
		if(isect1.getY() < isect2.getX() || isect2.getY() < isect1.getX()) 
			return new IntersectionData(false, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));
		
		Vector2f xTmp = new Vector2f(x0, x1);
		xTmp.sort();
		x0 = xTmp.getX();
		x1 = xTmp.getY();
		
		Vector2f yTmp = new Vector2f(y0, y1);
		yTmp.sort();
		y0 = yTmp.getX();
		y1 = yTmp.getY();
		
		float largerX = y1;
		float smallerY = x0;
		
		if(x1 > y1) 
			largerX = x1;
		
		if(x0 > y0)
			smallerY = y0;
		
		float overlapLength = largerX - smallerY;
		float halfOverlapLength = (float)Math.abs(overlapLength) / 2.0f;
		
		Vector3f center1 = (p1.add(p2.add(p3))).div(3.0f);
		Vector3f center2 = (t.p1.add(t.p2.add(t.p3))).div(3.0f);
		
		Vector3f intersection = center1.add(center2.mul(2.0f)).div(3.0f);
		
		float axisIntersection = largerX + halfOverlapLength;
		axisIntersection += intersection.get(index);
		
		intersection.set(axisIntersection, index);
		
		return new IntersectionData(true, center1.sub(center2), n2, intersection);
	}

	public ICollisionData intersectSphere(BoundingSphere other) {
		Plane p = new Plane(p1, p2, p3);
		BoundingPlane plane = new BoundingPlane(p.getNormal(), p.getConstant());
		return plane.IntersectSphere(other);
	}
	
	public ICollisionData intersectEllipsiod(BoundingEllipsiod other) {
		Plane p = new Plane(other.toEllipsiodSpace(p1), other.toEllipsiodSpace(p2), other.toEllipsiodSpace(p3));
		BoundingPlane plane = new BoundingPlane(p.getNormal(), p.getConstant());
		return plane.IntersectEllipsiod(other);
	}
}
