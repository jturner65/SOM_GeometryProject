package SOM_GeometryProj_PKG.geom_UI;

import java.util.LinkedHashMap;

import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_3DPointSOMExample;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers.Geom_3DPointMapMgr;
import base_Math_Objects.vectorObjs.doubles.myPoint;
import base_Math_Objects.vectorObjs.doubles.myVector;
import base_Render_Interface.IGraphicsAppInterface;
import base_SOM_Objects.som_geom.SOM_GeomMapManager;
import base_SOM_Objects.som_geom.geom_UI.SOM_AnimWorldWin;
import base_SOM_Objects.som_geom.geom_utils.geom_objs.SOM_GeomObjTypes;
import base_UI_Objects.GUI_AppManager;
import base_UI_Objects.windowUI.uiObjs.base.GUIObj_Params;

public class Geom_3DPointAnimResWin extends SOM_AnimWorldWin {
    /**
     * # of private boolean flags for this window - expands upon those determined in SOM_AnimWorldWin
     */
    private final int numPrivFlags = numBaseAnimWinPrivFlags;
    
    public Geom_3DPointAnimResWin(IGraphicsAppInterface _p, GUI_AppManager _AppMgr, int _winIdx) {
        super(_p, _AppMgr, _winIdx, SOM_GeomObjTypes.point);
        
    }

    @Override
    protected SOM_GeomMapManager buildGeom_SOMMapManager() {
        Geom_3DPointMapMgr _mgr = new Geom_3DPointMapMgr(somUIWin, this, AppMgr.get3dCubeBnds(), AppMgr.getArgsMap());
        return _mgr;
    }
    /**
     * Initialize any UI control flags appropriate for specific instanced SOM Animation window
     */
    @Override
    protected final void initDispFlags_Indiv() {}
    
    @Override
    protected void initMe_Indiv() {    
    }
    
    /**
     * Retrieve the total number of defined privFlags booleans (application-specific state bools and interactive buttons)
     */
    @Override
    public int getTotalNumOfPrivBools() {        return numPrivFlags;    }
    
    /**
     * Instance class determines the true and false labels for button to control showing full object, or just wire frame
     * If empty no button is displayed
     * @return array holding true(idx0) and false(idx1) labels for button
     */
    @Override
    protected final String[] getShowWireFrameBtnTFLabels() {    return null;}

    @Override
    protected void handleSOMAnimFlags_Indiv(int idx, boolean val) {
        switch (idx) {//special actions for each flag
            default                        : {return;}
        }
    }
    
    /**
     * Build all UI objects to be shown in left side bar menu for this window. This is the first child class function called by initThisWin
     * @param tmpUIObjMap : map of GUIObj_Params, keyed by unique string, with values describing the UI object
     *             - The object IDX                   
     *          - A double array of min/max/mod values                                                   
     *          - The starting value                                                                      
     *          - The label for object                                                                       
     *          - The object type (GUIObj_Type enum)
     *          - A boolean array of behavior configuration values : (unspecified values default to false)
     *               idx 0: value is sent to owning window,  
     *               idx 1: value is sent on any modifications (while being modified, not just on release), 
     *               idx 2: changes to value must be explicitly sent to consumer (are not automatically sent),
     *          - A boolean array of renderer format values :(unspecified values default to false) - Behavior Boolean array must also be provided!
     *                 - Should be multiline
     *                 - One object per row in UI space (i.e. default for multi-line and btn objects is false, single line non-buttons is true)
     *                 - Force this object to be on a new row/line (For side-by-side layouts)
     *                 - Text should be centered (default is false)
     *                 - Object should be rendered with outline (default for btns is true, for non-buttons is false)
     *                 - Should have ornament
     *                 - Ornament color should match label color 
     */
    @Override
    protected final void setupGUIObjsAras_Indiv(LinkedHashMap<String, GUIObj_Params> tmpUIObjMap) {}

