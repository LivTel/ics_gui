// CcsCommandDialog.java -*- mode: Fundamental;-*-
// $Header: /home/cjm/cvs/ics_gui/java/IcsCommandDialog.java,v 0.1 1999-11-22 09:53:49 cjm Exp $

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
	public final static String RCSID = new String("$Id: IcsCommandDialog.java,v 0.1 1999-11-22 09:53:49 cjm Exp $");
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
//
