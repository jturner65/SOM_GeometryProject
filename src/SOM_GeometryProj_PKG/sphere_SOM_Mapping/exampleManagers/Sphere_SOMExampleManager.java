package SOM_GeometryProj_PKG.sphere_SOM_Mapping.exampleManagers;

import java.util.ArrayList;

import base_SOM_Objects.SOM_MapManager;
import base_SOM_Objects.som_examples.SOM_Example;
import base_SOM_Objects.som_examples.SOM_ExampleManager;

public class Sphere_SOMExampleManager extends SOM_ExampleManager {

	public Sphere_SOMExampleManager(SOM_MapManager _mapMgr, String _exName, String _longExampleName,boolean _shouldValidate) {
		super(_mapMgr, _exName, _longExampleName, _shouldValidate);		
	}

	@Override
	protected void reset_Priv() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void buildFtrVec_Priv() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void buildAfterAllFtrVecsBuiltStructs_Priv() {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadAllPreProccedMapData(String subDir) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean saveAllPreProccedMapData() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void validateAndAddExToArray(ArrayList<SOM_Example> tmpList, SOM_Example ex) {
		// TODO Auto-generated method stub

	}

	@Override
	protected SOM_Example[] castArray(ArrayList<SOM_Example> tmpList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected SOM_Example[] noValidateBuildExampleArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void buildExampleArrayEnd_Priv(boolean validate) {
		// TODO Auto-generated method stub

	}

	@Override
	protected SOM_Example[] getExToSave() {
		if(!isExampleArrayBuilt()) {		buildExampleArray();	}	
		msgObj.dispInfoMessage("Sphere_SOMExampleManager","getExToSave","Size of exToSaveBMUs : " + SOMexampleArray.length);
		return SOMexampleArray;
	}

}
