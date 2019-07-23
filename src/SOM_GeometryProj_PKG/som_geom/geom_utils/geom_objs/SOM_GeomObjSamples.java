package SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs;

import SOM_GeometryProj_PKG.som_geom.geom_UI.SOM_AnimWorldWin;
import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomObj;
import base_UI_Objects.my_procApplet;
import base_Utils_Objects.vectorObjs.myPointf;
import processing.core.PShape;

/**
 * this class will hold a number of samples representing the surface of some object
 * @author john
 */

public class SOM_GeomObjSamples {
	/**
	 * owning object
	 */
	public final SOM_GeomObj ownr;
	/**
	 * descriptive string of object type
	 */
	public final String objTypeStrAndID;
	/**
	 * array of samples
	 */
	private SOM_GeomSamplePointf[] objSamplePts;
	/**
	 * array, idx 0 is rand color, idx 1 is loc color, idx 2 is no fill rnd, idx 3 is no fill loc clr, idx 4 is selected
	 */
	protected PShape[] sampleObjPShapes;
	/**
	 * location-based and random color arrays, for display
	 */
	public int[] locClrAra;
	public final int[] rndClrAra, labelClrAra;
	/**
	 * radius of sample point to display
	 */
	public final float ptRad;
	/**
	 * sphere detail of sample point to display
	 */
	public static final int ptDet = 2;	
	/**
	 * tag to denote the beginning/end of a sample point record in csv file
	 */
	private static final String samplPtTag = "SMPLPT,";
	
	public SOM_GeomObjSamples(SOM_GeomObj _ownr, SOM_GeomObjTypes _objGeomType, int[] _rndClrAra, int[] _lblClrAra) {
		ownr = _ownr;
		ptRad = (float)Math.pow(_objGeomType.getVal(), 1.4);
		objTypeStrAndID = ownr.getDispLabel();
		rndClrAra = new int[_rndClrAra.length];
		System.arraycopy(_rndClrAra, 0, rndClrAra, 0, _rndClrAra.length);
		labelClrAra = new int[_lblClrAra.length];
		System.arraycopy(_lblClrAra, 0, labelClrAra, 0, _lblClrAra.length);		
	}
	
	/**
	 * build samples from csv string listing
	 * @param _locForClr
	 * @param _csvStr
	 */
	public final void buildSampleSetAndPShapesFromCSVStr(my_procApplet pa, String _csvStr) {
		String[] tmpDatAra = _csvStr.split("ST_"+samplPtTag);
		String[] onlySamplesAra = tmpDatAra[1].trim().split("END_"+samplPtTag);
		String[] samplePtAra = onlySamplesAra[0].trim().split(samplPtTag);
		//idx 0 is # of samples
		int numSmplPts = Integer.parseInt(samplePtAra[0].trim().split(",")[0].trim());		
		objSamplePts = new SOM_GeomSamplePointf[numSmplPts];
		for(int i=0;i<objSamplePts.length;++i) {	
			String[] ptDescAra = samplePtAra[i+1].trim().split(",");
			objSamplePts[i] = new SOM_GeomSamplePointf(ptDescAra);				
		}		
		buildSamplePShapeObjs(pa);
	}
	
	public int getNumSamples() {return objSamplePts.length;}
	public SOM_GeomSamplePointf getSamplePt(int idx) {return objSamplePts[idx];}
	public SOM_GeomSamplePointf[] getAllSamplePts() {return objSamplePts;}
	
	/**
	 * build pshape to hold samples, to speed up rendering
	 * @param _numSmplPts
	 */
	public final void buildSampleSetAndPShapes(my_procApplet pa, int _numSmplPts) {
		objSamplePts = buildSamplesOfThisObject(0,_numSmplPts);
		buildSamplePShapeObjs(pa);		
	}//buildSampleSet
	
