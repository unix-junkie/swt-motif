/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * Instances of this class represent a selectable user interface object
 * that represents a button in a tool bar.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>PUSH, CHECK, RADIO, SEPARATOR, DROP_DOWN</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles CHECK, PUSH, RADIO, SEPARATOR and DROP_DOWN 
 * may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class ToolItem extends Item {
	ToolBar parent;
	Image hotImage, disabledImage;
	String toolTipText;
	Control control;
	boolean set;
	
	static final int DEFAULT_WIDTH = 24;
	static final int DEFAULT_HEIGHT = 22;
	static final int DEFAULT_SEPARATOR_WIDTH = 8;

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>ToolBar</code>) and a style value
 * describing its behavior and appearance. The item is added
 * to the end of the items maintained by its parent.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 *
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#PUSH
 * @see SWT#CHECK
 * @see SWT#RADIO
 * @see SWT#SEPARATOR
 * @see SWT#DROP_DOWN
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public ToolItem (ToolBar parent, int style) {
	super (parent, checkStyle (style));
	this.parent = parent;
	parent.createItem (this, parent.getItemCount ());
	parent.relayout ();
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>ToolBar</code>), a style value
 * describing its behavior and appearance, and the index
 * at which to place it in the items maintained by its parent.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 *
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 * @param index the index to store the receiver in its parent
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#PUSH
 * @see SWT#CHECK
 * @see SWT#RADIO
 * @see SWT#SEPARATOR
 * @see SWT#DROP_DOWN
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public ToolItem (ToolBar parent, int style, int index) {
	super (parent, checkStyle (style));
	this.parent = parent;
	parent.createItem (this, index);
	parent.relayout ();
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called when the mouse is over the arrow portion of a drop-down tool,
 * the event object detail field contains the value <code>SWT.ARROW</code>.
 * <code>widgetDefaultSelected</code> is not called.
 * </p>
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #removeSelectionListener
 * @see SelectionEvent
 */
public void addSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection,typedListener);
	addListener(SWT.DefaultSelection,typedListener);
}
static int checkStyle (int style) {
	return checkBits (style, SWT.PUSH, SWT.CHECK, SWT.RADIO, SWT.SEPARATOR, SWT.DROP_DOWN, 0);
}
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}
void createHandle (int index) {
	state |= HANDLE;
	int parentHandle = parent.handle;
	if ((style & SWT.SEPARATOR) != 0) {
		int orientation = (parent.style & SWT.HORIZONTAL) != 0 ? OS.XmVERTICAL : OS.XmHORIZONTAL;
		int [] argList = {
			OS.XmNheight, orientation == OS.XmVERTICAL ? DEFAULT_HEIGHT : DEFAULT_SEPARATOR_WIDTH,
			OS.XmNwidth, orientation == OS.XmHORIZONTAL ? DEFAULT_WIDTH : DEFAULT_SEPARATOR_WIDTH,
			OS.XmNancestorSensitive, 1,
			OS.XmNpositionIndex, index,			
			OS.XmNorientation, orientation,
			OS.XmNseparatorType, (parent.style & SWT.FLAT) != 0 ? OS.XmSHADOW_ETCHED_IN : OS.XmSHADOW_ETCHED_OUT,
		};
		handle = OS.XmCreateSeparator (parentHandle, null, argList, argList.length / 2);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		return;
	}
	int [] argList = {
		OS.XmNwidth, DEFAULT_WIDTH,
		OS.XmNheight, DEFAULT_HEIGHT,
		OS.XmNrecomputeSize, 0,
		OS.XmNhighlightThickness, (parent.style & SWT.NO_FOCUS) != 0 ? 0 : 1,
		OS.XmNmarginWidth, 2,
		OS.XmNmarginHeight, 1,
		OS.XmNtraversalOn, (parent.style & SWT.NO_FOCUS) != 0 ? 0 : 1,
		OS.XmNpositionIndex, index,
		OS.XmNshadowType, OS.XmSHADOW_OUT,
		OS.XmNancestorSensitive, 1,
	};
	handle = OS.XmCreateDrawnButton (parentHandle, null, argList, argList.length / 2);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	int pixel = parent.getBackgroundPixel ();
	setBackgroundPixel (pixel);
}

