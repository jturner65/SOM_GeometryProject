package SOM_GeometryProj_PKG.geom_SOM_Examples;

import java.util.TreeMap;

import SOM_GeometryProj_PKG.geom_Objects.SOM_GeomSmplForSOMExample;
import SOM_GeometryProj_PKG.som_geom.geom_examples.SOM_GeomExample;
import base_SOM_Objects.SOM_MapManager;
import base_Utils_Objects.vectorObjs.myPointf;
import base_Utils_Objects.vectorObjs.myVectorf;

/**
 * a traning example describing a line
 * @author john
 *
 */
public class Geom_LineSOMExample extends SOM_GeomExample {
	/**
	 * given points that make up this line
	 */
	public myPointf[] pts;
	/**
	 * direction vector for this line
	 */
	public myVectorf dir;
	/**
	 * point closest to 0,0 for this line
	 */
	public myPointf origin;
	/**
	 * display points for this line to draw maximally based on world bounds
	 */
	public final myPointf[] dispEndPts;	
	
	/**
	 * coordinate bounds for lines in sim/geom world - each object/example type has its own world bounds
	 * 		first idx : 0 is min; 1 is diff
	 * 		2nd idx : 0 is x, 1 is y, 2 is z (if z is present)
	 */
	protected static float[][] worldBounds = null;

	
	/**
	 * build a geometry-based training/validation example for the SOM
	 * @param _map owning map manager
	 * @param _a,_b : 2 points on the line descr
	 * @param _id the unique ID info for this training example
	 * @param _clrs 4 element array of colors for this object
	 * 		idx 0 : color based on location of this element in space
	 * 		idx 1 : random color
	 * 		idx 2 : color based on owning object's location in space
	 * 		idx 3 : owning object's random color
	 */

	public Geom_LineSOMExample(SOM_MapManager _map, String _id, SOM_GeomSmplForSOMExample[] _srcSmpls, float[][] _worldBounds) {
		super(_map, _id, _srcSmpls, _worldBounds);

		float[][] wb = getWorldTBounds();
		dispEndPts = new myPointf[2];
		dispEndPts[0] = getPointOnLine(wb[0][0]);
		dispEndPts[1] = getPointOnLine(wb[0][0] + wb[1][0]);	
	}
	
	public Geom_LineSOMExample(Geom_LineSOMExample _otr) {
		super(_otr);
		pts = _otr.pts;
		origin = _otr.origin;
		dir = _otr.dir;
		dispEndPts =_otr.dispEndPts;
		worldBounds = _otr.worldBounds;
	}
	
	/**
	 * build this example from passed source samples
	 * @param _srcSmpls
	 */
	protected final void buildExampleFromSrcObjs(SOM_GeomSmplForSOMExample[] _srcSmpls) {
		pts = new myPointf[] {new myPointf(_srcSmpls[0].getPoint()),new myPointf(_srcSmpls[1].getPoint())};
		pts[0].z = 0.0f;
		pts[1].z = 0.0f;
		//z is always 0 - making this in 2 d
		dir=new myVectorf(pts[0],pts[1]);
		dir.z = 0;
		dir._normalize();	
		//origin is closest point to 0,0 on line
		origin = findClosestPointOnLine(myPointf.ZEROPT);
		origin.z = 0;
	
	}//buildExampleFromSrcObjs
	
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
	@Override
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
	

	
	/**
	 * call from ctor of base class, but set statically for each instancing class type
	 * @param _worldBounds
	 */
	protected final void setWorldBounds(float[][]_worldBounds) {
		if(null!=worldBounds) {return;}
		worldBounds = new float[_worldBounds.length][];
		for(int i=0;i<worldBounds.length;++i) {
			float[] tmp = new float[_worldBounds[i].length];
			for(int j=0;j<tmp.length;++j) {	tmp[j]=_worldBounds[i][j];}
			worldBounds[i]=tmp;
		}
	}//setWorldBounds
	
	
	
	/**
	 * find projection t of vector ap (from a to p) on dir, then find a + t * dir
	 * @param p point to project
	 * @return
	 */
	public final myPointf findClosestPointOnLine(myPointf p) {
		myVectorf proj = new myVectorf(pts[0],p);
		return myVectorf._add(pts[0], proj._dot(dir), dir);
	}
	
	/**
	 * return a point on the line 
	 * @param t
	 * @return
	 */
	public final myPointf getPointOnLine(float t) {return myVectorf._add(pts[0], t, dir);}


//	/**
//	 * draw this example in the world - instance-class specific
//	 */
//	@Override
//	protected void _drawMe_Geom(my_procApplet pa) {
//		pa.strokeWeight(2.0f);
//		pa.line(dispEndPts[0],dispEndPts[1]);
//		pa.show(dispEndPts[0], 5.0f, "End 0", myVectorf.ZEROVEC, -1, true);
//		pa.show(dispEndPts[1], 5.0f, "End 1", myVectorf.ZEROVEC, -1, true);
//		pa.show(pts[0], 5.0f, "pt a", myVectorf.ZEROVEC, -1, true);
//		pa.show(pts[1], 5.0f, "pt b", myVectorf.ZEROVEC, -1, true);
//		pa.show(origin, 5.0f, "Origin "+OID, myVectorf.ZEROVEC, -1, true);
//	}
//
//	/**
//	 * TODO draw this example in the world based on BMU - instance-class specific
//	 */
//	@Override
//	protected void _drawMe_Geom_BMU(my_procApplet pa) {
//		pa.strokeWeight(2.0f);
//		pa.line(dispEndPts[0],dispEndPts[1]);
//		pa.show(dispEndPts[0], 5.0f, "End 0", myVectorf.ZEROVEC, -1, true);
//		pa.show(dispEndPts[1], 5.0f, "End 1", myVectorf.ZEROVEC, -1, true);
//		pa.show(pts[0], 5.0f, "pt a", myVectorf.ZEROVEC, -1, true);
//		pa.show(pts[1], 5.0f, "pt b", myVectorf.ZEROVEC, -1, true);
//		pa.show(origin, 5.0f, "Origin "+OID, myVectorf.ZEROVEC, -1, true);
//	}

	@Override
	protected void buildFeaturesMap() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getPreProcDescrForCSV() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRawDescColNamesForCSV() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void finalizeBuildBeforeFtrCalc() {
		// TODO Auto-generated method stub

	}

	@Override
	public void postFtrVecBuild() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void _buildFeatureVectorEnd_Priv() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setIsTrainingDataIDX_Priv() {
		// TODO Auto-generated method stub

	}

	@Override
	public TreeMap<Integer, Integer> getTrainingLabels() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void buildCompFtrVector(float _ratio) {
		// TODO Auto-generated method stub

	}

}
