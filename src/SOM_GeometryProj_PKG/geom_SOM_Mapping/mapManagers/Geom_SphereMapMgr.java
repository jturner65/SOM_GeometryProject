package SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers;

import java.util.TreeMap;

import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_SphereSOMExample;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.exampleManagers.Geom_SphereExManager;
import SOM_GeometryProj_PKG.geom_Utils.runners.Geom_SphereObjBldrRunner;
import SOM_GeometryProj_PKG.som_geom.SOM_GeomMapManager;
import SOM_GeometryProj_PKG.som_geom.geom_UI.SOM_AnimWorldWin;
import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomExampleManager;
import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomFtrBndMon;
import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomMapNode;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomObj;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading.SOM_GeomObjBldrRunner;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading.SOM_GeomObjBldrTasks;
import base_SOM_Objects.som_examples.SOM_ExDataType;
import base_SOM_Objects.som_examples.SOM_MapNode;
import base_SOM_Objects.som_ui.win_disp_ui.SOM_MapUIWin;
import base_Utils_Objects.vectorObjs.Tuple;
import base_Utils_Objects.vectorObjs.myPoint;

public class Geom_SphereMapMgr extends SOM_GeomMapManager {
	
	public static final int numFlags = numGeomBaseFlags;	

	/**
	 * min and max radius set from UI
	 */
	protected float minRad, maxRad;

	public Geom_SphereMapMgr(SOM_MapUIWin _win,  SOM_AnimWorldWin _dispWin, float[] _dims, float[][] _worldBounds, TreeMap<String, Object> _argsMap) {
		super(_win, _dispWin, _dims, _worldBounds, _argsMap, "Spheres");
	}

	/**
	 * build the thread runner for this map manager that will manage the various tasks related to the geometric objects
	 * assign default task
	 * @return
	 */
	@Override
	protected final SOM_GeomObjBldrRunner buildObjRunner() {
		return new Geom_SphereObjBldrRunner(this, th_exec, buildEmptyObjArray(), false, new int[] {numObjsToBuild, numSamplesPerObj}, worldBounds, SOM_GeomObjBldrTasks.buildBaseObj);		
	}
	/**
	 * build the training data bounds manager
	 */
	@Override
	protected final SOM_GeomFtrBndMon buildTrainDatFtrBndMgr() {
		//use # of ftrs mapped 
		return new SOM_GeomFtrBndMon(Geom_SphereSOMExample._numFtrs);
	};
	@Override
	protected final SOM_GeomObj[] buildEmptyObjArray() {		return new Geom_SphereSOMExample[numObjsToBuild];}
	

	/**
	 * build the example data mapper specific to instancing class
	 * @return
	 */
	@Override
	protected final SOM_GeomExampleManager buildExampleDataMappers_Indiv() {return new Geom_SphereExManager(this, "Spheres", "Sphere Geometric Objects",  SOM_ExDataType.Training, false);}
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
	protected final int getNumGeomFlags_Indiv() {	return numFlags;}
	@Override
	protected void setGeomFlag_Indiv(int idx, boolean val) {
		switch (idx) {//special actions for each flag
			default : {break;}
		}
	}
	////////////////////////
	// draw functions
	

	////////////////////////
	// mouse handling
	/**
	 * check mouse over/click in experiment; if btn == -1 then mouse over
	 * @param msx
	 * @param msy
	 * @param mseClckInWorld
	 * @param btn
	 * @return
	 */
	public final boolean checkMouseClick(int msx, int msy, myPoint mseClckInWorld, int btn) {
		return false;
	}
	/**
	 * check mouse drag/move in experiment; if btn == -1 then mouse over
	 * @param msx
	 * @param msy
	 * @param mseClckInWorld
	 * @param btn
	 * @return
	 */
	public final boolean checkMouseDragMove(int msx, int msy, myPoint mseClckInWorld, int btn){
		return false;
	}
	/**
	 * notify all exps that mouse has been released
	 */
	public final void setMouseRelease() {
		
	}
	
}//class Geom_SphereMapMgr
