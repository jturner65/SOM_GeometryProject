package SOM_GeometryProj_PKG.geom_UI;

import java.util.ArrayList;
import java.util.TreeMap;

import SOM_GeometryProj_PKG.SOM_GeometryMain;
import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_LineSOMExample;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers.Geom_LineMapMgr;
import base_SOM_Objects.som_geom.SOM_GeomMapManager;
import base_SOM_Objects.som_geom.geom_UI.SOM_AnimWorldWin;
import base_SOM_Objects.som_geom.geom_utils.geom_objs.SOM_GeomObjTypes;
import base_UI_Objects.my_procApplet;
import base_Utils_Objects.vectorObjs.myPoint;
import base_Utils_Objects.vectorObjs.myVector;

public class Geom_LineAnimResWin extends SOM_AnimWorldWin {
	/**
	 * # of private boolean flags for this window - expands upon those determined in SOM_AnimWorldWin
	 */
	private final int numPrivFlags = numBaseAnimWinPrivFlags;
	/**
	 * mins and diffs for window display
	 */
	protected float[][] win2DMinsAndDiffs;
	/**
	 * 
	 * @param _p
	 * @param _n
	 * @param _flagIdx
	 * @param fc
	 * @param sc
	 * @param rd
	 * @param rdClosed
	 * @param _winTxt
	 * @param _canDrawTraj
	 */	
	private final float dispBrdr = 10.0f;
	public Geom_LineAnimResWin(my_procApplet _p, String _n, int _flagIdx, int[] fc, int[] sc, float[] rd,float[] rdClosed, String _winTxt, boolean _canDrawTraj) {
		super(_p, _n, _flagIdx, fc, sc, rd, rdClosed, _winTxt, _canDrawTraj, SOM_GeomObjTypes.line);
		win2DMinsAndDiffs = new float[2][2];
		win2DMinsAndDiffs[0]= new float[] {-.5f*rectDim[2] + dispBrdr, -.5f*rectDim[3] + dispBrdr};
		win2DMinsAndDiffs[1] = new float[] {rectDim[2]-(2.0f*dispBrdr), rectDim[3]-(9.0f*dispBrdr)};
		super.initThisWin(_canDrawTraj, true, false);
	}

	/**
	 * return appropriate map manager for this window
	 */
	@Override
	public SOM_GeomMapManager buildGeom_SOMMapManager() {
		//(SOM_MapUIWin _win, SOM_AnimWorldWin _dispWin, float[] _dims, float[][] _worldBounds, TreeMap<String, Object> _argsMap)
		Geom_LineMapMgr _mgr = new Geom_LineMapMgr(somUIWin, this, win2DMinsAndDiffs, ((SOM_GeometryMain)pa).argsMap);
		return _mgr;
	}
	
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
	protected void setPrivFlags_Indiv(int idx, boolean val) {
		switch (idx) {//special actions for each flag
			default						: {return;}
		}
	}

	@Override
	protected void setupGUIObjsAras_Indiv(TreeMap<Integer, Object[]> tmpUIObjArray ,TreeMap<Integer, String[]> tmpListObjVals) {
				
	}
	
	/**
	 * send all instance-specific values from UI to map manager
	 */
	protected final void setMapMgrGeomObjVals_Indiv() {};

	@Override
	protected final String[] setUI_GeomObjFeatureListVals() {	return Geom_LineSOMExample.ftrNames;};
	
	@Override
	protected final int getMinNumObjs() {	return 1;}
	@Override
	protected final int getMaxNumObjs() {	return 50;}
	@Override
	protected final int getMinNumSmplsPerObj() {return 10;}
	@Override
	protected final int getMaxNumSmplsPerObj() {return 100;}
	/**
	 * calculate the max # of examples for this type object - clique of object description degree 
	 */
	@Override
	protected final long getNumTrainingExamples(int objs, int smplPerObj) {
		long ttlNumSamples = objs * smplPerObj;
		return (ttlNumSamples *(ttlNumSamples-1L))/2L;
	}
	
	
	@Override
	protected final void setUIWinVals_Indiv(int UIidx, float val) {
		switch(UIidx){	
		default : {break;}
		}
	}

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
	protected final void drawMeFirst_Indiv() {//need to translate by half the screen width to center coords
		//msgObj.dispInfoMessage("Geom_LineAnimResWin", "drawMeFirst_Indiv", "here");
		pa.translate(this.rectDim[0]+(this.rectDim[2]*.5f), (this.rectDim[1]+this.rectDim[3])*.5f);
		pa.pushStyle();
		pa.noStroke();
		pa.setFill(new int[] {255,255,255}, 255);
		pa.sphere(3.0f);
		pa.popStyle();
	}
	
	/**
	 * instance-specific drawing after objects are drawn but before info is saved
	 */
	protected final void drawMeLast_Indiv() {		
		
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
	public void drawTraj3D(float animTimeMod,myPoint trans){
		
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
	protected final myPoint getMsePtAs3DPt(myPoint mseLoc) {	return new myPoint(mseLoc.x, mseLoc.y, 0);}


}//class Geom_LineSOMAnimResWin
