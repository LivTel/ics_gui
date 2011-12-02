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
// IcsGUIConfigProperties.java
// $Header: /home/cjm/cvs/ics_gui/java/IcsGUIConfigProperties.java,v 0.21 2011-12-02 11:06:01 cjm Exp $
import java.lang.*;
import java.io.*;
import java.util.*;

import ngat.phase2.*;

/**
 * This class holds the Instrument Configuration information used by the CCS GUI. The information is held
 * in a Java properties file and this class extends java.util.Properties
 * @see java.util.Properties
 * @author Chris Mottram
 * @version $Revision: 0.21 $
 */
public class IcsGUIConfigProperties extends Properties
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: IcsGUIConfigProperties.java,v 0.21 2011-12-02 11:06:01 cjm Exp $");
	/**
	 * Configuration type specifier:CCD (RATCam).
	 */
	public final static int CONFIG_TYPE_CCD_RATCAM 		      = 0;
	/**
	 * Configuration type specifier:Spectrograph (MES).
	 */
	public final static int CONFIG_TYPE_SPECTROGRAPH_MES 	      = 1;
	/**
	 * Configuration type specifier:Spectrograph (NuView).
	 */
	public final static int CONFIG_TYPE_SPECTROGRAPH_NUVIEW       = 2;
	/**
	 * Configuration type specifier:Infra-Red camera (SupIRCam).
	 */
	public final static int CONFIG_TYPE_INFRA_RED_SUPIRCAM        = 3;
	/**
	 * Configuration type specifier:Spectrograph (FTSpec).
	 */
	public final static int CONFIG_TYPE_SPECTROGRAPH_FTSPEC       = 4;
	/**
	 * Configuration type specifier:Polarimeter (Ringo Star).
	 */
	public final static int CONFIG_TYPE_POLARIMETER_RINGOSTAR     = 5;
	/**
	 * Configuration type specifier:Spectrograph (FrodoSpec).
	 */
	public final static int CONFIG_TYPE_SPECTROGRAPH_FRODOSPEC    = 6;
	/**
	 * Configuration type specifier:IMAGER (RISE).
	 */
	public final static int CONFIG_TYPE_CCD_RISE                  = 7;
	/**
	 * Configuration type specifier:Polarimeter (Ringo2).
	 */
	public final static int CONFIG_TYPE_POLARIMETER_RINGO2        = 8;
	/**
	 * Configuration type specifier:CCD (THOR).
	 */
	public final static int CONFIG_TYPE_CCD_THOR                  = 9;
	/**
	 * Configuration type specifier:CCD (O).
	 */
	public final static int CONFIG_TYPE_CCD_O 	              = 10;
	/**
	 * List of legal values that can be held in the config type field.
	 * @see #CONFIG_TYPE_CCD_RATCAM
	 * @see #CONFIG_TYPE_SPECTROGRAPH_MES
	 * @see #CONFIG_TYPE_SPECTROGRAPH_NUVIEW
	 * @see #CONFIG_TYPE_INFRA_RED_SUPIRCAM
	 * @see #CONFIG_TYPE_SPECTROGRAPH_FTSPEC
	 * @see #CONFIG_TYPE_POLARIMETER_RINGOSTAR
	 * @see #CONFIG_TYPE_SPECTROGRAPH_FRODOSPEC
	 * @see #CONFIG_TYPE_CCD_RISE
	 * @see #CONFIG_TYPE_POLARIMETER_RINGO2
	 * @see #CONFIG_TYPE_CCD_THOR
	 * @see #CONFIG_TYPE_CCD_O
	 */
	public final static int CONFIG_TYPE_LIST[] = {CONFIG_TYPE_CCD_RATCAM,CONFIG_TYPE_SPECTROGRAPH_MES,
		CONFIG_TYPE_SPECTROGRAPH_NUVIEW,CONFIG_TYPE_INFRA_RED_SUPIRCAM,CONFIG_TYPE_SPECTROGRAPH_FTSPEC,
		CONFIG_TYPE_POLARIMETER_RINGOSTAR,CONFIG_TYPE_SPECTROGRAPH_FRODOSPEC,CONFIG_TYPE_CCD_RISE,
		CONFIG_TYPE_POLARIMETER_RINGO2,CONFIG_TYPE_CCD_THOR,CONFIG_TYPE_CCD_O};
	/**
	 * Default filename for properties file.
	 */
	public final static String DEFAULT_PROPERTIES_FILENAME = "./ccs_gui_config.properties";
	/**
	 * Filename for properties file. Defaults to DEFAULT_PROPERTIES_FILENAME.
	 * @see #DEFAULT_PROPERTIES_FILENAME
	 */
	protected String propertiesFilename = DEFAULT_PROPERTIES_FILENAME;

	/**
	 * Set the filename to load/save the instrument config properties to.
	 * @param s A string representing the filename.
	 * @see #propertiesFilename
	 */
	public void setPropertiesFilename(String s)
	{
		propertiesFilename = s;
	}

	/**
	 * Load method for the properties. Calls super.load with a file input stream derived
	 * from thepropertiesFilename.
	 * @see #propertiesFilename
	 * @exception IOException Thrown if an erro occurs with the IO.
	 * @exception FileNotFoundException Thrown if the file isn't found.
	 */
	public void load() throws IOException, FileNotFoundException
	{
		FileInputStream fis = null;

		fis = new FileInputStream(propertiesFilename);
		super.load(fis);
		fis.close();
	}

	/**
	 * Save method for the properties. Calls super.save with a file output stream derived from the
	 * propertiesFilename.
	 * @see #propertiesFilename
	 * @exception IOException Thrown if an erro occurs with the IO.
	 */
	public void save() throws IOException
	{
		FileOutputStream fos = null;

		fos = new FileOutputStream(propertiesFilename);
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
	 * @see #getPolarimeterConfigById
	 * @see #getFrodoSpecConfigById
	 * @see #getRISEConfigById
	 * @see #getRingo2PolarimeterConfigById
	 * @see #getTHORConfigById
	 * @see #getOConfigById
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
			case CONFIG_TYPE_POLARIMETER_RINGOSTAR:
				c = getPolarimeterConfigById(id);
				break;
			case CONFIG_TYPE_SPECTROGRAPH_FRODOSPEC:
				c = getFrodoSpecConfigById(id);
				break;
			case CONFIG_TYPE_CCD_RISE:
				c = getRISEConfigById(id);
				break;
			case CONFIG_TYPE_POLARIMETER_RINGO2:
				c = getRingo2PolarimeterConfigById(id);
				break;
			case CONFIG_TYPE_CCD_THOR:
				c = getTHORConfigById(id);
				break;
			case CONFIG_TYPE_CCD_O:
				c = getOConfigById(id);
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
			case CONFIG_TYPE_POLARIMETER_RINGOSTAR:
				remove(configIdStringXBin(id));
				remove(configIdStringYBin(id));
				break;
			case CONFIG_TYPE_SPECTROGRAPH_FRODOSPEC:
				remove(configIdStringXBin(id));
				remove(configIdStringYBin(id));
				remove(configIdStringArm(id));
				remove(configIdStringResolution(id));
				break;
			case CONFIG_TYPE_CCD_RISE:
				remove(configIdStringXBin(id));
				remove(configIdStringYBin(id));
				break;
			case CONFIG_TYPE_POLARIMETER_RINGO2:
				remove(configIdStringXBin(id));
				remove(configIdStringYBin(id));
				remove(configIdStringEMGain(id));
				remove(configIdStringTriggerType(id));
				remove(configIdStringWindowFlags(id));
				for(j=1;j<2;j++)
				{
					remove(configIdWindowStringXStart(id,j));
					remove(configIdWindowStringYStart(id,j));
					remove(configIdWindowStringXEnd(id,j));
					remove(configIdWindowStringYEnd(id,j));
				}
				break;
			case CONFIG_TYPE_CCD_THOR:
				remove(configIdStringXBin(id));
				remove(configIdStringYBin(id));
				remove(configIdStringEMGain(id));
				remove(configIdStringWindowFlags(id));
				for(j=1;j<2;j++)
				{
					remove(configIdWindowStringXStart(id,j));
					remove(configIdWindowStringYStart(id,j));
					remove(configIdWindowStringXEnd(id,j));
					remove(configIdWindowStringYEnd(id,j));
				}
				break;
			case CONFIG_TYPE_CCD_O:
				remove(configIdStringFilterWheel(id));
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
			case CONFIG_TYPE_POLARIMETER_RINGOSTAR:
					setConfigXBin(i-1,getConfigXBin(i));
					remove(configIdStringXBin(i));
					setConfigYBin(i-1,getConfigYBin(i));
					remove(configIdStringYBin(i));
					break;
			case CONFIG_TYPE_SPECTROGRAPH_FRODOSPEC:
					setConfigXBin(i-1,getConfigXBin(i));
					remove(configIdStringXBin(i));
					setConfigYBin(i-1,getConfigYBin(i));
					remove(configIdStringYBin(i));
					setConfigArm(i-1,getConfigArm(i));
					remove(configIdStringArm(i));
					setConfigResolution(i-1,getConfigResolution(i));
					remove(configIdStringResolution(i));
					break;
			case CONFIG_TYPE_CCD_RISE:
					setConfigXBin(i-1,getConfigXBin(i));
					remove(configIdStringXBin(i));
					setConfigYBin(i-1,getConfigYBin(i));
					remove(configIdStringYBin(i));
					break;
			case CONFIG_TYPE_POLARIMETER_RINGO2:
					setConfigXBin(i-1,getConfigXBin(i));
					remove(configIdStringXBin(i));
					setConfigYBin(i-1,getConfigYBin(i));
					remove(configIdStringYBin(i));
					setConfigEMGain(i-1,getConfigEMGain(i));
					remove(configIdStringEMGain(i));
					setConfigTriggerType(i-1,getConfigTriggerType(i));
					remove(configIdStringTriggerType(i));
					setConfigWindowFlags(i-1,getConfigWindowFlags(i));
					remove(configIdStringWindowFlags(i));

					for(j=1;j<2;j++)
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
			case CONFIG_TYPE_CCD_THOR:
					setConfigXBin(i-1,getConfigXBin(i));
					remove(configIdStringXBin(i));
					setConfigYBin(i-1,getConfigYBin(i));
					remove(configIdStringYBin(i));
					setConfigEMGain(i-1,getConfigEMGain(i));
					remove(configIdStringEMGain(i));

					setConfigWindowFlags(i-1,getConfigWindowFlags(i));
					remove(configIdStringWindowFlags(i));
					for(j=1;j<2;j++)
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
				case CONFIG_TYPE_CCD_O:
					setConfigFilterWheel(i-1,getConfigFilterWheel(i));
					remove(configIdStringFilterWheel(i));
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
	 * @param id The configuration name.
	 * @param type The type of the configuration.
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
	 * Method to get the filter wheel string of configuration id id (INFRA_RED_SUPIRCAM|CCD_O).
	 * @param id The id of the configuration.
	 * @return The configuration filter wheel string.
	 */
	public String getConfigFilterWheel(int id)
	{
		return getProperty(configIdStringFilterWheel(id));
	}

	/**
	 * Method to set the upper filter wheel string of configuration id id (INFRA_RED_SUPIRCAM|CCD_O).
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
	 * Method to get the filter string of configuration id id. Was used for THOR, no longer used.
	 * @param id The id of the configuration.
	 * @return The configuration filter string.
	 * @see #configIdStringFilter
	 */
	public String getConfigFilter(int id)
	{
		return getProperty(configIdStringFilter(id));
	}

	/**
	 * Method to set the filter string of configuration id id. Was used for THOR, no longer used.
	 * @param id The id of the configuration.
	 * @param s The configuration filter string.
	 * @see #configIdStringFilter
	 */
	public void setConfigFilter(int id,String s)
	{
	// jdk 1.1.x
		//put(configIdStringUpperFilterWheel(id),s);
	// jdk 1.2.x
		setProperty(configIdStringFilter(id),s);
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

	/**
	 * Method to get the arm of configuration id id as a string.
	 * This is a FrodoSpec only configuration value.
	 * @param id The id of the configuration.
	 * @return The configuration arm.
	 * @exception IllegalArgumentException Thrown if the relevant property 
	 * 	"ccs_gui_config."id".arm" does not contain either "red", "blue", or "unknown".
	 * @see ngat.phase2.FrodoSpecConfig#RED_ARM
	 * @see ngat.phase2.FrodoSpecConfig#BLUE_ARM
	 * @see ngat.phase2.FrodoSpecConfig#NO_ARM
	 */
	public int getConfigArm(int id) throws IllegalArgumentException
	{
		String s = null;

		s = getProperty(configIdStringArm(id));
		if(s.equals("red"))
			return FrodoSpecConfig.RED_ARM;
		else if (s.equals("blue"))
			return FrodoSpecConfig.BLUE_ARM;
		else if (s.equals("unknown"))
			return FrodoSpecConfig.NO_ARM;
		throw new IllegalArgumentException(this.getClass().getName()+":getConfigArm:Illegal arm string:"+s);
	}

	/**
	 * Method to get the arm of configuration id id as a string.
	 * @param id The id of the configuration.
	 * @return The configuration arm as a string.
	 */
	public String getConfigArmString(int id)
	{
		return getProperty(configIdStringArm(id));
	}

	/**
	 * Method to set the arm of configuration id id.
	 * This is a FrodoSpec only configuration value.
	 * @param id The id of the configuration.
	 * @param arm The configuration arm.
	 * @exception IllegalArgumentException Thrown if the arm can't be mapped to a string.
	 * @see ngat.phase2.FrodoSpecConfig#RED_ARM
	 * @see ngat.phase2.FrodoSpecConfig#BLUE_ARM
	 * @see ngat.phase2.FrodoSpecConfig#NO_ARM
	 */
	public void setConfigArm(int id,int arm) throws IllegalArgumentException
	{
		String armString = null;

		if(arm == FrodoSpecConfig.RED_ARM)
			armString = "red";
		else if(arm == FrodoSpecConfig.BLUE_ARM)
			armString = "blue";
		else if(arm == FrodoSpecConfig.NO_ARM)
			armString = "unknown";
		else
		{
			throw new IllegalArgumentException(this.getClass().getName()+":setConfigArm:Illegal arm:"+arm);
		}
		setProperty(configIdStringArm(id),armString);
	}

	/**
	 * Method to get the resolution of configuration id id as a string.
	 * This is a FrodoSpec only configuration value.
	 * @param id The id of the configuration.
	 * @return The configuration resolution.
	 * @exception IllegalArgumentException Thrown if the relevant property 
	 * 	"ccs_gui_config."id".resolution" does not contain either "low", "high", or "unknown".
	 * @see ngat.phase2.FrodoSpecConfig#RESOLUTION_LOW
	 * @see ngat.phase2.FrodoSpecConfig#RESOLUTION_HIGH 
	 * @see ngat.phase2.FrodoSpecConfig#RESOLUTION_UNKNOWN
	 */
	public int getConfigResolution(int id) throws IllegalArgumentException
	{
		String s = null;

		s = getProperty(configIdStringResolution(id));
		if(s.equals("low"))
			return FrodoSpecConfig.RESOLUTION_LOW;
		else if (s.equals("high"))
			return FrodoSpecConfig.RESOLUTION_HIGH;
		else if (s.equals("unknown"))
			return FrodoSpecConfig.RESOLUTION_UNKNOWN;
		throw new IllegalArgumentException(this.getClass().getName()+":getConfigArm:Illegal resolution string:"+s);
	}

	/**
	 * Method to get the resolution of configuration id id as a string.
	 * @param id The id of the configuration.
	 * @return The configuration resolution as a string.
	 */
	public String getConfigResolutionString(int id)
	{
		return getProperty(configIdStringResolution(id));
	}

	/**
	 * Method to set the resolution of configuration id id.
	 * This is a FrodoSpec only configuration value.
	 * @param id The id of the configuration.
	 * @param resolution The configuration resolution.
	 * @exception IllegalArgumentException Thrown if the resolution can't be mapped to a string.
	 * @see ngat.phase2.FrodoSpecConfig#RESOLUTION_LOW
	 * @see ngat.phase2.FrodoSpecConfig#RESOLUTION_HIGH 
	 * @see ngat.phase2.FrodoSpecConfig#RESOLUTION_UNKNOWN
	 */
	public void setConfigResolution(int id,int resolution) throws IllegalArgumentException
	{
		String resolutionString = null;

		if(resolution == FrodoSpecConfig.RESOLUTION_LOW)
			resolutionString = "low";
		else if(resolution == FrodoSpecConfig.RESOLUTION_HIGH)
			resolutionString = "high";
		else if(resolution == FrodoSpecConfig.RESOLUTION_UNKNOWN)
			resolutionString = "unknown";
		else
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":setConfigResolution:Illegal resolution:"+resolution);
		}
		setProperty(configIdStringResolution(id),resolutionString);
	}

	/**
	 * Method to get the EM Gain of configuration id id.
	 * This is a Ringo2/THOR only configuration value.
	 * @param id The id of the configuration.
	 * @return The configuration EM Gain number.
	 * @exception NumberFormatException Thrown if the relevant property 
	 * 	"ccs_gui_config."id".emGain" does not contain a numeric value.
	 */
	public int getConfigEMGain(int id) throws NumberFormatException
	{
		return getPropertyInteger(configIdStringEMGain(id));
	}

	/**
	 * Method to get the EM Gain of configuration id id as a string.
	 * This is a Ringo2/THOR only configuration value.
	 * @param id The id of the configuration.
	 * @return The configuration EM Gain number as a string.
	 */
	public String getConfigEMGainString(int id)
	{
		return getProperty(configIdStringEMGain(id));
	}

	/**
	 * Method to set the EM Gain of configuration id id.
	 * This is a Ringo2/THOR only configuration value.
	 * @param id The id of the configuration.
	 * @param emGain The configuration EM Gain number.
	 */
	public void setConfigEMGain(int id,int emGain)
	{
	// jdk 1.2.x
		setProperty(configIdStringEMGain(id),Integer.toString(emGain));
	}

	/**
	 * Method to get the triggrt type of configuration id id as a string.
	 * This is a Ringo2 only configuration value.
	 * @param id The id of the configuration.
	 * @return The configuration arm.
	 * @exception IllegalArgumentException Thrown if the relevant property 
	 * 	"ccs_gui_config."id".triggerType" does not contain either "external", or "internal".
	 * @see ngat.phase2.Ringo2PolarimeterConfig#TRIGGER_TYPE_EXTERNAL
	 * @see ngat.phase2.Ringo2PolarimeterConfig#TRIGGER_TYPE_INTERNAL
	 */
	public int getConfigTriggerType(int id) throws IllegalArgumentException
	{
		String s = null;

		s = getProperty(configIdStringTriggerType(id));
		if(s.equals("external"))
			return Ringo2PolarimeterConfig.TRIGGER_TYPE_EXTERNAL;
		else if (s.equals("internal"))
			return Ringo2PolarimeterConfig.TRIGGER_TYPE_INTERNAL;
		throw new IllegalArgumentException(this.getClass().getName()+
						   ":getConfigTriggerType:Illegal trigger type string:"+s);
	}

	/**
	 * Method to get the trigger type of configuration id id as a string.
	 * @param id The id of the configuration.
	 * @return The configuration trigger type as a string.
	 */
	public String getConfigTriggerTypeString(int id)
	{
		return getProperty(configIdStringTriggerType(id));
	}

	/**
	 * Method to set the trigger type of configuration id id.
	 * This is a Ringo2 only configuration value.
	 * @param id The id of the configuration.
	 * @param triggerType The configuration trigger type.
	 * @exception IllegalArgumentException Thrown if the trigger type can't be mapped to a string.
	 * @see ngat.phase2.Ringo2PolarimeterConfig#TRIGGER_TYPE_EXTERNAL
	 * @see ngat.phase2.Ringo2PolarimeterConfig#TRIGGER_TYPE_INTERNAL
	 */
	public void setConfigTriggerType(int id,int triggerType) throws IllegalArgumentException
	{
		String triggerTypeString = null;

		if(triggerType == Ringo2PolarimeterConfig.TRIGGER_TYPE_EXTERNAL)
			triggerTypeString = "external";
		else if(triggerType == Ringo2PolarimeterConfig.TRIGGER_TYPE_INTERNAL)
			triggerTypeString = "internal";
		else
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":setConfigTriggerType:Illegal trigger type:"+triggerType);
		}
		setProperty(configIdStringTriggerType(id),triggerTypeString);
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
		c.setCalibrateBefore(getConfigCalibrateBefore(id));
		c.setCalibrateAfter(getConfigCalibrateAfter(id));
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
		c.setCalibrateBefore(getConfigCalibrateBefore(id));
		c.setCalibrateAfter(getConfigCalibrateAfter(id));
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
		c.setCalibrateBefore(getConfigCalibrateBefore(id));
		c.setCalibrateAfter(getConfigCalibrateAfter(id));
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
		c.setCalibrateBefore(getConfigCalibrateBefore(id));
		c.setCalibrateAfter(getConfigCalibrateAfter(id));
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

	/**
	 * Method to return a PolarimeterConfig, constructed from the information against id id.
	 * @param id The Id number.
	 * @return The constructed PolarimeterConfig.
	 * @exception NumberFormatException Thrown if a numeric parameter is not returned from the properties
	 * 	file as a legal number.
	 * @exception IllegalArgumentException Thrown if the config id specified does not have a legal type.
	 */
	private PolarimeterConfig getPolarimeterConfigById(int id) throws NumberFormatException, 
									 IllegalArgumentException
	{
		PolarimeterConfig c = null;
		PolarimeterDetector detector = null;

	// check type
		if(getConfigType(id) != CONFIG_TYPE_POLARIMETER_RINGOSTAR)
		{
			throw new IllegalArgumentException(this.getClass().getName()+":getPolarimeterConfigById:Id "
				+id+" not a configuration of type Polarimeter (Ringo Star).");
		}
	// construct PolarimeterConfig
		c = new PolarimeterConfig(getConfigName(id));
		c.setCalibrateBefore(getConfigCalibrateBefore(id));
		c.setCalibrateAfter(getConfigCalibrateAfter(id));
	// setup detector
		detector = new PolarimeterDetector();
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
	 * Method to return a FrodoSpecConfig, constructed from the information against id id.
	 * @param id The Id number.
	 * @return The constructed FrodoSpecConfig.
	 * @exception NumberFormatException Thrown if a numeric parameter is not returned from the properties
	 * 	file as a legal number.
	 * @exception IllegalArgumentException Thrown if the config id specified does not have a legal type.
	 */
	private FrodoSpecConfig getFrodoSpecConfigById(int id) throws NumberFormatException, IllegalArgumentException
	{
		FrodoSpecConfig c = null;
		FrodoSpecDetector detector = null;

	// check type
		if(getConfigType(id) != CONFIG_TYPE_SPECTROGRAPH_FRODOSPEC)
		{
			throw new IllegalArgumentException(this.getClass().getName()+":getFrodoSpecConfigById:Id "
				+id+" not a configuration of type FrodoSpec.");
		}
	// construct LowResSpecConfig
		c = new FrodoSpecConfig(getConfigName(id));
		c.setCalibrateBefore(getConfigCalibrateBefore(id));
		c.setCalibrateAfter(getConfigCalibrateAfter(id));
		c.setArm(getConfigArm(id));
		c.setResolution(getConfigResolution(id));
	// setup detector
		detector = new FrodoSpecDetector();
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
	 * Method to return a RISEConfig, constructed from the information against id id.
	 * @param id The Id number.
	 * @return The constructed RISEConfig.
	 * @exception NumberFormatException Thrown if a numeric parameter is not returned from the properties
	 * 	file as a legal number.
	 * @exception IllegalArgumentException Thrown if the config id specified does not have a legal type.
	 */
	private RISEConfig getRISEConfigById(int id) throws NumberFormatException, 
									 IllegalArgumentException
	{
		RISEConfig c = null;
		RISEDetector detector = null;

	// check type
		if(getConfigType(id) != CONFIG_TYPE_CCD_RISE)
		{
			throw new IllegalArgumentException(this.getClass().getName()+":getRISEConfigById:Id "
				+id+" not a configuration of type Polarimeter (Ringo Star).");
		}
	// construct RISEConfig
		c = new RISEConfig(getConfigName(id));
		c.setCalibrateBefore(getConfigCalibrateBefore(id));
		c.setCalibrateAfter(getConfigCalibrateAfter(id));
	// setup detector
		detector = new RISEDetector();
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
	 * Method to return a Ringo2PolarimeterConfig, constructed from the information against id id.
	 * @param id The Id number.
	 * @return The constructed Ringo2PolarimeterConfig.
	 * @exception NumberFormatException Thrown if a numeric parameter is not returned from the properties
	 * 	file as a legal number.
	 * @exception IllegalArgumentException Thrown if the config id specified does not have a legal type.
	 */
	private Ringo2PolarimeterConfig getRingo2PolarimeterConfigById(int id) throws NumberFormatException, 
									 IllegalArgumentException
	{
		Ringo2PolarimeterConfig c = null;
		Ringo2PolarimeterDetector detector = null;
		Window windowArray[];

	// check type
		if(getConfigType(id) != CONFIG_TYPE_POLARIMETER_RINGO2)
		{
			throw new IllegalArgumentException(this.getClass().getName()+":getRingo2PolarimeterConfigById:Id "
				+id+" not a configuration of type Polarimeter (Ringo2).");
		}
	// construct Ringo2PolarimeterConfig
		c = new Ringo2PolarimeterConfig(getConfigName(id));
		c.setCalibrateBefore(getConfigCalibrateBefore(id));
		c.setCalibrateAfter(getConfigCalibrateAfter(id));
		c.setEmGain(getConfigEMGain(id));
		c.setTriggerType(getConfigTriggerType(id));
	// setup detector
		detector = new Ringo2PolarimeterDetector();
		detector.setXBin(getConfigXBin(id));
		detector.setYBin(getConfigYBin(id));
		// note, other Detector fields not set, as they are not used by the instrument.
	// don't set windows into detector, use default windows.
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
	 * Method to return a THORConfig, constructed from the information against id id.
	 * @param id The Id number.
	 * @return The constructed THORConfig.
	 * @exception NumberFormatException Thrown if a numeric parameter is not returned from the properties
	 * 	file as a legal number.
	 * @exception IllegalArgumentException Thrown if the config id specified does not have a legal type.
	 * @see #getConfigType
	 * @see #getConfigCalibrateBefore
	 * @see #getConfigCalibrateAfter
	 * @see #getConfigEMGain
	 * @see #getConfigXBin
	 * @see #getConfigYBin
	 * @see #getConfigWindowFlags
	 * @see #getConfigXStart
	 * @see #getConfigYStart
	 * @see #getConfigXEnd
	 * @see #getConfigYEnd
	 */
	private THORConfig getTHORConfigById(int id) throws NumberFormatException, IllegalArgumentException
	{
		THORConfig c = null;
		THORDetector detector = null;
		Window windowArray[];

	// check type
		if(getConfigType(id) != CONFIG_TYPE_CCD_THOR)
		{
			throw new IllegalArgumentException(this.getClass().getName()+":getTHORConfigById:Id "
				+id+" not a configuration of type THOR.");
		}
	// construct THORConfig
		c = new THORConfig(getConfigName(id));
		c.setCalibrateBefore(getConfigCalibrateBefore(id));
		c.setCalibrateAfter(getConfigCalibrateAfter(id));
		c.setEmGain(getConfigEMGain(id));
	// setup detector
		detector = new THORDetector();
		detector.setXBin(getConfigXBin(id));
		detector.setYBin(getConfigYBin(id));
		// note, other Detector fields not set, as they are not used by the instrument.
	// don't set windows into detector, use default windows.
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
	 * Method to return a OConfig, constructed from the information against id id.
	 * @param id The Id number.
	 * @return The constructed OConfig.
	 * @exception NumberFormatException Thrown if a numeric parameter is not returned from the properties
	 * 	file as a legal number.
	 * @exception IllegalArgumentException Thrown if the config id specified does not have a legal type.
	 */
	private OConfig getOConfigById(int id) throws NumberFormatException, IllegalArgumentException
	{
		OConfig c = null;
		ODetector detector = null;
		Window windowArray[];

	// check type
		if(getConfigType(id) != CONFIG_TYPE_CCD_O)
		{
			throw new IllegalArgumentException(this.getClass().getName()+":getOConfigById:Id "
				+id+" not a configuration of type O.");
		}
	// construct OConfig
		c = new OConfig(getConfigName(id));
		c.setCalibrateBefore(getConfigCalibrateBefore(id));
		c.setCalibrateAfter(getConfigCalibrateAfter(id));
		c.setFilterWheel(getConfigFilterWheel(id));
	// setup detector
		detector = new ODetector();
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
	 * Method to return a key for the filter for a particular config id.
	 * @param id The config id.
	 * @return The key string.
	 * @see #configIdString
	 */
	private String configIdStringFilter(int id)
	{
		return new String(configIdString(id)+"filter");
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
	 * Method to return a key for the arm for a particular config id.
	 * @param id The config id.
	 * @return The key string.
	 * @see #configIdString
	 */
	private String configIdStringArm(int id)
	{
		return new String(configIdString(id)+"arm");
	}

	/**
	 * Method to return a key for the EM Gain for a particular config id.
	 * @param id The config id.
	 * @return The key string.
	 * @see #configIdString
	 */
	private String configIdStringEMGain(int id)
	{
		return new String(configIdString(id)+"emGain");
	}

	/**
	 * Method to return a key for the trigger type for a particular config id.
	 * @param id The config id.
	 * @return The key string.
	 * @see #configIdString
	 */
	private String configIdStringTriggerType(int id)
	{
		return new String(configIdString(id)+"triggerType");
	}

	/**
	 * Method to return a key for the resolution for a particular config id.
	 * @param id The config id.
	 * @return The key string.
	 * @see #configIdString
	 */
	private String configIdStringResolution(int id)
	{
		return new String(configIdString(id)+"resolution");
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
// Revision 0.20  2011/11/09 11:42:23  cjm
// Fixed comments.
//
// Revision 0.19  2011/11/07 17:07:34  cjm
// Added support for config's for IO:O.
//
// Revision 0.18  2010/10/07 13:24:06  cjm
// Added THOR accessors.
//
// Revision 0.17  2009/11/24 14:17:10  cjm
// Added Ringo2 support.
//
// Revision 0.16  2008/11/13 15:38:22  cjm
// Fixed comments.
//
// Revision 0.15  2008/04/29 11:08:09  cjm
// Added setPropertiesFilename/propertiesFilename so the property filename can be changed
// from the default.
//
// Revision 0.14  2007/12/11 17:40:02  cjm
// Added RISE config handling - CONFIG_TYPE_CCD_RISE etc.
//
// Revision 0.13  2007/12/11 16:24:19  cjm
// Added FrodoSpec configuration property handling.
//
// Revision 0.12  2006/05/16 17:12:24  cjm
// gnuify: Added GNU General Public License.
//
// Revision 0.11  2005/11/29 16:31:40  cjm
// Added Polarimeter (Ringo Star) support.
//
// Revision 0.10  2003/11/17 19:06:44  cjm
// Added InstrumentConfig setCalibrateBefore and setCalibrateAfter when
// calling getXXXConfigById so all InstrumentConfig sub-classes have calibrateBefore and calibrateAfter
// flags properly set (only IRCamConfig had this previously).
//
// Revision 0.9  2003/11/14 15:02:12  cjm
// Added FTSpec / FixedFormatSpecConfig code.
//
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
