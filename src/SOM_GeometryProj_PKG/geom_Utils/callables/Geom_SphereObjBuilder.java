package SOM_GeometryProj_PKG.geom_Utils.callables;

import java.util.concurrent.ThreadLocalRandom;

import SOM_GeometryProj_PKG.geom_Objects.SOM_Sphere;
import SOM_GeometryProj_PKG.som_geom.SOM_GeomMapManager;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomObj;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading.SOM_GeomObjBldrTasks;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading.SOM_GeomObjBuilder;
import base_Utils_Objects.vectorObjs.myPointf;

public class Geom_SphereObjBuilder extends SOM_GeomObjBuilder {
	
	protected final float minRad,  maxRad, diffRad;

	public Geom_SphereObjBuilder(SOM_GeomMapManager _mapMgr, SOM_GeomObj[] _objArray, int[] _intVals, SOM_GeomObjBldrTasks _taskToDo, float[] radSpan, float[][] _worldBounds) {
		super(_mapMgr,  _objArray,_intVals, _taskToDo, "Spheres", _worldBounds);
		minRad = radSpan[0];
		maxRad = radSpan[1];
		diffRad = maxRad - minRad;
	}

	@Override
	protected SOM_GeomObj _buildSingleObject(int idx) {
		//SOM_Sphere(SOM_GeomMapManager _mapMgr, myPointf _loc, float _rad, int _numSmplPts, float[][] _worldBounds)
		
		float rad = (ThreadLocalRandom.current().nextFloat()*diffRad) + minRad;
		myPointf ctr = this.getRandPointInBounds_3D(rad);
		return new SOM_Sphere(mapMgr,ctr, rad, numSmplsPerObj, worldBounds);
	}

}
