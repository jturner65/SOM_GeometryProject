package SOM_GeometryProj_PKG.geom_ObjExamples;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers.Geom_PlaneMapMgr;
import base_SOM_Objects.som_examples.SOM_ExDataType;
import base_SOM_Objects.som_geom.geom_UI.SOM_AnimWorldWin;
import base_SOM_Objects.som_geom.geom_examples.SOM_GeomMapNode;
import base_SOM_Objects.som_geom.geom_examples.SOM_GeomObj;
import base_SOM_Objects.som_geom.geom_utils.geom_objs.SOM_GeomObjDrawType;
import base_SOM_Objects.som_geom.geom_utils.geom_objs.SOM_GeomObjTypes;
import base_SOM_Objects.som_geom.geom_utils.geom_objs.SOM_GeomSamplePointf;
import base_UI_Objects.my_procApplet;
import base_UI_Objects.windowUI.base.myDispWindow;
import base_Utils_Objects.vectorObjs.myPointf;
import base_Utils_Objects.vectorObjs.myVectorf;
import processing.core.PConstants;
import processing.core.PShape;

public class Geom_PlaneSOMExample extends SOM_GeomObj{ 
	private static int IDGen = 0;
	
	/**
	 * string array denoting names of features
	 */
	public static final String[] ftrNames = {"Norm x","Norm y","Norm z", "Origin x","Origin y","Origin z"};
	/**
	 * feature vector size for this object : 3d point + 3d line
	 */
	public static final int _numFtrs = ftrNames.length;
	
	/**
	 * # of source points used to build object
	 */
	public static final int _numSrcPts = 3;
	
	/**
	 * unique point on this plane closest to origin
	 */
	protected myPointf planeOrigin;
	/**
	 * equation of plane going through planeOrigin with planeNorm
	 * of the form eq[0]*x + eq[1]*y + eq[2]*z + eq[3] = 0
	 */
	protected float[] eq;
	
	/**
	 * basis vectors for plane - should be orthogonal and normalized - idx 0 is normal
	 */
	protected myVectorf[] basisVecs;

	/**
	 * display points for this plane to draw maximally based on world bounds
	 */
	protected myPointf[] dispBoundPts;
	/**
	 * this array holds the minimum and maximum values for x,y and z for this plane
	 */
	protected float[][] minMaxDiffValAra;
	/**
	 * use this with origin point to display
	 */
	protected myPointf[] orthoFrame;
	/**
	 * coordinate bounds in world for plane - static across all plane objects
	 * 		first idx : 0 is min; 1 is diff
	 * 		2nd idx : 0 is x, 1 is y, 2 is z
	 */
	protected static float[][] worldBounds=null;
	
	protected final static float frameLen = 100.0f;
	/**
	 * array, idx 0 is rand color, idx 1 is loc color, idx 2 is no fill rnd, idx 3 is no fill loc clr, idx 4 is selected
	 */
	protected PShape[] planeObjs;	
	
	
//	/**
//	 * angle between normal and "up" - use this for display
//	 */
//	protected final float rotAngle;
//	
//	/**
//	 * normalized axis between normal and "up" - use this to rotate for display
//	 */
//	protected final myVectorf rotAxis;
	
	/**
	 * Constructor for plane object
	 * @param _mapMgr owning som map manager
	 * @param _ptsOnPlane : non-colinear points on plane
	 * @param _numSmplPts : # of sample points to build for this object
	 * @param _worldBnds 4 points that bound the plane for display purposes
	 */	
	public Geom_PlaneSOMExample(Geom_PlaneMapMgr _mapMgr, SOM_ExDataType _exType, String _id, SOM_GeomSamplePointf[] _srcSmpls, int _numSmplPts, boolean _shouldBuildSamples) {
		super(_mapMgr,  _exType, _id, _srcSmpls, SOM_GeomObjTypes.plane, true, _shouldBuildSamples);	
		buildBasisOriginAndEq();
		super.buildLocClrInitObjAndSamples(buildLocForColor(planeOrigin, basisVecs[0]), _numSmplPts);
		if(_shouldBuildSamples) {
			buildPlanePShapes();
		} else {
			planeObjs = new PShape[0];
		}
	}//ctor
	
