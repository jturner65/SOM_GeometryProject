package SOM_GeometryProj_PKG.geom_Obj.sphere;

import java.util.concurrent.ThreadLocalRandom;

import SOM_GeometryProj_PKG.geom_Obj.base.SOM_GeomObj;
import SOM_GeometryProj_PKG.geom_SOM_Examples.Geom_SOMExample;
import SOM_GeometryProj_PKG.geom_SOM_Examples.Geom_SOMMapNode;
import base_SOM_Objects.SOM_MapManager;
import base_UI_Objects.my_procApplet;
import base_Utils_Objects.MyMathUtils;
import base_Utils_Objects.vectorObjs.myVectorf;

/**
 * class to hold center location, radius, color and samples of sphere used to train SOM
 * @author john
 *
 */
public class SOM_Sphere extends SOM_GeomObj{
	/**
	 * center locations
	 */
	public final myVectorf ctrLoc;
	
	public final int sphrDet;
	public final float rad;

	public SOM_Sphere(my_procApplet _pa, SOM_MapManager _mapMgr, myVectorf _loc, float _rad, int _numSmplPts, int[] _rndClrAra) {
		super(_pa, _mapMgr, _numSmplPts, _pa.getClrFromCubeLoc(_loc),_rndClrAra);
		ctrLoc = _loc;
		rad = _rad;	
		sphrDet = (int)(Math.sqrt(rad) + 10);	
	}//ctor
	
	private final myVectorf getRandPosOnSphere(double rad, myVectorf ctr){
		myVectorf pos = new myVectorf();
		double 	cosTheta = ThreadLocalRandom.current().nextDouble(-1,1), sinTheta =  Math.sin(Math.acos(cosTheta)),
				phi = ThreadLocalRandom.current().nextDouble(0,MyMathUtils.twoPi_f);
		pos.set(sinTheta * Math.cos(phi), sinTheta * Math.sin(phi),cosTheta);
		pos._mult(rad);
		pos._add(ctr);
		return pos;
	}//getRandPosOnSphere
	
	/**
	 * build this object's "exemplar" example
	 */
	@Override
	protected Geom_SOMExample buildObjExample() {return new Geom_SOMExample(mapMgr, ctrLoc, locClrAra, "Sphere "+ ID);}

	@Override
	/**
	 * get an appropriate sample location to build sample sets, based on what kind of object is being built
	 * @return
	 */
	protected final Geom_SOMExample buildSample(int i) {
		myVectorf _locVec = getRandPosOnSphere(rad,ctrLoc);
		int[] _locClrs = pa.getClrFromCubeLoc(_locVec);
		return new Geom_SOMExample(mapMgr,_locVec, _locClrs, "Sphr "+ ID + " Smpl "+i);
	}

	
	@Override
	public void drawMeClrRnd(my_procApplet pa){
		pa.pushMatrix();pa.pushStyle();		
			pa.show(ctrLoc, rad, sphrDet, rndClrAra, rndClrAra);		//show main sphere in random color
		pa.popStyle();pa.popMatrix();
	}//
	
	@Override
	public void drawMeClrLoc(my_procApplet pa){
		pa.pushMatrix();pa.pushStyle();		
			pa.show(ctrLoc, rad, sphrDet, locClrAra, locClrAra);		//show main sphere in location color
		pa.popStyle();pa.popMatrix();
	}//
	
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
		pa.translate(((Geom_SOMMapNode)(objExample.getBmu())).mapLoc); 
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
		pa.translate(((Geom_SOMMapNode)(objExample.getBmu())).mapLoc); 
		pa.unSetCamOrient_Glbl();
		pa.scale(.75f);
		pa.text(""+ID, rad,-rad,0); 
		pa.popStyle();pa.popMatrix();
	}



}//mySOMSphere
