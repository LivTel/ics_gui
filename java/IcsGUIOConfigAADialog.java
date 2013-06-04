/*   
    Copyright 2006, Astrophysics Research Institute, Liverpool John Moores University.

    This file is part of CcsGUI.

    CcsGUI is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    CcsGUI is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with CcsGUI; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/
// IcsGUIOConfigAADialog.java
// $Header: /home/cjm/cvs/ics_gui/java/IcsGUIOConfigAADialog.java,v 1.4 2013-06-04 09:00:22 cjm Exp $
import java.io.*;
import java.lang.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import ngat.phase2.OConfig;
import ngat.swing.*;
import ngat.util.*;

/**
 * This class provides an Add and Amend facility for 'O' optical camera Configurations.
 * @author Chris Mottram
 * @version $Revision: 1.4 $
 */
public class IcsGUIOConfigAADialog extends JDialog implements ActionListener
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: IcsGUIOConfigAADialog.java,v 1.4 2013-06-04 09:00:22 cjm Exp $");
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
	CcsConfigAADialogListener listener = null;
	/**
	 * A reference to the IcsGUI instance, used for logging calls.
	 */
	protected IcsGUI icsGUI = null;
	/**
	 * A copy of the reference to the instance of CcsGUIStatus.
	 */
	protected CcsGUIStatus icsGUIStatus = null;

	JTextField nameTextField = null;
	JCheckBox calibrateBeforeCheckBox = null;
	JCheckBox calibrateAfterCheckBox = null;
	/**
	 * An array of JComboBox's, one per filter wheel (actually 1 wheel and 2 slides).
	 * IO:O filters are 1-based, so the array has four elements, 0 is empty, 1 is O_FILTER_INDEX_FILTER_WHEEL,
	 * 2 is O_FILTER_INDEX_FILTER_SLIDE_LOWER and 3 is O_FILTER_INDEX_FILTER_SLIDE_UPPER.
	 * @see ngat.phase2.OConfig#O_FILTER_INDEX_COUNT
	 * @see ngat.phase2.OConfig#O_FILTER_INDEX_FILTER_WHEEL
	 * @see ngat.phase2.OConfig#O_FILTER_INDEX_FILTER_SLIDE_LOWER
	 * @see ngat.phase2.OConfig#O_FILTER_INDEX_FILTER_SLIDE_UPPER
	 */
	JComboBox filterComboBox[] = new JComboBox[OConfig.O_FILTER_INDEX_COUNT];
	JTextField xBinTextField = null;
	JTextField yBinTextField = null;
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
	public IcsGUIOConfigAADialog(Frame owner,IcsGUIConfigProperties c)
	{
		super(owner,"Add/Amend O Camera Configuration");
//		setResizable(false);
		configProperties = c;
	// there are 17 fields arranged vertically.
	// there are 2 titled border height from 2 sets of titled border
	// there is one set of buttons vertically 
		int height = (17*FIELD_HEIGHT)+(2*TITLED_BORDER_HEIGHT)+BUTTON_HEIGHT;

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
		// filters
		for(int i = OConfig.O_FILTER_INDEX_FILTER_WHEEL; i <= OConfig.O_FILTER_INDEX_FILTER_SLIDE_UPPER; i++)
		{
			// sub panel
			subPanel = new JPanel();
			getContentPane().add(subPanel);
			subPanel.setLayout(new SizedGridLayout(0,2,new Dimension(DIALOG_WIDTH,FIELD_HEIGHT)));
			// filter position combo box
			switch(i)
			{
				case OConfig.O_FILTER_INDEX_FILTER_WHEEL:
					label = new JLabel("Filter Wheel");
					break;
				case OConfig.O_FILTER_INDEX_FILTER_SLIDE_LOWER:
					label = new JLabel("Lower Filter Slide");
					break;
				case OConfig.O_FILTER_INDEX_FILTER_SLIDE_UPPER:
					label = new JLabel("Upper Filter Slide");
					break;
			}
			subPanel.add(label);
			filterComboBox[i] = new JComboBox();
			subPanel.add(filterComboBox[i]);
		}
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
	// Windows
		windowFlagCheckBox = new JCheckBox[WINDOW_COUNT];
		windowXStartTextField = new JTextField[WINDOW_COUNT];
		windowYStartTextField = new JTextField[WINDOW_COUNT];
		windowXEndTextField = new JTextField[WINDOW_COUNT];
		windowYEndTextField = new JTextField[WINDOW_COUNT];
		for(int i = 0;i < WINDOW_COUNT; i++)
		{
			if((i%2)==0)
			{
			// window panel
				subPanel = new JPanel();
				getContentPane().add(subPanel);
				subPanel.setLayout(new GridLayout(0,2));
			}
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
	 * @param s The instance of CcsGUIStatus to use.
	 * @see #icsGUIStatus
	 */
	public void setIcsGUIStatus(CcsGUIStatus s)
	{
		icsGUIStatus = s;
	}

	/**
	 * Method to manage the config Add/Amend dialog in add mode.
	 * @see #setFilterComboBoxs
	 */
	public void add()
	{
		configId = configProperties.getNewConfigId();

		nameTextField.setText("Configuration "+configId);
		calibrateBeforeCheckBox.setSelected(false);
		calibrateAfterCheckBox.setSelected(false);
		try
		{
			setFilterComboBoxs();
		}
		catch(Exception e)
		{
			System.err.println(this.getClass().getName()+":add:"+e);
			e.printStackTrace(System.err);
		}
		for(int i = OConfig.O_FILTER_INDEX_FILTER_WHEEL; i <= OConfig.O_FILTER_INDEX_FILTER_SLIDE_UPPER; i++)
		{
			filterComboBox[i].setSelectedItem("None");
		}
		xBinTextField.setText("1");
		yBinTextField.setText("1");
		for(int i=0;i<WINDOW_COUNT;i++)
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
	 * @see #setFilterComboBoxs
	 */
	public void amend(int id)
	{
		configId = id;

		nameTextField.setText(configProperties.getConfigName(id));
		calibrateBeforeCheckBox.setSelected(configProperties.getConfigCalibrateBefore(id));
		calibrateAfterCheckBox.setSelected(configProperties.getConfigCalibrateAfter(id));
		try
		{
			setFilterComboBoxs();
		}
		catch(Exception e)
		{
			icsGUI.error(this.getClass().getName()+":add:"+e);
			System.err.println(this.getClass().getName()+":add:"+e);
			e.printStackTrace(System.err);
		}
		for(int i = OConfig.O_FILTER_INDEX_FILTER_WHEEL; i <= OConfig.O_FILTER_INDEX_FILTER_SLIDE_UPPER; i++)
		{
			filterComboBox[i].setSelectedItem(configProperties.getConfigFilterWheel(id,i));
		}
		xBinTextField.setText(configProperties.getConfigXBinString(id));
		yBinTextField.setText(configProperties.getConfigYBinString(id));
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
	public void addCcsConfigAADialogListener(CcsConfigAADialogListener l)
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
		int xBin,yBin,windowFlags;
		int xStart[] = new int[WINDOW_COUNT];
		int yStart[] = new int[WINDOW_COUNT];
		int xEnd[] = new int[WINDOW_COUNT];
		int yEnd[] = new int[WINDOW_COUNT];
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
				windowFlags = 0;
				for(int i=0;i<WINDOW_COUNT;i++)
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
			configProperties.setConfigType(configId,IcsGUIConfigProperties.CONFIG_TYPE_CCD_O);
			configProperties.setConfigCalibrateBefore(configId,calibrateBeforeCheckBox.isSelected());
			configProperties.setConfigCalibrateAfter(configId,calibrateAfterCheckBox.isSelected());
			for(int i = OConfig.O_FILTER_INDEX_FILTER_WHEEL; 
			    i <= OConfig.O_FILTER_INDEX_FILTER_SLIDE_UPPER; i++)
			{
				configProperties.setConfigFilterWheel(configId,i,
								      (String)(filterComboBox[i].getSelectedItem()));
			}
			configProperties.setConfigXBin(configId,xBin);
			configProperties.setConfigYBin(configId,yBin);
			configProperties.setConfigWindowFlags(configId,windowFlags);
			for(int i=0;i<WINDOW_COUNT;i++)
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

	/**
	 * Method to populate the filter wheel combo boxs with filters loaded from a property file specified
	 * in the main set of properties.
	 * @see #filterComboBox
	 * @see #icsGUIStatus
	 * @exception FileNotFoundException Thrown if the current filter properties cannot be found.
	 * @exception IOException Thrown if an IO error occurs whilst loading the filter properties.
	 * @exception NGATPropertyException Thrown if a property has an illegal value.
	 * @exception IllegalArgumentException Thrown if the filter index is an unexpected value.
	 * @see #filterComboBox
	 */
	protected void setFilterComboBoxs() throws FileNotFoundException,IOException,NGATPropertyException,
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
		filterPropertiesFilename = icsGUIStatus.getProperty("ics_gui.filter.property.filename.o");
		if((filterPropertiesFilename == null)||(filterPropertiesFilename.equals("")))
		{
			icsGUI.log(this.getClass().getName()+
					   ":setFilterComboBoxs:filterPropertiesFilename is null.");
			return;
		}
		icsGUI.log(this.getClass().getName()+":setFilterComboBoxs:filterPropertiesFilename is "+
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
		for(int filterWheelIndex = 1; filterWheelIndex <= filterWheelCount; filterWheelIndex++)
		{
			if(filterComboBox[filterWheelIndex].getItemCount() > 0)
				filterComboBox[filterWheelIndex].removeAllItems();
		}
		// for each filter wheel
		for(int filterWheelIndex = 1; filterWheelIndex <= filterWheelCount; filterWheelIndex++)
		{
			// loop over number of filters in wheel
			filterCount = filterProperties.getInt("filterwheel."+filterWheelIndex+".count");
			icsGUI.log(this.getClass().getName()+":setFilterComboBoxs:filterWheelIndex: "+
				   filterWheelIndex+":filterCount is "+filterCount+".");
			for(int i  = 0; i < filterCount; i++)
			{
				// get filter type name
				filterName = filterProperties.getProperty("filterwheel."+filterWheelIndex+"."+
									  i+".type");
				icsGUI.log(this.getClass().getName()+":setFilterComboBoxs:filterWheelIndex: "+
					   filterWheelIndex+":filter "+i+" is "+filterName+".");
				// add to relevant combobox
				filterComboBox[filterWheelIndex].addItem(filterName);
			}
		}
		icsGUI.log(this.getClass().getName()+":setFilterComboBoxs:Finished.");
	}
}
//
// $Log: not supported by cvs2svn $
// Revision 1.3  2013/06/04 08:08:41  cjm
// Added support for neutral density filter slides.
//
// Revision 1.2  2011/11/09 11:41:53  cjm
// Fixed comments.
//
// Revision 1.1  2011/11/07 17:07:34  cjm
// Initial revision
//
//