	/**
	 * ctor from csv - only srcpts needs to be built from csv
	 * @param _mapMgr
	 * @param _animWin
	 * @param _exType
	 * @param _oid
	 * @param _csvDat
	 * @param _worldBounds
	 */
	public Geom_PlaneSOMExample(Geom_PlaneMapMgr _mapMgr, SOM_ExDataType _exType, String _oid, String _csvDat) {
		super(_mapMgr, _exType, _oid, _csvDat,  SOM_GeomObjTypes.plane, _numSrcPts, true);
		buildBasisOriginAndEq();
		//build new point location for color, squaring the distance from the origin to provide more diversity
		super.buildLocClrAndSamplesFromCSVStr(buildLocForColor(planeOrigin, basisVecs[0]), _csvDat);
		buildPlanePShapes();
	}
	
	/**
	 * ctor to build object corresponding to bmu's geometric object
	 * @param _mapMgr
	 * @param _mapNode
	 */
	public Geom_PlaneSOMExample(Geom_PlaneMapMgr _mapMgr, SOM_GeomMapNode _mapNode) {
		super(_mapMgr, _mapNode, SOM_GeomObjTypes.plane, true);
		buildBasisOriginAndEq();	
		super.buildLocClrInitObjAndSamples(buildLocForColor(planeOrigin, basisVecs[0]), _numSrcPts);
		if(dispBoundPts.length > 0) {	buildPlanePShapes();	} 
		else {
			msgObj.dispErrorMessage("Geom_PlaneSOMExample", "BMU Geom Obj Ctor", "Construction Failed due to src points from " + _mapNode.OID+ " having unacceptable format :" + this.geomSrcSamples.length + " pts :");
			for(int i=0;i<this.geomSrcSamples.length;++i) {
				msgObj.dispErrorMessage("Geom_PlaneSOMExample", "BMU Geom Obj Ctor", "\t"+geomSrcSamples[i].toString());
			}
			
		}
	}//ctor from bmu
	
	/**
	 * all planar defining components - called from constructors
	 */
	private void buildBasisOriginAndEq() {
		//plane norm
		myVectorf tmpNorm = myVectorf._cross(new myVectorf(getSrcPts()[0], getSrcPts()[1]), new myVectorf(getSrcPts()[0], getSrcPts()[2]))._normalize();
		eq = getPlanarEqFromPointAndNorm(tmpNorm, getSrcPts()[0]);
		//works because plane is built with unit normal in equation
		planeOrigin = new myPointf(-eq[0]*eq[3],-eq[1]*eq[3],-eq[2]*eq[3]);
		myVectorf tmpOriginVec = new myVectorf(planeOrigin);
		if(tmpOriginVec._dot(tmpNorm) < 0) {tmpNorm._mult(-1.0f);	}		//make normal point away from absolute origin
		
		//build basis vectors
		basisVecs = buildBasisVecs(tmpNorm);
		
		dispBoundPts = calc_plane_WBBox_IntersectPoints();
		minMaxDiffValAra = new float[][] {{100000,100000,100000},{-100000,-100000,-100000},{0,0,0}};
		for(int i=0;i<dispBoundPts.length;++i) {
			float[] ptAra = dispBoundPts[i].asArray();
			for(int j=0;j<ptAra.length;++j) {
				minMaxDiffValAra[0][j] = (minMaxDiffValAra[0][j] > ptAra[j] ? ptAra[j] : minMaxDiffValAra[0][j]);
				minMaxDiffValAra[1][j] = (minMaxDiffValAra[1][j] < ptAra[j] ? ptAra[j] : minMaxDiffValAra[1][j]);				
			}
		}
		for(int j=0;j<minMaxDiffValAra[2].length;++j) {minMaxDiffValAra[2][j] = minMaxDiffValAra[1][j] -minMaxDiffValAra[0][j];	}

		orthoFrame = new myPointf[3];
		
		for(int i=0;i<orthoFrame.length;++i) {
			orthoFrame[i]= myPointf._add(planeOrigin, frameLen, basisVecs[i]);
		}
	}//buildBasisOriginAndEq
	

