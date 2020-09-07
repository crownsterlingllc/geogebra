package org.geogebra.common.kernel.geos;

import static org.geogebra.test.TestStringUtil.unicode;
import static org.junit.Assert.assertEquals;

import org.geogebra.common.BaseUnitTest;
import org.geogebra.common.factories.AwtFactoryCommon;
import org.geogebra.common.jre.headless.AppCommon;
import org.geogebra.common.jre.headless.LocalizationCommon;
import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.StringTemplate;
import org.geogebra.common.main.AppCommon3D;
import org.geogebra.common.plugin.GeoClass;
import org.geogebra.test.commands.AlgebraTestHelper;
import org.junit.Assert;
import org.junit.Test;

import com.himamis.retex.editor.share.util.Unicode;

public class GeoInputBoxLinkedGeoTest extends BaseUnitTest {

	private GeoInputBox inputBox;

	@Override
	public AppCommon createAppCommon() {
		return new AppCommon3D(new LocalizationCommon(3),
				new AwtFactoryCommon());
	}

	@Test
	public void shouldNotShowQuotesForText() {
		setupInput("txt", "\"GeoGebra Rocks\"");
		t("ib", "GeoGebra Rocks");
		updateInput("GeoGebra Really Rocks");
		t("txt", "GeoGebra Really Rocks");
		hasType("txt", GeoClass.TEXT);
	}

	@Test
	public void shouldShowNewlineQuotesForText() {
		setupInput("txt", "\"GeoGebra\\\\nRocks\"");
		assertEquals("GeoGebra\\\\nRocks", inputBox.getText());
		updateInput("GeoGebra\\\\nReally\\\\nRocks");
		t("txt", "GeoGebra\nReally\nRocks");
	}

	@Test
	public void enteringNewValueShouldKeepVectorType() {
		setupAndCheckInput("v", "(1, 3)");
		t("Rename(v,\"V\")");
		updateInput("(1, 5)");
		t("V", "(1, 5)");
		hasType("V", GeoClass.VECTOR);
	}

	@Test
	public void enteringNewValueShouldKeepVectorType3D() {
		setupAndCheckInput("v3", "(1, 3, 6)");
		updateInput("(1, 5)");
		t("v3", "(1, 5, 0)");
		hasType("v3", GeoClass.VECTOR3D);
	}

	@Test
	public void enteringNewValueShouldKeepPlaneType() {
		setupAndCheckInput("p", "x + y - z = 0");
		updateInput("x = y");
		t("p", "x - y = 0");
		hasType("p", GeoClass.PLANE3D);
	}

	@Test
	public void enteringNewValueShouldKeepComplexNumber() {
		setupAndCheckInput("P", "1 + i");
		updateInput("7");
		t("P", "7 + 0" + Unicode.IMAGINARY);
		assertEquals("7",
				lookup("P").getDefinition(StringTemplate.defaultTemplate));
		hasType("P", GeoClass.POINT);
	}

	@Test
	public void enteringShortLinearExprShouldKeepLineType() {
		setupAndCheckInput("l", "y = 2x + 3");
		updateInput("3x + 5");
		t("l", "y = 3x + 5");
		hasType("l", GeoClass.LINE);
	}

	@Test
	public void symbolicShouldShowDefinition() {
		setupInput("l", "1 + 1 / 5");
		((GeoNumeric) lookup("l")).setSymbolicMode(true, false);
		inputBox.setSymbolicMode(true, false);
		assertEquals("1+1/5", inputBox.getTextForEditor());
		((GeoNumeric) lookup("l")).setSymbolicMode(false, false);
		assertEquals("1+1/5", inputBox.getTextForEditor());
	}

	@Test
	public void nonsymbolicShouldShowDefinitionForFraction() {
		setupInput("l", "1 + 1 / 5");
		((GeoNumeric) lookup("l")).setSymbolicMode(true, true);
		inputBox.setSymbolicMode(false, false);
		assertEquals("6 / 5", inputBox.getText());
		((GeoNumeric) lookup("l")).setSymbolicMode(false, true);
		assertEquals("1.2", inputBox.getText());
	}

