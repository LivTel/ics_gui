// CcsGUIMenuItemListener.java -*- mode: Fundamental;-*-
// $Header: /home/cjm/cvs/ics_gui/java/IcsGUIMenuItemListener.java,v 0.2 1999-12-09 17:02:12 cjm Exp $
import java.lang.*;
import java.lang.reflect.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import ngat.util.ThreadMonitorFrame;

/**
 * This class is an ActionListener for the CcsGUI menu bar items.
 */
public class CcsGUIMenuItemListener implements ActionListener
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: IcsGUIMenuItemListener.java,v 0.2 1999-12-09 17:02:12 cjm Exp $");
	/**
	 * The parent to the menu item listener. The instance of the main program.
	 */
	private CcsGUI parent = null;
	/**
	 * Listener for the dialogs.
	 */
	private GUIDialogListener dialogListener = null;
	/**
	 * Hashtable used to map Menu Item Selections to Dialog classes.
	 */
	private Hashtable dialogClassNameList = null;
	/**
	 * Identifier number. Each command sent to the CCS must have a unique id, this
	 * number is incremented each time a dialog is brought up so a unique id is created.
	 */
	private int uniqueCommandId = 0;

	/**
	 * Constructor. This sets the parent to be main program class.
	 * This also creates the dialogClassNameList hashtable.
	 * It resets the uniqueCommandId to zero.
	 * It constructs the dialog listener.
	 * @param p The parent object which owns the menu items.
	 * @see #dialogClassNameList
	 * @see #parent
	 * @see #uniqueCommandId
	 * @see #dialogListener
	 */
	public CcsGUIMenuItemListener(CcsGUI p)
	{
		super();

		parent = p;
		dialogClassNameList = new Hashtable();
		uniqueCommandId = 0;
		dialogListener = new CcsGUIDialogListener(p);
	}

	/**
	 * This fills the dialogClassNameList hashtable with
	 * a mapping from menu item names to CcsCommandDialogs. The dialogs are created using the
	 * createDialog method. The dialogListener is added as a dialog listener.
	 * @see #dialogListener
	 * @see #dialogClassNameList
	 * @see #createDialog
	 */
	public void createMapping()
	{
		String mappingList[][] = 
		{
			{"Acquire","ACQUIREDialog"},
			{"Arc","ARCDialog"},
			{"Bias","BIASDialog"},
			{"Dark","DARKDialog"},
			{"Lamp Flat","LAMPFLATDialog"},
			{"Sky Flat","SKYFLATDialog"},
			{"Glance","GLANCEDialog"},
			{"Movie","MOVIEDialog"},
			{"Multrun","MULTRUNDialog"},
			{"Runat","RUNATDialog"},
			{"Save","SAVEDialog"},
			{"Abort","ABORTDialog"},
			{"Get Status","GET_STATUSDialog"},
			{"Pause","PAUSEDialog"},
			{"Reboot","REBOOTDialog"},
			{"Resume","RESUMEDialog"},
			{"Stop","STOPDialog"},
			{"Config","CONFIGDialog"},
			{"Lamp Focus","LAMPFOCUSDialog"},
			{"Set Logging","SET_LOGGINGDialog"},
			{"Star Focus","STARFOCUSDialog"},
			{"Telescope Focus","TELFOCUSDialog"},
			{"Test","TESTDialog"},
		};
		CcsCommandDialog dialog = null;
		String menuName = null;
		String dialogClassName = null;
		int i;

		for(i=0;i<mappingList.length;i++)
		{
			menuName = mappingList[i][0];
			dialogClassName = mappingList[i][1];
			try
			{
				dialog = createDialog(dialogClassName);
			}
			catch(Exception e)// from createDialog
			{
				parent.error("Failed to create dialog class "+dialogClassName+" for menu:"+
					menuName+" : "+e);
				dialog = null;
			}
			if(dialog != null)
			{
				dialog.addGUIDialogListener(dialogListener);
				if(dialog instanceof CONFIGDialog)
				{
					CONFIGDialog configDialog = (CONFIGDialog)dialog;
					CcsGUIConfigButtonListener configButtonListener = null;

					configButtonListener = new CcsGUIConfigButtonListener(parent,configDialog);
					configDialog.addConfigButtonActionListener(configButtonListener);
					configDialog.setConfigProperties(parent.getStatus().getCCDConfigProperties());
				}
				dialogClassNameList.put(menuName,dialog);
			}
		}// end for on list
	}

	/**
	 * Routine that is called when a menu item is pressed.
	 * If Exit is called, the program is terminated.
	 * Otherwise we try to find a mapping from the menu item to it's dialog class,
	 * using dialogClassNameList. The dialog is set visible.
	 * @param event The event that caused this call to the listener.
	 * @see #dialogClassNameList
	 */
	public void actionPerformed(ActionEvent event)
	{
		String commandString = event.getActionCommand();
		CcsCommandDialog dialog = null;

		if(commandString.equals("Exit"))
		{
			parent.exit(0);
		}
		if(commandString.equals("Clear"))
		{
			parent.clearLog();
			parent.log("Log cleared.");
			return;
		}
		if(commandString.equals("Thread Monitor"))
		{
			ThreadMonitorFrame threadMonitorFrame = new ThreadMonitorFrame("Ccs GUI");
			threadMonitorFrame.getThreadMonitor().setUpdateTime(1000);
			threadMonitorFrame.pack();
			threadMonitorFrame.setVisible(true);
			parent.log("Thread Monitor started.");
			return;
		}
		if(commandString.equals("Spoof Requests"))
		{
			JCheckBoxMenuItem cbmi = (JCheckBoxMenuItem)event.getSource();

			if(cbmi.getState())
				parent.startISSServer();
			else
				parent.stopISSServer();
			return;
		}
		if(commandString.equals("Message Dialog"))
		{
			JCheckBoxMenuItem cbmi = (JCheckBoxMenuItem)event.getSource();

			if(cbmi.getState())
			{
				parent.setISSMessageDialog(true);
				parent.log("Message Dialogs will be brought up in response to ISS commands.");
			}
			else
			{
				parent.setISSMessageDialog(false);
				parent.log("Message Dialogs will NOT be brought up in response to ISS commands.");
			}
			return;
		}
	// get the dialog class name
		dialog = (CcsCommandDialog)dialogClassNameList.get(commandString);
		if(dialog == null)
		{
			parent.error("Failed to find mapping to dialog for menu:"+commandString);
			return;
		}
		dialog.setID(parent.getClass().getName()+":"+uniqueCommandId);
		uniqueCommandId++;
		dialog.setLocation(getDialogX(dialog),getDialogY(dialog));
		dialog.pack();
		dialog.setVisible(true);
	}

	/**
	 * Get the position the dialog should be placed at in the X direction.
	 * This method tries to place the dialog in the centre of the parent frame.
	 * @param d The dialog we are positioning. We read the width of this dialog.
	 * @return An X location.
	 */
	private int getDialogX(JDialog d)
	{
		int widthDifference;

		widthDifference = parent.getFrame().getWidth()-d.getWidth();
		if(widthDifference < 0)
			return 0;
		return parent.getFrame().getX()+(widthDifference/2);
	}

	/**
	 * Get the position the dialog should be placed at in the Y direction.
	 * This method tries to place the dialog in the centre of the parent frame.
	 * @param d The dialog we are positioning. We read the height of this dialog.
	 * @return An Y location.
	 */
	private int getDialogY(JDialog d)
	{
		int heightDifference;

		heightDifference = parent.getFrame().getHeight()-d.getHeight();
		if(heightDifference < 0)
			return 0;
		return parent.getFrame().getY()+(heightDifference/2);
	}

	/**
	 * Method that creates an instance of a dialog from it's class name.
	 * <ul>
	 * <li>It finds the Class object instance for this class.
	 * <li>It finds a constructor with java.awt.Frame as it's parameter.
	 * <li>It invokes the constructor with parent.getFrame().
	 * <li>The dialog instance is returned.
	 * </ul>
	 * @see #parent
	 */
	private final CcsCommandDialog createDialog(String dialogClassName) throws ClassNotFoundException, 
		NoSuchMethodException, SecurityException, InstantiationException, 
		IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		Class dialogClass = null;
		Class parameterTypeArray[] = new Class[1];
		Object parameterArray[] = new Object[1];
		Constructor constructor = null;
		CcsCommandDialog dialog = null;

	// get the dialog Class object instance
		try
		{
			dialogClass = Class.forName(dialogClassName);
		}
		catch(ClassNotFoundException e)
		{
			parent.error("Failed to find dialog class "+dialogClassName+" : "+e);
			throw e;
		}
	// get the dialog parameter type Class instance
		try
		{
			parameterTypeArray[0] = Class.forName("java.awt.Frame");
		}
		catch(ClassNotFoundException e)
		{
			parent.error("Failed to find dialog constructor class java.awt.Frame : "+e);
			throw e;
		}
	// get the dialogs constructor
		try
		{
			constructor = dialogClass.getConstructor(parameterTypeArray);
		}
		catch(NoSuchMethodException e)
		{
			parent.error("Failed to find dialog constructor class java.awt.Frame : "+e);
			throw e;
		}
		catch(SecurityException e)
		{
			parent.error("Failed to find dialog constructor class java.awt.Frame : "+e);
			throw e;
		}
	// construct the dialog. Pass the parent frame parent as the parent.
		parameterArray[0] = (Frame)(parent.getFrame());
		try
		{
			dialog = (CcsCommandDialog)constructor.newInstance(parameterArray);
		}
		catch(InstantiationException e)
		{
			parent.error("Failed to create instance of dialog class "+dialogClassName+" : "+e);
			throw e;
		}
		catch(IllegalAccessException e)
		{
			parent.error("Failed to create instance of dialog class "+dialogClassName+" : "+e);
			throw e;
		}
		catch(IllegalArgumentException e)
		{
			parent.error("Failed to create instance of dialog class "+dialogClassName+" : "+e);
			throw e;
		}
		catch(InvocationTargetException e)
		{
			parent.error("Failed to create instance of dialog class "+dialogClassName+" : "+e);
			throw e;
		}
		return dialog;
	}
}
//
// $Log: not supported by cvs2svn $
// Revision 0.1  1999/11/22 09:53:49  cjm
// initial revision.
//
//
