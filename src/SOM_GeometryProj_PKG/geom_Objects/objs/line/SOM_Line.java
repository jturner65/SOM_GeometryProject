package SOM_GeometryProj_PKG.geom_Objects.objs.line;

import java.util.concurrent.ThreadLocalRandom;

import SOM_GeometryProj_PKG.geom_Objects.objs.base.SOM_GeomObj;
import SOM_GeometryProj_PKG.geom_SOM_Examples.Geom_LineSOMExample;
import SOM_GeometryProj_PKG.geom_SOM_Examples.base.Geom_SOMExample;
import base_SOM_Objects.SOM_MapManager;
import base_UI_Objects.my_procApplet;
import base_Utils_Objects.MyMathUtils;
import base_Utils_Objects.vectorObjs.myPointf;
import base_Utils_Objects.vectorObjs.myVectorf;

public class SOM_Line extends SOM_GeomObj {
	public final int ID;
	private static int IDGen = 0;

	/**
	 * given points that make up this line
	 */
	public final myPointf[] pts;
	/**
	 * direction vector for this line
	 */
	public final myVectorf dir;
	/**
	 * point closest to 0,0 for this line
	 */
	public final myPointf origin;
	
	/**
	 * an object to restrict the bounds on this line - min,max, diff t value within which to sample line
	 */
	public final float[] worldTBounds;
	/**
	 * Constructor for line object
	 * @param _pa papplet
	 * @param _mapMgr owning som map manager
 	 * @param _a, _b : 2 points on line
	 * @param _locClrAra color based on location
	 * @param _worldBnds 2 points that bound the component of the line we wish to learn about/display - MUST BE ON LINE
	 */
	public SOM_Line(my_procApplet _pa, SOM_MapManager _mapMgr, myPointf _a, myPointf _b, int _numSmplPts, int[] _locClrAra, myPointf[] _worldBnds) {
		super(_pa, _mapMgr, _locClrAra);
		ID = IDGen++;
		pts = new myPointf[] {new myPointf(_a),new myPointf(_b)};
		pts[0].z = 0.0f;
		pts[1].z = 0.0f;
		//z is always 0 - making this in 2 d
		dir=new myVectorf(_a,_b);
		dir.z = 0;
		dir._normalize();	
		
		origin = findClosestPointOnLine(myPointf.ZEROPT);
		origin.z = 0;
		worldTBounds = new float[3];
		worldTBounds[0] = getTForPointOnLine(_worldBnds[0]);		//min
		worldTBounds[1] = getTForPointOnLine(_worldBnds[1]);		//max
		worldTBounds[2] = worldTBounds[1] - worldTBounds[0];		//diff
		
		super.buildObjExamples(_numSmplPts);
	}//ctor	
	/**
	 * increment this object type's ID
	 */
	protected final void incrID() {
		
	}

	public myPointf findClosestPointOnLine(myPointf p) {
		//find projection t of vector ap (from a to p) on dir, then find a + t * dir
		myVectorf proj = new myVectorf(pts[0],p);
		return myVectorf._add(pts[0], proj._dot(dir), dir);
	}
	/**
	 * return a point on the line 
	 * @param t
	 * @return
	 */
	public myPointf getPointOnLine(float t) {return myVectorf._add(pts[0], t, dir);}
	
	/**
	 * find t value for point on line - expects point to be on line!
	 * @param pt
	 * @return
	 */
	public float getTForPointOnLine(myPointf pt) {
		//pt  == origin  + t * dir
		// -> t = (pt.x - origin.x)/dir.x 
		float t = 0.0f;
		if(Math.abs(dir.x)> MyMathUtils.eps) {	t = (pt.x - origin.x)/dir.x;} 
		else {									t = (pt.y - origin.y)/dir.y;}//vertical line			
		return t;
	}
	
	
	/**
	 * get an appropriate sample location to build sample sets, based on what kind of object is being built
	 * @return
	 */
	/**
	 * build a geometry-based training/validation example for the SOM
	 * @param _map owning map manager
	 * @param _originPt : the point on this example line closest to 0,0
	 * @param _dirVec : the direction vector of the line for this example - should be normalized
	 * @param _id the unique ID info for this training example
	 * @param _clrs 4 element array of colors for this object
	 * 		idx 0 : color based on location of this element in space
	 * 		idx 1 : random color
	 * 		idx 2 : color based on owning object's location in space
	 * 		idx 3 : owning object's random color
	 */
	@Override
	protected Geom_SOMExample buildSample(int i) {
		return new Geom_LineSOMExample(mapMgr, origin, dir, "Line_"+ID + "_Smp_" + i, new int[][] {locClrAra, rndClrAra,locClrAra, rndClrAra});
	}
	@Override
	protected Geom_SOMExample buildObjExample() {
		return new Geom_LineSOMExample(mapMgr, origin, dir, "Line "+ID, new int[][] {locClrAra, rndClrAra,locClrAra, rndClrAra});
	}
	/**
	 * return a random point on this object
	 */
	@Override
	public final myPointf getRandPointOnObj() {
		float t = ((float) ThreadLocalRandom.current().nextFloat() *worldTBounds[2])+worldTBounds[0];
		return new myPointf(origin, t, dir);
	}

	@Override
	public void drawMeLabel(my_procApplet pa) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawMeClrRnd(my_procApplet pa) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawMeClrLoc(my_procApplet pa) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawMeSelected(my_procApplet pa, float animTmMod) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawMeLabel_BMU(my_procApplet pa) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawMeClrRnd_BMU(my_procApplet pa) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawMeClrLoc_BMU(my_procApplet pa) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawMeSelected_BMU(my_procApplet pa, float animTmMod) {
		// TODO Auto-generated method stub

	}

}
