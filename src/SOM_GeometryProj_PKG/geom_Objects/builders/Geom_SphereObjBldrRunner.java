package SOM_GeometryProj_PKG.geom_Objects.builders;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_runners.SOM_GeomObjBldrRunner;
import base_SOM_Objects.SOM_MapManager;

public class Geom_SphereObjBldrRunner extends SOM_GeomObjBldrRunner {

	public Geom_SphereObjBldrRunner(SOM_MapManager _mapMgr, ExecutorService _th_exec, String _dataTypName,
			boolean _forceST, int _numObjs, int _taskType) {
		super(_mapMgr, _th_exec, _dataTypName, _forceST, _numObjs, _taskType);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void execPerPartition(List<Callable<Boolean>> ExMappers, int dataSt, int dataEnd, int pIdx,
			int ttlParts) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void runMe_Indiv_ST() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void runMe_Indiv_End() {
		// TODO Auto-generated method stub

	}

}
