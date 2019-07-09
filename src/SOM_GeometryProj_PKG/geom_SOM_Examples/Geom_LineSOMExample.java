package SOM_GeometryProj_PKG.geom_SOM_Examples;

import java.util.TreeMap;

import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomExample;
import base_SOM_Objects.SOM_MapManager;
import base_UI_Objects.my_procApplet;
import base_Utils_Objects.vectorObjs.myPointf;
import base_Utils_Objects.vectorObjs.myVectorf;

/**
 * a traning example describing a line
 * @author john
 *
 */
public class Geom_LineSOMExample extends SOM_GeomExample {
	/**
	 * this is the point closest to 0,0 that lies on this line
	 */
	protected final myPointf originPt;
	/**
	 * this is the direction vector of this line - must be normalized
	 */
	protected final myVectorf dirVec;
	
	/**
	 * build a geometry-based training/validation example for the SOM
	 * @param _map owning map manager
	 * @param _originPt : the point on this example line closest to 0,0
	 * @param _dirVec : the direction vector of the line for this example - should be normalized
	 * @param _id the unique ID info for this training example
	 * @param _clrs 4 element array of colors for this object
	 * 		idx 0 : color based on location of this element in space
	 * 		idx 1 : random color
	 * 		idx 2 : color based on owning object's location in space
	 * 		idx 3 : owning object's random color
	 */

	public Geom_LineSOMExample(SOM_MapManager _map, myPointf _originPt, myVectorf _dirVec, String _id, int[][] _clrs) {
		super(_map, _id, _clrs);
		originPt = new myPointf(_originPt);
		dirVec = new myVectorf(_dirVec);
		dirVec._normalize();
	}

	public Geom_LineSOMExample(Geom_LineSOMExample _otr) {
		super(_otr);
		originPt = _otr.originPt;
		dirVec = _otr.dirVec;
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
