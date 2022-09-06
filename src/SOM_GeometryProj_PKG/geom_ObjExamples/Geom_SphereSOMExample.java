package SOM_GeometryProj_PKG.geom_ObjExamples;

import java.util.TreeMap;

import SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers.Geom_SphereMapMgr;
import base_JavaProjTools_IRender.base_Render_Interface.IRenderInterface;
import base_Math_Objects.vectorObjs.floats.myPointf;
import base_SOM_Objects.som_examples.SOM_ExDataType;
import base_SOM_Objects.som_geom.geom_UI.SOM_AnimWorldWin;
import base_SOM_Objects.som_geom.geom_examples.SOM_GeomMapNode;
import base_SOM_Objects.som_geom.geom_examples.SOM_GeomObj;
import base_SOM_Objects.som_geom.geom_utils.geom_objs.SOM_GeomObjDrawType;
import base_SOM_Objects.som_geom.geom_utils.geom_objs.SOM_GeomObjTypes;
import base_SOM_Objects.som_geom.geom_utils.geom_objs.SOM_GeomSamplePointf;

/**
 * class to hold center location, radius, color and samples of sphere used to train SOM
 * @author john
 *
 */
public class Geom_SphereSOMExample extends SOM_GeomObj{
	private static int IDGen = 0;
	/**
	 * string array denoting names of features
	 */
	public static final String[] ftrNames = {"Center x","Center y","Center z","Radius"};
	/**
	 * feature vector size for this object : 3d point + 3d line
	 */
	public static final int _numFtrs = ftrNames.length;
	
	/**
	 * # of source points used to build object
	 */
	public static final int _numSrcPts = 4;

	/**
	 * center location
	 */
	public final myPointf ctrLoc;
	
	public final int sphrDet;
	public final float radius;
	/**
	 * coordinate bounds in world for sphere - static across all sphere objects
	 * 		first idx : 0 is min; 1 is diff
	 * 		2nd idx : 0 is x, 1 is y, 2 is z
	 */
	protected static float[][] worldBounds=null;
	
	private static final String csvSphereTag = "SPHR,";
	
	/**
	 * build a sphere object to render and use as a training example, 
	 * @param _mapMgr : owning map manager
	 * @param _animWin : owning display window of 
	 * @param _exType : whether this is training/testing/validation etc.
	 * @param _id : pre-defined string ID put in SOM_Example OID field
	 * @param _srcSmpls : the points and owning objects that make up the minimally defining set of points for this object.  If src objects are null, then this object is a foundation/source object
	 * @param _numSmplPts : # of sample points to build for this object TODO move to post-construction process
	 * @param _worldBounds : bounds within which the points/samples of this object should remain constrained
	 */
	public Geom_SphereSOMExample(Geom_SphereMapMgr _mapMgr, SOM_ExDataType _exType, String _id, SOM_GeomSamplePointf[] _srcSmpls, int _numSmplPts, boolean _shouldBuildSamples, boolean _shouldBuildVisRep) {
		super(_mapMgr, _exType, _id, _srcSmpls,  SOM_GeomObjTypes.sphere, true,_shouldBuildSamples);
		//with 4 srcPts, find center of sphere
		//String res = "";
		//for(int i=0;i<srcPts.length;++i) {res += srcPts[i].toStrBrf() + "; ";	}
      	//msgObj.dispInfoMessage("SOM_Sphere", "ctor", "\nUsed Ctr : " + _ctr.toStrBrf() + " radius : "  + _rad +" | Find center and radius of sphere with pts : " + res);
      	ctrLoc = new myPointf();
      	radius = (float) findCenterAndRadFromPtsUsingDet(getSrcPts(), ctrLoc);
//      	if(!_shouldBuildSamples) {
//      		msgObj.dispInfoMessage("SOM_Sphere", "ctor", "Used ctrLoc : " + ctrLoc.toStrBrf() + " radius : "  + radius);
//      	}
      	sphrDet = (int)(Math.sqrt(radius) + 10);	
		//ctrLoc = findCtrOfSphereFrom4Pts(srcPts);
		super.buildLocClrInitObjAndSamples(ctrLoc, _numSmplPts);
	}//ctor
	
