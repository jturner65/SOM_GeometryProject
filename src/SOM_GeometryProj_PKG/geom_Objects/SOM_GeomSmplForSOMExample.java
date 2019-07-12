package SOM_GeometryProj_PKG.geom_Objects;

import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomObj;
import base_Utils_Objects.vectorObjs.myPointf;

/**
 * This object will hold a reference to a point and the SOM_GeomObj that the point comes from
 * A single SOM Example will be made up of 2 or more of these : 
 * 		2 for lines, 
 * 		3 for planes,		(triangle)
 * 		4 non-coplanar points for spheres (non-degenerate tet)
 * @author john
 */
public class SOM_GeomSmplForSOMExample {
	/**
	 * point for this sample
	 */
	protected final myPointf pt;
	/**
	 * owning object for this sample
	 */
	protected final SOM_GeomObj obj;
	
	public SOM_GeomSmplForSOMExample(SOM_GeomObj _obj, myPointf _pt) {
		obj = _obj;
		pt = new myPointf(_pt);
	}
	
	public myPointf getPoint() {return pt;}
	public SOM_GeomObj getObj() {return obj;}

}//class SOM_GeomSample