	@Test
	public void shouldShowValueForSimpleNumeric() {
		setupInput("l", "5");
		inputBox.setSymbolicMode(true, false);
		assertEquals("5", inputBox.getText());
		assertEquals("5", inputBox.getTextForEditor());
	}

	@Test
	public void shouldBeEmptyAfterSettingLineUndefined() {
		setupInput("f", "y = 5");
		t("SetValue(f, ?)");
		assertEquals("", inputBox.getText());
	}

	@Test
	public void symbolicShouldBeEmptyAfterSettingLineUndefined() {
		setupInput("f", "y = 5");
		t("SetValue(f, ?)");
		inputBox.setSymbolicMode(true, false);
		assertEquals("", inputBox.getText());
		assertEquals("", inputBox.getTextForEditor());
	}

	@Test
	public void shouldBeEmptyAfterSettingPlaneUndefined() {
		setupInput("eq1", "4x + 3y + 2z = 1");
		t("SetValue(eq1, ?)");
		assertEquals("", inputBox.getText());
	}

	@Test
	public void symbolicShouldBeEmptyAfterSettingPlaneUndefined() {
		setupInput("eq1", "4x + 3y + 2z = 1");
		t("SetValue(eq1, ?)");
		inputBox.setSymbolicMode(true, false);
		assertEquals("", inputBox.getText());
		assertEquals("", inputBox.getTextForEditor());
	}

	@Test
	public void symbolicShouldShowDefinitionFor3DPoints() {
		setupInput("P", "(?,?,?)");
		inputBox.setSymbolicMode(true, false);
		assertEquals("(?,?,?)", inputBox.getTextForEditor());
		updateInput("(sqrt(2), 1/3, 0)");
		assertEquals("(sqrt(2),1/3,0)", inputBox.getTextForEditor());
		add("SetValue(P,?)");
		assertEquals("(?,?,?)", inputBox.getTextForEditor());
	}

	@Test
	public void shouldAcceptLinesConicsAndFunctionsForImplicitCurve() {
		setupInput("eq1", "x^3 = y^2");
		updateInput("x = y"); // line
		assertEquals("x = y", inputBox.getText());
		updateInput("y = x"); // function (linear)
		assertEquals("y = x", inputBox.getText());
		updateInput("y = x^2"); // function (quadratic)
		assertEquals(unicode("y = x^2"), inputBox.getText());
		updateInput("x^2 = y^2"); // conic
		assertEquals(unicode("x^2 = y^2"), inputBox.getText());
	}

	@Test
	public void shouldAcceptLinesAndFunctionsForConics() {
		setupInput("eq1", "x^2 = y^2");
		updateInput("x = y"); // line
		assertEquals("x = y", inputBox.getText());
		updateInput("y = x"); // function (linear)
		assertEquals("y = x", inputBox.getText());
		updateInput("y = x^2"); // function (quadratic)
		assertEquals(unicode("y = x^2"), inputBox.getText());
	}

	@Test
	public void shouldAcceptFunctionsForLines() {
		setupInput("eq1", "x = y");
		updateInput("y = x"); // function (linear)
		assertEquals("y = x", inputBox.getText());
	}

	@Test
	public void shouldBeEmptyAfterPlaneInputUndefined() {
		setupInput("eq1", "4x + 3y + 2z = 1");
		GeoElement ib2 = add("in2=InputBox(eq1)");
		updateInput("?");
		// both input boxes undefined, we prefer empty string over question mark
		// even if that's what the user typed (APPS-1246)
		assertEquals("", inputBox.getText());
		assertEquals("", ((GeoInputBox) ib2).getText());
	}

	@Test
	public void shouldBeEmptyAfterImplicitUndefined() {
		setupInput("eq1", "x^2=y^3");
		updateInput("?");
		assertEquals("", inputBox.getText());
		assertEquals("eq1\\, \\text{undefined} ", lookup("eq1")
				.getLaTeXAlgebraDescriptionWithFallback(false,
						StringTemplate.defaultTemplate, false));
	}

