package SOM_GeometryProj_PKG.som_geom.geom_UI;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;

import SOM_GeometryProj_PKG.som_geom.SOM_GeomMapManager;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomObj;
import SOM_GeometryProj_PKG.SOM_GeometryMain;
import base_SOM_Objects.SOM_MapManager;
import base_UI_Objects.my_procApplet;
import base_UI_Objects.drawnObjs.myDrawnSmplTraj;
import base_UI_Objects.windowUI.myDispWindow;
import base_UI_Objects.windowUI.myGUIObj;
import base_Utils_Objects.io.MsgCodes;
import base_Utils_Objects.vectorObjs.myPoint;
import base_Utils_Objects.vectorObjs.myVector;

/**
 * this class will instance a combined window to hold an animation world and a map display window overlay
 * @author john
 *
 */

public abstract class SOM_AnimWorldWin extends myDispWindow {
	/**
	 * map manager corresponding to this animation world
	 */
	public SOM_GeomMapManager mapMgr;	
	//ui vars
	public static final int
		gIDX_NumUIObjs 			= 0,
		gIDX_NumUISamplesPerObj = 1,	//per object # of training examples
		gIDX_FractNumTrainEx	= 2,	//fraction of span from min # to max # of training examples to set as value - to counter how difficult it can be to change the value
		gIDX_NumTrainingEx		= 3,	//total # of training examples to synthesize for training - will be a combinatorial factor of # of objs and # of samples per obj 
		gIDX_SelDispUIObj		= 4;	//ID of a ui obj to be selected and highlighted					

	protected static final int numBaseAnimWinUIObjs = 5;
	//instancing class will specify numGUIObjs	
	protected double[] uiVals;				
	
	//raw values from ui components
	/**
	 * # of priv flags from base class and instancing class
	 */
	private int numPrivFlags;

	public static final int 
		debugAnimIDX 			= 0,				//debug
		
		showSamplePntsIDX 		= 1,				//show/hide sample points
		showFullSourceObjIDX	= 2,				//show/hide full source object(sources of sample points)
		
		showFullTrainingObjIDX	= 3,				//show/hide training data full objects		
		
		showUIObjLabelIDX		= 4,				//display the ui obj's ID as a text tag
		showUIObjSmplsLabelIDX	= 5,				//display the ui object's samples' IDs as a text tag
		showObjByWireFrmIDX		= 6,				//show object as wireframe, or as filled-in
		showSelUIObjIDX			= 7,				//highlight the ui obj with the selected idx
		showMapBasedLocsIDX 	= 8,				//show map-derived locations of training data instead of actual locations (or along with?)
		
		//saveUIObjDataIDX 		= 8,				//save ui obj locations as training data on next draw cycle
		useUIObjLocAsClrIDX		= 9,				//should use ui obj's location as both its and its samples' color
		
		useSmplsForTrainIDX 	= 10,				//use surface samples, or ui obj centers, for training data
		uiObjBMUsSetIDX			= 11,				//ui object's bmus have been set
		mapBuiltToCurUIObjsIDX 	= 12,				//the current ui obj configuration has an underlying map built to it
		regenUIObjsIDX 			= 13;				//regenerate ui objs with current specs
	
	protected static final int numBaseAnimWinPrivFlags = 14;
		
	//initial values
	public int numGeomObjs = 200, numSmplPointsPerObj = 200, numTrainingExamples = 40000, curSelGeomObjIDX = 0;
	//fraction of max count of binomial coefficient to set as # of training examples to sample from objects + samples
	public double fractOfBinomialForBaseNumTrainEx = .001;
	
	//object type the instancing window manages
	public final String geomObjType;
	
	//dimensions of SOM Map - hard coded to override setting from SOM Map UI Window - need to set in window
	protected float[] SOMMapDims = new float[] {834.8f,834.8f};

	public String[][] menuBtnNames = new String[][] {		//each must have literals for every button defined in side bar menu, or ignored
		{},
		{"Load Geometry Data", "Save Geometry data","Geom -> Train"},		//row 1
		{"Build Map","---","---","---"},	//row 2 
		{"---","---","---","---"},	//row 3
		{"---","---","---","---"},
		{"---","---","---","---","---"}	
	};

	public SOM_AnimWorldWin(my_procApplet _p, String _n, int _flagIdx, int[] fc, int[] sc, float[] rd, float[] rdClosed, String _winTxt, boolean _canDrawTraj, String _type) {
		super(_p, _n, _flagIdx, fc, sc, rd, rdClosed, _winTxt, _canDrawTraj);
		initAndSetAnimWorldVals();
		geomObjType = _type;
	}
	
	/**
	 * called after base window ctor/before instancing class ctor functions and initMe
	 * any initialization that is relevant for this abstract class's objects
	 */
	private void initAndSetAnimWorldVals() {}	
	
	/**
	 * instancing window will build the map manager that this anim world will use
	 * @return
	 */
	public abstract SOM_MapManager buildMapManager();
	/**
	 * return appropriate SOM Map Manager for this window
	 * @return
	 */
	public final SOM_MapManager getMapMgr() {return mapMgr;}


