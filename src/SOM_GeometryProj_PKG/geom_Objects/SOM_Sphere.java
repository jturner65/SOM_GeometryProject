package SOM_GeometryProj_PKG.geom_Objects;

import java.util.concurrent.ThreadLocalRandom;

import SOM_GeometryProj_PKG.geom_SOM_Examples.Geom_SphereSOMExample;
import SOM_GeometryProj_PKG.som_geom.SOM_GeomMapManager;
import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomExample;
import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomMapNode;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomObj;
import base_UI_Objects.my_procApplet;
import base_Utils_Objects.MyMathUtils;
import base_Utils_Objects.vectorObjs.myPointf;
import base_Utils_Objects.vectorObjs.myVectorf;

/**
 * class to hold center location, radius, color and samples of sphere used to train SOM
 * @author john
 *
 */
public class SOM_Sphere extends SOM_GeomObj{
	private static int IDGen = 0;
	/**
	 * center location
	 */
	public final myPointf ctrLoc;
	
	public final int sphrDet;
	public final float rad;
	/**
	 * coordinate bounds in world for sphere - static across all sphere objects
	 * 		first idx : 0 is min; 1 is diff
	 * 		2nd idx : 0 is x, 1 is y, 2 is z
	 */
	protected static float[][] worldBounds=null;

	public SOM_Sphere(SOM_GeomMapManager _mapMgr, myPointf[] _ptsOnSurface, myPointf _ctr, float _rad, int _numSmplPts, float[][] _worldBounds) {
		super(_mapMgr,_ptsOnSurface, _worldBounds, 4);
		setID(IDGen++);
		
		//TODO build center from non-planar points - center will be point equi-distant from all given points
		//with 4 srcPts, find center of sphere
		String res = "";
		for(int i=0;i<srcPts.length;++i) {res += srcPts[i].toStrBrf() + "; ";	}
      	//msgObj.dispInfoMessage("SOM_Sphere", "ctor", "\nUsed Ctr : " + _ctr.toStrBrf() + " rad : "  + _rad +" | Find center and radius of sphere with pts : " + res);
      	ctrLoc = new myPointf();
      	rad = findCenterAndRadFromPtsUsingDet(srcPts, ctrLoc);
		//ctrLoc = findCtrOfSphereFrom4Pts(srcPts);
	
		//radius is center to one of the points
		//rad = ctrLoc._dist(srcPts[0]);
      	float distFromGiven = ctrLoc._SqrDist(_ctr);
		if(!(distFromGiven < 1.0f)) {
			msgObj.dispErrorMessage("SOM_Sphere", "ctor", "Error : calculated center : " + ctrLoc.toStrBrf() +" != original center : " + _ctr.toStrBrf() + " | calced rad : " + rad + " | Given radius : " + _rad + " Dist : " + distFromGiven);
		}
						
		sphrDet = (int)(Math.sqrt(rad) + 10);	
		
		super.buildLocClrInitObjAndSamples(ctrLoc, _numSmplPts);
	}//ctor
	
	private String buildPtDispString(myPointf[] pts) {
		String res = "";
		for(int i=0;i<pts.length;++i) {res += pts[i].toStrBrf() + "; ";	}
		return res;
	}
	
	 /**
     * Find the center of radius of the sphere described by the passed 4 non-coplanar points.
     * NOTE it is assumed the points are not coplanar
     * @param pts 4 non-coplanar points to find center and radius of
     * @return _ctr destination to put center location
     */
    public myPointf findCtrOfSphereFrom4Pts(myPointf[] pts) {
    	myPointf ctr1 = new myPointf(), ctr2 = new myPointf();
    	myVectorf norm1 = new myVectorf(), norm2=new myVectorf();
    	myPointf[] pt1Ara = new myPointf[] {pts[0],pts[1],pts[2]};
    	myPointf[] pt2Ara = new myPointf[] {pts[1],pts[2],pts[3]};    	
      	findCtrAndNormOfCircleFrom3Points(pt1Ara, ctr1, norm1);
      	msgObj.dispInfoMessage("SOM_Sphere", "findCtrOfSphereFrom4Pts", "---------Circle Pts : " + buildPtDispString(pt1Ara) +" | Center  : " +ctr1.toStrBrf()+" | Norm : " + norm1.toStrBrf());
      	
      	
      	findCtrAndNormOfCircleFrom3Points(pt2Ara, ctr2, norm2);
      	msgObj.dispInfoMessage("SOM_Sphere", "findCtrOfSphereFrom4Pts", "---------Circle Pts : " + buildPtDispString(pt2Ara) +" | Center  : " +ctr2.toStrBrf()+" | Norm : " + norm2.toStrBrf());
    	//ctr 1 is center of inscribed circle in plane determined by norm1
      	//ctr 2 is center of incsribed circle in plane determined by norm2
      	//find intersection of these two point-ray lines, this will be center of circle
    	myPointf ctr = intersectTwoLines(ctr1, norm1,ctr2,norm2);
    	return ctr;
    }
    
