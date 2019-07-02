package SOM_GeometryProj_PKG.geom_SOM_Examples;

import java.util.ArrayList;
import java.util.TreeMap;

import base_SOM_Objects.SOM_MapManager;
import base_SOM_Objects.som_examples.SOM_Example;
import base_SOM_Objects.som_examples.SOM_MapNode;
import base_Utils_Objects.vectorObjs.Tuple;
import base_Utils_Objects.vectorObjs.myVectorf;

public class Geom_SOMMapNode extends SOM_MapNode {
	/**
	 * location in the 3d world of the sample;  
	 * this corresponds to the feature vector for this example, 
	 * but should only be used for display purposes
	 */
	public myVectorf worldLoc;	

	public Geom_SOMMapNode(SOM_MapManager _map, Tuple<Integer, Integer> _mapNodeLoc, float[] _ftrs) {super(_map, _mapNodeLoc, _ftrs);   worldLoc = new myVectorf(_ftrs[0],_ftrs[1],_ftrs[2]);}

	public Geom_SOMMapNode(SOM_MapManager _map, Tuple<Integer, Integer> _mapNodeLoc, String[] _strftrs) {super(_map, _mapNodeLoc, _strftrs);float[] _ftrs = this.getFtrs();worldLoc = new myVectorf(_ftrs[0],_ftrs[1],_ftrs[2]);}

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
	//in other words, this takes the passed example's "class" in our case all the order jps, and assigns them to this node
	protected void addTrainingExToBMUs_Priv(double dist, SOM_Example ex) {
	}

	@Override
	//assign relevant info to this map node from neighboring map node(s) to cover for this node not having any training examples assigned
	//only copies ex's mappings, which might not be appropriate
	protected void addMapNodeExToBMUs_Priv(double dist, SOM_MapNode ex) {
	}
	
	//by here ftrs for this map node have been built
	@Override
	protected void buildAllNonZeroFtrIDXs() {
		allNonZeroFtrIDXs = new ArrayList<Integer>();
		for(Integer idx : ftrMaps[ftrMapTypeKey].keySet()) {		allNonZeroFtrIDXs.add(idx);	}
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
	protected void _buildFeatureVectorEnd_Priv() {}

	@Override
	protected String dispFtrVal(TreeMap<Integer, Float> ftrs, Integer idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
