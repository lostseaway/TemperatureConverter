package controller;

import java.util.ArrayList;
import java.util.List;

import net.webservicex.ConvertTemperature;
import net.webservicex.ConvertTemperatureSoap;
import net.webservicex.TemperatureUnit;

/**
 * Controller for Temperature Converter
 * @author Thunyathon Jaruchotrattanasakul 55105469782
 *
 */
public class TempController {
	private ConvertTemperatureSoap proxy;
	private List<TemperatureUnit> unitList;
	public TempController(){
		ConvertTemperature factory = new ConvertTemperature();
		proxy = factory.getConvertTemperatureSoap();
		
		unitList = new ArrayList<TemperatureUnit>();
		unitList.add(TemperatureUnit.DEGREE_CELSIUS);
		unitList.add(TemperatureUnit.DEGREE_FAHRENHEIT);
		unitList.add(TemperatureUnit.DEGREE_RANKINE);
		unitList.add(TemperatureUnit.DEGREE_REAUMUR);
		unitList.add(TemperatureUnit.KELVIN);
	}
	
	/**
	 * get list of unit that server provide
	 * @return
	 */
	public List<String> getTempUnit(){
		List<String> list = new ArrayList<String>();
		for(int i = 0;i<unitList.size();i++) list.add(unitList.get(i).name());
		return list;
	}
	
	/**
	 * Convert temp funit to tunit
	 * @param temp
	 * @param funit
	 * @param tunit
	 * @return
	 */
	public double convert(double temp,int funit,int tunit){
		return proxy.convertTemp(temp, unitList.get(funit), unitList.get(tunit));
	}
}
