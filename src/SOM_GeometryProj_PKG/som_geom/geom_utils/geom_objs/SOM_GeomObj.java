package SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs;

import java.util.concurrent.ThreadLocalRandom;

import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomExample;
import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomMapNode;
import base_SOM_Objects.SOM_MapManager;
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
	
	protected static my_procApplet pa;
	/**
	 * reference to the owning map manager
	 */
	public static SOM_MapManager mapMgr;
	/**
	 * message object manages logging/printing to screen
	 */
	protected static MessageObject msgObj;	
	
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
			debugIDX 				= 0;		//draw this sphere's sample points
	public static final int numFlags = 1;	
		
	/**
	 * som example corresponding to this object explicitly (based on instancing object descriptor)
	 */
	public SOM_GeomExample objExample;				//ref to best matching unit of map

	/**
	 * som examples corresponding to sample points
	 */
	public SOM_GeomExample[] smplPtExamples;
	
	protected myPointf baseObjBMUWorldLoc;				//location of center of sphere for bmu
	
	public SOM_GeomObj(my_procApplet _pa, SOM_MapManager _mapMgr, int[] _locClrAra) {
		pa =_pa;mapMgr = _mapMgr;msgObj = mapMgr.buildMsgObj();
		initFlags();
		locClrAra = _locClrAra;
		rndClrAra = getRandClr();	
		//sample size/detail
		ptRad = 1.0f;
		ptDet = 2;
		baseObjBMUWorldLoc = new myPointf(0,0,0);
	}//ctor
	
	
	/**
	 * build a specified # of sample points for this object
	 * @param _numSmplPts
	 */
	public void buildObjExamples(int _numSmplPts) {
		objExample = buildObjExample();
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
	
	/**
	 * build a random color
	 * @return
	 */
	private final int lBnd = 90, uBnd = 200, 
			rndBnd = uBnd - lBnd;
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
	public abstract void drawMeClrRnd(my_procApplet pa);
	public abstract void drawMeClrLoc(my_procApplet pa);	
	public abstract void drawMeSelected(my_procApplet pa,float animTmMod);
	
	
	/**
	 * draw this object's samples, using the random color
	 * @param pa
	 */
	public void drawMeSmplsClrRnd(my_procApplet pa){
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
	public void drawMeSmplsClrLoc(my_procApplet pa){
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
	public void drawMeSmplsClrSmplLoc(my_procApplet pa){
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
	
	public void drawMeSmplsClrRnd_BMU(my_procApplet pa){
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
	
	public void drawMeSmplsClrLoc_BMU(my_procApplet pa){
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
