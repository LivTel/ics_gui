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
// IcsGUIUpdateListener.java
// $Header: /home/cjm/cvs/ics_gui/java/IcsGUIUpdateListener.java,v 0.4 2020-05-05 10:20:39 cjm Exp $
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
 * @version $Revision: f231dd9c240de86d28b3806ec501eb0afa20e696 $
 */
public class IcsGUIUpdateListener implements ActionListener
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: IcsGUIUpdateListener.java | Tue May 5 10:20:44 2020 +0000 | Chris Mottram  $");
	/**
	 * The instance of the main program.
	 */
	private IcsGUI parent = null;
	/**
	 * Thread that calls IcsGUI.sendCommand(GET_STATUS) every time status needs to be updated.
	 */
	private IcsGUIUpdateThread updateThread = null;

	/**
	 * Constructor. This sets the parent to be main program class.
	 * @param p The parent object which owns the menu items.
	 * @see #parent
	 */
	public IcsGUIUpdateListener(IcsGUI p)
	{
		super();

		parent = p;
	}


	/**
	 * Routine that is called when the auto-update check-box changes state.
	 * If the new state is set, it tries to get a valid autoUpdateTime from the parents
	 * text field. If the time is valid, a new IcsGUIUpdateThread is started with that time.
	 * If the time is invalid, the checkbox is set back to un-selected.
	 * If the new state is unset, the previously created thread is stopped and reset to null.
	 * @param event The event that caused this call to the listener.
	 * @see IcsGUI#getAutoUpdateTime
	 * @see IcsGUIUpdateThread
	 * @see IcsGUIUpdateThread#quit
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
				updateThread = new IcsGUIUpdateThread(parent,autoUpdateTime);
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
// Revision 0.3  2006/05/16 17:12:18  cjm
// gnuify: Added GNU General Public License.
//
// Revision 0.2  2003/09/19 14:08:45  cjm
// Changed CcsGUI to IcsGUI.
//
// Revision 0.1  1999/12/08 10:42:37  cjm
// initial revision.
//
//
