package SOM_GeometryProj_PKG.geom_Utils.callables;

import SOM_GeometryProj_PKG.geom_Objects.SOM_Line;
import SOM_GeometryProj_PKG.som_geom.SOM_GeomMapManager;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomObj;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading.SOM_GeomObjBldrTasks;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading.SOM_GeomObjBuilder;
import base_Utils_Objects.vectorObjs.myPointf;

public class Geom_LineObjBuilder extends SOM_GeomObjBuilder {

	public Geom_LineObjBuilder(SOM_GeomMapManager _mapMgr,  SOM_GeomObj[] _objArray, int[] _intVals, SOM_GeomObjBldrTasks _taskToDo, float[][] _worldBounds) {
		super(_mapMgr,  _objArray,_intVals, _taskToDo, "Lines", _worldBounds);
	}
	
	/**
	 * build a single line object
	 */
	@Override
	protected SOM_GeomObj _buildSingleObject(int idx) {

//	 	 * @param _a, _b : 2 points on line
//	 	 * @param _numSmplPts : # of points to build
//		 * @param _locClrAra color based on location
//		 * @param _worldBounds 2d array of bounds for where reasonable points should be generated
//		 * 		first idx 	: 0 is min; 1 is diff
//		 * 		2nd idx 	: 0 is x, 1 is y
//		 */
//		public SOM_Line(SOM_GeomMapManager _mapMgr, myPointf _a, myPointf _b, int _numSmplPts, float[][] _worldBounds) {
		myPointf a = getRandPointInBounds_2D();
		myPointf b = getRandPointInBounds_2D();
		
		SOM_Line line = new SOM_Line(mapMgr,a, b,numSmplsPerObj, worldBounds);
		return line;
	}


}
