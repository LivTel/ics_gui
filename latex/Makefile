# $Header: /home/cjm/cvs/ics_gui/latex/Makefile,v 0.5 2004-08-06 12:59:26 cjm Exp $

include ../../Makefile.common
include ../Makefile.common

PDFLATEX	= pdflatex

DOCSDIR 	= $(CCS_GUI_DOC_HOME)/latex

TEXSRCS 	= ccs_gui_design.tex icsgui_basic_user_guide.tex
FIGSRCS 	= rcs_ccs_interaction.fig
JPGSRCS = pci_card_low_res_annotated.jpg sdsu_psu_low_res_annotated.jpg sun_ultra5_back_low_res_annotated.jpg \
	sdsu_dewar_electronics_back_low_res_annotated.jpg
TIFFSRCS = icsgui_bias_dialog.tiff icsgui_config_aa_dialog.tiff icsgui_config_dialog.tiff \
	icsgui_config_list_dialog.tiff icsgui_dark_dialog.tiff icsgui_expose_menu.tiff icsgui_interrupt_menu.tiff \
	icsgui_iss_menu.tiff icsgui_main_screen.tiff icsgui_multrun_dialog.tiff icsgui_reboot_dialog.tiff \
	icsgui_send_menu.tiff icsgui_setup_menu.tiff terminal_low_res.tiff

DVIOBJS		= $(TEXSRCS:%.tex=%.dvi)
PSOBJS		= $(TEXSRCS:%.tex=$(DOCSDIR)/%.ps)
PDFOBJS		= $(TEXSRCS:%.tex=$(DOCSDIR)/%.pdf)

EPSOBJS		= $(FIGSRCS:%.fig=%.eps) $(JPGSRCS:%.jpg=%.eps) $(TIFFSRCS:%.tiff=%.eps)

DIRS 		= 

top: $(PSOBJS)

pdf: $(PDFOBJS)

$(DOCSDIR)/%.pdf: %.tex
	$(PDFLATEX) $<
#	$(PDFLATEX) $<
#	$(PDFLATEX) $<
#	$(DVIPS) $< -o
	$(MV) $*.pdf $(DOCSDIR)


$(DOCSDIR)/%.ps: %.dvi
	$(DVIPS) $< -o
	$(MV) $*.ps $(DOCSDIR)

#$(DVIOBJS): $(EPSOBJS)
ccs_gui_design.dvi: ccs_gui_ui_main.ps ccs_gui_ui_menu.ps ccs_gui_ui_runat.ps ccs_gui_ui_error.ps

icsgui_basic_user_guide.dvi: rcs_ccs_interaction.eps pci_card_low_res_annotated.eps sdsu_psu_low_res_annotated.eps \
	sun_ultra5_back_low_res_annotated.eps sdsu_dewar_electronics_back_low_res_annotated.eps \
	icsgui_bias_dialog.eps icsgui_config_aa_dialog.eps icsgui_config_dialog.eps \
	icsgui_config_list_dialog.eps icsgui_dark_dialog.eps icsgui_expose_menu.eps icsgui_interrupt_menu.eps \
	icsgui_iss_menu.eps icsgui_main_screen.eps icsgui_multrun_dialog.eps icsgui_reboot_dialog.eps \
	icsgui_send_menu.eps icsgui_setup_menu.eps terminal_low_res.eps

%.dvi: %.tex
	$(LATEX) $<
	$(LATEX) $<
	$(LATEX) $<

%.eps: %.jpg
	$(CONVERT) $< $@

%.eps: %.tiff
	$(CONVERT) $< $@

%.eps: %.fig
	$(FIG2DEV) $(FIG2DEV_OPTIONS) $< $@

depend:
	echo "NO depend rule for latex files."

clean:
	-$(RM) $(RM_OPTIONS) $(DVIOBJS) $(PSOBJS) $(EPSOBJS) $(LATEX_TIDY_OPTIONS) $(TIDY_OPTIONS)

tidy:
	-$(RM) $(RM_OPTIONS) $(LATEX_TIDY_OPTIONS) $(TIDY_OPTIONS)

backup: tidy
	-$(RM) $(RM_OPTIONS) $(DVIOBJS) $(EPSOBJS)

checkin:
	-$(CI) $(CI_OPTIONS) $(TEXSRCS) $(FIGSRCS) $(JPGSRCS) $(TIFFSRCS)

checkout:
	-$(CO) $(CO_OPTIONS) $(TEXSRCS) $(FIGSRCS) $(JPGSRCS) $(TIFFSRCS)

#
# $Log: not supported by cvs2svn $
# Revision 0.4  2003/11/14 15:30:29  cjm
# Added icsgui_basic_user_guide.
#
# Revision 0.3  2000/10/03 16:40:43  cjm
# Changed fig2dev options.
#
# Revision 0.2  1999/12/10 11:59:44  cjm
# Added backup target.
#
# Revision 0.1  1999/11/22 09:50:53  cjm
# initial revision.
#
#

