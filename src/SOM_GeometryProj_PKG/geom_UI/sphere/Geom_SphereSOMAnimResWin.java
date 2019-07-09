package SOM_GeometryProj_PKG.geom_UI.sphere;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;

import SOM_GeometryProj_PKG.SOM_GeometryMain;
import SOM_GeometryProj_PKG.geom_UI.base.SOM_AnimWorldWin;
import base_SOM_Objects.SOM_MapManager;
import base_UI_Objects.*;
import base_UI_Objects.drawnObjs.myDrawnSmplTraj;
import base_Utils_Objects.vectorObjs.myPoint;
import base_Utils_Objects.vectorObjs.myVector;

public class Geom_SphereSOMAnimResWin extends SOM_AnimWorldWin {
	//ui vars
	public final static int
		gIDX_MinRadius		= numBaseAnimWinUIObjs + 0,
		gIDX_MaxRadius		= numBaseAnimWinUIObjs + 1;		//ID of a UI object to be selected and highlighted

	public final int numGUIObjs = numBaseAnimWinUIObjs + 2;											//# of gui objects for ui

//	//private child-class flags - start at numBaseAnimWinPrivFlags
//	public static final int 
//		XXXXAnimIDX = 0;						//debug
	/**
	 * # of private boolean flags for this window - expands upon those determined in SOM_AnimWorldWin
	 */
	private final int numPrivFlags = numBaseAnimWinPrivFlags;
	

	public float minSphRad = 5, maxSphRad = 50;
	

	public Geom_SphereSOMAnimResWin(my_procApplet _p, String _n, int _flagIdx, int[] fc, int[] sc, float[] rd, float[] rdClosed,String _winTxt, boolean _canDrawTraj) {
		super(_p, _n, _flagIdx, fc, sc, rd, rdClosed, _winTxt, _canDrawTraj, "Spheres");
		super.initThisWin(_canDrawTraj, true, false);
	}

	/**
	 * return appropriate map manager for this window
	 */
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

	/**
	 * call to build or rebuild geometric objects
	 */
	@Override
	public final void initAllGeomObjs() {
		
	}
	/**
	 * call to save the data for all the objects in the scene
	 */
	@Override
	protected final void saveGeomObjInfo() {
		// TODO Auto-generated method stub
		
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
		tmpUIObjArray.add(new Object[] {new double[]{1,100,1}, (double)minSphRad, "Min sphere radius", new boolean[]{false, false, true}});   				//gIDX_NumUIObjs 		                                                                        
		tmpUIObjArray.add(new Object[] {new double[]{10,500,1},(double)maxSphRad, "Max sphere radius", new boolean[]{false, false, true}});  				//gIDX_NumUISamples 	                                                                        	
	}
	
	/**
	 * For instance-class specific ui values
	 * @param UIidx
	 */
	@Override
	protected void setUIWinVals_Indiv(int UIidx) {
		float val = (float)guiObjs[UIidx].getVal();
		int ival = (int)val;

		switch(UIidx){		
			case gIDX_MinRadius : {
				if(val != minSphRad){
					minSphRad = val;
					if(minSphRad >= maxSphRad) { maxSphRad = minSphRad + 1;setWinToUIVals(gIDX_MaxRadius, maxSphRad);   }
					initAllGeomObjs();}
				break;}
			case gIDX_MaxRadius	: {
				if(val != maxSphRad){
					maxSphRad = val;
					if(minSphRad >= maxSphRad)  { minSphRad = maxSphRad - 1;setWinToUIVals(gIDX_MinRadius, minSphRad);   }				
					initAllGeomObjs();}
				break;}
			default : {break;}
		}	
	}

	@Override
	protected void initMe_Indiv() {	
	}
	

	@Override
	public void initDrwnTrajIndiv(){}
	

	//overrides function in base class mseClkDisp
	@Override
	public void drawTraj3D(float animTimeMod,myPoint trans){
		
	}//drawTraj3D

