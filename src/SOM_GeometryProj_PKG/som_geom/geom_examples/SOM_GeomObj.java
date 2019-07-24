package SOM_GeometryProj_PKG.som_geom.geom_examples;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import SOM_GeometryProj_PKG.som_geom.SOM_GeomMapManager;
import SOM_GeometryProj_PKG.som_geom.geom_UI.SOM_AnimWorldWin;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomObjDrawType;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomObjSamples;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomObjTypes;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomSamplePointf;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomSmplDataForEx;
import base_SOM_Objects.som_examples.SOM_ExDataType;
import base_SOM_Objects.som_examples.SOM_Example;
import base_SOM_Objects.som_examples.SOM_FtrDataType;
import base_UI_Objects.my_procApplet;
import base_Utils_Objects.MyMathUtils;
import base_Utils_Objects.vectorObjs.Tuple;
import base_Utils_Objects.vectorObjs.myPointf;
import base_Utils_Objects.vectorObjs.myVectorf;
import processing.core.PShape;

/**
 * class to instance the base functionality of a geometric object represented by 
 * some parameters and also by samples for use in training and consuming a SOM
 * 
 * @author john
 */
public abstract class SOM_GeomObj extends SOM_Example  {
	/**
	 * Integer object ID specific to instancing class objects
	 */
	private int GeomObj_ID;

	/**
	 * label to display, based on ID and object type
	 */
	protected String dispLabel;
	
	/**
	 * location-based and random color arrays, for display
	 */
	public int[] locClrAra;
	public int[] rndClrAra;
	public int[] labelClrAra;
	
	/**
	 * state flags
	 */
	private int[] geomStFlags;					//state flags for this instance - bits in array holding relevant process info
	public static final int
			debugIDX 						= 0,		//draw this sphere's sample points
			is3dIDX							= 1;		//this object is in 3d or 2d
	public static final int numgeomStFlags = 2;	
		
	/**
	 * type of object (geometric)
	 */
	private SOM_GeomObjTypes objGeomType;
		
	/**
	 * list of original object point samples and their owning objects making up this example - 
	 * these will be used to determine the classes for this object, to be passed to bmu map node for this example 
	 */
	protected final SOM_GeomSmplDataForEx[] geomSrcSamples;	
	/**
	 * given source points that make up this object
	 */
	private final SOM_GeomSamplePointf[] srcPts;

	/**
	 * construction managing sample points on the surface of this geom object
	 */
	protected SOM_GeomObjSamples objSamples;

	/**
	 * all class ID's this object belongs to - in this case, the IDs of the geomSrcSamples samples making up this object
	 */
	protected HashSet<Integer> classIDs;
	
	/**
	 * all category ID's this object belongs to
	 */
	protected HashSet<Integer> categoryIDs;

	/**
	 * array, idx 0 is rand color, idx 1 is loc color, idx 2 is no fill rnd, idx 3 is no fill loc clr, idx 4 is selected
	 */
	protected PShape[] sampleObjPShapes;
	
	/**
	 * tag to denote the beginning/end of a source point record in csv file
	 */
	private static final String srcPtTag = "SRCPT,";

	/**
	 * distance to draw point label from point
	 */
	public static final float lblDist = 2.0f;

	
	/**
	 * build a geometry-based training/validation example for the SOM
	 * @param _mapMgr : owning map manager
	 * @param _exType : example data type (ML) (training, testing, validation, etc)
	 * @param _id  : the unique ID info for this training example
	 * @param _srcSmpls : the source points and their owning SOM_GeomObj objects that built this sample (if null then these are the points that make this object)
	 * @param _worldBounds : bounds in world for valid values for this object
	 * @param _GeoType : geometric object type
	 */	
	public SOM_GeomObj(SOM_GeomMapManager _mapMgr, SOM_ExDataType _exType, String _id, SOM_GeomSmplDataForEx[] _geomSrcSmpls, SOM_GeomObjTypes _GeoType) {
		super(_mapMgr, _exType,_id);
		_ctorInit(_mapMgr,incrID(), _GeoType);
		
		geomSrcSamples = _geomSrcSmpls;		
		labelClrAra = getGeomFlag(is3dIDX)? new int[] {0,0,0,255} : new int[] {255,255,255,255};
		
		srcPts = initAndBuildSourcePoints(geomSrcSamples);
		
		objSamples = new SOM_GeomObjSamples(this, objGeomType, rndClrAra, labelClrAra);
	}//ctor
	
