package SOM_GeometryProj_PKG.geom_ObjExamples;

import java.util.TreeMap;

import SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers.Geom_3DLineMapMgr;
import base_JavaProjTools_IRender.base_Render_Interface.IRenderInterface;
import base_Math_Objects.MyMathUtils;
import base_Math_Objects.vectorObjs.floats.myPointf;
import base_Math_Objects.vectorObjs.floats.myVectorf;
import base_SOM_Objects.som_examples.SOM_ExDataType;
import base_SOM_Objects.som_geom.SOM_GeomMapManager;
import base_SOM_Objects.som_geom.geom_UI.SOM_AnimWorldWin;
import base_SOM_Objects.som_geom.geom_examples.SOM_GeomLineObj;
import base_SOM_Objects.som_geom.geom_examples.SOM_GeomMapNode;
import base_SOM_Objects.som_geom.geom_utils.geom_objs.SOM_GeomObjDrawType;
import base_SOM_Objects.som_geom.geom_utils.geom_objs.SOM_GeomObjTypes;
import base_SOM_Objects.som_geom.geom_utils.geom_objs.SOM_GeomSamplePointf;
import base_UI_Objects.my_procApplet;
import processing.core.PConstants;

public class Geom_3DLineSOMExample extends SOM_GeomLineObj {
	private static int IDGen = 0;	
	/**
	 * string array denoting names of features
	 */
	public static final String[] ftrNames = {"Dir x","Dir y","Dir z","Origin x","Origin y","Origin z"};
	/**
	 * feature vector size for this object : 3d point + 3d line
	 */
	public static final int _numFtrs = ftrNames.length;
	/**
	 * normal to line
	 */
	public myVectorf biNorm;

	/**
	 * Constructor for 3d line object shared functionality
	 * 	
	 * @param _mapMgr : owning map manager
	 * @param _exType : whether this is training/testing/validation etc.
	 * @param _oid : pre-defined string ID put in SOM_Example OID field
	 * @param _srcSmpls : the points and owning objects that make up the minimally defining set of points for this object.  If src objects are null, then this object is a foundation/source object
	 * @param _numSmplPts : # of sample points to build for this object TODO move to post-construction process
	 * @param _shouldBuildSamples : whether we should build samples for this object
	 * @param _shouldBuildVisRep : whether we should pre-build a mesh representation of this object
	 */
	public Geom_3DLineSOMExample(Geom_3DLineMapMgr _mapMgr, SOM_ExDataType _exType, String _id, SOM_GeomSamplePointf[] _srcSmpls, int _numSmplPts, boolean _shouldBuildSamples, boolean _shouldBuildVisRep) {
		super(_mapMgr, _exType, _id, _srcSmpls, SOM_GeomObjTypes.line_3D, _numSmplPts,  _shouldBuildSamples, _shouldBuildVisRep);
	}//ctor		
	
	/**
	 * Constructor to build a 3d line based on csv data
	 * @param _mapMgr : owning map manager
	 * @param _exType : whether this is training/testing/validation etc.
	 * @param _oid : pre-defined string ID put in SOM_Example OID field
	 * @param _csvDat : String from CSV describing object
	 */
	public Geom_3DLineSOMExample(Geom_3DLineMapMgr _mapMgr, SOM_ExDataType _exType, String _oid, String _csvDat) {
		super(_mapMgr, _exType, _oid, _csvDat, SOM_GeomObjTypes.line_3D);
	}//csv ctor
	
	/**
	 * ctor to build a 3d line corresponding to map node
	 * @param _mapMgr : owning map manager
	 * @param _mapNode : the map node being represented
	 */
	public Geom_3DLineSOMExample(Geom_3DLineMapMgr _mapMgr, SOM_GeomMapNode _mapNode) {
		super(_mapMgr, _mapNode, SOM_GeomObjTypes.line_3D);
	}//bmu ctor

	public Geom_3DLineSOMExample(Geom_3DLineSOMExample _otr) {
		super(_otr);
	}//copy ctor

	/**
	 * build the normal and binormal to line from dir vector
	 * @return
	 */
	protected void buildNormFromDir() {
		if (1.0f != (dir._dot(myVectorf.FORWARD))) {norm = dir._cross(myVectorf.FORWARD)._normalize();} 
		else {		norm = dir._cross(myVectorf.UP)._normalize();}
		biNorm = norm._cross(dir)._normalize();		
	}
	
