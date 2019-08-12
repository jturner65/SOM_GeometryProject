package SOM_GeometryProj_PKG.geom_SOM_Mapping.exampleManagers;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_2DLineSOMExample;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers.Geom_2DLineMapMgr;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.procData_loaders.Geom_2DLineCSVDataLoader;
import SOM_GeometryProj_PKG.geom_Utils.trainDataGen.callables.Geom_2DLineTrainDatBuilder;
import base_SOM_Objects.SOM_MapManager;
import base_SOM_Objects.som_examples.SOM_ExDataType;
import base_SOM_Objects.som_examples.SOM_Example;
import base_SOM_Objects.som_fileIO.SOM_ExCSVDataLoader;
import base_SOM_Objects.som_geom.geom_examples.SOM_GeomExampleManager;
import base_SOM_Objects.som_geom.geom_examples.SOM_GeomTrainingExUniqueID;
import base_SOM_Objects.som_geom.geom_utils.geom_objs.SOM_GeomSamplePointf;
import base_SOM_Objects.som_geom.geom_utils.geom_threading.trainDataGen.SOM_GeomTrainExBuilder;

public class Geom_2DLineExManager extends SOM_GeomExampleManager {
	
	public Geom_2DLineExManager(SOM_MapManager _mapMgr, String _exName, String _longExampleName,  SOM_ExDataType _curDataType, boolean _shouldValidate, String _exMgrName) {
		super(_mapMgr, _exName, _longExampleName,  _curDataType,  _shouldValidate, _exMgrName);
	}
	

	@Override
	protected SOM_Example[] noValidateBuildExampleArray() {return (Geom_2DLineSOMExample[])(exampleMap.values().toArray(new Geom_2DLineSOMExample[0]));		}
	@Override
	protected SOM_Example[] castArray(ArrayList<SOM_Example> tmpList) {return (Geom_2DLineSOMExample[])(tmpList.toArray(new Geom_2DLineSOMExample[0]));		}
	
	@Override
	protected void buildAllEx_MT(SOM_GeomSamplePointf[] allSamples, int numThdCallables, int ttlNumTrainEx,SOM_GeomTrainingExUniqueID[] _idxsToUse) {
		List<Future<Boolean>> trainDataBldFtrs = new ArrayList<Future<Boolean>>();
		List<SOM_GeomTrainExBuilder> trainDataBldrs = new ArrayList<SOM_GeomTrainExBuilder>();
		//int numVals, int numThds
		int numPerThd = calcNumPerThd(ttlNumTrainEx, numThdCallables);
		//SOM_GeomMapManager _mapMgr, SOM_GeomExampleManager _exMgr,SOM_GeomSmplDataForEx[] _allExs, int[] _intVals
		int stIDX = 0, endIDX = numPerThd;
		
		for (int i=0; i<numThdCallables-1;++i) {				
			trainDataBldrs.add(new Geom_2DLineTrainDatBuilder((Geom_2DLineMapMgr) mapMgr, this, allSamples, new int[] {stIDX,endIDX,i, ttlNumTrainEx, numThdCallables},_idxsToUse));
			stIDX =endIDX;
			endIDX += numPerThd;
		}
		trainDataBldrs.add(new Geom_2DLineTrainDatBuilder((Geom_2DLineMapMgr) mapMgr, this, allSamples, new int[] {stIDX,ttlNumTrainEx,numThdCallables-1, ttlNumTrainEx, numThdCallables},_idxsToUse));
		try {trainDataBldFtrs = th_exec.invokeAll(trainDataBldrs);for(Future<Boolean> f: trainDataBldFtrs) { 			f.get(); 		}} catch (Exception e) { e.printStackTrace(); }			
	}
	
	/**
	 * with given # of samples to choose from, how many unique samples can be drawn? - 2 point lines
	 */
	@Override
	protected long getMaxNumUniqueTrainingEx(long ttlNumSamples) {return (ttlNumSamples *(ttlNumSamples-1L))/2L;}
	
	/**
	 * build a single list of sorted, unique idxs in allSamples that satisfy object creation constraints
	 * @param allSamples list of all object samples available
	 * @param rnd the current thread's rng engine
	 * @return sorted list of idxs
	 */
	@Override
	protected Integer[] genUniqueObjIDXs(SOM_GeomSamplePointf[] allSamples, ThreadLocalRandom rnd){
		TreeSet<Integer> idxs = new TreeSet<Integer>();
		//any pair of samples create a line, so just need a unique pair
		while(idxs.size() < 2) {	idxs.add(rnd.nextInt(0,allSamples.length));}		
		return idxs.toArray(new Integer[0]);
	}//genUniqueObjIDXs
	
	@Override
	protected void buildMTLoader(String[] loadSrcFNamePrefixAra, int numPartitions) {
		List<Future<Boolean>> preProcLoadFtrs = new ArrayList<Future<Boolean>>();
		List<SOM_ExCSVDataLoader> preProcLoaders = new ArrayList<SOM_ExCSVDataLoader>();
		//(SOM_GeomMapManager _mapMgr, int _thdIDX, String _fileName, String _yStr, String _nStr, SOM_ExDataType _exType, ConcurrentSkipListMap<String, SOM_Example> _mapToAddTo)
		for (int i=0; i<numPartitions;++i) {	preProcLoaders.add(new Geom_2DLineCSVDataLoader((Geom_2DLineMapMgr) mapMgr, i, loadSrcFNamePrefixAra[0]+"_"+i+".csv",  exampleName+ " Data file " + i +" of " +numPartitions + " loaded",  exampleName+ " Data File " + i +" of " +numPartitions +" Failed to load", curDataType, exampleMap));}	
		try {preProcLoadFtrs = th_exec.invokeAll(preProcLoaders);for(Future<Boolean> f: preProcLoadFtrs) { 			f.get(); 		}} catch (Exception e) { e.printStackTrace(); }					
	}

	@Override
	protected SOM_Example buildSingleExample(String _oid, String _str) {return new Geom_2DLineSOMExample((Geom_2DLineMapMgr)mapMgr, curDataType, _oid, _str);}


}//class Geom_LineSOMExampleManager
