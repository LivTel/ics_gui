// GUIMenuItemListener.java -*- mode: Fundamental;-*-
// $Header: /home/cjm/cvs/ics_gui/java/GUIDialogListener.java,v 0.1 1999-11-22 09:53:49 cjm Exp $
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
//
