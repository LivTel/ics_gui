# $Header: /home/cjm/cvs/ics_gui/latex/Makefile,v 0.3 2000-10-03 16:40:43 cjm Exp $

include ../../Makefile.common
include ../Makefile.common

DOCSDIR 	= $(CCS_GUI_DOC_HOME)/latex

TEXSRCS 	= ccs_gui_design.tex 
FIGSRCS 	= 

DVIOBJS		= $(TEXSRCS:%.tex=%.dvi)
PSOBJS		= $(TEXSRCS:%.tex=$(DOCSDIR)/%.ps)

EPSOBJS		= $(FIGSRCS:%.fig=%.eps)

DIRS 		= 

top: $(PSOBJS)

$(DOCSDIR)/%.ps: %.dvi
	$(DVIPS) $< -o
	$(MV) $*.ps $(DOCSDIR)

#$(DVIOBJS): $(EPSOBJS)
ccs_gui_design.dvi: ccs_gui_ui_main.ps ccs_gui_ui_menu.ps ccs_gui_ui_runat.ps ccs_gui_ui_error.ps

%.dvi: %.tex
	$(LATEX) $<
	$(LATEX) $<
	$(LATEX) $<

#%.eps: %.fig
#	$(FIG2DEV) $(FIG2DEV_OPTIONS) $< $@

depend:
	echo "NO depend rule for latex files."

clean:
	-$(RM) $(RM_OPTIONS) $(DVIOBJS) $(PSOBJS) $(EPSOBJS) $(LATEX_TIDY_OPTIONS) $(TIDY_OPTIONS)

tidy:
	-$(RM) $(RM_OPTIONS) $(LATEX_TIDY_OPTIONS) $(TIDY_OPTIONS)

backup: tidy
	-$(RM) $(RM_OPTIONS) $(DVIOBJS) $(EPSOBJS)

checkin:
	-$(CI) $(CI_OPTIONS) $(TEXSRCS) $(FIGSRCS)

checkout:
	-$(CO) $(CO_OPTIONS) $(TEXSRCS) $(FIGSRCS)

#
# $Log: not supported by cvs2svn $
# Revision 0.2  1999/12/10 11:59:44  cjm
# Added backup target.
#
# Revision 0.1  1999/11/22 09:50:53  cjm
# initial revision.
#
#

