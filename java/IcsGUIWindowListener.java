// CcsGUIWindowListener
// $Header: /home/cjm/cvs/ics_gui/java/IcsGUIWindowListener.java,v 0.2 2003-09-19 14:08:45 cjm Exp $
import java.lang.*;

import java.awt.event.*;

/**
 * This class is a sub-class of WindowAdapter. An instance of this class is attached to the IcsGUI
 * main window to catch windowClosing events. This calls IcsGUI's exit method to ensure the program
 * terminates nicely.
 * @see IcsGUI#exit
 * @author Chris Mottram
 * @version $Revision: 0.2 $
 */
public class CcsGUIWindowListener extends WindowAdapter
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: IcsGUIWindowListener.java,v 0.2 2003-09-19 14:08:45 cjm Exp $");
	/**
	 * The main program class instance. The instances exit method is called when a windowClosing 
	 * event occurs.
	 */
	private IcsGUI parent = null;

	/**
	 * Constructor. The top-level class instance is passed in.
	 * @param p The main program instance.
	 * @see #parent
	 */
	public CcsGUIWindowListener(IcsGUI p)
	{
		parent = p;
	}

	/**
	 * Method called when a windowClosing event occurs. This just calls IcsGUI.exit(0).
	 * @param e The window event that caused this method to be called.
	 * @see #parent
	 * @see IcsGUI#exit
	 */
	public void windowClosing(WindowEvent e)
	{
		parent.exit(0);
	}
}
//
// $Log: not supported by cvs2svn $
// Revision 0.1  1999/11/30 11:37:23  cjm
// initial revision.
//
//
