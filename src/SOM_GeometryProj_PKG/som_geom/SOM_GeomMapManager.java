package SOM_GeometryProj_PKG.som_geom;

import java.util.ArrayList;
import java.util.TreeMap;

import SOM_GeometryProj_PKG.geom_Utils.Geom_SOMMseOvrDisp;
import SOM_GeometryProj_PKG.geom_Utils.Geom_SOMProjConfig;
import SOM_GeometryProj_PKG.som_geom.geom_UI.SOM_AnimWorldWin;
import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomExampleManager;
import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomFtrBndMon;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomObj;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading.geomGen.SOM_GeomObjBldrRunner;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading.geomGen.SOM_GeomObjBldrTasks;
import base_SOM_Objects.SOM_MapManager;
import base_SOM_Objects.som_examples.SOM_Example;
import base_SOM_Objects.som_examples.SOM_ExampleManager;
import base_SOM_Objects.som_examples.SOM_FtrDataType;
import base_SOM_Objects.som_ui.SOM_MseOvrDisplay;
import base_SOM_Objects.som_ui.win_disp_ui.SOM_MapUIWin;
import base_SOM_Objects.som_ui.win_disp_ui.SOM_UIToMapCom;
import base_SOM_Objects.som_utils.SOM_ProjConfigData;
import base_UI_Objects.my_procApplet;
import base_Utils_Objects.io.MsgCodes;
import base_Utils_Objects.vectorObjs.myPoint;

/**
 * extension of SOM_MapManager intended for geometric/graphical objects
 * intended to eventually be abstract
 * @author john
 *
 */
public abstract class SOM_GeomMapManager extends SOM_MapManager {
	/**
	 * owning window to display samples in sim world
	 */
	public SOM_AnimWorldWin dispWin;	
	/**
	 * actual represented random/generated uiObjs - source examples used to derive training examples used by this experiment
	 */
	public SOM_GeomObj[] sourceGeomObjects;	
	
	/**
	 * training example ftr data bounds manager
	 */
	public SOM_GeomFtrBndMon trainDatObjBnds;
	
	/**
	 * actual represented random/generated uiObjs - source examples used to derive training examples used by this experiment
	 */
	public SOM_GeomObj[] trainDatGeomObjects;	

	/**
	 * mapper to manage the example training data built from the geometric objects
	 */
	protected SOM_GeomExampleManager geomSrcToTrainExDataManager;
	
	/**
	 * mapper for -training- data - geometric source data must be mapped into this construction, which is then used as source for training
	 */
	protected SOM_GeomExampleManager trainExDataManager;

	
	/**
	 * extended flags from base class
	 */
	public static final int 
		srcGeomObjsAllBuiltIDX 		= numBaseFlags + 0,
		trainDatObjsAllBuiltIDX 	= numBaseFlags + 1;
	
	public static final int numGeomBaseFlags = numBaseFlags + 2;
	

	/**
	 * # of preprocessed examples to save to a single file
	 */
	private static final int preProcDataPartSz = 50000;
	
	/**
	 * # of geometric objects to build
	 */
	protected int numObjsToBuild;
	
	/**
	 * # of samples per object
	 */
	protected int numSamplesPerObj;
	/**
	 * total # of training examples to build
	 */
	protected int ttlNumTrainExamples;
	
	 /**
	  * runnable object to manage various tasks
	  */
	protected SOM_GeomObjBldrRunner objRunner;
	
	/** 
	 * Type of geometric object
	 */
	protected String geomObjType;
	
	/**
	 * coordinate bounds in world for the objects this map manager owns 
	 * 		first idx : 0 is min; 1 is diff
	 * 		2nd idx : 0 is x, 1 is y, 2 is z
	 */
	protected final float[][] worldBounds;
		
