package SOM_GeometryProj_PKG.geom_ObjExamples;

import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomObj;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomObjTypes;
import base_Utils_Objects.vectorObjs.myPointf;

/**
 * This object will hold a reference to a point and the SOM_GeomObj that the point comes from
 * A single SOM Example will be made up of 2 or more of these : 
 * 		2 for lines, 
 * 		3 for planes,		(triangle)
 * 		4 non-coplanar points for spheres (non-degenerate tet)
 * @author john
 */
public class Geom_SmplDataForSOMExample {
	/**
	 * point for this sample
	 */
	protected final myPointf pt;
	/**
	 * owning object for this sample
	 */
	protected final SOM_GeomObj obj;
	
	public Geom_SmplDataForSOMExample(SOM_GeomObj _obj, myPointf _pt) {
		obj = _obj;
		pt = new myPointf(_pt);
	}
	
	public myPointf getPoint() {return pt;}
	public SOM_GeomObj getObj() {return obj;}
	
	public String toCSVStr_Header() {	return "smpl pt x, smpl pt y, smpl pt z, "+ obj.toCSVStr_Header();}
	
	public String toCSVStr() {			return  pt.x + ", " + pt.y + ", " + pt.z + ", " + obj.toCSVStr();}

}//class SOM_GeomSample