	@SuppressWarnings("static-access")
	public Geom_PlaneSOMExample(Geom_PlaneSOMExample _otr) {
		super(_otr);	
		planeOrigin = _otr.planeOrigin;
		eq = _otr.eq;
		basisVecs = _otr.basisVecs;
//		rotAngle = _otr.rotAngle;
//		rotAxis = _otr.rotAxis;
		dispBoundPts = _otr.dispBoundPts;
		minMaxDiffValAra = _otr.minMaxDiffValAra;
		orthoFrame = _otr.orthoFrame;
		worldBounds = _otr.worldBounds;
		planeObjs = _otr.planeObjs;		
	}
	
	
	/**
	 * build rotational quantities for this plane - how to rotate a horizontal plane to match the orientation of this plane
	 * using axis-angle
	 */
//	private void buildRotVecs() {
//		//axis and angle to rotate simple plane for display to coincide with orienation of this plane's normal
//		rotAxis = myVectorf.UP._cross(basisVecs[0]);
//		rotAxis._normalize();		
//		rotAngle = (float) Math.acos(basisVecs[0]._dot(myVectorf.UP));
//	}
	
	
	/**
	 * build pshapes representing planes for different colors, to speed up rendering
	 */
	private void buildPlanePShapes() {
		//create representation
		planeObjs = new PShape[SOM_GeomObjDrawType.getNumVals()];
		planeObjs[SOM_GeomObjDrawType.rndClr.getVal()] = buildPlaneShape(true, rndClrAra);
		planeObjs[SOM_GeomObjDrawType.locClr.getVal()] = buildPlaneShape(true, locClrAra);
		planeObjs[SOM_GeomObjDrawType.noFillRndClr.getVal()] = buildPlaneShape(false, rndClrAra);
		planeObjs[SOM_GeomObjDrawType.noFillLocClr.getVal()] = buildPlaneShape(false, locClrAra);
		planeObjs[SOM_GeomObjDrawType.selected.getVal()] = buildSelectedPoly();

	}//buildPlanePShapes

	
	private PShape buildPlaneShape(boolean hasFill, int[] clr) {
		PShape poly = myDispWindow.pa.createShape(); 
		//all have lines to center
		poly.beginShape(PConstants.TRIANGLE_FAN);
		if(hasFill) {
			poly.fill(clr[0],clr[1],clr[2],255);
		} else {
			poly.noFill();		
		}
		poly.stroke(clr[0],clr[1],clr[2],255);
		poly.strokeWeight(2.0f);
		poly.normal(basisVecs[0].x, basisVecs[0].y, basisVecs[0].z); 
		poly.vertex(planeOrigin.x,planeOrigin.y,planeOrigin.z);
		for(int i=0;i<dispBoundPts.length;++i){poly.vertex(dispBoundPts[i].x,dispBoundPts[i].y,dispBoundPts[i].z);} 
		poly.vertex(dispBoundPts[0].x,dispBoundPts[0].y,dispBoundPts[0].z);
		poly.endShape(PConstants.CLOSE);
		return poly;
	}
	
	/**
	 * build polygon shape for selected plane
	 * @return
	 */
	private PShape buildSelectedPoly() {
		PShape poly = myDispWindow.pa.createShape(); 
		poly.beginShape(PConstants.TRIANGLE_FAN);
		poly.noFill();				
		poly.stroke(120,120,120,255);
		poly.strokeWeight(2.0f);
		poly.normal(basisVecs[0].x, basisVecs[0].y, basisVecs[0].z); 
		poly.vertex(planeOrigin.x,planeOrigin.y,planeOrigin.z);
		for(int i=0;i<dispBoundPts.length;++i){poly.vertex(dispBoundPts[i].x,dispBoundPts[i].y,dispBoundPts[i].z);} 
		poly.vertex(dispBoundPts[0].x,dispBoundPts[0].y,dispBoundPts[0].z);
		poly.endShape(PConstants.CLOSE);
		return poly;
	}
		
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
	public final int[] getClrFromWorldLoc(myPointf pt){return getClrFromWorldLoc_3D(pt,worldBounds);}//getClrFromWorldLoc
	
	
	/**
	 * Find intersection of plane with ray
	 * @param RayOrig
	 * @param RayDir
	 * @return
	 */
	private myPointf rayIntersectPlane(myPointf RayOrig, myVectorf RayDir) {		
		Float denomVal = eq[0]* RayDir.x +eq[1]* RayDir.y+ eq[2]* RayDir.z;
	    if (denomVal == 0.0f) {        return null;}
	    Float tVal = - (eq[0]* RayOrig.x +eq[1]* RayOrig.y+ eq[2]* RayOrig.z + eq[3]) / denomVal;
	    if (tVal >= 0.f && tVal <= 1.f) {   	return (myPointf._add(RayOrig,tVal, RayDir));}
	    return null;	
	}

	
	private void _checkEachDir(ArrayList<myPointf> ptsAra, myVectorf dir, myPointf[] origAra) {
		for(int i=0;i<origAra.length;++i) {
		    myPointf p = rayIntersectPlane(origAra[i],dir);
		    if(null!=p) {ptsAra.add(p);}
		}
	}
	
