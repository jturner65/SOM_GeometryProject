package SOM_GeometryProj_PKG.geom_Utils.callables;

import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_LineSOMExample;
import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_SmplDataForSOMExample;
import SOM_GeometryProj_PKG.som_geom.SOM_GeomMapManager;
import SOM_GeometryProj_PKG.som_geom.geom_UI.SOM_AnimWorldWin;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomObj;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading.SOM_GeomObjBldrTasks;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading.SOM_GeomObjBuilder;
import base_SOM_Objects.som_examples.SOM_ExDataType;
import base_Utils_Objects.vectorObjs.myPointf;

public class Geom_LineObjBuilder extends SOM_GeomObjBuilder {

	public Geom_LineObjBuilder(SOM_GeomMapManager _mapMgr,  SOM_GeomObj[] _objArray, int[] _intVals, SOM_GeomObjBldrTasks _taskToDo, float[][] _worldBounds) {
		super(_mapMgr,  _objArray,_intVals, _taskToDo, "Lines", _worldBounds);
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
//		public SOM_Line(SOM_GeomMapManager _mapMgr, myPointf _a, myPointf _b, int _numSmplPts, float[][] _worldBounds) {
		myPointf[] pts = new myPointf[] {getRandPointInBounds_2D(),getRandPointInBounds_2D()};
		String ID = "Line_"+String.format("%05d", idx);
		Geom_SmplDataForSOMExample[] _srcSmpls = buildSrcGeomSmplAra(null, pts);
		
		//(SOM_GeomMapManager _mapMgr, SOM_AnimWorldWin _animWin, SOM_ExDataType _exType, String _id, SOM_GeomSmplForSOMExample[] _srcSmpls, int _numSmplPts, float[][] _worldBounds)
		Geom_LineSOMExample line = new Geom_LineSOMExample(mapMgr, animWin, _exDataType, ID, _srcSmpls, numSmplsPerObj, worldBounds);
		return line;
	}


}
