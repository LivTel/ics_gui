// CcsGUI.java -*- mode: Fundamental;-*-
// $Header: /home/cjm/cvs/ics_gui/java/CcsGUI.java,v 0.5 1999-12-14 15:17:50 cjm Exp $
import java.lang.*;
import java.io.*;
import java.net.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.plaf.metal.*;

import ngat.message.base.*;
import ngat.swing.*;
import ngat.util.*;

/**
 * This class is the start point for the Ccs GUI.
 * @author Chris Mottram
 * @version $Revision: 0.5 $
 */
public class CcsGUI
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: CcsGUI.java,v 0.5 1999-12-14 15:17:50 cjm Exp $");
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
	 * Text field for storing the update time for status queries.
	 */
	private JTextField autoUpdateTextField = null;
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
	 * Whether to start the ISS server or not to spoof ISS calls at startup.
	 * @see #server
	 */
	private boolean initiallyStartISSServer = false;
	/**
	 * Whether ISS commands that are received should manage a message dialog. This allows the
	 * user to manually configure the CCS's request.
	 */
	private boolean issMessageDialog = false;
	/**
	 * Menu item for bringing up dialog boxs in response to ISS requests.
	 */
	private JCheckBoxMenuItem messageDialogMenuItem = null;

	public CcsGUI()
	{
	}

	/**
	 * The main routine, called when Ccs is executed.
	 */
	public static void main(String[] args)
	{
		MetalLookAndFeel metalLookAndFeel = null;

	// setup LTMetalTheme
		try
		{
			Class cl = Class.forName(UIManager.getCrossPlatformLookAndFeelClassName());
			metalLookAndFeel = (MetalLookAndFeel)(cl.newInstance());
			metalLookAndFeel.setCurrentTheme(new LTMetalTheme());
			UIManager.setLookAndFeel(metalLookAndFeel);
		}
		catch(Exception e)
		{
			System.err.println("UIManager.setLookAndFeel failed:"+e);
		}
	// create and display a splash screen whilst we are initialising
		SplashScreen splashScreen = new SplashScreen(300,200,"lt.gif",
			"Liverpool John Moores University",null);
		splashScreen.show(30000);
	// construct gui main object
		CcsGUI ccsGUI = new CcsGUI();
		try
		{
			ccsGUI.initStatus();
			ccsGUI.parseArguments(args);
		}
		catch(Exception e)
		{
			ccsGUI.error(e.toString());
			System.exit(1);
		}
		ccsGUI.initGUI();
		ccsGUI.run();
	}

	/**
	 * Initialise the program status, from the configuration files.
	 * Creates the status object and initialises it.
	 * @exception FileNotFoundException Thrown if a configuration filename not found.
	 * @exception IOException Thrown if a configuration file has an IO error during reading.
	 * @see #status
	 * @see CcsGUIStatus#load
	 * @see CcsGUIStatus#loadCCDConfig
	 */
	private void initStatus() throws FileNotFoundException,IOException
	{
		status = new CcsGUIStatus();
		status.load();
		status.loadCCDConfig();
	}

	/**
	 * Initialise the program.
	 * Creates the frame and creates the widgets associated with it.
	 * Creates the menu bar.
	 * @see #frame
	 */
	private void initGUI()
	{
	// create the frame level layout manager
		GridBagLayout gridBagLayout = new GridBagLayout();
        	GridBagConstraints gridBagCon = new GridBagConstraints();
	// Create the top-level container.
		frame = new MinimumSizeFrame("CCS Interface",new Dimension(400,250));

		frame.getContentPane().setLayout(gridBagLayout);
		initMenuBar();
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
		frame.addWindowListener(new CcsGUIWindowListener(this));
	}

	private void initMainPanel(JPanel panel)
	{
	// create the frame level layout manager
		GridBagLayout gridBagLayout = new GridBagLayout();
	// setup panel
		panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		panel.setLayout(gridBagLayout);
		panel.setMinimumSize(new Dimension(450,400));
		panel.setPreferredSize(new Dimension(450,300));
		panel.setMaximumSize(new Dimension(1024,1024));
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
		String ccdStatusLabelString = new String("CCD Status:");
		String remainingExposureTimeLabelString = new String("Remaining Exposure Time:");
		String remainingExposuresLabelString = new String("Remaining Exposures:");
		String filterSelectedLabelString = new String("Filters Selected:");
		String ccdTemperatureLabelString = new String("CCD Temperature:");

		lastStatusPanel.setLayout(new GridLayout(0,2));
		lastStatusPanel.setMinimumSize(new Dimension(250,125));
		lastStatusPanel.setPreferredSize(new Dimension(1024,125));
		lastStatusPanel.setMaximumSize(new Dimension(1024,125));
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
	// auto update
		JCheckBox autoUpdateCheckbox = new JCheckBox("Auto-Update",false);
		autoUpdateCheckbox.addActionListener(new CcsGUIUpdateListener(this));
		lastStatusPanel.add(autoUpdateCheckbox);
		autoUpdateTextField = new JTextField();
		lastStatusPanel.add(autoUpdateTextField);
	// add border
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
		menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		menu.getAccessibleContext().setAccessibleDescription("The File Menu");
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
	// Build the ISS menu.
		menu = new JMenu("ISS");
		menu.setMnemonic(KeyEvent.VK_I);
		menu.getAccessibleContext().setAccessibleDescription("The Instrument Support System Menu");
		menuBar.add(menu);
        // Spoof ISS Requests
		menuItem = new JCheckBoxMenuItem("Spoof Requests",initiallyStartISSServer);
		menuItem.getAccessibleContext().
			setAccessibleDescription("Starts a thread to Spoof requests sent to the ISS");
		menuItem.addActionListener(menuItemListener);
		menu.add(menuItem);
        // ISS Message Dialogs
		messageDialogMenuItem = new JCheckBoxMenuItem("Message Dialog",issMessageDialog);
		messageDialogMenuItem.getAccessibleContext().
			setAccessibleDescription("Manage message dialog when an ISS command is received.");
		messageDialogMenuItem.addActionListener(menuItemListener);
		messageDialogMenuItem.setEnabled(false);
		menu.add(messageDialogMenuItem);
	}

	/**
 	 * The run routine. Sets the main frame visible.
	 * Starts the server, if we want to start it.
	 * @see #frame
	 * @see #initiallyStartISSServer
	 * @see #startISSServer
	 * @see #server
	 */
	private void run()
	{
		if(initiallyStartISSServer)
			startISSServer();
		frame.pack();
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
		try
		{
			status.saveCCDConfig();
		}
		catch(IOException e)
		{
			error(this.getClass().getName()+":trying to exit:saving CCD Config failed:"+e);
		}
		stopISSServer();
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
			SwingUtilities.invokeLater(new GUITextAppender(logTextArea,s+"\n",true));
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
			SwingUtilities.invokeLater(new GUITextAppender(logTextArea,s+"\n",true));
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
	 * Method to set whether to bring up message dialogs when dealing with ISS commands.
	 * @param b True if we want message dialogs, false otherwise.
	 * @see #issMessageDialog
	 */
	public void setISSMessageDialog(boolean b)
	{
		issMessageDialog = b;
	}

	/**
	 * Method to get whether to bring up message dialogs when dealing with ISS commands.
	 * @return Retrurns the value of the issMessageDialog field.
	 * @see #issMessageDialog
	 */
	public boolean getISSMessageDialog()
	{
		return issMessageDialog;
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

	/**
	 * Method to get the update time for status update. This is got from the autoUpdateTextField,
	 * and then parsed into a long to return. If a parse exception occurs, zero is returned.
	 * @return An auto update time. If an error occurs zero is returned.
	 */
	public long getAutoUpdateTime()
	{
		String s = null;
		long retval = 0;

		s = autoUpdateTextField.getText();
		try
		{
			retval = Long.parseLong(s);
		}
		catch(NumberFormatException e)
		{
			error("Could not get the auto-update time:`"+s+"' not a valid number.");
			retval = 0;
		}
		if(retval < 1000)
		{
			error("Auto-update time must be at least 1000 milliseconds.");
			retval = 0;
		}
		return retval;
	}

	/**
	 * Enable or disable the auto update text field, so that a new value cannot be entered
	 * whilst auto update is turned on.
	 * @param b Boolean value passed to setEnabled.
	 */
	public void setAutoUpdateTimeEnabled(boolean b)
	{
		autoUpdateTextField.setEnabled(b);
	}

	/**
	 * Method to start running the ISS server.
	 * Creates a new thread. Sets it's parent to this class instance.
	 * Calls the server run method to start the thread.
	 * Sets the ISS Message Dialog menu item sensitive.
	 * @see #issPortNumber
	 * @see #server
	 * @see #messageDialogMenuItem
	 */
	public void startISSServer()
	{
	// create and start server
		server = new CcsGUIServer("CCS GUI ISS Server",issPortNumber);
		server.setParent(this);
		server.start();
		log("ISS Server started on port:"+issPortNumber+".");
		messageDialogMenuItem.setEnabled(true);
	}

	/**
	 * Method to stop the ISS server. If the server reference is non-null, calls it's close method.
	 * Sets the server reference to null.
	 * Sets the ISS Message Dialog menu item in-sensitive.
	 * @see #server
	 * @see CcsGUIServer#close
	 * @see #messageDialogMenuItem
	 */
	public void stopISSServer()
	{
		if(server != null)
		{
			server.close();
		}
		server = null;
		log("ISS Server stoped.");
		messageDialogMenuItem.setEnabled(false);
	}

	/**
	 * This routine parses arguments passed into Ccs. It gets some default arguments from the configuration
	 * file. These are the CCS and ISS port numbers, and the CCS internet address.
	 * It then reads through the arguments in the list. This routine will stop the program 
	 * if a `-help' is one of the arguments.
	 * @param args The list of arguments to parse.
	 * @see #ccsAddress
	 * @see #ccsPortNumber
	 * @see #initiallyStartISSServer
	 * @see #issPortNumber
	 * @see #help
	 */
	private void parseArguments(String[] args) throws NumberFormatException,UnknownHostException
	{
	// initialise port numbers from properties file
		try
		{
			ccsPortNumber = status.getPropertyInteger("ccs_gui.net.default_CCS_port_number");
			issPortNumber = status.getPropertyInteger("ccs_gui.net.default_ISS_port_number");
		}
		catch(NumberFormatException e)
		{
			error(this.getClass().getName()+":parseArguments:initialsing port number:"+e);
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
	// look through the argument list.
		for(int i = 0; i < args.length;i++)
		{
			if(args[i].equals("-log"))
			{
				if((i+1)< args.length)
				{
					status.setLogLevel(Integer.parseInt(args[i+1]));
					i++;
				}
				else
					error("-log requires a log level");
			}
			else if(args[i].equals("-ccsport"))
			{
				if((i+1)< args.length)
				{
					ccsPortNumber = Integer.parseInt(args[i+1]);
					i++;
				}
				else
					error("-ccsport requires a port number");
			}
			else if(args[i].equals("-issspoof"))
			{
				initiallyStartISSServer = true;
			}
			else if(args[i].equals("-issdialog"))
			{
				issMessageDialog = true;
			}
			else if(args[i].equals("-issport"))
			{
				if((i+1)< args.length)
				{
					issPortNumber = Integer.parseInt(args[i+1]);
					i++;
				}
				else
					error("-issport requires a port number");
			}
			else if(args[i].equals("-ccsip")||args[i].equals("-ccsaddress"))
			{
				if((i+1)< args.length)
				{
					try
					{
						ccsAddress = InetAddress.getByName(args[i+1]);
					}
					catch(UnknownHostException e)
					{
						error(this.getClass().getName()+
							":illegal CCS address:"+args[i+1]+":"+e);
					}
					i++;
				}
				else
					error("-ccsaddress requires a valid ip address");
			}
			else if(args[i].equals("-h")||args[i].equals("-help"))
			{
				help();
				System.exit(0);
			}
			else
				error(this.getClass().getName()+"'"+args[i]+"' not a recognised option");
		}
	}

	/**
	 * Help message routine.
	 */
	private void help()
	{
		System.out.println(this.getClass().getName()+" Help:");
		System.out.println("CcsGUI is the `CCD Control System Graphical User Interface'.");
		System.out.println("Options are:");
		System.out.println("\t-ccsport <port number> - Ccs server port number.");
		System.out.println("\t-issspoof - Start an thread to pretend to be the ISS.");
		System.out.println("\t-issdialog - Bring up a message dialog for each ISS comamnd.");
		System.out.println("\t-issport <port number> - Port for ISS commands, if ISS spoofing is on.");
		System.out.println("\t-[ccsip]|[ccsaddress] <address> - Address to send CCS commands to.");
		System.out.println("\t-log <log level> - log level.");
	}
}
//
// $Log: not supported by cvs2svn $
// Revision 0.4  1999/12/14 12:18:01  cjm
// Added splash screen.
//
// Revision 0.3  1999/12/09 17:02:12  cjm
// More functionality added.
//
// Revision 0.2  1999/11/24 12:51:28  cjm
// Changed @see clause.
//
// Revision 0.1  1999/11/22 09:53:49  cjm
// initial revision.
//
//
