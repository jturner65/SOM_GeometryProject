package SOM_GeometryProj_PKG;

import processing.core.*;

import java.io.File;
import java.util.TreeMap;

import SOM_GeometryProj_PKG.geom_UI.Geom_LineAnimResWin;
import SOM_GeometryProj_PKG.geom_UI.Geom_PlaneAnimResWin;
import SOM_GeometryProj_PKG.geom_UI.Geom_SphereAnimResWin;
import SOM_GeometryProj_PKG.geom_UI.Geom_SideBarMenu;
import SOM_GeometryProj_PKG.som_geom.geom_UI.SOM_AnimWorldWin;
import SOM_GeometryProj_PKG.som_geom.geom_UI.SOM_GeomMapUIWin;
import base_SOM_Objects.SOM_MapManager;
import base_SOM_Objects.som_ui.win_disp_ui.SOM_MapUIWin;
import base_UI_Objects.*;
import base_UI_Objects.windowUI.myDispWindow;
/**
 * Experiment with self organizing maps in applications related to graphics and geometry
 * 
 * John Turner
 * 
 */
public class SOM_GeometryMain extends my_procApplet {
	//project-specific variables
	public String prjNmLong = "Building Animation Via SOM Interaction", prjNmShrt = "SOM_VisAnim";
	
	private int[] visFlags;
	private final int
		showUIMenu = 0,
		showSpereAnimRes = 1,
		showLineAnimRes = 2,
		showPlaneAnimRes = 3,
		showSOMMapUI = 4;
	public final int numVisFlags = 5;
	
	//idx's in dispWinFrames for each window - 0 is always left side menu window
	private static final int
		dispSphereAnimResIDX = 1,	
		dispLineAnimResIDX = 2,
		dispPlaneAnimResIDX = 3,
		dispSOMMapIDX = 4;
																		//set array of vector values (sceneFcsVals) based on application
	//private boolean cyclModCmp;										//comparison every draw of cycleModDraw			
	private final int[] bground = new int[]{244,244,244,255};		//bground color	
	
	/**
	 * fraction of height popup som win should use
	 */
	public final float PopUpWinOpenFraction = .20f;
	
	/**
	 * default args for SOM Map UI Window (passed to SOM Project config)
	 */
	public TreeMap<String, Object> argsMap;
///////////////
//CODE STARTS
///////////////	
	//////////////////////////////////////////////// code
	
	//needs main to run project - do not modify this code in any way
	public static void main(String[] passedArgs) {		
		String[] appletArgs = new String[] { "SOM_GeometryProj_PKG.SOM_GeometryMain" };
	    if (passedArgs != null) {PApplet.main(PApplet.concat(appletArgs, passedArgs)); } else {PApplet.main(appletArgs);		    }
	}//main	

	@Override
	protected int[] getDesiredAppDims() {return new int[] {(int)(getDisplayWidth()*.95f), (int)(getDisplayHeight()*.92f)};}
	//instance-specific setup code
	protected void setup_indiv() {
		
		setBkgrnd();
	}	
	@Override
	public void setBkgrnd(){background(bground[0],bground[1],bground[2],bground[3]);}//setBkgrnd	
	/**
	 * determine which main flags to show at upper left of menu 
	 */
	@Override
	protected void initMainFlags_Priv() {
		setMainFlagToShow_debugMode(false);
		setMainFlagToShow_saveAnim(true); 
		setMainFlagToShow_runSim(false);
		setMainFlagToShow_singleStep(false);
		setMainFlagToShow_showRtSideMenu(true);
	}
	
