# Makefile
# $Header: /home/cjm/cvs/ics_gui/java/Makefile,v 0.53 2015-06-24 13:29:58 cjm Exp $ 
include ../../Makefile.common
include	../Makefile.common

INCDIR 		= $(CCS_GUI_SRC_HOME)/include
BINDIR 		= $(CCS_GUI_BIN_HOME)/java
DOCSDIR 	= $(CCS_GUI_DOC_HOME)/javadocs
DOCFLAGS 	= -version -author -private

# note - only MAIN_SRCS checked in/out
# note - DIALOG_SRCS are deleted on clean target to ensure they are re-built
SRCS 		= $(MAIN_SRCS) $(DIALOG_SRCS)

MAIN_SRCS	= IcsGUI.java CcsGUIStatus.java CcsGUIWindowListener.java CcsGUIMenuItemListener.java \
		GUIDialogListener.java CcsGUIDialogListener.java CcsCommandDialog.java \
		CcsGUIClientConnectionThread.java IcsGUIFilenameShowListener.java \
		$(SERVER_SRCS) $(CONFIG_SRCS) $(UPDATE_SRCS)

SERVER_SRCS	= IcsGUIISSServer.java IcsGUIISSServerConnectionThread.java \
		IcsGUIBSSServer.java IcsGUIBSSServerConnectionThread.java
CONFIG_SRCS	= IcsGUIConfigProperties.java IcsGUIConfigListDialog.java CcsGUIConfigButtonListener.java \
		IcsGUICCDConfigAADialog.java IcsGUILowResSpecConfigAADialog.java IcsGUIIRCamConfigAADialog.java \
		IcsGUIFixedFormatSpecConfigAADialog.java IcsGUIPolarimeterConfigAADialog.java \
		IcsGUIFrodoSpecConfigAADialog.java IcsGUILOTUSConfigAADialog.java IcsGUIRISEConfigAADialog.java \
		IcsGUIRingo2PolarimeterConfigAADialog.java IcsGUIRingo3PolarimeterConfigAADialog.java \
		IcsGUISpratConfigAADialog.java IcsGUITHORConfigAADialog.java \
		IcsGUIOConfigAADialog.java CcsConfigAADialogListener.java 
UPDATE_SRCS	= CcsGUIUpdateListener.java CcsGUIUpdateThread.java

DIALOG_SRCS	= ACQUIREDialog.java $(CALIBRATE_DIALOG_SRCS) $(EXPOSE_DIALOG_SRCS) $(INTERRUPT_DIALOG_SRCS) $(SETUP_DIALOG_SRCS)

CALIBRATE_DIALOG_SRCS	= ARCDialog.java BIASDialog.java DARKDialog.java DAY_CALIBRATEDialog.java LAMPFLATDialog.java \
			MULTBIASDialog.java MULTDARKDialog.java SKYFLATDialog.java TWILIGHT_CALIBRATEDialog.java \
			FRODOSPEC_ARCDialog.java FRODOSPEC_BIASDialog.java FRODOSPEC_DARKDialog.java \
			FRODOSPEC_DAY_CALIBRATEDialog.java FRODOSPEC_TWILIGHT_CALIBRATEDialog.java \
			FRODOSPEC_LAMPFLATDialog.java 
EXPOSE_DIALOG_SRCS	= GLANCEDialog.java MOVIEDialog.java MULTRUNDialog.java RUNATDialog.java \
			TIMED_MULTRUNATDialog.java SAVEDialog.java \
			FRODOSPEC_GLANCEDialog.java FRODOSPEC_MULTRUNDialog.java FRODOSPEC_RUNATDialog.java
INTERRUPT_DIALOG_SRCS	= ABORTDialog.java FRODOSPEC_ABORTDialog.java GET_STATUSDialog.java PAUSEDialog.java \
			REBOOTDialog.java RESUMEDialog.java STOPDialog.java
SETUP_DIALOG_SRCS	= CONFIGDialog.java LAMPFOCUSDialog.java SET_LOGGINGDialog.java STARFOCUSDialog.java \
			TELFOCUSDialog.java TESTDialog.java FRODOSPEC_LAMPFOCUSDialog.java 

OBJS 		= $(SRCS:%.java=$(BINDIR)/%.class)
DOCS 		= $(SRCS:%.java=$(DOCSDIR)/%.html)

