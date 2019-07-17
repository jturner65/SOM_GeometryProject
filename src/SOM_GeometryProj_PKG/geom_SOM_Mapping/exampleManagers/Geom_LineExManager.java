package SOM_GeometryProj_PKG.geom_SOM_Mapping.exampleManagers;

import java.util.ArrayList;

import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_LineSOMExample;
import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_SphereSOMExample;
import SOM_GeometryProj_PKG.som_geom.SOM_GeomMapManager;
import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomExampleManager;
import base_SOM_Objects.SOM_MapManager;
import base_SOM_Objects.som_examples.SOM_ExDataType;
import base_SOM_Objects.som_examples.SOM_Example;

public class Geom_LineExManager extends SOM_GeomExampleManager {

	public Geom_LineExManager(SOM_MapManager _mapMgr, String _exName, String _longExampleName,  SOM_ExDataType _curDataType, boolean _shouldValidate) {
		super(_mapMgr, _exName, _longExampleName,  _curDataType,  _shouldValidate);
	}

	@Override
	protected void reset_Priv() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void buildFtrVec_Priv() {
		// TODO Auto-generated method stub

	}
	@Override
	protected SOM_Example[] noValidateBuildExampleArray() {return (Geom_LineSOMExample[])(exampleMap.values().toArray(new Geom_LineSOMExample[0]));		}
	@Override
	protected SOM_Example[] castArray(ArrayList<SOM_Example> tmpList) {return (Geom_LineSOMExample[])(tmpList.toArray(new Geom_LineSOMExample[0]));		}

	@Override
	protected void buildAfterAllFtrVecsBuiltStructs_Priv() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void buildMTLoader(String[] loadSrcFNamePrefixAra, int numPartitions) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void buildSTLoader(String[] loadSrcFNamePrefixAra, int numPartitions) {
		// TODO Auto-generated method stub

	}


	@Override
	protected SOM_Example buildSingleExample(String _oid, String _str) {
		return new Geom_LineSOMExample((SOM_GeomMapManager)mapMgr, animWin, curDataType, _oid, _str, ((SOM_GeomMapManager)mapMgr).getWorldBounds());
	}

}//class Geom_LineSOMExampleManager
