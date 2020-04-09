package SOM_GeometryProj_PKG.geom_ObjExamples;

import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers.Geom_3DLineMapMgr;
import base_JavaProjTools_IRender.base_Render_Interface.IRenderInterface;
import base_Math_Objects.MyMathUtils;
import base_Math_Objects.vectorObjs.floats.myPointf;
import base_Math_Objects.vectorObjs.floats.myVectorf;
import base_SOM_Objects.som_examples.SOM_ExDataType;
import base_SOM_Objects.som_geom.geom_UI.SOM_AnimWorldWin;
import base_SOM_Objects.som_geom.geom_examples.SOM_GeomMapNode;
import base_SOM_Objects.som_geom.geom_examples.SOM_GeomObj;
import base_SOM_Objects.som_geom.geom_utils.geom_objs.SOM_GeomObjDrawType;
import base_SOM_Objects.som_geom.geom_utils.geom_objs.SOM_GeomObjTypes;
import base_SOM_Objects.som_geom.geom_utils.geom_objs.SOM_GeomSamplePointf;
import base_UI_Objects.my_procApplet;
import processing.core.PConstants;

public class Geom_3DLineSOMExample extends SOM_GeomObj {
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
	 * # of source points used to build object
	 */
	public static final int _numSrcPts = 2;

	/**
	 * direction vector for this line
	 */
	public myVectorf dir;
	/**
	 * normal to line
	 */
	public myVectorf norm, biNorm;
	/**
	 * point closest to 0,0 for this line
	 */
	public myPointf origin;
	/**
	 * display points for this line to draw maximally based on world bounds
	 */
	public myPointf[] dispEndPts;
	
	/**
	 * coordinate bounds in world for line - static across all line objects
	 * 		first idx : 0 is min; 1 is diff
	 * 		2nd idx : 0 is x, 1 is y, 2 is z
	 */
	protected static float[][] worldBounds=null;
	
	/**
	 * an object to restrict the bounds on this line - min,max, diff s,t value within which to sample plane
	 */
	private float[][] worldTBounds;
	
	private String[] dispAra;
	
	/**
	 * Constructor for line object
	 * @param _mapMgr owning som map manager
 	 * @param _a, _b : 2 points on line
 	 * @param _numSmplPts : # of points to build
	 * @param _locClrAra color based on location
	 * @param _worldBounds 2d array of bounds for where reasonable points should be generated
	 * 		first idx 	: 0 is min; 1 is diff
	 * 		2nd idx 	: 0 is x, 1 is y
	 */
	public Geom_3DLineSOMExample(Geom_3DLineMapMgr _mapMgr, SOM_ExDataType _exType, String _id, SOM_GeomSamplePointf[] _srcSmpls, int _numSmplPts, boolean _shouldBuildSamples) {
		super(_mapMgr, _exType, _id, _srcSmpls, SOM_GeomObjTypes.line_3D, true, _shouldBuildSamples);
		build3DDirOriginAndDispPts("");		
		super.buildLocClrInitObjAndSamples(origin, _numSmplPts);
			
		boundOriginWithinLine();
	}//ctor		
	
	public Geom_3DLineSOMExample(Geom_3DLineMapMgr _mapMgr, SOM_ExDataType _exType, String _oid, String _csvDat) {
		super(_mapMgr, _exType, _oid, _csvDat, SOM_GeomObjTypes.line_3D, _numSrcPts, true);
		build3DDirOriginAndDispPts("");		
		super.buildLocClrAndSamplesFromCSVStr(origin, _csvDat);
	}//csv ctor
	
	/**
	 * ctor to build object corresponding to bmu geometric object
	 * @param _mapMgr
	 * @param _mapNode
	 */
	public Geom_3DLineSOMExample(Geom_3DLineMapMgr _mapMgr, SOM_GeomMapNode _mapNode) {
		super(_mapMgr, _mapNode, SOM_GeomObjTypes.line_3D, true);
		build3DDirOriginAndDispPts("BMU");
		super.buildLocClrInitObjAndSamples(origin, _numSrcPts);
		
		boundOriginWithinLine();
	}//bmu ctor
	
	/**
	 * build object-specific values for this object
	 * @param endPtPrefix whether the display string should include "bmu" or not
	 */
	private void build3DDirOriginAndDispPts(String endPtPrefix) {
		
		dir=_buildDir();	
		if (1.0f != (dir._dot(myVectorf.FORWARD))) {norm = dir._cross(myVectorf.FORWARD)._normalize();} 
		else {		norm = dir._cross(myVectorf.UP)._normalize();}
		biNorm = norm._cross(dir)._normalize();
		
		//origin is closest point to 0,0 on line
		origin = findClosestPointOnLine(myPointf.ZEROPT);		
		
		//build bounds on s and t, if appropriate - by here equations define objects should be built
		worldTBounds = calcTBounds();
		dispEndPts = new myPointf[2];
		dispAra = new String[2];
		dispEndPts[0] = getPointOnLine(worldTBounds[0][0]);
		dispEndPts[1] = getPointOnLine(worldTBounds[0][0] + worldTBounds[1][0]);
		
		dispAra[0] = endPtPrefix + "End pt 0 w/min t : " + worldTBounds[0][0] + " | "+dispEndPts[0].toStrBrf();
		dispAra[1] = endPtPrefix + "End pt 1 w/max t : " + (worldTBounds[0][0]+worldTBounds[1][0])+ " | "+dispEndPts[1].toStrBrf();			
	}
	