PROPERTY_SRCS	= $(CCS_GUI_PROPERTY_SRCS) $(CCS_GUI_CONFIG_PROPERTY_SRCS) \
		$(CCS_GUI_FITS_PROPERTY_SRCS) $(CCS_GUI_FITS_BSS_PROPERTY_SRCS)
CCS_GUI_PROPERTY_SRCS	= ltccd2.ccs_gui.properties ltccd4.ccs_gui.properties \
	ltobs9.ratcam.ccs_gui.properties ltobs9.supircam.ccs_gui.properties ltobs9.frodospec.ccs_gui.properties \
	ltobs9.ftspec.ics_gui.properties ltobs9.nuview1.ics_gui.properties ltobs9.rise.ics_gui.properties \
	ltobs9.ioi.ccs_gui.properties \
	supircam2.ccs_gui.properties nuview1.ccs_gui.properties ratcam1.ccs_gui.properties \
	lttmc.ccs_gui.properties estar6.ccs_gui.properties ringo1.ccs_gui.properties \
	ringo2.ccs_gui.properties ltobs9.ringo2.ccs_gui.properties \
	tiptilt1.ccs_gui.properties ltobs9.thor.ccs_gui.properties thor.ccs_gui.properties micc1.ccs_gui.properties \
	rise.ccs_gui.properties ltobs9.o.ccs_gui.properties o1.ccs_gui.properties ringo3.ccs_gui.properties \
	ringo3-1.ccs_gui.properties ioi1.ccs_gui.properties tlan-operator.ccs_gui.properties \
	frodospec1.frodospec.ccs_gui.properties frodospec1.sprat.ccs_gui.properties sprat1.sprat.ccs_gui.properties \
	ioi1.ioi.ccs_gui.properties ioi1.sprat.ccs_gui.properties observer1.ccs_gui.properties \
	lotus1.lotus.ccs_gui.properties lotus2.lotus.ccs_gui.properties ltobs9.lotus.ccs_gui.properties
# No longer used
#ltccd1.ccs_gui.properties ltccd3.ccs_gui.properties ftccd1.ccs_gui.properties ftccd2.ccs_gui.properties 
# spectrograph.scs_gui.properties ltobs9.fartcam.ccs_gui.properties ltobs9.dillcamsouth.ccs_gui.properties 
# ltobs9.spectrograph.scs_gui.properties supircam1.ccs_gui.properties ftspec1.ccs_gui.properties 
# ftspec2.ccs_gui.properties spectrograph.ccs_gui.properties ftntmc.ccs_gui.properties ftstmc.ccs_gui.properties 
# ftsocc.ccs_gui.properties 
# frodospec1.ccs_gui.properties sprat1.ccs_gui.properties 

CCS_GUI_CONFIG_PROPERTY_SRCS = ltccd2.ccs_gui_config.properties ltccd4.ccs_gui_config.properties \
	supircam2.ccs_gui_config.properties nuview1.ccs_gui_config.properties lttmc.ccs_gui_config.properties \
	ratcam1.ccs_gui_config.properties estar6.ccs_gui_config.properties \
	ringo1.ccs_gui_config.properties ringo2.ccs_gui_config.properties ltobs9.ringo2.ccs_gui_config.properties \
	tiptilt1.ccs_gui_config.properties thor.ccs_gui_config.properties micc1.ccs_gui_config.properties \
	rise.ccs_gui_config.properties o1.ccs_gui_config.properties ringo3.ccs_gui_config.properties \
	ringo3-1.ccs_gui_config.properties ioi1.ccs_gui_config.properties tlan-operator.ccs_gui_config.properties \
	frodospec1.frodospec.ccs_gui_config.properties frodospec1.sprat.ccs_gui_config.properties \
	sprat1.sprat.ccs_gui_config.properties ioi1.ioi.ccs_gui_config.properties \
	ioi1.sprat.ccs_gui_config.properties observer1.ccs_gui_config.properties \
	lotus1.lotus.ccs_gui_config.properties lotus2.lotus.ccs_gui_config.properties 
