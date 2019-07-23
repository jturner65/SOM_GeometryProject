package SOM_GeometryProj_PKG.geom_ObjExamples;

import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers.Geom_LineMapMgr;
import SOM_GeometryProj_PKG.som_geom.SOM_GeomMapManager;
import SOM_GeometryProj_PKG.som_geom.geom_UI.SOM_AnimWorldWin;
import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomMapNode;
import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomObj;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomObjDrawType;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomObjTypes;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomSamplePointf;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomSmplDataForEx;
import base_SOM_Objects.som_examples.SOM_ExDataType;
import base_SOM_Objects.som_examples.SOM_FtrDataType;
import base_UI_Objects.my_procApplet;
import base_Utils_Objects.MyMathUtils;
import base_Utils_Objects.vectorObjs.myPointf;
import base_Utils_Objects.vectorObjs.myVectorf;

public class Geom_LineSOMExample extends SOM_GeomObj {
	private static int IDGen = 0;
	
	/**
	 * feature vector size for this object 2d point  + 2d line
	 */
	public static final int _numFtrs = 4;

	/**
	 * direction vector for this line
	 */
	public final myVectorf dir;
	/**
	 * point closest to 0,0 for this line
	 */
	public final myPointf origin;
	/**
	 * display points for this line to draw maximally based on world bounds
	 */
	public final myPointf[] dispEndPts;
	
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
	public Geom_LineSOMExample(Geom_LineMapMgr _mapMgr, SOM_ExDataType _exType, String _id, SOM_GeomSmplDataForEx[] _srcSmpls, int _numSmplPts) {
		super(_mapMgr, _exType, _id, _srcSmpls, SOM_GeomObjTypes.line);
		getSrcPts()[0].z = 0.0f;
		getSrcPts()[1].z = 0.0f;
		//z is always 0 - making this in 2 d
		dir=new myVectorf(getSrcPts()[0],getSrcPts()[1]);
		dir.z = 0;
		dir._normalize();	
		//origin is closest point to 0,0 on line
		origin = findClosestPointOnLine(myPointf.ZEROPT);
		origin.z = 0;
		
		//build bounds on s and t, if appropriate - by here equations define objects should be built
		worldTBounds = calcTBounds();
		super.buildLocClrInitObjAndSamples(getSrcPts()[0], _numSmplPts);
		dispEndPts = new myPointf[2];
		dispAra = new String[2];
		float low_t = worldTBounds[0][0], hi_t = worldTBounds[0][0] + worldTBounds[1][0];
		dispEndPts[0] = getPointOnLine(worldTBounds[0][0]);
		dispEndPts[1] = getPointOnLine(worldTBounds[0][0] + worldTBounds[1][0]);
		
		float ctr_t = this.getTForPointOnLine(origin);
		if(ctr_t < low_t) {			origin.set(dispEndPts[0]);}
		else if (ctr_t > hi_t) {	origin.set(dispEndPts[1]);}
		
		dispAra[0] = "End pt 0 w/min t : " + worldTBounds[0][0] + " | "+dispEndPts[0].toStrBrf();
		dispAra[1] = "End pt 1 w/max t : " + (worldTBounds[0][0]+worldTBounds[1][0])+ " | "+dispEndPts[1].toStrBrf();

 
	}//ctor		
	
	public Geom_LineSOMExample(Geom_LineMapMgr _mapMgr, SOM_ExDataType _exType, String _oid, String _csvDat) {
		super(_mapMgr, _exType, _oid, _csvDat, SOM_GeomObjTypes.line);
		getSrcPts()[0].z = 0.0f;
		getSrcPts()[1].z = 0.0f;
		//z is always 0 - making this in 2 d
		dir=new myVectorf(getSrcPts()[0],getSrcPts()[1]);
		dir.z = 0;
		dir._normalize();	
		//origin is closest point to 0,0 on line
		origin = findClosestPointOnLine(myPointf.ZEROPT);
		origin.z = 0;
		
		//build bounds on s and t, if appropriate - by here equations define objects should be built
		worldTBounds = calcTBounds();
		super.buildLocClrAndSamplesFromCSVStr(getSrcPts()[0], _csvDat);
		
		dispEndPts = new myPointf[2];
		dispAra = new String[2];
		dispEndPts[0] = getPointOnLine(worldTBounds[0][0]);
		dispEndPts[1] = getPointOnLine(worldTBounds[0][0] + worldTBounds[1][0]);
		
		dispAra[0] = "End pt 0 w/min t : " + worldTBounds[0][0] + " | "+dispEndPts[0].toStrBrf();
		dispAra[1] = "End pt 1 w/max t : " + (worldTBounds[0][0]+worldTBounds[1][0])+ " | "+dispEndPts[1].toStrBrf();
		
	}
	
