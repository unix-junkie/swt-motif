#!/bin/sh
#*******************************************************************************
# Copyright (c) 2000, 2007 IBM Corporation and others.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     IBM Corporation - initial API and implementation
#     Kevin Cornell (Rational Software Corporation)
#     Tom Tromey (Red Hat, Inc.)
#     Sridhar Bidigalu (ICS)
#     Sumit Sarkar (Hewlett-Packard)
#*******************************************************************************

cd `dirname $0`

if [ "${OS}" = "" ]; then
	OS=`uname -s`
fi
if [ "${MODEL}" = "" ]; then
	MODEL=`uname -m`
fi

case $OS in
	"AIX")
		case $MODEL in		
		*) 
			if [ "${JAVA_HOME}" = "" ]; then
				JAVA_HOME=/bluebird/teamswt/swt-builddir/aixj9
			fi
			if [ "${MOTIF_HOME}" = "" ]; then
				MOTIF_HOME=/usr
			fi
			if [ "${CDE_HOME}" = "" ]; then
				CDE_HOME=/usr/dt
			fi
			OUTPUT_DIR=../../../org.eclipse.swt.motif.aix.ppc
			makefile="make_aix.mak"
			echo "Building AIX motif ppc version of SWT"
		esac
		;;
	"Linux")
		case $MODEL in
		*) 
			if [ "${JAVA_HOME}" = "" ]; then
				JAVA_HOME=/bluebird/teamswt/swt-builddir/IBMJava2-141
			fi
			if [ "${MOTIF_HOME}" = "" ]; then
				MOTIF_HOME=/bluebird/teamswt/swt-builddir/motif21
			fi
			if [ "${MOZILLA_SDK}" = "" ]; then
				MOZILLA_SDK=/bluebird/teamswt/swt-builddir/mozilla/1.4/linux_gtk2/mozilla/dist/sdk
				MOZILLA_INCLUDES="-include ${MOZILLA_SDK}/mozilla-config.h -I${MOZILLA_SDK}/../include/xpcom -I${MOZILLA_SDK}/../include/nspr -I${MOZILLA_SDK}/../include/embed_base -I${MOZILLA_SDK}/../include/embedstring -I${MOZILLA_SDK}/../include/string"
				MOZILLA_LIBS="${MOZILLA_SDK}/../lib/libembedstring.a -L${MOZILLA_SDK}/../bin -L${MOZILLA_SDK}/../lib/ -lxpcom -lnspr4 -lplds4 -lplc4"
				XULRUNNER_SDK=/bluebird/teamswt/swt-builddir/geckoSDK/1.8.0.4/gecko-sdk
				XULRUNNER_INCLUDES="-include ${XULRUNNER_SDK}/include/mozilla-config.h -I${XULRUNNER_SDK}/include"
				XULRUNNER_LIBS="-L${XULRUNNER_SDK}/lib -lxpcomglue"
			fi
			OUTPUT_DIR=../../../org.eclipse.swt.motif.linux.x86
			makefile="make_linux.mak"
			echo "Building Linux motif x86 version of SWT"
		esac
		;;
	"SunOS")
		case $MODEL in
		*) 
			if [ "${JAVA_HOME}" = "" ]; then
				JAVA_HOME=/usr/java
			fi
			if [ "${MOTIF_HOME}" = "" ]; then
				MOTIF_HOME=/usr/dt
			fi
			if [ "${CDE_HOME}" = "" ]; then
				CDE_HOME=/usr/dt		
			fi
			OUTPUT_DIR=../../../org.eclipse.swt.motif.solaris.sparc
			PATH=/usr/ccs/bin:/opt/SUNWspro/bin:$PATH
			export PATH
			makefile="make_solaris.mak"
			echo "Building Solaris motif sparc version of SWT"
		esac
		;;
	"HP-UX")
		case $MODEL in
			"ia64")
				if [ "${JAVA_HOME}" = "" ]; then
					JAVA_HOME=/opt/jdk14101
				fi
				if [ "${MOTIF_HOME}" = "" ]; then
					MOTIF_HOME=/usr
				fi
				if [ "${CDE_HOME}" = "" ]; then
					CDE_HOME=/usr/dt
				fi
				OUTPUT_DIR=../../../org.eclipse.swt.motif.hpux.ia64_32
				makefile="make_hpux_ia64_32.mak"
				echo "Building HPUX motif 32 bit ia64 version of SWT"
				;;
			*)
				if [ "${JAVA_HOME}" = "" ]; then
					JAVA_HOME=/opt/jdk14101
				fi
				if [ "${MOTIF_HOME}" = "" ]; then
					MOTIF_HOME=/usr
				fi
				if [ "${CDE_HOME}" = "" ]; then
					CDE_HOME=/usr/dt
				fi
				OUTPUT_DIR=../../../org.eclipse.swt.motif.hpux.PA_RISC
				makefile="make_hpux_PA_RISC.mak"
				echo "Building HPUX motif risc version of SWT"
				;;
		esac
		;;

	*)
		echo "Unknown OS -- build aborted"
		;;
esac

export JAVA_HOME MOTIF_HOME CDE_HOME MOZILLA_SDK MOZILLA_INCLUDES MOZILLA_LIBS XULRUNNER_SDK XULRUNNER_INCLUDES XULRUNNER_LIBS OUTPUT_DIR

make -f $makefile $1 $2 $3 $4
