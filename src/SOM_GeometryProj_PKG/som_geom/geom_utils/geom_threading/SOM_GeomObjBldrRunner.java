package SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading;


import java.util.concurrent.ExecutorService;

import base_SOM_Objects.SOM_MapManager;
import base_Utils_Objects.threading.runners.myThreadRunner;

/**
 * This class will build graphical objects
 * @author john
 *
 */
public abstract class SOM_GeomObjBldrRunner extends myThreadRunner {
	/**
	 * number of objects to synthesize
	 */
	protected int numObjs;
	/**
	 * what kind of task this runner will manage
	 */
	protected int taskType;
	
	/**
	 * # of examples to manage per threading partition, approximately
	 */
	protected static final int numPerPartiton = 50000;
	
	/**
	 * data name being processed
	 */
	protected final String dataTypeName;
	
	/**
	 * 
	 * @param _mapMgr
	 * @param _th_exec
	 * @param _dataTypName
	 * @param _forceST
	 * @param _numObjs
	 * @param _taskType
	 */
	
	public SOM_GeomObjBldrRunner(SOM_MapManager _mapMgr, ExecutorService _th_exec, String _dataTypName, boolean _forceST, int _numObjs, int _taskType) {
		super(_mapMgr.getMsgObj(), _th_exec, _mapMgr.isMTCapable() && !_forceST, _mapMgr.getNumUsableThreads()-1, _numObjs);		
		numObjs = _numObjs;
		taskType = _taskType;
		dataTypeName = _dataTypName;
	}
	
	@Override
	protected int getNumPerPartition() {		return numPerPartiton;}


	public void setNumObjs(int _num) {	numObjs = _num;}
	public void setTaskType(int _task) {taskType = _task;}



}//class SOM_GeomObjBuilder
