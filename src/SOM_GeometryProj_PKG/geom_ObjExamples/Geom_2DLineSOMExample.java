package SOM_GeometryProj_PKG.geom_ObjExamples;

import java.util.TreeMap;

import SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers.Geom_2DLineMapMgr;
import base_Math_Objects.MyMathUtils;
import base_Math_Objects.vectorObjs.floats.myPointf;
import base_Math_Objects.vectorObjs.floats.myVectorf;
import base_Render_Interface.IGraphicsAppInterface;
import base_SOM_Objects.som_examples.enums.SOM_ExDataType;
import base_SOM_Objects.som_geom.SOM_GeomMapManager;
import base_SOM_Objects.som_geom.geom_UI.SOM_AnimWorldWin;
import base_SOM_Objects.som_geom.geom_examples.SOM_GeomLineObj;
import base_SOM_Objects.som_geom.geom_examples.SOM_GeomMapNode;
import base_SOM_Objects.som_geom.geom_utils.geom_objs.SOM_GeomObjDrawType;
import base_SOM_Objects.som_geom.geom_utils.geom_objs.SOM_GeomObjTypes;
import base_SOM_Objects.som_geom.geom_utils.geom_objs.SOM_GeomSamplePointf;

public class Geom_2DLineSOMExample extends SOM_GeomLineObj {
    private static int IDGen = 0;    
    /**
     * string array denoting names of features
     */
    public static final String[] ftrNames = {"Dir x","Dir y","Origin x","Origin y"};
    /**
     * feature vector size for this object : 3d point + 3d line
     */
    public static final int _numFtrs = ftrNames.length;
    
    /**
     * Constructor for 2d line object shared functionality
     *     
     * @param _mapMgr : owning map manager
     * @param _exType : whether this is training/testing/validation etc.
     * @param _oid : pre-defined string ID put in SOM_Example OID field
     * @param _srcSmpls : the points and owning objects that make up the minimally defining set of points for this object.  If src objects are null, then this object is a foundation/source object
     * @param _numSmplPts : # of sample points to build for this object TODO move to post-construction process
     * @param _shouldBuildSamples : whether we should build samples for this object
     * @param _shouldBuildVisRep : whether we should pre-build a mesh representation of this object
     */
    public Geom_2DLineSOMExample(Geom_2DLineMapMgr _mapMgr, SOM_ExDataType _exType, String _id, SOM_GeomSamplePointf[] _srcSmpls, int _numSmplPts, boolean _shouldBuildSamples, boolean _shouldBuildVisRep) {
        super(_mapMgr, _exType, _id, _srcSmpls, SOM_GeomObjTypes.line_2D, _numSmplPts, _shouldBuildSamples,_shouldBuildVisRep);
    }//ctor        
    
    /**
     * Constructor to build a 2d line based on csv data
     * @param _mapMgr : owning map manager
     * @param _exType : whether this is training/testing/validation etc.
     * @param _oid : pre-defined string ID put in SOM_Example OID field
     * @param _csvDat : String from CSV describing object
     */
    public Geom_2DLineSOMExample(Geom_2DLineMapMgr _mapMgr, SOM_ExDataType _exType, String _oid, String _csvDat) {
        super(_mapMgr, _exType, _oid, _csvDat, SOM_GeomObjTypes.line_2D);
    }//csv ctor
    
    /**
     * ctor to build a 2d line corresponding to a map node
     * @param _mapMgr
     * @param _mapNode
     */
    public Geom_2DLineSOMExample(Geom_2DLineMapMgr _mapMgr, SOM_GeomMapNode _mapNode) {
        super(_mapMgr, _mapNode, SOM_GeomObjTypes.line_2D);
    }//bmu ctor
    
    public Geom_2DLineSOMExample(Geom_2DLineSOMExample _otr) {
        super(_otr);
    }//copy ctor
        
    /**
     * build the normal to this line from dir vector
     * @return
     */
    protected void buildNormFromDir() {
        norm = new myVectorf(dir.y, -dir.x, 0.0f);
    }
    
    protected myVectorf _buildDir() {
        myVectorf _dir = new myVectorf(getSrcPts()[0],getSrcPts()[1]);
        //z is always 0 - making this in 2 d
        _dir.z = 0;
        _dir._normalize();    
        if((_dir.y < 0.0) || ((_dir.y == 0.0) && (_dir.x < 0))) {    _dir._mult(-1.0);    }        //force all dir vectors to have the same "polarity" so 2 points always form the same line
        return _dir;
    }
    
    /**
     * convert a world location within the bounded cube region to be a 4-int color array
     */
    @Override
    public final int[] getClrFromWorldLoc(myPointf pt){return getClrFromWorldLoc_2D(pt, dir,((SOM_GeomMapManager) mapMgr).getWorldBounds());}//getClrFromWorldLoc

    /**
     * For every bound in line's space, set t value in bound's ortho dim
     * @return
     */
    @Override
    protected final TreeMap<Float, Integer> buildWorldBounds(float[] mins){
        TreeMap<Float, Integer> tmpBnds = new TreeMap<Float, Integer>();
        float curMax;
        float[] ptA_ara = getSrcPts()[0].asArray(), dirAra = dir.asArray();
        float [][] worldBounds = ((SOM_GeomMapManager) mapMgr).getWorldBounds();
        for(int i=0;i<mins.length;++i) {
            tmpBnds.put((mins[i] - ptA_ara[i])/dirAra[i], 2*i);
            curMax=worldBounds[0][i] + worldBounds[1][i];
            tmpBnds.put((curMax - ptA_ara[i])/dirAra[i], 1 + 2*i);
        }
        //want 2 points closest to 0 - 2 will be negative, and 2 will be positive if a is within bounds
        tmpBnds.remove(tmpBnds.firstKey());
        tmpBnds.remove(tmpBnds.lastKey());
        return tmpBnds;
    }
    
    
    /**
     * find t value for point on line - expects point to be on line!
     * @param pt
     * @return
     */
    @Override
    public final float getTForPointOnLine(myPointf pt) {
        //pt  == origin  + t * dir
        // -> t = (pt.x - origin.x)/dir.x 
        float t = 0.0f;
        if(Math.abs(dir.x)> MyMathUtils.EPS) {    t = (pt.x - getSrcPts()[0].x)/dir.x;} 
        else {                                    t = (pt.y - getSrcPts()[0].y)/dir.y;}//vertical line            
        return t;
    }
    
