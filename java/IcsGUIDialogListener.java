// CcsGUIDialogListener.java -*- mode: Fundamental;-*-
// $Header: /home/cjm/cvs/ics_gui/java/IcsGUIDialogListener.java,v 0.2 1999-11-24 13:47:02 cjm Exp $
import java.lang.*;
import java.io.*;
import java.util.*;

import java.awt.event.*;

import ngat.message.base.*;

/**
 * This class is an GUIDialogListener for the CcsGUI Dialog boxs.
 * @author Chris Mottram
 * @version $Revision: 0.2 $
 */
public class CcsGUIDialogListener implements GUIDialogListener
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: IcsGUIDialogListener.java,v 0.2 1999-11-24 13:47:02 cjm Exp $");
	/**
	 * The main class reference.
	 */
	private CcsGUI parent = null;

	public CcsGUIDialogListener(CcsGUI p)
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
// Revision 0.1  1999/11/22 09:53:49  cjm
// initial revision.
//
//
