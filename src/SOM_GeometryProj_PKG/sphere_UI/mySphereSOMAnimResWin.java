package SOM_GeometryProj_PKG.sphere_UI;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import SOM_GeometryProj_PKG.geomObj.sphere.SOM_Sphere;
import SOM_GeometryProj_PKG.sphere_SOM_Mapping.Sphere_SOMMapManager;
import base_UI_Objects.*;
import base_UI_Objects.drawnObjs.myDrawnSmplTraj;
import base_UI_Objects.windowUI.myDispWindow;
import base_UI_Objects.windowUI.myGUIObj;
import base_Utils_Objects.vectorObjs.myPoint;
import base_Utils_Objects.vectorObjs.myVector;

public class mySphereSOMAnimResWin extends myDispWindow {
	
	public Sphere_SOMMapManager mapMgr;						//map built from the ui obj's data
	//ui vars
	public final static int
		gIDX_NumUIObjs 		= 0,
		gIDX_NumUISamples 	= 1,
		gIDX_MinRadius		= 2,
		gIDX_MaxRadius		= 3,
		gIDX_SelDispUIObj	= 4;			//ID of a UI object to be selected and highlighted

	public final int numGUIObjs = 5;											//# of gui objects for ui
	
	//to handle real-time update of locations of spheres
	public myVector curMseLookVec;  //pa.c.getMse2DtoMse3DinWorld()
	public myPoint curMseLoc3D;		//pa.c.getMseLoc(pa.sceneCtrVals[pa.sceneIDX])

	//private child-class flags
	public static final int 
		debugAnimIDX = 0,						//debug
		sphereDataLoadedIDX = 1,				//all SOM spheres have been loaded
		showSamplePntsIDX 	= 2,				//show spheres by polys or by sampled surface points
		saveUIObjDataIDX 	= 3,				//save ui object locations as training data on next draw cycle
		currUIObjDatSavedIDX = 4,				//current ui obj data has been saved
		useUIObjLocAsClrIDX	= 5,				//should use ui obj's location as both its and its samples' color		
		showUIObjIdIDX		= 6,				//display the ui obj's ID as a text tag
		showSelUIObjIDX	= 7,					//highlight the sphere with the selected idx
		useSmplsForTrainIDX = 8,				//use surface samples, or ui obj's characteristics (i.e. center), for training data
		showMapBasedLocsIDX = 9,				//show map-derived locations of training data instead of actual locations (or along with?)
		mapBuiltToCurUIObjsIDX = 10,			//the current ui obj configuration has an underlying map built to it
		regenUIObjsIDX 	= 11;					//regenerate ui objs with current specs

	public static final int numPrivFlags = 12;
	
	//represented random uiObjs
	public SOM_Sphere[] uiObjs;
	//initial values
	public int numSpheres = 200, numSmplPoints = 200, curSelSphereIDX = 0;
	public float minSphRad = 5, maxSphRad = 50;
	

	public mySphereSOMAnimResWin(my_procApplet _p, String _n, int _flagIdx, int[] fc, int[] sc, float[] rd, float[] rdClosed,String _winTxt, boolean _canDrawTraj) {
		super(_p, _n, _flagIdx, fc, sc, rd, rdClosed, _winTxt, _canDrawTraj);
		super.initThisWin(_canDrawTraj, true, false);
	}
	
	@Override
	//initialize all private-flag based UI buttons here - called by base class
	public void initAllPrivBtns(){
		truePrivFlagNames = new String[]{								//needs to be in order of privModFlgIdxs
				"Debugging Spheres", "Regen Spheres", "Show Spheres", "Hide Labels", "Location as Color",
				//"Rnd Smpl Order", 
				"HiLiting Sel Sphr","Smpls As Train", "Save Data", "Showing Maps Locs"
		};
		falsePrivFlagNames = new String[]{			//needs to be in order of flags
				"Debug Spheres", "Regen Spheres","Show Sample Pts","Show Labels","Randomized Color",
				//"Seq Smpl Order", 
				"Turn HiLite On","Cntrs As Train","Save Data", "Showing Actual Locs"
		};
		privModFlgIdxs = new int[]{debugAnimIDX, regenUIObjsIDX, showSamplePntsIDX,showUIObjIdIDX, useUIObjLocAsClrIDX, //useSmplLocAsClrIDX, 
				//rndSphrDataIDX,  
				showSelUIObjIDX, useSmplsForTrainIDX,saveUIObjDataIDX, showMapBasedLocsIDX};
		numClickBools = privModFlgIdxs.length;	
		initPrivBtnRects(0,numClickBools);
	}//initAllPrivBtns
	
	@Override
	protected void initMe() {	
		//initUIBox();				//set up ui click region to be in sidebar menu below menu's entries			
		initPrivFlags(numPrivFlags);
		initAllSpheres();
		//this window uses right side info window
		setFlags(drawRightSideMenu, true);		//may need some re-scaling to keep things in the middle and visible

	}

	/**
	 * call to build or rebuilt spheres
	 */
	public void initAllSpheres(){
		setPrivFlags(sphereDataLoadedIDX,false);
		System.out.println("mySOMAnimResWin : initAllSpheres : Need to do this!!!");
	}
	