	//find intersection between this object's plane and every edge of world axis aligned bound box
	// Maximum out_point_count == 6, so out_points must point to 6-element array.
	// out_point_count == 0 mean no intersection.
	// out_points are not sorted.
	protected myPointf[] calc_plane_WBBox_IntersectPoints(){
	    ArrayList<myPointf> ptsAra = new ArrayList<myPointf>();
	    // Test edges along X axis
	    myVectorf dir = new myVectorf(worldBounds[1][0], 0.f, 0.f); 
	    
	    myPointf[] origAra = new myPointf[] {
	    		new myPointf(worldBounds[0][0], worldBounds[0][1], 						worldBounds[0][2]),										//min x, min y, min z
	    		new myPointf(worldBounds[0][0], worldBounds[0][1] +worldBounds[1][1], 	worldBounds[0][2]),					//min x, max y, min z
	    		new myPointf(worldBounds[0][0], worldBounds[0][1], 						worldBounds[0][2] +worldBounds[1][2]),					//min x, min y, max z
	    		new myPointf(worldBounds[0][0], worldBounds[0][1] +worldBounds[1][1], 	worldBounds[0][2] +worldBounds[1][2])	//min x, max y, max z
	    };    
	    
	    _checkEachDir(ptsAra, dir, origAra);


	    // Test edges along Y axis
	    dir.set(0.0f, worldBounds[1][1], 0.0f);
	    origAra = new myPointf[] {
	    		new myPointf(worldBounds[0][0], 					worldBounds[0][1], 	worldBounds[0][2]),                                     //min x, min y, min z  
	    		new myPointf(worldBounds[0][0] +worldBounds[1][0], 	worldBounds[0][1], 	worldBounds[0][2]),                  //max x, min y, min z  
	    		new myPointf(worldBounds[0][0], 					worldBounds[0][1], 	worldBounds[0][2] +worldBounds[1][2]),                   //min x, min y, max z  
	    		new myPointf(worldBounds[0][0] +worldBounds[1][1], 	worldBounds[0][1], 	worldBounds[0][2] +worldBounds[1][2]) //max x, min y, max z  
	    };    
	    
	    _checkEachDir(ptsAra, dir, origAra);


	    // Test edges along Z axis
	    dir.set(0.0f, 0.f, worldBounds[1][2]);
	    origAra = new myPointf[] {
	    		new myPointf(worldBounds[0][0], 					worldBounds[0][1], 						worldBounds[0][2]),                                      //min x, min y, min z  
	    		new myPointf(worldBounds[0][0] +worldBounds[1][0],	worldBounds[0][1], 						worldBounds[0][2]),                   //max x, min y, min z  
	    		new myPointf(worldBounds[0][0], 					worldBounds[0][1] +worldBounds[1][1], 	worldBounds[0][2]),                   //min x, max y, min z  
	    		new myPointf(worldBounds[0][0] +worldBounds[1][1], 	worldBounds[0][1] +worldBounds[1][1], 	worldBounds[0][2]) //max x, max y, min z  
	    };    
	    
	    _checkEachDir(ptsAra, dir, origAra);

	    
	    if(ptsAra.size() == 0) {
	    	msgObj.dispErrorMessage("Geom_PlaneSOMExample", "calc_plane_WBBox_IntersectPoints", "ID : " + this.dispLabel + " : Plane doesn't intersect with enclosing box somehow.");
	    	
	    	return new myPointf[0];
	    }//no intersection
	    //sort in cw order around normal
	    TreeMap<Float, myPointf> ptsMap = sortBoundPoints(ptsAra);
//	    System.out.println("Map : ");
//	    for(Float key : ptsMap.keySet()) {
//	    	myPointf pt = ptsMap.get(key);
//	    	System.out.println("key : "+ key + " : " + pt.toStrBrf()  );
//	    }
	    return ptsMap.values().toArray(new myPointf[0]);
	}	
	
	/**
	 * sort points around normal, using first point in list as starting point
	 * @param ptsAra
	 * @return
	 */
	private TreeMap<Float, myPointf> sortBoundPoints(ArrayList<myPointf> ptsAra){
		TreeMap<Float, myPointf> resMap = new TreeMap<Float, myPointf>();
		myVectorf baseVec = new myVectorf(planeOrigin, ptsAra.get(0));
		for(int i=0;i<ptsAra.size();++i) {
			myPointf pt = ptsAra.get(i);
			float res = (myVectorf._angleBetween_Xprod(new myVectorf(planeOrigin, pt),baseVec));
			resMap.put(res, pt);
		}		
		return resMap;
	}//sortBoundPoints	
	
