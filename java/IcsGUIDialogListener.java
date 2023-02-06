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
// IcsGUIDialogListener.java
// $Header: /home/cjm/cvs/ics_gui/java/IcsGUIDialogListener.java,v 0.5 2020-05-05 10:20:39 cjm Exp $
import java.lang.*;
import java.io.*;
import java.util.*;

import java.awt.event.*;

import ngat.message.base.*;

/**
 * This class is an GUIDialogListener for the IcsGUI Dialog boxs.
 * @author Chris Mottram
 * @version $Revision: 11749d7163e9a863d1e19d52699a0684487922d0 $
 */
public class IcsGUIDialogListener implements GUIDialogListener
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: IcsGUIDialogListener.java | Tue May 5 10:20:39 2020 +0000 | Chris Mottram  $");
	/**
	 * The main class reference.
	 */
	private IcsGUI parent = null;

	public IcsGUIDialogListener(IcsGUI p)
	{
		super();
		parent = p;
	}

	/**
	 * This method is called when Ok or Cancel button is pressed on a Dialog.
	 */
	public void actionPerformed(boolean ok,COMMAND command)
	{
		if(ok)
		{
			parent.log("Sending command:"+command.getClass().getName());
			parent.sendCommand(command);
		}
		else
			parent.log("Dialog cancelled.");
	}
}
//
// $Log: not supported by cvs2svn $
// Revision 0.4  2006/05/16 17:12:14  cjm
// gnuify: Added GNU General Public License.
//
// Revision 0.3  2003/09/19 14:08:45  cjm
// Changed CcsGUI to IcsGUI.
//
// Revision 0.2  1999/11/24 13:47:02  cjm
// Added author/version tags.
//
// Revision 0.1  1999/11/22 09:53:49  cjm
// initial revision.
//
//
