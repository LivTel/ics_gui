// TestAttributedTextArea.java
// $Header: /home/cjm/cvs/ics_gui/test/TestAttributedTextArea.java,v 1.1 2020-04-30 08:39:52 cjm Exp $
import java.lang.*;
import java.io.*;
import java.net.*;
import java.text.DecimalFormat;
import java.util.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.plaf.metal.*;

import ngat.message.base.*;
import ngat.swing.*;
import ngat.util.*;

/**
 * This class tests AttributedTextArea.
 * @author Chris Mottram
 * @version $Revision$
 */
public class TestAttributedTextArea
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id$");
	/**
	 * Text area for logging.
	 */
	private AttributedTextArea logTextArea = null;
	/**
	 * Top level frame for the program.
	 */
	private JFrame frame = null;

	public static void main(String args[])
	{
		TestAttributedTextArea tata = null;

		tata = new TestAttributedTextArea();

		tata.initGUI();
		tata.frame.pack();
		tata.frame.setVisible(true);
		tata.log("This is a test.");
		for(int i  = 0;i < args.length; i++)
		{
			tata.log("Argument "+i+" = "+args[i]+".",Color.green);
		}
		//System.exit(0);
	}

	/**
	 * Initialise the program.
	 * Creates the frame and creates the widgets associated with it.
	 * Creates the menu bar.
	 * @see #frame
	 * @see #initMenuBar
	 * @see #initMainPanel
	 */
	private void initGUI()
	{
		Image image = null;
		String titleString = null;

	// create the frame level layout manager
		GridBagLayout gridBagLayout = new GridBagLayout();
        	GridBagConstraints gridBagCon = new GridBagConstraints();
	// Create the top-level container.
		frame = new MinimumSizeFrame("TestAttributedTextArea",new Dimension(400,300));
	// set icon image
		//image = Toolkit.getDefaultToolkit().getImage(status.getProperty("ccs_gui.icon.filename"));
		//frame.setIconImage(image);

		frame.getContentPane().setLayout(gridBagLayout);
		/*
		 * An easy way to put space between a top-level container
		 * and its contents is to put the contents in a JPanel
		 * that has an "empty" border.
		 */
		JPanel panel = new JPanel();

		initMainPanel(panel);
	// Add the JPanel to the frame.
		gridBagCon.gridx = GridBagConstraints.RELATIVE;
		gridBagCon.gridy = GridBagConstraints.RELATIVE;
		gridBagCon.gridwidth = GridBagConstraints.REMAINDER;
		gridBagCon.gridheight = GridBagConstraints.REMAINDER;
		gridBagCon.fill = GridBagConstraints.BOTH;
		gridBagCon.weightx = 1.0;
		gridBagCon.weighty = 1.0;
		gridBagCon.anchor = GridBagConstraints.NORTHWEST;
		gridBagLayout.setConstraints(panel,gridBagCon);
		frame.getContentPane().add(panel);

	//Finish setting up the frame, and show it.
		//		frame.addWindowListener(new CcsGUIWindowListener(this));
	}

	/**
	 * Initialise the main panel. This consists of setting the panel layout, and then 
	 * adding the status panel and the log panel.
	 * @see #initLogPanel
	 */
	private void initMainPanel(JPanel panel)
	{
	// create the frame level layout manager
		GridBagLayout gridBagLayout = new GridBagLayout();
	// setup panel
		panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		panel.setLayout(gridBagLayout);
		panel.setMinimumSize(new Dimension(450,400));
		panel.setPreferredSize(new Dimension(450,400));
		panel.setMaximumSize(new Dimension(1024,1024));
	// log text area
		initLogPanel(panel,gridBagLayout);
	}

	/**
	 * Initialise log text area.
	 * Also initialises logTextAreaLineLimit, the maximum number of lines we are allowing in
	 * the log text area, by reading the number from the property file.
	 * @param panel The panel to put the status panel in.
	 * @param gridBagLayout The grid bag layout to set constraints for, before adding things
	 * 	to the panel.
	 * @see #logTextArea
	 * @see #logTextAreaLineLimit
	 */
	private void initLogPanel(JPanel panel,GridBagLayout gridBagLayout)
	{
		JScrollPane areaScrollPane = null;
        	GridBagConstraints gridBagCon = new GridBagConstraints();

		logTextArea = new AttributedTextArea("");
		logTextArea.setLineWrap(true);
		logTextArea.setWrapStyleWord(false);
		logTextArea.setEditable(false);

		areaScrollPane = new JScrollPane(logTextArea);
		areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPane.setMinimumSize(new Dimension(250,200));
		areaScrollPane.setPreferredSize(new Dimension(1024,800));
		areaScrollPane.setBorder(new TitledSmallerBorder("Log"));

		gridBagCon.gridx = 0;
		gridBagCon.gridy = 1;
		gridBagCon.gridwidth = GridBagConstraints.REMAINDER;
		gridBagCon.gridheight = GridBagConstraints.REMAINDER;
		gridBagCon.fill = GridBagConstraints.BOTH;
		gridBagCon.weightx = 1.0;
		gridBagCon.weighty = 1.0;
		gridBagLayout.setConstraints(areaScrollPane,gridBagCon);
		panel.add(areaScrollPane);
	}

	/**
	 * Routine to write the string, terminted with a new-line, to the current log-file.
	 * Also written to the log text area.
	 * The actual appending is done in the swing thread, using an ngat.swing.GUIAttributedTextAreaAppender.
	 * A ngat.swing.GUIAttributedTextAreaLengthLimiter is also invoked later, so that the logging text area
	 * does not get so long it swallows all the memory.
	 * @param s The string to write.
	 * @see #logTextArea
	 * @see #logStream
	 */
	public void log(String s)
	{
		if(logTextArea != null)
		{
			SwingUtilities.invokeLater(new GUIAttributedTextAreaAppender(logTextArea,s+"\n",Color.blue,
										     true));
			SwingUtilities.invokeLater(new GUIAttributedTextAreaLengthLimiter(logTextArea,
											  500,false));
		}
	}

	/**
	 * Routine to write the string, terminted with a new-line, to the current log-file.
	 * Also written to the log text area.
	 * The actual appending is done in the swing thread, using an ngat.swing.GUIAttributedTextAreaAppender.
	 * A ngat.swing.GUIAttributedTextAreaLengthLimiter is also invoked later, so that the logging text area
	 * does not get so long it swallows all the memory.
	 * @param s The string to write.
	 * @param c The colour of the text to write.
	 * @see #logTextArea
	 * @see #logStream
	 */
	public void log(String s,Color c)
	{
		if(logTextArea != null)
		{
			SwingUtilities.invokeLater(new GUIAttributedTextAreaAppender(logTextArea,s+"\n",c,true));
			SwingUtilities.invokeLater(new GUIAttributedTextAreaLengthLimiter(logTextArea,
											  500,false));
		}
	}

}
