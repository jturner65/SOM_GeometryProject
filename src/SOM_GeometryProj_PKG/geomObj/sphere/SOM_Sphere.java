package SOM_GeometryProj_PKG.geomObj.sphere;

import SOM_GeometryProj_PKG.sphere_SOM_Examples.Sphere_SOMExample;
import SOM_GeometryProj_PKG.sphere_SOM_Examples.Sphere_SOMMapNode;
import base_SOM_Objects.SOM_MapManager;
import base_SOM_Objects.som_examples.SOM_Example;
import base_UI_Objects.my_procApplet;
import base_Utils_Objects.MyMathUtils;
import base_Utils_Objects.io.MessageObject;
import base_Utils_Objects.vectorObjs.myVectorf;

/**
 * class to hold center location, radius, color and samples of sphere used to train SOM
 * @author john
 *
 */
public class SOM_Sphere {

	public static SOM_MapManager mapMgr;
	//message object manages logging/printing to screen
	protected static MessageObject msgObj;
	
	public final int ID;
	public static int IDGen = 0;
	public myVectorf loc;
	public int[] locClrAra;
	public float rad, ptRad;
	
	public Sphere_SOMExample dp;				//ref to best matching unit of map
		
	public Sphere_SOMExample[] smplPts;
	private int[] stFlags;						//state flags - bits in array holding relevant process info
	public static final int
			debugIDX 				= 0;		//draw this sphere's sample points
	public static final int numFlags = 1;		
	
	public int sphrDet, ptDet;
	public int[] rndClrVal;
	
	public SOM_Sphere(my_procApplet _pa, SOM_MapManager _mapMgr, myVectorf _loc, float _rad, int _numSmplPts, int[] _rndClrVal) {
		mapMgr=_mapMgr;
		msgObj = mapMgr.buildMsgObj();
		ID = IDGen++;
		initFlags();
		loc = _loc;
		locClrAra = _pa.getClrFromCubeLoc(loc);
		rad = _rad;
		
		dp = new Sphere_SOMExample(mapMgr, loc, locClrAra, "Sphere "+ ID);

		ptRad = MyMathUtils.min(.1f*rad, 3.0f);
		sphrDet = (int)(Math.sqrt(rad) + 10);
		ptDet = 2;//(int)(pa.sqrt(ptRad) + 3);
		rndClrVal = _rndClrVal;

		smplPts = new Sphere_SOMExample[_numSmplPts];
		//for each sphere build its surface samples
		for(int i = 0; i<_numSmplPts;++i){
			myVectorf _locVec = _pa.getRandPosOnSphere(rad,loc);
			int[] _locClrs = _pa.getClrFromCubeLoc(_locVec);
			smplPts[i] = new Sphere_SOMExample(mapMgr,_locVec, _locClrs, "Sphr "+ ID + " Smpl "+i);
			//pa.outStr2Scr("Sphere ID:"+ID+" smpl : "+ i+" : \tlabel : " + smplPts[i].label.toString());
		}
		//pa.outStr2Scr("Done building sphere " + ID);
	}//ctor
	
	public void drawMeClrRnd(my_procApplet pa){
		pa.pushMatrix();pa.pushStyle();		
			pa.show(loc, rad, sphrDet, rndClrVal, rndClrVal);		//show main sphere in random color
		pa.popStyle();pa.popMatrix();
	}//
	public void drawMeClrLoc(my_procApplet pa){
		pa.pushMatrix();pa.pushStyle();		
			pa.show(loc, rad, sphrDet, locClrAra, locClrAra);		//show main sphere in location color
		pa.popStyle();pa.popMatrix();
	}//
	private static float modCnt = 0;//counter that will determine when the color should switch
	public void drawMeSelected(my_procApplet pa,float animTmMod){//animTmMod is time since last frame
		modCnt += animTmMod;
		if(modCnt > 1.0){	modCnt = 0;	}//blink every ~second
		pa.pushMatrix();pa.pushStyle();		
		pa.noFill();//fill(255*modCnt,255);
		pa.stroke(255*modCnt, 255);		
		pa.translate(loc); 
		pa.sphere(rad*(modCnt + 1.0f)); 
		pa.popStyle();pa.popMatrix();
	}
	
	public void drawMeLabel(my_procApplet pa){
		pa.pushMatrix();pa.pushStyle();		
		pa.setColorValFill(0,255); 
		pa.setColorValStroke(0,255);		
		pa.translate(loc); 
		pa.unSetCamOrient_Glbl();
		pa.scale(.75f);
		pa.text(""+ID, rad,-rad,0); 
		pa.popStyle();pa.popMatrix();
	}
	
	public void drawMeSmplsClrRnd(my_procApplet pa){
		pa.pushMatrix();pa.pushStyle();
//		pa.setColorValFill(rndClrVal,255); 
//		pa.setColorValStroke(rndClrVal,255);
		pa.setFill(rndClrVal,255); 
		pa.setStroke(rndClrVal,255);
		pa.sphereDetail(ptDet);
		for(Sphere_SOMExample pt : smplPts){
			pa.pushMatrix(); 
			pa.translate(pt.worldLoc); 
			pa.sphere(ptRad); 
			pa.popMatrix();
		}
		pa.popStyle();pa.popMatrix();
	}//
	public void drawMeSmplsClrLoc(my_procApplet pa){
		pa.pushMatrix();pa.pushStyle();		
		pa.setFill(locClrAra,255);
		pa.setStroke(locClrAra,255);
		pa.sphereDetail(ptDet);
		for(Sphere_SOMExample pt : smplPts){
			pa.pushMatrix(); 
			pa.translate(pt.worldLoc); 
			pa.sphere(ptRad); 
			pa.popMatrix();
		}
		pa.popStyle();pa.popMatrix();
	}//	
	
