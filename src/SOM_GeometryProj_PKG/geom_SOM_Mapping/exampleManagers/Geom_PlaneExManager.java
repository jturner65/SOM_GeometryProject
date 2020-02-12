package SOM_GeometryProj_PKG.geom_SOM_Mapping.exampleManagers;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_PlaneSOMExample;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers.Geom_PlaneMapMgr;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.procData_loaders.Geom_PlaneCSVDataLoader;
import SOM_GeometryProj_PKG.geom_Utils.trainDataGen.callables.Geom_PlaneTrainDatBuilder;
import base_Math_Objects.vectorObjs.floats.myPointf;
import base_Math_Objects.vectorObjs.floats.myVectorf;
import base_SOM_Objects.SOM_MapManager;
import base_SOM_Objects.som_examples.SOM_ExDataType;
import base_SOM_Objects.som_examples.SOM_Example;
import base_SOM_Objects.som_fileIO.SOM_ExCSVDataLoader;
import base_SOM_Objects.som_geom.geom_examples.SOM_GeomExampleManager;
import base_SOM_Objects.som_geom.geom_examples.SOM_GeomTrainingExUniqueID;
import base_SOM_Objects.som_geom.geom_utils.geom_objs.SOM_GeomSamplePointf;
import base_SOM_Objects.som_geom.geom_utils.geom_threading.trainDataGen.SOM_GeomTrainExBuilder;

public class Geom_PlaneExManager extends SOM_GeomExampleManager {

	
	public Geom_PlaneExManager(SOM_MapManager _mapMgr, String _exName, String _longExampleName, SOM_ExDataType _curDataType, boolean _shouldValidate, String _exMgrName) {
		super(_mapMgr, _exName, _longExampleName, _curDataType, _shouldValidate, _exMgrName);
	}
	
	
	@Override
	protected SOM_Example[] noValidateBuildExampleArray() {return (Geom_PlaneSOMExample[])(exampleMap.values().toArray(new Geom_PlaneSOMExample[0]));		}
	@Override
	protected SOM_Example[] castArray(ArrayList<SOM_Example> tmpList) {return (Geom_PlaneSOMExample[])(tmpList.toArray(new Geom_PlaneSOMExample[0]));		}
	
	@Override
	protected void buildAllEx_MT(SOM_GeomSamplePointf[] allSamples, int numThdCallables, int ttlNumTrainEx, SOM_GeomTrainingExUniqueID[] _idxsToUse) {
		List<Future<Boolean>> trainDataBldFtrs = new ArrayList<Future<Boolean>>();
		List<SOM_GeomTrainExBuilder> trainDataBldrs = new ArrayList<SOM_GeomTrainExBuilder>();
		
		//SOM_GeomMapManager _mapMgr, SOM_GeomExampleManager _exMgr,SOM_GeomSamplePointf[] _allExs, int[] _intVals
		//for (int i=0; i<numThdCallables;++i) {	trainDataBldrs.add(new Geom_PlaneTrainDatBuilder((Geom_PlaneMapMgr) mapMgr, this, allSamples, new int[] {0,allSamples.length,i, numTtlToBuild, numThdCallables}));}
		//int numVals, int numThds
		int numPerThd = calcNumPerThd(ttlNumTrainEx, numThdCallables);
		//SOM_GeomMapManager _mapMgr, SOM_GeomExampleManager _exMgr,SOM_GeomSamplePointf[] _allExs, int[] _intVals
		int stIDX = 0, endIDX = numPerThd;
		
		for (int i=0; i<numThdCallables-1;++i) {				
			trainDataBldrs.add(new Geom_PlaneTrainDatBuilder((Geom_PlaneMapMgr) mapMgr, this, allSamples, new int[] {stIDX,endIDX,i, ttlNumTrainEx, numThdCallables},_idxsToUse));
			stIDX =endIDX;
			endIDX += numPerThd;
		}
		trainDataBldrs.add(new Geom_PlaneTrainDatBuilder((Geom_PlaneMapMgr) mapMgr, this, allSamples, new int[] {stIDX,ttlNumTrainEx,numThdCallables-1, ttlNumTrainEx, numThdCallables},_idxsToUse));		
		try {trainDataBldFtrs = th_exec.invokeAll(trainDataBldrs);for(Future<Boolean> f: trainDataBldFtrs) { 			f.get(); 		}} catch (Exception e) { e.printStackTrace(); }			
	}
	