	@Override
	//set flag values and execute special functionality for this sequencer
	public void setPrivFlags(int idx, boolean val){
		int flIDX = idx/32, mask = 1<<(idx%32);
		privFlags[flIDX] = (val ?  privFlags[flIDX] | mask : privFlags[flIDX] & ~mask);
		switch(idx){
			case debugAnimIDX 			: {	break;}				
			case sphereDataLoadedIDX 	: {	break;}		//sphere data has been loaded				
			case showSamplePntsIDX 		: {	break;}		//show sphere as sample points or as sphere
			case saveUIObjDataIDX 		: { break;}		//save all sphere centers, colors and IDs as training data/classes, and sample point locs, IDs and clrs as validation data
			case currUIObjDatSavedIDX 	: {if(val){pa.outStr2Scr("Current Sphere data saved"); } break;}
			case showUIObjIdIDX  		: { break;}//show labels for spheres
			case useUIObjLocAsClrIDX 	: { break;}		//color of spheres is location or is random
			//case useSmplLocAsClrIDX 	: { break;}		//color of samples is location or current sphere's color (either its location or random color)
			case showSelUIObjIDX 		: { break;}
			case useSmplsForTrainIDX	: {break;}		//use surface samples for train and centers for test, or vice versa
			case mapBuiltToCurUIObjsIDX  : {break;}     //whether map has been built and loaded for current config of spheres
			case regenUIObjsIDX		: {  if(val){initAllSpheres();privFlags[flIDX] =  privFlags[flIDX] & ~mask;} break;}		//remake all spheres, turn of flag
			//case rndSphrDataIDX			: { break;}		//whether or not to randomize training data before save
		}		
	}//setPrivFlags		
	
	//initialize structure to hold modifiable menu regions
	@Override
	protected void setupGUIObjsAras(){	
		//define all list objects here, if any exist
		TreeMap<Integer, String[]> tmpListObjVals = new TreeMap<Integer, String[]>();
		//pa.outStr2Scr("setupGUIObjsAras in :"+ name);
		guiMinMaxModVals = new double [][]{
			{10,1000,10},										//# of spheres
			{10,1000,10},										//# of per-sphere samples
			{1,100,1},											//min radius of spheres
			{10,500,1},											//max radius of spheres
			{0,numSpheres-1,1}											//which sphere to select to highlight
		};														//min max mod values for each modifiable UI comp	
		
		guiStVals = new double[]{
			numSpheres,
			numSmplPoints,
			minSphRad,
			maxSphRad,
			curSelSphereIDX
		};								//starting value
		guiObjNames = new String[]{
			"# of spheres",
			"# of samples per sphere",
			"Min sphere radius",
			"Max sphere radius",
			"ID of sphere to select"
		};							//name/label of component		
		//idx 0 is treat as int, idx 1 is obj has list vals, idx 2 is object gets sent to windows, 3 is object allows for lclick-up/rclick-down mod
		guiBoolVals = new boolean [][]{
			{true, false, true},
			{true, false, true},
			{false, false, true},			
			{false, false, true},
			{true, false, true}
		};						//per-object  list of boolean flags
		
		//since horizontal row of UI comps, uiClkCoords[2] will be set in buildGUIObjs		
		guiObjs = new myGUIObj[numGUIObjs];			//list of modifiable gui objects
		if(numGUIObjs > 0){
			buildGUIObjs(guiObjNames,guiStVals,guiMinMaxModVals,guiBoolVals,new double[]{xOff,yOff},tmpListObjVals);			//builds a horizontal list of UI comps
		}
	}
	@Override
	protected void setUIWinVals(int UIidx) {
		float val = (float)guiObjs[UIidx].getVal();
		int ival = (int)val;

		switch(UIidx){		
		case gIDX_NumUIObjs : {
			if(ival != numSpheres){numSpheres = ival;guiObjs[gIDX_SelDispUIObj].setNewMax(ival-1);initAllSpheres();}
			break;}
		case gIDX_NumUISamples : {
			if(ival != numSmplPoints){numSmplPoints = ival;initAllSpheres();}
			break;}
		case gIDX_MinRadius : {
			if(val != minSphRad){
				minSphRad = val;
				if(minSphRad >= maxSphRad) { maxSphRad = minSphRad + 1;setWinToUIVals(gIDX_MaxRadius, maxSphRad);   }
				initAllSpheres();}
			break;}
		case gIDX_MaxRadius	: {
			if(val != maxSphRad){
				maxSphRad = val;
				if(minSphRad >= maxSphRad)  { minSphRad = maxSphRad - 1;setWinToUIVals(gIDX_MinRadius, minSphRad);   }				
				initAllSpheres();}
			break;}
		case gIDX_SelDispUIObj :{
			if(ival != curSelSphereIDX){curSelSphereIDX = pa.min(ival, numSpheres-1);}//don't select a sphere Higher than the # of spheres
			break;}
		
		default : {break;}
		}
	}
	