	@Override
	public final void initAllPrivBtns() {
		
		//add an entry for each button, in the order they are wished to be displayed	true tag, false tag, btn IDX	
		ArrayList<Object[]> tmpBtnNamesArray = new ArrayList<Object[]>(); 
		tmpBtnNamesArray.add(new Object[]{"Debugging","Debug",debugAnimIDX});
		tmpBtnNamesArray.add(new Object[]{"Regenerating " +geomObjType + " Objs","Regenerate " +geomObjType + " Objs",regenUIObjsIDX});		
		tmpBtnNamesArray.add(new Object[]{"Showing " +geomObjType + " Objects", "Show " +geomObjType + " Objects", showFullSourceObjIDX});	
		tmpBtnNamesArray.add(new Object[]{"Showing " +geomObjType + " Sample Points", "Show " +geomObjType + " Sample Points", showSamplePntsIDX});	
		tmpBtnNamesArray.add(new Object[]{"Showing Labels", "Show Labels", showUIObjLabelIDX});    
		tmpBtnNamesArray.add(new Object[]{"Showing Sample Labels", "Show Sample Labels", showUIObjSmplsLabelIDX});    
		tmpBtnNamesArray.add(new Object[]{"Showing Loc-based Color", "Showing Random Color", useUIObjLocAsClrIDX});		
		tmpBtnNamesArray.add(new Object[]{"Showing " +geomObjType + " Training Exs", "Show " +geomObjType + " Training Exs", showFullTrainingObjIDX});	
		
		tmpBtnNamesArray.add(new Object[]{"Hi-Light Sel " +geomObjType + " ", "Enable " +geomObjType + " Hi-Light", showSelUIObjIDX});  		
		tmpBtnNamesArray.add(new Object[]{"Train From " +geomObjType + " Samples", "Train From " +geomObjType + " Centers/Bases", useSmplsForTrainIDX});    
		//tmpBtnNamesArray.add(new Object[]{"Save Data", "Save Data", saveUIObjDataIDX});        
		tmpBtnNamesArray.add(new Object[]{"Showing BMU-derived Locs", "Showing Actual Locs", showMapBasedLocsIDX});  
		
		String[] showWFObjsTFLabels = getShowWireFrameBtnTFLabels();
		if((null != showWFObjsTFLabels) && (showWFObjsTFLabels.length == 2)) {tmpBtnNamesArray.add(new Object[]{showWFObjsTFLabels[0],showWFObjsTFLabels[1],showObjByWireFrmIDX});}		
		

		//add instancing-class specific buttons - returns total # of private flags in instancing class
		numPrivFlags = initAllAnimWorldPrivBtns_Indiv(tmpBtnNamesArray);
		//finalize setup for UI toggle buttons - convert to arrays
		truePrivFlagNames = new String[tmpBtnNamesArray.size()];
		falsePrivFlagNames = new String[truePrivFlagNames.length];
		privModFlgIdxs = new int[truePrivFlagNames.length];
		for(int i=0;i<truePrivFlagNames.length;++i) {
			Object[] tmpAra = tmpBtnNamesArray.get(i);
			truePrivFlagNames[i] = (String) tmpAra[0];
			falsePrivFlagNames[i] = (String) tmpAra[1];
			privModFlgIdxs[i] = (int) tmpAra[2];
		}		
		numClickBools = truePrivFlagNames.length;	
		initPrivBtnRects(0,numClickBools);		
	}
	/**
	 * Instancing class-specific (application driven) UI buttons to display are built 
	 * in this function.  Add an entry to tmpBtnNamesArray for each button, in the order 
	 * they are to be displayed
	 * @param tmpBtnNamesArray array list of Object arrays, where in each object array : 
	 * 			the first element is the true string label, 
	 * 			the 2nd elem is false string array, and 
	 * 			the 3rd element is integer flag idx 
	 * @return total number of privBtnFlags in instancing class (including those not displayed)
	 */
	protected abstract int initAllAnimWorldPrivBtns_Indiv(ArrayList<Object[]> tmpBtnNamesArray);
	
	/**
	 * Instance class determines the true and false labels for button to control showing full object, or just wire frame
	 * If empty no button is displayed
	 * @return array holding true(idx0) and false(idx1) labels for button
	 */
	protected abstract String[] getShowWireFrameBtnTFLabels();
	
	/**
	 * call to build or rebuild source geometric objects
	 */
	public final void rebuildSourceGeomObjs() {
		msgObj.dispInfoMessage("SOM_AnimWorldWin", "rebuildSourceGeomObjs", "Start (re)building all objects of type " + this.geomObjType);
		setMapMgrGeomObjVals();
		((SOM_GeomMapManager) mapMgr).buildGeomExampleObjs();
		msgObj.dispInfoMessage("SOM_AnimWorldWin", "rebuildSourceGeomObjs", "Finished (re)building all objects of type " + this.geomObjType);
	}
	
	/**
	 * send all UI values to map manager before objrunner tasks are executed
	 */
	public final void setMapMgrGeomObjVals() {
		((SOM_GeomMapManager) mapMgr).setUIObjData(numGeomObjs, numSmplPointsPerObj, numTrainingExamples);
		setMapMgrGeomObjVals_Indiv();
	}
	
	
	/**
	 * send all instance-specific values from UI to map manager
	 */
	protected abstract void setMapMgrGeomObjVals_Indiv();
	
	/**
	 * regenerate samples for current set of base objects
	 */
	public final void regenBaseGeomObjSamples() {
		msgObj.dispInfoMessage("SOM_AnimWorldWin", "regenBaseGeomObjSamples", "Start regenerating " + numSmplPointsPerObj +" samples for all base objects of type " + this.geomObjType);
		setMapMgrGeomObjVals();		
		((SOM_GeomMapManager) mapMgr).rebuildGeomExObjSamples();		
		msgObj.dispInfoMessage("SOM_AnimWorldWin", "regenBaseGeomObjSamples", "Finished regenerating " + numSmplPointsPerObj +" samples for all base objects of type " + this.geomObjType);
	}	
	
	@Override
	public final void setPrivFlags(int idx, boolean val) {
		int flIDX = idx/32, mask = 1<<(idx%32);
		privFlags[flIDX] = (val ?  privFlags[flIDX] | mask : privFlags[flIDX] & ~mask);
		switch (idx) {//special actions for each flag
			case debugAnimIDX 				: {	break;}					
			case showSamplePntsIDX 			: {	break;}		//show/hide object's sample points
			case showFullSourceObjIDX		: { break;}		//show/hide full object
			case showFullTrainingObjIDX		: { break;}
			//case saveUIObjDataIDX 		: { if(val){saveGeomObjInfo();addPrivBtnToClear(saveUIObjDataIDX);}break;}		//save all object data
			case showUIObjLabelIDX  		: { break;}		//show labels for objects
			case showUIObjSmplsLabelIDX		: { break;}
			case useUIObjLocAsClrIDX 		: {
				msgObj.dispInfoMessage("SOM_AnimWorldWin", "setPrivFlags :: useUIObjLocAsClrIDX", "Val :  "+ val);
				break;}		//color of objects is location or is random
			case showSelUIObjIDX 			: { break;}
			case useSmplsForTrainIDX		: { break;}		//use surface samples for train and centers for test, or vice versa
			case showMapBasedLocsIDX		: { break;}
			case uiObjBMUsSetIDX			: { break;}
			case mapBuiltToCurUIObjsIDX 	: { break;}     //whether map has been built and loaded for current config of spheres
			case regenUIObjsIDX				: { if(val){rebuildSourceGeomObjs(); addPrivBtnToClear(regenUIObjsIDX);} break;}		//remake all objects, turn off flag
			case showObjByWireFrmIDX		: { break;}
			default							: { setPrivFlags_Indiv(idx,val);}
		}
	}//setPrivFlags
	
