# Makefile
# $Header: /home/cjm/cvs/ics_gui/create_dialog/Makefile,v 0.12 2020-05-05 10:20:00 cjm Exp $ 
include ../../Makefile.common
include	../Makefile.common

INCDIR 		= $(ICS_GUI_SRC_HOME)/include
BINDIR 		= $(ICS_GUI_BIN_HOME)/create_dialog
DOCSDIR 	= $(ICS_GUI_DOC_HOME)/create_dialog_javadocs
DOCFLAGS 	= -version -author -private

SRCS 		= CreateDialog.java
OBJS 		= $(SRCS:%.java=$(BINDIR)/%.class)
DOCS 		= $(SRCS:%.java=$(DOCSDIR)/%.html)

CALIBRATE_DIALOG_SRCS	= ngat.message.ISS_INST.ARC ngat.message.ISS_INST.BIAS ngat.message.ISS_INST.DARK \
			ngat.message.ISS_INST.DAY_CALIBRATE ngat.message.ISS_INST.LAMPFLAT \
			ngat.message.ISS_INST.MULTBIAS ngat.message.ISS_INST.MULTDARK \
			ngat.message.ISS_INST.SKYFLAT ngat.message.ISS_INST.TWILIGHT_CALIBRATE \
			ngat.message.ISS_INST.FRODOSPEC_ARC \
			ngat.message.ISS_INST.FRODOSPEC_BIAS ngat.message.ISS_INST.FRODOSPEC_DARK \
			ngat.message.ISS_INST.FRODOSPEC_DAY_CALIBRATE ngat.message.ISS_INST.FRODOSPEC_LAMPFLAT \
			ngat.message.ISS_INST.FRODOSPEC_TWILIGHT_CALIBRATE
EXPOSE_DIALOG_SRCS	= ngat.message.ISS_INST.GLANCE ngat.message.ISS_INST.MOVIE ngat.message.ISS_INST.MULTRUN \
			ngat.message.ISS_INST.RUNAT ngat.message.ISS_INST.TIMED_MULTRUNAT ngat.message.ISS_INST.SAVE \
			ngat.message.ISS_INST.FRODOSPEC_GLANCE ngat.message.ISS_INST.FRODOSPEC_MULTRUN \
			ngat.message.ISS_INST.FRODOSPEC_RUNAT 
INTERRUPT_DIALOG_SRCS	= ngat.message.ISS_INST.ABORT ngat.message.ISS_INST.GET_STATUS ngat.message.ISS_INST.PAUSE \
			ngat.message.ISS_INST.REBOOT ngat.message.ISS_INST.RESUME ngat.message.ISS_INST.STOP \
			ngat.message.ISS_INST.FRODOSPEC_ABORT
SETUP_DIALOG_SRCS	= ngat.message.ISS_INST.CONFIG ngat.message.ISS_INST.LAMPFOCUS \
			ngat.message.ISS_INST.SET_LOGGING ngat.message.ISS_INST.STARFOCUS \
			ngat.message.ISS_INST.TELFOCUS ngat.message.ISS_INST.TEST \
			ngat.message.ISS_INST.FRODOSPEC_LAMPFOCUS 
DIALOG_SRCS	= ngat.message.ISS_INST.ACQUIRE $(CALIBRATE_DIALOG_SRCS) $(EXPOSE_DIALOG_SRCS) $(INTERRUPT_DIALOG_SRCS) $(SETUP_DIALOG_SRCS)
DIALOG_OBJS 	= $(DIALOG_SRCS:ngat.message.ISS_INST.%=$(ICS_GUI_SRC_HOME)/java/%Dialog.java)

top: $(OBJS) docs dialogs

$(BINDIR)/%.class:%.java
	$(JAVAC) $(JAVAC_OPTIONS) -d $(BINDIR) $<

docs: $(DOCS)

$(DOCS): $(SRCS)
	$(JAVADOC) -d $(DOCSDIR) $(DOCFLAGS) $(SRCS)

dialogs: $(DIALOG_OBJS)

# This works under Solaris
$(ICS_GUI_SRC_HOME)/java/%Dialog.java:ngat.message.ISS_INST.%

