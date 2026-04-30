/*
 * @(#)TransformableFigure.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.draw.figure;

import java.awt.geom.AffineTransform;

/**
 * A <em>role interface</em> for {@link Figure}s that can be transformed.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public interface TransformableFigure {

    /**
     * Gets data which can be used to restore the transformation of the figure
     * without loss of precision, after a transform has been applied to it.
     *
     * @see #transform(AffineTransform)
     */
    public Object getTransformRestoreData();

    /**
     * Restores the transform of the figure to a previously stored state.
     */
    public void restoreTransformTo(Object restoreData);

    /**
     * Transforms the shape of the Figure. Transformations using double
     * precision arithmethics are inherently lossy operations. Therefore it is
     * recommended to use getTransformRestoreData() restoreTransformTo() to
     * provide lossless undo/redo functionality.
     * <p>
     * This is a basic operation which does not fire events. Use the following
     * code sequence, if you need event firing:
     * <pre>
     * figure.willChange();
     * figure.transform(...);
     * figure.changed();
     * </pre>
     *
     *
     * @param tx The transformation.
     * @see #getTransformRestoreData
     * @see #restoreTransformTo
     */
    public void transform(AffineTransform tx);

    /**
     * Returns true, if the user may transform this figure.
     * If this operation returns false, Tool's should not transform this
     * figure on behalf of the user.
     * <p>
     * Please note, that even if this method returns false, the Figure
     * may be transformed for other reasons. For example, if the Figure takes
     * part in an animation.
     *
     * @see #transform
     */
    public boolean isTransformable();
}
