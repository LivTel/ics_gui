/*   
    Copyright 2006, Astrophysics Research Institute, Liverpool John Moores University.

    This file is part of IcsGUI.

    IcsGUI is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    IcsGUI is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with IcsGUI; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/
// IcsGUILowResSpecConfigAADialog.java -*- mode: Fundamental;-*-
// $Header: /home/cjm/cvs/ics_gui/java/IcsGUILowResSpecConfigAADialog.java,v 0.4 2020-05-05 10:20:39 cjm Exp $
import java.lang.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import ngat.swing.*;

/**
 * This class provides an Add and Amend facility for Low Resolution Spectrograph Configurations.
 * @author Chris Mottram
 * @version $Revision: 0.4 $
 */
public class IcsGUILowResSpecConfigAADialog extends JDialog implements ActionListener
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: IcsGUILowResSpecConfigAADialog.java,v 0.4 2020-05-05 10:20:39 cjm Exp $");
	/**
	 * Button height.
	 */
	public final static int BUTTON_HEIGHT = 25;
	/**
	 * Multiplier for getting dialog height from number of fields.
	 */
	public final static int FIELD_HEIGHT = 20;
	/**
	 * Dialog width.
	 */
	public final static int DIALOG_WIDTH = 250;
	/**
	 * Number of windows in a configuration.
	 */
	public final static int WINDOW_COUNT = 4;
	/**
	 * Titled border height.
	 */
	public final static int TITLED_BORDER_HEIGHT = 25;
	/**
	 * The data to be displayed in the list.
	 */
	IcsGUIConfigProperties configProperties = null;
	/**
	 * The id of the configuration we are modifying.
	 */
	int configId = 0;
	/**
	 * The listener for this dialog.
	 */
	IcsConfigAADialogListener listener = null;

	JTextField nameTextField = null;
	JCheckBox calibrateBeforeCheckBox = null;
	JCheckBox calibrateAfterCheckBox = null;
	JTextField xBinTextField = null;
	JTextField yBinTextField = null;
	JTextField wavelengthTextField = null;

	/**
	 * Constructor.
	 * @param owner The parent frame of this dialog. Used in calling Dialog's constructor.
	 * @param c The configuration properties.
	 */
	public IcsGUILowResSpecConfigAADialog(Frame owner,IcsGUIConfigProperties c)
	{
		super(owner,"Add/Amend Low Resolution Spectrograph Configuration");
//		setResizable(false);
		configProperties = c;
	// there are 5 fields arranged vertically.
	// there are 2 titled border height from 2 sets of titled border
	// there is one set of buttons vertically 
		int height = (5*FIELD_HEIGHT)+(2*TITLED_BORDER_HEIGHT)+BUTTON_HEIGHT;

		getContentPane().setLayout(new SizedBoxLayout(getContentPane(),BoxLayout.Y_AXIS,
			new Dimension(DIALOG_WIDTH,height)));

		JPanel subPanel = null;
		JLabel label = null;
	// sub panel
		subPanel = new JPanel();
		getContentPane().add(subPanel);
		subPanel.setLayout(new SizedGridLayout(0,2,new Dimension(DIALOG_WIDTH,FIELD_HEIGHT)));
		subPanel.setPreferredSize(new Dimension(DIALOG_WIDTH,FIELD_HEIGHT));
	// name
		label = new JLabel("Name");
		subPanel.add(label);
		nameTextField = new JTextField();
		subPanel.add(nameTextField);
	// sub panel
		subPanel = new JPanel();
		getContentPane().add(subPanel);
		subPanel.setLayout(new SizedGridLayout(0,2,new Dimension(DIALOG_WIDTH,FIELD_HEIGHT)));
	// calibrateBefore flag
		calibrateBeforeCheckBox = new JCheckBox("Calibrate Before");
		subPanel.add(calibrateBeforeCheckBox);
	// calibrateAfter flag
		calibrateAfterCheckBox = new JCheckBox("Calibrate After");
		subPanel.add(calibrateAfterCheckBox);
	// sub panel
		subPanel = new JPanel();
		getContentPane().add(subPanel);
		subPanel.setLayout(new SizedGridLayout(0,2,new Dimension(DIALOG_WIDTH,FIELD_HEIGHT)));
	// grating position
		label = new JLabel("Wavelength");
		subPanel.add(label);
		wavelengthTextField = new JTextField();
		subPanel.add(wavelengthTextField);
	// sub panel
		subPanel = new JPanel();
		getContentPane().add(subPanel);
		subPanel.setLayout(new SizedGridLayout(0,2,new Dimension(DIALOG_WIDTH,FIELD_HEIGHT)));
	// X Binning
		label = new JLabel("X Binning");
		subPanel.add(label);
		xBinTextField = new JTextField();
		subPanel.add(xBinTextField);
	// sub panel
		subPanel = new JPanel();
		getContentPane().add(subPanel);
		subPanel.setLayout(new SizedGridLayout(0,2,new Dimension(DIALOG_WIDTH,FIELD_HEIGHT)));
	// Y Binning
		label = new JLabel("Y Binning");
		subPanel.add(label);
		yBinTextField = new JTextField();
		subPanel.add(yBinTextField);
	// sub panel
		subPanel = new JPanel();
		getContentPane().add(subPanel);
		subPanel.setLayout(new SizedGridLayout(0,2,new Dimension(DIALOG_WIDTH,BUTTON_HEIGHT)));
	// Ok button
		JButton okButton = new JButton("Ok");
		okButton.addActionListener(this);
		subPanel.add(okButton);
	// Cancel button
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		subPanel.add(cancelButton);
	}

	/**
	 * Method to manage the config Add/Amend dialog in add mode.
	 */
	public void add()
	{
		int i;

		configId = configProperties.getNewConfigId();

		nameTextField.setText("Configuration "+configId);
		calibrateBeforeCheckBox.setSelected(false);
		calibrateAfterCheckBox.setSelected(false);
		wavelengthTextField.setText("8000.0");
		xBinTextField.setText("1");
		yBinTextField.setText("1");

		this.setVisible(true);
	}

	/**
	 * Method to manage the config Add/Amend dialog in amend mode.
	 * @param id The id to amend
	 */
	public void amend(int id)
	{
		configId = id;

		nameTextField.setText(configProperties.getConfigName(id));
		calibrateBeforeCheckBox.setSelected(configProperties.getConfigCalibrateBefore(id));
		calibrateAfterCheckBox.setSelected(configProperties.getConfigCalibrateAfter(id));
		wavelengthTextField.setText(configProperties.getConfigWavelengthString(id));
		xBinTextField.setText(configProperties.getConfigXBinString(id));
		yBinTextField.setText(configProperties.getConfigYBinString(id));

		this.setVisible(true);
	}

	/**
	 * Method to add a listener to this dialog, so that when O.K. or Cancel is pressed
	 * the listener is informed. Only one listener can be added to this dialog.
	 * @param l The listener to set for this dialog.
	 */
	public void addIcsConfigAADialogListener(IcsConfigAADialogListener l)
	{
		listener = l;
	}

	/**
	 * ActionListener method. Called when a Button is pressed.
	 * If a listener is registered it is called.
	 * @param event The event that caused this method to be called.
	 * @see #listener
	 */
	public void actionPerformed(ActionEvent event)
	{
		String commandString = event.getActionCommand();
		String s = null;
		int testId = 0;
		int i;
		int xBin,yBin;
		double wavelength;
		boolean uniqueId = false;

		if(commandString.equals("Ok"))
		{
		// is the name unique only to the id we are amending (or none if we are adding)
			uniqueId = false;
			s = nameTextField.getText();
			try
			{
				testId = configProperties.getIdFromName(s);
				uniqueId = (testId == configId);
			}
			catch(IllegalArgumentException e)
			{
			// we must be adding a config with a name that doesn't exist
				uniqueId = true;
			}
			if(uniqueId == false)
			{
				JOptionPane.showMessageDialog((Component)null,
					(Object)("The configuration name `"+s+"'is not unique."),
					" Illegal name ",JOptionPane.ERROR_MESSAGE);
				return;
			}
		// get and test numeric data from text fields
			try
			{
				wavelength = Double.parseDouble(wavelengthTextField.getText());
// diddly test
				xBin = Integer.parseInt(xBinTextField.getText());
				if(xBin < 1)
					throw new NumberFormatException("X Binning is less than one");
				yBin = Integer.parseInt(yBinTextField.getText());
				if(yBin < 1)
					throw new NumberFormatException("Y Binning is less than one");
			}
			catch(NumberFormatException e)
			{
				JOptionPane.showMessageDialog((Component)null,
					(Object)("A field is not a number."+e),
					" Illegal Number ",JOptionPane.ERROR_MESSAGE);
				return;
			}
		// set data
			configProperties.setConfigName(configId,s);
			configProperties.setConfigType(configId,IcsGUIConfigProperties.CONFIG_TYPE_SPECTROGRAPH_NUVIEW);
			configProperties.setConfigCalibrateBefore(configId,calibrateBeforeCheckBox.isSelected());
			configProperties.setConfigCalibrateAfter(configId,calibrateAfterCheckBox.isSelected());
			configProperties.setConfigWavelength(configId,wavelength);
			configProperties.setConfigXBin(configId,xBin);
			configProperties.setConfigYBin(configId,yBin);
			if(listener != null)
				listener.actionPerformed(true,configId);
		}
		else
		{
			if(listener != null)
				listener.actionPerformed(false,0);
		}
	//unmanage dialog
		this.setVisible(false);
	}
}
//
// $Log: not supported by cvs2svn $
// Revision 0.3  2006/05/16 17:12:29  cjm
// gnuify: Added GNU General Public License.
//
// Revision 0.2  2001/07/10 18:21:28  cjm
// Rewritten.
//
// Revision 0.1  2000/11/30 18:48:36  cjm
// initial revision.
//
//