	/**
	 * building objects from CSV string
	 * @param _mapMgr
	 * @param _animWin
	 * @param _exType
	 * @param _oid
	 * @param _csvDat
	 * @param _worldBounds
	 * @param _GeoType
	 */
	public SOM_GeomObj(SOM_GeomMapManager _mapMgr, SOM_ExDataType _exType, String _oid, String _csvDat, SOM_GeomObjTypes _GeoType) {
		super(_mapMgr, _exType, _oid);
		_ctorInit(_mapMgr,Integer.parseInt(_csvDat.trim().split(",")[1].trim()),_GeoType);
		
		//only data needed to be saved
		srcPts = buildSrcPtsFromCSVString(objGeomType.getVal(), _csvDat);
		
		//build geomSrcSamples from srcPts 
		geomSrcSamples = new SOM_GeomSmplDataForEx[getSrcPts().length];
		for(int i=0;i<geomSrcSamples.length;++i) {geomSrcSamples[i] = new SOM_GeomSmplDataForEx(this, new SOM_GeomSamplePointf(getSrcPts()[i], dispLabel+"_gen_pt_"+i));}
		
		objSamples = new SOM_GeomObjSamples(this, objGeomType, rndClrAra, labelClrAra);
	}
	
	/**
	 * ctor to build object corresponding to bmu geometric object
	 * @param _mapMgr
	 * @param _mapNode
	 */	
	public SOM_GeomObj(SOM_GeomMapManager _mapMgr, SOM_GeomMapNode _mapNode, SOM_GeomObjTypes _GeoType) {
		super(_mapMgr, SOM_ExDataType.MapNode,_GeoType.toString()+"_"+_mapNode.OID);
		_ctorInit(_mapMgr,incrID(), _GeoType);

		float[] mapFtrsAra = _mapNode.getFtrs();	
		srcPts = buildSrcPtsFromBMUMapNodeFtrs(mapFtrsAra,  objGeomType.getVal(), dispLabel);
		String ftrs = "";
		for(int i=0;i<mapFtrsAra.length;++i) {
			ftrs += String.format("%.8f, ", mapFtrsAra[i]);
		}
		for(SOM_GeomSamplePointf pt : srcPts) {
			msgObj.dispInfoMessage("SOM_GeomObj::"+GeomObj_ID, "map node ctor", "Map Node:  "+ _mapNode.OID +" | " + pt.toStrBrf() + " | Ftrs : " +ftrs);
		}
		//build geomSrcSamples from srcPts 
		geomSrcSamples = new SOM_GeomSmplDataForEx[getSrcPts().length];
		for(int i=0;i<geomSrcSamples.length;++i) {geomSrcSamples[i] = new SOM_GeomSmplDataForEx(this, new SOM_GeomSamplePointf(getSrcPts()[i], dispLabel+"_gen_pt_"+i));}
		
		objSamples = new SOM_GeomObjSamples(this, objGeomType, rndClrAra, labelClrAra);
	}
	
	public SOM_GeomObj(SOM_GeomObj _otr) {
		super(_otr);
		GeomObj_ID = _otr.GeomObj_ID;
		dispLabel = _otr.dispLabel;
		//animWin = _otr.animWin;
		objGeomType = _otr.objGeomType;
		classIDs = _otr.classIDs;
		categoryIDs = _otr.categoryIDs;
		geomSrcSamples = _otr.geomSrcSamples;

		geomStFlags = _otr.geomStFlags;
		srcPts = _otr.getSrcPts();
		rndClrAra = _otr.rndClrAra;
		locClrAra = _otr.locClrAra;
		//minNumSmplsForEachExample = _otr.minNumSmplsForEachExample;
		labelClrAra = _otr.labelClrAra;
		objSamples = _otr.objSamples;
		sampleObjPShapes = _otr.sampleObjPShapes;
	}//copy ctor

