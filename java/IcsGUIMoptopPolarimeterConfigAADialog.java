//  IcsGUIMoptopPolarimeterConfigAADialog.java
// $Header$
import java.io.*;
import java.lang.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import ngat.swing.*;
import ngat.phase2.MOPTOPPolarimeterConfig;
import ngat.util.*;

/**
 * This class provides an Add and Amend facility for Moptop polarimeter Configurations.
 * @author Chris Mottram
 * @version $Revision$
 */
public class IcsGUIMoptopPolarimeterConfigAADialog extends JDialog implements ActionListener
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id$");
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
	/**
	 * A reference to the IcsGUI instance, used for logging calls.
	 */
	protected IcsGUI icsGUI = null;
	/**
	 * A copy of the reference to the instance of IcsGUIStatus.
	 */
	protected IcsGUIStatus icsGUIStatus = null;
	JTextField nameTextField = null;
	JCheckBox calibrateBeforeCheckBox = null;
	JCheckBox calibrateAfterCheckBox = null;
	JComboBox filterComboBox = null;
	JComboBox rotorSpeedComboBox = null;
	// one per detector?
	JTextField xBinTextField = null;
	JTextField yBinTextField = null;

	/**
	 * Constructor.
	 * @param owner The parent frame of this dialog. Used in calling Dialog's constructor.
	 * @param c The configuration properties.
	 */
	public IcsGUIMoptopPolarimeterConfigAADialog(Frame owner,IcsGUIConfigProperties c)
	{
		super(owner,"Add/Amend Moptop Configuration");
//		setResizable(false);
		configProperties = c;
	// there are 6 fields arranged vertically.
	// there are 1 titled border height from 1 sets of titled border
	// there is one set of buttons vertically 
		int height = (6*FIELD_HEIGHT)+TITLED_BORDER_HEIGHT+BUTTON_HEIGHT;

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
	// filter position
		label = new JLabel("Filter");
		subPanel.add(label);
		filterComboBox = new JComboBox();
		subPanel.add(filterComboBox);
	// sub panel
		subPanel = new JPanel();
		getContentPane().add(subPanel);
		subPanel.setLayout(new SizedGridLayout(0,2,new Dimension(DIALOG_WIDTH,FIELD_HEIGHT)));
	// rotor speed
		label = new JLabel("Rotor Speed");
		subPanel.add(label);
		rotorSpeedComboBox = new JComboBox();
		rotorSpeedComboBox.setEditable(false);
		rotorSpeedComboBox.addItem("slow");
		rotorSpeedComboBox.addItem("fast");
		subPanel.add(rotorSpeedComboBox);
		// two sets of these, one per detector?
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
	 * Set the IcsGUI reference. Used for logging.
	 * @param o The instance of IcsGUI to use.
	 * @see #icsGUI
	 */
	public void setIcsGUI(IcsGUI o)
	{
		icsGUI = o;
	}

	/**
	 * Set the IcsGUIStatus reference. Used to get a list of valid filters for the combo boxs.
	 * @param s The instance of IcsGUIStatus to use.
	 * @see #icsGUIStatus
	 */
	public void setIcsGUIStatus(IcsGUIStatus s)
	{
		icsGUIStatus = s;
	}

	/**
	 * Method to manage the config Add/Amend dialog in add mode.
	 * @see #setFilterComboBox
	 */
	public void add()
	{
		int i;

		configId = configProperties.getNewConfigId();

		nameTextField.setText("Configuration "+configId);
		calibrateBeforeCheckBox.setSelected(false);
		calibrateAfterCheckBox.setSelected(false);
		try
		{
			setFilterComboBox();
		}
		catch(Exception e)
		{
			System.err.println(this.getClass().getName()+":add:"+e);
			e.printStackTrace(System.err);
		}
		filterComboBox.setSelectedIndex(0);
		rotorSpeedComboBox.setSelectedItem("fast");
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
		try
		{
			setFilterComboBox();
		}
		catch(Exception e)
		{
			icsGUI.error(this.getClass().getName()+":amend:"+e);
			System.err.println(this.getClass().getName()+":amend:"+e);
			e.printStackTrace(System.err);
		}
		filterComboBox.setSelectedItem(configProperties.getConfigFilterWheel(id));
		rotorSpeedComboBox.setSelectedItem(configProperties.getConfigRotorSpeedString(id));
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
		String filterName = null;
		String rotorSpeedString = null;
		int testId = 0;
		int i;
		int xBin,yBin,rotorSpeed;
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
			filterName = (String)(filterComboBox.getSelectedItem());
		// get and test numeric data from text fields
			try
			{
				rotorSpeedString = (String)(rotorSpeedComboBox.getSelectedItem());
				if(rotorSpeedString.equals("slow"))
					rotorSpeed = MOPTOPPolarimeterConfig.ROTOR_SPEED_SLOW;
				else if(rotorSpeedString.equals("fast"))
					rotorSpeed = MOPTOPPolarimeterConfig.ROTOR_SPEED_FAST;
				else
					rotorSpeed = 0;
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
			configProperties.setConfigType(configId,IcsGUIConfigProperties.
							CONFIG_TYPE_POLARIMETER_MOPTOP);
			configProperties.setConfigCalibrateBefore(configId,calibrateBeforeCheckBox.isSelected());
			configProperties.setConfigCalibrateAfter(configId,calibrateAfterCheckBox.isSelected());
			configProperties.setConfigFilterWheel(configId,filterName);
			configProperties.setConfigRotorSpeed(configId,rotorSpeed);
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
	
	/**
	 * Method to populate the filter wheel combo box with filters loaded from a property file specified
	 * in the main set of properties.
	 * @see #filterComboBox
	 * @see #icsGUIStatus
	 * @exception FileNotFoundException Thrown if the current filter properties cannot be found.
	 * @exception IOException Thrown if an IO error occurs whilst loading the filter properties.
	 * @exception NGATPropertyException Thrown if a property has an illegal value.
	 * @exception IllegalArgumentException Thrown if the filter index is an unexpected value.
	 * @see #filterComboBox
	 */
	protected void setFilterComboBox() throws FileNotFoundException,IOException,NGATPropertyException,
						       IllegalArgumentException
	{
		NGATProperties filterProperties = null;
		FileInputStream fileInputStream = null;
		String filterPropertiesFilename = null;
		String filterName = null;
		int filterWheelCount,filterCount;

		icsGUI.log(this.getClass().getName()+":setFilterComboBoxs:Started.");
		if(icsGUIStatus == null)
		{
			icsGUI.log(this.getClass().getName()+":setFilterComboBoxs:icsGUIStatus is null.");
			return;
		}
		// get filter property filename
		filterPropertiesFilename = icsGUIStatus.getProperty("ics_gui.filter.property.filename.moptop");
		if((filterPropertiesFilename == null)||(filterPropertiesFilename.equals("")))
		{
			icsGUI.log(this.getClass().getName()+
					   ":setFilterComboBox:filterPropertiesFilename is null.");
			return;
		}
		icsGUI.log(this.getClass().getName()+":setFilterComboBox:filterPropertiesFilename is "+
				   filterPropertiesFilename+".");
		// load filterProperties
		filterProperties = new NGATProperties();
		fileInputStream = new FileInputStream(filterPropertiesFilename);
		filterProperties.load(fileInputStream);
		fileInputStream.close();
		filterWheelCount = filterProperties.getInt("filterwheel.count");
		icsGUI.log(this.getClass().getName()+":setFilterComboBoxs:filterWheelCount is "+
			   filterWheelCount+".");
		// clear current combo box list, ready for latest one.
		// Note must check items are in list first, as old JVMs(1.2.1) throw IndexOutOfBoundsException
		if(filterComboBox.getItemCount() > 0)
			filterComboBox.removeAllItems();
		for(int filterWheelIndex = 1; filterWheelIndex <= filterWheelCount; filterWheelIndex++)
		{
			// loop over number of filters in wheel
			filterCount = filterProperties.getInt("filterwheel."+filterWheelIndex+".count");
			icsGUI.log(this.getClass().getName()+":setFilterComboBoxs:filterWheelIndex: "+
				   filterWheelIndex+":filterCount is "+filterCount+".");
			for(int i  = 1; i <= filterCount; i++)
			{
				// get filter type name
				filterName = filterProperties.getProperty("filterwheel."+filterWheelIndex+"."+
									  i+".type");
				icsGUI.log(this.getClass().getName()+":setFilterComboBox:filterWheelIndex: "+
					   filterWheelIndex+":filter "+i+" is "+filterName+".");
				// add to relevant combobox
				filterComboBox.addItem(filterName);
			}
		}
		icsGUI.log(this.getClass().getName()+":setFilterComboBox:Finished.");
	}
}
