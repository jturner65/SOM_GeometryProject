package SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs;

import java.util.concurrent.ThreadLocalRandom;

import SOM_GeometryProj_PKG.som_geom.SOM_GeomMapManager;
import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomExample;
import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomMapNode;
import base_UI_Objects.my_procApplet;
import base_Utils_Objects.io.MessageObject;
import base_Utils_Objects.vectorObjs.myPointf;

/**
 * class to instance the base functionality of a geometric object represented by 
 * some parameters and also by samples for use in training and consuming a SOM
 * 
 * @author john
 */
public abstract class SOM_GeomObj {
	/**
	 * reference to the owning map manager - can't be static, may have many different mapmanagers
	 */
	public SOM_GeomMapManager mapMgr;
	/**
	 * message object manages logging/printing to screen
	 */
	protected  MessageObject msgObj;	
	
	/**
	 * radius of sample point to display
	 */
	public final float ptRad;
	/**
	 * sphere detail of sample point to display
	 */
	public final int ptDet;
	/**
	 * location-based and random color arrays, for display
	 */
	public final int[] locClrAra,rndClrAra;
	/**
	 * state flags
	 */
	private int[] stFlags;						//state flags - bits in array holding relevant process info
	public static final int
			debugIDX 				= 0,		//draw this sphere's sample points
			is3dIDX					= 1;		//this object is in 3d or 2d
	public static final int numFlags = 2;	
		
	/**
	 * som example corresponding to this object explicitly (based on instancing object descriptor)
	 */
	public SOM_GeomExample objExample;				//ref to best matching unit of map

	/**
	 * som examples corresponding to sample points
	 */
	public SOM_GeomExample[] smplPtExamples;
	
	protected myPointf baseObjBMUWorldLoc;				//location of center of sphere for bmu
	
	/**
	 * coordinate bounds in world 
	 * 		first idx : 0 is min; 1 is diff
	 * 		2nd idx : 0 is x, 1 is y, 2 is z
	 */
	protected final float[][] worldBounds;
	
	/**
	 * an object to restrict the bounds on this line - min,max, diff s,t value within which to sample plane
	 */
	private float[][] worldTBounds;

	
	public SOM_GeomObj(SOM_GeomMapManager _mapMgr, myPointf _locForClr, float[][] _worldBounds, boolean _is3D) {
		mapMgr = _mapMgr;msgObj = mapMgr.buildMsgObj();
		initFlags();
		setFlag(is3dIDX, _is3D);
		rndClrAra = getRandClr();	
		//sample size/detail
		ptRad = 1.0f;
		ptDet = 2;
		baseObjBMUWorldLoc = new myPointf(0,0,0);
		worldBounds = new float[_worldBounds.length][];
		for(int i=0;i<worldBounds.length;++i) {
			float[] tmp = new float[_worldBounds[i].length];
			for(int j=0;j<tmp.length;++j) {	tmp[j]=_worldBounds[i][j];}
			worldBounds[i]=tmp;
		}
		locClrAra = getClrFromWorldLoc(_locForClr);
	}//ctor
	
	
	
	/**
	 * calculate the bounds on s and t (if appropriate) for parametric formulation of object equation
	 * @return
	 */
	protected abstract float[][] calcTBounds();
	
	public float[][] getWorldTBounds(){return worldTBounds;}
	
	/**
	 * build a specified # of sample points for this object
	 * @param _numSmplPts
	 */
	protected final void buildInitObjAndSamples(int _numSmplPts) {
		//build bounds on s and t, if appropriate - by here equations define objects should be built
		worldTBounds = calcTBounds();
		objExample = buildObjExample();
		buildObjExamples(_numSmplPts);
	}
	
	
	/**
	 * build a specified # of sample points for this object
	 * @param _numSmplPts
	 */
	public final void buildObjExamples(int _numSmplPts) {
		smplPtExamples = new SOM_GeomExample[_numSmplPts];
		//for each sphere build its surface samples
		for(int i = 0; i<_numSmplPts;++i){			smplPtExamples[i] = buildSample(i);		}
	}
	
	/**
	 * get an appropriate sample location to build sample sets, based on what kind of object is being built
	 * @return
	 */
	protected abstract SOM_GeomExample buildSample(int i);
	/**
	 * build this object's "exemplar" example
	 */
	protected abstract SOM_GeomExample buildObjExample();

	/**
	 * return a random point on this object
	 */
	public abstract myPointf getRandPointOnObj();
	
