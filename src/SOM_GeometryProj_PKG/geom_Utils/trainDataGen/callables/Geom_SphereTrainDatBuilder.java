package SOM_GeometryProj_PKG.geom_Utils.trainDataGen.callables;

import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_SphereSOMExample;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.exampleManagers.Geom_SphereExManager;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers.Geom_SphereMapMgr;
import base_Math_Objects.MyMathUtils;
import base_Math_Objects.vectorObjs.floats.myPointf;
import base_Math_Objects.vectorObjs.floats.myVectorf;
import base_SOM_Objects.som_examples.enums.SOM_ExDataType;
import base_SOM_Objects.som_geom.SOM_GeomMapManager;
import base_SOM_Objects.som_geom.geom_examples.SOM_GeomObj;
import base_SOM_Objects.som_geom.geom_examples.SOM_GeomTrainingExUniqueID;
import base_SOM_Objects.som_geom.geom_utils.geom_objs.SOM_GeomSamplePointf;
import base_SOM_Objects.som_geom.geom_utils.geom_threading.trainDataGen.SOM_GeomTrainExBuilder;

public class Geom_SphereTrainDatBuilder extends SOM_GeomTrainExBuilder {

    public Geom_SphereTrainDatBuilder(SOM_GeomMapManager _mapMgr, Geom_SphereExManager _exMgr,SOM_GeomSamplePointf[] _allExs, int[] _intVals, SOM_GeomTrainingExUniqueID[] _idxsToUse) {
        super(_mapMgr, _exMgr, _allExs, _intVals, _idxsToUse);
    }
    
    /**
     * build determinants of minors for circle calc, using method of Determinants - 
     * Thanks Again Paul Bourke!! http://paulbourke.net/geometry/circlesphere/
     * @param pts 4 points of sphere
     * @param _ctr destination for derived center point
     * @return calculated radius
     */    
    private double findCenterAndRadFromPtsUsingDet(SOM_GeomSamplePointf[] pts, myPointf _ctr){
        double[][] ptsAsAra = new double[pts.length][];
        double[] ptSqMags = new double[pts.length];
        for(int i=0;i<ptsAsAra.length;++i) {
            ptsAsAra[i] = pts[i].asHAraPt_Dbl(); 
            ptSqMags[i] = 0.0f;
            for(int j=0;j<ptsAsAra[i].length-1; ++j) {ptSqMags[i] += ptsAsAra[i][j]*ptsAsAra[i][j];}//don't add 1 from homogeneous eqs
        }//homogeneous         
        
        //row minor
        double[][][] Minors = new double[5][][];
        for(int i=0;i<Minors.length;++i) {                    //copy all homogeneous point values into each minor
            double[][] tmpMinor = new double[4][];
            for(int p=0;p<ptsAsAra.length;++p) {            //per point
                double[] tmpRow = new double[4];
                for(int d=0;d<tmpRow.length;++d) {            tmpRow[d]=ptsAsAra[p][d];        }   // per dof            
                tmpMinor[p]=tmpRow;                        //row/col
            }
            Minors[i]=tmpMinor;
        }
        //Minor 11 is done (idx 0)
        double[][] tmpMinor;
        //set minor 1->5 first value properly - every minor has mag of each point as first col
        for(int i=1;i<Minors.length;++i) {
            tmpMinor = Minors[i];
            for(int row=0;row<tmpMinor.length;++row) {    tmpMinor[row][0] = ptSqMags[row];}
        }
        //Minor 12 is done (idx 1) (mag y z 1 per row) - all rest of minors have x_i as 2nd col value
        for(int i=2;i<Minors.length;++i) {
            tmpMinor = Minors[i];
            for(int row=0;row<tmpMinor.length;++row) {    tmpMinor[row][1] = ptsAsAra[row][0];}           
        }
        //Minor 13 is done (idx 2) (mag x z 1 per row)
        for(int i=3;i<Minors.length;++i) {
            tmpMinor = Minors[i];
            for(int row=0;row<tmpMinor.length;++row) {    tmpMinor[row][2] = ptsAsAra[row][1];}           
        }
        //Minor 14 is done (idx 3) (mag x y 1 per row) 
        //Minor 15 doesn't have 1 in final location
        for(int row=0;row<Minors[4].length;++row) {    Minors[4][row][3] = ptsAsAra[row][2];}  
        
        double[] dets = new double[5];
   
        for(int idx = 0; idx<dets.length; ++idx) {         dets[idx]= detMat(Minors[idx]);} 
        
        _ctr.set(.5f*(dets[1]/dets[0]), -.5f *(dets[2]/dets[0]), .5f*(dets[3]/dets[0]));
        
        double rad = Math.sqrt(_ctr.sqrDist(myPointf.ZEROPT) -(dets[4]/dets[0]));
        return rad;
    }//findCenterAndRadFromPtsUsingDet