	/**
	 * set values for instancing class-specific boolean flags
	 * @param idx
	 * @param val
	 */
	protected abstract void setPrivFlags_Indiv(int idx, boolean val);
	
	@Override
	protected final void setupGUIObjsAras() {
		//list box values - keyed by list obj IDX, value is string array of list obj values
		TreeMap<Integer, String[]> tmpListObjVals = new TreeMap<Integer, String[]>();
		
		ArrayList<Object[]> tmpUIObjArray = new ArrayList<Object[]>();
		//tmpBtnNamesArray.add(new Object[]{"Building SOM","Build SOM ",buildSOMExe});
		//object array of elements of following format  : 
		//	the first element double array of min/max/mod values
		//	the 2nd element is starting value
		//	the 3rd elem is label for object
		//	the 4th element is boolean array of {treat as int, has list values, value is sent to owning window}
		int minNumObjs = getMinNumObjs(),maxNumObjs = getMaxNumObjs(),diffNumObjs = (maxNumObjs - minNumObjs > 100 ? 10 : 1);
		numGeomObjs = minNumObjs;
		tmpUIObjArray.add(new Object[] {new double[]{minNumObjs,maxNumObjs,diffNumObjs}, (double)(numGeomObjs*1.0), "# of " +geomObjType + " Objects", new boolean[]{true, false, true}});   				//gIDX_NumUIObjs 		                                                                        
		int minNumSmplsPerObj = getMinNumSmplsPerObj(), maxNumSmplsPerObj = getMaxNumSmplsPerObj(), diffNumSmplsPerObj = (maxNumSmplsPerObj - minNumSmplsPerObj > 100 ? 10 : 1);
		numSmplPointsPerObj = minNumSmplsPerObj;
		tmpUIObjArray.add(new Object[] {new double[]{minNumSmplsPerObj,maxNumSmplsPerObj,diffNumSmplsPerObj}, (double)(numSmplPointsPerObj), "# of samples per Object", new boolean[]{true, false, true}});  				//gIDX_NumUISamplesPerObj 
		//gIDX_FractNumTrainEx fractOfBinomialForBaseNumTrainEx
		tmpUIObjArray.add(new Object[] {new double[]{0.00001,1.000,0.00001}, fractOfBinomialForBaseNumTrainEx,  "Fract of Binomial for Train Ex", new boolean[]{false, false, true}});  				//gIDX_FractNumTrainEx 
		
		//gIDX_NumTraingEx
		long minNumTrainingExamples = getNumTrainingExamples(minNumObjs,minNumSmplsPerObj), maxNumTrainingExamples = getNumTrainingExamples(numGeomObjs,numSmplPointsPerObj), diffNumTrainingEx = (maxNumTrainingExamples - minNumTrainingExamples) > 1000 ? 1000 : 10;
		numTrainingExamples = (int) minNumTrainingExamples;
		tmpUIObjArray.add(new Object[] {new double[]{minNumTrainingExamples,maxNumTrainingExamples,diffNumTrainingEx}, (double)(numTrainingExamples), "Ttl # of Train Ex ["+minNumTrainingExamples+", "+maxNumTrainingExamples+"]", new boolean[]{true, false, true}});  				//gIDX_NumUISamplesPerObj 
		
		tmpUIObjArray.add(new Object[] {new double[]{0,numGeomObjs-1,1}, (double)(curSelGeomObjIDX), "ID of Object to Select", new boolean[]{true, false, true}});   													//gIDX_SelDispUIObj	      
	
		
		//populate instancing application objects
		setupGUIObjsAras_Indiv(tmpUIObjArray,tmpListObjVals);
		
		int numGUIObjs = tmpUIObjArray.size();		
		guiMinMaxModVals = new double [numGUIObjs][3];
		guiStVals = new double[numGUIObjs];
		guiObjNames = new String[numGUIObjs];
		guiBoolVals = new boolean [numGUIObjs][4];
		uiVals = new double[numGUIObjs];//raw values
		for(int i =0;i<numGUIObjs; ++i) {
			guiMinMaxModVals[i] = (double[])tmpUIObjArray.get(i)[0];
			guiStVals[i] = (Double)tmpUIObjArray.get(i)[1];
			guiObjNames[i] = (String)tmpUIObjArray.get(i)[2];
			guiBoolVals[i] = (boolean[])tmpUIObjArray.get(i)[3];
			uiVals[i] = guiStVals[i];
		}
		
		//since horizontal row of UI comps, uiClkCoords[2] will be set in buildGUIObjs		
		guiObjs = new myGUIObj[numGUIObjs];			//list of modifiable gui objects

		buildGUIObjs(guiObjNames,guiStVals,guiMinMaxModVals,guiBoolVals,new double[]{xOff,yOff},tmpListObjVals);			//builds a horizontal list of UI comps	
	
	}//setupGUIObjsAras
	
	/**
	 * calculate the max # of examples for this type object - clique of object description degree 
	 */	
	protected abstract long getNumTrainingExamples(int objs, int smplPerObj);
	
