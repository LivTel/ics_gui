// CcsGUIServerConnectionThread.java -*- mode: Fundamental;-*-
// $Header: /home/cjm/cvs/ics_gui/java/IcsGUIServerConnectionThread.java,v 0.2 1999-12-09 17:02:12 cjm Exp $
import java.lang.*;
import java.lang.reflect.InvocationTargetException;
import java.io.*;
import java.net.*;
import java.util.*;

import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import ngat.net.*;
import ngat.message.base.*;
import ngat.message.ISS_INST.*;
import ngat.swing.GUIMessageDialogShower;

/**
 * This class extends the TCPServerConnectionThread class for the CcsGUI application. This
 * allows CcsGUI to emulate the ISS's response to the CCS sending it commands.
 * @author Chris Mottram
 * @version $Revision: 0.2 $
 */
public class CcsGUIServerConnectionThread extends TCPServerConnectionThread
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: IcsGUIServerConnectionThread.java,v 0.2 1999-12-09 17:02:12 cjm Exp $");
	/**
	 * Default time taken to respond to a command.
	 */
	private final static int DEFAULT_ACKNOWLEDGE_TIME = 60*1000;
	/**
	 * The CcsGUI object.
	 */
	private CcsGUI parent = null;

	/**
	 * Constructor of the thread. This just calls the superclass constructors.
	 * @param connectionSocket The socket the thread is to communicate with.
	 */
	public CcsGUIServerConnectionThread(Socket connectionSocket)
	{
		super(connectionSocket);
	}

	/**
	 * Routine to set this objects pointer to the parent object.
	 * @param c The parent object.
	 */
	public void setParent(CcsGUI c)
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
		if((command instanceof AG_START)||
			(command instanceof AG_STOP)||
			(command instanceof GET_FITS)||
			(command instanceof MOVE_FOLD)||
			(command instanceof OFFSET_DEC)||
			(command instanceof OFFSET_FOCUS)||
			(command instanceof OFFSET_RA)||
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
	 * sent to it by the CcsGUI. 
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
		parent.log("Command:"+command.getClass().getName()+" received from the Ccs.");
		if(parent.getISSMessageDialog())
		{
			ACK infiniteAcknowledge = new ACK(command.getId());

		// Make the Ccs wait forever.
			infiniteAcknowledge.setTimeToComplete(0);
			try
			{
				sendAcknowledge(infiniteAcknowledge);
			// bring up a dialog.
				SwingUtilities.invokeAndWait(new GUIMessageDialogShower((Component)null,
					(Object)("Please process Command "+command.getClass().getName()+
					" and press Ok."),
					" ISS Command Received ",JOptionPane.INFORMATION_MESSAGE));
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
		}
	// setup return object.
		if(command instanceof AG_START)
		{
			// do nothing 
			AG_START_DONE agStartDone = new AG_START_DONE(command.getId());

			agStartDone.setErrorNum(0);
			agStartDone.setErrorString("");
			agStartDone.setSuccessful(true);
			done = agStartDone;
		}
		if(command instanceof AG_STOP)
		{
			// do nothing 
			AG_STOP_DONE agStopDone = new AG_STOP_DONE(command.getId());

			agStopDone.setErrorNum(0);
			agStopDone.setErrorString("");
			agStopDone.setSuccessful(true);
			done = agStopDone;
		}
		if(command instanceof GET_FITS)
		{
			// do nothing 
			GET_FITS_DONE getFitsDone = new GET_FITS_DONE(command.getId());
			Hashtable hashTable = new Hashtable();

// lots more stuff to go here
			hashTable.put("OBSERVAT","LIVERPOOL");
			hashTable.put("TELESCOP","LIVERPOOL");
			hashTable.put("OBSERVER","observer");
			hashTable.put("OBJECT","object");
			hashTable.put("RADECSYS","FK5");
			hashTable.put("RA",new Double(0.0));
			hashTable.put("DEC",new Double(0.0));
			// hashTable.put("ST","sidereal time");// sidereal time
			// UTSTART
			// gm_time = gmtime(&tp);
			// strftime(gmt, 70, "%T", gm_time);
			hashTable.put("UTSTART","gmt");
			hashTable.put("EQUINOX",new Double(2000.0));
			hashTable.put("EPOCH",new Double(1999.5));
			hashTable.put("AIRMASS",new Double(99.9));
			getFitsDone.setFitsHeader(hashTable);
			getFitsDone.setErrorNum(0);
			getFitsDone.setErrorString("");
			getFitsDone.setSuccessful(true);
			done = getFitsDone;
		}
		if(command instanceof MOVE_FOLD)
		{
			MOVE_FOLD moveFold = null;
			// do nothing 
			MOVE_FOLD_DONE moveFoldDone = new MOVE_FOLD_DONE(command.getId());

			moveFold = (MOVE_FOLD)command;
			parent.log(command.getClass().getName()+" has port number "+
				moveFold.getMirror_position()+".");

			moveFoldDone.setErrorNum(0);
			moveFoldDone.setErrorString("");
			moveFoldDone.setSuccessful(true);
			done = moveFoldDone;
		}
		if(command instanceof OFFSET_DEC)
		{
			// do nothing 
			OFFSET_DEC_DONE offsetDecDone = new OFFSET_DEC_DONE(command.getId());

			offsetDecDone.setErrorNum(0);
			offsetDecDone.setErrorString("");
			offsetDecDone.setSuccessful(true);
			done = offsetDecDone;
		}
		if(command instanceof OFFSET_FOCUS)
		{
			// do nothing 
			OFFSET_FOCUS_DONE offsetFocusDone = new OFFSET_FOCUS_DONE(command.getId());

			offsetFocusDone.setErrorNum(0);
			offsetFocusDone.setErrorString("");
			offsetFocusDone.setSuccessful(true);
			done = offsetFocusDone;
		}
		if(command instanceof OFFSET_RA)
		{
			// do nothing 
			OFFSET_RA_DONE offsetRaDone = new OFFSET_RA_DONE(command.getId());

			offsetRaDone.setErrorNum(0);
			offsetRaDone.setErrorString("");
			offsetRaDone.setSuccessful(true);
			done = offsetRaDone;
		}
		if(command instanceof OFFSET_ROTATOR)
		{
			// do nothing 
			OFFSET_ROTATOR_DONE offsetRotatorDone = new OFFSET_ROTATOR_DONE(command.getId());

			offsetRotatorDone.setErrorNum(0);
			offsetRotatorDone.setErrorString("");
			offsetRotatorDone.setSuccessful(true);
			done = offsetRotatorDone;
		}
		if(command instanceof SET_FOCUS)
		{
			// do nothing 
			SET_FOCUS_DONE setFocusDone = new SET_FOCUS_DONE(command.getId());

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
	 * @see CcsGUI#error
	 * @see #parent
	 */
	protected void processError(String errorString)
	{
		parent.error(errorString);
	}
}
//
// $Log: not supported by cvs2svn $
// Revision 0.1  1999/11/22 09:53:49  cjm
// initial revision.
//
//