	/**
	 * save data in appropriate formats for spheres and sphere sample points, to appropriately named files
	 */
	private void saveSphereInfo(){
		
		
	}
	


	@Override
	public void initDrwnTrajIndiv(){}
	

	//overrides function in base class mseClkDisp
	@Override
	public void drawTraj3D(float animTimeMod,myPoint trans){
		
	}//drawTraj3D
	@Override
	protected void drawMe(float animTimeMod) {
//		curMseLookVec = pa.c.getMse2DtoMse3DinWorld(pa.sceneCtrVals[pa.sceneIDX]);			//need to be here
//		curMseLoc3D = pa.c.getMseLoc(pa.sceneCtrVals[pa.sceneIDX]);
		//pa.outStr2Scr("Current mouse loc in 3D : " + curMseLoc3D.toStrBrf() + "| scenectrvals : " + pa.sceneCtrVals[pa.sceneIDX].toStrBrf() +"| current look-at vector from mouse point : " + curMseLookVec.toStrBrf());
		pa.pushMatrix();pa.pushStyle();//nested ifthen shenannigans to get rid of if checks in each individual draw
		if(getPrivFlags(sphereDataLoadedIDX)){ 	
			if(getPrivFlags(showMapBasedLocsIDX)){		
				if (getPrivFlags(mapBuiltToCurUIObjsIDX)){//show all spheres/samples based on map-derived locations if selected and map is made
					//draw spheres/samples based on map info - use 1st 3 features of non-scaled ftr data from map's nodes as x-y-z 
					if(getPrivFlags(useUIObjLocAsClrIDX)){
						if(getPrivFlags(showSamplePntsIDX)){  	for(SOM_Sphere s : uiObjs){s.drawMeSmplsClrLoc_BMU(pa);}			}//useSmplLocAsClrIDX
						else {									for(SOM_Sphere s : uiObjs){s.drawMeClrLoc_BMU(pa);}}
					} else {
						if(getPrivFlags(showSamplePntsIDX)){	for(SOM_Sphere s : uiObjs){s.drawMeSmplsClrRnd_BMU(pa);}} 
						else {									for(SOM_Sphere s : uiObjs){s.drawMeClrRnd_BMU(pa);}}
					}
					if(getPrivFlags(showUIObjIdIDX)){			for(SOM_Sphere s : uiObjs){s.drawMeLabel_BMU(pa);}	}
					if(getPrivFlags(showSelUIObjIDX)){			uiObjs[curSelSphereIDX].drawMeSelected_BMU(pa,animTimeMod);     }
				} else {										setPrivFlags(showMapBasedLocsIDX, false);	}	//turn off flag if not possible to draw 
			} else {				
				if(getPrivFlags(useUIObjLocAsClrIDX)){
					if(getPrivFlags(showSamplePntsIDX)){		for(SOM_Sphere s : uiObjs){s.drawMeSmplsClrLoc(pa);}} //useSmplLocAsClrIDX
					else {										for(SOM_Sphere s : uiObjs){s.drawMeClrLoc(pa);}}
				} else {
					if(getPrivFlags(showSamplePntsIDX)){		for(SOM_Sphere s : uiObjs){s.drawMeSmplsClrRnd(pa);}} 
					else {										for(SOM_Sphere s : uiObjs){s.drawMeClrRnd(pa);}}
				}
				if(getPrivFlags(showUIObjIdIDX)){				for(SOM_Sphere s : uiObjs){s.drawMeLabel(pa);}	}
				if(getPrivFlags(showSelUIObjIDX)){				uiObjs[curSelSphereIDX].drawMeSelected(pa,animTimeMod);     }
			}//use locs or map-locs
		}
		pa.popStyle();pa.popMatrix();
		if(getPrivFlags(saveUIObjDataIDX)){saveSphereInfo();	setPrivFlags(saveUIObjDataIDX, false);	}
	}//drawMe
	
	@Override
	public void drawCustMenuObjs(){}
	@Override
	protected boolean simMe(float modAmtSec) { return true;	}


	@Override
	protected void stopMe() {	}	
	
	//debug function
	public void dbgFunc0(){
		//show min and max radius give to map nodes
		//pa.outStr2Scr("Min Radius : " +dataPoint.minRad + "|Max Radius : " +dataPoint.maxRad);
	}	
	public void dbgFunc1(){	
	}	
	public void dbgFunc2(){	
	}	
	public void dbgFunc3(){	
	}	
	public void dbgFunc4(){	
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
	protected void snapMouseLocs(int oldMouseX, int oldMouseY, int[] newMouseLoc) {}	

	@Override
	protected void hndlMouseRelIndiv() {}
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
	protected void showMe() {}

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

	@Override
	protected void setCameraIndiv(float[] camVals) {		
		//, float rx, float ry, float dz are now member variables of every window
		pa.camera(camVals[0],camVals[1],camVals[2],camVals[3],camVals[4],camVals[5],camVals[6],camVals[7],camVals[8]);      
		// puts origin of all drawn objects at screen center and moves forward/away by dz
		pa.translate(camVals[0],camVals[1],(float)dz); 
	    setCamOrient();	
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

