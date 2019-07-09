package SOM_GeometryProj_PKG.som_geom.geom_examples;

import base_SOM_Objects.SOM_MapManager;
import base_SOM_Objects.som_examples.SOM_Example;
import base_SOM_Objects.som_examples.SOM_ExampleManager;
/**
 * example manager base class for Geometry-based SOM projects
 * @author john
 *
 */
public abstract class SOM_GeomExampleManager extends SOM_ExampleManager {

	public SOM_GeomExampleManager(SOM_MapManager _mapMgr, String _exName, String _longExampleName,boolean _shouldValidate) {
		super(_mapMgr, _exName, _longExampleName, new boolean[] {_shouldValidate, true});		
	}

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
