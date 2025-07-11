package SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers;

import java.util.Map;

import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_PlaneSOMExample;
import SOM_GeometryProj_PKG.geom_ObjExamples.mapNodes.Geom_PlaneSOMMapNode;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.exampleManagers.Geom_PlaneExManager;
import SOM_GeometryProj_PKG.geom_Utils.geomGen.runners.Geom_PlaneObjBldrRunner;
import base_Math_Objects.vectorObjs.tuples.Tuple;
import base_Render_Interface.IGraphicsAppInterface;
import base_SOM_Objects.som_examples.enums.SOM_ExDataType;
import base_SOM_Objects.som_examples.enums.SOM_FtrDataType;
import base_SOM_Objects.som_geom.SOM_GeomMapManager;
import base_SOM_Objects.som_geom.geom_UI.SOM_AnimWorldWin;
import base_SOM_Objects.som_geom.geom_examples.SOM_GeomExampleManager;
import base_SOM_Objects.som_geom.geom_examples.SOM_GeomObj;
import base_SOM_Objects.som_geom.geom_utils.Geom_SOMMseOvrDisp;
import base_SOM_Objects.som_geom.geom_utils.geom_objs.SOM_GeomObjTypes;
import base_SOM_Objects.som_geom.geom_utils.geom_threading.geomGen.SOM_GeomObjBldrRunner;
import base_SOM_Objects.som_geom.geom_utils.geom_threading.geomGen.SOM_GeomObjBldrTasks;
import base_SOM_Objects.som_mapnodes.base.SOM_MapNode;
import base_SOM_Objects.som_ui.SOM_MseOvrDisplay;
import base_SOM_Objects.som_ui.win_disp_ui.SOM_MapUIWin;

public class Geom_PlaneMapMgr extends SOM_GeomMapManager {
    
    public static final int numFlags = numGeomBaseFlags;    
    
    //(SOM_MapUIWin _win, SOM_AnimWorldWin _dispWin, float[] _dims, float[][] _worldBounds, TreeMap<String, Object> _argsMap)
    public Geom_PlaneMapMgr(SOM_MapUIWin _win,  SOM_AnimWorldWin _dispWin, float[][] _worldBounds, Map<String, Object> _argsMap) {
        super(_win, _dispWin, _worldBounds, _argsMap,SOM_GeomObjTypes.plane, Geom_PlaneSOMExample._numFtrs);
    }

    /**
     * build the thread runner for this map manager that will manage the various tasks related to the geometric objects
     * @return
     */
    @Override
    protected final SOM_GeomObjBldrRunner buildObjRunner() {
        return new Geom_PlaneObjBldrRunner(this, th_exec, buildEmptyObjArray(), false, new int[] {numObjsToBuild, numSamplesPerObj}, SOM_GeomObjBldrTasks.buildBaseObj);        
    }

    @Override
    protected final SOM_GeomObj[] buildEmptyObjArray() {        return new Geom_PlaneSOMExample[numObjsToBuild];}
    
    /**
     * build the example data mapper specific to instancing class
     * @return
     */
    @Override
    protected final SOM_GeomExampleManager buildExampleDataMappers_Indiv(String _exMgrName) {return new Geom_PlaneExManager(this, SOM_GeomObjTypes.plane.toString(), "Planar Geometric Objects",  SOM_ExDataType.Training, false, _exMgrName);}

    /**
     * send any instance-specific control/ui values to objRunners, based on task
     */
    @Override
    protected final void execObjRunner_Pre_Indiv(SOM_GeomObjBldrTasks _task) {
        switch(_task) {
            case buildBaseObj             : { break;}
            case regenSamplesBaseObj    : { break;}
            default : {break;}
        }
    }
    public final int getNumSamplesToBuildObject() {return Geom_PlaneSOMExample._numSrcPts;}

    /**
     * build the example that represents the SOM data where the mouse is
     */
    @Override
    protected final SOM_MseOvrDisplay buildMseOverExample() {return new Geom_SOMMseOvrDisp(this,0.0f);}
    @Override
    protected void buildValidationDataAra() {    }
    @Override
    public SOM_MapNode buildMapNode(Tuple<Integer, Integer> mapLoc,  SOM_FtrDataType _ftrTypeUsedToTrain, String[] tkns) {
        return new Geom_PlaneSOMMapNode(this,mapLoc, _ftrTypeUsedToTrain, tkns);
    }    
    @Override
    public String getClassSegmentTitleString(int classID) {        return null;}
    @Override
    public String getCategorySegmentTitleString(int catID) {    return null;}    
    
    /**
     * The maximum number of training examples to draw to prevent lag/crashing/overflow. 
     * The purpose of showing these is to illustrate the random distribution of examples.
     */
    @Override
    public int getMaxNumExsToShow() {return 1000;}
    @Override
    protected final int getNumGeomFlags_Indiv() {    return numFlags;}
    @Override
    protected void setGeomFlag_Indiv(int idx, boolean val) {
        switch (idx) {//special actions for each flag
            default : {break;}
        }
    }
    ////////////////////////
    // draw functions
    /**
     * draw the ortho frame for all objects
     * @param ri
     */
    public final void drawAllPlanesOrthoFrames(IGraphicsAppInterface ri) {
        if(getFlag(srcGeomObjsAllBuiltIDX)){
            {for(SOM_GeomObj s : sourceGeomObjects){((Geom_PlaneSOMExample)s).drawOrthoFrame(ri);}}
        }
    }
    

    
}//class Geom_PlaneMapMgr