	public Geom_SphereSOMExample(Geom_SphereMapMgr _mapMgr, SOM_ExDataType _exType, String _oid, String _csvDat) {
		super(_mapMgr, _exType, _oid, _csvDat,  SOM_GeomObjTypes.sphere, _numSrcPts, true);
		ctrLoc = new myPointf();
		
		radius = buildCenterAndRadFromCSVStr(ctrLoc, _csvDat);
     	sphrDet = (int)(Math.sqrt(radius) + 10);	
		super.buildLocClrAndSamplesFromCSVStr(ctrLoc, _csvDat);
	}
	
	/**
	 * ctor to build object corresponding to bmu geometric object
	 * @param _mapMgr
	 * @param _mapNode
	 */
	public Geom_SphereSOMExample(Geom_SphereMapMgr _mapMgr, SOM_GeomMapNode _mapNode) {
		super(_mapMgr, _mapNode, SOM_GeomObjTypes.sphere, true);
		//with 4 srcPts, find center of sphere
		//String res = "";
		//for(int i=0;i<srcPts.length;++i) {res += srcPts[i].toStrBrf() + "; ";	}
      	//msgObj.dispInfoMessage("SOM_Sphere", "ctor", "\nUsed Ctr : " + _ctr.toStrBrf() + " radius : "  + _rad +" | Find center and radius of sphere with pts : " + res);
      	ctrLoc = new myPointf();
      	radius = (float) findCenterAndRadFromPtsUsingDet(getSrcPts(), ctrLoc);		
      	sphrDet = (int)(Math.sqrt(radius) + 10);	
		//ctrLoc = findCtrOfSphereFrom4Pts(srcPts);
		super.buildLocClrInitObjAndSamples(ctrLoc, _numSrcPts);		
	}
	
	@SuppressWarnings("static-access")
	public Geom_SphereSOMExample(Geom_SphereSOMExample _otr) {
		super(_otr);
		ctrLoc = _otr.ctrLoc;
		radius = _otr.radius;
		sphrDet = _otr.sphrDet;
		worldBounds = _otr.worldBounds;
	}//copy ctor

	/**
	 * test whether center and radius derived from the 4 source points 
	 * for this sphere are similar enough to the values used to derive 
	 * those 4 source points, within the passed tolerance
	 * @param _ctr center of sphere used to derive source points
	 * @param _rad radius of sphere used to derive source points
	 */
	public final boolean testSphereConstruction(myPointf _ctr, float _rad, float tol) {
      	float sqDistFromGiven = ctrLoc._SqrDist(_ctr);
		if(!(sqDistFromGiven < tol)) {
			msgObj.dispWarningMessage("SOM_Sphere", "testSphereConstruction", "Warning : calculated center : " + ctrLoc.toStrBrf() +" != original center : " + _ctr.toStrBrf() + " | calced rad : " + radius + " | Given radius : " + _rad + " Sq Dist : " + sqDistFromGiven);
			return false;
		}
		return true;
	}//testSphereConstruction
	