# This works under Linux
$(ICS_GUI_SRC_HOME)/java/ACQUIREDialog.java:ngat.message.ISS_INST.ACQUIRE
$(ICS_GUI_SRC_HOME)/java/ARCDialog.java:ngat.message.ISS_INST.ARC
$(ICS_GUI_SRC_HOME)/java/BIASDialog.java:ngat.message.ISS_INST.BIAS
$(ICS_GUI_SRC_HOME)/java/DARKDialog.java:ngat.message.ISS_INST.DARK
$(ICS_GUI_SRC_HOME)/java/DAY_CALIBRATEDialog.java:ngat.message.ISS_INST.DAY_CALIBRATE
$(ICS_GUI_SRC_HOME)/java/LAMPFLATDialog.java:ngat.message.ISS_INST.LAMPFLAT
$(ICS_GUI_SRC_HOME)/java/MULTBIASDialog.java:ngat.message.ISS_INST.MULTBIAS
$(ICS_GUI_SRC_HOME)/java/MULTDARKDialog.java:ngat.message.ISS_INST.MULTDARK
$(ICS_GUI_SRC_HOME)/java/SKYFLATDialog.java:ngat.message.ISS_INST.SKYFLAT
$(ICS_GUI_SRC_HOME)/java/TWILIGHT_CALIBRATEDialog.java:ngat.message.ISS_INST.TWILIGHT_CALIBRATE
$(ICS_GUI_SRC_HOME)/java/FRODOSPEC_ARCDialog.java:ngat.message.ISS_INST.FRODOSPEC_ARC
$(ICS_GUI_SRC_HOME)/java/FRODOSPEC_BIASDialog.java:ngat.message.ISS_INST.FRODOSPEC_BIAS
$(ICS_GUI_SRC_HOME)/java/FRODOSPEC_DARKDialog.java:ngat.message.ISS_INST.FRODOSPEC_DARK
$(ICS_GUI_SRC_HOME)/java/FRODOSPEC_DAY_CALIBRATEDialog.java:ngat.message.ISS_INST.FRODOSPEC_DAY_CALIBRATE
$(ICS_GUI_SRC_HOME)/java/FRODOSPEC_LAMPFLATDialog.java:ngat.message.ISS_INST.FRODOSPEC_LAMPFLAT
$(ICS_GUI_SRC_HOME)/java/FRODOSPEC_TWILIGHT_CALIBRATEDialog.java:ngat.message.ISS_INST.FRODOSPEC_TWILIGHT_CALIBRATE
$(ICS_GUI_SRC_HOME)/java/GLANCEDialog.java:ngat.message.ISS_INST.GLANCE
$(ICS_GUI_SRC_HOME)/java/MOVIEDialog.java:ngat.message.ISS_INST.MOVIE
$(ICS_GUI_SRC_HOME)/java/MULTRUNDialog.java:ngat.message.ISS_INST.MULTRUN
$(ICS_GUI_SRC_HOME)/java/FRODOSPEC_GLANCEDialog.java:ngat.message.ISS_INST.FRODOSPEC_GLANCE
$(ICS_GUI_SRC_HOME)/java/FRODOSPEC_MULTRUNDialog.java:ngat.message.ISS_INST.FRODOSPEC_MULTRUN
$(ICS_GUI_SRC_HOME)/java/FRODOSPEC_RUNATDialog.java:ngat.message.ISS_INST.FRODOSPEC_RUNAT
$(ICS_GUI_SRC_HOME)/java/RUNATDialog.java:ngat.message.ISS_INST.RUNAT
$(ICS_GUI_SRC_HOME)/java/TIMED_MULTRUNATDialog.java:ngat.message.ISS_INST.TIMED_MULTRUNAT
$(ICS_GUI_SRC_HOME)/java/SAVEDialog.java:ngat.message.ISS_INST.SAVE
$(ICS_GUI_SRC_HOME)/java/ABORTDialog.java:ngat.message.ISS_INST.ABORT
$(ICS_GUI_SRC_HOME)/java/FRODOSPEC_ABORTDialog.java:ngat.message.ISS_INST.FRODOSPEC_ABORT
$(ICS_GUI_SRC_HOME)/java/GET_STATUSDialog.java:ngat.message.ISS_INST.GET_STATUS
$(ICS_GUI_SRC_HOME)/java/PAUSEDialog.java:ngat.message.ISS_INST.PAUSE
$(ICS_GUI_SRC_HOME)/java/REBOOTDialog.java:ngat.message.ISS_INST.REBOOT
$(ICS_GUI_SRC_HOME)/java/RESUMEDialog.java:ngat.message.ISS_INST.RESUME
$(ICS_GUI_SRC_HOME)/java/STOPDialog.java:ngat.message.ISS_INST.STOP
$(ICS_GUI_SRC_HOME)/java/CONFIGDialog.java:ngat.message.ISS_INST.CONFIG
$(ICS_GUI_SRC_HOME)/java/LAMPFOCUSDialog.java:ngat.message.ISS_INST.LAMPFOCUS
$(ICS_GUI_SRC_HOME)/java/SET_LOGGINGDialog.java:ngat.message.ISS_INST.SET_LOGGING
$(ICS_GUI_SRC_HOME)/java/STARFOCUSDialog.java:ngat.message.ISS_INST.STARFOCUS
$(ICS_GUI_SRC_HOME)/java/TELFOCUSDialog.java:ngat.message.ISS_INST.TELFOCUS
$(ICS_GUI_SRC_HOME)/java/TESTDialog.java:ngat.message.ISS_INST.TEST
$(ICS_GUI_SRC_HOME)/java/FRODOSPEC_LAMPFOCUSDialog.java:ngat.message.ISS_INST.FRODOSPEC_LAMPFOCUS

ngat.message.ISS_INST.%:
	(cd $(BINDIR); $(JAVA) CreateDialog -c $@; mv $*Dialog.java $(ICS_GUI_SRC_HOME)/java/);

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
# Revision 0.11  2011/11/07 17:06:54  cjm
# Added support for MULTBIAS and MULTDARK.
#
# Revision 0.10  2010/04/07 16:33:08  cjm
# Added FRODOSPEC_LAMPFLAT.
#
# Revision 0.9  2010/01/15 11:36:53  cjm
# Added TIMED_MULTRUNAT dialog.
#
# Revision 0.8  2009/05/05 14:21:41  cjm
# Added FRODOSPEC_LAMPFOCUS dialog.
#
# Revision 0.7  2009/04/30 10:05:15  cjm
# Added FRODOSPEC_ABORT dialog.
#
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
