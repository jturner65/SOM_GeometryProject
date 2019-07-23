package SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading.base;

import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

import SOM_GeometryProj_PKG.som_geom.SOM_GeomMapManager;
import SOM_GeometryProj_PKG.som_geom.geom_UI.SOM_AnimWorldWin;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomObj;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomSamplePointf;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomSmplDataForEx;
import base_Utils_Objects.MyMathUtils;
import base_Utils_Objects.io.MessageObject;
import base_Utils_Objects.vectorObjs.myPoint;
import base_Utils_Objects.vectorObjs.myPointf;
import base_Utils_Objects.vectorObjs.myVector;
import base_Utils_Objects.vectorObjs.myVectorf;

public abstract class SOM_GeomCallable implements Callable<Boolean> {
	/**
	 * owning map manager
	 */
	protected final SOM_GeomMapManager mapMgr;

	protected final String dataType;

	/**
	 * message object
	 */
	protected final MessageObject msgObj;
	/**
	 * start and end index in array of data, thread index, # of 
	 */
	protected final int stIdx, endIdx, thdIDX;	
	/**
	 * monitor progress
	 */
	protected int progressBnd;
	protected static final float progAmt = .2f;
	protected double progress = -progAmt;

	/**
	 * coordinate bounds in world 
	 * 		first idx : 0 is min; 1 is diff
	 * 		2nd idx : 0 is x, 1 is y, 2 is z
	 */
	protected final float[][] worldBounds;	

	
	public SOM_GeomCallable(SOM_GeomMapManager _mapMgr, int _stIdx, int _endIdx, int _thdIdx, String _dataType) {
		mapMgr=_mapMgr;
		dataType=_dataType;
		msgObj = mapMgr.buildMsgObj();
		worldBounds = mapMgr.getWorldBounds();		
		
		stIdx = _stIdx;
		endIdx = _endIdx;
		thdIDX= _thdIdx;
		progressBnd = (int) ((endIdx-stIdx) * progAmt);
	}

	
	protected final void incrProgress(int idx, String task) {
		if(((idx-stIdx) % progressBnd) == 0) {		
			progress += progAmt;	
			msgObj.dispInfoMessage("SOM_GeomCallable","incrProgress::thdIDX=" + String.format("%02d", thdIDX)+" ", "Progress performing " + task +" at : " + String.format("%.2f",progress));
		}
		if(progress > 1.0) {progress = 1.0;}
	}
	public final double getProgress() {	return progress;}
	
	/**
	 * determine how many work elements should be assigned per thread 
	 * @param numVals total number of work elements to execute
	 * @param numThds total number of threads available
	 * @return number of work elements per thread to assign
	 */
	public final int calcNumPerThd(int numVals, int numThds) {	return (int) ((numVals -1)/(1.0*numThds)) + 1;	}//calcNumPerThd
	
	
	/**
	 * build array of SOM_GeomSmplForSOMExample objects, which each holds a sample 
	 * point in anim space and the object it came from, or null if new
	 * @param objs the array of source objects, or null if creating new source objects
	 * @param pts the array of points
	 * @return
	 */
	protected SOM_GeomSmplDataForEx[] buildSrcGeomSmplAra(SOM_GeomObj[] objs, SOM_GeomSamplePointf[] pts) {
		SOM_GeomSmplDataForEx[] srcGeomData = new SOM_GeomSmplDataForEx[pts.length];
		if(null==objs) {//creation
			for(int i=0;i<pts.length;++i) {	srcGeomData[i] = new SOM_GeomSmplDataForEx(null, pts[i]);}
		} else {
			for(int i=0;i<pts.length;++i) {	srcGeomData[i] = new SOM_GeomSmplDataForEx(objs[i], pts[i]);}
		}
		return srcGeomData;
	}
	
	
	protected myPointf getRandPointInBounds_2D() {
		myPointf x = new myPointf( 
				(ThreadLocalRandom.current().nextFloat() *worldBounds[1][0])+worldBounds[0][0], 
				(ThreadLocalRandom.current().nextFloat() *worldBounds[1][1])+worldBounds[0][1],0);
		return x;
	}
	

