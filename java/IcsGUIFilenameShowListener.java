// IcsGUIFilenameShowListener.java -*- mode: Fundamental;-*-
// $Header: /home/cjm/cvs/ics_gui/java/IcsGUIFilenameShowListener.java,v 1.3 2001-07-31 09:35:41 cjm Exp $
import java.lang.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import ngat.net.TitClient;
import ngat.util.*;

/**
 * This class is an ActionListener for the CcsGUI Show Filename push button. 
 * It checks CcsGUI's hostname against the Ccs's hostname, and uses an instance of TitClient
 * to transfer the image to the CcsGUI's machine if necessary.
 * It calls a command specified in the config file to display the FITS image.
 * @author Chris Mottram
 * @version $Revision: 1.3 $
 */
public class IcsGUIFilenameShowListener implements ActionListener
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: IcsGUIFilenameShowListener.java,v 1.3 2001-07-31 09:35:41 cjm Exp $");
	/**
	 * The instance of the main program.
	 */
	private CcsGUI parent = null;
	/**
	 * A copy of the CcsGUI's reference to it's status object.
	 */
	private CcsGUIStatus status = null;

	/**
	 * Constructor. This sets the parent to be main program class.
	 * It caches the parent's status object reference.
	 * @param p The parent object which owns the menu items.
	 * @see #parent
	 * @see #status
	 */
	public IcsGUIFilenameShowListener(CcsGUI p)
	{
		super();

		parent = p;
		status = parent.getStatus();
	}

	/**
	 * Routine that is called when the show button is pressed.
	 * The following operations are performed:
	 * <ul>
	 * <li>The current filename is got from the CcsGUI status window. It is checked to ensure it is valid.
	 * <li>A FilenameShower thread is started to do the rest of the processing.
	 * </ul>
	 */
	public void actionPerformed(ActionEvent event)
	{
		Thread thread = null;
		String filename = null;

	// get and check Ics filename
		filename = parent.getFilename();
		if(filename == null)
		{
			JOptionPane.showMessageDialog((Component)null,
				(Object)("Show Filename Error:Filename is null.")," Show Filename Error ",
				JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(filename.length() < 1)
		{
			JOptionPane.showMessageDialog((Component)null,
				(Object)("Show Filename Error:Filename has no length.")," Show Filename Error ",
				JOptionPane.ERROR_MESSAGE);
			return;
		}
	// Start a new thread (of the inner class) to do the work
		thread = new Thread(new FilenameShower(filename));
		thread.setName("FilenameShower:"+filename);
		thread.start();
	}

	/**
	 * Inner class that gets the filename and calls the specified program to display the file.
	 * Started as a separate thread so the GUI does not hang.
	 * @see #run
	 */
	private class FilenameShower implements Runnable
	{
		/**
		 * The current filename, passed into the constructor.
		 */
		protected String filename = null;

		/**
		 * Default constructor. Sets filename.
		 * @see #filename
		 */
		public FilenameShower(String f)
		{
			super();
			filename = f;
		}

		/**
		 * Run method call when the thread is started.
		 * The following operations are performed:
		 * <ul>
		 * <li>The address of the machine the Ccs is on is checked against the address of the machine the 
		 * 	CcsGUI is on.
		 * 	If they are different, the <i>copyFile</i> method is invoked to copy the FITS file to the
		 * 	CcsGUI machine.
		 * <li>The <b>ics_gui.fits.show.command</b> property is queried to get the command to show the FITS
		 * 	file. An instance of ExecuteCommand is created and run to run this command. The return value
		 * 	is checked.
		 * </ul>
		 * @see #copyFile
		 * @see CcsGUI#getFilename
		 * @see CcsGUI#getCcsAddress
		 * @see ngat.util.ExecuteCommand
		 */
		public void run()
		{
			String showCommand = null;
			String commandString = null;
			InetAddress icsAddress = null;
			InetAddress ccsGUIAddress = null;
			ExecuteCommand command = null;
			int retval;

		// Check - Is the CcsGUI on the same machine as the Ccs?
			icsAddress = parent.getCcsAddress();
			try
			{
				ccsGUIAddress = InetAddress.getLocalHost();
			}
			catch(UnknownHostException e)
			{
				parent.error("Show Filename Error:Could not find local host:"+e);
				JOptionPane.showMessageDialog((Component)null,
					(Object)("Show Filename Error:Could not find local host:"+e),
					" Show Filename Error ",JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(icsAddress.equals(ccsGUIAddress) == false)
			{
				try
				{
					filename = copyFile(icsAddress,filename);
				}
				catch(Exception e)
				{
					parent.error("Show Filename Error:Copy failed:"+e);
					JOptionPane.showMessageDialog((Component)null,
						(Object)("Show Filename Error:Copy failed:"+e)," Show Filename Error ",
						JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		// Show FITS file
			showCommand = status.getProperty("ics_gui.fits.show.command");
			commandString = StringUtilities.replace(showCommand,"%s",filename);
			command = new ExecuteCommand(commandString);
			parent.log("Executing Command:"+commandString);
			command.run();
			if(command.getException() != null)
			{
				String errorString = null;
	
				errorString = new String("Show Filename Error:Command ("+commandString+") returned:"+
							command.getException());
				parent.error(errorString);
				JOptionPane.showMessageDialog((Component)null,
					(Object)(errorString)," Show Filename Error ",
					JOptionPane.ERROR_MESSAGE);
			}
			retval = command.getExitValue();
			if(retval != 0)
			{
				String errorString = null;

				errorString = new String("Show Filename Error:Command ("+commandString+") returned:"+
							retval+":"+command.getErrorString());
				parent.error(errorString);
				JOptionPane.showMessageDialog((Component)null,
					(Object)(errorString)," Show Filename Error ",
					JOptionPane.ERROR_MESSAGE);
			}
			if(command.getErrorString() != null)
				parent.log("Command returned Error String:"+command.getErrorString());
			parent.log("Command returned Output:"+command.getOutputString());
		}

		/** 
		 * This method copies a file on the machine the Ics is running on, to a file on the machine
		 * the CcsGUI is running on. It assumes the Ics is running a TitServer.
		 * The following processing occurs:
		 * <ul>
		 * <li>The TIT port number is retrieved from the <b>ccs_gui.net.default_TIT_port_number</b> property.
		 * <li>A temporary directory is retrieved from the <b>ics_gui.fits.show.temp_dir</b> property.
		 * 	This exists on the CcsGUI machine and must be terminated by a path seperator.
		 * <li>A destination filename is constructed using the temporary directory and the source filename's
		 * 	name (without it's path component).
		 * <li>A TitClient instance is constructed using the passed in icsAddress and the TIT port number.
		 * <li>The file is retrieved using the TitClient instance, from the srcFilename to the
		 * 	destination filename.
		 * <li>The destination file's <b>deleteOnExit</b> method is called. This means the CcsGUI will delete
		 * 	this temporary copy of the FITS file when it exits.
		 * <li>The destination filename is returned as a string.
		 * </ul>
		 * @param icsAddress The address of the machine the Ics process is running on.
		 * @param srcFilename A string representing the absolute filename of the Ics computer.
		 * @return A string representing the absolute filename of the copied file on the CcsGUI computer
		 * 	is returned.
		 * @exception IOException Thrown if an IO error occurs when transfering the FITS file.
		 * @exception NumberFormatException Thrown if the property containing the TIT port number is not a 
		 * 	valid integer.
		 */
		protected String copyFile(InetAddress icsAddress, String srcFilename) throws IOException,
			NumberFormatException
		{
			TitClient titClient = null;
			File srcFile = null;
			File destFile = null;
			String tempDirName = null;
			int titPortNumber = 0;

			titPortNumber = status.getPropertyInteger("ccs_gui.net.default_TIT_port_number");
			tempDirName = status.getProperty("ics_gui.fits.show.temp_dir");
			if(tempDirName.endsWith(System.getProperty("file.separator")) == false)
				tempDirName = tempDirName.concat(System.getProperty("file.separator"));
			srcFile = new File(srcFilename);
			destFile = new File(tempDirName+srcFile.getName());
			titClient = new TitClient("CcsGUI",icsAddress.getHostAddress(),titPortNumber);
			titClient.request(srcFilename,destFile.toString());
		// warning, this next line means this copied file is deleted when CcsGUI exits
			destFile.deleteOnExit();
			return destFile.toString();
		}
	}// end class

}
//
// $Log: not supported by cvs2svn $
// Revision 1.2  2001/07/12 10:13:56  cjm
// Added seperator checking to temporary directory, so that
// if the property containg the temporary directory does not
// end in a path seperator, one is added.
//
// Revision 1.1  2001/07/10 18:21:28  cjm
// Initial revision
//
//
