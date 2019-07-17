package SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading;

import java.util.HashMap;
import java.util.Map;
/**
 * enum delimiting tasks that can be performed by SOM_GeomObjBuilders
 * @author john
 *
 */
public enum SOM_GeomObjBldrTasks {
	buildBaseObj(0), regenSamplesBaseObj(1);
	private int value; 
	private static Map<Integer, SOM_GeomObjBldrTasks> map = new HashMap<Integer, SOM_GeomObjBldrTasks>(); 
	static { for (SOM_GeomObjBldrTasks enumV : SOM_GeomObjBldrTasks.values()) { map.put(enumV.value, enumV);}}
	private SOM_GeomObjBldrTasks(int _val){value = _val;} 
	public int getVal(){return value;}
	public static SOM_GeomObjBldrTasks getVal(int idx){return map.get(idx);}
	public static int getNumVals(){return map.size();}						//get # of values in enum
	@Override
    public String toString() { return ""+value; }	

}//GeomObjTasks