	@Override
	//build windows here
	protected void initVisOnce_Priv() {
		//including strings for default directories specific to current project setup
		argsMap = new TreeMap<String,Object>();
		//provide default values used by program
		argsMap.put("configDir", "GeometryProject" + File.separator+"config" + File.separator);
		argsMap.put("dataDir", "GeometryProject" + File.separator);
		argsMap.put("logLevel",0);//0 is console alone,1 is log file alone, 2 is both

		
		showInfo = true;
		drawnTrajEditWidth = 10;
		//includes 1 for menu window (never < 1) - always have same # of visFlags as myDispWindows
		int numWins = numVisFlags;		
		//titles and descs, need to be set before sidebar menu is defined
		String[] _winTitles = new String[]{"","Sphere World","Lines World","Planes World","SOM Map UI"},
				_winDescr = new String[] {"", "Display Spheres and Sphere surface samples","Display Lines and sample points","Display Planes and plan surface samples","Visualize Sphere SOM Node location and color mapping"};
		initWins(numWins,_winTitles, _winDescr);
		//call for menu window
		buildInitMenuWin(showUIMenu);
		//menu bar init
		int wIdx = dispMenuIDX,fIdx=showUIMenu;
		dispWinFrames[wIdx] = new Geom_SideBarMenu(this, winTitles[wIdx], fIdx, winFillClrs[wIdx], winStrkClrs[wIdx], winRectDimOpen[wIdx], winRectDimClose[wIdx], winDescr[wIdx],dispWinFlags[wIdx][dispCanDrawInWinIDX]);	
		//instanced window dimensions when open and closed - only showing 1 open at a time
		float[] _dimOpen  =  new float[]{menuWidth, 0, width-menuWidth, height}, _dimClosed  =  new float[]{menuWidth, 0, hideWinWidth, height};	
		//setInitDispWinVals : use this to define the values of a display window
		//int _winIDX, 
		//float[] _dimOpen, float[] _dimClosed  : dimensions opened or closed
		//String _ttl, String _desc 			: window title and description
		//boolean[] _dispFlags 					: 
		//   flags controlling display of window :  idxs : 0 : canDrawInWin; 1 : canShow3dbox; 2 : canMoveView; 3 : dispWinIs3d
		//int[] _fill, int[] _strk, 			: window fill and stroke colors
		//int _trajFill, int _trajStrk)			: trajectory fill and stroke colors, if these objects can be drawn in window (used as alt color otherwise)
		//specify windows that cannot be shown simultaneously here
		initXORWins(new int[]{showSpereAnimRes,showLineAnimRes,showPlaneAnimRes},new int[]{dispSphereAnimResIDX,dispLineAnimResIDX, dispPlaneAnimResIDX});

		wIdx = dispSphereAnimResIDX; fIdx= showSpereAnimRes;
		setInitDispWinVals(wIdx, _dimOpen, _dimClosed,new boolean[]{false,true,true,true}, new int[]{255,245,255,255},new int[]{0,0,0,255},new int[]{180,180,180,255},new int[]{100,100,100,255}); 
		dispWinFrames[wIdx] = new Geom_SphereAnimResWin(this, winTitles[wIdx], fIdx, winFillClrs[wIdx], winStrkClrs[wIdx], winRectDimOpen[wIdx], winRectDimClose[wIdx], winDescr[wIdx],dispWinFlags[wIdx][dispCanDrawInWinIDX]);		
		
		wIdx = dispLineAnimResIDX; fIdx= showLineAnimRes;
		setInitDispWinVals(wIdx, _dimOpen, _dimClosed,new boolean[]{false,false,true,false}, new int[]{0,0,0,255},new int[]{0,0,0,255},new int[]{180,180,180,255},new int[]{100,100,100,255}); 
		dispWinFrames[wIdx] = new Geom_LineAnimResWin(this, winTitles[wIdx], fIdx, winFillClrs[wIdx], winStrkClrs[wIdx], winRectDimOpen[wIdx], winRectDimClose[wIdx], winDescr[wIdx],dispWinFlags[wIdx][dispCanDrawInWinIDX]);		
		
		wIdx = dispPlaneAnimResIDX; fIdx= showPlaneAnimRes;
		setInitDispWinVals(wIdx, _dimOpen, _dimClosed,new boolean[]{false,true,true,true}, new int[]{255,255,245,255},new int[]{0,0,0,255},new int[]{180,180,180,255},new int[]{100,100,100,255}); 
		dispWinFrames[wIdx] = new Geom_PlaneAnimResWin(this, winTitles[wIdx], fIdx, winFillClrs[wIdx], winStrkClrs[wIdx], winRectDimOpen[wIdx], winRectDimClose[wIdx], winDescr[wIdx],dispWinFlags[wIdx][dispCanDrawInWinIDX]);		
		
//		wIdx = dispSOMMapIDX; fIdx=showSOMMapUI;
//		setInitDispWinVals(wIdx, _dimOpen, _dimClosed,new boolean[]{false,false,false,false}, new int[]{50,40,20,255}, new int[]{255,255,255,255},new int[]{180,180,180,255},new int[]{100,100,100,255});
//		dispWinFrames[wIdx] = new mySOMMapUIWin(this, winTitles[wIdx], fIdx, winFillClrs[wIdx], winStrkClrs[wIdx], winRectDimOpen[wIdx], winRectDimClose[wIdx], winDescr[wIdx],dispWinFlags[wIdx][dispCanDrawInWinIDX]);		
//		
//		//specify windows that cannot be shown simultaneously here
//		initXORWins(new int[]{showSpereAnimRes,showLineAnimRes,showPlaneAnimRes,showSOMMapUI},new int[]{dispSphereAnimResIDX,dispLineAnimResIDX, dispPlaneAnimResIDX,dispSOMMapIDX});
		float popUpWinHeight = PopUpWinOpenFraction * height;
		_dimOpen  =  new float[]{menuWidth, popUpWinHeight, width-menuWidth, height-popUpWinHeight};
		//hidden
		_dimClosed  =  new float[]{menuWidth, height-hidWinHeight, width-menuWidth, hidWinHeight};
		
		wIdx = dispSOMMapIDX; fIdx=showSOMMapUI;
		setInitDispWinVals(wIdx, _dimOpen, _dimClosed,new boolean[]{false,false,false,false}, new int[]{50,40,20,255}, new int[]{255,255,255,255},new int[]{180,180,180,255},new int[]{100,100,100,255});
		dispWinFrames[wIdx] = new SOM_GeomMapUIWin(this, winTitles[wIdx], fIdx, winFillClrs[wIdx], winStrkClrs[wIdx], winRectDimOpen[wIdx], winRectDimClose[wIdx], winDescr[wIdx],dispWinFlags[wIdx][dispCanDrawInWinIDX],argsMap);		
		
	}//	initVisOnce_Priv
	
