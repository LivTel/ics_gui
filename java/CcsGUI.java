// CcsGUI.java -*- mode: Fundamental;-*-
// $Header: /home/cjm/cvs/ics_gui/java/CcsGUI.java,v 0.2 1999-11-24 12:51:28 cjm Exp $
import java.lang.*;
import java.io.*;
import java.net.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import ngat.message.base.*;
import ngat.util.*;

/**
 * This class is the start point for the Ccs GUI.
 * @author Chris Mottram
 * @version $Revision: 0.2 $
 */
public class CcsGUI
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: CcsGUI.java,v 0.2 1999-11-24 12:51:28 cjm Exp $");
	/**
	 * The stream to write error messages to - defaults to System.err.
	 */
	private PrintWriter errorStream = new PrintWriter(System.err,true);
	/**
	 * The stream to write log messages to - defaults to System.out.
	 */
	private PrintWriter logStream = new PrintWriter(System.out,true);
	/**
	 * Top level frame for the program.
	 */
	private JFrame frame = null;
	private final String ccdStatusLabelString = new String("CCD Status:");
	private final String remainingExposureTimeLabelString = new String("Remaining Exposure Time:");
	private final String remainingExposuresLabelString = new String("Remaining Exposures:");
	private final String filterSelectedLabelString = new String("Filters Selected:");
	private final String ccdTemperatureLabelString = new String("CCD Temperature:");
	/**
	 * Label for last command sent.
	 */
	private JLabel ccdStatusLabel = null;
	/**
	 * Label for last acknowledge received.
	 */
	private JLabel remainingExposureTimeLabel = null;
	/**
	 * Label for last DONE message recieved: successful field.
	 */
	private JLabel remainingExposuresLabel = null;
	/**
	 * Label for last DONE message recieved: errorNum field.
	 */
	private JLabel filterSelectedLabel = null;
	/**
	 * Label for last DONE message recieved: errorString field.
	 */
	private JLabel ccdTemperatureLabel = null;
	/**
	 * Text area for logging.
	 */
	private JTextArea logTextArea = null;
	/**
	 * CcsGUI status information.
	 */
	private CcsGUIStatus status = null;
	/**
	 * The port number to send ccs commands to.
	 */
	private int ccsPortNumber = 0;
	/**
	 * The ip address of the machine the CCS is running on, to send CCS commands to.
	 */
	private InetAddress ccsAddress = null;
	/**
	 * The port number to listen for connections from the CCS on.
	 */
	private int issPortNumber = 0;
	/**
	 * Reference to the ISS server thread.
	 */
	private CcsGUIServer server = null;
	/**
	 * Thread scheduler.
	 */
	private CPUScheduler scheduler = null;

	public CcsGUI()
	{
	}

	/**
	 * The main routine, called when Ccs is executed.
	 */
	public static void main(String[] args)
	{
		UIManager.installLookAndFeel("Liverpool Telescope Look and Feel","LTLookAndFeel");
		try
		{
			UIManager.setLookAndFeel("LTLookAndFeel");
//			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		}
		catch(Exception e)
		{
			System.err.println("UIManager.setLookAndFeel failed:"+e);
		}
System.err.println(UIManager.getLookAndFeel());
		CcsGUI ccsGUI = new CcsGUI();
		try
		{
			ccsGUI.init();
		}
		catch(Exception e)
		{
			ccsGUI.error(e.toString());
			System.exit(1);
		}
		ccsGUI.run();
	}

	/**
	 * Initialise the program.
	 * Creates the status object and initialises it.
	 * Creates the frame and creates the widgets associated with it.
	 * Creates the menu bar.
	 * @see #frame
	 */
	private void init() throws FileNotFoundException,IOException,NumberFormatException,UnknownHostException
	{
		status = new CcsGUIStatus();
		status.load();

	// initialise port numbers from properties file
		try
		{
			ccsPortNumber = status.getPropertyInteger("ccs_gui.net.default_CCS_port_number");
			issPortNumber = status.getPropertyInteger("ccs_gui.net.default_ISS_port_number");
		}
		catch(NumberFormatException e)
		{
			error(this.getClass().getName()+":init:initialsing port number:"+e);
			throw e;
		}
	// initialise address's from properties file
		try
		{
			ccsAddress = InetAddress.getByName(status.getProperty("ccs_gui.net.default_CCS_address"));
		}
		catch(UnknownHostException e)
		{
			error(this.getClass().getName()+":illegal internet address:"+e);
			throw e;
		}
	// create the frame level layout manager
		GridBagLayout gridBagLayout = new GridBagLayout();
        	GridBagConstraints gridBagCon = new GridBagConstraints();
	//Create the top-level container.
		frame = new MinimumSizeFrame("CCS Interface",new Dimension(400,200));

		frame.getContentPane().setLayout(gridBagLayout);
		initMenuBar();
		/*
		 * An easy way to put space between a top-level container
		 * and its contents is to put the contents in a JPanel
		 * that has an "empty" border.
		 */
		JPanel panel = new JPanel();

		panel.setMinimumSize(new Dimension(250,400));
		panel.setPreferredSize(new Dimension(1280,1024));
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
		frame.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
// diddly more code here - see also menu item listener
//diddly			parent.exit(0);
				System.exit(0);
			}
		});
	}

	private void initMainPanel(JPanel panel)
	{
	// create the frame level layout manager
		GridBagLayout gridBagLayout = new GridBagLayout();
	// setup panel
		panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		panel.setLayout(gridBagLayout);
		panel.setMinimumSize(new Dimension(450,400));
		panel.setPreferredSize(new Dimension(1024,1024));
	//  status labels
		initStatusPanel(panel,gridBagLayout);
	// log text area
		initLogPanel(panel,gridBagLayout);
	}


	/**
	 * Initialise status area.
	 * @param panel The panel to put the status panel in.
	 * @param gridBagLayout The grid bag layout to set constraints for, before adding things
	 * 	to the panel.
	 */
	private void initStatusPanel(JPanel panel,GridBagLayout gridBagLayout)
	{
		JPanel lastStatusPanel = new JPanel();
		JLabel label = null;
        	GridBagConstraints gridBagCon = new GridBagConstraints();

		lastStatusPanel.setLayout(new GridLayout(0,2));
		lastStatusPanel.setMinimumSize(new Dimension(250,75));
		lastStatusPanel.setPreferredSize(new Dimension(1024,75));
		lastStatusPanel.setMaximumSize(new Dimension(1024,75));
	// ccd status
		label = new JLabel(ccdStatusLabelString);
		lastStatusPanel.add(label);
		ccdStatusLabel = new JLabel("Unknown");
		lastStatusPanel.add(ccdStatusLabel);
	// remaining exposure time
		label = new JLabel(remainingExposureTimeLabelString);
		lastStatusPanel.add(label);
		remainingExposureTimeLabel = new JLabel("0.000");
		lastStatusPanel.add(remainingExposureTimeLabel);
	// remaining exposures
		label = new JLabel(remainingExposuresLabelString);
		lastStatusPanel.add(label);
		remainingExposuresLabel = new JLabel("0");
		lastStatusPanel.add(remainingExposuresLabel);
	// selected filter
		label = new JLabel(filterSelectedLabelString);
		lastStatusPanel.add(label);
		filterSelectedLabel = new JLabel("Unknown");
		lastStatusPanel.add(filterSelectedLabel);
	// ccd temperature
		label = new JLabel(ccdTemperatureLabelString);
		lastStatusPanel.add(label);
		ccdTemperatureLabel = new JLabel("Unknown");
		lastStatusPanel.add(ccdTemperatureLabel);

		lastStatusPanel.setBorder(new TitledSmallerBorder("Status"));
	// these constraints mean that the GridBagLayout can't alter the size of lastStatusPanel
		gridBagCon.gridx = 0;
		gridBagCon.gridy = 0;
		gridBagCon.gridwidth = GridBagConstraints.REMAINDER;
		gridBagCon.gridheight = GridBagConstraints.RELATIVE;
		gridBagCon.fill = GridBagConstraints.HORIZONTAL;
		gridBagCon.weightx = 1.0;
		gridBagCon.weighty = 0.0;
		gridBagCon.anchor = GridBagConstraints.NORTH;
		gridBagLayout.setConstraints(lastStatusPanel,gridBagCon);
		panel.add(lastStatusPanel);
	}

	/**
	 * Initialise log text area.
	 * @param panel The panel to put the status panel in.
	 * @param gridBagLayout The grid bag layout to set constraints for, before adding things
	 * 	to the panel.
	 */
	private void initLogPanel(JPanel panel,GridBagLayout gridBagLayout)
	{
		JScrollPane areaScrollPane = null;
        	GridBagConstraints gridBagCon = new GridBagConstraints();

		logTextArea = new JTextArea("");
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
	 * Method to initialise the top-level menu bar.
	 */
	private void initMenuBar()
	{
		JMenuBar menuBar;
		JMenu menu, submenu;
		JMenuItem menuItem;
		CcsGUIMenuItemListener menuItemListener = new CcsGUIMenuItemListener(this);

	// Create the mapping from Menu item names to dialog classes.
	// This needs updating when the menu items names change.
		menuItemListener.createMapping();
	//Create the menu bar.
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

	//Build the general menu.
		menu = new JMenu("General");
		menu.setMnemonic(KeyEvent.VK_G);
		menu.getAccessibleContext().setAccessibleDescription("The General Menu");
		menuBar.add(menu);
        // Thread Monitor
		menuItem = new JMenuItem("Thread Monitor",KeyEvent.VK_T);
		menuItem.getAccessibleContext().setAccessibleDescription("Starts a thread monitor");
		menuItem.addActionListener(menuItemListener);
		menu.add(menuItem);
        // Exit
		menuItem = new JMenuItem("Exit",KeyEvent.VK_X);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Exits the program");
		menuItem.addActionListener(menuItemListener);
		menu.add(menuItem);
	//Build the log menu.
		menu = new JMenu("Log");
		menu.setMnemonic(KeyEvent.VK_L);
		menu.getAccessibleContext().setAccessibleDescription("The Log Menu");
		menuBar.add(menu);
        // Clear
		menuItem = new JMenuItem("Clear",KeyEvent.VK_C);
		menuItem.getAccessibleContext().setAccessibleDescription("Clears the log window");
		menuItem.addActionListener(menuItemListener);
		menu.add(menuItem);
	//Build the send menu.
		menu = new JMenu("Send");
		menu.setMnemonic(KeyEvent.VK_S);
		menu.getAccessibleContext().setAccessibleDescription(
			"The Menu to send commands from");
		menuBar.add(menu);
        // ACQUIRE
		menuItem = new JMenuItem("Acquire",KeyEvent.VK_A);
		menuItem.getAccessibleContext().setAccessibleDescription("Send an Acquire command");
		menuItem.addActionListener(menuItemListener);
		menu.add(menuItem);
	// CALIBRATE
		submenu = new JMenu("Calibrate");
		submenu.setMnemonic(KeyEvent.VK_C);
        // ARC
		menuItem = new JMenuItem("Arc",KeyEvent.VK_A);
		menuItem.getAccessibleContext().setAccessibleDescription("Send an Arc command");
		menuItem.addActionListener(menuItemListener);
		submenu.add(menuItem);
        // BIAS
		menuItem = new JMenuItem("Bias",KeyEvent.VK_B);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B,ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Send an Bias command");
		menuItem.addActionListener(menuItemListener);
		submenu.add(menuItem);
        // DARK
		menuItem = new JMenuItem("Dark",KeyEvent.VK_D);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Send an Dark command");
		menuItem.addActionListener(menuItemListener);
		submenu.add(menuItem);
        // LAMPFLAT
		menuItem = new JMenuItem("Lamp Flat",KeyEvent.VK_L);
		menuItem.getAccessibleContext().setAccessibleDescription("Send an Lamp Flat command");
		menuItem.addActionListener(menuItemListener);
		submenu.add(menuItem);
        // SKYFLAT
		menuItem = new JMenuItem("Sky Flat",KeyEvent.VK_S);
		menuItem.getAccessibleContext().setAccessibleDescription("Send an Sky Flat command");
		menuItem.addActionListener(menuItemListener);
		submenu.add(menuItem);
	// add CALIBRATE submenu
		menu.add(submenu);
	// EXPOSE
		submenu = new JMenu("Expose");
		submenu.setMnemonic(KeyEvent.VK_E);
        // GLANCE
		menuItem = new JMenuItem("Glance",KeyEvent.VK_G);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Send a Glance command");
		menuItem.addActionListener(menuItemListener);
		submenu.add(menuItem);
        // MOVIE
		menuItem = new JMenuItem("Movie",KeyEvent.VK_V);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Send a Movie command");
		menuItem.addActionListener(menuItemListener);
		submenu.add(menuItem);
        // MULTRUN
		menuItem = new JMenuItem("Multrun",KeyEvent.VK_M);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Send a Multrun command");
		menuItem.addActionListener(menuItemListener);
		submenu.add(menuItem);
        // RUNAT
		menuItem = new JMenuItem("Runat",KeyEvent.VK_R);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Send a Runat command");
		menuItem.addActionListener(menuItemListener);
		submenu.add(menuItem);
        // SAVE
		menuItem = new JMenuItem("Save",KeyEvent.VK_S);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Send a Save command");
		menuItem.addActionListener(menuItemListener);
		submenu.add(menuItem);
	// add EXPOSE submenu
		menu.add(submenu);
	// INTERRUPT
		submenu = new JMenu("Interrupt");
		submenu.setMnemonic(KeyEvent.VK_I);
        // ABORT
		menuItem = new JMenuItem("Abort",KeyEvent.VK_A);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Send an Abort command");
		menuItem.addActionListener(menuItemListener);
		submenu.add(menuItem);
        // GET_STATUS
		menuItem = new JMenuItem("Get Status",KeyEvent.VK_G);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G,ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Send an Get Status command");
		menuItem.addActionListener(menuItemListener);
		submenu.add(menuItem);
        // PAUSE
		menuItem = new JMenuItem("Pause",KeyEvent.VK_P);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Send an Pause command");
		menuItem.addActionListener(menuItemListener);
		submenu.add(menuItem);
        // REBOOT
		menuItem = new JMenuItem("Reboot");
		menuItem.getAccessibleContext().setAccessibleDescription("Send an Reboot command");
		menuItem.addActionListener(menuItemListener);
		submenu.add(menuItem);
        // RESUME
		menuItem = new JMenuItem("Resume",KeyEvent.VK_R);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Send an Resume command");
		menuItem.addActionListener(menuItemListener);
		submenu.add(menuItem);
        // STOP
		menuItem = new JMenuItem("Stop",KeyEvent.VK_S);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Send a Stop command");
		menuItem.addActionListener(menuItemListener);
		submenu.add(menuItem);
	// add INTERRUPT submenu
		menu.add(submenu);
	// SETUP
		submenu = new JMenu("Setup");
		submenu.setMnemonic(KeyEvent.VK_S);
        // CONFIG
		menuItem = new JMenuItem("Config",KeyEvent.VK_C);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Send a Config command");
		menuItem.addActionListener(menuItemListener);
		submenu.add(menuItem);
        // LAMPFOCUS
		menuItem = new JMenuItem("Lamp Focus",KeyEvent.VK_L);
		menuItem.getAccessibleContext().setAccessibleDescription("Send a Lamp Focus command");
		menuItem.addActionListener(menuItemListener);
		submenu.add(menuItem);
        // SET_LOGGING
		menuItem = new JMenuItem("Set Logging",KeyEvent.VK_S);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Send a Set Log level command");
		menuItem.addActionListener(menuItemListener);
		submenu.add(menuItem);
        // STARFOCUS
		menuItem = new JMenuItem("Star Focus",KeyEvent.VK_F);
		menuItem.getAccessibleContext().setAccessibleDescription("Send a Star Focus command");
		menuItem.addActionListener(menuItemListener);
		submenu.add(menuItem);
        // TELFOCUS
		menuItem = new JMenuItem("Telescope Focus",KeyEvent.VK_T);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T,ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Send a Telescope Focus command");
		menuItem.addActionListener(menuItemListener);
		submenu.add(menuItem);
        // TEST
		menuItem = new JMenuItem("Test",KeyEvent.VK_E);
		menuItem.getAccessibleContext().setAccessibleDescription("Send a Test command");
		menuItem.addActionListener(menuItemListener);
		submenu.add(menuItem);
	// add SETUP submenu
		menu.add(submenu);


	}

	/**
 	 * The run routine. Sets the main frame visible. Starts the 
	 * @see #frame.
	 */
	private void run()
	{
		scheduler = new CPUScheduler(100);// diddly ccs_gui.properties
		server = new CcsGUIServer("CCS GUI ISS Server",issPortNumber);
		server.setParent(this);
		scheduler.addThread(server);
		server.start();
		frame.pack();
		frame.setSize(450,300);
		frame.setVisible(true);
	}

	/**
	 * Main program exit routine. Waits for command to complete before exiting, if n is zero,
	 * otherwise just terminates.
	 * @param n The return exit value to return to the calling shell/program.
	 */
	public void exit(int n)
	{
		int tryCount = 0,i;

		try
		{
			if(n == 0)
			{
				log("Exiting program: wait for "+status.clientThreadListCount()+
					" commands to finish processing.");
				tryCount = 0;
				while((status.clientThreadListCount()>0)&&(tryCount < 3))
				{
					log("There are "+status.clientThreadListCount()+
						" commands still processing.");
					for(i=0;i<status.clientThreadListCount();i++)
					{
						CcsGUIClientConnectionThread thread = null;

						thread = status.clientThreadAt(i);
						if(thread.isAlive() == false)
						{
							status.removeClientThread(thread);
							i--;
						}
					}
					if(status.clientThreadListCount()>0)
						Thread.sleep(30000);
					tryCount++;
				}
				if(tryCount == 3)
					error("Exiting program:Failed to wait for "+status.clientThreadListCount()+
						" commands still processing.");
			}
		}
		catch(InterruptedException e)
		{
			error(this.getClass().getName()+":trying to exit:"+e);
		}
		server.close();
		System.exit(n);
	}

	/**
	 * Method to clear the log text area.
	 * @see #logTextArea
	 */
	public void clearLog()
	{
		logTextArea.setText(null);
	}

	/**
	 * Routine to write the string, terminted with a new-line, to the current log-file.
	 * Also written to the log text area
	 * @param s The string to write.
	 * @see #logTextArea
	 * @see #logStream
	 */
	public void log(String s)
	{
		if(logTextArea != null)
		{
			SwingUtilities.invokeLater(new GUITextAppender(logTextArea,s+"\n"));
		}
		logStream.println(s);
	}

	/**
	 * Routine to write the string, terminted with a new-line, to the current error-file.
	 * @param s The string to write.
	 * @see #errorStream
	 */
	public void error(String s)
	{
		if(logTextArea != null)
		{
			SwingUtilities.invokeLater(new GUITextAppender(logTextArea,s+"\n"));
		}
		errorStream.println(s);
	}

	/**
	 * Return the GUI's parent frame.
	 */
	public JFrame getFrame()
	{
		return frame;
	}

	/**
	 * Return the GUI's status object.
	 * @return The GUI's status object.
	 * @see #status
	 */
	public CcsGUIStatus getStatus()
	{
		return status;
	}

	/**
	 * Return the GUI's thread scheduler.
	 * @return The scheduler.
	 * @see #scheduler
	 */
	public CPUScheduler getScheduler()
	{
		return scheduler;
	}

	/**
	 * Method to send a command to the CCS.
	 * @param command The command to send.
	 */	
	public void sendCommand(COMMAND command)
	{
		CcsGUIClientConnectionThread thread = null;

		thread = new CcsGUIClientConnectionThread(ccsAddress,ccsPortNumber,command);
		thread.setParent(this);
		scheduler.addThread(thread);
		thread.start();
		status.addClientThread(thread);
	}

	/**
	 * Method to set the ccd status label.
	 * @param s The string to set the label to.
	 */
	public void setCCDStatus(String s)
	{
		if(ccdStatusLabel != null)
		{
			SwingUtilities.invokeLater(new GUILabelSetter(ccdStatusLabel,s));
		}
	}
}
//
// $Log: not supported by cvs2svn $
// Revision 0.1  1999/11/22 09:53:49  cjm
// initial revision.
//
//