    /**
     * build determinants of minors for circle calc, using method of Determinants - 
     * Thanks Again Paul Bourke!! http://paulbourke.net/geometry/circlesphere/
     * @param pts 4 points of sphere
     * @param _ctr destination for derived center point
     * @return calculated radius
     */    
    private double findCenterAndRadFromPtsUsingDet(myPointf[] pts, myPointf _ctr){
    	double[][] ptsAsAra = new double[pts.length][];
    	double[] ptSqMags = new double[pts.length];
    	for(int i=0;i<ptsAsAra.length;++i) {
    		ptsAsAra[i] = pts[i].asHAraPt_Dbl(); 
    		ptSqMags[i] = 0.0f;
    		for(int j=0;j<ptsAsAra[i].length-1; ++j) {ptSqMags[i] += ptsAsAra[i][j]*ptsAsAra[i][j];}//don't add 1 from homogeneous eqs
    	}//homogeneous     	
    	
    	//row minor
    	double[][][] Minors = new double[5][][];
    	for(int i=0;i<Minors.length;++i) {					//copy all homogeneous point values into each minor
    		double[][] tmpMinor = new double[4][];
    		for(int p=0;p<ptsAsAra.length;++p) {			//per point
    			double[] tmpRow = new double[4];
    			for(int d=0;d<tmpRow.length;++d) {			tmpRow[d]=ptsAsAra[p][d];		}   // per dof			
    			tmpMinor[p]=tmpRow;						//row/col
    		}
    		Minors[i]=tmpMinor;
    	}
    	//Minor 11 is done (idx 0)
    	double[][] tmpMinor;
    	//set minor 1->5 first value properly - every minor has mag of each point as first col
    	for(int i=1;i<Minors.length;++i) {
    		tmpMinor = Minors[i];
    		for(int row=0;row<tmpMinor.length;++row) {	tmpMinor[row][0] = ptSqMags[row];}
    	}
    	//Minor 12 is done (idx 1) (mag y z 1 per row) - all rest of minors have x_i as 2nd col value
    	for(int i=2;i<Minors.length;++i) {
    		tmpMinor = Minors[i];
    		for(int row=0;row<tmpMinor.length;++row) {	tmpMinor[row][1] = ptsAsAra[row][0];}   		
    	}
    	//Minor 13 is done (idx 2) (mag x z 1 per row)
    	for(int i=3;i<Minors.length;++i) {
    		tmpMinor = Minors[i];
    		for(int row=0;row<tmpMinor.length;++row) {	tmpMinor[row][2] = ptsAsAra[row][1];}   		
    	}
    	//Minor 14 is done (idx 3) (mag x y 1 per row) 
    	//Minor 15 doesn't have 1 in final location
    	for(int row=0;row<Minors[4].length;++row) {	Minors[4][row][3] = ptsAsAra[row][2];}  
    	
    	double[] dets = new double[5];
   
    	for(int idx = 0; idx<dets.length; ++idx) { 		dets[idx]= detMat(Minors[idx]);} 
    	
    	_ctr.set(.5f*(dets[1]/dets[0]), -.5f *(dets[2]/dets[0]), .5f*(dets[3]/dets[0]));
    	
    	double rad = Math.sqrt(_ctr._SqrDist(myPointf.ZEROPT) -(dets[4]/dets[0]));
    	return rad;
    }//findCenterAndRadFromPtsUsingDet

	/**
	 * calculates the determinant of a Matrix - moved from MyMathUtils for threading
	 * @param M n x n matrix - don't over do it
	 * @return
	 */
	private double detMat(double[][] M){ 
		double sum=0, s;
		if(M.length==1){	return(M[0][0]); }
		for(int i=0;i < M.length;i++){ 														
			double[][] minor= new double[M.length-1][M.length-1];
			for(int b=0;b<M.length;++b){
				if(b==i) {continue;}
				int bIdx = (b<i)? b : b-1;
				for(int a=1;a<M.length;++a){			minor[a-1][bIdx] = M[a][b];	}
			}	
			s = (i%2==0) ? 1.0f : -1.0f;
			sum += s * M[0][i] * (detMat(minor)); 										
		}
		return(sum); //returns determinant value. once stack is finished, returns final determinant.
	}//detMat
	
