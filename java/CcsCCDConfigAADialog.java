// CcsCCDConfigAADialog.java -*- mode: Fundamental;-*-
// $Header: /home/cjm/cvs/ics_gui/java/CcsCCDConfigAADialog.java,v 0.3 2000-07-12 14:16:18 cjm Exp $
import java.lang.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import ngat.swing.*;

/**
 * This class provides an Add and Amend facility for CCD Configurations.
 * @author Chris Mottram
 * @version $Revision: 0.3 $
 */
public class CcsCCDConfigAADialog extends JDialog implements ActionListener
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: CcsCCDConfigAADialog.java,v 0.3 2000-07-12 14:16:18 cjm Exp $");
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
	CcsCCDConfigProperties ccdConfigProperties = null;
	/**
	 * The id of the configuration we are modifying.
	 */
	int ccdConfigId = 0;
	/**
	 * The listener for this dialog.
	 */
	CcsConfigAADialogListener listener = null;

	JTextField nameTextField = null;
	JTextField lowerFilterWheelTextField = null;
	JTextField upperFilterWheelTextField = null;
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
	 * @param c The ccd configuration properties.
	 */
	public CcsCCDConfigAADialog(Frame owner,CcsCCDConfigProperties c)
	{
		super(owner,"Add/Amend CCD Configuration");
		setResizable(false);
		ccdConfigProperties = c;
	// there are 15 fields arranged vertically.
	// there are 2 titled border height from 2 sets of titled border
	// there is one set of buttons vertically 
		int height = (15*FIELD_HEIGHT)+(2*TITLED_BORDER_HEIGHT)+BUTTON_HEIGHT;

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
	// lower filter wheel
		label = new JLabel("Lower Filter Wheel");
		subPanel.add(label);
		lowerFilterWheelTextField = new JTextField();
		subPanel.add(lowerFilterWheelTextField);
	// sub panel
		subPanel = new JPanel();
		getContentPane().add(subPanel);
		subPanel.setLayout(new SizedGridLayout(0,2,new Dimension(DIALOG_WIDTH,FIELD_HEIGHT)));
	// upper filter wheel
		label = new JLabel("Upper Filter Wheel");
		subPanel.add(label);
		upperFilterWheelTextField = new JTextField();
		subPanel.add(upperFilterWheelTextField);
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
	 * Method to manage the config Add/Amend dialog in add mode.
	 */
	public void add()
	{
		int i;

		ccdConfigId = ccdConfigProperties.getNewConfigId();

		nameTextField.setText("Configuration "+ccdConfigId);
		lowerFilterWheelTextField.setText("None");
		upperFilterWheelTextField.setText("None");
		xBinTextField.setText("1");
		yBinTextField.setText("1");
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
		ccdConfigId = id;

		nameTextField.setText(ccdConfigProperties.getConfigName(id));
		lowerFilterWheelTextField.setText(ccdConfigProperties.getConfigLowerFilterWheel(id));
		upperFilterWheelTextField.setText(ccdConfigProperties.getConfigUpperFilterWheel(id));
		xBinTextField.setText(ccdConfigProperties.getConfigXBinString(id));
		yBinTextField.setText(ccdConfigProperties.getConfigYBinString(id));
		for(int i=0;i<WINDOW_COUNT;i++)
		{
			windowFlagCheckBox[i].setSelected((ccdConfigProperties.getConfigWindowFlags(id)&(1<<i))>0);
			windowXStartTextField[i].setText(ccdConfigProperties.getConfigXStartString(id,i+1));
			windowYStartTextField[i].setText(ccdConfigProperties.getConfigYStartString(id,i+1));
			windowXEndTextField[i].setText(ccdConfigProperties.getConfigXEndString(id,i+1));
			windowYEndTextField[i].setText(ccdConfigProperties.getConfigYEndString(id,i+1));
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
		int i;
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
				testId = ccdConfigProperties.getIdFromName(s);
				uniqueId = (testId == ccdConfigId);
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
			ccdConfigProperties.setConfigName(ccdConfigId,s);
			ccdConfigProperties.setConfigLowerFilterWheel(ccdConfigId,lowerFilterWheelTextField.getText());
			ccdConfigProperties.setConfigUpperFilterWheel(ccdConfigId,upperFilterWheelTextField.getText());
			ccdConfigProperties.setConfigXBin(ccdConfigId,xBin);
			ccdConfigProperties.setConfigYBin(ccdConfigId,yBin);
			ccdConfigProperties.setConfigWindowFlags(ccdConfigId,windowFlags);
			for(i=0;i<WINDOW_COUNT;i++)
			{
				ccdConfigProperties.setConfigXStart(ccdConfigId,i+1,xStart[i]);
				ccdConfigProperties.setConfigYStart(ccdConfigId,i+1,yStart[i]);
				ccdConfigProperties.setConfigXEnd(ccdConfigId,i+1,xEnd[i]);
				ccdConfigProperties.setConfigYEnd(ccdConfigId,i+1,yEnd[i]);
			}
			if(listener != null)
				listener.actionPerformed(true,ccdConfigId);
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
// Revision 0.2  2000/02/08 17:39:56  cjm
// Added window flags check-box.
//
// Revision 0.1  1999/12/09 16:39:57  cjm
// initial revision.
//
//