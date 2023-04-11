#*******************************************************************************
# Copyright (c) 2000, 2004 IBM Corporation and others.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Common Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/cpl-v10.html
# 
# Contributors:
#     IBM Corporation - initial API and implementation
#*******************************************************************************

# Makefile for creating SWT libraries on Linux

include make_common.mak

SWT_VERSION=$(maj_ver)$(min_ver)

# Define the installation directories for various products.
#    JAVA_HOME  - The JDK > 1.3
#    MOTIF_HOME - Motif includes and libraries
#    QT_HOME    - identifier namespace package (used by KDE)
JAVA_HOME   = /bluebird/teamswt/swt-builddir/IBMJava2-141
MOTIF_HOME = /bluebird/teamswt/swt-builddir/motif21
# Redhat 9
QT_HOME    = /usr/lib/qt-3.1
# SuSE 8.2
#QT_HOME    = /usr/lib/qt3

# Define the various DLL (shared) libraries to be made.

SWT_PREFIX   = swt
WS_PREFIX    = motif
SWT_LIB      = lib$(SWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
SWT_OBJS      = swt.o callback.o os.o os_structs.o os_custom.o os_stats.o
SWT_LIBS      = -L$(MOTIF_HOME)/lib -lXm -L/usr/lib -L/usr/X11R6/lib \
	           -rpath . -x -shared -lX11 -lm -lXext -lXt -lXp -ldl -lXinerama -lXtst
CFLAGS = -O -s -DSWT_VERSION=$(SWT_VERSION) -DLINUX -DMOTIF  -fpic -I./ \
	-I$(JAVA_HOME)/include -I$(MOTIF_HOME)/include -I/usr/X11R6/include 

GNOME_PREFIX = swt-gnome
GNOME_LIB    = lib$(GNOME_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
GNOME_OBJECTS= swt.o gnome.o gnome_structs.o gnome_stats.o
GNOME_CFLAGS = `pkg-config --cflags gnome-vfs-module-2.0 libgnome-2.0 libgnomeui-2.0`
GNOME_LIBS = -shared -fpic -fPIC `pkg-config --libs gnome-vfs-module-2.0 libgnome-2.0 libgnomeui-2.0`

KDE_PREFIX   = swt-kde
KDE_LIB      = lib$(KDE_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
KDE_OBJS      = kde.o
KDE_LIBS      = -L/usr/lib  -L$(QT_HOME)/lib -shared  -lkdecore -lqt
KDE_CFLAGS   = -fno-rtti -c -O -I/usr/include/kde -I$(QT_HOME)/include -I$(JAVA_HOME)/include

AWT_PREFIX   = swt-awt
AWT_LIB      = lib$(AWT_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
AWT_OBJS      = swt_awt.o
AWT_LIBS      = -L$(JAVA_HOME)/jre/bin -ljawt -shared

GTK_PREFIX  = swt-gtk
GTK_LIB     = lib$(GTK_PREFIX)-$(WS_PREFIX)-$(SWT_VERSION).so
GTK_OBJS     = swt.o gtk.o
GTK_CFLAGS  = `pkg-config --cflags gtk+-2.0`
GTK_LIBS     = -x -shared `pkg-config --libs-only-l --libs-only-L gtk+-2.0`
	
all: make_swt make_awt make_gnome make_gtk

kde: make_kde

make_swt: $(SWT_LIB)

$(SWT_LIB): $(SWT_OBJS)
	$(LD) -o $@ $(SWT_OBJS) $(SWT_LIBS)
	
swt.o: swt.c swt.h
	$(CC) $(CFLAGS) -c swt.c
os.o: os.c os.h swt.h os_custom.h
	$(CC) $(CFLAGS) -c os.c
os_structs.o: os_structs.c os_structs.h os.h swt.h
	$(CC) $(CFLAGS) -c os_structs.c 
os_custom.o: os_custom.c os_structs.h os.h swt.h
	$(CC) $(CFLAGS) -c os_custom.c
os_stats.o: os_stats.c os_structs.h os.h os_stats.h swt.h
	$(CC) $(CFLAGS) -c os_stats.c

make_gnome: $(GNOME_LIB)

$(GNOME_LIB): $(GNOME_OBJECTS)
	gcc -o $@ $(GNOME_OBJECTS) $(GNOME_LIBS)

gnome.o: gnome.c
	gcc -O -Wall -DSWT_VERSION=$(SWT_VERSION) -DLINUX -DGTK -I$(JAVA_HOME)/include $(GNOME_CFLAGS) -c -o gnome.o gnome.c

gnome_structs.o: gnome_structs.c
	gcc -O -Wall -DSWT_VERSION=$(SWT_VERSION) -DLINUX -DGTK -I$(JAVA_HOME)/include $(GNOME_CFLAGS) -c -o gnome_structs.o gnome_structs.c

gnome_stats.o: gnome_stats.c
	gcc -O -Wall -DSWT_VERSION=$(SWT_VERSION) -DLINUX -DGTK -I$(JAVA_HOME)/include $(GNOME_CFLAGS) -c -o gnome_stats.o gnome_stats.c

make_kde: $(KDE_LIB)

$(KDE_LIB): $(KDE_OBJS)
	ld -o $@ $(KDE_OBJS) $(KDE_LIBS)

$(KDE_OBJS): kde.cc
	g++ $(KDE_CFLAGS) -o kde.o kde.cc

make_awt: $(AWT_LIB)

$(AWT_LIB): $(AWT_OBJS)
	ld -o $@ $(AWT_OBJS) $(AWT_LIBS)

make_gtk: $(GTK_LIB)

$(GTK_LIB): $(GTK_OBJS)
	ld -o $@ $(GTK_OBJS) $(GTK_LIBS)

gtk.o: gtk.c
	$(CC) $(CFLAGS) $(GTK_CFLAGS) -c -o gtk.o gtk.c
		
clean:
	rm -f *.so *.o

