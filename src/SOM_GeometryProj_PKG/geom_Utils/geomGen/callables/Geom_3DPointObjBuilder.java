package SOM_GeometryProj_PKG.geom_Utils.geomGen.callables;

import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_3DPointSOMExample;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers.Geom_3DPointMapMgr;
import base_Math_Objects.vectorObjs.floats.myPointf;
import base_SOM_Objects.som_examples.enums.SOM_ExDataType;
import base_SOM_Objects.som_geom.SOM_GeomMapManager;
import base_SOM_Objects.som_geom.geom_examples.SOM_GeomObj;
import base_SOM_Objects.som_geom.geom_utils.geom_objs.SOM_GeomSamplePointf;
import base_SOM_Objects.som_geom.geom_utils.geom_threading.geomGen.SOM_GeomObjBldrTasks;
import base_SOM_Objects.som_geom.geom_utils.geom_threading.geomGen.SOM_GeomObjBuilder;

public class Geom_3DPointObjBuilder extends SOM_GeomObjBuilder {

	public Geom_3DPointObjBuilder(SOM_GeomMapManager _mapMgr, SOM_GeomObj[] _objArray, int[] _intVals,
			SOM_GeomObjBldrTasks _taskToDo) {
		super(_mapMgr, _objArray, _intVals, _taskToDo);
	}
	/**
	 * build a single point object
	 */

	@Override
	protected SOM_GeomObj _buildSingleObject(SOM_ExDataType _exDataType, int idx) {
		myPointf a = getRandPointInBounds_3D();
		String ID = "Pt_"+String.format("%06d", idx);
		SOM_GeomSamplePointf[] pts = new SOM_GeomSamplePointf[] {new SOM_GeomSamplePointf(a, ID+"_gen_pt_0",null)};
		Geom_3DPointSOMExample point = new Geom_3DPointSOMExample(((Geom_3DPointMapMgr)mapMgr),_exDataType, ID, pts, numSmplsPerObj, true, true);
		return point;
	}//_buildSingleObject

}//class Geom_3DPointObjBuilder
