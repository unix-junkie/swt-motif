#*******************************************************************************
# Copyright (c) 2000, 2010 IBM Corporation and others.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     IBM Corporation - initial API and implementation
#*******************************************************************************

# Makefile for creating SWT libraries on Linux

include make_common.mak

SWT_VERSION=$(maj_ver)$(min_ver)

# This makefile expects the following environment variables set:
#    JAVA_HOME  - The JDK > 1.3
#    MOTIF_HOME - Motif includes and libraries

# Define the various DLL (shared) libraries to be made.

SWT_PREFIX = swt
WS_PREFIX = motif
SWT_LIB = lib$(SWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
SWT_OBJS = swt.o c.o c_stats.o callback.o os.o os_structs.o os_custom.o os_stats.o
SWT_LIBS = -L$(MOTIF_HOME)/lib -lXm -L/usr/lib -L/usr/X11R6/lib \
	           -rpath . -x -shared -lX11 -lm -lXext -lXt -ldl -lXinerama -lXtst

# Uncomment for Native Stats tool
#NATIVE_STATS = -DNATIVE_STATS

CFLAGS = -O -Wall -DSWT_VERSION=$(SWT_VERSION) $(NATIVE_STATS) -DUSE_ASSEMBLER -DLINUX -DMOTIF -DNO_XPRINTING_EXTENSIONS -fpic \
	-I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux -I$(MOTIF_HOME)/include -I/usr/X11R6/include

AWT_PREFIX = swt-awt
AWT_LIB = lib$(AWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
AWT_OBJS = swt_awt.o
JVM_ARCH = $(shell uname -m || arch)
ifeq ($(JVM_ARCH),x86_64)
JVM_ARCH = amd64
endif
AWT_LIBS = -L$(JAVA_HOME)/lib -L$(JAVA_HOME)/lib/$(JVM_ARCH) -ljawt -shared

GTK_PREFIX = swt-gtk
GTK_LIB = lib$(GTK_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
GTK_OBJS = swt.o gtk.o
GTK_CFLAGS = `pkg-config --cflags gtk+-2.0`
GTK_LIBS = -x -shared `pkg-config --libs-only-L gtk+-2.0` -lgtk-x11-2.0

CAIRO_PREFIX = swt-cairo
CAIRO_LIB = lib$(CAIRO_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
CAIRO_OBJECTS = swt.o cairo.o cairo_structs.o cairo_stats.o
CAIROCFLAGS = `pkg-config --cflags cairo`
CAIROLIBS = -shared -fpic -fPIC `pkg-config --libs-only-L cairo` -lcairo

GLX_PREFIX = swt-glx
GLX_LIB = lib$(GLX_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
GLX_OBJECTS = swt.o glx.o glx_structs.o glx_stats.o
GLXCFLAGS =
GLXLIBS = -shared -fpic -fPIC -L/usr/X11R6/lib -lGL -lGLU -lm

ifndef NO_STRIP
	CFLAGS := $(CFLAGS) -s
	CAIROLIBS := $(CAIROLIBS) -s
endif

all: make_swt make_awt make_gtk make_cairo make_glx

make_swt: $(SWT_LIB)

$(SWT_LIB): $(SWT_OBJS)
	$(LD) -o $@ $(SWT_OBJS) $(SWT_LIBS)

swt.o: swt.c swt.h
	$(CC) $(CFLAGS) -c swt.c
os.o: os.c os.h swt.h os_custom.h
	$(CC) $(CFLAGS) -c os.c
os_structs.o: os_structs.c os_structs.h os.h swt.h
	$(CC) $(CFLAGS) -c os_structs.c
os_stats.o: os_stats.c os_structs.h os.h os_stats.h swt.h
	$(CC) $(CFLAGS) -c os_stats.c

make_awt: $(AWT_LIB)

$(AWT_LIB): $(AWT_OBJS)
	ld -o $@ $(AWT_OBJS) $(AWT_LIBS)

make_gtk: $(GTK_LIB)

$(GTK_LIB): $(GTK_OBJS)
	ld -o $@ $(GTK_OBJS) $(GTK_LIBS)

gtk.o: gtk.c
	$(CC) $(CFLAGS) $(GTK_CFLAGS) -c -o gtk.o gtk.c

make_cairo: $(CAIRO_LIB)

$(CAIRO_LIB): $(CAIRO_OBJECTS)
	$(LD) -o $(CAIRO_LIB) $(CAIRO_OBJECTS) $(CAIROLIBS)

cairo.o: cairo.c cairo.h swt.h
	$(CC) $(CFLAGS) $(CAIROCFLAGS) -c cairo.c
cairo_custom.o: cairo_custom.c cairo_structs.h cairo.h swt.h
	$(CC) $(CFLAGS) $(CAIROCFLAGS) -c cairo_custom.c
cairo_structs.o: cairo_structs.c cairo_structs.h cairo.h swt.h
	$(CC) $(CFLAGS) $(CAIROCFLAGS) -c cairo_structs.c
cairo_stats.o: cairo_stats.c cairo_structs.h cairo.h cairo_stats.h swt.h
	$(CC) $(CFLAGS) $(CAIROCFLAGS) -c cairo_stats.c

make_glx: $(GLX_LIB)

$(GLX_LIB): $(GLX_OBJECTS)
	$(LD) -o $(GLX_LIB) $(GLX_OBJECTS) $(GLXLIBS)

glx.o: glx.c
	$(CC) $(CFLAGS) $(GLXCFLAGS) -c glx.c

glx_structs.o: glx_structs.c
	$(CC) $(CFLAGS) $(GLXCFLAGS) -c glx_structs.c

glx_stats.o: glx_stats.c glx_stats.h
	$(CC) $(CFLAGS) $(GLXCFLAGS) -c glx_stats.c


install: all
	cp *.so $(OUTPUT_DIR)

clean:
	rm -f *.o *.a *.so *.sl

