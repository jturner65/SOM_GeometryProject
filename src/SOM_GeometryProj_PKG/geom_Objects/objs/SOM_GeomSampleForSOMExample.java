package SOM_GeometryProj_PKG.geom_Objects.objs;

import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomObj;
import base_Utils_Objects.vectorObjs.myPointf;

/**
 * This object will hold a reference to a point and the SOM_GeomObj that the point comes from
 * @author john
 *
 */
public class SOM_GeomSampleForSOMExample {
	/**
	 * point for this sample
	 */
	protected final myPointf pt;
	/**
	 * owning object for this sample
	 */
	protected final SOM_GeomObj obj;
	
	public SOM_GeomSampleForSOMExample(myPointf _pt, SOM_GeomObj _obj) {
		pt = new myPointf(_pt);
		obj = _obj;
	}
	
	public myPointf getPoint() {return pt;}
	public SOM_GeomObj getObj() {return obj;}

}//class SOM_GeomSample
