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
// IcsGUIServer.java
// $Header: /home/cjm/cvs/ics_gui/java/IcsGUIServer.java,v 0.5 2006-05-16 17:12:16 cjm Exp $
import java.lang.*;
import java.io.*;
import java.net.*;

import ngat.net.*;

/**
 * This class extends the TCPServer class for the IcsGUI application. The IcsGUI sends
 * commands to the Ccs. Some Ccs commands involve sending commands back to the ISS, and
 * this class is designed to catch these requests and to spawn a IcsGUIServerConnectionThread to deal with them.
 * @author Chris Mottram
 * @version $Revision$
 */
public class IcsGUIServer extends TCPServer
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id$");
	/**
	 * Field holding the instance of the IcsGUI currently executing, 
	 * so we can pass this to spawned threads.
	 */
	private IcsGUI parent = null;

	/**
	 * The constructor. Call the inherited constrctor.
	 */
	public IcsGUIServer(String name,int portNumber)
	{
		super(name,portNumber);
	}

	/**
	 * Routine to set this objects pointer to the parent object.
	 * @param o The parent object.
	 * @see #parent
	 */
	public void setParent(IcsGUI o)
	{
		this.parent = o;
	}

	/**
	 * This routine spawns threads to handle connection to the server. This routine
	 * spawns <a href="IcsGUIServerConnectionThread.html">IcsGUIServerConnectionThread</a> thread.
	 * @see IcsGUIServerConnectionThread
	 */
	public void startConnectionThread(Socket connectionSocket)
	{
		IcsGUIServerConnectionThread thread = null;

		thread = new IcsGUIServerConnectionThread(connectionSocket);
		thread.setParent(parent);
		thread.start();
	}

	/**
	 * Overwritten processError method, that receives error messages when an error occurs.
	 * The error is dealt with using the parent's error method.
	 * @param errorString The error string generated.
	 * @see IcsGUI#error
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
	 * @see IcsGUI#error
	 * @see IcsGUI#getErrorStream
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
// Revision 0.4  2003/09/19 14:08:45  cjm
// Changed CcsGUI to IcsGUI.
//
// Revision 0.3  2001/07/10 18:21:28  cjm
// errors now print stack trace.
//
// Revision 0.2  2001/02/13 10:27:47  cjm
// Added processError method to put errors into the logging box.
//
// Revision 0.1  1999/11/22 09:53:49  cjm
// initial revision.
//
//
