package SOM_GeometryProj_PKG.som_geom.geom_UI;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;

import SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers.Geom_LineMapMgr;
import SOM_GeometryProj_PKG.geom_UI.Geom_SideBarMenu;
import SOM_GeometryProj_PKG.som_geom.SOM_GeomMapManager;
import base_SOM_Objects.SOM_MapManager;
import base_SOM_Objects.som_ui.win_disp_ui.SOM_MapUIWin;
import base_UI_Objects.my_procApplet;
import base_Utils_Objects.io.MsgCodes;
import base_Utils_Objects.vectorObjs.myPoint;
import base_Utils_Objects.vectorObjs.myVector;

/**
 * window to manage mapMgr interaction - acts like a pop-up window, so is subordinate to a SOM_AnimWorldWin instance
 * @author john
 *
 */
public class SOM_GeomMapUIWin extends SOM_MapUIWin {
	
	public static final int 
		mapShowLocClrIDX 			= numSOMBasePrivFlags + 0;			//show img built of map with each pxl clr built from the 1st 3 features of the interpolated point at that pxl between the map nodes
	
	public final int _numPrivFlags = numSOMBasePrivFlags + 1;

	/**
	 * default args for building map manager
	 */
	protected TreeMap<String, Object> argsMap;
	
	public SOM_GeomMapUIWin(my_procApplet _p, String _n, int _flagIdx, int[] fc, int[] sc, float[] rd, float[] rdClosed, String _winTxt, boolean _canDrawTraj, TreeMap<String, Object> _argsMap) {
		super(_p, _n, _flagIdx, fc, sc, rd, rdClosed, _winTxt, _canDrawTraj);
		argsMap = _argsMap;
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
		tmpBtnNamesArray.add(new Object[] {"Showing Feature[0:2] Clr","Not Showing Feature[0:2] Clr", mapShowLocClrIDX});          
		return _numPrivFlags;
	}
	
	/**
	 * build instance-specific map manager
	 */
	@Override
	protected SOM_MapManager buildMapMgr(float[] SOM_mapDims) {
		//no need to set win here - this is set in SOM Win UI Base class
		//this is just a place holder - windows will set proper map manager when this window is selected to be active
		return new Geom_LineMapMgr(null, null, SOM_mapDims, null, argsMap);
	}
	

	/**
	 * instance-specific window initialization
	 */
	@Override
	protected void initMeIndiv() {
		//default to showing right side bar menu
		//setFlags(showRightSideMenu, true);	
		//moved from mapMgr ctor, to remove dependence on papplet in that object
		//pa.setAllMenuBtnNames(menuBtnNames);	
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
	protected final String[] getClassBtnTFLabels() {	return new String[] {"Hide Classes ","Show Classes "};}
	/**
	 * Instance class determines the true and false labels the category buttons use - if empty then no categories used
	 * @return array holding true(idx0) and false(idx1) labels for buttons to control display of category-based segment
	 */
	@Override
	protected final String[] getCategoryBtnTFLabels() {	return new String[] {"Hide Categories", "Show Categories"};}	
	
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
	protected final void setupGUIObjsArasIndiv(ArrayList<Object[]> tmpUIObjArray, TreeMap<Integer, String[]> tmpListObjVals) {		
	}
	@Override
	protected void initMapArasIndiv(int w, int h, int format, int num2ndFtrVals) {
		
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

	@Override
	protected final void launchMenuBtnHndlr() {	}

}//myTrajEditWin
