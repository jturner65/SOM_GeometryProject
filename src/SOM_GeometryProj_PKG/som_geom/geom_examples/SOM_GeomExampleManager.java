package SOM_GeometryProj_PKG.som_geom.geom_examples;

import java.util.ArrayList;

import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading.SOM_GeomObjBldrRunner;
import base_SOM_Objects.SOM_MapManager;
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

	public SOM_GeomExampleManager(SOM_MapManager _mapMgr, String _exName, String _longExampleName, boolean _shouldValidate) {
		super(_mapMgr, _exName, _longExampleName, new boolean[] {_shouldValidate, true});	
	}
	
	/**
	 * set obj runner so that example manager can consume it
	 * @param _objRunner
	 */
	public void setObjRunner(SOM_GeomObjBldrRunner _objRunner) {objRunner=_objRunner;}
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


}