	/**
	 * set bmu world location based on whether map trained by samples or by obj fundamentals
	 * @param _useSmpls
	 */
	public final void setBaseObjBMULoc(boolean _useSmpls) {
		baseObjBMUWorldLoc.set(0,0,0);
		if(_useSmpls) {
			int numSmpls = smplPtExamples.length;
			for(SOM_GeomExample pt : smplPtExamples){				baseObjBMUWorldLoc._add(((SOM_GeomMapNode)(pt.getBmu())).worldLoc);}
			baseObjBMUWorldLoc._div(1.0f*numSmpls);
		} else {
			if(null != objExample.getBmu()) {	baseObjBMUWorldLoc.set(((SOM_GeomMapNode)(objExample.getBmu())).worldLoc);}			
		}
		
	}//setBaseObjBMULoc	
			

	private final int lBnd = 40, uBnd = 255, rndBnd = uBnd - lBnd;
	//convert a world location within the bounded cube region to be a 4-int color array
	public final int[] getClrFromWorldLoc(myPointf t){
		if(getFlag(is3dIDX)) {			//for 3d world bounds is idx 0 == 0,1 for min,diff; idx 1 == 0,1,2 for x,y,z
			return new int[]{(int)(255*(t.x-worldBounds[0][0])/worldBounds[1][0]),(int)(255*(t.y-worldBounds[0][1])/worldBounds[1][1]),(int)(255*(t.z-worldBounds[0][2])/worldBounds[1][2]),255};}
		else {							//for 2d world bounds is idx 0 == 0,1 for min, diff, idx 1 == 0,1 for x,y
			float rs = (t.x-worldBounds[0][0])/worldBounds[1][0], gs = (t.y-worldBounds[0][1])/worldBounds[1][1];
			float bs = Math.abs(rs - gs);
			return new int[]{(int)(rndBnd*rs)+lBnd,(int)(rndBnd*gs)+lBnd,(int)(rndBnd*bs)+lBnd,255};
		}
	}//getClrFromWorldLoc
	
	/**
	 * build a random color
	 * @return
	 */
	protected final int[] getRandClr() {
		int r = ThreadLocalRandom.current().nextInt(rndBnd)+lBnd, g = ThreadLocalRandom.current().nextInt(rndBnd)+lBnd, b =ThreadLocalRandom.current().nextInt(rndBnd)+lBnd;
		return new int[] {r,g,b};
	}
	
	
	/**
	 * Draw this object with a label
	 * @param pa
	 */
	public abstract void drawMeLabel(my_procApplet pa);
	
	/**
	 * draw entire object this class represents, using location as color or using randomly assigned color
	 * @param pa
	 */
	
	public final void drawMeClrRnd(my_procApplet pa, int alpha) {
		pa.pushMatrix();pa.pushStyle();		
		pa.setFill(rndClrAra,alpha);
		pa.setStroke(rndClrAra,alpha);
		_drawMe_Geom(pa);
		pa.popStyle();pa.popMatrix();	
	}	
	
	
	public final void drawMeClrLoc(my_procApplet pa, int alpha) {
		pa.pushMatrix();pa.pushStyle();		
		pa.setFill(locClrAra,50);
		pa.setStroke(locClrAra,50);
		_drawMe_Geom(pa);
		pa.popStyle();pa.popMatrix();	
	}
	
	
	/**
	 * draw this object
	 * @param pa
	 */
	protected abstract void _drawMe_Geom(my_procApplet pa);
	
	
	protected abstract void _drawMe_Geom_Lbl(my_procApplet pa);
	

	public abstract void drawMeSelected(my_procApplet pa,float animTmMod);
	
	
	/**
	 * draw this object's samples, using the random color
	 * @param pa
	 */
	public final void drawMeSmplsClrRnd(my_procApplet pa){
		pa.pushMatrix();pa.pushStyle();
//		pa.setFill(rndClrAra,255); 
//		pa.setStroke(rndClrAra,255);
//		pa.sphereDetail(ptDet);
		for(SOM_GeomExample pt : smplPtExamples){
			pt.drawMeObjRandClr(pa,ptDet);
//			pa.pushMatrix(); 
//			pa.translate(pt.worldLoc); 
//			pa.sphere(ptRad); 
//			pa.popMatrix();
		}
		pa.popStyle();pa.popMatrix();
	}//
	
	/**
	 * draw this object's samples, using the location-based color
	 * @param pa
	 */
	public final void drawMeSmplsClrLoc(my_procApplet pa){
		pa.pushMatrix();pa.pushStyle();		
		for(SOM_GeomExample pt : smplPtExamples){
			pt.drawMeObjLocClr(pa,ptDet);
//			pa.pushMatrix(); 
//			pa.translate(pt.worldLoc); 
//			pa.sphere(ptRad); 
//			pa.popMatrix();
		}
		pa.popStyle();pa.popMatrix();
	}//	
	