# No longer used
#ltccd1.ccs_gui_config.properties ltccd3.ccs_gui_config.properties ftccd1.ccs_gui_config.properties 
# ftccd2.ccs_gui_config.properties supircam1.ccs_gui_config.properties ftspec1.ccs_gui_config.properties 
# ftspec2.ccs_gui_config.properties spectrograph.ccs_gui_config.properties ftntmc.ccs_gui_config.properties 
# ftstmc.ccs_gui_config.properties 
# frodospec1.ccs_gui_config.properties sprat1.ccs_gui_config.properties

CCS_GUI_FITS_PROPERTY_SRCS = ltccd2.ccs_gui.fits.properties ltccd4.ccs_gui.fits.properties \
	nuview1.ccs_gui.fits.properties lttmc.ccs_gui.fits.properties ratcam1.ccs_gui.fits.properties \
	estar6.ccs_gui.fits.properties ringo1.ccs_gui.fits.properties \
	ringo2.ccs_gui.fits.properties ltobs9.ringo2.ccs_gui.fits.properties tiptilt1.ccs_gui.fits.properties \
	thor.ccs_gui.fits.properties micc1.ccs_gui.fits.properties rise.ccs_gui.fits.properties \
	o1.ccs_gui.fits.properties ringo3.ccs_gui.fits.properties ringo3-1.ccs_gui.fits.properties \
	ioi1.ccs_gui.fits.properties tlan-operator.ccs_gui.fits.properties \
	frodospec1.ccs_gui.fits.properties \
	sprat1.ccs_gui.fits.properties ioi1.ccs_gui.fits.properties observer1.ccs_gui.fits.properties \
	lotus1.ccs_gui.fits.properties lotus2.ccs_gui.fits.properties 
# No longer used
#ltccd1.ccs_gui.fits.properties ltccd3.ccs_gui.fits.properties ftccd1.ccs_gui.fits.properties 
# ftccd2.ccs_gui.fits.properties ftspec1.ccs_gui.fits.properties ftspec2.ccs_gui.fits.properties 
# spectrograph.ccs_gui.fits.properties ftntmc.ccs_gui.fits.properties ftstmc.ccs_gui.fits.properties 
# frodospec1.ccs_gui.fits.properties sprat1.ccs_gui.fits.properties
CCS_GUI_FITS_BSS_PROPERTY_SRCS = o1.ics_gui.fits.bss.properties lttmc.ics_gui.fits.bss.properties \
				thor.ics_gui.fits.bss.properties ioi1.ics_gui.fits.bss.properties \
				tlan-operator.ics_gui.fits.bss.properties ioi1.ioi.ics_gui.fits.bss.properties \
				observer1.ics_gui.fits.bss.properties 
PROPERTY_OBJS	= $(PROPERTY_SRCS:%.properties=$(BINDIR)/%.properties)

SCRIPT_SRCS	= icsgui scsgui ccsgui frodospecgui spratgui ioigui lotusgui xv_wrapper.csh
SCRIPT_OBJS	= $(SCRIPT_SRCS:%=$(BINDIR)/%)

GIF_SRCS	= lt.gif lt_icon.gif ratcam.gif ratcam_ltccd1.gif ratcam_ftccd1.gif ratcam_ftccd2.gif \
		ratcam_ltccd2.gif ratcam_ltccd3.gif supircam.gif ftspec.gif nuview_capella.gif rise.gif \
		io_o.jpg io_i.jpg
GIF_OBJS	= $(GIF_SRCS:%=$(BINDIR)/%)

JPG_SRCS	= ringo-BD64106.jpg thor.jpg
JPG_OBJS	= $(JPG_SRCS:%=$(BINDIR)/%)

PNG_SRCS	= lotus.png
PNG_OBJS	= $(PNG_SRCS:%=$(BINDIR)/%)

SOUND_SRCS	= wav/command_completed.wav wav/command_failed.wav wav/filename.wav \
		wav/welcome.wav wav/iss_message.wav \
		wav/hal1moment.wav wav/halfault.wav wav/halfunctnl.wav wav/halgoodevening.wav wav/halmessage4u.wav \
		wav/halopperational.wav wav/halquitelike.wav wav/halsmoothly.wav wav/halsorrydave.wav wav/halwell.wav
SOUND_OBJS	= $(SOUND_SRCS:%=$(BINDIR)/%)



top: $(OBJS) properties gifs jpgs pngs scripts sounds docs

