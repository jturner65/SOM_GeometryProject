package SOM_GeometryProj_PKG.geom_UI.base.anim;

import java.io.File;
import java.util.ArrayList;

import base_SOM_Objects.SOM_MapManager;
import base_SOM_Objects.som_ui.win_disp_ui.SOM_MapUIWin;
import base_UI_Objects.my_procApplet;
import base_UI_Objects.drawnObjs.myDrawnSmplTraj;
import base_UI_Objects.windowUI.myDispWindow;
import base_Utils_Objects.vectorObjs.myPoint;
import base_Utils_Objects.vectorObjs.myVector;

/**
 * this class will instance a combined window to hold an animation world and a map display window overlay
 * @author john
 *
 */

public abstract class SOM_AnimWorldWin extends myDispWindow {
	/**
	 * map manager corresponding to this animation world
	 */
	public SOM_MapManager mapMgr;	
	
	
	
		
	
	public SOM_AnimWorldWin(my_procApplet _p, String _n, int _flagIdx, int[] fc, int[] sc, float[] rd, float[] rdClosed, String _winTxt, boolean _canDrawTraj) {
		super(_p, _n, _flagIdx, fc, sc, rd, rdClosed, _winTxt, _canDrawTraj);
		
	}

	@Override
	public void setPrivFlags(int idx, boolean val) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initAllPrivBtns() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void launchMenuBtnHndlr() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setupGUIObjsAras() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setUIWinVals(int UIidx) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processTrajIndiv(myDrawnSmplTraj drawnTraj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initMe() {
		// TODO Auto-generated method stub

	}

	
	/////////////////////////////
	// drawing routines
	@Override
	protected void showMe() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void closeMe() {
		// TODO Auto-generated method stub

	}
	@Override
	protected void setCameraIndiv(float[] camVals) {
		//, float rx, float ry, float dz are now member variables of every window
		pa.camera(camVals[0],camVals[1],camVals[2],camVals[3],camVals[4],camVals[5],camVals[6],camVals[7],camVals[8]);      
		// puts origin of all drawn objects at screen center and moves forward/away by dz
		pa.translate(camVals[0],camVals[1],(float)dz); 
	    setCamOrient();	
	}

	@Override
	protected void drawMe(float animTimeMod) {
		// TODO Auto-generated method stub

	}

	/////////////////////////////
	// end drawing routines
	
	/////////////////////////////
	// sim routines	
	@Override
	protected boolean simMe(float modAmtSec) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void stopMe() {
	}
	/////////////////////////////
	// end sim routines	

	@Override
	public void hndlFileLoad(File file, String[] vals, int[] stIdx) {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<String> hndlFileSave(File file) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected void resizeMe(float scale) {}
	@Override
	protected void endShiftKeyI() {}
	@Override
	protected void endAltKeyI() {}
	@Override
	protected void endCntlKeyI() {}

}//SOM_AnimWorldWin
