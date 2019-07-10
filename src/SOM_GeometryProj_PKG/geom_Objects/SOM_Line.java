package SOM_GeometryProj_PKG.geom_Objects;

import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import SOM_GeometryProj_PKG.geom_SOM_Examples.Geom_LineSOMExample;
import SOM_GeometryProj_PKG.som_geom.SOM_GeomMapManager;
import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomExample;
import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_objs.SOM_GeomObj;
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
	 * display points for this line to draw maximally
	 */
	public final myPointf[] dispPts;
	
	/**
	 * Constructor for line object
	 * @param _mapMgr owning som map manager
 	 * @param _a, _b : 2 points on line
 	 * @param _numSmplPts : # of points to build
	 * @param _locClrAra color based on location
	 * @param _worldBounds 2d array of bounds for where reasonable points should be generated
	 * 		first idx 	: 0 is min; 1 is diff
	 * 		2nd idx 	: 0 is x, 1 is y
	 */
	public SOM_Line(SOM_GeomMapManager _mapMgr, myPointf _a, myPointf _b, int _numSmplPts, float[][] _worldBounds) {
		super(_mapMgr, _a, _worldBounds, false);
		ID = IDGen++;
		pts = new myPointf[] {new myPointf(_a),new myPointf(_b)};
		pts[0].z = 0.0f;
		pts[1].z = 0.0f;
		//z is always 0 - making this in 2 d
		dir=new myVectorf(_a,_b);
		dir.z = 0;
		dir._normalize();	
		//origin is closest point to 0,0 on line
		origin = findClosestPointOnLine(myPointf.ZEROPT);
		origin.z = 0;
		
		super.buildInitObjAndSamples(_numSmplPts);
		float[][] wb = getWorldTBounds();
		dispPts = new myPointf[2];
		dispPts[0] = getPointOnLine(wb[0][0]);
		dispPts[1] = getPointOnLine(wb[0][0] + wb[1][0]);
 
	}//ctor	
	/**
	 * calculate the bounds on s and t (if appropriate) for parametric formulation of object equation
	 * worldBounds is 
	 * 		first idx 	: 0 is min; 1 is diff
	 * 		2nd idx 	: 0 is x, 1 is y
	 * result is
	 * 		first idx 	: 0==min, 1==diff
	 * 		2nd idx 	: 0==t (only 1 value)
	 * @return result array
	 */
	protected final float[][] calcTBounds(){
		float[] ptA_ara = pts[0].asArray(), dirAra = dir.asArray();
		//eq  pt = pta + t * dir -> t = (pt-pta)/dir for each dof
		//mins has location for each dof
		float[] mins = worldBounds[0];		
		float[][] res = new float[2][mins.length-1];
		for(int i=0;i<mins.length-1;++i) {
			res[0][i]=100000000.0f;			
		}
		//for every bound, set t value in bound's ortho dim
		float[] tAtBounds = new float[worldBounds[0].length * worldBounds.length];
		TreeMap<Float, Integer> tmpBnds = new TreeMap<Float, Integer>();
		float curMax;
	
		for(int i=0;i<mins.length;++i) {
			tmpBnds.put((mins[i] - ptA_ara[i])/dirAra[i], 2*i);
			curMax=worldBounds[0][i] + worldBounds[1][i];
			tmpBnds.put((curMax - ptA_ara[i])/dirAra[i], 1 + 2*i);
		}
		//want 2 points closest to 0 - 2 will be negative, and 2 will be positive if a is within bounds
		tmpBnds.remove(tmpBnds.firstKey());
		tmpBnds.remove(tmpBnds.lastKey());
		float a = tmpBnds.firstKey();
		float b = tmpBnds.lastKey();
		if(a < b) {
			res[0][0] = a;
			res[1][0] = b-a;			
		} else {			
			res[0][0] = b;
			res[1][0] = a-b;
		}	
		return res;
	}//calcTBounds()

	public final myPointf findClosestPointOnLine(myPointf p) {
		//find projection t of vector ap (from a to p) on dir, then find a + t * dir
		myVectorf proj = new myVectorf(pts[0],p);
		return myVectorf._add(pts[0], proj._dot(dir), dir);
	}
	/**
	 * return a point on the line 
	 * @param t
	 * @return
	 */
	public final myPointf getPointOnLine(float t) {return myVectorf._add(pts[0], t, dir);}
	
	/**
	 * find t value for point on line - expects point to be on line!
	 * @param pt
	 * @return
	 */
	public final float getTForPointOnLine(myPointf pt) {
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
	protected final SOM_GeomExample buildSample(int i) {
		return new Geom_LineSOMExample(mapMgr, origin, dir, "Line_"+ID + "_Smp_" + i, new int[][] {locClrAra, rndClrAra,locClrAra, rndClrAra});
	}
	@Override
	protected final SOM_GeomExample buildObjExample() {
		return new Geom_LineSOMExample(mapMgr, origin, dir, "Line "+ID, new int[][] {locClrAra, rndClrAra,locClrAra, rndClrAra});
	}
	/**
	 * return a random point on this object
	 */
	@Override
	public final myPointf getRandPointOnObj() {
		float[][] wb = getWorldTBounds();
		float t = ((float) ThreadLocalRandom.current().nextFloat() *wb[1][0])+wb[0][0];
		return new myPointf(origin, t, dir);
	}
	
	
	@Override
	protected final void _drawMe_Geom(my_procApplet pa) {
		pa.strokeWeight(2.0f);
		pa.line(dispPts[0],dispPts[1]);
		pa.show(dispPts[0], 5.0f, "End 0", myVectorf.ZEROVEC, -1, true);
		pa.show(dispPts[1], 5.0f, "End 1", myVectorf.ZEROVEC, -1, true);
		pa.show(pts[0], 5.0f, "pt a", myVectorf.ZEROVEC, -1, true);
		pa.show(pts[1], 5.0f, "pt b", myVectorf.ZEROVEC, -1, true);
		pa.show(origin, 5.0f, "Origin "+ID, myVectorf.ZEROVEC, -1, true);
	}

	@Override
	protected final void _drawMe_Geom_Lbl(my_procApplet pa) {
		float[][] wb = getWorldTBounds();
		pa.strokeWeight(2.0f);
		pa.line(dispPts[0],dispPts[1]);
		//(myPointf P, float r, String s, myVectorf D, int clr, boolean flat)
		String id0 = ID+" End point 0 using min t : " + wb[0][0] + " | "+dispPts[0].toStrBrf(),
				id1 = ID+" End point 1 using max t : " + (wb[0][0]+wb[1][0])+ " | "+dispPts[1].toStrBrf();
		pa.show(dispPts[0], 5.0f, id0, myVectorf.ZEROVEC, -1, true);
		pa.show(dispPts[1], 5.0f, id1, myVectorf.ZEROVEC, -1, true);
		pa.show(pts[0], 5.0f, "pt a :"+pts[0].toStrBrf(), myVectorf.ZEROVEC, -1, true);
		pa.show(pts[1], 5.0f, "pt b :"+pts[1].toStrBrf(), myVectorf.ZEROVEC, -1, true);
		pa.show(origin, 5.0f, "Origin "+ID + "|"+id0+"|"+id1, myVectorf.ZEROVEC, -1, true);
	}

	
	

	@Override
	public void drawMeLabel(my_procApplet pa) {
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
