package SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs;

import base_Utils_Objects.vectorObjs.myPointf;
import base_Utils_Objects.vectorObjs.myVectorf;

/**
 * this struct just expands on a myPointf object to add a unique ID
 * 
 * @author john
 *
 */
public class SOM_GeomSamplePointf extends myPointf {
	/**
	 * unique id for this point
	 */
	public final long ID;
	protected static long idIncr = 0;
	/**
	 * display label for this point
	 */
	public final String name;
	
	/**
	 * build a Geom_SamplePoint from given coordinates
	 * @param _x : x coord
	 * @param _y : y coord
	 * @param _z : z coord
	 */
	public SOM_GeomSamplePointf(float _x, float _y, float _z, String _name){super(_x,_y,_z);name=_name; ID = idIncr++;}        						
	/**
	 * build a Geom_SamplePoint from given coordinates(as doubles)
	 * @param _x : x coord
	 * @param _y : y coord
	 * @param _z : z coord
	 */
	public SOM_GeomSamplePointf(double _x, double _y, double _z, String _name){super((float)_x,(float)_y,(float)_z); name=_name;ID = idIncr++;}          
	/**
	 * build a Geom_SamplePoint from given point
	 * @param p : point object to build Geom_SamplePoint from
	 */
	public SOM_GeomSamplePointf(myPointf p, String _name){ super(p.x, p.y, p.z); name=_name; ID = idIncr++;}                                                          
	/**
	 * copy constructor
	 * @param p : Geom_SamplePoint object to copy, with same ID
	 */
	public SOM_GeomSamplePointf(SOM_GeomSamplePointf p, String _name){ super(p.x, p.y, p.z); name=_name; ID = p.ID;}                                                   
	/**
	 * build Geom_SamplePoint as displacement from point A by vector B
	 * @param A : starting point
	 * @param B : displacement vector
	 */
	public SOM_GeomSamplePointf(myPointf A, myVectorf B, String _name) {super(A.x+B.x,A.y+B.y,A.z+B.z);  name=_name;ID = idIncr++;};
	/**
	 * Interpolate between A and B by s -> (0->1)
	 * @param A : first point to interpolate from
	 * @param s : value [0,1] to determine linear interpolation
	 * @param B : second point to interpolate from
	 */
	public SOM_GeomSamplePointf(myPointf A, float s, myPointf B, String _name) {super(A.x+s*(B.x-A.x),A.y+s*(B.y-A.y),A.z+s*(B.z-A.z));  name=_name;ID = idIncr++;};	
	/**
	 * constructor from csv string
	 * @param _csvStrAra : array of string data in format of toCSVStr output :
	 * 	idx 0,1,2 : x,y,z
	 */
	public SOM_GeomSamplePointf(String[] _csvStrAra) {
		super(Float.parseFloat(_csvStrAra[0].trim()),Float.parseFloat(_csvStrAra[1].trim()),Float.parseFloat(_csvStrAra[2].trim()) );
		ID = Integer.parseInt(_csvStrAra[3].trim());
		idIncr = ID +1;
		name = _csvStrAra[4].trim();
	}
	
	/**
	 * empty constructor
	 */
	public SOM_GeomSamplePointf(String _name){ super(0,0,0);name=_name;ID = idIncr++;}   	
	
	public final String toCSVHeaderStr() {return "x,y,z,ID,Name,";}
	
	public final String toCSVStr() {
		String res = super.toStrCSV("%.8f") +","+ID+","+name+",";
		return res;
	}
	
}//Geom_SamplePoint
