package SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading;

import java.util.concurrent.Callable;

import base_Utils_Objects.io.MessageObject;

/**
 * Instances of this class will execute some portion of work
 * @author john
 *
 */
public abstract class SOM_GeomObjBuilder implements Callable<Boolean> {
	protected final MessageObject msgObj;
	protected final int stIdx, endIdx, thdIDX, progressBnd;

	protected final String dataType;
	protected static final float progAmt = .2f;
	protected double progress = -progAmt;
	protected final int taskToDo;

	public SOM_GeomObjBuilder(MessageObject _msgObj, int _stExIDX, int _endExIDX, int _thdIDX, int _taskToDo, String _datatype) {
		msgObj = _msgObj;
		stIdx = _stExIDX;
		endIdx = _endExIDX;
		progressBnd = (int) ((endIdx-stIdx) * progAmt);
		thdIDX= _thdIDX;
		taskToDo = _taskToDo;
		dataType = _datatype;		
	}//ctor
	
	
	protected void incrProgress(int idx) {
		if(((idx-stIdx) % progressBnd) == 0) {		
			progress += progAmt;	
			msgObj.dispInfoMessage("MapFtrCalc","incrProgress::thdIDX=" + String.format("%02d", thdIDX)+" ", "Progress for saving BMU mappings for examples of type : " +dataType +" at : " + String.format("%.2f",progress));
		}
		if(progress > 1.0) {progress = 1.0;}
	}

	public double getProgress() {	return progress;}


}//SOM_GeomObjBuilder
