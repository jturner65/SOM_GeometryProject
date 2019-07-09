package SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers;

import java.util.TreeMap;

import SOM_GeometryProj_PKG.geom_SOM_Mapping.exampleManagers.Geom_SphereExManager;
import SOM_GeometryProj_PKG.som_geom.SOM_GeomMapManager;
import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomExampleManager;
import base_SOM_Objects.som_ui.win_disp_ui.SOM_MapUIWin;
import base_UI_Objects.my_procApplet;

public class Geom_SphereMapMgr extends SOM_GeomMapManager {

	public Geom_SphereMapMgr(SOM_MapUIWin _win, float[] _dims, TreeMap<String, Object> _argsMap) {
		super(_win, _dims, _argsMap, "Spheres");
	}

	public Geom_SphereMapMgr(float[] _dims, TreeMap<String, Object> _argsMap) {
		this(null,_dims, _argsMap);
	}

	/**
	 * build the example data mapper specific to instancing class
	 * @return
	 */
	@Override
	protected final SOM_GeomExampleManager buildExampleDataMappers_Indiv() {return new Geom_SphereExManager(this, "Spheres", "Sphere Geometric Objects", useChiSqDist);}

	@Override
	public void buildGeomExampleObjs() {
		// TODO Auto-generated method stub

	}

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
	protected float getPreBuiltMapInfoDetail(my_procApplet pa, String[] str, int i, float yOff, boolean isLoaded) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected float drawResultBarPriv1(my_procApplet pa, float yOff) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected float drawResultBarPriv2(my_procApplet pa, float yOff) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected float drawResultBarPriv3(my_procApplet pa, float yOff) {
		// TODO Auto-generated method stub
		return 0;
	}

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
	protected int getNumFlags() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void setFlag_Indiv(int idx, boolean val) {
		// TODO Auto-generated method stub

	}

}
