package org.lsw.soap.client.task;

import java.awt.HeadlessException;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.lsw.soap.client.controller.TempController;
import org.lsw.soap.client.view.MainActivity;

/**
 * SwingWorker for Create Conection !
 * @author Thunyathon Jaruchotrattanasakul 55105469782
 *
 */
public class ConnectTask extends SwingWorker<Double , Object>{

	private TempController controller;
	private MainActivity main;
	private double temp;
	private int funit;
	private int tunit;
	public ConnectTask(TempController con,MainActivity main){
		this.controller = con;
		this.main = main;
	}
	
	/**
	 * Add Parameter for preparing calculation
	 * @param temp
	 * @param funit
	 * @param tunit
	 */
	public void addParam(double temp,int funit,int tunit){
		this.temp = temp;
		this.funit = funit;
		this.tunit = tunit;
	}
	@Override
	protected Double doInBackground() throws Exception {
		main.setStatusBar("Loading Data");
		
		return controller.convert(temp, funit, tunit);
	}
	@Override
	protected void done() {
			main.setEnable();
			main.setStatusBar("Successful!");
			try {
				JOptionPane.showMessageDialog(main,
					    this.get(),
					    "Converted!",
					    JOptionPane.PLAIN_MESSAGE);
			} catch (HeadlessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
