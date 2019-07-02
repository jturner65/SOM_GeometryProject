package SOM_GeometryProj_PKG.geom_Obj.line;

import SOM_GeometryProj_PKG.geom_Obj.base.SOM_GeomObj;
import SOM_GeometryProj_PKG.geom_SOM_Examples.Geom_SOMExample;
import base_SOM_Objects.SOM_MapManager;
import base_UI_Objects.my_procApplet;
import base_Utils_Objects.vectorObjs.myPointf;
import base_Utils_Objects.vectorObjs.myVectorf;

public class SOM_Line extends SOM_GeomObj {
	/**
	 * direction vector for this line
	 */
	public myVectorf dir;
	/**
	 * point closest to 0,0 for this line
	 */
	public myPointf origin;
	public SOM_Line(my_procApplet _pa, SOM_MapManager _mapMgr, int _numSmplPts,  myVectorf _dir, myPointf _origin, int[] _locClrAra, int[] _rndClrAra) {
		super(_pa, _mapMgr, _numSmplPts, _locClrAra, _rndClrAra);
		//z is always 0 - making this in 2 d
		dir=_dir;
		dir.z = 0;
		dir._normalize();
		origin = _origin;
		origin.z = 0;
	}//ctor	
	
	
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