	/**
	 * make sure that point chosen as closest to origin is bounded within the allowable bounds of the line - if not use one of the extremal points
	 */
	private void boundOriginWithinLine() {
		float low_t = worldTBounds[0][0], hi_t = worldTBounds[0][0] + worldTBounds[1][0];
		float ctr_t = this.getTForPointOnLine(origin);
		if(ctr_t < low_t) {			origin.set(dispEndPts[0]);}
		else if (ctr_t > hi_t) {	origin.set(dispEndPts[1]);}
	
	}
	
	private myVectorf _buildDir() {
		myVectorf _dir = new myVectorf(getSrcPts()[0],getSrcPts()[1]);	
		_dir._normalize();		
		if((_dir.y < 0.0) || 
		((_dir.y == 0.0) && ((_dir.x < 0) || ((_dir.x == 0) && (_dir.z < 0))))) {	_dir._mult(-1.0);	}		//force all dir vectors to have the same "polarity" so 2 points always form the same line
		return _dir;
	}
	
	
	@SuppressWarnings("static-access")
	public Geom_3DLineSOMExample(Geom_3DLineSOMExample _otr) {
		super(_otr);
		dir = _otr.dir;
		origin = _otr.origin;
		dispEndPts = _otr.dispEndPts;
		worldBounds = _otr.worldBounds;
	}//copy ctor
	

	/**
	 * call from ctor of base class, but set statically for each instancing class type
	 * @param _worldBounds
	 */
	protected final void setWorldBounds(float[][]_worldBounds) {
		if(null!=worldBounds) {return;}
		worldBounds = new float[_worldBounds.length][];
		for(int i=0;i<worldBounds.length;++i) {
			float[] tmp = new float[_worldBounds[i].length];
			for(int j=0;j<tmp.length;++j) {	tmp[j]=_worldBounds[i][j];}
			worldBounds[i]=tmp;
		}
	}//setWorldBounds

	/**
	 * convert a world location within the bounded cube region to be a 4-int color array
	 */
	public final int[] getClrFromWorldLoc(myPointf pt){return getClrFromWorldLoc_3D(pt, worldBounds);}//getClrFromWorldLoc

	
	/**
	 * calculate the bounds on s and t (if appropriate) for parametric formulation of object equation
	 * worldBounds is 
	 * 		first idx 	: 0 is min; 1 is diff
	 * 		2nd idx 	: 0 is x, 1 is y
	 * result is
	 * 		first idx 	: 0==min, 1==diff
	 * 		2nd idx 	: 0==t (only 1 value)
	 * @return result array
	 */
	protected final float[][] calcTBounds(){
		float[] ptA_ara = getSrcPts()[0].asArray(), dirAra = dir.asArray();
		//eq  pt = pta + t * dir -> t = (pt-pta)/dir for each dof
		//mins has location for each dof
		float[] mins = worldBounds[0];		
		float[][] res = new float[2][mins.length-1];
		for(int i=0;i<mins.length-1;++i) {
			res[0][i]=100000000.0f;			
		}
		//for every bound, set t value in bound's ortho dim
		//float[] tAtBounds = new float[worldBounds[0].length * worldBounds.length];
		TreeMap<Float, Integer> tmpBnds = new TreeMap<Float, Integer>();
		float curMax;
	
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
		
		float a = tmpBnds.firstKey(), b = tmpBnds.lastKey();
		if(a < b) {
			res[0][0] = a;
			res[1][0] = b-a;			
		} else {			
			res[0][0] = b;
			res[1][0] = a-b;
		}	
		return res;
	}//calcTBounds()

	public final myPointf findClosestPointOnLine(myPointf p) {
		//find projection t of vector ap (from a to p) on dir, then find a + t * dir
		myVectorf proj = new myVectorf(getSrcPts()[0],p);
		return myVectorf._add(getSrcPts()[0], proj._dot(dir), dir);
	}
	/**
	 * return a point on the line 
	 * @param t
	 * @return
	 */
	public final myPointf getPointOnLine(float t) {return myVectorf._add(getSrcPts()[0], t, dir);}
	
	/**
	 * find t value for point on line - expects point to be on line!
	 * @param pt
	 * @return
	 */
	public final float getTForPointOnLine(myPointf pt) {
		//pt  == origin  + t * dir
		// -> t = (pt.x - origin.x)/dir.x 
		float t = 0.0f;
		if(Math.abs(dir.x)> MyMathUtils.eps) {			t = (pt.x - getSrcPts()[0].x)/dir.x;} 
		else if(Math.abs(dir.y)> MyMathUtils.eps) {		t = (pt.y - getSrcPts()[0].y)/dir.y;}	
		else {											t = (pt.z - getSrcPts()[0].z)/dir.z;}	//line along z axis				
		return t;
	}
	
	/**
	 * return a random point on this object
	 */
	@Override
	public final myPointf getRandPointOnObj() {
		float t = ((float) ThreadLocalRandom.current().nextFloat() *worldTBounds[1][0])+worldTBounds[0][0];
		return getPointOnLine(t);
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
	/**
	 * Instance-class specific required info for this example to build feature data - use this so we don't have to reload and rebuilt from data every time
	 * @return
	 */
	@Override
	protected final String getPreProcDescrForCSV_Indiv() {return "";}	
	/**
	 * Instance-class specific column names of rawDescrForCSV data
	 * @return
	 */
	@Override
	protected String getRawDescColNamesForCSV_Indiv() {	return "";}	

	@Override
	public final TreeMap<Integer, Integer> getTrainingLabels() {
		TreeMap<Integer, Integer> res = new TreeMap<Integer, Integer>();
		return res;
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
	public void drawMyLabel(IRenderInterface pa, SOM_AnimWorldWin animWin) {
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

		float da = MyMathUtils.twoPi_f/36;
		pa.pushMatState();	
		((my_procApplet)pa).beginShape(PConstants.QUAD_STRIP);
			for(float a=0; a<=MyMathUtils.twoPi_f+da; a+=da) {
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