	@Override
	//called from base class, once at start of program after vis init is called - set initial windows to show - always show UI Menu
	protected void initOnce_Priv(){
		//which objects to initially show
		setVisFlag(showUIMenu, true);					//show input UI menu	
		//setVisFlag(showSpereAnimRes, true);
		setVisFlag(showLineAnimRes, true);
	}//	initOnce
	
	@Override
	//called multiple times, whenever re-initing
	protected void initProgram_Indiv(){	}//initProgram	
	@Override
	protected void initVisProg_Indiv() {}		


	@Override
	protected String getPrjNmLong() {return prjNmLong;}

	@Override
	protected String getPrjNmShrt() {		return prjNmShrt;	}

	
	//////////////////////////////////////////////////////
	/// user interaction
	//////////////////////////////////////////////////////	
	//key is key pressed
	//keycode is actual physical key pressed == key if shift/alt/cntl not pressed.,so shift-1 gives key 33 ('!') but keycode 49 ('1')

	@Override
	protected void handleKeyPress(char key, int keyCode) {
		switch (key){
			case ' ' : {toggleSimIsRunning(); break;}							//run sim
			case 'f' : {dispWinFrames[curFocusWin].setInitCamView();break;}					//reset camera
			case 'a' :
			case 'A' : {toggleSaveAnim();break;}						//start/stop saving every frame for making into animation
			case 's' :
			case 'S' : {save(getScreenShotSaveName(prjNmShrt));break;}//save picture of current image			
			default : {	}
		}//switch	
	}

	@Override
	//gives multiplier based on whether shift, alt or cntl (or any combo) is pressed
	public double clickValModMult(){return ((altIsPressed() ? .1 : 1.0) * (shiftIsPressed() ? 10.0 : 1.0));}	
	//keys/criteria are present that means UI objects are modified by set values based on clicks (as opposed to dragging for variable values)
	//to facilitate UI interaction non-mouse computers, set these to be single keys
	@Override
	public boolean isClickModUIVal() {
		//TODO change this to manage other key settings for situations where multiple simultaneous key presses are not optimal or conventient
		return altIsPressed() || shiftIsPressed();		
	}
	
