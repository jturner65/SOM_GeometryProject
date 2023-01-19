package SOM_GeometryProj_PKG.geom_SOM_Mapping.procData_loaders;

import java.util.concurrent.ConcurrentSkipListMap;

import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_PlaneSOMExample;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers.Geom_PlaneMapMgr;
import base_SOM_Objects.som_examples.base.SOM_Example;
import base_SOM_Objects.som_examples.enums.SOM_ExDataType;
import base_SOM_Objects.som_fileIO.SOM_ExCSVDataLoader;
import base_SOM_Objects.som_geom.SOM_GeomMapManager;

public class Geom_PlaneCSVDataLoader extends SOM_ExCSVDataLoader {
	protected final SOM_ExDataType exType;
	public Geom_PlaneCSVDataLoader(SOM_GeomMapManager _mapMgr, int _thdIDX, String _fileName, String _yStr, String _nStr, SOM_ExDataType _exType, ConcurrentSkipListMap<String, SOM_Example> _mapToAddTo) {
		super(_mapMgr, _thdIDX, _fileName, _yStr, _nStr, _mapToAddTo);
		type="planeCSVDataLoader";
		exType=_exType;
	}

	@Override
	//Geom_LineSOMExample(SOM_GeomMapManager _mapMgr, SOM_ExDataType _exType, String _oid, String _csvDat)
	protected SOM_Example buildExample(String oid, String str) {		return new Geom_PlaneSOMExample(((Geom_PlaneMapMgr)mapMgr),exType, oid, str);}

}
