// CcsGUIStatus.java -*- mode: Fundamental;-*-
// $Header: /home/cjm/cvs/ics_gui/java/IcsGUIStatus.java,v 0.1 1999-11-22 09:53:49 cjm Exp $
import java.lang.*;
import java.io.*;
import java.util.*;

/**
 * This class holds status information for the CcsGUI program.
 * @author Chris Mottram
 * @version $Revision: 0.1 $
 */
public class CcsGUIStatus
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: IcsGUIStatus.java,v 0.1 1999-11-22 09:53:49 cjm Exp $");
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
	 * List of client threads running.
	 */
	private Vector clientThreadList = null;

	/**
	 * Constructor. Creates the clientThreadList.
	 * @see #clientThreadList
	 */
	public CcsGUIStatus()
	{
		super();
		clientThreadList = new Vector();
	}

	/**
	 * The load method for the class. This loads the property file from disc.
	 * @see #properties
	 */
	public void load() throws FileNotFoundException,IOException
	{
		FileInputStream fileInputStream = null;
		
		properties = new Properties();
		fileInputStream = new FileInputStream(PROPERTY_FILE_NAME);
		properties.load(fileInputStream);
		fileInputStream.close();
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
//
