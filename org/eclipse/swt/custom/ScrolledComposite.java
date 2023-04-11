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
package org.eclipse.swt.custom;


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * A ScrolledComposite provides scrollbars and will scroll its content when the user
 * uses the scrollbars.
 *
 *
 * <p>There are two ways to use the ScrolledComposite:
 * 
 * <p>
 * 1) Set the size of the control that is being scrolled and the ScrolledComposite 
 * will show scrollbars when the contained control can not be fully seen.
 * 
 * 2) The second way imitates the way a browser would work.  Set the minimum size of
 * the control and the ScrolledComposite will show scroll bars if the visible area is 
 * less than the minimum size of the control and it will expand the size of the control 
 * if the visible area is greater than the minimum size.  This requires invoking 
 * both setMinWidth(), setMinHeight() and setExpandHorizontal(), setExpandVertical().
 * 
 * <code><pre>
 * public static void main (String [] args) {
 *      Display display = new Display ();
 *      Color red = display.getSystemColor(SWT.COLOR_RED);
 *      Color blue = display.getSystemColor(SWT.COLOR_BLUE);
 *      Shell shell = new Shell (display);
 *      shell.setLayout(new FillLayout());
 * 	
 *      // set the size of the scrolled content - method 1
 *      final ScrolledComposite sc1 = new ScrolledComposite(shell, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
 *      final Composite c1 = new Composite(sc1, SWT.NONE);
 *      sc1.setContent(c1);
 *      c1.setBackground(red);
 *      GridLayout layout = new GridLayout();
 *      layout.numColumns = 4;
 *      c1.setLayout(layout);
 *      Button b1 = new Button (c1, SWT.PUSH);
 *      b1.setText("first button");
 *      c1.setSize(c1.computeSize(SWT.DEFAULT, SWT.DEFAULT));
 *      
 *      // set the minimum width and height of the scrolled content - method 2
 *      final ScrolledComposite sc2 = new ScrolledComposite(shell, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
 *      sc2.setExpandHorizontal(true);
 *      sc2.setExpandVertical(true);
 *      final Composite c2 = new Composite(sc2, SWT.NONE);
 *      sc2.setContent(c2);
 *      c2.setBackground(blue);
 *      layout = new GridLayout();
 *      layout.numColumns = 4;
 *      c2.setLayout(layout);
 *      Button b2 = new Button (c2, SWT.PUSH);
 *      b2.setText("first button");
 *      sc2.setMinSize(c2.computeSize(SWT.DEFAULT, SWT.DEFAULT));
 *      
 *      Button add = new Button (shell, SWT.PUSH);
 *      add.setText("add children");
 *      final int[] index = new int[]{0};
 *      add.addListener(SWT.Selection, new Listener() {
 *          public void handleEvent(Event e) {
 *              index[0]++;
 *              Button button = new Button(c1, SWT.PUSH);
 *              button.setText("button "+index[0]);
 *              // reset size of content so children can be seen - method 1
 *              c1.setSize(c1.computeSize(SWT.DEFAULT, SWT.DEFAULT));
 *              c1.layout();
 *              
 *              button = new Button(c2, SWT.PUSH);
 *              button.setText("button "+index[0]);
 *              // reset the minimum width and height so children can be seen - method 2
 *              sc2.setMinSize(c2.computeSize(SWT.DEFAULT, SWT.DEFAULT));
 *              c2.layout();
 *          }
 *      });
 * 
 *      shell.open ();
 *      while (!shell.isDisposed ()) {
 *          if (!display.readAndDispatch ()) display.sleep ();
 *      }
 *      display.dispose ();
 * }
 * </pre></code>
 *
 * <dl>
 * <dt><b>Styles:</b><dd>H_SCROLL, V_SCROLL
 * </dl>
 */
public class ScrolledComposite extends Composite {

	private Control content;
	private Listener contentListener;
	
	private int minHeight = 0;
	private int minWidth = 0;
	private boolean expandHorizontal = false;
	private boolean expandVertical = false;
	private boolean alwaysShowScroll = false;
	private boolean inResize = false;

/**
 * Constructs a new instance of this class given its parent
 * and a style value describing its behavior and appearance.
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
 * @param parent a widget which will be the parent of the new instance (cannot be null)
 * @param style the style of widget to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 * </ul>
 *
 * @see SWT#H_SCROLL
 * @see SWT#V_SCROLL
 * @see #getStyle
 */	
public ScrolledComposite(Composite parent, int style) {
	super(parent, checkStyle(style));
	
	ScrollBar hBar = getHorizontalBar ();
	if (hBar != null) {
		hBar.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				hScroll();
			}
		});
	}
	
	ScrollBar vBar = getVerticalBar ();
	if (vBar != null) {
		vBar.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				vScroll();
			}
		});
	}
	
	addListener (SWT.Resize,  new Listener () {
		public void handleEvent (Event e) {
			resize();
		}
	});
	
	contentListener = new Listener() {
		public void handleEvent(Event e) {
			if (e.type != SWT.Resize) return;
			resize();
		}
	};
}

