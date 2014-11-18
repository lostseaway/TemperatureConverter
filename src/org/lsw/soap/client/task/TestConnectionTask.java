package org.lsw.soap.client.task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.lsw.soap.client.controller.ControllerFactory;
import org.lsw.soap.client.view.MainActivity;

/**
 * Swing Worker for Testing Conection!
 * @author Thunyathon Jaruchotrattanasakul 55105469782
 *
 */
public class TestConnectionTask extends SwingWorker<Boolean , Object>{

	private MainActivity main;
	public TestConnectionTask(MainActivity main){
		this.main = main;
	}
	@Override
	protected Boolean doInBackground() throws Exception {
		ControllerFactory.getController();
		return this.testInet("www.webservicex.net");
	}
	
	
	@Override
	protected void done() {
		try {
			if(!this.get()){
				main.setEnable();
				Object[] options = {"Cancel",
	                    "Retry"};
					int n = JOptionPane.showOptionDialog(main,
						"No Internet Connection\n Please Check Your Connection.",
						"NO Connection",
					    JOptionPane.YES_NO_CANCEL_OPTION,
					    JOptionPane.ERROR_MESSAGE,
					    null,
					    options,
					    options[1]);
					if(n==0){
						main.setStatusBar("No Connection!");
					}
					if(n==1){
						TestConnectionTask check = new TestConnectionTask(main);
						check.execute();
					}
			}
			else{
				main.setStatusBar("Connected!!");
				main.calculate();
			}
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 * Checking internet Connection
	 * @param site
	 * @return
	 */
	public boolean testInet(String site) {
	    Socket sock = new Socket();
	    InetSocketAddress addr = new InetSocketAddress(site,80);
	    try {
	        sock.connect(addr,3000);
	        return true;
	    } catch (IOException e) {
	        return false;
	    } finally {
	        try {sock.close();}
	        catch (IOException e) {}
	    }
	}
}
	
