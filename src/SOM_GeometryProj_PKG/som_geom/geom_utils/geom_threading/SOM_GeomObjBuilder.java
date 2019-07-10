package SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading;

import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

import SOM_GeometryProj_PKG.som_geom.SOM_GeomMapManager;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomObj;
import base_Utils_Objects.io.MessageObject;
import base_Utils_Objects.vectorObjs.myPointf;
import base_Utils_Objects.vectorObjs.myVectorf;

/**
 * Instances of this class will execute some portion of work
 * @author john
 *
 */
public abstract class SOM_GeomObjBuilder implements Callable<Boolean> {
	protected final SOM_GeomMapManager mapMgr;
	protected final MessageObject msgObj;
	protected final int stIdx, endIdx, thdIDX, numSmplsPerObj;	

	protected final String dataType;
	
	protected final int progressBnd;
	protected static final float progAmt = .2f;
	protected double progress = -progAmt;
	protected final SOM_GeomObjBldrTasks taskToDo;
	/**
	 * ref to array of data to be processed - should be predefined/prebuilt and just populated in runner
	 */
	protected SOM_GeomObj[] objArray;
	/**
	 * coordinate bounds in world 
	 * 		first idx : 0 is min; 1 is diff
	 * 		2nd idx : 0 is x, 1 is y, 2 is z
	 */
	protected final float[][] worldBounds;	
	/**
	 * 
	 * @param _mapMgr map manager for this builder
	 * @param _intVals : array holding (idx 0 : stIdx, idx 1 : endIdx, idx 2 : thdIDX, idx 3 : numSmplsPerObj)
	 * @param _endExIDX
	 * @param _thdIDX
	 * @param _taskToDo
	 * @param _datatype
	 * @param _worldBounds 	coordinate bounds in world 
	 * 		first idx : 0 is min; 1 is diff
	 * 		2nd idx : 0 is x, 1 is y, 2 is z (if used)
	 */
	public SOM_GeomObjBuilder(SOM_GeomMapManager _mapMgr, SOM_GeomObj[] _objArray, int[] _intVals, SOM_GeomObjBldrTasks _taskToDo, String _datatype, float[][] _worldBounds) {
		mapMgr = _mapMgr;
		msgObj = mapMgr.buildMsgObj();
		
		stIdx = _intVals[0];
		endIdx = _intVals[1];
		thdIDX= _intVals[2];
		numSmplsPerObj = _intVals[3];
		
		progressBnd = (int) ((endIdx-stIdx) * progAmt);
		taskToDo = _taskToDo;
		dataType = _datatype;		
		objArray = _objArray;
		worldBounds = new float[_worldBounds.length][];
		for(int i=0;i<worldBounds.length;++i) {
			float[] tmp = new float[_worldBounds[i].length];
			for(int j=0;j<tmp.length;++j) {	tmp[j]=_worldBounds[i][j];}
			worldBounds[i]=tmp;
		}
		
	}//ctor
	
	protected myPointf getRandPointInBounds_2D() {
		myPointf x = new myPointf( 
				(ThreadLocalRandom.current().nextFloat() *worldBounds[1][0])+worldBounds[0][0], 
				(ThreadLocalRandom.current().nextFloat() *worldBounds[1][1])+worldBounds[0][1],0);
		return x;
	}
	

	protected myPointf getRandPointInBounds_3D() {
		myPointf x = new myPointf( 
				(ThreadLocalRandom.current().nextFloat() *worldBounds[1][0])+worldBounds[0][0], 
				(ThreadLocalRandom.current().nextFloat() *worldBounds[1][1])+worldBounds[0][1],
				(ThreadLocalRandom.current().nextFloat() *worldBounds[1][2])+worldBounds[0][2]);
		return x;
	}
	
	protected myPointf getRandPointInBounds_3D(float bnd) {
		float tbnd = 2.0f*bnd;
		myPointf x = new myPointf( 
				(ThreadLocalRandom.current().nextFloat() *(worldBounds[1][0]-tbnd))+worldBounds[0][0]+bnd, 
				(ThreadLocalRandom.current().nextFloat() *(worldBounds[1][1]-tbnd))+worldBounds[0][1]+bnd,
				(ThreadLocalRandom.current().nextFloat() *(worldBounds[1][2]-tbnd))+worldBounds[0][2]+bnd);
		return x;
	}
	
	protected myVectorf getRandNormal_3D() {
		myVectorf x = new myVectorf( 
				(ThreadLocalRandom.current().nextFloat() *2.0f)-1.0f, 
				(ThreadLocalRandom.current().nextFloat() *2.0f)-1.0f,
				(ThreadLocalRandom.current().nextFloat() *2.0f)-1.0f);		
		return x._normalized();
	}
	

	
	protected final void incrProgress(int idx) {
		if(((idx-stIdx) % progressBnd) == 0) {		
			progress += progAmt;	
			msgObj.dispInfoMessage("SOM_GeomObjBuilder","incrProgress::thdIDX=" + String.format("%02d", thdIDX)+" ", "Progress for saving BMU mappings for examples of type : " +dataType +" at : " + String.format("%.2f",progress));
		}
		if(progress > 1.0) {progress = 1.0;}
	}

	public final double getProgress() {	return progress;}
	
	/**
	 * build objects
	 */
	protected void _buildTask() {
		msgObj.dispInfoMessage("SOM_GeomObjBuilder", "_buildTask::thdIDX=", "Start building " + (endIdx-stIdx) + " " +dataType +" objects at idxs : ["+stIdx+", "+endIdx+"]");
		for(int i=stIdx; i<endIdx;++i) {	objArray[i]=_buildSingleObject(i);	}
		msgObj.dispInfoMessage("SOM_GeomObjBuilder", "_buildTask::thdIDX=", "Finished building " + (endIdx-stIdx) + " " +dataType +" objects at idxs : ["+stIdx+", "+endIdx+"]");
	}
	/**
	 * build a single object to be stored at idx
	 * @param idx idx of object in resultant array
	 * @return build object
	 */
	protected abstract SOM_GeomObj _buildSingleObject(int idx);

	@Override
	public final Boolean call() throws Exception {
		switch (taskToDo) {
			case buildObj : { _buildTask(); break;}
			default :{
				msgObj.dispErrorMessage("SOM_GeomObjBuilder", "call::thdIDX=", "Unsupported task chosen : " + taskToDo.toString() +".  Aborting");
			}
		}
		return true;
	}
	

}//SOM_GeomObjBuilder