private static int checkStyle (int style) {
	int mask = SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
	return style & mask;
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	/*
	* When a composite does layout without using a layout
	* manager, it must take into account the preferred size
	* of it's children when computing it's preferred size in
	* the same way that a layout manager would.  In particular,
	* when a scrolled composite hides the scroll bars and
	* places a child to fill the client area, then repeated
	* calls to compute the preferred size of the scrolled
	* composite should not keep adding in the space used by
	* the scroll bars.
	*/
	if (content == null) {
		return super.computeSize (wHint, hHint, changed);
	}
	Point size = content.computeSize (wHint, hHint, changed);
	Rectangle trim = computeTrim (0, 0, size.x, size.y);
	return new Point (trim.width, trim.height);
}

/**
 * Returns the Always Show Scrollbars flag.  True if the scrollbars are 
 * always shown even if they are not required.  False if the scrollbars are only 
 * visible when some part of the composite needs to be scrolled to be seen.
 * The H_SCROLL and V_SCROLL style bits are also required to enable scrollbars in the 
 * horizontal and vertical directions.
 * 
 * @return the Always Show Scrollbars flag value
 */
public boolean getAlwaysShowScrollBars() {
	//checkWidget();
	return alwaysShowScroll;
}

/**
 * Get the content that is being scrolled.
 * 
 * @return the control displayed in the content area
 */
public Control getContent() {
	//checkWidget();
	return content;
}

private void hScroll() {
	if (content == null) return;
	Point location = content.getLocation ();
	ScrollBar hBar = getHorizontalBar ();
	int hSelection = hBar.getSelection ();
	content.setLocation (-hSelection, location.y);
}

public void layout(boolean changed) {
	checkWidget();
	if (content == null) return;
	Rectangle contentRect = content.getBounds();
	ScrollBar hBar = getHorizontalBar ();
	ScrollBar vBar = getVerticalBar ();
	if (!alwaysShowScroll) {
		boolean hVisible = needHScroll(contentRect, false);
		boolean vVisible = needVScroll(contentRect, hVisible);
		if (!hVisible && vVisible) hVisible = needHScroll(contentRect, vVisible);
		if (hBar != null) hBar.setVisible(hVisible);
		if (vBar != null) vBar.setVisible(vVisible);
	}

	Rectangle hostRect = getClientArea();
	if (expandHorizontal) {
		contentRect.width = Math.max(minWidth, hostRect.width);	
	}
	if (expandVertical) {
		contentRect.height = Math.max(minHeight, hostRect.height);
	}

	if (hBar != null) {
		hBar.setMaximum (contentRect.width);
		hBar.setThumb (Math.min (contentRect.width, hostRect.width));
		int hPage = contentRect.width - hostRect.width;
		int hSelection = hBar.getSelection ();
		if (hSelection >= hPage) {
			if (hPage <= 0) {
				hSelection = 0;
				hBar.setSelection(0);
			}
			contentRect.x = -hSelection;
		}
	}

	if (vBar != null) {
		vBar.setMaximum (contentRect.height);
		vBar.setThumb (Math.min (contentRect.height, hostRect.height));
		int vPage = contentRect.height - hostRect.height;
		int vSelection = vBar.getSelection ();
		if (vSelection >= vPage) {
			if (vPage <= 0) {
				vSelection = 0;
				vBar.setSelection(0);
			}
			contentRect.y = -vSelection;
		}
	}
	
	content.setBounds (contentRect);
}

