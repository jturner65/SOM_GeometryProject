package SOM_GeometryProj_PKG.geom_Utils.geomGen.runners;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers.Geom_LineMapMgr;
import SOM_GeometryProj_PKG.geom_Utils.geomGen.callables.Geom_LineObjBuilder;
import SOM_GeometryProj_PKG.som_geom.SOM_GeomMapManager;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomObj;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading.geomGen.SOM_GeomObjBldrRunner;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading.geomGen.SOM_GeomObjBldrTasks;
import base_SOM_Objects.SOM_MapManager;

public class Geom_LineObjBldrRunner extends SOM_GeomObjBldrRunner {

	public Geom_LineObjBldrRunner(Geom_LineMapMgr _mapMgr, ExecutorService _th_exec, SOM_GeomObj[] _objArray, boolean _forceST, int[] _numVals, SOM_GeomObjBldrTasks _taskType) {
		super(_mapMgr, _th_exec, _objArray, "Lines", _forceST, _numVals, _taskType);
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
		ExMappers.add(new Geom_LineObjBuilder((Geom_LineMapMgr) mapMgr, objArray, new int[] {dataSt, dataEnd, pIdx, numSmplsPerObj}, taskType));
	}
	
	/**
	 * exec entire dataset in one thread
	 */
	@Override
	protected void runMe_Indiv_ST() {
		Geom_LineObjBuilder rnr = new Geom_LineObjBuilder((Geom_LineMapMgr) mapMgr, objArray, new int[] {0, objArray.length, 0,  numSmplsPerObj},taskType);
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
