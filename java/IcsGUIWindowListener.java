// CcsGUIWindowListener -*- mode: Fundamental;-*-
// $Header: /home/cjm/cvs/ics_gui/java/IcsGUIWindowListener.java,v 0.1 1999-11-30 11:37:23 cjm Exp $
import java.lang.*;

import java.awt.event.*;

/**
 * This class is a sub-class of WindowAdapter. An instance of this class is attached to the CcsGUI
 * main window to catch windowClosing events. This calls CcsGUI's exit method to ensure the program
 * terminates nicely.
 * @see CcsGUI#exit
 * @author Chris Mottram
 * @version $Revision: 0.1 $
 */
public class CcsGUIWindowListener extends WindowAdapter
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: IcsGUIWindowListener.java,v 0.1 1999-11-30 11:37:23 cjm Exp $");
	/**
	 * The main program class instance. The instances exit method is called when a windowClosing 
	 * event occurs.
	 */
	private CcsGUI parent = null;

	/**
	 * Constructor. The top-level class instance is passed in.
	 * @param p The main program instance.
	 * @see #parent
	 */
	public CcsGUIWindowListener(CcsGUI p)
	{
		parent = p;
	}

	/**
	 * Method called when a windowClosing event occurs. This just calls CcsGUI.exit(0).
	 * @param e The window event that caused this method to be called.
	 * @see #parent
	 * @see CcsGUI#exit
	 */
	public void windowClosing(WindowEvent e)
	{
		parent.exit(0);
	}
}
//
// $Log: not supported by cvs2svn $
//
