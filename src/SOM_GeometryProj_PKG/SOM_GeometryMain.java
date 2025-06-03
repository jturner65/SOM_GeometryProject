package SOM_GeometryProj_PKG;

import java.io.File;
import java.util.HashMap;

import SOM_GeometryProj_PKG.geom_UI.*;
import base_SOM_Objects.som_geom.geom_UI.SOM_AnimWorldWin;
import base_UI_Objects.GUI_AppManager;
import base_Utils_Objects.io.messaging.MsgCodes;

/**
 * Experiment with self organizing maps in applications related to graphics and geometry
 * 
 * John Turner
 * 
 */
public class SOM_GeometryMain extends GUI_AppManager {
	//project-specific variables
	public final String prjNmShrt = "SOM_VisAnim",
			prjNmLong = "Reconstructing Meshes Via SOM Interaction", 
			projDesc = "Reconstruct 2d lines, 3d lines, planes or spheres given surface point clouds using SOM clustering.";

	
	//idx's in dispWinFrames for each window - 0 is always left side menu window
	private static final int
		disp3DPointAnimResIDX = 1,
		disp2DLineAnimResIDX = 2,
		disp3DLineAnimResIDX = 3,
		dispPlaneAnimResIDX = 4,
		dispSphereAnimResIDX = 5
		;
	/**
	 * # of visible windows including side menu (always at least 1 for side menu)
	 */
	private static final int numVisWins = 6;

	
	//set array of vector values (sceneFcsVals) based on application		
	private final int[] bground = new int[]{244,244,244,255};		//bground color	
	
	/**
	 * Labels for buttons that describe what mouse-over on the SOM displays
	 */
	public static final String[] MseOvrLblsAra = new String[] { "Loc", "Dist", "Pop", "Ftr", "Class", "Cat", "None" };
	
///////////////
//CODE STARTS
///////////////	
	//////////////////////////////////////////////// code
	
	//needs main to run project - do not modify this code in any way
	public static void main(String[] passedArgs) {		
		SOM_GeometryMain me = new SOM_GeometryMain();
		SOM_GeometryMain.invokeProcessingMain(me, passedArgs);		    
	}//main	
	
	public SOM_GeometryMain() {super();}

	@Override
	protected boolean showMachineData() {return true;}	
	/**
	 * Set various relevant runtime arguments in argsMap
	 * @param _passedArgs command-line arguments
	 */
	@Override
	protected HashMap<String,Object> setRuntimeArgsVals(HashMap<String, Object> _passedArgsMap) {
		//ignore passed map, build new map
		HashMap<String, Object> argsMap = new HashMap<String, Object>();
		//provide default values used by SOM program
		argsMap.put("configDir", "GeometryProject" + File.separator+"config" + File.separator);
		argsMap.put("dataDir", "GeometryProject" + File.separator);
		argsMap.put("logLevel",2);//0 is console alone,1 is log file alone, 2 is both
		return argsMap;
	}
	
	/**
	 * Called in pre-draw initial setup, before first init
	 * potentially override setup variables on per-project basis.
	 * Do not use for setting background color or Skybox anymore.
	 *  	(Current settings in ProcessingRenderer) 	
	 *  	strokeCap(PROJECT);
	 *  	textSize(txtSz);
	 *  	textureMode(NORMAL);			
	 *  	rectMode(CORNER);	
	 *  	sphereDetail(4);	 * 
	 */
	@Override
	protected void setupAppDims_Indiv() {}
	@Override
	protected boolean getUseSkyboxBKGnd(int winIdx) {	return false;}
	@Override
	protected String getSkyboxFilename(int winIdx) {	return "";}
	@Override
	protected int[] getBackgroundColor(int winIdx) {return bground;}
	@Override
	protected int getNumDispWindows() {	return numVisWins;	}	
	
	@Override
	public void setSmoothing() {		ri.setSmoothing(4);		}

	/**
	 * whether or not we want to restrict window size on widescreen monitors
	 * 
	 * @return 0 - use monitor size regardless
	 * 			1 - set window size to meet reasonable disp ratio 
	 * 			2+ - TBD
	 */
	@Override
	protected int setAppWindowDimRestrictions() {	return 1;}	

