
package view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.BevelBorder;

import controller.TempController;
/**
 * Main Activity of Temperature Converter
 * @author Thunyathon Jaruchotrattanasakul 55105469782
 *
 */
public class MainActivity extends JFrame implements Runnable {

	private TempController controller;
	private JLabel urlLabel;
	private JTextField tempField;
	private JComboBox fUnitList;
	private JComboBox tUnitList;
	private List<String> list;
	private JLabel statusLabel;
	private JButton conB;
	public MainActivity(){
		super("Temperature Converter");
		controller = new TempController();
		
		this.initComponets();
	}
	private void initComponets() {
		list = controller.getTempUnit();
		FlowLayout layout = new FlowLayout();
		Container contain = new Container();
		contain.setLayout(layout);
		urlLabel = new JLabel("Temperature");
		contain.add(urlLabel);
		tempField = new JTextField(20);
		tempField.addKeyListener(new KeyListener(){

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==1)checkError();
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		contain.add(tempField);
		

		fUnitList = new JComboBox(list.toArray());		
		contain.add(fUnitList);
		
		tUnitList = new JComboBox(list.toArray());
		tUnitList.setSelectedIndex(1);
		contain.add(tUnitList);
		
		conB = new JButton("CONVERT!");
		contain.add(conB);
		conB.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				checkError();
				
			}
			
			
		});
		
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		super.add(statusPanel, BorderLayout.SOUTH);
		statusPanel.setPreferredSize(new Dimension(this.getWidth(), 16));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		statusLabel = new JLabel("");
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(statusLabel);

		
		super.add(contain, BorderLayout.NORTH);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}
	public void setStatusBar(String text){
		statusLabel.setText(text);
	}
	
	/**
	 * Checking the input is error or not
	 */
	private void checkError(){
		double temp;
		
		try {
		   temp = Double.parseDouble(tempField.getText());
		}
		catch (NumberFormatException e) {
			
			JOptionPane.showMessageDialog(this,
				    "Bad Input",
				    "Converted!",
				    JOptionPane.ERROR_MESSAGE);
			tempField.setText("");
			return;
		}
		
		if(fUnitList.getSelectedIndex() == tUnitList.getSelectedIndex()){
			JOptionPane.showMessageDialog(this,
				    "BAD Unit",
				    "Converted!",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}
		this.setDisable();
		TestConnectionTask check = new TestConnectionTask(this);
		check.execute();
		
	}
	
	/**
	 * Calculate the converted value
	 */
	public void calculate(){
		ConnectTask task = new ConnectTask(controller, this);
		task.addParam(Double.parseDouble(tempField.getText()),fUnitList.getSelectedIndex(), tUnitList.getSelectedIndex());
		task.execute();
	}
	
	/**
	 * Set Component Disable
	 */
	public void setDisable(){
		tempField.setEditable(false);
		tempField.setEnabled(false);
		fUnitList.setEnabled(false);
		tUnitList.setEnabled(false);
		conB.setEnabled(false);
		
	}
	
	/**
	 * Set Component Enable
	 */
	public void setEnable(){
		tempField.setEditable(true);
		tempField.setEnabled(true);
		fUnitList.setEnabled(true);
		tUnitList.setEnabled(true);
		conB.setEnabled(true);
	}
	
	@Override
	public void run() {
		pack();
		this.setVisible(true);	
	}
	
	

}
class ConnectTask extends SwingWorker<Double , Object>{

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
		try {
			main.setEnable();
			main.setStatusBar("Successful!");
			JOptionPane.showMessageDialog(main,
				    this.get(),
				    "Converted!",
				    JOptionPane.PLAIN_MESSAGE);
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class TestConnectionTask extends SwingWorker<Boolean , Object>{

	private MainActivity main;
	public TestConnectionTask(MainActivity main){
		this.main = main;
	}
	@Override
	protected Boolean doInBackground() throws Exception {
		main.setStatusBar("Checking Connection!");
		return this.testInet("www.webservicex.net");
	}
	
	
	@Override
	protected void done() {
		try {
			if(!this.get()){
				main.setEnable();
				JOptionPane.showMessageDialog(main,
					    "No Internet Connection\n Please Check Your Connection.",
					    "NO Connection",
					    JOptionPane.ERROR_MESSAGE);
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

