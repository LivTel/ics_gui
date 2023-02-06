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
// IcsGUIUpdateThread.java
// $Header: /home/cjm/cvs/ics_gui/java/IcsGUIUpdateThread.java,v 0.9 2020-05-05 10:20:39 cjm Exp $

import java.lang.*;
/**
 * The IcsGUIUpdateThread extends Thread.
 * The thread sleeps for a length determined by the auto update time.
 * It then calls parent.sendCommand(GET_STATUS) to update the GUI's status.
 * This continues until the thread is quit.
 * @author Chris Mottram
 * @version $Revision: bb61f5eb6a1f7f939b3f6711be33a17b8efeb8a9 $
 */
public class IcsGUIUpdateThread extends Thread
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: IcsGUIUpdateThread.java | Tue May 5 10:20:46 2020 +0000 | Chris Mottram  $");
	/**
	 * The IcsGUI object.
	 */
	private IcsGUI parent = null;
	/**
	 * The time to sleep (in milliseconds) between spawning GET_STATUS calls.
	 */
	private long autoUpdateTime = 0;
	/**
	 * Boolean to quit the thread.
	 */
	private boolean quit = false;

	/**
	 * A constructor for this class. Calls the parent constructor. Sets the parent reference.
	 * Sets the auto-update time.
	 * @param p The parent object.
	 * @param time The time to wait between calls to GET_STATUS.
	 */
	public IcsGUIUpdateThread(IcsGUI p,long time)
	{
		super();
		parent = p;
		autoUpdateTime = time;
		quit = false;
	}

	/**
	 * Run method. 
	 * This thread runs in a loop until quit is true. It uses parent.sendCommand
	 * to send a GET_STATUS command to the server. The associated client connection thread automatically
	 * updates the relevant GUI elements. The thread then sleeps for the autoUpdateTime,
	 * @see #quit
	 * @see #autoUpdateTime
	 * @see IcsGUI#sendCommand
	 */
	public void run()
	{
		ngat.message.ISS_INST.GET_STATUS command = null;
		IcsGUIClientConnectionThread thread = null;
		String idString = null;
		int id,level;

		id = 0;
		try
		{
			level = parent.getStatus().getPropertyInteger("ics_gui.auto_update.get_status.level");
		}
		catch(NumberFormatException e)
		{
			parent.error("Auto-update:Invalid GET_STATUS level:Using zero.");
			level = 0;
		}
		while(!quit)
		{
		// send the command
			idString = new String(this.getClass().getName()+":"+id);
			command = new ngat.message.ISS_INST.GET_STATUS(idString);
			command.setLevel(level);
			parent.log("Auto-update sending command:"+command.getClass().getName()+
				":level="+command.getLevel());
			thread = parent.sendCommand(command);
			while(thread.isAlive())
			{
				try
				{
					thread.join();
				}
				catch(InterruptedException e)
				{
					parent.error("The auto update thread was interruped(2):"+e);
				}
			}
			id++;
		// sleep for a bit
			try
			{
				Thread.sleep(autoUpdateTime);
			}
			catch(InterruptedException e)
			{
				parent.error("The auto update thread was interruped(1):"+e);
			}
		}// end while
	}

	/**
	 * Quit method. Sets quit to true, so that the run method will exit.
	 */
	public void quit()
	{
		quit = true;
	}
}
//
// $Log: not supported by cvs2svn $
// Revision 0.8  2006/05/16 17:12:19  cjm
// gnuify: Added GNU General Public License.
//
// Revision 0.7  2003/09/19 14:08:45  cjm
// Changed CcsGUI to IcsGUI.
//
// Revision 0.6  2003/07/15 16:20:01  cjm
// Fixed documentation bug.
//
// Revision 0.5  2000/07/03 10:33:09  cjm
// Level of GET_STATUS now determined by
// ccs_gui.auto_update.get_status.level property.
//
// Revision 0.4  2000/05/22 11:14:06  cjm
// Swapped round position of sleep to make stopping
// /starting the thread look more responsive.
//
// Revision 0.3  2000/03/10 11:57:10  cjm
// The run method now waits for the currently spawned GET_STATUS client thread to terminate before
// entering the sleep for the next one. This stops the GUI spawning lots of client threads
// that the Ccs spends all it's time starting and not finishing.
//
// Revision 0.2  2000/02/15 16:15:27  cjm
// Now sets level of GET_STATUS command.
//
// Revision 0.1  1999/12/08 10:43:21  cjm
// initial revision.
//
//
