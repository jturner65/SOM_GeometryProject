package SOM_GeometryProj_PKG.geom_UI;

import java.util.TreeMap;

import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_SphereSOMExample;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers.Geom_SphereMapMgr;
import base_Math_Objects.vectorObjs.doubles.myPoint;
import base_Math_Objects.vectorObjs.doubles.myVector;
import base_Render_Interface.IRenderInterface;
import base_SOM_Objects.som_geom.SOM_GeomMapManager;
import base_SOM_Objects.som_geom.geom_UI.SOM_AnimWorldWin;
import base_SOM_Objects.som_geom.geom_utils.geom_objs.SOM_GeomObjTypes;
import base_UI_Objects.GUI_AppManager;
import base_UI_Objects.windowUI.uiObjs.base.GUIObj_Params;

public class Geom_SphereAnimResWin extends SOM_AnimWorldWin {
	//ui vars
	public final static int
		gIDX_MinRadius		= numBaseAnimWinUIObjs + 0,
		gIDX_MaxRadius		= numBaseAnimWinUIObjs + 1;		//ID of a UI object to be selected and highlighted

	/**
	 * # of private boolean flags for this window - expands upon those determined in SOM_AnimWorldWin
	 */
	private final int numPrivFlags = numBaseAnimWinPrivFlags;
	
	/**
	 * set initial values
	 */
	public float minSphRad = 50, maxSphRad = 100;
	
	/**
	 * 
	 * @param _p
	 * @param _AppMgr
	 * @param _winIdx
	 */
	public Geom_SphereAnimResWin(IRenderInterface _p, GUI_AppManager _AppMgr, int _winIdx) {
		super(_p, _AppMgr, _winIdx, SOM_GeomObjTypes.sphere);
		super.initThisWin(false);
	}

	/**
	 * return appropriate map manager for this window
	 */
	@Override
	public final SOM_GeomMapManager buildGeom_SOMMapManager() {
		Geom_SphereMapMgr _mgr = new Geom_SphereMapMgr(somUIWin, this, AppMgr.get3dCubeBnds(),  AppMgr.getArgsMap());
		return _mgr;
	}
	
	/**
	 * Initialize any UI control flags appropriate for specific instanced SOM Animation window
	 */
	@Override
	protected final void initDispFlags_Indiv() {}

