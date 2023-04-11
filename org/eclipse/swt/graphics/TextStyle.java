/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;

import org.eclipse.swt.*;

/**
 * <code>TextStyle</code> defines a set of styles that can be applied
 * to a range of text.
 * <p>
 * The hashCode() method in this class uses the values of the public
 * fields to compute the hash value. When storing instances of the
 * class in hashed collections, do not modify these fields after the
 * object has been inserted.  
 * </p>
 * <p>
 * Application code does <em>not</em> need to explicitly release the
 * resources managed by each instance when those instances are no longer
 * required, and thus no <code>dispose()</code> method is provided.
 * </p>
 * 
 * @see TextLayout
 * @see Font
 * @see Color
 *  
 * @since 3.0
 */
public class TextStyle {

	/**
	 * the font of the style
	 */
	public Font font;

	/**
	 * the foreground of the style
	 */
	public Color foreground;

	/**
	 * the background of the style
	 */
	public Color background;

/** 
 * Create a new text style with the specified font, foreground
 * and background.
 *
 * @param font the font of the style, <code>null</code> if none 
 * @param foreground the foreground color of the style, <code>null</code> if none 
 * @param background the background color of the style, <code>null</code> if none
 */
public TextStyle (Font font, Color foreground, Color background) {
	if (font != null && font.isDisposed()) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	if (foreground != null && foreground.isDisposed()) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	if (background != null && background.isDisposed()) SWT.error (SWT.ERROR_INVALID_ARGUMENT);	
	this.font = font;
	this.foreground = foreground;
	this.background = background;
}

public boolean equals(Object object) {
	if (object == this) return true;
	if (object == null) return false;
	if (!(object instanceof TextStyle)) return false;
	TextStyle style = (TextStyle)object;	
	if (this.foreground != null) {
		if (!this.foreground.equals(style.foreground)) return false;
	} else if (style.foreground != null) return false;
	if (this.background != null) {
		if (!this.background.equals(style.background)) return false;
	} else if (style.background != null) return false;
	if (this.font != null) {
		if (!this.font.equals(style.font)) return false;
	} else if (style.font != null) return false;	
	return true;
}

public int hashCode() {
	int hash = super.hashCode();
	if (font != null) hash ^= font.hashCode();
	if (foreground != null) hash ^= foreground.hashCode();
	if (background != null) hash ^= background.hashCode();
	return hash;
}
}
