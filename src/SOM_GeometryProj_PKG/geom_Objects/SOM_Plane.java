package SOM_GeometryProj_PKG.geom_Objects;

import java.util.concurrent.ThreadLocalRandom;

import SOM_GeometryProj_PKG.geom_SOM_Examples.Geom_PlaneSOMExample;
import SOM_GeometryProj_PKG.som_geom.SOM_GeomMapManager;
import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomExample;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomObj;
import base_SOM_Objects.SOM_MapManager;
import base_UI_Objects.my_procApplet;
import base_Utils_Objects.vectorObjs.myPointf;
import base_Utils_Objects.vectorObjs.myVectorf;

public class SOM_Plane extends SOM_GeomObj{ 
	public final int ID;
	private static int IDGen = 0;
	
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
	 * angle between normal and "up" - use this for display
	 */
	protected final float rotAngle;
	
	/**
	 * normalized axis between normal and "up" - use this to rotate for display
	 */
	protected final myVectorf rotAxis;
	/**
	 * use this with origin point to display
	 */
	protected final myPointf[] orthoFrame;
	/**
	 * coordinate bounds in world for plane - static across all plane objects
	 * 		first idx : 0 is min; 1 is diff
	 * 		2nd idx : 0 is x, 1 is y, 2 is z
	 */
	protected static float[][] worldBounds=null;

	/**
	 * Constructor for plane object
	 * @param _mapMgr owning som map manager
	 * @param _ptsOnPlane : non-colinear points on plane
	 * @param _numSmplPts : # of sample points to build for this object
	 * @param _worldBnds 4 points that bound the plane for display purposes
	 */	
	public SOM_Plane(SOM_GeomMapManager _mapMgr, myPointf[] _ptsOnPlane, int _numSmplPts, float[][] _worldBounds) {
		super(_mapMgr,_ptsOnPlane, _worldBounds, 3);	
		ID = IDGen++;
		//planeOrigin = _ctr;	
		//norm is idx 0
		//determine normal
		basisVecs = new myVectorf[3];
		//plane norm
		basisVecs[0] = myVectorf._cross(new myVectorf(srcPts[0], srcPts[1]), new myVectorf(srcPts[0], srcPts[2]))._normalize();
		if(basisVecs[0]._dot(myVectorf.FORWARD) == 1.0f) {//if planeNorm is in x direction means plane is y-z, so y axis will work as basis
			basisVecs[1] = new myVectorf(myVectorf.RIGHT);
		} else {
			basisVecs[1] = basisVecs[0]._cross(myVectorf.FORWARD);
			basisVecs[1]._normalize();
		}
		basisVecs[2] = basisVecs[1]._cross(basisVecs[0]);
		basisVecs[2]._normalize();		

		eq = getPlanarEqFromPointAndNorm(basisVecs[0], srcPts[0]);
		planeOrigin = new myPointf(eq[0]*eq[3],eq[1]*eq[3],eq[2]*eq[3]);
		
		//axis and angle to rotate simple plane for display to coincide with orienation of this plane's normal
		rotAxis = myVectorf.UP._cross(basisVecs[0]);
		rotAxis._normalize();		
		rotAngle = (float) Math.acos(basisVecs[0]._dot(myVectorf.UP));
		//find closest point on plane to origin
		//planeOrigin 

		orthoFrame = new myPointf[3];
		float frameLen = 100.0f;
		for(int i=0;i<orthoFrame.length;++i) {
			orthoFrame[i]= myPointf._add(planeOrigin, frameLen, basisVecs[i]);
		}
		
		super.buildLocClrInitObjAndSamples(planeOrigin, _numSmplPts);
	}//ctor
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
	 * calculate the bounds on s and t (if appropriate) for parametric formulation of object equation
	 * worldBounds is 
	 * 		first idx 	: 0 is min; 1 is diff
	 * 		2nd idx 	: 0 is x, 1 is y, 2 is z
	 * result is
	 * 		first idx 	: 0==min, 1==diff
	 * 		2nd idx 	: 0==s, 1==t, 
	 * @return
	 */
	protected final float[][] calcTBounds(){
		float[][] res = new float[][] {{0.0f,0.0f},{0.0f,0.0f}};
		
		return res;
	}

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
	
		float[][] wb = getWorldTBounds();
		float randS = ((float) ThreadLocalRandom.current().nextFloat() *wb[1][0])+wb[0][0];
		float randT = ((float) ThreadLocalRandom.current().nextFloat() *wb[1][1])+wb[0][1];
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
	protected SOM_GeomExample buildSample(int idx) {
		//(SOM_MapManager _map, String _id, int[][] _clrs, SOM_GeomSmplForSOMExample[] _srcSmpls, float[][] _worldBounds)
		return new Geom_PlaneSOMExample(mapMgr, "Plane_"+getID() + "_Smp_" + idx, buildSrcObjDataAra(true), worldBounds);
	}
	@Override
	protected SOM_GeomExample buildObjExample() {
		// TODO Auto-generated method stub
		return new Geom_PlaneSOMExample(mapMgr, "Plane_"+getID(), buildSrcObjDataAra(false), worldBounds);
	}
	@Override
	public void drawMyLabel(my_procApplet pa) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void _drawMe_Geom(my_procApplet pa) {
		pa.pushMatrix();pa.pushStyle();	
		
		_drawOrthoFrame(pa);		
		pa.popStyle();pa.popMatrix();	
		drawPlane(pa,planeOrigin); 
	}
	
	@Override
	protected void _drawMe_Geom_BMU(my_procApplet pa) {
		pa.pushMatrix();pa.pushStyle();	
		//rotate up to plane norm planeNorm
		_drawOrthoFrame(pa);	
		pa.popStyle();pa.popMatrix();	
		drawPlane(pa,baseObjBMUWorldLoc); 
	}
	
	/**
	 * draw plane centered at passed point
	 * @param pa
	 * @param _origin
	 */
	private void drawPlane(my_procApplet pa, myPointf _origin) {
		pa.pushMatrix();pa.pushStyle();	
		pa.translate(_origin.x,_origin.y,_origin.z); 
		//rotate up to plane norm planeNorm
		pa.rotate(rotAngle,rotAxis);
		//draw plane as box currently
		//TODO replace with method built on end points derived from equation
		pa.strokeWeight(1.0f);
		pa.box(500,500,1);
		
		pa.popStyle();pa.popMatrix();	
		
	}//drawPlane
	
	private void _drawOrthoFrame(my_procApplet pa) {
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
				
	}//_drawOrthoFrame
	

	@Override
	protected final void _drawMe_Geom_Lbl(my_procApplet pa) {
		
		
	}
	@Override
	public void drawMeLabel_BMU(my_procApplet pa) {
		// TODO Auto-generated method stub
		
	}


	
	@Override
	public void drawMeSelected(my_procApplet pa, float animTmMod) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void drawMeSelected_BMU(my_procApplet pa, float animTmMod) {
		// TODO Auto-generated method stub
		
	}

}
