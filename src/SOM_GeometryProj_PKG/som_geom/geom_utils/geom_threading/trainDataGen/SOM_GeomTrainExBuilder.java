package SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading.trainDataGen;

import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

import SOM_GeometryProj_PKG.som_geom.SOM_GeomMapManager;
import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomExampleManager;
import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomObj;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomSmplDataForEx;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading.base.SOM_GeomCallable;
import base_SOM_Objects.som_examples.SOM_ExDataType;
/**
 * build training data
 * @author john
 *
 */
public abstract class SOM_GeomTrainExBuilder extends SOM_GeomCallable {
	/**
	 * # of examples for each callable of this type to build 
	 */
	protected final int numExToBuildPerThd;
	/**
	 * this is the example manager that will receive the examples synthesized from this callable
	 */
	protected final SOM_GeomExampleManager exMgr;
	
	/**
	 * ref to array of all example objects
	 */
	protected final SOM_GeomSmplDataForEx[] allExamples;
	
	/**
	 * the number of samples required to build an object
	 */
	protected final int numExPerObj;
	

	
	public SOM_GeomTrainExBuilder(SOM_GeomMapManager _mapMgr, SOM_GeomExampleManager _exMgr, SOM_GeomSmplDataForEx[] _allExs, int[] _intVals, String _dataType, int _numExPerObj) {
		super(_mapMgr, _intVals[0],_intVals[1],_intVals[2], _dataType);
		//idxs 0,1,2 are st,end,thrdIDX
		numExToBuildPerThd = calcNumPerThd(_intVals[3], _intVals[4]);
		System.out.println("Building total : " + _intVals[3] + " over "+ _intVals[4] + " threads : " + numExToBuildPerThd + " per thread");
		exMgr = _exMgr;
		allExamples = _allExs;
		numExPerObj = _numExPerObj;
		progressBnd = (int) (numExToBuildPerThd * progAmt);
	}

	private void buildTrainExData() {
		ThreadLocalRandom rnd = ThreadLocalRandom.current();
		SOM_GeomSmplDataForEx[] exAra = new SOM_GeomSmplDataForEx[numExPerObj];
		for(int i=0;i<numExToBuildPerThd;++i) {
			exAra = genPtsForObj(rnd);
			SOM_GeomObj obj = _buildSingleObjectFromSamples(SOM_ExDataType.Training,exAra, i); incrProgress(i,"Building Training Data");
			exMgr.addExampleToMap(obj);		
		}	
	}//buildTrainExData
	
	protected String getObjID(int idx) {return String.format("%05d", (thdIDX * numExToBuildPerThd) + idx);}
	
	/**
	 * for lines just need 2 points; planes need 3 non-colinear points; spheres need 4 non-coplanar points, no 3 of which are colinear
	 * @return
	 */
	protected abstract SOM_GeomSmplDataForEx[] genPtsForObj(ThreadLocalRandom rnd);
	
	protected Integer[] genUniqueIDXs(int numToGen,  ThreadLocalRandom rnd){
		HashSet<Integer> idxs = new HashSet<Integer>();
		while(idxs.size() < numToGen) {	idxs.add(rnd.nextInt(0,allExamples.length));}		
		return idxs.toArray(new Integer[0]);
	}
	//for(int i=0;i<res.length;++i) {	res[i]=allExamples[rnd.nextInt(0,allExamples.length)];}
	//return res;
	
	/**
	 * build a single object to be stored at idx
	 * @param idx idx of object in resultant array
	 * @return build object
	 */
	protected abstract SOM_GeomObj _buildSingleObjectFromSamples(SOM_ExDataType _exDataType, SOM_GeomSmplDataForEx[] exAra, int idx);

	
	@Override
	public Boolean call() throws Exception {
		msgObj.dispInfoMessage("SOM_GeomTrainExBuilder", "call::thdIDX=", "Start building " + numExToBuildPerThd + " " +dataType +" training example objects from geom obj samples.");
		buildTrainExData();
		msgObj.dispInfoMessage("SOM_GeomTrainExBuilder", "call::thdIDX=", "Finished building " + numExToBuildPerThd + " " +dataType +" training example objects from geom obj samples.");
		
		return true;
	}

}//SOM_GeomTrainExBuilder
