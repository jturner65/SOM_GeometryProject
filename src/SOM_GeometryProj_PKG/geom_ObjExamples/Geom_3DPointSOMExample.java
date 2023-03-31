package SOM_GeometryProj_PKG.geom_ObjExamples;

import java.util.TreeMap;

import base_Math_Objects.vectorObjs.floats.myPointf;
import base_Render_Interface.IRenderInterface;
import base_SOM_Objects.som_examples.enums.SOM_ExDataType;
import base_SOM_Objects.som_geom.SOM_GeomMapManager;
import base_SOM_Objects.som_geom.geom_UI.SOM_AnimWorldWin;
import base_SOM_Objects.som_geom.geom_examples.SOM_GeomMapNode;
import base_SOM_Objects.som_geom.geom_examples.SOM_GeomObj;
import base_SOM_Objects.som_geom.geom_utils.geom_objs.SOM_GeomObjDrawType;
import base_SOM_Objects.som_geom.geom_utils.geom_objs.SOM_GeomObjTypes;
import base_SOM_Objects.som_geom.geom_utils.geom_objs.SOM_GeomSamplePointf;

public class Geom_3DPointSOMExample extends SOM_GeomObj {
	private static int IDGen = 0;
	/**
	 * string array denoting names of features
	 */
	public static final String[] ftrNames = {"x","y","z"};
	/**
	 * feature vector size for this object : 3d point
	 */
	public static final int _numFtrs = ftrNames.length;
	/**
	 * point location
	 */
	private myPointf location;
	/**
	 * # of source points used to build object
	 */
	public static final int _numSrcPts = 1;
	
	/**
	 * Constructor for a point object to render and use as a training example
	 * @param _mapMgr : owning map manager
	 * @param _exType : whether this is training/testing/validation etc.
	 * @param _oid : pre-defined string ID put in SOM_Example OID field
	 * @param _srcSmpls : the points and owning objects that make up the minimally defining set of points for this object.  If src objects are null, then this object is a foundation/source object
	 * @param _numSmplPts : # of sample points to build for this object TODO move to post-construction process
	 * @param _shouldBuildSamples : whether we should build samples for this object
	 * @param _shouldBuildVisRep : whether we should pre-build a mesh representation of this object
	 */
	public Geom_3DPointSOMExample(SOM_GeomMapManager _mapMgr, SOM_ExDataType _exType, String _oid,SOM_GeomSamplePointf[] _srcSmpls, int _numSmplPts, boolean _shouldBuildSamples, boolean _shouldBuildVisRep) {
		super(_mapMgr, _exType, _oid, _srcSmpls, SOM_GeomObjTypes.point, _numSmplPts, true, _shouldBuildSamples, _shouldBuildVisRep);
	}
	/**
	 * Constructor to built a point based on csv data
	 * @param _mapMgr : owning map manager
	 * @param _exType : whether this is training/testing/validation etc.
	 * @param _oid : pre-defined string ID put in SOM_Example OID field
	 * @param _csvDat : String from CSV describing object
	 */
	public Geom_3DPointSOMExample(SOM_GeomMapManager _mapMgr, SOM_ExDataType _exType, String _oid, String _csvDat) {
		super(_mapMgr, _exType, _oid, _csvDat, SOM_GeomObjTypes.point, _numSrcPts, true);
	}
	
	/**
	 * ctor to build object corresponding to bmu's geometric object
	 * @param _mapMgr : owning map manager
	 * @param _mapNode : the map node being represented
	 */
	public Geom_3DPointSOMExample(SOM_GeomMapManager _mapMgr, SOM_GeomMapNode _mapNode) {
		super(_mapMgr, _mapNode, SOM_GeomObjTypes.point, true);
	}//ctor from bmu

	public Geom_3DPointSOMExample(Geom_3DPointSOMExample _otr) {
		super(_otr);
		location = _otr.location;
	}//copy ctor

	@Override
	protected void initObjVals(int _numSmplPts) {
		location = new myPointf(getSrcPts()[0]);
		super.buildLocClrInitObjAndSamples(location, _numSmplPts);
	}

	@Override
	protected void initObjValsFromCSV(String _csvDat) {
		location = new myPointf(getSrcPts()[0]);
		super.buildLocClrAndSamplesFromCSVStr(location, _csvDat);
	}

	@Override
	protected void initObjValsForMapNode(SOM_GeomMapNode _mapNode) {
		location = new myPointf(getSrcPts()[0]);
		super.buildLocClrInitObjAndSamples(location, _numSrcPts);	
	}

	@Override
	protected SOM_GeomSamplePointf[] buildSrcPtsFromBMUMapNodeFtrs(float[] mapFtrs, String _dispLabel) {
		SOM_GeomSamplePointf[] res = new SOM_GeomSamplePointf[_numSrcPts];
		res[0] = new SOM_GeomSamplePointf(mapFtrs[0],mapFtrs[1],mapFtrs[2], _dispLabel+"_BMU_pt_0", this);		
		return res;
	}

	@Override
	public myPointf getRandPointOnObj() {return new myPointf(location);}
	
	/**
	 * object shape-specific feature building - ftrVecMag calced in base class
	 */
	@Override
	protected void buildFeaturesMap_Indiv() {
		//set point location as features
		ftrMaps[unNormFtrMapTypeKey].put(0,location.x);
		ftrMaps[unNormFtrMapTypeKey].put(1,location.y);
		ftrMaps[unNormFtrMapTypeKey].put(2,location.z);
	}//buildFeaturesMap_Indiv
	
	/**
	 * Instance-class specific required info for this example to build feature data - use 
	 * this so we don't have to reload and rebuilt from data every time
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
	public TreeMap<Integer, Integer> getTrainingLabels() {
		TreeMap<Integer, Integer> res = new TreeMap<Integer, Integer>();
		// TODO Auto-generated method stub
		return res;
	}
	
	///////////////////////////
	// draw functionality
	@Override
	public void drawMyLabel(IRenderInterface ri, SOM_AnimWorldWin animWin) {
		ri.pushMatState();	
		ri.setFill(labelClrAra, 255);
		ri.setStroke(labelClrAra, 255);
		ri.setStrokeWt(2.0f);
		_drawLabelAtLoc_3D(ri, location, animWin, dispLabel + " loc : " + location.toStrBrf(), 1.5f, 2.5f);
		ri.popMatState();
	}//drawMyLabel

	@Override
	protected void _drawMe_Geom(IRenderInterface ri, SOM_GeomObjDrawType drawType) {
		ri.pushMatState();	
		ri.drawSphere(location, 0.1f, 3);
		ri.popMatState();	
	}
	protected float modCnt = 0;//counter that will determine when the color should switch
	@Override
	protected void _drawMeSelected(IRenderInterface ri, float animTmMod) {
		modCnt += animTmMod;
		if(modCnt > 1.0){	modCnt = 0;	}//blink every ~second
		ri.pushMatState();	
		ri.noFill();//fill(255*modCnt,255);
		int _v = (int) (255*modCnt);		
		ri.setStroke(_v,_v,_v, 255);		
		ri.translate(location); 
		ri.drawSphere(0.1f*(modCnt + 1.0f)); 
		ri.popMatState();	
	}

	/**
	 * initialize object's ID
	 */
	@Override
	protected final int incrID() {return IDGen++;}
}//class Geom_3DPointSOMExample