	@Override
	public String getPrjNmShrt() {return prjNmShrt;}
	@Override
	public String getPrjNmLong() {return prjNmLong;}
	@Override
	public String getPrjDescr() {return projDesc;}

	/**
	 * Set minimum level of message object console messages to display for this application. If null then all messages displayed
	 * @return
	 */
	@Override
	protected final MsgCodes getMinConsoleMsgCodes() {return null;}
	/**
	 * Set minimum level of message object log messages to save to log for this application. If null then all messages saved to log.
	 * @return
	 */
	@Override
	protected final MsgCodes getMinLogMsgCodes() {return null;}

	/**
	 * determine which main flags to show at upper left of menu 
	 */
	@Override
	protected void initBaseFlags_Indiv() {
		setBaseFlagToShow_debugMode(true);
		setBaseFlagToShow_saveAnim(true); 
		setBaseFlagToShow_runSim(false);
		setBaseFlagToShow_singleStep(false);
		setBaseFlagToShow_showRtSideMenu(true);	
		setBaseFlagToShow_showStatusBar(true);
		setBaseFlagToShow_showDrawableCanvas(false);
	}
	
	@Override
	//build windows here
	protected void initAllDispWindows() {
		showInfo = true;	
		//titles and descs, need to be set before sidebar menu is defined
		String[] _winTitles = new String[]{"","3D Point Cloud","2D Lines","3D Lines","3D Planes","3D Spheres"},//,"SOM Map UI"},
				_winDescr = new String[] {"","Display 3D Point Cloud","Display 2D Lines and Line sample points","Display 3D Lines and Line sample points","Display Planes and Plane surface samples","Display Spheres and Sphere surface samples"};//,"Visualize Sphere SOM Node location and color mapping"};

		//instanced window dims when open and closed - only showing 1 open at a time - and init cam vals
		float[][] _floatDims  = getDefaultWinAndCameraDims();	

		//Builds sidebar menu button config - application-wide menu button bar titles and button names
		String[] menuBtnTitles = new String[]{"Load/Save Geometry Data","Training Data","SOM Building/Display","Functions 4"};
		String[][] menuBtnNames = new String[][] { // each must have literals for every button defined in side bar menu, or ignored
			{ "Load Geometry Data", "Save Geometry Data", "---" }, 				// row 1
			{ "Calc Opt # Ex.", "Build Training Data", "Save Train Data" },		// row 2
			{ "Show SOM Win", "LD SOM Config", "Build Map", "---" }, 			// row 3
			{ "---", "---", "---", "---" }};
		String[] dbgBtnNames = new String[] {"Debug 0","Debug 1","Debug 2","Debug 3","Debug 4"};
		buildSideBarMenu(_winTitles, menuBtnTitles, menuBtnNames, dbgBtnNames, true, true);

		//specify windows that cannot be shown simultaneously here
		initXORWins(
				new int[]{disp3DPointAnimResIDX,disp2DLineAnimResIDX,disp3DLineAnimResIDX, dispPlaneAnimResIDX,dispSphereAnimResIDX},
				new int[]{disp3DPointAnimResIDX,disp2DLineAnimResIDX,disp3DLineAnimResIDX, dispPlaneAnimResIDX,dispSphereAnimResIDX});
		
		
		//define windows
		/**
		 *  _winIdx The index in the various window-descriptor arrays for the dispWindow being set
		 *  _title string title of this window
		 *  _descr string description of this window
		 *  _dispFlags Essential flags describing the nature of the dispWindow for idxs : 
		 * 		0 : dispWinIs3d, 
		 * 		1 : canDrawInWin; 
		 * 		2 : canShow3dbox (only supported for 3D); 
		 * 		3 : canMoveView
		 *  _floatVals an array holding float arrays for 
		 * 				rectDimOpen(idx 0),
		 * 				rectDimClosed(idx 1),
		 * 				initCameraVals(idx 2)
		 *  _intClrVals and array holding int arrays for
		 * 				winFillClr (idx 0),
		 * 				winStrkClr (idx 1),
		 * 				winTrajFillClr(idx 2),
		 * 				winTrajStrkClr(idx 3),
		 * 				rtSideFillClr(idx 4),
		 * 				rtSideStrkClr(idx 5)
		 *  _sceneCenterVal center of scene, for drawing objects (optional)
		 *  _initSceneFocusVal initial focus target for camera (optional)
		 */
		
		//Initialize all SOM anim worlds
		int wIdx = disp3DPointAnimResIDX;
		setInitDispWinVals(wIdx, _winTitles[wIdx], _winDescr[wIdx], getDfltBoolAra(true), _floatDims,		
				new int[][] {new int[]{255,255,245,255},new int[]{0,0,0,255},
					new int[]{180,180,180,255},new int[]{100,100,100,255},
					new int[]{0,0,0,200},new int[]{255,255,255,255}});
		setDispWindow(wIdx, new Geom_3DPointAnimResWin(ri, this, wIdx));		
		
		
		wIdx = disp2DLineAnimResIDX;
		setInitDispWinVals(wIdx, _winTitles[wIdx], _winDescr[wIdx], getDfltBoolAra(false), _floatDims,
				new int[][] {new int[]{0,0,0,255}, new int[]{255,255,255,255},
					new int[]{180,180,180,255}, new int[]{100,100,100,255},
					new int[]{0,0,0,200},new int[]{255,255,255,255}});
		setDispWindow(wIdx, new Geom_2DLineAnimResWin(ri, this, wIdx));		
		
		wIdx = disp3DLineAnimResIDX;
		//setInitDispWinVals(wIdx, _dimOpen, _dimClosed,new boolean[]{false,true,true,true}, new int[]{255,255,245,255},new int[]{0,0,0,255},new int[]{180,180,180,255},new int[]{100,100,100,255}); 
		setInitDispWinVals(wIdx, _winTitles[wIdx], _winDescr[wIdx], getDfltBoolAra(true), _floatDims,		
				new int[][] {new int[]{255,255,245,255},new int[]{0,0,0,255},
					new int[]{180,180,180,255},new int[]{100,100,100,255},
					new int[]{0,0,0,200},new int[]{255,255,255,255}});
		setDispWindow(wIdx, new Geom_3DLineAnimResWin(ri, this, wIdx));		
		
		wIdx = dispPlaneAnimResIDX;
		//setInitDispWinVals(wIdx, _dimOpen, _dimClosed,new boolean[]{false,true,true,true}, new int[]{255,255,245,255},new int[]{0,0,0,255},new int[]{180,180,180,255},new int[]{100,100,100,255}); 
		setInitDispWinVals(wIdx, _winTitles[wIdx], _winDescr[wIdx], getDfltBoolAra(true), _floatDims,		
				new int[][] {new int[]{245,255,255,255},new int[]{0,0,0,255},
					new int[]{180,180,180,255},new int[]{100,100,100,255},
					new int[]{0,0,0,200},new int[]{255,255,255,255}});
		setDispWindow(wIdx, new Geom_PlaneAnimResWin(ri, this, wIdx));		

		wIdx = dispSphereAnimResIDX;
		//setInitDispWinVals(wIdx, _dimOpen, _dimClosed,new boolean[]{false,true,true,true}, new int[]{255,245,255,255},new int[]{0,0,0,255},new int[]{180,180,180,255},new int[]{100,100,100,255}); 
		setInitDispWinVals(wIdx, _winTitles[wIdx], _winDescr[wIdx], getDfltBoolAra(true), _floatDims,		
				new int[][] {new int[]{255,245,255,255},new int[]{0,0,0,255},
					new int[]{180,180,180,255},new int[]{100,100,100,255},
					new int[]{0,0,0,200},new int[]{255,255,255,255}});
		setDispWindow(wIdx, new Geom_SphereAnimResWin(ri, this, wIdx));		
		
		//build SOM sub-windows for each anim res window
		for(int i=1;i<numDispWins;++i) {
			setCurFocusWin(i);
			((SOM_AnimWorldWin)getDispWindow(i)).buildAndSetSOM_MapDispUIWin( -1);
		}
	}//	initAllDispWindows
	
