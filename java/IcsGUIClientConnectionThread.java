// CcsGUIClientConnectionThread.java -*- mode: Fundamental;-*-
// $Header: /home/cjm/cvs/ics_gui/java/IcsGUIClientConnectionThread.java,v 0.4 2000-02-22 15:42:15 cjm Exp $

import java.lang.*;
import java.io.*;
import java.net.*;
import java.util.*;

import ngat.net.*;
import ngat.message.base.*;
import ngat.message.ISS_INST.MOVIE_ACK;
import ngat.message.ISS_INST.MULTRUN_ACK;
import ngat.message.ISS_INST.MULTRUN_DP_ACK;
import ngat.message.ISS_INST.CALIBRATE_DONE;
import ngat.message.ISS_INST.EXPOSE_DONE;
import ngat.message.ISS_INST.GET_STATUS_DONE;

/**
 * The CcsGUIClientConnectionThread extends TCPClientConnectionThread. 
 * It implements the generic ISS instrument command protocol.
 * It is used to send commands from the CcsGUI to the Ccs.
 * @author Chris Mottram
 * @version $Revision: 0.4 $
 */
public class CcsGUIClientConnectionThread extends TCPClientConnectionThreadMA
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: IcsGUIClientConnectionThread.java,v 0.4 2000-02-22 15:42:15 cjm Exp $");
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
		if(acknowledge instanceof MULTRUN_DP_ACK)
			printMultrunDpAck((MULTRUN_DP_ACK)acknowledge);
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
	 * Routine to print out more details if the acknowledge was a MULTRUN_DP_ACK. Note the returned filename
	 * will have already been printed out by printMultrunAck, as MULTRUN_DP_ACK is a sub-class of MULTRUN_ACK.
	 * @param multrunDpAck The acknowledge object passed back to the client.
	 */
	private void printMultrunDpAck(MULTRUN_DP_ACK multrunDpAck)
	{
		parent.log("Seeing:"+multrunDpAck.getSeeing()+":Brightest Counts:"+multrunDpAck.getCounts()+
			":Brightest X Pixel:"+multrunDpAck.getXpix()+":Brightest Y Pixel:"+multrunDpAck.getYpix());
	}

	/**
	 * This routine processes the done object returned by the server. Currently prints out
	 * the basic return values in done. If done is an instance of GET_STATUS_DONE calls printGetStatus
	 * to print out more informatiom.
	 * @see #printGetStatus
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
				"successful:"+done.getSuccessful());
			if(done.getSuccessful())
			{
				if(done instanceof GET_STATUS_DONE)
					printGetStatusDone((GET_STATUS_DONE)done);
				if(done instanceof CALIBRATE_DONE)
					printCalibrateDone((CALIBRATE_DONE)done);
				if(done instanceof EXPOSE_DONE)
					printExposeDone((EXPOSE_DONE)done);
			}
			else
			{
				parent.log("Error:error Number:"+done.getErrorNum()+
					"\nerror String:"+done.getErrorString());
			}
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

	/**
	 * Method to display some status for a GET_STATUS message.
	 * @param getStatusDone The DONE object instance containing status data to display.
	 * @see #processDone
	 */
	private void printGetStatusDone(GET_STATUS_DONE getStatusDone)
	{
		Hashtable displayInfo = null;
		Integer integer = null;
		int exposureCount,exposureNumber;

	// current mode
		parent.log("The current mode:"+getStatusDone.getCurrentMode()+".");
		parent.setCCDStatusLabel(getStatusDone.getCurrentMode());
	// displayInfo hash table
		displayInfo = getStatusDone.getDisplayInfo();
		if(displayInfo == null)
		{
			parent.log("The display information was null.");
			return;
		}
	// log contents of hash table
		for (Enumeration e = displayInfo.keys(); e.hasMoreElements();)
		{
			Object key = e.nextElement();
			Object value = displayInfo.get(key);
			parent.log(key.toString()+":"+value.toString());
		}
	// set current command status
		parent.setCurrentCommandLabel((String)(displayInfo.get("currentCommand")));
	// set filters selected status
		parent.setFiltersSelectedLabel((String)(displayInfo.get("Filter Wheel:0")),
			(String)(displayInfo.get("Filter Wheel:1")));
	// set remaining exposures status
		integer = (Integer)(displayInfo.get("Exposure Count"));
		exposureCount = integer.intValue();
		integer = (Integer)(displayInfo.get("Exposure Number"));
		exposureNumber = integer.intValue();
		if(exposureCount <= 0)// special case MOVIE - or not exposing
			parent.setRemainingExposuresLabel(0);
		else
			parent.setRemainingExposuresLabel(exposureCount-exposureNumber);
	}

	/**
	 * Method to print out parameters associated with a CALIBRATE_DONE (sub)class instance.
	 * These parameters are <i>peakCounts</i> and <i>meanCounts</i>.
	 * @param calibrateDone The instance of the DONE message.
	 */
	private void printCalibrateDone(CALIBRATE_DONE calibrateDone)
	{
		parent.log("peakCounts:"+calibrateDone.getPeakCounts()+
			":meanCounts:"+calibrateDone.getMeanCounts());
	}

	/**
	 * Method to print out parameters associated with a EXPOSE_DONE (sub)class instance.
	 * These parameters are <i>Seeing</i>, <i>Counts</i>, <i>X_pix</i> and <i>Y_pix</i>.
	 * @param exposeDone The instance of the DONE message.
	 */
	private void printExposeDone(EXPOSE_DONE exposeDone)
	{
		parent.log("Seeing:"+exposeDone.getSeeing()+
			":Brightest Counts:"+exposeDone.getCounts()+
			":Brightest X Pixel:"+exposeDone.getXpix()+
			":Brightest Y Pixel:"+exposeDone.getYpix());
	}
}
//
// $Log: not supported by cvs2svn $
// Revision 0.3  2000/02/21 10:46:48  cjm
// Added more prints for EXPOSE/CALIBRATE_DONE and MULTRUN_DP_ACK.
//
// Revision 0.2  1999/12/09 17:02:12  cjm
// More functionality added.
//
// Revision 0.1  1999/11/22 09:53:49  cjm
// initial revision.
//
//
