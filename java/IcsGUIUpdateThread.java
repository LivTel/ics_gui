// CcsGUIUpdateThread.java -*- mode: Fundamental;-*-
// $Header: /home/cjm/cvs/ics_gui/java/IcsGUIUpdateThread.java,v 0.1 1999-12-08 10:43:21 cjm Exp $

import java.lang.*;
/**
 * The CcsGUIUpdateThread extends Thread.
 * The thread sleeps for a length determined by the auto update time.
 * It then calls parent.sendCommand(GET_STATUS) to update the GUI's status.
 * This continues until the thread is quit.
 * @author Chris Mottram
 * @version $Revision: 0.1 $
 */
public class CcsGUIUpdateThread extends Thread
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: IcsGUIUpdateThread.java,v 0.1 1999-12-08 10:43:21 cjm Exp $");
	/**
	 * The CcsGUI object.
	 */
	private CcsGUI parent = null;
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
	 * @parm time The time to wait between calls to GET_STATUS.
	 */
	public CcsGUIUpdateThread(CcsGUI p,long time)
	{
		super();
		parent = p;
		autoUpdateTime = time;
	}

	/**
	 * Run method. 
	 * This runs until quit is true. It sleeps for the autoUpdateTime, then uses parent.sendCommand
	 * to send a GET_STATUS command to the server. The associated client connection thread automatically
	 * updates the relevant GUI elements.
	 * @see #quit
	 * @see #autoUpdateTime
	 * @see CcsGUI#sendCommand
	 */
	public void run()
	{
		ngat.message.ISS_INST.GET_STATUS command = null;
		String idString = null;
		int id;

		id = 0;
		quit = false;
		while(!quit)
		{
			try
			{
				Thread.sleep(autoUpdateTime);
			}
			catch(InterruptedException e)
			{
				parent.error("The auto update thread was interruped:"+e);
			}
			idString = new String(this.getClass().getName()+":"+id);
			command = new ngat.message.ISS_INST.GET_STATUS(idString);
			parent.log("Auto-update sending command:"+command.getClass().getName());
      			parent.sendCommand(command);
			id++;
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
//
