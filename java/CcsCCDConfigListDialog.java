// CcsCCDConfigListDialog.java -*- mode: Fundamental;-*-
// $Header: /home/cjm/cvs/ics_gui/java/CcsCCDConfigListDialog.java,v 0.1 1999-12-08 10:41:12 cjm Exp $
import java.lang.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import ngat.swing.*;

/**
 * This class provides a list 
 */
public class CcsCCDConfigListDialog extends JDialog implements ActionListener
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: CcsCCDConfigListDialog.java,v 0.1 1999-12-08 10:41:12 cjm Exp $");
	/**
	 * The data to be displayed in the list.
	 */
	CcsCCDConfigProperties ccdConfigProperties = null;
	/**
	 * The config dialog that caused this dialog to be managed.
	 */
	CONFIGDialog configDialog = null;
	/**
	 * Internal JList that actually displays the CCD Configurations.
	 */
	private JList list = null;

	/**
	 * Constructor.
	 * @param owner The parent frame of this dialog. Used in calling Dialog's constructor.
	 */
	public CcsCCDConfigListDialog(Frame owner,CcsCCDConfigProperties c)
	{
		super(owner,"CCD Configuration List");

		JScrollPane scrollPane = null;
		JMenu menu = null;
		JMenuItem menuItem = null;

		ccdConfigProperties = c;
		getContentPane().setLayout(new BorderLayout());
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
		panel.setPreferredSize(new Dimension(250,500));
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
	}

	/**
	 * This method sets the list up with data in the CCD Config Prooperties.
	 * This method should be called <b>just</b> before the dialog is managed, to ensure the
	 * list is up to date with the properties.
	 * @see #ccdConfigProperties
	 * @see CcsCCDConfigProperties#getConfigNameList
	 * @see #list
	 */
	public void setData()
	{
		Vector l = null;
		int i;

		l = ccdConfigProperties.getConfigNameList();
		list.setListData(l);
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
					SwingUtilities.invokeLater(new GUIDialogUnmanager(this));
				}
			}
		}
		if(commandString.equals("Add"))
		{
			JOptionPane.showMessageDialog((Component)null,(Object)("This menu is not implemented yet."),
				" Menu not Implmeneted ",JOptionPane.WARNING_MESSAGE);
		}
		if(commandString.equals("Amend"))
		{
			JOptionPane.showMessageDialog((Component)null,(Object)("This menu is not implemented yet."),
				" Menu not Implmeneted ",JOptionPane.WARNING_MESSAGE);
		}
		if(commandString.equals("Delete"))
		{
			JOptionPane.showMessageDialog((Component)null,(Object)("This menu is not implemented yet."),
				" Menu not Implmeneted ",JOptionPane.WARNING_MESSAGE);
		}
		if(commandString.equals("Quit"))
		{
			SwingUtilities.invokeLater(new GUIDialogUnmanager(this));
		}
	}
}
//
// $Log: not supported by cvs2svn $
//
