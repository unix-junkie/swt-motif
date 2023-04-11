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
package org.eclipse.swt.events;


import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.graphics.GC;

/**
 * Instances of this class are sent as a result of
 * visible areas of controls requiring re-painting.
 *
 * @see PaintListener
 */

public final class PaintEvent extends TypedEvent {
	
	/**
	 * the graphics context to use when painting
	 * that is configured to use the colors, font and
	 * damaged region of the control.  It is valid
	 * only during the paint and must not be disposed
	 */
	public GC gc;
	
	/**
	 * the x offset of the bounding rectangle of the 
	 * region that requires painting
	 */
	public int x;
	
	/**
	 * the y offset of the bounding rectangle of the 
	 * region that requires painting
	 */
	public int y;
	
	/**
	 * the width of the bounding rectangle of the 
	 * region that requires painting
	 */
	public int width;
	
	/**
	 * the height of the bounding rectangle of the 
	 * region that requires painting
	 */
	public int height;

	/**
	 * the number of following paint events which
     * are pending which may always be zero on
	 * some platforms
	 */
	public int count;

/**
 * Constructs a new instance of this class based on the
 * information in the given untyped event.
 *
 * @param e the untyped event containing the information
 */
public PaintEvent(Event e) {
	super(e);
	this.gc = e.gc;
	this.x = e.x;
	this.y = e.y;
	this.width = e.width;
	this.height = e.height;
	this.count = e.count;
}

}