	public SOM_GeomMapManager(SOM_MapUIWin _win, SOM_AnimWorldWin _dispWin, float[] _dims, float[][] _worldBounds, TreeMap<String, Object> _argsMap, String _geomObjType, int _numFtrs) {
		super(_win, _dims, _argsMap);
			//# of training features determined by type of object
		this.setNumTrainFtrs(_numFtrs);
		//worldBounds=_worldBounds;
		geomObjType=_geomObjType;
		projConfigData.setSOMProjName(geomObjType);	
		dispWin = _dispWin;
		if(dispWin != null) {
			System.out.println(geomObjType + " World bounds set to value!");
			worldBounds=_worldBounds;
			objRunner = buildObjRunner();		
			geomSrcToTrainExDataManager.setObjRunner(objRunner);
		} else {
			System.out.println(geomObjType + " World bound set to null!");
			worldBounds = null;
			objRunner = null;
		}
		trainDatObjBnds = buildTrainDatFtrBndMgr();
	}//ctor	
	
	/**
	 * build the thread runner for this map manager that will manage the various tasks related to the geometric objects
	 * @return
	 */
	protected abstract SOM_GeomObjBldrRunner buildObjRunner();	
	/**
	 * build the training data bounds manager
	 */
	protected final SOM_GeomFtrBndMon buildTrainDatFtrBndMgr() {
		//use # of ftrs mapped 
		return new SOM_GeomFtrBndMon(getNumTrainFtrs());
	};
	/**
	 * build instance-specific project file configuration 
	 */
	@Override
	protected final SOM_ProjConfigData buildProjConfigData(TreeMap<String, Object> _argsMap) {				return new Geom_SOMProjConfig(this,_argsMap);	}	

	@Override
	//build the example that represents the SOM data where the mouse is
	protected final SOM_MseOvrDisplay buildMseOverExample() {return new Geom_SOMMseOvrDisp(this,0.0f);}

	/**
	 * build an interface to manage communications between UI and SOM map dat
	 * This interface will need to include a reference to an application-specific UI window
	 */
	@Override
	protected final SOM_UIToMapCom buildSOM_UI_Interface() {	return new SOM_UIToMapCom(this, win);}	
	
	/**
	 * build the map of example mappers used to manage all the data the SOM will consume - called from base class ctor
	 */
	@Override
	protected final void buildExampleDataMappers() {
		SOM_ExampleManager ex = buildExampleDataMappers_Indiv("geomSrcExData"); // this manages TRAINING data
		exampleDataMappers.put("geomSrcExData",  ex);
		SOM_ExampleManager exTrain = buildExampleDataMappers_Indiv("trainingData"); // this manages TRAINING data
		exampleDataMappers.put("trainingData",  exTrain);
		
		geomSrcToTrainExDataManager = (SOM_GeomExampleManager) exampleDataMappers.get("geomSrcExData");
		trainExDataManager = (SOM_GeomExampleManager) exampleDataMappers.get("trainingData");
	}
	
	/**
	 * build the example data mapper specific to instancing class
	 * @return
	 */
	protected abstract SOM_GeomExampleManager buildExampleDataMappers_Indiv(String _name);
	
	/**
	 * Load and process raw data, and save results as preprocessed csvs - this is only necessary
	 * when building a SOM from a pre-built dataset.  This project will use synthesized geometric objects
	 * to build SOM data, and so this won't be used
	 * @param fromCSVFiles : whether loading data from csv files or from SQL calls
	 * 
	 */
	@Override
	public final void loadAndPreProcAllRawData(boolean fromCSVFiles) {}
	
	/**
	 * set number of objects to build and number of sampels per object
	 * @param _numObjs
	 * @param _numSamples
	 */
	public final void setUIObjData(int _numObjs, int _numSamples, int _numTrainExamples) {
		numObjsToBuild=_numObjs;
		numSamplesPerObj=_numSamples;
		ttlNumTrainExamples = _numTrainExamples;
	}
	
