package SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import SOM_GeometryProj_PKG.som_geom.SOM_GeomMapManager;
import SOM_GeometryProj_PKG.som_geom.geom_UI.SOM_AnimWorldWin;
import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomSamplePointf;
import base_SOM_Objects.som_examples.SOM_ExDataType;
import base_SOM_Objects.som_examples.SOM_Example;
import base_UI_Objects.my_procApplet;
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
	 * owning display window for this object
	 */
	protected final SOM_AnimWorldWin animWin;

	/**
	 * label to display, based on ID and object type
	 */
	protected String dispLabel;
	
	/**
	 * location-based and random color arrays, for display
	 */
	public int[] locClrAra;
	public final int[] rndClrAra;
	public final int[] labelClrAra;
	
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
	public final SOM_GeomObjTypes objGeomType;
		
	/**
	 * list of original object point samples and their owning objects making up this example - 
	 * these will be used to determine the classes for this object, to be passed to bmu map node for this example 
	 */
	protected final SOM_GeomSmplDataForEx[] geomSrcSamples;	
	/**
	 * given source points that make up this object
	 */
	public final SOM_GeomSamplePointf[] srcPts;

	/**
	 * construction managing sample points on the surface of this geom object
	 */
	public SOM_GeomObjSamples objSamples;

	/**
	 * all class ID's this object belongs to
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
	 * tag to denote the beginning/end of a sample point record in csv file
	 */
	private static final String samplPtTag = "SMPLPT,";
	
	/**
	 * build a geometry-based training/validation example for the SOM
	 * @param _mapMgr : owning map manager
	 * @param _exType : example data type (ML) (training, testing, validation, etc)
	 * @param _id  : the unique ID info for this training example
	 * @param _srcSmpls : the source points and their owning SOM_GeomObj objects that built this sample (if null then these are the points that make this object)
	 * @param _worldBounds : bounds in world for valid values for this object
	 * @param _GeoType : geometric object type
	 */	
	public SOM_GeomObj(SOM_GeomMapManager _mapMgr, SOM_AnimWorldWin _animWin, SOM_ExDataType _exType, String _id, SOM_GeomSmplDataForEx[] _srcSmpls, SOM_GeomObjTypes _GeoType) {
		super(_mapMgr, _exType,_id);
		animWin = _animWin;
		objGeomType=_GeoType;
		classIDs = new HashSet<Integer>();
		categoryIDs = new HashSet<Integer>();
		geomSrcSamples = _srcSmpls;
		
		initGeomFlags();
		setGeomFlag(is3dIDX, objGeomType.getVal()>2);
		rndClrAra = getRandClr();	
		labelClrAra = getGeomFlag(is3dIDX)? new int[] {0,0,0,255} : new int[] {255,255,255,255};
		
		srcPts = initAndBuildSourcePoints(geomSrcSamples);
		
		objSamples = new SOM_GeomObjSamples(this);
		setWorldBounds(_mapMgr.getWorldBounds());				
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
		animWin = _mapMgr.dispWin;
		objGeomType = _GeoType;		
		initGeomFlags();
		rndClrAra = getRandClr();		
		int minNumSmplsForEachExample = objGeomType.getVal();
		setGeomFlag(is3dIDX, minNumSmplsForEachExample>2);
		labelClrAra = getGeomFlag(is3dIDX)? new int[] {0,0,0,255} : new int[] {255,255,255,255};
		
		//only data needed to be saved
		srcPts = buildSrcPtsFromCSVString(minNumSmplsForEachExample, _csvDat);
		
		//build geomSrcSamples from srcPts
		geomSrcSamples = new SOM_GeomSmplDataForEx[srcPts.length];
		for(int i=0;i<geomSrcSamples.length;++i) {geomSrcSamples[i] = new SOM_GeomSmplDataForEx(this, srcPts[i]);}
		
		objSamples = new SOM_GeomObjSamples(this);
		setWorldBounds(_mapMgr.getWorldBounds());						
	}
	
	public SOM_GeomObj(SOM_GeomObj _otr) {
		super(_otr);
		animWin = _otr.animWin;
		objGeomType = _otr.objGeomType;
		classIDs = _otr.classIDs;
		categoryIDs = _otr.categoryIDs;
		geomSrcSamples = _otr.geomSrcSamples;

		geomStFlags = _otr.geomStFlags;
		srcPts = _otr.srcPts;
		rndClrAra = _otr.rndClrAra;
		locClrAra = _otr.locClrAra;
		//minNumSmplsForEachExample = _otr.minNumSmplsForEachExample;
		labelClrAra = _otr.labelClrAra;
		objSamples = _otr.objSamples;
		sampleObjPShapes = _otr.sampleObjPShapes;
	}//copy ctor

	/**
	 * initialize object's ID, and build SOM_GeomSamplePointf array from the source samples used to derive this object
	 * @param _srcSmpls
	 * @return
	 */
	protected abstract SOM_GeomSamplePointf[] initAndBuildSourcePoints(SOM_GeomSmplDataForEx[] _srcSmpls);
	
	/**
	 * column names of rawDescrForCSV data (preprocessed data)
	 * @return
	 */
	@Override
	public final String getRawDescColNamesForCSV() {
		String res = "OID,GeomObj_ID,TAG,";	
		for(int i=0;i<srcPts.length;++i) {res += srcPts[i].toCSVHeaderStr()+"TAG,";}
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
		for(int i=0;i<srcPts.length;++i) {res += srcPts[i].toCSVStr() + srcPtTag;}			
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
	private SOM_GeomSamplePointf[] buildSrcPtsFromCSVString(int _numSrcSmpls, String _csvDat) {
		SOM_GeomSamplePointf[] res = new SOM_GeomSamplePointf[_numSrcSmpls];
		String[] strDatAra = _csvDat.split(srcPtTag);
		//from idx 1 to end is src point csv strs
		for(int i=0;i<res.length;++i) {	
			String[] parseStrAra = strDatAra[i+1].trim().split(",");			
			res[i] = new SOM_GeomSamplePointf(parseStrAra);}
		return res;		
	}
	
	
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
	public final void buildSampleSetAndPShapes(int _numSmplPts) {
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
	 * return a random point on this object
	 */
	public abstract myPointf getRandPointOnObj();
	
	@Override
	protected final void buildFeaturesMap() {
		clearFtrMap(ftrMapTypeKey);//
		buildFeaturesMap_Indiv();
		ftrVecMag = 0;
		int ftrSize = ftrMaps[ftrMapTypeKey].size();
		for(int i=0;i<ftrSize;++i) {
			Float val = ftrMaps[ftrMapTypeKey].get(i);
			ftrVecMag += val;
			((SOM_GeomMapManager)mapMgr).trainDatObjBnds.checkValInBnds(i, val);			
		}
		ftrVecMag /= 1.0f*ftrSize;

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
		calcStdFtrVector(ftrMaps[ftrMapTypeKey], ftrMaps[stdFtrMapTypeKey], mapMgr.getTrainFtrMins(),mapMgr.getTrainFtrDiffs());
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
		nodeClrs = mapMgr.getClrFillStrkTxtAra(exampleDataType);
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

	/**
	 * column header data for saving preprocessed sample data of a particular type
	 * @return
	 */
	public final String toCSVStr_Header() {	
		String objtypeStr = this.objGeomType.toString();
		String res = "Owning " + objtypeStr + " ID, ";
		for(int i=0;i<this.srcPts.length;++i) {	res += objtypeStr+" pt "+i+".x, "+objtypeStr+" pt "+i+".y, "+objtypeStr+" pt "+i+".z, ";}
		return res;
	}
	
		
	////////////////////////
	// draw functions
	protected final int lBnd = 40, uBnd = 255, rndBnd = uBnd - lBnd;
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
	 * Draw this object's
	 * @param pa
	 */
	public abstract void drawMyLabel(my_procApplet pa);
			
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
	 * draw this object's samples, using the random color
	 * @param pa
	 */
	public abstract void drawMySmplsLabel(my_procApplet pa);
	
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
	public abstract void drawMeLabel_BMU(my_procApplet pa);
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

	
	protected final void setID(int _id) {GeomObj_ID=_id; dispLabel = objGeomType.name() + "_"+GeomObj_ID;}
	public final int getID() {return GeomObj_ID;}
	
	/**
	 * csv data for saving preprocessed sample data of a particular type
	 * @return
	 */
	public final String toCSVStr() {	
		String res = ""+getID()+", ";
		for(int i=0;i<srcPts.length-1;++i) {		res+=srcPts[i].toStrCSV() +", ";	}
		res+=srcPts[srcPts.length-1].toStrCSV();
		return res;
	}

	private void initGeomFlags(){geomStFlags = new int[1 + numFlags/32]; for(int i = 0; i<numFlags; ++i){setFlag(i,false);}}
	public final void setGeomFlag(int idx, boolean val){
		int flIDX = idx/32, mask = 1<<(idx%32);
		geomStFlags[flIDX] = (val ?  geomStFlags[flIDX] | mask : geomStFlags[flIDX] & ~mask);
		switch (idx) {//special actions for each flag
			case debugIDX : {break;}			
		}
	}//setFlag	
	public final boolean getGeomFlag(int idx){int bitLoc = 1<<(idx%32);return (geomStFlags[idx/32] & bitLoc) == bitLoc;}		



}//class SOM_GeomObj
