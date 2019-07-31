package SOM_GeometryProj_PKG.geom_SOM_Mapping.procData_loaders;

import java.util.concurrent.ConcurrentSkipListMap;

import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_SphereSOMExample;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers.Geom_SphereMapMgr;
import base_SOM_Objects.som_examples.SOM_ExDataType;
import base_SOM_Objects.som_examples.SOM_Example;
import base_SOM_Objects.som_fileIO.SOM_ExCSVDataLoader;
import base_SOM_Objects.som_geom.SOM_GeomMapManager;

public class Geom_SphereCSVDataLoader extends SOM_ExCSVDataLoader {
	protected final SOM_ExDataType exType;
	public Geom_SphereCSVDataLoader(SOM_GeomMapManager _mapMgr, int _thdIDX, String _fileName, String _yStr, String _nStr, SOM_ExDataType _exType, ConcurrentSkipListMap<String, SOM_Example> _mapToAddTo) {
		super(_mapMgr, _thdIDX, _fileName, _yStr, _nStr, _mapToAddTo);
		type="sphereCSVDataLoader";
		exType=_exType;
	}

	@Override
	//Geom_LineSOMExample(SOM_GeomMapManager _mapMgr, SOM_ExDataType _exType, String _oid, String _csvDat)
	protected SOM_Example buildExample(String oid, String str) {		return new Geom_SphereSOMExample(((Geom_SphereMapMgr)mapMgr),exType, oid, str);}

}
