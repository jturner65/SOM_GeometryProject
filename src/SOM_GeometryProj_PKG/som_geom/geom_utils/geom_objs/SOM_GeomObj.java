package SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs;

import java.awt.Color;
import java.awt.color.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import SOM_GeometryProj_PKG.geom_ObjExamples.GeomObjDrawType;
import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_SmplDataForSOMExample;
import SOM_GeometryProj_PKG.som_geom.SOM_GeomMapManager;
import SOM_GeometryProj_PKG.som_geom.geom_UI.SOM_AnimWorldWin;
import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomExample;
import base_SOM_Objects.som_examples.SOM_ExDataType;
import base_SOM_Objects.som_examples.SOM_Example;
import base_UI_Objects.my_procApplet;
import base_Utils_Objects.io.MessageObject;
import base_Utils_Objects.vectorObjs.myPointf;
import processing.core.PConstants;
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
	 * # of sample points required to uniquely define an object of this type (i.e. # of examples used to build training example)
	 */
	public final int minNumSmplsForEachExample;
	
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
	 * radius of sample point to display
	 */
	public static final float ptRad = 3.0f;
	/**
	 * sphere detail of sample point to display
	 */
	public static final int ptDet = 2;	
		
	/**
	 * type of object (geometric)
	 */
	public final SOM_GeomObjTypes objGeomType;
		
	/**
	 * list of original object point samples and their owning objects making up this example - 
	 * these will be used to determine the classes for this object, to be passed to bmu map node for this example 
	 */
	protected final Geom_SmplDataForSOMExample[] geomSrcSamples;	
	/**
	 * given source points that make up this object
	 */
	public final myPointf[] srcPts;
	/**
	 * random sample points on this object, which will all share this object's constituent, uniquely identifying characteristics
	 */
	public myPointf[] objSamplePts;


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
	protected PShape[] sampleObjs;	

	
	/**
	 * build a geometry-based training/validation example for the SOM
	 * @param _mapMgr : owning map manager
	 * @param _exType : example data type (ML) (training, testing, validation, etc)
	 * @param _id  : the unique ID info for this training example
	 * @param _srcSmpls : the source points and their owning SOM_GeomObj objects that built this sample (if null then these are the points that make this object)
	 * @param _worldBounds : bounds in world for valid values for this object
	 * @param _GeoType : geometric object type
	 */
	
	public SOM_GeomObj(SOM_GeomMapManager _mapMgr, SOM_AnimWorldWin _animWin, SOM_ExDataType _exType, String _id, Geom_SmplDataForSOMExample[] _srcSmpls, float[][] _worldBounds, SOM_GeomObjTypes _GeoType) {
		super(_mapMgr, _exType,_id);
		animWin = _animWin;
		objGeomType=_GeoType;
		classIDs = new HashSet<Integer>();
		categoryIDs = new HashSet<Integer>();
		geomSrcSamples = _srcSmpls;
		srcPts = new myPointf[geomSrcSamples.length];
		for(int i=0;i<geomSrcSamples.length;++i) {srcPts[i]=new myPointf(geomSrcSamples[i].getPoint());}

		initGeomFlags();
		minNumSmplsForEachExample=objGeomType.getVal();
		setGeomFlag(is3dIDX, minNumSmplsForEachExample>2);
		rndClrAra = getRandClr();	
		labelClrAra = getGeomFlag(is3dIDX)? new int[] {0,0,0,255} : new int[] {255,255,255,255};
		
		setWorldBounds(_worldBounds);
				
	}//ctor
	
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
		minNumSmplsForEachExample = _otr.minNumSmplsForEachExample;
		labelClrAra = _otr.labelClrAra;
	}//copy ctor

	
	
	/**
	 * build a specified # of sample points for this object
	 * @param _numSmplPts
	 */
	protected final void buildLocClrInitObjAndSamples(myPointf _locForClr, int _numSmplPts) {
		locClrAra = getClrFromWorldLoc(_locForClr);		

		buildSampleSet(_numSmplPts);
	}//buildLocClrInitObjAndSamples
	
	/**
	 * build pshape to hold samples, to speed up rendering
	 * @param _numSmplPts
	 */
	public final void buildSampleSet(int _numSmplPts) {
		objSamplePts = new myPointf[_numSmplPts];
		for(int i=0;i<objSamplePts.length;++i) {	
			objSamplePts[i]=getRandPointOnObj(); 
			//msgObj.dispInfoMessage("SOM_GeomObj::"+type.toString(), "buildLocClrInitObjAndSamples", "ID : " + ID + " | sample pt loc : " + objSamplePts[i].toStrBrf());
		}	
		
		//create representation
		sampleObjs = new PShape[GeomObjDrawType.getNumVals()];
		sampleObjs[GeomObjDrawType.rndClr.getVal()] = buildSampleCloud(rndClrAra);
		sampleObjs[GeomObjDrawType.locClr.getVal()] = buildSampleCloud(locClrAra);
		sampleObjs[GeomObjDrawType.noFillRndClr.getVal()] = buildSampleCloud(rndClrAra);
		sampleObjs[GeomObjDrawType.noFillLocClr.getVal()] = buildSampleCloud(locClrAra);
		sampleObjs[GeomObjDrawType.selected.getVal()] = null;
		
	}//buildSampleSet
	
	private PShape buildSampleCloud(int[] clr) {
		PShape poly = mapMgr.win.pa.createShape(PConstants.GROUP); 
		for(int i=0;i<objSamplePts.length;++i) {
			myPointf pt = objSamplePts[i];
			PShape tmp = mapMgr.win.pa.createShape(PConstants.GROUP);			
			tmp.translate(pt.x,pt.y,pt.z);
			PShape tmp2 = mapMgr.win.pa.createShape(PConstants.SPHERE, ptRad);
			tmp2.beginShape();
			tmp2.fill(clr[0], clr[1], clr[2],255);		
			tmp2.stroke(clr[0], clr[1], clr[2],255);
			tmp2.strokeWeight(3.0f);
			tmp2.endShape();
			tmp.addChild(tmp2);
			poly.addChild(tmp);
		}

		return poly;
	}//buildSampleCloud
	
	/**
	 * return a random point on this object
	 */
	public abstract myPointf getRandPointOnObj();
			
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
		_drawMe_Geom(pa,GeomObjDrawType.rndClr);
		pa.popStyle();pa.popMatrix();	
	}	
	
	
	public final void drawMeClrLoc(my_procApplet pa) {
		pa.pushMatrix();pa.pushStyle();		
		pa.setFill(locClrAra,255);
		pa.setStroke(locClrAra,255);
		_drawMe_Geom(pa,GeomObjDrawType.locClr);
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
		_drawMe_Geom(pa,GeomObjDrawType.noFillRndClr);
		pa.popStyle();pa.popMatrix();	
	}	
	
	public final void drawMeClrLoc_WF(my_procApplet pa) {
		pa.pushMatrix();pa.pushStyle();		
		pa.noFill();
		pa.setStroke(locClrAra,255);
		_drawMe_Geom(pa,GeomObjDrawType.noFillLocClr);
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
		_drawMe_Geom_BMU(pa,  GeomObjDrawType.rndClr);
		pa.popStyle();pa.popMatrix();	
	}	
	
	
	public final void drawMeClrLoc_BMU(my_procApplet pa) {
		pa.pushMatrix();pa.pushStyle();		
		pa.setFill(locClrAra,255);
		pa.setStroke(locClrAra,255);
		_drawMe_Geom_BMU(pa, GeomObjDrawType.locClr );
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
		_drawMe_Geom_BMU(pa, GeomObjDrawType.noFillRndClr);
		pa.popStyle();pa.popMatrix();	
	}	
	
	
	public final void drawMeClrLoc_WF_BMU(my_procApplet pa) {
		pa.pushMatrix();pa.pushStyle();		
		pa.noFill();
		pa.setStroke(locClrAra,255);
		_drawMe_Geom_BMU(pa, GeomObjDrawType.noFillLocClr);
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
	protected abstract void _drawMe_Geom(my_procApplet pa, GeomObjDrawType drawType);
	
	/**
	 * draw this object at location dictated by bmu
	 * @param pa
	 * @param animTmMod
	 */	
	protected abstract void _drawMe_Geom_BMU(my_procApplet pa, GeomObjDrawType drawType);
	
	/**
	 * draw this object with appropriate selected highlight/cue
	 * @param pa
	 * @param animTmMod
	 */
	public abstract void drawMeSelected(my_procApplet pa, float animTmMod);
	/**
	 * draw this object's samples, using the random color
	 * @param pa
	 */
	public abstract void drawMySmplsLabel(my_procApplet pa);
	
	/**
	 * draw this object's samples, using the random color
	 * @param pa
	 */
	public final void drawMeSmplsClrRnd(my_procApplet pa){
		pa.pushMatrix();pa.pushStyle();
		pa.setFill(rndClrAra,255); 
		pa.setStroke(rndClrAra,255);
		_drawAllSamples(pa);
		pa.popStyle();pa.popMatrix();
	}//
	
	/**
	 * draw this object's samples, using the location-based color
	 * @param pa
	 */
	public final void drawMeSmplsClrLoc(my_procApplet pa){
		pa.pushMatrix();pa.pushStyle();		
		pa.setFill(locClrAra,255);
		pa.setStroke(locClrAra,255);
		_drawAllSamples(pa);
		pa.popStyle();pa.popMatrix();
	}//	
	
	private final void _drawAllSamples(my_procApplet pa){
		pa.sphereDetail(ptDet);
		for(myPointf pt : objSamplePts){
			pa.pushMatrix(); 
			pa.translate(pt); 
			pa.sphere(ptRad); 
			pa.popMatrix();
		}
	}
	

	//////////////////////////////
	// bmu drawing
	/**
	 * Draw this object's bmu with a label
	 * @param pa
	 */
	public abstract void drawMeLabel_BMU(my_procApplet pa);
	public abstract void drawMeSelected_BMU(my_procApplet pa,float animTmMod);
	
	public final void drawMeSmplsClrRnd_BMU(my_procApplet pa){
		pa.pushMatrix();pa.pushStyle();
//		pa.setFill(rndClrAra,255); 
//		pa.setStroke(rndClrAra,255);
//		pa.sphereDetail(ptDet);
		for(myPointf pt : objSamplePts){
			//pt.drawMeObjRandClr_BMU(pa);
//			pa.pushMatrix(); 
//			pa.translate(((Geom_SOMMapNode)(pt.getBmu())).mapLoc); 
//			pa.sphere(ptRad); 
//			pa.popMatrix();
		}
		pa.popStyle();pa.popMatrix();
	}//
	
	public final void drawMeSmplsClrLoc_BMU(my_procApplet pa){
		pa.pushMatrix();pa.pushStyle();		
//		pa.setFill(locClrAra,255);
//		pa.setStroke(locClrAra,255);
//		pa.sphereDetail(ptDet);
		for(myPointf pt : objSamplePts){
			//pt.drawMeObjLocClr_BMU(pa);
//			pa.pushMatrix(); 
//			pa.translate(((Geom_SOMMapNode)(pt.getBmu())).worldLoc); 
//			pa.sphere(ptRad); 
//			pa.popMatrix();
		}
		pa.popStyle();pa.popMatrix();
	}//	

	public void drawMeSmplsClrSmplLoc_BMU(my_procApplet pa){
		pa.pushMatrix();pa.pushStyle();		
		//pa.noStroke();
		//pa.sphereDetail(ptDet);
		for(myPointf pt : objSamplePts){
			//pt.drawMeMyLocClr_BMU(pa);
			
//			pa.pushMatrix(); pa.pushStyle();
//			pa.fill(pt.locClrs[0],pt.locClrs[1],pt.locClrs[2], pt.locClrs[3]);
//			pa.stroke(pt.locClrs[0],pt.locClrs[1],pt.locClrs[2], pt.locClrs[3]);
//			
//			pa.translate(((Geom_SOMMapNode)(pt.getBmu())).worldLoc); 
//			pa.sphere(ptRad); 
//			pa.popStyle();pa.popMatrix();
		}
		pa.popStyle();pa.popMatrix();
	}
	
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
	 * column header data for saving preprocessed sample data of a particular type
	 * @return
	 */
	public final String toCSVStr_Header() {	
		String objtypeStr = this.objGeomType.toString();
		String res = "Owning " + objtypeStr + " ID, ";
		for(int i=0;i<this.srcPts.length;++i) {	res += objtypeStr+" pt "+i+".x, "+objtypeStr+" pt "+i+".y, "+objtypeStr+" pt "+i+".z, ";}
		return res;
	}
	
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