	private void _ctorInit(SOM_GeomMapManager _mapMgr, int _geoObj_ID, SOM_GeomObjTypes _GeoType) {
		setWorldBounds(_mapMgr.getWorldBounds());	
		objGeomType = _GeoType;		
		GeomObj_ID = _geoObj_ID;		//sets GeomObj_ID to be count of instancing class objs
		dispLabel = objGeomType.toString() + "_"+GeomObj_ID;
		
		classIDs = new HashSet<Integer>();
		categoryIDs = new HashSet<Integer>();
		
		initGeomFlags();
		setGeomFlag(is3dIDX, objGeomType.getVal()>2);
		labelClrAra = getGeomFlag(is3dIDX)? new int[] {0,0,0,255} : new int[] {255,255,255,255};
		rndClrAra = getRandClr();		
	}//_ctorInit
	
	
	/**
	 * initialize object's ID
	 */
	protected abstract int incrID();
	
	/**
	 * initialize object's ID, and build SOM_GeomSamplePointf array from the source samples used to derive this object
	 * @param _srcSmpls
	 * @return
	 */
	protected final SOM_GeomSamplePointf[] initAndBuildSourcePoints(SOM_GeomSmplDataForEx[] _srcSmpls) {
		SOM_GeomSamplePointf[] ptAra = new SOM_GeomSamplePointf[_srcSmpls.length];
		for(int i=0;i<_srcSmpls.length;++i) {
			if(_srcSmpls[i].getObj() == null) {_srcSmpls[i].setObj(this);}
			ptAra[i]=new SOM_GeomSamplePointf(_srcSmpls[i].getPoint(), dispLabel+"_SrcPt_"+i);
		}
		return ptAra;
	}//initAndBuildSourcePoints

	
	/**
	 * column names of rawDescrForCSV data (preprocessed data)
	 * @return
	 */
	@Override
	public final String getRawDescColNamesForCSV() {
		String res = "OID,GeomObj_ID,TAG,";	
		for(int i=0;i<getSrcPts().length;++i) {res += getSrcPts()[i].toCSVHeaderStr()+"TAG,";}
		res +=getRawDescColNamesForCSV_Indiv();	
		res += objSamples.getRawDescColNamesForCSV();
		return res;
	}
	/**
	 * instance-class specific format for header column names
	 * @return
	 */
	protected abstract String getRawDescColNamesForCSV_Indiv();
	
	/**
	 * required info for this example to build feature data - use this so we don't have to reload and rebuilt from data every time
	 * @return
	 */
	@Override
	public final String getPreProcDescrForCSV() {
		//first have example id, then have geom obj type id
		String res = ""+OID+","+ GeomObj_ID+"," + srcPtTag;
		//only need to save srcPts
		for(int i=0;i<getSrcPts().length;++i) {res += getSrcPts()[i].toCSVStr() + srcPtTag;}			
		res += getPreProcDescrForCSV_Indiv();
		res += objSamples.getPreProcDescrForCSV();
		return res;
	}
	
	/**
	 * instance-class specific format for header column names
	 * @return
	 */
	protected abstract String getPreProcDescrForCSV_Indiv();
	
	/**
	 * build a specified # of sample points for this object
	 * @param _numSmplPts
	 */
	protected final void buildLocClrInitObjAndSamples(myPointf _locForClr, int _numSmplPts) {
		locClrAra = getClrFromWorldLoc(_locForClr);		
		objSamples.buildSampleSetAndPShapes(mapMgr.win.pa,_numSmplPts);
	}//buildLocClrInitObjAndSamples
	
	/**
	 * build array of source points from csv data
	 * @param _numSrcSmpls
	 * @param _csvDat
	 * @return
	 */
	private final SOM_GeomSamplePointf[] buildSrcPtsFromCSVString(int _numSrcSmpls, String _csvDat) {
		SOM_GeomSamplePointf[] res = new SOM_GeomSamplePointf[_numSrcSmpls];
		String[] strDatAra = _csvDat.split(srcPtTag),parseStrAra;
		//from idx 1 to end is src point csv strs
		for(int i=0;i<res.length;++i) {	
			parseStrAra = strDatAra[i+1].trim().split(",");			
			res[i] = new SOM_GeomSamplePointf(parseStrAra);}
		return res;		
	}//buildSrcPtsFromCSVString
	
