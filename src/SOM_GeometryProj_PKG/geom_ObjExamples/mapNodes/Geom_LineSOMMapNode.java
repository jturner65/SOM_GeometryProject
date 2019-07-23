package SOM_GeometryProj_PKG.geom_ObjExamples.mapNodes;

import SOM_GeometryProj_PKG.geom_ObjExamples.Geom_LineSOMExample;
import SOM_GeometryProj_PKG.som_geom.SOM_GeomMapManager;
import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomMapNode;
import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomObj;
import base_SOM_Objects.SOM_MapManager;
import base_Utils_Objects.vectorObjs.Tuple;

public class Geom_LineSOMMapNode extends SOM_GeomMapNode {

	public Geom_LineSOMMapNode(SOM_MapManager _map, Tuple<Integer, Integer> _mapNodeLoc, float[] _ftrs) {		super(_map, _mapNodeLoc, _ftrs);}

	public Geom_LineSOMMapNode(SOM_MapManager _map, Tuple<Integer, Integer> _mapNodeLoc, String[] _strftrs) {	super(_map, _mapNodeLoc, _strftrs);}
	
	/**
	 * build the visualization object for this map node
	 * @return
	 */
	protected final SOM_GeomObj buildVisObj() {	return new Geom_LineSOMExample((SOM_GeomMapManager) mapMgr,this);}

}//class Geom_LineSOMMapNode
