// IcsGUIConfigListDialog.java -*- mode: Fundamental;-*-
// $Header: /home/cjm/cvs/ics_gui/java/IcsGUIConfigListDialog.java,v 0.3 2001-07-10 18:21:28 cjm Exp $
import java.lang.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import ngat.swing.*;

/**
 * This class provides a list of configurations for the Ccs. 
 */
public class IcsGUIConfigListDialog extends JDialog implements ActionListener, CcsConfigAADialogListener
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public static String RCSID = new String("$Id: IcsGUIConfigListDialog.java,v 0.3 2001-07-10 18:21:28 cjm Exp $");
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
	protected final static String SPECTROGRAPH_NUVIEW_BUTTON_STRING = "Spectrograph (NuView)";
	/**
	 * String to go on buttons.
	 */
	protected final static String INFRA_RED_SUPIRCAM_BUTTON_STRING = "Infra Red Camera (SupIRCam)";
	/**
	 * List of strings that describe instruments. Note, make sure in the same order as 
	 * IcsGUIConfigProperties.CONFIG_TYPE_LIST.
	 * @see IcsGUIConfigProperties#CONFIG_TYPE_LIST
	 * @see #CCD_RATCAM_BUTTON_STRING
	 * @see #SPECTROGRAPH_MES_BUTTON_STRING
	 * @see #SPECTROGRAPH_NUVIEW_BUTTON_STRING
	 * @see #INFRA_RED_SUPIRCAM_BUTTON_STRING
	 */
	protected final static String INSTRUMENT_STRING_ARRAY[] = {CCD_RATCAM_BUTTON_STRING,
		SPECTROGRAPH_MES_BUTTON_STRING,SPECTROGRAPH_NUVIEW_BUTTON_STRING,INFRA_RED_SUPIRCAM_BUTTON_STRING};
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
	private CcsCCDConfigAADialog addAmendCCDDialog = null;
	/**
	 * The Add/Amend dialog to use when Add or Amend is selected for a NuView configuration.
	 */
	private IcsGUILowResSpecConfigAADialog addAmendNuViewDialog = null;
	/**
	 * The config dialog that caused this dialog to be managed.
	 */
	private CONFIGDialog configDialog = null;
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
	 * Creates the add/amend dialog.
	 * @param owner The parent frame of this dialog. Used in calling Dialog's constructor.
	 * @param c A reference to the object holding the instrument Configuration data. Used to fill the list,
	 *	and for updating purposes.
	 * @see #constructFileMenu
	 * @see #constructFilterMenu
	 */
	public IcsGUIConfigListDialog(Frame owner,IcsGUIConfigProperties c)
	{
		super(owner,"Instrument Configuration List");

		JScrollPane scrollPane = null;
		JMenu menu = null;
		JMenuItem menuItem = null;

		configProperties = c;
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
		panel.add(scrollPane);
	// create a CCD add/amend dialog
		addAmendCCDDialog = new CcsCCDConfigAADialog(owner,c);
		addAmendCCDDialog.addCcsConfigAADialogListener(this);
	// create a NuView add/amend dialog
		addAmendNuViewDialog = new IcsGUILowResSpecConfigAADialog(owner,c);
		addAmendNuViewDialog.addCcsConfigAADialogListener(this);
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
						addAmendCCDDialog.add();
						break;
					case IcsGUIConfigProperties.CONFIG_TYPE_SPECTROGRAPH_NUVIEW:
						addAmendNuViewDialog.setLocation(getX()+getWidth(),getY());
						addAmendNuViewDialog.pack();
						addAmendNuViewDialog.add();
						break;
					case IcsGUIConfigProperties.CONFIG_TYPE_SPECTROGRAPH_MES:
					case IcsGUIConfigProperties.CONFIG_TYPE_INFRA_RED_SUPIRCAM:
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
						addAmendCCDDialog.amend(id);
						break;
					case IcsGUIConfigProperties.CONFIG_TYPE_SPECTROGRAPH_NUVIEW:
						addAmendNuViewDialog.setLocation(getX()+getWidth(),getY());
						addAmendNuViewDialog.pack();
						addAmendNuViewDialog.amend(id);
						break;
					case IcsGUIConfigProperties.CONFIG_TYPE_SPECTROGRAPH_MES:
					case IcsGUIConfigProperties.CONFIG_TYPE_INFRA_RED_SUPIRCAM:
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
	 * Method implementing the CcsConfigAADialogListener interface. Called when an
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
	 */
	private void constructAddSubMenu(JMenu parentMenu)
	{
		JMenu menu = null;
		JMenuItem menuItem = null;

		menu = new JMenu("Add");
		menu.setMnemonic(KeyEvent.VK_A);
		menu.getAccessibleContext().setAccessibleDescription("Add Configuration Menu");
		parentMenu.add(menu);
		for(int i=0;i < INSTRUMENT_STRING_ARRAY.length;i++)
		{
			menuItem = new JMenuItem(ADD_STRING+INSTRUMENT_STRING_ARRAY[i]);
//diddly			menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,ActionEvent.CTRL_MASK));
			menuItem.getAccessibleContext().setAccessibleDescription("Adds a "+
				INSTRUMENT_STRING_ARRAY[i]+" configuration");
			menuItem.addActionListener(this);
			menu.add(menuItem);
		}
	}

	/**
	 * Internal method to create the filter menu and add it to the menu-bar.
	 * @param menuBar The menu-bar to add it to.
	 * @see #INSTRUMENT_STRING_ARRAY
	 */
	private void constructFilterMenu(JMenuBar menuBar)
	{
		JMenu menu = null;
		ButtonGroup filterButtonGroup = null;
		JRadioButtonMenuItem radioButton = null;

		menu = new JMenu("Filter");
		menu.setMnemonic(KeyEvent.VK_L);
		menu.getAccessibleContext().setAccessibleDescription("The Filter Menu");
		menuBar.add(menu);
	// button group
		filterButtonGroup = new ButtonGroup();
	// none
		radioButton = new JRadioButtonMenuItem(FILTER_STRING+NONE_STRING,true);
		radioButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,ActionEvent.CTRL_MASK));
		radioButton.getAccessibleContext().setAccessibleDescription("Removes all filters");
		radioButton.addActionListener(this);
		filterButtonGroup.add(radioButton);
		menu.add(radioButton);
		menu.addSeparator();
	// instrument buttons
		for(int i=0;i < INSTRUMENT_STRING_ARRAY.length;i++)
		{
			radioButton = new JRadioButtonMenuItem(FILTER_STRING+INSTRUMENT_STRING_ARRAY[i]);
//diddly		radioButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,ActionEvent.CTRL_MASK));
			radioButton.getAccessibleContext().
				setAccessibleDescription("Filter by "+INSTRUMENT_STRING_ARRAY[i]);
			radioButton.addActionListener(this);
			filterButtonGroup.add(radioButton);
			menu.add(radioButton);
		}
	}
}
//
// $Log: not supported by cvs2svn $
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