    ////////////////////////////
    // feature functionality (inherited from SOM_Example
    
    /**
     * build an array of source points from the characteristic features of the source map node
     */
    @Override
    protected final SOM_GeomSamplePointf[] buildSrcPtsFromBMUMapNodeFtrs(float[] mapFtrs,  String _dispLabel) {
        SOM_GeomSamplePointf[] res = new SOM_GeomSamplePointf[_numSrcPts];
        myVectorf tmpNorm = new myVectorf(mapFtrs[0],mapFtrs[1],0.0f)._normalize();//should be normalized, but in case it isn't
        myPointf ctrPt = new myPointf(mapFtrs[2],mapFtrs[3],0.0f);
        res[0]= new SOM_GeomSamplePointf(myVectorf._add(ctrPt, 11.0f,tmpNorm), _dispLabel+"_BMU_pt_0", this);
        res[1]= new SOM_GeomSamplePointf(myVectorf._add(ctrPt, -12.0f,tmpNorm), _dispLabel+"_BMU_pt_1", this);
        return res;            
    }//buildSrcPtsFromBMUMapNode
    
    
    /**
     * object shape-specific feature building - ftrVecMag calced in base class
     */
    @Override
    protected final void buildFeaturesMap_Indiv() {
        //build feature-based example for this example - for lines example should be closest origin point + normalized dir vector
        //ONLY USE x,y VALS
        ftrMaps[unNormFtrMapTypeKey].put(0,dir.x);
        ftrMaps[unNormFtrMapTypeKey].put(1,dir.y);
        
        ftrMaps[unNormFtrMapTypeKey].put(2,origin.x);
        ftrMaps[unNormFtrMapTypeKey].put(3,origin.y);
    }
    
    ///////////////////////////
    // draw functionality
    
    @Override
    protected final void _drawMe_Geom(IGraphicsAppInterface ri, SOM_GeomObjDrawType drawType) {
        ri.pushMatState();    
        if((drawType.getVal() == 2) || (drawType.getVal() == 3)) {
            ri.setStroke(120, 120,120,150);
            ri.setStrokeWt(1.0f);
            ri.drawLine(dispEndPts[0],dispEndPts[1]);            
        }  else {
            ri.setStrokeWt(2.0f);
            ri.drawLine(dispEndPts[0],dispEndPts[1]);            
        }
        
        _drawPointAtLoc_2D(ri,dispEndPts[0], 5.0f);
        _drawPointAtLoc_2D(ri,dispEndPts[1], 5.0f);
        _drawPointAtLoc_2D(ri,getSrcPts()[0], 5.0f);
        _drawPointAtLoc_2D(ri,getSrcPts()[1], 5.0f);
        _drawPointAtLoc_2D(ri,origin, 5.0f);            
        ri.popMatState();
    }

    @Override
    public final void drawMyLabel(IGraphicsAppInterface ri, SOM_AnimWorldWin _notUsedIn2D) {
        ri.pushMatState();        
        ri.setFill(labelClrAra,255);
        ri.setStroke(labelClrAra,255);
        ri.setStrokeWt(2.0f);
        //(myPointf P, float r, String s, myVectorf D, int clr, boolean flat)
        _drawLabelAtLoc_2D(ri,dispEndPts[0], 5.0f, dispLabel+dispAra[0], 0.0f);
        _drawLabelAtLoc_2D(ri,dispEndPts[1], 5.0f, dispLabel+dispAra[1], 0.0f);
        _drawLabelAtLoc_2D(ri,getSrcPts()[0], 5.0f, "pt a :"+getSrcPts()[0].toStrBrf(), 0.0f);
        _drawLabelAtLoc_2D(ri,getSrcPts()[1], 5.0f, "pt b :"+getSrcPts()[1].toStrBrf(), 0.0f);
        _drawLabelAtLoc_2D(ri,origin, 5.0f, dispLabel+"| Origin " + origin.toStrBrf() + " | Dir : " + dir.toStrBrf() +" | " +dispAra[0]+"->"+dispAra[1], 0.0f);
        ri.popMatState();    
    }
    
    protected float modCnt = 0;//counter that will determine when the color should switch
    
    private final float blinkDist = 20.0f;
    @Override
    protected final void _drawMeSelected(IGraphicsAppInterface ri, float animTmMod) {
        modCnt += animTmMod*2.0f;
        if(modCnt > 1.0){    modCnt = 0.0f;    }//blink every ~second
        ri.drawLine( myPointf._add(dispEndPts[0], blinkDist*modCnt, norm),  myPointf._add(dispEndPts[1], blinkDist*modCnt, norm));
        ri.drawLine( myPointf._add(dispEndPts[0], -blinkDist*modCnt, norm),  myPointf._add(dispEndPts[1], -blinkDist*modCnt, norm));
        
    }//_drawMeSelected

    /**
     * initialize object's ID
     */
    @Override
    protected final int incrID() {return IDGen++;}



}//class SOM_Line