	protected myPointf getRandPointInBounds_3D() {
		myPointf x = new myPointf( 
				(ThreadLocalRandom.current().nextFloat() *worldBounds[1][0])+worldBounds[0][0], 
				(ThreadLocalRandom.current().nextFloat() *worldBounds[1][1])+worldBounds[0][1],
				(ThreadLocalRandom.current().nextFloat() *worldBounds[1][2])+worldBounds[0][2]);
		return x;
	}
	
	protected myPointf getRandPointInBounds_3D(float bnd) {
		float tbnd = 2.0f*bnd;
		myPointf x = new myPointf( 
				(ThreadLocalRandom.current().nextFloat() *(worldBounds[1][0]-tbnd))+worldBounds[0][0]+bnd, 
				(ThreadLocalRandom.current().nextFloat() *(worldBounds[1][1]-tbnd))+worldBounds[0][1]+bnd,
				(ThreadLocalRandom.current().nextFloat() *(worldBounds[1][2]-tbnd))+worldBounds[0][2]+bnd);
		return x;
	}
	
	protected myPoint getRandPointInBounds_2D_Double() {
		myPoint x = new myPoint( 
				(ThreadLocalRandom.current().nextDouble() *worldBounds[1][0])+worldBounds[0][0], 
				(ThreadLocalRandom.current().nextDouble() *worldBounds[1][1])+worldBounds[0][1],0);
		return x;
	}
	

	protected myPoint getRandPointInBounds_3D_Double() {
		myPoint x = new myPoint( 
				(ThreadLocalRandom.current().nextDouble() *worldBounds[1][0])+worldBounds[0][0], 
				(ThreadLocalRandom.current().nextDouble() *worldBounds[1][1])+worldBounds[0][1],
				(ThreadLocalRandom.current().nextDouble() *worldBounds[1][2])+worldBounds[0][2]);
		return x;
	}
	
	protected myPoint getRandPointInBounds_3D_Double(double bnd) {
		double tbnd = 2.0*bnd;
		myPoint x = new myPoint( 
				(ThreadLocalRandom.current().nextDouble() *(worldBounds[1][0]-tbnd))+worldBounds[0][0]+bnd, 
				(ThreadLocalRandom.current().nextDouble() *(worldBounds[1][1]-tbnd))+worldBounds[0][1]+bnd,
				(ThreadLocalRandom.current().nextDouble() *(worldBounds[1][2]-tbnd))+worldBounds[0][2]+bnd);
		return x;
	}
	
	/**
	 * find an array of 3 non-colinear points 
	 * @return array of 3 non-colinear points
	 */
	protected myPointf[] getRandPlanePoints() {
		myPointf a = getRandPointInBounds_3D();
		myPointf b = getRandPointInBounds_3D();
		myPointf c;
		myVectorf ab = new myVectorf(a,b), ac;
		ab._normalize();				
		do {
			c = getRandPointInBounds_3D();
			ac = new myVectorf(a,c);
			ac._normalize();
		} while (Math.abs(ab._dot(ac))==1.0f);
		myPointf[] planePts = new myPointf[] {a,b,c};		
		return planePts;
	}
	
	/**
	 * find an array of 3 non-colinear points
	 * @return array of 3 non-colinear points
	 */
	protected myPoint[] getRandPlanePoints_Double() {
		myPoint a = getRandPointInBounds_3D_Double();
		myPoint b = getRandPointInBounds_3D_Double();
		myPoint c;
		myVector ab = new myVector(a,b), ac;
		ab._normalize();
		do {
			c = getRandPointInBounds_3D_Double();
			ac = new myVector(a,c);
			ac._normalize();
		} while (Math.abs(ab._dot(ac))==1.0f);
		myPoint[] planePts = new myPoint[] {a,b,c};		
		return planePts;
	}
	