	@Override
	//these tie using the UI buttons to modify the window in with using the boolean tags - PITA but currently necessary
	public void handleShowWin(int btn, int val, boolean callFlags){//display specific windows - multi-select/ always on if sel
		if(!callFlags){//called from setflags - only sets button state in UI to avoid infinite loop
			setMenuBtnState(Geom_SideBarMenu.btnShowWinIdx,btn, val);
		} else {//called from clicking on buttons in UI
		
			//val is btn state before transition 
			boolean bVal = (val == 1?  false : true);
			//each entry in this array should correspond to a clickable window, not counting menu
			setVisFlag(btn+showSpereAnimRes, bVal);
		}
	}//handleShowWin
	
	@Override
	//get the ui rect values of the "master" ui region (another window) -> this is so ui objects of one window can be made, clicked, and shown displaced from those of the parent windwo
	public float[] getUIRectVals(int idx){
		//this.pr("In getUIRectVals for idx : " + idx);
		switch(idx){
		case dispMenuIDX 				: { return new float[0];}			//idx 0 is parent menu sidebar
		case dispSphereAnimResIDX		: { return dispWinFrames[dispMenuIDX].uiClkCoords;}
		case dispLineAnimResIDX			: { return dispWinFrames[dispMenuIDX].uiClkCoords;}
		case dispPlaneAnimResIDX 		: { return dispWinFrames[dispMenuIDX].uiClkCoords;}
		case dispSOMMapIDX 				: {	return getMaxUIClkCoords();}
		default :  return dispWinFrames[dispMenuIDX].uiClkCoords;
		}
	}	
	
	private float[] getMaxUIClkCoords() {
		float[] res = new float[] {0.0f,0.0f,0.0f,0.0f}, tmpCoords;
		for (int winIDX : winDispIdxXOR) {
			tmpCoords = dispWinFrames[winIDX].uiClkCoords;
			for(int i=0;i<tmpCoords.length;++i) {
				if(res[i]<tmpCoords[i]) {res[i]=tmpCoords[i];}
			}
		}
		return res;
	}
	
