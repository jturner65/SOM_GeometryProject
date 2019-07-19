package SOM_GeometryProj_PKG.geom_Utils.trainDataGen.callables;

import java.util.concurrent.ThreadLocalRandom;

import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_LineSOMExample;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers.Geom_LineMapMgr;
import SOM_GeometryProj_PKG.som_geom.SOM_GeomMapManager;
import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomExampleManager;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomObj;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomObjTypes;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomSamplePointf;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomSmplDataForEx;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading.trainDataGen.SOM_GeomTrainExBuilder;
import base_SOM_Objects.som_examples.SOM_ExDataType;
import base_Utils_Objects.vectorObjs.myPointf;

public class Geom_LineTrainDatBuilder extends SOM_GeomTrainExBuilder {

	public Geom_LineTrainDatBuilder(SOM_GeomMapManager _mapMgr, SOM_GeomExampleManager _exMgr,SOM_GeomSmplDataForEx[] _allExs, int[] _intVals) {
		super(_mapMgr, _exMgr, _allExs, _intVals, "Lines", SOM_GeomObjTypes.line.getVal());
	}

	/**
	 * for lines just need 2 points; planes need 3 non-colinear points; spheres need 4 non-coplanar points, no 3 of which are colinear
	 * @return
	 */
	protected final SOM_GeomSmplDataForEx[] genPtsForObj(ThreadLocalRandom rnd) {
		SOM_GeomSmplDataForEx[] res = new SOM_GeomSmplDataForEx[numExPerObj];
		Integer[] idxs = genUniqueIDXs(numExPerObj, rnd);
		for(int i=0;i<res.length;++i) {	res[i]=allExamples[idxs[i]];}
		return res;
	};

	@Override
	protected SOM_GeomObj _buildSingleObjectFromSamples(SOM_ExDataType _exDataType, SOM_GeomSmplDataForEx[] exAra, int idx) {
		String ID = "Line_Train_"+getObjID(idx);
		Geom_LineSOMExample line = new Geom_LineSOMExample(((Geom_LineMapMgr)mapMgr), animWin, _exDataType, ID, exAra, numExPerObj);
		return line;
		
	}

}
