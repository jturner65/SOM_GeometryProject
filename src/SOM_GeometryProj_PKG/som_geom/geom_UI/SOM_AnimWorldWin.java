package SOM_GeometryProj_PKG.som_geom.geom_UI;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;

import SOM_GeometryProj_PKG.som_geom.SOM_GeomMapManager;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomObj;
import SOM_GeometryProj_PKG.SOM_GeometryMain;
import base_SOM_Objects.SOM_MapManager;
import base_UI_Objects.my_procApplet;
import base_UI_Objects.drawnObjs.myDrawnSmplTraj;
import base_UI_Objects.windowUI.myDispWindow;
import base_UI_Objects.windowUI.myGUIObj;

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
	//ui vars
	public static final int
		gIDX_NumUIObjs 			= 0,
		gIDX_NumUISamples 		= 1,
		gIDX_SelDispUIObj		= 2;					//ID of a ui obj to be selected and highlighted

	protected static final int numBaseAnimWinUIObjs = 3;
	//instancing class will specify numGUIObjs	
	protected double[] uiVals;				
	
	//raw values from ui components
	/**
	 * # of priv flags from base class and instancing class
	 */
	private int numPrivFlags;

	public static final int 
		debugAnimIDX 			= 0,				//debug
		uiObjDataLoadedIDX 		= 1,				//all SOM ui objs have been loaded
		showSamplePntsIDX 		= 2,				//show ui objs by polys or by sampled points
		saveUIObjDataIDX 		= 3,				//save ui obj locations as training data on next draw cycle
		useUIObjLocAsClrIDX		= 4,				//should use ui obj's location as both its and its samples' color
		showUIObjIdIDX			= 5,				//display the ui obj's ID as a text tag
		showSelUIObjIDX			= 6,				//highlight the ui obj with the selected idx
		useSmplsForTrainIDX 	= 7,				//use surface samples, or ui obj centers, for training data
		showMapBasedLocsIDX 	= 8,				//show map-derived locations of training data instead of actual locations (or along with?)
		uiObjBMUsSetIDX			= 9,				//ui object's bmus have been set
		mapBuiltToCurUIObjsIDX 	= 10,				//the current ui obj configuration has an underlying map built to it
		regenUIObjsIDX 			= 11;				//regenerate ui objs with current specs
	
	protected static final int numBaseAnimWinPrivFlags = 12;
		
	//initial values
	public int numGeomObjs = 200, numSmplPoints = 200, curSelGeomObjIDX = 0;
	
	//represented random/generated uiObjs
	public SOM_GeomObj[] uiObjs;
	
	//object type the instancing window manages
	public final String geomObjType;
	
	//dimensions of SOM Map - hard coded to override setting from SOM Map UI Window - need to set in window
	protected float[] SOMMapDims = new float[] {834.8f,834.8f};


	public SOM_AnimWorldWin(my_procApplet _p, String _n, int _flagIdx, int[] fc, int[] sc, float[] rd, float[] rdClosed, String _winTxt, boolean _canDrawTraj, String _type) {
		super(_p, _n, _flagIdx, fc, sc, rd, rdClosed, _winTxt, _canDrawTraj);
		initAndSetAnimWorldVals();
		geomObjType = _type;
	}
	
	/**
	 * called after base window ctor/before instancing class ctor functions and initMe
	 * any initialization that is relevant for this abstract class's objects
	 */
	private void initAndSetAnimWorldVals() {}	
	
	/**
	 * instancing window will build the map manager that this anim world will use
	 * @return
	 */
	public abstract SOM_MapManager buildMapManager();
	/**
	 * return appropriate SOM Map Manager for this window
	 * @return
	 */
	public final SOM_MapManager getMapMgr() {return mapMgr;}


	@Override
	public final void initAllPrivBtns() {
		
		//add an entry for each button, in the order they are wished to be displayed	true tag, false tag, btn IDX	
		ArrayList<Object[]> tmpBtnNamesArray = new ArrayList<Object[]>();
		tmpBtnNamesArray.add(new Object[]{"Debugging","Debug",debugAnimIDX});
		tmpBtnNamesArray.add(new Object[]{"Regenerating " +geomObjType + " Objs","Regenerate " +geomObjType + " Objs",regenUIObjsIDX});		
		tmpBtnNamesArray.add(new Object[]{"Showing Sample Points", "Showing " +geomObjType + " Objects", showSamplePntsIDX});		
		tmpBtnNamesArray.add(new Object[]{"Hide Labels", "Show Labels", showUIObjIdIDX});    		
		tmpBtnNamesArray.add(new Object[]{"Location as Color", "Randomized Color", useUIObjLocAsClrIDX});		
		tmpBtnNamesArray.add(new Object[]{"Hi-Light Sel " +geomObjType + " ", "Enable " +geomObjType + " Hi-Light", showSelUIObjIDX});  
		
		tmpBtnNamesArray.add(new Object[]{"Train From " +geomObjType + " Samples", "Train From " +geomObjType + " Centers/Bases", useSmplsForTrainIDX});    
		tmpBtnNamesArray.add(new Object[]{"Save Data", "Save Data", saveUIObjDataIDX});        
		tmpBtnNamesArray.add(new Object[]{"Showing BMU-derived Locs", "Showing Actual Locs", showMapBasedLocsIDX});  

		//add instancing-class specific buttons - returns total # of private flags in instancing class
		numPrivFlags = initAllAnimWorldPrivBtns_Indiv(tmpBtnNamesArray);
		//finalize setup for UI toggle buttons - convert to arrays
		truePrivFlagNames = new String[tmpBtnNamesArray.size()];
		falsePrivFlagNames = new String[truePrivFlagNames.length];
		privModFlgIdxs = new int[truePrivFlagNames.length];
		for(int i=0;i<truePrivFlagNames.length;++i) {
			Object[] tmpAra = tmpBtnNamesArray.get(i);
			truePrivFlagNames[i] = (String) tmpAra[0];
			falsePrivFlagNames[i] = (String) tmpAra[1];
			privModFlgIdxs[i] = (int) tmpAra[2];
		}		
		numClickBools = truePrivFlagNames.length;	
		initPrivBtnRects(0,numClickBools);		
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
	protected abstract int initAllAnimWorldPrivBtns_Indiv(ArrayList<Object[]> tmpBtnNamesArray);
	
	/**
	 * call to build or rebuild geometric objects
	 */
	public final void initAllGeomObjs() {
		((SOM_GeomMapManager) mapMgr).setNumObjsAndSamples(numGeomObjs, numSmplPoints);
		initAllGeomObjs_Indiv();
	}
	/**
	 * call to build or rebuild geometric objects
	 */
	protected abstract void initAllGeomObjs_Indiv();
	/**
	 * call to save the data for all the objects in the scene
	 */
	protected abstract void saveGeomObjInfo();

	@Override
	public final void setPrivFlags(int idx, boolean val) {
		int flIDX = idx/32, mask = 1<<(idx%32);
		privFlags[flIDX] = (val ?  privFlags[flIDX] | mask : privFlags[flIDX] & ~mask);
		switch (idx) {//special actions for each flag
			case debugAnimIDX 			: {	break;}				
			case uiObjDataLoadedIDX 	: {	break;}		//object data has been loaded				
			case showSamplePntsIDX 		: {	break;}		//show object as sample points or as sphere
			case saveUIObjDataIDX 		: { break;}		//save all object data
			case showUIObjIdIDX  		: { break;}		//show labels for objects
			case useUIObjLocAsClrIDX 	: { break;}		//color of objects is location or is random
			case showSelUIObjIDX 		: { break;}
			case useSmplsForTrainIDX	: { break;}		//use surface samples for train and centers for test, or vice versa
			case showMapBasedLocsIDX	: { break;}
			case uiObjBMUsSetIDX		: { break;}
			case mapBuiltToCurUIObjsIDX : { break;}     //whether map has been built and loaded for current config of spheres
			case regenUIObjsIDX			: { if(val){initAllGeomObjs();privFlags[flIDX] =  privFlags[flIDX] & ~mask;} break;}		//remake all objects, turn off flag
			default						: { setPrivFlags_Indiv(idx,val);}
		}
	}//setPrivFlags
	
	/**
	 * set values for instancing class-specific boolean flags
	 * @param idx
	 * @param val
	 */
	protected abstract void setPrivFlags_Indiv(int idx, boolean val);
	
	@Override
	protected final void setupGUIObjsAras() {
		//list box values - keyed by list obj IDX, value is string array of list obj values
		TreeMap<Integer, String[]> tmpListObjVals = new TreeMap<Integer, String[]>();
		
		ArrayList<Object[]> tmpUIObjArray = new ArrayList<Object[]>();
		//tmpBtnNamesArray.add(new Object[]{"Building SOM","Build SOM ",buildSOMExe});
		//object array of elements of following format  : 
		//	the first element double array of min/max/mod values
		//	the 2nd element is starting value
		//	the 3rd elem is label for object
		//	the 4th element is boolean array of {treat as int, has list values, value is sent to owning window}
		tmpUIObjArray.add(new Object[] {new double[]{10,1000,10}, (double)(numGeomObjs*1.0), "# of " +geomObjType + " Objects", new boolean[]{true, false, true}});   				//gIDX_NumUIObjs 		                                                                        
		tmpUIObjArray.add(new Object[] {new double[]{10,1000,10}, (double)(numSmplPoints), "# of samples per Object", new boolean[]{true, false, true}});  				//gIDX_NumUISamples 	                                                                        
		tmpUIObjArray.add(new Object[] {new double[]{0,numGeomObjs-1,1}, (double)(curSelGeomObjIDX), "ID of Object to Select", new boolean[]{true, false, true}});   													//gIDX_SelDispUIObj	                                                                        

	
		//populate instancing application objects
		setupGUIObjsAras_Indiv(tmpUIObjArray,tmpListObjVals);
		
		int numGUIObjs = tmpUIObjArray.size();		
		guiMinMaxModVals = new double [numGUIObjs][3];
		guiStVals = new double[numGUIObjs];
		guiObjNames = new String[numGUIObjs];
		guiBoolVals = new boolean [numGUIObjs][4];
		uiVals = new double[numGUIObjs];//raw values
		for(int i =0;i<numGUIObjs; ++i) {
			guiMinMaxModVals[i] = (double[])tmpUIObjArray.get(i)[0];
			guiStVals[i] = (Double)tmpUIObjArray.get(i)[1];
			guiObjNames[i] = (String)tmpUIObjArray.get(i)[2];
			guiBoolVals[i] = (boolean[])tmpUIObjArray.get(i)[3];
			uiVals[i] = guiStVals[i];
		}
		
		//since horizontal row of UI comps, uiClkCoords[2] will be set in buildGUIObjs		
		guiObjs = new myGUIObj[numGUIObjs];			//list of modifiable gui objects

		buildGUIObjs(guiObjNames,guiStVals,guiMinMaxModVals,guiBoolVals,new double[]{xOff,yOff},tmpListObjVals);			//builds a horizontal list of UI comps	
	
	}//setupGUIObjsAras
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
	protected abstract void setupGUIObjsAras_Indiv(ArrayList<Object[]> tmpUIObjArray, TreeMap<Integer, String[]> tmpListObjVals);

	@Override
	protected  final void setUIWinVals(int UIidx) {
		float val = (float)guiObjs[UIidx].getVal();
		int ival = (int)val;

		switch(UIidx){		
		case gIDX_NumUIObjs : {
			if(ival != numGeomObjs){numGeomObjs = ival;guiObjs[gIDX_SelDispUIObj].setNewMax(ival-1);initAllGeomObjs();}
			break;}
		case gIDX_NumUISamples : {
			if(ival != numSmplPoints){numSmplPoints = ival;initAllGeomObjs();}
			break;}
		case gIDX_SelDispUIObj :{
			if(ival != curSelGeomObjIDX){curSelGeomObjIDX = pa.min(ival, numGeomObjs-1);}//don't select a sphere Higher than the # of spheres
			break;}
		
		default : {setUIWinVals_Indiv(UIidx);}
		}	
	}
	/**
	 * For instance-class specific ui values
	 * @param UIidx
	 */
	protected abstract void setUIWinVals_Indiv(int UIidx);

	@Override
	protected final void initMe() {
		//build map manager
		mapMgr = buildMapManager();
		
		//capable of using right side menu
		setFlags(drawRightSideMenu, true);	
		//init specific sim flags
		initPrivFlags(numPrivFlags);
		//build objects - instance class
		initAllGeomObjs();
		//instance-specific init
		initMe_Indiv();
	}
	/**
	 * instancing class-specific functionality
	 */
	protected abstract void initMe_Indiv();
	
/////////////////////////////
	// drawing routines
	@Override
	protected final void setCameraIndiv(float[] camVals) {
		//, float rx, float ry, float dz are now member variables of every window
		pa.camera(camVals[0],camVals[1],camVals[2],camVals[3],camVals[4],camVals[5],camVals[6],camVals[7],camVals[8]);      
		// puts origin of all drawn objects at screen center and moves forward/away by dz
		pa.translate(camVals[0],camVals[1],(float)dz); 
	    setCamOrient();	
	}

	@Override
	protected final void drawMe(float animTimeMod) {
//		curMseLookVec = pa.c.getMse2DtoMse3DinWorld(pa.sceneCtrVals[pa.sceneIDX]);			//need to be here
//		curMseLoc3D = pa.c.getMseLoc(pa.sceneCtrVals[pa.sceneIDX]);
		//pa.outStr2Scr("Current mouse loc in 3D : " + curMseLoc3D.toStrBrf() + "| scenectrvals : " + pa.sceneCtrVals[pa.sceneIDX].toStrBrf() +"| current look-at vector from mouse point : " + curMseLookVec.toStrBrf());
		pa.pushMatrix();pa.pushStyle();//nested ifthen shenannigans to get rid of if checks in each individual draw
		if(getPrivFlags(uiObjDataLoadedIDX)){ 	
			if(getPrivFlags(showMapBasedLocsIDX)){		
				if (getPrivFlags(mapBuiltToCurUIObjsIDX)){//show all spheres/samples based on map-derived locations if selected and map is made
					//draw spheres/samples based on map info - use 1st 3 features of non-scaled ftr data from map's nodes as x-y-z 
					if(getPrivFlags(useUIObjLocAsClrIDX)){
						if(getPrivFlags(showSamplePntsIDX)){  	for(SOM_GeomObj s : uiObjs){s.drawMeSmplsClrLoc_BMU(pa);}			}//useSmplLocAsClrIDX
						else {									for(SOM_GeomObj s : uiObjs){s.drawMeClrLoc_BMU(pa);}}
					} else {
						if(getPrivFlags(showSamplePntsIDX)){	for(SOM_GeomObj s : uiObjs){s.drawMeSmplsClrRnd_BMU(pa);}} 
						else {									for(SOM_GeomObj s : uiObjs){s.drawMeClrRnd_BMU(pa);}}
					}
					if(getPrivFlags(showUIObjIdIDX)){			for(SOM_GeomObj s : uiObjs){s.drawMeLabel_BMU(pa);}	}
					if(getPrivFlags(showSelUIObjIDX)){			uiObjs[curSelGeomObjIDX].drawMeSelected_BMU(pa,animTimeMod);     }
				} else {										setPrivFlags(showMapBasedLocsIDX, false);	}	//turn off flag if not possible to draw 
			} else {				
				if(getPrivFlags(useUIObjLocAsClrIDX)){
					if(getPrivFlags(showSamplePntsIDX)){		for(SOM_GeomObj s : uiObjs){s.drawMeSmplsClrLoc(pa);}} //useSmplLocAsClrIDX
					else {										for(SOM_GeomObj s : uiObjs){s.drawMeClrLoc(pa);}}
				} else {
					if(getPrivFlags(showSamplePntsIDX)){		for(SOM_GeomObj s : uiObjs){s.drawMeSmplsClrRnd(pa);}} 
					else {										for(SOM_GeomObj s : uiObjs){s.drawMeClrRnd(pa);}}
				}
				if(getPrivFlags(showUIObjIdIDX)){				for(SOM_GeomObj s : uiObjs){s.drawMeLabel(pa);}	}
				if(getPrivFlags(showSelUIObjIDX)){				uiObjs[curSelGeomObjIDX].drawMeSelected(pa,animTimeMod);     }
			}//use locs or map-locs
		}
		pa.popStyle();pa.popMatrix();
		if(getPrivFlags(saveUIObjDataIDX)){saveGeomObjInfo();	setPrivFlags(saveUIObjDataIDX, false);	}
	}//drawMe
	
	/**
	 * draw som map window UI Objects
	 */
	@Override
	public final void drawCustMenuObjs(){
		((SOM_GeometryMain) pa).drawSOMUIObjs();		
	}


	/////////////////////////////
	// end drawing routines
	
	/////////////////////////////
	// sim routines	
	@Override
	protected final boolean simMe(float modAmtSec) {		return false;	}

	@Override
	protected final void stopMe() {}
	/////////////////////////////
	// end sim routines	

	@Override
	protected final void processTrajIndiv(myDrawnSmplTraj drawnNoteTraj){		}
	@Override
	protected final void addSScrToWinIndiv(int newWinKey){}
	@Override
	protected final void addTrajToScrIndiv(int subScrKey, String newTrajKey){}
	@Override
	protected final void delSScrToWinIndiv(int idx) {}	
	@Override
	protected final void delTrajToScrIndiv(int subScrKey, String newTrajKey) {}
	//resize drawn all trajectories
	@Override
	protected final void resizeMe(float scale) {}
	@Override
	protected final void closeMe() {}
	@Override
	protected final void showMe() {	
		setCustMenuBtnNames();
	}

}//SOM_AnimWorldWin
