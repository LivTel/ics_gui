# $Header: /home/cjm/cvs/ics_gui/latex/Makefile,v 0.1 1999-11-22 09:50:53 cjm Exp $

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
#	@for i in $(DIRS); \
#	do \
#		(echo making in $$i...; cd $$i; $(MAKE) ); \
#	done;

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
#	$(FIG2DEV) -L ps $< $@

depend:
	echo "NO depend rule for latex files."

clean:
	$(RM) $(RM_OPTIONS) $(DVIOBJS) $(PSOBJS) $(EPSOBJS) $(LATEX_TIDY_OPTIONS) $(TIDY_OPTIONS)
#	@for i in $(DIRS); \
#	do \
#		(echo cleaning in $$i...; cd $$i; $(MAKE) clean); \
#	done;

tidy:
	$(RM) $(RM_OPTIONS) $(LATEX_TIDY_OPTIONS) $(TIDY_OPTIONS)
#	@for i in $(DIRS); \
#	do \
#		(echo tidying in $$i...; cd $$i; $(MAKE) tidy); \
#	done;

checkin:
	-$(CI) $(CI_OPTIONS) $(TEXSRCS) $(FIGSRCS)
#	@for i in $(DIRS); \
#	do \
#		(echo checking in in $$i...; cd $$i; $(MAKE) checkin); \
#	done;

checkout:
	-$(CO) $(CO_OPTIONS) $(TEXSRCS) $(FIGSRCS)
#	@for i in $(DIRS); \
#	do \
#		(echo checking out in $$i...; cd $$i; $(MAKE) checkout); \
#	done;

#
# $Log: not supported by cvs2svn $
#

