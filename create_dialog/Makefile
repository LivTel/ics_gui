# Makefile
# $Header: /home/cjm/cvs/ics_gui/create_dialog/Makefile,v 0.7 2009-04-30 10:05:15 cjm Exp $ 
include ../../Makefile.common
include	../Makefile.common

INCDIR 		= $(CCS_GUI_SRC_HOME)/include
BINDIR 		= $(CCS_GUI_BIN_HOME)/create_dialog
DOCSDIR 	= $(CCS_GUI_DOC_HOME)/create_dialog_javadocs
DOCFLAGS 	= -version -author -private

SRCS 		= CreateDialog.java
OBJS 		= $(SRCS:%.java=$(BINDIR)/%.class)
DOCS 		= $(SRCS:%.java=$(DOCSDIR)/%.html)

CALIBRATE_DIALOG_SRCS	= ngat.message.ISS_INST.ARC ngat.message.ISS_INST.BIAS ngat.message.ISS_INST.DARK \
			ngat.message.ISS_INST.DAY_CALIBRATE ngat.message.ISS_INST.LAMPFLAT \
			ngat.message.ISS_INST.SKYFLAT ngat.message.ISS_INST.TWILIGHT_CALIBRATE \
			ngat.message.ISS_INST.FRODOSPEC_ARC \
			ngat.message.ISS_INST.FRODOSPEC_BIAS ngat.message.ISS_INST.FRODOSPEC_DARK \
			ngat.message.ISS_INST.FRODOSPEC_DAY_CALIBRATE \
			ngat.message.ISS_INST.FRODOSPEC_TWILIGHT_CALIBRATE
EXPOSE_DIALOG_SRCS	= ngat.message.ISS_INST.GLANCE ngat.message.ISS_INST.MOVIE ngat.message.ISS_INST.MULTRUN \
	ngat.message.ISS_INST.RUNAT ngat.message.ISS_INST.SAVE \
	ngat.message.ISS_INST.FRODOSPEC_GLANCE ngat.message.ISS_INST.FRODOSPEC_MULTRUN \
	ngat.message.ISS_INST.FRODOSPEC_RUNAT 
INTERRUPT_DIALOG_SRCS	= ngat.message.ISS_INST.ABORT ngat.message.ISS_INST.GET_STATUS ngat.message.ISS_INST.PAUSE ngat.message.ISS_INST.REBOOT ngat.message.ISS_INST.RESUME ngat.message.ISS_INST.STOP ngat.message.ISS_INST.FRODOSPEC_ABORT
SETUP_DIALOG_SRCS	= ngat.message.ISS_INST.CONFIG ngat.message.ISS_INST.LAMPFOCUS ngat.message.ISS_INST.SET_LOGGING ngat.message.ISS_INST.STARFOCUS ngat.message.ISS_INST.TELFOCUS ngat.message.ISS_INST.TEST 
DIALOG_SRCS	= ngat.message.ISS_INST.ACQUIRE $(CALIBRATE_DIALOG_SRCS) $(EXPOSE_DIALOG_SRCS) $(INTERRUPT_DIALOG_SRCS) $(SETUP_DIALOG_SRCS)
DIALOG_OBJS 	= $(DIALOG_SRCS:ngat.message.ISS_INST.%=$(CCS_GUI_SRC_HOME)/java/%Dialog.java)

top: $(OBJS) docs dialogs

$(BINDIR)/%.class:%.java
	$(JAVAC) $(JAVAC_OPTIONS) -d $(BINDIR) $<

docs: $(DOCS)

$(DOCS): $(SRCS)
	$(JAVADOC) -d $(DOCSDIR) $(DOCFLAGS) $(SRCS)

dialogs: $(DIALOG_OBJS)

# This works under Solaris
$(CCS_GUI_SRC_HOME)/java/%Dialog.java:ngat.message.ISS_INST.%

