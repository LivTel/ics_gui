// CcsConfigAADialogListener.java -*- mode: Fundamental;-*-
// $Header: /home/cjm/cvs/ics_gui/java/IcsConfigAADialogListener.java,v 0.1 1999-12-08 16:32:59 cjm Exp $
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
	 * @param if If ok is true, the dialog will return the id of the configuration added/amended.
	 */
	public abstract void actionPerformed(boolean ok,int id);
}
//
// $Log: not supported by cvs2svn $
//
