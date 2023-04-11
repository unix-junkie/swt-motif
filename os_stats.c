/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

#include "swt.h"
#include "os_stats.h"

#ifdef NATIVE_STATS

int OS_nativeFunctionCount = 467;
int OS_nativeFunctionCallCount[467];
char * OS_nativeFunctionNames[] = {
	"CODESET",
	"FD_1ISSET",
	"FD_1SET",
	"FD_1ZERO",
	"LC_1CTYPE",
	"MB_1CUR_1MAX",
	"MonitorEnter",
	"MonitorExit",
	"XRenderPictureAttributes_1sizeof",
	"_1Call",
	"_1ConnectionNumber",
	"_1XAllocColor",
	"_1XBell",
	"_1XBlackPixel",
	"_1XChangeActivePointerGrab",
	"_1XChangeProperty",
	"_1XChangeWindowAttributes",
	"_1XCheckIfEvent",
	"_1XCheckMaskEvent",
	"_1XCheckWindowEvent",
	"_1XClearArea",
	"_1XClipBox",
	"_1XCloseDisplay",
	"_1XCopyArea",
	"_1XCopyPlane",
	"_1XCreateBitmapFromData",
	"_1XCreateColormap",
	"_1XCreateFontCursor",
	"_1XCreateGC",
	"_1XCreateImage",
	"_1XCreatePixmap",
	"_1XCreatePixmapCursor",
	"_1XCreateRegion",
	"_1XCreateWindow",
	"_1XDefaultColormap",
	"_1XDefaultColormapOfScreen",
	"_1XDefaultDepthOfScreen",
	"_1XDefaultGCOfScreen",
	"_1XDefaultRootWindow",
	"_1XDefaultScreen",
	"_1XDefaultScreenOfDisplay",
	"_1XDefaultVisual",
	"_1XDefineCursor",
	"_1XDestroyImage",
	"_1XDestroyRegion",
	"_1XDestroyWindow",
	"_1XDisplayHeight",
	"_1XDisplayHeightMM",
	"_1XDisplayWidth",
	"_1XDisplayWidthMM",
	"_1XDrawArc",
	"_1XDrawLine",
	"_1XDrawLines",
	"_1XDrawPoint",
	"_1XDrawRectangle",
	"_1XEmptyRegion",
	"_1XEventsQueued",
	"_1XFillArc",
	"_1XFillPolygon",
	"_1XFillRectangle",
	"_1XFilterEvent",
	"_1XFlush",
	"_1XFontsOfFontSet",
	"_1XFree",
	"_1XFreeColormap",
	"_1XFreeColors",
	"_1XFreeCursor",
	"_1XFreeFont",
	"_1XFreeFontNames",
	"_1XFreeFontPath",
	"_1XFreeGC",
	"_1XFreeModifiermap",
	"_1XFreePixmap",
	"_1XFreeStringList",
	"_1XGetFontPath",
	"_1XGetGCValues",
	"_1XGetGeometry",
	"_1XGetIconSizes",
	"_1XGetImage",
	"_1XGetInputFocus",
	"_1XGetModifierMapping",
	"_1XGetWindowAttributes",
	"_1XGetWindowProperty",
	"_1XGrabKeyboard",
	"_1XGrabPointer",
	"_1XInitThreads",
	"_1XInternAtom",
	"_1XIntersectRegion",
	"_1XKeysymToKeycode",
	"_1XKeysymToString",
	"_1XListFonts",
	"_1XListProperties",
	"_1XLocaleOfFontSet",
	"_1XLookupString",
	"_1XLowerWindow",
	"_1XMapWindow",
	"_1XMoveResizeWindow",
	"_1XOffsetRegion",
	"_1XOpenDisplay",
	"_1XPointInRegion",
	"_1XPolygonRegion",
	"_1XPutImage",
	"_1XQueryBestCursor",
	"_1XQueryColor",
	"_1XQueryPointer",
	"_1XQueryTree",
	"_1XRaiseWindow",
	"_1XReconfigureWMWindow",
	"_1XRectInRegion",
	"_1XRenderComposite",
	"_1XRenderCreatePicture",
	"_1XRenderFindStandardFormat",
	"_1XRenderFindVisualFormat",
	"_1XRenderFreePicture",
	"_1XRenderQueryExtension",
	"_1XRenderQueryVersion",
	"_1XRenderSetPictureClipRectangles",
	"_1XRenderSetPictureClipRegion",
	"_1XRenderSetPictureTransform",
	"_1XReparentWindow",
	"_1XResizeWindow",
	"_1XRootWindowOfScreen",
	"_1XSelectInput",
	"_1XSendEvent",
	"_1XSetBackground",
	"_1XSetClipMask",
	"_1XSetClipRectangles",
	"_1XSetDashes",
	"_1XSetErrorHandler",
	"_1XSetFillRule",
	"_1XSetFillStyle",
	"_1XSetFontPath",
	"_1XSetForeground",
	"_1XSetFunction",
	"_1XSetGraphicsExposures",
	"_1XSetIOErrorHandler",
	"_1XSetInputFocus",
	"_1XSetLineAttributes",
	"_1XSetRegion",
	"_1XSetStipple",
	"_1XSetSubwindowMode",
	"_1XSetTSOrigin",
	"_1XSetTile",
	"_1XSetTransientForHint",
	"_1XSetWMNormalHints",
	"_1XSetWindowBackgroundPixmap",
	"_1XShapeCombineMask",
	"_1XShapeCombineRegion",
	"_1XSubtractRegion",
	"_1XSync",
	"_1XSynchronize",
	"_1XTestFakeButtonEvent",
	"_1XTestFakeKeyEvent",
	"_1XTestFakeMotionEvent",
	"_1XTranslateCoordinates",
	"_1XUndefineCursor",
	"_1XUngrabKeyboard",
	"_1XUngrabPointer",
	"_1XUnionRectWithRegion",
	"_1XUnionRegion",
	"_1XUnmapWindow",
	"_1XWarpPointer",
	"_1XWhitePixel",
	"_1XWithdrawWindow",
	"_1XineramaIsActive",
	"_1XineramaQueryScreens",
	"_1XmAddWMProtocolCallback",
	"_1XmChangeColor",
	"_1XmClipboardCopy",
	"_1XmClipboardEndCopy",
	"_1XmClipboardEndRetrieve",
	"_1XmClipboardInquireCount",
	"_1XmClipboardInquireFormat",
	"_1XmClipboardInquireLength",
	"_1XmClipboardRetrieve",
	"_1XmClipboardStartCopy",
	"_1XmClipboardStartRetrieve",
	"_1XmComboBoxAddItem",
	"_1XmComboBoxDeletePos",
	"_1XmComboBoxSelectItem",
	"_1XmCreateArrowButton",
	"_1XmCreateCascadeButtonGadget",
	"_1XmCreateComboBox",
	"_1XmCreateDialogShell",
	"_1XmCreateDrawingArea",
	"_1XmCreateDrawnButton",
	"_1XmCreateErrorDialog",
	"_1XmCreateFileSelectionDialog",
	"_1XmCreateForm",
	"_1XmCreateFrame",
	"_1XmCreateInformationDialog",
	"_1XmCreateLabel",
	"_1XmCreateList",
	"_1XmCreateMainWindow",
	"_1XmCreateMenuBar",
	"_1XmCreateMessageDialog",
	"_1XmCreatePopupMenu",
	"_1XmCreatePulldownMenu",
	"_1XmCreatePushButton",
	"_1XmCreatePushButtonGadget",
	"_1XmCreateQuestionDialog",
	"_1XmCreateScale",
	"_1XmCreateScrollBar",
	"_1XmCreateScrolledList",
	"_1XmCreateScrolledText",
	"_1XmCreateSeparator",
	"_1XmCreateSeparatorGadget",
	"_1XmCreateSimpleSpinBox",
	"_1XmCreateTextField",
	"_1XmCreateToggleButton",
	"_1XmCreateToggleButtonGadget",
	"_1XmCreateWarningDialog",
	"_1XmCreateWorkingDialog",
	"_1XmDestroyPixmap",
	"_1XmDragCancel",
	"_1XmDragStart",
	"_1XmDropSiteRegister",
	"_1XmDropSiteUnregister",
	"_1XmDropSiteUpdate",
	"_1XmDropTransferAdd",
	"_1XmDropTransferStart",
	"_1XmFileSelectionBoxGetChild",
	"_1XmFontListAppendEntry",
	"_1XmFontListCopy",
	"_1XmFontListEntryFree",
	"_1XmFontListEntryGetFont",
	"_1XmFontListEntryLoad",
	"_1XmFontListFree",
	"_1XmFontListFreeFontContext",
	"_1XmFontListInitFontContext",
	"_1XmFontListNextEntry",
	"_1XmGetAtomName",
	"_1XmGetDragContext",
	"_1XmGetFocusWidget",
	"_1XmGetPixmap",
	"_1XmGetPixmapByDepth",
	"_1XmGetXmDisplay",
	"_1XmImMbLookupString",
	"_1XmImRegister",
	"_1XmImSetFocusValues",
	"_1XmImSetValues",
	"_1XmImUnregister",
	"_1XmImUnsetFocus",
	"_1XmInternAtom",
	"_1XmListAddItemUnselected",
	"_1XmListDeleteAllItems",
	"_1XmListDeleteItemsPos",
	"_1XmListDeletePos",
	"_1XmListDeletePositions",
	"_1XmListDeselectAllItems",
	"_1XmListDeselectPos",
	"_1XmListGetKbdItemPos",
	"_1XmListGetSelectedPos",
	"_1XmListItemPos",
	"_1XmListPosSelected",
	"_1XmListReplaceItemsPosUnselected",
	"_1XmListSelectPos",
	"_1XmListSetKbdItemPos",
	"_1XmListSetPos",
	"_1XmListUpdateSelectedList",
	"_1XmMainWindowSetAreas",
	"_1XmMessageBoxGetChild",
	"_1XmParseMappingCreate",
	"_1XmParseMappingFree",
	"_1XmProcessTraversal",
	"_1XmRenderTableAddRenditions",
	"_1XmRenderTableFree",
	"_1XmRenditionCreate",
	"_1XmRenditionFree",
	"_1XmStringBaseline",
	"_1XmStringCompare",
	"_1XmStringComponentCreate",
	"_1XmStringConcat",
	"_1XmStringCreate",
	"_1XmStringCreateLocalized",
	"_1XmStringDraw",
	"_1XmStringDrawImage",
	"_1XmStringDrawUnderline",
	"_1XmStringEmpty",
	"_1XmStringExtent",
	"_1XmStringFree",
	"_1XmStringGenerate",
	"_1XmStringHeight",
	"_1XmStringParseText",
	"_1XmStringUnparse",
	"_1XmStringWidth",
	"_1XmTabCreate",
	"_1XmTabFree",
	"_1XmTabListFree",
	"_1XmTabListInsertTabs",
	"_1XmTextClearSelection",
	"_1XmTextCopy",
	"_1XmTextCut",
	"_1XmTextDisableRedisplay",
	"_1XmTextEnableRedisplay",
	"_1XmTextFieldPaste",
	"_1XmTextGetInsertionPosition",
	"_1XmTextGetLastPosition",
	"_1XmTextGetMaxLength",
	"_1XmTextGetSelection",
	"_1XmTextGetSelectionPosition",
	"_1XmTextGetString",
	"_1XmTextGetSubstring",
	"_1XmTextGetSubstringWcs",
	"_1XmTextInsert",
	"_1XmTextPaste",
	"_1XmTextPosToXY",
	"_1XmTextReplace",
	"_1XmTextScroll",
	"_1XmTextSetEditable",
	"_1XmTextSetHighlight",
	"_1XmTextSetInsertionPosition",
	"_1XmTextSetMaxLength",
	"_1XmTextSetSelection",
	"_1XmTextSetString",
	"_1XmTextShowPosition",
	"_1XmTextXYToPos",
	"_1XmUpdateDisplay",
	"_1XmWidgetGetDisplayRect",
	"_1XmbTextListToTextProperty",
	"_1XmbTextPropertyToTextList",
	"_1XpCancelJob",
	"_1XpCreateContext",
	"_1XpDestroyContext",
	"_1XpEndJob",
	"_1XpEndPage",
	"_1XpFreePrinterList",
	"_1XpGetOneAttribute",
	"_1XpGetPageDimensions",
	"_1XpGetPrinterList",
	"_1XpGetScreenOfContext",
	"_1XpSetAttributes",
	"_1XpSetContext",
	"_1XpStartJob",
	"_1XpStartPage",
	"_1XtAddCallback",
	"_1XtAddEventHandler",
	"_1XtAddExposureToRegion",
	"_1XtAppAddInput",
	"_1XtAppAddTimeOut",
	"_1XtAppCreateShell",
	"_1XtAppGetSelectionTimeout",
	"_1XtAppNextEvent",
	"_1XtAppPeekEvent",
	"_1XtAppPending",
	"_1XtAppProcessEvent",
	"_1XtAppSetErrorHandler",
	"_1XtAppSetFallbackResources",
	"_1XtAppSetSelectionTimeout",
	"_1XtAppSetWarningHandler",
	"_1XtBuildEventMask",
	"_1XtCallActionProc",
	"_1XtClass",
	"_1XtConfigureWidget",
	"_1XtCreateApplicationContext",
	"_1XtCreatePopupShell",
	"_1XtDestroyApplicationContext",
	"_1XtDestroyWidget",
	"_1XtDisownSelection",
	"_1XtDispatchEvent",
	"_1XtDisplay",
	"_1XtDisplayToApplicationContext",
	"_1XtFree",
	"_1XtGetDisplays",
	"_1XtGetMultiClickTime",
	"_1XtGetSelectionValue",
	"_1XtGetValues",
	"_1XtInsertEventHandler",
	"_1XtIsManaged",
	"_1XtIsRealized",
	"_1XtIsSubclass",
	"_1XtIsTopLevelShell",
	"_1XtLastTimestampProcessed",
	"_1XtMalloc",
	"_1XtManageChild",
	"_1XtMapWidget",
	"_1XtMoveWidget",
	"_1XtNameToWidget",
	"_1XtOpenDisplay",
	"_1XtOverrideTranslations",
	"_1XtOwnSelection",
	"_1XtParent",
	"_1XtParseTranslationTable",
	"_1XtPopdown",
	"_1XtPopup",
	"_1XtQueryGeometry",
	"_1XtRealizeWidget",
	"_1XtRegisterDrawable",
	"_1XtRemoveEventHandler",
	"_1XtRemoveInput",
	"_1XtRemoveTimeOut",
	"_1XtResizeWidget",
	"_1XtResizeWindow",
	"_1XtSetLanguageProc",
	"_1XtSetMappedWhenManaged",
	"_1XtSetValues",
	"_1XtToolkitInitialize",
	"_1XtToolkitThreadInitialize",
	"_1XtTranslateCoords",
	"_1XtUnmanageChild",
	"_1XtUnmapWidget",
	"_1XtUnregisterDrawable",
	"_1XtWindow",
	"_1XtWindowToWidget",
	"_1_1XmSetMenuTraversal",
	"_1_1XtDefaultAppContext",
	"_1access",
	"_1applicationShellWidgetClass",
	"_1dlclose",
	"_1dlopen",
	"_1dlsym",
	"_1overrideShellWidgetClass",
	"_1shellWidgetClass",
	"_1topLevelShellWidgetClass",
	"_1transientShellWidgetClass",
	"_1xmMenuShellWidgetClass",
	"close",
	"fd_1set_1sizeof",
	"iconv",
	"iconv_1close",
	"iconv_1open",
	"localeconv_1decimal_1point",
	"memmove__ILorg_eclipse_swt_internal_motif_XButtonEvent_2I",
	"memmove__ILorg_eclipse_swt_internal_motif_XClientMessageEvent_2I",
	"memmove__ILorg_eclipse_swt_internal_motif_XConfigureEvent_2I",
	"memmove__ILorg_eclipse_swt_internal_motif_XExposeEvent_2I",
	"memmove__ILorg_eclipse_swt_internal_motif_XImage_2I",
	"memmove__ILorg_eclipse_swt_internal_motif_XKeyEvent_2I",
	"memmove__ILorg_eclipse_swt_internal_motif_XmDragProcCallbackStruct_2I",
	"memmove__ILorg_eclipse_swt_internal_motif_XmSpinBoxCallbackStruct_2I",
	"memmove__ILorg_eclipse_swt_internal_motif_XmTextBlockRec_2I",
	"memmove__ILorg_eclipse_swt_internal_motif_XmTextVerifyCallbackStruct_2I",
	"memmove__Lorg_eclipse_swt_internal_motif_Visual_2II",
	"memmove__Lorg_eclipse_swt_internal_motif_XAnyEvent_2II",
	"memmove__Lorg_eclipse_swt_internal_motif_XButtonEvent_2II",
	"memmove__Lorg_eclipse_swt_internal_motif_XCharStruct_2II",
	"memmove__Lorg_eclipse_swt_internal_motif_XClientMessageEvent_2II",
	"memmove__Lorg_eclipse_swt_internal_motif_XConfigureEvent_2II",
	"memmove__Lorg_eclipse_swt_internal_motif_XCreateWindowEvent_2II",
	"memmove__Lorg_eclipse_swt_internal_motif_XCrossingEvent_2II",
	"memmove__Lorg_eclipse_swt_internal_motif_XDestroyWindowEvent_2II",
	"memmove__Lorg_eclipse_swt_internal_motif_XEvent_2II",
	"memmove__Lorg_eclipse_swt_internal_motif_XExposeEvent_2II",
	"memmove__Lorg_eclipse_swt_internal_motif_XFocusChangeEvent_2II",
	"memmove__Lorg_eclipse_swt_internal_motif_XFontStruct_2II",
	"memmove__Lorg_eclipse_swt_internal_motif_XIconSize_2II",
	"memmove__Lorg_eclipse_swt_internal_motif_XImage_2II",
	"memmove__Lorg_eclipse_swt_internal_motif_XKeyEvent_2II",
	"memmove__Lorg_eclipse_swt_internal_motif_XModifierKeymap_2II",
	"memmove__Lorg_eclipse_swt_internal_motif_XMotionEvent_2II",
	"memmove__Lorg_eclipse_swt_internal_motif_XPropertyEvent_2II",
	"memmove__Lorg_eclipse_swt_internal_motif_XReparentEvent_2II",
	"memmove__Lorg_eclipse_swt_internal_motif_XineramaScreenInfo_2II",
	"memmove__Lorg_eclipse_swt_internal_motif_XmAnyCallbackStruct_2II",
	"memmove__Lorg_eclipse_swt_internal_motif_XmDragProcCallbackStruct_2II",
	"memmove__Lorg_eclipse_swt_internal_motif_XmDropFinishCallbackStruct_2II",
	"memmove__Lorg_eclipse_swt_internal_motif_XmDropProcCallbackStruct_2II",
	"memmove__Lorg_eclipse_swt_internal_motif_XmSpinBoxCallbackStruct_2II",
	"memmove__Lorg_eclipse_swt_internal_motif_XmTextBlockRec_2II",
	"memmove__Lorg_eclipse_swt_internal_motif_XmTextVerifyCallbackStruct_2II",
	"nl_1langinfo",
	"pipe",
	"read",
	"select",
	"setResourceMem",
	"setlocale",
	"write",
};

#define STATS_NATIVE(func) Java_org_eclipse_swt_tools_internal_NativeStats_##func

JNIEXPORT jint JNICALL STATS_NATIVE(OS_1GetFunctionCount)
	(JNIEnv *env, jclass that)
{
	return OS_nativeFunctionCount;
}

JNIEXPORT jstring JNICALL STATS_NATIVE(OS_1GetFunctionName)
	(JNIEnv *env, jclass that, jint index)
{
	return (*env)->NewStringUTF(env, OS_nativeFunctionNames[index]);
}

JNIEXPORT jint JNICALL STATS_NATIVE(OS_1GetFunctionCallCount)
	(JNIEnv *env, jclass that, jint index)
{
	return OS_nativeFunctionCallCount[index];
}

#endif
