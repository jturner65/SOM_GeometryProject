package SOM_GeometryProj_PKG.som_geom.geom_examples;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;

import SOM_GeometryProj_PKG.geom_Objects.SOM_GeomSmplForSOMExample;
import base_SOM_Objects.SOM_MapManager;
import base_SOM_Objects.som_examples.SOM_ExDataType;
import base_SOM_Objects.som_examples.SOM_Example;
import base_UI_Objects.my_procApplet;
/**
 * Training example corresponding to a sample from a geometric/graphical object
 * @author john
 *
 */
public abstract class SOM_GeomExample extends SOM_Example {
	
	/**
	 * list of point samples and their owning objects making up this training example - 
	 * these will be used to determine the classes for this object, to be passed to bmu map node for this example 
	 */
	protected final SOM_GeomSmplForSOMExample[] geomSrcSamples;	

	
	/**
	 * all class ID's this object belongs to
	 */
	protected HashSet<Integer> classIDs;
	
	/**
	 * all category ID's this object belongs to
	 */
	protected HashSet<Integer> categoryIDs;
	
	
	/**
	 * an object to restrict the bounds on this line - min,max, diff s,t value within which to sample plane
	 */
	private float[][] worldTBounds;
	
	/**
	 * build a geometry-based training/validation example for the SOM
	 * @param _map owning map manager
	 * @param _id the unique ID info for this training example
	 * @param _clrs 4 element array of colors for this object
	 * 		idx 0 : color based on location of this element in space
	 * 		idx 1 : random color
	 * 		idx 2 : color based on owning object's location in space
	 * 		idx 3 : owning object's random color
	 * @param _srcSmpls : the source points and their owning geometric objects that built this sample
	 * @param _worldBounds : bounds in sim world : 
	 * 		first idx : 0 is min; 1 is diff
	 * 		2nd idx : 0 is x, 1 is y, 2 is z (if z is present)
	 */
	public SOM_GeomExample(SOM_MapManager _map, String _id, SOM_GeomSmplForSOMExample[] _srcSmpls, float[][] _worldBounds) {
		super(_map, SOM_ExDataType.Training, _id);
		classIDs = new HashSet<Integer>();
		categoryIDs = new HashSet<Integer>();
		//done this way so can be set statically for each instancing class type
		setWorldBounds(_worldBounds);
		geomSrcSamples = _srcSmpls;
		buildExampleFromSrcObjs(_srcSmpls);
		//calculate the bounds on the possible t (or s and t) values for this object
		worldTBounds = calcTBounds();
	}//data ctor

	public SOM_GeomExample(SOM_GeomExample _otr) {
		super(_otr);
		classIDs = _otr.classIDs;
		categoryIDs = _otr.categoryIDs;
		geomSrcSamples = _otr.geomSrcSamples;
	}//copy ctor
	
	/**
	 * build this example's relevant info from passed source samples
	 * @param _srcSmpls
	 */
	protected abstract void buildExampleFromSrcObjs(SOM_GeomSmplForSOMExample[] _srcSmpls);
	
	/**
	 * call from ctor of base class, but set statically for each instancing class type
	 * @param _worldBounds
	 */
	protected abstract void setWorldBounds(float[][] _worldBounds);
	
	/**
	 * called from instancing class, once objects that make up example eq are built;
	 * calculate the bounds on s and t (or just t if appropriate) for parametric formulation of object equation
	 * worldBounds is 
	 * 		first idx 	: 0 is min; 1 is diff
	 * 		2nd idx 	: 0 is x, 1 is y, 2 is z, if appropriate
	 * result is
	 * 		first idx 	: 0==min, 1==diff
	 * 		2nd idx 	: 0==t (only 1 value)
	 * @return result array
	 */
	protected abstract float[][] calcTBounds();
	public float[][] getWorldTBounds(){return worldTBounds;}


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
	
	///////////////////
	// drawing functions in geom sim world space - drawing training example 
	
//	public final void drawMeObjLocClr(my_procApplet pa) {
//		pa.pushMatrix();pa.pushStyle();		
//		pa.setFill(objLocClr,255);
//		pa.setStroke(objLocClr,255);
//		_drawMe_Geom(pa);
//		pa.popStyle();pa.popMatrix();	
//	}
//	
//	public final void drawMeObjRandClr(my_procApplet pa) {
//		pa.pushMatrix();pa.pushStyle();		
//		pa.setFill(objRandClr,255);
//		pa.setStroke(objRandClr,255);
//		_drawMe_Geom(pa);
//		pa.popStyle();pa.popMatrix();	
//	}
//	
//	public final void drawMeMyLocClr(my_procApplet pa) {
//		pa.pushMatrix();pa.pushStyle();		
//		pa.setFill(ptLocClr,255);
//		pa.setStroke(ptLocClr,255);
//		_drawMe_Geom(pa);
//		pa.popStyle();pa.popMatrix();	
//	}
//	
//	public final void drawMeMyRandClr(my_procApplet pa) {
//		pa.pushMatrix();pa.pushStyle();		
//		pa.setFill(ptRandClr,255);
//		pa.setStroke(ptRandClr,255);
//		_drawMe_Geom(pa);
//		pa.popStyle();pa.popMatrix();	
//	}
//	
//	public final void drawMeObjLocClr_BMU(my_procApplet pa) {
//		pa.pushMatrix();pa.pushStyle();		
//		pa.setFill(objLocClr,255);
//		pa.setStroke(objLocClr,255);
//		_drawMe_Geom_BMU(pa);
//		pa.popStyle();pa.popMatrix();	
//	}
//	
//	public final void drawMeObjRandClr_BMU(my_procApplet pa) {
//		pa.pushMatrix();pa.pushStyle();		
//		pa.setFill(objRandClr,255);
//		pa.setStroke(objRandClr,255);
//		_drawMe_Geom_BMU(pa);
//		pa.popStyle();pa.popMatrix();	
//	}
//	
//	public final void drawMeMyLocClr_BMU(my_procApplet pa) {
//		pa.pushMatrix();pa.pushStyle();		
//		pa.setFill(ptLocClr,255);
//		pa.setStroke(ptLocClr,255);
//		_drawMe_Geom_BMU(pa);
//		pa.popStyle();pa.popMatrix();	
//	}
//	
//	public final void drawMeMyRandClr_BMU(my_procApplet pa) {
//		pa.pushMatrix();pa.pushStyle();		
//		pa.setFill(ptRandClr,255);
//		pa.setStroke(ptRandClr,255);
//		_drawMe_Geom_BMU(pa);
//		pa.popStyle();pa.popMatrix();	
//	}
//	
//
//	
//	/**
//	 * draw this example in the world - instance-class specific
//	 */
//	protected abstract void _drawMe_Geom(my_procApplet pa);
//
//	/**
//	 * draw this example in the world based on BMU - instance-class specific
//	 */
//	protected abstract void _drawMe_Geom_BMU(my_procApplet pa);

}//Sphere_SOMExample