	//////////////////////////////////////////
	/// graphics and base functionality utilities and variables
	//////////////////////////////////////////
	@Override
		//init boolean state machine flags for program
	public void initVisFlags(){
		visFlags = new int[1 + numVisFlags/32];for(int i =0; i<numVisFlags;++i){forceVisFlag(i,false);}	
		((Geom_SideBarMenu)dispWinFrames[dispMenuIDX]).initPFlagColors();			//init sidebar window flags
	}		
	@Override
	//address all flag-setting here, so that if any special cases need to be addressed they can be
	public void setVisFlag(int idx, boolean val ){
		int flIDX = idx/32, mask = 1<<(idx%32);
		visFlags[flIDX] = (val ?  visFlags[flIDX] | mask : visFlags[flIDX] & ~mask);
		switch (idx){
			case showUIMenu 	    : { dispWinFrames[dispMenuIDX].setFlags(myDispWindow.showIDX,val);    break;}											//whether or not to show the main ui window (sidebar)			
			case showSpereAnimRes	: {setDispAndModMapMgr(showSpereAnimRes, dispSphereAnimResIDX, val);break;}//{setWinFlagsXOR(dispSphereAnimResIDX, val); break;}
			case showLineAnimRes	: {setDispAndModMapMgr(showLineAnimRes, dispLineAnimResIDX, val);break;}//{setWinFlagsXOR(dispLineAnimResIDX, val); break;}
			case showPlaneAnimRes	: {setDispAndModMapMgr(showPlaneAnimRes, dispPlaneAnimResIDX, val);break;}//{setWinFlagsXOR(dispPlaneAnimResIDX, val); break;}
			case showSOMMapUI 		: {		//set active map manager based on currently displayed window
				dispWinFrames[dispSOMMapIDX].setFlags(myDispWindow.showIDX,val); 
				SOM_MapManager mapMgr = ((SOM_AnimWorldWin)dispWinFrames[curFocusWin]).getMapMgr();
				if(null != mapMgr) {			((SOM_MapUIWin)dispWinFrames[dispSOMMapIDX]).setMapMgr(mapMgr);			}
				setWinsHeight(dispSOMMapIDX); break;}
			default : {break;}
		}
	}//setFlags  
	/**
	 * send appropriate map manager to map manager window
	 * @param flagIDX
	 * @param dispIDX
	 * @param val
	 */
	private void setDispAndModMapMgr(int flagIDX, int dispIDX, boolean val) {
		setWinFlagsXOR(dispIDX, val);
		if((val) && (getVisFlag(showSOMMapUI))) {
			SOM_MapManager mapMgr = ((SOM_AnimWorldWin)dispWinFrames[dispIDX]).getMapMgr();
			if(null != mapMgr) {			((SOM_MapUIWin)dispWinFrames[dispSOMMapIDX]).setMapMgr(mapMgr);			}		
		}
	}//setDispAndModMapMgr
	
	
	@Override
	//get vis flag
	public boolean getVisFlag(int idx){int bitLoc = 1<<(idx%32);return (visFlags[idx/32] & bitLoc) == bitLoc;}	
	@Override
	public void forceVisFlag(int idx, boolean val) {
		int flIDX = idx/32, mask = 1<<(idx%32);
		visFlags[flIDX] = (val ?  visFlags[flIDX] | mask : visFlags[flIDX] & ~mask);
		//doesn't perform any other ops - to prevent looping
	}
	@Override
	/**
	 * overridding main because handling 2d + 3d windows
	 */
	public void draw(){
		if(!isFinalInitDone()) {initOnce(); return;}	
		float modAmtMillis = getModAmtMillis();
		//simulation section
		if(isRunSim() ){
			//run simulation
			drawCount++;									//needed here to stop draw update so that pausing sim retains animation positions	
			for(int i =1; i<numDispWins; ++i){if((isShowingWindow(i)) && (dispWinFrames[i].getFlags(myDispWindow.isRunnable))){dispWinFrames[i].simulate(modAmtMillis);}}
			if(isSingleStep()){setSimIsRunning(false);}
			simCycles++;
		}		//play in current window

		//drawing section
		pushMatrix();pushStyle();
		drawSetup();																//initialize camera, lights and scene orientation and set up eye movement		
		if((curFocusWin == -1) || (curDispWinIs3D())){	//allow for single window to have focus, but display multiple windows	
			//if refreshing screen, this clears screen, sets background		
			setBkgrnd();				
			draw3D_solve3D(modAmtMillis);
			if(curDispWinCanShow3dbox()){drawBoxBnds();}		
			
		} //else {	//either/or 2d window
		//2d windows paint window box so background is always cleared
		c.buildCanvas();
		c.drawMseEdge();
		popStyle();popMatrix(); 
		for(int i =1; i<numDispWins; ++i){if (isShowingWindow(i) && !(dispWinFrames[i].getFlags(myDispWindow.is3DWin))){dispWinFrames[i].draw2D(modAmtMillis);}}
		//}
		drawUI(modAmtMillis);																	//draw UI overlay on top of rendered results			
		if (doSaveAnim()) {	savePic();}
		updateConsoleStrs();
		surface.setTitle(getPrjNmLong() + " : " + (int)(frameRate) + " fps|cyc curFocusWin : " + curFocusWin);
	}//draw	

	@Override
	protected int[] getClr_Custom(int colorVal, int alpha) {	return new int[] {255,255,255,alpha};}
	/**
	 * display the SOM window's UI objects
	 */
	public void drawSOMUIObjs() {
		if(getVisFlag(showSOMMapUI)){
			pushMatrix();pushStyle();			
			dispWinFrames[dispSOMMapIDX].drawGUIObjs();					//draw what user-modifiable fields are currently available
			dispWinFrames[dispSOMMapIDX].drawClickableBooleans();					//draw what user-modifiable fields are currently available
			//dispWinFrames[curFocusWin].drawCustMenuObjs();					//customizable menu objects for each window
			popStyle();	popMatrix();						
		}
	}
	
}//class SOM_GeometryMain
