package SOM_GeometryProj_PKG.som_geom;

import java.util.TreeMap;

import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_SmplDataForSOMExample;
import SOM_GeometryProj_PKG.geom_Utils.Geom_SOMMseOvrDisp;
import SOM_GeometryProj_PKG.geom_Utils.Geom_SOMProjConfig;
import SOM_GeometryProj_PKG.som_geom.geom_UI.SOM_AnimWorldWin;
import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomExampleManager;
import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomFtrBndMon;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomObj;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading.SOM_GeomObjBldrRunner;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading.SOM_GeomObjBldrTasks;
import base_SOM_Objects.SOM_MapManager;
import base_SOM_Objects.som_examples.SOM_Example;
import base_SOM_Objects.som_examples.SOM_ExampleManager;
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
	public SOM_GeomObj[] synthGeomObjects;	
	
	/**
	 * mapper to manage the example geometric objects training data
	 */
	protected SOM_GeomExampleManager trainDatExMapper;
	
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
		
	public SOM_GeomMapManager(SOM_MapUIWin _win, SOM_AnimWorldWin _dispWin, float[] _dims, float[][] _worldBounds, TreeMap<String, Object> _argsMap, String _geomObjType) {
		super(_win, _dims, _argsMap);
		//worldBounds=_worldBounds;
		geomObjType=_geomObjType;
		projConfigData.setSOMProjName(geomObjType);	
		dispWin = _dispWin;
		if(dispWin != null) {
			worldBounds=_worldBounds;
			objRunner = buildObjRunner();		
			trainDatExMapper.setObjRunner(objRunner);
		} else {
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
	protected abstract SOM_GeomFtrBndMon buildTrainDatFtrBndMgr();
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
		SOM_ExampleManager ex = buildExampleDataMappers_Indiv(); // this manages TRAINING data
		exampleDataMappers.put("trainExamples",  ex);
		trainDatExMapper = (SOM_GeomExampleManager) exampleDataMappers.get("trainExamples");
	}
	
	/**
	 * build the example data mapper specific to instancing class
	 * @return
	 */
	protected abstract SOM_GeomExampleManager buildExampleDataMappers_Indiv();
	
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
	public final void setNumObjsAndSamples(int _numObjs, int _numSamples) {
		numObjsToBuild=_numObjs;
		numSamplesPerObj=_numSamples;
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
		getMsgObj().dispMessage("Geom_SOMMapManager","execObjRunnerTask::"+_callingMethod, "Start " + _taskDescr,MsgCodes.info1);
		objRunner.runMe();
		getMsgObj().dispMessage("Geom_SOMMapManager","execObjRunnerTask::"+_callingMethod, "Finished " + _taskDescr,MsgCodes.info1);
	}//execObjRunnerTask	
	
	public void generateAllPreProcExamples() {
		getMsgObj().dispMessage("Geom_SOMMapManager","generateAllPreProcExamples","Start Processing all " + geomObjType + " Examples into SOM Training Examples", MsgCodes.info5);
		
		
		
		getMsgObj().dispMessage("Geom_SOMMapManager","generateAllPreProcExamples","Finished Processing all Preproccessed " + geomObjType + " Examples.  ", MsgCodes.info5);
	}
	
	//save all currently preprocced loaded data - source data along with their constituent samples
	public void saveAllPreProcExamples() {
		getMsgObj().dispMessage("Geom_SOMMapManager","saveAllPreProcExamples","Start Saving all Preproccessed " + geomObjType + " Examples.", MsgCodes.info5);
		boolean saveSuccess = trainDatExMapper.saveAllPreProccedExampleData();
		
		getMsgObj().dispMessage("Geom_SOMMapManager","saveAllPreProcExamples","Finished Saving all Preproccessed " + geomObjType + " Examples. Success : " + saveSuccess, MsgCodes.info5);
	}
	
	/**
	 * (re)build base examples
	 */
	public final void buildGeomExampleObjs() {
		objRunner.setNumSamplesPerObj(numSamplesPerObj);		
		execObjRunnerTask(buildEmptyObjArray(), SOM_GeomObjBldrTasks.buildBaseObj, "buildGeomExampleObjs","building "+ numObjsToBuild+" geom example objects of type : " + this.geomObjType);
		sourceGeomObjects = objRunner.getObjArray();	
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
	

	/**
	 * build an array of constituent SOM_GeomSmplForSOMExample objects, either from source points, or from randomly derived values, for array of geom objects passed
	 * @param isRand whether points should be randomly generated from object or built from object constituent points
	 * @return
	 */
	public Geom_SmplDataForSOMExample[] buildSrcObjDataAra(SOM_GeomObj[] objAra, int[] perObjIDX, boolean isRand) {
		Geom_SmplDataForSOMExample[] srcSamples = new Geom_SmplDataForSOMExample[objAra.length];
		if(isRand) {	for(int i=0;i<srcSamples.length;++i) {		srcSamples[i] = new Geom_SmplDataForSOMExample(objAra[i], objAra[i].getRandPointOnObj());}} 
		else {			for(int i=0;i<srcSamples.length;++i) {		srcSamples[i] = new Geom_SmplDataForSOMExample(objAra[i], objAra[i].objSamplePts[perObjIDX[i]]);	}}
		return srcSamples;
	}	

	/**
	 * build an array of constituent SOM_GeomSmplForSOMExample objects, either from source points, or from randomly derived values, for array of geom objects passed
	 * @param isRand whether points should be randomly generated from object or built from object constituent points
	 * @return
	 */
	public Geom_SmplDataForSOMExample[] buildSrcObjDataAra(SOM_GeomObj obj, boolean isRand) {
		Geom_SmplDataForSOMExample[] srcSamples = new Geom_SmplDataForSOMExample[obj.srcPts.length];
		if(isRand) {	for(int i=0;i<srcSamples.length;++i) {		srcSamples[i] = new Geom_SmplDataForSOMExample(obj, obj.getRandPointOnObj());}} 
		else {			for(int i=0;i<srcSamples.length;++i) {		srcSamples[i] = new Geom_SmplDataForSOMExample(obj, obj.srcPts[i]);	}}
		return srcSamples;
	}	


	@Override
	/**
	 * this function will build the input data used by the SOM - this will be partitioned by some amount into test and train data (usually will use 100% train data, but may wish to test label mapping)
	 */
	protected final SOM_Example[] buildSOM_InputData() {
		SOM_Example[] res = trainDatExMapper.buildExampleArray();	//cast to appropriate mapper when flag custOrdersAsTrainDataIDX is set
		getMsgObj().dispMessage("Geom_SOMMapManager","buildSOM_InputData", "Size of input data : " + res.length,MsgCodes.info5);
		return res;
	}
	/**
	 * load some previously saved geometric object information
	 */
	@Override
	protected final void loadPreProcTrainData(String subDir, boolean forceLoad) {
		// TODO Auto-generated method stub

	}

	@Override
	//using the passed map information, build the testing and training data partitions and save them to files
	protected void buildTrainTestFromPartition(float trainTestPartition) {
		getMsgObj().dispMessage("Sphere_SOMMapManager","buildTestTrainFromInput","Starting Building Input, Test, Train data arrays.", MsgCodes.info5);

		//set input data, shuffle it and set test and train partitions
		setInputTrainTestShuffleDataAras(trainTestPartition);
		
		getMsgObj().dispMessage("Sphere_SOMMapManager","buildTestTrainFromInput","Finished Building Input, Test, Train, data arrays.", MsgCodes.info5);
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

	@Override
	/**
	 * products are zone/segment descriptors corresponding to certain feature, class or category configurations that are descriptive of training data
	 */
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
		//build class segments from mapped training examples
		buildClassSegmentsOnMap();
		//build category segments from mapped training examples
		buildCategorySegmentsOnMap();
		//set "product" bmus (any examples that don't directly relate to training data but rather provide descriptions of training data)
		setProductBMUs();

	}//setAllBMUsFromMap
	
	@Override
	public final int getPreProcDatPartSz() {return preProcDataPartSz;}

	public final float[][] getWorldBounds(){return worldBounds;}
	
	@Override
	public Float[] getTrainFtrMins() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Float[] getTrainFtrDiffs() {
		// TODO Auto-generated method stub
		return null;
	}
	
	////////////////////////////
	// draw functions
	
	/**
	 * draw source objects in owning anim res window
	 * @param pa
	 * @param animTimeMod
	 * @param curSelGeomObjIDX
	 * @param showMapBasedLocs
	 * @param showSelUIObj
	 */
	public final void drawSrcObjsInUIWindow(my_procApplet pa, float animTimeMod, int curSelGeomObjIDX, boolean showMapBasedLocs, boolean showSelUIObj) {
		//msgObj.dispInfoMessage("SOM_AnimWorldWin", "drawMe", "ui obj data loaded is true");
		if(showMapBasedLocs){				//show map-based locations
			dispWin._drawMe_UseBMUs(pa, sourceGeomObjects, animTimeMod);
			if(showSelUIObj){				sourceGeomObjects[curSelGeomObjIDX].drawMeSelected_BMU(pa,animTimeMod);     }
		} else {										//show objects based on their own location/data, not map-derived quantities		
			dispWin._drawMe_UseActual(pa, sourceGeomObjects, animTimeMod);
			if(showSelUIObj){				sourceGeomObjects[curSelGeomObjIDX].drawMeSelected(pa,animTimeMod);     }
		}//use locs or map-locs
	}

	/**
	 * draw synthesized objects in owning anim res window
	 * @param pa
	 * @param animTimeMod
	 * @param curSelGeomObjIDX
	 * @param showMapBasedLocs
	 * @param showSelUIObj
	 */
	public final void drawSynthObjsInUIWindow(my_procApplet pa, float animTimeMod, int curSelGeomObjIDX, boolean showMapBasedLocs, boolean showSelUIObj) {
		//msgObj.dispInfoMessage("SOM_AnimWorldWin", "drawMe", "ui obj data loaded is true");
		if(showMapBasedLocs){				//show map-based locations
			dispWin._drawMe_UseBMUs(pa, synthGeomObjects, animTimeMod);
			if(showSelUIObj){				synthGeomObjects[curSelGeomObjIDX].drawMeSelected_BMU(pa,animTimeMod);     }
		} else {										//show objects based on their own location/data, not map-derived quantities		
			dispWin._drawMe_UseActual(pa, synthGeomObjects, animTimeMod);
			if(showSelUIObj){				synthGeomObjects[curSelGeomObjIDX].drawMeSelected(pa,animTimeMod);     }
		}//use locs or map-locs
	}

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
	public String getFtrWtSegmentTitleString(int ftrCalcType, int ftrIDX) {		
		String ftrTypeDesc = getDataDescFromInt(ftrCalcType);
		return "Feature Weight Segment using " + ftrTypeDesc +" examples for ftr idx : " + ftrIDX;
	}


}//class Geom_SOMMapManager
