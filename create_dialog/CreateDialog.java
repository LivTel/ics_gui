/*   
    Copyright 2006, Astrophysics Research Institute, Liverpool John Moores University.

    This file is part of IcsGUI.

    IcsGUI is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    IcsGUI is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with IcsGUI; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/
// CreateDialog.java
// $Header: /home/cjm/cvs/ics_gui/create_dialog/CreateDialog.java,v 0.30 2020-05-05 10:19:25 cjm Exp $
import java.lang.*;
import java.lang.reflect.*;
import java.io.*;
import java.util.*;

/**
 * Main class to write a dialog for an input class.
 * @author Chris Mottram
 * @version $Revision: 005c0481ab709e9b1fba06c9ee9ee487640f86de $
 */
public class CreateDialog
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: CreateDialog.java | Tue May 5 16:57:39 2020 +0100 | Chris Mottram  $");
	/**
	 * Multiplier for getting dialog height from number of fields.
	 */
	public final static int FIELD_HEIGHT = 20;
	/**
	 * Dialog border height.
	 */
	public final static int DIALOG_BORDER_HEIGHT = 5;
	/**
	 * Button height.
	 */
	public final static int BUTTON_HEIGHT = 25;
	/**
	 * Input class.
	 */
	private String inputClassName = null;
	/**
	 * The ngat.message class we are generating a dialog for.
	 */
	private Class inputClass = null;
	/**
	 * List of fields in the inputClass we are writing the dialog for.
	 */
	private Vector fieldList = null;
	/**
	 * Hashtable storing label string for field names.
	 */
	private Hashtable labelHashtable = null;
	/**
	 * Stream to write output to.
	 */
	private PrintStream outputStream = System.out;
	/**
	 * If we are writing to file, this is the associated stream. A Print Stream is
	 * written around this, use outputStream to write the data.
	 * @see #outputStream
	 */
	private FileOutputStream fileOutputStream = null;
	/**
	 * Boolean determining whether to put special config methods in the source file.
	 */
	private boolean configMethods = false;

	/**
	 * The main routine, called when CreateDialog is executed.
	 * @param args Command line arguments.
	 */
	public static void main(String[] args)
	{
		CreateDialog cd = new CreateDialog();
		int retval;

		cd.parseArgs(args);
		cd.init();
		try
		{
			cd.getClassInstance();
		}
		catch(ClassNotFoundException e)
		{
			System.err.println(e);
			System.exit(1);
		}
		try
		{
			cd.getFields();
		}
		catch(SecurityException e)
		{
			System.err.println(e);
			System.exit(1);
		}
		try
		{
			cd.openOutput();
		}
		catch(IOException e)
		{
			System.err.println(e);
			System.err.println("Using stdout instead");
			cd.fileOutputStream = null;
			cd.outputStream = System.out;
		}
		retval = 0;
		try
		{
			cd.writeDialog();
		}
		catch(Exception e)
		{
			System.err.println(e);
			retval = 1;
		}
		try
		{
			cd.closeOutput();
		}
		catch(IOException e)
		{
			System.err.println(e);
			System.exit(1);
		}
		System.exit(retval);
	}

	/**
	 * This routine parses arguments passed into CreateDialog.
	 * @param args Command line arguments.
	 * @see #help
	 */
	private void parseArgs(String[] args)
	{
		for(int i = 0; i < args.length;i++)
		{
			if(args[i].equals("-c")||args[i].equals("-class"))
			{
				if((i+1)< args.length)
				{
					inputClassName = new String(args[i+1]);
					i++;
				}
				else
					System.err.println("-class requires a classname");
			}
			if(args[i].equals("-h")||args[i].equals("-help"))
			{
				help();
				System.exit(0);
			}
		}// end for
		if(inputClassName == null)
		{
			System.err.println("No input class name Specified");
			help();
			System.exit(1);
		}
	}

	/**
	 * Initialisation routine.
	 */
	private void init()
	{
	// setup map from field names to label names
		labelHashtable = new Hashtable();

	// COMMAND
		labelHashtable.put("id","Identifier");
		// ACQUIRE
		labelHashtable.put("acquisitionMode","Mode");
		labelHashtable.put("threshold","Threshold");
		labelHashtable.put("RA","RA");
		labelHashtable.put("dec","Declination");
		labelHashtable.put("RARate","RA Rate");
		labelHashtable.put("decRate","Declination Rate");
		labelHashtable.put("calculationTime","Calculation Time");
		labelHashtable.put("moving","Is Moving");
		labelHashtable.put("xPixel","X Pixel");
		labelHashtable.put("yPixel","Y Pixel");
	// ARC LAMPFLAT
		labelHashtable.put("lamp","Lamp");
	// DAY_CALIBRATE TWILIGHT_CALIBRATE
		labelHashtable.put("timeToComplete","Time to Complete(ms)");
	// SKYFLAT TWILIGHT_CALIBRATE
		labelHashtable.put("telescopeAzimuth","Telescope Azimuth");
		labelHashtable.put("telescopeAltitude","Telescope Altitude");
		labelHashtable.put("useTime","Use Time");
	// EXPOSE
		labelHashtable.put("pipelineProcess","Pipeline Process");
		labelHashtable.put("standard","Standard");
	// MULTRUN
		labelHashtable.put("numberExposures","No. of Exposures");
	// RUNAT
		labelHashtable.put("startTime","Start Time");
		labelHashtable.put("exposureTime","Exposure Time(ms)");
	// TIMED_MULTRUNAT
		labelHashtable.put("totalDuration","Total Duration(ms)");
	// REBOOT SET_LOGGING TEST
		labelHashtable.put("level","Level");
	// CONFIG
		labelHashtable.put("config","Config");
	// TELFOCUS
		labelHashtable.put("startFocus","Start Focus");
		labelHashtable.put("endFocus","End Focus");
		labelHashtable.put("step","Focus Step");
		labelHashtable.put("currentFocus","Current Focus");
	// FRODOSPEC_MULTRUN
		labelHashtable.put("arm","Arm");
	// FRODOSPEC_CONFIG
		labelHashtable.put("resolution","Resolution");
	}

	/**
	 * Get an instance of class for the class we are creating the dialog for.
	 * @throws ClassNotFoundException Thrown if Class.forName fails.
	 */
	public void getClassInstance() throws ClassNotFoundException
	{
		inputClass = Class.forName(inputClassName);
	}

	/**
	 * Get the fields from the inputClass.
	 * Each super-classes fields are inserted at the beginning of the fieldList,
	 * but in ascending order.
	 * @see #fieldList
	 */
	public void getFields() throws SecurityException
	{
		Field[] fieldArray;
		Class cl = null;
		int i,modifierValue,fieldInsertIndex;

		cl = inputClass;
		fieldList = new Vector();
		while(cl.getName().equals("java.lang.Object") == false)
		{
			fieldArray = cl.getDeclaredFields();// excludes inherited fields.
			// insert each suoer-classes fields starting at the start of the array
			fieldInsertIndex = 0;
			for(i=0;i<fieldArray.length;i++)
			{
				// Only get non-final non-static (non-class) fields
				modifierValue = fieldArray[i].getModifiers();
				if(((Modifier.isFinal(modifierValue)) || (Modifier.isStatic(modifierValue)))== false)
				{
					fieldList.insertElementAt(fieldArray[i],fieldInsertIndex);
					// each super-classes fields are inserted at the beginning of the list,
					// but in ascending order.
					fieldInsertIndex++;
				}
			}
		// get the super-class
			cl = cl.getSuperclass();
		}
	}

	/**
	 * Open output filename.
	 * @throws IOException Thrown if opening the Java source file for output fails.
	 * @see #inputClass
	 * @see #fileOutputStream
	 * @see #outputStream
	 */
	public void openOutput() throws IOException
	{
		fileOutputStream = new FileOutputStream(getUnqualifiedClassName(inputClass.getName())+"Dialog.java");
		outputStream = new PrintStream(fileOutputStream);
	}

	/**
	 * Write a swing Java dialog source file from the fields.
	 * @exception IllegalArgumentException Thrown if one of the input fields is of an unknown class.
	 * @see #writeGNULicense
	 * @see #writeFieldDeclaration
	 * @see #writeInputFieldDeclaration
	 * @see #writeInputFieldConstruct
	 * @see #writeInputFieldGet
	 * @see #writeCommandFieldSet
	 */
	private void writeDialog() throws IllegalArgumentException
	{
		Field f = null;
		int i;
		int height;

	// headers and imports
		outputStream.println("// "+getUnqualifiedClassName(inputClass.getName())+
			"Dialog.java");
		outputStream.println("// Generated by "+this.getClass().getName()+" : Version : $Revision: 005c0481ab709e9b1fba06c9ee9ee487640f86de $");
		outputStream.println("// DO NOT EDIT this file, it is machine generated.");
		writeGNULicense(outputStream);
		outputStream.println("import java.lang.*;");
		outputStream.println("import java.text.*;");
		outputStream.println("import java.util.*;");
		outputStream.println("import java.awt.*;");
		outputStream.println("import java.awt.event.*;");
		outputStream.println("import javax.swing.*;");
		outputStream.println("");
		outputStream.println("import ngat.message.base.*;");
		outputStream.println("import ngat.phase2.*;");
		// for RA/dec parsing
		outputStream.println("import ngat.astrometry.Position;");
		outputStream.println("");
	// class comment
		outputStream.println("/**");
		outputStream.println(" * Dialog class to draw an input dialog for the "+
			getUnqualifiedClassName(inputClass.getName())+" class in swing.");
		outputStream.println(" * @author "+this.getClass().getName());
		outputStream.println(" */");

		outputStream.println("public class "+getUnqualifiedClassName(inputClass.getName())+
			"Dialog extends IcsCommandDialog implements ActionListener");
		outputStream.println("{");
	// for each field - define a field of the fieldname for temporary storage
		for(i=0;i<fieldList.size();i++)
		{
			f = (Field)(fieldList.elementAt(i));
			writeFieldDeclaration(f,outputStream);
		}
	// for each field - define a suitable dialog field of the fieldname for input
		for(i=0;i<fieldList.size();i++)
		{
			f = (Field)(fieldList.elementAt(i));
			writeInputFieldDeclaration(f,outputStream);
		}
		outputStream.println("");
	// constructor
		outputStream.println("\t/**");
		outputStream.println("\t * Constructor.");
		outputStream.println("\t * @param owner The frame owning this dialog.");
		outputStream.println("\t */");
		outputStream.println("\tpublic "+getUnqualifiedClassName(inputClass.getName())+"Dialog(Frame owner)");
		outputStream.println("\t{");
		outputStream.println("\t\tsuper(owner,\""+getUnqualifiedClassName(inputClass.getName())+" Dialog\");");
		outputStream.println("\t\tgetContentPane().setLayout(new BorderLayout());");
		outputStream.println("\t\t//setResizable(false);");
	// create and add main panel
		outputStream.println("\t// create the panel");
		outputStream.println("\t\tJPanel panel = new JPanel();");
		outputStream.println("\t\tpanel.setMinimumSize(new Dimension(250,250));");
		height = (fieldList.size()*FIELD_HEIGHT)+(2*DIALOG_BORDER_HEIGHT)+BUTTON_HEIGHT;
		outputStream.println("\t\tpanel.setPreferredSize(new Dimension("+400+","+
			height+"));");
		outputStream.println("\t// Add the JPanel to the frame.");
		outputStream.println("\t\tgetContentPane().add(panel);");
		outputStream.println("\t// setup panel");
		outputStream.println("\t\tpanel.setBorder(BorderFactory.createEmptyBorder("+DIALOG_BORDER_HEIGHT+","
			+DIALOG_BORDER_HEIGHT+","+DIALOG_BORDER_HEIGHT+","+DIALOG_BORDER_HEIGHT+"));");
		outputStream.println("\t\tpanel.setLayout(new GridLayout(0,2));");
		outputStream.println("\t\tpanel.setMinimumSize(new Dimension(250,250));");
		outputStream.println("\t\tpanel.setPreferredSize(new Dimension(300,"+height+"));");
	// for each field - add a label and construct the text field
		outputStream.println("");
		outputStream.println("\t\tJLabel label = null;");
	// go through the list inorder, the fields should be ordered superclass->subclass,
	// with fields within each class in the right order.
		for(i=0;i<fieldList.size();i++)
		{
			f = (Field)(fieldList.elementAt(i));
			writeInputFieldConstruct(f,outputStream);
		}// end for on field
	// add Ok/Cancel button
		outputStream.println("\t// Ok button");
		outputStream.println("\t\tJButton okButton = new JButton(\"Ok\");");
		outputStream.println("\t\tokButton.addActionListener(this);");
		outputStream.println("\t\tpanel.add(okButton);");
		outputStream.println("\t// Cancel button");
		outputStream.println("\t\tJButton cancelButton = new JButton(\"Cancel\");");
		outputStream.println("\t\tcancelButton.addActionListener(this);");
		outputStream.println("\t\tpanel.add(cancelButton);");
	// add action listener
	// finish the constructor
		outputStream.println("\t\tsetSize(300,"+height+");");
		outputStream.println("\t}");
		outputStream.println("");
	// ActionListener actionPerformed method
		outputStream.println("\t/**");
		outputStream.println("\t * actionPerformed method, called when Ok/Cancel pressed.");
		outputStream.println("\t * Should call the dialogListener if set with a constructed command.");
		outputStream.println("\t * Should unmanage the dialog.");
		outputStream.println("\t * @param event The event that generated this call.");
		outputStream.println("\t */");
		outputStream.println("\tpublic void actionPerformed(ActionEvent event)");
		outputStream.println("\t{");
		outputStream.println("\t\tJButton source = (JButton)(event.getSource());");
		outputStream.println("\t\t"+inputClassName+" command = null;");
		outputStream.println("\t\tboolean ok = false;");
		outputStream.println("");
		outputStream.println("\t\tif(source.getText().equals(\"Ok\"))");
		outputStream.println("\t\t\tok = true;");
		outputStream.println("\t\telse");
		outputStream.println("\t\t\tok = false;");
		outputStream.println("\t\tif(ok)");
		outputStream.println("\t\t{");
		outputStream.println("\t\t// get the value from each input field");
	// get the value for each field from it's input field to it's actual field
		for(i=fieldList.size()-1;i>-1;i--)
		{
			f = (Field)(fieldList.elementAt(i));
			writeInputFieldGet(f,outputStream);
		}
		outputStream.println("\t\t// construct and set fields in the COMMAND object");
		outputStream.println("\t\t\tcommand = new "+inputClassName+"(id);");
	// set COMMAND fields here
		for(i=fieldList.size()-1;i>-1;i--)
		{
			f = (Field)(fieldList.elementAt(i));
			writeCommandFieldSet(f,outputStream);
		}
		outputStream.println("\t\t}");
		outputStream.println("\t\tif(dialogListener != null)");
		outputStream.println("\t\t\tdialogListener.actionPerformed(ok,command);");
		outputStream.println("\t//unmanage dialog");
		outputStream.println("\t\tthis.setVisible(false);");
		outputStream.println("\t}");
	// setID method
		outputStream.println("");
		outputStream.println("\t/**");
		outputStream.println("\t * setID method. Sets id and associated Dialog Text Field.");
		outputStream.println("\t * @param s The id string.");
		outputStream.println("\t */");
		outputStream.println("\tpublic void setID(String s)");
		outputStream.println("\t{");
		outputStream.println("\t\tid = new String(s);");
		outputStream.println("\t\tidTextField.setText(s);");
		outputStream.println("\t}");
	// if there is a field of class ngat.phase2.InstrumentConfig - add some special methods here
		if(configMethods)
		{
		// addConfigButtonActionListener method
			outputStream.println("");
			outputStream.println("\t/**");
			outputStream.println("\t * Add an action listener to the config Button.");
			outputStream.println("\t * @param al The action Listener.");
			outputStream.println("\t */");
			outputStream.println("\tpublic void addConfigButtonActionListener(ActionListener al)");
			outputStream.println("\t{");
			outputStream.println("\t\tconfigButton.addActionListener(al);");
			outputStream.println("\t}");
			outputStream.println("");
		// setConfigProperties method
			outputStream.println("\t/**");
			outputStream.println("\t * Set a reference to the list of properties to choose from.");
			outputStream.println("\t * @param p The list of properties.");
			outputStream.println("\t */");
			outputStream.println("\tpublic void setConfigProperties(IcsGUIConfigProperties p)");
			outputStream.println("\t{");
			outputStream.println("\t\tconfigProperties = p;");
			outputStream.println("\t}");
			outputStream.println("");
		// setConfigTextField method
			outputStream.println("\t/**");
			outputStream.println("\t * Set the configs text field to a string.");
			outputStream.println("\t * @param s The string to set the field to.");
			outputStream.println("\t */");
			outputStream.println("\tpublic void setConfigTextField(String s)");
			outputStream.println("\t{");
			outputStream.println("\t\tconfigTextField.setText(s);");
			outputStream.println("\t}");
			outputStream.println("");
		// setIcsGUIStatus method
			outputStream.println("\t/**");
			outputStream.println("\t * Set the IcsGUIStatus reference.");
			outputStream.println("\t * @param s The instance of IcsGUIStatus to use.");
			outputStream.println("\t */");
			outputStream.println("\tpublic void setIcsGUIStatus(IcsGUIStatus s)");
			outputStream.println("\t{");
			outputStream.println("\t\ticsGUIStatus = s;");
			outputStream.println("\t}");
			outputStream.println("");
		// getIcsGUIStatus method
			outputStream.println("\t/**");
			outputStream.println("\t * Get the IcsGUIStatus reference.");
			outputStream.println("\t * @return The instance of IcsGUIStatus to use.");
			outputStream.println("\t */");
			outputStream.println("\tpublic IcsGUIStatus getIcsGUIStatus()");
			outputStream.println("\t{");
			outputStream.println("\t\treturn icsGUIStatus;");
			outputStream.println("\t}");
		}
	// end class, Log and finish
		outputStream.println("}");
	}

	/**
	 * Write the GNU Public license into our machine generated Dialog.
	 * @param os The output stream to write the license to.
	 */
	private void writeGNULicense(PrintStream os)
	{
		int i;
		String licenseList[] = {
			"/* ",
			"    Copyright 2006, Astrophysics Research Institute, Liverpool John Moores University.",
			"",
			"    This file is part of IcsGUI.",
			"",
			"    IcsGUI is free software; you can redistribute it and/or modify",
			"    it under the terms of the GNU General Public License as published by",
			"    the Free Software Foundation; either version 2 of the License, or",
			"    (at your option) any later version.",
			"",
			"    IcsGUI is distributed in the hope that it will be useful,",
			"    but WITHOUT ANY WARRANTY; without even the implied warranty of",
			"    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the",
			"    GNU General Public License for more details.",
			"",
			"    You should have received a copy of the GNU General Public License",
			"    along with IcsGUI; if not, write to the Free Software",
			"    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA",
			"",
			"*/",
		};
		for(i=0;i<licenseList.length;i++)
		{
			os.println(licenseList[i]);
		}
	}

	/**
	 * Write a declaration for the field. This declaration usually the same class and name
	 * as the message field's class and name. If the declaration is for ngat.phase2.InstrumentConfig,
	 * a config properties is declared as well.
	 * This method also set configMethods to true if a field of ngat.phase2.InstrumentConfig is encountered.
	 * This allows us to write several extra special methods to the source code.
	 * @param f The field to write the declaration for.
	 * @param os The output stream to write the declaration to.
	 * @see #configMethods
	 */
	private void writeFieldDeclaration(Field f,PrintStream os)
	{
		os.println("\t/**");
		os.println("\t * Dialog field:"+f.getName()+" of type "+
			getUnqualifiedClassName(f.getType().getName())+".");
		os.println("\t */");
		os.println("\tprivate "+getUnqualifiedClassName(f.getType().getName())+" "+f.getName()+";");
		if(f.getType().getName().equals("ngat.phase2.InstrumentConfig"))
		{
			os.println("\t/**");
			os.println("\t * "+f.getName()+"Properties is a list of configs to select from.");
			os.println("\t */");
			os.println("\tprivate IcsGUIConfigProperties "+f.getName()+"Properties;");
			os.println("\t/**");
			os.println("\t * icsGUIStatus is a reference to the IcsGUIStatus object with properties.");
			os.println("\t */");
			os.println("\tprivate IcsGUIStatus icsGUIStatus;");
			configMethods = true;
		}
	}

	/**
	 * Write an input field declaration for the field. This involves looking at the class of
	 * the field and choosing a suitable input field.
	 * @param f The field to write the declaration for.
	 * @param os The output stream to write the declaration to.
	 * @exception IllegalArgumentException Thrown if the field is of a class that we haven't written
	 * 	a handler for.
	 * @see #writeInputFieldConstruct
	 */
	private void writeInputFieldDeclaration(Field f,PrintStream os) throws IllegalArgumentException
	{
		os.println("\t/**");
		os.println("\t * Input field for "+f.getName()+".");
		os.println("\t */");
		if(f.getName().equals("arm")||
		   f.getName().equals("resolution")||
		   f.getName().equals("acquisitionMode")||
		   f.getName().equals("lamp"))
		{
			os.println("\tprivate JComboBox "+f.getName()+"ComboBox;");
		}
		else if(f.getType().getName().equals("java.lang.String")||
			(f.getType().getName().equals("int"))||(f.getType().getName().equals("long"))||
			(f.getType().getName().equals("float"))||(f.getType().getName().equals("double"))||
			(f.getType().getName().equals("java.util.Date")))
		{
			os.println("\tprivate JTextField "+f.getName()+"TextField;");
		}
		else if (f.getType().getName().equals("boolean"))
		{
			os.println("\tprivate JCheckBox "+f.getName()+"CheckBox;");
		}
		else if (f.getType().getName().equals("ngat.phase2.InstrumentConfig"))
		{
			os.println("\tprivate JTextField "+f.getName()+"TextField;");
			os.println("\t/**");
			os.println("\t * Button used to call up list for "+f.getName()+".");
			os.println("\t */");
			os.println("\tprivate JButton configButton;");
		}
		else 
			throw new IllegalArgumentException("writeInputFieldDeclaration: field "+f.getName()+
				" has unknown type "+f.getType().getName());
	}

	/**
	 * Write the code to construct the input field associated with f to the print stream.
	 * The input field was declared in writeInputFieldDeclaration.
	 * This must be constrcuted and added to the panel. The panel has a GridLayout with 2
	 * grid positions, so a blank position sometimes needs to be added.
	 * @param f The field to write the code for.
	 * @param os The output stream to write the code to.
	 * @exception IllegalArgumentException Thrown if the field is of a class that we haven't written
	 * 	a handler for.
	 * @see #writeInputFieldDeclaration
	 */
	private void writeInputFieldConstruct(Field f,PrintStream os) throws IllegalArgumentException
	{
		if(f.getName().equals("arm"))
		{
			os.println("\t// "+f.getName());
			os.println("\t\tlabel = new JLabel(\""+getLabelName(f.getName())+"\");");
			os.println("\t\tpanel.add(label);");
			os.println("\t\t"+f.getName()+"ComboBox = new JComboBox();");
			os.println("\t\t"+f.getName()+"ComboBox.setEditable(false);");
			os.println("\t\t"+f.getName()+"ComboBox.addItem(\"red\");");
			os.println("\t\t"+f.getName()+"ComboBox.addItem(\"blue\");");
			os.println("\t\tpanel.add("+f.getName()+"ComboBox);");
		}
		else if (f.getName().equals("resolution"))
		{
			os.println("\t// "+f.getName());
			os.println("\t\tlabel = new JLabel(\""+getLabelName(f.getName())+"\");");
			os.println("\t\tpanel.add(label);");
			os.println("\t\t"+f.getName()+"ComboBox = new JComboBox();");
			os.println("\t\t"+f.getName()+"ComboBox.setEditable(false);");
			os.println("\t\t"+f.getName()+"ComboBox.addItem(\"low\");");
			os.println("\t\t"+f.getName()+"ComboBox.addItem(\"high\");");
			os.println("\t\tpanel.add("+f.getName()+"ComboBox);");
		}
		else if (f.getName().equals("acquisitionMode"))
		{
			os.println("\t// "+f.getName());
			os.println("\t\tlabel = new JLabel(\""+getLabelName(f.getName())+"\");");
			os.println("\t\tpanel.add(label);");
			os.println("\t\t"+f.getName()+"ComboBox = new JComboBox();");
			os.println("\t\t"+f.getName()+"ComboBox.setEditable(false);");
			os.println("\t\t"+f.getName()+"ComboBox.addItem(\"none\");");
			os.println("\t\t"+f.getName()+"ComboBox.addItem(\"brightest\");");
			os.println("\t\t"+f.getName()+"ComboBox.addItem(\"wcs\");");
			os.println("\t\tpanel.add("+f.getName()+"ComboBox);");
		}
		else if (f.getName().equals("lamp"))
		{
			os.println("\t// "+f.getName());
			os.println("\t\tlabel = new JLabel(\""+getLabelName(f.getName())+"\");");
			os.println("\t\tpanel.add(label);");
			os.println("\t\t"+f.getName()+"ComboBox = new JComboBox();");
			os.println("\t\t"+f.getName()+"ComboBox.setEditable(true);");
			os.println("\t\t"+f.getName()+"ComboBox.addItem(\"Ne\");");
			os.println("\t\t"+f.getName()+"ComboBox.addItem(\"Xe\");");
			os.println("\t\t"+f.getName()+"ComboBox.addItem(\"W\");");
			os.println("\t\t"+f.getName()+"ComboBox.addItem(\"Ne Xe\");");
			os.println("\t\tpanel.add("+f.getName()+"ComboBox);");
		}
		else if(f.getType().getName().equals("java.lang.String")||
			(f.getType().getName().equals("int"))||(f.getType().getName().equals("long"))||
			(f.getType().getName().equals("float"))||(f.getType().getName().equals("double"))||
			(f.getType().getName().equals("java.util.Date")))
		{
			os.println("\t// "+f.getName());
			os.println("\t\tlabel = new JLabel(\""+getLabelName(f.getName())+"\");");
			os.println("\t\tpanel.add(label);");
			os.println("\t\t"+f.getName()+"TextField = new JTextField();");
			os.println("\t\tpanel.add("+f.getName()+"TextField);");
		}
		else if (f.getType().getName().equals("boolean"))
		{
			os.println("\t// "+f.getName());
			os.println("\t\t"+f.getName()+"CheckBox = new JCheckBox(\""+getLabelName(f.getName())+
				"\",false);");
			os.println("\t\tpanel.add("+f.getName()+"CheckBox);");
		// checkbox is both label and control - add an empty label to satisfy GridLayout
			os.println("\t\tlabel = new JLabel(\"\");");
			os.println("\t\tpanel.add(label);");
		}
		else if(f.getType().getName().equals("ngat.phase2.InstrumentConfig"))
		{
			os.println("\t// "+f.getName());
			os.println("\t\tconfigButton = new JButton(\""+getLabelName(f.getName())+"\");");
			os.println("\t\tpanel.add(configButton);");
			os.println("\t\t"+f.getName()+"TextField = new JTextField();");
			os.println("\t\t"+f.getName()+"TextField.setEditable(false);");
			os.println("\t\tpanel.add("+f.getName()+"TextField);");
		}
		else
			throw new IllegalArgumentException("writeInputFieldConstruct: field "+f.getName()+
				" has unknown type "+f.getType().getName());
	}

	/**
	 * Write the code to get a value from the Input Field swing box and put it into the input field.
	 * @param f The field to write the code for.
	 * @param os The output stream to write the code to.
	 * @exception IllegalArgumentException Thrown if the field is of a class that we haven't written
	 * 	a handler for.
	 * @see #writeInputFieldDeclaration
	 * @see #writeInputFieldConstruct
	 */
	private void writeInputFieldGet(Field f,PrintStream os) throws IllegalArgumentException
	{
		os.println("\t\t// "+f.getName());
		if(f.getName().equals("arm"))
		{
			os.println("\t\t\tString "+f.getName()+"String = null;");
			os.println("\t\t\t"+f.getName()+"String = (String)("+f.getName()+
				   "ComboBox.getSelectedItem());");
			os.println("\t\t\tif("+f.getName()+"String.equals(\"red\"))");
			os.println("\t\t\t\t"+f.getName()+" = FrodoSpecConfig.RED_ARM;");
			os.println("\t\t\telse if("+f.getName()+"String.equals(\"blue\"))");
			os.println("\t\t\t\t"+f.getName()+" = FrodoSpecConfig.BLUE_ARM;");
			os.println("\t\t\telse");
			os.println("\t\t\t\tJOptionPane.showMessageDialog((Component)null,(Object)(\""+
				getLabelName(f.getName())+
				":\"+\n\t\t\t\t\t"+
				f.getName()+
				"ComboBox.getSelectedItem()+\n\t\t\t\t\t\" not a valid arm.\"),"+
				"\" Input Error \",JOptionPane.ERROR_MESSAGE);");
		}
		else if(f.getName().equals("resolution"))
		{
			os.println("\t\t\tString "+f.getName()+"String = null;");
			os.println("\t\t\t"+f.getName()+"String = (String)("+f.getName()+
				   "ComboBox.getSelectedItem());");
			os.println("\t\t\tif("+f.getName()+"String.equals(\"low\"))");
			os.println("\t\t\t\t"+f.getName()+" = FrodoSpecConfig.RESOLUTION_LOW;");
			os.println("\t\t\telse if("+f.getName()+"String.equals(\"high\"))");
			os.println("\t\t\t\t"+f.getName()+" = FrodoSpecConfig.RESOLUTION_HIGH;");
			os.println("\t\t\telse");
			os.println("\t\t\t\tJOptionPane.showMessageDialog((Component)null,(Object)(\""+
				getLabelName(f.getName())+
				":\"+\n\t\t\t\t\t"+
				f.getName()+
				"ComboBox.getSelectedItem()+\n\t\t\t\t\t\" not a valid resolution.\"),"+
				"\" Input Error \",JOptionPane.ERROR_MESSAGE);");
		}
		else if(f.getName().equals("acquisitionMode"))
		{
			os.println("\t\t\tString "+f.getName()+"String = null;");
			os.println("\t\t\t"+f.getName()+"String = (String)("+f.getName()+
				   "ComboBox.getSelectedItem());");
			os.println("\t\t\tif("+f.getName()+"String.equals(\"none\"))");
			os.println("\t\t\t\t"+f.getName()+" = TelescopeConfig.ACQUIRE_MODE_NONE;");
			os.println("\t\t\telse if("+f.getName()+"String.equals(\"brightest\"))");
			os.println("\t\t\t\t"+f.getName()+" = TelescopeConfig.ACQUIRE_MODE_BRIGHTEST;");
			os.println("\t\t\telse if("+f.getName()+"String.equals(\"wcs\"))");
			os.println("\t\t\t\t"+f.getName()+" = TelescopeConfig.ACQUIRE_MODE_WCS;");
			os.println("\t\t\telse");
			os.println("\t\t\t\tJOptionPane.showMessageDialog((Component)null,(Object)(\""+
				getLabelName(f.getName())+
				":\"+\n\t\t\t\t\t"+
				f.getName()+
				"ComboBox.getSelectedItem()+\n\t\t\t\t\t\" not a valid mode.\"),"+
				"\" Input Error \",JOptionPane.ERROR_MESSAGE);");
		}
		else if(f.getName().equals("lamp"))
		{
			os.println("\t\t\t"+f.getName()+" = new "+getUnqualifiedClassName(f.getType().getName())+
				"((String)("+f.getName()+"ComboBox.getSelectedItem()));");
		}
		else if(f.getName().equals("RA"))
		{
			// parse RA into decimal radians using Position.parseHMS
			os.println("\t\t\ttry");
			os.println("\t\t\t{");
			os.println("\t\t\t\t"+f.getName()+" = Position.parseHMS("+f.getName()+
				"TextField.getText());");
			os.println("\t\t\t}");
			os.println("\t\t\tcatch(ParseException e)");
			os.println("\t\t\t{");
			os.println("\t\t\t\tJOptionPane.showMessageDialog((Component)null,(Object)(\""+
				getLabelName(f.getName())+
				":\"+\n\t\t\t\t\t"+
				f.getName()+
				"TextField.getText()+\n\t\t\t\t\t\" not a Right Ascension.\"),"+
				"\" Input Error \",JOptionPane.ERROR_MESSAGE);");
			os.println("\t\t\t\treturn;");
			os.println("\t\t\t}");
		}
		else if(f.getName().equals("dec"))
		{
			// parse devc into decimal radians using Position.parseDMS
			os.println("\t\t\ttry");
			os.println("\t\t\t{");
			os.println("\t\t\t\t"+f.getName()+" = Position.parseDMS("+f.getName()+
				"TextField.getText());");
			os.println("\t\t\t}");
			os.println("\t\t\tcatch(ParseException e)");
			os.println("\t\t\t{");
			os.println("\t\t\t\tJOptionPane.showMessageDialog((Component)null,(Object)(\""+
				getLabelName(f.getName())+
				":\"+\n\t\t\t\t\t"+
				f.getName()+
				"TextField.getText()+\n\t\t\t\t\t\" not a Declination.\"),"+
				"\" Input Error \",JOptionPane.ERROR_MESSAGE);");
			os.println("\t\t\t\treturn;");
			os.println("\t\t\t}");
		}
		else if(f.getType().getName().equals("int"))
		{
			os.println("\t\t\tInteger "+f.getName()+"Int = null;");
			os.println("\t\t\ttry");
			os.println("\t\t\t{");
			os.println("\t\t\t\t"+f.getName()+"Int = Integer.valueOf("+f.getName()+
				"TextField.getText());");
			os.println("\t\t\t}");
			os.println("\t\t\tcatch(NumberFormatException e)");
			os.println("\t\t\t{");
			os.println("\t\t\t\tJOptionPane.showMessageDialog((Component)null,(Object)(\""+
				getLabelName(f.getName())+
				":\"+\n\t\t\t\t\t"+
				f.getName()+
				"TextField.getText()+\n\t\t\t\t\t\" not an integer value.\"),"+
				"\" Input Error \",JOptionPane.ERROR_MESSAGE);");
			os.println("\t\t\t\treturn;");
			os.println("\t\t\t}");
			os.println("\t\t\t"+f.getName()+" = "+f.getName()+"Int.intValue();");
		}
		else if(f.getType().getName().equals("long"))
		{
			os.println("\t\t\tLong "+f.getName()+"Long = null;");
			os.println("\t\t\ttry");
			os.println("\t\t\t{");
			os.println("\t\t\t\t"+f.getName()+"Long = Long.valueOf("+f.getName()+
				"TextField.getText());");
			os.println("\t\t\t}");
			os.println("\t\t\tcatch(NumberFormatException e)");
			os.println("\t\t\t{");
			os.println("\t\t\t\tJOptionPane.showMessageDialog((Component)null,(Object)(\""+
				getLabelName(f.getName())+
				":\"+\n\t\t\t\t\t"+
				f.getName()+
				"TextField.getText()+\n\t\t\t\t\t\" not a long value.\"),"+
				"\" Input Error \",JOptionPane.ERROR_MESSAGE);");
			os.println("\t\t\t\treturn;");
			os.println("\t\t\t}");
			os.println("\t\t\t"+f.getName()+" = "+f.getName()+"Long.longValue();");
		}
		else if(f.getType().getName().equals("double"))
		{
			os.println("\t\t\tDouble "+f.getName()+"Double = null;");
			os.println("\t\t\ttry");
			os.println("\t\t\t{");
			os.println("\t\t\t\t"+f.getName()+"Double = Double.valueOf("+f.getName()+
				"TextField.getText());");
			os.println("\t\t\t}");
			os.println("\t\t\tcatch(NumberFormatException e)");
			os.println("\t\t\t{");
			os.println("\t\t\t\tJOptionPane.showMessageDialog((Component)null,(Object)(\""+
				getLabelName(f.getName())+
				":\"+\n\t\t\t\t\t"+
				f.getName()+
				"TextField.getText()+\n\t\t\t\t\t\" not a double value.\"),"+
				"\" Input Error \",JOptionPane.ERROR_MESSAGE);");
			os.println("\t\t\t\treturn;");
			os.println("\t\t\t}");
			os.println("\t\t\t"+f.getName()+" = "+f.getName()+"Double.doubleValue();");
		}
		else if(f.getType().getName().equals("float"))
		{
			os.println("\t\t\tFloat "+f.getName()+"Float = null;");
			os.println("\t\t\ttry");
			os.println("\t\t\t{");
			os.println("\t\t\t\t"+f.getName()+"Float = Float.valueOf("+f.getName()+
				"TextField.getText());");
			os.println("\t\t\t}");
			os.println("\t\t\tcatch(NumberFormatException e)");
			os.println("\t\t\t{");
			os.println("\t\t\t\tJOptionPane.showMessageDialog((Component)null,(Object)(\""+
				getLabelName(f.getName())+
				":\"+\n\t\t\t\t\t"+
				f.getName()+
				"TextField.getText()+\n\t\t\t\t\t\" not a float value.\"),"+
				"\" Input Error \",JOptionPane.ERROR_MESSAGE);");
			os.println("\t\t\t\treturn;");
			os.println("\t\t\t}");
			os.println("\t\t\t"+f.getName()+" = "+f.getName()+"Float.floatValue();");
		}
		else if(f.getType().getName().equals("boolean"))
		{
			os.println("\t\t\t"+f.getName()+" = "+f.getName()+"CheckBox.isSelected();");
		}
		else if(f.getType().getName().equals("java.util.Date"))
		{
			os.println("\t\t\tDateFormat "+f.getName()+
				"DF = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.LONG);");
			os.println("\t\t\ttry");
			os.println("\t\t\t{");
			os.println("\t\t\t\t"+f.getName()+"DF.setLenient(true);");
			os.println("\t\t\t\t"+f.getName()+" = "+f.getName()+"DF.parse("+f.getName()+
				"TextField.getText());");
			os.println("\t\t\t}");
			os.println("\t\t\tcatch(ParseException e)");
			os.println("\t\t\t{");
			os.println("\t\t\t\tJOptionPane.showMessageDialog((Component)null,(Object)(\""+
				getLabelName(f.getName())+
				":\"+\n\t\t\t\t\t"+
				f.getName()+
				"TextField.getText()+\n\t\t\t\t\t\" not a date "+
				"(e.g. \"+"+f.getName()+"DF.format(new Date())+\").\"),"+
				"\" Input Error \",JOptionPane.ERROR_MESSAGE);");
			os.println("\t\t\t\treturn;");
			os.println("\t\t\t}");
		}
		else if(f.getType().getName().equals("ngat.phase2.InstrumentConfig"))
		{
		// setup nameId integer for config id
			os.println("\t\t\tint "+f.getName()+"Id = -1;");
		// get nameId from textfield via nameProperties.getIdFromName (IcsGUIConfigProperties method)
			os.println("\t\t\ttry");
			os.println("\t\t\t{");
			os.println("\t\t\t\t"+f.getName()+"Id = "+f.getName()+"Properties.getIdFromName("+
				f.getName()+"TextField.getText());");
			os.println("\t\t\t}");
			os.println("\t\t\tcatch(IllegalArgumentException e)");
			os.println("\t\t\t{");
			os.println("\t\t\t\tJOptionPane.showMessageDialog((Component)null,(Object)(\""+
				getLabelName(f.getName())+
				":\"+\n\t\t\t\t\t"+
				f.getName()+
				"TextField.getText()+\n\t\t\t\t\t\" not a valid configuration.\"),"+
				"\" Input Error \",JOptionPane.ERROR_MESSAGE);");
			os.println("\t\t\t\treturn;");
			os.println("\t\t\t}");
		// construct InstrumentConfig from nameId via nameProperties.getConfigById 
		// (IcsGUIConfigProperties method)
			os.println("\t\t\ttry");
			os.println("\t\t\t{");
			os.println("\t\t\t\t"+f.getName()+" = "+f.getName()+"Properties.getConfigById("+
				f.getName()+"Id);");
			os.println("\t\t\t}");
			os.println("\t\t\tcatch(NumberFormatException e)");
			os.println("\t\t\t{");
			os.println("\t\t\t\tJOptionPane.showMessageDialog((Component)null,(Object)(\""+
				getLabelName(f.getName())+
				":\"+\n\t\t\t\t\t"+
				f.getName()+
				"TextField.getText()+\n\t\t\t\t\t\" does not contain a valid configuration.\\n\"+e),"+
				"\n\t\t\t\t\t\" Input Error \",JOptionPane.ERROR_MESSAGE);");
			os.println("\t\t\t\treturn;");
			os.println("\t\t\t}");
		}
		else if(f.getType().getName().equals("java.lang.String"))
		{
			os.println("\t\t\t"+f.getName()+" = new "+getUnqualifiedClassName(f.getType().getName())+
				"("+f.getName()+"TextField.getText());");
		}
		else
			throw new IllegalArgumentException("writeInputFieldGet: field "+f.getName()+
				" has unknown type "+f.getType().getName());
	}

	/**
	 * Write the code to set a field in the COMMAND object from the dialog's field.
	 * @param f The field to write the code for.
	 * @param os The output stream to write the code to.
	 * @see #writeInputFieldDeclaration
	 * @see #writeInputFieldConstruct
	 * @see #writeInputFieldGet
	 */
	private void writeCommandFieldSet(Field f,PrintStream os)
	{
		String methodName = null;
		char firstChar;

		if(f.getName().equals("id"))// done in the constructor
			return;
		firstChar = f.getName().charAt(0);// first character of field name
		firstChar = Character.toUpperCase(firstChar);// is uppercase
		methodName = new String("set"+firstChar+f.getName().substring(1));
		os.println("\t\t\tcommand."+methodName+"("+f.getName()+");");
	}

	/**
	 * Get an unqualified class name for the passed in string.
	 * @param s The class name.
	 * @return The unqualified class name for the passed in string.
	 */
	private String getUnqualifiedClassName(String s)
	{
		int index = 0;

		index = s.lastIndexOf(".");
		if(index < 0)
			index = 0;// there was no dot, start from the start
		else
			index++; // we don't want the '.'
		return new String(s.substring(index,s.length()));
	}

	/**
	 * Method to get a label name given a field name.
	 * @param fieldName The name of the field to get the label name for.
	 * @return The label name for a given field name.
	 * @see #labelHashtable
	 */
	private String getLabelName(String fieldName)
	{
		String labelName = null;

		labelName = (String)labelHashtable.get(fieldName);
		if(labelName == null)
		{
			System.err.println(this.getClass().getName()+":getLabelName:No label found for:"+fieldName);
			labelName = fieldName;
		}
		return new String(labelName);
	}

	/**
	 * Close the output stream, if we opened it.
	 * @throws IOException Thrown if we fail to close the fileOutputStream.
	 * @see #fileOutputStream
	 */
	public void closeOutput() throws IOException
	{
		if(fileOutputStream != null)
		{
			fileOutputStream.close();
		}
	}

	/**
	 * Help message routine.
	 */
	private void help()
	{
		System.out.println(this.getClass().getName()+" help:");
		System.out.println("java "+this.getClass().getName()+" -c[lass] <class name>");
		System.out.println("The class name must be specified.");
	}

}
//
// $Log: not supported by cvs2svn $
// Revision 0.29  2020/04/29 13:45:04  cjm
// Added extra acquire fields.
//
// Revision 0.28  2014/10/02 10:50:06  cjm
// Added threshold.
//
// Revision 0.27  2010/01/15 11:36:35  cjm
// Added totalDuration mapping.
//
// Revision 0.26  2008/11/13 15:39:34  cjm
// Fixed actionPerformed generated comments.
//
// Revision 0.25  2008/02/29 14:48:31  cjm
// lamp now selected from drop down combo box.
//
// Revision 0.24  2007/11/01 14:34:02  cjm
// Added acquisitionMode ACQUIRE handling / ComboBoxs.
//
// Revision 0.23  2007/08/03 14:30:34  cjm
// Added arm and resolution for FRODOSPEC.
// Added new Acquire parameters.
// arm and resolution use ComboBoxs.
// ACQUIRE RA amd Dec use ngat.astrometry.Position.parse[H|D]MS.
//
// Revision 0.22  2006/05/16 17:22:16  cjm
// Added writeGNULicense.
//
// Revision 0.21  2006/05/16 17:13:27  cjm
// gnuify: Added GNU General Public License.
//
// Revision 0.20  2004/05/12 22:32:01  cjm
// Fixed configTextField bug in setConfigTextField CONFIG method.
//
// Revision 0.19  2004/04/26 11:19:58  cjm
// Changed the way getFields orders fields in the resultant field-list.
// They are ordered super-class->sub-class, but with fields withjin each class
// in declared order.
//
// writeDialog now creates field widgetry in the same order as fieldList.
// This puts fields in the dialog in a sensible order.
//
// Revision 0.18  2003/09/19 14:07:38  cjm
// Made CreateDialog add extra methods to get ICS status object in the
// config dialog.
//
// Revision 0.17  2003/08/20 15:53:41  cjm
// Changed Time to Complete and Exposure Time labels to include (ms)
// so that the user knows the values are in milliseconds.
//
// Revision 0.16  2002/12/10 16:57:16  cjm
// Fixed import statement.
//
// Revision 0.15  2002/12/10 16:34:45  cjm
// Converted ngat.phase2.nonpersist.NPInstrumentConfig to ngat.phase2.InstrumentConfig.
// Removed -*- mode: Fundamental;-*-.
// Added protection against final or static (class) fields having input boxes.
//
// Revision 0.14  2001/08/20 09:46:18  cjm
// Added error checking on field types (classes), and
// added Long input code.
//
// Revision 0.13  2001/08/02 14:18:43  cjm
// Added new labels for DAY_CALIBRATE, TWILIGHT_CALIBRATE and SKYFLAT.
//
// Revision 0.12  2001/05/04 18:25:21  cjm
// Changed config label.
//
// Revision 0.11  2001/05/01 09:28:15  cjm
// Added Exposure standard field name translation.
//
// Revision 0.10  2000/12/20 17:58:27  cjm
// Added error information to message dialog.
//
// Revision 0.9  2000/12/01 18:42:59  cjm
// New Config using nonpersist.
//
// Revision 0.8  2000/11/24 11:36:50  cjm
// Fixed for new CONFIG field ngat.phase2.nonpersist.NPInstrumentConfig.
//
// Revision 0.7  2000/11/24 10:13:06  cjm
// CONFIG now uses class ngat.phase2.nonpersist.NPInstrumentConfig.
//
// Revision 0.6  2000/08/10 16:23:37  cjm
// Added TELFOCUS labels to hashtable.
//
// Revision 0.5  2000/07/14 16:26:44  cjm
// Backup.
//
// Revision 0.4  2000/07/12 14:10:33  cjm
// Added setResizable call to stop dialogs being resizable.
//
// Revision 0.3  1999/12/10 14:55:10  cjm
// backup.
//
// Revision 0.2  1999/11/22 10:58:45  cjm
// Removed RCS stuff from generated source file, they will not be put in RCS and it just picks up
// CreateDialog RCS stuff instead.
//
// Revision 0.1  1999/11/22 09:51:30  cjm
// initial revision.
//
//
