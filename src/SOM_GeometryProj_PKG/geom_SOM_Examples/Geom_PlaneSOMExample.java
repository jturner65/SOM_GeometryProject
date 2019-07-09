package SOM_GeometryProj_PKG.geom_SOM_Examples;

import java.util.TreeMap;

import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomExample;
import base_SOM_Objects.SOM_MapManager;
import base_UI_Objects.my_procApplet;
import base_Utils_Objects.vectorObjs.myPointf;
import base_Utils_Objects.vectorObjs.myVectorf;

/**
 * a training example describing a plane
 * @author john
 *
 */
public class Geom_PlaneSOMExample extends SOM_GeomExample {
	/**
	 * this is ths point closest to 0,0,0 that lies on this plane
	 */
	protected myPointf originPt;
	/**
	 * this is the vector normal of this plane - must be normalized
	 */
	protected myVectorf normVec;

	/**
	 * build a geometry-based training/validation example for the SOM
	 * @param _map owning map manager
	 * @param _originPt : the point on this plane closest to 0,0,0
	 * @param _normVec : the normal of this plane - should be normalized
	 * @param _id the unique ID info for this training example
	 * @param _clrs 4 element array of colors for this object
	 * 		idx 0 : color based on location of this element in space
	 * 		idx 1 : random color
	 * 		idx 2 : color based on owning object's location in space
	 * 		idx 3 : owning object's random color
	 */
	public Geom_PlaneSOMExample(SOM_MapManager _map, myPointf _originPt, myVectorf _normVec, String _id, int[][] _clrs) {
		super(_map, _id, _clrs);
		originPt = new myPointf(_originPt);
		normVec = new myVectorf(_normVec);
	}

	public Geom_PlaneSOMExample(Geom_PlaneSOMExample _otr) {
		super(_otr);
		originPt = _otr.originPt;
		normVec = _otr.normVec;
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