	@Override
	protected myVectorf _buildDir() {
		myVectorf _dir = new myVectorf(getSrcPts()[0],getSrcPts()[1]);	
		_dir._normalize();		
		if((_dir.y < 0.0) || 
		((_dir.y == 0.0) && ((_dir.x < 0) || ((_dir.x == 0) && (_dir.z < 0))))) {	_dir._mult(-1.0);	}		//force all dir vectors to have the same "polarity" so 2 points always form the same line
		return _dir;
	}
	
	/**
	 * For every bound in line's space, set t value in bound's ortho dim
	 * @return
	 */
	@Override
	protected final TreeMap<Float, Integer> buildWorldBounds(float[] mins){
		TreeMap<Float, Integer> tmpBnds = new TreeMap<Float, Integer>();
		float curMax;
		float[] ptA_ara = getSrcPts()[0].asArray(), dirAra = dir.asArray();
		float [][] worldBounds = ((SOM_GeomMapManager) mapMgr).getWorldBounds();
		for(int i=0;i<mins.length;++i) {
			tmpBnds.put((mins[i] - ptA_ara[i])/dirAra[i], 2*i);
			curMax=worldBounds[0][i] + worldBounds[1][i];
			tmpBnds.put((curMax - ptA_ara[i])/dirAra[i], 1 + 2*i);
		}
		//has 6 values, want 2 values points closest to 0 (2 middle values) - 3 will be negative, and 3 will be positive if a is within bounds
		tmpBnds.remove(tmpBnds.firstKey());
		tmpBnds.remove(tmpBnds.firstKey());
		tmpBnds.remove(tmpBnds.lastKey());
		tmpBnds.remove(tmpBnds.lastKey());
		return tmpBnds;
	}
	
	
	/**
	 * find t value for point on line - expects point to be on line!
	 * @param pt
	 * @return
	 */
	@Override
	public final float getTForPointOnLine(myPointf pt) {
		//pt  == origin  + t * dir
		// -> t = (pt.x - origin.x)/dir.x 
		float t = 0.0f;
		if(Math.abs(dir.x)> MyMathUtils.EPS) {			t = (pt.x - getSrcPts()[0].x)/dir.x;} 
		else if(Math.abs(dir.y)> MyMathUtils.EPS) {		t = (pt.y - getSrcPts()[0].y)/dir.y;}	
		else {											t = (pt.z - getSrcPts()[0].z)/dir.z;}	//line along z axis				
		return t;
	}

	////////////////////////////
	// feature functionality (inherited from SOM_Example
	
	/**
	 * build an array of source points from the characteristic features of the source map node
	 */
	@Override
	protected final SOM_GeomSamplePointf[] buildSrcPtsFromBMUMapNodeFtrs(float[] mapFtrs, String _dispLabel) {
		SOM_GeomSamplePointf[] res = new SOM_GeomSamplePointf[_numSrcPts];
		myVectorf tmpNorm = new myVectorf(mapFtrs[0],mapFtrs[1],mapFtrs[2])._normalize();//should be normalized, but in case it isn't
		myPointf ctrPt = new myPointf(mapFtrs[3],mapFtrs[4],mapFtrs[5]);
		res[0]= new SOM_GeomSamplePointf(myVectorf._add(ctrPt, 11.0f,tmpNorm), _dispLabel+"_BMU_pt_0", this);
		res[1]= new SOM_GeomSamplePointf(myVectorf._add(ctrPt, -12.0f,tmpNorm), _dispLabel+"_BMU_pt_1", this);
		return res;			
	}//buildSrcPtsFromBMUMapNode
	
	
	/**
	 * object shape-specific feature building - ftrVecMag calced in base class
	 */
	@Override
	protected final void buildFeaturesMap_Indiv() {
		//build feature-based example for this example - for lines example should be closest origin point + normalized dir vector
		//ONLY USE x,y VALS
		ftrMaps[unNormFtrMapTypeKey].put(0,dir.x);
		ftrMaps[unNormFtrMapTypeKey].put(1,dir.y);
		ftrMaps[unNormFtrMapTypeKey].put(2,dir.z);
		
		ftrMaps[unNormFtrMapTypeKey].put(3,origin.x);
		ftrMaps[unNormFtrMapTypeKey].put(4,origin.y);
		ftrMaps[unNormFtrMapTypeKey].put(5,origin.z);
	}


