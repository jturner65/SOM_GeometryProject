package SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs;

import java.util.HashMap;
import java.util.Map;


public enum SOM_GeomObjDrawType {
	locClr(0), rndClr(1), noFillLocClr(2), noFillRndClr(3), selected(4);
	private int value; 
	private static Map<Integer, SOM_GeomObjDrawType> map = new HashMap<Integer, SOM_GeomObjDrawType>(); 
	static { for (SOM_GeomObjDrawType enumV : SOM_GeomObjDrawType.values()) { map.put(enumV.value, enumV);}}
	private SOM_GeomObjDrawType(int _val){value = _val;} 
	public int getVal(){return value;}
	public static SOM_GeomObjDrawType getVal(int idx){return map.get(idx);}
	public static int getNumVals(){return map.size();}						//get # of values in enum
	@Override
    public String toString() { return ""+value; }	

}
