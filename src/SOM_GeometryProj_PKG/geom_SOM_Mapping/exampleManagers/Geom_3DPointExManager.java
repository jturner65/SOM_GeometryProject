package SOM_GeometryProj_PKG.geom_SOM_Mapping.exampleManagers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_3DPointSOMExample;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers.Geom_3DPointMapMgr;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.procData_loaders.Geom_3DPointCSVDataLoader;
import SOM_GeometryProj_PKG.geom_Utils.trainDataGen.callables.Geom_3DPointTrainDatBuilder;
import base_SOM_Objects.som_examples.base.SOM_Example;
import base_SOM_Objects.som_examples.enums.SOM_ExDataType;
import base_SOM_Objects.som_fileIO.SOM_ExCSVDataLoader;
import base_SOM_Objects.som_geom.geom_examples.SOM_GeomExampleManager;
import base_SOM_Objects.som_geom.geom_examples.SOM_GeomTrainingExUniqueID;
import base_SOM_Objects.som_geom.geom_utils.geom_objs.SOM_GeomSamplePointf;
import base_SOM_Objects.som_geom.geom_utils.geom_threading.trainDataGen.SOM_GeomTrainExBuilder;
import base_SOM_Objects.som_managers.SOM_MapManager;

public class Geom_3DPointExManager extends SOM_GeomExampleManager {

	public Geom_3DPointExManager(SOM_MapManager _mapMgr, String _exName, String _longExampleName,SOM_ExDataType _curDataType, boolean _shouldValidate, String _exMgrName) {
		super(_mapMgr, _exName, _longExampleName, _curDataType, _shouldValidate, _exMgrName);
	}

	@Override
	protected SOM_Example[] noValidateBuildExampleArray() {return (Geom_3DPointSOMExample[])(exampleMap.values().toArray(new Geom_3DPointSOMExample[0]));		}
	@Override
	protected SOM_Example[] castArray(ArrayList<SOM_Example> tmpList) {return (Geom_3DPointSOMExample[])(tmpList.toArray(new Geom_3DPointSOMExample[0]));		}

	/**
	 * with given # of samples to choose from, how many unique samples can be drawn? - only 1 per sample
	 */
	@Override
	protected long getMaxNumUniqueTrainingEx(long ttlNumSamples) {	return ttlNumSamples;}

	/**
	 * build a single list of sorted, unique idxs in allSamples that satisfy object creation constraints
	 * @param allSamples list of all object samples available
	 * @param rnd the current thread's rng engine
	 * @return sorted list of idxs - can only be samples
	 */
	@Override
	protected Integer[] genUniqueObjIDXs(SOM_GeomSamplePointf[] allSamples, ThreadLocalRandom rnd) {
		//samples are objects, so just get a random sample
		return new Integer[] {rnd.nextInt(0,allSamples.length)};
	}

	@Override
	protected void buildAllEx_MT(SOM_GeomSamplePointf[] allSamples, int numThdCallables, int ttlNumTrainEx,
			SOM_GeomTrainingExUniqueID[] _idxsToUse) {
		List<Future<Boolean>> trainDataBldFtrs = new ArrayList<Future<Boolean>>();
		List<SOM_GeomTrainExBuilder> trainDataBldrs = new ArrayList<SOM_GeomTrainExBuilder>();
		
		int numPerThd = calcNumPerThd(ttlNumTrainEx, numThdCallables);
		int stIDX = 0, endIDX = numPerThd;		
		for (int i=0; i<numThdCallables-1;++i) {				
			trainDataBldrs.add(new Geom_3DPointTrainDatBuilder((Geom_3DPointMapMgr) mapMgr, this, allSamples, new int[] {stIDX,endIDX,i, ttlNumTrainEx, numThdCallables},_idxsToUse));
			stIDX =endIDX;
			endIDX += numPerThd;
		}
		if (stIDX < ttlNumTrainEx -1) {
			trainDataBldrs.add(new Geom_3DPointTrainDatBuilder((Geom_3DPointMapMgr) mapMgr, this, allSamples, new int[] {stIDX,ttlNumTrainEx,numThdCallables-1, ttlNumTrainEx, numThdCallables},_idxsToUse));
		}
		try {trainDataBldFtrs = th_exec.invokeAll(trainDataBldrs);for(Future<Boolean> f: trainDataBldFtrs) { 			f.get(); 		}} catch (Exception e) { e.printStackTrace(); }			
	}

	@Override
	protected void buildMTLoader(String[] loadSrcFNamePrefixAra, int numPartitions) {
		List<Future<Boolean>> preProcLoadFtrs = new ArrayList<Future<Boolean>>();
		List<SOM_ExCSVDataLoader> preProcLoaders = new ArrayList<SOM_ExCSVDataLoader>();

		for (int i=0; i<numPartitions;++i) {	preProcLoaders.add(new Geom_3DPointCSVDataLoader((Geom_3DPointMapMgr) mapMgr, i, loadSrcFNamePrefixAra[0]+"_"+i+".csv",  exampleName+ " Data file " + i +" of " +numPartitions + " loaded",  exampleName+ " Data File " + i +" of " +numPartitions +" Failed to load", curDataType, exampleMap));}	
		try {preProcLoadFtrs = th_exec.invokeAll(preProcLoaders);for(Future<Boolean> f: preProcLoadFtrs) { 			f.get(); 		}} catch (Exception e) { e.printStackTrace(); }					

	}//buildMTLoader

	@Override
	protected SOM_Example buildSingleExample(String _oid, String _str) {return new Geom_3DPointSOMExample((Geom_3DPointMapMgr)mapMgr, curDataType, _oid, _str);}

}//class Geom_3DPointExManager
