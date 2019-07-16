package SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers;

import java.util.TreeMap;

import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_PlaneSOMExample;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.exampleManagers.Geom_PlaneExManager;
import SOM_GeometryProj_PKG.geom_Utils.runners.Geom_PlaneObjBldrRunner;
import SOM_GeometryProj_PKG.som_geom.SOM_GeomMapManager;
import SOM_GeometryProj_PKG.som_geom.geom_UI.SOM_AnimWorldWin;
import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomExample;
import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomExampleManager;
import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomMapNode;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomObj;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading.SOM_GeomObjBldrRunner;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading.SOM_GeomObjBldrTasks;
import base_SOM_Objects.som_examples.SOM_ExDataType;
import base_SOM_Objects.som_examples.SOM_MapNode;
import base_SOM_Objects.som_ui.win_disp_ui.SOM_MapUIWin;
import base_Utils_Objects.vectorObjs.Tuple;

public class Geom_PlaneMapMgr extends SOM_GeomMapManager {
	
	public static final int numFlags = numBaseFlags;	
	

	//(SOM_MapUIWin _win, SOM_AnimWorldWin _dispWin, float[] _dims, float[][] _worldBounds, TreeMap<String, Object> _argsMap)
	public Geom_PlaneMapMgr(SOM_MapUIWin _win,  SOM_AnimWorldWin _dispWin, float[] _dims, float[][] _worldBounds, TreeMap<String, Object> _argsMap) {
		super(_win, _dispWin, _dims,  _worldBounds,  _argsMap, "Planes");
	}

	/**
	 * build the thread runner for this map manager that will manage the various tasks related to the geometric objects
	 * @return
	 */
	protected final SOM_GeomObjBldrRunner buildObjRunner() {
		return new Geom_PlaneObjBldrRunner(this, th_exec, buildEmptyObjArray(), false, new int[] {numObjsToBuild, numSamplesPerObj}, worldBounds, SOM_GeomObjBldrTasks.buildBaseObj);		
	}
	@Override
	protected final SOM_GeomObj[] buildEmptyObjArray() {		return new Geom_PlaneSOMExample[numObjsToBuild];}
	
	/**
	 * send any instance-specific control/ui values to objRunners
	 */
	@Override
	protected final void buildGeomExampleObjs_Indiv() {}


	/**
	 * build the example data mapper specific to instancing class
	 * @return
	 */
	@Override
	protected final SOM_GeomExampleManager buildExampleDataMappers_Indiv() {return new Geom_PlaneExManager(this, "Planes", "Planar Geometric Objects", useChiSqDist);}

	@Override
	protected Integer[] getAllClassLabels() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getClassSegMappingDescrStr() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Integer[] getAllCategoryLabels() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getCategorySegMappingDescrStr() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void buildValidationDataAra() {
		// TODO Auto-generated method stub

	}

	@Override
	public SOM_MapNode buildMapNode(Tuple<Integer, Integer> mapLoc, String[] tkns) {return new SOM_GeomMapNode(this,mapLoc, tkns);}	

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
	protected final int getNumFlags() {	return numFlags;}
	@Override
	protected void setFlag_Indiv(int idx, boolean val) {
		switch (idx) {//special actions for each flag
			default : {break;}
		}
	}

}
