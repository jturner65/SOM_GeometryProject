package SOM_GeometryProj_PKG.som_geom.geom_examples;

import java.util.ArrayList;

import SOM_GeometryProj_PKG.som_geom.geom_UI.SOM_AnimWorldWin;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading.SOM_GeomObjBldrRunner;
import base_SOM_Objects.SOM_MapManager;
import base_SOM_Objects.som_examples.SOM_ExDataType;
import base_SOM_Objects.som_examples.SOM_Example;
import base_SOM_Objects.som_examples.SOM_ExampleManager;
/**
 * example manager base class for Geometry-based SOM projects
 * @author john
 *
 */
public abstract class SOM_GeomExampleManager extends SOM_ExampleManager {
	 /**
	  * runnable object to manage various tasks
	  */
	protected SOM_GeomObjBldrRunner objRunner;
	/**
	 * anim window the objects in this mapper will be rendered in
	 */
	protected SOM_AnimWorldWin animWin;
	/**
	 * set this to data type being managed by this example manager (training, validation, etc) 
	 */
	protected final SOM_ExDataType curDataType;

	public SOM_GeomExampleManager(SOM_MapManager _mapMgr, String _exName, String _longExampleName, SOM_ExDataType _curDataType, boolean _shouldValidate) {
		super(_mapMgr, _exName, _longExampleName, new boolean[] {_shouldValidate, true});	
		curDataType = _curDataType;
	}
	
	/**
	 * set obj runner so that example manager can consume it
	 * @param _objRunner
	 */
	public void setObjRunner(SOM_GeomObjBldrRunner _objRunner) {objRunner=_objRunner;}
	
	/**
	 * set anim world window that the examples managed by this object will be rendered within
	 * @param the window 
	 */
	public void setAnimWorldWin(SOM_AnimWorldWin _win) {animWin = _win;}
	
	/**
	 * no need to validate examples for this kind of project
	 */
	@Override
	protected final void validateAndAddExToArray(ArrayList<SOM_Example> tmpList, SOM_Example ex) {tmpList.add(ex);	}

	@Override
	/**
	 * after example array has been built, and specific funcitonality for these types of examples, especially if validation should occur
	 */
	protected final void buildExampleArrayEnd_Priv(boolean validate) {}

	/**
	 * return array of examples to save their bmus
	 * @return
	 */
	@Override
	protected SOM_Example[] getExToSave() {
		if(!isExampleArrayBuilt()) {		buildExampleArray();	}	
		msgObj.dispInfoMessage("Geom_SOMExampleManager::"+exampleName,"getExToSave","Size of exToSaveBMUs : " + SOMexampleArray.length);
		return SOMexampleArray;
	}


}//class SOM_GeomExampleManager
