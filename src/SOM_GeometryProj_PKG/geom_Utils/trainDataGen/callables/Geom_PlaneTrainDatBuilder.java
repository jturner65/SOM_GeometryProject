package SOM_GeometryProj_PKG.geom_Utils.trainDataGen.callables;

import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_PlaneSOMExample;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers.Geom_PlaneMapMgr;
import SOM_GeometryProj_PKG.som_geom.SOM_GeomMapManager;
import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomExampleManager;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomObj;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomObjTypes;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomSmplDataForEx;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading.trainDataGen.SOM_GeomTrainExBuilder;
import base_SOM_Objects.som_examples.SOM_ExDataType;
import base_Utils_Objects.vectorObjs.myPointf;
import base_Utils_Objects.vectorObjs.myVectorf;

public class Geom_PlaneTrainDatBuilder extends SOM_GeomTrainExBuilder {

	public Geom_PlaneTrainDatBuilder(SOM_GeomMapManager _mapMgr, SOM_GeomExampleManager _exMgr,	SOM_GeomSmplDataForEx[] _allExs, int[] _intVals) {
		super(_mapMgr, _exMgr, _allExs, _intVals, "Planes", SOM_GeomObjTypes.plane.getVal());
	}
	
	/**
	 * for lines just need 2 points; planes need 3 non-colinear points; spheres need 4 non-coplanar points, no 3 of which are colinear
	 * @return
	 */
	@Override
	protected final SOM_GeomSmplDataForEx[] genPtsForObj(ThreadLocalRandom rnd) {
		SOM_GeomSmplDataForEx[] res = new SOM_GeomSmplDataForEx[numExPerObj];
		//1st 2 points are always ok
		Integer[] idxs2 = genUniqueIDXs(2, rnd);
		for(int i=0;i<2;++i) {	res[i]=allExamples[idxs2[i]];}
		myPointf a =res[0].getPoint();
		myVectorf ab = new myVectorf(a,res[1].getPoint());
		ab._normalize();
		myVectorf ac;
		SOM_GeomSmplDataForEx c;
		do {
			int cIDX = idxs2[0];
			while((cIDX==idxs2[0]) || (cIDX==idxs2[1])){	cIDX =rnd.nextInt(0,allExamples.length);}		

			c = allExamples[cIDX];
			ac = new myVectorf(a,c.getPoint());
			ac._normalize();			
		}while (Math.abs(ab._dot(ac))==1.0f);
		res[2]=c;

		return res;
	};
	
	@Override
	protected SOM_GeomObj _buildSingleObjectFromSamples(SOM_ExDataType _exDataType, SOM_GeomSmplDataForEx[] exAra, int idx) {
		String ID = "Plane_Train_"+getObjID(idx);
		return new Geom_PlaneSOMExample(((Geom_PlaneMapMgr)mapMgr), animWin, _exDataType, ID, exAra, numExPerObj);
	}

}
