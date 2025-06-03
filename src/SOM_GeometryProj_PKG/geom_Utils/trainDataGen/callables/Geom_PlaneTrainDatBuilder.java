package SOM_GeometryProj_PKG.geom_Utils.trainDataGen.callables;

import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_PlaneSOMExample;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.exampleManagers.Geom_PlaneExManager;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers.Geom_PlaneMapMgr;
import base_Math_Objects.MyMathUtils;
import base_Math_Objects.vectorObjs.floats.myPointf;
import base_Math_Objects.vectorObjs.floats.myVectorf;
import base_SOM_Objects.som_examples.enums.SOM_ExDataType;
import base_SOM_Objects.som_geom.SOM_GeomMapManager;
import base_SOM_Objects.som_geom.geom_examples.SOM_GeomObj;
import base_SOM_Objects.som_geom.geom_examples.SOM_GeomTrainingExUniqueID;
import base_SOM_Objects.som_geom.geom_utils.geom_objs.SOM_GeomSamplePointf;
import base_SOM_Objects.som_geom.geom_utils.geom_threading.trainDataGen.SOM_GeomTrainExBuilder;

public class Geom_PlaneTrainDatBuilder extends SOM_GeomTrainExBuilder {

	public Geom_PlaneTrainDatBuilder(SOM_GeomMapManager _mapMgr, Geom_PlaneExManager _exMgr, SOM_GeomSamplePointf[] _allExs, int[] _intVals, SOM_GeomTrainingExUniqueID[] _idxsToUse) {
		super(_mapMgr, _exMgr, _allExs, _intVals, _idxsToUse);
	}
	
	/**
	 * for lines just need 2 points; planes need 3 non-colinear points; spheres need 4 non-coplanar points, no 3 of which are colinear
	 * @return
	 */
	@Override
	protected final SOM_GeomSamplePointf[] genPtsForObj() {
		SOM_GeomSamplePointf[] res = new SOM_GeomSamplePointf[numExPerObj];
		//1st 2 points are always ok
		Integer[] idxs2 = genUniqueIDXs(2);
		for(int i=0;i<2;++i) {	res[i]=allExamples[idxs2[i]];}
		myPointf a =res[0];
		myVectorf ab = new myVectorf(a,res[1]);
		ab._normalize();
		myVectorf ac;
		SOM_GeomSamplePointf c;
		do {
			int cIDX = idxs2[0];
			while((cIDX==idxs2[0]) || (cIDX==idxs2[1])){	cIDX =MyMathUtils.randomInt(0,allExamples.length);}		

			c = allExamples[cIDX];
			ac = new myVectorf(a,c);
			ac._normalize();			
		}while (Math.abs(ab._dot(ac))==1.0f);
		res[2]=c;

		return res;
	};
	
	@Override
	protected SOM_GeomObj _buildSingleObjectFromSamples(SOM_ExDataType _exDataType, SOM_GeomSamplePointf[] exAra, int idx) {
		String ID = "Plane_Train_"+getObjID(idx);
		return new Geom_PlaneSOMExample(((Geom_PlaneMapMgr)mapMgr), _exDataType, ID, exAra, numExPerObj, false, idx < mapMgr.getMaxNumExsToShow());
	}

}
