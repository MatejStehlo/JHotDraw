/*
 * @(#)ConnectableFigure.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.draw.figure;

import java.awt.geom.Point2D;
import java.util.Collection;
import org.jhotdraw.draw.connector.Connector;

/**
 * A <em>role interface</em> for {@link Figure}s that can be connected.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public interface ConnectableFigure {

    /**
     * Returns true if this Figure can be connected to a {@link ConnectionFigure}.
     */
    public boolean isConnectable();

    /**
     * Gets a connector for this figure at the given location.
     * A figure can have different connectors at different locations.
     *
     * @param p the location of the connector.
     * @param prototype The prototype used to create a connection or null if
     * unknown. This allows for specific connectors for different
     * connection figures.
     */
    public Connector findConnector(Point2D.Double p, ConnectionFigure prototype);

    /**
     * Gets a compatible connector.
     * If the provided connector is part of this figure, return the connector.
     * If the provided connector is part of another figure, return a connector
     * with the same semantics for this figure.
     * Returns null, if no compatible connector is available.
     */
    public Connector findCompatibleConnector(Connector c, boolean isStartConnector);

    /**
     * Returns all connectors of this Figure for the specified prototype of
     * a ConnectionFigure.
     * <p>
     * This is used by connection tools and connection handles
     * to visualize the connectors when the user is about to
     * create a ConnectionFigure to this Figure.
     *
     * @param prototype The prototype used to create a connection or null if
     * unknown. This allows for specific connectors for different
     * connection figures.
     */
    public Collection<Connector> getConnectors(ConnectionFigure prototype);
}