    /**
     * calculates the determinant of a Matrix - moved from MyMathUtils for threading
     * @param M n x n matrix - don't over do it
     * @return
     */
    private double detMat(double[][] M){ 
        double sum=0, s;
        if(M.length==1){    return(M[0][0]); }
        for(int i=0;i < M.length;i++){                                                         
            double[][] minor= new double[M.length-1][M.length-1];
            for(int b=0;b<M.length;++b){
                if(b==i) {continue;}
                int bIdx = (b<i)? b : b-1;
                for(int a=1;a<M.length;++a){            minor[a-1][bIdx] = M[a][b];    }
            }    
            s = (i%2==0) ? 1.0f : -1.0f;
            sum += s * M[0][i] * (detMat(minor));                                         
        }
        return(sum); //returns determinant value. once stack is finished, returns final determinant.
    }//detMat
    
    /**
     * for lines just need 2 points; planes need 3 non-colinear points; spheres need 4 non-coplanar points, no 3 of which are colinear
     * @return
     */
    @Override
    protected final SOM_GeomSamplePointf[] genPtsForObj() {
        SOM_GeomSamplePointf[] res = new SOM_GeomSamplePointf[numExPerObj];
        //1st 2 points are always ok
        Integer[] idxs2 = genUniqueIDXs(2);
        for(int i=0;i<2;++i) {    res[i]=allExamples[idxs2[i]];}
        myPointf a =res[0];
        myVectorf ab = new myVectorf(a,res[1]);
        ab._normalize();
        myVectorf ac, ad;
        SOM_GeomSamplePointf c,d;
        int cIDX;
        do {
            cIDX = idxs2[0];
            while((cIDX==idxs2[0]) || (cIDX==idxs2[1])){    cIDX =MyMathUtils.randomInt(0,allExamples.length);}        
            c = allExamples[cIDX];
            ac = new myVectorf(a,c);
            ac._normalize();            
        }while (Math.abs(ab._dot(ac))==1.0f);
        res[2]=c;
        myVectorf planeNorm = ab._cross(ac)._normalize();
        //now find d so that it does not line in plane of abc - vector from ab
        do {
            int dIDX = idxs2[0];
            while((dIDX==idxs2[0]) || (dIDX==idxs2[1]) || (dIDX==cIDX)){    dIDX =MyMathUtils.randomInt(0,allExamples.length);}                
            d = allExamples[dIDX];
            ad = new myVectorf(a,d);
            ad._normalize();
        } while (ad._dot(planeNorm) == 0.0f);//if 0 then in plane (ortho to normal)
        res[3]=d;        
        return res;
    };
    
    
    @SuppressWarnings("unused")
    private void _dbgDispSphere(SOM_GeomSamplePointf[] exAra, int idx) {
        myPointf ctrLoc = new myPointf();
          double radius = findCenterAndRadFromPtsUsingDet(exAra, ctrLoc);        

        String tmp = "";
        for(int i=0;i<numExPerObj;++i) {
            tmp += exAra[i].toString() + " | ";
        }
        this.msgObj.dispInfoMessage("Geom_SphereTrainDatBuilder", "_buildSingleObjectFromSamples", "Idx : " + idx + " Sphere ctr : " + ctrLoc.toStrBrf()+" | rad : " +radius +" | " + tmp);    
    }

    @Override
    protected SOM_GeomObj _buildSingleObjectFromSamples(SOM_ExDataType _exDataType, SOM_GeomSamplePointf[] exAra, int idx) {
        String ID = "Sphere_Train_"+getObjID(idx);
        //_dbgDispSphere(exAra, idx);
        //Geom_SphereSOMExample obj = new Geom_SphereSOMExample(((Geom_SphereMapMgr)mapMgr), _exDataType, ID, exAra, numExPerObj, false, idx < mapMgr.getMaxNumExsToShow());
        //boolean passed = obj.testSphereConstruction(ctr, rad, 1.0f);
        return new Geom_SphereSOMExample(((Geom_SphereMapMgr)mapMgr), _exDataType, ID, exAra, numExPerObj, false, idx < mapMgr.getMaxNumExsToShow());
    }

}
