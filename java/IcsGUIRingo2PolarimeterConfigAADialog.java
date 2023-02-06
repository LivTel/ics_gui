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
// IcsGUIRingo2PolarimeterConfigAADialog.java
// $Header: /home/cjm/cvs/ics_gui/java/IcsGUIRingo2PolarimeterConfigAADialog.java,v 1.2 2020-05-05 10:20:39 cjm Exp $
import java.lang.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import ngat.swing.*;
import ngat.phase2.Ringo2PolarimeterConfig;

/**
 * This class provides an Add and Amend facility for Ringo2 Polarimeter Configurations.
 * @author Chris Mottram
 * @version $Revision: 98404f49e4c35a5c59ab3678f31a3b1d35ba5297 $
 */
public class IcsGUIRingo2PolarimeterConfigAADialog extends JDialog implements ActionListener
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: IcsGUIRingo2PolarimeterConfigAADialog.java | Tue May 5 10:20:41 2020 +0000 | Chris Mottram  $");
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
	public final static int WINDOW_COUNT = 1;
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
	JTextField emGainTextField = null;
	JComboBox triggerTypeComboBox = null;
	JCheckBox windowFlagCheckBox[] = null;
	JTextField windowXStartTextField[] = null;
	JTextField windowYStartTextField[] = null;
	JTextField windowXEndTextField[] = null;
	JTextField windowYEndTextField[] = null;

	/**
	 * Constructor.
	 * @param owner The parent frame of this dialog. Used in calling Dialog's constructor.
	 * @param c The configuration properties.
	 */
	public IcsGUIRingo2PolarimeterConfigAADialog(Frame owner,IcsGUIConfigProperties c)
	{
		super(owner,"Add/Amend Polarimeter Configuration");
//		setResizable(false);
		configProperties = c;
	// there are 11 fields arranged vertically.
	// there are 2 titled border height from 2 sets of titled border
	// there is one set of buttons vertically 
		int height = (11*FIELD_HEIGHT)+(2*TITLED_BORDER_HEIGHT)+BUTTON_HEIGHT;

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
		subPanel.setLayout(new SizedGridLayout(0,2,new Dimension(DIALOG_WIDTH,FIELD_HEIGHT)));
		subPanel.setPreferredSize(new Dimension(DIALOG_WIDTH,FIELD_HEIGHT));
	// emGain
		label = new JLabel("EM Gain");
		subPanel.add(label);
		emGainTextField = new JTextField();
		subPanel.add(emGainTextField);
	// sub panel
		subPanel = new JPanel();
		getContentPane().add(subPanel);
		subPanel.setLayout(new SizedGridLayout(0,2,new Dimension(DIALOG_WIDTH,FIELD_HEIGHT)));
	// trigger type
		label = new JLabel("Trigger Type");
		subPanel.add(label);
		triggerTypeComboBox = new JComboBox();
		triggerTypeComboBox.setEditable(false);
		triggerTypeComboBox.addItem("internal");
		triggerTypeComboBox.addItem("external");
		subPanel.add(triggerTypeComboBox);
	// Windows
		windowFlagCheckBox = new JCheckBox[WINDOW_COUNT];
		windowXStartTextField = new JTextField[WINDOW_COUNT];
		windowYStartTextField = new JTextField[WINDOW_COUNT];
		windowXEndTextField = new JTextField[WINDOW_COUNT];
		windowYEndTextField = new JTextField[WINDOW_COUNT];
		for(int i = 0;i < WINDOW_COUNT; i++)
		{
			// window panel
			subPanel = new JPanel();
			getContentPane().add(subPanel);
			subPanel.setLayout(new GridLayout(0,2));
		// window panel
			JPanel windowPanel = new JPanel();
			subPanel.add(windowPanel);
			windowPanel.setBorder(new TitledSmallerBorder("Window "+(i+1)));
			windowPanel.setLayout(new SizedGridLayout(0,2,
				new Dimension(DIALOG_WIDTH,TITLED_BORDER_HEIGHT+(5*FIELD_HEIGHT))));
		// window flag
			windowFlagCheckBox[i] = new JCheckBox("Use");
			windowPanel.add(windowFlagCheckBox[i]);
			label = new JLabel("");// for grid layout only
			windowPanel.add(label);
		// x start
			label = new JLabel("X Start");
			windowPanel.add(label);
			windowXStartTextField[i] = new JTextField();
			windowPanel.add(windowXStartTextField[i]);
		// y start
			label = new JLabel("Y Start");
			windowPanel.add(label);
			windowYStartTextField[i] = new JTextField();
			windowPanel.add(windowYStartTextField[i]);
		// x end
			label = new JLabel("X End");
			windowPanel.add(label);
			windowXEndTextField[i] = new JTextField();
			windowPanel.add(windowXEndTextField[i]);
		// y end
			label = new JLabel("Y End");
			windowPanel.add(label);
			windowYEndTextField[i] = new JTextField();
			windowPanel.add(windowYEndTextField[i]);
		}// end for on windows
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
		xBinTextField.setText("1");
		yBinTextField.setText("1");
		emGainTextField.setText("1");
		triggerTypeComboBox.setSelectedItem("external");
		for(i=0;i<WINDOW_COUNT;i++)
		{
			windowFlagCheckBox[i].setSelected(false);
			windowXStartTextField[i].setText("-1");
			windowYStartTextField[i].setText("-1");
			windowXEndTextField[i].setText("-1");
			windowYEndTextField[i].setText("-1");
		}
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
		xBinTextField.setText(configProperties.getConfigXBinString(id));
		yBinTextField.setText(configProperties.getConfigYBinString(id));
		emGainTextField.setText(configProperties.getConfigEMGainString(id));
		triggerTypeComboBox.setSelectedItem(configProperties.getConfigTriggerTypeString(id));
		for(int i=0;i<WINDOW_COUNT;i++)
		{
			windowFlagCheckBox[i].setSelected((configProperties.getConfigWindowFlags(id)&(1<<i))>0);
			windowXStartTextField[i].setText(configProperties.getConfigXStartString(id,i+1));
			windowYStartTextField[i].setText(configProperties.getConfigYStartString(id,i+1));
			windowXEndTextField[i].setText(configProperties.getConfigXEndString(id,i+1));
			windowYEndTextField[i].setText(configProperties.getConfigYEndString(id,i+1));
		}
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
	 * @see ngat.phase2.Ringo2PolarimeterConfig#TRIGGER_TYPE_EXTERNAL
	 * @see ngat.phase2.Ringo2PolarimeterConfig#TRIGGER_TYPE_INTERNAL
	 */
	public void actionPerformed(ActionEvent event)
	{
		String commandString = event.getActionCommand();
		String s = null;
		String triggerTypeString = null;
		int xStart[] = new int[WINDOW_COUNT];
		int yStart[] = new int[WINDOW_COUNT];
		int xEnd[] = new int[WINDOW_COUNT];
		int yEnd[] = new int[WINDOW_COUNT];
		int testId = 0;
		int i;
		int xBin,yBin,emGain,triggerType,windowFlags;
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
				xBin = Integer.parseInt(xBinTextField.getText());
				if(xBin < 1)
					throw new NumberFormatException("X Binning is less than one");
				yBin = Integer.parseInt(yBinTextField.getText());
				if(yBin < 1)
					throw new NumberFormatException("Y Binning is less than one");
				emGain = Integer.parseInt(emGainTextField.getText());
				if((emGain < 0)||(emGain > 100))
					throw new NumberFormatException("EM Gain is out of range 0..100");
				triggerTypeString  = (String)(triggerTypeComboBox.getSelectedItem());
				if(triggerTypeString.equals("external"))
					triggerType = Ringo2PolarimeterConfig.TRIGGER_TYPE_EXTERNAL;
				else if(triggerTypeString.equals("internal"))
					triggerType = Ringo2PolarimeterConfig.TRIGGER_TYPE_INTERNAL;
				else // Show an error message
				{
					JOptionPane.showMessageDialog((Component)null,
								      (Object)("The trigger type `"+triggerTypeString+
									       "' was not recognised."),
								      " Illegal trigger type ",
								      JOptionPane.ERROR_MESSAGE);
					return;
				}
				windowFlags = 0;
				for(i=0;i<WINDOW_COUNT;i++)
				{
					if(windowFlagCheckBox[i].isSelected())
						windowFlags |= (1<<i);
					xStart[i] = Integer.parseInt(windowXStartTextField[i].getText());
					yStart[i] = Integer.parseInt(windowYStartTextField[i].getText());
					xEnd[i] = Integer.parseInt(windowXEndTextField[i].getText());
					yEnd[i] = Integer.parseInt(windowYEndTextField[i].getText());
				}
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
			configProperties.setConfigType(configId,IcsGUIConfigProperties.
							CONFIG_TYPE_POLARIMETER_RINGO2);
			configProperties.setConfigCalibrateBefore(configId,calibrateBeforeCheckBox.isSelected());
			configProperties.setConfigCalibrateAfter(configId,calibrateAfterCheckBox.isSelected());
			configProperties.setConfigXBin(configId,xBin);
			configProperties.setConfigYBin(configId,yBin);
			configProperties.setConfigEMGain(configId,emGain);
			configProperties.setConfigTriggerType(configId,triggerType);
			configProperties.setConfigWindowFlags(configId,windowFlags);
			for(i=0;i<WINDOW_COUNT;i++)
			{
				configProperties.setConfigXStart(configId,i+1,xStart[i]);
				configProperties.setConfigYStart(configId,i+1,yStart[i]);
				configProperties.setConfigXEnd(configId,i+1,xEnd[i]);
				configProperties.setConfigYEnd(configId,i+1,yEnd[i]);
			}
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
// Revision 1.1  2009/11/24 14:17:10  cjm
// Initial revision
//
//
