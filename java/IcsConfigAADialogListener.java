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
// CcsConfigAADialogListener.java -*- mode: Fundamental;-*-
// $Header: /home/cjm/cvs/ics_gui/java/IcsConfigAADialogListener.java,v 0.3 2008-11-13 15:43:21 cjm Exp $
import java.lang.*;
import java.io.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import ngat.message.base.*;

/**
 * This is an interface, that is used for callbacks when a Ccs Config Dialog callback is needed.
 */
public interface CcsConfigAADialogListener
{
	/**
	 * Method called when Ok or Cancel is pressed.
	 * @param ok The dialog returns true if the Ok button was pressed, otherwise false.
	 * @param id If ok is true, the dialog will return the id of the configuration added/amended.
	 */
	public abstract void actionPerformed(boolean ok,int id);
}
//
// $Log: not supported by cvs2svn $
// Revision 0.2  2006/05/16 17:12:11  cjm
// gnuify: Added GNU General Public License.
//
// Revision 0.1  1999/12/08 16:32:59  cjm
// initial revision.
//
//