	/**
	 * actually configre and execute objRunner task for specified objects, task to perform
	 * @param _objs array of objs to perform task upon
	 * @param _task task to perform
	 * @param _callingMethod method which invoked this method
	 * @param _taskDescr string description of task to perform
	 */
	private final void execObjRunnerTask(SOM_GeomObj[] _objs, SOM_GeomObjBldrTasks _task, String _callingMethod, String _taskDescr) {
		//set task type to perform
		objRunner.setTaskType(_task);
		//set instance-specific values
		execObjRunner_Pre_Indiv(_task);
		//set array to work with in obj runner
		objRunner.setObjArray(_objs);	//sets # of work units/objects to build based on size of array
		getMsgObj().dispMessage("SOM_GeomMapManager","execObjRunnerTask::"+_callingMethod, "Start " + _taskDescr,MsgCodes.info1);
		objRunner.runMe();
		getMsgObj().dispMessage("SOM_GeomMapManager","execObjRunnerTask::"+_callingMethod, "Finished " + _taskDescr,MsgCodes.info1);
	}//execObjRunnerTask	
	
	public final void resetTrainDataObjs() {
		trainExDataManager.reset();
		trainDatGeomObjects = new SOM_GeomObj[0];
		setFlag(trainDatObjsAllBuiltIDX,false);
	}
	
	
	/**
	 * (re)build base examples setFlag(trainDatObjsAllBuiltIDX,true);
	 */
	public final void buildGeomExampleObjs() {
		objRunner.setNumSamplesPerObj(numSamplesPerObj);	
		setFlag(srcGeomObjsAllBuiltIDX, false);
		resetTrainDataObjs();
		
		execObjRunnerTask(buildEmptyObjArray(), SOM_GeomObjBldrTasks.buildBaseObj, "buildGeomExampleObjs","building "+ numObjsToBuild+" geom example objects of type : " + this.geomObjType);
		sourceGeomObjects = objRunner.getObjArray();
		setFlag(srcGeomObjsAllBuiltIDX, true);
			//data in example manager is out of synch
		geomSrcToTrainExDataManager.reset();
		for(SOM_GeomObj obj : sourceGeomObjects) {	geomSrcToTrainExDataManager.addExampleToMap(obj);	}		
	}
	
	/**
	 * regenerate samples for all existing objects - use existing array 
	 */
	public final void rebuildGeomExObjSamples() {
		objRunner.setNumSamplesPerObj(numSamplesPerObj);
		execObjRunnerTask(sourceGeomObjects, SOM_GeomObjBldrTasks.regenSamplesBaseObj, "rebuildGeomExObjSamples","rebuilding " + numSamplesPerObj +" samples for all "+ numObjsToBuild+" geom example objects of type : " + this.geomObjType);	
	}
	
	/**
	 * send any instance-specific control/ui values to objRunners, based on task
	 */
	protected abstract void execObjRunner_Pre_Indiv(SOM_GeomObjBldrTasks _task);
	
	/**
	 * This will build an appropriately sized empty object array, to be passed to the runner so that it can fill the array
	 * @return
	 */
	protected abstract SOM_GeomObj[] buildEmptyObjArray();
	

//	/**
//	 * build an array of constituent SOM_GeomSmplForSOMExample objects, either from source points, or from randomly derived values, for array of geom objects passed
//	 * @param isRand whether points should be randomly generated from object or built from object constituent points
//	 * @return
//	 */
//	public SOM_GeomSmplDataForEx[] buildSrcObjDataAra(SOM_GeomObj[] objAra, int[] perObjIDX, boolean isRand) {
//		SOM_GeomSmplDataForEx[] srcSamples = new SOM_GeomSmplDataForEx[objAra.length];
//		if(isRand) {	for(int i=0;i<srcSamples.length;++i) {		srcSamples[i] = new SOM_GeomSmplDataForEx(objAra[i], objAra[i].getRandPointOnObj());}} 
//		else {			for(int i=0;i<srcSamples.length;++i) {		srcSamples[i] = new SOM_GeomSmplDataForEx(objAra[i], objAra[i].objSamples.getSamplePt(perObjIDX[i]));	}}
//		return srcSamples;
//	}	
//
//	/**
//	 * build an array of constituent SOM_GeomSmplForSOMExample objects, either from source points, or from randomly derived values, for array of geom objects passed
//	 * @param isRand whether points should be randomly generated from object or built from object constituent points
//	 * @return
//	 */
//	public SOM_GeomSmplDataForEx[] buildSrcObjDataAra(SOM_GeomObj obj, boolean isRand) {
//		SOM_GeomSmplDataForEx[] srcSamples = new SOM_GeomSmplDataForEx[obj.srcPts.length];
//		if(isRand) {	for(int i=0;i<srcSamples.length;++i) {		srcSamples[i] = new SOM_GeomSmplDataForEx(obj, obj.getRandPointOnObj());}} 
//		else {			for(int i=0;i<srcSamples.length;++i) {		srcSamples[i] = new SOM_GeomSmplDataForEx(obj, obj.srcPts[i]);	}}
//		return srcSamples;
//	}	