	@Test
	public void shouldBeEmptyAfterDependentNumberUndefined() {
		add("a=1");
		setupInput("b", "3a");
		updateInput("x=y");
		assertEquals("b\\, \\text{undefined} ", lookup("b")
				.getLaTeXAlgebraDescriptionWithFallback(false,
						StringTemplate.defaultTemplate, false));
	}

	@Test
	public void shouldAllowQuestionMarkWhenLinkedToText() {
		setupInput("txt", "\"GeoGebra Rocks\"");
		updateInput("?");
		assertEquals("?", inputBox.getText());
	}

	@Test
	public void shouldBeEmptyAfterSettingComplexUndefined() {
		setupInput("z1", "3 + i");
		t("SetValue(z1, ?)");
		assertEquals("", inputBox.getText());
	}

	@Test
	public void symbolicShouldBeEmptyAfterSettingComplexUndefined() {
		setupInput("z1", "3 + i");
		t("SetValue(z1, ?)");
		inputBox.setSymbolicMode(true, false);
		assertEquals("", inputBox.getText());
		assertEquals("", inputBox.getTextForEditor());
	}

	@Test
	public void functionParameterShouldNotChangeToX() {
		add("f(c) = c / ?");
		inputBox = add("ib=InputBox(f)");
		inputBox.setSymbolicMode(false, false);
		assertEquals("c / ?", inputBox.getText());
		updateInput("?");
		assertEquals("", inputBox.getText());
		updateInput("c / 3");
		assertEquals("c / 3", inputBox.getText());
	}

	@Test
	public void independentVectorsMustBeColumnEditable() {
		setupInput("l", "(1, 2, 3)");
		assertEquals("{{1}, {2}, {3}}", inputBox.getTextForEditor());
	}

	@Test
	public void symbolicShouldSupportVectorsWithVariables() {
		add("a: 1");
		setupInput("l", "(1, 2, a)");
		assertEquals("(1, 2, a)", inputBox.getText());
		assertEquals("{{1}, {2}, {a}}", inputBox.getTextForEditor());
	}

	@Test
	public void compound2DVectorsMustBeFlatEditable() {
		add("u: (1, 2)");
		add("v: (3, 4)");
		setupInput("l", "u + v");
		assertEquals("u+v", inputBox.getTextForEditor());
	}

	@Test
	public void compound3DVectorsMustBeFlatEditable() {
		add("u: (1, 2, 3)");
		add("v: (3, 4, 5)");
		setupInput("l", "u + v");
		assertEquals("u+v", inputBox.getTextForEditor());
	}

	@Test
	public void twoVariableFunctionParameterShouldNotChangeToX() {
		add("g(p, q) = p / ?");
		inputBox = add("ib=InputBox(g)");
		inputBox.setSymbolicMode(false, false);
		assertEquals("p / ?", inputBox.getText());
		updateInput("?");
		assertEquals("", inputBox.getText());
		updateInput("p / q");
		assertEquals("p / q", inputBox.getText());
	}

	@Test
	public void testGeoNumericExtendsMinMaxInSymbolic() {
		GeoNumeric numeric = add("a = 5");
		numeric.setShowExtendedAV(true);
		numeric.initAlgebraSlider();
		Assert.assertFalse(numeric.getIntervalMax() >= 20);
		Assert.assertFalse(numeric.getIntervalMin() <= -20);

		GeoInputBox inputBox = add("ib = InputBox(a)");
		inputBox.setSymbolicMode(true);

		inputBox.updateLinkedGeo("20");
		inputBox.updateLinkedGeo("-20");

		Assert.assertTrue(numeric.getIntervalMax() >= 20);
		Assert.assertTrue(numeric.getIntervalMin() <= -20);
	}

	@Test
	public void testGeoNumericIsClampedToMinMaxInNonSymbolic() {
		GeoNumeric numeric = add("a = 0");
		numeric.setShowExtendedAV(true);
		numeric.initAlgebraSlider();

		Assert.assertEquals(-5, numeric.getIntervalMin(), Kernel.MAX_PRECISION);
		Assert.assertEquals(5, numeric.getIntervalMax(), Kernel.MAX_PRECISION);

		inputBox = add("ib = InputBox(a)");

		inputBox.updateLinkedGeo("-10");
		Assert.assertEquals(-5, numeric.getValue(), Kernel.MAX_PRECISION);

		inputBox.updateLinkedGeo("10");
		Assert.assertEquals(5, numeric.getValue(), Kernel.MAX_PRECISION);
	}

