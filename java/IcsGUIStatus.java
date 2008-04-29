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
// CcsGUIStatus.java
// $Header: /home/cjm/cvs/ics_gui/java/IcsGUIStatus.java,v 0.7 2008-04-29 11:03:33 cjm Exp $
import java.lang.*;
import java.io.*;
import java.util.*;

import ngat.sound.*;

/**
 * This class holds status information for the CcsGUI program.
 * @author Chris Mottram
 * @version $Revision: 0.7 $
 */
public class CcsGUIStatus
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: IcsGUIStatus.java,v 0.7 2008-04-29 11:03:33 cjm Exp $");
	/**
	 * File name containing properties for ccs gui.
	 */
	private final static String PROPERTY_FILE_NAME = new String("./ccs_gui.properties");
	/**
	 * The logging level.
	 */
	private int logLevel = 0;//CcsConstants.CCS_LOG_LEVEL_NONE;
	/**
	 * A list of properties held in the properties file. This contains configuration information in ccs_gui
	 * that needs to be changed irregularily.
	 */
	private Properties properties = null;
	/**
	 * A list of Instrument Configurations held in a properties file. These properties are used for selecting and 
	 * filling Instrument Configurations.
	 */
	private IcsGUIConfigProperties configProperties = null;
	/**
	 * List of client threads running.
	 */
	private Vector clientThreadList = null;

	/**
	 * Constructor. Creates the clientThreadList. Constrcuts the properties and configProperties
	 * @see #clientThreadList
	 * @see #properties
	 * @see #configProperties
	 */
	public CcsGUIStatus()
	{
		super();
		clientThreadList = new Vector();
		properties = new Properties();
		configProperties = new IcsGUIConfigProperties();
	}

	/**
	 * The load method for the class. This loads the property file from disc, using the default filename.
	 * @see #load(java.lang.String)
	 * @see #PROPERTY_FILE_NAME
	 */
	public void load() throws FileNotFoundException,IOException
	{
		load(PROPERTY_FILE_NAME);
	}

	/**
	 * The load method for the class. This loads the property file from disc, from the specified filename.
	 * @param filename The filename of a valid properties file to load.
	 * @see #properties
	 */
	public void load(String filename) throws FileNotFoundException,IOException
	{
		FileInputStream fileInputStream = null;

		fileInputStream = new FileInputStream(filename);
		properties.load(fileInputStream);
		fileInputStream.close();
	}

	/**
	 * Method to load Instrument Configurations from a non-standard property filename.
	 * Calls configProperties.setPropertiesFilename to set the filename.
	 * Calls configProperties.load to load the properties filename.
	 * @see #configProperties
	 * @see IcsGUIConfigProperties#setPropertiesFilename
	 * @see IcsGUIConfigProperties#load
	 */
	public void loadInstrumentConfig(String filename) throws FileNotFoundException,IOException
	{
		configProperties.setPropertiesFilename(filename);
		configProperties.load();
	}

	/**
	 * Method to load Instrument Configurations.
	 * Calls configProperties.load.
	 * @see #configProperties
	 * @see IcsGUIConfigProperties#load
	 */
	public void loadInstrumentConfig() throws FileNotFoundException,IOException
	{
		configProperties.load();
	}

	/**
	 * Method to save Instrument Configurations.
	 * Calls configProperties.load.
	 * @see #configProperties
	 * @see IcsGUIConfigProperties#save
	 */
	public void saveInstrumentConfig() throws IOException
	{
		configProperties.save();
	}

	/**
	 * Set the logging level for Ccs GUI.
	 * @param level The level of logging.
	 */
	public synchronized void setLogLevel(int level)
	{
		logLevel = level;
	}

	/**
	 * Get the logging level for Ccs GUI.
	 * @return The current log level.
	 */	
	public synchronized int getLogLevel()
	{
		return logLevel;
	}

	/**
	 * Add a client thread to the list.
	 * @param thread The thread to add.
	 */
	public synchronized void addClientThread(CcsGUIClientConnectionThread thread)
	{
		clientThreadList.addElement(thread);
	}

	/**
	 * Get the thread at index index in the list
	 * @param index The index in the list.
	 */
	public synchronized CcsGUIClientConnectionThread clientThreadAt(int index)
	{
		return (CcsGUIClientConnectionThread)clientThreadList.elementAt(index);
	}

	/**
	 * Delete a client thread from the list.
	 * @param thread The thread to add.
	 */
	public synchronized void removeClientThread(CcsGUIClientConnectionThread thread)
	{
		clientThreadList.removeElement(thread);
	}

	/**
	 * Method to get the number of client threads in the list.
	 */
	public synchronized int clientThreadListCount()
	{
		return clientThreadList.size();
	}

	/**
	 * Get the Instrument Configuration Property list.
	 * @return The properties.
	 * @see #configProperties
	 */
	public IcsGUIConfigProperties getInstrumentConfigProperties()
	{
		return configProperties;
	}

	/**
	 * Play a sample specified by the property.
	 * @param audioThread The audio thread to play the sample with.
	 * @param propertyName The name of a property key. The value contains a sample "name",
	 *      or a list of names is available from the property key (&lt;property key&gt;.&lt;N&gt),
	 *      and a random name is selected.
	 */
	public void play(SoundThread audioThread,String propertyName)
	{
		Random random = null;
		List sampleNameList = null;
		String sampleName = null;
		int index;
		boolean done;

		sampleName = getProperty(propertyName);
		// if default property value does not exist, see if there is a list to select from.
		if(sampleName == null)
		{
			sampleNameList = new Vector();
			index = 0;
			done = false;
			while(done == false)
			{
				sampleName = getProperty(propertyName+"."+index);
				if(sampleName != null)
					sampleNameList.add(sampleName);
				index++;
				done = (sampleName == null);
			}
			if(sampleNameList.size() > 0)
			{
				random = new Random();
				index = random.nextInt(sampleNameList.size());
				sampleName = (String)(sampleNameList.get(index));
			}
		}
		audioThread.play(sampleName);
	}

	/**
	 * Routine to get a properties value, given a key. Just calls the properties object getProperty routine.
	 * @param p The property key we want the value for.
	 * @return The properties value, as a string object.
	 * @see #properties
	 */
	public String getProperty(String p)
	{
		return properties.getProperty(p);
	}

	/**
	 * Routine to get a properties value, given a key. The value must be a valid integer, else a 
	 * NumberFormatException is thrown.
	 * @param p The property key we want the value for.
	 * @return The properties value, as an integer.
	 * @exception NumberFormatException If the properties value string is not a valid integer, this
	 * 	exception will be thrown when the Integer.parseInt routine is called.
	 * @see #properties
	 */
	public int getPropertyInteger(String p) throws NumberFormatException
	{
		String valueString = null;
		int returnValue = 0;

		valueString = properties.getProperty(p);
		returnValue = Integer.parseInt(valueString);
		return returnValue;
	}

	/**
	 * Routine to get a properties value, given a key. The value must be a valid double, else a 
	 * NumberFormatException is thrown.
	 * @param p The property key we want the value for.
	 * @return The properties value, as an double.
	 * @exception NumberFormatException If the properties value string is not a valid double, this
	 * 	exception will be thrown when the Double.valueOf routine is called.
	 * @see #properties
	 */
	public double getPropertyDouble(String p) throws NumberFormatException
	{
		String valueString = null;
		Double returnValue = null;

		valueString = properties.getProperty(p);
		returnValue = Double.valueOf(valueString);
		return returnValue.doubleValue();
	}

	/**
	 * Routine to get a properties boolean value, given a key. The properties value should be either 
	 * "true" or "false".
	 * Boolean.valueOf is used to convert the string to a boolean value.
	 * @param p The property key we want the boolean value for.
	 * @return The properties value, as an boolean.
	 * @see #properties
	 */
	public boolean getPropertyBoolean(String p)
	{
		String valueString = null;
		Boolean b = null;

		valueString = properties.getProperty(p);
		b = Boolean.valueOf(valueString);
		return b.booleanValue();
	}
}
//
// $Log: not supported by cvs2svn $
// Revision 0.6  2006/05/16 17:12:17  cjm
// gnuify: Added GNU General Public License.
//
// Revision 0.5  2004/06/15 19:16:03  cjm
// Added play method for better audio feedback.
//
// Revision 0.4  2003/06/06 15:44:31  cjm
// Changes on which property filename is loaded.
//
// Revision 0.3  2000/11/30 18:47:44  cjm
// Made generic for other instruments.
//
// Revision 0.2  1999/12/09 17:02:12  cjm
// More functionality added.
//
// Revision 0.1  1999/11/22 09:53:49  cjm
// initial revision.
//
//
