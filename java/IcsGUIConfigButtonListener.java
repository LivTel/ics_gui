// CcsGUIConfigButtonListener.java
// $Header: /home/cjm/cvs/ics_gui/java/IcsGUIConfigButtonListener.java,v 0.4 2003-11-14 15:02:12 cjm Exp $
import java.lang.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * This class is an ActionListener for the IcsGUI CONFIGDialog config button. It should bring up a 
 * IcsGUIConfigListDialog, and any selected configs should go back to the CONFIGDialog.
 * @author Chris Mottram
 * @version $Revision: 0.4 $
 */
public class CcsGUIConfigButtonListener implements ActionListener
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: IcsGUIConfigButtonListener.java,v 0.4 2003-11-14 15:02:12 cjm Exp $");
	/**
	 * The instance of the main program.
	 */
	private IcsGUI parent = null;
	/**
	 * The CONFIGDialog that wants to bring up the IcsGUIConfigListDialog.
	 */
	private CONFIGDialog configDialog = null;
	/**
	 * The list dialog to be brought up when the button is pressed.
	 */
	private IcsGUIConfigListDialog configListDialog = null;

	/**
	 * Constructor. This sets the parent to be main program class.
	 * It constructs the IcsGUIConfigListDialog to be made visible when the button is pressed.
	 * @param p The parent object which owns the menu items.
	 * @see #parent
	 * @see #configListDialog
	 */
	public CcsGUIConfigButtonListener(IcsGUI p,CONFIGDialog cd)
	{
		super();

		parent = p;
		configDialog = cd;
		configListDialog = new IcsGUIConfigListDialog(p.getFrame(),p.getStatus().
								getInstrumentConfigProperties(),p);
	}


	/**
	 * Routine that is called when the config button is pressed.
	 * @param event The event that caused this call to the listener.
	 */
	public void actionPerformed(ActionEvent event)
	{
		configListDialog.setLocation(configDialog.getX()+configDialog.getWidth(),configDialog.getY());
		configListDialog.setConfigDialog(configDialog);
		configListDialog.setData();
		configListDialog.pack();
		configListDialog.setVisible(true);
	}
}
//
// $Log: not supported by cvs2svn $
// Revision 0.3  2003/09/19 14:08:45  cjm
// Changed CcsGUI to IcsGUI.
//
// Revision 0.2  2000/11/30 18:47:44  cjm
// Made generic for other instruments.
//
// Revision 0.1  1999/12/08 10:48:29  cjm
// initial revision.
//
//
