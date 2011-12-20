package geogebra.web.kernel;

import geogebra.common.io.DocHandler;
import geogebra.common.kernel.AbstractAnimationManager;
import geogebra.common.kernel.AbstractKernel;
import geogebra.common.kernel.AbstractUndoManager;
import geogebra.common.kernel.Construction;
import geogebra.common.kernel.EquationSolverInterface;
import geogebra.common.kernel.MacroInterface;
import geogebra.common.kernel.MacroKernelInterface;
import geogebra.common.kernel.SystemOfEquationsSolverInterface;
import geogebra.common.kernel.arithmetic.Equation;
import geogebra.common.kernel.arithmetic.FunctionalNVar;
import geogebra.common.kernel.arithmetic.MyList;
import geogebra.common.kernel.arithmetic.Polynomial;
import geogebra.common.kernel.cas.GeoGebraCasInterface;
import geogebra.common.kernel.commands.AbstractCommandDispatcher;
import geogebra.common.kernel.geos.AbstractGeoElementSpreadsheet;
import geogebra.common.kernel.geos.AbstractGeoTextField;
import geogebra.common.kernel.geos.GeoBoolean;
import geogebra.common.kernel.geos.GeoConicPart;
import geogebra.common.kernel.geos.GeoElement;
import geogebra.common.kernel.geos.GeoElementGraphicsAdapter;
import geogebra.common.kernel.geos.GeoFunctionable;
import geogebra.common.kernel.geos.GeoList;
import geogebra.common.kernel.geos.GeoNumeric;
import geogebra.common.kernel.geos.GeoPolygon;
import geogebra.common.kernel.implicit.GeoImplicitPoly;
import geogebra.common.kernel.kernelND.GeoPointND;
import geogebra.common.kernel.optimization.ExtremumFinder;
import geogebra.common.kernel.parser.ParserInterface;
import geogebra.common.main.AbstractApplication;
import geogebra.common.util.GgbMat;
import geogebra.common.util.LaTeXCache;
import geogebra.common.util.NumberFormatAdapter;
import geogebra.common.util.ScientificFormatAdapter;
import geogebra.web.main.Application;
import geogebra.web.util.NumberFormat;

public class Kernel extends AbstractKernel {

	public Kernel(Application application) {
		// TODO Auto-generated constructor stub
	}

	@Override
    public NumberFormatAdapter getNumberFormat() {
	    // TODO Auto-generated method stub
	    return new NumberFormat();
    }

	@Override
    public NumberFormatAdapter getNumberFormat(String s) {
	    // TODO Auto-generated method stub
	    return new NumberFormat(s);
    }

	@Override
    public GeoElementGraphicsAdapter newGeoElementGraphicsAdapter() {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public ScientificFormatAdapter getScientificFormat(int a, int b, boolean c) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    protected void notifyEuclidianViewCE() {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public LaTeXCache newLaTeXCache() {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public AbstractApplication getApplication() {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public GeoGebraCasInterface newGeoGebraCAS() {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public AbstractAnimationManager getAnimatonManager() {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public AbstractGeoElementSpreadsheet getGeoElementSpreadsheet() {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public GgbMat getGgbMat(MyList myList) {
	    // TODO Auto-generated method stub
	    return null;
    }


	@Override
    public GeoNumeric getDefaultNumber(boolean geoAngle) {
	    // TODO Auto-generated method stub
	    return null;
    }


	
	@Override
    public GeoElement[] PolygonND(String[] labels, GeoPointND[] P) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public GeoElement[] PolyLineND(String[] labels, GeoPointND[] P) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public GeoConicPart newGeoConicPart(Construction cons, int type) {
	    // TODO Auto-generated method stub
	    return null;
    }
	

	@Override
    public GeoImplicitPoly newGeoImplicitPoly(Construction cons) {
	    // TODO Auto-generated method stub
	    return null;
    }

	
	@Override
    public ParserInterface getParser() {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public ExtremumFinder getExtremumFinder() {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public EquationSolverInterface getEquationSolver() {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public void resetGeoGebraCAS() {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public AbstractUndoManager getUndoManager(Construction cons) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public GeoImplicitPoly ImplicitPoly(String label, Polynomial lhs) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public GeoElement DependentImplicitPoly(String label, Equation equ) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public AbstractCommandDispatcher getCommandDispatcher() {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public GeoElement[] useMacro(String[] labels, MacroInterface macro,
            GeoElement[] arg) {
	    // TODO Auto-generated method stub
	    return null;
    }

	public DocHandler newMyXMLHandler(Construction cons) {
	    // TODO Auto-generated method stub
	    return null;
    }


	@Override
    public GeoElement createGeoElement(Construction cons2, String type) {
	    // TODO Auto-generated method stub
	    return null;
    }


	@Override
    public SystemOfEquationsSolverInterface getSystemOfEquationsSolver(
            EquationSolverInterface eSolver) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public AbstractGeoTextField getGeoTextField(Construction cons) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public GgbMat getGgbMat(GeoList inputList) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public GeoElement TravelingSalesman(String a, GeoList b) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public GeoElement Voronoi(String a, GeoList b) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public GeoElement ConvexHull(String a, GeoList b) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public GeoElement MinimumSpanningTree(String a, GeoList b) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public GeoElement DelauneyTriangulation(String a, GeoList b) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public GeoElement ShortestDistance(String label, GeoList geoList,
            GeoPointND geoPointND, GeoPointND geoPointND2, GeoBoolean geoBoolean) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public GeoElement SolveODE(String label, FunctionalNVar functionalNVar,
            FunctionalNVar functionalNVar2, GeoNumeric geoNumeric,
            GeoNumeric geoNumeric2, GeoNumeric geoNumeric3,
            GeoNumeric geoNumeric4) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public GeoElement SolveODE2(String label, GeoFunctionable geoFunctionable,
            GeoFunctionable geoFunctionable2, GeoFunctionable geoFunctionable3,
            GeoNumeric geoNumeric, GeoNumeric geoNumeric2,
            GeoNumeric geoNumeric3, GeoNumeric geoNumeric4,
            GeoNumeric geoNumeric5) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public GeoElement[] Union(String[] labels, GeoPolygon geoPolygon,
            GeoPolygon geoPolygon2) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public MacroKernelInterface newMacroKernel() {
	    // TODO Auto-generated method stub
	    return null;
    }

	
}