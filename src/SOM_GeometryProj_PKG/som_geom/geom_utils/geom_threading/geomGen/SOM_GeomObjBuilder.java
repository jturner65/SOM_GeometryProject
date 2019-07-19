package SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading.geomGen;

import SOM_GeometryProj_PKG.som_geom.SOM_GeomMapManager;
import SOM_GeometryProj_PKG.som_geom.geom_UI.SOM_AnimWorldWin;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomObj;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading.base.SOM_GeomCallable;
import base_SOM_Objects.som_examples.SOM_ExDataType;

/**
 * Instances of this class will execute some portion of work
 * @author john
 *
 */
public abstract class SOM_GeomObjBuilder extends SOM_GeomCallable {
	/**
	 * owning animation win holding geometric objects
	 */
	protected final int numSmplsPerObj;	
	
	protected final SOM_GeomObjBldrTasks taskToDo;
	/**
	 * ref to array of data to be processed - should be predefined/prebuilt and just populated in runner
	 */
	protected SOM_GeomObj[] objArray;
	
	/**
	 * base constructor to build runner
	 * @param _mapMgr map manager for this builder
	 * @param _intVals : array holding (idx 0 : stIdx, idx 1 : endIdx, idx 2 : thdIDX, idx 3 : numSmplsPerObj)
	 * @param _endExIDX
	 * @param _thdIDX
	 * @param _taskToDo
	 * @param _datatype
	 * @param _worldBounds 	coordinate bounds in world 
	 * 		first idx : 0 is min; 1 is diff
	 * 		2nd idx : 0 is x, 1 is y, 2 is z (if used)
	 */
	public SOM_GeomObjBuilder(SOM_GeomMapManager _mapMgr, SOM_GeomObj[] _objArray, int[] _intVals, SOM_GeomObjBldrTasks _taskToDo, String _datatype) {
		super(_mapMgr, _intVals[0], _intVals[1], _intVals[2],_datatype);
		
		numSmplsPerObj = _intVals[3];
		
		taskToDo = _taskToDo;	
		objArray = _objArray;
	
	}//ctor
	
	/**
	 * build objects
	 */
	protected void _buildBaseObj_Task() {
		msgObj.dispInfoMessage("SOM_GeomObjBuilder", "_buildBaseObj_Task::thdIDX=", "Start building " + (endIdx-stIdx) + " " +dataType +" base objects at idxs : ["+stIdx+", "+endIdx+"]");
		for(int i=stIdx; i<endIdx;++i) {	objArray[i]=_buildSingleObject(SOM_ExDataType.Training,i);}//incrProgress(i);}
		msgObj.dispInfoMessage("SOM_GeomObjBuilder", "_buildBaseObj_Task::thdIDX=", "Finished building " + (endIdx-stIdx) + " " +dataType +" base objects at idxs : ["+stIdx+", "+endIdx+"]");
	}
	
	/**
	 * regenerate the samples for the base objects
	 */
	protected void _regenSampleBaseObj_Task() {
		msgObj.dispInfoMessage("SOM_GeomObjBuilder", "_regenSampleBaseObj_Task::thdIDX=", "Start regenerating samples for " + (endIdx-stIdx) + " " +dataType +" base objects at idxs : ["+stIdx+", "+endIdx+"]");
		for(int i=stIdx; i<endIdx;++i) {	objArray[i].buildSampleSetAndPShapes(numSmplsPerObj);}//incrProgress(i);}
		msgObj.dispInfoMessage("SOM_GeomObjBuilder", "_regenSampleBaseObj_Task::thdIDX=", "Finished regenerating samples for  " + (endIdx-stIdx) + " " +dataType +" base objects at idxs : ["+stIdx+", "+endIdx+"]");
	}
	
	/**
	 * build a single object to be stored at idx
	 * @param idx idx of object in resultant array
	 * @return build object
	 */
	protected abstract SOM_GeomObj _buildSingleObject(SOM_ExDataType _exDataType, int idx);
		

	@Override
	public final Boolean call() throws Exception {
		switch (taskToDo) {
			case buildBaseObj 				: { _buildBaseObj_Task(); break;}
			case regenSamplesBaseObj 		: { _regenSampleBaseObj_Task(); break;}
			default :{
				msgObj.dispErrorMessage("SOM_GeomObjBuilder", "call::thdIDX=", "Unsupported task chosen : " + taskToDo.toString() +".  Aborting");
			}
		}
		return true;
	}
	

}//SOM_GeomObjBuilder
