package SOM_GeometryProj_PKG.geom_UI;

import java.util.ArrayList;
import java.util.TreeMap;

import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_3DPointSOMExample;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers.Geom_3DPointMapMgr;
import base_Math_Objects.vectorObjs.doubles.myPoint;
import base_Math_Objects.vectorObjs.doubles.myVector;
import base_Render_Interface.IRenderInterface;
import base_SOM_Objects.som_geom.SOM_GeomMapManager;
import base_SOM_Objects.som_geom.geom_UI.SOM_AnimWorldWin;
import base_SOM_Objects.som_geom.geom_utils.geom_objs.SOM_GeomObjTypes;
import base_UI_Objects.GUI_AppManager;

public class Geom_3DPointAnimResWin extends SOM_AnimWorldWin {
	/**
	 * # of private boolean flags for this window - expands upon those determined in SOM_AnimWorldWin
	 */
	private final int numPrivFlags = numBaseAnimWinPrivFlags;
	
	public Geom_3DPointAnimResWin(IRenderInterface _p, GUI_AppManager _AppMgr, int _winIdx) {
		super(_p, _AppMgr, _winIdx, SOM_GeomObjTypes.point);
		super.initThisWin(false);
	}

	@Override
	protected SOM_GeomMapManager buildGeom_SOMMapManager() {
		Geom_3DPointMapMgr _mgr = new Geom_3DPointMapMgr(somUIWin, this, AppMgr.get3dCubeBnds(), AppMgr.getArgsMap());
		return _mgr;
	}
	/**
	 * Initialize any UI control flags appropriate for specific instanced SOM Animation window
	 */
	@Override
	protected final void initDispFlags_Indiv() {}
	
	@Override
	protected void initMe_Indiv() {	
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
	protected int initAllAnimWorldPrivBtns_Indiv(ArrayList<Object[]> tmpBtnNamesArray) {
		// TODO Auto-generated method stub
		return numPrivFlags;
	}
	
	/**
	 * Instance class determines the true and false labels for button to control showing full object, or just wire frame
	 * If empty no button is displayed
	 * @return array holding true(idx0) and false(idx1) labels for button
	 */
	@Override
	protected final String[] getShowWireFrameBtnTFLabels() {	return null;}

	@Override
	protected void handleSOMAnimFlags_Indiv(int idx, boolean val) {
		switch (idx) {//special actions for each flag
			default						: {return;}
		}
	}

	@Override
	protected void setupGUIObjsAras_Indiv(TreeMap<Integer, Object[]> tmpUIObjArray ,TreeMap<Integer, String[]> tmpListObjVals) {}
	
	/**
	 * send all instance-specific values from UI to map manager
	 */
	@Override
	protected final void setMapMgrGeomObjVals_Indiv() {};
	
	@Override
	protected String[] setUI_GeomObjFeatureListVals() {return Geom_3DPointSOMExample.ftrNames;}


	@Override
	protected long getNumTrainingExamples(int objs, int smplPerObj) {
		long ttlNumSamples = objs * smplPerObj;
		return ttlNumSamples;
	}

	@Override
	protected int getMinNumObjs() {return 10;}
	@Override
	protected int getMaxNumObjs() {return 500000;}
	@Override
	protected int getMinNumSmplsPerObj() {return 1;}
	@Override
	protected int getMaxNumSmplsPerObj() {return 1;}

	@Override
	protected int getModNumSmplsPerObj() {return 1;}
	
	@Override
	protected boolean setUI_IntValsCustom_Indiv(int UIidx, int ival, int oldVal) {return false;}

	@Override
	protected boolean setUI_FloatValsCustom_Indiv(int UIidx, float ival, float oldVal) {return false;}

	@Override
	protected void getAllUIValsForPreProcSave_Indiv(TreeMap<String, String> vals) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setAllUIValsFromPreProcLoad_Indiv(TreeMap<String, String> uiVals) {
		// TODO Auto-generated method stub

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
	@Override
	protected final void drawMeFirst_Indiv() {	}
	
	/**
	 * instance-specific drawing after objects are drawn but before info is saved
	 */
	@Override
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
	protected final void hndlMseRelease_Priv() {}	
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
}//class Geom_3DPointAnimResWin
