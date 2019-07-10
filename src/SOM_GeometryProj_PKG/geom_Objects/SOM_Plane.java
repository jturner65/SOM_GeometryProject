package SOM_GeometryProj_PKG.geom_Objects;

import java.util.concurrent.ThreadLocalRandom;

import SOM_GeometryProj_PKG.som_geom.SOM_GeomMapManager;
import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomExample;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomObj;
import base_UI_Objects.my_procApplet;
import base_Utils_Objects.vectorObjs.myPointf;
import base_Utils_Objects.vectorObjs.myVectorf;

public class SOM_Plane extends SOM_GeomObj{ 
	public final int ID;
	private static int IDGen = 0;
	
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
	 * angle between normal and "up" - use this for display
	 */
	protected final float rotAngle;
	
	/**
	 * normalized axis between normal and "up" - use this to rotate for display
	 */
	protected final myVectorf rotAxis;
	
	protected final myPointf[] orthoFrame;

	/**
	 * Constructor for line object
	 * @param _mapMgr owning som map manager
 	 * @param _norm planar normal
	 * @param _origin point closest to 0,0,0 on plane
	 * @param _locClrAra color based on location
	 * @param _worldBnds 4 points that bound the plane for display purposes
	 */	
	public SOM_Plane(SOM_GeomMapManager _mapMgr, myVectorf _norm, myPointf _ctr, int _numSmplPts, float[][] _worldBounds) {
		super(_mapMgr,_ctr, _worldBounds, true);	
		ID = IDGen++;
		planeNorm = _norm;
		planeNorm._normalize();
		planeOrigin = _ctr;		
		eq = getPlanarEqFromPointAndNorm(planeNorm, planeOrigin);
		rotAxis = myVectorf.UP._cross(planeNorm);
		//rotAngle = (float) Math.asin(rotAxis.magn);
		rotAxis._normalize();
		
		rotAngle = (float) Math.acos(planeNorm._dot(myVectorf.UP));
		
		basisVecs = new myVectorf[2];
		if(planeNorm._dot(myVectorf.FORWARD) == 1.0f) {//if planeNorm is in x direction means plane is y-z, so y axis will work as basis
			basisVecs[0] = new myVectorf(0.0f,1.0f,0.0f);
		} else {
			basisVecs[0] = planeNorm._cross(myVectorf.FORWARD);
			basisVecs[0]._normalize();
		}
		basisVecs[1] = basisVecs[0]._cross(planeNorm);
		basisVecs[1]._normalize();		

		orthoFrame = new myPointf[3];
		float frameLen = 100.0f;
		orthoFrame[0]= myPointf._add(planeOrigin, frameLen, planeNorm);
		orthoFrame[1]= myPointf._add(planeOrigin, frameLen, basisVecs[0]);
		orthoFrame[2]= myPointf._add(planeOrigin, frameLen, basisVecs[1]);
		
		
		
		super.buildInitObjAndSamples(_numSmplPts);
	}//ctor
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
	protected SOM_GeomExample buildSample(int i) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected SOM_GeomExample buildObjExample() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void drawMeLabel(my_procApplet pa) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void _drawMe_Geom(my_procApplet pa) {
		pa.pushMatrix();pa.pushStyle();	
		//rotate up to plane norm planeNorm
		_drawOrthoFrame(pa);		
		pa.translate(planeOrigin.x,planeOrigin.y,planeOrigin.z); 
		pa.rotate(rotAngle,rotAxis);
		_drawPlane(pa);
		pa.popStyle();pa.popMatrix();	
	}
	
	private void _drawPlane(my_procApplet pa) {
		pa.strokeWeight(1.0f);
		pa.box(500,500,1);
		
	}
	
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
		for(int i=0;i<orthoFrame.length;++i) {
			orthoFrame[i].showMeSphere(pa, 5.0f);
		}
				
	}//_drawOrthoFrame
	

	@Override
	protected final void _drawMe_Geom_Lbl(my_procApplet pa) {
		
		
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
