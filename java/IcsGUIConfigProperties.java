// IcsGUIConfigProperties.java
// $Header: /home/cjm/cvs/ics_gui/java/IcsGUIConfigProperties.java,v 0.9 2003-11-14 15:02:12 cjm Exp $
import java.lang.*;
import java.io.*;
import java.util.*;

import ngat.phase2.*;

/**
 * This class holds the Instrument Configuration information used by the CCS GUI. The information is held
 * in a Java properties file and this class extends java.util.Properties
 * @see java.util.Properties
 * @author Chris Mottram
 * @version $Revision: 0.9 $
 */
public class IcsGUIConfigProperties extends Properties
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: IcsGUIConfigProperties.java,v 0.9 2003-11-14 15:02:12 cjm Exp $");
	/**
	 * Configuration type specifier:CCD (RATCam).
	 */
	public final static int CONFIG_TYPE_CCD_RATCAM 		= 0;
	/**
	 * Configuration type specifier:Spectrograph (MES).
	 */
	public final static int CONFIG_TYPE_SPECTROGRAPH_MES 	= 1;
	/**
	 * Configuration type specifier:Spectrograph (NuView).
	 */
	public final static int CONFIG_TYPE_SPECTROGRAPH_NUVIEW = 2;
	/**
	 * Configuration type specifier:Infra-Red camera (SupIRCam).
	 */
	public final static int CONFIG_TYPE_INFRA_RED_SUPIRCAM 	= 3;
	/**
	 * Configuration type specifier:Spectrograph (FTSpec).
	 */
	public final static int CONFIG_TYPE_SPECTROGRAPH_FTSPEC = 4;
	/**
	 * List of legal values that can be held in the config type field.
	 * @see #CONFIG_TYPE_CCD_RATCAM
	 * @see #CONFIG_TYPE_SPECTROGRAPH_MES
	 * @see #CONFIG_TYPE_SPECTROGRAPH_NUVIEW
	 * @see #CONFIG_TYPE_INFRA_RED_SUPIRCAM
	 * @see #CONFIG_TYPE_SPECTROGRAPH_FTSPEC
	 */
	public final static int CONFIG_TYPE_LIST[] = {CONFIG_TYPE_CCD_RATCAM,CONFIG_TYPE_SPECTROGRAPH_MES,
		CONFIG_TYPE_SPECTROGRAPH_NUVIEW,CONFIG_TYPE_INFRA_RED_SUPIRCAM,CONFIG_TYPE_SPECTROGRAPH_FTSPEC};
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
		//super.save(fos,"#\n# ICS GUI Instrument CONFIG configuration file\n#\n");
	// jdk 1.2.x
		super.store(fos,"#\n# ICS GUI Instrument CONFIG configuration file\n#\n");
		fos.close();
	}

	/**
	 * Method to get a list of Instrument Configuration names.
	 * @return A Vector, each element is a String with a Instrument Configuration Name.
	 */
	public Vector getConfigNameList()
	{
		Vector list = null;
		String s = null;
		int i;

		list = new Vector();
		i = 0;
		while((s = getConfigName(i)) != null)
		{
			list.add(s);
			i++;
		}
		return list;
	}

	/**
	 * Method to get a list of Configuration names, filtered by a type.
	 * @param type The type of configuration to filter the returned list by. See CONFIG_TYPE_LIST for a list
	 * 	of valid types.
	 * @return A List, each element is a String with a Configuration Name of the specified type.
	 * @see #getConfigName
	 * @see #getConfigType
	 * @see #CONFIG_TYPE_LIST
	 */
	public Vector getConfigNameList(int type)
	{
		Vector list = null;
		String s = null;
		int i;

		list = new Vector();
		i = 0;
		while((s = getConfigName(i)) != null)
		{
			if(getConfigType(i) == type)
				list.add(s);
			i++;
		}
		return list;
	}

	/**
	 * Method to get a new Id to insert a new configuration with. This is done by
	 * getting config names with ids until one fails.
	 * @see #getConfigName
	 */
	public int getNewConfigId()
	{
		String s = null;
		int i;

		i = 0;
		while((s = getConfigName(i)) != null)
			i++;
		return i;
	}

	/**
	 * Method to return a InstrumentConfig, constructed from the information against id id.
	 * @param id The Id number.
	 * @return The constructed InstrumentConfig.
	 * @exception NumberFormatException Thrown if a numeric parameter is not returned from the properties
	 * 	file as a legal number.
	 * @exception IllegalArgumentException Thrown if the config id specified does not have a legal type.
	 * @see #getConfigType
	 * @see #getCCDConfigById
	 * @see #getMESConfigById
	 * @see #getNuViewConfigById
	 * @see #getIRCamConfigById
	 * @see #getFTSpecConfigById
	 */
	public InstrumentConfig getConfigById(int id) throws NumberFormatException, IllegalArgumentException
	{
		InstrumentConfig c = null;
		int type;

	// check type
		type = getConfigType(id);
		switch(type)
		{
			case CONFIG_TYPE_CCD_RATCAM:
				c = getCCDConfigById(id);
				break;
			case CONFIG_TYPE_SPECTROGRAPH_MES:
				c = getMESConfigById(id);
				break;
			case CONFIG_TYPE_SPECTROGRAPH_NUVIEW:
				c = getNuViewConfigById(id);
				break;
			case CONFIG_TYPE_INFRA_RED_SUPIRCAM:
				c = getIRCamConfigById(id);
				break;
			case CONFIG_TYPE_SPECTROGRAPH_FTSPEC:
				c = getFTSpecConfigById(id);
				break;
			default:
				throw new IllegalArgumentException(this.getClass().getName()+":getConfigById:Id "
					+id+" type "+type+" not a supported type of configuration.");
		}
	// return config
		return (InstrumentConfig)c;
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
	 * Method to delete an id. This involves removing all the elements of the id, and them moving down
	 * things with greater ids to the id numbers are still contiguous.
	 * @param id The id of the configuration to remove.
	 */
	public void deleteId(int id)
	{
		String s = null;
		int i,j,type;

	// what type is this id
		type = getConfigType(id);
	// remove id id
		remove(configIdStringName(id));
		remove(configIdStringType(id));
		remove(configIdStringCalibrateBefore(id));
		remove(configIdStringCalibrateAfter(id));
		switch(type)
		{
			case CONFIG_TYPE_CCD_RATCAM:
				remove(configIdStringLowerFilterWheel(id));
				remove(configIdStringUpperFilterWheel(id));
				remove(configIdStringXBin(id));
				remove(configIdStringYBin(id));
				remove(configIdStringWindowFlags(id));
				for(j=1;j<5;j++)
				{
					remove(configIdWindowStringXStart(id,j));
					remove(configIdWindowStringYStart(id,j));
					remove(configIdWindowStringXEnd(id,j));
					remove(configIdWindowStringYEnd(id,j));
				}
				break;
			case CONFIG_TYPE_SPECTROGRAPH_MES:
				remove(configIdStringXBin(id));
				remove(configIdStringYBin(id));
				remove(configIdStringFilterSlideName(id));
				break;
			case CONFIG_TYPE_SPECTROGRAPH_NUVIEW:
				remove(configIdStringXBin(id));
				remove(configIdStringYBin(id));
				remove(configIdStringWavelength(id));
				break;
			case CONFIG_TYPE_INFRA_RED_SUPIRCAM:
				remove(configIdStringFilterWheel(id));
				break;
			case CONFIG_TYPE_SPECTROGRAPH_FTSPEC:
				remove(configIdStringXBin(id));
				remove(configIdStringYBin(id));
				break;
			default:
				throw new IllegalArgumentException(this.getClass().getName()+":deleteId:Id "
					+id+" type "+type+" not a supported type of configuration.");
		}
	// move all ids after id down one
		i = id+1;
		while((s = getConfigName(i)) != null)
		{
		// what type is this id
			type = getConfigType(i);

			setConfigName(i-1,getConfigName(i));
			remove(configIdStringName(i));
			setConfigType(i-1,getConfigType(i));
			remove(configIdStringType(i));
			setConfigCalibrateBefore(i-1,getConfigCalibrateBefore(i));
			remove(configIdStringCalibrateBefore(i));
			setConfigCalibrateAfter(i-1,getConfigCalibrateAfter(i));
			remove(configIdStringCalibrateAfter(i));
			switch(type)
			{
				case CONFIG_TYPE_CCD_RATCAM:
					setConfigLowerFilterWheel(i-1,getConfigLowerFilterWheel(i));
					remove(configIdStringLowerFilterWheel(i));
					setConfigUpperFilterWheel(i-1,getConfigUpperFilterWheel(i));
					remove(configIdStringUpperFilterWheel(i));
					setConfigXBin(i-1,getConfigXBin(i));
					remove(configIdStringXBin(i));
					setConfigYBin(i-1,getConfigYBin(i));
					remove(configIdStringYBin(i));
					setConfigWindowFlags(i-1,getConfigWindowFlags(i));
					remove(configIdStringWindowFlags(i));

					for(j=1;j<5;j++)
					{
						setConfigXStart(i-1,j,getConfigXStart(i,j));
						remove(configIdWindowStringXStart(i,j));
						setConfigYStart(i-1,j,getConfigYStart(i,j));
						remove(configIdWindowStringYStart(i,j));
						setConfigXEnd(i-1,j,getConfigXEnd(i,j));
						remove(configIdWindowStringXEnd(i,j));
						setConfigYEnd(i-1,j,getConfigYEnd(i,j));
						remove(configIdWindowStringYEnd(i,j));
					}
					break;
			case CONFIG_TYPE_SPECTROGRAPH_MES:
					setConfigXBin(i-1,getConfigXBin(i));
					remove(configIdStringXBin(i));
					setConfigYBin(i-1,getConfigYBin(i));
					remove(configIdStringYBin(i));
					setConfigFilterSlideName(i-1,getConfigFilterSlideName(i));
					remove(configIdStringFilterSlideName(i));
					break;
			case CONFIG_TYPE_SPECTROGRAPH_NUVIEW:
					setConfigXBin(i-1,getConfigXBin(i));
					remove(configIdStringXBin(i));
					setConfigYBin(i-1,getConfigYBin(i));
					remove(configIdStringYBin(i));
					setConfigWavelength(i-1,getConfigWavelength(i));
					remove(configIdStringWavelength(i));
					break;
			case CONFIG_TYPE_INFRA_RED_SUPIRCAM:
					setConfigFilterWheel(i-1,getConfigFilterWheel(i));
					remove(configIdStringFilterWheel(i));
					break;
			case CONFIG_TYPE_SPECTROGRAPH_FTSPEC:
					setConfigXBin(i-1,getConfigXBin(i));
					remove(configIdStringXBin(i));
					setConfigYBin(i-1,getConfigYBin(i));
					remove(configIdStringYBin(i));
					break;
			default:
				throw new IllegalArgumentException(this.getClass().getName()+":deleteId:Id "
					+i+" type "+type+" not a supported type of configuration.");
			}
			i++;
		}// while ids exist
	}

// set and get methods for each configuration field
	/**
	 * Method to get the name of configuration id id.
	 * @param id The id of the configuration.
	 * @return The configuration name.
	 */
	public String getConfigName(int id)
	{
		return getProperty(configIdStringName(id));
	}

	/**
	 * Method to set the name of configuration id id.
	 * @param id The id of the configuration.
	 * @param n The configuration name.
	 */
	public void setConfigName(int id,String n)
	{
	// jdk 1.1.x
		//put(configIdStringName(id),n);
	// jdk 1.2.x
		setProperty(configIdStringName(id),n);
	}

	/**
	 * Method to get the configuration type of configuration id id.
	 * @param id The id of the configuration.
	 * @return The configuration type.
	 * @exception NumberFormatException Thrown if the relevant property 
	 * 	"ccs_gui_config."id".type" does not contain a numeric value.
	 * @exception IllegalArgumentException Thrown if checkConfigType thinks the the type number is illegal.
	 * @see #checkConfigType
	 */
	public int getConfigType(int id) throws NumberFormatException, IllegalArgumentException
	{
		int type;

	// retrieve type number.
		type = getPropertyInteger(configIdStringType(id));
	// is the type number legal?
		checkConfigType(type);
	// return type number
		return type;
	}

	/**
	 * Method to set the type of configuration id id.
	 * @param type The type of the configuration.
	 * @param n The configuration name.
	 * @exception IllegalArgumentException Thrown if checkConfigType thinks the the type number is illegal.
	 * @see #checkConfigType
	 */
	public void setConfigType(int id,int type) throws IllegalArgumentException
	{
	// check against legal type
		checkConfigType(type);
	// jdk 1.1.x
		//put(configIdStringType(id),Integer.toString(type));
	// jdk 1.2.x
		setProperty(configIdStringType(id),Integer.toString(type));
	}

	/**
	 * Method to get the calibrate before flag of configuration id id.
	 * @param id The id of the configuration.
	 * @return The configuration calibrate before flag.
	 */
	public boolean getConfigCalibrateBefore(int id)
	{
		return getPropertyBoolean(configIdStringCalibrateBefore(id));
	}

	/**
	 * Method to set the calibrate before flag of configuration id id.
	 * @param id The id of the configuration.
	 * @param cb The configuration calibrate before flag.
	 */
	public void setConfigCalibrateBefore(int id,boolean cb)
	{
	// jdk 1.1.x
		//if(cb)
		//	put(configIdStringCalibrateBefore(id),"true");
		//else
		//	put(configIdStringCalibrateBefore(id),"false");
	// jdk 1.2.x
		if(cb)
			setProperty(configIdStringCalibrateBefore(id),"true");
		else
			setProperty(configIdStringCalibrateBefore(id),"false");
	}

	/**
	 * Method to get the calibrate after flag of configuration id id.
	 * @param id The id of the configuration.
	 * @return The configuration calibrate after flag.
	 */
	public boolean getConfigCalibrateAfter(int id)
	{
		return getPropertyBoolean(configIdStringCalibrateAfter(id));
	}

	/**
	 * Method to set the calibrate after flag of configuration id id.
	 * @param id The id of the configuration.
	 * @param cb The configuration calibrate after flag.
	 */
	public void setConfigCalibrateAfter(int id,boolean cb)
	{
	// jdk 1.1.x
		//if(cb)
		//	put(configIdStringCalibrateAfter(id),"true");
		//else
		//	put(configIdStringCalibrateAfter(id),"false");
	// jdk 1.2.x
		if(cb)
			setProperty(configIdStringCalibrateAfter(id),"true");
		else
			setProperty(configIdStringCalibrateAfter(id),"false");
	}

	/**
	 * Method to get the lower filter wheel string of configuration id id.
	 * @param id The id of the configuration.
	 * @return The configuration lower filter wheel string.
	 */
	public String getConfigLowerFilterWheel(int id)
	{
		return getProperty(configIdStringLowerFilterWheel(id));
	}

	/**
	 * Method to set the lower filter wheel string of configuration id id.
	 * @param id The id of the configuration.
	 * @param s The configuration lower filter wheel string.
	 */
	public void setConfigLowerFilterWheel(int id,String s)
	{
	// jdk 1.1.x
		//put(configIdStringLowerFilterWheel(id),s);
	// jdk 1.2.x
		setProperty(configIdStringLowerFilterWheel(id),s);
	}

	/**
	 * Method to get the upper filter wheel string of configuration id id.
	 * @param id The id of the configuration.
	 * @return The configuration upper filter wheel string.
	 */
	public String getConfigUpperFilterWheel(int id)
	{
		return getProperty(configIdStringUpperFilterWheel(id));
	}

	/**
	 * Method to set the upper filter wheel string of configuration id id.
	 * @param id The id of the configuration.
	 * @param s The configuration upper filter wheel string.
	 */
	public void setConfigUpperFilterWheel(int id,String s)
	{
	// jdk 1.1.x
		//put(configIdStringUpperFilterWheel(id),s);
	// jdk 1.2.x
		setProperty(configIdStringUpperFilterWheel(id),s);
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
		return getPropertyInteger(configIdStringXBin(id));
	}

	/**
	 * Method to get the X binning of configuration id id as a string.
	 * @param id The id of the configuration.
	 * @return The configuration X binning number as a string.
	 */
	public String getConfigXBinString(int id)
	{
		return getProperty(configIdStringXBin(id));
	}

	/**
	 * Method to set the X binning of configuration id id.
	 * @param id The id of the configuration.
	 * @param xBin The configuration X binning number.
	 */
	public void setConfigXBin(int id,int xBin)
	{
	// jdk 1.1.x
		//put(configIdStringXBin(id),Integer.toString(xBin));
	// jdk 1.2.x
		setProperty(configIdStringXBin(id),Integer.toString(xBin));
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
		return getPropertyInteger(configIdStringYBin(id));
	}

	/**
	 * Method to get the Y binning of configuration id id as a string.
	 * @param id The id of the configuration.
	 * @return The configuration Y binning number as a string.
	 */
	public String getConfigYBinString(int id)
	{
		return getProperty(configIdStringYBin(id));
	}

	/**
	 * Method to set the Y binning of configuration id id.
	 * @param id The id of the configuration.
	 * @param yBin The configuration Y binning number.
	 */
	public void setConfigYBin(int id,int yBin)
	{
	// jdk 1.1.x
		//put(configIdStringYBin(id),Integer.toString(yBin));
	// jdk 1.2.x
		setProperty(configIdStringYBin(id),Integer.toString(yBin));
	}

	/**
	 * Method to get the filter slide name of configuration id id.
	 * This is a MES only configuration value.
	 * @param id The id of the configuration.
	 * @return The configuration filter slide name.
	 */
	public String getConfigFilterSlideName(int id)
	{
		return getProperty(configIdStringFilterSlideName(id));
	}

	/**
	 * Method to set the filter slide name of configuration id id.
	 * This is a MES only configuration value.
	 * @param id The id of the configuration.
	 * @param s The configuration filter slide name.
	 */
	public void setConfigFilterSlideName(int id,String s)
	{
	// jdk 1.1.x
		//put(configIdStringFilterSlideName(id),s);
	// jdk 1.2.x
		setProperty(configIdStringFilterSlideName(id),s);
	}

	/**
	 * Method to get the wavelength of configuration id id as a string.
	 * This is a NuView only configuration value.
	 * @param id The id of the configuration.
	 * @return The configuration wavelength.
	 * @exception NumberFormatException Thrown if the relevant property 
	 * 	"ccs_gui_config."id".wavelength" does not contain a numeric value.
	 */
	public double getConfigWavelength(int id) throws NumberFormatException
	{
		return getPropertyDouble(configIdStringWavelength(id));
	}

	/**
	 * Method to get the wavelength of configuration id id as a string.
	 * @param id The id of the configuration.
	 * @return The configuration wavelength number as a string.
	 */
	public String getConfigWavelengthString(int id)
	{
		return getProperty(configIdStringWavelength(id));
	}

	/**
	 * Method to set the wavelength of configuration id id.
	 * This is a NuView only configuration value.
	 * @param id The id of the configuration.
	 * @param wavelength The configuration wavelength, in Angstroms.
	 */
	public void setConfigWavelength(int id,double wavelength)
	{
	// jdk 1.1.x
		//put(configIdStringWavelength(id),Double.toString(wavelength));
	// jdk 1.2.x
		setProperty(configIdStringWavelength(id),Double.toString(wavelength));
	}

	/**
	 * Method to get the filter wheel string of configuration id id (INFRA_RED_SUPIRCAM).
	 * @param id The id of the configuration.
	 * @return The configuration filter wheel string.
	 */
	public String getConfigFilterWheel(int id)
	{
		return getProperty(configIdStringFilterWheel(id));
	}

	/**
	 * Method to set the upper filter wheel string of configuration id id (INFRA_RED_SUPIRCAM).
	 * @param id The id of the configuration.
	 * @param s The configuration filter wheel string.
	 */
	public void setConfigFilterWheel(int id,String s)
	{
	// jdk 1.1.x
		//put(configIdStringUpperFilterWheel(id),s);
	// jdk 1.2.x
		setProperty(configIdStringFilterWheel(id),s);
	}

	/**
	 * Method to get the window flags of configuration id id.
	 * @param id The id of the configuration.
	 * @return The configuration window flags number.
	 * @exception NumberFormatException Thrown if the relevant property 
	 * 	"ccs_gui_config."id".windowFlags" does not contain a numeric value.
	 */
	public int getConfigWindowFlags(int id) throws NumberFormatException
	{
		return getPropertyInteger(configIdStringWindowFlags(id));
	}

	/**
	 * Method to get the window flags of configuration id id as a string.
	 * @param id The id of the configuration.
	 * @return The configuration window flags number as a string.
	 */
	public String getConfigWindowFlagsString(int id)
	{
		return getProperty(configIdStringWindowFlags(id));
	}

	/**
	 * Method to set the window flags of configuration id id.
	 * @param id The id of the configuration.
	 * @param windowFlags The configuration window flags number.
	 */
	public void setConfigWindowFlags(int id,int windowFlags)
	{
	// jdk 1.1.x
		//put(configIdStringWindowFlags(id),Integer.toString(windowFlags));
	// jdk 1.2.x
		setProperty(configIdStringWindowFlags(id),Integer.toString(windowFlags));
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
		return getPropertyInteger(configIdWindowStringXStart(id,windowNumber));
	}

	/**
	 * Method to get the X start offset of the specified configuration and window number as a string.
	 * @param id The id of the configuration.
	 * @param windowNumber The number of the window to get the start offset for. This should be
	 * 	between 1 and 4 inclusive.
	 * @return The X start offset as a string.
	 */
	public String getConfigXStartString(int id,int windowNumber)
	{
		return getProperty(configIdWindowStringXStart(id,windowNumber));
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
		//put(configIdWindowStringXStart(id,windowNumber),Integer.toString(xStart));
	// jdk 1.2.x
		setProperty(configIdWindowStringXStart(id,windowNumber),Integer.toString(xStart));
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
		return getPropertyInteger(configIdWindowStringYStart(id,windowNumber));
	}

	/**
	 * Method to get the Y start offset of the specified configuration and window number as a string.
	 * @param id The id of the configuration.
	 * @param windowNumber The number of the window to get the start offset for. This should be
	 * 	between 1 and 4 inclusive.
	 * @return The Y start offset as a string.
	 */
	public String getConfigYStartString(int id,int windowNumber)
	{
		return getProperty(configIdWindowStringYStart(id,windowNumber));
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
		//put(configIdWindowStringYStart(id,windowNumber),Integer.toString(yStart));
	// jdk 1.2.x
		setProperty(configIdWindowStringYStart(id,windowNumber),Integer.toString(yStart));
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
		return getPropertyInteger(configIdWindowStringXEnd(id,windowNumber));
	}

	/**
	 * Method to get the X end offset of the specified configuration and window number as a string.
	 * @param id The id of the configuration.
	 * @param windowNumber The number of the window to get the end offset for. This should be
	 * 	between 1 and 4 inclusive.
	 * @return The X end offset as a string.
	 */
	public String getConfigXEndString(int id,int windowNumber)
	{
		return getProperty(configIdWindowStringXEnd(id,windowNumber));
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
		//put(configIdWindowStringXEnd(id,windowNumber),Integer.toString(xEnd));
	// jdk 1.2.x
		setProperty(configIdWindowStringXEnd(id,windowNumber),Integer.toString(xEnd));
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
		return getPropertyInteger(configIdWindowStringYEnd(id,windowNumber));
	}

	/**
	 * Method to get the Y end offset of the specified configuration and window number as a string.
	 * @param id The id of the configuration.
	 * @param windowNumber The number of the window to get the end offset for. This should be
	 * 	between 1 and 4 inclusive.
	 * @return The Y end offset as a string.
	 */
	public String getConfigYEndString(int id,int windowNumber)
	{
		return getProperty(configIdWindowStringYEnd(id,windowNumber));
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
		//put(configIdWindowStringYEnd(id,windowNumber),Integer.toString(yEnd));
	// jdk 1.2.x
		setProperty(configIdWindowStringYEnd(id,windowNumber),Integer.toString(yEnd));
	}

// methods to create an instance of an instrument config
	/**
	 * Method to return a CCDConfig, constructed from the information against id id.
	 * @param id The Id number.
	 * @return The constructed CCDConfig.
	 * @exception NumberFormatException Thrown if a numeric parameter is not returned from the properties
	 * 	file as a legal number.
	 * @exception IllegalArgumentException Thrown if the config id specified does not have a legal type.
	 */
	private CCDConfig getCCDConfigById(int id) throws NumberFormatException, IllegalArgumentException
	{
		CCDConfig c = null;
		CCDDetector detector = null;
		Window windowArray[];

	// check type
		if(getConfigType(id) != CONFIG_TYPE_CCD_RATCAM)
		{
			throw new IllegalArgumentException(this.getClass().getName()+":getCCDConfigById:Id "
				+id+" not a configuration of type CCD.");
		}
	// construct CCDConfig
		c = new CCDConfig(getConfigName(id));
		c.setLowerFilterWheel(getConfigLowerFilterWheel(id));
		c.setUpperFilterWheel(getConfigUpperFilterWheel(id));
	// setup detector
		detector = new CCDDetector();
		detector.setXBin(getConfigXBin(id));
		detector.setYBin(getConfigYBin(id));
		// note, other Detector fields not set, as they are not used by the instrument.

	// setup window list
		windowArray = new Window[detector.getMaxWindowCount()];
		for(int i = 0; i < detector.getMaxWindowCount(); i++)
		{
		// Note, windows are only non-null if they are active in RCS created configs
		// Lets re-create that effect here, we can use the config window flags.
			if((getConfigWindowFlags(id) & (1<<i))>0)
			{
				windowArray[i] = new Window();

				windowArray[i].setXs(getConfigXStart(id,i+1));
				windowArray[i].setYs(getConfigYStart(id,i+1));
				windowArray[i].setXe(getConfigXEnd(id,i+1));
				windowArray[i].setYe(getConfigYEnd(id,i+1));
			}
			else
				windowArray[i] = null;
		}// end for on windows
	// set windows into detector
		detector.setWindows(windowArray);
	// Note flags are held IN the window list, so must setWindowFlags AFTER detector windows set
		detector.setWindowFlags(getConfigWindowFlags(id));
	// set detector into config
		c.setDetector(0,detector);
	// return config
		return c;
	}

	/**
	 * Method to return a LowResSpecConfig, constructed from the information against id id.
	 * @param id The Id number.
	 * @return The constructed LowResSpecConfig.
	 * @exception NumberFormatException Thrown if a numeric parameter is not returned from the properties
	 * 	file as a legal number.
	 * @exception IllegalArgumentException Thrown if the config id specified does not have a legal type.
	 */
	private LowResSpecConfig getNuViewConfigById(int id) throws NumberFormatException, IllegalArgumentException
	{
		LowResSpecConfig c = null;
		LowResSpecDetector detector = null;

	// check type
		if(getConfigType(id) != CONFIG_TYPE_SPECTROGRAPH_NUVIEW)
		{
			throw new IllegalArgumentException(this.getClass().getName()+":getCCDConfigById:Id "
				+id+" not a configuration of type Nu-View.");
		}
	// construct LowResSpecConfig
		c = new LowResSpecConfig(getConfigName(id));
		c.setWavelength(getConfigWavelength(id));
	// setup detector
		detector = new LowResSpecDetector();
		detector.setXBin(getConfigXBin(id));
		detector.setYBin(getConfigYBin(id));
		// note, other Detector fields not set, as they are not used by the instrument.
	// don't set windows into detector, use default windows.
	// Note flags are held IN the window list, so must setWindowFlags AFTER detector windows set
		detector.setWindowFlags(0);
	// set detector into config
		c.setDetector(0,detector);
	// return config
		return c;
	}

	/**
	 * Method to return a HiResSpecConfig, constructed from the information against id id.
	 * @param id The Id number.
	 * @return The constructed HiResSpecConfig.
	 * @exception NumberFormatException Thrown if a numeric parameter is not returned from the properties
	 * 	file as a legal number.
	 * @exception IllegalArgumentException Thrown if the config id specified does not have a legal type.
	 */
	private HiResSpecConfig getMESConfigById(int id) throws NumberFormatException, IllegalArgumentException
	{
		HiResSpecConfig c = null;
		LowResSpecDetector detector = null;

	// check type
		if(getConfigType(id) != CONFIG_TYPE_SPECTROGRAPH_MES)
		{
			throw new IllegalArgumentException(this.getClass().getName()+":getCCDConfigById:Id "
				+id+" not a configuration of type MES.");
		}
	// construct HiResSpecConfig
		c = new HiResSpecConfig(getConfigName(id));
		c.setFilterSlideName(getConfigFilterSlideName(id));
	// setup detector
		detector = new LowResSpecDetector();
		detector.setXBin(getConfigXBin(id));
		detector.setYBin(getConfigYBin(id));
		// note, other Detector fields not set, as they are not used by the instrument.

	// set windows into detector
		detector.setWindows(null);
	// Note flags are held IN the window list, so must setWindowFlags AFTER detector windows set
		detector.setWindowFlags(0);
	// set detector into config
		c.setDetector(0,detector);
	// return config
		return c;
	}

	/**
	 * Method to return a IRCamConfig, constructed from the information against id id.
	 * @param id The Id number.
	 * @return The constructed IRCamConfig.
	 * @exception NumberFormatException Thrown if a numeric parameter is not returned from the properties
	 * 	file as a legal number.
	 * @exception IllegalArgumentException Thrown if the config id specified does not have a legal type.
	 */
	private IRCamConfig getIRCamConfigById(int id) throws NumberFormatException, IllegalArgumentException
	{
		IRCamConfig c = null;
		IRCamDetector detector = null;

	// check type
		if(getConfigType(id) != CONFIG_TYPE_INFRA_RED_SUPIRCAM)
		{
			throw new IllegalArgumentException(this.getClass().getName()+":getIRCamConfigById:Id "
				+id+" not a configuration of type Infra Red (SupIRCam).");
		}
	// construct IRCamConfig
		c = new IRCamConfig(getConfigName(id));
		c.setFilterWheel(getConfigFilterWheel(id));
		c.setCalibrateBefore(getConfigCalibrateBefore(id));
		c.setCalibrateAfter(getConfigCalibrateAfter(id));
	// setup detector
		detector = new IRCamDetector();
		detector.setXBin(1);
		detector.setYBin(1);
		// note, other Detector fields not set, as they are not used by the instrument.
	// don't set windows into detector, use default windows.
	// Note flags are held IN the window list, so must setWindowFlags AFTER detector windows set
		detector.setWindowFlags(0);
	// set detector into config
		c.setDetector(0,detector);
	// return config
		return c;
	}

	/**
	 * Method to return a FixedFormatSpecConfig, constructed from the information against id id.
	 * @param id The Id number.
	 * @return The constructed FixedFormatSpecConfig.
	 * @exception NumberFormatException Thrown if a numeric parameter is not returned from the properties
	 * 	file as a legal number.
	 * @exception IllegalArgumentException Thrown if the config id specified does not have a legal type.
	 */
	private FixedFormatSpecConfig getFTSpecConfigById(int id) throws NumberFormatException, 
									 IllegalArgumentException
	{
		FixedFormatSpecConfig c = null;
		FixedFormatSpecDetector detector = null;

	// check type
		if(getConfigType(id) != CONFIG_TYPE_SPECTROGRAPH_FTSPEC)
		{
			throw new IllegalArgumentException(this.getClass().getName()+":getCCDConfigById:Id "
				+id+" not a configuration of type FTSpec.");
		}
	// construct FixedFormatSpecConfig
		c = new FixedFormatSpecConfig(getConfigName(id));
	// setup detector
		detector = new FixedFormatSpecDetector();
		detector.setXBin(getConfigXBin(id));
		detector.setYBin(getConfigYBin(id));
		// note, other Detector fields not set, as they are not used by the instrument.
	// don't set windows into detector, use default windows.
	// Note flags are held IN the window list, so must setWindowFlags AFTER detector windows set
		detector.setWindowFlags(0);
	// set detector into config
		c.setDetector(0,detector);
	// return config
		return c;
	}

// method to check values in the property file.
	/**
	 * Method to check whether a number is a legal configuration type number.
	 * To be this it must exist in CONFIG_TYPE_LIST.
	 * @param type The type number.
	 * @exception IllegalArgumentException Thrown if the type number is not in CONFIG_TYPE_LIST 
	 * 	e.g. the number is illegal.
	 * @see #CONFIG_TYPE_LIST
	 */
	private void checkConfigType(int type) throws IllegalArgumentException
	{
		int index;
		boolean found;

		index = 0;
		found = false;
		while((found == false)&&(index < CONFIG_TYPE_LIST.length))
		{
			found = (type == CONFIG_TYPE_LIST[index]);
			index++;
		}
		if(found == false)
		{
			throw new IllegalArgumentException(this.getClass().getName()+
				"getConfigType:Illegal type:"+type+".");
		}
	}

// methods to get property keys
	/**
	 * Method to return a key for the name string for a particular config id.
	 * @param id The config id.
	 * @return The key string.
	 * @see #configIdString
	 */
	private String configIdStringName(int id)
	{
		return new String(configIdString(id)+"name");
	}

	/**
	 * Method to return the type key string for a particular config id.
	 * @param id The config id.
	 * @return The key string.
	 * @see #configIdString
	 */
	private String configIdStringType(int id)
	{
		return new String(configIdString(id)+"type");
	}

	/**
	 * Method to return a key for the calibrate before flag for a particular config id.
	 * @param id The config id.
	 * @return The key string.
	 * @see #configIdString
	 */
	private String configIdStringCalibrateBefore(int id)
	{
		return new String(configIdString(id)+"calibrateBefore");
	}

	/**
	 * Method to return a key for the calibrate after flag for a particular config id.
	 * @param id The config id.
	 * @return The key string.
	 * @see #configIdString
	 */
	private String configIdStringCalibrateAfter(int id)
	{
		return new String(configIdString(id)+"calibrateAfter");
	}

	/**
	 * Method to return a key for the lower filter wheel for a particular config id.
	 * @param id The config id.
	 * @return The key string.
	 * @see #configIdString
	 */
	private String configIdStringLowerFilterWheel(int id)
	{
		return new String(configIdString(id)+"lowerFilterWheel");
	}

	/**
	 * Method to return a key for the upper filter wheel for a particular config id.
	 * @param id The config id.
	 * @return The key string.
	 * @see #configIdString
	 */
	private String configIdStringUpperFilterWheel(int id)
	{
		return new String(configIdString(id)+"upperFilterWheel");
	}

	/**
	 * Method to return a key for the filter slide name for a particular config id.
	 * @param id The config id.
	 * @return The key string.
	 * @see #configIdString
	 */
	private String configIdStringFilterSlideName(int id)
	{
		return new String(configIdString(id)+"filterSlideName");
	}

	/**
	 * Method to return a key for the wavelength for a particular config id.
	 * @param id The config id.
	 * @return The key string.
	 * @see #configIdString
	 */
	private String configIdStringWavelength(int id)
	{
		return new String(configIdString(id)+"filterWavelength");
	}

	/**
	 * Method to return a key for the filter wheel for a particular config id.
	 * @param id The config id.
	 * @return The key string.
	 * @see #configIdString
	 */
	private String configIdStringFilterWheel(int id)
	{
		return new String(configIdString(id)+"filterWheel");
	}

	/**
	 * Method to return a key for the x binning for a particular config id.
	 * @param id The config id.
	 * @return The key string.
	 * @see #configIdString
	 */
	private String configIdStringXBin(int id)
	{
		return new String(configIdString(id)+"xBin");
	}

	/**
	 * Method to return a key for the y binning for a particular config id.
	 * @param id The config id.
	 * @return The key string.
	 * @see #configIdString
	 */
	private String configIdStringYBin(int id)
	{
		return new String(configIdString(id)+"yBin");
	}

	/**
	 * Method to return a key for the window flags for a particular config id.
	 * @param id The config id.
	 * @return The key string.
	 * @see #configIdString
	 */
	private String configIdStringWindowFlags(int id)
	{
		return new String(configIdString(id)+"windowFlags");
	}

	/**
	 * Method to return a key for the x start position for a particular config id and window number.
	 * @param id The config id.
	 * @param windowNumber The number of the window, from 1 to 4.
	 * @return The key string.
	 * @see #configIdWindowString
	 */
	private String configIdWindowStringXStart(int id,int windowNumber)
	{
		return new String(configIdWindowString(id,windowNumber)+"xStart");
	}


	/**
	 * Method to return a key for the y start position for a particular config id and window number.
	 * @param id The config id.
	 * @param windowNumber The number of the window, from 1 to 4.
	 * @return The key string.
	 * @see #configIdWindowString
	 */
	private String configIdWindowStringYStart(int id,int windowNumber)
	{
		return new String(configIdWindowString(id,windowNumber)+"yStart");
	}

	/**
	 * Method to return a key for the x end position for a particular config id and window number.
	 * @param id The config id.
	 * @param windowNumber The number of the window, from 1 to 4.
	 * @return The key string.
	 * @see #configIdWindowString
	 */
	private String configIdWindowStringXEnd(int id,int windowNumber)
	{
		return new String(configIdWindowString(id,windowNumber)+"xEnd");
	}

	/**
	 * Method to return a key for the y end position for a particular config id and window number.
	 * @param id The config id.
	 * @param windowNumber The number of the window, from 1 to 4.
	 * @return The key string.
	 * @see #configIdWindowString
	 */
	private String configIdWindowStringYEnd(int id,int windowNumber)
	{
		return new String(configIdWindowString(id,windowNumber)+"yEnd");
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
		return new String(configIdString(id)+"window"+windowNumber+".");
	}

// method to get a property as a non-string data type
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

	/**
	 * Routine to get a properties value, given a key. The value must be a valid double, else a 
	 * NumberFormatException is thrown.
	 * @param p The property key we want the value for.
	 * @return The properties value, as an double.
	 * @exception NumberFormatException If the properties value string is not a valid double, this
	 * 	exception will be thrown when the Double.valueOf routine is called.
	 */
	public double getPropertyDouble(String p) throws NumberFormatException
	{
		String valueString = null;
		Double returnValue = null;

		valueString = getProperty(p);
		returnValue = Double.valueOf(valueString);
		return returnValue.doubleValue();
	}

	/**
	 * Routine to get a properties boolean value, given a key. The properties value should be either 
	 * "true" or "false".
	 * Boolean.valueOf is used to convert the string to a boolean value.
	 * @param p The property key we want the boolean value for.
	 * @return The properties value, as an boolean.
	 */
	public boolean getPropertyBoolean(String p)
	{
		String valueString = null;
		Boolean b = null;

		valueString = getProperty(p);
		b = Boolean.valueOf(valueString);
		return b.booleanValue();
	}
}
//
// $Log: not supported by cvs2svn $
// Revision 0.8  2003/08/21 14:24:04  cjm
// Added calibrateBefore and calibrateAfter calls.
//
// Revision 0.7  2003/07/15 16:20:01  cjm
// Added IRCam property configuration.
//
// Revision 0.6  2002/12/16 18:35:51  cjm
// Changed NPCCDConfig to CCDConfig
//
// Revision 0.5  2001/07/10 18:21:28  cjm
// Added ability to save Nu_View and MES configurations.
//
// Revision 0.4  2001/02/27 13:45:03  cjm
// Changed addNPDetector to setNPDetector to reflect changes
// in ngat.phase2.nonpersist.NPCCDConfig.
//
// Revision 0.3  2000/12/20 17:56:02  cjm
// Fixed window code.
//
// Revision 0.2  2000/11/30 18:47:44  cjm
// Made generic for other instruments.
//
// Revision 0.1  2000/11/28 15:45:41  cjm
// initial revision.
//
// Revision 0.1  2000/11/28 12:09:45  cjm
// initial revision.
//
// Revision 1.1  2000/11/28 12:07:11  cjm
// Initial revision
//
// Revision 0.6  2000/11/28 11:58:04  cjm
// Added configuration type.
//
// Revision 0.5  2000/11/24 15:40:54  cjm
// Fixed null pointer problems with detectors.
//
// Revision 0.4  2000/11/24 11:38:15  cjm
// Fixed for new CONFIG field ngat.phase2.nonpersist.NPInstrumentConfig.
//
// Revision 0.3  2000/02/08 16:34:21  cjm
// Added window flags code.
//
// Revision 0.2  1999/12/09 17:02:12  cjm
// More functionality added.
//
// Revision 0.1  1999/11/25 13:33:39  cjm
// initial revision.
//
//