void click (boolean dropDown, XInputEvent xEvent) {
	if ((style & SWT.RADIO) != 0) {
		selectRadio ();
	} else {
		if ((style & SWT.CHECK) != 0) setSelection(!set);			
	}
	Event event = new Event ();
	if ((style & SWT.DROP_DOWN) != 0) {
		if (dropDown) {
			event.detail = SWT.ARROW;
			int [] argList = {OS.XmNx, 0, OS.XmNy, 0, OS.XmNheight, 0};
			OS.XtGetValues (handle, argList, argList.length / 2);
			event.x = (short) argList [1];
			event.y = (short) argList [3] + (short) argList [5];
		}
	}
	if (xEvent != null) setInputState (event, xEvent);
	postEvent (SWT.Selection, event);
}

Point computeSize () {
	if ((style & SWT.SEPARATOR) != 0) {
		int [] argList = {
			OS.XmNwidth, 0,
			OS.XmNheight, 0,
		};
		OS.XtGetValues (handle, argList, argList.length / 2);
		int width = argList [1], height = argList [3];
		return new Point(width, height);
	}
	int [] argList = {
		OS.XmNmarginHeight, 0,
		OS.XmNmarginWidth, 0,
		OS.XmNshadowThickness, 0,
	};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int marginHeight = argList [1], marginWidth = argList [3];
	int shadowThickness = argList [5];
	if ((parent.style & SWT.FLAT) != 0) {
		Display display = getDisplay ();
		shadowThickness = Math.min (2, display.buttonShadowThickness);
	}
	int textWidth = 0, textHeight = 0;
	if (text.length () != 0) {
		GC gc = new GC (parent);
		int flags = SWT.DRAW_DELIMITER | SWT.DRAW_TAB | SWT.DRAW_MNEMONIC;
		Point textExtent = gc.textExtent (text, flags);
		textWidth = textExtent.x;
		textHeight = textExtent.y;
		gc.dispose ();
	}
	int imageWidth = 0, imageHeight = 0;
	if (image != null) {
		Rectangle rect = image.getBounds ();
		imageWidth = rect.width;
		imageHeight = rect.height;
	}
	int width = 0, height = 0;
	if ((parent.style & SWT.RIGHT) != 0) {
		width = imageWidth + textWidth;
		height = Math.max (imageHeight, textHeight);
		if (imageWidth != 0 && textWidth != 0) width += 2;
	} else {
		height = imageHeight + textHeight;
		if (imageHeight != 0 && textHeight != 0) height += 2;
		width = Math.max (imageWidth, textWidth);
	}
	if ((style & SWT.DROP_DOWN) != 0) width += 12;
	
	if (width != 0) {
		width += (marginWidth + shadowThickness) * 2 + 2;
	} else {
		width = DEFAULT_WIDTH;
	}
	if (height != 0) {
		height += (marginHeight + shadowThickness) * 2 + 2;
	} else {
		height = DEFAULT_HEIGHT;
	}
	return new Point (width, height);
}
void createWidget (int index) {
	super.createWidget (index);
	parent.relayout ();
}
public void dispose () {
	if (isDisposed()) return;
	ToolBar parent = this.parent;
	super.dispose ();
	parent.relayout ();
}
/**
 * Returns a rectangle describing the receiver's size and location
 * relative to its parent.
 *
 * @return the receiver's bounding rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Rectangle getBounds () {
	checkWidget();
	int [] argList = {OS.XmNx, 0, OS.XmNy, 0, OS.XmNwidth, 0, OS.XmNheight, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return new Rectangle ((short) argList [1], (short) argList [3], argList [5], argList [7]);
}
/**
 * Returns the control that is used to fill the bounds of
 * the item when the items is a <code>SEPARATOR</code>.
 *
 * @return the control
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Control getControl () {
	checkWidget();
	return control;
}
/**
 * Returns the receiver's disabled image if it has one, or null
 * if it does not.
 * <p>
 * The disabled image is displayed when the receiver is disabled.
 * </p>
 *
 * @return the receiver's disabled image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Image getDisabledImage () {
	checkWidget();
	return disabledImage;
}
/**
 * Returns <code>true</code> if the receiver is enabled, and
 * <code>false</code> otherwise. A disabled control is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #isEnabled
 */
