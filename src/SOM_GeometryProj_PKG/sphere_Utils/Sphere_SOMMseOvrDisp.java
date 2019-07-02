package SOM_GeometryProj_PKG.sphere_Utils;

import java.util.ArrayList;
import java.util.TreeMap;

import base_SOM_Objects.SOM_MapManager;
import base_SOM_Objects.som_examples.SOM_ExDataType;
import base_SOM_Objects.som_examples.SOM_MapNode;
import base_SOM_Objects.som_ui.SOM_MseOvrDisplay;

public class Sphere_SOMMseOvrDisp extends SOM_MseOvrDisplay {

	public Sphere_SOMMseOvrDisp(SOM_MapManager _mapMgr, float _dispThesh) {
		super(_mapMgr, _dispThesh);
	}

	@Override
	protected int[] setNodeColors() {return mapMgr.getClrFillStrkTxtAra(SOM_ExDataType.MouseOver);}

	/**
	 * instancing-specific initialization called for every data change for mouse object
	 */
	@Override
	protected void initAll_Indiv() {	}

	@Override
	protected String getFtrDispTitleString(int count) {return "Ftrs :  count : "+count;}
	/**
	 * construct per feature display value
	 * @param ftrIDX : the index in the feature vector
	 * @param ftr : the value in the ftr vector
	 * @param strongestFtrs : the map being populated with the string arrays at each ftr value
	 */
	@Override
	protected void buildPerFtrData(Integer ftrIDX, Float ftr, TreeMap<Float, ArrayList<String>> strongestFtrs) {
		ArrayList<String> vals = strongestFtrs.get(ftr);
		if(null==vals) {vals = new ArrayList<String>();}
		vals.add(""+ftrIDX);
		strongestFtrs.put(ftr, vals);
	}

	@Override
	protected String getClassProbTitleString(SOM_MapNode nearestMapNode, int ttlNumClasses) { return nearestMapNode.mapNodeCoord.toString() + " Class Probs : ("+ttlNumClasses+" classes mapped) ";}
	@Override
	protected String getCategoryProbTitleString(SOM_MapNode nearestMapNode, int ttlNumCategories) { return nearestMapNode.mapNodeCoord.toString() + " Category Probs : ("+ttlNumCategories+" categories mapped) ";}

}//Sphere_SOMMseOvrDisp
