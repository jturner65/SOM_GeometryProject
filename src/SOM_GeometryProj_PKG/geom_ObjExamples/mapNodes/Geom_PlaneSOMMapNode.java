package SOM_GeometryProj_PKG.geom_ObjExamples.mapNodes;

import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_PlaneSOMExample;
import base_SOM_Objects.SOM_MapManager;
import base_SOM_Objects.som_examples.SOM_FtrDataType;
import base_SOM_Objects.som_geom.SOM_GeomMapManager;
import base_SOM_Objects.som_geom.geom_examples.SOM_GeomMapNode;
import base_SOM_Objects.som_geom.geom_examples.SOM_GeomObj;
import base_Utils_Objects.vectorObjs.Tuple;

public class Geom_PlaneSOMMapNode extends SOM_GeomMapNode {

	public Geom_PlaneSOMMapNode(SOM_MapManager _map, Tuple<Integer, Integer> _mapNodeLoc,  SOM_FtrDataType _ftrTypeUsedToTrain, float[] _ftrs) {		super(_map, _mapNodeLoc, _ftrTypeUsedToTrain, _ftrs);}
	public Geom_PlaneSOMMapNode(SOM_MapManager _map, Tuple<Integer, Integer> _mapNodeLoc,  SOM_FtrDataType _ftrTypeUsedToTrain, String[] _strftrs) {	super(_map, _mapNodeLoc, _ftrTypeUsedToTrain, _strftrs);}
	/**
	 * build the visualization object for this map node
	 * @return
	 */
	protected final SOM_GeomObj buildVisObj() {	return new Geom_PlaneSOMExample((SOM_GeomMapManager) mapMgr,this);}
}//class Geom_PlaneSOMMapNode