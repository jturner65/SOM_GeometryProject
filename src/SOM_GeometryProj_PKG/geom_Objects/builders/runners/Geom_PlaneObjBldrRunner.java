package SOM_GeometryProj_PKG.geom_Objects.builders.runners;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import SOM_GeometryProj_PKG.geom_Objects.builders.callables.Geom_PlaneObjBuilder;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading.SOM_GeomObjBldrRunner;
import base_SOM_Objects.SOM_MapManager;

public class Geom_PlaneObjBldrRunner extends SOM_GeomObjBldrRunner {

	public Geom_PlaneObjBldrRunner(SOM_MapManager _mapMgr, ExecutorService _th_exec, boolean _forceST, int _numObjs, int _taskType) {
		super(_mapMgr, _th_exec, "Planes", _forceST, _numObjs, _taskType);
		// TODO Auto-generated constructor stub
	}

	/**
	 * build callable object that will be invoked
	 * @param dataSt start idx in data
	 * @param dataEnd end idx in data
	 * @param pIdx thread/partition idx
	 * @return callable to be invoked 
	 */	
	@Override
	protected void execPerPartition(List<Callable<Boolean>> ExMappers, int dataSt, int dataEnd, int pIdx,int ttlParts) {
		ExMappers.add(new Geom_PlaneObjBuilder(msgObj, dataSt, dataEnd, pIdx, taskType, dataTypeName));
	}
	
	/**
	 * exec entire dataset in one thread
	 */
	@Override
	protected void runMe_Indiv_ST() {
		//int _stExIDX, int _endExIDX, int _thdIDX, int _taskToDo, String _datatype)
		Geom_PlaneObjBuilder rnr = new Geom_PlaneObjBuilder(msgObj, 0, numObjs, 0, taskType, dataTypeName);
		try {			rnr.call();		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * after either MT or ST execution, final execution to perform
	 */
	@Override
	protected void runMe_Indiv_End() {
		// TODO Auto-generated method stub

	}

}
