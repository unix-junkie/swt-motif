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

import org.eclipse.swt.events.*;

/**
 * This event is sent after a text change occurs.
 */
public final class ExtendedModifyEvent extends TypedEvent {
	public int start;			// start offset of the new text
	public int length;			// length of the new text
	public String replacedText;	// replaced text or empty string if no text was replaced
	
public ExtendedModifyEvent(StyledTextEvent e) {
	super(e);
	start = e.start;
	length = e.end - e.start;
	replacedText = e.text;
}
}
