package org.lsw.soap.client.view;

import net.webservicex.ConvertTemperature;
import net.webservicex.ConvertTemperatureSoap;
import net.webservicex.TemperatureUnit;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		ConvertTemperature factory = new ConvertTemperature();
//		ConvertTemperatureSoap proxy = factory.getConvertTemperatureSoap();
//		double temp = proxy.convertTemp(100, TemperatureUnit.DEGREE_FAHRENHEIT, TemperatureUnit.DEGREE_CELSIUS);
//		System.out.println(proxy.convertTemp(100, TemperatureUnit.DEGREE_FAHRENHEIT, TemperatureUnit.DEGREE_CELSIUS));
		MainActivity main = new MainActivity();
		main.run();
	}

}
