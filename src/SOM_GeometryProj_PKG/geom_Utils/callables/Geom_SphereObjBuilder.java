package SOM_GeometryProj_PKG.geom_Utils.callables;

import java.util.concurrent.ThreadLocalRandom;

import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_SmplDataForSOMExample;
import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_SphereSOMExample;
import SOM_GeometryProj_PKG.som_geom.SOM_GeomMapManager;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomObj;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading.SOM_GeomObjBldrTasks;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading.SOM_GeomObjBuilder;
import base_SOM_Objects.som_examples.SOM_ExDataType;
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
	protected SOM_GeomObj _buildSingleObject(SOM_ExDataType _exDataType, int idx) {
		//SOM_Sphere(SOM_GeomMapManager _mapMgr, myPointf _loc, float _rad, int _numSmplPts, float[][] _worldBounds)
		
		//pick characteristics of target sphere
		float rad = (ThreadLocalRandom.current().nextFloat()*diffRad) + minRad;
		myPointf ctr = this.getRandPointInBounds_3D(rad);
			
		//now get 4 points to determine this sphere - make sure they are ortho		
		myPointf[] spherePts = getRandSpherePoints(rad,ctr);		
		String ID = "Sphere_"+String.format("%05d", idx);
		Geom_SmplDataForSOMExample[] _srcSmpls = buildSrcGeomSmplAra(null, spherePts);
		
		//(SOM_GeomMapManager _mapMgr, SOM_AnimWorldWin _animWin, SOM_ExDataType _exType, String _id, Geom_SmplDataForSOMExample[] _srcSmpls, myPointf _ctr, float _rad, int _numSmplPts, float[][] _worldBounds)
		Geom_SphereSOMExample obj = new Geom_SphereSOMExample(mapMgr,animWin, _exDataType, ID, _srcSmpls, numSmplsPerObj, worldBounds);
		boolean passed = obj.testSphereConstruction(ctr, rad, 1.0f);
		return obj;
	}

}//Geom_SphereObjBuilder
