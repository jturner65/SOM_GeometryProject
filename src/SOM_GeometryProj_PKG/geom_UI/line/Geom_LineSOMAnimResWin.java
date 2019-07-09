package SOM_GeometryProj_PKG.geom_UI.line;

import java.util.ArrayList;
import java.util.TreeMap;

import SOM_GeometryProj_PKG.geom_UI.base.SOM_AnimWorldWin;
import base_SOM_Objects.SOM_MapManager;
import base_UI_Objects.my_procApplet;
import base_Utils_Objects.vectorObjs.myPoint;
import base_Utils_Objects.vectorObjs.myVector;

public class Geom_LineSOMAnimResWin extends SOM_AnimWorldWin {
	/**
	 * # of private boolean flags for this window - expands upon those determined in SOM_AnimWorldWin
	 */
	private final int numPrivFlags = numBaseAnimWinPrivFlags;
	
	public Geom_LineSOMAnimResWin(my_procApplet _p, String _n, int _flagIdx, int[] fc, int[] sc, float[] rd,float[] rdClosed, String _winTxt, boolean _canDrawTraj) {
		super(_p, _n, _flagIdx, fc, sc, rd, rdClosed, _winTxt, _canDrawTraj, "Lines");
		super.initThisWin(_canDrawTraj, true, false);
	}

	@Override
	protected SOM_MapManager buildMapManager() {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public void initAllGeomObjs() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void saveGeomObjInfo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setPrivFlags_Indiv(int idx, boolean val) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setupGUIObjsAras_Indiv(ArrayList<Object[]> tmpUIObjArray,
			TreeMap<Integer, String[]> tmpListObjVals) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setUIWinVals_Indiv(int UIidx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initMe_Indiv() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawCustMenuObjs() {
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
	protected myPoint getMsePtAs3DPt(myPoint mseLoc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setVisScreenDimsPriv() {
		// TODO Auto-generated method stub
		
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

}
