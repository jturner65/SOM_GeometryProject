package SOM_GeometryProj_PKG.geom_Utils.geomGen.runners;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers.Geom_3DPointMapMgr;
import SOM_GeometryProj_PKG.geom_Utils.geomGen.callables.Geom_3DPointObjBuilder;
import base_SOM_Objects.som_geom.SOM_GeomMapManager;
import base_SOM_Objects.som_geom.geom_examples.SOM_GeomObj;
import base_SOM_Objects.som_geom.geom_utils.geom_threading.geomGen.SOM_GeomObjBldrRunner;
import base_SOM_Objects.som_geom.geom_utils.geom_threading.geomGen.SOM_GeomObjBldrTasks;

public class Geom_3DPointObjBldrRunner extends SOM_GeomObjBldrRunner {

    public Geom_3DPointObjBldrRunner(SOM_GeomMapManager _mapMgr, ExecutorService _th_exec, SOM_GeomObj[] _objArray,
            boolean _forceST, int[] _numVals, SOM_GeomObjBldrTasks _taskType) {
        super(_mapMgr, _th_exec, _objArray, _forceST, _numVals, _taskType);
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
        ExMappers.add(new Geom_3DPointObjBuilder((Geom_3DPointMapMgr) mapMgr, objArray, new int[] {dataSt, dataEnd, pIdx, numSmplsPerObj}, taskType));
    }
    
    /**
     * exec entire dataset in one thread
     */
    @Override
    protected void runMe_Indiv_ST() {
        Geom_3DPointObjBuilder rnr = new Geom_3DPointObjBuilder((Geom_3DPointMapMgr) mapMgr, objArray, new int[] {0, objArray.length, 0,  numSmplsPerObj},taskType);
        try {            rnr.call();        } 
        catch (Exception e) {            e.printStackTrace();        }
    }
    /**
     * after either MT or ST execution, final execution to perform
     */
    @Override
    protected void runMe_Indiv_End() {}

}//class Geom_3DPointObjBldrRunner