	protected abstract int getMinNumObjs();
	protected abstract int getMaxNumObjs();
	protected abstract int getMinNumSmplsPerObj();
	protected abstract int getMaxNumSmplsPerObj();
	/**
	 * Instancing class-specific (application driven) UI objects should be defined
	 * in this function.  Add an entry to tmpBtnNamesArray for each button, in the order 
	 * they are to be displayed
	 * @param tmpUIObjArray array list of Object arrays, where in each object array : 
	 * 			the first element double array of min/max/mod values
	 * 			the 2nd element is starting value
	 * 			the 3rd elem is label for object
	 * 			the 4th element is boolean array of {treat as int, has list values, value is sent to owning window}
	 * @param tmpListObjVals treemap keyed by object IDX and value is list of strings of values for all UI list select objects
	 */
	protected abstract void setupGUIObjsAras_Indiv(ArrayList<Object[]> tmpUIObjArray, TreeMap<Integer, String[]> tmpListObjVals);

	/**
	 * update # of max training examples based on updated number of desired objects or samples per object
	 */
	private void refreshNumTrainingExampleBounds() {
		// binomial coefficient - n (total # of samples across all objects) choose k (dim of minimal defining set of each object)
		long newMaxVal = getNumTrainingExamples(numGeomObjs,numSmplPointsPerObj); 
		guiObjs[gIDX_NumTrainingEx].setNewMax(newMaxVal);	
		int minVal = (int) guiObjs[gIDX_NumTrainingEx].getMinVal();
		guiObjs[gIDX_NumTrainingEx].setNewDispText("Ttl # of Train Ex ["+minVal+", "+newMaxVal+"]");
		refreshNumTrainingExamples();
	}//refreshNumTrainingExampleBounds
	
	private void refreshNumTrainingExamples() {
		long TtlNumExamples = getNumTrainingExamples(numGeomObjs, numSmplPointsPerObj);
		double newVal = fractOfBinomialForBaseNumTrainEx * TtlNumExamples;
		guiObjs[gIDX_NumTrainingEx].setVal(newVal);
		setUIWinVals(gIDX_NumTrainingEx);
	}
		
	@Override
	protected  final void setUIWinVals(int UIidx) {
		float val = (float)guiObjs[UIidx].getVal();
		int ival = (int)val;

		switch(UIidx){		
		case gIDX_NumUIObjs : {
			if(ival != numGeomObjs){
				numGeomObjs = ival;
				guiObjs[gIDX_SelDispUIObj].setNewMax(ival-1);
				refreshNumTrainingExampleBounds();
				rebuildSourceGeomObjs();}
			break;}
		case gIDX_NumUISamplesPerObj : {
			if(ival != numSmplPointsPerObj){
				numSmplPointsPerObj = ival;
				refreshNumTrainingExampleBounds();
				regenBaseGeomObjSamples();}
			break;}
		case gIDX_FractNumTrainEx :{					//fraction of total # of possible samples in current configuration to use for training examples
			if(val != fractOfBinomialForBaseNumTrainEx) { fractOfBinomialForBaseNumTrainEx = val; refreshNumTrainingExamples();}
			break;}
		case gIDX_NumTrainingEx :{
			if(ival != numTrainingExamples){numTrainingExamples = ival;}
			setMapMgrGeomObjVals();
			break;}
		case gIDX_SelDispUIObj :{
			if(ival != curSelGeomObjIDX){curSelGeomObjIDX = pa.min(ival, numGeomObjs-1);}//don't select a sphere Higher than the # of spheres
			break;}
		default : {setUIWinVals_Indiv(UIidx, val);}
		}	
	}
	/**
	 * For instance-class specific ui values
	 * @param UIidx
	 */
	protected abstract void setUIWinVals_Indiv(int UIidx, float val);

	@Override
	protected final void initMe() {
		//build map manager
		mapMgr = (SOM_GeomMapManager) buildMapManager();
		
		//capable of using right side menu
		setFlags(drawRightSideMenu, true);	
		//init specific sim flags
		initPrivFlags(numPrivFlags);
		setPrivFlags(showFullSourceObjIDX,true);
		//build objects - instance class - only execute if window is being shown
		//initAllGeomObjs();
		//instance-specific init
		pa.setAllMenuBtnNames(menuBtnNames);	

		initMe_Indiv();
	}
	/**
	 * instancing class-specific functionality
	 */
	protected abstract void initMe_Indiv();
	
	/**
	 * get ui values used to build current geometry (for preproc save)
	 * @return
	 */
	public TreeMap<String, String> getAllUIValsForPreProcSave(){
		TreeMap<String, String> res = new TreeMap<String, String>();
		res.put("gIDX_NumUIObjs", String.format("%4d", (int)guiObjs[gIDX_NumUIObjs].getVal()));
		res.put("gIDX_NumUISamplesPerObj", String.format("%4d", (int)guiObjs[gIDX_NumUISamplesPerObj].getVal()));
		res.put("gIDX_FractNumTrainEx", String.format("%.4f", guiObjs[gIDX_FractNumTrainEx].getVal()));
		res.put("gIDX_NumTrainingEx", String.format("%4d", (int)guiObjs[gIDX_NumTrainingEx].getVal()));
		
		getAllUIValsForPreProcSave_Indiv(res);
		return res;
		
	}
	/**
	 * get instance-class specific ui values used to build current geometry (for preproc save)
	 * @return
	 */
	protected abstract void getAllUIValsForPreProcSave_Indiv(TreeMap<String, String> vals);
	