	private final float buildCenterAndRadFromCSVStr(myPointf _ctr, String _csvDat) {
		String[] datAra = _csvDat.trim().split(csvSphereTag);
		//idx 1 is sphere data
		String[] ptsAsStr = datAra[1].trim().split(",");
		_ctr.set(Float.parseFloat(ptsAsStr[1].trim()),Float.parseFloat(ptsAsStr[2].trim()),Float.parseFloat(ptsAsStr[3].trim()));
		return Float.parseFloat(ptsAsStr[0].trim());
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
	 * return a random point on this object
	 */
	@Override
	public final myPointf getRandPointOnObj() {return getRandPosOnSphere(radius,ctrLoc);}

	////////////////////////////
	// feature functionality (inherited from SOM_Example
	/**
	 * build an array of source points from the characteristic features of the source map node
	 */
	@Override
	protected final SOM_GeomSamplePointf[] buildSrcPtsFromBMUMapNodeFtrs(float[] mapFtrs, String _dispLabel) {
		SOM_GeomSamplePointf[] res = new SOM_GeomSamplePointf[_numSrcPts];
		myPointf _ctrLoc = new myPointf(mapFtrs[0],mapFtrs[1],mapFtrs[2]);
		float _rad = mapFtrs[3];
		myPointf[] pts = getRandSpherePoints(_rad, _ctrLoc);
		for(int i=0;i<res.length; ++i) {res[i] = new SOM_GeomSamplePointf(pts[i], _dispLabel+"_BMU_pt_"+i, this);}
		return res;			
	}//buildSrcPtsFromBMUMapNode
	
	
	/**
	 * object shape-specific feature building - ftrVecMag calced in base class
	 */
	@Override
	protected final void buildFeaturesMap_Indiv() {
		//set sphere center and radius as features
		ftrMaps[unNormFtrMapTypeKey].put(0,ctrLoc.x);
		ftrMaps[unNormFtrMapTypeKey].put(1,ctrLoc.y);
		ftrMaps[unNormFtrMapTypeKey].put(2,ctrLoc.z);
		ftrMaps[unNormFtrMapTypeKey].put(3,radius);
	}
	
	/**
	 * Instance-class specific column names of rawDescrForCSV data
	 * @return
	 */
	@Override
	protected String getRawDescColNamesForCSV_Indiv() {
		// TODO Auto-generated method stub
		return "TAG, radius, center x, center y, center z, TAG, ";
	}
	/**
	 * Instance-class specific required info for this example to build feature data - use this so we don't have to reload and rebuilt from data every time
	 * @return
	 */
	@Override
	protected final String getPreProcDescrForCSV_Indiv() {
		String res = csvSphereTag + String.format("%.8f", radius) + ", "+ctrLoc.toStrCSV("%.8f")+"," + csvSphereTag;
		return res;
	}

	@Override
	public final TreeMap<Integer, Integer> getTrainingLabels() {
		TreeMap<Integer, Integer> res = new TreeMap<Integer, Integer>();
		// TODO Auto-generated method stub
		return res;
	}

	
	///////////////////////////
	// draw functionality
	@Override
	protected void _drawMe_Geom(IRenderInterface pa, SOM_GeomObjDrawType drawType) {
		pa.pushMatState();	
		pa.drawSphere(ctrLoc, radius, sphrDet);
//		pa.sphereDetail(sphrDet);
//		pa.translate(ctrLoc.x,ctrLoc.y,ctrLoc.z); 
//		pa.sphere(radius); 
		pa.popMatState();	
	}

	protected float modCnt = 0;//counter that will determine when the color should switch
	
	@Override
	protected final void _drawMeSelected(IRenderInterface pa,float animTmMod){//animTmMod is time since last frame
		modCnt += animTmMod;
		if(modCnt > 1.0){	modCnt = 0;	}//blink every ~second
		pa.pushMatState();	
		pa.noFill();//fill(255*modCnt,255);
		int _v = (int) (255*modCnt);		
		pa.setStroke(_v,_v,_v, 255);		
		pa.translate(ctrLoc); 
		pa.drawSphere(radius*(modCnt + 1.0f)); 
		pa.popMatState();
	}
	
	@Override
	public void drawMyLabel(IRenderInterface pa, SOM_AnimWorldWin animWin){
		pa.pushMatState();		
		pa.setFill(this.labelClrAra, 255);
		pa.setStroke(this.labelClrAra, 255);		
		_drawLabelAtLoc_3D(pa, ctrLoc, animWin, dispLabel + " Origin : " + ctrLoc.toStrBrf() +" | Radius " + String.format("%.4f", radius), 1.0f, .71f*radius);
		pa.popMatState();
	}
	
	
	///////////////BMU / datapoint drawing

	/**
	 * initialize object's ID
	 */
	protected final int incrID() {return IDGen++;}



}//mySOMSphere
