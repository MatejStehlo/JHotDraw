package org.jhotdraw.draw;

import org.testng.annotations.Test;
import static org.testng.Assert.*;
import java.awt.Rectangle;
import java.awt.Point;

public class DefaultDrawingViewTest {
    @Test
    public void testSetDrawing() {
        DefaultDrawingView view = new DefaultDrawingView();
        Drawing drawing = new DefaultDrawing();
        view.setDrawing(drawing);
        assertEquals(view.getDrawing(), drawing);
    }

    @Test
    public void testScaleFactor() {
        DefaultDrawingView view = new DefaultDrawingView();
        view.setScaleFactor(2.0);
        assertEquals(view.getScaleFactor(), 2.0);
    }

    @Test
    public void testUpdateBufferedArea() {
        DefaultDrawingView view = new DefaultDrawingView();
        Rectangle vr = new Rectangle(10, 10, 100, 100);
        Point shift = new Point(0, 0);
        
        // This should set bufferedArea and dirtyArea to vr because they were empty
        view.updateBufferedArea(vr, shift);
        
        assertEquals(view.bufferedArea, vr);
        assertEquals(view.dirtyArea, vr);
        assertEquals(shift, new Point(0, 0));
    }

    @Test
    public void testUpdateBufferedAreaShift() {
        DefaultDrawingView view = new DefaultDrawingView();
        Rectangle vr1 = new Rectangle(0, 0, 100, 100);
        Point shift = new Point(0, 0);
        view.updateBufferedArea(vr1, shift);
        
        // Shift by 10 pixels to the right
        Rectangle vr2 = new Rectangle(10, 0, 100, 100);
        view.updateBufferedArea(vr2, shift);
        
        assertEquals(shift, new Point(-10, 0));
        assertEquals(view.bufferedArea, vr2);
        // dirtyArea should now contain the new area [100, 0, 10, 100]
        assertTrue(view.dirtyArea.contains(100, 0, 10, 100));
    }
}
