package SOM_GeometryProj_PKG.geom_SOM_Examples;

import java.util.TreeMap;

import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomExample;
import base_SOM_Objects.SOM_MapManager;
import base_UI_Objects.my_procApplet;
import base_Utils_Objects.vectorObjs.myPointf;
import base_Utils_Objects.vectorObjs.myVectorf;

/**
 * a traning example describing part or all of a sphere
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
	public Geom_SphereSOMExample(SOM_MapManager _map, myPointf _objCtrLoc, myPointf _myLoc, float _rad, String _id, int[][] _clrs) {
		super(_map, _id, _clrs);
		objCtrLoc = new myPointf(_objCtrLoc);
		myLoc = _myLoc;
		sphereRad = _rad;
	}

	public Geom_SphereSOMExample(Geom_SphereSOMExample _otr) {
		super(_otr);
		objCtrLoc = _otr.objCtrLoc;
		myLoc = _otr.myLoc;
		sphereRad = _otr.sphereRad;
	}

	/**
	 * draw this example in the world - instance-class specific
	 */
	@Override
	protected void _drawMe_Geom(my_procApplet pa) {
		// TODO Auto-generated method stub

	}

	/**
	 * draw this example in the world based on BMU - instance-class specific
	 */
	@Override
	protected void _drawMe_Geom_BMU(my_procApplet pa) {
		// TODO Auto-generated method stub

	}

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
