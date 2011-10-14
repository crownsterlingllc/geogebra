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

package com.quantimegroup.solutions.archimedean.app;import java.awt.*;import com.quantimegroup.solutions.archimedean.utils.Axes;import com.quantimegroup.solutions.archimedean.utils.IntList;import com.quantimegroup.solutions.archimedean.utils.ObjectList;import com.quantimegroup.solutions.archimedean.utils.OrderedDouble;import com.quantimegroup.solutions.archimedean.utils.OrderedTriple;import com.quantimegroup.solutions.archimedean.utils.Rotater;import com.quantimegroup.solutions.archimedean.utils.SmartPolygon;public class SpaceSide {	protected SpacePoly owner;	private IntList index;	private ObjectList behinders;	private Polygon poly;	public int normal;	private boolean drawn = false;	private ObjectList points, vectors;	boolean isREGULAR = true;	public SpacePoly getOwner() {		return owner;	}	public void setOwner(SpacePoly p) {		owner = p;	}	public ObjectList getPoints() {		return points;	}	public void setPoints(ObjectList p) {		points = p;	}	public ObjectList getVectors() {		return vectors;	}	public void setVectors(ObjectList p) {		vectors = p;	}	public boolean getDrawn() {		return drawn;	}	public void setDrawn(boolean p) {		drawn = p;	}	public OrderedTriple getNormal() {		return (OrderedTriple) vectors.get(normal);	}	public void setNormal(int p) {		normal = p;	}	public IntList getIndex() {		return index;	}	public void setIndex(IntList p) {		index = p;	}	private SpaceSide() {	}	public SpaceSide(int numPoints) {		index = new IntList(numPoints);		poly = new Polygon(new int[numPoints + 1], new int[numPoints + 1], numPoints + 1);	}	public SpaceSide(SpaceSide s) {		this(s.numPoints());		System.arraycopy(s.poly.xpoints, 0, poly.xpoints, 0, poly.npoints);		System.arraycopy(s.poly.ypoints, 0, poly.ypoints, 0, poly.npoints);		poly.npoints = s.poly.npoints;		normal = s.normal;		owner = s.owner;		points = s.points;		vectors = s.vectors;		index = s.index.copy();	}	public SpaceSide(OrderedTriple p0, OrderedTriple p1, OrderedTriple p2, ObjectList s, ArchiBuilder boss) throws Exception {		this(s.num);		Axes a = new Axes();		a.setX(p0.minus(p1));		a.setY(p1.minus(p0).cross(p2.minus(p1)));		a.setZ(a.getX().cross(a.getY()));		/*		a.X = p0.minus( p1 );		 a.Y = p1.minus( p0 ).cross( p2.minus( p1 ) );		 a.Z = a.X.cross( a.Y );		 */a.origin = p0;		a.correct();		for (int i = 0; i < s.num; ++i){			OrderedTriple p = ((OrderedTriple) s.get(i)).minus((OrderedTriple) s.get(0));//to put newSide.points[0] at the origin			a.transformPoint(p);//rotate to correct orientation			index.add(boss.registerPoint(p));		}	}	public static SpaceSide sideFromTwoPointsAndNormal(OrderedTriple p0, OrderedTriple p1, OrderedTriple n, ObjectList s, ArchiBuilder boss)			throws Exception {		SpaceSide me = new SpaceSide(s.num);		Axes a = new Axes();		a.setX(p0.minus(p1));		a.setY(n);		a.setZ(a.getX().cross(a.getY()));		/*		a.X = p0.minus( p1 );		 a.Y = n;		 a.Z = a.X.cross( a.Y );		 */a.origin = p0;		a.correct();		for (int i = 0; i < s.num; ++i){			OrderedTriple p = ((OrderedTriple) s.get(i)).minus((OrderedTriple) s.get(0));//to put newSide.points[0] at the origin			a.transformPoint(p);//rotate to correct orientation			me.index.add(boss.registerPoint(p));		}		return me;	}	public SpaceSide(int numEdges, double edgeLength, ObjectList pointStorage) throws Exception {		//builds side in XZ plane with center at origin and one edge parallel to the X axis in the negative Z half of the XZ plane		this(numEdges);		double dtheta = Math.PI * 2 / numEdges;		double theta = dtheta / 2 - Math.PI / 2;		double radius = edgeLength / (2 * Math.sin(dtheta / 2));		for (int i = 0; i < numEdges; ++i, theta -= dtheta){			OrderedTriple p = new OrderedTriple(Math.cos(theta), 0, Math.sin(theta));			p.timesEquals(radius);			points = pointStorage;			points.add(p);			index.add(points.find(p));		}	}	static ObjectList createPoly(int numEdges, double edgeLength) {		ObjectList points = new ObjectList(numEdges);		double dtheta = Math.PI * 2 / numEdges;		double theta = dtheta / 2 - Math.PI / 2;		double radius = edgeLength / (2 * Math.sin(dtheta / 2));		for (int i = 0; i < numEdges; ++i, theta -= dtheta){			OrderedTriple p = new OrderedTriple(Math.cos(theta), 0, Math.sin(theta));			p.timesEquals(radius);			points.add(p);		}		return points;	}	public int numPoints() {		return index.num;	}	public boolean hasPoint(OrderedTriple p) {//returns my index		for (int i = 0; i < numPoints(); ++i){			if (getPoint(i) == p)				return true;		}		return false;	}	public int findPoint(OrderedTriple p) {		for (int i = 0; i < numPoints(); ++i){			if (points.get(index.get(i)) == p)				return i;		}		return -1;	}	public int findIndex(int i) {		return index.find(i);	}	int toRange(int i) {		while (i < 0)			i += numPoints();		while (i >= numPoints())			i -= numPoints();		return i;	}	public OrderedTriple getPoint(int i) {		return (OrderedTriple) points.get(index.get(i));	}	public OrderedTriple wrapgetPoint(int i) {		return (OrderedTriple) points.get(index.get(toRange(i)));	}	public int getIndex(int i) {		return index.get(i);	}	public int wrapgetIndex(int i) {		return index.wrapget(i);	}	public OrderedTriple calcNormal() {		//0-1 X 2-1		OrderedTriple[] p = threeDistinctPoints();		OrderedTriple v1 = p[0].minus(p[1]);		OrderedTriple v2 = p[2].minus(p[1]);		return v1.cross(v2);	}	public void draw(Graphics g, boolean transparent) {		if (!transparent && !visible() && !owner.showBackSides)			return;		if (owner.gui.draw){			if (!visible())				g.setColor(Color.getHSBColor(owner.gui.hue1, 1f, 0.4f));			else				g.setColor(Color.getHSBColor(owner.gui.hue1, 1f, 1f));		}else			g.setColor(owner.edgeColor);		g.drawPolygon(poly);	}	public void draw(Graphics g) {		draw(g, false);	}	public void fill(Graphics g) {		if (!visible() && !owner.showBackSides)			return;		OrderedTriple ray = owner.lightSource.minus(getPoint(0));		double brightness = (ray.dot(getNormal())) / (getNormal().length() * ray.length());		if (brightness < 0)			brightness *= -0.5;		else			brightness *= 0.8;		brightness += 0.2;		float hue = owner.gui.getHue(this);		double white = 1;		if (visible()){//add reflected light			//OrderedTriple p0 = getPoint( 0 );			try{				OrderedTriple p0 = getCenter();				OrderedTriple p1 = owner.lightSource;				OrderedTriple v1 = p1.minus(p0);				OrderedTriple N = getNormal().unit();				double theta = N.radBetween(v1);				N.timesEquals(N.comp(v1));				OrderedTriple p3 = p0.plus(N);				OrderedTriple p2 = p3.times(2).minus(p1);				OrderedTriple v2 = p2.minus(p0);				OrderedTriple S = SpacePoint.viewer.minus(p0);				double alpha = S.radBetween(v2);				double maxAngle = Math.PI / 2;				if (alpha < maxAngle){					white = Math.pow(alpha / maxAngle, .25);					white -= .2;					white = Math.max(white, 0);				}			}catch (Exception e){				//center didn't work probably because the side is infinitesimal			}		}		g.setColor(Color.getHSBColor(hue, (float) white, (float) brightness));		g.fillPolygon(poly);	}	public void render(Graphics g) {		if (drawn)			return;		if (!visible() && !owner.showBackSides){			drawn = true;			return;		}		if (behinders != null){			for (int i = 0; i < behinders.num; ++i){				SpaceSide s = (SpaceSide) behinders.objects[i];				s.update();				boolean inLoop = false;				for (int j = 0; j < owner.drawChain.num; ++j){					if (owner.drawChain.objects[j] == s){						inLoop = true;						break;					}				}				if (!inLoop){					owner.drawChain.add(s);					s.render(g);				}			}		}		fill(g);		if (owner.gui.drawEdges){			draw(g, false);		}		drawn = true;	}	public void update() {		for (int i = 0; i < poly.npoints - 1; ++i){			SpacePoint p = (SpacePoint) getPoint(i);			poly.xpoints[i] = p.screenx;			poly.ypoints[i] = p.screeny;		}		poly.xpoints[poly.npoints - 1] = poly.xpoints[0];		poly.ypoints[poly.npoints - 1] = poly.ypoints[0];	}	public boolean visible() {		return SpacePoint.viewer.minus(getPoint(0)).dot(getNormal()) >= 0;	}	public static OrderedTriple getCircumcenter(OrderedTriple p1, OrderedTriple p2, OrderedTriple p3) {		OrderedTriple e1 = p2.minus(p1);		OrderedTriple m1 = p2.mid(p1);		OrderedTriple e2 = p3.minus(p2);		OrderedTriple m2 = p3.mid(p2);		OrderedTriple normal = e1.cross(e2);		OrderedTriple r1 = normal.cross(e1);		OrderedTriple r2 = normal.cross(e2);		return OrderedTriple.sectLines(m1, m1.plus(r1), m2, m2.plus(r2));	}	public static OrderedTriple getIncenter(OrderedTriple p1, OrderedTriple p2, OrderedTriple p3, OrderedTriple p4) {		OrderedTriple v1 = p2.minus(p1).unit().times(100);		OrderedTriple v2 = p3.minus(p2).unit().times(100);		OrderedTriple v3 = p4.minus(p3).unit().times(100);		OrderedTriple mid1 = v1.negative().mid(v2);		OrderedTriple mid2 = v2.negative().mid(v3);		return OrderedTriple.sectLines(p2, mid1.plus(p2), p3, mid2.plus(p3));	}	public OrderedTriple getCenter() {//works only for sides that can be inscribed in circles		OrderedTriple[] p = threeDistinctPoints();		return getCircumcenter(p[0], p[1], p[2]);	}	public OrderedTriple getIncenter() {//works only for sides that can be inscribed in circles		OrderedTriple[] p = fourDistinctPoints();		return getIncenter(p[0], p[1], p[2], p[3]);	}	double getEdgeLength() {		return getPoint(0).distance(getPoint(1));	}	public static ObjectList circumscribedTangent(ObjectList s) {		OrderedTriple p1 = (OrderedTriple) s.get(0), p2 = (OrderedTriple) s.get(1), p3 = (OrderedTriple) s.get(2);		OrderedTriple normal = p2.minus(p1).cross(p3.minus(p2));		OrderedTriple center = getCircumcenter(p1, p2, p3);//get its center		ObjectList tangents = new ObjectList(s.num);		for (int j = 0; j < s.num; ++j){//construct tangents to its circumscribed circle at its points			OrderedTriple radiusVector = ((OrderedTriple) s.get(j)).minus(center);			OrderedTriple tangent = radiusVector.cross(normal);			tangents.add(tangent);		}		ObjectList tangentSide = new ObjectList(s.num);		for (int j = 0; j < tangents.num; ++j){//intersect consecutive tangents and add point to dualSide			p1 = (OrderedTriple) s.get(j);			p2 = (OrderedTriple) s.wrapget(j + 1);			OrderedTriple tanv1 = (OrderedTriple) tangents.get(j);			OrderedTriple tanv2 = (OrderedTriple) tangents.wrapget(j + 1);			OrderedTriple intersection = OrderedTriple.sectLines(p1, p1.plus(tanv1), p2, p2.plus(tanv2));			tangentSide.add(intersection);		}		return tangentSide;	}	public OrderedTriple sectLine(OrderedTriple L1, OrderedTriple L2) {		OrderedTriple[] p = threeDistinctPoints();		return OrderedTriple.sectPlaneLine(p[0], p[1], p[2], L1, L2);	}	public OrderedTriple outwardPerpendicular(OrderedTriple p1, OrderedTriple p2) {		//p1->p2 must be clockwize		return getNormal().cross(p2.minus(p1));	}	public boolean inside(OrderedTriple p) {		double epsilon = 1e-5;		int numRealSides = 0;		OrderedTriple L1, L2 = wrapgetPoint(-1);		for (int i = 0; i < numPoints(); ++i){			L1 = L2;			L2 = getPoint(i);			if (L1.equals(L2))				continue;			++numRealSides;			if (p.minus(L1).dot(outwardPerpendicular(L1, L2)) > epsilon)				return false;		}		return numRealSides >= 3;	}	public boolean inside(int x, int y) {		return SmartPolygon.inside(poly, x, y);		//		boolean result1 = SmartPolygon.inside( poly, new OrderedDouble( x, y ) );			//		boolean result2 = SmartPolygon.inside( poly, x, y );		//		if( result1 != result2 ){		//			System.out.println( "not equal insides" );		//			result1 = SmartPolygon.inside( poly, new OrderedDouble( x, y ), true );			//			result2 = SmartPolygon.inside( poly, x, y );		//		}		//		return result2;	}	public boolean counterClockwizeInside(int x, int y) {		//works for convex, counter-clockwize sides		return SmartPolygon.inside(poly, new OrderedDouble(x, y), false);	}	boolean isClockwise(OrderedTriple norm) {		return calcNormal().dot(norm) > 0;	}	boolean isAdjacent(SpaceSide s) {		int count = 0;		for (int i = 0; i < numPoints(); ++i){			if (s.index.find(index.get(i)) != -1)				++count;		}		return count == 2;	}	OrderedTriple[] threeDistinctPoints() {		//assumes that there are three distinct points		OrderedTriple[] p = new OrderedTriple[3];		double epsilon = 1e-10;		p[0] = getPoint(0);		int i;		for (i = 1; i < numPoints(); ++i){			p[1] = getPoint(i);			if (!p[1].isApprox(p[0], epsilon))				break;		}		for (i = i + 1; i < numPoints(); ++i){			p[2] = getPoint(i);			if (!p[2].isApprox(p[1], epsilon))				break;		}		return p;	}	OrderedTriple[] fourDistinctPoints() {		//assumes that there are three distinct points		OrderedTriple[] p = new OrderedTriple[4];		double epsilon = 1e-10;		p[0] = getPoint(0);		int i;		for (i = 1; i < numPoints(); ++i){			p[1] = getPoint(i);			if (!p[1].isApprox(p[0], epsilon))				break;		}		for (i = i + 1; i < numPoints(); ++i){			p[2] = getPoint(i);			if (!p[2].isApprox(p[1], epsilon))				break;		}		for (i = i + 1; i < numPoints() * 2; ++i){			p[3] = wrapgetPoint(i);			if (!p[3].isApprox(p[2], epsilon))				break;		}		return p;	}	static double triangleArea(double a, double b, double c) {		double s = (a + b + c) / 2;		return Math.sqrt(s * (s - a) * (s - b) * (s - c));	}	double getArea() {		double area = 0;		OrderedTriple p0 = getPoint(0);		OrderedTriple p1;		OrderedTriple p2 = getPoint(1);		double a, b;		double c = p2.distance(p0);		for (int i = 2; i < numPoints(); ++i){			p1 = p2;			p2 = getPoint(i);			a = c;			b = p2.distance(p1);			c = p0.distance(p2);			area += triangleArea(a, b, c);		}		return area;	}	boolean calcRegular() {		double epsilon = 1e-1;		ObjectList tempPoints = new ObjectList(numPoints());		for (int i = 0; i < numPoints(); ++i){			tempPoints.add(new OrderedTriple(getPoint(i)));		}		OrderedTriple axis = getCenter();		if(axis == null){			return false;		}		if (axis.isApprox(OrderedTriple.origin(), epsilon)){			axis = getNormal();		}		//Rotater r = new Rotater( getCenter(), Math.PI*2/numPoints() );		Rotater r = new Rotater(axis, Math.PI * 2 / numPoints());		Axes a = new Axes();		a.timesEquals(r);		ObjectList copiedPoints = new ObjectList(numPoints());		for (int i = 0; i < numPoints(); ++i){			copiedPoints.add(getPoint(i));		}		for (int i = 0; i < tempPoints.num; ++i){			OrderedTriple pi = (OrderedTriple) tempPoints.get(i);			a.transformPoint(pi);			int found = ArchiBuilder.findApproxPoint(pi, copiedPoints);			if (found == -1)				return false;			else				copiedPoints.removeIndex(found);		}		return copiedPoints.num == 0;	}}