	/**
	 * build an array of source points from the characteristic features of the source map node
	 */
	protected abstract SOM_GeomSamplePointf[] buildSrcPtsFromBMUMapNodeFtrs(float[] mapFtrs, int _numSrcSmpls, String _dispLabel);
	
	
	/**
	 * build location color for this object and build samples from csv string listing
	 * @param _locForClr
	 * @param _csvStr
	 */
	protected final void buildLocClrAndSamplesFromCSVStr(myPointf _locForClr, String _csvStr) {
		locClrAra = getClrFromWorldLoc(_locForClr);			
		objSamples.buildSampleSetAndPShapesFromCSVStr(mapMgr.win.pa, _csvStr);
	}
	
	/**
	 * build pshape to hold samples, to speed up rendering
	 * @param _numSmplPts
	 */
	public final void buildSmplSetAndSmplPShapes(int _numSmplPts) {
		objSamples.buildSampleSetAndPShapes(mapMgr.win.pa,_numSmplPts);
	}//buildSampleSet
	

	/**
	 * build orthonormal basis from the passed normal (unit)
	 * @param tmpNorm : normal 
	 * @return ortho basis
	 */
	protected myVectorf[] buildBasisVecs(myVectorf tmpNorm) {
		myVectorf[] basisVecs = new myVectorf[3];
		//build basis vectors
		basisVecs[0] = tmpNorm;
		if(basisVecs[0]._dot(myVectorf.FORWARD) == 1.0f) {//if planeNorm is in x direction means plane is y-z, so y axis will work as basis
			basisVecs[1] = new myVectorf(myVectorf.RIGHT);
		} else {
			basisVecs[1] = basisVecs[0]._cross(myVectorf.FORWARD);
			basisVecs[1]._normalize();
		}
		basisVecs[2] = basisVecs[1]._cross(basisVecs[0]);
		basisVecs[2]._normalize();		
		return basisVecs;
	}
	
	/**
	 * get a random location of a point on the surface of a sphere centered at ctr and with radius rad
	 * @param rad
	 * @param ctr
	 * @return
	 */
	protected final myPointf getRandPosOnSphere(double rad, myPointf ctr){
		myPointf pos = new myPointf();
		double 	cosTheta = ThreadLocalRandom.current().nextDouble(-1,1), sinTheta =  Math.sin(Math.acos(cosTheta)),
				phi = ThreadLocalRandom.current().nextDouble(0,MyMathUtils.twoPi_f);
		pos.set(sinTheta * Math.cos(phi), sinTheta * Math.sin(phi),cosTheta);
		pos._mult((float) rad);
		pos._add(ctr);
		return pos;
	}//getRandPosOnSphere
	
	/**
	 * return 4 points that describe a sphere uniquely - no trio of points can be colinear, and the 4 points cannot be co planar
	 * get 3 non-colinear points, find 4th by finding normal of plane 3 points describe
	 * @param rad radius of desired sphere
	 * @param ctr center of desired sphere
	 */	
	protected final myPointf[] getRandSpherePoints(float rad, myPointf ctr){
		myPointf a = getRandPosOnSphere(rad, ctr),b;
		do { b = getRandPosOnSphere(rad, ctr);} while (a.equals(b));
		myPointf c,d;
		myVectorf ab = new myVectorf(a,b), ac = myVectorf.ZEROVEC, bc, ad;
		ab._normalize();
		int iter = 0;
		boolean eqFail = false, dotProdFail = false;
		do {
			++iter;
			c = getRandPosOnSphere(rad, ctr);
			eqFail = (a.equals(c)) || (b.equals(c));
			if(eqFail) {continue;}
			ac = new myVectorf(a,c);
			ac._normalize();
			dotProdFail = (Math.abs(ab._dot(ac))==1.0f);
		} while (eqFail || dotProdFail);
		//4th point needs to be non-coplanar - will guarantee that 
		//it is also not collinear with any pair of existing points
		//normal to abc plane
		myVectorf planeNorm = ab._cross(ac)._normalize();
		//now find d so that it does not line in plane of abc - vector from ab
		eqFail = false; dotProdFail = false;
		do {
			++iter;
			d = getRandPosOnSphere(rad, ctr);
			eqFail = a.equals(d) || b.equals(d) || c.equals(d);
			if(eqFail) {continue;}
			ad = new myVectorf(a,d);
			ad._normalize();
			dotProdFail = (ad._dot(planeNorm) == 0.0f);
		} while (eqFail || dotProdFail);//if 0 then in plane (ortho to normal)
		
		myPointf[] spherePts = new myPointf[] {a,b,c,d};		
		if(iter>2) {//check this doesn't take too long - most of the time should never take more than a single iteration through each do loop
			msgObj.dispInfoMessage("Geom_SphereSOMExample","getRandSpherePoints", "Took Longer than 2 iterations to generate 4 points for sphere : " + iter);
			
		}
		return spherePts;
	}//getRandSpherePoints
	
	
	/**
	 * return a random point on this object
	 */
	public abstract myPointf getRandPointOnObj();
	
