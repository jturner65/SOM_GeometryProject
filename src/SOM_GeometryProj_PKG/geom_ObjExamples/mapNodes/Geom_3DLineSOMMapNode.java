package SOM_GeometryProj_PKG.geom_ObjExamples.mapNodes;

import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_3DLineSOMExample;
import SOM_GeometryProj_PKG.geom_SOM_Mapping.mapManagers.Geom_3DLineMapMgr;
import base_Math_Objects.vectorObjs.tuples.Tuple;
import base_SOM_Objects.som_examples.enums.SOM_FtrDataType;
import base_SOM_Objects.som_geom.geom_examples.SOM_GeomMapNode;
import base_SOM_Objects.som_geom.geom_examples.SOM_GeomObj;
import base_SOM_Objects.som_managers.SOM_MapManager;

public class Geom_3DLineSOMMapNode extends SOM_GeomMapNode {

	public Geom_3DLineSOMMapNode(SOM_MapManager _map, Tuple<Integer, Integer> _mapNodeLoc,  SOM_FtrDataType _ftrTypeUsedToTrain, float[] _ftrs) {		super(_map, _mapNodeLoc, _ftrTypeUsedToTrain, _ftrs);}
	public Geom_3DLineSOMMapNode(SOM_MapManager _map, Tuple<Integer, Integer> _mapNodeLoc,  SOM_FtrDataType _ftrTypeUsedToTrain, String[] _strftrs) {	super(_map, _mapNodeLoc, _ftrTypeUsedToTrain, _strftrs);}
	
	/**
	 * build the visualization object for this map node
	 * @return
	 */
	protected final SOM_GeomObj buildVisObj() {	return new Geom_3DLineSOMExample((Geom_3DLineMapMgr) mapMgr,this);}

}//class Geom_LineSOMMapNode