	@Override
	/**
	 * this function will build the input data used by the SOM - this will be partitioned by some amount into test and train data (usually will use 100% train data, but may wish to test label mapping)
	 */
	protected final SOM_Example[] buildSOM_InputData() {
		SOM_Example[] res = trainExDataManager.buildExampleArray();	//cast to appropriate mapper when flag custOrdersAsTrainDataIDX is set
		getMsgObj().dispMessage("SOM_GeomMapManager","buildSOM_InputData", "Size of input data : " + res.length,MsgCodes.info5);
		return res;
	}
	/**
	 * load some previously saved geometric object information
	 */
	@Override
	public final void loadPreProcTrainData(String subDir, boolean forceLoad) {
		getMsgObj().dispMessage("SOM_GeomMapManager","loadPreProcTrainData","Begin loading preprocced data from " + subDir +  "directory.", MsgCodes.info5);
			//load geometry data
		if(!geomSrcToTrainExDataManager.isDataPreProcced() || forceLoad) {			
				//first need to load UI info about preprocced data (settings used to generate data)
			TreeMap<String,String> uiToBuildGeom = geomSrcToTrainExDataManager.loadGeomObjsUIVals(subDir);
			if((null != uiToBuildGeom) && (uiToBuildGeom.size()!=0)) {		//loaded successfully
				dispWin.setAllUIValsFromPreProcLoad(uiToBuildGeom);
			} else {
				getMsgObj().dispWarningMessage("SOM_GeomMapManager","loadPreProcTrainData","UI Values used to build Geom failed to load - UI may be out of sync with current Geometry objects.");
			}
				//now load actual object data
			geomSrcToTrainExDataManager.loadAllPreProccedExampleData(subDir);
		} else {
			getMsgObj().dispMessage("SOM_GeomMapManager","loadPreProcTrainData","Not loading preprocessed " + geomObjType +" geometric examples since they are already loaded.", MsgCodes.info1);
		}
			//copy mapper geom examples to example array
		sourceGeomObjects = (SOM_GeomObj[]) geomSrcToTrainExDataManager.buildExampleArray();
		setFlag(srcGeomObjsAllBuiltIDX, true);
			//finalize and calc ftr vecs on geometry if we have loaded new data 
		finishSOMExampleBuild(geomSrcToTrainExDataManager,  ""+geomObjType +" geometric (graphical source) example");

		getMsgObj().dispMessage("SOM_GeomMapManager","loadAllPreProccedData","Finished loading preprocced data from " + subDir +  " directory.", MsgCodes.info5);
	}
	
