package SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers;

import java.util.TreeMap;

import SOM_GeometryProj_PKG.geom_Objects.SOM_Sphere;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.exampleManagers.Geom_SphereExManager;
import SOM_GeometryProj_PKG.geom_Utils.runners.Geom_SphereObjBldrRunner;
import SOM_GeometryProj_PKG.som_geom.SOM_GeomMapManager;
import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomExampleManager;
import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomMapNode;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomObj;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading.SOM_GeomObjBldrRunner;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading.SOM_GeomObjBldrTasks;
import base_SOM_Objects.som_examples.SOM_MapNode;
import base_SOM_Objects.som_ui.win_disp_ui.SOM_MapUIWin;
import base_UI_Objects.windowUI.myDispWindow;
import base_Utils_Objects.vectorObjs.Tuple;

public class Geom_SphereMapMgr extends SOM_GeomMapManager {
	
	public static final int numFlags = numBaseFlags;	

	/**
	 * min and max radius set from UI
	 */
	protected float minRad, maxRad;

	public Geom_SphereMapMgr(SOM_MapUIWin _win, float[] _dims, TreeMap<String, Object> _argsMap) {
		super(_win, _dims, _argsMap, "Spheres");
	}

	public Geom_SphereMapMgr(float[] _dims, TreeMap<String, Object> _argsMap) {
		this(null,_dims, _argsMap);
	}
	
	/**
	 * build the thread runner for this map manager that will manage the various tasks related to the geometric objects
	 * assign default task
	 * @return
	 */
	protected final SOM_GeomObjBldrRunner buildObjRunner() {
		return new Geom_SphereObjBldrRunner(this, th_exec, buildEmptyObjArray(), false, new int[] {numObjsToBuild, numSamplesPerObj}, worldBounds, SOM_GeomObjBldrTasks.buildObj);		
	}
	@Override
	protected SOM_GeomObj[] buildEmptyObjArray() {		return new SOM_Sphere[numObjsToBuild];}

	/**
	 * build the example data mapper specific to instancing class
	 * @return
	 */
	@Override
	protected final SOM_GeomExampleManager buildExampleDataMappers_Indiv() {return new Geom_SphereExManager(this, "Spheres", "Sphere Geometric Objects", useChiSqDist);}
	/**
	 * send any instance-specific control/ui values to objRunners
	 */
	@Override
	protected final void buildGeomExampleObjs_Indiv() {	((Geom_SphereObjBldrRunner)objRunner).setRadSpan(minRad, maxRad);}
	
	/**
	 * call from UI to set min and max radius
	 */
	public final void setMinMaxRad(float min, float max) {minRad = min; maxRad=max;}
	
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