	/**
	 * set ui values used to build preproc data being loaded
	 * @return
	 */
	public void setAllUIValsFromPreProcLoad(TreeMap<String, String> uiVals) {
		guiObjs[gIDX_FractNumTrainEx].setVal(Double.parseDouble(uiVals.get("gIDX_FractNumTrainEx")));
		guiObjs[gIDX_NumUIObjs].setVal(Integer.parseInt(uiVals.get("gIDX_NumUIObjs")));
		guiObjs[gIDX_NumUISamplesPerObj].setVal(Integer.parseInt(uiVals.get("gIDX_NumUISamplesPerObj")));
		guiObjs[gIDX_NumTrainingEx].setVal(Integer.parseInt(uiVals.get("gIDX_NumTrainingEx")));
		
		setAllUIValsFromPreProcLoad_Indiv(uiVals);
		setAllUIWinVals();
	}
	/**
	 * set ui instance-specific values used to build preproc data being loaded
	 * @return
	 */
	protected abstract void setAllUIValsFromPreProcLoad_Indiv(TreeMap<String, String> uiVals);
	
/////////////////////////////
	// drawing routines
	@Override
	protected final void setCameraIndiv(float[] camVals) {
		//, float rx, float ry, float dz are now member variables of every window
		pa.camera(camVals[0],camVals[1],camVals[2],camVals[3],camVals[4],camVals[5],camVals[6],camVals[7],camVals[8]);      
		// puts origin of all drawn objects at screen center and moves forward/away by dz
		pa.translate(camVals[0],camVals[1],(float)dz); 
	    setCamOrient();	
	}

	@Override
	protected final void drawMe(float animTimeMod) {
		pa.pushMatrix();pa.pushStyle();//nested ifthen shenannigans to get rid of if checks in each individual draw
		drawMeFirst_Indiv();
			//check if geom objs are built in mapMgr
		//mapMgr.drawSrcObjsInUIWindow(pa, animTimeMod, curSelGeomObjIDX, getPrivFlags(showMapBasedLocsIDX));
		if(mapMgr.getGeomObjsBuilt()) {
			boolean wantDrawBMUs = getPrivFlags(showMapBasedLocsIDX);
			boolean shouldDrawBMUs = (wantDrawBMUs && getPrivFlags(mapBuiltToCurUIObjsIDX));
			if(!shouldDrawBMUs && wantDrawBMUs) {	setPrivFlags(showMapBasedLocsIDX,false);}
			_drawObjs(mapMgr.sourceGeomObjects, curSelGeomObjIDX, animTimeMod, shouldDrawBMUs,
											getPrivFlags(showSamplePntsIDX),getPrivFlags(showFullSourceObjIDX),getPrivFlags(useUIObjLocAsClrIDX),
											getPrivFlags(showSelUIObjIDX),getPrivFlags(showObjByWireFrmIDX), getPrivFlags(showUIObjLabelIDX));
		}
			//check if train samples are built in map mgr
		if((mapMgr.getTrainDataObjsBuilt()) && (getPrivFlags(showFullTrainingObjIDX))){
			//mapMgr.drawSynthObjsInUIWindow(pa, animTimeMod, getPrivFlags(showMapBasedLocsIDX));
			_drawObjs(mapMgr.trainDatGeomObjects, -1, animTimeMod, false, false, true, getPrivFlags(useUIObjLocAsClrIDX), false, getPrivFlags(showObjByWireFrmIDX), getPrivFlags(showUIObjLabelIDX));
		} else {
			setPrivFlags(showFullTrainingObjIDX, false);
		}
		 //else {			msgObj.dispInfoMessage("SOM_AnimWorldWin", "drawMe", "ui obj data loaded is false");}
		drawMeLast_Indiv();
		pa.popStyle();pa.popMatrix();
		
	}//drawMe
	
	private void _drawObjs(SOM_GeomObj[] objs, int curSelObjIDX, float animTimeMod, boolean mapBuiltAndUseMapLoc, boolean showSmpls, boolean showObjs, boolean useLocClr, boolean showSel,boolean showWireFrame, boolean showLabel) {
		if(mapBuiltAndUseMapLoc) {
			if(showSmpls) {		_drawObjSmpls_UseBMUs(objs,useLocClr);}
			if(showObjs) {		_drawObjs_UseBMUs(objs,useLocClr, showWireFrame, showLabel);}			
		} else {
			if(showSmpls) {		_drawObjSmpls(objs,curSelObjIDX,animTimeMod,useLocClr,showSel,showLabel);}
			if(showObjs) {		_drawObjs_UseActual(objs,curSelObjIDX,animTimeMod,useLocClr,showSmpls,showSel, showWireFrame, showLabel);	}
		} 
			
	}//_drawObjs
	
	
	private void _drawObjSmpls_UseBMUs(SOM_GeomObj[] objs, boolean useLocClr) {
		if(useLocClr){			for(SOM_GeomObj s : objs){s.drawMeSmplsClrLoc_BMU(pa);}} //loc color
		else {					for(SOM_GeomObj s : objs){s.drawMeSmplsClrRnd_BMU(pa);}}//rand color
	}//_drawObjSmpls_UseBMUs
	
	private void _drawObjSmpls(SOM_GeomObj[] objs, int curSelObjIDX, float animTimeMod, boolean useLocClr, boolean showSel, boolean showLabel) {
		if(useLocClr){			for(SOM_GeomObj s : objs){s.drawMeSmplsClrLoc(pa);}} //loc color
		else {					for(SOM_GeomObj s : objs){s.drawMeSmplsClrRnd(pa);}}//rand color
		if(showLabel){			for(SOM_GeomObj s : objs){s.drawMySmplsLabel(pa);}	}
		if((curSelObjIDX != -1) && showSel) {				
			if(useLocClr){	objs[curSelObjIDX].drawMeSelected_ClrLoc_Smpl(pa,animTimeMod); }
			else {									objs[curSelObjIDX].drawMeSelected_ClrRnd_Smpl(pa,animTimeMod); }
		}
	}//_drawObjSmpls
	
	private void _drawObjs_UseBMUs(SOM_GeomObj[] objs, boolean useLocClr, boolean showWireFrame, boolean showLabel) {
		if(showWireFrame) {			//draw objects with wire frames
			if(useLocClr){		for(SOM_GeomObj s : objs){s.drawMeClrLoc_WF_BMU(pa);}} //loc color
			else {				for(SOM_GeomObj s : objs){s.drawMeClrRnd_WF_BMU(pa);}}//rand color
		} else {
			if(useLocClr){		for(SOM_GeomObj s : objs){s.drawMeClrLoc_BMU(pa);}} //loc color
			else {				for(SOM_GeomObj s : objs){s.drawMeClrRnd_BMU(pa);}}//rand color
		}		
		if(showLabel){			for(SOM_GeomObj s : objs){s.drawMeLabel_BMU(pa);}	}
	}//_drawObjs_UseBMUs
	
