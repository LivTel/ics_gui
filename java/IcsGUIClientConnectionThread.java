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
// CcsGUIClientConnectionThread.java
// $Header: /home/cjm/cvs/ics_gui/java/IcsGUIClientConnectionThread.java,v 0.30 2009-02-18 16:18:48 cjm Exp $

import java.awt.*;
import java.lang.*;
import java.io.*;
import java.net.*;
import java.text.*;
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
 * @version $Revision: 0.30 $
 */
public class CcsGUIClientConnectionThread extends TCPClientConnectionThreadMA
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: IcsGUIClientConnectionThread.java,v 0.30 2009-02-18 16:18:48 cjm Exp $");
	/**
	 * The CcsGUI object.
	 */
	private IcsGUI parent = null;

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
	public void setParent(IcsGUI c)
	{
		this.parent = c;
	}

	/**
	 * This routine processes the acknowledge object returned by the server. Currently
	 * prints out a message, giving the time to completion if the acknowledge was not null.
	 * @see #printFilenameAck
	 * @see #printCalibrateDpAck
	 * @see #printExposeDpAck
	 * @see #printTemperatureAck
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
		if(acknowledge instanceof TEMPERATURE_ACK)
			printTemperatureAck((TEMPERATURE_ACK)acknowledge);
	}

	/**
	 * Routine to print out more details if the acknowledge was a (subclass of) FILENAME_ACK. 
	 * This prints out the returned filename. The filename label is also updated.
	 * Some audio feedback is performed, if required.
	 * @param filenameAck The acknowledge object passed back to the client.
	 * @see IcsGUI#setFilenameLabel
	 * @see IcsGUI#getAudioFeedback
	 * @see IcsGUI#getAudioThread
	 */
	private void printFilenameAck(FILENAME_ACK filenameAck)
	{
		String commandName = null;

	// get command name from class of ACK
		commandName = filenameAck.getClass().getName();
		commandName = StringUtilities.replace(commandName,"_DP_ACK","");
		commandName = StringUtilities.replace(commandName,"_ACK","");
	// print filename to log
		parent.log("The current "+commandName+" frame is:"+filenameAck.getFilename()+".",Color.cyan);
	// update filename label
		parent.setFilenameLabel(filenameAck.getFilename());
	// audio feedback
		if(parent.getAudioFeedback())
		{
			parent.getStatus().play(parent.getAudioThread(),"ics_gui.audio.event.filename");
		}
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
	 * Routine to print out more details if the acknowledge was a (subclass of) TEMPERATURE_ACK. 
	 * This prints out the ambient and current temperature of the instrument's CCD, after converting them from 
	 * degrees Kelvin to degrees Centigrade. 
	 * The CCD Temperature label is also updated.
	 * @param temperatureAck The acknowledge object passed back to the client.
	 * @see IcsGUI#setCCDTemperatureLabel
	 * @see IcsGUI#CENTIGRADE_TO_KELVIN
	 */
	private void printTemperatureAck(TEMPERATURE_ACK temperatureAck)
	{
		String commandName = null;
		DecimalFormat decimalFormat = null;

		decimalFormat = new DecimalFormat("###.00");
	// get command name from class of ACK
		commandName = temperatureAck.getClass().getName();
		commandName = StringUtilities.replace(commandName,"_ACK","");
	// print temperatures to log
		parent.log(commandName+":Ambient Temperature: "+
			   decimalFormat.format(temperatureAck.getAmbientTemperature()-IcsGUI.CENTIGRADE_TO_KELVIN)+
			   " :Current Temperature: "+decimalFormat.format(temperatureAck.getCurrentTemperature()-
									  IcsGUI.CENTIGRADE_TO_KELVIN));
	// update CCD temperature label
		parent.setCCDTemperatureLabel(temperatureAck.getCurrentTemperature());
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
			parent.log("Null done message received for:"+command.getClass().getName()+".",Color.red);
		}
		else
		{
			if(done.getSuccessful())
			{
				parent.log("Done message received for:"+command.getClass().getName()+":"+
					   "successful:"+done.getSuccessful(),Color.green);
				if(done instanceof GET_STATUS_DONE)
					printGetStatusDone((GET_STATUS_DONE)done);
				if(done instanceof CALIBRATE_DONE)
					printCalibrateDone((CALIBRATE_DONE)done);
				if(done instanceof EXPOSE_DONE)
					printExposeDone((EXPOSE_DONE)done);
				if(done instanceof TELFOCUS_DONE)
					printTelFocusDone((TELFOCUS_DONE)done);
				// audio feedback - only if not GET_STATUS
				if(parent.getAudioFeedback()&&((done instanceof GET_STATUS_DONE) == false))
				{
					parent.getStatus().play(parent.getAudioThread(),
								"ics_gui.audio.event.command-completed");
				}
			}
			else
			{
				parent.log("Done message received for:"+command.getClass().getName()+":"+
					   "successful:"+done.getSuccessful(),Color.red);
				parent.log("Error:error Number:"+done.getErrorNum()+
					"\nerror String:"+done.getErrorString(),Color.red);
				// audio feedback - only if not GET_STATUS
				if(parent.getAudioFeedback()&&((done instanceof GET_STATUS_DONE) == false))
				{
					parent.getStatus().play(parent.getAudioThread(),
								"ics_gui.audio.event.command-failed");
				}
			}
		}
		parent.getStatus().removeClientThread(this);
	}

	/**
	 * This method is called when the thread generates an error.
	 * Currently, this just prints the string using the parents error method.
	 * @param errorString The error string.
	 * @see #parent
	 * @see IcsGUI#error
	 * @see IcsGUI#setStatusBackground
	 * @see ngat.message.ISS_INST.GET_STATUS
	 * @see ngat.message.ISS_INST.GET_STATUS_DONE#VALUE_STATUS_FAIL
	 */
	protected void processError(String errorString)
	{
		parent.error(errorString);
	// audio feedback
		if(parent.getAudioFeedback())
		{
			parent.getStatus().play(parent.getAudioThread(),"ics_gui.audio.event.command-error");
		}
		// if we have an error getting a DONE from GET_STATUS, the RCS treats the instrument as being off-line
		// Therefore we turn the icsgui backgrounfd to fail
		if(command instanceof GET_STATUS)
		{
			// Overall Instrument health status
			parent.setStatusBackground(GET_STATUS_DONE.VALUE_STATUS_FAIL);
		}
	}

	/**
	 * This method is called when the thread generates an error due to an exception being thrown.
	 * This prints the string using the parents error method. It then prints the exception stack trace
	 * to the parents error stream.
	 * @param errorString The error string.
	 * @param exception The exception that was thrown.
	 * @see #parent
	 * @see IcsGUI#error
	 * @see IcsGUI#getErrorStream
	 * @see IcsGUI#setStatusBackground
	 * @see ngat.message.ISS_INST.GET_STATUS
	 * @see ngat.message.ISS_INST.GET_STATUS_DONE#VALUE_STATUS_FAIL
	 */
	protected void processError(String errorString,Exception exception)
	{
		parent.error(errorString+exception);
		exception.printStackTrace(parent.getErrorStream());
	// audio feedback
		if(parent.getAudioFeedback())
		{
			parent.getStatus().play(parent.getAudioThread(),"ics_gui.audio.event.command-error");
		}
		// if we have an error getting a DONE from GET_STATUS, the RCS treats the instrument as being off-line
		// Therefore we turn the icsgui backgrounfd to fail
		if(command instanceof GET_STATUS)
		{
			// Overall Instrument health status
			parent.setStatusBackground(GET_STATUS_DONE.VALUE_STATUS_FAIL);
		}
	}

	/**
	 * Method to display some status for a GET_STATUS message.
	 * @param getStatusDone The DONE object instance containing status data to display.
	 * @see #processDone
	 * @see #printGetStatusDoneTwoArms
	 * @see IcsGUI#setCCDTemperatureLabelForeground
	 * @see IcsGUI#setStatusBackground
	 * @see ngat.message.ISS_INST.GET_STATUS_DONE#KEYWORD_INSTRUMENT_STATUS
	 * @see ngat.message.ISS_INST.GET_STATUS_DONE#KEYWORD_DETECTOR_TEMPERATURE_INSTRUMENT_STATUS
	 */
	private void printGetStatusDone(GET_STATUS_DONE getStatusDone)
	{
		CcsGUIStatus status = null;
		Hashtable displayInfo = null;
		String instrumentString = null;
		String statusString = null;
		String currentCommandString = null;
		String filterTypeString = null;
		Integer integer = null;
		Long longObject = null;
		Double ccdTemperature = null;
		int exposureCount,exposureNumber;
		long exposureLength,elapsedExposureTime;

	// get status object
		status = parent.getStatus();
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
		Enumeration e = displayInfo.keys();
		ArrayList al = null;
		// Collections.list only available since 1.4
		// ArrayList al = Collections.list(e);
		// rewrite as a loop?
		while(e.hasMoreElements())
		{
			Object key = e.nextElement();
			al.add(key);
		}
		Collections.sort(al,String.CASE_INSENSITIVE_ORDER);
		for(int i = 0; i < al.size(); i++)
		{
			Object key = al.get(i);
			Object value = displayInfo.get(key);
			parent.log(key.toString()+":"+value.toString());
		}
		//for (Enumeration e = displayInfo.keys(); e.hasMoreElements();)
		//{
		//	Object key = e.nextElement();
		//	Object value = displayInfo.get(key);
		//	parent.log(key.toString()+":"+value.toString());
		//}
	// which instrument are we getting status from?
		instrumentString = (String)(displayInfo.get("Instrument"));
		if(instrumentString == null)
			instrumentString = "RATCam";
	// set current command status
		currentCommandString = (String)(displayInfo.get("currentCommand"));
		if(currentCommandString != null)
			parent.setCurrentCommandLabel(currentCommandString);
	// set filters selected status
		filterTypeString = status.getProperty("ics_gui.get_status.filter_type."+instrumentString);
		if(filterTypeString == null)
		{
			parent.setFiltersSelectedLabel("UNKNOWN");
			parent.log("Filter type string was null for:ics_gui.get_status.filter_type."+instrumentString);
		}
		else if(filterTypeString.equals("TWO_WHEELS"))
		{
			parent.setFiltersSelectedLabel("lower",(String)(displayInfo.get("Filter Wheel:0")),
				"upper",(String)(displayInfo.get("Filter Wheel:1")));
		}
		else if(filterTypeString.equals("WAVELENGTH"))
		{
			parent.setFiltersSelectedLabel((Double)(displayInfo.get("Filter Wavelength")));
		}
		else if(filterTypeString.equals("POSITION"))
		{
			parent.setFiltersSelectedLabel((Integer)(displayInfo.get("Filter Position")));
		}
		else if(filterTypeString.equals("ONE_WHEEL"))
		{
			parent.setFiltersSelectedLabel((String)(displayInfo.get("Filter Wheel:0")));
		}
		else if(filterTypeString.equals("FIXED"))
		{
			parent.setFiltersSelectedLabel((String)("Fixed"));
		}
		else if(filterTypeString.equals("TWO_ARMS"))
		{
			parent.setFiltersSelectedLabel("Red",(String)(displayInfo.get("red.Grating Position String")),
				"Blue",(String)(displayInfo.get("blue.Grating Position String")));
		}
		else
		{
			parent.setFiltersSelectedLabel("UNKNOWN");
			parent.log("Unknown filter type string:"+filterTypeString);
		}
		if(filterTypeString.equals("TWO_ARMS"))
		{
			// special FrodoSpec case.
			printGetStatusDoneTwoArms(displayInfo,getStatusDone);
		}
		else
		{
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
				if(integer != null)
					exposureLength = integer.longValue();
				else
					exposureLength = 0;
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
		}// end if not TWO_ARMS
		// Detector temperature health status
		// NB status might not be present or NULL, setCCDTemperatureLabelForeground
		// takes care of this
		statusString=(String)(displayInfo.get(GET_STATUS_DONE.KEYWORD_DETECTOR_TEMPERATURE_INSTRUMENT_STATUS));
		parent.setCCDTemperatureLabelForeground(statusString);
		// Overall Instrument health status
		// Again might be null, setStatusBackground take scare of this
		statusString = (String)(displayInfo.get(GET_STATUS_DONE.KEYWORD_INSTRUMENT_STATUS));
		parent.setStatusBackground(statusString);
	}

	/**
	 * Method to display some status for FrodoSpec.
	 * @param getStatusDone The DONE object instance containing status data to display.
	 * @see #processDone
	 * @see #printGetStatusDoneTwoArms
	 */
	private void printGetStatusDoneTwoArms(Hashtable displayInfo,GET_STATUS_DONE getStatusDone)
	{
		Integer integer = null;
		Long longObject = null;
		String redCurrentCommandString = null;
		String blueCurrentCommandString = null;
		Double redCCDTemperature = null;
		Double blueCCDTemperature = null;
		int exposureCount,exposureNumber,redCurrentMode,blueCurrentMode;
		int redRemainingExposures,blueRemainingExposures;
		long exposureLength,elapsedExposureTime;
		long redRemainingExposureTime,blueRemainingExposureTime;

		// set CCD Status
		// red
		integer = (Integer)(displayInfo.get("red.Current Mode"));
		if(integer != null)
			redCurrentMode = integer.intValue();
		else
			redCurrentMode = 0;
		// blue
		integer = (Integer)(displayInfo.get("blue.Current Mode"));
		if(integer != null)
			blueCurrentMode = integer.intValue();
		else
			blueCurrentMode = 0;
		parent.setCCDStatusLabel(redCurrentMode,blueCurrentMode);
		// current command
		redCurrentCommandString = (String)(displayInfo.get("red.currentCommand"));
		if(redCurrentCommandString.startsWith("ngat.message.ISS_INST."))
			redCurrentCommandString = redCurrentCommandString.substring(22); // strlen ngat.message...
		blueCurrentCommandString = (String)(displayInfo.get("blue.currentCommand"));
		if(blueCurrentCommandString.startsWith("ngat.message.ISS_INST."))
			blueCurrentCommandString = blueCurrentCommandString.substring(22); // strlen ngat.message...
		parent.setCurrentCommandLabel("Red:"+redCurrentCommandString+": Blue:"+blueCurrentCommandString);
	// set filters selected status
		// diddly
		// set remaining exposures status
		// red
		integer = (Integer)(displayInfo.get("red.Exposure Count"));
		if(integer != null)
			exposureCount = integer.intValue();
		else
			exposureCount = 0;
		integer = (Integer)(displayInfo.get("red.Exposure Number"));
		if(integer != null)
			exposureNumber = integer.intValue();
		else
			exposureNumber = 0;

		if(exposureCount <= 0)// special case MOVIE - or not exposing
			redRemainingExposures = 0;
		else
			redRemainingExposures = exposureCount-exposureNumber;
		// blue
		integer = (Integer)(displayInfo.get("blue.Exposure Count"));
		if(integer != null)
			exposureCount = integer.intValue();
		else
			exposureCount = 0;
		integer = (Integer)(displayInfo.get("blue.Exposure Number"));
		if(integer != null)
			exposureNumber = integer.intValue();
		else
			exposureNumber = 0;

		if(exposureCount <= 0)// special case MOVIE - or not exposing
			blueRemainingExposures = 0;
		else
			blueRemainingExposures = exposureCount-exposureNumber;
		// set GUI label
		parent.setRemainingExposuresLabel(redRemainingExposures,blueRemainingExposures);
		// set remaining exposure time label
		//if(getStatusDone.getCurrentMode() == GET_STATUS_DONE.MODE_EXPOSING)
		//{
		// red arm
		integer = (Integer)(displayInfo.get("red.Exposure Length"));
		if(integer != null)
			exposureLength = integer.longValue();
		else
			exposureLength = 0;
		// If Elapsed Exposure Time was included, use this to calculate remaining exposure time
		if(displayInfo.containsKey("red.Elapsed Exposure Time"))
		{
			integer = (Integer)(displayInfo.get("red.Elapsed Exposure Time"));
			elapsedExposureTime = integer.longValue();
			redRemainingExposureTime = exposureLength-elapsedExposureTime;
			if(redRemainingExposureTime < 0)
				redRemainingExposureTime = 0;
		}
		// otherwise, use current time - exposure start time to calculate remaining exposure time
		else
		{
			longObject = (Long)(displayInfo.get("red.Exposure Start Time"));
			elapsedExposureTime = System.currentTimeMillis()-longObject.longValue();
			redRemainingExposureTime = exposureLength-elapsedExposureTime;
			parent.log("Red Elapsed Exposure Time not received, using Exposure Start Time"+
				   " to calculate remaining exposure time.");
		}
		// blue arm
		integer = (Integer)(displayInfo.get("blue.Exposure Length"));
		if(integer != null)
			exposureLength = integer.longValue();
		else
			exposureLength = 0;
		// If Elapsed Exposure Time was included, use this to calculate remaining exposure time
		if(displayInfo.containsKey("blue.Elapsed Exposure Time"))
		{
			integer = (Integer)(displayInfo.get("blue.Elapsed Exposure Time"));
			elapsedExposureTime = integer.longValue();
			blueRemainingExposureTime = exposureLength-elapsedExposureTime;
			if(blueRemainingExposureTime < 0)
				blueRemainingExposureTime = 0;
		}
		// otherwise, use current time - exposure start time to calculate remaining exposure time
		else
		{
			longObject = (Long)(displayInfo.get("blue.Exposure Start Time"));
			elapsedExposureTime = System.currentTimeMillis()-longObject.longValue();
			blueRemainingExposureTime = exposureLength-elapsedExposureTime;
			parent.log("Blue Elapsed Exposure Time not received, using Exposure Start Time"+
				   " to calculate remaining exposure time.");
		}
		parent.setRemainingExposureTimeLabel(redRemainingExposureTime,blueRemainingExposureTime);
		// set temperature
		if(displayInfo.containsKey("red.Temperature"))
			redCCDTemperature = (Double)(displayInfo.get("red.Temperature"));
		if(displayInfo.containsKey("blue.Temperature"))
			blueCCDTemperature = (Double)(displayInfo.get("blue.Temperature"));
		if((redCCDTemperature != null)&&(blueCCDTemperature != null))
		{
			parent.setCCDTemperatureLabel(redCCDTemperature.doubleValue(),
						      blueCCDTemperature.doubleValue());
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
	 * @see IcsGUI#setFilenameLabel
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
	 * @see IcsGUI#setFilenameLabel
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
// Revision 0.29  2008/11/20 15:01:33  cjm
// Fixed comment problems.
// icsgui now turns background RED if it fails to get a DONE from a GET_STATUS message.
// Tried to make FRODOSPEC current command more readable by removing 'ngat.message.ISS_INST' preamble...
//
// Revision 0.28  2008/11/03 16:53:30  cjm
// Changed printGetStatusDone so that display info key/value pairs are ordered by string.
//
// Revision 0.27  2008/04/29 14:58:53  cjm
// Changed setCCDTemperatureLabelBackground to setCCDTemperatureLabelForeground as changing
// the background does nothing!
//
// Revision 0.26  2008/04/29 09:55:21  cjm
// Set  temperature and status panel background colour based on GET_STATUS standard instrument status.
//
// Revision 0.25  2008/01/11 15:33:07  cjm
// Added some FrodoSpec Status information.
//
// Revision 0.24  2006/09/22 11:30:35  cjm
// Replaced hard coded GET_STATUS Instrument -> "Filter Selected" mapping with ics_gui.get_status.filter_type
// config mapping.
//
// Revision 0.23  2006/09/04 15:32:24  cjm
// Added HawkCam.
//
// Revision 0.22  2006/05/16 17:12:12  cjm
// gnuify: Added GNU General Public License.
//
// Revision 0.21  2004/06/15 19:15:22  cjm
// Changed audio feedback and added new feedbacks.
//
// Revision 0.20  2004/06/03 17:09:25  cjm
// Added DillCamSouth to instrument test.
//
// Revision 0.19  2004/03/03 16:10:02  cjm
// printTemperatureAck assumes temperature in degrees Kelvin, and converts
// to centigrade before printing out. The parameter to setCCDTemperatureLabel
// is not converted, as that converts internally.
//
// Revision 0.18  2004/01/13 20:15:03  cjm
// Added printTemperatureAck TEMPERATURE_ACK handling routine.
//
// Revision 0.17  2003/11/14 15:02:12  cjm
// Added DillCam and FTSpec tests for setting selected filters.
//
// Revision 0.16  2003/09/19 14:08:45  cjm
// Changed CcsGUI to IcsGUI.
// Added audio feedback for done messages.
//
// Revision 0.15  2003/08/21 14:24:04  cjm
// Added colouration of filename and COMMAND_DONE logging.
// Added SupIRCam setFiltersSelectedLabel call.
// Fixed exposure length when null.
//
// Revision 0.14  2002/12/16 18:35:51  cjm
// Now uses GET_STATUS_DONE mode constants.
//
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