	/**
	 * save all currently preprocced loaded data - source data along with their constituent samples
	 */
	public void saveAllPreProcExamples() {
		getMsgObj().dispMessage("SOM_GeomMapManager","saveAllPreProcExamples","Start Saving all Preproccessed " + geomObjType + " Examples.", MsgCodes.info5);
			//save current data as preprocced data
		boolean saveSuccess = geomSrcToTrainExDataManager.saveAllPreProccedExampleData();		
			//need to save current UI configuration used to generate geometric data
		boolean saveUISuccess = geomSrcToTrainExDataManager.saveGeomObjsUIVals(dispWin.getAllUIValsForPreProcSave());
		getMsgObj().dispMessage("SOM_GeomMapManager","saveAllPreProcExamples","Finished Saving all Preproccessed " + geomObjType + " Examples. Data Success : " + saveSuccess + " | UI Vals Success : " + saveUISuccess, MsgCodes.info5);
	}//saveAllPreProcExamples
	
	
	//get mins/diffs for ftr vals per ftr jp and for all vals per all jps
	public Float[][] getMinBndsAra() {
		ArrayList<Float[]> tmpBnds = new ArrayList<Float[]>();
		tmpBnds.add(0,trainDatObjBnds.getMinBndsAra());	
		return tmpBnds.toArray(new Float[1][] );
	}
	public Float[][] getDiffsBndsAra() {		
		ArrayList<Float[]> tmpBnds = new ArrayList<Float[]>();
		tmpBnds.add(0,trainDatObjBnds.getDiffBndsAra());	
		return tmpBnds.toArray(new Float[1][] );
	}	
	
	
	public void saveCurrentTrainData() {
		getMsgObj().dispMessage("SOM_GeomMapManager","saveCurrentTrainData","Begin saving current data as training data, mins and diffs. Train size : " + numTrainData+ " Testing size : " + numTestData+".", MsgCodes.info5);	
		initNewSOMDirsAndSaveData();
		
		
		getMsgObj().dispMessage("SOM_GeomMapManager","saveCurrentTrainData","Finished saving current data as training data, mins and diffs. Train size : " + numTrainData+ " Testing size : " + numTestData+".", MsgCodes.info5);	
	}
	
	/**
	 * finish building the training example data - finalize each example and then perform calculation to derive weight vector
	 * @param dataManager
	 * @param exampleMapperDesc
	 */
	protected void finishSOMExampleBuild(SOM_GeomExampleManager dataManager, String exampleMapperDesc) {
		getMsgObj().dispMessage("SOM_GeomMapManager","finishSOMExampleBuild","Begin finalize mappers, calculate feature data, diffs, mins, and calculate post-global-ftr-data calcs.", MsgCodes.info5);
			//current SOM map, if there is one, is now out of date, do not use
		setSOMMapNodeDataIsLoaded(false);
			//finalize customer prospects and products (and true prospects if they exist) - customers are defined by having criteria that enable their behavior to be used as to train the SOM		
		getMsgObj().dispInfoMessage("SOM_GeomMapManager","_finalizeAllMappersBeforeFtrCalc","Begin finalize of all example data, preparing each example for feature calculation.");
			//finalize examples before feature calcs
		dataManager.finalizeAllExamples();
		getMsgObj().dispInfoMessage("SOM_GeomMapManager","_finalizeAllMappersBeforeFtrCalc","Finished finalizing examples before feature calc.");
			//feature vector only corresponds to actual -customers- since this is what is used to build the map - build feature vector for customer prospects				
		boolean geomObjFtrBldSuccess = dataManager.buildFeatureVectors();	
		if(!geomObjFtrBldSuccess) {getMsgObj().dispMessage("SOM_GeomMapManager","finishSOMExampleBuild","Building " + exampleMapperDesc +" Feature vectors failed due to above error (no data available).  Aborting - No features have been calculated for any examples!", MsgCodes.error1);	return;	}

		getMsgObj().dispMessage("SOM_GeomMapManager","finishSOMExampleBuild","Finished buildFeatureVectors | Begin calculating diffs and mins", MsgCodes.info1);	
			//now get mins and diffs from calc object
		setMinsAndDiffs(getMinBndsAra(), getDiffsBndsAra());  
//		Float[] tmpTrnMins = getTrainFtrMins(), tmpTrnDiffs = getTrainFtrDiffs() ;
//		if(tmpTrnMins.length == 0 ) {
//			getMsgObj().dispInfoMessage("SOM_GeomMapManager","finishSOMExampleBuild", "No training mins/diffs set for " + exampleMapperDesc +"s features");
//		} else {
//			for(int i=0;i<tmpTrnMins.length;++i) {
//				getMsgObj().dispInfoMessage("SOM_GeomMapManager","finishSOMExampleBuild","			set min : " + tmpTrnMins[i] + " and diff : " + tmpTrnDiffs[i] + " for " + exampleMapperDesc +"s features");
//			}
//		}
		getMsgObj().dispMessage("SOM_GeomMapManager","finishSOMExampleBuild","Finished calculating diffs and mins | Begin building post-feature calc structs for " + exampleMapperDesc +"s (i.e. std ftrs) dependent on diffs and mins", MsgCodes.info1);
		
			//now finalize post feature calc -this will do std features			
		dataManager.buildAfterAllFtrVecsBuiltStructs();		
		
		getMsgObj().dispMessage("SOM_GeomMapManager","finishSOMExampleBuild","Finished finalize mappers, calculate feature data, diffs, mins, and calculate post-global-ftr-data calcs.", MsgCodes.info5);						
		//} else {	getMsgObj().dispMessage("SOM_GeomMapManager","finishSOMExampleBuild","No prospects or products loaded to calculate/finalize.", MsgCodes.warning2);	}
	}//finishSOMExampleBuild	