#
# java sources
#
$(BINDIR)/%.class:%.java
	$(JAVAC) $(JAVAC_OPTIONS) -d $(BINDIR) $<

#
# configuration files
#
properties: $(PROPERTY_OBJS)

$(BINDIR)/%.properties: %.properties
	$(CP) -f $< $@

#
# run scripts
#
scripts: $(SCRIPT_OBJS)

$(BINDIR)/icsgui: icsgui
	$(CP) icsgui $@

$(BINDIR)/ccsgui: ccsgui
	$(CP) ccsgui $@

$(BINDIR)/scsgui: scsgui
	$(CP) scsgui $@

$(BINDIR)/frodospecgui: frodospecgui
	$(CP) frodospecgui $@

$(BINDIR)/spratgui: spratgui
	$(CP) spratgui $@

$(BINDIR)/ioigui: ioigui
	$(CP) ioigui $@

$(BINDIR)/lotusgui: lotusgui
	$(CP) lotusgui $@

$(BINDIR)/xv_wrapper.csh: xv_wrapper.csh
	$(CP) xv_wrapper.csh $@

#
# gifs
#
gifs: $(GIF_OBJS) 

$(BINDIR)/%.gif: %.gif
	$(CP) $< $@

#
# jpgs
#
jpgs: $(JPG_OBJS) 

$(BINDIR)/%.jpg: %.jpg
	$(CP) $< $@

#
# pngs
#
pngs: $(PNG_OBJS) 

$(BINDIR)/%.png: %.png
	$(CP) $< $@

#
# sounds
#
sounds: $(SOUND_OBJS) 

$(BINDIR)/%.wav: %.wav
	$(CP) $< $@

#
# documentation
#
docs: $(DOCS)

$(DOCS): $(SRCS)
	$(JAVADOC) -d $(DOCSDIR) $(DOCFLAGS) $(SRCS)

checkout:
	$(CO) $(CO_OPTIONS) $(MAIN_SRCS)

checkin:
	-$(CI) $(CI_OPTIONS) $(MAIN_SRCS)

clean: tidy
	-$(RM) $(RM_OPTIONS) $(OBJS) $(DIALOG_SRCS)

tidy:
	-$(RM) $(RM_OPTIONS) $(TIDY_OPTIONS)

backup: tidy
	-$(RM) $(RM_OPTIONS) $(DIALOG_SRCS)