	/**
	 * use sample's location color as colors to draw samples
	 * @param pa
	 */
	public final void drawMeSmplsClrSmplLoc(my_procApplet pa){
		pa.pushMatrix();pa.pushStyle();		
		//pa.noStroke();
		//pa.sphereDetail(ptDet);
		for(SOM_GeomExample pt : smplPtExamples){
			pt.drawMeMyLocClr(pa, ptDet);
//			pa.pushMatrix(); pa.pushStyle();
//			pa.fill(pt.locClrs[0],pt.locClrs[1],pt.locClrs[2], pt.locClrs[3]);
//			pa.stroke(pt.locClrs[0],pt.locClrs[1],pt.locClrs[2], pt.locClrs[3]);			
//			pa.translate(pt.worldLoc); 
//			pa.sphere(ptRad); 
//			pa.popStyle();pa.popMatrix();
		}
		pa.popStyle();pa.popMatrix();
	}

	//////////////////////////////
	// bmu drawing
	/**
	 * Draw this object's bmu with a label
	 * @param pa
	 */
	public abstract void drawMeLabel_BMU(my_procApplet pa);
	public abstract void drawMeClrRnd_BMU(my_procApplet pa);
	public abstract void drawMeClrLoc_BMU(my_procApplet pa);
	public abstract void drawMeSelected_BMU(my_procApplet pa,float animTmMod);
	
	public final void drawMeSmplsClrRnd_BMU(my_procApplet pa){
		pa.pushMatrix();pa.pushStyle();
//		pa.setFill(rndClrAra,255); 
//		pa.setStroke(rndClrAra,255);
//		pa.sphereDetail(ptDet);
		for(SOM_GeomExample pt : smplPtExamples){
			pt.drawMeObjRandClr_BMU(pa,ptDet);
//			pa.pushMatrix(); 
//			pa.translate(((Geom_SOMMapNode)(pt.getBmu())).mapLoc); 
//			pa.sphere(ptRad); 
//			pa.popMatrix();
		}
		pa.popStyle();pa.popMatrix();
	}//
	
	public final void drawMeSmplsClrLoc_BMU(my_procApplet pa){
		pa.pushMatrix();pa.pushStyle();		
//		pa.setFill(locClrAra,255);
//		pa.setStroke(locClrAra,255);
//		pa.sphereDetail(ptDet);
		for(SOM_GeomExample pt : smplPtExamples){
			pt.drawMeObjLocClr_BMU(pa,ptDet);
//			pa.pushMatrix(); 
//			pa.translate(((Geom_SOMMapNode)(pt.getBmu())).worldLoc); 
//			pa.sphere(ptRad); 
//			pa.popMatrix();
		}
		pa.popStyle();pa.popMatrix();
	}//	

	public void drawMeSmplsClrSmplLoc_BMU(my_procApplet pa){
		pa.pushMatrix();pa.pushStyle();		
		//pa.noStroke();
		//pa.sphereDetail(ptDet);
		for(SOM_GeomExample pt : smplPtExamples){
			pt.drawMeMyLocClr_BMU(pa, ptDet);
			
//			pa.pushMatrix(); pa.pushStyle();
//			pa.fill(pt.locClrs[0],pt.locClrs[1],pt.locClrs[2], pt.locClrs[3]);
//			pa.stroke(pt.locClrs[0],pt.locClrs[1],pt.locClrs[2], pt.locClrs[3]);
//			
//			pa.translate(((Geom_SOMMapNode)(pt.getBmu())).worldLoc); 
//			pa.sphere(ptRad); 
//			pa.popStyle();pa.popMatrix();
		}
		pa.popStyle();pa.popMatrix();
	}
	
	private void initFlags(){stFlags = new int[1 + numFlags/32]; for(int i = 0; i<numFlags; ++i){setFlag(i,false);}}
	public final void setFlag(int idx, boolean val){
		int flIDX = idx/32, mask = 1<<(idx%32);
		stFlags[flIDX] = (val ?  stFlags[flIDX] | mask : stFlags[flIDX] & ~mask);
		switch (idx) {//special actions for each flag
			case debugIDX : {break;}			
		}
	}//setFlag	
	public final boolean getFlag(int idx){int bitLoc = 1<<(idx%32);return (stFlags[idx/32] & bitLoc) == bitLoc;}		



}//class SOM_GeomObj
