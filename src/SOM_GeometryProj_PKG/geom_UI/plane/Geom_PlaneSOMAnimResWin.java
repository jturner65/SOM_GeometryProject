/**
 * 
 */
package SOM_GeometryProj_PKG.geom_UI.plane;

import java.util.ArrayList;
import java.util.TreeMap;

import SOM_GeometryProj_PKG.SOM_GeometryMain;
import SOM_GeometryProj_PKG.geom_UI.base.SOM_AnimWorldWin;
import base_SOM_Objects.SOM_MapManager;
import base_UI_Objects.my_procApplet;
import base_Utils_Objects.vectorObjs.myPoint;
import base_Utils_Objects.vectorObjs.myVector;

/**
 * @author john
 *
 */
public class Geom_PlaneSOMAnimResWin extends SOM_AnimWorldWin {
	
	
//	//private child-class flags - start at numBaseAnimWinPrivFlags
//	public static final int 
//		XXXXAnimIDX = 0;						//debug
	private final int numPrivFlags = numBaseAnimWinPrivFlags;	

	public Geom_PlaneSOMAnimResWin(my_procApplet _p, String _n, int _flagIdx, int[] fc, int[] sc, float[] rd,float[] rdClosed, String _winTxt, boolean _canDrawTraj) {
		super(_p, _n, _flagIdx, fc, sc, rd, rdClosed, _winTxt, _canDrawTraj);
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
		//tmpBtnNamesArray.add(new Object[]{"Debugging","Debug",debugAnimIDX});
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
	public String getObjectType() {
		// TODO Auto-generated method stub
		return "Planes";
	}

	/**
	 * set values for instancing class-specific boolean flags
	 * @param idx
	 * @param val
	 */
	@Override
	protected void setPrivFlags_Indiv(int idx, boolean val) {
		int flIDX = idx/32, mask = 1<<(idx%32);
		privFlags[flIDX] = (val ?  privFlags[flIDX] | mask : privFlags[flIDX] & ~mask);
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
	protected void setupGUIObjsAras_Indiv(ArrayList<Object[]> tmpUIObjArray, TreeMap<Integer, String[]> tmpListObjVals) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setUIWinVals_Indiv(int UIidx) {
		float val = (float)guiObjs[UIidx].getVal();
		int ival = (int)val;
		switch(UIidx){		
			default : {break;}
		}	
	}

	@Override
	protected void initMe_Indiv() {	
	}

	@Override
	public void drawCustMenuObjs() {
		((SOM_GeometryMain) pa).drawSOMUIObjs();
		
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
	}

	@Override
	protected myPoint getMsePtAs3DPt(myPoint mseLoc) {		return new myPoint(mseLoc);	}

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
		return false;
	}

	@Override
	protected boolean hndlMouseDragIndiv(int mouseX, int mouseY, int pmouseX, int pmouseY, myPoint mouseClickIn3D,myVector mseDragInWorld, int mseBtn) {
		return false;
	}

	@Override
	protected void snapMouseLocs(int oldMouseX, int oldMouseY, int[] newMouseLoc) {
	}

	@Override
	protected void hndlMouseRelIndiv() {	}

	@Override
	protected void setCustMenuBtnNames() {
		
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