	@Override
	protected final void buildFeaturesMap() {
		clearFtrMap(rawftrMapTypeKey);//
		buildFeaturesMap_Indiv();
		//find magnitude of features
		ftrVecMag = 0;
		Float ftrSqrMag = 0.0f;
		int ftrSize = ftrMaps[rawftrMapTypeKey].size();
		for(int i=0;i<ftrSize;++i) {
			Float val = ftrMaps[rawftrMapTypeKey].get(i);
			ftrSqrMag += val * val;
			((SOM_GeomMapManager)mapMgr).trainDatObjBnds.checkValInBnds(i, val);			
		}
		ftrVecMag = (float)Math.sqrt(ftrSqrMag);

	}//buildFeaturesMap
	
	protected abstract void buildFeaturesMap_Indiv();
			
	/**
	 * Return all the relevant classes found in this example/that this example belongs 
	 * to, for class segment calculation
	 * @return class IDs present in this example
	 */	
	@Override
	protected final HashSet<Integer> getAllClassIDsForClsSegmentCalc() {		return classIDs;}

	/**
	 * Return all the relevant categories found in this example or that this example 
	 * belongs to, for category segment membership calculation
	 * @return category IDs present in this example
	 */	
	@Override
	protected final HashSet<Integer> getAllCategoryIDsForCatSegmentCalc() {		return categoryIDs;}
	
	@Override
	protected final void buildStdFtrsMap() {	//build standardized features
		calcStdFtrVector(ftrMaps[rawftrMapTypeKey], ftrMaps[stdFtrMapTypeKey], mapMgr.getTrainFtrMins(),mapMgr.getTrainFtrDiffs(), -1.0f, 2.0f);
		setFlag(stdFtrsBuiltIDX, true);
	}	
	
	@Override
	protected final void buildAllNonZeroFtrIDXs() {
		allNonZeroFtrIDXs = new ArrayList<Integer>();
		//all idxs should be considered "non-zero", even those with zero value, since these examples are dense
		for(int i=0;i<numFtrs;++i) {allNonZeroFtrIDXs.add(i);}		
	}

	@Override
	// any processing that must occur once all constituent data records are added to
	// this example - must be called externally, BEFORE ftr vec is built
	public final void finalizeBuildBeforeFtrCalc() {	}

	@Override
	//called after all features of this kind of object are built
	public final void postFtrVecBuild() {}
	@Override
	//this is called after an individual example's features are built
	protected final void _buildFeatureVectorEnd_Priv() {}

	@Override
	protected final void setIsTrainingDataIDX_Priv() {
		exampleDataType = isTrainingData ? SOM_ExDataType.Training : SOM_ExDataType.Testing;
		setMapNodeClrs(mapMgr.getClrFillStrkTxtAra(exampleDataType));
	}
	
	/**
	 * return the appropriate string value for the dense training data - should be numeric key value to save in lrn or csv dense file
	 * Strafford will always use sparse data so this doesn't matter
	 * @return
	 */
	@Override
	protected String getDenseTrainDataKey() {
		return String.format("%09d", testTrainDataIDX);
	}

