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
// IcsGUIBSSServerConnectionThread.java
// $Header: /home/cjm/cvs/ics_gui/java/IcsGUIBSSServerConnectionThread.java,v 1.1 2011-10-31 13:52:30 cjm Exp $
import java.lang.*;
import java.lang.reflect.InvocationTargetException;
import java.io.*;
import java.net.*;
import java.util.*;

import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import ngat.fits.*;
import ngat.net.*;
import ngat.message.base.*;
import ngat.message.ISS_INST.*;
import ngat.message.INST_BSS.*;

/**
 * This class extends the TCPServerConnectionThread class for the IcsGUI application. This
 * allows IcsGUI to emulate the ISS's response to the instrument sending it commands.
 * @author Chris Mottram
 * @version $Revision: f44807ce8017463d3931e3f19f402e0eddeb854d $
 */
public class IcsGUIBSSServerConnectionThread extends TCPServerConnectionThread
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: IcsGUIBSSServerConnectionThread.java | Mon Oct 31 13:52:34 2011 +0000 | Chris Mottram  $");
	/**
	 * Default time taken to respond to a command.
	 */
	private final static int DEFAULT_ACKNOWLEDGE_TIME = 60*1000;
	/**
	 * File name containing FITS defaults properties for Ics GUI.
	 */
	private final static String FITS_DEFAULTS_FILE_NAME = "./ics_gui.fits.bss.properties";
	/**
	 * The IcsGUI object.
	 */
	private IcsGUI parent = null;

	/**
	 * Constructor of the thread. This just calls the superclass constructors.
	 * @param connectionSocket The socket the thread is to communicate with.
	 */
	public IcsGUIBSSServerConnectionThread(Socket connectionSocket)
	{
		super(connectionSocket);
	}

	/**
	 * Routine to set this objects pointer to the parent object.
	 * @param c The parent object.
	 */
	public void setParent(IcsGUI c)
	{
		this.parent = c;
	}

	/**
	 * This method calculates the time it will take for the command to complete and is called
	 * from the classes inherited run method.
	 */
	protected ACK calculateAcknowledgeTime()
	{
		ACK acknowledge = null;
		int time;

		acknowledge = new ACK(command.getId());
		if((command instanceof ngat.message.INST_BSS.GET_FITS)||
			(command instanceof ngat.message.INST_BSS.GET_FOCUS_OFFSET))
		{
			acknowledge.setTimeToComplete(DEFAULT_ACKNOWLEDGE_TIME);
		}
		else
			acknowledge.setTimeToComplete(DEFAULT_ACKNOWLEDGE_TIME);
		return acknowledge;
	}

	/**
	 * This method overrides the processCommand method in the ngat.net.TCPServerConnectionThread class.
	 * It is called from the inherited run method. It is responsible for performing the commands
	 * sent to it by the IcsGUI. 
	 * It should also construct the done object to describe the results of the command.
	 */
	protected void processCommand()
	{
		if(command == null)
		{
			processError("processCommand:command was null.");
			done = new COMMAND_DONE(command.getId());
			done.setErrorNum(1);
			done.setErrorString("processCommand:command was null.");
			done.setSuccessful(false);
			return;
		}
		parent.log("Command:"+command.getClass().getName()+" received from the instrument.");
	// setup return object.
		if(command instanceof ngat.message.INST_BSS.GET_FITS)
		{
			ngat.message.INST_BSS.GET_FITS_DONE getFitsDone = 
				new ngat.message.INST_BSS.GET_FITS_DONE(command.getId());
			Vector fitsHeaderList = null;
			FitsHeaderDefaults getFitsDefaults = null;

			parent.log(command.getClass().getName()+" received.");
			try
			{
				getFitsDefaults = new FitsHeaderDefaults();
				//diddly BSS filename
				getFitsDefaults.load(FITS_DEFAULTS_FILE_NAME);
				fitsHeaderList = getFitsDefaults.getCardImageList();
				getFitsDone.setFitsHeader(fitsHeaderList);
				getFitsDone.setErrorNum(0);
				getFitsDone.setErrorString("");
				getFitsDone.setSuccessful(true);
			}
			catch(Exception e)
			{
				fitsHeaderList = new Vector();
				getFitsDone.setFitsHeader(fitsHeaderList);
				getFitsDone.setErrorNum(2);
				getFitsDone.setErrorString("GET_FITS:Getting FITS defaults failed:"+e);
				getFitsDone.setSuccessful(false);
			}
			done = getFitsDone;
		}
		if(command instanceof ngat.message.INST_BSS.GET_FOCUS_OFFSET)
		{
			ngat.message.INST_BSS.GET_FOCUS_OFFSET getFocusOffsetCommand = 
				(ngat.message.INST_BSS.GET_FOCUS_OFFSET)command;
			ngat.message.INST_BSS.GET_FOCUS_OFFSET_DONE getFocusOffsetDone = 
				new ngat.message.INST_BSS.GET_FOCUS_OFFSET_DONE(command.getId());

			getFocusOffsetDone.setFocusOffset(0.0f);
			getFocusOffsetDone.setErrorNum(0);
			getFocusOffsetDone.setErrorString("");
			getFocusOffsetDone.setSuccessful(true);
			done = getFocusOffsetDone;
		}
		parent.log("Command:"+command.getClass().getName()+" Completed.");
	}

	/**
	 * This method is called when the thread generates an error.
	 * Currently, this just prints the string using the parents error method.
	 * @param errorString The error string.
	 * @see IcsGUI#error
	 * @see #parent
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
//
