/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.dnd;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;


/**
 * This class provides a default drag under effect during a drag and drop. 
 * The current implementation does not provide any visual feedback.
 * 
 * <p>The drop target effect has the same API as the 
 * <code>DropTargetAdapter</code> so that it can provide custom visual 
 * feedback when a <code>DropTargetEvent</code> occurs. 
 * </p>
 * 
 * <p>Classes that wish to provide their own drag under effect
 * can extend the <code>DropTargetEffect</code> and override any applicable methods 
 * in <code>DropTargetAdapter</code> to display their own drag under effect.</p>
 *
 * <p>The feedback value is either one of the FEEDBACK constants defined in 
 * class <code>DND</code> which is applicable to instances of this class, 
 * or it must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>DND</code> effect constants. 
 * </p>
 * <p>
 * <dl>
 * <dt><b>Feedback:</b></dt>
 * <dd>FEEDBACK_EXPAND, FEEDBACK_INSERT_AFTER, FEEDBACK_INSERT_BEFORE, 
 * FEEDBACK_NONE, FEEDBACK_SELECT, FEEDBACK_SCROLL</dd>
 * </dl>
 * </p>
 * 
 * @see DropTargetAdapter
 * @see DropTargetEvent
 * 
 * @since 3.3
 */
public class DropTargetEffect extends DropTargetAdapter {
	Control control;

	/**
	 * Creates a new <code>DropTargetEffect</code> to handle the drag under effect on the specified 
	 * <code>Control</code>.
	 * 
	 * @param control the <code>Control</code> over which the user positions the cursor to drop the data
	 * 
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the control is null</li>
	 * </ul>
	 */
	public DropTargetEffect(Control control) {
		if (control == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		this.control = control;
	}

	/**
	 * Returns the Control which is registered for this DropTargetEffect.  This is the control over which the 
	 * user positions the cursor to drop the data.
	 *
	 * @return the Control which is registered for this DropTargetEffect
	 */
	public Control getControl() {
		return control;
	}
	
	/**
	 * Returns the item at the given x-y coordinate in the receiver
	 * or null if no such item exists. The x-y coordinate is in the
	 * display relative coordinates.
	 *
	 * @param x the x coordinate used to locate the item
	 * @param y the y coordinate used to locate the item
	 * @return the item at the given x-y coordinate, or null if the coordinate is not in a selectable item
	 */
	public Widget getItem(int x, int y) {
		if (control instanceof Table) {
			return getItem((Table) control, x, y);
		}
		if (control instanceof Tree) {
			return getItem((Tree) control, x, y);
		}			
		return null;
	}
	
	Widget getItem(Table table, int x, int y) {
		Point coordinates = new Point(x, y);
		coordinates = table.toControl(coordinates);
		Item item = table.getItem(coordinates);
		if (item == null) {
			Rectangle area = table.getClientArea();
			if (area.contains(coordinates)) {
				// Scan across the width of the table.
				for (int x1 = area.x; x1 < area.x + area.width; x1++) {
					Point pt = new Point(x1, coordinates.y);
					item = table.getItem(pt);
					if (item != null) {
						break;
					}
				}
			}
		}
		return item;
	}
	
	Widget getItem(Tree tree, int x, int y) {
		Point coordinates = new Point(x, y);
		coordinates = tree.toControl(coordinates);
		Item item = tree.getItem(coordinates);
		if (item == null) {
			Rectangle area = tree.getClientArea();
			if (area.contains(coordinates)) {
				// Scan across the width of the tree.
				for (int x1 = area.x; x1 < area.x + area.width; x1++) {
					Point pt = new Point(x1, coordinates.y);
					item = tree.getItem(pt);
					if (item != null) {
						break;
					}
				}
			}
		}
		return item;
	}
	
	TreeItem nextItem(Tree tree, TreeItem item) {
		if (item == null) return null;
		if (item.getExpanded()) return item.getItem(0);
		TreeItem childItem = item;
		TreeItem parentItem = childItem.getParentItem();
		int index = parentItem == null ? tree.indexOf(childItem) : parentItem.indexOf(childItem);
		int count = parentItem == null ? tree.getItemCount() : parentItem.getItemCount();
		while (true) {
			if (index + 1 < count) return parentItem == null ? tree.getItem(index + 1) : parentItem.getItem(index + 1);
			if (parentItem == null) return null;
			childItem = parentItem;
			parentItem = childItem.getParentItem();
			index = parentItem == null ? tree.indexOf(childItem) : parentItem.indexOf(childItem);
			count = parentItem == null ? tree.getItemCount() : parentItem.getItemCount();
		}
	}
	
	TreeItem previousItem(Tree tree, TreeItem item) {
		if (item == null) return null;
		TreeItem childItem = item;
		TreeItem parentItem = childItem.getParentItem();
		int index = parentItem == null ? tree.indexOf(childItem) : parentItem.indexOf(childItem);
		if (index == 0) return parentItem;
		TreeItem nextItem = parentItem == null ? tree.getItem(index-1) : parentItem.getItem(index-1);
		int count = nextItem.getItemCount();
		while (count > 0 && nextItem.getExpanded()) {
			nextItem = nextItem.getItem(count - 1);
			count = nextItem.getItemCount();
		}
		return nextItem;
	}
}
