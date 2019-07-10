package SOM_GeometryProj_PKG.geom_Utils.callables;

import java.util.concurrent.ThreadLocalRandom;

import SOM_GeometryProj_PKG.geom_Objects.SOM_Plane;
import SOM_GeometryProj_PKG.som_geom.SOM_GeomMapManager;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomObj;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading.SOM_GeomObjBldrTasks;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading.SOM_GeomObjBuilder;
import base_Utils_Objects.io.MessageObject;
import base_Utils_Objects.vectorObjs.myPointf;
import base_Utils_Objects.vectorObjs.myVectorf;

public class Geom_PlaneObjBuilder extends SOM_GeomObjBuilder {

	public Geom_PlaneObjBuilder(SOM_GeomMapManager _mapMgr, SOM_GeomObj[] _objArray, int[] _intVals, SOM_GeomObjBldrTasks _taskToDo, float[][] _worldBounds) {
		super(_mapMgr,  _objArray,_intVals, _taskToDo, "Planes", _worldBounds);
	}

	@Override
	protected SOM_GeomObj _buildSingleObject(int idx) {
//		/**
//		 * Constructor for line object
//		 * @param _mapMgr owning som map manager
//	 	 * @param _norm planar normal
//		 * @param _origin point closest to 0,0,0 on plane
//		 * @param _locClrAra color based on location
//		 * @param _worldBnds 4 points that bound the plane for display purposes
//		 */	
//		 SOM_Plane(SOM_GeomMapManager _mapMgr, myVectorf _norm, myPointf _ctr, int _numSmplPts, float[][] _worldBounds)
		myVectorf norm = getRandNormal_3D();
		myPointf ctr = myPointf._mult(norm, ThreadLocalRandom.current().nextFloat()*.25f*worldBounds[1][0]);
		return new SOM_Plane(mapMgr, norm, ctr, numSmplsPerObj, worldBounds);
	}

}