	/**
	 * with given # of samples to choose from, how many unique samples can be drawn?
	 */
	@Override
	protected long getMaxNumUniqueTrainingEx(long ttlNumSamples) {	return (ttlNumSamples *(ttlNumSamples-1L)*(ttlNumSamples-2L))/6L;}

	/**
	 * build a single list of sorted, unique idxs in allSamples that satisfy object creation constraints
	 * @param allSamples list of all object samples available
	 * @param rnd the current thread's rng engine
	 * @return sorted list of idxs
	 */
	@Override
	protected Integer[] genUniqueObjIDXs(SOM_GeomSamplePointf[] allSamples, ThreadLocalRandom rnd){
		TreeSet<Integer> idxs = new TreeSet<Integer>();
		//1st 2 points are always ok
		while(idxs.size() < 2) {	idxs.add(rnd.nextInt(0,allSamples.length));}
		int firstIDX = idxs.first();
		int scndIDX = idxs.last();
		//find 3rd point
		myPointf a = allSamples[firstIDX];
		myVectorf ab = new myVectorf(a,allSamples[scndIDX]);
		ab._normalize();
		myVectorf ac;
		SOM_GeomSamplePointf c;
		int cIDX;
		do {
			cIDX = firstIDX;
			while((cIDX==firstIDX) || (cIDX==scndIDX)){	cIDX = rnd.nextInt(0,allSamples.length);}		

			c = allSamples[cIDX];
			ac = new myVectorf(a,c);
			ac._normalize();			
		} while (Math.abs(ab._dot(ac))==1.0f);
		idxs.add(cIDX);
//		if(idxs.size()<3) {
//			this.msgObj.dispErrorMessage("Geom_PlaneExManager", "genUniqueObjIDXs", " Error!!! somehow did not generate 3 unique plane points : " + idxs.size() + " firstIDX : " + firstIDX + " scndIDX : " + scndIDX + " cIDX : "+ cIDX);
//		}
		return idxs.toArray(new Integer[0]);
	}//genUniqueObjIDXs

	@Override
	protected void buildMTLoader(String[] loadSrcFNamePrefixAra, int numPartitions) {
		List<Future<Boolean>> preProcLoadFtrs = new ArrayList<Future<Boolean>>();
		List<SOM_ExCSVDataLoader> preProcLoaders = new ArrayList<SOM_ExCSVDataLoader>();
		//(SOM_GeomMapManager _mapMgr, int _thdIDX, String _fileName, String _yStr, String _nStr, SOM_ExDataType _exType, ConcurrentSkipListMap<String, SOM_Example> _mapToAddTo)
		for (int i=0; i<numPartitions;++i) {	preProcLoaders.add(new Geom_PlaneCSVDataLoader((Geom_PlaneMapMgr) mapMgr, i, loadSrcFNamePrefixAra[0]+"_"+i+".csv",  exampleName+ " Data file " + i +" of " +numPartitions + " loaded",  exampleName+ " Data File " + i +" of " +numPartitions +" Failed to load", curDataType, exampleMap));}	
		try {preProcLoadFtrs = th_exec.invokeAll(preProcLoaders);for(Future<Boolean> f: preProcLoadFtrs) { 			f.get(); 		}} catch (Exception e) { e.printStackTrace(); }					
	}

	@Override
	protected SOM_Example buildSingleExample(String _oid, String _str) {return new Geom_PlaneSOMExample(((Geom_PlaneMapMgr)mapMgr), curDataType, _oid, _str);}


}
