// CcsGUIClientConnectionThread.java -*- mode: Fundamental;-*-
// $Header: /home/cjm/cvs/ics_gui/java/IcsGUIClientConnectionThread.java,v 0.2 1999-12-09 17:02:12 cjm Exp $

import java.lang.*;
import java.io.*;
import java.net.*;
import java.util.*;

import ngat.net.*;
import ngat.message.base.*;
import ngat.message.ISS_INST.MOVIE_ACK;
import ngat.message.ISS_INST.MULTRUN_ACK;
import ngat.message.ISS_INST.GET_STATUS_DONE;

/**
 * The CcsGUIClientConnectionThread extends TCPClientConnectionThread. 
 * It implements the generic ISS instrument command protocol.
 * It is used to send commands from the CcsGUI to the Ccs.
 * @author Chris Mottram
 * @version $Revision: 0.2 $
 */
public class CcsGUIClientConnectionThread extends TCPClientConnectionThreadMA
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: IcsGUIClientConnectionThread.java,v 0.2 1999-12-09 17:02:12 cjm Exp $");
	/**
	 * The CcsGUI object.
	 */
	private CcsGUI parent = null;

	/**
	 * A constructor for this class. Currently just calls the parent class's constructor.
	 */
	public CcsGUIClientConnectionThread(InetAddress address,int portNumber,COMMAND c)
	{
		super(address,portNumber,c);
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
	 * This routine processes the acknowledge object returned by the server. Currently
	 * prints out a message, giving the time to completion if the acknowledge was not null.
	 */
	protected void processAcknowledge()
	{
		if(acknowledge == null)
		{
			parent.log("Null acknowledgment received for command:"+command.getClass().getName()+".");
			return;
		}
		parent.log("Acknowledgment received for command:"+command.getClass().getName()+
			" with time to complete:"+acknowledge.getTimeToComplete()+".");
		if(acknowledge instanceof MOVIE_ACK)
			printMovieAck((MOVIE_ACK)acknowledge);
		if(acknowledge instanceof MULTRUN_ACK)
			printMultrunAck((MULTRUN_ACK)acknowledge);
	}

	/**
	 * Routine to print out more details if the acknowledge was a MOVIE_ACK. This is currently the
	 * returned filename.
	 * @param movieAck The acknowledge object passed back to the client.
	 */
	private void printMovieAck(MOVIE_ACK movieAck)
	{
		parent.log("The current movie frame is:"+movieAck.getFilename()+".");
	}

	/**
	 * Routine to print out more details if the acknowledge was a MULTRUN_ACK. This is currently the
	 * returned filename.
	 * @param multrunAck The acknowledge object passed back to the client.
	 */
	private void printMultrunAck(MULTRUN_ACK multrunAck)
	{
		parent.log("The current Multrun frame is:"+multrunAck.getFilename()+".");
	}

	/**
	 * This routine processes the done object returned by the server. Currently prints out
	 * the basic return values in done.
	 */
	protected void processDone()
	{

		if(done == null)
		{
			parent.log("Null done message received for:"+command.getClass().getName()+".");
		}
		else
		{
			parent.log("Done message received for:"+command.getClass().getName()+":"+
				"\nerror Number:"+done.getErrorNum()+
				"\nerror String:"+done.getErrorString()+
				"\nsuccessful:"+done.getSuccessful());
			if(done instanceof GET_STATUS_DONE)
				printGetStatus((GET_STATUS_DONE)done);
		}
		parent.getStatus().removeClientThread(this);
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

	private void printGetStatus(GET_STATUS_DONE getStatusDone)
	{
		Hashtable displayInfo = null;

		parent.log("The current mode:"+getStatusDone.getCurrentMode()+".");
		parent.setCCDStatus(""+getStatusDone.getCurrentMode());
		displayInfo = getStatusDone.getDisplayInfo();
		if(displayInfo == null)
		{
			parent.log("The display information was null.");
			return;
		}
		for (Enumeration e = displayInfo.keys(); e.hasMoreElements();)
		{
			Object key = e.nextElement();
			Object value = displayInfo.get(key);
			parent.log(key.toString()+":"+value.toString());
		}
	}
}
//
// $Log: not supported by cvs2svn $
// Revision 0.1  1999/11/22 09:53:49  cjm
// initial revision.
//
//
