# Makefile
# $Header: /home/cjm/cvs/ics_gui/Makefile,v 1.4 2020-04-29 13:56:15 cjm Exp $ 

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
	tar cvf $(BACKUP_DIR)/ics_gui.tar .
	compress $(BACKUP_DIR)/ics_gui.tar

# $Log: not supported by cvs2svn $
# Revision 1.3  2007/09/26 17:40:32  cjm
# Swaped back to cd's - Solaris make doesn't support -C.
#
# Revision 1.2  2007/01/09 15:21:56  cjm
# Swapped 'cd's for $(MAKE) -C.
#
# Revision 1.1  2003/11/14 15:26:56  cjm
# Initial revision
#