	/**
	 *  this will build the comparison feature vector array that is used as the comparison vector 
	 *  in distance measurements - for most cases this will just be a copy of the ftr vector array
	 *  but in some instances, there might be an alternate vector to be used to handle when, for 
	 *  example, an example has ftrs that do not appear on the map
	 * @param _ignored : ignored this is ignored for this kind of example
	 */
	@Override
	public final void buildCompFtrVector(float _ignored) {compFtrMaps = ftrMaps;}
	
	@Override
	protected final String dispFtrVal(TreeMap<Integer, Float> ftrs, Integer idx) {
		Float ftr = ftrs.get(idx);
		return "idx : " + idx + " | val : " + String.format("%1.4g",  ftr) + " || ";
	}
		
	////////////////////////
	// draw functions
	protected static final int lBnd = 40, uBnd = 255, rndBnd = uBnd - lBnd;
	//convert a world location within the bounded cube region to be a 4-int color array
	public abstract int[] getClrFromWorldLoc(myPointf pt);
	/**
	 * 2 d get world loc color
	 * @param pt
	 * @param _worldBounds
	 * @return
	 */
	public final int[] getClrFromWorldLoc_2D(myPointf pt,float[][] _worldBounds) {		//for 2d world bounds is idx 0 == 0,1 for min, diff, idx 1 == 0,1 for x,y
		float rs = (pt.x-_worldBounds[0][0])/_worldBounds[1][0], gs = (pt.y-_worldBounds[0][1])/_worldBounds[1][1];
		float bs = Math.abs(rs - gs);
		return new int[]{(int)(rndBnd*rs)+lBnd,(int)(rndBnd*gs)+lBnd,(int)(rndBnd*bs)+lBnd,255};
	}
	/**
	 * 3 d get world loc color
	 * @param pt point
	 * @param _worldBounds
	 * @return
	 */
	public final int[] getClrFromWorldLoc_3D(myPointf pt,float[][] _worldBounds) {		
		return new int[]{(int)(255*(pt.x-_worldBounds[0][0])/_worldBounds[1][0]),(int)(255*(pt.y-_worldBounds[0][1])/_worldBounds[1][1]),(int)(255*(pt.z-_worldBounds[0][2])/_worldBounds[1][2]),255};
	}
	
	/**
	 * build a random color
	 * @return
	 */
	protected final int[] getRandClr() {
		int r = ThreadLocalRandom.current().nextInt(rndBnd)+lBnd, g = ThreadLocalRandom.current().nextInt(rndBnd)+lBnd, b =ThreadLocalRandom.current().nextInt(rndBnd)+lBnd;
		return new int[] {r,g,b};
	}

	/**
	 * draw entire object this class represents, using location as color or using randomly assigned color
	 * @param pa
	 */	
	public final void drawMeClrRnd(my_procApplet pa) {
		pa.pushMatrix();pa.pushStyle();		
		pa.setFill(rndClrAra,255);
		pa.setStroke(rndClrAra,255);
		_drawMe_Geom(pa,SOM_GeomObjDrawType.rndClr);
		pa.popStyle();pa.popMatrix();	
	}	
	
	
	public final void drawMeClrLoc(my_procApplet pa) {
		pa.pushMatrix();pa.pushStyle();		
		pa.setFill(locClrAra,255);
		pa.setStroke(locClrAra,255);
		_drawMe_Geom(pa,SOM_GeomObjDrawType.locClr);
		pa.popStyle();pa.popMatrix();	
	}
	
	/**
	 * draw entire object this class represents, using location as color or using randomly assigned color
	 * @param pa
	 */	
	public final void drawMeClrRnd_WF(my_procApplet pa) {
		pa.pushMatrix();pa.pushStyle();		
		pa.noFill();
		pa.setStroke(rndClrAra,255);
		_drawMe_Geom(pa,SOM_GeomObjDrawType.noFillRndClr);
		pa.popStyle();pa.popMatrix();	
	}	
	
	public final void drawMeClrLoc_WF(my_procApplet pa) {
		pa.pushMatrix();pa.pushStyle();		
		pa.noFill();
		pa.setStroke(locClrAra,255);
		_drawMe_Geom(pa,SOM_GeomObjDrawType.noFillLocClr);
		pa.popStyle();pa.popMatrix();	
	}
	
