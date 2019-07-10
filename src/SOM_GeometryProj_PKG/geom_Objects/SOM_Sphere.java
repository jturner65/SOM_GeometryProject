package SOM_GeometryProj_PKG.geom_Objects;

import java.util.concurrent.ThreadLocalRandom;

import SOM_GeometryProj_PKG.geom_SOM_Examples.Geom_SphereSOMExample;
import SOM_GeometryProj_PKG.som_geom.SOM_GeomMapManager;
import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomExample;
import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomMapNode;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomObj;
import base_SOM_Objects.SOM_MapManager;
import base_UI_Objects.my_procApplet;
import base_Utils_Objects.MyMathUtils;
import base_Utils_Objects.vectorObjs.myPointf;
import base_Utils_Objects.vectorObjs.myVectorf;

/**
 * class to hold center location, radius, color and samples of sphere used to train SOM
 * @author john
 *
 */
public class SOM_Sphere extends SOM_GeomObj{
	public final int ID;
	private static int IDGen = 0;
	/**
	 * center location
	 */
	public final myPointf ctrLoc;
	
	public final int sphrDet;
	public final float rad;

	public SOM_Sphere(SOM_GeomMapManager _mapMgr, myPointf _loc, float _rad, int _numSmplPts, float[][] _worldBounds) {
		super(_mapMgr,_loc, _worldBounds, true);
		ID = IDGen++;
		ctrLoc = _loc;
		rad = _rad;	
		sphrDet = (int)(Math.sqrt(rad) + 10);	
		
		super.buildInitObjAndSamples(_numSmplPts);
	}//ctor
	
	private final myPointf getRandPosOnSphere(double rad, myPointf ctr){
		myPointf pos = new myPointf();
		double 	cosTheta = ThreadLocalRandom.current().nextDouble(-1,1), sinTheta =  Math.sin(Math.acos(cosTheta)),
				phi = ThreadLocalRandom.current().nextDouble(0,MyMathUtils.twoPi_f);
		pos.set(sinTheta * Math.cos(phi), sinTheta * Math.sin(phi),cosTheta);
		pos._mult((float) rad);
		pos._add(ctr);
		return pos;
	}//getRandPosOnSphere
	
	/**
	 * calculate the bounds on s and t (if appropriate) for parametric formulation of object equation
	 * idx 0 : 0==min, 1==diff
	 * idx 1 : 0==t, 1==s, 
	 * @return
	 */
	protected final float[][] calcTBounds(){
		float[][] res = new float[][] {{0.0f,0.0f},{0.0f,0.0f}};
		
		return res;
	}

	/**
	 * build this object's "exemplar" example
	 */
	@Override
	protected SOM_GeomExample buildObjExample() {return new Geom_SphereSOMExample(mapMgr, ctrLoc, ctrLoc, rad, "Sphere "+ ID, new int[][] {locClrAra, rndClrAra,locClrAra, rndClrAra});}

	/**
	 * get an appropriate sample location to build sample sets, based on what kind of object is being built
	 * @return
	 */
	@Override
	protected final SOM_GeomExample buildSample(int i) {
		myPointf _locVec = getRandPosOnSphere(rad,ctrLoc);
		int[] _locClrs = mapMgr.win.pa.getClrFromCubeLoc(_locVec);
		return new Geom_SphereSOMExample(mapMgr, ctrLoc, _locVec,  rad, "Sphr "+ ID + " Smpl "+i, new int[][] {locClrAra, rndClrAra,_locClrs, getRandClr()} );
	}
	
	/**
	 * return a random point on this object
	 */
	@Override
	public final myPointf getRandPointOnObj() {return getRandPosOnSphere(rad,ctrLoc);}
	
	@Override
	protected void _drawMe_Geom(my_procApplet pa) {
		pa.pushMatrix();pa.pushStyle();	
		pa.sphereDetail(sphrDet);
		pa.translate(ctrLoc.x,ctrLoc.y,ctrLoc.z); 
		pa.sphere(rad); 
		pa.popStyle();pa.popMatrix();	
	}

	@Override
	protected final void _drawMe_Geom_Lbl(my_procApplet pa) {
		
		
	}
	
	
	private static float modCnt = 0;//counter that will determine when the color should switch
	@Override
	public void drawMeSelected(my_procApplet pa,float animTmMod){//animTmMod is time since last frame
		modCnt += animTmMod;
		if(modCnt > 1.0){	modCnt = 0;	}//blink every ~second
		pa.pushMatrix();pa.pushStyle();		
		pa.noFill();//fill(255*modCnt,255);
		pa.stroke(255*modCnt, 255);		
		pa.translate(ctrLoc); 
		pa.sphere(rad*(modCnt + 1.0f)); 
		pa.popStyle();pa.popMatrix();
	}
	@Override
	public void drawMeSelected_BMU(my_procApplet pa,float animTmMod){//animTmMod is time since last frame
		modCnt += animTmMod;
		if(modCnt > 1.0){	modCnt = 0;	}//blink every ~second
		pa.pushMatrix();pa.pushStyle();		
		pa.noFill();
		pa.stroke(255*modCnt, 255);		
		pa.translate(((SOM_GeomMapNode)(objExample.getBmu())).mapLoc); 
		pa.sphere(rad*(modCnt + 1.0f)); 
		pa.popStyle();pa.popMatrix();
	}	
	@Override
	public void drawMeLabel(my_procApplet pa){
		pa.pushMatrix();pa.pushStyle();		
		pa.setColorValFill(0,255); 
		pa.setColorValStroke(0,255);		
		pa.translate(ctrLoc); 
		pa.unSetCamOrient_Glbl();
		pa.scale(.75f);
		pa.text(""+ID, rad,-rad,0); 
		pa.popStyle();pa.popMatrix();
	}

	

///////////////BMU / datapoint drawing
	@Override
	public void drawMeClrRnd_BMU(my_procApplet pa){
		pa.pushMatrix();pa.pushStyle();		
			pa.show(baseObjBMUWorldLoc, rad, sphrDet, rndClrAra, rndClrAra);		//show main sphere in random color
		pa.popStyle();pa.popMatrix();
	}//
	@Override
	public void drawMeClrLoc_BMU(my_procApplet pa){
		pa.pushMatrix();pa.pushStyle();		
			pa.show(baseObjBMUWorldLoc, rad, sphrDet, locClrAra, locClrAra);		//show main sphere in location color
		pa.popStyle();pa.popMatrix();
	}//
	
	@Override
	public void drawMeLabel_BMU(my_procApplet pa){
		pa.pushMatrix();pa.pushStyle();		
		pa.setColorValFill(0,255); 
		pa.setColorValStroke(0,255);		
		pa.translate(((SOM_GeomMapNode)(objExample.getBmu())).mapLoc); 
		pa.unSetCamOrient_Glbl();
		pa.scale(.75f);
		pa.text(""+ID, rad,-rad,0); 
		pa.popStyle();pa.popMatrix();
	}



}//mySOMSphere
