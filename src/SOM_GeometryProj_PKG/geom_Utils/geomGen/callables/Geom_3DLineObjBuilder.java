package SOM_GeometryProj_PKG.geom_Utils.geomGen.callables;

import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_3DLineSOMExample;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers.Geom_3DLineMapMgr;
import base_Math_Objects.vectorObjs.floats.myPointf;
import base_SOM_Objects.som_examples.SOM_ExDataType;
import base_SOM_Objects.som_geom.geom_examples.SOM_GeomObj;
import base_SOM_Objects.som_geom.geom_utils.geom_objs.SOM_GeomSamplePointf;
import base_SOM_Objects.som_geom.geom_utils.geom_threading.geomGen.SOM_GeomObjBldrTasks;
import base_SOM_Objects.som_geom.geom_utils.geom_threading.geomGen.SOM_GeomObjBuilder;

public class Geom_3DLineObjBuilder extends SOM_GeomObjBuilder {

	public Geom_3DLineObjBuilder(Geom_3DLineMapMgr _mapMgr,  SOM_GeomObj[] _objArray, int[] _intVals, SOM_GeomObjBldrTasks _taskToDo) {
		super(_mapMgr,  _objArray,_intVals, _taskToDo);
	}
	
	/**
	 * build a single line object
	 */
	@Override
	protected SOM_GeomObj _buildSingleObject(SOM_ExDataType _exDataType, int idx) {

//	 	 * @param _a, _b : 2 points on line
//	 	 * @param _numSmplPts : # of points to build
//		 * @param _locClrAra color based on location
//		 * @param _worldBounds 2d array of bounds for where reasonable points should be generated
//		 * 		first idx 	: 0 is min; 1 is diff
//		 * 		2nd idx 	: 0 is x, 1 is y
//		 */
		myPointf a = getRandPointInBounds_3D(),b;
		do {b = getRandPointInBounds_3D();} while(a.equals(b));
		String ID = "Line_"+String.format("%05d", idx);
		SOM_GeomSamplePointf[] pts = new SOM_GeomSamplePointf[] {new SOM_GeomSamplePointf(a, ID+"_gen_pt_0",null),new SOM_GeomSamplePointf(b, ID+"_gen_pt_1", null)};
		//SOM_GeomSamplePointf[] _srcSmpls = buildSrcGeomSmplAra(null, pts);
		
		Geom_3DLineSOMExample line = new Geom_3DLineSOMExample(((Geom_3DLineMapMgr)mapMgr), _exDataType, ID, pts, numSmplsPerObj, true, true);
		return line;
	}

}//class Geom_LineObjBuilder
