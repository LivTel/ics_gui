# Makefile -*- mode: Fundamental;-*-
# $Header: /home/cjm/cvs/ics_gui/java/Makefile,v 0.7 1999-12-14 12:18:27 cjm Exp $ 
include ../../Makefile.common
include	../Makefile.common

INCDIR 		= $(CCS_GUI_SRC_HOME)/include
BINDIR 		= $(CCS_GUI_BIN_HOME)/java
DOCSDIR 	= $(CCS_GUI_DOC_HOME)/javadocs
DOCFLAGS 	= -version -author -private

# note - only MAIN_SRCS checked in/out
# note - DIALOG_SRCS are deleted on clean target to ensure they are re-built
SRCS 		= $(MAIN_SRCS) $(DIALOG_SRCS)

MAIN_SRCS	= CcsGUI.java CcsGUIStatus.java CcsGUIWindowListener.java CcsGUIMenuItemListener.java GUIDialogListener.java CcsGUIDialogListener.java CcsCommandDialog.java CcsGUIClientConnectionThread.java $(SERVER_SRCS) $(CONFIG_SRCS) $(UPDATE_SRCS)

SERVER_SRCS	= CcsGUIServer.java CcsGUIServerConnectionThread.java
CONFIG_SRCS	= CcsCCDConfigProperties.java CcsCCDConfigListDialog.java CcsGUIConfigButtonListener.java CcsCCDConfigAADialog.java CcsConfigAADialogListener.java 
UPDATE_SRCS	= CcsGUIUpdateListener.java CcsGUIUpdateThread.java

DIALOG_SRCS	= ACQUIREDialog.java $(CALIBRATE_DIALOG_SRCS) $(EXPOSE_DIALOG_SRCS) $(INTERRUPT_DIALOG_SRCS) $(SETUP_DIALOG_SRCS)

CALIBRATE_DIALOG_SRCS	= ARCDialog.java BIASDialog.java DARKDialog.java LAMPFLATDialog.java SKYFLATDialog.java
EXPOSE_DIALOG_SRCS	= GLANCEDialog.java MOVIEDialog.java MULTRUNDialog.java RUNATDialog.java SAVEDialog.java
INTERRUPT_DIALOG_SRCS	= ABORTDialog.java GET_STATUSDialog.java PAUSEDialog.java REBOOTDialog.java RESUMEDialog.java STOPDialog.java
SETUP_DIALOG_SRCS	= CONFIGDialog.java LAMPFOCUSDialog.java SET_LOGGINGDialog.java STARFOCUSDialog.java TELFOCUSDialog.java TESTDialog.java

OBJS 		= $(SRCS:%.java=$(BINDIR)/%.class)
DOCS 		= $(SRCS:%.java=$(DOCSDIR)/%.html)

top: $(OBJS) config splash docs

$(BINDIR)/%.class:%.java
	$(JAVAC) $(JAVAC_OPTIONS) -d $(BINDIR) $<

config: $(BINDIR)/ccs_gui.properties

$(BINDIR)/ccs_gui.properties: $(HOST).ccs_gui.properties
	$(CP) $(HOST).ccs_gui.properties $(BINDIR)/ccs_gui.properties

splash: $(BINDIR)/lt.gif

$(BINDIR)/lt.gif: lt.gif
	$(CP) lt.gif $(BINDIR)/lt.gif

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
