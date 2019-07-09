package SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers.base;

import java.util.TreeMap;

import SOM_GeometryProj_PKG.geom_SOM_Examples.Geom_SOMMapNode;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.exampleManagers.Geom_SOMExampleManager;
import SOM_GeometryProj_PKG.geom_Utils.Geom_SOMMseOvrDisp;
import SOM_GeometryProj_PKG.geom_Utils.Geom_SOMProjConfig;
import base_SOM_Objects.SOM_MapManager;
import base_SOM_Objects.som_examples.SOM_Example;
import base_SOM_Objects.som_examples.SOM_MapNode;
import base_SOM_Objects.som_ui.SOM_MseOvrDisplay;
import base_SOM_Objects.som_ui.win_disp_ui.SOM_MapUIWin;
import base_SOM_Objects.som_ui.win_disp_ui.SOM_UIToMapCom;
import base_SOM_Objects.som_utils.SOM_ProjConfigData;
import base_UI_Objects.my_procApplet;
import base_Utils_Objects.io.MsgCodes;
import base_Utils_Objects.vectorObjs.Tuple;

/**
 * extension of SOM_MapManager intended for geometric/graphical objects
 * intended to eventually be abstract
 * @author john
 *
 */
public abstract class Geom_SOMMapManager extends SOM_MapManager {
	/**
	 * mapper to manage the example geometric objects and their training data
	 */
	protected Geom_SOMExampleManager exMapper;
	
	/**
	 * # of preprocessed examples to save to a single file
	 */
	private final int preProcDataPartSz = 50000;

	public Geom_SOMMapManager(SOM_MapUIWin _win, float[] _dims, TreeMap<String, Object> _argsMap) {
		super(_win, _dims, _argsMap);
		
	}
	//ctor from non-UI stub main
	public Geom_SOMMapManager(float[] _dims, TreeMap<String, Object> _argsMap) {this(null,_dims, _argsMap);}		
	
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
	 * Load and process raw data, and save results as preprocessed csvs - this is only necessary
	 * when building a SOM from a pre-built dataset.  This project will use synthesized geometric objects
	 * to build SOM data, and so this won't be used
	 * @param fromCSVFiles : whether loading data from csv files or from SQL calls
	 * 
	 */
	@Override
	public final void loadAndPreProcAllRawData(boolean fromCSVFiles) {}

	@Override
	/**
	 * this function will build the input data used by the SOM - this will be partitioned by some amount into test and train data (usually will use 100% train data, but may wish to test label mapping)
	 */
	protected SOM_Example[] buildSOM_InputData() {
		SOM_Example[] res = exMapper.buildExampleArray();	//cast to appropriate mapper when flag custOrdersAsTrainDataIDX is set
		getMsgObj().dispMessage("Geom_SOMMapManager","buildSOM_InputData", "Size of input data : " + res.length,MsgCodes.info5);
		return res;
	}
	/**
	 * load some previously saved geometric object information
	 */
	@Override
	protected void loadPreProcTrainData(String subDir, boolean forceLoad) {
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
	public void loadAllDataAndBuildMappings() {}

	@Override
	/**
	 * no secondary maps for this project
	 */
	protected int _getNumSecondaryMaps() {		return 0;}
	
	/**
	 * we never ignore zero features
	 */
	@Override
	public void setMapExclZeroFtrs(boolean val) {this.getMsgObj().dispInfoMessage("Geom_SOMMapManager", "setMapExclZeroFtrs", "Setting ignored : we never ignore 0 features for geometric data, which is dense and low dimensional.");}

	@Override
	protected void saveAllSegment_BMUReports_Indiv() {}

	@Override
	/**
	 * products are zone/segment descriptors corresponding to certain feature, class or category configurations that are descriptive of training data
	 */
	protected void setProductBMUs() {
		// TODO Auto-generated method stub

	}

	@Override
	/**
	 * any instance-class specific code to execute when new map nodes are being loaded
	 */
	protected void initMapNodesPriv() {
		// TODO Auto-generated method stub

	}

	@Override
	public SOM_MapNode buildMapNode(Tuple<Integer, Integer> mapLoc, String[] tkns) {return new Geom_SOMMapNode(this,mapLoc, tkns);}	

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
	public int getPreProcDatPartSz() {return preProcDataPartSz;}


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
	
	/**
	 * for saving bmu reports based on ftr vals
	 */
	@Override
	public String getFtrWtSegmentTitleString(int ftrCalcType, int ftrIDX) {		
		String ftrTypeDesc = getDataDescFromInt(ftrCalcType);
		return "Feature Weight Segment using " + ftrTypeDesc +" examples for ftr idx : " + ftrIDX;
	}


}//class Geom_SOMMapManager