	/**
	 * ctor to build object corresponding to bmu geometric object
	 * @param _mapMgr
	 * @param _mapNode
	 */
	public Geom_LineSOMExample(SOM_GeomMapManager _mapMgr, SOM_GeomMapNode _mapNode) {
		super(_mapMgr, _mapNode, SOM_GeomObjTypes.line);
		getSrcPts()[0].z = 0.0f;
		getSrcPts()[1].z = 0.0f;
		//z is always 0 - making this in 2 d
		dir=new myVectorf(getSrcPts()[0],getSrcPts()[1]);
		dir.z = 0;
		dir._normalize();	
		//origin is closest point to 0,0 on line
		origin = findClosestPointOnLine(myPointf.ZEROPT);
		origin.z = 0;
		
		//build bounds on s and t, if appropriate - by here equations define objects should be built
		worldTBounds = calcTBounds();
		super.buildLocClrInitObjAndSamples(getSrcPts()[0], SOM_GeomObjTypes.line.getVal());
		dispEndPts = new myPointf[2];
		dispAra = new String[2];
		float low_t = worldTBounds[0][0], hi_t = worldTBounds[0][0] + worldTBounds[1][0];
		dispEndPts[0] = getPointOnLine(worldTBounds[0][0]);
		dispEndPts[1] = getPointOnLine(worldTBounds[0][0] + worldTBounds[1][0]);
		
		float ctr_t = this.getTForPointOnLine(origin);
		if(ctr_t < low_t) {			origin.set(dispEndPts[0]);}
		else if (ctr_t > hi_t) {	origin.set(dispEndPts[1]);}
		
		dispAra[0] = "BMU_End pt 0 w/min t : " + worldTBounds[0][0] + " | "+dispEndPts[0].toStrBrf();
		dispAra[1] = "BMU_End pt 1 w/max t : " + (worldTBounds[0][0]+worldTBounds[1][0])+ " | "+dispEndPts[1].toStrBrf();
			
	}
	
	
	public Geom_LineSOMExample(Geom_LineSOMExample _otr) {
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
	public final int[] getClrFromWorldLoc(myPointf pt){return getClrFromWorldLoc_2D(pt,worldBounds);}//getClrFromWorldLoc

	
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
		//want 2 points closest to 0 - 2 will be negative, and 2 will be positive if a is within bounds
		tmpBnds.remove(tmpBnds.firstKey());
		tmpBnds.remove(tmpBnds.lastKey());
		float a = tmpBnds.firstKey();
		float b = tmpBnds.lastKey();
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
		if(Math.abs(dir.x)> MyMathUtils.eps) {	t = (pt.x - getSrcPts()[0].x)/dir.x;} 
		else {									t = (pt.y - getSrcPts()[0].y)/dir.y;}//vertical line			
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
	protected final SOM_GeomSamplePointf[] buildSrcPtsFromBMUMapNodeFtrs(float[] mapFtrs, int _numSrcSmpls, String _dispLabel) {
		SOM_GeomSamplePointf[] res = new SOM_GeomSamplePointf[_numSrcSmpls];
		myVectorf tmpNorm = new myVectorf(mapFtrs[0],mapFtrs[1],0.0f)._normalize();//should be normalized, but in case it isn't
		myPointf ctrPt = new myPointf(mapFtrs[2],mapFtrs[3],0.0f);
		res[0]= new SOM_GeomSamplePointf(myVectorf._add(ctrPt, 11.0f,tmpNorm), _dispLabel+"_BMU_pt_0");
		res[1]= new SOM_GeomSamplePointf(myVectorf._add(ctrPt, -12.0f,tmpNorm), _dispLabel+"_BMU_pt_1");
		return res;			
	}//buildSrcPtsFromBMUMapNode
	
	
	/**
	 * object shape-specific feature building - ftrVecMag calced in base class
	 */
	@Override
	protected final void buildFeaturesMap_Indiv() {
		//build feature-based example for this example - for lines example should be closest origin point + normalized dir vector
		//ONLY USE x,y VALS
		ftrMaps[ftrMapTypeKey].put(0,dir.x);
		ftrMaps[ftrMapTypeKey].put(1,dir.y);
		
		ftrMaps[ftrMapTypeKey].put(2,origin.x);
		ftrMaps[ftrMapTypeKey].put(3,origin.y);
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
		// TODO Auto-generated method stub
		return res;
	}	
	
	private final void showMyPoint(my_procApplet pa, myPointf P, float r) {
		pa.pushMatrix(); pa.pushStyle(); 
		pa.translate(P.x,P.y,0); 
		pa.circle(0,0,r,r);						
		pa.popStyle(); pa.popMatrix();
	} // render sphere of radius r and center P)	

	private final void showMyPoint(my_procApplet pa, myPointf P, float r, String s, float _off) {
		pa.pushMatrix(); pa.pushStyle(); 
		pa.translate(P.x,P.y,0); 
		pa.circle(0,0,r,r);				
		pa.text(s, _off, _off);
		pa.popStyle(); pa.popMatrix();
	} // render sphere of radius r and center P)	

	///////////////////////////
	// draw functionality
	@Override
	protected final void _drawMe_Geom(my_procApplet pa, SOM_GeomObjDrawType drawType) {
		pa.pushMatrix();pa.pushStyle();	
		pa.strokeWeight(2.0f);
		pa.line(dispEndPts[0],dispEndPts[1]);
		showMyPoint(pa,dispEndPts[0], 5.0f);
		showMyPoint(pa,dispEndPts[1], 5.0f);
		showMyPoint(pa,getSrcPts()[0], 5.0f);
		showMyPoint(pa,getSrcPts()[1], 5.0f);
		showMyPoint(pa,origin, 5.0f);
		pa.popStyle();pa.popMatrix();
	}

	/**
	 * TODO we need to re-calculate drawing quantities based on bmu construction
	 */
	@Override
	protected final void _drawMe_Geom_BMU(my_procApplet pa, SOM_GeomObjDrawType drawType) {
//		pa.strokeWeight(2.0f);
//		pa.line(dispEndPts[0],dispEndPts[1]);
//		pa.show(dispEndPts[0], 5.0f, "", myVectorf.ZEROVEC, -1, true);
//		pa.show(dispEndPts[1], 5.0f, "", myVectorf.ZEROVEC, -1, true);
//		pa.show(getSrcPts()[0], 5.0f, "", myVectorf.ZEROVEC, -1, true);
//		pa.show(getSrcPts()[1], 5.0f, "", myVectorf.ZEROVEC, -1, true);
//		pa.show(origin, 5.0f, "", myVectorf.ZEROVEC, -1, true);
	}

	@Override
	public void drawMyLabel(my_procApplet pa, SOM_AnimWorldWin animWin) {
		pa.pushMatrix();pa.pushStyle();		
		pa.setFill(labelClrAra,255);
		pa.setStroke(labelClrAra,255);
		pa.strokeWeight(2.0f);
		//(myPointf P, float r, String s, myVectorf D, int clr, boolean flat)
		showMyPoint(pa,dispEndPts[0], 5.0f, dispLabel+dispAra[0], 0.0f);
		showMyPoint(pa,dispEndPts[1], 5.0f, dispLabel+dispAra[1], 0.0f);
		showMyPoint(pa,getSrcPts()[0], 5.0f, "pt a :"+getSrcPts()[0].toStrBrf(), 0.0f);
		showMyPoint(pa,getSrcPts()[1], 5.0f, "pt b :"+getSrcPts()[1].toStrBrf(), 0.0f);
		showMyPoint(pa,origin, 5.0f, dispLabel+"| Origin " + origin.toStrBrf() + " | Dir : " + dir.toStrBrf() +" | " +dispAra[0]+"->"+dispAra[1], 0.0f);
		pa.popStyle();pa.popMatrix();	
	}
	
	/**
	 * draw this object's samples, using the random color
	 * @param pa
	 */
	@Override
	public final void drawMySmplsLabel(my_procApplet pa, SOM_AnimWorldWin animWin){	objSamples.drawMySmplsLabel_2D(pa);}//

	
	protected static float modCnt = 0;//counter that will determine when the color should switch
	@Override
	protected final void _drawMeSelected(my_procApplet pa, float animTmMod) {
		modCnt += animTmMod;
		if(modCnt > 1.0){	modCnt = 0.0f;	}//blink every ~second
		
	}//_drawMeSelected

	@Override
	public void drawMyLabel_BMU(my_procApplet pa) {
		// TODO Auto-generated method stub

	}

	@Override
	protected final void _drawMeSelected_BMU(my_procApplet pa, float animTmMod) {
		// TODO Auto-generated method stub

	}


	/**
	 * initialize object's ID
	 */
	protected final int incrID() {return IDGen++;}



}//class SOM_Line
