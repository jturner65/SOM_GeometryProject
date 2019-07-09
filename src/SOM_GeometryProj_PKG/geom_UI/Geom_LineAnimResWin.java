package SOM_GeometryProj_PKG.geom_UI;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;

import SOM_GeometryProj_PKG.SOM_GeometryMain;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers.Geom_LineMapMgr;
import SOM_GeometryProj_PKG.som_geom.geom_UI.SOM_AnimWorldWin;
import base_SOM_Objects.SOM_MapManager;
import base_UI_Objects.my_procApplet;
import base_Utils_Objects.vectorObjs.myPoint;
import base_Utils_Objects.vectorObjs.myVector;

public class Geom_LineAnimResWin extends SOM_AnimWorldWin {
	/**
	 * # of private boolean flags for this window - expands upon those determined in SOM_AnimWorldWin
	 */
	private final int numPrivFlags = numBaseAnimWinPrivFlags;

	public Geom_LineAnimResWin(my_procApplet _p, String _n, int _flagIdx, int[] fc, int[] sc, float[] rd,float[] rdClosed, String _winTxt, boolean _canDrawTraj) {
		super(_p, _n, _flagIdx, fc, sc, rd, rdClosed, _winTxt, _canDrawTraj, "Lines");
		super.initThisWin(_canDrawTraj, true, false);
	}

	/**
	 * return appropriate map manager for this window
	 */
	@Override
	public SOM_MapManager buildMapManager() {
		Geom_LineMapMgr _mgr = new Geom_LineMapMgr(SOMMapDims, ((SOM_GeometryMain)pa).argsMap);
		return _mgr;
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
	 * call to build or rebuild geometric objects
	 */
	protected final void initAllGeomObjs_Indiv() {
		
	}

	@Override
	protected void saveGeomObjInfo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setPrivFlags_Indiv(int idx, boolean val) {
		switch (idx) {//special actions for each flag
			default						: {return;}
		}
	}

	@Override
	protected void setupGUIObjsAras_Indiv(ArrayList<Object[]> tmpUIObjArray,TreeMap<Integer, String[]> tmpListObjVals) {
				
	}

	@Override
	protected void setUIWinVals_Indiv(int UIidx) {
		switch(UIidx){	
		default : {break;}
		}
	}

	@Override
	protected void initMe_Indiv() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void launchMenuBtnHndlr() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String[] getSaveFileDirNamesPriv() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void initDrwnTrajIndiv() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected myPoint getMsePtAs3DPt(myPoint mseLoc) {	return new myPoint(mseLoc.x, mseLoc.y, 0);}

	@Override
	protected void setVisScreenDimsPriv() {
		float xStart = rectDim[0] + .5f*(curVisScrDims[0] - (curVisScrDims[1]-(2*xOff)));
		
	}

	@Override
	protected boolean hndlMouseMoveIndiv(int mouseX, int mouseY, myPoint mseClckInWorld) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean hndlMouseClickIndiv(int mouseX, int mouseY, myPoint mseClckInWorld, int mseBtn) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean hndlMouseDragIndiv(int mouseX, int mouseY, int pmouseX, int pmouseY, myPoint mouseClickIn3D,
			myVector mseDragInWorld, int mseBtn) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void snapMouseLocs(int oldMouseX, int oldMouseY, int[] newMouseLoc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void hndlMouseRelIndiv() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setCustMenuBtnNames() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void drawRightSideInfoBarPriv(float modAmtMillis) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void drawOnScreenStuffPriv(float modAmtMillis) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	protected void endShiftKeyI() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void endAltKeyI() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void endCntlKeyI() {
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
}//class Geom_LineSOMAnimResWin
