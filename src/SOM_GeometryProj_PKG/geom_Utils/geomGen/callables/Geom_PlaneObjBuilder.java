package SOM_GeometryProj_PKG.geom_Utils.geomGen.callables;


import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_PlaneSOMExample;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers.Geom_PlaneMapMgr;
import base_Math_Objects.vectorObjs.floats.myPointf;
import base_SOM_Objects.som_examples.enums.SOM_ExDataType;
import base_SOM_Objects.som_geom.geom_examples.SOM_GeomObj;
import base_SOM_Objects.som_geom.geom_utils.geom_objs.SOM_GeomSamplePointf;
import base_SOM_Objects.som_geom.geom_utils.geom_threading.geomGen.SOM_GeomObjBldrTasks;
import base_SOM_Objects.som_geom.geom_utils.geom_threading.geomGen.SOM_GeomObjBuilder;

public class Geom_PlaneObjBuilder extends SOM_GeomObjBuilder {

    public Geom_PlaneObjBuilder(Geom_PlaneMapMgr _mapMgr, SOM_GeomObj[] _objArray, int[] _intVals, SOM_GeomObjBldrTasks _taskToDo) {
        super(_mapMgr,  _objArray,_intVals, _taskToDo);
    }

    @Override
    protected SOM_GeomObj _buildSingleObject(SOM_ExDataType _exDataType, int idx) {
//        /**
//         * Constructor for line object
//         * @param _mapMgr owning som map manager
//         * @param _ptsOnPlane : non-colinear points on plane : idx 0 is "ctr" - point closest to 0,0,0 for plane
//          * @param _basis :  basis vectors for plane - idx 0 is normal
//         * @param _numSmplPts : # of sample points to build for this object
//         * @param _worldBnds 4 points that bound the plane for display purposes
//         */    
//        public SOM_Plane(SOM_GeomMapManager _mapMgr, myPointf[] _ptsOnPlane, myVectorf[] _basis, int _numSmplPts, float[][] _worldBounds) {
        //points must not be colinear
        myPointf[] planePts = getRandPlanePoints();
        String ID = "Plane_"+String.format("%05d", idx);
        SOM_GeomSamplePointf[] pts = new SOM_GeomSamplePointf[planePts.length];
        for(int i=0;i<pts.length; ++i) {pts[i] = new SOM_GeomSamplePointf(planePts[i],ID+"_gen_pt_"+i, null);}
        
        //SOM_GeomSmplDataForEx[] _srcSmpls = buildSrcGeomSmplAra(null, pts);
        
        //(SOM_GeomMapManager _mapMgr, SOM_AnimWorldWin _animWin, SOM_ExDataType _exType, String _id, SOM_GeomSmplForSOMExample[] _srcSmpls, int _numSmplPts, float[][] _worldBounds)
        //animWin, _exDataType, ID, _srcSmpls, numSmplsPerObj, worldBounds);
        return new Geom_PlaneSOMExample(((Geom_PlaneMapMgr)mapMgr), _exDataType, ID, pts, numSmplsPerObj, true, true);
    }

}//class Geom_PlaneObjBuilder
