package org.lsw.soap.client.controller;

/**
 * Factory for TempController
 * @author Thunyathon Jaruchotrattanasakul 55105469782
 *
 */
public class ControllerFactory {

	private static TempController tempCon=null;
	private ControllerFactory(){
		
	}
	
	/**
	 * Get temperatrue controller
	 * @return
	 */
	public static TempController getController(){
		if(tempCon==null){
			tempCon = new TempController();
		}
		return tempCon;
	}
	

}