private boolean needHScroll(Rectangle contentRect, boolean vVisible) {
	ScrollBar hBar = getHorizontalBar();
	if (hBar == null) return false;
	
	Rectangle hostRect = getBounds();
	int border = getBorderWidth();
	hostRect.width -= 2*border;
	ScrollBar vBar = getVerticalBar();
	if (vVisible && vBar != null) hostRect.width -= vBar.getSize().x;
	
	if (!expandHorizontal && contentRect.width > hostRect.width) return true;
	if (expandHorizontal && minWidth > hostRect.width) return true;
	return false;
}

private boolean needVScroll(Rectangle contentRect, boolean hVisible) {
	ScrollBar vBar = getVerticalBar();
	if (vBar == null) return false;
	
	Rectangle hostRect = getBounds();
	int border = getBorderWidth();
	hostRect.height -= 2*border;
	ScrollBar hBar = getHorizontalBar();
	if (hVisible && hBar != null) hostRect.height -= hBar.getSize().y;
	
	if (!expandHorizontal && contentRect.height > hostRect.height) return true;
	if (expandHorizontal && minHeight > hostRect.height) return true;
	return false;
}

private void resize() {
	if (inResize) return;
	inResize = true;
	layout();
	inResize = false;
}
/**
 * Return the point in the content that currenly appears in the top left 
 * corner of the scrolled composite.
 * 
 * @return the point in the content that currenly appears in the top left 
 * corner of the scrolled composite.  If no content has been set, this returns
 * (0, 0).
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.0
 */
public Point getOrigin() {
	checkWidget();
	if (content == null) return new Point(0, 0);
	Point location = content.getLocation();
	return new Point(-location.x, -location.y);
}
/**
 * Scrolls the content so that the specified point in the content is in the top 
 * left corner.  If no content has been set, nothing will occur.  
 * 
 * Negative values will be ignored.  Values greater than the maximum scroll 
 * distance will result in scrolling to the end of the scrollbar.
 *
 * @param origin the point on the content to appear in the top left corner 
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_INVALID_ARGUMENT - value of origin is outside of content
 * </ul>
 * @since 2.0
 */
public void setOrigin(Point origin) {
	setOrigin(origin.x, origin.y);
}
/**
 * Scrolls the content so that the specified point in the content is in the top 
 * left corner.  If no content has been set, nothing will occur.  
 * 
 * Negative values will be ignored.  Values greater than the maximum scroll 
 * distance will result in scrolling to the end of the scrollbar.
 *
 * @param x the x coordinate of the content to appear in the top left corner 
 * 
 * @param y the y coordinate of the content to appear in the top left corner 
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.0
 */
