package SOM_GeometryProj_PKG.geom_Obj.base;

import base_SOM_Objects.SOM_MapManager;

/**
 * class to instance the base functionality of a geometric object represented by 
 * some parameters and also by samples for use in training and consuming a SOM
 * 
 * @author john
 */
public abstract class SOM_GeomObj {
	public final int ID;
	public static int IDGen = 0;
	/**
	 * reference to the owning map manager
	 */
	public static SOM_MapManager mapMgr;
	
	
	
	public SOM_GeomObj(SOM_MapManager _mapMgr) {
		ID = IDGen++;mapMgr = _mapMgr;
		
		
	}

}//class SOM_GeomObj