public boolean getEnabled () {
	checkWidget();
	int [] argList = {OS.XmNsensitive, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1] != 0;
}
public Display getDisplay () {
	Composite parent = this.parent;
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay ();
}
/**
 * Returns the receiver's hot image if it has one, or null
 * if it does not.
 * <p>
 * The hot image is displayed when the mouse enters the receiver.
 * </p>
 *
 * @return the receiver's hot image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Image getHotImage () {
	checkWidget();
	return hotImage;
}
/**
 * Returns the receiver's parent, which must be a <code>ToolBar</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public ToolBar getParent () {
	checkWidget();
	return parent;
}
/**
 * Returns <code>true</code> if the receiver is selected,
 * and false otherwise.
 * <p>
 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
 * it is selected when it is checked (which some platforms draw as a
 * pushed in button). If the receiver is of any other type, this method
 * returns false.
 * </p>
 *
 * @return the selection state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getSelection () {
	checkWidget();
	if ((style & (SWT.CHECK | SWT.RADIO)) == 0) return false;
	return set;
}
/**
 * Returns the receiver's tool tip text, or null if it has not been set.
 *
 * @return the receiver's tool tip text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getToolTipText () {
	checkWidget();
	return toolTipText;
}
/**
 * Gets the width of the receiver.
 *
 * @return the width
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getWidth () {
	checkWidget();
	int [] argList = {OS.XmNwidth, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1];
}
boolean hasCursor () {
	int [] unused = new int [1], buffer = new int [1];
	int xDisplay = OS.XtDisplay (handle);
	int xWindow, xParent = OS.XDefaultRootWindow (xDisplay);
	do {
		if (OS.XQueryPointer (
			xDisplay, xParent, unused, buffer,
			unused, unused, unused, unused, unused) == 0) return false;
		if ((xWindow = buffer [0]) != 0) xParent = xWindow;
	} while (xWindow != 0);
	return handle == OS.XtWindowToWidget (xDisplay, xParent);
}
void hookEvents () {
	super.hookEvents ();
	if ((style & SWT.SEPARATOR) != 0) return;
	int windowProc = getDisplay ().windowProc;
	OS.XtAddEventHandler (handle, OS.KeyPressMask, false, windowProc, KEY_PRESS);
	OS.XtAddEventHandler (handle, OS.KeyReleaseMask, false, windowProc, KEY_RELEASE);
	OS.XtAddEventHandler (handle, OS.ButtonPressMask, false, windowProc, BUTTON_PRESS);
	OS.XtAddEventHandler (handle, OS.ButtonReleaseMask, false, windowProc, BUTTON_RELEASE);
	OS.XtAddEventHandler (handle, OS.PointerMotionMask, false, windowProc, POINTER_MOTION);
	OS.XtAddEventHandler (handle, OS.EnterWindowMask, false, windowProc, ENTER_WINDOW);
	OS.XtAddEventHandler (handle, OS.LeaveWindowMask, false, windowProc, LEAVE_WINDOW);
	OS.XtAddCallback (handle, OS.XmNexposeCallback, windowProc, EXPOSURE_CALLBACK);
	OS.XtInsertEventHandler (handle, OS.FocusChangeMask, false, windowProc, FOCUS_CHANGE, OS.XtListTail);
}
int hoverProc (int id) {
	boolean showTip = toolTipText != null;
	parent.hoverProc (id, !showTip);
	if (showTip) {
		Display display = getDisplay ();
		display.showToolTip (handle, toolTipText);
	}
	return 0;
}
/**
 * Returns <code>true</code> if the receiver is enabled and all
 * of the receiver's ancestors are enabled, and <code>false</code>
 * otherwise. A disabled control is typically not selectable from the
 * user interface and draws with an inactive or "grayed" look.
 *
 * @return the receiver's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #getEnabled
 */
