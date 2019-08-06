package SOM_GeometryProj_PKG.geom_Utils.geomGen.callables;

import java.util.concurrent.ThreadLocalRandom;

import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_SphereSOMExample;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers.Geom_SphereMapMgr;
import base_SOM_Objects.som_examples.SOM_ExDataType;
import base_SOM_Objects.som_geom.geom_examples.SOM_GeomObj;
import base_SOM_Objects.som_geom.geom_utils.geom_objs.SOM_GeomSamplePointf;
import base_SOM_Objects.som_geom.geom_utils.geom_threading.geomGen.SOM_GeomObjBldrTasks;
import base_SOM_Objects.som_geom.geom_utils.geom_threading.geomGen.SOM_GeomObjBuilder;
import base_Utils_Objects.vectorObjs.myPointf;

public class Geom_SphereObjBuilder extends SOM_GeomObjBuilder {
	
	protected final float minRad,  maxRad, diffRad;

	public Geom_SphereObjBuilder(Geom_SphereMapMgr _mapMgr, SOM_GeomObj[] _objArray, int[] _intVals, SOM_GeomObjBldrTasks _taskToDo, float[] radSpan) {
		super(_mapMgr,  _objArray,_intVals, _taskToDo);
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
//		for(myPointf p : spherePts) {
//			msgObj.dispInfoMessage("Geom_SphereObjBuilder", "_buildSingleObject", "pt : " + p.toStrBrf());
//		}
		String ID = "Sphere_"+String.format("%05d", idx);
		SOM_GeomSamplePointf[] pts = new SOM_GeomSamplePointf[spherePts.length];
		for(int i=0;i<pts.length; ++i) {pts[i] = new SOM_GeomSamplePointf(spherePts[i],ID+"_gen_pt_"+i, null);}

		//SOM_GeomSmplDataForEx[] _srcSmpls = buildSrcGeomSmplAra(null, pts);
		
		//(SOM_GeomMapManager _mapMgr, SOM_AnimWorldWin _animWin, SOM_ExDataType _exType, String _id, Geom_SmplDataForSOMExample[] _srcSmpls, myPointf _ctr, float _rad, int _numSmplPts, float[][] _worldBounds)
		Geom_SphereSOMExample obj = new Geom_SphereSOMExample(((Geom_SphereMapMgr)mapMgr), _exDataType, ID, pts, numSmplsPerObj, true);
		//boolean passed = 
		obj.testSphereConstruction(ctr, rad, 1.0f);
		
		return obj;
	}

}//Geom_SphereObjBuilder
