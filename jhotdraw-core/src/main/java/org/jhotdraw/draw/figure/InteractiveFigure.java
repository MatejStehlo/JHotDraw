/*
 * @(#)InteractiveFigure.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.draw.figure;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Collection;
import javax.swing.Action;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.handle.Handle;
import org.jhotdraw.draw.tool.Tool;

/**
 * A <em>role interface</em> for {@link Figure}s that can be interacted with by the user.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public interface InteractiveFigure {

    /**
     * Returns true, if the user may select this figure.
     * If this operation returns false, Tool's should not select this
     * figure on behalf of the user.
     * <p>
     * Please note, that even if this method returns false, the Figure
     * may become part of a selection for other reasons. For example,
     * if the Figure is part of a GroupFigure, then the Figure is
     * indirectly part of the selection, when the user selects the
     * GroupFigure.
     */
    public boolean isSelectable();

    /**
     * Returns true, if the user may remove this figure.
     * If this operation returns false, Tool's should not remove this
     * figure on behalf of the user.
     * <p>
     * Please note, that even if this method returns false, the Figure
     * may be removed from the Drawing for other reasons. For example,
     * if the Figure is used to display a warning message, the Figure
     * can be removed from the Drawing, when the warning message is
     * no longer relevant.
     */
    public boolean isRemovable();

    /**
     * Creates handles used to manipulate the figure.
     *
     * @param detailLevel The detail level of the handles. Usually this is 0 for
     * bounding box handles and 1 for point handles. The value -1 is used
     * by the SelectAreaTracker and the HandleTracker to highlight figures, over which the mouse
     * pointer is hovering.
     * @return a Collection of handles
     * @see Handle
     */
    public Collection<Handle> createHandles(int detailLevel);

    /**
     * Returns a cursor for the specified location.
     */
    public Cursor getCursor(Point2D.Double p);

    /**
     * Returns a collection of Action's for the specified location on the figure.
     *
     * <p>
     * The collection may contain null entries. These entries are used
     * interpreted as separators in the popup menu.
     * <p>
     * Actions can use the property Figure.ACTION_SUBMENU to specify a
     * submenu.
     */
    public Collection<Action> getActions(Point2D.Double p);

    /**
     * Returns a specialized tool for the specified location.
     * <p>
     * Returns null, if no specialized tool is available.
     */
    public Tool getTool(Point2D.Double p);

    /**
     * Returns a tooltip for the specified location on the figure.
     */
    public String getToolTipText(Point2D.Double p);

    /**
     * Handles a drop.
     *
     * @param p The location of the mouse event.
     * @param droppedFigures The dropped figures.
     * @param view The drawing view which is the source of the mouse event.
     * @return Returns true, if the figures should snap back to the location
     * they were dragged from.
     */
    public boolean handleDrop(Point2D.Double p, Collection<Figure> droppedFigures, DrawingView view);

    /**
     * Handles a mouse click.
     *
     * @param p The location of the mouse event.
     * @param evt The mouse event.
     * @param view The drawing view which is the source of the mouse event.
     *
     * @return Returns true, if the event was consumed.
     */
    public boolean handleMouseClick(Point2D.Double p, MouseEvent evt, DrawingView view);
}