	/**
	 * synthesize training data from currently specified geometric data 
	 */
	public void generateTrainingData() {
		float trainTestPartition = this.projConfigData.getTrainTestPartition();
		getMsgObj().dispMessage("SOM_GeomMapManager","generateAllPreProcExamples","Start Processing all " + geomObjType + " Examples into SOM Training/Testing Examples with " + String.format("%1.2f", (trainTestPartition*100.0f)) +"% Training Data.", MsgCodes.info5);
		buildTrainTestFromPartition(trainTestPartition);
		getMsgObj().dispMessage("SOM_GeomMapManager","generateAllPreProcExamples","Finished Processing all Preproccessed " + geomObjType + " Examples.  ", MsgCodes.info5);
	}//generateTrainingData
	

	/**
	 * using the passed partition information, build the testing and training data partitions
	 */
	@Override
	protected void buildTrainTestFromPartition(float trainTestPartition) {
		getMsgObj().dispMessage("SOM_GeomMapManager::"+geomObjType,"buildTestTrainFromInput","Starting Building Input, Test, Train data arrays.", MsgCodes.info5);		
		if(!getFlag(srcGeomObjsAllBuiltIDX)) {
			getMsgObj().dispWarningMessage("SOM_GeomMapManager::"+geomObjType,"buildTestTrainFromInput","Failed Building Input, Test, Train data arrays due to no Geometric data loaded to build training data from.  Aborting");return;
		} else {
			resetTrainDataObjs();
				//synthesize training data using current configuration from UI with loaded data
			trainExDataManager.buildTrainingDataFromGeomObjs(geomSrcToTrainExDataManager, ttlNumTrainExamples);
			trainDatGeomObjects = (SOM_GeomObj[]) trainExDataManager.buildExampleArray();
			setFlag(trainDatObjsAllBuiltIDX,true);
				//finalize and calc ftr vecs on geometry if we have loaded new data 
			finishSOMExampleBuild(trainExDataManager,  ""+geomObjType +" Geom object-derived training example");
		}
				
		//set input data, shuffle it and set test and train partitions
		//only build test and train partitions if training data has been synthesized from geometric examples
		setInputTrainTestShuffleDataAras(trainTestPartition);
		
		getMsgObj().dispMessage("SOM_GeomMapManager::"+geomObjType,"buildTestTrainFromInput","Finished Building Input, Test, Train, data arrays.", MsgCodes.info5);
	}

	/**
	 * this is used to load preprocessed data, calculate features, load specified prebuilt map, map all examples and save results
	 * This functionality doesn't need to be available for this application
	 */
	@Override
	public final void loadAllDataAndBuildMappings() {}

