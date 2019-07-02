package SOM_GeometryProj_PKG.sphere_Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;

import base_SOM_Objects.SOM_MapManager;
import base_SOM_Objects.som_utils.SOM_ProjConfigData;
import base_Utils_Objects.io.MsgCodes;

public class Sphere_SOMProjConfig extends SOM_ProjConfigData {
	//file name to use to save sphere-project-specific SOM config data
	private final String custSphereSOMConfigDataFileName= "SphereProj_SOM_CustomMapTrainData.txt";

	public Sphere_SOMProjConfig(SOM_MapManager _mapMgr, TreeMap<String, Object> _argsMap) {	super(_mapMgr, _argsMap);}

	@Override
	protected void _loadIndivConfigVarsPriv(String varName, String val) {
		switch (varName) {
		default : {
			msgObj.dispMessage("Sphere_SOMProjConfig","_loadIndivConfigVarsPriv","Unknown/unhandled config var name :  " +varName +" with value " + val +" .", MsgCodes.warning2);
			}
		}
	}//_loadIndivConfigVarsPriv
	
	
	/**
	 * get the project-specific per-map detail data used to display information regarding prebuilt maps
	 */
	@Override
	protected String[] getPreBuiltMapInfoStr_Indiv(ArrayList<String> res, String _mapDir) {
		// TODO Auto-generated method stub
		return res.toArray(new String[0]);
	}//getPreBuiltMapInfoStr_Indiv

	@Override
	protected ArrayList<String> buildCustSOMConfigData() {
		ArrayList<String> customData = new ArrayList<String>();
		customData.add("# This file holds Sphere project-specific configuration data pertaining to the training of the SOM in this directory");
		customData.add("# This data is here to override any default configuration data that may be loaded in from the project config file.");
		customData.add("#");
		customData.add("#");
		return customData;
	}//buildCustSOMConfigData
	
	/**
	 * This will save any application-specific reporting data by adding to arraylist
	 * @param reportData
	 */
	@Override
	protected void saveSOM_ExecReport_Indiv(ArrayList<String> reportData) {
	}
	
	/**
	 * Instance-specific loading necessary for proper consumption of pre-built SOM
	 */
	@Override
	protected void setSOM_UsePreBuilt_Indiv() {
		String custConfigSOMFileName = getSOMExpCustomConfigFileName();		
		String[] fileStrings = fileIO.loadFileIntoStringAra(custConfigSOMFileName, "Custom application-specific SOM Exp Config File loaded", "Custom application-specific SOM Exp Config File Failed to load");
		int idx = 0; boolean found = false;
		//find start of first block of data - 
		while (!found && (idx < fileStrings.length)){if(fileStrings[idx].contains(fileComment)){++idx; }  else {found=true;}}
		//idx here is for first non-comment field
		while (idx < fileStrings.length) {
			if((fileStrings[idx].contains(fileComment)) || (fileStrings[idx].trim().length()==0)){++idx; continue;}
			String[] tkns = fileStrings[idx].trim().split(SOM_MapManager.csvFileToken);
			String val = tkns[1].trim().replace("\"", "");
			String varName = tkns[0].trim();
			switch (varName) {		
				default : {
					msgObj.dispMessage("Sphere_SOMProjConfig","setSOM_UsePreBuilt_Indiv","Unknown/unhandled custom config var name :  " +varName +" with value " + val +" .", MsgCodes.warning2);
				}
			}//switch
			++idx;
		}//while
	}
	
	/**
	 * file name of custom config used to save instance-specific implementation details/files 
	 * @return file name of config file for custom config variables, under SOM Exec dir
	 */
	@Override
	protected final String getSOMExpCustomConfigFileName_Indiv() {	return custSphereSOMConfigDataFileName;}


	@Override
	protected String getSegmentFileNamePrefix_Indiv(String segType, String fNamePrefix) {
		switch(segType.toLowerCase()) {
		default : {
			msgObj.dispMessage("Sphere_SOMProjConfig","getSegmentFileNamePrefix_Indiv","Unknown Segment Type " +segType.toLowerCase() +" so unable to build appropriate file name prefix.  Aborting.", MsgCodes.warning2);
			return "";	}
		}
	}//getSegmentFileNamePrefix_Indiv
	
	/**
	 * use this to provide raw data location
	 */
	@Override
	public String getRawDataLoadInfo(boolean fromFiles, String baseDirName, String baseFName) {
		String dataLocStrData = "";
		dataLocStrData = SOM_QualifiedDataDir + subDirLocs.get("SOM_SourceCSV") + baseDirName + File.separator + baseFName+".csv";
		return dataLocStrData;
	}

}//Sphere_SOMProjConfigData
