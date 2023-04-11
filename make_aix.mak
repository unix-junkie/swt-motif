#*******************************************************************************
# Copyright (c) 2000, 2004 IBM Corporation and others.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Common Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/cpl-v10.html
# 
# Contributors:
#     IBM Corporation - initial API and implementation
#     Kevin Cornell (Rational Software Corporation)
#*******************************************************************************

# Makefile for creating SWT libraries on AIX

include make_common.mak

SWT_VERSION=$(maj_ver)$(min_ver)

CC=cc_r

# Define the installation directories for various products.
#    JAVA_HOME  - The JDK > 1.3
#    CDE_HOME - CDE includes and libraries
#    MOTIF_HOME - Motif includes and libraries
JAVA_HOME   = /usr/java131
MOTIF_HOME = /bluebird/teamswt/swt-builddir/motif21
CDE_HOME   = /usr/dt

# Define the various DLL (shared) libraries to be made.

SWT_PREFIX   = swt
WS_PREFIX    = motif
SWT_LIB      = lib$(SWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).a
SWT_OBJS      = swt.o callback.o os.o os_structs.o os_custom.o os_stats.o
SWT_LIBS      = -L$(MOTIF_HOME) -G -bnoentry -lc_r -lC_r -lm -bexpall -lXm -lMrm -lXt -lX11 -lXext -liconv -lXtst

CDE_PREFIX   = swt-cde
CDE_LIB      = lib$(CDE_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).a
CDE_OBJS      = cde.o
CDE_LIBS      = -L$(CDE_HOME)/lib -bnoentry -bexpall -lDtSvc -lc

#
# The following CFLAGS are for compiling both the SWT library and the CDE
# library.
#
CFLAGS = -O -s \
	-DSWT_VERSION=$(SWT_VERSION) \
	-DAIX -DMOTIF -DCDE \
	-DNO_XPRINTING_EXTENSIONS -DNO_XINERAMA_EXTENSIONS \
	-q mbcs -qlanglvl=extended -qmaxmem=8192 \
	-I$(JAVA_HOME)/include \
	-I$(MOTIF_HOME)/include \
	-I$(CDE_HOME)/include

all: make_swt

make_swt: $(SWT_LIB)

$(SWT_LIB): $(SWT_OBJS)
	ld $(SWT_LIBS) -o $(SWT_LIB) $(SWT_OBJS)

make_cde: $(CDE_LIB)

$(CDE_LIB): $(CDE_OBJS)
	ld -o $@ $(CDE_OBJS) $(CDE_LIBS)


clean:
	rm -f *.o *.so *.a
