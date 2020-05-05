/*   
    Copyright 2006, Astrophysics Research Institute, Liverpool John Moores University.

    This file is part of CcsGUI.

    CcsGUI is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    CcsGUI is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with CcsGUI; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/
// IcsGUIBSSServer.java
// $Header: /home/cjm/cvs/ics_gui/java/IcsGUIBSSServer.java,v 1.2 2020-05-05 10:20:39 cjm Exp $
import java.lang.*;
import java.io.*;
import java.net.*;

import ngat.net.*;

/**
 * This class extends the TCPServer class for the IcsGUI application. The IcsGUI sends
 * commands to an instrument. Some instrument commands involve sending commands back to the BSS, and
 * this class is designed to catch these requests and to spawn a IcsGUIBSSServerConnectionThread to deal with them.
 * @author Chris Mottram
 * @version $Revision: 1.2 $
 */
public class IcsGUIBSSServer extends TCPServer
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: IcsGUIBSSServer.java,v 1.2 2020-05-05 10:20:39 cjm Exp $");
	/**
	 * Field holding the instance of the IcsGUI currently executing, 
	 * so we can pass this to spawned threads.
	 */
	private IcsGUI parent = null;

	/**
	 * The constructor. Call the inherited constrctor.
	 * @param name The name of the server.
	 * @param portNumber The port number the server is wairing for connections on.
	 */
	public IcsGUIBSSServer(String name,int portNumber)
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
	 * spawns <a href="IcsGUIBSSServerConnectionThread.html">IcsGUIBSSServerConnectionThread</a> thread.
	 * @see IcsGUIBSSServerConnectionThread
	 */
	public void startConnectionThread(Socket connectionSocket)
	{
		IcsGUIBSSServerConnectionThread thread = null;

		thread = new IcsGUIBSSServerConnectionThread(connectionSocket);
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
// Revision 1.1  2011/10/31 13:52:34  cjm
// Initial revision
//
//
