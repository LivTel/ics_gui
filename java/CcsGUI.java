// CcsGUI.java -*- mode: Fundamental;-*-
// $Header: /home/cjm/cvs/ics_gui/java/CcsGUI.java,v 0.16 2002-05-23 12:44:53 cjm Exp $
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
 * This class is the start point for the Ccs GUI.
 * @author Chris Mottram
 * @version $Revision: 0.16 $
 */
public class CcsGUI
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: CcsGUI.java,v 0.16 2002-05-23 12:44:53 cjm Exp $");
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
	 * Label for current Ccs status.
	 */
	private JLabel ccdStatusLabel = null;
	/**
	 * Label for last command sent.
	 */
	private JLabel currentCommandLabel = null;
	/**
	 * Label for remaining exposure time.
	 */
	private JLabel remainingExposureTimeLabel = null;
	/**
	 * Label for remaining exposures.
	 */
	private JLabel remainingExposuresLabel = null;
	/**
	 * Label for filters selected.
	 */
	private JLabel filterSelectedLabel = null;
	/**
	 * Label for CCD temperature.
	 */
	private JLabel ccdTemperatureLabel = null;
	/**
	 * Label for last FITS filename received.
	 */
	private JLabel filenameLabel = null;

	/**
	 * Text field for storing the update time for status queries.
	 */
	private JTextField autoUpdateTextField = null;
	/**
	 * Text area for logging.
	 */
	private JTextArea logTextArea = null;
	/**
	 * Field holding the maximum number of lines of text the log text area should have.
	 * This field is used in the log method to stop the log text area getting so long CcsGUI runs
	 * out of memory. A GUITextLengthLimiter is used to do this.
	 * @see #log
	 */
	private int logTextAreaLineLimit = 500;
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
	 * This does the following:
	 * <ul>
	 * <li>Sets up the look and feel.
	 * <li>Starts a splash screen.
	 * <li>Constructs an instance of CcsGUI.
	 * <li>Calls initStatus, to load the properties.
	 * <li>Calls parseArguments, to look at command line arguments.
	 * <li>Calls initGUI, to create the screens.
	 * <li>Calls run.
	 * </ul>
	 * @see #initStatus
	 * @see #parseArguments
	 * @see #initGUI
	 * @see #run
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
		ngat.swing.SplashScreen splashScreen = new ngat.swing.SplashScreen(350,200,"lt.gif",
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
	 * Changes the errorStream and logStream to those specified in the configuration files.
	 * @exception FileNotFoundException Thrown if a configuration filename not found.
	 * @exception IOException Thrown if a configuration file has an IO error during reading.
	 * @see #status
	 * @see CcsGUIStatus#load
	 * @see CcsGUIStatus#loadInstrumentConfig
	 * @see #errorStream
	 * @see #logStream
	 */
	private void initStatus() throws FileNotFoundException,IOException
	{
		String filename = null;
		FileOutputStream fos = null;

		status = new CcsGUIStatus();
		status.load();
		status.loadInstrumentConfig();
	// change errorStream to files defined in loaded properties
		filename = status.getProperty("ccs_gui.file.error");
		if((filename != null)&&(filename.length()>0))
		{
			try
			{
				fos = new FileOutputStream(filename,true);
			}
			catch(IOException e)
			{
				error(this.getClass().getName()+":init:"+e);
				fos = null;
			}
			if(fos != null)
			{
				// deprecated statement.
				// This is the only way to set System error stream for runtime (JVM) errors.
				System.setErr(new PrintStream(fos));
				errorStream = new PrintWriter(fos,true);
			}
		}
	// change logStream to files defined in loaded properties
		filename = status.getProperty("ccs_gui.file.log");
		if((filename != null)&&(filename.length()>0))
		{
			try
			{
				fos = new FileOutputStream(filename,true);
			}
			catch(IOException e)
			{
				error(this.getClass().getName()+":init:"+e);
				fos = null;
			}
			if(fos != null)
				logStream = new PrintWriter(fos,true);
		}
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
		if(ccsAddress != null)
			titleString = new String("Ccs Interface:"+ccsAddress.getHostName());
		else
			titleString = new String("Ccs Interface:None");
		frame = new MinimumSizeFrame(titleString,new Dimension(400,300));
	// set icon image
		image = Toolkit.getDefaultToolkit().getImage("lt_icon.gif");
		frame.setIconImage(image);

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

	/**
	 * Initialise the main panel. This consists of setting the panel layout, and then 
	 * adding the status panel and the log panel.
	 * @see #initStatusPanel
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
		int minTime;

		lastStatusPanel.setLayout(new GridLayout(0,2));
		lastStatusPanel.setMinimumSize(new Dimension(250,160));
		lastStatusPanel.setPreferredSize(new Dimension(1024,160));
		lastStatusPanel.setMaximumSize(new Dimension(1024,160));
	// ccd status
		label = new JLabel("CCD Status:");
		lastStatusPanel.add(label);
		ccdStatusLabel = new JLabel("Unknown");
		lastStatusPanel.add(ccdStatusLabel);
	// current Command
		label = new JLabel("Current Command:");
		lastStatusPanel.add(label);
		currentCommandLabel = new JLabel("None");
		lastStatusPanel.add(currentCommandLabel);
	// remaining exposure time
		label = new JLabel("Remaining Exposure Time:");
		lastStatusPanel.add(label);
		remainingExposureTimeLabel = new JLabel("0.000");
		lastStatusPanel.add(remainingExposureTimeLabel);
	// remaining exposures
		label = new JLabel("Remaining Exposures:");
		lastStatusPanel.add(label);
		remainingExposuresLabel = new JLabel("0");
		lastStatusPanel.add(remainingExposuresLabel);
	// selected filter
		label = new JLabel("Filter(s) Selected:");
		lastStatusPanel.add(label);
		filterSelectedLabel = new JLabel("Unknown");
		lastStatusPanel.add(filterSelectedLabel);
	// ccd temperature
		label = new JLabel("CCD Temperature:");
		lastStatusPanel.add(label);
		ccdTemperatureLabel = new JLabel("Unknown");
		lastStatusPanel.add(ccdTemperatureLabel);
	// FITS filename 
		// filenameButton
		JButton filenameButton = new JButton("FITS Filename:");
		filenameButton.setHorizontalAlignment(JButton.LEFT);
		filenameButton.setHorizontalTextPosition(JButton.LEFT);
		filenameButton.addActionListener(new IcsGUIFilenameShowListener(this));
		lastStatusPanel.add(filenameButton);
		// filenameLabel
		filenameLabel = new JLabel("");
		lastStatusPanel.add(filenameLabel);
	// auto update
		JCheckBox autoUpdateCheckbox = new JCheckBox("Auto-Update",false);
		autoUpdateCheckbox.addActionListener(new CcsGUIUpdateListener(this));
		lastStatusPanel.add(autoUpdateCheckbox);
		autoUpdateTextField = new JTextField();
		try
		{
			minTime = status.getPropertyInteger("ics_gui.auto_update.min_time");
		}
		catch(NumberFormatException e)
		{
			minTime = 2000;
		}
		autoUpdateTextField.setText(""+minTime);
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
	// initialise logTextArea line limit from property file
		try
		{
			logTextAreaLineLimit = status.getPropertyInteger("ccs_gui.log_text.max_length");
		}
		catch(NumberFormatException e)
		{ // non-fatal error...
			error(this.getClass().getName()+":initLogPanel:initialsing log text area line limit:"+e);
		}
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
        // DAY_CALIBRATE
		menuItem = new JMenuItem("Day Calibrate",KeyEvent.VK_Y);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y,ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Send an Daylight Calibrate command");
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
        // TWILIGHT_CALIBRATE
		menuItem = new JMenuItem("Twilight Calibrate",KeyEvent.VK_W);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Send an Twilight Calibrate command");
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
		menuItem = new JMenuItem("Reboot",KeyEvent.VK_O);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Send an Reboot command");
		menuItem.addActionListener(menuItemListener);
		submenu.add(menuItem);
        // RESUME
		menuItem = new JMenuItem("Resume",KeyEvent.VK_U);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U,ActionEvent.CTRL_MASK));
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
	 * @see CcsGUIStatus#clientThreadList
	 * @see CcsGUIStatus#clientThreadAt
	 * @see CcsGUIStatus#removeClientThread
	 * @see CcsGUIStatus#saveInstrumentConfig
	 * @see #stopISSServer
	 * @see #log
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
			status.saveInstrumentConfig();
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
	 * Also written to the log text area.
	 * The actual appending is done in the swing thread, using an ngat.swing.GUITextAppender.
	 * A ngat.swing.GUITextLengthLimiter is also invoked later, so that the logging text area
	 * does not get so long it swallows all the memory.
	 * @param s The string to write.
	 * @see #logTextArea
	 * @see #logStream
	 */
	public void log(String s)
	{
		if(logTextArea != null)
		{
			SwingUtilities.invokeLater(new GUITextAppender(logTextArea,s+"\n",true));
			SwingUtilities.invokeLater(new GUITextLengthLimiter(logTextArea,logTextAreaLineLimit,false));
		}
		logStream.println(s);
	}

	/**
	 * Routine to write the string, terminted with a new-line, to the current error-file.
	 * The error is added to the log text area, the actual appending is done in the swing thread, 
	 * using an ngat.swing.GUITextAppender.
	 * A ngat.swing.GUITextLengthLimiter is also invoked later, so that the logging text area
	 * does not get so long it swallows all the memory.
	 * @param s The string to write.
	 * @see #errorStream
	 */
	public void error(String s)
	{
		if(logTextArea != null)
		{
			SwingUtilities.invokeLater(new GUITextAppender(logTextArea,s+"\n",true));
			SwingUtilities.invokeLater(new GUITextLengthLimiter(logTextArea,logTextAreaLineLimit,false));
		}
		errorStream.println(s);
	}

	/**
	 * Return the GUI's error stream.
	 * @return The error stream used by error.
	 */
	public PrintWriter getErrorStream()
	{
		return errorStream;
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
	 * Return the Ccs GUI's Ccs internet address object, used for communication
	 * with the Ccs.
	 * @return The current reference stored in ccsAddress.
	 * @see #ccsAddress
	 */
	public InetAddress getCcsAddress()
	{
		return ccsAddress;
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
	 * Method to send a command to the CCS. A CcsGUIClientConnectionThread is constructed with
	 * the passed in command, and the Ccs Address and port number from the configuration file.
	 * The thread is started, and added to the status's client thread list.
	 * @param command The command to send.
	 * @return The started client thread is returned.
	 */	
	public CcsGUIClientConnectionThread sendCommand(COMMAND command)
	{
		CcsGUIClientConnectionThread thread = null;

		thread = new CcsGUIClientConnectionThread(ccsAddress,ccsPortNumber,command);
		thread.setParent(this);
		thread.start();
		status.addClientThread(thread);
		return thread;
	}

	/**
	 * Method to set the ccd status label. The mode number is translated into an equivalent string.
	 * Note, the list of strings in this method should be kept up to date with the 
	 * currentMode number returned by GET_STATUSImplementation in the Ccs process.
	 * @param currentMode The mode number from the get status command.
	 */
	public void setCCDStatusLabel(int currentMode)
	{
		String currentModeList[] = {"Idle","Config","Clearing","Exposure","Readout","Error"};
		String s = null;

		if((currentMode < 0)||(currentMode >= currentModeList.length))
			currentMode = currentModeList.length-1;
		if(ccdStatusLabel != null)
		{
			SwingUtilities.invokeLater(new GUILabelSetter(ccdStatusLabel,currentModeList[currentMode]));
		}
	}

	/**
	 * Method to set the current command label.
	 * @param commandString The string to set the label to. If it is null, 'None' is used instead.
	 */
	public void setCurrentCommandLabel(String commandString)
	{
		if(commandString == null)
			commandString = new String("None");
		if(currentCommandLabel != null)
		{
			SwingUtilities.invokeLater(new GUILabelSetter(currentCommandLabel,commandString));
		}
	}

	/**
	 * Method to set the remaining exposures label.
	 * @param remainingExposures The number of exposures remaining to complete the current command.
	 */
	public void setRemainingExposuresLabel(int remainingExposures)
	{
		String s = null;

		s = Integer.toString(remainingExposures);
		if(remainingExposuresLabel != null)
		{
			SwingUtilities.invokeLater(new GUILabelSetter(remainingExposuresLabel,s));
		}
	}

	/**
	 * Method to set the remaining exposure time label.
	 * @param remainingExposureTime The time to complete the exposure, in milliseconds.
	 */
	public void setRemainingExposureTimeLabel(long remainingExposureTime)
	{
		String s = null;

		s = Double.toString(((double)remainingExposureTime)/1000.0);
		if(remainingExposureTimeLabel != null)
		{
			SwingUtilities.invokeLater(new GUILabelSetter(remainingExposureTimeLabel,s));
		}
	}

	/**
	 * Method to set the selected filters label. If one of the parameters is null, the String
	 * 'Unknown' is used instead. This is used for RATCam filter settings.
	 * @param lowerFilterString The name of the lower filter wheel's selected filter.
	 * @param upperFilterString The name of the lower filter wheel's selected filter.
	 */
	public void setFiltersSelectedLabel(String lowerFilterString,String upperFilterString)
	{
		if(lowerFilterString == null)
			lowerFilterString = new String("Unknown");
		if(upperFilterString == null)
			upperFilterString = new String("Unknown");
		if(filterSelectedLabel != null)
		{
			SwingUtilities.invokeLater(new GUILabelSetter(filterSelectedLabel,"lower:"+
				lowerFilterString+" upper:"+upperFilterString));
		}
	}

	/**
	 * Method to set the selected filters label. This is used for Nu-View filter settings.
	 * @param wavelength A Double object with the wavelegnth of the filter, in angstroms.
	 */
	public void setFiltersSelectedLabel(Double wavelength)
	{
		DecimalFormat decimalFormat = null;
		String wavelengthString;
		double wavelengthValue;

		if(wavelength != null)
		{
			wavelengthValue = wavelength.doubleValue();
		// format number to 2d.p.
			decimalFormat = new DecimalFormat("#####.00");
			wavelengthString = decimalFormat.format(wavelengthValue);
		}
		else
			wavelengthString = "Unknown";
		if(filterSelectedLabel != null)
		{
			SwingUtilities.invokeLater(new GUILabelSetter(filterSelectedLabel,"wavelength:"+
				wavelengthString));
		}
	}

	/**
	 * Method to set the selected filters label. This is used for MES filter settings.
	 * @param positionIndex An Integer object with the position index of the filter in the slide.
	 */
	public void setFiltersSelectedLabel(Integer positionIndex)
	{
		String positionString;

		if(positionIndex != null)
			positionString = positionIndex.toString();
		else
			positionString = "Unknown";
		if(filterSelectedLabel != null)
		{
			SwingUtilities.invokeLater(new GUILabelSetter(filterSelectedLabel,"Slide Index:"+
				positionString));
		}
	}

	/**
	 * Method to set the CCD Temperature label.
	 * @param ccdTemperature The temperature in degrees C
	 */
	public void setCCDTemperatureLabel(double ccdTemperature)
	{
		DecimalFormat decimalFormat = null;
		String s = null;

	// format number to 2d.p.
		decimalFormat = new DecimalFormat("###.00");
		s = decimalFormat.format(ccdTemperature);
		if(ccdTemperatureLabel != null)
		{
			SwingUtilities.invokeLater(new GUILabelSetter(ccdTemperatureLabel,s));
		}
	}

	/**
	 * Method to set the filename label to the last FITS filename received in a message from the Ics.
	 * @param filenameString The string to set the label to. If it is null or of zero length, 
	 * 	the label is not updated.
	 */
	public void setFilenameLabel(String filenameString)
	{
		if(filenameString != null)
		{
			if(filenameString.length() > 0)
			{
				if(filenameLabel != null)
				{
					SwingUtilities.invokeLater(new GUILabelSetter(filenameLabel,filenameString));
				}
			}
		}
	}

	/**
	 * Retrieve the current contents of the filename label, which contains the last filename
	 * returned to the Ccs GUI.
	 * @return A String containing the last filename. This can be blank, if no filenames have been
	 * 	returned to the GUI yet.
	 * @see #filenameLabel
	 */
	public String getFilename()
	{
		return filenameLabel.getText();
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
		int minTime;

		s = autoUpdateTextField.getText();
		try
		{
			retval = Long.parseLong(s);
			minTime = status.getPropertyInteger("ics_gui.auto_update.min_time");
			if(retval < minTime)
			{
				error("Auto-update time must be at least "+minTime+" milliseconds.");
				retval = 0;
			}
		}
		catch(NumberFormatException e)
		{
			error("Could not get the auto-update time:"+s+": not a valid number.");
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
		log("ISS Server stopped.");
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
// Revision 0.15  2001/07/10 18:21:28  cjm
// filenameLabel added.
// Label strings changed.
// filenameButton added.
// ics_gui.auto_update.min_time added.
// getCcsAddress added.
// setFiltersSelectedLabel for Nu-View added.
// getFilename added.
//
// Revision 0.14  2001/02/27 13:45:40  cjm
// Modified keyboard shortcuts.
//
// Revision 0.13  2000/11/30 18:47:44  cjm
// Made generic for other instruments.
//
// Revision 0.12  2000/07/11 09:23:07  cjm
// More descriptive title string.
// log and error filename configuration now supported.
//
// Revision 0.11  2000/06/27 11:30:34  cjm
// Limited number of lines in logTextArea, using a GUITextLengthLimiter.
//
// Revision 0.10  2000/06/19 08:50:28  cjm
// Backup.
//
// Revision 0.9  2000/03/10 11:51:37  cjm
// Modified sendCommand to return the thread it starts.
//
// Revision 0.9  2000/03/10 11:44:09  cjm
// Modified sendCommand to return the started thread.
//
// Revision 0.8  2000/03/10 11:21:09  cjm
// Added setCCDTemperatureLabel method.
//
// Revision 0.7  2000/02/28 19:14:40  cjm
// Backup.
//
// Revision 0.6  2000/02/04 16:13:05  cjm
// Changed splash screen size.
//
// Revision 0.5  1999/12/14 15:17:50  cjm
// Changed calls to GUITextAppender to reflect new scroll option.
//
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