	///////////////////////////
	// draw functionality
	
	@Override
	protected final void _drawMe_Geom(IRenderInterface pa, SOM_GeomObjDrawType drawType) {
		pa.pushMatState();	
		if((drawType.getVal() == 2) || (drawType.getVal() == 3)) {
			pa.setStroke(120, 120,120,150);
			pa.setStrokeWt(1.0f);
			pa.drawLine(dispEndPts[0],dispEndPts[1]);			
		}  else {
			pa.setStrokeWt(2.0f);
			pa.drawLine(dispEndPts[0],dispEndPts[1]);			
		}
		
		_drawPointAtLoc_3D(pa,dispEndPts[0], 2.0f);
		_drawPointAtLoc_3D(pa,dispEndPts[1], 2.0f);
		_drawPointAtLoc_3D(pa,getSrcPts()[0], 2.0f);
		_drawPointAtLoc_3D(pa,getSrcPts()[1], 2.0f);
		_drawPointAtLoc_3D(pa,origin, 2.0f);			
		pa.popMatState();
	}

	@Override
	public final void drawMyLabel(IRenderInterface pa, SOM_AnimWorldWin animWin) {
		pa.pushMatState();		
		pa.setFill(labelClrAra,255);
		pa.setStroke(labelClrAra,255);
		pa.setStrokeWt(2.0f);
		//(myPointf P, float r, String s, myVectorf D, int clr, boolean flat)
		_drawLabelAtLoc_3D(pa,dispEndPts[0], animWin, dispLabel+dispAra[0], 1.5f, 2.5f);
		_drawLabelAtLoc_3D(pa,dispEndPts[1], animWin, dispLabel+dispAra[1], 1.5f, 2.5f);
		_drawLabelAtLoc_3D(pa,getSrcPts()[0], animWin, "pt a :"+getSrcPts()[0].toStrBrf(), 1.5f, 2.5f);
		_drawLabelAtLoc_3D(pa,getSrcPts()[1], animWin, "pt b :"+getSrcPts()[1].toStrBrf(), 1.5f, 2.5f);
		_drawLabelAtLoc_3D(pa, origin, animWin, dispLabel+"| Origin " + origin.toStrBrf() + " | Dir : " + dir.toStrBrf() +" | " +dispAra[0]+"->"+dispAra[1], 1.5f, 2.5f);
		pa.popMatState();	
	}
	
	protected float modCnt = 0;//counter that will determine when the color should switch
	
	private final float blinkDist = 20.0f;
	
	protected final void cylinder(IRenderInterface pa, myPointf A, myPointf B, float r, int[] c1, int[] c2) {
		myVectorf V = new myVectorf(A,B);

		float da = MyMathUtils.TWO_PI_F/36;
		pa.pushMatState();	
		((my_procApplet)pa).beginShape(PConstants.QUAD_STRIP);
			for(float a=0; a<=MyMathUtils.TWO_PI_F+da; a+=da) {
				float rCosA = (float) (r*Math.cos(a));
				float rSinA = (float) (r*Math.sin(a));
				pa.setStroke(c1, 255); 
				//gl_vertex(myPoint._add(P,r*cos(a),I,r*sin(a),J,0,V)); 
				pa.gl_vertex(myPointf._add(A,rCosA,norm,rSinA,biNorm)); 
				pa.setStroke(c2, 255);
				pa.gl_vertex(myPointf._add(A,rCosA,norm,rSinA,biNorm, 1.0f, V));}
		((my_procApplet)pa).endShape();
		pa.popMatState();	
	}
	
	@Override
	protected final void _drawMeSelected(IRenderInterface pa, float animTmMod) {
		modCnt += animTmMod*2.0f;
		if(modCnt > 1.0){	modCnt = 0.0f;	}//blink every ~second
		
		cylinder(pa, dispEndPts[0], dispEndPts[1], blinkDist*modCnt, locClrAra, rndClrAra);
		
	}//_drawMeSelected


	/**
	 * initialize object's ID
	 */
	protected final int incrID() {return IDGen++;}



}//class SOM_Line
