// CcsGUIUpdateListener.java
// $Header: /home/cjm/cvs/ics_gui/java/IcsGUIUpdateListener.java,v 0.2 2003-09-19 14:08:45 cjm Exp $
import java.lang.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * This class is an ActionListener for the IcsGUI auto-update checkbox. It starts a thread
 * to call the Ccs with a GET_STATUS command when the check-box is checked. It stops the thread
 * when the checkbox in un-checked.
 * @author Chris Mottram
 * @version $Revision: 0.2 $
 */
public class CcsGUIUpdateListener implements ActionListener
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: IcsGUIUpdateListener.java,v 0.2 2003-09-19 14:08:45 cjm Exp $");
	/**
	 * The instance of the main program.
	 */
	private IcsGUI parent = null;
	/**
	 * Thread that calls IcsGUI.sendCommand(GET_STATUS) every time status needs to be updated.
	 */
	private CcsGUIUpdateThread updateThread = null;

	/**
	 * Constructor. This sets the parent to be main program class.
	 * @param p The parent object which owns the menu items.
	 * @see #parent
	 */
	public CcsGUIUpdateListener(IcsGUI p)
	{
		super();

		parent = p;
	}


	/**
	 * Routine that is called when the auto-update check-box changes state.
	 * If the new state is set, it tries to get a valid autoUpdateTime from the parents
	 * text field. If the time is valid, a new CcsGUIUpdateThread is started with that time.
	 * If the time is invalid, the checkbox is set back to un-selected.
	 * If the new state is unset, the previously created thread is stopped and reset to null.
	 * @param event The event that caused this call to the listener.
	 * @see IcsGUI#getAutoUpdateTime
	 * @see CcsGUIUpdateThread
	 * @see CcsGUIUpdateThread#quit
	 */
	public void actionPerformed(ActionEvent event)
	{
		JCheckBox cb = (JCheckBox)event.getSource();
		long autoUpdateTime = 0;

		if(cb.isSelected())
		{
			autoUpdateTime = parent.getAutoUpdateTime();
			if(autoUpdateTime > 0)
			{
				updateThread = new CcsGUIUpdateThread(parent,autoUpdateTime);
				updateThread.start();
				parent.setAutoUpdateTimeEnabled(false);
			}
			else
			{
				cb.setSelected(false);
				updateThread = null;
				parent.setAutoUpdateTimeEnabled(true);
			}
		}
		else
		{
			if(updateThread != null)
				updateThread.quit();
			updateThread = null;
			parent.setAutoUpdateTimeEnabled(true);
		}
	}
}
//
// $Log: not supported by cvs2svn $
// Revision 0.1  1999/12/08 10:42:37  cjm
// initial revision.
//
//
