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

package com.quantimegroup.solutions.archimedean.app;import com.quantimegroup.solutions.archimedean.utils.IntList;import com.quantimegroup.solutions.archimedean.utils.Misc;import com.quantimegroup.solutions.archimedean.utils.ObjectList;import com.quantimegroup.solutions.archimedean.utils.OrderedTriple;public class ArchiCorner {	private ArchiBuilder boss;	private ObjectList firstSides;	private int hub;	private IntList spokes;	private OrderedTriple pole;	private IntList sides;	public ArchiBuilder getBoss() {		return boss;	}	public void setBoss(ArchiBuilder p) {		boss = p;	}	public ObjectList getFirstSides() {		return firstSides;	}	public void setFirstSides(ObjectList p) {		firstSides = p;	}	public int getHub() {		return hub;	}	public void setHub(int p) {		hub = p;	}	public IntList getSpokes() {		return spokes;	}	public void setSpokes(IntList p) {		spokes = p;	}	public IntList getSides() {		return sides;	}	public void setSides(IntList p) {		sides = p;	}	public OrderedTriple getPole() {		if (pole == null) return getHubPoint().negative();		return pole;	}	public void setPole(OrderedTriple p) {		pole = p;	}	static public double init(ObjectList firstSides, double length) {		int[] polyTypes = new int[firstSides.num];		for (int i = 0; i < firstSides.num; ++i){			ObjectList s = (ObjectList) firstSides.get(i);			polyTypes[i] = s.num;		}		MySolver solver = new MySolver(polyTypes, length);		double r = solver.solve();		return Math.asin(r / length);	}	public ArchiCorner() {	}	public ArchiCorner(OrderedTriple hub, int numSpokes, ArchiBuilder boss) throws Exception {		this.boss = boss;		this.hub = boss.registerPoint(hub);		spokes = new IntList(numSpokes);		sides = new IntList(numSpokes);		firstSides = new ObjectList(numSpokes);	}	public ArchiCorner(OrderedTriple hub, SpaceSide s1, SpaceSide s2, int numSpokes, double poleTheta, ArchiBuilder boss, int recursionDepth)			throws Exception {		this(hub, numSpokes, boss);		//System.out.println("recursion depth = " + recursionDepth);		start(s1, s2, numSpokes, poleTheta);		complete(numSpokes, poleTheta, recursionDepth);	}	public static ArchiCorner simpleCorner(OrderedTriple hub, SpaceSide s1, SpaceSide s2, int numSpokes, double poleTheta, ArchiBuilder boss)			throws Exception {		ArchiCorner c = new ArchiCorner(hub, numSpokes, boss);		int i1 = s1.findIndex(c.hub);		int i2 = s2.findIndex(c.hub);		c.spokes.add(s1.wrapgetIndex(i1 + 1));		c.spokes.add(s1.wrapgetIndex(i1 - 1));		c.spokes.add(s2.wrapgetIndex(i2 - 1));		OrderedTriple h = c.getHubPoint();		OrderedTriple v1 = ((OrderedTriple) (c.getSpoke(0))).minus(h);		OrderedTriple v2 = ((OrderedTriple) (c.getSpoke(1))).minus(h);		c.pole = OrderedTriple.findThirdVector(v1, v2, poleTheta, poleTheta, v1.cross(v2));		return c;	}	public static void createFirstCorner(ObjectList firstSides, double length, int numSpokes, double poleTheta, ArchiBuilder boss)			throws Exception {		createFirstCorner(firstSides, length, numSpokes, poleTheta, boss, true);	}	public static void createFirstCorner(ObjectList firstSides, double length, int numSpokes, double poleTheta, ArchiBuilder boss,			boolean finish) throws Exception {		if (finish){			OrderedTriple hub = new OrderedTriple(0, 0, 0);			ArchiCorner c = new ArchiCorner(hub, numSpokes, boss);			//c.boss = boss;			c.firstSides = firstSides.copy();			c.pole = new OrderedTriple(0, -1, 0).times(length);			double y = length * Math.cos(poleTheta);			double x = Math.sqrt(length * length - y * y);			c.spokes.add(boss.registerPoint(new OrderedTriple(x, -y, 0)));			c.complete(numSpokes, poleTheta, 0);		}else{			OrderedTriple hub = new OrderedTriple(0, 0, 0);			ArchiCorner c = new ArchiCorner(hub, numSpokes, boss);			c.boss = boss;			c.firstSides = firstSides.copy();			c.pole = OrderedTriple.yAxis().times(length);			double y = length * Math.cos(poleTheta);			double x = Math.sqrt(length * length - y * y);			c.spokes.add(boss.registerPoint(new OrderedTriple(x, y, 0)));			c.completeSpokes(poleTheta);			c.completeSides();			c.boss.registerCorner(c);			c.errorCheck();		}	}	public void start(SpaceSide s1, SpaceSide s2, int numSpokes, double poleTheta) throws Exception {		int i1 = s1.findIndex(hub);		int i2 = s2.findIndex(hub);		spokes.add(s1.wrapgetIndex(i1 + 1));		spokes.add(s1.wrapgetIndex(i1 - 1));		spokes.add(s2.wrapgetIndex(i2 - 1));		OrderedTriple h = getHubPoint();		OrderedTriple v1 = ((OrderedTriple) (getSpoke(0))).minus(h);		OrderedTriple v2 = ((OrderedTriple) (getSpoke(1))).minus(h);		pole = OrderedTriple.findThirdVector(v1, v2, poleTheta, poleTheta, v1.cross(v2));		getFirstSides(s1, s2, 0, numSpokes);	}	public int getFirstSides(SpaceSide s1, SpaceSide s2, int start, int numSpokes) throws Exception {		int foundi = -1, inc = 0;		for (int i = start; i < numSpokes; ++i){			if (s1.numPoints() == ((ObjectList) boss.firstSides.get(i)).num){				if (s2.numPoints() == ((ObjectList) boss.firstSides.wrapget(i + 1)).num){					foundi = i;					inc = 1;					break;				}else if (s2.numPoints() == ((ObjectList) boss.firstSides.wrapget(i - 1)).num){					foundi = i;					inc = -1;					break;				}			}		}		if (inc == 0 || foundi == -1){			String str = String.valueOf("EXCEPTION getFirstSides. inc = " + inc + " foundi = " + foundi);			str += String.valueOf("\ns1.points.num = " + s1.numPoints() + " s2.points.num = " + s2.numPoints());			throw new Exception(str);		}		firstSides = boss.firstSides.wrapCopy(foundi, inc);		return foundi + 1;	}	public void complete(int numSpokes, double poleTheta, int recursionDepth) throws Exception {		if (recursionDepth > 400){			throw new Exception("recursion Depth = " + recursionDepth);			//return;		}		completeSpokes(poleTheta);		completeSides();		boss.registerCorner(this);		errorCheck();		propagateCorners(numSpokes, poleTheta, boss, recursionDepth);	}	public void completeSpokes(double poleTheta) throws Exception {		for (int i = 1; i < firstSides.num; ++i){//complete spokes			if (spokes.num > i) continue;			ObjectList s = (ObjectList) firstSides.get(i - 1);			double polyTheta = polyAngle(s.num);			OrderedTriple h = getHubPoint();			OrderedTriple lastSpoke = ((OrderedTriple) wrapgetSpoke(i - 1)).minus(h);			OrderedTriple nextSpoke = OrderedTriple.findThirdVector(pole, lastSpoke, poleTheta, polyTheta, pole.cross(lastSpoke));			spokes.add(boss.registerPoint(h.plus(nextSpoke)));		}		OrderedTriple s1 = getSpoke(0).minus(getHubPoint());		OrderedTriple s2 = getSpoke(firstSides.num - 1).minus(getHubPoint());		double check1 = s1.radBetween(s2);		double check2 = polyAngle(((ObjectList) firstSides.getLast()).num);		if (!OrderedTriple.isApprox(check1, check2, 1e-5)){			int[] polyTypes = new int[firstSides.num];			for (int i = 0; i < firstSides.num; ++i){				ObjectList s = (ObjectList) firstSides.get(i);				polyTypes[i] = s.num;			}			MySolver solver1 = new MySolver(polyTypes, polyTypes.length, 100, true);//inside pole method			double r1 = solver1.solve();			System.out.println(r1);			MySolver solver2 = new MySolver(polyTypes, polyTypes.length, 100, false);//outside pole method			double r2 = solver2.solve();			System.out.println(r2);		}	}	public void completeSides() throws Exception {		for (int i = 0; i < firstSides.num; ++i){//fill in sides			OrderedTriple p0 = (OrderedTriple) getSpoke(i);			OrderedTriple p1 = getHubPoint();			OrderedTriple p2 = (OrderedTriple) wrapgetSpoke(i + 1);			int index = boss.sideIsBuilt(p2, p1, p0);			if (index == -1){				ObjectList master = (ObjectList) firstSides.get(i);				SpaceSide s = new SpaceSide(p2, p1, p0, master, boss);				s.setPoints(boss.points);				s.setVectors(boss.vectors);				index = boss.registerSide(s);			}			sides.add(index);		}	}	public void propagateCorners(int numSpokes, double poleTheta, ArchiBuilder boss, int recursionDepth) throws Exception {		int hubIndex, s1index, s2index;		for (int i = 0; i < spokes.num; ++i){			OrderedTriple newHub = (OrderedTriple) getSpoke(i);			hubIndex = spokes.get(i);			SpaceSide s1 = getSide(i);			s1index = sides.get(i);			SpaceSide s2 = wrapgetSide(i - 1);			s2index = sides.wrapget(i - 1);			if (boss.cornerIsBuilt(hubIndex, s1index, s2index) == -1){				if (!(boss.ambiguous && s1.numPoints() == boss.ambiguousPoly && s2.numPoints() == boss.ambiguousPoly)){					ArchiCorner newc = new ArchiCorner(newHub, s1, s2, numSpokes, poleTheta, boss, recursionDepth + 1);				}			}		}	}	void errorCheck() {		/*if (spokes.num < numSpokes) System.out.println("ERROR only " + spokes.num + " out of " + numSpokes + " spokes complete");		 if (sides.num < numSpokes) System.out.println("ERROR only " + sides.num + " out of " + numSpokes + " sides complete");		 for (int i = 0; i < numSpokes; ++i){		 OrderedTriple v1 = ((OrderedTriple) (spokes.wrapget(i))).minus(hub);		 OrderedTriple v2 = ((OrderedTriple) (spokes.wrapget(i + 1))).minus(hub);		 if (!isApprox(v1.radBetween(pole), poleTheta, 0.1)){		 System.out.println("ERROR poleTheta in corner " + boss.corners.find(this) + " between spoke " + i + " and pole");		 }		 SpaceSide s = (SpaceSide) sides.get(i);		 if (s != null){		 double polyTheta = polyAngle(((SpaceSide) sides.get(i)).numPoints());		 if (!isApprox(v1.radBetween(v2), polyTheta, 0.1)){		 System.out.println("ERROR polyTheta in corner " + boss.corners.find(this) + " between spoke " + i + " and spoke " + (i + 1));		 }		 }		 }*/	}	static double polyAngle(int numSides) {		return Math.PI * (1 - 2.0 / numSides);	}	static boolean isApprox(double a, double b, double e) {		return Math.abs(a - b) <= e;	}	OrderedTriple getSpoke(int i) {		return boss.getPoint(spokes.get(i));	}	OrderedTriple wrapgetSpoke(int i) {		return boss.getPoint(spokes.wrapget(i));	}	SpaceSide getSide(int i) {		return boss.getSide(sides.get(i));	}	SpaceSide wrapgetSide(int i) {		return boss.getSide(sides.wrapget(i));	}	OrderedTriple getHubPoint() {		return boss.getPoint(hub);	}	static int[] getSignature(int[] polys, int length) {		IntList sign = new IntList(length);		for (int i = 0; i < length; ++i){			sign.add(polys[i]);		}		IntList bestSign = sign, temp;		for (int i = 0; i < length; ++i){			temp = sign.wrapCopy(i, 1);			if (Misc.arrayCompare(temp.ints, bestSign.ints) < 0) bestSign = temp;			temp = sign.wrapCopy(i, -1);		}		return bestSign.ints;	}	int[] getSignature() {		IntList sign = new IntList(sides.num);		for (int i = 0; i < sides.num; ++i){			sign.add(getSide(i).numPoints());		}		IntList bestSign = sign, temp;		for (int i = 0; i < sides.num; ++i){			temp = sign.wrapCopy(i, 1);			if (Misc.arrayCompare(temp.ints, bestSign.ints) < 0) bestSign = temp;			temp = sign.wrapCopy(i, -1);		}		return bestSign.ints;	}}