    /**
     * build determinants of minors for circle calc, using method of Determinants - Thanks Again Paul Bourke!! http://paulbourke.net/geometry/circlesphere/
     * @param pts
     * @return
     */    
    private float findCenterAndRadFromPtsUsingDet(myPointf[] pts, myPointf _ctr){
    	float[][] ptsAsAra = new float[pts.length][];
    	float[] ptSqMags = new float[pts.length];
    	for(int i=0;i<ptsAsAra.length;++i) {
    		ptsAsAra[i] = pts[i].asHAraPt(); 
    		ptSqMags[i] = 0.0f;
    		for(int j=0;j<ptsAsAra[i].length-1; ++j) {ptSqMags[i] += ptsAsAra[i][j]*ptsAsAra[i][j];}//don't add 1 from homogeneous eqs
    	}//homogeneous 
    	//row minor
    	float[][][] Minors = new float[5][][];
    	for(int i=0;i<Minors.length;++i) {					//copy all homogeneous point values into each minor
    		float[][] tmpMinor = new float[4][];
    		for(int p=0;p<ptsAsAra.length;++p) {			//per point
    			float[] tmpRow = new float[4];
    			for(int d=0;d<tmpRow.length;++d) {			tmpRow[d]=ptsAsAra[p][d];		}   // per dof			
    			tmpMinor[p]=tmpRow;						//row/col
    		}
    		Minors[i]=tmpMinor;
    	}
    	//Minor 11 is done (idx 0)
    	//set minor 1->5 first value properly - every minor has mag of each point as first col
    	for(int i=1;i<Minors.length;++i) {
    		float[][] tmpMinor = Minors[i];
    		for(int row=0;row<tmpMinor.length;++row) {	tmpMinor[row][0] = ptSqMags[row];}
    	}
    	//Minor 12 is done (idx 1) (mag y z 1 per row) - all rest of minors have x_i as 2nd col value
    	for(int i=2;i<Minors.length;++i) {
    		float[][] tmpMinor = Minors[i];
    		for(int row=0;row<tmpMinor.length;++row) {	tmpMinor[row][1] = ptsAsAra[row][0];}   		
    	}
    	//Minor 13 is done (idx 2) (mag x z 1 per row)
    	for(int i=3;i<Minors.length;++i) {
    		float[][] tmpMinor = Minors[i];
    		for(int row=0;row<tmpMinor.length;++row) {	tmpMinor[row][2] = ptsAsAra[row][1];}   		
    	}
    	//Minor 14 is done (idx 3) (mag x y 1 per row) 
    	//Minor 15 doesn't have 1 in final location
    	for(int row=0;row<Minors[4].length;++row) {	Minors[4][row][3] = ptsAsAra[row][2];}  
    	
    	double[] dets = new double[5];
   
    	for(int idx = 0; idx<dets.length; ++idx) { 		dets[idx]= MyMathUtils.detMat(Minors[idx]);}
    	
    	_ctr.set(.5f*(dets[1]/dets[0]), -.5f *(dets[2]/dets[0]), .5f*(dets[3]/dets[0]));
    	
    	float rad = (float) Math.sqrt(_ctr._SqrDist(myPointf.ZEROPT) -(dets[4]/dets[0]));
    	return rad;
    }//findCenterAndRadFromPtsUsingDet
    