	/**
	 * draw entire object this class represents at location dictated by BMU, using location as color or using randomly assigned color
	 * @param pa
	 */	
	public final void drawMeClrRnd_BMU(my_procApplet pa) {
		pa.pushMatrix();pa.pushStyle();		
		pa.setFill(rndClrAra,255);
		pa.setStroke(rndClrAra,255);
		_drawMe_Geom_BMU(pa,  SOM_GeomObjDrawType.rndClr);
		pa.popStyle();pa.popMatrix();	
	}	
	
	
	public final void drawMeClrLoc_BMU(my_procApplet pa) {
		pa.pushMatrix();pa.pushStyle();		
		pa.setFill(locClrAra,255);
		pa.setStroke(locClrAra,255);
		_drawMe_Geom_BMU(pa, SOM_GeomObjDrawType.locClr );
		pa.popStyle();pa.popMatrix();	
	}
	
	/**
	 * draw entire object this class represents, using location as color or using randomly assigned color
	 * @param pa
	 */	
	public final void drawMeClrRnd_WF_BMU(my_procApplet pa) {
		pa.pushMatrix();pa.pushStyle();		
		pa.noFill();
		pa.setStroke(rndClrAra,255);
		_drawMe_Geom_BMU(pa, SOM_GeomObjDrawType.noFillRndClr);
		pa.popStyle();pa.popMatrix();	
	}	
	
	
	public final void drawMeClrLoc_WF_BMU(my_procApplet pa) {
		pa.pushMatrix();pa.pushStyle();		
		pa.noFill();
		pa.setStroke(locClrAra,255);
		_drawMe_Geom_BMU(pa, SOM_GeomObjDrawType.noFillLocClr);
		pa.popStyle();pa.popMatrix();	
	}
		
	/**
	 * Draw this object's label
	 * @param pa
	 */
	public abstract void drawMyLabel(my_procApplet pa, SOM_AnimWorldWin animWin);
	
	protected void _drawLabelAtLoc_3D(my_procApplet pa, myPointf pt, SOM_AnimWorldWin animWin, String label, float _scl, float _off) {
		pa.pushMatrix();pa.pushStyle();		
		pa.translate(pt.x,pt.y,pt.z); 
		animWin.unSetCamOrient();
		pa.scale(_scl);
		pa.text(label, _off,-_off,0); 
		pa.popStyle();pa.popMatrix();
	}
	
	/**
	 * draw this object
	 * @param pa
	 */
	protected abstract void _drawMe_Geom(my_procApplet pa, SOM_GeomObjDrawType drawType);
	
	/**
	 * draw this object at location dictated by bmu
	 * @param pa
	 * @param animTmMod
	 */	
	protected abstract void _drawMe_Geom_BMU(my_procApplet pa, SOM_GeomObjDrawType drawType);
	
	/**
	 * draw this object with appropriate selected highlight/cue
	 * @param pa
	 * @param animTmMod
	 */
	public void drawMeSelected_ClrLoc(my_procApplet pa, float animTmMod) {
		drawMeClrLoc(pa);
		_drawMeSelected(pa,animTmMod);
	
	};
	public void drawMeSelected_ClrLoc_Smpl(my_procApplet pa, float animTmMod) {
		drawMeSelected_ClrLoc(pa,animTmMod);
		objSamples.drawMeSmplsSelected(pa);
	}
	/**
	 * draw this object with appropriate selected highlight/cue
	 * @param pa
	 * @param animTmMod
	 */
	public void drawMeSelected_ClrRnd(my_procApplet pa, float animTmMod) {
		drawMeClrRnd(pa);
		_drawMeSelected(pa,animTmMod);
	};
	public void drawMeSelected_ClrRnd_Smpl(my_procApplet pa, float animTmMod) {
		drawMeSelected_ClrRnd(pa,animTmMod);
		objSamples.drawMeSmplsSelected(pa);
	}
	protected abstract void _drawMeSelected(my_procApplet pa, float animTmMod);
	
	
	/**
	 * draw this object's samples' labels
	 * @param pa
	 */
	public abstract void drawMySmplsLabel(my_procApplet pa, SOM_AnimWorldWin animWin);
	
