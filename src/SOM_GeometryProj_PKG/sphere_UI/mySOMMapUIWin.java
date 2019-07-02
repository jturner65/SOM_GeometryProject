package SOM_GeometryProj_PKG.sphere_UI;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;

import SOM_GeometryProj_PKG.sphere_SOM_Mapping.Sphere_SOMMapManager;
import base_SOM_Objects.SOM_MapManager;
import base_SOM_Objects.som_ui.win_disp_ui.SOM_MapUIWin;
import base_UI_Objects.my_procApplet;
import base_Utils_Objects.io.MsgCodes;
import base_Utils_Objects.vectorObjs.myPoint;
import base_Utils_Objects.vectorObjs.myVector;

//window that accepts trajectory editing
public class mySOMMapUIWin extends SOM_MapUIWin {
	
	public static final int 
		mapShowLocClrIDX 			= numSOMBasePrivFlags + 0;			//show img built of map with each pxl clr built from the 1st 3 features of the interpolated point at that pxl between the map nodes
	
	public final int _numPrivFlags = numSOMBasePrivFlags + 1;
	
	public String[][] menuBtnNames = new String[][] {		//each must have literals for every button defined in side bar menu, or ignored
		{"---", "---","---"},		//row 1
		{"---","---","---","---"},	//row 2
		{"---","---","---","---"},	//row 3
		{"---","---","---","---"},
		{"---","---","---","---","---"}	
	};
	
