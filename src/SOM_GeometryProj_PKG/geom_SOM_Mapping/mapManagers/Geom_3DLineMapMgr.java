package SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers;

import java.util.Map;

import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_3DLineSOMExample;
import SOM_GeometryProj_PKG.geom_ObjExamples.mapNodes.Geom_3DLineSOMMapNode;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.exampleManagers.Geom_3DLineExManager;
import SOM_GeometryProj_PKG.geom_Utils.geomGen.runners.Geom_3DLineObjBldrRunner;
import base_Math_Objects.vectorObjs.tuples.Tuple;
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


public class Geom_3DLineMapMgr extends SOM_GeomMapManager {
    
    public static final int numFlags = numGeomBaseFlags;    
    //SOM_GeomMapManager(SOM_MapUIWin _win, SOM_AnimWorldWin _dispWin, float[] _dims, float[][] _worldBounds, TreeMap<String, Object> _argsMap, String _geomObjType)
    public Geom_3DLineMapMgr(SOM_MapUIWin _win, SOM_AnimWorldWin _dispWin, float[][] _worldBounds, Map<String, Object> _argsMap) {
        super(_win, _dispWin, _worldBounds, _argsMap, SOM_GeomObjTypes.line_3D, Geom_3DLineSOMExample._numFtrs);
    }

    /**
     * build the thread runner for this map manager that will manage the various tasks related to the geometric objects
     * @return
     */
    protected final SOM_GeomObjBldrRunner buildObjRunner() {
        return new Geom_3DLineObjBldrRunner(this, th_exec, buildEmptyObjArray(), false, new int[] {numObjsToBuild, numSamplesPerObj}, SOM_GeomObjBldrTasks.buildBaseObj);        
    }

    @Override
    protected final SOM_GeomObj[] buildEmptyObjArray() {        return new Geom_3DLineSOMExample[numObjsToBuild];}
    
    /**
     * build the example data mapper specific to instancing class for saving -training- data - this is called before geomObjDataType is set, so need to manually set descriptor string
     * @return
     */
    @Override
    protected final SOM_GeomExampleManager buildExampleDataMappers_Indiv(String _exMgrName) {return new Geom_3DLineExManager(this, SOM_GeomObjTypes.line_3D.toString(), "3D Line Geometric Objects", SOM_ExDataType.Training, false,_exMgrName);}
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
    @Override
    public final int getNumSamplesToBuildObject() {return Geom_3DLineSOMExample._numSrcPts;}
        
    /**
     * build the example that represents the SOM data where the mouse is
     */
    @Override
    protected final SOM_MseOvrDisplay buildMseOverExample() {return new Geom_SOMMseOvrDisp(this,0.0f);}
    @Override
    protected final void buildValidationDataAra() {}
    @Override
    public final SOM_MapNode buildMapNode(Tuple<Integer, Integer> mapLoc,  SOM_FtrDataType _ftrTypeUsedToTrain, String[] tkns) {
        return new Geom_3DLineSOMMapNode(this,mapLoc, _ftrTypeUsedToTrain, tkns);
    }    
    @Override
    public final String getClassSegmentTitleString(int classID) {        return null;}
    @Override
    public final String getCategorySegmentTitleString(int catID) {    return null;}
    /**
     * The maximum number of training examples to draw to prevent lag/crashing/overflow. 
     * The purpose of showing these is to illustrate the random distribution of examples.
     */
    @Override
    public int getMaxNumExsToShow() {return 10000;}
    @Override
    protected final int getNumGeomFlags_Indiv() {    return numFlags;}
    @Override
    protected void setGeomFlag_Indiv(int idx, boolean val) {
        switch (idx) {//special actions for each flag
            default : {break;}
        }
    }

}//class Geom_3DLineMapMgr
