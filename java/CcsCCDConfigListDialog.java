// CcsCCDConfigListDialog.java -*- mode: Fundamental;-*-
// $Header: /home/cjm/cvs/ics_gui/java/CcsCCDConfigListDialog.java,v 0.3 2000-11-29 11:29:26 cjm Exp $
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
public class CcsCCDConfigListDialog extends JDialog implements ActionListener, CcsConfigAADialogListener
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: CcsCCDConfigListDialog.java,v 0.3 2000-11-29 11:29:26 cjm Exp $");
	/**
	 * The data to be displayed in the list.
	 */
	private IcsGUIConfigProperties configProperties = null;
	/**
	 * The Add/Amend dialog to use when Add or Amend is selected.
	 */
	private CcsCCDConfigAADialog addAmendDialog = null;
	/**
	 * The config dialog that caused this dialog to be managed.
	 */
	private CONFIGDialog configDialog = null;
	/**
	 * Internal JList that actually displays the Instrument Configurations.
	 */
	private JList list = null;

	/**
	 * Constructor. Calls the JDialog constructor. Creates the components in the dialog.
	 * Creates the add/amend dialog.
	 * @param owner The parent frame of this dialog. Used in calling Dialog's constructor.
	 * @param c A reference to the object holding the instrument Configuration data. Used to fill the list,
	 *	and for updating purposes.
	 */
	public CcsCCDConfigListDialog(Frame owner,IcsGUIConfigProperties c)
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
		menuItem = new JMenuItem("Add",KeyEvent.VK_A);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Adds a configuration");
		menuItem.addActionListener(this);
		menu.add(menuItem);
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
	// create an add/amend dialog
		addAmendDialog = new CcsCCDConfigAADialog(owner,c);
		addAmendDialog.addCcsConfigAADialogListener(this);
	}

	/**
	 * This method sets the list up with data in the Instrument Config Prooperties.
	 * This method should be called <b>just</b> before the dialog is managed, to ensure the
	 * list is up to date with the properties.
	 * @see #configProperties
	 * @see IcsGUIConfigProperties#getConfigNameList
	 * @see #list
	 */
	public void setData()
	{
		Vector l = null;
		int i;

		l = configProperties.getConfigNameList();
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
	 */
	public void actionPerformed(ActionEvent event)
	{
		String commandString = event.getActionCommand();
		String s = null;
		Object o = null;
		int id = 0;
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
		if(commandString.equals("Add"))
		{
			addAmendDialog.setLocation(getX()+getWidth(),getY());
			addAmendDialog.pack();
			addAmendDialog.add();
		}
		if(commandString.equals("Amend"))
		{
			try
			{
				id = getSelectedConfigId();
				addAmendDialog.setLocation(getX()+getWidth(),getY());
				addAmendDialog.pack();
				addAmendDialog.amend(id);
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
		if(commandString.equals("Quit"))
		{
			SwingUtilities.invokeLater(new GUIDialogUnmanager(this));
		}
	}

	/**
	 * Method implementing the CcsConfigAADialogListener interface. Called when an
	 * Instrument Configuration is added/amended.
	 * @param ok Boolean, true if the Ok button was pressed on the dialog.
	 * @param id The id of the Instrument Configuration if ok was true.
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
}
//
// $Log: not supported by cvs2svn $
// Revision 0.2  1999/12/09 17:02:12  cjm
// More functionality added.
//
// Revision 0.1  1999/12/08 10:41:12  cjm
// initial revision.
//
//
