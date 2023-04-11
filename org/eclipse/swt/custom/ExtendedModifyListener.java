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

import org.eclipse.swt.internal.SWTEventListener;

public interface ExtendedModifyListener extends SWTEventListener {
/**
 * This method is called after a text change occurs.
 * <p>
 *
 * @param event.start the start offset of the new text (input)
 * @param event.length the length of the new text (input)
 * @param event.replacedText the replaced text (input)
 */
public void modifyText(ExtendedModifyEvent event);
}