	public void drawMeSmplsClrSmplLoc(my_procApplet pa){
		pa.pushMatrix();pa.pushStyle();		
		//pa.noStroke();
		pa.sphereDetail(ptDet);
		for(Sphere_SOMExample pt : smplPts){
			pa.pushMatrix(); pa.pushStyle();
			pa.fill(pt.locClrs[0],pt.locClrs[1],pt.locClrs[2], pt.locClrs[3]);
			pa.stroke(pt.locClrs[0],pt.locClrs[1],pt.locClrs[2], pt.locClrs[3]);
			
			pa.translate(pt.worldLoc); 
			pa.sphere(ptRad); 
			pa.popStyle();pa.popMatrix();
		}
		pa.popStyle();pa.popMatrix();
	}
	
///////////////BMU / datapoint drawing
	public void drawMeClrRnd_BMU(my_procApplet pa){
		pa.pushMatrix();pa.pushStyle();		
		//TODO dp.bmu is null by here.
			pa.show(((Sphere_SOMMapNode)(dp.getBmu())).worldLoc, rad, sphrDet, rndClrVal, rndClrVal);		//show main sphere in random color
		pa.popStyle();pa.popMatrix();
	}//
	public void drawMeClrLoc_BMU(my_procApplet pa){
		pa.pushMatrix();pa.pushStyle();		
		//TODO dp.bmu is null by here.
			pa.show(((Sphere_SOMMapNode)(dp.getBmu())).worldLoc, rad, sphrDet, locClrAra, locClrAra);		//show main sphere in location color
		pa.popStyle();pa.popMatrix();
	}//

	
	public void drawMeSelected_BMU(my_procApplet pa,float animTmMod){//animTmMod is time since last frame
		modCnt += animTmMod;
		if(modCnt > 1.0){	modCnt = 0;	}//blink every ~second
		pa.pushMatrix();pa.pushStyle();		
		pa.noFill();
		pa.stroke(255*modCnt, 255);		
		pa.translate(((Sphere_SOMMapNode)(dp.getBmu())).mapLoc); 
		pa.sphere(rad*(modCnt + 1.0f)); 
		pa.popStyle();pa.popMatrix();
	}
	
	public void drawMeLabel_BMU(my_procApplet pa){
		pa.pushMatrix();pa.pushStyle();		
		pa.setColorValFill(0,255); 
		pa.setColorValStroke(0,255);		
		pa.translate(((Sphere_SOMMapNode)(dp.getBmu())).mapLoc); 
		pa.unSetCamOrient_Glbl();
		pa.scale(.75f);
		pa.text(""+ID, rad,-rad,0); 
		pa.popStyle();pa.popMatrix();
	}

	public void drawMeSmplsClrRnd_BMU(my_procApplet pa){
		pa.pushMatrix();pa.pushStyle();
		pa.setFill(rndClrVal,255); 
		pa.setStroke(rndClrVal,255);
		pa.sphereDetail(ptDet);
		for(Sphere_SOMExample pt : smplPts){
			pa.pushMatrix(); 
			pa.translate(((Sphere_SOMMapNode)(pt.getBmu())).mapLoc); 
			pa.sphere(ptRad); 
			pa.popMatrix();
		}
		pa.popStyle();pa.popMatrix();
	}//
	
	public void drawMeSmplsClrLoc_BMU(my_procApplet pa){
		pa.pushMatrix();pa.pushStyle();		
		pa.setFill(locClrAra,255);
		pa.setStroke(locClrAra,255);
		pa.sphereDetail(ptDet);
		for(Sphere_SOMExample pt : smplPts){
			pa.pushMatrix(); 
			pa.translate(((Sphere_SOMMapNode)(pt.getBmu())).worldLoc); 
			pa.sphere(ptRad); 
			pa.popMatrix();
		}
		pa.popStyle();pa.popMatrix();
	}//	

	public void drawMeSmplsClrSmplLoc_BMU(my_procApplet pa){
		pa.pushMatrix();pa.pushStyle();		
		//pa.noStroke();
		pa.sphereDetail(ptDet);
		for(Sphere_SOMExample pt : smplPts){
			pa.pushMatrix(); pa.pushStyle();
			pa.fill(pt.locClrs[0],pt.locClrs[1],pt.locClrs[2], pt.locClrs[3]);
			pa.stroke(pt.locClrs[0],pt.locClrs[1],pt.locClrs[2], pt.locClrs[3]);
			
			pa.translate(((Sphere_SOMMapNode)(pt.getBmu())).worldLoc); 
			pa.sphere(ptRad); 
			pa.popStyle();pa.popMatrix();
		}
		pa.popStyle();pa.popMatrix();
	}
	
	//returns ara of coords to be used as training features for som
	//public dataPoint getDataPoint(){		return dp;}
	
	private void initFlags(){stFlags = new int[1 + numFlags/32]; for(int i = 0; i<numFlags; ++i){setFlag(i,false);}}
	public void setFlag(int idx, boolean val){
		int flIDX = idx/32, mask = 1<<(idx%32);
		stFlags[flIDX] = (val ?  stFlags[flIDX] | mask : stFlags[flIDX] & ~mask);
		switch (idx) {//special actions for each flag
			case debugIDX : {break;}			
		}
	}//setFlag	
	public boolean getFlag(int idx){int bitLoc = 1<<(idx%32);return (stFlags[idx/32] & bitLoc) == bitLoc;}		

}//mySOMSphere
