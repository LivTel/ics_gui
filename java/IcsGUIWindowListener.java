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
// CcsGUIWindowListener
// $Header: /home/cjm/cvs/ics_gui/java/IcsGUIWindowListener.java,v 0.3 2006-05-16 17:12:20 cjm Exp $
import java.lang.*;

import java.awt.event.*;

/**
 * This class is a sub-class of WindowAdapter. An instance of this class is attached to the IcsGUI
 * main window to catch windowClosing events. This calls IcsGUI's exit method to ensure the program
 * terminates nicely.
 * @see IcsGUI#exit
 * @author Chris Mottram
 * @version $Revision: 0.3 $
 */
public class CcsGUIWindowListener extends WindowAdapter
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: IcsGUIWindowListener.java,v 0.3 2006-05-16 17:12:20 cjm Exp $");
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
// Revision 0.2  2003/09/19 14:08:45  cjm
// Changed CcsGUI to IcsGUI.
//
// Revision 0.1  1999/11/30 11:37:23  cjm
// initial revision.
//
//