	@Override
	protected final void initMe_Indiv() {	
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
	protected final String[] getShowWireFrameBtnTFLabels() {	return new String[]{"Showing Sphere Wireframe","Showing Full Sphere"};}

	/**
	 * set values for instancing class-specific boolean flags
	 * @param idx
	 * @param val
	 */
	@Override
	protected final void handleSOMAnimFlags_Indiv(int idx, boolean val) {
		switch (idx) {//special actions for each flag
			default						: {return;}
		}
	}//setPrivFlags_Indiv
	/**
	 * Build all UI objects to be shown in left side bar menu for this window.  This is the first child class function called by initThisWin
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
	 *          - A boolean array of renderer format values :(unspecified values default to false)
	 *           	idx 0: whether multi-line(stacked) or not                                                  
	 *              idx 1: if true, build prefix ornament                                                      
	 *              idx 2: if true and prefix ornament is built, make it the same color as the text fill color.
	 */
	protected final void setupGUIObjsAras_Indiv(TreeMap<String, GUIObj_Params> tmpUIObjMap) {
		tmpUIObjMap.put("gIDX_MinRadius", uiMgr.uiObjInitAra_Float(gIDX_MinRadius, new double[]{20,500,1}, (double)minSphRad, "Min sphere radius"));   				//gIDX_MinRadius	                                                                        
		tmpUIObjMap.put("gIDX_MaxRadius", uiMgr.uiObjInitAra_Float(gIDX_MaxRadius, new double[]{100,1000,1},(double)maxSphRad, "Max sphere radius"));  				//gIDX_MaxRadius	                                                                        	
	}

	/**
	 * Build all UI buttons to be shown in left side bar menu for this window. This is for instancing windows to add to button region
	 * USE tmpUIBoolSwitchObjMap.size() for start idx
	 * @param tmpUIBoolSwitchObjMap : map of GUIObj_Params to be built containing all flag-backed boolean switch definitions, keyed by sequential value == objId
	 * 				the first element is the object index
	 * 				the second element is true label
	 * 				the third element is false label
	 * 				the final element is integer flag idx 
	 */
	protected final void setupGUIBoolSwitchAras_Indiv(TreeMap<String, GUIObj_Params> tmpUIBoolSwitchObjMap) {}
	
	@Override
	protected final String[] setUI_GeomObjFeatureListVals() {	return Geom_SphereSOMExample.ftrNames;};	
	@Override
	protected final int getMinNumObjs() {	return 1;}
	@Override
	protected final int getMaxNumObjs() {	return 20;}
	@Override
	protected final int getMinNumSmplsPerObj() {return 10;}
	@Override
	protected final int getMaxNumSmplsPerObj() {return 500;}
	@Override
	protected final int getModNumSmplsPerObj() {return 100;}
	/**
	 * calculate the max # of examples for this type object : n choose k where k is degree
	 */
	@Override
	protected final long getNumTrainingExamples(int objs, int smplPerObj) {		
		long ttlNumSamples = objs * smplPerObj;
//		this.msgObj.dispInfoMessage(className, "getNumTrainingExamples", "Choose function :  " +tmpEx );
		long tmpEx2 = (ttlNumSamples *(ttlNumSamples-1L)*(ttlNumSamples-2L)*(ttlNumSamples-3L))/24L;
		//this.msgObj.dispInfoMessage(className, "getNumTrainingExamples", "Manual calc : " + tmpEx2);
		return tmpEx2;
	}
	/**
	 * send all instance-specific values from UI to map manager
	 */
	protected final void setMapMgrGeomObjVals_Indiv() {
		((Geom_SphereMapMgr) mapMgr).setMinMaxRad(minSphRad, maxSphRad);
		
	};

	@Override
	protected boolean setUI_IntValsCustom_Indiv(int UIidx, int ival, int oldVal) {
		return false;
	}

	@Override
	protected boolean setUI_FloatValsCustom_Indiv(int UIidx, float val, float oldVal) {
		switch(UIidx){		
			case gIDX_MinRadius : {
				if(val != minSphRad){
					minSphRad = val;
					if(minSphRad >= maxSphRad) { maxSphRad = minSphRad + 1;uiMgr.setWinToUIVals(gIDX_MaxRadius, maxSphRad);   }
					rebuildSourceGeomObjs();}
					return true;}
			case gIDX_MaxRadius	: {
				if(val != maxSphRad){
					maxSphRad = val;
					if(minSphRad >= maxSphRad)  { minSphRad = maxSphRad - 1;uiMgr.setWinToUIVals(gIDX_MinRadius, minSphRad);   }				
					rebuildSourceGeomObjs();}
					return true;}
			default : {break;}
		}
		return false;
	}
	

	@Override
	protected void getAllUIValsForPreProcSave_Indiv(TreeMap<String, String> vals) {
		vals.put("gIDX_MinRadius", String.format("%4d", (int)uiMgr.getUIValue(gIDX_MinRadius)));
		vals.put("gIDX_MaxRadius", String.format("%4d", (int)uiMgr.getUIValue(gIDX_MaxRadius)));
		
	}

	@Override
	protected void setAllUIValsFromPreProcLoad_Indiv(TreeMap<String, String> uiVals) {
		uiMgr.setNewUIValue(gIDX_MinRadius, Double.parseDouble(uiVals.get("gIDX_MinRadius")));
		uiMgr.setNewUIValue(gIDX_MaxRadius, Double.parseDouble(uiVals.get("gIDX_MaxRadius")));
	}

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
		// TODO Auto-generated method stub
		
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
	protected final myPoint getMsePtAs3DPt(myPoint mseLoc) {		return new myPoint(mseLoc);	}

}//class Geom_SphereSOMAnimResWin

