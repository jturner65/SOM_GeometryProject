package SOM_GeometryProj_PKG.geom_Utils.trainDataGen.callables;

import java.util.concurrent.ThreadLocalRandom;

import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_SphereSOMExample;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers.Geom_SphereMapMgr;
import SOM_GeometryProj_PKG.som_geom.SOM_GeomMapManager;
import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomExampleManager;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomObj;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomObjTypes;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomSmplDataForEx;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading.trainDataGen.SOM_GeomTrainExBuilder;
import base_SOM_Objects.som_examples.SOM_ExDataType;
import base_Utils_Objects.vectorObjs.myPointf;
import base_Utils_Objects.vectorObjs.myVectorf;

public class Geom_SphereTrainDatBuilder extends SOM_GeomTrainExBuilder {

	public Geom_SphereTrainDatBuilder(SOM_GeomMapManager _mapMgr, SOM_GeomExampleManager _exMgr,SOM_GeomSmplDataForEx[] _allExs, int[] _intVals) {
		super(_mapMgr, _exMgr, _allExs, _intVals, "Spheres", SOM_GeomObjTypes.sphere.getVal());
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
		myVectorf ac, ad;
		SOM_GeomSmplDataForEx c,d;
		int cIDX;
		do {
			cIDX = idxs2[0];
			while((cIDX==idxs2[0]) || (cIDX==idxs2[1])){	cIDX =rnd.nextInt(0,allExamples.length);}		
			c = allExamples[cIDX];
			ac = new myVectorf(a,c.getPoint());
			ac._normalize();			
		}while (Math.abs(ab._dot(ac))==1.0f);
		res[2]=c;
		myVectorf planeNorm = ab._cross(ac)._normalize();
		//now find d so that it does not line in plane of abc - vector from ab
		do {
			int dIDX = idxs2[0];
			while((dIDX==idxs2[0]) || (dIDX==idxs2[1])|| (dIDX==cIDX)){	dIDX =rnd.nextInt(0,allExamples.length);}	
			
			d = allExamples[rnd.nextInt(0,allExamples.length)];
			ad = new myVectorf(a,d.getPoint());
			ad._normalize();
		} while (ad._dot(planeNorm) == 0.0f);//if 0 then in plane (ortho to normal)
		res[3]=d;		
		return res;
	};

	@Override
	protected SOM_GeomObj _buildSingleObjectFromSamples(SOM_ExDataType _exDataType, SOM_GeomSmplDataForEx[] exAra, int idx) {
		String ID = "Sphere_Train_"+getObjID(idx);
		Geom_SphereSOMExample obj = new Geom_SphereSOMExample(((Geom_SphereMapMgr)mapMgr),animWin, _exDataType, ID, exAra, numExPerObj);
		//boolean passed = obj.testSphereConstruction(ctr, rad, 1.0f);
		return obj;
	}

}