	/**
	 * return the t value for point along given basis - POINT IS ASSUMED TO BE ON PLANE ALREADY
	 * @param pt
	 * @param idx either 0 or 1, for which basis we are checking
	 * @return t value for point along given basis
	 */
	public float findTVal(myPointf pt, int idx) {
		//pt = origin + basis[0] * s + basis[1] * t
		//proj onto basis (dot prod)		
		//BA : vector from A->B, in this case from origin to pt
		myVectorf ptFromOrigin = new myVectorf(planeOrigin, pt);
		//BA.dot(basis(idx)) == distance along basis for projection
		return ptFromOrigin._dot(basisVecs[idx]);
	}
	
	/**
	 * point normal form of plane
	 * @param _n unit normal of plane
	 * @param _p point on plane
	 * @return eq : coefficients of the plane equation in the form eq[0]*x + eq[1]*y + eq[2]*z + eq[3] = 0
	 */
	public float[] getPlanarEqFromPointAndNorm(myVectorf _n, myPointf _p) {return new float[] { _n.x, _n.y, _n.z, _n.x * -_p.x + _n.y * -_p.y + _n.z * -_p.z};}
	
	/**
	 * return a random point on this object(plane)
	 */
	@Override
	public final myPointf getRandPointOnObj() {
		//myPointf _add(myPointf O, float a, myVectorf I, float b, myVectorf J)
		// idx 0 : 0==min, 1==diff
		// idx 1 : 0==s, 1==t, 
		myPointf pt,ptTmp;
		//minMaxDiffValAra : idx 0 : 0 is min, idx 1 is max, idx 2 is diff; idx 1 : 0,1,2 is x,y,z
		do {
			ptTmp = new myPointf(
				(ThreadLocalRandom.current().nextFloat() * minMaxDiffValAra[2][0]) + minMaxDiffValAra[0][0],
				(ThreadLocalRandom.current().nextFloat() * minMaxDiffValAra[2][1]) + minMaxDiffValAra[0][1],
				(ThreadLocalRandom.current().nextFloat() * minMaxDiffValAra[2][2]) + minMaxDiffValAra[0][2]);
			//point in space bounded by limits - now project to plane using origin point
			
			myVectorf OP = new myVectorf(planeOrigin, ptTmp);
			//project onto plane
			myVectorf OPNorm = myVectorf._mult(basisVecs[0], basisVecs[0]._dot(OP));
			myVectorf OPTan = myVectorf._sub(OP, OPNorm);
			pt = new myPointf(planeOrigin,OPTan);
		} while ((minMaxDiffValAra[0][0] > pt.x) || (pt.x > minMaxDiffValAra[1][0]) ||
				(minMaxDiffValAra[0][1] > pt.y) || (pt.y > minMaxDiffValAra[1][1])  ||
				(minMaxDiffValAra[0][2] > pt.z) || (pt.z > minMaxDiffValAra[1][2])	);

		return pt;
	}

	////////////////////////////
	// feature functionality (inherited from SOM_Example
	
	/**
	 * build an array of source points from the characteristic features of the source map node
	 */
	@Override
	protected final SOM_GeomSamplePointf[] buildSrcPtsFromBMUMapNodeFtrs(float[] mapFtrs,  String _dispLabel) {
		SOM_GeomSamplePointf[] res = new SOM_GeomSamplePointf[_numSrcPts];
		myVectorf tmpNorm = new myVectorf(mapFtrs[0],mapFtrs[1],mapFtrs[2])._normalize();//should be normalized, but in case it isn't
		myVectorf[] basisVecs = buildBasisVecs(tmpNorm);
		myPointf originPt = new myPointf(mapFtrs[3],mapFtrs[4],mapFtrs[5]);		
		//float[] eq = getPlanarEqFromPointAndNorm(tmpNorm, originPt);
		res[0]= new SOM_GeomSamplePointf(myPointf._add(originPt, 10.0f, basisVecs[1]), _dispLabel+"_BMU_pt_0", this);
		res[1]= new SOM_GeomSamplePointf(myPointf._add(originPt, 10.0f, basisVecs[2]), _dispLabel+"_BMU_pt_1", this);
		res[2]= new SOM_GeomSamplePointf(myPointf._add(originPt, 10.0f, basisVecs[1], 10.0f, basisVecs[2]) , _dispLabel+"_BMU_pt_2", this);
		
		return res;			
	}//buildSrcPtsFromBMUMapNode
	

	
	/**
	 * object shape-specific feature building - ftrVecMag calced in base class
	 */
	@Override
	protected final void buildFeaturesMap_Indiv() {
		//describe plane as normal and planar origin
		ftrMaps[rawftrMapTypeKey].put(0,basisVecs[0].x);
		ftrMaps[rawftrMapTypeKey].put(1,basisVecs[0].y);
		ftrMaps[rawftrMapTypeKey].put(2,basisVecs[0].z);

		ftrMaps[rawftrMapTypeKey].put(3,planeOrigin.x);
		ftrMaps[rawftrMapTypeKey].put(4,planeOrigin.y);
		ftrMaps[rawftrMapTypeKey].put(5,planeOrigin.z);

	}//buildFeaturesMap_Indiv
	
