package SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers;

import java.util.TreeMap;

import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_SphereSOMExample;
import SOM_GeometryProj_PKG.geom_ObjExamples.mapNodes.Geom_SphereSOMMapNode;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.exampleManagers.Geom_SphereExManager;
import SOM_GeometryProj_PKG.geom_Utils.Geom_SOMMseOvrDisp;
import SOM_GeometryProj_PKG.geom_Utils.geomGen.runners.Geom_SphereObjBldrRunner;
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

public class Geom_SphereMapMgr extends SOM_GeomMapManager {
	
	public static final int numFlags = numGeomBaseFlags;	

	/**
	 * min and max radius set from UI
	 */
	protected float minRad, maxRad;

	public Geom_SphereMapMgr(SOM_MapUIWin _win,  SOM_AnimWorldWin _dispWin, float[][] _worldBounds, TreeMap<String, Object> _argsMap) {
		super(_win, _dispWin, _worldBounds, _argsMap, SOM_GeomObjTypes.sphere, Geom_SphereSOMExample._numFtrs);
	}

	/**
	 * build the thread runner for this map manager that will manage the various tasks related to the geometric objects
	 * assign default task
	 * @return
	 */
	@Override
	protected final SOM_GeomObjBldrRunner buildObjRunner() {
		return new Geom_SphereObjBldrRunner(this, th_exec, buildEmptyObjArray(), false, new int[] {numObjsToBuild, numSamplesPerObj}, SOM_GeomObjBldrTasks.buildBaseObj);		
	}

	@Override
	protected final SOM_GeomObj[] buildEmptyObjArray() {		return new Geom_SphereSOMExample[numObjsToBuild];}
	

	/**
	 * build the example data mapper specific to instancing class
	 * @return
	 */
	@Override
	protected final SOM_GeomExampleManager buildExampleDataMappers_Indiv(String _exMgrName) {return new Geom_SphereExManager(this, SOM_GeomObjTypes.sphere.toString(), "Sphere Geometric Objects",  SOM_ExDataType.Training, false, _exMgrName);}
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
		((Geom_SphereObjBldrRunner)objRunner).setRadSpan(minRad, maxRad);
	}
	public final int getNumSamplesToBuildObject() {return 4;}
	
	/**
	 * build the example that represents the SOM data where the mouse is
	 */
	@Override
	protected final SOM_MseOvrDisplay buildMseOverExample() {return new Geom_SOMMseOvrDisp(this,0.0f);}
	
	/**
	 * call from UI to set min and max radius
	 */
	public final void setMinMaxRad(float min, float max) {minRad = min; maxRad=max;}


	@Override
	protected void buildValidationDataAra() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public SOM_MapNode buildMapNode(Tuple<Integer, Integer> mapLoc,  SOM_FtrDataType _ftrTypeUsedToTrain, String[] tkns) {return new Geom_SphereSOMMapNode(this,mapLoc, _ftrTypeUsedToTrain, tkns);}	

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
	////////////////////////
	// draw functions
	


}//class Geom_SphereMapMgr
