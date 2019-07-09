package SOM_GeometryProj_PKG.geom_Objects.objs.plane;

import java.util.concurrent.ThreadLocalRandom;

import SOM_GeometryProj_PKG.geom_Objects.objs.base.SOM_GeomObj;
import SOM_GeometryProj_PKG.geom_SOM_Examples.base.Geom_SOMExample;
import base_SOM_Objects.SOM_MapManager;
import base_UI_Objects.my_procApplet;
import base_Utils_Objects.vectorObjs.myPointf;
import base_Utils_Objects.vectorObjs.myVectorf;

public class SOM_Plane extends SOM_GeomObj{ 
	
	/**
	 * plane normal for this planar object - should be normalized!
	 */
	protected final myVectorf planeNorm;
	/**
	 * unique point on this plane closest to origin
	 */
	protected final myPointf planeOrigin;
	/**
	 * equation of plane going through planeOrigin with planeNorm
	 * of the form eq[0]*x + eq[1]*y + eq[2]*z + eq[3] = 0
	 */
	protected final float[] eq;
	
	/**
	 * basis vectors for plane - should be orthogonal and normalized
	 */
	protected final myVectorf[] basisVecs;
	/**
	 * an object to restrict the bounds on this line - min,max, diff s,t value within which to sample plane
	 */
	public final float[][] worldTBounds;

	/**
	 * Constructor for line object
	 * @param _pa papplet
	 * @param _mapMgr owning som map manager
 	 * @param _norm planar normal
	 * @param _origin point closest to 0,0,0 on plane
	 * @param _locClrAra color based on location
	 * @param _worldBnds 4 points that bound the plane for display purposes
	 */
	
	public SOM_Plane(my_procApplet _pa, SOM_MapManager _mapMgr, int _numSmplPts, myVectorf _norm, myPointf _ctr, int[] _locClrAra, myPointf[] _worldBnds) {
		super(_pa, _mapMgr, _locClrAra);	
		planeNorm = _norm;
		planeNorm._normalize();
		planeOrigin = _ctr;		
		eq = getPlanarEqFromPointAndNorm(planeNorm, planeOrigin);
		
		basisVecs = new myVectorf[2];
		if(planeNorm._dot(myVectorf.FORWARD) == 1.0f) {//if planeNorm is in x direction means plane is y-z, so y axis will work as basis
			basisVecs[0] = new myVectorf(0.0f,1.0f,0.0f);
		} else {
			basisVecs[0] = planeNorm._cross(myVectorf.FORWARD);
			basisVecs[0]._normalize();
		}
		basisVecs[1] = basisVecs[0]._cross(planeNorm);
		basisVecs[1]._normalize();		
		float minT = 1000000.0f, minS = minT, maxT = -1.0f*minT, maxS = maxT;
		float curT, curS;
		for(int i=0;i<_worldBnds.length;++i) {
			myPointf p = _worldBnds[i];
			curS = findTVal(p, 0);
			curT = findTVal(p, 1);
			if(curT < minT) {minT = curT;}
			if(curT > maxT) {maxT = curT;}
			if(curS < minS) {minS = curS;}
			if(curS > maxS) {maxS = curS;}		
		}		
		worldTBounds = new float[][] {{minS,maxS, maxS-minS},{minT,maxT, maxT-minT}};
		
		super.buildObjExamples(_numSmplPts);
	}//ctor
	
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
	public float[] getPlanarEqFromPointAndNorm(myVectorf _n, myPointf _p) {
		float[] eq = new float[4];
		eq[0]= _n.x ;
		eq[1]= _n.y ;
		eq[2]= _n.z ;
		eq[3]= _n.x * -_p.x + _n.y * -_p.y + _n.z * -_p.z;	
		return eq;
	}
	
	/**
	 * return a random point on this object(plane)
	 */
	@Override
	public final myPointf getRandPointOnObj() {
		//myPointf _add(myPointf O, float a, myVectorf I, float b, myVectorf J)
		float randS = ((float) ThreadLocalRandom.current().nextFloat() *worldTBounds[0][2])+worldTBounds[0][0];
		float randT = ((float) ThreadLocalRandom.current().nextFloat() *worldTBounds[1][2])+worldTBounds[1][0];
		return myPointf._add(planeOrigin, randS, basisVecs[0], randT, basisVecs[1]);
	}

	
	/**
	 * get an appropriate sample location to build sample sets, based on what kind of object is being built
	 * @return
	 */
	//(SOM_MapManager _map, myVectorf _objCtrLoc, myPointf _myLoc, String _id, int[][] _clrs)
//	 * 		idx 0 : color based on location of this element in space
//	 * 		idx 1 : random color
//	 * 		idx 2 : color based on owning object's location in space
//	 * 		idx 3 : owning object's random color	
	@Override
	protected Geom_SOMExample buildSample(int i) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected Geom_SOMExample buildObjExample() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void drawMeLabel(my_procApplet pa) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void drawMeClrRnd(my_procApplet pa) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void drawMeClrLoc(my_procApplet pa) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void drawMeSelected(my_procApplet pa, float animTmMod) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void drawMeLabel_BMU(my_procApplet pa) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void drawMeClrRnd_BMU(my_procApplet pa) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void drawMeClrLoc_BMU(my_procApplet pa) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void drawMeSelected_BMU(my_procApplet pa, float animTmMod) {
		// TODO Auto-generated method stub
		
	}

}