	@Override
	protected void drawRightSideInfoBarPriv(float modAmtMillis) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void drawOnScreenStuffPriv(float modAmtMillis) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void drawCustMenuObjs(){
		((SOM_GeometryMain) pa).drawSOMUIObjs();		
	}


	@Override
	protected void launchMenuBtnHndlr() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void processTrajIndiv(myDrawnSmplTraj drawnNoteTraj){	}
	@Override
	protected boolean hndlMouseMoveIndiv(int mouseX, int mouseY, myPoint mseClckInWorld){
		return false;
	}
	//alt key pressed handles trajectory
	//cntl key pressed handles unfocus of spherey
	@Override
	protected boolean hndlMouseClickIndiv(int mouseX, int mouseY, myPoint mseClckInWorld, int mseBtn) {
		boolean res = checkUIButtons(mouseX, mouseY);
		if(res) {return res;}
//		//pa.outStr2Scr("sphere ui click in world : " + mseClckInWorld.toStrBrf());
//		if((!privFlags[sphereSelIDX]) && (curSelSphere!="")){			//set flags to fix sphere
//			res = true;
//			setPrivFlags(sphereSelIDX,true);			
//		} else if((privFlags[sphereSelIDX]) && (curSelSphere!="")){
//			if(pa.flags[pa.cntlKeyPressed]){			//cntl+click to deselect a sphere		
//				setPrivFlags(sphereSelIDX,false);
//				curSelSphere = ""; 
//				res = true;
//			} else {									//pass click through to selected sphere
//				res = sphereCntls.get(curSelSphere).hndlMouseClickIndiv(mouseX, mouseY, mseClckInWorld,curMseLookVec);				
//			}
//		}
		return res;
	}//hndlMouseClickIndiv

	@Override
	protected boolean hndlMouseDragIndiv(int mouseX, int mouseY, int pmouseX, int pmouseY, myPoint mouseClickIn3D, myVector mseDragInWorld, int mseBtn) {
		boolean res = false;
		//pa.outStr2Scr("hndlMouseDragIndiv sphere ui drag in world mouseClickIn3D : " + mouseClickIn3D.toStrBrf() + " mseDragInWorld : " + mseDragInWorld.toStrBrf());
//		if((privFlags[sphereSelIDX]) && (curSelSphere!="")){//pass drag through to selected sphere
//			//pa.outStr2Scr("sphere ui drag in world mouseClickIn3D : " + mouseClickIn3D.toStrBrf() + " mseDragInWorld : " + mseDragInWorld.toStrBrf());
//			res = sphereCntls.get(curSelSphere).hndlMouseDragIndiv(mouseX, mouseY, pmouseX, pmouseY, mouseClickIn3D,curMseLookVec, mseDragInWorld);
//		}
		return res;
	}
	@Override
	protected void hndlMouseRelIndiv() {}
	
	@Override
	protected void snapMouseLocs(int oldMouseX, int oldMouseY, int[] newMouseLoc) {}	
	@Override
	protected void endShiftKeyI() {}
	@Override
	protected void endAltKeyI() {}
	@Override
	protected void endCntlKeyI() {}
	@Override
	protected void addSScrToWinIndiv(int newWinKey){}
	@Override
	protected void addTrajToScrIndiv(int subScrKey, String newTrajKey){}
	@Override
	protected void delSScrToWinIndiv(int idx) {}	
	@Override
	protected void delTrajToScrIndiv(int subScrKey, String newTrajKey) {}
	//resize drawn all trajectories
	@Override
	protected void resizeMe(float scale) {}
	@Override
	protected void closeMe() {}


	@Override
	protected String[] getSaveFileDirNamesPriv() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected myPoint getMsePtAs3DPt(myPoint mseLoc) {		return new myPoint(mseLoc);	}

	@Override
	protected void setVisScreenDimsPriv() {
		float xStart = rectDim[0] + .5f*(curVisScrDims[0] - (curVisScrDims[1]-(2*xOff)));

		//now build calc analysis offset struct
		
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

}