public boolean isEnabled () {
	checkWidget();
	return getEnabled () && parent.isEnabled ();
}
void manageChildren () {
	OS.XtManageChild (handle);
}
void redraw () {
	int display = OS.XtDisplay (handle);
	if (display == 0) return;
	int window = OS.XtWindow (handle);
	if (window == 0) return;
	OS.XClearArea (display, window, 0, 0, 0, 0, true);
}
void releaseChild () {
	super.releaseChild ();
	parent.destroyItem (this);
}
void releaseWidget () {
	Display display = getDisplay ();
	display.releaseToolTipHandle (handle);
	super.releaseWidget ();
	parent = null;
	control = null;
	toolTipText = null;
	image = disabledImage = hotImage = null; 
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is selected.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #addSelectionListener
 */
public void removeSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Selection, listener);
	eventTable.unhook(SWT.DefaultSelection,listener);	
}
void selectRadio () {
	int index = 0;
	ToolItem [] items = parent.getItems ();
	while (index < items.length && items [index] != this) index++;
	int i = index - 1;
	while (i >= 0 && items [i].setRadioSelection (false)) --i;
	int j = index + 1;
	while (j < items.length && items [j].setRadioSelection (false)) j++;
	setSelection (true);
}
void setBackgroundPixel(int pixel) {
	int [] argList = {OS.XmNforeground, 0, OS.XmNhighlightColor, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	OS.XmChangeColor (handle, pixel);
	OS.XtSetValues (handle, argList, argList.length / 2);
}
void setBounds (int x, int y, int width, int height) {
	if (control != null) control.setBounds(x, y, width, height);
	/*
	* Feature in Motif.  Motif will not allow a window
	* to have a zero width or zero height.  The fix is
	* to ensure these values are never zero.
	*/
	int newWidth = Math.max (width, 1), newHeight = Math.max (height, 1);
	OS.XtConfigureWidget (handle, x, y, newWidth, newHeight, 0);
}
/**
 * Sets the control that is used to fill the bounds of
 * the item when the items is a <code>SEPARATOR</code>.
 *
 * @param control the new control
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the control has been disposed</li> 
 *    <li>ERROR_INVALID_PARENT - if the control is not in the same widget tree</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setControl (Control control) {
	checkWidget();
	if (control != null) {
		if (control.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
		if (control.parent != parent) error (SWT.ERROR_INVALID_PARENT);
	}
	if ((style & SWT.SEPARATOR) == 0) return;
	this.control = control;
	if (control != null && !control.isDisposed ()) {
		control.setBounds (getBounds ());
		/*
		 * It is possible that the control was created with a 
		 * z-order below that of the current tool item. In this
		 * case, the control is not visible because it is 
		 * obscured by the tool item. The fix is to move the 
		 * control above this tool item in the z-order.  
		 * The code below is similar to the code found in 
		 * setZOrder.
		 */
		int xDisplay = OS.XtDisplay (handle);
		if (xDisplay == 0) return;
		if (!OS.XtIsRealized (handle)) {
			Shell shell = parent.getShell ();
			shell.realizeWidget ();
		}
		int topHandle1 = control.topHandle ();
		int window1 = OS.XtWindow (topHandle1);
		if (window1 == 0) return;
		int topHandle2 = this.topHandle ();
		int window2 = OS.XtWindow (topHandle2);
		if (window2 == 0) return;
		XWindowChanges struct = new XWindowChanges ();
		struct.sibling = window2;
		struct.stack_mode = OS.Above;
		int screen = OS.XDefaultScreen (xDisplay);
		int flags = OS.CWStackMode | OS.CWSibling;
		OS.XReconfigureWMWindow (xDisplay, window1, screen, flags, struct);
	}
}
/**
 * Enables the receiver if the argument is <code>true</code>,
 * and disables it otherwise.
 * <p>
 * A disabled control is typically
 * not selectable from the user interface and draws with an
 * inactive or "grayed" look.
 * </p>
 *
 * @param enabled the new enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setEnabled (boolean enabled) {
	checkWidget();
	int [] argList = {OS.XmNsensitive, enabled ? 1 : 0};
	OS.XtSetValues (handle, argList, argList.length / 2);
}
void setForegroundPixel(int pixel) {
	int [] argList = {OS.XmNforeground, pixel};
	OS.XtSetValues (handle, argList, argList.length / 2);
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return;
	int xWindow = OS.XtWindow (handle);
	if (xWindow == 0) return;
	OS.XClearArea (xDisplay, xWindow, 0, 0, 0, 0, true);
}
/**
 * Sets the receiver's disabled image to the argument, which may be
 * null indicating that no disabled image should be displayed.
 * <p>
 * The disbled image is displayed when the receiver is disabled.
 * </p>
 *
 * @param image the disabled image to display on the receiver (may be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setDisabledImage (Image image) {
	checkWidget();
	if (image != null && image.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	if ((style & SWT.SEPARATOR) != 0) return;
	disabledImage = image;
	if (!getEnabled ()) redraw ();
}
/**
 * Sets the receiver's hot image to the argument, which may be
 * null indicating that no hot image should be displayed.
 * <p>
 * The hot image is displayed when the mouse enters the receiver.
 * </p>
 *
 * @param image the hot image to display on the receiver (may be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setHotImage (Image image) {
	checkWidget();
	if (image != null && image.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	if ((style & SWT.SEPARATOR) != 0) return;
	hotImage = image;
	if ((parent.style & SWT.FLAT) != 0) redraw ();
}
public void setImage (Image image) {
	checkWidget();
	if (image != null && image.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	if ((style & SWT.SEPARATOR) != 0) return;
	super.setImage (image);
	Point size = computeSize ();
	setSize (size.x, size.y, true);
	redraw ();
}
boolean setRadioSelection (boolean value) {
	if ((style & SWT.RADIO) == 0) return false;
	if (getSelection () != value) {
		setSelection (value);
		postEvent (SWT.Selection);
	}
	return true;
}
/**
 * Sets the selection state of the receiver.
 * <p>
 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
 * it is selected when it is checked (which some platforms draw as a
 * pushed in button).
 * </p>
 *
 * @param selected the new selection state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (boolean selected) {
	checkWidget();
	if ((style & (SWT.CHECK | SWT.RADIO)) == 0) return;
	if (selected == set) return;
	set = selected;
	setDrawPressed (set);
}

void setSize (int width, int height, boolean layout) {
	int [] argList = {OS.XmNwidth, 0, OS.XmNheight, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	if (argList [1] != width || argList [3] != height) {
		OS.XtResizeWidget (handle, width, height, 0);
		if (layout) parent.relayout ();
	}
}
/**
 * Sets the receiver's text. The string may include
 * the mnemonic character.
 * </p>
 * <p>
 * Mnemonics are indicated by an '&amp' that causes the next
 * character to be the mnemonic.  When the user presses a
 * key sequence that matches the mnemonic, a selection
 * event occurs. On most platforms, the mnemonic appears
 * underlined but may be emphasised in a platform specific
 * manner.  The mnemonic indicator character '&amp' can be
 * escaped by doubling it in the string, causing a single
 *'&amp' to be displayed.
 * </p>
 * 
 * @param string the new text
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the text is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setText (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.SEPARATOR) != 0) return;
	super.setText (string);
	Point size = computeSize ();
	setSize (size.x, size.y, true);
	redraw ();
}

/**
 * Sets the receiver's tool tip text to the argument, which
 * may be null indicating that no tool tip text should be shown.
 *
 * @param string the new tool tip text (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setToolTipText (String string) {
	checkWidget();
	toolTipText = string;
}
/**
 * Sets the width of the receiver.
 *
 * @param width the new width
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setWidth (int width) {
	checkWidget();
	if ((style & SWT.SEPARATOR) == 0) return;
	if (width < 0) return;
	int [] argList = {OS.XmNheight, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	setSize (width, argList [1], true);
	if (control != null && !control.isDisposed ()) {
		control.setBounds (getBounds ());
	}
}
void setDrawPressed (boolean value) {
	int shadowType = value ? OS.XmSHADOW_IN : OS.XmSHADOW_OUT;
	int [] argList = {OS.XmNshadowType, shadowType};
	OS.XtSetValues(handle, argList, argList.length / 2);
}
boolean translateAccelerator (int key, int keysym, XKeyEvent xEvent) {
	return parent.translateAccelerator (key, keysym, xEvent);
}
boolean translateMnemonic (int key, XKeyEvent xEvent) {
	return parent.translateMnemonic (key, xEvent);
}
boolean translateTraversal (int key, XKeyEvent xEvent) {
	return parent.translateTraversal (key, xEvent);
}
void propagateWidget (boolean enabled) {
	propagateHandle (enabled, handle);
}
int XButtonPress (int w, int client_data, int call_data, int continue_to_dispatch) {
	Display display = getDisplay ();
//	Shell shell = parent.getShell ();
	display.hideToolTip ();
	XButtonEvent xEvent = new XButtonEvent ();
	OS.memmove (xEvent, call_data, XButtonEvent.sizeof);
	if (xEvent.button == 1) {
		if (!set && (style & SWT.RADIO) == 0) {
			setDrawPressed (!set);
		}
	}
	
	/*
	* Forward the mouse event to the parent.
	* This is necessary so that mouse listeners
	* in the parent will be called, despite the
	* fact that the event did not really occur
	* in X in the parent.  This is done to be
	* compatible with Windows.
	*/
	int [] argList = {OS.XmNx, 0, OS.XmNy, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	xEvent.window = OS.XtWindow (parent.handle);
	xEvent.x += argList [1];  xEvent.y += argList [3];
	OS.memmove (call_data, xEvent, XButtonEvent.sizeof);
	parent.XButtonPress (w, client_data, call_data, continue_to_dispatch);

	/*
	* It is possible that the shell may be
	* disposed at this point.  If this happens
	* don't send the activate and deactivate
	* events.
	*/	
//	if (!shell.isDisposed()) {
//		shell.setActiveControl (parent);
//	}
	return 0;
}
int XButtonRelease (int w, int client_data, int call_data, int continue_to_dispatch) {
	Display display = getDisplay ();
	display.hideToolTip(); 
	XButtonEvent xEvent = new XButtonEvent ();
	OS.memmove (xEvent, call_data, XButtonEvent.sizeof);
	if (xEvent.button == 1) {
		int [] argList = {OS.XmNwidth, 0, OS.XmNheight, 0};
		OS.XtGetValues (handle, argList, argList.length / 2);
		int width = argList [1], height = argList [3];
		if (0 <= xEvent.x && xEvent.x < width && 0 <= xEvent.y && xEvent.y < height) {
			click (xEvent.x > width - 12, xEvent);
		}
		setDrawPressed(set);
	}

	/*
	* Forward the mouse event to the parent.
	* This is necessary so that mouse listeners
	* in the parent will be called, despite the
	* fact that the event did not really occur
	* in X in the parent.  This is done to be
	* compatible with Windows.
	*/
	int [] argList = {OS.XmNx, 0, OS.XmNy, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	xEvent.window = OS.XtWindow (parent.handle);
	xEvent.x += argList [1];  xEvent.y += argList [3];
	OS.memmove (call_data, xEvent, XButtonEvent.sizeof);
	parent.XButtonRelease (w, client_data, call_data, continue_to_dispatch);

	return 0;
}
int XEnterWindow (int w, int client_data, int call_data, int continue_to_dispatch) {
	XCrossingEvent xEvent = new XCrossingEvent ();
	OS.memmove (xEvent, call_data, XCrossingEvent.sizeof);
	if ((xEvent.state & OS.Button1Mask) != 0) {
		setDrawPressed (!set);
	} else {
		if ((parent.style & SWT.FLAT) != 0) redraw ();
	}
	return 0;
}
int XFocusChange (int w, int client_data, int call_data, int continue_to_dispatch) {
	/*
	* Forward the focus event to the parent.
	* This is necessary so that focus listeners
	* in the parent will be called, despite the
	* fact that the event did not really occur
	* in X in the parent.  This is done to be
	* compatible with Windows.
	*/
	XFocusChangeEvent xEvent = new XFocusChangeEvent ();
	OS.memmove (xEvent, call_data, XFocusChangeEvent.sizeof);
	xEvent.window = OS.XtWindow (parent.handle);
//	OS.memmove (call_data, xEvent, XFocusChangeEvent.sizeof);
	if (OS.IsDBLocale) {
		parent.XFocusChange (w, client_data, call_data, continue_to_dispatch);
	} 
	return 0;
}
int XKeyPress (int w, int client_data, int call_data, int continue_to_dispatch) {
	XKeyEvent xEvent = new XKeyEvent ();
	OS.memmove (xEvent, call_data, XKeyEvent.sizeof);
	int [] keysym = new int [1];
	OS.XLookupString (xEvent, null, 0, keysym, null);
	keysym [0] &= 0xFFFF;
	switch (keysym [0]) {
		case OS.XK_space:
			click (false, xEvent);
			break;
		case OS.XK_KP_Enter:
		case OS.XK_Return:
			click (true, xEvent);
			break;
	}
	/*
	* Forward the key event to the parent.
	* This is necessary so that key listeners
	* in the parent will be called, despite the
	* fact that the event did not really occur
	* in X in the parent.  This is done to be
	* compatible with Windows.
	*/
	xEvent.window = OS.XtWindow (parent.handle);
//	OS.memmove (callData, xEvent, XKeyEvent.sizeof);
	parent.XKeyPress (w, client_data, call_data, continue_to_dispatch);
	return 0;
}
int XKeyRelease (int w, int client_data, int call_data, int continue_to_dispatch) {
	XKeyEvent xEvent = new XKeyEvent ();
	OS.memmove (xEvent, call_data, XKeyEvent.sizeof);

	/*
	* Forward the key event to the parent.
	* This is necessary so that key listeners
	* in the parent will be called, despite the
	* fact that the event did not really occur
	* in X in the parent.  This is done to be
	* compatible with Windows.
	*/
	xEvent.window = OS.XtWindow (parent.handle);
//	OS.memmove (callData, xEvent, XKeyEvent.sizeof);
	parent.XKeyRelease (w, client_data, call_data, continue_to_dispatch);
	return 0;
}
int XLeaveWindow (int w, int client_data, int call_data, int continue_to_dispatch) {
	Display display = getDisplay ();
	display.removeMouseHoverTimeOut ();
	display.hideToolTip ();
	XCrossingEvent xEvent = new XCrossingEvent ();
	OS.memmove (xEvent, call_data, XCrossingEvent.sizeof);
	if ((xEvent.state & OS.Button1Mask) != 0) {
		setDrawPressed (set);
	} else {
		if ((parent.style & SWT.FLAT) != 0) redraw ();
	}
	return 0;
}
int XmNexposureCallback (int w, int client_data, int call_data) {
	if ((style & SWT.SEPARATOR) != 0) return 0;
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return 0;
	int xWindow = OS.XtWindow (handle);
	if (xWindow == 0) return 0;
	int [] argList = {
		OS.XmNcolormap, 0,
		OS.XmNwidth, 0,
		OS.XmNheight, 0,
	};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int width = argList [3], height = argList [5];
	
	Image currentImage = image;
	boolean enabled = getEnabled();

	if ((parent.style & SWT.FLAT) != 0) {
		Display display = getDisplay ();
		boolean hasCursor = hasCursor ();
		
		/* Set the shadow thickness */
		int thickness = 0;
		if (set || (hasCursor && enabled)) {
			thickness = Math.min (2, display.buttonShadowThickness);
		}
		argList = new int [] {OS.XmNshadowThickness, thickness};
		OS.XtSetValues (handle, argList, argList.length / 2);
		
		/* Determine if hot image should be used */
		if (enabled && hasCursor && hotImage != null) {
			currentImage = hotImage;
		}
	}

	ToolDrawable wrapper = new ToolDrawable ();
	wrapper.device = getDisplay ();
	wrapper.display = xDisplay;
	wrapper.drawable = xWindow;
	wrapper.font = parent.font;
	wrapper.colormap = argList [1];	
	GC gc = new GC (wrapper);
	
	XmAnyCallbackStruct cb = new XmAnyCallbackStruct ();
	OS.memmove (cb, call_data, XmAnyCallbackStruct.sizeof);
	if (cb.event != 0) {
		XExposeEvent xEvent = new XExposeEvent ();
		OS.memmove (xEvent, cb.event, XExposeEvent.sizeof);
		Rectangle rect = new Rectangle (xEvent.x, xEvent.y, xEvent.width, xEvent.height);
		gc.setClipping (rect);
	}
	
	if (!enabled) {
		Display display = getDisplay ();
		currentImage = disabledImage;
		if (currentImage == null && image != null) {
			currentImage = new Image (display, image, SWT.IMAGE_DISABLE);
		}
		Color disabledColor = display.getSystemColor (SWT.COLOR_WIDGET_NORMAL_SHADOW);
		gc.setForeground (disabledColor);
	} else {
		gc.setForeground (parent.getForeground ());
	}
	gc.setBackground (parent.getBackground ());
	
	int textX = 0, textY = 0, textWidth = 0, textHeight = 0;
	if (text.length () != 0) {
		int flags = SWT.DRAW_DELIMITER | SWT.DRAW_TAB | SWT.DRAW_MNEMONIC;
		Point textExtent = gc.textExtent (text, flags);
		textWidth = textExtent.x;
		textHeight = textExtent.y;
	}	
	int imageX = 0, imageY = 0, imageWidth = 0, imageHeight = 0;
	if (currentImage != null) {
		Rectangle imageBounds = currentImage.getBounds ();
		imageWidth = imageBounds.width;
		imageHeight = imageBounds.height;
	}
	
	int spacing = 0;
	if (textWidth != 0 && imageWidth != 0) spacing = 2;
	if ((parent.style & SWT.RIGHT) != 0) {
		imageX = (width - imageWidth - textWidth - spacing) / 2;
		imageY = (height - imageHeight) / 2;
		textX = spacing + imageX + imageWidth;
		textY = (height - textHeight) / 2;
	} else {		
		imageX = (width - imageWidth) / 2;
		imageY = (height - imageHeight - textHeight - spacing) / 2;
		textX = (width - textWidth) / 2;
		textY = spacing + imageY + imageHeight;
	}
	
	if ((style & SWT.DROP_DOWN) != 0) {
		textX -= 6;  imageX -=6;
	}
	if (textWidth > 0) {
		int flags = SWT.DRAW_DELIMITER | SWT.DRAW_TAB | SWT.DRAW_MNEMONIC;
		gc.drawText(text, textX, textY, flags);
	}
	if (imageWidth > 0) gc.drawImage(currentImage, imageX, imageY);
	if ((style & SWT.DROP_DOWN) != 0) {
		int startX = width - 12, startY = (height - 2) / 2;
		int [] arrow = {startX, startY, startX + 3, startY + 3, startX + 6, startY};
		gc.setBackground (parent.getForeground ());
		gc.fillPolygon (arrow);
		gc.drawPolygon (arrow);
	}
	gc.dispose ();
	
	if (!enabled && disabledImage == null) {
		if (currentImage != null) currentImage.dispose ();
	}
	return 0;
}
int XPointerMotion (int w, int client_data, int call_data, int continue_to_dispatch) {
	Display display = getDisplay ();
	display.addMouseHoverTimeOut (handle);

	/*
	* Forward the mouse event to the parent.
	* This is necessary so that mouse listeners
	* in the parent will be called, despite the
	* fact that the event did not really occur
	* in X in the parent.  This is done to be
	* compatible with Windows.
	*/
	XButtonEvent xEvent = new XButtonEvent ();
	OS.memmove (xEvent, call_data, XButtonEvent.sizeof);
	int [] argList = {OS.XmNx, 0, OS.XmNy, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	xEvent.window = OS.XtWindow (parent.handle);
	xEvent.x += argList [1];  xEvent.y += argList [3];
	/*
	* This code is intentionally commented.
	* Currently, the implementation of the
	* mouse move code in the parent interferes
	* with tool tips for tool items.
	*/
//	OS.memmove (callData, xEvent, XButtonEvent.sizeof);
//	parent.XPointerMotion (w, client_data, call_data, continue_to_dispatch);
	parent.sendMouseEvent (SWT.MouseMove, 0, xEvent);

	return 0;
}
}
