package SOM_GeometryProj_PKG.som_geom.geom_examples;

import java.util.ArrayList;
import java.util.TreeMap;

import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_LineSOMExample;
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
	public final void setObjRunner(SOM_GeomObjBldrRunner _objRunner) {objRunner=_objRunner;}
	
	@Override
	protected final void reset_Priv() {
		
	}

	@Override
	protected final void buildFtrVec_Priv() {

	}	
	
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
	 * 
	 */
	@Override
	protected final void buildSTLoader(String[] loadSrcFNamePrefixAra, int numPartitions) {
		for (int i=numPartitions-1; i>=0;--i) {
			String dataFile = loadSrcFNamePrefixAra[0]+"_"+i+".csv";
			String[] csvLoadRes = fileIO.loadFileIntoStringAra(dataFile,  exampleName+ " Data file " + i +" of " +numPartitions +" loaded",  exampleName+ " Data File " + i +" of " +numPartitions +" Failed to load");
			//ignore first entry - header
			for (int j=1;j<csvLoadRes.length; ++j) {
				String str = csvLoadRes[j];
				int pos = str.indexOf(',');
				String oid = str.substring(0, pos);
				SOM_Example ex = buildSingleExample(oid, str);
				exampleMap.put(oid, ex);			
			}
		}	
	}//buildSTLoader
	
	/**
	 * save and load the UI values used to build the preprocessed anim data for this project
	 * @param uiVals
	 */
	
	public final void saveAnimUIVals(TreeMap<String,String> uiVals) {
		
		
		
	}
	
	public final TreeMap<String,String> loadAnimUIVals() {
		TreeMap<String,String> res = new TreeMap<String,String>();
		
		
		
		
		return res;		
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


}//class SOM_GeomExampleManager
