package SOM_GeometryProj_PKG.geom_Objects.builders.callables;

import SOM_GeometryProj_PKG.som_geom.geom_utils.geom_threading.SOM_GeomObjBuilder;
import base_Utils_Objects.io.MessageObject;

public class Geom_LineObjBuilder extends SOM_GeomObjBuilder {

	public Geom_LineObjBuilder(MessageObject _msgObj, int _stExIDX, int _endExIDX, int _thdIDX, int _taskToDo, String _datatype) {
		super(_msgObj, _stExIDX, _endExIDX, _thdIDX, _taskToDo, _datatype);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Boolean call() throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

}
