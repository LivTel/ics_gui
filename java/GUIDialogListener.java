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
// GUIMenuItemListener.java -*- mode: Fundamental;-*-
// $Header: /home/cjm/cvs/ics_gui/java/GUIDialogListener.java,v 0.2 2006-05-16 17:12:22 cjm Exp $
import java.lang.*;
import java.io.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import ngat.message.base.*;

/**
 * This is an interface, that is used for callbacks when a GUI Dialog callback is needed.
 */
public interface GUIDialogListener
{
	/**
	 * Method called when Ok or Cancel is pressed.
	 * @param ok The dialog returns true if the Ok button was pressed, otherwise false.
	 * @param command If ok is true, the dialog will return a constructed command object,
	 * 	ready to be sent to the server.
	 */
	public abstract void actionPerformed(boolean ok,COMMAND command);
}
//
// $Log: not supported by cvs2svn $
// Revision 0.1  1999/11/22 09:53:49  cjm
// initial revision.
//
//