#
# $Log: not supported by cvs2svn $
# Revision 0.52  2015/06/09 13:16:06  cjm
# Added support for LOTUS.
#
# Revision 0.51  2014/10/02 15:35:51  cjm
# Added observer1.
#
# Revision 0.50  2014/09/04 13:29:40  cjm
# Property updates for sprat. frodospecgui / spratgui / ioigui scripts added.
#
# Revision 0.49  2014/04/04 14:42:21  cjm
# Added IcsGUISpratConfigAADialog.java and various config files.
#
# Revision 0.48  2012/03/16 12:21:14  cjm
# Added Ringo3 support.
#
# Revision 0.47  2011/11/23 11:04:52  cjm
# Added io jpegs.
#
# Revision 0.46  2011/11/07 17:11:21  cjm
# Added BSS and IO classes.
#
# Revision 0.45  2010/10/07 13:24:53  cjm
# Added thor.
#
# Revision 0.44  2010/04/08 10:20:19  cjm
# Added FRODOSPEC_LAMPFLATDialog.java .
#
# Revision 0.43  2010/01/15 14:32:57  cjm
# Added TIMED_MULTRUNATDialog.java.
#
# Revision 0.42  2009/05/05 14:24:52  cjm
# Added FRODOSPEC_LAMPFOCUSDialog.java .
#
# Revision 0.41  2009/04/30 10:14:17  cjm
# Added FRODOSPEC_ABORTDialog.java.
#
# Revision 0.40  2008/10/21 17:45:54  cjm
# Added FRODOSPEC_ARCDialog.java.
#
# Revision 0.39  2008/01/11 15:00:48  cjm
# Added estar6 configs for temporary frodospec1 testing.
#
# Revision 0.38  2007/12/11 17:39:44  cjm
# Added rise config / IcsGUIRISEConfigAADialog.java.
#
# Revision 0.37  2007/08/01 13:55:53  cjm
# Added FrodoSpec config dialogs.
# Added frodospec config properties.
#
# Revision 0.36  2007/06/19 15:01:17  cjm
# Added ltccd4 config.
#
# Revision 0.35  2006/08/31 14:20:19  cjm
# Added ftsocc.ccs_gui.properties.
#
# Revision 0.34  2006/06/26 15:33:22  cjm
# Added ftspec2.
#
# Revision 0.33  2005/11/29 19:07:41  cjm
# Changed ratcam1.ics_gui.properties to ratcam1.ccs_gui.properties.
#
# Revision 0.32  2005/11/29 17:04:39  cjm
# Added IcsGUIPolarimeterConfigAADialog.java, ratcam1 configs,
# nuview and ringo icons.
#
# Revision 0.31  2004/10/13 19:53:40  cjm
# Added supircam2.
#
# Revision 0.30  2004/09/23 12:43:33  cjm
# Added fts configs.
#
# Revision 0.29  2004/07/30 12:47:10  cjm
# Added ltobs9.nuview1.ics_gui.properties.
#
# Revision 0.28  2004/07/06 18:08:30  cjm
# Added nuview1 configs.
#
# Revision 0.27  2004/05/24 18:18:19  cjm
# Added ftccd2.
#
# Revision 0.26  2004/04/19 18:15:51  cjm
# Added spectrograph configs.
#
# Revision 0.25  2004/03/23 14:30:20  cjm
# Added ftspec gif.
#
# Revision 0.24  2004/03/23 13:56:49  cjm
# Added ftspec1 configs.
#
# Revision 0.23  2004/02/04 14:06:27  cjm
# Changed properties.
#
# Revision 0.22  2004/01/15 15:20:22  cjm
# Added different ccs_gui_config.properties per installation.
# Added different ccs_gui.fits.properties per installation.
#
# Revision 0.21  2004/01/13 20:16:08  cjm
# Added properties.
#
# Revision 0.20  2003/11/14 15:07:01  cjm
# Added new sources for FTSpec AA box.
# Added new ccs_gui config files.
# Added separate ccs_gui_config.properties for different released machines,
# so that can contain different filter settings when deployed
# (i.e. RATCam/FARTCam have different filters in the wheels.
#
# Revision 0.19  2003/09/19 14:41:24  cjm
# n
# Changed some CcsGUI sources to IcsGUI.
# Added sound make rules.
#
# Revision 0.18  2003/07/15 16:21:43  cjm
# Added IcsGUIIRCamConfigAADialog source.
#
# Revision 0.17  2003/06/06 15:46:29  cjm
# *** empty log message ***
#
# Revision 0.16  2003/03/11 11:11:11  cjm
# Added properties.
#
# Revision 0.15  2002/12/16 19:13:13  cjm
# Added config properties.
#
# Revision 0.14  2002/05/23 12:49:27  cjm
# Added DAY_CALIBRATEDialog.java and TWILIGHT_CALIBRATEDialog.java dialogs.
#
# Revision 0.13  2001/07/11 10:24:45  cjm
# Added IcsGUIFilenameShowListener and changes to configuration files.
#
# Revision 0.12  2001/03/30 15:56:56  cjm
# Added ltobs5.ccs_gui.properties.
#
# Revision 0.11  2000/12/01 18:43:14  cjm
# New config dialogs for different instruments.
#
# Revision 0.10  2000/11/14 11:55:50  cjm
# Added ltobs5.scs_gui.properties.
#
# Revision 0.9  2000/06/19 08:50:34  cjm
# Backup.
#
# Revision 0.8  2000/06/13 17:26:12  cjm
# Changes.
#
# Revision 0.7  1999/12/14 12:18:27  cjm
# Added splash gif targets.
#
# Revision 0.6  1999/12/10 11:57:06  cjm
# Added backup target.
#
# Revision 0.5  1999/12/09 16:36:42  cjm
# *** empty log message ***
#
# Revision 0.4  1999/12/07 17:50:45  cjm
# Added auto-update sources.
#
# Revision 0.3  1999/11/30 11:25:54  cjm
# Added CcsGUIWindowListener source file.
#
# Revision 0.2  1999/11/29 11:41:48  cjm
# Removed source files now in ngat.swing;
#
# Revision 0.1  1999/11/22 09:53:49  cjm
# initial revision.
#
#
