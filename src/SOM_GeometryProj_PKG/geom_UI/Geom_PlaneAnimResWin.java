/**
 * 
 */
package SOM_GeometryProj_PKG.geom_UI;

import java.util.ArrayList;
import java.util.TreeMap;

import SOM_GeometryProj_PKG.SOM_GeometryMain;
import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_PlaneSOMExample;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers.Geom_PlaneMapMgr;
import SOM_GeometryProj_PKG.som_geom.geom_UI.SOM_AnimWorldWin;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomObj;
import base_SOM_Objects.SOM_MapManager;
import base_UI_Objects.my_procApplet;
import base_Utils_Objects.vectorObjs.myPoint;
import base_Utils_Objects.vectorObjs.myVector;

/**
 * @author john
 *
 */
public class Geom_PlaneAnimResWin extends SOM_AnimWorldWin {
	
	
	//private child-class flags - start at numBaseAnimWinPrivFlags
	/**
	 * # of private boolean flags for this window - expands upon those determined in SOM_AnimWorldWin
	 */
	public static final int 
		showOrthoFrameIDX 			= numBaseAnimWinPrivFlags + 0;
	private final int numPrivFlags = numBaseAnimWinPrivFlags + 1;	
	

	public Geom_PlaneAnimResWin(my_procApplet _p, String _n, int _flagIdx, int[] fc, int[] sc, float[] rd,float[] rdClosed, String _winTxt, boolean _canDrawTraj) {
		super(_p, _n, _flagIdx, fc, sc, rd, rdClosed, _winTxt, _canDrawTraj, "Planes");
		super.initThisWin(_canDrawTraj, true, false);
	}

	@Override
	public SOM_MapManager buildMapManager() {
		//(SOM_MapUIWin _win, SOM_AnimWorldWin _dispWin, float[] _dims, float[][] _worldBounds, TreeMap<String, Object> _argsMap)
		Geom_PlaneMapMgr _mgr = new Geom_PlaneMapMgr(null, this, SOMMapDims, pa.cubeBnds, ((SOM_GeometryMain)pa).argsMap);
		return _mgr;
	}

	@Override
	protected final void initMe_Indiv() {	
		setPrivFlags(showOrthoFrameIDX, true);
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
	@Override
	protected final int initAllAnimWorldPrivBtns_Indiv(ArrayList<Object[]> tmpBtnNamesArray) {
		tmpBtnNamesArray.add(new Object[]{"Showing Plane Ortho Frame","Hiding Plane Ortho Frame",showOrthoFrameIDX});
		return numPrivFlags;
	}
	
	/**
	 * Instance class determines the true and false labels for button to control showing full object, or just wire frame
	 * If empty no button is displayed
	 * @return array holding true(idx0) and false(idx1) labels for button
	 */
	@Override
	protected final String[] getShowWireFrameBtnTFLabels() {	return new String[] {"Showing Plane Wireframe","Showing Full Plane"};}


	/**
	 * set values for instancing class-specific boolean flags
	 * @param idx
	 * @param val
	 */
	@Override
	protected final void setPrivFlags_Indiv(int idx, boolean val) {
		switch (idx) {//special actions for each flag
			case showOrthoFrameIDX 	: {break;}
			default					: {return;}
		}
	}//setPrivFlags_Indiv
	
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
	protected final void setupGUIObjsAras_Indiv(ArrayList<Object[]> tmpUIObjArray, TreeMap<Integer, String[]> tmpListObjVals) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * send all instance-specific values from UI to map manager
	 */
	protected final void setMapMgrGeomObjVals_Indiv() {};

	
	@Override
	protected final int getMinNumObjs() {	return 1;}
	@Override
	protected final int getMaxNumObjs() {	return 20;}
	@Override
	protected final int getMinNumSmplsPerObj() {return 10;}
	@Override
	protected final int getMaxNumSmplsPerObj() {return 500;}
	/**
	 * calculate the max # of examples for this type object - clique of object description degree 
	 */
	@Override
	protected final long getNumTrainingExamples(int objs, int smplPerObj) {
		long ttlNumSamples = objs * smplPerObj;
		return (ttlNumSamples *(ttlNumSamples-1)*(ttlNumSamples-2))/6;
	}

	@Override
	protected final void setUIWinVals_Indiv(int UIidx, float val) {
		int ival = (int)val;
		switch(UIidx){		
			default : {break;}
		}	
	}

	/**
	 * call to save the data for all the objects in the scene
	 */
	@Override
	protected final void saveGeomObjInfo() {		
	}
	

	//////////////////////////////
	// instance-based draw handling
	
	@Override
	protected final void drawRightSideInfoBarPriv(float modAmtMillis) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected final void drawOnScreenStuffPriv(float modAmtMillis) {
		// TODO Auto-generated method stub
		
	}

	
	/**
	 * instance-specific drawing setup before objects are actually drawn 
	 */
	protected final void drawMeFirst_Indiv() {//need to translate by half the screen width to center coords
		//pa.hint(pa.ENABLE_DEPTH_SORT);//slow
	}
	
	/**
	 * instance-specific drawing after objects are drawn but before info is saved
	 */
	protected final void drawMeLast_Indiv() {		
		//pa.hint(pa.DISABLE_DEPTH_SORT);//slow
		if(getPrivFlags(uiObjDataLoadedIDX)){
			if(getPrivFlags(showOrthoFrameIDX)) {((Geom_PlaneMapMgr)mapMgr).drawAllPlanesOrthoFrames(pa);}
		}
	}	
	
	//////////////////////////////
	//  manage menu button selections and setup

	@Override
	protected final void setCustMenuBtnNames() {
		// TODO Auto-generated method stub
		
	}
	
	//////////////////////////////
	// instance-based mouse handling

	@Override
	protected final boolean hndlMseMove_Priv(int mouseX, int mouseY, myPoint mseClckInWorld) {
		return false;
	}

	@Override
	protected final boolean hndlMseClick_Priv(int mouseX, int mouseY, myPoint mseClckInWorld, int mseBtn) {
		return false;
	}

	@Override
	protected final boolean hndlMseDrag_Priv(int mouseX, int mouseY, int pmouseX, int pmouseY, myPoint mouseClickIn3D,myVector mseDragInWorld, int mseBtn) {
		return false;
	}

	@Override
	protected final void hndlMseRelease_Priv() {		
	}
	
	
	@Override
	public final void initDrwnTrajIndiv(){}
	

	//overrides function in base class mseClkDisp
	@Override
	public final void drawTraj3D(float animTimeMod,myPoint trans){
		
	}//drawTraj3D

	
	@Override
	protected final void snapMouseLocs(int oldMouseX, int oldMouseY, int[] newMouseLoc) {}	
	@Override
	protected final void endShiftKeyI() {}
	@Override
	protected final void endAltKeyI() {}
	@Override
	protected final void endCntlKeyI() {}
	@Override
	protected myPoint getMsePtAs3DPt(myPoint mseLoc) {		return new myPoint(mseLoc);	}


}//class Geom_PlaneSOMAnimResWin
