package SOM_GeometryProj_PKG.geom_SOM_Examples;

import java.util.TreeMap;

import SOM_GeometryProj_PKG.geom_Objects.SOM_GeomSmplForSOMExample;
import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomExample;
import base_SOM_Objects.SOM_MapManager;
import base_UI_Objects.my_procApplet;
import base_Utils_Objects.vectorObjs.myPointf;
import base_Utils_Objects.vectorObjs.myVectorf;

/**
 * a training example describing part or all of a sphere
 * @author john
 *
 */
public class Geom_SphereSOMExample extends SOM_GeomExample {
	/**
	 * owning sphere's 3d center location
	 */
	protected myPointf objCtrLoc;
	/**
	 * my 3d location
	 */
	protected myPointf myLoc;
	
	/**
	 * owning sphere's radius
	 */
	protected float sphereRad;

	/**
	 * coordinate bounds for lines in sim/geom world - each object/example type has its own world bounds
	 * 		first idx : 0 is min; 1 is diff
	 * 		2nd idx : 0 is x, 1 is y, 2 is z (if z is present)
	 */
	protected static float[][] worldBounds = null;

	/**
	 * build a geometry-based training/validation example for the SOM
	 * @param _map owning map manager
	 * @param _objCtrLoc : owning sphere center world location
	 * @param _myLoc : this example's world location
	 * @param _id the unique ID info for this training example
	 * @param _clrs 4 element array of colors for this object
	 * 		idx 0 : color based on location of this element in space
	 * 		idx 1 : random color
	 * 		idx 2 : color based on owning object's location in space
	 * 		idx 3 : owning object's random color
	 */
	public Geom_SphereSOMExample(SOM_MapManager _map, String _id, SOM_GeomSmplForSOMExample[] _srcSmpls, float[][] _worldBounds) {
		super(_map, _id, _srcSmpls, _worldBounds);
	}

	public Geom_SphereSOMExample(Geom_SphereSOMExample _otr) {
		super(_otr);
		objCtrLoc = _otr.objCtrLoc;
		myLoc = _otr.myLoc;
		sphereRad = _otr.sphereRad;
	}

	/**
	 * build this example from passed source samples
	 * @param _srcSmpls
	 */
	protected final void buildExampleFromSrcObjs(SOM_GeomSmplForSOMExample[] _srcSmpls) {
		
		
	}//buildExampleFromSrcObjs
	
	/**
	 * calculate the bounds on s and t (if appropriate) for parametric formulation of object equation
	 * worldBounds is 
	 * 		first idx 	: 0 is min; 1 is diff
	 * 		2nd idx 	: 0 is x, 1 is y, 2 is z
	 * result is
	 * 		first idx 	: 0==min, 1==diff
	 * 		2nd idx 	: 0==s, 1==t
	 * @return result array
	 */
	@Override	protected float[][] calcTBounds() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * call from ctor of base class, but set statically for each instancing class type
	 * @param _worldBounds
	 */
	protected final void setWorldBounds(float[][]_worldBounds) {
		if(null!=worldBounds) {return;}
		worldBounds = new float[_worldBounds.length][];
		for(int i=0;i<worldBounds.length;++i) {
			float[] tmp = new float[_worldBounds[i].length];
			for(int j=0;j<tmp.length;++j) {	tmp[j]=_worldBounds[i][j];}
			worldBounds[i]=tmp;
		}
	}//setWorldBounds
//	/**
//	 * draw this example in the world - instance-class specific
//	 */
//	@Override
//	protected void _drawMe_Geom(my_procApplet pa) {
//		// TODO Auto-generated method stub
//
//	}
//
//	/**
//	 * draw this example in the world based on BMU - instance-class specific
//	 */
//	@Override
//	protected void _drawMe_Geom_BMU(my_procApplet pa) {
//		// TODO Auto-generated method stub
//
//	}

	@Override
	protected void buildFeaturesMap() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getPreProcDescrForCSV() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRawDescColNamesForCSV() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void finalizeBuildBeforeFtrCalc() {
		// TODO Auto-generated method stub

	}

	@Override
	public void postFtrVecBuild() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void _buildFeatureVectorEnd_Priv() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setIsTrainingDataIDX_Priv() {
		// TODO Auto-generated method stub

	}

	@Override
	public TreeMap<Integer, Integer> getTrainingLabels() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void buildCompFtrVector(float _ratio) {
		// TODO Auto-generated method stub

	}

}
