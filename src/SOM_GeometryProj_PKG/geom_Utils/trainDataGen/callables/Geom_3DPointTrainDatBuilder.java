package SOM_GeometryProj_PKG.geom_Utils.trainDataGen.callables;


import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_3DPointSOMExample;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.exampleManagers.Geom_3DPointExManager;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers.Geom_3DPointMapMgr;
import base_SOM_Objects.som_examples.enums.SOM_ExDataType;
import base_SOM_Objects.som_geom.SOM_GeomMapManager;
import base_SOM_Objects.som_geom.geom_examples.SOM_GeomObj;
import base_SOM_Objects.som_geom.geom_examples.SOM_GeomTrainingExUniqueID;
import base_SOM_Objects.som_geom.geom_utils.geom_objs.SOM_GeomSamplePointf;
import base_SOM_Objects.som_geom.geom_utils.geom_threading.trainDataGen.SOM_GeomTrainExBuilder;

public class Geom_3DPointTrainDatBuilder extends SOM_GeomTrainExBuilder {

	public Geom_3DPointTrainDatBuilder(SOM_GeomMapManager _mapMgr, Geom_3DPointExManager _exMgr,SOM_GeomSamplePointf[] _allExs, int[] _intVals, SOM_GeomTrainingExUniqueID[] _idxsToUse) {
		super(_mapMgr, _exMgr, _allExs, _intVals, _idxsToUse);
	}

	@Override
	protected SOM_GeomSamplePointf[] genPtsForObj() {
		SOM_GeomSamplePointf[] res = new SOM_GeomSamplePointf[numExPerObj];
		Integer[] idxs = genUniqueIDXs(numExPerObj);
		for(int i=0;i<res.length;++i) {	res[i]=allExamples[idxs[i]];}		
		return res;
	}

	@Override
	protected SOM_GeomObj _buildSingleObjectFromSamples(SOM_ExDataType _exDataType, SOM_GeomSamplePointf[] exAra,
			int idx) {
		String ID = "3D_Point_Train_"+getObjID(idx);
		return new Geom_3DPointSOMExample((Geom_3DPointMapMgr)mapMgr,_exDataType, ID, exAra, numExPerObj, false, idx < mapMgr.getMaxNumExsToShow());	
	}

}//class Geom_3DPointTrainDatBuilder
