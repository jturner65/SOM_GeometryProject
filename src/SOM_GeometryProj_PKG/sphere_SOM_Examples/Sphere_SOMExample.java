package SOM_GeometryProj_PKG.sphere_SOM_Examples;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;

import base_SOM_Objects.SOM_MapManager;
import base_SOM_Objects.som_examples.SOM_ExDataType;
import base_SOM_Objects.som_examples.SOM_Example;
import base_Utils_Objects.io.MsgCodes;
import base_Utils_Objects.vectorObjs.Tuple;
import base_Utils_Objects.vectorObjs.myVectorf;
/**
 * Training example corresponding to a point on the surface 
 * of a sphere, or to the center of the sphere
 * @author john
 *
 */
public class Sphere_SOMExample extends SOM_Example {
	/**
	 * location in the 3d world of the sample;  
	 * this corresponds to the feature vector for this example, 
	 * but should only be used for display purposes
	 */
	public myVectorf worldLoc;	
	/**
	 * color built off location of this example in 3d space
	 */
	public int[] locClrs;
	/**
	 * probability structure for this exaple - probability of every node for each 
	 * class this product covers keyed by class, value is map keyed by mapnode tuple loc, value is 
	 * probability (ratio of # of examples of key class mapped to that node over # of all examples mapped 
	 * to that node)	
	 */
	protected ConcurrentSkipListMap<Integer, ConcurrentSkipListMap<Tuple<Integer,Integer>,Float>> perClassMapNodeProbMap;
	/**
	 * probability structure for this product - probability of every node for each category 
	 * this example covers, keyed by category, value is map keyed by mapnode tuple loc, value is 
	 * probability (ratio of # of examples of key category mapped to that node over # of all 
	 * category examples mapped to that node) 
	 */
	protected ConcurrentSkipListMap<Integer, ConcurrentSkipListMap<Tuple<Integer,Integer>,Float>> perCategoryMapNodeProbMap;


	public Sphere_SOMExample(SOM_MapManager _map,myVectorf _worldLoc, int[] _locClrs, String _id) {
		super(_map, SOM_ExDataType.Training, _id);
		locClrs = _locClrs;
		worldLoc = _worldLoc;
		perClassMapNodeProbMap = new ConcurrentSkipListMap<Integer, ConcurrentSkipListMap<Tuple<Integer,Integer>,Float>>();
		perCategoryMapNodeProbMap = new ConcurrentSkipListMap<Integer, ConcurrentSkipListMap<Tuple<Integer,Integer>,Float>>();
	}

	public Sphere_SOMExample(Sphere_SOMExample _otr) {
		super(_otr);
		worldLoc = _otr.worldLoc;
		locClrs = _otr.locClrs;
		perClassMapNodeProbMap = _otr.perClassMapNodeProbMap;
		perCategoryMapNodeProbMap = _otr.perCategoryMapNodeProbMap;
	}

	@Override
	public void setSegmentsAndProbsFromBMU() {
		//build this node's segment membership and probabilities based on its BMU
		//verify not null
		perClassMapNodeProbMap.clear();		
		perCategoryMapNodeProbMap.clear();
		if(isBmuNull()) {	msgObj.dispMessage("Sphere_SOMExample","setSegmentsAndProbsFromBMU","Error !!! No BMU defined for this example : " + OID + " | Aborting further segment calculations.",MsgCodes.warning2 ); return;	}
		Tuple<Integer,Integer> bmuCoords = getBMUMapNodeCoord();
		//set all jp(class)-based map node probabilities
		ConcurrentSkipListMap<Tuple<Integer,Integer>,Float> jpClassMapNodes;
		Set<Integer> classKeySet = getBMUClassSegIDs();
		for(Integer cls : classKeySet) {
			addClassSegment(cls, getBMUClassSegment(cls));
			jpClassMapNodes = new ConcurrentSkipListMap<Tuple<Integer,Integer>,Float>();
			jpClassMapNodes.put(bmuCoords, getBMUProbForClass(cls));
			perClassMapNodeProbMap.put(cls, jpClassMapNodes);			
		}
		//set all jpgroup(category)-based map node probabilities				
		ConcurrentSkipListMap<Tuple<Integer,Integer>,Float> jpgCatMapNodes;		
		Set<Integer> catKeySet = getBMUCategorySegIDs();
		for(Integer cat : catKeySet) {
			addCategorySegment(cat, getBMUCategorySegment(cat));
			jpgCatMapNodes = new ConcurrentSkipListMap<Tuple<Integer,Integer>,Float>();
			jpgCatMapNodes.put(bmuCoords, getBMUProbForCategory(cat));
			perCategoryMapNodeProbMap.put(cat, jpgCatMapNodes);
		}		
	}

	@Override
	protected void buildFeaturesMap() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void buildStdFtrsMap() {
		//build standardized features
		calcStdFtrVector(ftrMaps[ftrMapTypeKey], ftrMaps[stdFtrMapTypeKey], mapMgr.getTrainFtrMins(),mapMgr.getTrainFtrDiffs());
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
	protected void buildAllNonZeroFtrIDXs() {
		allNonZeroFtrIDXs = new ArrayList<Integer>();
		//all idxs should be considered "non-zero", even those with zero value, since these examples are dense
		for(int i=0;i<numFtrs;++i) {allNonZeroFtrIDXs.add(i);}		
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
	public void buildCompFtrVector(float _ratio) {compFtrMaps = ftrMaps;}
	
	@Override
	protected String dispFtrVal(TreeMap<Integer, Float> ftrs, Integer idx) {
		Float ftr = ftrs.get(idx);
		return "idx : " + idx + " | val : " + String.format("%1.4g",  ftr) + " || ";
	}

}//Sphere_SOMExample