# This works under Linux
$(CCS_GUI_SRC_HOME)/java/ACQUIREDialog.java:ngat.message.ISS_INST.ACQUIRE
$(CCS_GUI_SRC_HOME)/java/ARCDialog.java:ngat.message.ISS_INST.ARC
$(CCS_GUI_SRC_HOME)/java/BIASDialog.java:ngat.message.ISS_INST.BIAS
$(CCS_GUI_SRC_HOME)/java/DARKDialog.java:ngat.message.ISS_INST.DARK
$(CCS_GUI_SRC_HOME)/java/DAY_CALIBRATEDialog.java:ngat.message.ISS_INST.DAY_CALIBRATE
$(CCS_GUI_SRC_HOME)/java/LAMPFLATDialog.java:ngat.message.ISS_INST.LAMPFLAT
$(CCS_GUI_SRC_HOME)/java/SKYFLATDialog.java:ngat.message.ISS_INST.SKYFLAT
$(CCS_GUI_SRC_HOME)/java/TWILIGHT_CALIBRATEDialog.java:ngat.message.ISS_INST.TWILIGHT_CALIBRATE
$(CCS_GUI_SRC_HOME)/java/FRODOSPEC_ARCDialog.java:ngat.message.ISS_INST.FRODOSPEC_ARC
$(CCS_GUI_SRC_HOME)/java/FRODOSPEC_BIASDialog.java:ngat.message.ISS_INST.FRODOSPEC_BIAS
$(CCS_GUI_SRC_HOME)/java/FRODOSPEC_DARKDialog.java:ngat.message.ISS_INST.FRODOSPEC_DARK
$(CCS_GUI_SRC_HOME)/java/FRODOSPEC_DAY_CALIBRATEDialog.java:ngat.message.ISS_INST.FRODOSPEC_DAY_CALIBRATE
$(CCS_GUI_SRC_HOME)/java/FRODOSPEC_TWILIGHT_CALIBRATEDialog.java:ngat.message.ISS_INST.FRODOSPEC_TWILIGHT_CALIBRATE
$(CCS_GUI_SRC_HOME)/java/GLANCEDialog.java:ngat.message.ISS_INST.GLANCE
$(CCS_GUI_SRC_HOME)/java/MOVIEDialog.java:ngat.message.ISS_INST.MOVIE
$(CCS_GUI_SRC_HOME)/java/MULTRUNDialog.java:ngat.message.ISS_INST.MULTRUN
$(CCS_GUI_SRC_HOME)/java/FRODOSPEC_GLANCEDialog.java:ngat.message.ISS_INST.FRODOSPEC_GLANCE
$(CCS_GUI_SRC_HOME)/java/FRODOSPEC_MULTRUNDialog.java:ngat.message.ISS_INST.FRODOSPEC_MULTRUN
$(CCS_GUI_SRC_HOME)/java/FRODOSPEC_RUNATDialog.java:ngat.message.ISS_INST.FRODOSPEC_RUNAT
$(CCS_GUI_SRC_HOME)/java/RUNATDialog.java:ngat.message.ISS_INST.RUNAT
$(CCS_GUI_SRC_HOME)/java/SAVEDialog.java:ngat.message.ISS_INST.SAVE
$(CCS_GUI_SRC_HOME)/java/ABORTDialog.java:ngat.message.ISS_INST.ABORT
$(CCS_GUI_SRC_HOME)/java/FRODOSPEC_ABORTDialog.java:ngat.message.ISS_INST.FRODOSPEC_ABORT
$(CCS_GUI_SRC_HOME)/java/GET_STATUSDialog.java:ngat.message.ISS_INST.GET_STATUS
$(CCS_GUI_SRC_HOME)/java/PAUSEDialog.java:ngat.message.ISS_INST.PAUSE
$(CCS_GUI_SRC_HOME)/java/REBOOTDialog.java:ngat.message.ISS_INST.REBOOT
$(CCS_GUI_SRC_HOME)/java/RESUMEDialog.java:ngat.message.ISS_INST.RESUME
$(CCS_GUI_SRC_HOME)/java/STOPDialog.java:ngat.message.ISS_INST.STOP
$(CCS_GUI_SRC_HOME)/java/CONFIGDialog.java:ngat.message.ISS_INST.CONFIG
$(CCS_GUI_SRC_HOME)/java/LAMPFOCUSDialog.java:ngat.message.ISS_INST.LAMPFOCUS
$(CCS_GUI_SRC_HOME)/java/SET_LOGGINGDialog.java:ngat.message.ISS_INST.SET_LOGGING
$(CCS_GUI_SRC_HOME)/java/STARFOCUSDialog.java:ngat.message.ISS_INST.STARFOCUS
$(CCS_GUI_SRC_HOME)/java/TELFOCUSDialog.java:ngat.message.ISS_INST.TELFOCUS
$(CCS_GUI_SRC_HOME)/java/TESTDialog.java:ngat.message.ISS_INST.TEST

ngat.message.ISS_INST.%:
	(cd $(BINDIR); $(JAVA) CreateDialog -c $@; mv $*Dialog.java $(CCS_GUI_SRC_HOME)/java/);

checkout:
	$(CO) $(CO_OPTIONS) $(SRCS)

checkin:
	-$(CI) $(CI_OPTIONS) $(SRCS)

clean: tidy
	-$(RM) $(RM_OPTIONS) $(OBJS) $(DIALOG_OBJS)

tidy:
	-$(RM) $(RM_OPTIONS) $(TIDY_OPTIONS)

backup: tidy
	-$(RM) $(RM_OPTIONS) $(DIALOG_OBJS)

#
# $Log: not supported by cvs2svn $
# Revision 0.6  2008/10/21 17:46:05  cjm
# Added FRODOSPEC_ARC.
#
# Revision 0.5  2008/03/05 14:35:07  cjm
# Added FrodoSpec dialogs.
#
# Revision 0.4  2004/04/26 11:29:28  cjm
# Added Linux compatibility stuff for calling CreateDialog OK.
#
# Revision 0.3  2001/08/02 14:19:16  cjm
# Added source classes DAY_CALIBRATE and TWILIGHT_CALIBRATE.
#
# Revision 0.2  1999/12/10 11:52:54  cjm
# Added backup target.
#
# Revision 0.1  1999/11/22 09:51:30  cjm
# initial revision.
#
#
