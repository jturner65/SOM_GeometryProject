package SOM_GeometryProj_PKG.geom_Objects.builders.base;


import java.util.concurrent.ExecutorService;

import SOM_GeometryProj_PKG.geom_UI.base.SOM_AnimWorldWin;
import base_Utils_Objects.io.MessageObject;
import base_Utils_Objects.threading.runners.myThreadRunner;

public abstract class SOM_GeomObjBuilder extends myThreadRunner {
	
	
	public SOM_GeomObjBuilder(MessageObject _msgObj, ExecutorService _th_exec, boolean _canMT, int _numThds, int _numWorkUnits) {
		super(_msgObj, _th_exec, _canMT, _numThds, _numWorkUnits);
	}
	/**
	 * owning window
	 */
	protected SOM_AnimWorldWin win;
	/**
	 * number of objects to synthesize
	 */
	protected int numObjs;
	/**
	 * callables to build objects
	 */
	
	
	
//	public SOM_GeomObjBuilder(SOM_AnimWorldWin _win, int _num) {
//		win = _win;
//		numObjs = _num;
//	}
	
	public void setNumObjs(int _num) {	numObjs = _num;}


	


}//class SOM_GeomObjBuilder
