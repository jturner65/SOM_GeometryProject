package SOM_GeometryProj_PKG.geom_SOM_Examples.base;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;

import SOM_GeometryProj_PKG.geom_Objects.SOM_GeomSampleForSOMExample;
import base_SOM_Objects.SOM_MapManager;
import base_SOM_Objects.som_examples.SOM_ExDataType;
import base_SOM_Objects.som_examples.SOM_Example;
import base_UI_Objects.my_procApplet;
/**
 * Training example corresponding to a sample from a geometric/graphical object
 * @author john
 *
 */
public abstract class Geom_SOMExample extends SOM_Example {
	
	/**
	 * this example's world color
	 */
	protected final int[] ptLocClr;
	
	/**
	 * this example's individual random color
	 */
	protected final int[] ptRandClr;
	
	/**
	 * this example's owning object's world color
	 */
	protected final int[] objLocClr;
	
	/**
	 * this example's owning object's random color
	 */
	protected final int[] objRandClr;
	
	/**
	 * list of point samples and their owning objects making up this training example - these will be used to determine the classes for this object, to be passed to bmu map node for this example
	 */
	protected ArrayList<SOM_GeomSampleForSOMExample> samples;
	
	
	/**
	 * all class ID's this object belongs to
	 */
	protected HashSet<Integer> classIDs;
	
	/**
	 * all category ID's this object belongs to
	 */
	protected HashSet<Integer> categoryIDs;
	
	/**
	 * build a geometry-based training/validation example for the SOM
	 * @param _map owning map manager
	 * @param _id the unique ID info for this training example
	 * @param _clrs 4 element array of colors for this object
	 * 		idx 0 : color based on location of this element in space
	 * 		idx 1 : random color
	 * 		idx 2 : color based on owning object's location in space
	 * 		idx 3 : owning object's random color
	 */
	public Geom_SOMExample(SOM_MapManager _map,String _id, int[][] _clrs) {
		super(_map, SOM_ExDataType.Training, _id);
		ptLocClr = _clrs[0];
		ptRandClr = _clrs[1];
		objLocClr = _clrs[2];
		objRandClr = _clrs[3];
		classIDs = new HashSet<Integer>();
		categoryIDs = new HashSet<Integer>();
		
		
	}

	public Geom_SOMExample(Geom_SOMExample _otr) {
		super(_otr);
		ptLocClr = _otr.ptLocClr;
		ptRandClr = _otr.ptRandClr;
		objLocClr = _otr.objLocClr;
		objRandClr = _otr.objRandClr;
		classIDs = _otr.classIDs;
		categoryIDs = _otr.categoryIDs;
		samples = _otr.samples;
	}

	@Override
	protected final void buildStdFtrsMap() {	//build standardized features
		calcStdFtrVector(ftrMaps[ftrMapTypeKey], ftrMaps[stdFtrMapTypeKey], mapMgr.getTrainFtrMins(),mapMgr.getTrainFtrDiffs());
	}	
	
	/**
	 * Return all the relevant classes found in this example/that this example belongs 
	 * to, for class segment calculation
	 * @return class IDs present in this example
	 */	
	@Override
	protected final HashSet<Integer> getAllClassIDsForClsSegmentCalc() {		return classIDs;}

	/**
	 * Return all the relevant categories found in this example or that this example 
	 * belongs to, for category segment membership calculation
	 * @return category IDs present in this example
	 */	
	@Override
	protected final HashSet<Integer> getAllCategoryIDsForCatSegmentCalc() {		return categoryIDs;}

	@Override
	protected final void buildAllNonZeroFtrIDXs() {
		allNonZeroFtrIDXs = new ArrayList<Integer>();
		//all idxs should be considered "non-zero", even those with zero value, since these examples are dense
		for(int i=0;i<numFtrs;++i) {allNonZeroFtrIDXs.add(i);}		
	}

	
	@Override
	protected final String dispFtrVal(TreeMap<Integer, Float> ftrs, Integer idx) {
		Float ftr = ftrs.get(idx);
		return "idx : " + idx + " | val : " + String.format("%1.4g",  ftr) + " || ";
	}
	
	public final void drawMeObjLocClr(my_procApplet pa, int det) {
		pa.pushMatrix();pa.pushStyle();		
		pa.setFill(objLocClr,255);
		pa.setStroke(objLocClr,255);
		pa.sphereDetail(det);
		_drawMe_Geom(pa);
		pa.popStyle();pa.popMatrix();	
	}
	
	public final void drawMeObjRandClr(my_procApplet pa, int det) {
		pa.pushMatrix();pa.pushStyle();		
		pa.setFill(objRandClr,255);
		pa.setStroke(objRandClr,255);
		pa.sphereDetail(det);
		_drawMe_Geom(pa);
		pa.popStyle();pa.popMatrix();	
	}
	
	public final void drawMeMyLocClr(my_procApplet pa, int det) {
		pa.pushMatrix();pa.pushStyle();		
		pa.setFill(ptLocClr,255);
		pa.setStroke(ptLocClr,255);
		pa.sphereDetail(det);
		_drawMe_Geom(pa);
		pa.popStyle();pa.popMatrix();	
	}
	
	public final void drawMeMyRandClr(my_procApplet pa, int det) {
		pa.pushMatrix();pa.pushStyle();		
		pa.setFill(ptRandClr,255);
		pa.setStroke(ptRandClr,255);
		pa.sphereDetail(det);
		_drawMe_Geom(pa);
		pa.popStyle();pa.popMatrix();	
	}
	
	public final void drawMeObjLocClr_BMU(my_procApplet pa, int det) {
		pa.pushMatrix();pa.pushStyle();		
		pa.setFill(objLocClr,255);
		pa.setStroke(objLocClr,255);
		pa.sphereDetail(det);
		_drawMe_Geom_BMU(pa);
		pa.popStyle();pa.popMatrix();	
	}
	
	public final void drawMeObjRandClr_BMU(my_procApplet pa, int det) {
		pa.pushMatrix();pa.pushStyle();		
		pa.setFill(objRandClr,255);
		pa.setStroke(objRandClr,255);
		pa.sphereDetail(det);
		_drawMe_Geom_BMU(pa);
		pa.popStyle();pa.popMatrix();	
	}
	
	public final void drawMeMyLocClr_BMU(my_procApplet pa, int det) {
		pa.pushMatrix();pa.pushStyle();		
		pa.setFill(ptLocClr,255);
		pa.setStroke(ptLocClr,255);
		pa.sphereDetail(det);
		_drawMe_Geom_BMU(pa);
		pa.popStyle();pa.popMatrix();	
	}
	
	public final void drawMeMyRandClr_BMU(my_procApplet pa, int det) {
		pa.pushMatrix();pa.pushStyle();		
		pa.setFill(ptRandClr,255);
		pa.setStroke(ptRandClr,255);
		pa.sphereDetail(det);
		_drawMe_Geom_BMU(pa);
		pa.popStyle();pa.popMatrix();	
	}
	

	
	/**
	 * draw this example in the world - instance-class specific
	 */
	protected abstract void _drawMe_Geom(my_procApplet pa);

	/**
	 * draw this example in the world based on BMU - instance-class specific
	 */
	protected abstract void _drawMe_Geom_BMU(my_procApplet pa);

}//Sphere_SOMExample
