// CcsCCDConfigProperties.java -*- mode: Fundamental;-*-
// $Header: /home/cjm/cvs/ics_gui/java/CcsCCDConfigProperties.java,v 0.1 1999-11-25 13:33:39 cjm Exp $
import java.lang.*;
import java.io.*;
import java.util.*;

import ngat.phase2.*;

/**
 * This class holds the CCD Config information used by the CCS GUI. The information is held
 * in a Java proerties file and this class extends java.util.Properties
 * @see java.util.Properties
 * @author Chris Mottram
 * @version $Revision: 0.1 $
 */
public class CcsCCDConfigProperties extends Properties
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: CcsCCDConfigProperties.java,v 0.1 1999-11-25 13:33:39 cjm Exp $");
	/**
	 * Filename for properties file.
	 */
	public final static String PROPERTIES_FILENAME = "./ccs_gui_config.properties";

	/**
	 * Load method for the properties. Calls super.load with a file input stream derived
	 * from the PROPERTIES_FILENAME.
	 * @see #PROPERTIES_FILENAME
	 * @exception IOException Thrown if an erro occurs with the IO.
	 * @exception FileNotFoundException Thrown if the file isn't found.
	 */
	public void load() throws IOException, FileNotFoundException
	{
		FileInputStream fis = null;

		fis = new FileInputStream(PROPERTIES_FILENAME);
		super.load(fis);
		fis.close();
	}

	/**
	 * Save method for the properties. Calls super.save with a file output stream derived from the
	 * PROPERTIES_FILENAME.
	 * @see #PROPERTIES_FILENAME
	 * @exception IOException Thrown if an erro occurs with the IO.
	 */
	public void save() throws IOException
	{
		FileOutputStream fos = null;

		fos = new FileOutputStream(PROPERTIES_FILENAME);
	// jdk 1.1.x
		//super.save(fos,"#\n# CCS GUI CCD CONFIG configuration file\n#\n");
	// jdk 1.2.x
		super.store(fos,"#\n# CCS GUI CCD CONFIG configuration file\n#\n");
		fos.close();
	}

	/**
	 * Method to get the number of configurations in the list.
	 * @return The number of configurations.
	 */
	public int getConfigurationCount()
	{
		String s = null;
		int i;

		i = 0;
		while(getProperty(configIdString(i)+"name") != null)
			i++;
		return i;
	}

	/**
	 * Method to return a CCDConfig, constructed from the information against id id.
	 * @param id The Id number.
	 * @return The constructed CCDConfig.
	 */
	public CCDConfig getCCDConfigById(int id) throws NumberFormatException
	{
		CCDConfig c = null;

		c = new CCDConfig(getConfigName(id));
		c.setLowerFilterWheel(getConfigLowerFilterWheel(id));
		c.setUpperFilterWheel(getConfigUpperFilterWheel(id));
		c.setXbin(getConfigXBin(id));
		c.setYbin(getConfigYBin(id));

		c.setXs1(getConfigXStart(id,1));
		c.setYs1(getConfigYStart(id,1));
		c.setXe1(getConfigXEnd(id,1));
		c.setYe1(getConfigYEnd(id,1));

		c.setXs2(getConfigXStart(id,2));
		c.setYs2(getConfigYStart(id,2));
		c.setXe2(getConfigXEnd(id,2));
		c.setYe2(getConfigYEnd(id,2));

		c.setXs3(getConfigXStart(id,3));
		c.setYs3(getConfigYStart(id,3));
		c.setXe3(getConfigXEnd(id,3));
		c.setYe3(getConfigYEnd(id,3));

		c.setXs4(getConfigXStart(id,4));
		c.setYs4(getConfigYStart(id,4));
		c.setXe4(getConfigXEnd(id,4));
		c.setYe4(getConfigYEnd(id,4));
		return c;
	}

	/**
	 * Method to get the id of the configuration of name n. Note if two configuration's have the 
	 * same name, this will return the one with the smallest id.
	 * @param n The name string of the configuration.
	 * @return Returns the id of the configuration, if it is found.
	 * @exception IllegalArgumentException Thrown if the name is not a valid configuration name.
	 */
	public int getIdFromName(String n) throws IllegalArgumentException
	{
		String test = null;
		int id;

		id = 0;
		while((test = getConfigName(id)) != null)
		{
			if(test.equals(n))
				return id;
			id++;
		}
		throw new IllegalArgumentException("Configuration name '"+n+"' not found.");
	}

	/**
	 * Method to get the name of configuration id id.
	 * @param id The id of the configuration.
	 * @return The configuration name.
	 */
	public String getConfigName(int id)
	{
		return getProperty(configIdString(id)+"name");
	}

	/**
	 * Method to set the name of configuration id id.
	 * @param id The id of the configuration.
	 * @param n The configuration name.
	 */
	public void setConfigName(int id,String n)
	{
	// jdk 1.1.x
		//put(configIdString(id)+"name",n);
	// jdk 1.2.x
		setProperty(configIdString(id)+"name",n);
	}

	/**
	 * Method to get the lower filter wheel string of configuration id id.
	 * @param id The id of the configuration.
	 * @return The configuration lower filter wheel string.
	 */
	public String getConfigLowerFilterWheel(int id)
	{
		return getProperty(configIdString(id)+"lowerFilterWheel");
	}

	/**
	 * Method to set the lower filter wheel string of configuration id id.
	 * @param id The id of the configuration.
	 * @param s The configuration lower filter wheel string.
	 */
	public void setConfigLowerFilterWheel(int id,String s)
	{
	// jdk 1.1.x
		//put(configIdString(id)+"lowerFilterWheel",s);
	// jdk 1.2.x
		setProperty(configIdString(id)+"lowerFilterWheel",s);
	}

	/**
	 * Method to get the upper filter wheel string of configuration id id.
	 * @param id The id of the configuration.
	 * @return The configuration upper filter wheel string.
	 */
	public String getConfigUpperFilterWheel(int id)
	{
		return getProperty(configIdString(id)+"upperFilterWheel");
	}

	/**
	 * Method to set the upper filter wheel string of configuration id id.
	 * @param id The id of the configuration.
	 * @param s The configuration upper filter wheel string.
	 */
	public void setConfigUpperFilterWheel(int id,String s)
	{
	// jdk 1.1.x
		//put(configIdString(id)+"upperFilterWheel",s);
	// jdk 1.2.x
		setProperty(configIdString(id)+"upperFilterWheel",s);
	}

	/**
	 * Method to get the X binning of configuration id id.
	 * @param id The id of the configuration.
	 * @return The configuration X binning number.
	 * @exception NumberFormatException Thrown if the relevant property 
	 * 	"ccs_gui_config."id".xBin" does not contain a numeric value.
	 */
	public int getConfigXBin(int id) throws NumberFormatException
	{
		return getPropertyInteger(configIdString(id)+"xBin");
	}

	/**
	 * Method to set the X binning of configuration id id.
	 * @param id The id of the configuration.
	 * @param xBin The configuration X binning number.
	 */
	public void setConfigXBin(int id,int xBin)
	{
	// jdk 1.1.x
		//put(configIdString(id)+"xBin",Integer.toString(xBin));
	// jdk 1.2.x
		setProperty(configIdString(id)+"xBin",Integer.toString(xBin));
	}

	/**
	 * Method to get the Y binning of configuration id id.
	 * @param id The id of the configuration.
	 * @return The configuration Y binning number.
	 * @exception NumberFormatException Thrown if the relevant property 
	 * 	"ccs_gui_config."id".yBin" does not contain a numeric value.
	 */
	public int getConfigYBin(int id) throws NumberFormatException
	{
		return getPropertyInteger(configIdString(id)+"yBin");
	}

	/**
	 * Method to set the Y binning of configuration id id.
	 * @param id The id of the configuration.
	 * @param yBin The configuration Y binning number.
	 */
	public void setConfigYBin(int id,int yBin)
	{
	// jdk 1.1.x
		//put(configIdString(id)+"yBin",Integer.toString(yBin));
	// jdk 1.2.x
		setProperty(configIdString(id)+"yBin",Integer.toString(yBin));
	}

	/**
	 * Method to get the X start offset of the specified configuration and window number.
	 * @param id The id of the configuration.
	 * @param windowNumber The number of the window to get the start offset for. This should be
	 * 	between 1 and 4 inclusive.
	 * @return The X start offset.
	 * @exception NumberFormatException Thrown if the relevant property 
	 * 	"ccs_gui_config."id".window"windowNumber".xStart" does not contain a numeric value.
	 */
	public int getConfigXStart(int id,int windowNumber) throws NumberFormatException
	{
		return getPropertyInteger(configIdWindowString(id,windowNumber)+"xStart");
	}

	/**
	 * Method to set the X start offset of the specified configuration and window number.
	 * @param id The id of the configuration.
	 * @param windowNumber The number of the window to get the start offset for. This should be
	 * 	between 1 and 4 inclusive.
	 * @param xStart The X start offset.
	 */
	public void setConfigXStart(int id,int windowNumber,int xStart)
	{
	// jdk 1.1.x
		//put(configIdWindowString(id,windowNumber)+"xStart",Integer.toString(xStart));
	// jdk 1.2.x
		setProperty(configIdWindowString(id,windowNumber)+"xStart",Integer.toString(xStart));
	}

	/**
	 * Method to get the Y start offset of the specified configuration and window number.
	 * @param id The id of the configuration.
	 * @param windowNumber The number of the window to get the start offset for. This should be
	 * 	between 1 and 4 inclusive.
	 * @return The Y start offset.
	 * @exception NumberFormatException Thrown if the relevant property 
	 * 	"ccs_gui_config."id".window"windowNumber".yStart" does not contain a numeric value.
	 */
	public int getConfigYStart(int id,int windowNumber) throws NumberFormatException
	{
		return getPropertyInteger(configIdWindowString(id,1)+"yStart");
	}

	/**
	 * Method to set the Y start offset of the specified configuration and window number.
	 * @param id The id of the configuration.
	 * @param windowNumber The number of the window to get the start offset for. This should be
	 * 	between 1 and 4 inclusive.
	 * @param yStart The Y start offset.
	 */
	public void setConfigYStart(int id,int windowNumber,int yStart)
	{
	// jdk 1.1.x
		//put(configIdWindowString(id,windowNumber)+"yStart",Integer.toString(yStart));
	// jdk 1.2.x
		setProperty(configIdWindowString(id,windowNumber)+"yStart",Integer.toString(yStart));
	}

	/**
	 * Method to get the X end offset of the specified configuration and window number.
	 * @param id The id of the configuration.
	 * @param windowNumber The number of the window to get the end offset for. This should be
	 * 	between 1 and 4 inclusive.
	 * @return The X end offset.
	 * @exception NumberFormatException Thrown if the relevant property 
	 * 	"ccs_gui_config."id".window"windowNumber".xEnd" does not contain a numeric value.
	 */
	public int getConfigXEnd(int id,int windowNumber) throws NumberFormatException
	{
		return getPropertyInteger(configIdWindowString(id,1)+"xEnd");
	}

	/**
	 * Method to set the X end offset of the specified configuration and window number.
	 * @param id The id of the configuration.
	 * @param windowNumber The number of the window to get the end offset for. This should be
	 * 	between 1 and 4 inclusive.
	 * @param xEnd The X end offset.
	 */
	public void setConfigXEnd(int id,int windowNumber,int xEnd)
	{
	// jdk 1.1.x
		//put(configIdWindowString(id,windowNumber)+"xEnd",Integer.toString(xEnd));
	// jdk 1.2.x
		setProperty(configIdWindowString(id,windowNumber)+"xEnd",Integer.toString(xEnd));
	}

	/**
	 * Method to get the Y end offset of the specified configuration and window number.
	 * @param id The id of the configuration.
	 * @param windowNumber The number of the window to get the end offset for. This should be
	 * 	between 1 and 4 inclusive.
	 * @return The Y end offset.
	 * @exception NumberFormatException Thrown if the relevant property 
	 * 	"ccs_gui_config."id".window"windowNumber".yEnd" does not contain a numeric value.
	 */
	public int getConfigYEnd(int id,int windowNumber) throws NumberFormatException
	{
		return getPropertyInteger(configIdWindowString(id,1)+"yEnd");
	}

	/**
	 * Method to set the Y end offset of the specified configuration and window number.
	 * @param id The id of the configuration.
	 * @param windowNumber The number of the window to get the end offset for. This should be
	 * 	between 1 and 4 inclusive.
	 * @param yEnd The Y end offset.
	 */
	public void setConfigYEnd(int id,int windowNumber,int yEnd)
	{
	// jdk 1.1.x
		//put(configIdWindowString(id,windowNumber)+"yEnd",Integer.toString(yEnd));
	// jdk 1.2.x
		setProperty(configIdWindowString(id,windowNumber)+"yEnd",Integer.toString(yEnd));
	}

	/**
	 * Method to return a partial key string for a configuration of a particular id.
	 * @param id The Id of the required configuration.
	 * @return A string suitable for use as a partial key in the hashtable.
	 * 	e.g. "ccs_gui_config.0."
	 */
	private String configIdString(int id)
	{
		return new String("ccs_gui_config."+id+".");
	}

	/**
	 * Method to return a partial key string for a configuration of a particular id, and a particular window.
	 * @param id The Id of the required configuration.
	 * @param windowNumber The number of the window.
	 * @return A string suitable for use as a partial key in the hashtable.
	 * 	e.g. "ccs_gui_config.0.window1."
	 */
	private String configIdWindowString(int id,int windowNumber)
	{
		return new String(configIdString(id)+".window"+windowNumber+".");
	}

	/**
	 * Routine to get a properties value, given a key. The value must be a valid integer, else a 
	 * NumberFormatException is thrown.
	 * @param p The property key we want the value for.
	 * @return The properties value, as an integer.
	 * @exception NumberFormatException If the properties value string is not a valid integer, this
	 * 	exception will be thrown when the Integer.parseInt routine is called.
	 */
	private int getPropertyInteger(String p) throws NumberFormatException
	{
		String valueString = null;
		int returnValue = 0;

		valueString = getProperty(p);
		returnValue = Integer.parseInt(valueString);
		return returnValue;
	}
}
//
// $Log: not supported by cvs2svn $
//
