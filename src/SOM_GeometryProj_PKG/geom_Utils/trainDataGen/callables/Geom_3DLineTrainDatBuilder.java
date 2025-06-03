package SOM_GeometryProj_PKG.geom_Utils.trainDataGen.callables;

import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_3DLineSOMExample;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.exampleManagers.Geom_3DLineExManager;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers.Geom_3DLineMapMgr;
import base_SOM_Objects.som_examples.enums.SOM_ExDataType;
import base_SOM_Objects.som_geom.SOM_GeomMapManager;
import base_SOM_Objects.som_geom.geom_examples.SOM_GeomObj;
import base_SOM_Objects.som_geom.geom_examples.SOM_GeomTrainingExUniqueID;
import base_SOM_Objects.som_geom.geom_utils.geom_objs.SOM_GeomSamplePointf;
import base_SOM_Objects.som_geom.geom_utils.geom_threading.trainDataGen.SOM_GeomTrainExBuilder;

public class Geom_3DLineTrainDatBuilder extends SOM_GeomTrainExBuilder {

	public Geom_3DLineTrainDatBuilder(SOM_GeomMapManager _mapMgr, Geom_3DLineExManager _exMgr,SOM_GeomSamplePointf[] _allExs, int[] _intVals, SOM_GeomTrainingExUniqueID[] _idxsToUse) {
		super(_mapMgr, _exMgr, _allExs, _intVals, _idxsToUse);
	}

	/**
	 * for lines just need 2 points; planes need 3 non-collinear points; spheres need 4 non-coplanar points, no 3 of which are collinear
	 * @return
	 */
	@Override
	protected final SOM_GeomSamplePointf[] genPtsForObj() {
		SOM_GeomSamplePointf[] res = new SOM_GeomSamplePointf[numExPerObj];
		Integer[] idxs = genUniqueIDXs(numExPerObj);
		for(int i=0;i<res.length;++i) {	res[i]=allExamples[idxs[i]];}
		return res;
	};

	@Override
	protected SOM_GeomObj _buildSingleObjectFromSamples(SOM_ExDataType _exDataType, SOM_GeomSamplePointf[] exAra, int idx) {
		String ID = "3D_Line_Train_"+getObjID(idx);
		return new Geom_3DLineSOMExample(((Geom_3DLineMapMgr)mapMgr),_exDataType, ID, exAra, numExPerObj, false, idx < mapMgr.getMaxNumExsToShow());		
	}

}