	public SOM_AnimWorldWin getLinesWindow() {return (SOM_AnimWorldWin) getDispWindow(disp2DLineAnimResIDX);}
	public SOM_AnimWorldWin get3DLinesWindow() {return (SOM_AnimWorldWin) getDispWindow(disp3DLineAnimResIDX);}
	public SOM_AnimWorldWin getPlanesWindow() {return (SOM_AnimWorldWin) getDispWindow(dispPlaneAnimResIDX);}
	public SOM_AnimWorldWin getSpheresWindow() {return (SOM_AnimWorldWin) getDispWindow(dispSphereAnimResIDX);}
	
	@Override
	//called from base class, once at start of program after vis init is called - set initial windows to show - always show UI Menu
	protected void initOnce_Indiv(){
		setWinVisFlag(disp2DLineAnimResIDX, true);
		setShowStatusBar(true);
	}//	initOnce
	
	@Override
	//called multiple times, whenever re-initing
	protected void initProgram_Indiv(){	}//initProgram	

	/**
	 * present an application-specific array of mouse over btn names 
	 * for the selection of the desired mouse over text display - if is length 0 or null, will not be displayed
	 */
	@Override
	public String[] getMouseOverSelBtnLabels() {
		return MseOvrLblsAra;
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
			case 'f' : {getCurFocusDispWindow().setInitCamView();break;}					//reset camera
			case 'a' :
			case 'A' : {toggleSaveAnim();break;}						//start/stop saving every frame for making into animation
			case 's' :
			case 'S' : {break;}//{save(getScreenShotSaveName(prjNmShrt));break;}//save picture of current image			
			default : {	}
		}//switch	
	}

	//keys/criteria are present that means UI objects are modified by set values based on clicks (as opposed to dragging for variable values)
	//to facilitate UI interaction non-mouse computers, set these to be single keys
	@Override
	public boolean isClickModUIVal() {
		//TODO change this to manage other key settings for situations where multiple simultaneous key presses are not optimal or conventient
		return altIsPressed() || shiftIsPressed();		
	}
	
	@Override
	//get the ui rect values of the "master" ui region (another window) -> this is so ui objects of one window can be
	//made, clicked, and shown displaced from those of the parent window
	public float[] getUIRectVals_Indiv(int idx, float[] menuClickDim){
		switch(idx){
		case disp3DPointAnimResIDX 		: { return menuClickDim;}
		case disp2DLineAnimResIDX		: { return menuClickDim;}
		case disp3DLineAnimResIDX		: { return menuClickDim;}
		case dispPlaneAnimResIDX 		: { return menuClickDim;}
		case dispSphereAnimResIDX		: { return menuClickDim;}
		//case dispSOMMapIDX 				: {	return getMaxUIClkCoords();}
		default :  return menuClickDim;
		}
	}	
	
	//////////////////////////////////////////
	/// graphics and base functionality utilities and variables
	//////////////////////////////////////////
	
	/**
	 * return the number of visible window flags for this application
	 * @return
	 */
	@Override
	public int getNumVisFlags() {return numVisWins;}
	@Override
	//address all flag-setting here, so that if any special cases need to be addressed they can be
	protected void setVisFlag_Indiv(int idx, boolean val ){
		switch (idx){
			case disp3DPointAnimResIDX : {setWinFlagsXOR(disp3DPointAnimResIDX, val);break;}
			case disp2DLineAnimResIDX : {setWinFlagsXOR(disp2DLineAnimResIDX, val);break;}
			case disp3DLineAnimResIDX : {setWinFlagsXOR(disp3DLineAnimResIDX, val);break;}
			case dispPlaneAnimResIDX  : {setWinFlagsXOR(dispPlaneAnimResIDX, val);break;}
			case dispSphereAnimResIDX : {setWinFlagsXOR(dispSphereAnimResIDX, val);break;}
			default : {break;}
		}
	}//setFlags  
	
	/**
	 * Individual extending Application Manager post-drawMe functions
	 * @param modAmtMillis
	 * @param is3DDraw
	 */
	@Override
	protected void drawMePost_Indiv(float modAmtMillis, boolean is3DDraw) {
		//draw SOM window in current window if active/displayed
		ri.pushMatState();
		((SOM_AnimWorldWin)getCurFocusDispWindow()).drawSOMWinUI(modAmtMillis);
		ri.popMatState();		
	}

}//class SOM_GeometryMain
