package SOM_GeometryProj_PKG.som_geom.geom_examples;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;

import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_SmplDataForSOMExample;
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
	protected final Geom_SmplDataForSOMExample[] geomSrcSamples;	

	
	/**
	 * all class ID's this object belongs to
	 */
	protected HashSet<Integer> classIDs;
	
	/**
	 * all category ID's this object belongs to
	 */
	protected HashSet<Integer> categoryIDs;
	
	
	/**
	 * an parameterized bounds to restrict the allowable region of this object in the world
	 */
	private float[][] worldTBounds;
	
	/**
	 * build a geometry-based training/validation example for the SOM
	 * @param _map owning map manager
	 * @param _type data type of example (training, testing, validation, etc)
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
	public SOM_GeomExample(SOM_MapManager _map, SOM_ExDataType _type, String _id, Geom_SmplDataForSOMExample[] _srcSmpls, float[][] _worldBounds) {
		super(_map, _type, _id);
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
	protected abstract void buildExampleFromSrcObjs(Geom_SmplDataForSOMExample[] _srcSmpls);
	
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
	


}//Sphere_SOMExample
