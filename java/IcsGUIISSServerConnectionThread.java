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
// IcsGUIISSServerConnectionThread.java
// $Header: /home/cjm/cvs/ics_gui/java/IcsGUIISSServerConnectionThread.java,v 1.2 2015-09-02 14:58:17 cjm Exp $
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
import ngat.swing.GUIMessageDialogShower;

/**
 * This class extends the TCPServerConnectionThread class for the IcsGUI application. This
 * allows IcsGUI to emulate the ISS's response to the instrument sending it commands.
 * @author Chris Mottram
 * @version $Revision$
 */
public class IcsGUIISSServerConnectionThread extends TCPServerConnectionThread
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id$");
	/**
	 * Default time taken to respond to a command.
	 */
	private final static int DEFAULT_ACKNOWLEDGE_TIME = 60*1000;
	/**
	 * File name containing FITS defaults properties for Ccs GUI.
	 */
	private final static String FITS_DEFAULTS_FILE_NAME = "./ics_gui.fits.properties";
	/**
	 * The IcsGUI object.
	 */
	private IcsGUI parent = null;
	/**
	 * Variable holding the filename of a properties file to load FITS header defaults from.
	 * Defaults to FITS_DEFAULTS_FILE_NAME.
	 * @see #FITS_DEFAULTS_FILE_NAME
	 */
	private String fitsDefaultsFilename = new String(FITS_DEFAULTS_FILE_NAME);
	
	/**
	 * Constructor of the thread. This just calls the superclass constructors.
	 * @param connectionSocket The socket the thread is to communicate with.
	 */
	public IcsGUIISSServerConnectionThread(Socket connectionSocket)
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
	 * Routine to set the FITS header defaults filename FITS header defaults are laoded from when a 
	 * GET_FITS ISS command is received by this thread.
 	 * @param s A string containing a valid Java properties filename containing valid FITS header defaults.
	 * @see #fitsDefaultsFilename
	 */
	public void setFITSDefaultsFilename(String s)
	{
		fitsDefaultsFilename = s;
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
		if((command instanceof AG_START)||
			(command instanceof AG_STOP)||
			(command instanceof GET_FITS)||
			(command instanceof MOVE_FOLD)||
			(command instanceof OFFSET_FOCUS)||
			(command instanceof OFFSET_RA_DEC)||
			(command instanceof OFFSET_ROTATOR)||
			(command instanceof SET_FOCUS))
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
	 * @see #fitsDefaultsFilename
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
		if(parent.getISSMessageDialog())
		{
			ACK infiniteAcknowledge = new ACK(command.getId());

		// Make the Ccs wait forever.
			infiniteAcknowledge.setTimeToComplete(0);
			try
			{
				sendAcknowledge(infiniteAcknowledge);
		        // audio feedback
				if(parent.getAudioFeedback())
				{
					parent.getAudioThread().play(parent.getStatus().
							     getProperty("ics_gui.audio.event.iss-message"));
				}
			// bring up a dialog.
				if(command instanceof SET_FOCUS)
				{
					SwingUtilities.invokeAndWait(new GUIMessageDialogShower((Component)null,
						(Object)("Please process Command "+command.getClass().getName()+
						":"+((SET_FOCUS)command).getFocus()+" and press Ok."),
						" ISS Command Received ",JOptionPane.INFORMATION_MESSAGE));
				}
				else if(command instanceof OFFSET_FOCUS)
				{
					SwingUtilities.invokeAndWait(new GUIMessageDialogShower((Component)null,
						(Object)("Please process Command "+command.getClass().getName()+
						":"+((OFFSET_FOCUS)command).getFocusOffset()+" and press Ok."),
						" ISS Command Received ",JOptionPane.INFORMATION_MESSAGE));
				}
				else if(command instanceof OFFSET_RA_DEC)
				{
					SwingUtilities.invokeAndWait(new GUIMessageDialogShower((Component)null,
						(Object)("Please process Command "+command.getClass().getName()+
						":"+((OFFSET_RA_DEC)command).getRaOffset()+","+
						((OFFSET_RA_DEC)command).getDecOffset()+" and press Ok."),
						" ISS Command Received ",JOptionPane.INFORMATION_MESSAGE));
				}
				else if(command instanceof OFFSET_X_Y)
				{
					SwingUtilities.invokeAndWait(new GUIMessageDialogShower((Component)null,
						(Object)("Please process Command "+command.getClass().getName()+
						":"+((OFFSET_X_Y)command).getXOffset()+","+
						((OFFSET_X_Y)command).getYOffset()+" and press Ok."),
						" ISS Command Received ",JOptionPane.INFORMATION_MESSAGE));
				}
				else
				{
					SwingUtilities.invokeAndWait(new GUIMessageDialogShower((Component)null,
						(Object)("Please process Command "+command.getClass().getName()+
						" and press Ok."),
						" ISS Command Received ",JOptionPane.INFORMATION_MESSAGE));
				}
			}
			catch(IOException e)
			{
				parent.error("Bringing up message dialog for command:"+command.getClass().getName()+
					" failed:"+e);
			}
			catch(InterruptedException e)
			{
				parent.error("Bringing up message dialog for command:"+command.getClass().getName()+
					" failed:"+e);
			}
			catch(InvocationTargetException e)
			{
				parent.error("Bringing up message dialog for command:"+command.getClass().getName()+
					" failed:"+e);
			}
		}// end if get ISS message dialog
	// setup return object.
		if(command instanceof AG_START)
		{
			AG_START_DONE agStartDone = new AG_START_DONE(command.getId());

			parent.log(command.getClass().getName()+" received.");
			agStartDone.setErrorNum(0);
			agStartDone.setErrorString("");
			agStartDone.setSuccessful(true);
			done = agStartDone;
		}
		if(command instanceof AG_STOP)
		{
			AG_STOP_DONE agStopDone = new AG_STOP_DONE(command.getId());

			parent.log(command.getClass().getName()+" received.");
			agStopDone.setErrorNum(0);
			agStopDone.setErrorString("");
			agStopDone.setSuccessful(true);
			done = agStopDone;
		}
		if(command instanceof GET_FITS)
		{
			GET_FITS_DONE getFitsDone = new GET_FITS_DONE(command.getId());
			Vector fitsHeaderList = null;
			FitsHeaderDefaults getFitsDefaults = null;

			parent.log(command.getClass().getName()+" received.");
			try
			{
				parent.log(command.getClass().getName()+":Loading FITS headers from "+
					   fitsDefaultsFilename+".");
				getFitsDefaults = new FitsHeaderDefaults();
				getFitsDefaults.load(fitsDefaultsFilename);
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
		if(command instanceof MOVE_FOLD)
		{
			MOVE_FOLD moveFold = null;
			MOVE_FOLD_DONE moveFoldDone = new MOVE_FOLD_DONE(command.getId());

			moveFold = (MOVE_FOLD)command;
			parent.log(command.getClass().getName()+" has port number "+
				moveFold.getMirror_position()+".");

			moveFoldDone.setErrorNum(0);
			moveFoldDone.setErrorString("");
			moveFoldDone.setSuccessful(true);
			done = moveFoldDone;
		}
		if(command instanceof OFFSET_FOCUS)
		{
			OFFSET_FOCUS offsetFocusCommand = (OFFSET_FOCUS)command;
			OFFSET_FOCUS_DONE offsetFocusDone = new OFFSET_FOCUS_DONE(command.getId());

			parent.log(command.getClass().getName()+" to "+
				offsetFocusCommand.getFocusOffset()+".");
			offsetFocusDone.setErrorNum(0);
			offsetFocusDone.setErrorString("");
			offsetFocusDone.setSuccessful(true);
			done = offsetFocusDone;
		}
		if(command instanceof OFFSET_RA_DEC)
		{
			OFFSET_RA_DEC offsetRaDecCommand = (OFFSET_RA_DEC)command;
			OFFSET_RA_DEC_DONE offsetRaDecDone = new OFFSET_RA_DEC_DONE(command.getId());

			parent.log(command.getClass().getName()+" to "+
				offsetRaDecCommand.getRaOffset()+","+offsetRaDecCommand.getDecOffset()+".");
			offsetRaDecDone.setErrorNum(0);
			offsetRaDecDone.setErrorString("");
			offsetRaDecDone.setSuccessful(true);
			done = offsetRaDecDone;
		}
		if(command instanceof OFFSET_X_Y)
		{
			OFFSET_X_Y offsetXYCommand = (OFFSET_X_Y)command;
			OFFSET_X_Y_DONE offsetXYDone = new OFFSET_X_Y_DONE(command.getId());

			parent.log(command.getClass().getName()+" to "+
				offsetXYCommand.getXOffset()+","+offsetXYCommand.getYOffset()+".");
			offsetXYDone.setErrorNum(0);
			offsetXYDone.setErrorString("");
			offsetXYDone.setSuccessful(true);
			done = offsetXYDone;
		}
		if(command instanceof OFFSET_ROTATOR)
		{
			OFFSET_ROTATOR offsetRotatorCommand = (OFFSET_ROTATOR)command;
			OFFSET_ROTATOR_DONE offsetRotatorDone = new OFFSET_ROTATOR_DONE(command.getId());

			parent.log(command.getClass().getName()+" to "+
				offsetRotatorCommand.getRotatorOffset()+".");
			offsetRotatorDone.setErrorNum(0);
			offsetRotatorDone.setErrorString("");
			offsetRotatorDone.setSuccessful(true);
			done = offsetRotatorDone;
		}
		if(command instanceof SET_FOCUS)
		{
			SET_FOCUS setFocusCommand = (SET_FOCUS)command;
			SET_FOCUS_DONE setFocusDone = new SET_FOCUS_DONE(command.getId());

			parent.log(command.getClass().getName()+" to "+
				setFocusCommand.getFocus()+".");
			setFocusDone.setErrorNum(0);
			setFocusDone.setErrorString("");
			setFocusDone.setSuccessful(true);
			done = setFocusDone;
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
// Revision 1.1  2011/11/07 17:07:34  cjm
// Initial revision
//
// Revision 0.12  2006/05/16 17:12:16  cjm
// gnuify: Added GNU General Public License.
//
// Revision 0.11  2003/09/19 14:08:45  cjm
// Changed CcsGUI to IcsGUI.
//
// Revision 0.10  2001/09/12 19:28:14  cjm
// Updated for new OFFSET_RA_DEC command.
//
// Revision 0.9  2001/07/10 18:21:28  cjm
// Changed GET_FITS implementation to use FitsHeaderDefaults file.
// errors print a stack trace
//
// Revision 0.8  2001/01/24 15:01:11  cjm
// Improved OFFSET_FOCUS message dialog.
//
// Revision 0.7  2000/09/20 10:15:37  cjm
// Added COMPRESS keyword/value.
//
// Revision 0.6  2000/08/29 11:42:24  cjm
// Added SET_FOCUS specific dialog.
//
// Revision 0.5  2000/08/11 14:32:33  cjm
// Added logging commands.
//
// Revision 0.4  2000/05/25 10:59:26  cjm
// Fixed LONGITUD keyword.
//
// Revision 0.3  2000/05/23 15:31:45  cjm
// Improved GET_FITS command response, FITS keywords returned now as per
// LT FITS keywords specification.
//
// Revision 0.2  1999/12/09 17:02:12  cjm
// More functionality added.
//
// Revision 0.1  1999/11/22 09:53:49  cjm
// initial revision.
//
//
