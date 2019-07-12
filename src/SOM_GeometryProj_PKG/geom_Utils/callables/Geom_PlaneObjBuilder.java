package SOM_GeometryProj_PKG.geom_Utils.callables;


import SOM_GeometryProj_PKG.geom_Objects.SOM_Plane;
import SOM_GeometryProj_PKG.som_geom.SOM_GeomMapManager;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomObj;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading.SOM_GeomObjBldrTasks;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading.SOM_GeomObjBuilder;
import base_Utils_Objects.vectorObjs.myPointf;

public class Geom_PlaneObjBuilder extends SOM_GeomObjBuilder {

	public Geom_PlaneObjBuilder(SOM_GeomMapManager _mapMgr, SOM_GeomObj[] _objArray, int[] _intVals, SOM_GeomObjBldrTasks _taskToDo, float[][] _worldBounds) {
		super(_mapMgr,  _objArray,_intVals, _taskToDo, "Planes", _worldBounds);
	}

	@Override
	protected SOM_GeomObj _buildSingleObject(int idx) {
//		/**
//		 * Constructor for line object
//		 * @param _mapMgr owning som map manager
//		 * @param _ptsOnPlane : non-colinear points on plane : idx 0 is "ctr" - point closest to 0,0,0 for plane
//	 	 * @param _basis :  basis vectors for plane - idx 0 is normal
//		 * @param _numSmplPts : # of sample points to build for this object
//		 * @param _worldBnds 4 points that bound the plane for display purposes
//		 */	
//		public SOM_Plane(SOM_GeomMapManager _mapMgr, myPointf[] _ptsOnPlane, myVectorf[] _basis, int _numSmplPts, float[][] _worldBounds) {
		//points must not be colinear
		myPointf[] planePts = getRandPlanePoints();
		return new SOM_Plane(mapMgr, planePts, numSmplsPerObj, worldBounds);
	}

}
