// CcsGUIServer.java -*- mode: Fundamental;-*-
// $Header: /home/cjm/cvs/ics_gui/java/IcsGUIServer.java,v 0.3 2001-07-10 18:21:28 cjm Exp $
import java.lang.*;
import java.io.*;
import java.net.*;

import ngat.net.*;

/**
 * This class extends the TCPServer class for the CcsGUI application. The CcsGUI sends
 * commands to the Ccs. Some Ccs commands involve sending commands back to the ISS, and
 * this class is designed to catch these requests and to spawn a CcsGUIServerConnectionThread to deal with them.
 * @author Chris Mottram
 * @version $Revision: 0.3 $
 */
public class CcsGUIServer extends TCPServer
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: IcsGUIServer.java,v 0.3 2001-07-10 18:21:28 cjm Exp $");
	/**
	 * Field holding the instance of the CcsGUI currently executing, 
	 * so we can pass this to spawned threads.
	 */
	private CcsGUI parent = null;

	/**
	 * The constructor. Call the inherited constrctor.
	 */
	public CcsGUIServer(String name,int portNumber)
	{
		super(name,portNumber);
	}

	/**
	 * Routine to set this objects pointer to the parent object.
	 * @param o The parent object.
	 * @see #parent
	 */
	public void setParent(CcsGUI o)
	{
		this.parent = o;
	}

	/**
	 * This routine spawns threads to handle connection to the server. This routine
	 * spawns <a href="CcsGUIServerConnectionThread.html">CcsGUIServerConnectionThread</a> thread.
	 * @see CcsGUIServerConnectionThread
	 */
	public void startConnectionThread(Socket connectionSocket)
	{
		CcsGUIServerConnectionThread thread = null;

		thread = new CcsGUIServerConnectionThread(connectionSocket);
		thread.setParent(parent);
		thread.start();
	}

	/**
	 * Overwritten processError method, that receives error messages when an error occurs.
	 * The error is dealt with using the parent's error method.
	 * @param errorString The error string generated.
	 * @see CcsGUI#error
	 */
	protected void processError(String errorString)
	{
		parent.error(errorString);
	}

	/**
	 * This method is called when the thread generates an error due to an exception being thrown.
	 * This prints the string using the parents error method. It then prints the exception stack trace
	 * to the parents error stream.
	 * @param errorString The error string.
	 * @param exception The exception that was thrown.
	 * @see CcsGUI#error
	 * @see CcsGUI#getErrorStream
	 * @see #parent
	 */
	protected void processError(String errorString,Exception exception)
	{
		parent.error(errorString+exception);
		exception.printStackTrace(parent.getErrorStream());
	}
}
//
// $Log: not supported by cvs2svn $
// Revision 0.2  2001/02/13 10:27:47  cjm
// Added processError method to put errors into the logging box.
//
// Revision 0.1  1999/11/22 09:53:49  cjm
// initial revision.
//
//