    /**
     * derive center point and planar norm of circle inscribed within 3 points (must be non-coplanar)
     * @param pts 3 points to find circle of
     * @param ctr center of circle - populated in here
     * @param cNorm planar norm of circle - populated in here
     */
    public void findCtrAndNormOfCircleFrom3Points(myPointf[] pts, myPointf ctr, myVectorf cNorm) {
    	myVectorf[] edgeDir = new myVectorf[pts.length];
    	myPointf[] midPoints = new myPointf[pts.length];
    	int stIdx, endIdx;
    	for(int i=0;i<edgeDir.length;++i) {
    		stIdx = i;
    		endIdx = (i+1) % edgeDir.length;
    		midPoints[stIdx] = myPointf._average(pts[stIdx], pts[endIdx]); 
    		edgeDir[stIdx] = new myVectorf(pts[stIdx], pts[endIdx]);   	
    		//msgObj.dispInfoMessage("SOM_Sphere", "findCtrAndNormOfCircleFrom3Points", "\t             Edge between " +pts[stIdx].toStrBrf()+" and " + pts[endIdx].toStrBrf()+ " = " +edgeDir[i].toStrBrf());
    		edgeDir[stIdx]._normalize();
    	}
    	//norm to plane
    	cNorm.set(edgeDir[0]._cross(edgeDir[1])._normalize());
  		//msgObj.dispInfoMessage("SOM_Sphere", "findCtrAndNormOfCircleFrom3Points", "\t             Normal to plane " +cNorm.toStrBrf());
  		    	
    	//bisectors are lines through midPoints ortho to edges
    	myVectorf[] biNorms = new myVectorf[3];
    	for(int i=0;i<biNorms.length;++i) {		biNorms[i]=cNorm._cross(edgeDir[i])._normalize();   	}
    	//now find intersection point of first 2 bisectors == mp[i] + t * bi[i]
    	ctr.set(intersectTwoLines(midPoints[0], biNorms[0],midPoints[1],biNorms[1]));
    }//
    
	
	/**
	 * Determine the point of intersection between two point-dir-form lines
	 * @param a origin point for first line
	 * @param aDir direction of first line
	 * @param b origin point for 2nd line
	 * @param bDir direction of 2nd line
	 * @return point of intersection for these two lines
	 */
	public myPointf intersectTwoLines(myPointf a, myVectorf aDir, myPointf b, myVectorf bDir) {
		
		float[] aPtAra = a.asArray(),aDirAra = aDir.asArray(), bPtAra = b.asArray(), bDirAra = bDir.asArray();
		int sIDX = -1, tIDX = -1;
		for(int i=0;i<aDirAra.length; ++i) {	if(aDirAra[i] != 0.0f) {sIDX = i; break;}}
		if(sIDX == -1) {//bad, means dir is 0; if constructed properly should never happen
			msgObj.dispErrorMessage("SOM_Sphere", "intersectTwoLines", "Somehow direction vector a has no non-zero elements");
		}
		for(int i=0;i<bDirAra.length; ++i) {	if((bDirAra[i] != 0.0f) && (i != sIDX)) {tIDX = i; break;}}		
		if(tIDX == -1) {//bad, means dir is 0; if constructed properly should never happen
			msgObj.dispErrorMessage("SOM_Sphere", "intersectTwoLines", "Somehow direction vector b has no non-zero elements");
		}
		float adS_bdT = (aDirAra[sIDX] * bDirAra[tIDX]);
		float d = 1.0f - ((aDirAra[tIDX] * bDirAra[sIDX])/adS_bdT);
		float t0 = (aPtAra[tIDX]-bPtAra[tIDX])/bDirAra[tIDX];
		float t1 = ((bPtAra[sIDX]-aPtAra[sIDX]) * (aDirAra[tIDX]))/adS_bdT;
		float t = (t0 + t1)/d;		
		myPointf res = myPointf._add(a, t, aDir);
		
		
		return res;
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

	
	private final myPointf getRandPosOnSphere(double rad, myPointf ctr){
		myPointf pos = new myPointf();
		double 	cosTheta = ThreadLocalRandom.current().nextDouble(-1,1), sinTheta =  Math.sin(Math.acos(cosTheta)),
				phi = ThreadLocalRandom.current().nextDouble(0,MyMathUtils.twoPi_f);
		pos.set(sinTheta * Math.cos(phi), sinTheta * Math.sin(phi),cosTheta);
		pos._mult((float) rad);
		pos._add(ctr);
		return pos;
	}//getRandPosOnSphere
	
	/**
	 * calculate the bounds on s and t (if appropriate) for parametric formulation of object equation
	 * idx 0 : 0==min, 1==diff
	 * idx 1 : 0==t, 1==s, 
	 * @return
	 */
	protected final float[][] calcTBounds(){
		float[][] res = new float[][] {{0.0f,0.0f},{0.0f,0.0f}};
		
		return res;
	}

	/**
	 * build this object's "exemplar" example
	 */
	@Override
	protected SOM_GeomExample buildObjExample() {			return new Geom_SphereSOMExample(mapMgr, "Sphere "+ getID(), buildSrcObjDataAra(false), worldBounds);}

	/**
	 * get an appropriate sample location to build sample sets, based on what kind of object is being built
	 * @return
	 */
	@Override
	protected final SOM_GeomExample buildSample(int idx) {	return new Geom_SphereSOMExample(mapMgr, "Sphr "+ getID() + " Smpl "+idx, buildSrcObjDataAra(true), worldBounds );}
	
	/**
	 * return a random point on this object
	 */
	@Override
	public final myPointf getRandPointOnObj() {return getRandPosOnSphere(rad,ctrLoc);}
	
	@Override
	protected void _drawMe_Geom(my_procApplet pa) {
		pa.pushMatrix();pa.pushStyle();	
		pa.sphereDetail(sphrDet);
		pa.translate(ctrLoc.x,ctrLoc.y,ctrLoc.z); 
		pa.sphere(rad); 
		pa.popStyle();pa.popMatrix();	
	}
		
	@Override
	protected void _drawMe_Geom_BMU(my_procApplet pa) {
		pa.pushMatrix();pa.pushStyle();	
		pa.sphereDetail(sphrDet);
		pa.translate(baseObjBMUWorldLoc.x,baseObjBMUWorldLoc.y,baseObjBMUWorldLoc.z); 
		pa.sphere(rad); 
		pa.popStyle();pa.popMatrix();	
	}

	@Override
	protected final void _drawMe_Geom_Lbl(my_procApplet pa) {
		
		
	}
	
	
	protected static float modCnt = 0;//counter that will determine when the color should switch
	
	@Override
	public void drawMeSelected(my_procApplet pa,float animTmMod){//animTmMod is time since last frame
		modCnt += animTmMod;
		if(modCnt > 1.0){	modCnt = 0;	}//blink every ~second
		pa.pushMatrix();pa.pushStyle();		
		pa.noFill();//fill(255*modCnt,255);
		pa.stroke(255*modCnt, 255);		
		pa.translate(ctrLoc); 
		pa.sphere(rad*(modCnt + 1.0f)); 
		pa.popStyle();pa.popMatrix();
	}
	
	
	@Override
	public void drawMeSelected_BMU(my_procApplet pa,float animTmMod){//animTmMod is time since last frame
		modCnt += animTmMod;
		if(modCnt > 1.0){	modCnt = 0;	}//blink every ~second
		pa.pushMatrix();pa.pushStyle();		
		pa.noFill();
		pa.stroke(255*modCnt, 255);		
		pa.translate(baseObjBMUWorldLoc); 
		pa.sphere(rad*(modCnt + 1.0f)); 
		pa.popStyle();pa.popMatrix();
	}	
	
	
	@Override
	public void drawMyLabel(my_procApplet pa){
		pa.pushMatrix();pa.pushStyle();		
		pa.setColorValFill(0,255); 
		pa.setColorValStroke(0,255);		
		pa.translate(ctrLoc); 
		pa.unSetCamOrient_Glbl();
		pa.scale(.75f);
		pa.text(""+getID(), rad,-rad,0); 
		pa.popStyle();pa.popMatrix();
	}

	

///////////////BMU / datapoint drawing
	
	@Override
	public void drawMeLabel_BMU(my_procApplet pa){
		pa.pushMatrix();pa.pushStyle();		
		pa.setColorValFill(0,255); 
		pa.setColorValStroke(0,255);		
		pa.translate(((SOM_GeomMapNode)(objExample.getBmu())).mapLoc); 
		pa.unSetCamOrient_Glbl();
		pa.scale(.75f);
		pa.text(""+getID(), rad,-rad,0); 
		pa.popStyle();pa.popMatrix();
	}



}//mySOMSphere