	/**
	 * draw this object's samples, using the random color
	 * @param pa
	 */
	public final void drawMeSmplsClrRnd(my_procApplet pa){	objSamples.drawMeSmplsClrRnd(pa);}//
	
	/**
	 * draw this object's samples, using the location-based color
	 * @param pa
	 */
	public final void drawMeSmplsClrLoc(my_procApplet pa){	objSamples.drawMeSmplsClrLoc(pa);}//
	


	//////////////////////////////
	// bmu drawing
	/**
	 * Draw this object's bmu with a label
	 * @param pa
	 */
	public abstract void drawMyLabel_BMU(my_procApplet pa);
	public void drawMeSelected_BMU_ClrLoc(my_procApplet pa,float animTmMod) {
		drawMeClrLoc_BMU(pa);
		_drawMeSelected_BMU(pa, animTmMod);
	}
	public void drawMeSelected_BMU_ClrRnd(my_procApplet pa,float animTmMod) { 
		drawMeClrRnd_BMU(pa);
		_drawMeSelected_BMU(pa, animTmMod);
	}
	protected abstract void _drawMeSelected_BMU(my_procApplet pa, float animTmMod);
	
	
	public final void drawMeSmplsClrRnd_BMU(my_procApplet pa){
//		pa.pushMatrix();pa.pushStyle();
//		pa.setFill(rndClrAra,255); 
//		pa.setStroke(rndClrAra,255);
//		pa.sphereDetail(ptDet);
//		for(myPointf pt : objSamplePts){
//			//pt.drawMeObjRandClr_BMU(pa);
//			pa.pushMatrix(); 
//			pa.translate(((Geom_SOMMapNode)(pt.getBmu())).mapLoc); 
//			pa.sphere(ptRad); 
//			pa.popMatrix();
//		}
//		pa.popStyle();pa.popMatrix();
	}//
	
	public final void drawMeSmplsClrLoc_BMU(my_procApplet pa){
//		pa.pushMatrix();pa.pushStyle();		
//		pa.setFill(locClrAra,255);
//		pa.setStroke(locClrAra,255);
//		pa.sphereDetail(ptDet);
//		for(myPointf pt : objSamplePts){
//			//pt.drawMeObjLocClr_BMU(pa);
//			pa.pushMatrix(); 
//			pa.translate(((Geom_SOMMapNode)(pt.getBmu())).worldLoc); 
//			pa.sphere(ptRad); 
//			pa.popMatrix();
//		}
//		pa.popStyle();pa.popMatrix();
	}//	


	////////////////////////
	// end draw functions

	////////////////////////
	// data accessor functions

	/**
	 * call from ctor of base class, but set statically for each instancing class type
	 * @param _worldBounds
	 */
	protected abstract void setWorldBounds(float[][] _worldBounds);

	//public final int getID() {return GeomObj_ID;}
	
	
	public int getNumSamples() {return objSamples.getNumSamples();}
	public SOM_GeomSamplePointf getSamplePt(int idx) {return objSamples.getSamplePt(idx);}
	public SOM_GeomSamplePointf[] getAllSamplePts() {return objSamples.getAllSamplePts();}

	

	private void initGeomFlags(){geomStFlags = new int[1 + numFlags/32]; for(int i = 0; i<numFlags; ++i){setFlag(i,false);}}
	public final void setGeomFlag(int idx, boolean val){
		int flIDX = idx/32, mask = 1<<(idx%32);
		geomStFlags[flIDX] = (val ?  geomStFlags[flIDX] | mask : geomStFlags[flIDX] & ~mask);
		switch (idx) {//special actions for each flag
			case debugIDX : {break;}			
		}
	}//setFlag	
	public final boolean getGeomFlag(int idx){int bitLoc = 1<<(idx%32);return (geomStFlags[idx/32] & bitLoc) == bitLoc;}

	public SOM_GeomSamplePointf[] getSrcPts() {	return srcPts;}

	public String getDispLabel() {return dispLabel;}

}//class SOM_GeomObj
