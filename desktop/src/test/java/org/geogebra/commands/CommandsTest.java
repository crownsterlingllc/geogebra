package org.geogebra.commands;

import java.util.Locale;

import org.geogebra.common.kernel.StringTemplate;
import org.geogebra.common.kernel.commands.AlgebraProcessor;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoList;
import org.geogebra.common.kernel.kernelND.GeoElementND;
import org.geogebra.common.main.App;
import org.geogebra.common.util.StringUtil;
import org.geogebra.common.util.Unicode;
import org.geogebra.desktop.main.AppDNoGui;
import org.geogebra.desktop.main.LocalizationD;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CommandsTest extends Assert{
	static AppDNoGui app;
	static AlgebraProcessor ap;

	private static void  t(String input, String expected){
		testSyntax(input, new String[] { expected }, app, ap,
				StringTemplate.xmlTemplate);
	}

	private static void t(String input, String expected, StringTemplate tpl) {
		testSyntax(input, new String[] { expected }, app, ap, tpl);
	}

	public static void t(String s, String[] expected, StringTemplate tpl) {
		testSyntax(s, expected, app, ap, tpl);
	}

	public static void t(String s, String[] expected) {
		testSyntax(s, expected, app, ap, StringTemplate.xmlTemplate);
	}

	public static void testSyntax(String s, String[] expected, App app,
			AlgebraProcessor ap, StringTemplate tpl) {
		if(syntaxes==-1000){
			Throwable t = new Throwable();
			String cmdName = t.getStackTrace()[2].getMethodName().substring(3);
			String syntax = app.getLocalization().getCommand(cmdName+".Syntax");
			syntaxes = 0;
			for(int i=0;i<syntax.length();i++)
				if(syntax.charAt(i)=='[')syntaxes++;
			System.out.println();
			System.out.print(cmdName+" ");
			
			/*
			// This code helps to force timeout for each syntax. Not used at the moment.
			GeoGebraCAS cas = (GeoGebraCAS) app.getKernel()
					.getGeoGebraCAS();
			try {
				cas.getCurrentCAS().evaluateRaw("caseval(\"timeout 8\")");
			} catch (Throwable e) {
				App.error("CAS error " + e);
			} 
			*/
			
			
		}
		Throwable t = null;
		GeoElementND[] result = null;
		try {
			result = ap.processAlgebraCommandNoExceptionHandling(s,
					false, TestErrorHandler.INSTANCE, false, null);
		}catch (Throwable e) {
			t = e;
		}
		syntaxes--;
		assertNull(t);
		Assert.assertNotNull(s,result);

		Assert.assertEquals(s + " count:", expected.length, result.length);
		// for (int i = 0; i < expected.length; i++) {
		// String actual = result[i].toValueString(tpl);
		// System.out.println("\"" + actual + "\",");
		// }
		for (int i = 0; i < expected.length; i++) {
			String actual = result[i].toValueString(tpl);
			Assert.assertEquals(s + ":" + actual, expected[i], actual);
		}
		System.out.print("+");

	}

	private static int syntaxes = -1000;
	
	@Before
	public void resetSyntaxes(){
		syntaxes = -1000;
		app.getKernel().clearConstruction(true);
	}
	@After
	public void checkSyntaxes(){
		Assert.assertTrue("unchecked syntaxes: "+syntaxes,syntaxes<=0);
	}
	
	@BeforeClass
	public static void setupApp() {
		app = new AppDNoGui(new LocalizationD(3));
		app.setLanguage(Locale.US);
		ap = app.getKernel().getAlgebraProcessor();
		// make sure x=y is a line, not plane
		app.getGgbApi().setPerspective("1");
	    // Setting the general timeout to 11 seconds. Feel free to change this.
		app.getKernel().getApplication().getSettings().getCasSettings().setTimeoutMilliseconds(11000);
	}

	@Test
	public void testQuadricExpr() {
		t("-y^2=z-1", "-y" + Unicode.Superscript_2 + " + 0z"
				+ Unicode.Superscript_2 + " - z = -1");
		t("y^2=1-z", "y" + Unicode.Superscript_2 + " + 0z"
				+ Unicode.Superscript_2 + " + z = 1");
	}

	@Test
	public void listArithmetic() {
		t("{1,2,3}*2", "{2, 4, 6}");
		t("{1,2,3}+3", "{4, 5, 6}");
		t("list1:={1,2,3}", "{1, 2, 3}");
		t("listF:={x, 2 * x,3 * x+1}", "{x, (2 * x), (3 * x) + 1}");
		t("matrix1:={{1, 2, 3}, {2, 4, 6}, {3, 6, 9}}",
				"{{1, 2, 3}, {2, 4, 6}, {3, 6, 9}}");
		t("list1(1)", "1");
		t("list1(4)", "NaN");
		t("list1(0)", "NaN");
		t("list1(-1)", "3");
		t("list1(-5)", "NaN");
		t("list1(1,2)", "NaN");
		t("listF(1)", "x");
		t("listF(2)", "(2 * x)");
		t("listF(2,7)", "14");
		t("matrix1(2)", "{2, 4, 6}");
		t("matrix1(-1)", "{3, 6, 9}");
		t("matrix1(-5)", "{NaN, NaN, NaN}");
		t("matrix1(2,3)", "6");
		t("matrix1(2,3,4)", "NaN");
		t("matrix1(2,-1)", "6");
		t("Delete[list1]", new String[] {});
		t("Delete[matrix1]", new String[] {});
	}

	@Test
	public void tuples() {
		t("(1..2,1..2)", "{(1, 1), (2, 2)}");
	}

	private GeoElement get(String label) {
		return app.getKernel().lookupLabel(label);
	}

	@Test
	public void listPropertiesTest() {
		t("mat1={{1,2,3}}", "{{1, 2, 3}}");
		Assert.assertTrue(((GeoList) get("mat1")).isEditableMatrix());
		t("slider1=7", "7");
		t("mat2={{1,2,slider1}}", "{{1, 2, 7}}");
		Assert.assertTrue(((GeoList) get("mat2")).isEditableMatrix());
		t("mat2={{1,2,slider1},Reverse[{1,2,3}]}", "{{1, 2, 7}, {3, 2, 1}}");
		Assert.assertFalse(((GeoList) get("mat2")).isEditableMatrix());
	}

	@Test
	public void operationSequence() {
		Assert.assertEquals(StringUtil.fixVerticalBars("1..2"), "1"
				+ Unicode.ellipsis + "2");
		t("3.2..7.999", "{3, 4, 5, 6, 7, 8}");
		t("-3.2..3.2", "{-3, -2, -1, 0, 1, 2, 3}");
		t("3.2..-2", "{3, 2, 1, 0, -1, -2}");
		t("seqa=2*(1..5)", "{2, 4, 6, 8, 10}");
		assertEquals("<expression label =\"seqa\" exp=\"(2 * (1"
				+ Unicode.ellipsis + "5))\"/>",
				app.getGgbApi().getXML("seqa").split("\n")[0]);
		t("seqa=(1..3)+3", "{4, 5, 6}");
		assertEquals(
				"<expression label =\"seqa\" exp=\"(1" + Unicode.ellipsis
						+ "3) + 3\"/>",
				app.getGgbApi().getXML("seqa").split("\n")[0]);
	}
	@Test
	public void cmdMidpoint(){
		t("Midpoint[(0,0),(2,2)]","(1, 1)");
		t("Midpoint[0<x<2]","1");
		t("Midpoint[Segment[(0,0),(2,2)]]","(1, 1)");
		t("Midpoint[(x-1)^2+(y-1)^2=pi]","(1, 1)");
	}
	
	@Test
	public void cmdIsInRegion(){
		t("IsInRegion[(0,0),Circle[(1,1),2]]","true");
		t("IsInRegion[(0,0),Circle[(1,1),1]]","false");
		t("IsInRegion[(0,0,0),x+y+z=1]","false");
		t("IsInRegion[(0,0,0),x+y+z=0]","true");
		t("IsInRegion[(0,0,0),Polygon[(0,0,1),(1,0,0),(0,1,0)]]","false");
		t("IsInRegion[(1/3,1/3,1/3),Polygon[(0,0,1),(1,0,0),(0,1,0)]]","true");
		//move the centroid a bit in z-axis, it should no longer be inside
		t("IsInRegion[(1/3,1/3,1/2),Polygon[(0,0,1),(1,0,0),(0,1,0)]]","false");
	}
	
	@Test
	public void cmdCross(){
		t("Cross[(0,0,1),(1,0,0)]","(0, 1, 0)");
		t("Cross[(0,0,1),(0,1,0)]","(-1, 0, 0)");
		t("Cross[(0,0,1),(0,0,1)]","(0, 0, 0)");
		t("Cross[(0,1),(2,0)]","-2");
		t("Cross[(0,1),(0,2)]","0");
	}
	
	@Test
	public void functionDependentPoly() {
		t("s(x,y)=x+y", "x + y");
		t("s(1,2)*x=1", "x = 0.3333333333333333");
	}

	@Test
	public void cmdDot(){
		t("Dot[(0,0,1),(1,0,0)]","0");
		t("Dot[(0,0,1),(0,0,1)]","1");
		t("Dot[(0,3),(0,2)]","6");
	}

	@Test
	public void cmdNormalize() {
		t("Normalize[{1,3,2}]", "{0, 1, 0.5}");
		t("Normalize[{(1,1),(3,1),(2,1)}]", "{(0, 0), (1, 0), (0.5, 0)}");
	}
	
	@Test
	public void cmdDataFunction(){
		t("DataFunction[]", "DataFunction[{}, {},x]");
		t("DataFunction[]", new String[] { "DataFunction[x]" },
				StringTemplate.defaultTemplate);
	}
	
	@Test
	public void cmdAreCongruent() {
		t("AreCongruent[Segment[(0,1),(1,0)],Segment[(1,0),(0,1)]]", "true");
		t("AreCongruent[Segment[(0,1),(1,0)],Segment[(-1,0),(0,-1)]]", "true");
		t("AreCongruent[Segment[(0,1),(1,0)],Segment[(2,0),(0,2)]]", "false");
	}

	@Test
	public void cmdIntersect() {
		t("Intersect[3x=4y,Curve[5*sin(t),5*cos(t),t,0,6]]", new String[] {
				"(4, 3)", "(-4, -3)" },
				StringTemplate.editTemplate);
		t("Intersect[x=y,x+y=2]", "(1, 1)");
		t("Intersect[x=y,x^2+y^2=2]", new String[] { "(1, 1)", "(-1, -1)" });
		t("Intersect[x=y,x^2+y^2=2, 1]", "(1, 1)");
		t("Intersect[x=y,x^2+y^2=2, (-5, -3)]", "(-1, -1)");
	}

	@Test
	public void cmdNumerator() {
		t("Numerator[ (x + 2)/(x+1) ]", "x + 2");
		t("Numerator[ 3/7 ]", "3");
		t("Numerator[ 5/(-8) ]", "-5");
		t("Numerator[ 2/0 ]", "1");
	}

	@Test
	public void cmdDenominator() {
		t("Denominator[ (x + 2)/(x+1) ]", "x + 1");
		t("Denominator[ 3/7 ]", "7");
		t("Denominator[ 5/(-8) ]", "8");
		t("Denominator[ 2/0 ]", "0");
	}

	@Test
	public void cmdMaximize() {
		t("slider:=Slider[0,5]", "0");
		t("Maximize[ 5-(3-slider)^2, slider ]", "3");
		t("ptPath:=Point[(x-3)^2+(y-4)^2=25]", "(0, 0)",
				StringTemplate.defaultTemplate);
		t("Maximize[ y(ptPath), ptPath ]", "(3, 9)",
				StringTemplate.defaultTemplate);
	}

	@Test
	public void cmdMinimize() {
		t("slider:=Slider[0,5]", "0");
		t("Minimize[ 5+(3-slider)^2, slider ]", "3");
		t("ptPath:=Point[(x-3)^2+(y-4)^2=25]", "(0, 0)",
				StringTemplate.defaultTemplate);
		t("Minimize[ y(ptPath), ptPath ]", "(3, -1)",
				StringTemplate.defaultTemplate);
	}

	@Test
	public void cmdIteration() {
		t("Iteration[ x*2, 2, 5 ]", "64");
		t("Iteration[ t*2, t, {(2,3)}, 5 ]", "(64, 96)");
		t("Iteration[ x*y, {1,1}, 6 ]", "720");
		t("Iteration[ x*y, {1,1}, 0 ]", "1");
		t("Iteration[ x*y, {1,1}, -1 ]", "NaN");
	}

	@Test
	public void cmdIterationList() {
		t("IterationList[ x*2, 2, 5 ]", "{2, 4, 8, 16, 32, 64}");
		t("IterationList[ a+b, a, b, {1,1}, 5 ]", "{1, 1, 2, 3, 5, 8}");
		t("IterationList[ x*y, {1,1}, 6 ]", "{1, 1, 2, 6, 24, 120, 720}");
	}

	@Test
	public void cmdImplicitSurface() {
		t("ImplicitSurface[sin(x)+sin(y)+sin(z)]",
				"sin(x) + sin(y) + sin(z) = 0");
	}

	@Test
	public void cmdSetConstructionStep() {
		app.setSaved();
		app.clearConstruction();
		t("cs=ConstructionStep[]", "1");
		t("2", "2");
		t("7", "7");
		t("SetConstructionStep[2]", new String[] {});
		t("cs", "2");
		t("SetConstructionStep[1]", new String[] {});
		t("cs", "1");
		app.clearConstruction();
	}

	@Test
	public void cmdSVD() {
		t("SVD[ {{1}} ]", "{{{1}}, {{1}}, {{1}}}");
	}

	@Test
	public void cmdSequence() {
		t("Sequence[ 4 ]", "{1, 2, 3, 4}");
		t("Sequence[ 3.2, 7.999 ]", "{3, 4, 5, 6, 7, 8}");
		t("Sequence[ -3.2, 3.2 ]", "{-3, -2, -1, 0, 1, 2, 3}");
		t("Sequence[ 3.2, -2 ]", "{3, 2, 1, 0, -1, -2}");
		t("Sequence[ t^2, t, 1, 4 ]", "{1, 4, 9, 16}");
		t("Sequence[ t^2, t, 1, 4, 2 ]", "{1, 9}");
		t("Sequence[ t^2, t, 1, 4, -2 ]", "{}");
		t("Length[Unique[Sequence[ random(), t, 1, 10]]]", "10");

	}

	@Test
	public void cmdOrthogonalPlane() {
		t("OrthogonalPlane[ (0,0,1), X=(p,2p,3p) ]", "x + 2y + 3z = 3");
		t("OrthogonalPlane[ (0,0,1), Vector[(1,2,3)] ]", "x + 2y + 3z = 3");
	}

	@Test
	public void cmdDifference() {
		t("Difference[Polygon[(0,0),(2,0),4],Polygon[(1,1),(3,1),(3,3),(1,3)]]",
				new String[] { "3", "(2, 1)", "(1, 1)", "(1, 2)", "(0, 2)",
						"(0, 0)", "(2, 0)", "1", "1", "1", "2", "2", "1" },
				StringTemplate.defaultTemplate);
		t("Difference[Polygon[(0,0),(2,0),4],Polygon[(1,1),(3,1),(3,3),(1,3)], true]",
				new String[] { "3", "3", "(3, 3)", "(1, 3)", "(1, 2)",
						"(2, 2)", "(2, 1)", "(3, 1)", "(2, 1)", "(1, 1)",
						"(1, 2)", "(0, 2)", "(0, 0)", "(2, 0)", "2", "1", "1",
						"1", "1", "2", "1", "1", "1", "2", "2", "1" },
				StringTemplate.defaultTemplate);
	}

	@Test
	public void parametricSyntaxes() {
		t("X=(s,2s)", "X = (0, 0) + s (1, 2)");
		t("Intersect[X=(s,s),x+y=2]", "(1, 1)");
	}

	private void ti(String in, String out) {
		testSyntax(in.replace("i", Unicode.IMAGINARY),
				new String[] { out.replace("i", Unicode.IMAGINARY) }, app, ap,
				StringTemplate.xmlTemplate);

	}
	@Test
	public void complexArithmetic() {
		ti("(0i)^2", "0 + 0i");
		ti("(0i)^0", "NaN - NaNi");
		ti("(0i)^-1", "NaN - NaNi");
		ti("(2+0i)^0", "1 + 0i");
		ti("(1/0+0i)^0", "NaN - NaNi");
	}

	@Test
	public void redefine() {
		t("la={1}", "{1}");
		t("lb={2}", "{2}");
		t("lc=la", "{1}");
		t("lc=lb", "{2}");
		t("1*lb", "{2}");

	}

	@Test
	public void parsePower() {
		t("a=4", "4");
		t("pia", "12.566370614359172");
		t("pix", "(" + Unicode.PI_STRING + " * x)");
		t("sinx", "sin(x)");
		t("x" + Unicode.PI_STRING, "(" + Unicode.PI_STRING + " * x)");
		t("sinxdeg", "sin((1*" + Unicode.DEGREE + " * x))");


	}

	@Test
	public void cmdSum() {
		t("listSum={1,10,1/2}", "{1, 10, 0.5}");
		t("Sum[ listSum , listSum]", "101.25");
		t("Sum[ listSum ]", "11.5");
		t("Sum[ listSum , 2 ]", "11");
		t("Sum[ listSum , 0 ]", "0");
		t("Sum[{x+y,0x+y}]", "x + y + (0 * x) + y");
		t("Sum[{x,y}]", "x + y");
		t("Sum[{(1,2),(3,4)}]", "(4, 6)");
		t("Sum[{(1,2,7),(3,4),(1,1,1)}]", "(5, 7, 8)");
		t("Sum[{\"Geo\",\"Gebra\"}]", "GeoGebra");
		t("Sum[{}]", "0");
		t("Sum[{x+y,2*x}]", "x + y + (2 * x)");
		t("Sum[x^k,k,1,5]", "x^(1) + x^(2) + x^(3) + x^(4) + x^(5)");
		t("Sum[2^k,k,1,5]", "62");
		t("Sum[(k,k),k,1,5]", "(15, 15)");
	}

	@Test
	public void cmdProduct() {
		t("Product[ {1,2,3,4} ]", "24");
		t("Product[ 1..10,  5 ]", "120");
		t("Product[ {1,2,3},  {100,1,2} ]", "18");
		t("round(Product[ k/(k+1),k,1,7 ],5)", "0.125");
		t("Product[{x,y}]", "(x * y)");
		t("Product[ (k,k),k,1,5 ]", "-480 - 480" + Unicode.IMAGINARY);

	}

	@Test
	public void cmdPlane() {
		t("Plane[ (0,0,1),(1,0,0),(0,1,0) ]", "x + y + z = 1");
		t("Plane[ Polygon[(0,0,1),(2,0,0),(0,3,0)] ]", "3x + 2y + 6z = 6");
		t("Plane[ Ellipse[(0,0,1),(2,0,0),(0,3,0)] ]", "3x + 2y + 6z = 6");
		t("Plane[ (1,2,3),X=(s,s,s) ]", "x - 2y + z = 0");
		t("Plane[ (1,2,3),x+y+z=0 ]", "x + y + z = 6");
		t("Plane[ X=(s,s,s+1),X=(s,s,s) ]", "-x + y = 0");
		t("Plane[ (0,0,1),Vector[(1,0,0)],Vector[(0,1,0)] ]", "z = 1");
	}

	@Test
	public void cmdSurface() {
		t("Surface[u*v,u+v,u^2+v^2,u,-1,1,v,1,3]",
				"((u * v), u + v, u^(2) + v^(2))");
		t("Surface[2x,2pi]", "(u, ((2 * u) * cos(v)), ((2 * u) * sin(v)))");
		t("Surface[2x,2pi,yAxis]",
				"((cos(v) * u), ((1 - cos(v) + cos(v)) * (2 * u)), ((-sin(v)) * u))");
	}

	@Test
	public void cmdCube() {
		t("Cube[(0,0,0),(0,0,2)]",
				new String[] { "8", "(2, 0, 0)", "(0, 2, 0)", "(0, 2, 2)",
						"(2, 2, 2)", "(2, 2, 0)", "4", "4", "4", "4", "4", "4",
						"2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2",
						"2" });
		t("Cube[(0,0,0),(0,2,0),(0,2,2)]",
				new String[] { "8", "(0, 0, 2)", "(2, 0, 0)", "(2, 2, 0)",
						"(2, 2, 2)", "(2, 0, 2)", "4", "4", "4", "4", "4", "4",
						"2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2",
						"2" });
		t("Cube[(0,0,0),(0,0,2),xAxis]",
				new String[] { "8", "(0, -2, 2)", "(0, -2, 0)", "(2, 0, 0)",
						"(2, 0, 2)", "(2, -2, 2)", "(2, -2, 0)", "4", "4", "4",
						"4", "4", "4", "2", "2", "2", "2", "2", "2", "2", "2",
						"2", "2", "2",
						"2" });
	}

	@Test
	public void cmdVolume() {
		t("Volume[Cube[(0,0,1),(0,1,0)]]", eval("round(sqrt(8),5)"),
				StringTemplate.editTemplate);
		t("Volume[Sphere[(0,0,1),4]]", eval("round(4/3*pi*4^3,5)"),
				StringTemplate.editTemplate);
	}

	@Test
	public void cmdSphere() {
		t("Sphere[(0,0,1),4]", indices("x^2 + y^2 + (z - 1)^2 = 16"));
		t("Sphere[(0,0,1),(0,4,1)]", indices("x^2 + y^2 + (z - 1)^2 = 16"));
	}

	@Test
	public void cmdCone() {
		t("Cone[x^2+y^2=9,4]", new String[] { eval("round(12*pi,5)"),
 "X = (0, 0, 4)",
 eval("round(pi*15,5)"), },
				StringTemplate.editTemplate);
		t("Cone[(0,0,0),(0,0,4),3]", new String[] { eval("round(12*pi,5)"),
				"X = (0, 0, 0) + (3 cos(t), -3 sin(t), 0)",
				eval("round(pi*15,5)"), },
				StringTemplate.editTemplate);
		t("Cone[(0,0,0),Vector[(0,0,4)],pi/4]",
				new String[] {
 indices("x^2 + y^2 - 1z^2 = 0") },
				StringTemplate.editTemplate);
	}

	private String indices(String string) {
		return string.replace("^2", Unicode.Superscript_2 + "");
	}

	private String eval(String string) {
		return ap.evaluateToNumeric(string, true)
				.toValueString(StringTemplate.xmlTemplate);
	}
}
