package SOM_GeometryProj_PKG.som_geom.geom_examples;

import base_SOM_Objects.som_examples.SOM_FtrDataBoundMonitor;

public class SOM_GeomFtrBndMon extends SOM_FtrDataBoundMonitor {
	//only 1 data type per experiment?
	
	public SOM_GeomFtrBndMon(int _numFtrVals) {
		super(1, _numFtrVals);
	}
	//check if value is in bnds array for particular jp, otherwise modify bnd - only 1 type of data
	public final void checkValInBnds(Integer destIDX, float val) {
		super.checkValInBnds(0,destIDX, val);
	}
	

}//class SOM_GeomFtrBndMon