	private void t(String input, String... expected) {
		AlgebraTestHelper.testSyntaxSingle(input, expected,
				getApp().getKernel().getAlgebraProcessor(),
				StringTemplate.xmlTemplate);
	}

	private void hasType(String label, GeoClass geoClass) {
		assertEquals(lookup(label).getGeoClassType(), geoClass);
	}

	private void updateInput(String string) {
		inputBox.textObjectUpdated(new ConstantTextObject(string));
	}

	private void setupAndCheckInput(String label, String value) {
		setupInput(label, value);
		assertEquals(value,
				inputBox.toValueString(StringTemplate.testTemplate));
	}

	private void setupInput(String label, String value) {
		add(label + ":" + value);
		inputBox = add("ib=InputBox(" + label + ")");
	}

	@Test
	public void testCanBeSymbolicForPlane() {
		add("A = (0,0)");
		add("B = (2,0)");
		add("C = (2,2)");
		add("p:Plane(A,B,C)");
		GeoInputBox inputBox = add("InputBox(p)");
		Assert.assertTrue(inputBox.canBeSymbolic());
	}

	@Test
	public void testCanBeSymbolicForEquation() {
		add("eq1:x^3+y^3=1");
		GeoInputBox inputBox1 = add("InputBox(eq1)");
		add("eq2:x^2+y^2+z^2=1");
		GeoInputBox inputBox2 = add("InputBox(eq2)");
		Assert.assertTrue(inputBox1.canBeSymbolic());
		Assert.assertTrue(inputBox2.canBeSymbolic());
	}

	@Test
	public void symbolicShouldBeEmptyAfterSettingConicUndefined() {
		setupInput("eq1", "xx+yy = 1");
		inputBox.setSymbolicMode(true, false);
		updateInput("?");
		assertEquals("", inputBox.getTextForEditor());
		getApp().setXML(getApp().getXML(), true);
		assertEquals("", inputBox.getTextForEditor());
		assertEquals("eq1\\, \\text{undefined} ", lookup("eq1")
				.getLaTeXAlgebraDescriptionWithFallback(false,
						StringTemplate.defaultTemplate, false));
	}

	@Test
	public void symbolicShouldBeEmptyAfterSettingQuadricUndefined() {
		setupInput("eq1", "x^2 + y^2 + z^2 = 1");
		inputBox.setSymbolicMode(true, false);
		inputBox.updateLinkedGeo("?");
		assertEquals("", inputBox.getTextForEditor());
		getApp().setXML(getApp().getXML(), true);
		assertEquals("", inputBox.getTextForEditor());
		assertEquals("eq1\\, \\text{undefined} ", lookup("eq1")
				.getLaTeXAlgebraDescriptionWithFallback(false,
						StringTemplate.defaultTemplate, false));
	}

	@Test
	public void minusShouldStayInNumerator() {
		setupInput("f", "x");
		inputBox.setSymbolicMode(true, false);
		updateInput("(-1)/4 x");
		assertEquals("-1/4 x", inputBox.getTextForEditor());
		assertEquals("\\frac{-1}{4} \\; x", inputBox.getText());
	}

	@Test
	public void minusShouldStayInFrontOfFraction() {
		setupInput("f", "x");
		inputBox.setSymbolicMode(true, false);
		updateInput("-(1/4) x");
		assertEquals("-(1/4) x", inputBox.getTextForEditor());
		assertEquals("-\\frac{1}{4} \\; x", inputBox.getText());
	}

	@Test
	public void implicitMultiplicationWithParenthesis() {
		add("c = 2");
		add("a = c + 2");
		setupInput("a", "2");
		updateInput("cc(2)");
		assertEquals("c c * 2", inputBox.getText());
	}

	@Test
	public void implicitMultiplicationWithEvaluatable() {
		add("f: y = 2 * x + 3");
		setupInput("g", "x");
		updateInput("xf(x) + 4");
		assertEquals("x f(x) + 4", inputBox.getText());
	}
}
