# Makefile -*- mode: Fundamental;-*-
# $Header: /home/cjm/cvs/ics_gui/Makefile,v 1.1 2003-11-14 15:26:56 cjm Exp $ 

include ../Makefile.common

# create_dialog must come before java - it generates source code for the java directory
DIRS = create_dialog java latex

top:
	@for i in $(DIRS); \
	do \
		(echo making in $$i...; cd $$i; $(MAKE) ); \
	done;

checkin:
	-@for i in $(DIRS); \
	do \
		(echo checkin in $$i...; cd $$i; $(MAKE) checkin; $(CI) $(CI_OPTIONS) Makefile); \
	done;

checkout:
	@for i in $(DIRS); \
	do \
		(echo checkout in $$i...; cd $$i; $(CO) $(CO_OPTIONS) Makefile; $(MAKE) checkout); \
	done;

depend:
	echo there are no dependancies to make....

clean:
	-$(RM) $(RM_OPTIONS) $(TIDY_OPTIONS)
	@for i in $(DIRS); \
	do \
		(echo clean in $$i...; cd $$i; $(MAKE) clean); \
	done;

tidy:
	-$(RM) $(RM_OPTIONS) $(TIDY_OPTIONS)
	@for i in $(DIRS); \
	do \
		(echo tidy in $$i...; cd $$i; $(MAKE) tidy); \
	done;

backup: checkin
	@for i in $(DIRS); \
	do \
		(echo backup in $$i...; cd $$i; $(MAKE) backup); \
	done;
	tar cvf $(BACKUP_DIR)/ccs_gui.tar .
	compress $(BACKUP_DIR)/ccs_gui.tar

# $Log: not supported by cvs2svn $
