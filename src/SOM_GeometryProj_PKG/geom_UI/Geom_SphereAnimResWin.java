package SOM_GeometryProj_PKG.geom_UI;

import java.util.ArrayList;
import java.util.TreeMap;

import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_SphereSOMExample;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers.Geom_SphereMapMgr;
import base_Render_Interface.IRenderInterface;
import base_Math_Objects.vectorObjs.doubles.myPoint;
import base_Math_Objects.vectorObjs.doubles.myVector;
import base_SOM_Objects.som_geom.SOM_GeomMapManager;
import base_SOM_Objects.som_geom.geom_UI.SOM_AnimWorldWin;
import base_SOM_Objects.som_geom.geom_utils.geom_objs.SOM_GeomObjTypes;
import base_UI_Objects.GUI_AppManager;

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
		//tmpBtnNamesArray.add(uiMgr.uiObjInitAra_Btn(new String[] {"Debugging","Debug",debugAnimIDX});
		return numPrivFlags;
	}
	/**
	 * Instance class determines the true and false labels for button to control showing full object, or just wire frame
	 * If empty no button is displayed
	 * @return array holding true(idx0) and false(idx1) labels for button
	 */
	@Override
	protected final String[] getShowWireFrameBtnTFLabels() {	return new String[] {"Showing Sphere Wireframe","Showing Full Sphere"};}

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
	protected final void setupGUIObjsAras_Indiv(TreeMap<Integer, Object[]> tmpUIObjArray , TreeMap<Integer, String[]> tmpListObjVals) {
		tmpUIObjArray.put(gIDX_MinRadius, uiMgr.uiObjInitAra_Float(new double[]{20,500,1}, (double)minSphRad, "Min sphere radius"));   				//gIDX_MinRadius	                                                                        
		tmpUIObjArray.put(gIDX_MaxRadius, uiMgr.uiObjInitAra_Float(new double[]{100,1000,1},(double)maxSphRad, "Max sphere radius"));  				//gIDX_MaxRadius	                                                                        	
	}
	
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