	private void _drawObjs_UseActual(SOM_GeomObj[] objs, int curSelObjIDX, float animTimeMod, boolean useLocClr, boolean showSmpls, boolean showSel,boolean showWireFrame, boolean showLabel) {
		if(!showSmpls){
			if((curSelObjIDX != -1) && showSel) {				
				if(useLocClr){	objs[curSelObjIDX].drawMeSelected_ClrLoc(pa,animTimeMod); }
				else {									objs[curSelObjIDX].drawMeSelected_ClrRnd(pa,animTimeMod); }
			}
		}
		if(showWireFrame) {			//draw objects with wire frames
			if(useLocClr){		for(SOM_GeomObj s : objs){s.drawMeClrLoc_WF(pa);}} //loc color
			else {				for(SOM_GeomObj s : objs){s.drawMeClrRnd_WF(pa);}}//rand color
		} else {
			if(useLocClr){		for(SOM_GeomObj s : objs){s.drawMeClrLoc(pa);}} //loc color
			else {				for(SOM_GeomObj s : objs){s.drawMeClrRnd(pa);}}//rand color
		}
		if(showLabel){			for(SOM_GeomObj s : objs){s.drawMyLabel(pa);}	}
		
		
	}//_drawObjs_UseActual
//
//	public void _drawObjs_UseBMUsx(my_procApplet pa, SOM_GeomObj[] objs, int curSelObjIDX, float animTimeMod) {
//		//msgObj.dispInfoMessage("SOM_AnimWorldWin", "drawMe", "showMapBasedLocsIDX is true");
//		if (getPrivFlags(mapBuiltToCurUIObjsIDX)){//show all objs based on map-derived locations if selected and map is made (i.e. draw bmu's location for object, instead of object itself
//			//draw spheres/samples based on map info - use 1st 3 features of non-scaled ftr data from map's nodes as x-y-z 
//			if(getPrivFlags(showSamplePntsIDX)){			//sample points don't use wire frames
//				if(getPrivFlags(useUIObjLocAsClrIDX)){			for(SOM_GeomObj s : objs){s.drawMeSmplsClrLoc_BMU(pa);}} //loc color
//				else {											for(SOM_GeomObj s : objs){s.drawMeSmplsClrRnd_BMU(pa);}}//rand color
//			} 
//			if(getPrivFlags(showFullSourceObjIDX)){										//draw objects
//				if(getPrivFlags(showObjByWireFrmIDX)) {			//draw objects with wire frames
//					if(getPrivFlags(useUIObjLocAsClrIDX)){		for(SOM_GeomObj s : objs){s.drawMeClrLoc_WF_BMU(pa);}} //loc color
//					else {										for(SOM_GeomObj s : objs){s.drawMeClrRnd_WF_BMU(pa);}}//rand color
//				} else {
//					if(getPrivFlags(useUIObjLocAsClrIDX)){		for(SOM_GeomObj s : objs){s.drawMeClrLoc_BMU(pa);}} //loc color
//					else {										for(SOM_GeomObj s : objs){s.drawMeClrRnd_BMU(pa);}}//rand color
//				}
//			}//if show sample points only, else
//			if(getPrivFlags(showUIObjLabelIDX)){			for(SOM_GeomObj s : objs){s.drawMeLabel_BMU(pa);}	}
//			if((curSelObjIDX != -1) && getPrivFlags(showSelUIObjIDX)) {	
//				if(getPrivFlags(useUIObjLocAsClrIDX)){	objs[curSelObjIDX].drawMeSelected_BMU_ClrLoc(pa,animTimeMod); }
//				else {									objs[curSelObjIDX].drawMeSelected_BMU_ClrRnd(pa,animTimeMod); }
//			}
//		} else {										setPrivFlags(showMapBasedLocsIDX, false);	}	//turn off flag if not possible to draw 
//		
//	}//_drawMe_UseBMUs
//	
//	public void _drawObjs_UseActualx(my_procApplet pa, SOM_GeomObj[] objs, int curSelObjIDX, float animTimeMod) {
//		//msgObj.dispInfoMessage("SOM_AnimWorldWin", "drawMe", "showMapBasedLocsIDX is false");
//		
//		if(getPrivFlags(showSamplePntsIDX)){			//sample points don't use wire frames
//			if(getPrivFlags(useUIObjLocAsClrIDX)){			for(SOM_GeomObj s : objs){s.drawMeSmplsClrLoc(pa);}} //loc color
//			else {											for(SOM_GeomObj s : objs){s.drawMeSmplsClrRnd(pa);}}//rand color
//			if(getPrivFlags(showUIObjSmplsLabelIDX)){				for(SOM_GeomObj s : objs){s.drawMySmplsLabel(pa);}	}
//			if((curSelObjIDX != -1) && getPrivFlags(showSelUIObjIDX)) {				
//				if(getPrivFlags(useUIObjLocAsClrIDX)){	objs[curSelObjIDX].drawMeSelected_ClrLoc_Smpl(pa,animTimeMod); }
//				else {									objs[curSelObjIDX].drawMeSelected_ClrRnd_Smpl(pa,animTimeMod); }
//			}
//		} else {
//			if((curSelObjIDX != -1) && getPrivFlags(showSelUIObjIDX)) {				
//				if(getPrivFlags(useUIObjLocAsClrIDX)){	objs[curSelObjIDX].drawMeSelected_ClrLoc(pa,animTimeMod); }
//				else {									objs[curSelObjIDX].drawMeSelected_ClrRnd(pa,animTimeMod); }
//			}
//			
//		}
//		if(getPrivFlags(showFullSourceObjIDX)){										//draw objects
//			if(getPrivFlags(showObjByWireFrmIDX)) {			//draw objects with wire frames
//				if(getPrivFlags(useUIObjLocAsClrIDX)){		for(SOM_GeomObj s : objs){s.drawMeClrLoc_WF(pa);}} //loc color
//				else {										for(SOM_GeomObj s : objs){s.drawMeClrRnd_WF(pa);}}//rand color
//			} else {
//				if(getPrivFlags(useUIObjLocAsClrIDX)){		for(SOM_GeomObj s : objs){s.drawMeClrLoc(pa);}} //loc color
//				else {										for(SOM_GeomObj s : objs){s.drawMeClrRnd(pa);}}//rand color
//			}
//			if(getPrivFlags(showUIObjLabelIDX)){				for(SOM_GeomObj s : objs){s.drawMyLabel(pa);}	}
//		}//if show sample points only, else
//
//	}//_drawMe_UseActual
	
	
	/**
	 * instance-specific drawing setup before objects are actually drawn 
	 */
	protected abstract void drawMeFirst_Indiv();
	/**
	 * instance-specific drawing after objects are drawn but before info is saved
	 */
	protected abstract void drawMeLast_Indiv();
	