    /**
     * Build all UI buttons to be shown in left side bar menu for this window. This is for instancing windows to add to button region
     * @param firstIdx : the first index to use in the map/as the objIdx
     * @param tmpUIBoolSwitchObjMap : map of GUIObj_Params to be built containing all flag-backed boolean switch definitions, keyed by sequential value == objId
     *                 the first element is the object index
     *                 the second element is true label
     *                 the third element is false label
     *                 the final element is integer flag idx 
     */
    @Override
    protected final void setupGUIBoolSwitchAras_Indiv(int firstIdx, LinkedHashMap<String, GUIObj_Params> tmpUIBoolSwitchObjMap)  {}

    
    /**
     * send all instance-specific values from UI to map manager
     */
    @Override
    protected final void setMapMgrGeomObjVals_Indiv() {};
    
    @Override
    protected String[] setUI_GeomObjFeatureListVals() {return Geom_3DPointSOMExample.ftrNames;}


    @Override
    protected long getNumTrainingExamples(int objs, int smplPerObj) {
        long ttlNumSamples = objs * smplPerObj;
        return ttlNumSamples;
    }

    @Override
    protected int getMinNumObjs() {return 10;}
    @Override
    protected int getMaxNumObjs() {return 500000;}
    @Override
    protected int getMinNumSmplsPerObj() {return 1;}
    @Override
    protected int getMaxNumSmplsPerObj() {return 1;}

    @Override
    protected int getModNumSmplsPerObj() {return 1;}
    
    @Override
    protected boolean setUI_IntValsCustom_Indiv(int UIidx, int ival, int oldVal) {return false;}

    @Override
    protected boolean setUI_FloatValsCustom_Indiv(int UIidx, float ival, float oldVal) {return false;}

    @Override
    protected void getAllUIValsForPreProcSave_Indiv(LinkedHashMap<String, String> vals) {}

    @Override
    protected void setAllUIValsFromPreProcLoad_Indiv(LinkedHashMap<String, String> uiVals) {}
    //////////////////////////////
    // instance-based draw handling
    /**
     * any instance-window specific display
     * @param modAmtMillis
     */
    @Override
    protected final float drawRightSideInfoBar_Indiv(float modAmtMillis, float yOff) {        
        return yOff;
    }

    @Override
    protected final void drawOnScreenStuffPriv(float modAmtMillis) {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * instance-specific drawing setup before objects are actually drawn 
     */
    @Override
    protected final void drawMeFirst_Indiv() {    }
    
    /**
     * instance-specific drawing after objects are drawn but before info is saved
     */
    @Override
    protected final void drawMeLast_Indiv() {        
        
    }
    //////////////////////////////
    //  manage menu button selections and setup

    @Override
    protected final void setCustMenuBtnLabels() {
        // TODO Auto-generated method stub
        
    }
    //////////////////////////////
    // instance-based mouse handling

    @Override
    protected final boolean hndlMseMove_Priv(int mouseX, int mouseY, myPoint mseClckInWorld) {  return false;}
    @Override
    protected final boolean hndlMseClick_Priv(int mouseX, int mouseY, myPoint mseClckInWorld, int mseBtn) { return false;}
    @Override
    protected final boolean hndlMseDrag_Priv(int mouseX, int mouseY, int pmouseX, int pmouseY, myPoint mouseClickIn3D,myVector mseDragInWorld, int mseBtn) {return false;}
    @Override
    protected final void hndlMseRelease_Priv() {    }
    @Override
    protected boolean handleMouseWheel_Indiv(int ticks, float mult) {        return false;    } 
    
    @Override
    public final void initDrwnTraj_Indiv(){}
    @Override
    protected final void snapMouseLocs(int oldMouseX, int oldMouseY, int[] newMouseLoc) {}    
    @Override
    protected final void endShiftKey_Indiv() {}
    @Override
    protected final void endAltKey_Indiv() {}
    @Override
    protected final void endCntlKey_Indiv() {}
    @Override
    protected myPoint getMsePtAs3DPt(myPoint mseLoc) {        return new myPoint(mseLoc);    }
}//class Geom_3DPointAnimResWin