	@Override
	/**
	 * no secondary maps for this project
	 */
	protected final int _getNumSecondaryMaps() {		return 0;}
	
	/**
	 * we never ignore zero features since these objects will be sufficiently low dimensional to use dense training
	 */
	@Override
	public final void setMapExclZeroFtrs(boolean val) {}

	@Override
	protected final void saveAllSegment_BMUReports_Indiv() {}

	
	/**
	 * products are zone/segment descriptors corresponding to certain feature, class or category configurations that are descriptive of training data
	 */
	@Override
	protected final void setProductBMUs() {
		// TODO Auto-generated method stub

	}

	@Override
	/**
	 * any instance-class specific code to execute when new map nodes are being loaded
	 */
	protected final void initMapNodesPriv() {
		// TODO Auto-generated method stub
	}


	@Override
	protected final Integer[] getAllClassLabels() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected final String getClassSegMappingDescrStr() {
		// TODO Auto-generated method stub
		return "Sample Point Membership";
	}

	@Override
	protected final Integer[] getAllCategoryLabels() {
		// TODO Auto-generated method stub
		return new Integer[0];
	}

	@Override
	protected final String getCategorySegMappingDescrStr() {
		// TODO Auto-generated method stub
		return "";
	}

	
	
	@Override
	//return appropriately pathed file name for map image of specified ftr idx
	public String getSOMLocClrImgForFtrFName(int ftrIDX) {		return projConfigData.getSOMLocClrImgForFtrFName(ftrIDX);	}

	/**
	 * augment a map of application-specific descriptive quantities and their values, for the SOM Execution human-readable report
	 * @param res map already created holding exampleDataMappers create time
	 */
	@Override
	public void getSOMExecInfo_Indiv(TreeMap<String, String> res) {

	}


	/**
	 * check mouse over/click in experiment; if btn == -1 then mouse over
	 * @param msx
	 * @param msy
	 * @param mseClckInWorld
	 * @param btn
	 * @return
	 */
	public abstract boolean checkMouseClick(int msx, int msy, myPoint mseClckInWorld, int btn);
	/**
	 * check mouse drag/move in experiment; if btn == -1 then mouse over
	 * @param msx
	 * @param msy
	 * @param mseClckInWorld
	 * @param btn
	 * @return
	 */
	public abstract boolean checkMouseDragMove(int msx, int msy, myPoint mseClckInWorld, int btn);
	/**
	 * notify all exps that mouse has been released
	 */
	public abstract void setMouseRelease();

	
	/**
	 * called from map as bmus after loaded and training data bmus are set from bmu file
	 */
	public void setAllBMUsFromMap() {
		//make sure class and category segments are built 
		//build class segments from mapped training examples - these are the sample points
		buildClassSegmentsOnMap();
		//build category segments from mapped training examples
		//buildCategorySegmentsOnMap();
		//set "product" bmus (any examples that don't directly relate to training data but rather provide descriptions of training data)
		//perhaps set these to be geometric source data?
		//setProductBMUs();

	}//setAllBMUsFromMap
	
	@Override
	public final int getPreProcDatPartSz() {return preProcDataPartSz;}

	public final float[][] getWorldBounds(){return worldBounds;}
	
	@Override
	public Float[] getTrainFtrMins() {return this.getMinVals(0);}
	@Override
	public Float[] getTrainFtrDiffs() {return this.getDiffVals(0);}
	
	public final void setGeomObjsBuilt(boolean val) {setFlag(srcGeomObjsAllBuiltIDX, val);}
	public final boolean getGeomObjsBuilt() {return getFlag(srcGeomObjsAllBuiltIDX);}	
	
	public final void setTrainDataObjsBuilt(boolean val) {setFlag(trainDatObjsAllBuiltIDX, val);}
	public final boolean getTrainDataObjsBuilt() {return getFlag(trainDatObjsAllBuiltIDX);}	
	