	/**
	 * draw som map window UI Objects
	 */
	@Override
	public final void drawCustMenuObjs(){
		((SOM_GeometryMain) pa).drawSOMUIObjs();		
	}


	/////////////////////////////
	// end drawing routines
	
	/////////////////////////////
	// sim routines	
	@Override
	protected final boolean simMe(float modAmtSec) {		return false;	}

	@Override
	protected final void stopMe() {}
	/////////////////////////////
	// end sim routines	
	
	@Override
	protected final void launchMenuBtnHndlr() {
		msgObj.dispMessage("SOM_AnimWorldWin","launchMenuBtnHndlr","Begin requested action", MsgCodes.info4);
		int btn = curCustBtn[curCustBtnType];
		switch(curCustBtnType) {
		case SOM_GeomSideBarMenu.btnAuxFunc1Idx : {//row 1 of menu side bar buttons
			//{"Gen Training Data", "Save Training data","Load Training Data"},		//row 1
			msgObj.dispMessage("SOM_AnimWorldWin","launchMenuBtnHndlr","Click Functions 1 in "+name+" : btn : " + btn, MsgCodes.info4);
			switch(btn){
				case 0 : {	
					//build training data from geom examples
					mapMgr.loadPreprocAndBuildTestTrainPartitions(mapMgr.projConfigData.getTrainTestPartition(), true);
					resetButtonState();
					break;}
				case 1 : {	
					mapMgr.saveAllPreProcExamples();
					resetButtonState();
					break;}
				case 2 : {	
					mapMgr.generateTrainingData();
					resetButtonState();
					break;}
				default : {
					msgObj.dispMessage("SOM_AnimWorldWin","launchMenuBtnHndlr","Unknown Functions 1 btn : "+btn, MsgCodes.warning2);
					break;}
			}	
			break;}//row 1 of menu side bar buttons
	
		case SOM_GeomSideBarMenu.btnAuxFunc2Idx : {//row 2 of menu side bar buttons
			msgObj.dispMessage("SOM_AnimWorldWin","launchMenuBtnHndlr","Click Functions 2 in "+name+" : btn : " + btn, MsgCodes.info4);//{"Ld&Bld SOM Data", "Load SOM Config", "Ld & Make Map", "Ld Prebuilt Map"},	//row 2
			//		{"Train Data","True Prspcts", "Prods", "SOM Cfg", "Func 14"},	//row 2

			switch(btn){
				case 0 : {	
					mapMgr.loadTrainDataMapConfigAndBuildMap(false);
					resetButtonState();
					break;}
				case 1 : {	
					resetButtonState();
					break;}
				case 2 : {	
					//this will load all true prospects from preprocessed prospect files.
					resetButtonState();
					break;}
				case 3 : {//load all training data, default map config, and build map
					mapMgr.loadSOMConfig();//pass fraction of data to use for training
					resetButtonState();
					break;}
				default : {
					msgObj.dispMessage("SOM_AnimWorldWin","launchMenuBtnHndlr","Unknown Functions 2 btn : "+btn, MsgCodes.warning2);
					resetButtonState();
					break;}	
			}
			break;}//row 2 of menu side bar buttons
		case SOM_GeomSideBarMenu.btnAuxFunc3Idx : {//row 3 of menu side bar buttons
			msgObj.dispMessage("SOM_AnimWorldWin","launchMenuBtnHndlr","Click Functions 3 in "+name+" : btn : " + btn, MsgCodes.info4);//{"Ld&Bld SOM Data", "Load SOM Config", "Ld & Make Map", "Ld Prebuilt Map"},	//row 2
			switch(btn){
				case 0 : {	
					resetButtonState();
					break;}
				case 1 : {	
					resetButtonState();
					break;}
				case 2 : {	
					resetButtonState();
					break;}
				case 3 : {
					resetButtonState();
					break;}
				default : {
					msgObj.dispMessage("SOM_AnimWorldWin","launchMenuBtnHndlr","Unknown Functions 3 btn : "+btn, MsgCodes.warning2);
					resetButtonState();
					break;}	
			}
			break;}//row 3 of menu side bar buttons
		case SOM_GeomSideBarMenu.btnAuxFunc4Idx : {//row 3 of menu side bar buttons
			msgObj.dispMessage("SOM_AnimWorldWin","launchMenuBtnHndlr","Click Functions 3 in "+name+" : btn : " + btn, MsgCodes.info4);//{"Ld&Bld SOM Data", "Load SOM Config", "Ld & Make Map", "Ld Prebuilt Map"},	//row 2
			switch(btn){
				case 0 :
				case 1 : 
				case 2 : 
				case 3 : {//load all training data, default map config, and build map
					mapMgr.loadPretrainedExistingMap(btn, true);//runs in thread, button state reset there
					resetButtonState();
					break;}
				default : {
					msgObj.dispMessage("SOM_AnimWorldWin","launchMenuBtnHndlr","Unknown Functions 3 btn : "+btn, MsgCodes.warning2);
					resetButtonState();
					break;}	
			}
			break;}//row 3 of menu side bar buttons
		case SOM_GeomSideBarMenu.btnDBGSelCmpIdx : {//row 4 of menu side bar buttons (debug)	
			msgObj.dispMessage("SOM_AnimWorldWin","launchMenuBtnHndlr","Click Debug in "+name+" : btn : " + btn, MsgCodes.info4);
			//{"All->Bld Map","All Dat To Map", "Func 22", "Func 23", "Prblt Map"},	//row 3
			switch(btn){
				case 0 : {	
					resetButtonState();
					break;}
				case 1 : {	
					resetButtonState();
					break;}
				case 2 : {	
					resetButtonState();
					break;}
				case 3 : {//show current mapdat status
					resetButtonState();
					break;}
				case 4 : {						
					resetButtonState();
					break;}
				default : {
					msgObj.dispMessage("SOM_AnimWorldWin","launchMenuBtnHndlr","Unknown Debug btn : "+btn, MsgCodes.warning2);
					resetButtonState();
					break;}
			}				
			break;}//row 4 of menu side bar buttons (debug)			
		}		
		msgObj.dispMessage("SOM_AnimWorldWin","launchMenuBtnHndlr","End requested action (multithreaded actions may still be working).", MsgCodes.info4);
	}
	@Override
	protected final void setVisScreenDimsPriv() {
		float xStart = rectDim[0] + .5f*(curVisScrDims[0] - (curVisScrDims[1]-(2*xOff)));
		
	}	
	
