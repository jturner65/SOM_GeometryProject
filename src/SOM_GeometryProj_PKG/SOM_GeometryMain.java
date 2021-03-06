package SOM_GeometryProj_PKG;

import java.io.File;
import java.util.TreeMap;

import SOM_GeometryProj_PKG.geom_UI.Geom_2DLineAnimResWin;
import SOM_GeometryProj_PKG.geom_UI.Geom_3DLineAnimResWin;
import SOM_GeometryProj_PKG.geom_UI.Geom_PlaneAnimResWin;
import SOM_GeometryProj_PKG.geom_UI.Geom_SphereAnimResWin;
//import base_JavaProjTools_IRender.base_Render_Interface.IRenderInterface;
import base_Math_Objects.vectorObjs.doubles.myPoint;
import base_Math_Objects.vectorObjs.doubles.myVector;
import base_SOM_Objects.som_geom.geom_UI.SOM_AnimWorldWin;
import base_SOM_Objects.som_geom.geom_UI.SOM_GeomMapUIWin;
import base_UI_Objects.GUI_AppManager;
import base_UI_Objects.my_procApplet;
import base_UI_Objects.windowUI.base.myDispWindow;
import base_UI_Objects.windowUI.sidebar.mySideBarMenu;

/**
 * Experiment with self organizing maps in applications related to graphics and geometry
 * 
 * John Turner
 * 
 */
public class SOM_GeometryMain extends GUI_AppManager {
	//project-specific variables
	public String prjNmLong = "Building Animation Via SOM Interaction", prjNmShrt = "SOM_VisAnim";
	
	//protected int[] visFlags;
	private final int
		showUIMenu = 0,
		show2DLineAnimRes = 1,
		show3DLineAnimRes = 2,
		showPlaneAnimRes = 3,
		showSpereAnimRes = 4
		;
	public final int numVisFlags = 5;
	
