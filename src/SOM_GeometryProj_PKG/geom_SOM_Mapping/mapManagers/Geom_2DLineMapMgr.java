package SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers;

import java.util.TreeMap;

import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_2DLineSOMExample;
import SOM_GeometryProj_PKG.geom_ObjExamples.mapNodes.Geom_2DLineSOMMapNode;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.exampleManagers.Geom_2DLineExManager;
import SOM_GeometryProj_PKG.geom_Utils.Geom_SOMMseOvrDisp;
import SOM_GeometryProj_PKG.geom_Utils.geomGen.runners.Geom_2DLineObjBldrRunner;
import base_SOM_Objects.som_examples.SOM_ExDataType;
import base_SOM_Objects.som_examples.SOM_FtrDataType;
import base_SOM_Objects.som_examples.SOM_MapNode;
import base_SOM_Objects.som_geom.SOM_GeomMapManager;
import base_SOM_Objects.som_geom.geom_UI.SOM_AnimWorldWin;
import base_SOM_Objects.som_geom.geom_examples.SOM_GeomExampleManager;
import base_SOM_Objects.som_geom.geom_examples.SOM_GeomObj;
import base_SOM_Objects.som_geom.geom_utils.geom_objs.SOM_GeomObjTypes;
import base_SOM_Objects.som_geom.geom_utils.geom_threading.geomGen.SOM_GeomObjBldrRunner;
import base_SOM_Objects.som_geom.geom_utils.geom_threading.geomGen.SOM_GeomObjBldrTasks;
import base_SOM_Objects.som_ui.SOM_MseOvrDisplay;
import base_SOM_Objects.som_ui.win_disp_ui.SOM_MapUIWin;
import base_Utils_Objects.vectorObjs.Tuple;

public class Geom_2DLineMapMgr extends SOM_GeomMapManager {
	
	public static final int numFlags = numGeomBaseFlags;	
	//SOM_GeomMapManager(SOM_MapUIWin _win, SOM_AnimWorldWin _dispWin, float[] _dims, float[][] _worldBounds, TreeMap<String, Object> _argsMap, String _geomObjType)
	public Geom_2DLineMapMgr(SOM_MapUIWin _win, SOM_AnimWorldWin _dispWin, float[][] _worldBounds, TreeMap<String, Object> _argsMap) {
		super(_win, _dispWin, _worldBounds, _argsMap, SOM_GeomObjTypes.line_2D, Geom_2DLineSOMExample._numFtrs);
	}

	/**
	 * build the thread runner for this map manager that will manage the various tasks related to the geometric objects
	 * @return
	 */
	protected final SOM_GeomObjBldrRunner buildObjRunner() {
		return new Geom_2DLineObjBldrRunner(this, th_exec, buildEmptyObjArray(), false, new int[] {numObjsToBuild, numSamplesPerObj}, SOM_GeomObjBldrTasks.buildBaseObj);		
	}

	@Override
	protected SOM_GeomObj[] buildEmptyObjArray() {		return new Geom_2DLineSOMExample[numObjsToBuild];}
	
	/**
	 * build the example data mapper specific to instancing class for saving -training- data
	 * @return
	 */
	@Override
	protected final SOM_GeomExampleManager buildExampleDataMappers_Indiv(String _exMgrName) {return new Geom_2DLineExManager(this, SOM_GeomObjTypes.line_2D.toString(), "2D Line Geometric Objects", SOM_ExDataType.Training, false,_exMgrName);}
	/**
	 * send any instance-specific control/ui values to objRunners, based on task
	 */
	@Override
	protected final void execObjRunner_Pre_Indiv(SOM_GeomObjBldrTasks _task) {
		switch(_task) {
			case buildBaseObj 			: { break;}
			case regenSamplesBaseObj	: { break;}
			default : {break;}
		}
	}
	public final int getNumSamplesToBuildObject() {return 2;}

	
	/**
	 * build the example that represents the SOM data where the mouse is
	 */
	@Override
	protected final SOM_MseOvrDisplay buildMseOverExample() {return new Geom_SOMMseOvrDisp(this,0.0f);}

	@Override
	protected void buildValidationDataAra() {
	}

	@Override
	public SOM_MapNode buildMapNode(Tuple<Integer, Integer> mapLoc,  SOM_FtrDataType _ftrTypeUsedToTrain, String[] tkns) {return new Geom_2DLineSOMMapNode(this,mapLoc, _ftrTypeUsedToTrain, tkns);}	

	@Override
	public String getClassSegmentTitleString(int classID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCategorySegmentTitleString(int catID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected final int getNumGeomFlags_Indiv() {	return numFlags;}
	@Override
	protected void setGeomFlag_Indiv(int idx, boolean val) {
		switch (idx) {//special actions for each flag
			default : {break;}
		}
	}

}//classGeom_LineMapMgr
