package SOM_GeometryProj_PKG.som_geom.geom_examples;

import java.util.ArrayList;
import java.util.TreeMap;

import base_SOM_Objects.SOM_MapManager;
import base_SOM_Objects.som_examples.SOM_Example;
import base_SOM_Objects.som_examples.SOM_FtrDataType;
import base_SOM_Objects.som_examples.SOM_MapNode;
import base_Utils_Objects.vectorObjs.Tuple;

public abstract class SOM_GeomMapNode extends SOM_MapNode {
	/**
	 * a visual representation of the data in this map node
	 */
	protected SOM_GeomObj visObj;
	

	public SOM_GeomMapNode(SOM_MapManager _map, Tuple<Integer, Integer> _mapNodeLoc, SOM_FtrDataType _ftrTypeUsedToTrain,float[] _ftrs) {super(_map, _mapNodeLoc, _ftrTypeUsedToTrain,_ftrs); }

	public SOM_GeomMapNode(SOM_MapManager _map, Tuple<Integer, Integer> _mapNodeLoc, SOM_FtrDataType _ftrTypeUsedToTrain,String[] _strftrs) {super(_map, _mapNodeLoc, _ftrTypeUsedToTrain,_strftrs); }

	@Override
	protected void _initDataFtrMappings() {
	}

	@Override
	protected String getFtrWtSegment_CSVStr_Indiv(TreeMap<Float, ArrayList<String>> mapNodeProbs) {
		String res = "" + mapNodeCoord.toCSVString()+",";
		for (Float prob : mapNodeProbs.keySet()) {
			ArrayList<String> valsAtProb = mapNodeProbs.get(prob);
			String probString = ""+String.format("%1.7g", prob)+",";			
			for(String segStr : valsAtProb) {	
				//Integer jpIDX = Integer.parseInt(segStr);
				res +=probString + segStr + "," ;//+ jpJpgMon.getFtrJpByIdx(jpIDX)+ "," + jpJpgMon.getFtrJpStrByIdx_Short(jpIDX)+ ",";
			}
		}			
		return res;		
	}
	
	/**
	 * build the visualization object for this map node
	 * @return
	 */
	protected abstract SOM_GeomObj buildVisObj();

	@Override
	protected final String getFtrWtSegment_CSVStr_Hdr() {return "Map Node Loc,Probability,Ftr IDX";}

	/**
	 * get per-bmu segment descriptor, with key being either "class","category", "ftrwt" or something managed by instancing class
	 * @param segmentType "class","category", "ftrwt" or something managed by instancing class
	 * @return descriptor of this map node's full weight profile for the passed segment
	 */
	@Override
	protected final String getSegment_CSVStr_Indiv(String segmentType) {
		switch(segmentType.toLowerCase()) {
		default 		: {		return "";}//unknown type of segment returns empty string
		}
	}
	//descriptor string for any instance-specific segments
	@Override
	protected final String getSegment_Hdr_CSVStr_Indiv(String segmentType) {
		switch(segmentType.toLowerCase()) {
		default 		: {		return "";}//unknown type of segment returns empty string
		}
	}

	@Override
	//manage instancing map node handling - specifically, handle using 2ndary features as node markers (like a product tag or a class)
	//in other words, this takes the passed example's "class", in our case all the constituent points that make up the example, and assigns them to this node
	//class and category are managed by an integer key
	protected void addTrainingExToBMUs_Priv(double sqDist, SOM_Example ex) {
//		TreeMap<Tuple<Integer, Integer>, Integer> trainExOrderCounts = ((Straff_CustProspectExample)ex).getOrderCountsForExample();
//		//for each jpg-jp used in training example, assign 
//		//TreeMap<Integer, Integer> jpCountsAtJpGrp, npJpCountsAtJpGrp;
//		for (Tuple<Integer, Integer> jpgJp : trainExOrderCounts.keySet()) {
//			Integer jpg = jpgJp.x, jp = jpgJp.y;
//			Float numOrders = 1.0f*trainExOrderCounts.get(jpgJp);	
//			Float newCount = getClassSegManager().addSegDataFromTrainingEx(new Integer[] {jp}, numOrders, "_JPCount_JP_", "JP Orders present for jp :");
//			//newcount includes existing counts in this node, so needs to be used to map to category as well
			//no category yet for geometric objects
//			//Float dummy = getCategorySegManager().addSegDataFromTrainingEx(new Integer[] {jpg,jp}, newCount, "_JPGroupCount_JPG_", "JPGroup Orders present for jpg :");
//	
//		}
	}
	/**
	 * get salient name prefix for class segment for the objects mapped to this bmu
	 * @return
	 */
	@Override
	public final String getClassSegName() {return "_SrcPt_Count_Pt_";}
	/**
	 * get salient descriptions for class segment for the objects mapped to this bmu
	 * @return
	 */
	@Override
	public final String getClassSegDesc() {return "Source Pt Counts for Source Pt :";}
	
	/**
	 * get salient name prefix for category segment for the objects mapped to this bmu
	 * @return
	 */
	@Override
	public final String getCategorySegName() {return "";}
	/**
	 * get salient descriptions for category segment for the objects mapped to this bmu
	 * @return
	 */
	@Override
	public final String getCategorySegDesc() {return "";}
	@Override
	//assign relevant info to this map node from neighboring map node(s) to cover for this node not having any training examples assigned
	//only copies ex's mappings, which might not be appropriate
	protected void addMapNodeExToBMUs_Priv(double dist, SOM_MapNode ex) {
		getClassSegManager().copySegDataFromBMUMapNode(dist, ex.getMappedClassCounts(), "_Pt_ID_", "Point Name :");
		getCategorySegManager().copySegDataFromBMUMapNode(dist, ex.getMappedCategoryCounts(),"_Obj_ID_","Source Object :");
	}
	
	//by here ftrs for this map node have been built
	@Override
	protected void buildAllNonZeroFtrIDXs() {
		allNonZeroFtrIDXs = new ArrayList<Integer>();
		for(Integer idx : ftrMaps[rawftrMapTypeKey].keySet()) {		allNonZeroFtrIDXs.add(idx);	}
	}//buildAllNonZeroFtrIDXs
	/**
	 * called after features for this map node were built, but before another map node is built
	 */
	@Override
	public void postFtrVecBuild() {}

	/**
	 * called at the end of feature vector building, but before another map node is built
	 */
	@Override
	protected void _buildFeatureVectorEnd_Priv() {visObj = buildVisObj();}

	@Override
	protected String dispFtrVal(TreeMap<Integer, Float> ftrs, Integer idx) {
		return "";
	}
	/**
	 * return the appropriate string value for the dense training data - should be numeric key value to save in lrn or csv dense file
	 * @return
	 */
	@Override
	protected String getDenseTrainDataKey() {
		return OID;
	}
}
