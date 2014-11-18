package org.lsw.soap.client.view;

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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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

import org.lsw.soap.client.controller.*;
import org.lsw.soap.client.task.*;

import net.webservicex.TemperatureUnit;

/**
 * Main Activity of Temperature Converter
 * 
 * @author Thunyathon Jaruchotrattanasakul 55105469782
 * 
 */
public class MainActivity extends JFrame implements Runnable {

	private JLabel urlLabel;
	private JTextField tempField;
	private JComboBox fUnitList;
	private JComboBox tUnitList;
	private List<String> list;
	private JLabel statusLabel;
	private JButton conB;

	public MainActivity() {
		super("Temperature Converter");
		this.initComponets();
	}

	private void initComponets() {
		list = new ArrayList<String>();
		list.add(TemperatureUnit.DEGREE_CELSIUS.toString());
		list.add(TemperatureUnit.DEGREE_FAHRENHEIT.toString());
		list.add(TemperatureUnit.DEGREE_RANKINE.toString());
		list.add(TemperatureUnit.DEGREE_REAUMUR.toString());
		list.add(TemperatureUnit.KELVIN.toString());
		FlowLayout layout = new FlowLayout();
		Container contain = new Container();
		contain.setLayout(layout);
		urlLabel = new JLabel("Temperature");
		contain.add(urlLabel);
		tempField = new JTextField(20);
		tempField.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 1)
					try {
						checkError();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ExecutionException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

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
		conB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				try {
					checkError();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

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

	public void setStatusBar(String text) {
		statusLabel.setText(text);
	}

	/**
	 * Checking the input is error or not
	 * 
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	private void checkError() throws InterruptedException, ExecutionException {
		double temp;
		this.setStatusBar("Checking Input!");
		try {
			temp = Double.parseDouble(tempField.getText());
		} catch (NumberFormatException e) {

			JOptionPane.showMessageDialog(this, "Bad Input", "Converted!",
					JOptionPane.ERROR_MESSAGE);
			tempField.setText("");
			return;
		}

		if (fUnitList.getSelectedIndex() == tUnitList.getSelectedIndex()) {
			JOptionPane.showMessageDialog(this, "BAD Unit", "Converted!",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		this.setStatusBar("Checking Connection!");
		this.setDisable();
		TestConnectionTask check = new TestConnectionTask(this);
		check.execute();
		try {
			check.get(10, TimeUnit.SECONDS);
		} catch (TimeoutException e) {
			timeOut(check);
		}

	}

	/**
	 * Calculate the converted value
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public void calculate() throws InterruptedException, ExecutionException {

		ConnectTask task = new ConnectTask(ControllerFactory.getController(),
				this);

		try {
			task.addParam(Double.parseDouble(tempField.getText()),
					fUnitList.getSelectedIndex(), tUnitList.getSelectedIndex());
			task.execute();
			task.get(10, TimeUnit.SECONDS);
		} catch (TimeoutException e) {
			timeOut(task);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Set Component Disable
	 */
	public void setDisable() {
		tempField.setEditable(false);
		tempField.setEnabled(false);
		fUnitList.setEnabled(false);
		tUnitList.setEnabled(false);
		conB.setEnabled(false);

	}

	/**
	 * Set Component Enable
	 */
	public void setEnable() {
		tempField.setEditable(true);
		tempField.setEnabled(true);
		fUnitList.setEnabled(true);
		tUnitList.setEnabled(true);
		conB.setEnabled(true);
	}
	
	/**
	 * Show Dialog when timeout
	 * @param w
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public void timeOut(SwingWorker w) throws InterruptedException, ExecutionException{
		Object[] options = { "Cancel", "Retry" };
		int n = JOptionPane.showOptionDialog(this,
				"Connection TimeOut.",
				"TimeOut!", JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.ERROR_MESSAGE, null, options, options[1]);
		if (n == 0) {
			this.setStatusBar("Connection TimeOut!");
			this.setEnable();
		}
		if (n == 1) {
			this.checkError();
		}
		try {
			w.cancel(true);
		} catch (CancellationException e1) {
			return;
		}
		return;
	}

	@Override
	public void run() {
		pack();
		this.setVisible(true);
	}

}
