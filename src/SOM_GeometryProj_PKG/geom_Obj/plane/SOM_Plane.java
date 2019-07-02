package SOM_GeometryProj_PKG.geom_Obj.plane;

import SOM_GeometryProj_PKG.geom_Obj.base.SOM_GeomObj;
import SOM_GeometryProj_PKG.geom_SOM_Examples.Geom_SOMExample;
import base_SOM_Objects.SOM_MapManager;
import base_UI_Objects.my_procApplet;
import base_Utils_Objects.vectorObjs.myPointf;
import base_Utils_Objects.vectorObjs.myVectorf;

public class SOM_Plane extends SOM_GeomObj{ 
	
	/**
	 * plane normal for this planar object
	 */
	protected myVectorf planeNorm;
	/**
	 * unique point on this plane closest to origin
	 */
	protected myPointf planeOrigin;
	/**
	 * equation of plane going through planeOrigin with planeNorm
	 * of the form eq[0]*x + eq[1]*y + eq[2]*z + eq[3] = 0
	 */
	protected float[] eq;
	
	public SOM_Plane(my_procApplet _pa, SOM_MapManager _mapMgr, int _numSmplPts, myVectorf _norm, myPointf _ctr, int[] _locClrAra, int[] _rndClrAra) {
		super(_pa, _mapMgr, _numSmplPts, _locClrAra, _rndClrAra);
	
		planeNorm = _norm;
		planeOrigin = _ctr;		
		eq = getPlanarEqFromPointAndNorm(planeNorm, planeOrigin);
	}
	/**
	 * point normal form of plane
	 * @param _n
	 * @param _p
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