public void setOrigin(int x, int y) {
	checkWidget();
	if (content == null) return;
	ScrollBar hBar = getHorizontalBar ();
	if (hBar != null) {
		hBar.setSelection(x);
		x = -hBar.getSelection ();
	} else {
		x = 0;
	}
	ScrollBar vBar = getVerticalBar ();
	if (vBar != null) {
		vBar.setSelection(y);
		y = -vBar.getSelection ();
	} else {
		y = 0;
	}
	content.setLocation(x, y);
}
/**
 * Set the Always Show Scrollbars flag.  True if the scrollbars are 
 * always shown even if they are not required.  False if the scrollbars are only 
 * visible when some part of the composite needs to be scrolled to be seen.
 * The H_SCROLL and V_SCROLL style bits are also required to enable scrollbars in the 
 * horizontal and vertical directions.
 * 
 * @param show true to show the scrollbars even when not required, false to show scrollbars only when required
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setAlwaysShowScrollBars(boolean show) {
	checkWidget();
	if (show == alwaysShowScroll) return;
	alwaysShowScroll = show;
	ScrollBar hBar = getHorizontalBar ();
	if (hBar != null && alwaysShowScroll) hBar.setVisible(true);
	ScrollBar vBar = getVerticalBar ();
	if (vBar != null && alwaysShowScroll) vBar.setVisible(true);
	layout();
}

/**
 * Set the content that will be scrolled.
 * 
 * @param content the control to be displayed in the content area
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setContent(Control content) {
	checkWidget();
	if (this.content != null && !this.content.isDisposed()) {
		this.content.removeListener(SWT.Resize, contentListener);
		this.content.setBounds(new Rectangle(-200, -200, 0, 0));	
	}
	
	this.content = content;
	ScrollBar vBar = getVerticalBar ();
	ScrollBar hBar = getHorizontalBar ();
	if (this.content != null) {
		if (vBar != null) {
			vBar.setMaximum (0);
			vBar.setThumb (0);
			vBar.setSelection(0);
		}
		if (hBar != null) {
			hBar.setMaximum (0);
			hBar.setThumb (0);
			hBar.setSelection(0);
		}
		content.setLocation(0, 0);
		layout();
		this.content.addListener(SWT.Resize, contentListener);
	} else {
		if (hBar != null) hBar.setVisible(alwaysShowScroll);
		if (vBar != null) vBar.setVisible(alwaysShowScroll);
	}
}
/**
 * Configure the ScrolledComposite to resize the content object to be as wide as the 
 * ScrolledComposite when the width of the ScrolledComposite is greater than the
 * minimum width specified in setMinWidth.  If the ScrolledComposite is less than the
 * minimum width, the content will not resized and instead the horizontal scroll bar will be
 * used to view the entire width.
 * If expand is false, this behaviour is turned off.  By default, this behaviour is turned off.
 * 
 * @param expand true to expand the content control to fill available horizontal space
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setExpandHorizontal(boolean expand) {
	checkWidget();
	if (expand == expandHorizontal) return;
	expandHorizontal = expand;
	layout();
}
/**
 * Configure the ScrolledComposite to resize the content object to be as tall as the 
 * ScrolledComposite when the height of the ScrolledComposite is greater than the
 * minimum height specified in setMinHeight.  If the ScrolledComposite is less than the
 * minimum height, the content will not resized and instead the vertical scroll bar will be
 * used to view the entire height.
 * If expand is false, this behaviour is turned off.  By default, this behaviour is turned off.
 * 
 * @param expand true to expand the content control to fill available vertical space
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setExpandVertical(boolean expand) {
	checkWidget();
	if (expand == expandVertical) return;
	expandVertical = expand;
	layout();
}
public void setLayout (Layout layout) {
	// do not allow a layout to be set on this class because layout is being handled by the resize listener
	checkWidget();
	return;
}
/**
 * Specify the minimum height at which the ScrolledComposite will begin scrolling the
 * content with the vertical scroll bar.  This value is only relevant if  
 * setExpandVertical(true) has been set.
 * 
 * @param height the minimum height or 0 for default height
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMinHeight(int height) {
	setMinSize(minWidth, height);
}
/**
 * Specify the minimum width and height at which the ScrolledComposite will begin scrolling the
 * content with the horizontal scroll bar.  This value is only relevant if  
 * setExpandHorizontal(true) and setExpandVertical(true) have been set.
 * 
 * @param size the minimum size or null for the default size
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMinSize(Point size) {
	if (size == null) {
		setMinSize(0, 0);
	} else {
		setMinSize(size.x, size.y);
	}
}
/**
 * Specify the minimum width and height at which the ScrolledComposite will begin scrolling the
 * content with the horizontal scroll bar.  This value is only relevant if  
 * setExpandHorizontal(true) and setExpandVertical(true) have been set.
 * 
 * @param width the minimum width or 0 for default width
 * @param height the minimum height or 0 for default height
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMinSize(int width, int height) {
	checkWidget();
	if (width == minWidth && height == minHeight) return;
	minWidth = Math.max(0, width);
	minHeight = Math.max(0, height);
	layout();
}
/**
 * Specify the minimum width at which the ScrolledComposite will begin scrolling the
 * content with the horizontal scroll bar.  This value is only relevant if  
 * setExpandHorizontal(true) has been set.
 * 
 * @param width the minimum width or 0 for default width
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMinWidth(int width) {
	setMinSize(width, minHeight);
}

private void vScroll() {
	if (content == null) return;
	Point location = content.getLocation ();
	ScrollBar vBar = getVerticalBar ();
	int vSelection = vBar.getSelection ();
	content.setLocation (location.x, -vSelection);
}
}
