/*
Archimedean 1.1, a 3D applet/application for visualizing, building, 
transforming and analyzing Archimedean solids and their derivatives.
Copyright 1998, 2011 Raffi J. Kasparian, www.raffikasparian.com.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.quantimegroup.solutions.archimedean.utils;public class OrderedTriple extends Object {	public double x, y, z;	public static OrderedTriple origin() {		return new OrderedTriple(0, 0, 0);	}	static OrderedTriple xAxis() {		return new OrderedTriple(1, 0, 0);	}	public static OrderedTriple yAxis() {		return new OrderedTriple(0, 1, 0);	}	static OrderedTriple zAxis() {		return new OrderedTriple(0, 0, 1);	}	public OrderedTriple() {		this.x = this.y = this.z = 0;	}	public OrderedTriple(double x, double y, double z) {		this.x = x;		this.y = y;		this.z = z;	}	public OrderedTriple(OrderedTriple t) {		this(t.x, t.y, t.z);	}	public OrderedTriple(OrderedTriple t1, OrderedTriple t2) {		this(t2.minus(t1));	}	public void become(OrderedTriple t) {		x = t.x;		y = t.y;		z = t.z;	}	public void become(double x, double y, double z) {		this.x = x;		this.y = y;		this.z = z;	}	public OrderedTriple copy() {		return new OrderedTriple(this);	}	public void print() {		System.out.println(Math.round(x) + ",\t" + Math.round(y) + ",\t" + Math.round(z));	}	public double lengthSquared() {		return x * x + y * y + z * z;	}	public double length() {		return Math.sqrt(x * x + y * y + z * z);	}	public double distanceSquared(OrderedTriple t) {		//distance to point t		return minus(t).lengthSquared();	}	public double distance(OrderedTriple t) {		//distance to point t		return minus(t).length();	}	public double distance(OrderedTriple p1, OrderedTriple p2) {		//distance to line through p1 and p2		OrderedTriple v1 = p2.minus(p1);		OrderedTriple v2 = minus(p1);		return v2.length() * v2.sin(v1);	}	public double xcos() {		return x / length();	}	public double ycos() {		return y / length();	}	public double zcos() {		return z / length();	}	public OrderedTriple plus(OrderedTriple v) {		return new OrderedTriple(x + v.x, y + v.y, z + v.z);	}	public void plusEquals(OrderedTriple v) {		x += v.x;		y += v.y;		z += v.z;	}	public OrderedTriple minus(OrderedTriple v) {		return new OrderedTriple(x - v.x, y - v.y, z - v.z);	}	public void minusEquals(OrderedTriple v) {		x -= v.x;		y -= v.y;		z -= v.z;	}	public double dot(OrderedTriple v) {		return x * v.x + y * v.y + z * v.z;	}	public OrderedTriple cross(OrderedTriple v) {		//A X B = ( AyBz - ByAz, AzBx - BzAx, AxBy - BxAy )		return new OrderedTriple(y * v.z - v.y * z, z * v.x - v.z * x, x * v.y - v.x * y);	}	public OrderedTriple times(double a) {		return new OrderedTriple(a * x, a * y, a * z);	}	public void timesEquals(double a) {		x *= a;		y *= a;		z *= a;	}	public OrderedTriple dividedBy(double a) {		return new OrderedTriple(x / a, y / a, z / a);	}	public void dividedByEquals(double a) {		x /= a;		y /= a;		z /= a;	}	public OrderedTriple negative() {		return new OrderedTriple(-x, -y, -z);	}	public boolean equals(OrderedTriple t) {		double dx = x - t.x, dy = y - t.y, dz = z - t.z;		return (dx == 0 && dy == 0 && dz == 0);		//if( x == t.x && y == t.y && z == t.z ) return true;		//return false;	}	public int sortOrder(OrderedTriple t) {		if (x > t.x)			return 1;		else if (x < t.x)			return -1;		else if (y > t.y)			return 1;		else if (y < t.y)			return -1;		else if (z > t.z)			return 1;		else if (z < t.z)			return -1;		else			return 0;	}	public boolean isApprox(OrderedTriple t, double e) {		double dx = x - t.x, dy = y - t.y, dz = z - t.z;		//if( Math.abs( x - t.x ) <= e && Math.abs( y - t.y ) <= e && Math.abs( z - t.z ) <= e ) return true;		if (Math.abs(dx) <= e && Math.abs(dy) <= e && Math.abs(dz) <= e)			return true;		return false;	}	public double radBetween(OrderedTriple t) {		return Math.acos((dot(t)) / (length() * t.length()));	}	public double degBetween(OrderedTriple t) {		return Rotater.rad2Deg(radBetween(t));	}	public static OrderedTriple findThirdVector(OrderedTriple v1, OrderedTriple v2, double a1, double a2, OrderedTriple testv)			throws Exception {		//v1 and v2 must be equal lengths N.  returns the vector a1 rad from v1, a2 rad from v2 and N length.  if there are two such vectors		//it returns the one with positive component of testv		//System.out.println( "angle between v1 & v2: " + v1.degBetween( v2 ) );		double epsilon = 0.0001;		if (isApprox(a1, 0, epsilon))			return new OrderedTriple(v1);		if (isApprox(a1, Math.PI, epsilon))			return v1.negative();		if (isApprox(a2, 0, epsilon))			return new OrderedTriple(v2);		if (isApprox(a2, Math.PI, epsilon))			return v2.negative();		OrderedTriple v3 = null;		double a = v1.x;		double b = v1.y;		double c = v1.z;		double d = v2.x;		double e = v2.y;		double f = v2.z;		double g, h, i;		double roots[] = null;		double N = v1.length();		double A = N * N * Math.cos(a1);		double B = N * N * Math.cos(a2);		double x12 = a * e - b * d;		double x23 = b * f - c * e;		double x31 = c * d - a * f;		double P, Q, R, S;		P = Q = R = S = 0;		if (!isApprox(x12, 0, epsilon)){			//System.out.println( "route 1" );			P = x23 / x12;			Q = (e * A - b * B) / x12;			R = x31 / x12;			S = (a * B - d * A) / x12;			roots = solveQuadratic(1 + P * P + R * R, 2 * P * Q + 2 * R * S, Q * Q + S * S - N * N);			i = roots[0];			v3 = new OrderedTriple(P * i + Q, R * i + S, i);			if (testv != null && v3.dot(testv) < 0){				i = roots[1];				v3.become(P * i + Q, R * i + S, i);			}		}else if (!isApprox(x23, 0, epsilon)){			//System.out.println( "route 2" );			P = x31 / x23;			Q = (f * A - c * B) / x23;			R = x12 / x23;			S = (b * B - e * A) / x23;			roots = solveQuadratic(1 + P * P + R * R, 2 * P * Q + 2 * R * S, Q * Q + S * S - N * N);			g = roots[0];			v3 = new OrderedTriple(g, P * g + Q, R * g + S);			if (testv != null && v3.dot(testv) < 0){				g = roots[1];				v3.become(g, P * g + Q, R * g + S);			}		}else if (!isApprox(x31, 0, epsilon)){			//System.out.println( "route 3" );			P = x23 / x31;			Q = (c * B - f * A) / x31;			R = x12 / x31;			S = (d * A - a * B) / x31;			roots = solveQuadratic(1 + P * P + R * R, 2 * P * Q + 2 * R * S, Q * Q + S * S - N * N);			h = roots[0];			v3 = new OrderedTriple(P * h + Q, h, R * h + S);			if (testv != null && v3.dot(testv) < 0){				h = roots[1];				v3.become(P * h + Q, h, R * h + S);			}		}else{			throw new Exception("EXCEPTION: no route found in findThirdVector");		}		//if( ! isApprox( v3.radBetween( v1 ), a1, 0.1 ) ) System.out.println( "error v1" );		//if( ! isApprox( v3.radBetween( v2 ), a2, 0.1 ) ) System.out.println( "error v2" );		return v3;	}	public static double[] solveQuadratic(double a, double b, double c) throws Exception {		double roots[];		double epsilon = -0.1;		if (a == 0){			roots = new double[1];			roots[0] = -c / b;		}		double d2 = b * b - 4 * a * c;		if (d2 < epsilon){			//System.out.println( "b2 = " + b*b + ", 4ac = " + 4*a*c + ", d2 = " + d2 );			throw new Exception("solveQuadratic error: d2 < 0");		}else if (d2 < 0)			d2 = 0;		double d = Math.sqrt(d2);		double r1 = (-b + d) / (2 * a);		double r2 = (-b - d) / (2 * a);		if (Math.abs(a) < 1e-10){			roots = new double[3];			roots[2] = -c / b;		}else{			roots = new double[2];		}		roots[0] = r1;		roots[1] = r2;		return roots;	}	public static boolean isApprox(double a, double b, double e) {		return Math.abs(a - b) <= e;	}	public static OrderedTriple sectLines(OrderedTriple p1, OrderedTriple p2, OrderedTriple p3, OrderedTriple p4) {		//line from p1 through p2 and line from p3 through p4		OrderedTriple A = p2.minus(p1);		OrderedTriple B = p4.minus(p3);		OrderedTriple C = p3.minus(p1);		OrderedDouble KT1 = OrderedDouble.solveEquations(A.x, -B.x, C.x, A.y, -B.y, C.y);		OrderedDouble KT2 = OrderedDouble.solveEquations(A.y, -B.y, C.y, A.z, -B.z, C.z);		OrderedDouble KT3 = OrderedDouble.solveEquations(A.z, -B.z, C.z, A.x, -B.x, C.x);		double K = 0;		if (KT1 == null && KT2 == null && KT3 == null)			return null;		else if (KT1 != null)			K = KT1.x;		else if (KT2 != null)			K = KT2.x;		else if (KT3 != null)			K = KT3.x;		return A.times(K).plus(p1);	}	static OrderedTriple oldSectLines(OrderedTriple p1, OrderedTriple p2, OrderedTriple p3, OrderedTriple p4) {		//line from p1 through p2 and line from p3 through p4		OrderedDouble d1 = OrderedDouble.sectLines(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, p4.x, p4.y);//d1 = (x, y)		OrderedDouble d2 = OrderedDouble.sectLines(p1.y, p1.z, p2.y, p2.z, p3.y, p3.z, p4.y, p4.z);//d2 = (y, z)		OrderedDouble d3 = OrderedDouble.sectLines(p1.z, p1.x, p2.z, p2.x, p3.z, p3.x, p4.z, p4.x);//d3 = (z, x)		if (d1 != null && d2 != null){			//if( isApprox( d1.y, d2.x, 0.1 ) )			return new OrderedTriple(d1.x, d1.y, d2.y);			//else return null;		}else if (d2 != null && d3 != null){			//if( isApprox( d2.y, d3.x, 0.1 ) )			return new OrderedTriple(d3.y, d2.x, d2.y);			//else return null;		}else if (d3 != null && d1 != null){			//if( isApprox( d3.y, d1.x, 0.1 ) )				return new OrderedTriple(d1.x, d1.y, d3.x);			//else return null;		}else if (d1 != null)			return new OrderedTriple(d1.x, d1.y, p1.z);		else if (d2 != null)			return new OrderedTriple(p1.x, d2.x, d2.y);		else if (d3 != null)			return new OrderedTriple(d3.y, p1.y, d3.x);		p1.print();		p2.print();		p3.print();		p4.print();		return null;	}	public OrderedTriple mid(OrderedTriple p) {		return plus(p).dividedBy(2);	}	public OrderedTriple mid(OrderedTriple p, double percent) {		percent /= 100;		return plus(p.minus(this).times(percent));	}	OrderedTriple towards(OrderedTriple p, double distance) {		OrderedTriple v = p.minus(this);		return plus(v.times(distance / v.length()));	}	void towardsEquals(OrderedTriple p, double distance) {		OrderedTriple v = p.minus(this);		plus(v.times(distance / v.length()));	}	public double cos(OrderedTriple v) {		//returns the cosine of the angle between them		return dot(v) / (length() * v.length());	}	double sin(OrderedTriple v) {		//returns the sine of the angle between them		double cos = cos(v);		return Math.sqrt(1 - cos * cos);	}	OrderedTriple bisectAngle(OrderedTriple v) {		//v = v.times( length()/v.length() );		v = v.unit().times(length());		return mid(v);	}	public OrderedTriple unit() {		return dividedBy(length());	}	public double comp(OrderedTriple v) {		//returns the distance along this that vector v2 progresses		//cos = this.v/||this||*||v||;		//cos = l/||v||		//l = cos*||v|| =  this.v/||this||;		return dot(v) / length();	}	public static OrderedTriple sectPlaneLine(OrderedTriple P1, OrderedTriple P2, OrderedTriple P3, OrderedTriple L1, OrderedTriple L2) {		//Find the 3D coordinates of the intersection of the plane containing points P1, P2, P3 and the line containing L1, L2		OrderedTriple n = P2.minus(P1).cross(P3.minus(P2));		OrderedTriple l = L2.minus(L1);		double A = n.dot(l);		if (A == 0)			return null;		double B = n.dot(P1);		double C = n.dot(L1);		double K = (B - C) / A;		return l.times(K).plus(L1);	}	/*	static OrderedTriple sectPlaneLine( OrderedTriple P1, OrderedTriple P2, OrderedTriple P3, OrderedTriple L1, OrderedTriple L2 ){	 OrderedTriple n = P2.minus( P1 ).cross( P3.minus( P2 ) );	 OrderedTriple l = L2.minus( L1 );	 double A = n.dot( l );	 if( A == 0 ) return null;	 double B = n.dot( P1 );	 double C = n.dot( L1 );	 double K = ( B - C )/A;	 return l.times( K ).plus( L1 );			 }	 */	public static double pointPlaneDistance(OrderedTriple p, OrderedTriple P1, OrderedTriple P2, OrderedTriple P3) {		OrderedTriple n = P2.minus(P1).cross(P3.minus(P2));		return sectPlaneLine(P1, P2, P3, p, p.plus(n)).distance(p);	}	public OrderedTriple arbitraryPerpendicular() {		OrderedTriple perp = new OrderedTriple(1, 1, 1);		if (x != 0)			perp.x = (y + z) / -x;		else if (y != 0)			perp.y = (x + z) / -y;		else if (z != 0)			perp.z = (x + y) / -z;		else			return null;		return perp;	}	static double[] planeFromNormalAndPoint(OrderedTriple n, OrderedTriple p) {		double[] answer = { n.x, n.y, n.z, n.dot(p) };		return answer;	}	static double[] planeFrom3Points(OrderedTriple p1, OrderedTriple p2, OrderedTriple p3) {		return planeFromNormalAndPoint(p1.minus(p2).cross(p2.minus(p3)), p1);	}	static OrderedTriple arbitraryPlanePoint(double A, double B, double C, double D) {		if (A != 0)			return new OrderedTriple(-D / A, 0, 0);		if (B != 0)			return new OrderedTriple(0, -D / B, 0);		if (C != 0)			return new OrderedTriple(0, 0, -D / C);		return null;	}	public static OrderedTriple[] sectSphereLine(OrderedTriple c, double r, OrderedTriple a, OrderedTriple b) {		//Find the intersection(s) of the sphere with radius length r and center at point c and the line containing points a and b		OrderedTriple v = b.minus(a);		OrderedTriple d = a.minus(c);		try{			double[] k = solveQuadratic(v.lengthSquared(), 2 * d.dot(v), d.lengthSquared() - r * r);			OrderedTriple[] answer = { v.times(k[0]).plus(a), v.times(k[1]).plus(a) };			return answer;		}catch (Exception e){			return null;		}	}	static OrderedTriple[] sectSphereLine(double r, OrderedTriple a, OrderedTriple b) {		//Find the intersection(s) of the sphere with radius length r and center at the origin and the line containing points a and b		OrderedTriple v = b.minus(a);		try{			double[] k = solveQuadratic(v.lengthSquared(), 2 * a.dot(v), a.lengthSquared() - r * r);			OrderedTriple[] answer = { v.times(k[0]).plus(a), v.times(k[1]).plus(a) };			return answer;		}catch (Exception e){			return null;		}	}	static OrderedTriple[] sectPlanes(OrderedTriple p11, OrderedTriple p12, OrderedTriple p13, OrderedTriple p21, OrderedTriple p22,			OrderedTriple p23) {		OrderedTriple n1 = p11.minus(p12).cross(p12.minus(p13));		OrderedTriple n2 = p21.minus(p22).cross(p22.minus(p23));		OrderedTriple n3 = n1.cross(n2);		double[] m1 = { n1.x, n1.y, n1.z };		double[] m2 = { n2.x, n2.y, n2.z };		double[] m3 = { n3.x, n3.y, n3.z };		double[][] m = { m1, m2, m3 };		Matrix M = new Matrix(m);		double[] b1 = { n1.dot(p11) };		double[] b2 = { n2.dot(p21) };		double[] b3 = { 0 };		double[][] b = { b1, b2, b3 };		Matrix B = new Matrix(b);		Matrix S = M.inverse().times(B);		//OrderedTriple s1 = new OrderedTriple( S.mat[0][0], S.mat[1][0], S.mat[2][0] );		OrderedTriple s1 = new OrderedTriple(S.mat[0][0], S.mat[1][0], S.mat[2][0]);		OrderedTriple s2 = s1.plus(n3);		OrderedTriple[] answer = { s1, s2 };		return answer;	}	public String toString() {		return "(" + x + ", " + y + ", " + z + ")";	}	/*	static OrderedTriple[] sectPlanes( OrderedTriple p11, OrderedTriple p12, OrderedTriple p13, 	 OrderedTriple p21, OrderedTriple p22, OrderedTriple p23 ){	 OrderedTriple n1 = p11.minus( p12 ).cross( p12.minus( p13 ) );	 OrderedTriple n2 = p21.minus( p22 ).cross( p22.minus( p23 ) );	 OrderedTriple n3 = n1.cross( n2 );	 	 Matrix M = new Matrix( n1, n2, n3 );	 double[] b1 = { n1.dot( p11 ) };	 double[] b2 = { n2.dot( p21 ) };	 double[] b3 = { 0 };	 double[][] b = { b1, b2, b3 };	 Matrix B = new Matrix( b );	 Matrix S = M.inverse().times( B );	 OrderedTriple s1 = new OrderedTriple( S.mat[0][0], S.mat[1][0], S.mat[2][0] );	 OrderedTriple s2 = s1.plus( n3 );	 OrderedTriple[] answer = { s1, s2 };	 return answer;	 }	 */	public double getX() {		return x;	}	public double getY() {		return y;	}	public double getZ() {		return z;	}		}