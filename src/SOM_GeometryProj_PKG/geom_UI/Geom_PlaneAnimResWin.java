/**
 * 
 */
package SOM_GeometryProj_PKG.geom_UI;

import java.util.TreeMap;

import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_PlaneSOMExample;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers.Geom_PlaneMapMgr;
import base_Math_Objects.vectorObjs.doubles.myPoint;
import base_Math_Objects.vectorObjs.doubles.myVector;
import base_Render_Interface.IRenderInterface;
import base_SOM_Objects.som_geom.SOM_GeomMapManager;
import base_SOM_Objects.som_geom.geom_UI.SOM_AnimWorldWin;
import base_SOM_Objects.som_geom.geom_utils.geom_objs.SOM_GeomObjTypes;
import base_UI_Objects.GUI_AppManager;
import base_UI_Objects.windowUI.uiObjs.base.GUIObj_Params;

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
	
	/**
	 * 
	 * @param _p
	 * @param _AppMgr
	 * @param _winIdx
	 */
	public Geom_PlaneAnimResWin(IRenderInterface _p, GUI_AppManager _AppMgr, int _winIdx) {
		super(_p, _AppMgr, _winIdx, SOM_GeomObjTypes.plane);
		super.initThisWin(false);
	}

	@Override
	public SOM_GeomMapManager buildGeom_SOMMapManager() {
		Geom_PlaneMapMgr _mgr = new Geom_PlaneMapMgr(somUIWin, this, AppMgr.get3dCubeBnds(),  AppMgr.getArgsMap());
		return _mgr;
	}
	
	/**
	 * Initialize any UI control flags appropriate for specific instanced SOM Animation window
	 */
	@Override
	protected final void initDispFlags_Indiv() {}

	@Override
	protected final void initMe_Indiv() {	
		uiMgr.setPrivFlag(showOrthoFrameIDX, true);
	}
	
	/**
	 * Retrieve the total number of defined privFlags booleans (application-specific state bools and interactive buttons)
	 */
	@Override
	public int getTotalNumOfPrivBools() {		return numPrivFlags;	}
	
	/**
	 * Instance class determines the true and false labels for button to control showing full object, or just wire frame
	 * If empty no button is displayed
	 * @return array holding true(idx0) and false(idx1) labels for button
	 */
	@Override
	protected final String[] getShowWireFrameBtnTFLabels() {	return new String[]{"Showing Plane Wireframe","Showing Full Plane"};}


	/**
	 * set values for instancing class-specific boolean flags
	 * @param idx
	 * @param val
	 */
	@Override
	protected final void handleSOMAnimFlags_Indiv(int idx, boolean val) {
		switch (idx) {//special actions for each flag
			case showOrthoFrameIDX 	: {break;}
			default					: {return;}
		}
	}//setPrivFlags_Indiv
	
	/**
	 * Build all UI objects to be shown in left side bar menu for this window. This is the first child class function called by initThisWin
	 * @param tmpUIObjMap : map of GUIObj_Params, keyed by unique string, with values describing the UI object
	 * 			- The object IDX                   
	 *          - A double array of min/max/mod values                                                   
	 *          - The starting value                                                                      
	 *          - The label for object                                                                       
	 *          - The object type (GUIObj_Type enum)
	 *          - A boolean array of behavior configuration values : (unspecified values default to false)
	 *           	idx 0: value is sent to owning window,  
	 *           	idx 1: value is sent on any modifications (while being modified, not just on release), 
	 *           	idx 2: changes to value must be explicitly sent to consumer (are not automatically sent),
	 *          - A boolean array of renderer format values :(unspecified values default to false) - Behavior Boolean array must also be provided!
	 * 				idx 0 : Should be multiline
	 * 				idx 1 : One object per row in UI space (i.e. default for multi-line and btn objects is false, single line non-buttons is true)
	 * 				idx 2 : Text should be centered (default is false)
	 * 				idx 3 : Object should be rendered with outline (default for btns is true, for non-buttons is false)
	 * 				idx 4 : Should have ornament
	 * 				idx 5 : Ornament color should match label color 
	 */
	@Override
	protected final void setupGUIObjsAras_Indiv(TreeMap<String, GUIObj_Params> tmpUIObjMap) {}

	/**
	 * Build all UI buttons to be shown in left side bar menu for this window. This is for instancing windows to add to button region
	 * @param firstIdx : the first index to use in the map/as the objIdx
	 * @param tmpUIBoolSwitchObjMap : map of GUIObj_Params to be built containing all flag-backed boolean switch definitions, keyed by sequential value == objId
	 * 				the first element is the object index
	 * 				the second element is true label
	 * 				the third element is false label
	 * 				the final element is integer flag idx 
	 */
	@Override
	protected final void setupGUIBoolSwitchAras_Indiv(int firstIdx, TreeMap<String, GUIObj_Params> tmpUIBoolSwitchObjMap)  {
		int idx=firstIdx;
		tmpUIBoolSwitchObjMap.put("Button_"+idx, uiMgr.uiObjInitAra_Switch(idx, "button_"+idx++, "Showing Plane Ortho Frame","Hiding Plane Ortho Frame",showOrthoFrameIDX));
	}
	/**
	 * send all instance-specific values from UI to map manager
	 */
	protected final void setMapMgrGeomObjVals_Indiv() {};

	@Override
	protected final String[] setUI_GeomObjFeatureListVals() {	return Geom_PlaneSOMExample.ftrNames;};

	@Override
	protected final int getMinNumObjs() {	return 1;}
	@Override
	protected final int getMaxNumObjs() {	return 20;}
	@Override
	protected final int getMinNumSmplsPerObj() {return 20;}
	@Override
	protected final int getMaxNumSmplsPerObj() {return 1000;}
	@Override
	protected final int getModNumSmplsPerObj() {return 100;}
	/**
	 * calculate the max # of examples for this type object - clique of object description degree 
	 */
	@Override
	protected final long getNumTrainingExamples(int objs, int smplPerObj) {
		long ttlNumSamples = objs * smplPerObj;
		return (ttlNumSamples *(ttlNumSamples-1L)*(ttlNumSamples-2L))/6L;
	}
	@Override
	protected boolean setUI_IntValsCustom_Indiv(int UIidx, int ival, int oldVal) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean setUI_FloatValsCustom_Indiv(int UIidx, float ival, float oldVal) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void getAllUIValsForPreProcSave_Indiv(TreeMap<String, String> vals) {}

	@Override
	protected void setAllUIValsFromPreProcLoad_Indiv(TreeMap<String, String> uiVals) {}
	
	//////////////////////////////
	// instance-based draw handling
	/**
	 * any instance-window specific display
	 * @param modAmtMillis
	 */
	@Override
	protected final float drawRightSideInfoBar_Indiv(float modAmtMillis, float yOff) {
		
		return yOff;
	}

	@Override
	protected final void drawOnScreenStuffPriv(float modAmtMillis) {
	}

	
	/**
	 * instance-specific drawing setup before objects are actually drawn 
	 */
	protected final void drawMeFirst_Indiv() {//need to translate by half the screen width to center coords
	}
	
	/**
	 * instance-specific drawing after objects are drawn but before info is saved
	 */
	protected final void drawMeLast_Indiv() {		
		if(uiMgr.getPrivFlag(showOrthoFrameIDX)) {((Geom_PlaneMapMgr)mapMgr).drawAllPlanesOrthoFrames(ri);}
	}	
	
	//////////////////////////////
	//  manage menu button selections and setup

	@Override
	protected final void setCustMenuBtnLabels() {
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
	public final void initDrwnTraj_Indiv(){}	
	@Override
	protected final void snapMouseLocs(int oldMouseX, int oldMouseY, int[] newMouseLoc) {}	
	@Override
	protected final void endShiftKey_Indiv() {}
	@Override
	protected final void endAltKey_Indiv() {}
	@Override
	protected final void endCntlKey_Indiv() {}
	@Override
	protected myPoint getMsePtAs3DPt(myPoint mseLoc) {		return new myPoint(mseLoc);	}


}//class Geom_PlaneSOMAnimResWin
