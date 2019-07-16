package SOM_GeometryProj_PKG.geom_ObjExamples;

import java.util.HashMap;
import java.util.Map;


public enum GeomObjDrawType {
	locClr(0), rndClr(1), noFillLocClr(2), noFillRndClr(3), selected(4);
	private int value; 
	private static Map<Integer, GeomObjDrawType> map = new HashMap<Integer, GeomObjDrawType>(); 
	static { for (GeomObjDrawType enumV : GeomObjDrawType.values()) { map.put(enumV.value, enumV);}}
	private GeomObjDrawType(int _val){value = _val;} 
	public int getVal(){return value;}
	public static GeomObjDrawType getVal(int idx){return map.get(idx);}
	public static int getNumVals(){return map.size();}						//get # of values in enum
	@Override
    public String toString() { return ""+value; }	

}