	/**
	 * Instance-class specific required info for this example to build feature data - use this so we don't have to reload and rebuilt from data every time
	 * @return
	 */
	@Override
	protected final String getPreProcDescrForCSV_Indiv() {
		// TODO Auto-generated method stub
		return "";
	}
	
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
	/**
	 * get an appropriate sample location to build sample sets, based on what kind of object is being built
	 * @return
	 */
//_drawLabelAtLoc_3D(my_procApplet pa, myPointf pt, SOM_AnimWorldWin animWin, String label, float _off)
	@Override
	public void drawMyLabel(my_procApplet pa, SOM_AnimWorldWin animWin) {
		pa.pushMatrix();pa.pushStyle();	
		pa.setFill(this.labelClrAra, 255);
		pa.setStroke(this.labelClrAra, 255);
		pa.strokeWeight(2.0f);
		_drawLabelAtLoc_3D(pa, planeOrigin, animWin, dispLabel + " Origin : " + planeOrigin.toStrBrf() +" | Normal " + basisVecs[0].toStrBrf(), 1.5f, 2.5f);
		for(int i=0;i<dispBoundPts.length;++i){
			pa.line(planeOrigin, dispBoundPts[i]);
			_drawLabelAtLoc_3D(pa, dispBoundPts[i], animWin, dispLabel + " Bound " + i+" : " + dispBoundPts[i].toStrBrf(), 1.25f,  2.0f);
		}
		
		pa.popStyle();pa.popMatrix();
	}//drawMyLabel

	@Override
	protected void _drawMe_Geom(my_procApplet pa, SOM_GeomObjDrawType drawType) {
		if(planeObjs.length==0) {return;}
		pa.pushMatrix();pa.pushStyle();			
		pa.strokeWeight(2.0f);

		pa.shape(planeObjs[drawType.getVal()]);		
		pa.popStyle();pa.popMatrix();
	}

	public void drawOrthoFrame(my_procApplet pa) {
		pa.pushMatrix();pa.pushStyle();	
		pa.strokeWeight(3.0f);
		pa.stroke(255,0,0,255);
		pa.line(planeOrigin, orthoFrame[0]);
		pa.stroke(0,255,0,255);
		pa.strokeWeight(3.0f);
		pa.line(planeOrigin, orthoFrame[1]);
		pa.stroke(0,0,255,255);
		pa.strokeWeight(3.0f);
		pa.line(planeOrigin, orthoFrame[2]);
		planeOrigin.showMeSphere(pa, 5.0f);
		for(int i=0;i<orthoFrame.length;++i) {	orthoFrame[i].showMeSphere(pa, 5.0f);}
		pa.popStyle();pa.popMatrix();				
	}//_drawOrthoFrame
	
	protected float modCnt = 0;//counter that will determine when the color should switch

	private static final int selIDX = SOM_GeomObjDrawType.selected.getVal();
	@Override
	protected final void _drawMeSelected(my_procApplet pa, float animTmMod) {
		if(planeObjs.length==0) {return;}
		modCnt += animTmMod;
		if(modCnt > .5){	modCnt = 0;	}//blink every ~second
		pa.pushMatrix();pa.pushStyle();			
		pa.strokeWeight(2.0f);
		pa.translate(planeOrigin);
		pa.scale(1.0f + modCnt*.5f);
		pa.translate(-planeOrigin.x,-planeOrigin.y,-planeOrigin.z);
		
		pa.shape(planeObjs[selIDX]);
		
		pa.popStyle();pa.popMatrix();
	}

	/**
	 * initialize object's ID
	 */
	protected final int incrID() {return IDGen++;}



}//class Geom_PlaneSOMExample