	@Override
	protected final int getNumFlags() {	return getNumGeomFlags_Indiv();}
	protected abstract int getNumGeomFlags_Indiv();
	@Override
	protected final void setFlag_Indiv(int idx, boolean val) {
		switch (idx) {//special actions for each flag
			case srcGeomObjsAllBuiltIDX 		: {break;}
			case trainDatObjsAllBuiltIDX 		: {break;}
			default : {setGeomFlag_Indiv(idx, val); break;}
		}
	}
	/**
	 * instancing-class specific flag handling
	 * @param idx
	 * @param val
	 */
	protected abstract void setGeomFlag_Indiv(int idx, boolean val);

	////////////////////////////
	// draw functions
	
//	/**
//	 * draw source objects in owning anim res window
//	 * @param pa
//	 * @param animTimeMod
//	 * @param curSelGeomObjIDX
//	 * @param showMapBasedLocs
//	 * @param showSelUIObj
//	 */
//	public final void drawSrcObjsInUIWindow(my_procApplet pa, float animTimeMod, int curSelGeomObjIDX, boolean showMapBasedLocs) {
//		if(getFlag(srcGeomObjsAllBuiltIDX)){
//			//msgObj.dispInfoMessage("SOM_AnimWorldWin", "drawMe", "ui obj data loaded is true");
//			if(showMapBasedLocs){				//show map-based locations
//				dispWin._drawObjs_UseBMUs(pa, sourceGeomObjects, curSelGeomObjIDX, animTimeMod);
//			} else {										//show objects based on their own location/data, not map-derived quantities		
//				dispWin._drawObjs_UseActual(pa, sourceGeomObjects, curSelGeomObjIDX, animTimeMod);
//			}//use locs or map-locs
//		}
//	}//drawSrcObjsInUIWindow
//
//	/**
//	 * draw synthesized objects in owning anim res window
//	 * @param pa
//	 * @param animTimeMod
//	 * @param curSelGeomObjIDX
//	 * @param showMapBasedLocs
//	 * @param showSelUIObj
//	 */
//	public final void drawSynthObjsInUIWindow(my_procApplet pa, float animTimeMod, boolean showMapBasedLocs) {
//		if(getFlag(trainDatObjsAllBuiltIDX)){
//			//msgObj.dispInfoMessage("SOM_AnimWorldWin", "drawMe", "ui obj data loaded is true");
//			if(showMapBasedLocs){				//show map-based locations
//				dispWin._drawObjs_UseBMUs(pa, trainDatGeomObjects, -1, animTimeMod);
//			} else {										//show objects based on their own location/data, not map-derived quantities		
//				dispWin._drawObjs_UseActual(pa, trainDatGeomObjects, -1, animTimeMod);
//			}//use locs or map-locs
//		}
//	}//drawSynthObjsInUIWindow

	@Override
	protected final float getPreBuiltMapInfoDetail(my_procApplet pa, String[] str, int i, float yOff, boolean isLoaded) {
		// TODO Auto-generated method stub
		return yOff;
	}
	
	@Override
	protected final float drawResultBarPriv1(my_procApplet pa, float yOff) {
		// TODO Auto-generated method stub
		return yOff;
	}

	@Override
	protected final float drawResultBarPriv2(my_procApplet pa, float yOff) {
		// TODO Auto-generated method stub
		return yOff;
	}

	@Override
	protected final float drawResultBarPriv3(my_procApplet pa, float yOff) {
		// TODO Auto-generated method stub
		return yOff;
	}

	
	/**
	 * for saving bmu reports based on ftr vals
	 */
	@Override
	public String getFtrWtSegmentTitleString(SOM_FtrDataType ftrCalcType, int ftrIDX) {		
		String ftrTypeDesc = getDataDescFromInt(ftrCalcType);
		return "Feature Weight Segment using " + ftrTypeDesc +" examples for ftr idx : " + ftrIDX;
	}


}//class SOM_GeomMapManager