	@Override
	protected final boolean hndlMouseMoveIndiv(int mouseX, int mouseY, myPoint mseClckInWorld){
		
		boolean res = mapMgr.checkMouseDragMove(mouseX, mouseY, mseClckInWorld, -1);
		if(res) {return res;}
		
		return hndlMseMove_Priv(mouseX, mouseY, mseClckInWorld);
	}
	
	/**
	 * instance-specific mouse move handling
	 * @param mouseX
	 * @param mouseY
	 * @param mseClckInWorld
	 * @return
	 */
	protected abstract boolean hndlMseMove_Priv(int mouseX, int mouseY, myPoint mseClckInWorld);
	
	//alt key pressed handles trajectory
	//cntl key pressed handles unfocus of spherey
	@Override
	protected final boolean hndlMouseClickIndiv(int mouseX, int mouseY, myPoint mseClckInWorld, int mseBtn) {
		boolean res = checkUIButtons(mouseX, mouseY);
		if(res) {return res;}
		res = mapMgr.checkMouseClick(mouseX, mouseY, mseClckInWorld, mseBtn);
		if(res) {return res;}
		return hndlMseClick_Priv(mouseX, mouseY,mseClckInWorld,mseBtn);
	}//hndlMouseClickIndiv
	/**
	 * instance-specific mouse click handling
	 * @param mouseX
	 * @param mouseY
	 * @param mseClckInWorld
	 * @return
	 */
	protected abstract boolean hndlMseClick_Priv(int mouseX, int mouseY, myPoint mseClckInWorld, int mseBtn);

	@Override
	protected final boolean hndlMouseDragIndiv(int mouseX, int mouseY, int pmouseX, int pmouseY, myPoint mouseClickIn3D, myVector mseDragInWorld, int mseBtn) {
		boolean res = false;
		//pa.outStr2Scr("hndlMouseDragIndiv sphere ui drag in world mouseClickIn3D : " + mouseClickIn3D.toStrBrf() + " mseDragInWorld : " + mseDragInWorld.toStrBrf());
//		if((privFlags[sphereSelIDX]) && (curSelSphere!="")){//pass drag through to selected sphere
//			//pa.outStr2Scr("sphere ui drag in world mouseClickIn3D : " + mouseClickIn3D.toStrBrf() + " mseDragInWorld : " + mseDragInWorld.toStrBrf());
//			res = sphereCntls.get(curSelSphere).hndlMouseDragIndiv(mouseX, mouseY, pmouseX, pmouseY, mouseClickIn3D,curMseLookVec, mseDragInWorld);
//		}
		if(res) {return res;}
		res = mapMgr.checkMouseDragMove(mouseX, mouseY, mouseClickIn3D, mseBtn);
		if(res) {return res;}		
		return hndlMseDrag_Priv(mouseX, mouseY, pmouseX, pmouseY, mouseClickIn3D, mseDragInWorld, mseBtn);
	}

	/**
	 * instance-specific mouse drag handling
	 * @param mouseX
	 * @param mouseY
	 * @param pmouseX
	 * @param pmouseY
	 * @param mouseClickIn3D
	 * @param mseDragInWorld
	 * @param mseBtn
	 * @return
	 */
	protected abstract boolean hndlMseDrag_Priv(int mouseX, int mouseY, int pmouseX, int pmouseY, myPoint mouseClickIn3D, myVector mseDragInWorld, int mseBtn);	

	@Override
	protected final void hndlMouseRelIndiv() {
		mapMgr.setMouseRelease();
		hndlMseRelease_Priv();
	}
	/**
	 * instance-specific functionality for mouse release
	 */
	protected abstract void hndlMseRelease_Priv();
	
	@Override
	protected final void processTrajIndiv(myDrawnSmplTraj drawnNoteTraj){		}
	@Override
	protected final void addSScrToWinIndiv(int newWinKey){}
	@Override
	protected final void addTrajToScrIndiv(int subScrKey, String newTrajKey){}
	@Override
	protected final void delSScrToWinIndiv(int idx) {}	
	@Override
	protected final void delTrajToScrIndiv(int subScrKey, String newTrajKey) {}
	//resize drawn all trajectories
	@Override
	protected final void resizeMe(float scale) {}
	@Override
	protected final void closeMe() {}
	@Override
	protected final void showMe() {	
		setCustMenuBtnNames();
	}
	@Override
	protected final String[] getSaveFileDirNamesPriv() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final void hndlFileLoad(File file, String[] vals, int[] stIdx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public final ArrayList<String> hndlFileSave(File file) {
		// TODO Auto-generated method stub
		return null;
	}


}//SOM_AnimWorldWin
