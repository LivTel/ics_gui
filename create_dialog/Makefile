# Makefile -*- mode: Fundamental;-*-
# $Header: /home/cjm/cvs/ics_gui/create_dialog/Makefile,v 0.3 2001-08-02 14:19:16 cjm Exp $ 
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
			ngat.message.ISS_INST.SKYFLAT ngat.message.ISS_INST.TWILIGHT_CALIBRATE
EXPOSE_DIALOG_SRCS	= ngat.message.ISS_INST.GLANCE ngat.message.ISS_INST.MOVIE ngat.message.ISS_INST.MULTRUN ngat.message.ISS_INST.RUNAT ngat.message.ISS_INST.SAVE
INTERRUPT_DIALOG_SRCS	= ngat.message.ISS_INST.ABORT ngat.message.ISS_INST.GET_STATUS ngat.message.ISS_INST.PAUSE ngat.message.ISS_INST.REBOOT ngat.message.ISS_INST.RESUME ngat.message.ISS_INST.STOP
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

$(CCS_GUI_SRC_HOME)/java/%Dialog.java: ngat.message.ISS_INST.%

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
# Revision 0.2  1999/12/10 11:52:54  cjm
# Added backup target.
#
# Revision 0.1  1999/11/22 09:51:30  cjm
# initial revision.
#
#
