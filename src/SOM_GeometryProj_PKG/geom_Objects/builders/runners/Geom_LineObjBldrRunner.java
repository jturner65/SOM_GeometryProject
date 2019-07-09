package SOM_GeometryProj_PKG.geom_Objects.builders.runners;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import SOM_GeometryProj_PKG.geom_Objects.builders.callables.Geom_LineObjBuilder;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading.SOM_GeomObjBldrRunner;
import base_SOM_Objects.SOM_MapManager;

public class Geom_LineObjBldrRunner extends SOM_GeomObjBldrRunner {

	public Geom_LineObjBldrRunner(SOM_MapManager _mapMgr, ExecutorService _th_exec, boolean _forceST, int _numObjs, int _taskType) {
		super(_mapMgr, _th_exec, "Lines", _forceST, _numObjs, _taskType);
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
		ExMappers.add(new Geom_LineObjBuilder(msgObj, dataSt, dataEnd, pIdx, taskType, dataTypeName));
	}
	
	/**
	 * exec entire dataset in one thread
	 */
	@Override
	protected void runMe_Indiv_ST() {
		Geom_LineObjBuilder rnr = new Geom_LineObjBuilder(msgObj, 0, numObjs, 0, taskType, dataTypeName);
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

}//class SOM_GeomLineObjBldrRunner