	/**
	 * build sample points array of points on owning object - maintain offset with samples already built for owning object if not replacing them
	 * @param _numSmplPtsToBuild
	 * @return
	 */
	public final SOM_GeomSamplePointf[] buildSamplesOfThisObject(int _numSmplPtsToBuild) { return buildSamplesOfThisObject(objSamplePts.length, _numSmplPtsToBuild);}
	private final SOM_GeomSamplePointf[] buildSamplesOfThisObject(int _stSmplLblIDX, int _numSmplPtsToBuild) {
		SOM_GeomSamplePointf[] tmpSmplAra = new SOM_GeomSamplePointf[_numSmplPtsToBuild];
		int stIDX = 0;
		if(0==_stSmplLblIDX) {//if starting from 0, then this means we are making original sample set - add ownr.src pts
			for(int i=0;i<ownr.getSrcPts().length;++i) {
				tmpSmplAra[i]=new SOM_GeomSamplePointf(ownr.getSrcPts()[i],objTypeStrAndID+"_Smpl_"+String.format("%04d", i));
			}
			stIDX=ownr.getSrcPts().length;
		}
		for(int i=stIDX;i<tmpSmplAra.length;++i) {	
			tmpSmplAra[i]=new SOM_GeomSamplePointf(ownr.getRandPointOnObj(), objTypeStrAndID+"_Smpl_"+String.format("%04d", (i+_stSmplLblIDX))); 
			//msgObj.dispInfoMessage("SOM_GeomObj::"+type.toString(), "buildLocClrInitObjAndSamples", "ID : " + ID + " | sample pt loc : " + objSamplePts[i].toStrBrf());
		}		
		return tmpSmplAra;
	}
	private void buildSamplePShapeObjs(my_procApplet pa) {
		//update colors - these will be set in owner by here
		locClrAra = new int[ownr.locClrAra.length];
		System.arraycopy(ownr.locClrAra, 0, locClrAra, 0, ownr.locClrAra.length);

		//create representation
		sampleObjPShapes = new PShape[3];
		sampleObjPShapes[SOM_GeomObjDrawType.rndClr.getVal()] = buildSampleCloud(pa,rndClrAra);
		sampleObjPShapes[SOM_GeomObjDrawType.locClr.getVal()] = buildSampleCloud(pa,locClrAra);
		sampleObjPShapes[2] = buildSampleCloud(pa,labelClrAra);

	}
	
	private PShape buildSampleCloud(my_procApplet pa, int[] clrs) {
		PShape poly = pa.createShape(); 
		poly.beginShape(pa.POINTS);
		poly.fill(clrs[0],clrs[1],clrs[2],255);
		poly.stroke(clrs[0],clrs[1],clrs[2],255);
		poly.strokeWeight(5.0f);
		for(int i=0;i<objSamplePts.length;++i) {
			myPointf pt = objSamplePts[i];
			poly.vertex(pt.x,pt.y,pt.z);
		}
		poly.endShape();
		return poly;
	}//buildSampleCloud	

/**
	 * get description tag of these samples to save to CSV
	 * @return
	 */
	public final String getRawDescColNamesForCSV() {
		String res = "Samples Start Tag,# Samples, TAG,";
		for(int i=0; i<objSamplePts.length;++i) {res += objSamplePts[i].toCSVHeaderStr()+"TAG,";}
		return res;
	}

	/**
	 * required info for this example to build feature data - use this so we don't have to reload and rebuilt from data every time
	 * @return
	 */
	public final String getPreProcDescrForCSV() {
		String res = "ST_"+samplPtTag+objSamplePts.length+","+samplPtTag;
		for(int i=0; i<objSamplePts.length;++i) {res += objSamplePts[i].toCSVStr()+samplPtTag;}
		res += "END_"+samplPtTag;
		return res;
	}
	
	////////////////////
	// draw routines
	
	
	/**
	 * draw this object's samples, using the random color
	 * @param pa
	 */
	public final void drawMeSmplsClrRnd(my_procApplet pa){		pa.shape(sampleObjPShapes[SOM_GeomObjDrawType.rndClr.getVal()]);}//
	
	/**
	 * draw this object's samples, using the location-based color
	 * @param pa
	 */
	public final void drawMeSmplsClrLoc(my_procApplet pa){		pa.shape(sampleObjPShapes[SOM_GeomObjDrawType.locClr.getVal()]);}//		
	
	public final void drawMeSmplsSelected(my_procApplet pa) {	pa.shape(sampleObjPShapes[2]);}
	
	/**
	 * draw this object's samples, using the random color
	 * @param pa
	 */
	public final void drawMySmplsLabel_2D(my_procApplet pa){
		pa.pushMatrix();pa.pushStyle();
		pa.setFill(labelClrAra,255); 
		pa.setStroke(labelClrAra,255);
		for(int i=0;i<objSamplePts.length;++i){
			SOM_GeomSamplePointf pt = objSamplePts[i];
			pa.pushMatrix(); pa.pushStyle();
			pa.translate(pt); 
			pa.text(""+pt.name, ownr.lblDist,-ownr.lblDist,0); 
			pa.popStyle();pa.popMatrix();
		}
		pa.popStyle();pa.popMatrix();
	}//

	/**
	 * draw this object's samples, using the random color
	 * @param pa
	 */
	public final void drawMySmplsLabel_3D(my_procApplet pa,SOM_AnimWorldWin animWin){
		pa.pushMatrix();pa.pushStyle();
		pa.setFill(labelClrAra,255); 
		pa.setStroke(labelClrAra,255);
		for(int i=0;i<objSamplePts.length;++i){
			SOM_GeomSamplePointf pt = objSamplePts[i];
			pa.pushMatrix(); pa.pushStyle();
			pa.translate(pt); 
			animWin.unSetCamOrient();
			pa.text(""+pt.name, ownr.lblDist,-ownr.lblDist,0); 
			pa.popStyle();pa.popMatrix();
		}
		pa.popStyle();pa.popMatrix();
	}//


}//class SOM_GeomObjSamples
