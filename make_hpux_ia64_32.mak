#*******************************************************************************
# Copyright (c) 2000, 2008 IBM Corporation and others.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     IBM Corporation - initial API and implementation
#     Kevin Cornell (Rational Software Corporation)
#     Sridhar Bidigalu (ICS)
#     Sumit Sarkar (Hewlett-Packard)
#*******************************************************************************

# Makefile for SWT libraries on HP-UX for 32 bit ia64 architecture

include make_common.mak

SWT_VERSION=$(maj_ver)$(min_ver)

# This makefile expects the following environment variables set:
#    JAVA_HOME  - The JDK > 1.3
#    CDE_HOME - CDE includes and libraries
#    MOTIF_HOME - Motif includes and libraries

# Define the various DLL (shared) libraries to be made.

SWT_PREFIX = swt
WS_PREFIX = motif
SWT_LIB = lib$(SWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
SWT_OBJS = swt.o c.o c_stats.o callback.o os.o os_structs.o os_custom.o os_stats.o
SWT_LIBS = -L$(MOTIF_HOME)/lib -L/usr/lib -G -lXm -lXt -lX11 -lc -ldld -lm -lXp -lXtst

CDE_PREFIX = swt-cde
CDE_LIB = lib$(CDE_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
CDE_OBJS = swt.o cde.o cde_structs.o cde_stats.o
CDE_LIBS = -G -L$(CDE_HOME)/lib -L$(CDE_HOME)/lib/hpux32 -lDtSvc

AWT_PREFIX = swt-awt
AWT_LIB = lib$(AWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
AWT_OBJS = swt_awt.o
AWT_LIBS = -G  -L/usr/lib -lX11 -lc -L$(AWT_HOME) -L$(AWT_HOME)/server -ljawt 
# Uncomment for Native Stats tool
#NATIVE_STATS = -DNATIVE_STATS

#
# The following CFLAGS are for compiling both the SWT library and the CDE
# library.
#
CFLAGS =  \
	-DSWT_VERSION=$(SWT_VERSION) $(NATIVE_STATS) \
	-DNO_XINERAMA_EXTENSIONS \
	-D_HPUX -D_POSIX_C_SOURCE=199506L -DMOTIF -DCDE \
	-I./ \
	-I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/hp-ux \
	-I$(MOTIF_HOME)/include \
	-I$(CDE_HOME)/include

all: make_swt make_awt make_cde

make_swt: $(SWT_LIB)

$(SWT_LIB): $(SWT_OBJS)
	ld +nodefaultrpath -b -z -o $@ $(SWT_OBJS) $(SWT_LIBS)

make_cde: $(CDE_LIB)

$(CDE_LIB): $(CDE_OBJS)
	ld +nodefaultrpath -b -z -o $@ $(CDE_OBJS) $(CDE_LIBS)

make_awt: $(AWT_LIB)

$(AWT_LIB): $(AWT_OBJS)
	ld +nodefaultrpath -b -z -o $(AWT_LIB) $(AWT_OBJS) $(AWT_LIBS)
	
install: all
	cp *.so $(OUTPUT_DIR)

clean:
	rm -f *.o *.a *.so *.sl 
