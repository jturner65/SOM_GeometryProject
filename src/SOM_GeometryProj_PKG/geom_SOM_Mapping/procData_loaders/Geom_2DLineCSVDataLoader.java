package SOM_GeometryProj_PKG.geom_SOM_Mapping.procData_loaders;

import java.util.concurrent.ConcurrentSkipListMap;

import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_2DLineSOMExample;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers.Geom_2DLineMapMgr;
import base_SOM_Objects.som_examples.base.SOM_Example;
import base_SOM_Objects.som_examples.enums.SOM_ExDataType;
import base_SOM_Objects.som_fileIO.SOM_ExCSVDataLoader;
import base_SOM_Objects.som_geom.SOM_GeomMapManager;

public class Geom_2DLineCSVDataLoader extends SOM_ExCSVDataLoader {
	protected final SOM_ExDataType exType;
	public Geom_2DLineCSVDataLoader(SOM_GeomMapManager _mapMgr, int _thdIDX, String _fileName, String _yStr, String _nStr, SOM_ExDataType _exType, ConcurrentSkipListMap<String, SOM_Example> _mapToAddTo) {
		super(_mapMgr, _thdIDX, _fileName, _yStr, _nStr, _mapToAddTo);
		type="line2DCSVDataLoader";
		exType=_exType;
	}

	@Override
	protected SOM_Example buildExample(String oid, String str) {		return new Geom_2DLineSOMExample((Geom_2DLineMapMgr)mapMgr,exType, oid, str);}

}
