# Makefile
# $Header: /home/cjm/cvs/ics_gui/Makefile,v 1.2 2007-01-09 15:21:56 cjm Exp $ 

include ../Makefile.common

# create_dialog must come before java - it generates source code for the java directory
DIRS = create_dialog java latex

top:
	@for i in $(DIRS); \
	do \
		(echo making in $$i...; $(MAKE) -C $$i ); \
	done;

checkin:
	-@for i in $(DIRS); \
	do \
		(echo checkin in $$i...; $(MAKE) -C $$i checkin; cd $$i; $(CI) $(CI_OPTIONS) Makefile); \
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
		(echo clean in $$i...; $(MAKE) -C $$i clean); \
	done;

tidy:
	-$(RM) $(RM_OPTIONS) $(TIDY_OPTIONS)
	@for i in $(DIRS); \
	do \
		(echo tidy in $$i...; $(MAKE) -C $$i tidy); \
	done;

backup: checkin
	@for i in $(DIRS); \
	do \
		(echo backup in $$i...; $(MAKE) -C $$i backup); \
	done;
	tar cvf $(BACKUP_DIR)/ccs_gui.tar .
	compress $(BACKUP_DIR)/ccs_gui.tar

# $Log: not supported by cvs2svn $
# Revision 1.1  2003/11/14 15:26:56  cjm
# Initial revision
#
