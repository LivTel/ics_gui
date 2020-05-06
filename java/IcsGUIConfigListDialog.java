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
// IcsGUIConfigListDialog.java
// $Header: /home/cjm/cvs/ics_gui/java/IcsGUIConfigListDialog.java,v 0.21 2020-05-05 10:20:39 cjm Exp $
import java.lang.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import ngat.swing.*;

/**
 * This class provides a list of configurations for the instrument GUI. 
 */
public class IcsGUIConfigListDialog extends JDialog implements ActionListener, IcsConfigAADialogListener
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public static String RCSID = new String("$Id$");
	/**
	 * String to go on buttons.
	 */
	protected final static String CCD_RATCAM_BUTTON_STRING = "CCD Camera (RATCam)";
	/**
	 * String to go on buttons.
	 */
	protected final static String SPECTROGRAPH_MES_BUTTON_STRING = "Spectrograph (MES)";
	/**
	 * String to go on buttons.
	 */
	protected final static String SPECTROGRAPH_NUVIEW_BUTTON_STRING = "Spectrograph (Meaburn)";
	/**
	 * String to go on buttons.
	 */
	protected final static String INFRA_RED_SUPIRCAM_BUTTON_STRING = "Infra Red Camera (SupIRCam/IO:I)";
	/**
	 * String to go on buttons.
	 */
	protected final static String SPECTROGRAPH_FTSPEC_BUTTON_STRING = "Spectrograph (FTSpec)";
	/**
	 * String to go on buttons.
	 */
	protected final static String POLARIMETER_RINGOSTAR_BUTTON_STRING = "Polarimeter (Ringo Star)";
	/**
	 * String to go on buttons.
	 */
	protected final static String SPECTROGRAPH_FRODOSPEC_BUTTON_STRING = "Spectrograph (FrodoSpec)";
	/**
	 * String to go on buttons.
	 */
	protected final static String CCD_RISE_BUTTON_STRING = "CCD Camera (RISE)";
	/**
	 * String to go on buttons.
	 */
	protected final static String POLARIMETER_RINGO2_BUTTON_STRING = "Polarimeter (Ringo 2)";
	/**
	 * String to go on buttons.
	 */
	protected final static String CCD_THOR_BUTTON_STRING = "CCD Camera (THOR)";
	/**
	 * String to go on buttons.
	 */
	protected final static String CCD_O_BUTTON_STRING = "CCD Camera (O)";
	/**
	 * String to go on buttons.
	 */
	protected final static String POLARIMETER_RINGO3_BUTTON_STRING = "Polarimeter (Ringo 3)";
	/**
	 * String to go on buttons.
	 */
	protected final static String SPECTROGRAPH_SPRAT_BUTTON_STRING = "Spectrograph (Sprat)";
	/**
	 * String to go on buttons.
	 */
	protected final static String SPECTROGRAPH_LOTUS_BUTTON_STRING = "Spectrograph (LOTUS)";
	/**
	 * String to go on buttons.
	 */
	protected final static String POLARIMETER_MOPTOP_BUTTON_STRING = "Polarimeter (Moptop)";
	/**
	 * List of strings that describe instruments. Note, make sure in the same order as 
	 * IcsGUIConfigProperties.CONFIG_TYPE_LIST.
	 * @see IcsGUIConfigProperties#CONFIG_TYPE_LIST
	 * @see #CCD_RATCAM_BUTTON_STRING
	 * @see #SPECTROGRAPH_MES_BUTTON_STRING
	 * @see #SPECTROGRAPH_NUVIEW_BUTTON_STRING
	 * @see #INFRA_RED_SUPIRCAM_BUTTON_STRING
	 * @see #SPECTROGRAPH_FTSPEC_BUTTON_STRING
	 * @see #POLARIMETER_RINGOSTAR_BUTTON_STRING
	 * @see #SPECTROGRAPH_FRODOSPEC_BUTTON_STRING
	 * @see #CCD_RISE_BUTTON_STRING
	 * @see #POLARIMETER_RINGO2_BUTTON_STRING
	 * @see #CCD_THOR_BUTTON_STRING
	 * @see #CCD_O_BUTTON_STRING
	 * @see #POLARIMETER_RINGO3_BUTTON_STRING
	 * @see #SPECTROGRAPH_SPRAT_BUTTON_STRING
	 * @see #SPECTROGRAPH_LOTUS_BUTTON_STRING
	 * @see #POLARIMETER_MOPTOP_BUTTON_STRING
	 */
	protected final static String INSTRUMENT_STRING_ARRAY[] = {CCD_RATCAM_BUTTON_STRING,
		SPECTROGRAPH_MES_BUTTON_STRING,SPECTROGRAPH_NUVIEW_BUTTON_STRING,
	        INFRA_RED_SUPIRCAM_BUTTON_STRING,SPECTROGRAPH_FTSPEC_BUTTON_STRING,
		POLARIMETER_RINGOSTAR_BUTTON_STRING,SPECTROGRAPH_FRODOSPEC_BUTTON_STRING,CCD_RISE_BUTTON_STRING,
		POLARIMETER_RINGO2_BUTTON_STRING,CCD_THOR_BUTTON_STRING,CCD_O_BUTTON_STRING,
		POLARIMETER_RINGO3_BUTTON_STRING,SPECTROGRAPH_SPRAT_BUTTON_STRING,SPECTROGRAPH_LOTUS_BUTTON_STRING,
								   POLARIMETER_MOPTOP_BUTTON_STRING};
	/**
	 * String to pre-pend to add menu instrument entries.
	 */
	protected final static String ADD_STRING = "Add ";
	/**
	 * String to pre-pend to filter menu instrument entries.
	 */
	protected final static String FILTER_STRING = "By ";
	/**
	 * String used to specify no filtering buttons.
	 */
	protected final static String NONE_STRING = "None";
	/**
	 * Number used to specify no filtering of the list.
	 */
	protected final static int NONE_FILTER_NUMBER = -1;
	/**
	 * The data to be displayed in the list.
	 */
	private IcsGUIConfigProperties configProperties = null;
	/**
	 * The Add/Amend dialog to use when Add or Amend is selected for a CCD configuration.
	 */
	private IcsGUICCDConfigAADialog addAmendCCDDialog = null;
	/**
	 * The Add/Amend dialog to use when Add or Amend is selected for a NuView configuration.
	 */
	private IcsGUILowResSpecConfigAADialog addAmendNuViewDialog = null;
	/**
	 * The Add/Amend dialog to use when Add or Amend is selected for a NuView configuration.
	 */
	private IcsGUIIRCamConfigAADialog addAmendIRCamDialog = null;
	/**
	 * The Add/Amend dialog to use when Add or Amend is selected for a FTSpec configuration.
	 */
	private IcsGUIFixedFormatSpecConfigAADialog addAmendFTSpecDialog = null;
	/**
	 * The Add/Amend dialog to use when Add or Amend is selected for a Polarimeter (Ringo Star) configuration.
	 */
	private IcsGUIPolarimeterConfigAADialog addAmendPolarimeterDialog = null;
	/**
	 * The Add/Amend dialog to use when Add or Amend is selected for a FrodoSpec configuration.
	 */
	private IcsGUIFrodoSpecConfigAADialog addAmendFrodoSpecDialog = null;
	/**
	 * The Add/Amend dialog to use when Add or Amend is selected for a RISE configuration.
	 */
	private IcsGUIRISEConfigAADialog addAmendRISEDialog = null;
	/**
	 * The Add/Amend dialog to use when Add or Amend is selected for a Polarimeter (Ringo 2) configuration.
	 */
	private IcsGUIRingo2PolarimeterConfigAADialog addAmendRingo2PolarimeterDialog = null;
	/**
	 * The Add/Amend dialog to use when Add or Amend is selected for a THOR configuration.
	 */
	private IcsGUITHORConfigAADialog addAmendTHORDialog = null;
	/**
	 * The Add/Amend dialog to use when Add or Amend is selected for a O configuration.
	 */
	private IcsGUIOConfigAADialog addAmendODialog = null;
	/**
	 * The Add/Amend dialog to use when Add or Amend is selected for a Polarimeter (Ringo 3) configuration.
	 */
	private IcsGUIRingo3PolarimeterConfigAADialog addAmendRingo3PolarimeterDialog = null;
	/**
	 * The Add/Amend dialog to use when Add or Amend is selected for a Sprat configuration.
	 */
	private IcsGUISpratConfigAADialog addAmendSpratDialog = null;
	/**
	 * The Add/Amend dialog to use when Add or Amend is selected for a LOTUS configuration.
	 */
	private IcsGUILOTUSConfigAADialog addAmendLOTUSDialog = null;
	/**
	 * The Add/Amend dialog to use when Add or Amend is selected for a Polarimeter (Moptop) configuration.
	 */
	private IcsGUIMoptopPolarimeterConfigAADialog addAmendMoptopPolarimeterDialog = null;
	/**
	 * The config dialog that caused this dialog to be managed.
	 */
	private CONFIGDialog configDialog = null;
	/**
	 * A copy of the reference to the instance of IcsGUI.
	 */
	private IcsGUI icsGUI = null;
	/**
	 * A copy of the reference to the instance of IcsGUIStatus.
	 */
	private IcsGUIStatus icsGUIStatus = null;
	/**
	 * Internal JList that actually displays the Instrument Configurations.
	 */
	private JList list = null;
	/**
	 * Number representing which type of instrument configuration to filter the list by.
	 * Defaults to NONE_FILTER_NUMBER, which means no filtering takes place.
	 * @see #NONE_FILTER_NUMBER
	 */
	private int filterTypeNumber = NONE_FILTER_NUMBER;

	/**
	 * Constructor. Calls the JDialog constructor. Creates the components in the dialog.
	 * Creates the add/amend dialogs.
	 * Also copies IcsGUI reference (and associated icsGUIStatus) to pass on to Add/Amend boxs.
	 * @param owner The parent frame of this dialog. Used in calling Dialog's constructor.
	 * @param c A reference to the object holding the instrument Configuration data. Used to fill the list,
	 *	and for updating purposes.
	 * @param i The instance of the IcsGUI that is creating this dialog.
	 * @see #constructFileMenu
	 * @see #constructFilterMenu
	 * @see #icsGUI
	 * @see #icsGUIStatus
	 * @see IcsGUI#getRemoteX
	 */
	public IcsGUIConfigListDialog(Frame owner,IcsGUIConfigProperties c,IcsGUI i)
	{
		super(owner,"Instrument Configuration List");

		JScrollPane scrollPane = null;
		JMenu menu = null;
		JMenuItem menuItem = null;

		configProperties = c;
		icsGUI = i;
		icsGUIStatus = icsGUI.getStatus();
		getContentPane().setLayout(new BorderLayout());
		setResizable(true);
	// setup menu bar
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
	// setup file menu
		constructFileMenu(menuBar);
	// setup filter menu
		constructFilterMenu(menuBar);
	// create the panel
		JPanel panel = new JPanel();
		panel.setMinimumSize(new Dimension(250,250));
		panel.setPreferredSize(new Dimension(250,250));
		panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		panel.setLayout(new BorderLayout());
	// Add the JPanel to the frame.
		getContentPane().add(panel);
	// create the list
		list = new JList();
		list.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	// create a scroll pane, add the list and add it to the panel
		scrollPane = new JScrollPane();
		scrollPane.getViewport().setView(list);
		// diddly Note, this remoteX fix will not compile on java 1.2 on the Solaris machines.
		//if(icsGUI.getRemoteX())
		//{
		//	scrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
		//}
		panel.add(scrollPane);
	// create a CCD add/amend dialog
		addAmendCCDDialog = new IcsGUICCDConfigAADialog(owner,c);
		addAmendCCDDialog.addIcsConfigAADialogListener(this);
	// create a NuView add/amend dialog
		addAmendNuViewDialog = new IcsGUILowResSpecConfigAADialog(owner,c);
		addAmendNuViewDialog.addIcsConfigAADialogListener(this);
	// create a IRCam add/amend dialog
		addAmendIRCamDialog = new IcsGUIIRCamConfigAADialog(owner,c);
		addAmendIRCamDialog.addIcsConfigAADialogListener(this);
	// create a FTSpec add/amend dialog
		addAmendFTSpecDialog = new IcsGUIFixedFormatSpecConfigAADialog(owner,c);
		addAmendFTSpecDialog.addIcsConfigAADialogListener(this);
	// create a Polarimater add/amend dialog
		addAmendPolarimeterDialog = new IcsGUIPolarimeterConfigAADialog(owner,c);
		addAmendPolarimeterDialog.addIcsConfigAADialogListener(this);
	// create a FrodoSpec add/amend dialog
		addAmendFrodoSpecDialog = new IcsGUIFrodoSpecConfigAADialog(owner,c);
		addAmendFrodoSpecDialog.addIcsConfigAADialogListener(this);
	// create a RISE add/amend dialog
		addAmendRISEDialog = new IcsGUIRISEConfigAADialog(owner,c);
		addAmendRISEDialog.addIcsConfigAADialogListener(this);
	// create a Ringo2 Polarimater add/amend dialog
		addAmendRingo2PolarimeterDialog = new IcsGUIRingo2PolarimeterConfigAADialog(owner,c);
		addAmendRingo2PolarimeterDialog.addIcsConfigAADialogListener(this);
	// create a THOR add/amend dialog
		addAmendTHORDialog = new IcsGUITHORConfigAADialog(owner,c);
		addAmendTHORDialog.addIcsConfigAADialogListener(this);
	// create a O add/amend dialog
		addAmendODialog = new IcsGUIOConfigAADialog(owner,c);
		addAmendODialog.addIcsConfigAADialogListener(this);
	// create a Ringo3 Polarimater add/amend dialog
		addAmendRingo3PolarimeterDialog = new IcsGUIRingo3PolarimeterConfigAADialog(owner,c);
		addAmendRingo3PolarimeterDialog.addIcsConfigAADialogListener(this);
	// create a Sprat add/amend dialog
		addAmendSpratDialog = new IcsGUISpratConfigAADialog(owner,c);
		addAmendSpratDialog.addIcsConfigAADialogListener(this);
	// create a LOTUS add/amend dialog
		addAmendLOTUSDialog = new IcsGUILOTUSConfigAADialog(owner,c);
		addAmendLOTUSDialog.addIcsConfigAADialogListener(this);
	// create a Moptop Polarimater add/amend dialog
		addAmendMoptopPolarimeterDialog = new IcsGUIMoptopPolarimeterConfigAADialog(owner,c);
		addAmendMoptopPolarimeterDialog.addIcsConfigAADialogListener(this);
	}

	/**
	 * This method sets the list up with data in the Instrument Config Prooperties.
	 * This method should be called <b>just</b> before the dialog is managed, to ensure the
	 * list is up to date with the properties.
	 * @see #configProperties
	 * @see IcsGUIConfigProperties#getConfigNameList
	 * @see #list
	 * @see #filterTypeNumber
	 * @see #NONE_FILTER_NUMBER
	 */
	public void setData()
	{
		Vector l = null;
		int i;

		if(filterTypeNumber == NONE_FILTER_NUMBER)
			l = configProperties.getConfigNameList();
		else
			l = configProperties.getConfigNameList(filterTypeNumber);
		list.setListData(l);
	}

	public boolean isResizable()
	{
		return true;
	}

	/**
	 * Method to set the config dialog instance that has managed this dialog.
	 * @param c The CONFIGDialog instance.
	 * @see #configDialog
	 */
	public void setConfigDialog(CONFIGDialog c)
	{
		configDialog = c;
	}

	/**
	 * ActionListener method. Called when a menu item is selected.
	 * @see #INSTRUMENT_STRING_ARRAY
	 * @see IcsGUIConfigProperties#CONFIG_TYPE_LIST
	 * @see #NONE_STRING
	 * @see #NONE_FILTER_NUMBER
	 */
	public void actionPerformed(ActionEvent event)
	{
		String commandString = event.getActionCommand();
		String s = null;
		Object o = null;
		int id = 0;
		int type = 0;
		int retval = 0;

		if(commandString.equals("Select"))
		{
		// get the selected string.
			o = list.getSelectedValue();
			if(o == null)
			{
				JOptionPane.showMessageDialog((Component)null,(Object)("Please Select an item."),
					" No Item Selected ",JOptionPane.WARNING_MESSAGE);
				return;
			}
			if(o instanceof String)
			{
				s = (String)o;
			// set the config dialogs string
				if(configDialog != null)
				{
					configDialog.setConfigTextField(s);
				// unmanage the dialog later - this allows the menu to disappear
					SwingUtilities.invokeLater(new GUIDialogUnmanager(this));
				}
			}
		}
	// check for add sub-menu
		for(int i = 0; i < INSTRUMENT_STRING_ARRAY.length; i++)
		{
			if(commandString.equals(ADD_STRING+INSTRUMENT_STRING_ARRAY[i]))
			{
			// This assumes INSTRUMENT_STRING_ARRAY[i] is the string description for the instrument
			// IcsGUIConfigProperties.CONFIG_TYPE_LIST[i].
				switch(i)
				{
					case IcsGUIConfigProperties.CONFIG_TYPE_CCD_RATCAM:
						addAmendCCDDialog.setLocation(getX()+getWidth(),getY());
						addAmendCCDDialog.pack();
						addAmendCCDDialog.setIcsGUIStatus(icsGUIStatus);
						addAmendCCDDialog.add();
						break;
					case IcsGUIConfigProperties.CONFIG_TYPE_SPECTROGRAPH_NUVIEW:
						addAmendNuViewDialog.setLocation(getX()+getWidth(),getY());
						addAmendNuViewDialog.pack();
						addAmendNuViewDialog.add();
						break;
					case IcsGUIConfigProperties.CONFIG_TYPE_INFRA_RED_SUPIRCAM:
						addAmendIRCamDialog.setLocation(getX()+getWidth(),getY());
						addAmendIRCamDialog.pack();
						addAmendIRCamDialog.add();
						break;
					case IcsGUIConfigProperties.CONFIG_TYPE_SPECTROGRAPH_FTSPEC:
						addAmendFTSpecDialog.setLocation(getX()+getWidth(),getY());
						addAmendFTSpecDialog.pack();
						addAmendFTSpecDialog.add();
						break;
					case IcsGUIConfigProperties.CONFIG_TYPE_POLARIMETER_RINGOSTAR:
						addAmendPolarimeterDialog.setLocation(getX()+getWidth(),getY());
						addAmendPolarimeterDialog.pack();
						addAmendPolarimeterDialog.add();
						break;
					case IcsGUIConfigProperties.CONFIG_TYPE_SPECTROGRAPH_FRODOSPEC:
						addAmendFrodoSpecDialog.setLocation(getX()+getWidth(),getY());
						addAmendFrodoSpecDialog.pack();
						addAmendFrodoSpecDialog.add();
						break;
					case IcsGUIConfigProperties.CONFIG_TYPE_CCD_RISE:
						addAmendRISEDialog.setLocation(getX()+getWidth(),getY());
						addAmendRISEDialog.pack();
						addAmendRISEDialog.add();
						break;
					case IcsGUIConfigProperties.CONFIG_TYPE_POLARIMETER_RINGO2:
						addAmendRingo2PolarimeterDialog.setLocation(getX()+getWidth(),getY());
						addAmendRingo2PolarimeterDialog.pack();
						addAmendRingo2PolarimeterDialog.add();
						break;
					case IcsGUIConfigProperties.CONFIG_TYPE_CCD_THOR:
						addAmendTHORDialog.setLocation(getX()+getWidth(),getY());
						addAmendTHORDialog.pack();
						addAmendTHORDialog.add();
						break;
					case IcsGUIConfigProperties.CONFIG_TYPE_CCD_O:
						addAmendODialog.setLocation(getX()+getWidth(),getY());
						addAmendODialog.pack();
						addAmendODialog.setIcsGUI(icsGUI);
						addAmendODialog.setIcsGUIStatus(icsGUIStatus);
						addAmendODialog.add();
						break;
					case IcsGUIConfigProperties.CONFIG_TYPE_POLARIMETER_RINGO3:
						addAmendRingo3PolarimeterDialog.setLocation(getX()+getWidth(),getY());
						addAmendRingo3PolarimeterDialog.pack();
						addAmendRingo3PolarimeterDialog.add();
						break;
					case IcsGUIConfigProperties.CONFIG_TYPE_SPECTROGRAPH_SPRAT:
						addAmendSpratDialog.setLocation(getX()+getWidth(),getY());
						addAmendSpratDialog.pack();
						addAmendSpratDialog.add();
						break;
					case IcsGUIConfigProperties.CONFIG_TYPE_SPECTROGRAPH_LOTUS:
						addAmendLOTUSDialog.setLocation(getX()+getWidth(),getY());
						addAmendLOTUSDialog.pack();
						addAmendLOTUSDialog.add();
						break;
					case IcsGUIConfigProperties.CONFIG_TYPE_POLARIMETER_MOPTOP:
						addAmendMoptopPolarimeterDialog.setLocation(getX()+getWidth(),getY());
						addAmendMoptopPolarimeterDialog.pack();
						addAmendMoptopPolarimeterDialog.add();
						break;
					case IcsGUIConfigProperties.CONFIG_TYPE_SPECTROGRAPH_MES:
					default:
						JOptionPane.showMessageDialog((Component)null,
							(Object)("Add operation not supported yet"),
							" Operation failed ",JOptionPane.ERROR_MESSAGE);
						break;
				}// end switch
			}// end if
		}// end for
		if(commandString.equals("Amend"))
		{
			try
			{
				id = getSelectedConfigId();
				type = configProperties.getConfigType(id);
				switch(type)
				{
					case IcsGUIConfigProperties.CONFIG_TYPE_CCD_RATCAM:
						addAmendCCDDialog.setLocation(getX()+getWidth(),getY());
						addAmendCCDDialog.pack();
						addAmendCCDDialog.setIcsGUIStatus(icsGUIStatus);
						addAmendCCDDialog.amend(id);
						break;
					case IcsGUIConfigProperties.CONFIG_TYPE_SPECTROGRAPH_NUVIEW:
						addAmendNuViewDialog.setLocation(getX()+getWidth(),getY());
						addAmendNuViewDialog.pack();
						addAmendNuViewDialog.amend(id);
						break;
					case IcsGUIConfigProperties.CONFIG_TYPE_INFRA_RED_SUPIRCAM:
						addAmendIRCamDialog.setLocation(getX()+getWidth(),getY());
						addAmendIRCamDialog.pack();
						addAmendIRCamDialog.amend(id);
						break;
					case IcsGUIConfigProperties.CONFIG_TYPE_SPECTROGRAPH_FTSPEC:
						addAmendFTSpecDialog.setLocation(getX()+getWidth(),getY());
						addAmendFTSpecDialog.pack();
						addAmendFTSpecDialog.amend(id);
						break;
					case IcsGUIConfigProperties.CONFIG_TYPE_POLARIMETER_RINGOSTAR:
						addAmendPolarimeterDialog.setLocation(getX()+getWidth(),getY());
						addAmendPolarimeterDialog.pack();
						addAmendPolarimeterDialog.amend(id);
						break;
					case IcsGUIConfigProperties.CONFIG_TYPE_SPECTROGRAPH_FRODOSPEC:
						addAmendFrodoSpecDialog.setLocation(getX()+getWidth(),getY());
						addAmendFrodoSpecDialog.pack();
						addAmendFrodoSpecDialog.amend(id);
						break;
					case IcsGUIConfigProperties.CONFIG_TYPE_CCD_RISE:
						addAmendRISEDialog.setLocation(getX()+getWidth(),getY());
						addAmendRISEDialog.pack();
						addAmendRISEDialog.amend(id);
						break;
					case IcsGUIConfigProperties.CONFIG_TYPE_POLARIMETER_RINGO2:
						addAmendRingo2PolarimeterDialog.setLocation(getX()+getWidth(),getY());
						addAmendRingo2PolarimeterDialog.pack();
						addAmendRingo2PolarimeterDialog.amend(id);
						break;
					case IcsGUIConfigProperties.CONFIG_TYPE_CCD_THOR:
						addAmendTHORDialog.setLocation(getX()+getWidth(),getY());
						addAmendTHORDialog.pack();
						addAmendTHORDialog.amend(id);
						break;
					case IcsGUIConfigProperties.CONFIG_TYPE_CCD_O:
						addAmendODialog.setLocation(getX()+getWidth(),getY());
						addAmendODialog.pack();
						addAmendODialog.setIcsGUI(icsGUI);
						addAmendODialog.setIcsGUIStatus(icsGUIStatus);
						addAmendODialog.amend(id);
						break;
					case IcsGUIConfigProperties.CONFIG_TYPE_POLARIMETER_RINGO3:
						addAmendRingo3PolarimeterDialog.setLocation(getX()+getWidth(),getY());
						addAmendRingo3PolarimeterDialog.pack();
						addAmendRingo3PolarimeterDialog.amend(id);
						break;
					case IcsGUIConfigProperties.CONFIG_TYPE_SPECTROGRAPH_SPRAT:
						addAmendSpratDialog.setLocation(getX()+getWidth(),getY());
						addAmendSpratDialog.pack();
						addAmendSpratDialog.amend(id);
						break;
					case IcsGUIConfigProperties.CONFIG_TYPE_SPECTROGRAPH_LOTUS:
						addAmendLOTUSDialog.setLocation(getX()+getWidth(),getY());
						addAmendLOTUSDialog.pack();
						addAmendLOTUSDialog.amend(id);
						break;
					case IcsGUIConfigProperties.CONFIG_TYPE_POLARIMETER_MOPTOP:
						addAmendMoptopPolarimeterDialog.setLocation(getX()+getWidth(),getY());
						addAmendMoptopPolarimeterDialog.pack();
						addAmendMoptopPolarimeterDialog.amend(id);
						break;
					case IcsGUIConfigProperties.CONFIG_TYPE_SPECTROGRAPH_MES:
					default:
						JOptionPane.showMessageDialog((Component)null,
							(Object)("Amend type not supported"),
							" Operation failed ",JOptionPane.ERROR_MESSAGE);
						break;
				}// end switch
			}
			catch(IllegalArgumentException e)
			{
				JOptionPane.showMessageDialog((Component)null,
					(Object)("Getting selected configuration failed:"+e),
					" Operation failed ",JOptionPane.ERROR_MESSAGE);
			}
		}
		if(commandString.equals("Delete"))
		{
			try
			{
				id = getSelectedConfigId();
			}
			catch(IllegalArgumentException e)
			{
				JOptionPane.showMessageDialog((Component)null,
					(Object)("Getting selected configuration failed:"+e),
					" Operation failed ",JOptionPane.ERROR_MESSAGE);
				return;
			}
			retval = JOptionPane.showConfirmDialog((Component)null,
				(Object)("Are you sure you want to delete:"+configProperties.getConfigName(id)),
				" Delete "+configProperties.getConfigName(id),
				JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
			if(retval == JOptionPane.YES_OPTION)
			{
				configProperties.deleteId(id);
				setData();
			}
		}
	// check for filter sub-menu
		for(int i = 0; i < INSTRUMENT_STRING_ARRAY.length; i++)
		{
			if(commandString.equals(FILTER_STRING+INSTRUMENT_STRING_ARRAY[i]))
			{
			// This assumes INSTRUMENT_STRING_ARRAY[i] is the string description for the instrument
			// IcsGUIConfigProperties.CONFIG_TYPE_LIST[i].
				filterTypeNumber = IcsGUIConfigProperties.CONFIG_TYPE_LIST[i];
				setData();
			}// end if
		}// end for
		if(commandString.equals(FILTER_STRING+NONE_STRING))
		{
			filterTypeNumber = NONE_FILTER_NUMBER;
			setData();
		}
		if(commandString.equals("Quit"))
		{
			SwingUtilities.invokeLater(new GUIDialogUnmanager(this));
		}
	}

	/**
	 * Method implementing the IcsConfigAADialogListener interface. Called when an
	 * Instrument Configuration is added/amended. Calls setData, to reset the list.
	 * @param ok Boolean, true if the Ok button was pressed on the dialog.
	 * @param id The id of the Instrument Configuration if ok was true.
	 * @see #setData
	 */
	public void actionPerformed(boolean ok,int id)
	{
		setData();
	}

	/**
	 * Method to get the id of the selected Instrument Configuration in the list.
	 * @return The id.
	 * @exception IllegalArgumentException Thrown if no item is selected, the selected item is not a string,
	 * 	or a configuration cannot be found with the correct name.
	 */
	private int getSelectedConfigId() throws IllegalArgumentException
	{
		String s = null;
		Object o = null;

		// get the selected string.
		o = list.getSelectedValue();
		if(o == null)
			throw new IllegalArgumentException("No Item Selected");
		if((o instanceof String)==false)
			throw new IllegalArgumentException("Selected Item wrong class:"+o.getClass().getName());
		s = (String)o;
		return configProperties.getIdFromName(s);
	}

	/**
	 * Internal method to create the file menu and add it to the menu-bar.
	 * @param menuBar The menu-bar to add it to.
	 * @see #constructAddSubMenu
	 */
	private void constructFileMenu(JMenuBar menuBar)
	{
		JMenu menu = null;
		JMenuItem menuItem = null;

		menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		menu.getAccessibleContext().setAccessibleDescription("The File Menu");
		menuBar.add(menu);
	// select
		menuItem = new JMenuItem("Select",KeyEvent.VK_S);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Selects the item");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		menu.addSeparator();
	// add
		constructAddSubMenu(menu);
	// amend
		menuItem = new JMenuItem("Amend",KeyEvent.VK_M);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Amends a configuration");
		menuItem.addActionListener(this);
		menu.add(menuItem);
	// delete
		menuItem = new JMenuItem("Delete",KeyEvent.VK_D);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Delete a configuration");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		menu.addSeparator();
	// quit
		menuItem = new JMenuItem("Quit",KeyEvent.VK_Q);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Quits the list");
		menuItem.addActionListener(this);
		menu.add(menuItem);
	}

	/**
	 * Internal method to create the file menu and add it to the menu-bar.
	 * @param parentMenu The parent menu to add it to.
	 * @see #INSTRUMENT_STRING_ARRAY
	 * @see #ADD_STRING
	 * @see #icsGUI
	 */
	private void constructAddSubMenu(JMenu parentMenu)
	{
		JMenu menu = null;
		JMenuItem menuItem = null;
		boolean showFilter;

		menu = new JMenu("Add");
		menu.setMnemonic(KeyEvent.VK_A);
		menu.getAccessibleContext().setAccessibleDescription("Add Configuration Menu");
		parentMenu.add(menu);
		for(int i=0;i < INSTRUMENT_STRING_ARRAY.length;i++)
		{
			try
			{
				showFilter = icsGUIStatus.getPropertyBoolean("ics_gui.config.filter."+i);
			}
			catch(Exception e)
			{
				showFilter = true;
				icsGUI.error(this.getClass().getName()+":constructAddSubMenu:"+
						   "Show Filter configuration for filter "+i+" not found.");
			}
			if(showFilter)
			{
				menuItem = new JMenuItem(ADD_STRING+INSTRUMENT_STRING_ARRAY[i]);
//diddly			menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,ActionEvent.CTRL_MASK));
				menuItem.getAccessibleContext().setAccessibleDescription("Adds a "+
							       INSTRUMENT_STRING_ARRAY[i]+" configuration");
				menuItem.addActionListener(this);
				menu.add(menuItem);
			}//end if showFilter
		}// end for
	}

	/**
	 * Internal method to create the filter menu and add it to the menu-bar.
	 * @param menuBar The menu-bar to add it to.
	 * @see #INSTRUMENT_STRING_ARRAY
	 * @see #icsGUI
	 */
	private void constructFilterMenu(JMenuBar menuBar)
	{
		JMenu menu = null;
		ButtonGroup filterButtonGroup = null;
		JRadioButtonMenuItem radioButton = null;
		int defaultFilterNumber;
		boolean showFilter;

		menu = new JMenu("Filter");
		menu.setMnemonic(KeyEvent.VK_L);
		menu.getAccessibleContext().setAccessibleDescription("The Filter Menu");
		menuBar.add(menu);
	// get default filter configuration from properties
		try
		{
			defaultFilterNumber = icsGUIStatus.getPropertyInteger("ics_gui.config.filter.default");
		}
		catch(Exception e)
		{
			defaultFilterNumber = -1;
			icsGUI.error(this.getClass().getName()+":constructFilterMenu:"+
					   "Default filter number configuration not found.");
		}
	// set Filter type number to the default picked up from the config file.
	// filterTypeNumber is used by setData to filter the list.
		filterTypeNumber = defaultFilterNumber;
	// button group
		filterButtonGroup = new ButtonGroup();
	// none
		radioButton = new JRadioButtonMenuItem(FILTER_STRING+NONE_STRING,(defaultFilterNumber == -1));
		radioButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,ActionEvent.CTRL_MASK));
		radioButton.getAccessibleContext().setAccessibleDescription("Removes all filters");
		radioButton.addActionListener(this);
		filterButtonGroup.add(radioButton);
		menu.add(radioButton);
		menu.addSeparator();
	// instrument buttons
		for(int i=0;i < INSTRUMENT_STRING_ARRAY.length;i++)
		{
			try
			{
				showFilter = icsGUIStatus.getPropertyBoolean("ics_gui.config.filter."+i);
			}
			catch(Exception e)
			{
				showFilter = true;
				icsGUI.error(this.getClass().getName()+":constructFilterMenu:"+
						   "Show Filter configuration for filter "+i+" not found.");
			}
			if(showFilter)
			{
				radioButton = new JRadioButtonMenuItem(FILTER_STRING+INSTRUMENT_STRING_ARRAY[i],
							       (defaultFilterNumber == i));
//diddly		       radioButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,ActionEvent.CTRL_MASK));
				radioButton.getAccessibleContext().
					setAccessibleDescription("Filter by "+INSTRUMENT_STRING_ARRAY[i]);
				radioButton.addActionListener(this);
				filterButtonGroup.add(radioButton);
				menu.add(radioButton);
			}// end if showFilter
		}// end for
	}
}
//
// $Log: not supported by cvs2svn $
// Revision 0.20  2015/06/09 13:13:39  cjm
// Added LOTUS Config Add/Amend.
//
// Revision 0.19  2014/04/04 11:17:39  cjm
// Added Sprat Config Add/Amend.
//
// Revision 0.18  2013/05/01 12:26:42  cjm
// SUPIRCAM button now also for IO:I.
//
// Revision 0.17  2012/03/16 12:19:47  cjm
// Added Ringo3 support.
//
// Revision 0.16  2011/11/07 17:07:34  cjm
// Added support for config's for IO:O.
//
// Revision 0.15  2010/10/07 13:24:06  cjm
// Added THOR accessors.
//
// Revision 0.14  2009/11/24 14:17:10  cjm
// Added Ringo2 support.
//
// Revision 0.13  2008/01/15 11:17:28  cjm
// Changed Nuview to Meaburn.
//
// Revision 0.12  2007/12/11 17:40:40  cjm
// Added RISE config handling.
//
// Revision 0.11  2006/05/16 17:12:23  cjm
// gnuify: Added GNU General Public License.
//
// Revision 0.10  2005/11/29 16:31:31  cjm
// Added Polarimeter (Ringo Star) support.
//
// Revision 0.9  2004/01/15 15:53:19  cjm
// Commented out remoteX fix that won't compile on Solaris Java 1.2.
//
// Revision 0.8  2004/01/13 20:34:37  cjm
// Added remote X option.
//
// Revision 0.7  2003/11/14 15:02:12  cjm
// Added FTSpec / FixedFormatSpecConfig code.
// Also added options from config file to show only relevent config filters and
// add buttons, and to choose a default config filter to filter by.
//
// Revision 0.6  2003/08/22 14:05:18  cjm
// Added reference to IcsGUIStatus to alow CONFIGDialog (and its subdialogs)
// access to properties.
//
// Revision 0.5  2003/08/21 14:29:04  cjm
// Changed CcsCCDConfigAADialog to IcsGUICCDConfigAADialog.
//
// Revision 0.4  2003/07/15 16:20:01  cjm
// Added IRCam property configuration.
//
// Revision 0.3  2001/07/10 18:21:28  cjm
// Added ability to Add/Amend/Delete Nu-View and MES configurations.
//
// Revision 0.2  2000/11/30 18:47:44  cjm
// Made generic for other instruments.
//
// Revision 0.1  2000/11/29 11:30:25  cjm
// initial revision.
//
// Revision 0.3  2000/11/29 11:29:26  cjm
// Made more generic, for any kind of instrument.
//
// Revision 0.2  1999/12/09 17:02:12  cjm
// More functionality added.
//
// Revision 0.1  1999/12/08 10:41:12  cjm
// initial revision.
//
//
