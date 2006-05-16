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
// CcsCommandDialog.java -*- mode: Fundamental;-*-
// $Header: /home/cjm/cvs/ics_gui/java/IcsCommandDialog.java,v 0.2 2006-05-16 17:12:11 cjm Exp $

import java.awt.*;
import javax.swing.*;

/**
 * This class extends JDialog, adding functionality to support a GUIDialogListener,
 * which enables the send command dialogs to return something useful.
 * It is an abstract class, sub-classes must over-ride the <i>setID</i> method. 
 */
public abstract class CcsCommandDialog extends JDialog
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: IcsCommandDialog.java,v 0.2 2006-05-16 17:12:11 cjm Exp $");
	/**
	 * GUIDialogListener, holds a reference to callback when Ok/Cancel pressed.
	 */
	protected GUIDialogListener dialogListener = null;

	/**
	 * Constructor.
	 */
	public CcsCommandDialog(Frame owner,String title)
	{
		super(owner,title);
	}

	/**
	 * addGUIDialogListener, allows us to add an object to callback when Ok/Cancel pressed.
	 * @param l The GUIDialogListener to set as the listener.
	 * @see GUIDialogListener
	 */
	public void addGUIDialogListener(GUIDialogListener l)
	{
		dialogListener = l;
	}

	/**
	 * setID method. Sets id and associated Dialog Text Field.
	 * This method must be overridden in sub-classes.
	 * This method assumes all ngat.message.ISS_INST sub-classes have an id field
	 * (they do: it is always inherited from COMMAND).
	 * @param s The id string.
	 */
	public abstract void setID(String s);
}
//
// $Log: not supported by cvs2svn $
// Revision 0.1  1999/11/22 09:53:49  cjm
// initial revision.
//
//