	/**
	 * get random unit vector
	 * @return
	 */
	protected myVectorf getRandNormal_3D() {
		myVectorf x = new myVectorf( 
				(ThreadLocalRandom.current().nextFloat() *2.0f)-1.0f, 
				(ThreadLocalRandom.current().nextFloat() *2.0f)-1.0f,
				(ThreadLocalRandom.current().nextFloat() *2.0f)-1.0f);		
		return x._normalized();
	}
	
	protected myVector getRandNormal_3D_Double() {
		myVector x = new myVector( 
				(ThreadLocalRandom.current().nextDouble() *2.0f)-1.0f, 
				(ThreadLocalRandom.current().nextDouble() *2.0f)-1.0f,
				(ThreadLocalRandom.current().nextDouble() *2.0f)-1.0f);		
		return x._normalized();
	}
	
	/**
	 * get uniformly random position on sphere surface with passed radius and center
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
	
	protected final myPoint getRandPosOnSphere_Double(double rad, myPoint ctr){
		myPoint pos = new myPoint();
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
	protected final myPointf[] getRandSpherePoints(double rad, myPointf ctr){
		myPointf a = getRandPosOnSphere(rad, ctr), b = getRandPosOnSphere(rad, ctr);
		myPointf c,d;
		myVectorf ab = new myVectorf(a,b), ac, bc, ad;
		ab._normalize();
		int iter = 0;
		do {
			++iter;
			c = getRandPosOnSphere(rad, ctr);
			ac = new myVectorf(a,c);
			ac._normalize();
		} while (Math.abs(ab._dot(ac))==1.0f);		//if 1 or -1 then collinear
		//4th point needs to be non-coplanar - will guarantee that 
		//it is also not collinear with any pair of existing points
		//normal to abc plane
		myVectorf planeNorm = ab._cross(ac)._normalize();
		//now find d so that it does not line in plane of abc - vector from ab
		do {
			++iter;
			d = getRandPosOnSphere(rad, ctr);
			ad = new myVectorf(a,d);
			ad._normalize();
		} while (ad._dot(planeNorm) == 0.0f);//if 0 then in plane (ortho to normal)
		
		myPointf[] spherePts = new myPointf[] {a,b,c,d};		
		if(iter>2) {//check this doesn't take too long - most of the time should never take more than a single iteration through each do loop
			msgObj.dispInfoMessage("SOM_GeomObjBuilder","getRandSpherePoints::thdIDX=" + String.format("%02d", thdIDX)+" ", "Took Longer than 2 iterations to generate 4 points for sphere : " + iter);
			
		}
		return spherePts;
	}
	
	protected final myPoint[] getRandSpherePoints_Double(double rad, myPoint ctr){
		myPoint a = getRandPosOnSphere_Double(rad, ctr), b = getRandPosOnSphere_Double(rad, ctr);
		myPoint c,d;
		myVector ab = new myVector(a,b), ac, bc, ad;
		ab._normalize();
		int iter = 0;
		do {
			++iter;
			c = getRandPosOnSphere_Double(rad, ctr);
			ac = new myVector(a,c);
			ac._normalize();
		} while (Math.abs(ab._dot(ac))==1.0f);		//if 1 or -1 then collinear
		//4th point needs to be non-coplanar - will guarantee that 
		//it is also not collinear with any pair of existing points
		//normal to abc plane
		myVector planeNorm = ab._cross(ac)._normalize();
		//now find d so that it does not line in plane of abc - vector from ab
		do {
			++iter;
			d = getRandPosOnSphere_Double(rad, ctr);
			ad = new myVector(a,d);
			ad._normalize();
		} while (ad._dot(planeNorm) == 0.0f);//if 0 then in plane (ortho to normal)
		
		myPoint[] spherePts = new myPoint[] {a,b,c,d};		
		if(iter>2) {
			msgObj.dispInfoMessage("SOM_GeomObjBuilder","getRandSpherePoints_Double::thdIDX=" + String.format("%02d", thdIDX)+" ", "Took Longer than 2 iterations to generate 4 points for sphere : " + iter);			
		}
		return spherePts;
	}
	
	

}// class SOM_GeomCallable 
