package SOM_GeometryProj_PKG.geom_Utils.geomGen.callables;

import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_LineSOMExample;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers.Geom_LineMapMgr;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomObj;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomSamplePointf;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomSmplDataForEx;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading.geomGen.SOM_GeomObjBldrTasks;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading.geomGen.SOM_GeomObjBuilder;
import base_SOM_Objects.som_examples.SOM_ExDataType;
import base_Utils_Objects.vectorObjs.myPointf;

public class Geom_LineObjBuilder extends SOM_GeomObjBuilder {

	public Geom_LineObjBuilder(Geom_LineMapMgr _mapMgr,  SOM_GeomObj[] _objArray, int[] _intVals, SOM_GeomObjBldrTasks _taskToDo) {
		super(_mapMgr,  _objArray,_intVals, _taskToDo, "Lines");
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
		myPointf a = getRandPointInBounds_2D();
		myPointf b = getRandPointInBounds_2D();
		String ID = "Line_"+String.format("%05d", idx);
		SOM_GeomSamplePointf[] pts = new SOM_GeomSamplePointf[] {new SOM_GeomSamplePointf(a, ID+"_gen_pt_0" ),new SOM_GeomSamplePointf(b, ID+"_gen_pt_1")};
		SOM_GeomSmplDataForEx[] _srcSmpls = buildSrcGeomSmplAra(null, pts);
		
		Geom_LineSOMExample line = new Geom_LineSOMExample(((Geom_LineMapMgr)mapMgr), animWin, _exDataType, ID, _srcSmpls, numSmplsPerObj);
		return line;
	}


}