	//idx's in dispWinFrames for each window - 0 is always left side menu window
	private static final int
		disp2DLineAnimResIDX = 1,
		disp3DLineAnimResIDX = 2,
		dispPlaneAnimResIDX = 3,
		dispSphereAnimResIDX = 4
		;
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
		SOM_GeometryMain me = new SOM_GeometryMain();
		my_procApplet._invokedMain(me, passedArgs);		    
	}//main	
	
	public SOM_GeometryMain() {super();}
	
	@Override
	protected void setSmoothing() {		pa.setSmoothing(4);		}

	/**
	 * whether or not we want to restrict window size on widescreen monitors
	 * 
	 * @return 0 - use monitor size regardless
	 * 			1 - use smaller dim to be determine window 
	 * 			2+ - TBD
	 */
	@Override
	protected int setAppWindowDimRestrictions() {	return 1;}	
	
	//instance-specific setup code
	@Override
	protected void setup_indiv() {		setBkgrnd();	}	
	@Override
	public void setBkgrnd(){((my_procApplet)pa).background(bground[0],bground[1],bground[2],bground[3]);}//setBkgrnd	
	/**
	 * determine which main flags to show at upper left of menu 
	 */
	@Override
	protected void initMainFlags_Indiv() {
		setMainFlagToShow_debugMode(false);
		setMainFlagToShow_saveAnim(true); 
		setMainFlagToShow_runSim(false);
		setMainFlagToShow_singleStep(false);
		setMainFlagToShow_showRtSideMenu(true);
	}
	
	@Override
	//build windows here
	protected void initVisOnce_Indiv() {
		//including strings for default directories specific to current project setup
		argsMap = new TreeMap<String,Object>();
		//provide default values used by program
		argsMap.put("configDir", "GeometryProject" + File.separator+"config" + File.separator);
		argsMap.put("dataDir", "GeometryProject" + File.separator);
		argsMap.put("logLevel",0);//0 is console alone,1 is log file alone, 2 is both

		
		showInfo = true;
		//drawnTrajEditWidth = 10;
		//includes 1 for menu window (never < 1) - always have same # of visFlags as myDispWindows
		int numWins = numVisFlags;		
		//titles and descs, need to be set before sidebar menu is defined
		String[] _winTitles = new String[]{"","2D Lines","3D Lines","3D Planes","3D Spheres"},//,"SOM Map UI"},
				_winDescr = new String[] {"","Display 2D Lines and Line sample points","Display 3D Lines and Line sample points","Display Planes and Plane surface samples","Display Spheres and Sphere surface samples"};//,"Visualize Sphere SOM Node location and color mapping"};
		initWins(numWins,_winTitles, _winDescr);
		//call for menu window
		buildInitMenuWin(showUIMenu);
		//menu bar init
		int wIdx = dispMenuIDX,fIdx=showUIMenu;
		dispWinFrames[wIdx] = buildSideBarMenu(wIdx, fIdx, new String[]{"Load/Save Geometry Data","Functions 2","Functions 3","Functions 4"}, new int[] {3,4,4,4}, 5, true, true);//new SOM_GeomSideBarMenu(this, winTitles[wIdx], fIdx, winFillClrs[wIdx], winStrkClrs[wIdx], winRectDimOpen[wIdx], winRectDimClose[wIdx], winDescr[wIdx]);	
		//instanced window dimensions when open and closed - only showing 1 open at a time
		float[] _dimOpen  =  new float[]{menuWidth, 0, pa.getWidth()-menuWidth, pa.getHeight()}, 
				_dimClosed  =  new float[]{menuWidth, 0, hideWinWidth, pa.getHeight()};	
		//setInitDispWinVals : use this to define the values of a display window
		//int _winIDX, 
		//float[] _dimOpen, float[] _dimClosed  : dimensions opened or closed
		//String _ttl, String _desc 			: window title and description
		//boolean[] _dispFlags 					: 
		//   flags controlling display of window :  idxs : 0 : canDrawInWin; 1 : canShow3dbox; 2 : canMoveView; 3 : dispWinIs3d
		//int[] _fill, int[] _strk, 			: window fill and stroke colors
		//int _trajFill, int _trajStrk)			: trajectory fill and stroke colors, if these objects can be drawn in window (used as alt color otherwise)
		//specify windows that cannot be shown simultaneously here
		initXORWins(new int[]{show2DLineAnimRes,show3DLineAnimRes,showPlaneAnimRes,showSpereAnimRes},new int[]{disp2DLineAnimResIDX,disp3DLineAnimResIDX, dispPlaneAnimResIDX,dispSphereAnimResIDX});

		wIdx = disp2DLineAnimResIDX; fIdx= show2DLineAnimRes;
		setInitDispWinVals(wIdx, _dimOpen, _dimClosed,new boolean[]{false,false,true,false}, new int[]{0,0,0,255},new int[]{255,255,255,255},new int[]{180,180,180,255},new int[]{100,100,100,255}); 
		dispWinFrames[wIdx] = new Geom_2DLineAnimResWin(pa, this, winTitles[wIdx], fIdx, winFillClrs[wIdx], winStrkClrs[wIdx], winRectDimOpen[wIdx], winRectDimClose[wIdx], winDescr[wIdx]);		
		
		wIdx = disp3DLineAnimResIDX; fIdx= show3DLineAnimRes;
		setInitDispWinVals(wIdx, _dimOpen, _dimClosed,new boolean[]{false,true,true,true}, new int[]{255,255,245,255},new int[]{0,0,0,255},new int[]{180,180,180,255},new int[]{100,100,100,255}); 
		dispWinFrames[wIdx] = new Geom_3DLineAnimResWin(pa, this, winTitles[wIdx], fIdx, winFillClrs[wIdx], winStrkClrs[wIdx], winRectDimOpen[wIdx], winRectDimClose[wIdx], winDescr[wIdx]);		
		
		wIdx = dispPlaneAnimResIDX; fIdx= showPlaneAnimRes;
		setInitDispWinVals(wIdx, _dimOpen, _dimClosed,new boolean[]{false,true,true,true}, new int[]{255,255,245,255},new int[]{0,0,0,255},new int[]{180,180,180,255},new int[]{100,100,100,255}); 
		dispWinFrames[wIdx] = new Geom_PlaneAnimResWin(pa, this, winTitles[wIdx], fIdx, winFillClrs[wIdx], winStrkClrs[wIdx], winRectDimOpen[wIdx], winRectDimClose[wIdx], winDescr[wIdx]);		

		wIdx = dispSphereAnimResIDX; fIdx= showSpereAnimRes;
		setInitDispWinVals(wIdx, _dimOpen, _dimClosed,new boolean[]{false,true,true,true}, new int[]{255,245,255,255},new int[]{0,0,0,255},new int[]{180,180,180,255},new int[]{100,100,100,255}); 
		dispWinFrames[wIdx] = new Geom_SphereAnimResWin(pa, this, winTitles[wIdx], fIdx, winFillClrs[wIdx], winStrkClrs[wIdx], winRectDimOpen[wIdx], winRectDimClose[wIdx], winDescr[wIdx]);		
		
		
		for(int i=1;i<dispWinFrames.length;++i) {
			curFocusWin = i;
			((SOM_AnimWorldWin)dispWinFrames[i]).setGeomMapUIWin(buildSOM_MapDispUIWin((SOM_AnimWorldWin)dispWinFrames[i],winTitles[i], -1));
		}
	}//	initVisOnce_Indiv
		
	private SOM_GeomMapUIWin buildSOM_MapDispUIWin(SOM_AnimWorldWin ownerWin, String owner, int fIdx) {
		float height = pa.getHeight();
		float width = pa.getWidth();
		float popUpWinHeight = PopUpWinOpenFraction * height;
		float[] _dimOpen  =  new float[]{menuWidth, popUpWinHeight, width-menuWidth, height-popUpWinHeight};
		//hidden
		float[] _dimClosed  =  new float[]{menuWidth, height-hidWinHeight, width-menuWidth, hidWinHeight};
		
		//(int _winIDX, float[] _dimOpen, float[] _dimClosed, boolean[] _dispFlags, int[] _fill, int[] _strk, int[] _trajFill, int[] _trajStrk)
		SOM_GeomMapUIWin resWin = new SOM_GeomMapUIWin(pa, this, "SOM Map UI for " + owner, fIdx, new int[]{20,40,50,200}, new int[]{255,255,255,255}, _dimOpen, _dimClosed, "Visualize SOM Node location for "+owner,argsMap,ownerWin);	
		resWin.finalInit(false,false, false, new myPoint(-gridDimX/2.0,-gridDimY/2.0,-gridDimZ/2.0), new myVector(0,0,0));
		resWin.setTrajColors(new int[]{180,180,180,255},new int[]{100,100,100,255});
		resWin.setRtSideUIBoxClrs(new int[]{0,0,0,200},new int[]{255,255,255,255});
		return resWin;
	}
	
	public SOM_AnimWorldWin getLinesWindow() {return (SOM_AnimWorldWin) dispWinFrames[disp2DLineAnimResIDX];}
	public SOM_AnimWorldWin getPlanesWindow() {return (SOM_AnimWorldWin) dispWinFrames[dispPlaneAnimResIDX];}
	public SOM_AnimWorldWin getSpheresWindow() {return (SOM_AnimWorldWin) dispWinFrames[dispSphereAnimResIDX];}
	
	@Override
	//called from base class, once at start of program after vis init is called - set initial windows to show - always show UI Menu
	protected void initOnce_Indiv(){
		//which objects to initially show
		setVisFlag(showUIMenu, true);					//show input UI menu	
		//setVisFlag(showSpereAnimRes, true);
		setVisFlag(show2DLineAnimRes, true);
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

	/**
	 * present an application-specific array of mouse over btn names 
	 * for the selection of the desired mouse over text display - if is length 0 or null, will not be displayed
	 */
	@Override
	public String[] getMouseOverSelBtnNames() {
		return SOM_AnimWorldWin.MseOvrLblsAra;
	}
	
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
			case 'S' : {break;}//{save(getScreenShotSaveName(prjNmShrt));break;}//save picture of current image			
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
			setMenuBtnState(mySideBarMenu.btnShowWinIdx,btn, val);
		} else {//called from clicking on buttons in UI
		
			//val is btn state before transition 
			boolean bVal = (val == 1?  false : true);
			//each entry in this array should correspond to a clickable window, not counting menu
			setVisFlag(btn+1, bVal);
		}
	}//handleShowWin
	
	@Override
	//get the ui rect values of the "master" ui region (another window) -> this is so ui objects of one window can be made, clicked, and shown displaced from those of the parent windwo
	public float[] getUIRectVals(int idx){
		//this.pr("In getUIRectVals for idx : " + idx);
		switch(idx){
		case dispMenuIDX 				: { return new float[0];}			//idx 0 is parent menu sidebar
		case disp2DLineAnimResIDX			: { return dispWinFrames[dispMenuIDX].uiClkCoords;}
		case disp3DLineAnimResIDX			: { return dispWinFrames[dispMenuIDX].uiClkCoords;}
		case dispPlaneAnimResIDX 		: { return dispWinFrames[dispMenuIDX].uiClkCoords;}
		case dispSphereAnimResIDX		: { return dispWinFrames[dispMenuIDX].uiClkCoords;}
		//case dispSOMMapIDX 				: {	return getMaxUIClkCoords();}
		default :  return dispWinFrames[dispMenuIDX].uiClkCoords;
		}
	}	
	
	@SuppressWarnings("unused")
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
	
	/**
	 * return the number of visible window flags for this application
	 * @return
	 */
	@Override
	public int getNumVisFlags() {return numVisFlags;}
	@Override
	//address all flag-setting here, so that if any special cases need to be addressed they can be
	protected void setVisFlag_Indiv(int idx, boolean val ){
		switch (idx){
			case showUIMenu 	    : { dispWinFrames[dispMenuIDX].setFlags(myDispWindow.showIDX,val);    break;}											//whether or not to show the main ui window (sidebar)			
			case show2DLineAnimRes	: {setDispAndModMapMgr(show2DLineAnimRes, disp2DLineAnimResIDX, val);break;}//{setWinFlagsXOR(dispLineAnimResIDX, val); break;}
			case show3DLineAnimRes	: {setDispAndModMapMgr(show3DLineAnimRes, disp3DLineAnimResIDX, val);break;}//{setWinFlagsXOR(dispLineAnimResIDX, val); break;}
			case showPlaneAnimRes	: {setDispAndModMapMgr(showPlaneAnimRes, dispPlaneAnimResIDX, val);break;}//{setWinFlagsXOR(dispPlaneAnimResIDX, val); break;}
			case showSpereAnimRes	: {setDispAndModMapMgr(showSpereAnimRes, dispSphereAnimResIDX, val);break;}//{setWinFlagsXOR(dispSphereAnimResIDX, val); break;}
//			case showSOMMapUI 		: {		//set active map manager based on currently displayed window
//				dispWinFrames[dispSOMMapIDX].setFlags(myDispWindow.showIDX,val); 
//				if(val) {
//					System.out.println("Sending window map mgr from setVisFlag : " + dispWinFrames[curFocusWin].name);
//					SOM_MapManager mapMgr = ((SOM_AnimWorldWin)dispWinFrames[curFocusWin]).getMapMgr();
//					if(null != mapMgr) {			((SOM_MapUIWin)dispWinFrames[dispSOMMapIDX]).setMapMgr(mapMgr);			}
//				}
//				setWinsHeight(dispSOMMapIDX); break;}
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
//		if(dispIDX != curFocusWin) {
//		//if(val) {//(val) && (getVisFlag(showSOMMapUI))) {
//			System.out.println("Sending window map mgr from setDispAndModMapMgr : " + dispWinFrames[curFocusWin].name);
//			SOM_MapManager mapMgr = ((SOM_AnimWorldWin)dispWinFrames[curFocusWin]).getMapMgr();
//			if(null != mapMgr) {			((SOM_MapUIWin)dispWinFrames[dispSOMMapIDX]).setMapMgr(mapMgr);			}		
//		//}
//		}
	}//setDispAndModMapMgr
	

	@Override
	/**
	 * Overriding main because handling 2d + 3d windows
	 */
	public final void drawMe(float modAmtMillis){
																//initialize camera, lights and scene orientation and set up eye movement		
		if((curFocusWin == -1) || (curDispWinIs3D())){	//allow for single window to have focus, but display multiple windows	
			//if refreshing screen, this clears screen, sets background		
			setBkgrnd();				
			draw3D_solve3D(modAmtMillis,-c.getViewDimW()/2.0f+40);
			if(curDispWinCanShow3dbox()){drawBoxBnds();}		
			
		} //else {	//either/or 2d window
		//2d windows paint window box so background is always cleared
		c.buildCanvas();
		c.drawMseEdge(dispWinFrames[curFocusWin]);
		pa.popMatState(); 
		for(int i =1; i<numDispWins; ++i){if (isShowingWindow(i) && !(dispWinFrames[i].getFlags(myDispWindow.is3DWin))){dispWinFrames[i].draw2D(modAmtMillis);}}
		//draw SOM window in current window if active/displayed
		pa.pushMatState();
		((SOM_AnimWorldWin) dispWinFrames[curFocusWin]).drawSOMWinUI(modAmtMillis);
		pa.popMatState();
	}//draw	

	@Override
	public int[] getClr_Custom(int colorVal, int alpha) {	return new int[] {255,255,255,alpha};}
//	/**
//	 * display the SOM window's UI objects
//	 */
//	public void drawSOMUIObjs() {
//		if(getVisFlag(showSOMMapUI)){
//			pushMatrix();pushStyle();			
//			dispWinFrames[dispSOMMapIDX].drawGUIObjs();					//draw what user-modifiable fields are currently available
//			dispWinFrames[dispSOMMapIDX].drawClickableBooleans();					//draw what user-modifiable fields are currently available
//			//dispWinFrames[curFocusWin].drawCustMenuObjs();					//customizable menu objects for each window
//			popStyle();	popMatrix();						
//		}
//	}


	
}//class SOM_GeometryMain
