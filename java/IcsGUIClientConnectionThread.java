// CcsGUIClientConnectionThread.java
// $Header: /home/cjm/cvs/ics_gui/java/IcsGUIClientConnectionThread.java,v 0.14 2002-12-16 18:35:51 cjm Exp $

import java.lang.*;
import java.io.*;
import java.net.*;
import java.util.*;

import ngat.net.*;
import ngat.message.base.*;
import ngat.message.ISS_INST.*;
import ngat.util.StringUtilities;

/**
 * The CcsGUIClientConnectionThread extends TCPClientConnectionThread. 
 * It implements the generic ISS instrument command protocol.
 * It is used to send commands from the CcsGUI to the Ccs.
 * @author Chris Mottram
 * @version $Revision: 0.14 $
 */
public class CcsGUIClientConnectionThread extends TCPClientConnectionThreadMA
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: IcsGUIClientConnectionThread.java,v 0.14 2002-12-16 18:35:51 cjm Exp $");
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
	 * @see #printFilenameAck
	 * @see #printCalibrateDpAck
	 * @see #printExposeDpAck
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
		if(acknowledge instanceof FILENAME_ACK)
			printFilenameAck((FILENAME_ACK)acknowledge);
		if(acknowledge instanceof CALIBRATE_DP_ACK)
			printCalibrateDpAck((CALIBRATE_DP_ACK)acknowledge);
		if(acknowledge instanceof EXPOSE_DP_ACK)
			printExposeDpAck((EXPOSE_DP_ACK)acknowledge);
	}

	/**
	 * Routine to print out more details if the acknowledge was a (subclass of) FILENAME_ACK. 
	 * This prints out the returned filename. The filename label is also updated.
	 * @param filenameAck The acknowledge object passed back to the client.
	 * @see CcsGUI#setFilenameLabel
	 */
	private void printFilenameAck(FILENAME_ACK filenameAck)
	{
		String commandName = null;

	// get command name from class of ACK
		commandName = filenameAck.getClass().getName();
		commandName = StringUtilities.replace(commandName,"_DP_ACK","");
		commandName = StringUtilities.replace(commandName,"_ACK","");
	// print filename to log
		parent.log("The current "+commandName+" frame is:"+filenameAck.getFilename()+".");
	// update filename label
		parent.setFilenameLabel(filenameAck.getFilename());
	}

	/**
	 * Routine to print out more details if the acknowledge was a (subclass of) CALIBRATE_DP_ACK. 
	 * Note the returned filename will have already been printed out by printFilenameAck, 
	 * as CALIBRATE_DP_ACK is a sub-class of FILENAME_ACK.
	 * @param calibrateDpAck The acknowledge object passed back to the client.
	 * @see #printFilenameAck
	 */
	private void printCalibrateDpAck(CALIBRATE_DP_ACK calibrateDpAck)
	{
		String commandName = null;

	// get command name from class of ACK
		commandName = calibrateDpAck.getClass().getName();
		commandName = StringUtilities.replace(commandName,"_DP_ACK","");
	// print data pipeline values to log.
		parent.log(commandName+":Mean Counts:"+calibrateDpAck.getMeanCounts()+
			":Peak Counts:"+calibrateDpAck.getPeakCounts());
	}

	/**
	 * Routine to print out more details if the acknowledge was a (subclass of) EXPOSE_DP_ACK. 
	 * Note the returned filename will have already been printed out by printFilenameAck, 
	 * as EXPOSE_DP_ACK is a sub-class of FILENAME_ACK.
	 * @param exposeDpAck The acknowledge object passed back to the client.
	 * @see #printFilenameAck
	 */
	private void printExposeDpAck(EXPOSE_DP_ACK exposeDpAck)
	{
		String commandName = null;

	// get command name from class of ACK
		commandName = exposeDpAck.getClass().getName();
		commandName = StringUtilities.replace(commandName,"_DP_ACK","");
	// print data pipeline values to log.
		parent.log(commandName+" Seeing:"+exposeDpAck.getSeeing()+
			":Brightest Counts:"+exposeDpAck.getCounts()+
			":Brightest X Pixel:"+exposeDpAck.getXpix()+":Brightest Y Pixel:"+exposeDpAck.getYpix()+
			":Photometricity:"+exposeDpAck.getPhotometricity()+
			":Sky Brightness:"+exposeDpAck.getSkyBrightness()+
			":Saturated:"+exposeDpAck.getSaturation());
	}

	/**
	 * This routine processes the done object returned by the server. Currently prints out
	 * the basic return values in done. If done is an instance of some sub-class
	 * a <b>print...Done</b> method is called to print out more informatiom.
	 * @see #printGetStatusDone
	 * @see #printCalibrateDone
	 * @see #printExposeDone
	 * @see #printTelFocusDone
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
				if(done instanceof TELFOCUS_DONE)
					printTelFocusDone((TELFOCUS_DONE)done);
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

	/**
	 * Method to display some status for a GET_STATUS message.
	 * @param getStatusDone The DONE object instance containing status data to display.
	 * @see #processDone
	 */
	private void printGetStatusDone(GET_STATUS_DONE getStatusDone)
	{
		Hashtable displayInfo = null;
		String instrumentString = null;
		Integer integer = null;
		Long longObject = null;
		Double ccdTemperature = null;
		int exposureCount,exposureNumber;
		long exposureLength,elapsedExposureTime;

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
	// which instrument are we getting status from?
		instrumentString = (String)(displayInfo.get("Instrument"));
		if(instrumentString == null)
			instrumentString = "RATCam";
	// set current command status
		parent.setCurrentCommandLabel((String)(displayInfo.get("currentCommand")));
	// set filters selected status
		if(instrumentString.equals("RATCam")||instrumentString.equals("FARTCam"))
		{
			parent.setFiltersSelectedLabel((String)(displayInfo.get("Filter Wheel:0")),
				(String)(displayInfo.get("Filter Wheel:1")));
		}
		else if(instrumentString.equals("Nu-View"))
		{
			parent.setFiltersSelectedLabel((Double)(displayInfo.get("Filter Wavelength")));
		}
		else if(instrumentString.equals("MES"))
		{
			parent.setFiltersSelectedLabel((Integer)(displayInfo.get("Filter Position")));
		}
	// set remaining exposures status
		integer = (Integer)(displayInfo.get("Exposure Count"));
		exposureCount = integer.intValue();
		integer = (Integer)(displayInfo.get("Exposure Number"));
		exposureNumber = integer.intValue();
		if(exposureCount <= 0)// special case MOVIE - or not exposing
			parent.setRemainingExposuresLabel(0);
		else
			parent.setRemainingExposuresLabel(exposureCount-exposureNumber);
	// set remaining exposure time label
		if(getStatusDone.getCurrentMode() == GET_STATUS_DONE.MODE_EXPOSING)
		{
			integer = (Integer)(displayInfo.get("Exposure Length"));
			exposureLength = integer.longValue();
		// If Elapsed Exposure Time was included, use this to calculate remaining exposure time
			if(displayInfo.containsKey("Elapsed Exposure Time"))
			{
				integer = (Integer)(displayInfo.get("Elapsed Exposure Time"));
				elapsedExposureTime = integer.longValue();
				parent.setRemainingExposureTimeLabel(exposureLength-elapsedExposureTime);
			}
		// otherwise, use current time - exposure start time to calculate remaining exposure time
			else
			{
				longObject = (Long)(displayInfo.get("Exposure Start Time"));
 				elapsedExposureTime = System.currentTimeMillis()-longObject.longValue();
				parent.setRemainingExposureTimeLabel(exposureLength-elapsedExposureTime);
				parent.log("Elapsed Exposure Time not received, using Exposure Start Time"+
					" to calculate remaining exposure time.");
			}
		}
		else
			parent.setRemainingExposureTimeLabel(0L);
	// set temperature
		if(displayInfo.containsKey("Temperature"))
		{
			ccdTemperature = (Double)(displayInfo.get("Temperature"));
			if(ccdTemperature != null)
				parent.setCCDTemperatureLabel(ccdTemperature.doubleValue());
		}
		else
		{
			parent.log("Temperature not updated.");
		}
	}

	/**
	 * Method to print out parameters associated with a CALIBRATE_DONE (sub)class instance.
	 * These parameters are <i>filename</i>, <i>peakCounts</i> and <i>meanCounts</i>.
	 * @param calibrateDone The instance of the DONE message.
	 * @see CcsGUI#setFilenameLabel
	 */
	private void printCalibrateDone(CALIBRATE_DONE calibrateDone)
	{
		parent.log("Filename:"+calibrateDone.getFilename()+
			":peakCounts:"+calibrateDone.getPeakCounts()+
			":meanCounts:"+calibrateDone.getMeanCounts());
		parent.setFilenameLabel(calibrateDone.getFilename());
	}

	/**
	 * Method to print out parameters associated with a EXPOSE_DONE (sub)class instance.
	 * These parameters are <i>Seeing</i>, <i>Counts</i>, <i>X_pix</i> and <i>Y_pix</i>.
	 * @param exposeDone The instance of the DONE message.
	 * @see CcsGUI#setFilenameLabel
	 */
	private void printExposeDone(EXPOSE_DONE exposeDone)
	{
		parent.log("Filename:"+exposeDone.getFilename()+
			":Seeing:"+exposeDone.getSeeing()+
			":Brightest Counts:"+exposeDone.getCounts()+
			":Brightest X Pixel:"+exposeDone.getXpix()+
			":Brightest Y Pixel:"+exposeDone.getYpix()+
			":Photometricity:"+exposeDone.getPhotometricity()+
			":Sky Brightness:"+exposeDone.getSkyBrightness()+
			":Saturated:"+exposeDone.getSaturation());
		parent.setFilenameLabel(exposeDone.getFilename());
	}

	/**
	 * Method to print out parameters associated with a TELFOCUS_DONE (sub)class instance.
	 * These parameters are <i>Seeing</i> and <i>Current Focus</i>. The Chi-Squared fit parameters
	 * are also printed:<i>A</i>,<i>B</i> and <i>C</i>, and also the best chi-squared found <i>chiSquared</i>.
	 * @param telFocusDone The instance of the DONE message.
	 */
	private void printTelFocusDone(TELFOCUS_DONE telFocusDone)
	{
		parent.log("Seeing:"+telFocusDone.getSeeing()+
			":Current Focus:"+telFocusDone.getCurrentFocus());
		parent.log("Chi Squared "+telFocusDone.getChiSquared()+
			":y = ("+telFocusDone.getA()+"x\u00B2) + ("+
			telFocusDone.getB()+"x) + "+telFocusDone.getC());
	}
}
//
// $Log: not supported by cvs2svn $
// Revision 0.13  2002/05/23 12:44:53  cjm
// Added prints for extra fields in EXPOSE_DONE.
//
// Revision 0.12  2001/08/15 08:41:36  cjm
// Re-written ACK and TELFOCUS_DONE prints for new ACK hierarchy,
// and new TELFOCUS_DONE fields.
//
// Revision 0.11  2001/07/10 18:21:28  cjm
// setFilenameLabel called for returned filenames.
// stack trace added to error handler.
// setFiltersSelectedLabel calls dependant of instrument.
//
// Revision 0.10  2000/08/29 11:41:38  cjm
// Added printTelFocusDone to processDone.
//
// Revision 0.9  2000/07/03 10:34:08  cjm
// Remaining exposure time implementation changed so when
// Elapsed Exposure Time not returned by Ccs an approximation
// is returned.
//
// Revision 0.8  2000/06/15 12:13:34  cjm
// Test whether Temperature information returned.
// Stops NullPointerException.
//
// Revision 0.7  2000/03/27 16:47:31  cjm
// Improve remaining exposure time/CCD temperature with new keys from Ccs.
//
// Revision 0.6  2000/03/02 12:09:36  cjm
// Added filename printing to DONE messages.
//
// Revision 0.5  2000/02/28 19:14:40  cjm
// Backup.
//
// Revision 0.4  2000/02/22 15:42:15  cjm
// DONE subclass prints only occur on successful is true.
//
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