	public mySOMMapUIWin(my_procApplet _p, String _n, int _flagIdx, int[] fc, int[] sc, float[] rd, float[] rdClosed, String _winTxt, boolean _canDrawTraj) {
		super(_p, _n, _flagIdx, fc, sc, rd, rdClosed, _winTxt, _canDrawTraj);
		super.initThisWin(_canDrawTraj, true, false);
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
	protected final int initAllSOMPrivBtns_Indiv(ArrayList<Object[]> tmpBtnNamesArray) {
		tmpBtnNamesArray.add(new Object[] {"Hide Non-Product Job Practices","Show Non-Product Job Practices", mapShowLocClrIDX});          
		return _numPrivFlags;
	}
	
	/**
	 * build instance-specific map manager
	 */
	@Override
	protected SOM_MapManager buildMapMgr() {
		//SOM_mapDims : start x and y and dimensions of full map visualization as function of visible window size;
		//including strings for default directories specific to current project setup and Strafford
		TreeMap<String, Object> _argsMap = new TreeMap<String,Object>();
		//provide default values used by program
		_argsMap.put("configDir", "SphereProject" + File.separator+"config" + File.separator);
		_argsMap.put("dataDir", "SphereProject" + File.separator);
		_argsMap.put("logLevel",0);//0 is console alone,1 is log file alone, 2 is both
		
		return new Sphere_SOMMapManager(SOM_mapDims, _argsMap);
	}
	/**
	 * instance-specific window initialization
	 */
	@Override
	protected void initMeIndiv() {
		//default to showing right side bar menu
		setFlags(showRightSideMenu, true);	
		//moved from mapMgr ctor, to remove dependence on papplet in that object
		pa.setAllMenuBtnNames(menuBtnNames);	
		initMapAras(1, 1);
	}

	/**
	 * Instance class determines the true and false labels the class-category locking should use
	 * @return array holding true(idx0) and false(idx1) labels for buttons to control display of whether 
	 * category should be locked to allow selection through within-category classes
	 */
	@Override
	protected String[] getClassCatLockBtnTFLabels() {return new String[] {"Category Changes with Class","Lock Category; restrict Class to Cat"};}
	/**
	 * Instance class determines the true and false labels the class buttons use - if empty then no classes used
	 * @return array holding true(idx0) and false(idx1) labels for buttons to control display of class-based segment
	 */
	@Override
	protected final String[] getClassBtnTFLabels() {	return new String[] {"Hide Sphere Classes ","Show Sphere Classes "};}
	/**
	 * Instance class determines the true and false labels the category buttons use - if empty then no categories used
	 * @return array holding true(idx0) and false(idx1) labels for buttons to control display of category-based segment
	 */
	@Override
	protected final String[] getCategoryBtnTFLabels() {	return new String[] {"Hide Sphere Categories", "Show Sphere Categories"};}	
	
	/**
	 * This will return instance class-based true and false labels for save segment data.  if empty then no segment saving possible
	 * @return array holding true(idx0) and false(idx1) labels for buttons to control saving of segment data
	 */
	@Override
	protected final String[] getSegmentSaveBtnTFLabels() {return new String[] {"Saving Class, Category and Feature weight segment BMUs", "Save Class, Category and Feature weight segment BMUs" };}
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
	@Override
	protected final void setupGUIObjsArasIndiv(ArrayList<Object[]> tmpUIObjArray, TreeMap<Integer, String[]> tmpListObjVals) {		// TODO Auto-generated method stub
		
	}
	@Override
	protected void initMapArasIndiv(int w, int h, int format, int num2ndFtrVals) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void setVisScreenDimsPriv_Indiv() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void setPrivFlagsIndiv(int idx, boolean val) {
		switch (idx) {//special actions for each flag
			case mapShowLocClrIDX		: {//draw all map nodes, even empty
				break;}						
		}
	}
	
	
	@Override
	//draw 2d constructs over 3d area on screen - draws behind left menu section
	//modAmtMillis is in milliseconds
	protected void drawRightSideInfoBarPriv(float modAmtMillis) {
		pa.pushMatrix();pa.pushStyle();
		//display current simulation variables - call sim world through sim exec
		if(curPreBuiltMapIDX == -1) {curPreBuiltMapIDX=0;}
		mapMgr.drawResultBar(pa, yOff,curPreBuiltMapIDX);
		pa.popStyle();pa.popMatrix();					
	}//drawOnScreenStuff
	
	@Override
	protected void drawOnScreenStuffPriv(float modAmtMillis) {}
	@Override
	//set flags that should be set on each frame - these are set at beginning of frame draw
	protected void drawSetDispFlags() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void drawMapIndiv() {
		
		
	}

	@Override
	protected void drawSegmentsUMatrixDispIndiv() {
			
	}

	@Override
	protected void setUIWinValsIndiv(int UIidx) {}
	
	
	@Override
	public void initDrwnTrajIndiv(){}
	@Override
	public void drawCustMenuObjs(){}
	@Override
	protected boolean simMe(float modAmtSec) {	return true;}
	//set camera to custom location - only used if dispFlag set
	@Override
	protected void setCameraIndiv(float[] camVals){	}
	@Override
	protected void stopMe() {}	
	@Override
	protected void launchMenuBtnHndlr() {
		msgObj.dispMessage("mySOMMapUIWin","launchMenuBtnHndlr","Begin requested action", MsgCodes.info4);
		int btn = curCustBtn[curCustBtnType];
		switch(curCustBtnType) {
		case mySideBarMenu.btnAuxFunc1Idx : {//row 1 of menu side bar buttons
			msgObj.dispMessage("mySOMMapUIWin","launchMenuBtnHndlr","Click Functions 1 in "+name+" : btn : " + btn, MsgCodes.info4);
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
				default : {
					msgObj.dispMessage("mySOMMapUIWin","launchMenuBtnHndlr","Unknown Functions 1 btn : "+btn, MsgCodes.warning2);
					break;}
			}	
			break;}//row 1 of menu side bar buttons
	
		case mySideBarMenu.btnAuxFunc2Idx : {//row 2 of menu side bar buttons
			msgObj.dispMessage("mySOMMapUIWin","launchMenuBtnHndlr","Click Functions 2 in "+name+" : btn : " + btn, MsgCodes.info4);//{"Ld&Bld SOM Data", "Load SOM Config", "Ld & Make Map", "Ld Prebuilt Map"},	//row 2
			//		{"Train Data","True Prspcts", "Prods", "SOM Cfg", "Func 14"},	//row 2

			switch(btn){
				case 0 : {	
					mapMgr.loadPreprocAndBuildTestTrainPartitions(getTrainTestDatPartition(), true);
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
					msgObj.dispMessage("mySOMMapUIWin","launchMenuBtnHndlr","Unknown Functions 2 btn : "+btn, MsgCodes.warning2);
					resetButtonState();
					break;}	
			}
			break;}//row 2 of menu side bar buttons
		case mySideBarMenu.btnAuxFunc3Idx : {//row 3 of menu side bar buttons
			msgObj.dispMessage("mySOMMapUIWin","launchMenuBtnHndlr","Click Functions 3 in "+name+" : btn : " + btn, MsgCodes.info4);//{"Ld&Bld SOM Data", "Load SOM Config", "Ld & Make Map", "Ld Prebuilt Map"},	//row 2
			switch(btn){
				case 0 : {	
					mapMgr.loadTrainDataMapConfigAndBuildMap(false);
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
					msgObj.dispMessage("mySOMMapUIWin","launchMenuBtnHndlr","Unknown Functions 3 btn : "+btn, MsgCodes.warning2);
					resetButtonState();
					break;}	
			}
			break;}//row 3 of menu side bar buttons
		case mySideBarMenu.btnAuxFunc4Idx : {//row 3 of menu side bar buttons
			msgObj.dispMessage("mySOMMapUIWin","launchMenuBtnHndlr","Click Functions 3 in "+name+" : btn : " + btn, MsgCodes.info4);//{"Ld&Bld SOM Data", "Load SOM Config", "Ld & Make Map", "Ld Prebuilt Map"},	//row 2
			switch(btn){
				case 0 :
				case 1 : 
				case 2 : 
				case 3 : {//load all training data, default map config, and build map
					mapMgr.loadPretrainedExistingMap(btn, true);//runs in thread, button state reset there
					resetButtonState();
					break;}
				default : {
					msgObj.dispMessage("mySOMMapUIWin","launchMenuBtnHndlr","Unknown Functions 3 btn : "+btn, MsgCodes.warning2);
					resetButtonState();
					break;}	
			}
			break;}//row 3 of menu side bar buttons
		case mySideBarMenu.btnDBGSelCmpIdx : {//row 4 of menu side bar buttons (debug)	
			msgObj.dispMessage("mySOMMapUIWin","launchMenuBtnHndlr","Click Debug in "+name+" : btn : " + btn, MsgCodes.info4);
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
					msgObj.dispMessage("mySOMMapUIWin","launchMenuBtnHndlr","Unknown Debug btn : "+btn, MsgCodes.warning2);
					resetButtonState();
					break;}
			}				
			break;}//row 4 of menu side bar buttons (debug)			
		}		
		msgObj.dispMessage("mySOMMapUIWin","launchMenuBtnHndlr","End requested action (multithreaded actions may still be working).", MsgCodes.info4);
	}
	@Override
	protected String[] getSaveFileDirNamesPriv() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setCustMenuBtnNames() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void hndlFileLoad(File file, String[] vals, int[] stIdx) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public ArrayList<String> hndlFileSave(File file) {
		// TODO Auto-generated method stub
		return null;
	}

	//handle mouseover 
	@Override
	protected boolean hndlMouseMoveIndiv(int mouseX, int mouseY, myPoint mseClckInWorld){
		boolean res = false;
		if(getPrivFlags(mapDataLoadedIDX)){ res = chkMouseOvr(mouseX, mouseY);	}
		return res;
	}	
	@Override
	protected boolean hndlMouseClickIndiv(int mouseX, int mouseY, myPoint mseClckInWorld, int mseBtn) {
		boolean mod = false;			
		if(mod) {return mod;}
		else {return checkUIButtons(mouseX, mouseY);}
	}
	@Override
	protected boolean hndlMouseDragIndiv(int mouseX, int mouseY,int pmouseX, int pmouseY, myPoint mouseClickIn3D, myVector mseDragInWorld, int mseBtn) {
		boolean mod = false;
		
		return mod;
	}
	@Override
	protected void hndlMouseRelIndiv() {	}	
	@Override
	public String toString(){
		String res = super.toString();
		return res;
	}

	@Override
	protected String getCategoryUIObjLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getClassUIObjLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setPrivFlags_LockCatForClassSegs(boolean val) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected int getCategoryFromClass(int _curCatIDX, int _classIDX) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getClassFromCategory(int _catIDX, int _curClassIDX) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getClassLabelFromIDX(int _idx) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getCategoryLabelFromIDX(int _idx) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void drawMapRectangle_Indiv(int curImgNum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void drawPerFtrMap_Indiv() {
		// TODO Auto-generated method stub
		
	}

}//myTrajEditWin
