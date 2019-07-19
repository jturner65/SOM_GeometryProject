package SOM_GeometryProj_PKG.geom_SOM_Mapping.exampleManagers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_LineSOMExample;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers.Geom_LineMapMgr;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.procData_loaders.Geom_LineCSVDataLoader;
import SOM_GeometryProj_PKG.geom_Utils.trainDataGen.callables.Geom_LineTrainDatBuilder;
import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomExampleManager;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading.trainDataGen.SOM_GeomTrainExBuilder;
import base_SOM_Objects.SOM_MapManager;
import base_SOM_Objects.som_examples.SOM_ExDataType;
import base_SOM_Objects.som_examples.SOM_Example;
import base_SOM_Objects.som_fileIO.SOM_ExCSVDataLoader;

public class Geom_LineExManager extends SOM_GeomExampleManager {
	
	public Geom_LineExManager(SOM_MapManager _mapMgr, String _exName, String _longExampleName,  SOM_ExDataType _curDataType, boolean _shouldValidate) {
		super(_mapMgr, _exName, _longExampleName,  _curDataType,  _shouldValidate);
	}
	

	/**
	 * no need to validate examples for this kind of project - shouldn't get called
	 */
	@Override
	protected final void validateAndAddExToArray(ArrayList<SOM_Example> tmpList, SOM_Example ex) {tmpList.add(ex);	}
	@Override
	protected SOM_Example[] noValidateBuildExampleArray() {return (Geom_LineSOMExample[])(exampleMap.values().toArray(new Geom_LineSOMExample[0]));		}
	@Override
	protected SOM_Example[] castArray(ArrayList<SOM_Example> tmpList) {return (Geom_LineSOMExample[])(tmpList.toArray(new Geom_LineSOMExample[0]));		}

	@Override
	protected void buildAfterAllFtrVecsBuiltStructs_Priv() {
	}
	
	@Override
	protected void buildAllEx_MT(int numThdCallables, int ttlNumTrainEx) {
		List<Future<Boolean>> trainDataBldFtrs = new ArrayList<Future<Boolean>>();
		List<SOM_GeomTrainExBuilder> trainDataBldrs = new ArrayList<SOM_GeomTrainExBuilder>();
		
		//SOM_GeomMapManager _mapMgr, SOM_GeomExampleManager _exMgr,SOM_GeomSmplDataForEx[] _allExs, int[] _intVals
		for (int i=0; i<numThdCallables;++i) {	trainDataBldrs.add(new Geom_LineTrainDatBuilder((Geom_LineMapMgr) mapMgr, this, allSamples, new int[] {0,allSamples.length,i, ttlNumTrainEx, numThdCallables}));}
		
		try {trainDataBldFtrs = th_exec.invokeAll(trainDataBldrs);for(Future<Boolean> f: trainDataBldFtrs) { 			f.get(); 		}} catch (Exception e) { e.printStackTrace(); }					
		
	}
	
	@Override
	protected void buildMTLoader(String[] loadSrcFNamePrefixAra, int numPartitions) {
		List<Future<Boolean>> preProcLoadFtrs = new ArrayList<Future<Boolean>>();
		List<SOM_ExCSVDataLoader> preProcLoaders = new ArrayList<SOM_ExCSVDataLoader>();
		//(SOM_GeomMapManager _mapMgr, int _thdIDX, String _fileName, String _yStr, String _nStr, SOM_ExDataType _exType, ConcurrentSkipListMap<String, SOM_Example> _mapToAddTo)
		for (int i=0; i<numPartitions;++i) {	preProcLoaders.add(new Geom_LineCSVDataLoader((Geom_LineMapMgr) mapMgr, i, loadSrcFNamePrefixAra[0]+"_"+i+".csv",  exampleName+ " Data file " + i +" of " +numPartitions + " loaded",  exampleName+ " Data File " + i +" of " +numPartitions +" Failed to load", curDataType, exampleMap));}	
		try {preProcLoadFtrs = th_exec.invokeAll(preProcLoaders);for(Future<Boolean> f: preProcLoadFtrs) { 			f.get(); 		}} catch (Exception e) { e.printStackTrace(); }					
	}

	@Override
	protected SOM_Example buildSingleExample(String _oid, String _str) {
		return new Geom_LineSOMExample((Geom_LineMapMgr)mapMgr, curDataType, _oid, _str);
	}

}//class Geom_LineSOMExampleManager
